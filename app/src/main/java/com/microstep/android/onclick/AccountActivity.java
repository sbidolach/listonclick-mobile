package com.microstep.android.onclick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.rest.model.IdentityIdentifierXml;
import com.microstep.android.onclick.rest.model.IdentityXml;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.rest.model.OnClickXml;
import com.microstep.android.onclick.rest.model.StatusXml;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.PasswordUtil;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountActivity extends Activity implements OnClickListener {
	
	public final static int REQUEST_ACCOUNT_ASSIGN = 1;
	public final static int REQUEST_ACCOUNT_REGISTER = 2;
	
    private EditText textEmail;
    private EditText textFirstName;
    private EditText textLastName;
    private TextView textPassword;
    private TextView textPassword1;
    
    private LinearLayout lProgess = null;
            
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		createAccountView();
	}
	
	@Override
	public void onClick(View v) {
				
		switch(v.getId()){
			case R.id.button_account_assign_email:
				
				if(Utils.isOnline(this)){
    	        	NoteService noteService = WebServiceFactory.getNoteService();
    	        	String email = textEmail.getText().toString();    	        	
    	        	if("".equals(email)){
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(R.string.dialog_email_empty)
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();
    	        		return;
    	        	}
    	        	
    	        	Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    	        	Matcher m = p.matcher(email);
    	        	if(m.matches()){
    	        		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    	        		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    	        		StatusXml status = noteService.addDeviceToAccount(
    	        				dc.getUniqueID(), email, dc.getLanguage(), login.getAuthToken());
    	        		switch(status.getCode()){
    	        			case 0:
    	        			case 400:    	        				
    	        				login = noteService.loginDevice(dc.getUniqueID());
    	        				SecurityContextHolder.getSecurityContext().setLogin(login);
    	        				showEditView();
    	        				break;
    	        			case 200:
    	        				showRegisterView(email);
    	        				break;
    	        		}        	        		
    	        	}
    	        	
        		}else{
        			AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(R.string.dialog_no_network)
				       .setCancelable(false)
				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				    }).show();
        		}
        		
				break;
			case R.id.button_account_register:
				
				if(Utils.isOnline(this)){
    	        	    	        	
    	        	String email = textEmail.getText().toString();
    	        	String p1 = textPassword.getText().toString();
    	        	String p2 = textPassword1.getText().toString();
    	        	
    	        	if(email.length() == 0 || p1.length() == 0 || p2.length() == 0){    	        		
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(R.string.dialog_bad_form)
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();    					
    	        		return;
    	        	}
    	        	
    	        	Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    	        	Matcher m = p.matcher(email);
    	        	if(!m.matches()){
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(R.string.dialog_email_wrong)
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();
    	        		return;
    	        	}
    	        	
    	        	if(!p1.equals(p2)){
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(R.string.dialog_password_different)
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();
    	        		return;
    	        	}
    	        	
    	        	if(p1.length() < 5){
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(R.string.dialog_password_short)
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();
    	        		return;
    	        	}

    	        	NoteService noteService = WebServiceFactory.getNoteService();
    	        	String password = PasswordUtil.encrypt(p1);
    	        	OnClickXml onClickXml = noteService.registerAccount(email, password, getCurrency(this));    	        	
    	        	StatusXml statusXml = onClickXml.getStatusXml();

    	        	if(statusXml.getCode() == 0){

    	        		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    	        		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    	        		StatusXml status = noteService.addDeviceToAccount(
    	        				dc.getUniqueID(), email, dc.getLanguage(), login.getAuthToken());

    	        		if(status.getCode() == 0 || status.getCode() == 400){
	        				login = noteService.loginDevice(dc.getUniqueID());
	        				SecurityContextHolder.getSecurityContext().setLogin(login);
	        				showEditView();
	        			}
    	        		
//    	        		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
//    	        		Login login = noteService.loginDevice(dc.getUniqueID());    	        		
//        	        	SecurityContextHolder.getSecurityContext().setLogin(login);        	        
//    	        		showEditView();
    	        	}else{
    	        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    					builder.setMessage(statusXml.getMessage())
    				       .setCancelable(false)
    				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				        	   dialog.cancel();
    				           }
    				    }).show();
    	        	}
        	        
        		}else{
        			AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(R.string.dialog_no_network)
				       .setCancelable(false)
				       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				    }).show();
        		}
				
				break;
			case R.id.bn_back:
				finish();
				break;
			case R.id.button_account_edit:
				// TODO in future
				break;
			case R.id.button_account_remove:
				
				new AlertDialog.Builder(this)
				.setMessage(String.format(getResources().getString(R.string.question_account_remove),  textEmail.getText()))
				.setCancelable(false)
				.setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Login login = SecurityContextHolder.getSecurityContext().getLogin();
						NoteService noteService = WebServiceFactory.getNoteService();				
						StatusXml statusXml = noteService.removeDeviceFromAccount(
								login.getUserId(), login.getAccountId(), login.getAuthToken());
						
						if(statusXml.getCode() == 0){
							DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
							login = noteService.loginDevice(dc.getUniqueID());
							SecurityContextHolder.getSecurityContext().setLogin(login);
							finish();					
						}else{
							AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
							builder.setMessage(statusXml.getMessage())
						       .setCancelable(false)
						       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   dialog.cancel();
						           }
						    }).show();
						}			
					}
				})
				.setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();							
				              
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void createAccountView(){		
		
		if(Utils.isOnline(this)){		
			Login login = SecurityContextHolder.getSecurityContext().getLogin();
			if(login != null && !"-1".equals(login.getUserId())){						
				try{
					if(login.getEmail() != null){
						showEditView();
					}else{
						showAssignView();
					}
				}catch(Exception e){
					finish();
				}			
			}else{			
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.dialog_error_network)
			       .setCancelable(false)
			       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finish();
			        	   dialog.cancel();
			           }
			    }).show();					
			}
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.dialog_no_network)
		       .setCancelable(false)
		       .setPositiveButton(R.string.button_shop_ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		        	   dialog.cancel();
		           }
		    }).show();
		}
	}
	
	private void showEditView(){
		
		String accountId = SecurityContextHolder.getSecurityContext().getLogin().getAccountId(); 
		String authToken = SecurityContextHolder.getSecurityContext().getLogin().getAuthToken();
		
		setContentView(R.layout.account_edit);
		this.lProgess = (LinearLayout) this.findViewById(R.id.layout_progress);
		Button remove = (Button) this.findViewById(R.id.button_account_remove);
		remove.setOnClickListener(this);		
				
		this.lProgess.setVisibility(View.VISIBLE);	
		NoteService noteService = WebServiceFactory.getNoteService();
		IdentityXml identityXml = noteService.getAccount(accountId, authToken);
		if(identityXml != null){
			for(IdentityIdentifierXml identityIdentifierXml : identityXml.getIdentityIdentifierXml()){
				if(identityIdentifierXml.getIdentityIdentifierType().equals("ONCLICK_LOGIN")){
					textEmail = (EditText) this.findViewById(R.id.account_email);
					textEmail.setText(identityIdentifierXml.getIdentifier());	
					textEmail.setEnabled(false);
					textEmail.setKeyListener(null);
					textFirstName = (EditText) this.findViewById(R.id.account_firstname);
					textFirstName.setText(identityXml.getFirstName());
					textFirstName.setEnabled(false);
					textFirstName.setKeyListener(null);
					textLastName = (EditText) this.findViewById(R.id.account_lastname);
					textLastName.setText(identityXml.getLastName());
					textLastName.setEnabled(false);
					textLastName.setKeyListener(null);
				}
			}
		}
		
		Button back = (Button) this.findViewById(R.id.bn_back);
		back.setOnClickListener(this);		
		this.lProgess.setVisibility(View.GONE);		
	}
	
	private void showAssignView(){
		setContentView(R.layout.account_assign);
		Button form = (Button) this.findViewById(R.id.button_account_assign_email);
		form.setOnClickListener(this);
		textEmail = (EditText) this.findViewById(R.id.account_email);
		Button back = (Button) this.findViewById(R.id.bn_back);
		back.setOnClickListener(this);
	}
	
	private void showRegisterView(String email){
		setContentView(R.layout.account_register);		
		Button form = (Button) this.findViewById(R.id.button_account_register);
		form.setOnClickListener(this);
		textEmail = (EditText) this.findViewById(R.id.account_email);
		textEmail.setText(email);
    	textPassword = (TextView) this.findViewById(R.id.account_password);
    	textPassword1 = (TextView) this.findViewById(R.id.account_password1);
    	Button back = (Button) this.findViewById(R.id.bn_back);
		back.setOnClickListener(this);
	}
	
	private String getCurrency(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);        
        String currency = prefs.getString("currencyPref", Utils.getDefaulCurrency(this));      	        	
        String[] values = getResources().getStringArray(R.array.currencyValues);
        String[] array = getResources().getStringArray(R.array.currencyArray);
        int i = 0;
        for(String s : values){
        	if(s.equals(currency)){
        		return array[i];
        	}
        	i++;
        }
        return "USD";
	}
	
}
