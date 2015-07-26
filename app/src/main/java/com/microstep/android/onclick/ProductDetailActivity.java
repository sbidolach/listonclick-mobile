package com.microstep.android.onclick;

import java.util.ArrayList;
import java.util.List;

import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.image.ImageManager;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.NoteService;
import com.microstep.android.onclick.rest.WebServiceFactory;
import com.microstep.android.onclick.rest.model.ProductXml;
import com.microstep.android.onclick.security.SecurityContextHolder;
import com.microstep.android.onclick.util.Language;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ProductDetailActivity extends Activity implements OnClickListener {
	
	private Product product = null;
	private Shop shop = null;
	private Organization organization = null;
    private Category category = null;
	private Boolean isEditable = true;
	
	private ArrayAdapter<String> adapterProductName;
	
	private AutoCompleteTextView eName;
	private EditText eCount;
    private EditText ePrice;
    private Spinner sUnit;
    private Spinner sCategory;
    private Button bBack;
    private TextView tDescription;
    
    private final int MIN_SEARCH = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scandetail);		
        
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			this.product = (Product) bundle.getSerializable("product");
			this.shop = (Shop) bundle.getSerializable("shop");
			this.organization = (Organization) bundle.getSerializable("organization");
			this.category = (Category) bundle.getSerializable("category");
			if(this.organization != null && this.category != null){
				this.isEditable = true;
			}else{
				this.isEditable = (Boolean) bundle.getSerializable("isEditable");
			}
		}
        						
        //set up button
        Button bAdd = (Button) findViewById(R.id.button_productdetail_add);
        bAdd.setOnClickListener(this);
        Button bSel = (Button) findViewById(R.id.button_productdetail_sel);
        bSel.setOnClickListener(this);       
        Button bCancel = (Button) findViewById(R.id.button_productdetail_cancel);
        bCancel.setOnClickListener(this);
        
        if(product == null){ 
        	bSel.setVisibility(View.GONE); 
        }else if(organization != null){
        	bSel.setVisibility(View.GONE);
        }else if(isEditable != null && !isEditable){        
        	bAdd.setVisibility(View.GONE);
        	bSel.setVisibility(View.GONE);
        }
        
        eName = (AutoCompleteTextView) findViewById(R.id.productdetail_name_auto);        
        adapterProductName = new ArrayAdapter<String>(this, R.layout.productname_item);
        adapterProductName.setNotifyOnChange(true);
        eName.setAdapter(adapterProductName);         
        
        eCount = (EditText) findViewById(R.id.productdetail_count);
        ePrice = (EditText) findViewById(R.id.productdetail_value);
        sUnit = (Spinner) findViewById(R.id.productdetail_unit);
        sCategory = (Spinner) findViewById(R.id.productdetail_category);
        tDescription = (TextView) findViewById(R.id.scan_bottom_text);
        tDescription.setText("");
        
        DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();        
        List<Category> categories = FactoryDAO.getCategoryDAO(this)
        		.getCategories(Language.value(dc.getLanguage()));
        String[] items = new String[categories.size()+1];
        int i = 0;
        items[i++] = getResources().getString(R.string.main_no_category);
        for(Category c : categories){
        	items[i++] = c.getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);

        if(product != null){        	
        	eName.setText(product.getName());
        	if(product.getCount() != null){
        		eCount.setText(Utils.getUnitValue(product));        		
        	}
        	if(product.getPrice() != null && !product.getPrice().equals(Double.valueOf(0))){
        		ePrice.setText(String.valueOf(product.getPrice()));
        	}    
        	sUnit.setSelection(product.getUnit());
        	if(product.getDescription() != null){
        		tDescription.setText(product.getDescription());
        	}
        	        	
        	Category category = product.getCategory();
        	if(category != null){
        		category = FactoryDAO.getCategoryDAO(this).getById(category.getId());
        		if(category != null){
        			int position = 0;
		            for(Category c : categories){
		            	position++;
		            	String categoryName = c.getName();
		            	if(categoryName.equals(category.getName())){
		            		sCategory.setSelection(position);
		            		break;
		            	}
		            }
        		}
        	}
        	
        	if(product.getImageNormalPath() != null){
        		ImageView imageView = (ImageView) findViewById(R.id.productimage);
        		imageView.setTag(product.getImageNormalPath());
        		ImageManager imageManager = new ImageManager(this, 300000);
        		imageManager.displayImage(product.getImageNormalPath(), imageView, R.drawable.img_no_photo);
        	}
        	
        	if(product.getDescription() != null){
        		TextView bottomText = (TextView) findViewById(R.id.scan_bottom_text);
        		bottomText.setText(product.getDescription());
        	}
        }
        
        // Set title of header
        if(shop != null){
    		TextView topTitle = (TextView) this.findViewById(R.id.text_title_top);
    		topTitle.setText(shop.getTitle());
    	}else if(organization != null){
    		TextView topTitle = (TextView) this.findViewById(R.id.text_title_top);
    		topTitle.setText(organization.getName());
    	}
        
        this.bBack = (Button) this.findViewById(R.id.bn_back);
		this.bBack.setOnClickListener(this);		
		this.eName.setThreshold(MIN_SEARCH);                  	
    	this.eName.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if(Utils.isOnline(ProductDetailActivity.this) && (isEditable == null || isEditable != false)){
					GetProductNameTask task = new GetProductNameTask();
					String name = (String) ProductDetailActivity.this.sCategory.getSelectedItem();				
					if(!name.equals(getResources().getString(R.string.main_no_category))){
			    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
						Category category = FactoryDAO.getCategoryDAO(ProductDetailActivity.this)
								.getByName(name, Language.value(dc.getLanguage()));
			    		task.execute(eName.getText().toString(), String.valueOf(category.getId()));
			    	}else{
			    		task.execute(eName.getText().toString(), "0");
			    	}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}			
		});
                
	}
		
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.button_productdetail_cancel:
				finish();
				break;
			case R.id.bn_back:
				finish();
				break;
			default:
				final int buttonId = v.getId();
				if(this.shop != null){
					clickButtonSelectOrAdd(buttonId);
				}else{

					new AlertDialog.Builder(ProductDetailActivity.this)
						.setMessage(getResources().getString(R.string.question_shop_create))
					    .setCancelable(false)
					    .setPositiveButton(R.string.button_shop_yes, new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog, int id) {
					    		if(product != null){					    							    								    		
					    			Intent intent = new Intent();				
					    			product.setOrganization(organization);
						    		product.setOrganizationCategory(category);	
						    		intent.putExtra("product", product);
						    		setResult(OrganizationListProductActivity.ACTION_ADD_BASKET, intent);
						    		ProductDetailActivity.this.finish();
					    		}
					    	}
					    })
					    .setNegativeButton(R.string.button_shop_no, new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog, int id) {
					    		dialog.cancel();
					    	}
				    }).show();										
				}
				break;
		}				   	

	}
	
	private void clickButtonSelectOrAdd(int buttonId){
		int iUnit = this.sUnit.getSelectedItemPosition();
		String sCategory = (String) this.sCategory.getSelectedItem();
		
		String name = eName.getText().toString();
		String count = eCount.getText().toString();
		String price = ePrice.getText().toString();
		
		if(name != null && !"".equals(name)){
		
			Product product = this.product;
			if(product == null){ 
				product = new Product(); 
			} 
	    	product.setCount(1D);
	    	Intent intent = new Intent();	    	 

	    	try{  
	    		
	    		if(!"".equals(name)){ product.setName(name); }	            	
        		if(!"".equals(count)){ product.setCount(Double.parseDouble(count)); }
        		if(!"".equals(price)){ product.setPrice(Double.parseDouble(price)); }        		
        		product.setUnit(iUnit);
        		
        		if(!sCategory.equals(getResources().getString(R.string.main_no_category))){
        			DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
        			Category category = FactoryDAO.getCategoryDAO(this)
        					.getByName(sCategory, Language.value(dc.getLanguage()));
            		product.setCategory(category);
            	}else{
            		product.setCategory(null);
            	}
        		        		
        		switch(buttonId){
    				case R.id.button_productdetail_add:     				
	    	        	intent.putExtra("product", product);
	    	        	if(this.product == null){	    	        		
	    	        		setResult(ProductListActivity.ACTION_ADD, intent);
	    	        	}else{
	    	        		if(this.organization != null){
	    	        			product.setOrganization(organization);
					    		product.setOrganizationCategory(category);
	    	        			setResult(OrganizationListProductActivity.ACTION_ADD_ITEM, intent);
	    	        		}else{
	    	        			setResult(ProductListActivity.ACTION_EDIT, intent);
	    	        		}
	    	        	}
	    				break;	
	    			case R.id.button_productdetail_sel:
	    				if(!product.isSelected()){ product.setSelected(true); }
	    	        	intent.putExtra("product", product);
	    	        	setResult(ProductListActivity.ACTION_SELECT, intent);	    			
	    				break;
        		}        		
        		finish();        		
	    	}catch(Exception e){
        		Log.e("Blad", e.getMessage());
        	}
		}
	}
	
	public class GetProductNameTask extends AsyncTask<String, Void, ArrayList<String>> {
		
		@Override
	    // three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args){
			Log.d("gottaGo", "doInBackground");
			ArrayList<String> predictionsArr = new ArrayList<String>();				
			try{
				if(args[0].length() >= MIN_SEARCH){
					NoteService noteService = WebServiceFactory.getNoteService();
					List<ProductXml> list = noteService.getProductName(args[0], args[1]);
					for (ProductXml product : list) {
						if(!product.getName().equals(args[0])){
							predictionsArr.add(product.getName());
						}
					}
				}
			}catch (Exception e) {
				Log.e("YourApp", "GetPlaces : doInBackground", e);
			}
			return predictionsArr;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result){
			Log.d("YourApp", "onPostExecute : " + result.size());
			//update the adapter
			adapterProductName = new ArrayAdapter<String>(getBaseContext(), R.layout.productname_item, result);
			adapterProductName.setNotifyOnChange(true);
			//attach the adapter to textview
			eName.setAdapter(adapterProductName);
			Log.d("YourApp", "onPostExecute : autoCompleteAdapter" + adapterProductName.getCount());
			adapterProductName.notifyDataSetChanged();
		}

	}
	
}