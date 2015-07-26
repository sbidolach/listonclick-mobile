package com.microstep.android.onclick;

import com.microstep.onclick.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InfoActivity extends Activity implements OnClickListener {
	
	public final static int REQUEST_SEND_EMAIL = 1;
	public final static int REQUEST_SEND_SMS = 2;
	
	public final static String CONTACT_EMAIL = "contact@listonclick.com";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		createInfoView();
	}
	
	@Override
	public void onClick(View v) {
		
		EditText text = null;
		Button back = null;
		
		switch(v.getId()){
			case R.id.button_contact_form:
				setContentView(R.layout.info_contact_form);				
				Button button = (Button) this.findViewById(R.id.button_send_email);
				button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						EditText email = (EditText) InfoActivity.this.findViewById(R.id.edittext_email);
						EditText text = (EditText) InfoActivity.this.findViewById(R.id.edittext_text);
						
						StringBuffer buffer = new StringBuffer();
						buffer.append(getResources().getString(R.string.share_email_contact) + ":\n");						
						buffer.append(email.getText().toString());
						buffer.append("\n\n");
						buffer.append(getResources().getString(R.string.share_email_content) + ":\n");
						buffer.append(text.getText().toString());
						
						Intent emailIntent = new Intent(Intent.ACTION_SEND);                   
                        emailIntent.setType("plain/text");                   
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{CONTACT_EMAIL});                 
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_form));    
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, buffer.toString());
                        InfoActivity.this.startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), REQUEST_SEND_EMAIL);						
					}
				});
				back = (Button) this.findViewById(R.id.bn_back);
				back.setOnClickListener(this);
				break;
			case R.id.button_invite_friend:
				setContentView(R.layout.info_share);
				Button sms = (Button) this.findViewById(R.id.button_share_sms);
				sms.setOnClickListener(this);
				Button email = (Button) this.findViewById(R.id.button_share_email);
				email.setOnClickListener(this);
				Button facebook = (Button) this.findViewById(R.id.button_share_facebook);
				facebook.setOnClickListener(this);
				back = (Button) this.findViewById(R.id.bn_back);
				back.setOnClickListener(this);
				break;
			case R.id.button_share_sms:
				text = (EditText) InfoActivity.this.findViewById(R.id.editext_share);
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);         
				sendIntent.setData(Uri.parse("sms:"));
				sendIntent.putExtra("sms_body", text.getText().toString());
				InfoActivity.this.startActivityForResult(sendIntent, REQUEST_SEND_SMS);
				break;
			case R.id.button_share_email:
				text = (EditText) InfoActivity.this.findViewById(R.id.editext_share);
				Intent emailIntent = new Intent(Intent.ACTION_SEND);                   
                emailIntent.setType("plain/text");                             
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_email_title));    
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text.getText().toString());
                try {
                	InfoActivity.this.startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), REQUEST_SEND_EMAIL);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(InfoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }                
				break;
			case R.id.button_share_facebook:
				text = (EditText) InfoActivity.this.findViewById(R.id.editext_share);				
				Intent postOnFacebookWallIntent = new Intent(this, ShareOnFacebook.class);
				postOnFacebookWallIntent.putExtra("facebookMessage", text.getText().toString());
				startActivity(postOnFacebookWallIntent);				
				break;
			case R.id.bn_back:
				finish();
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_SEND_EMAIL){
			createInfoView();
		}else if(requestCode == REQUEST_SEND_SMS){
			createInfoView();
		}	
	}
	
	private void createInfoView(){
		setContentView(R.layout.info);
		Button form = (Button) this.findViewById(R.id.button_contact_form);
		form.setOnClickListener(this);
		Button friend = (Button) this.findViewById(R.id.button_invite_friend);
		friend.setOnClickListener(this);
		Button back = (Button) this.findViewById(R.id.bn_back);
		back.setOnClickListener(this);
	}
	
}
