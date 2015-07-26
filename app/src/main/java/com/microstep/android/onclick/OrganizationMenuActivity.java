/**
 * 
 */
package com.microstep.android.onclick;

import com.google.zxing.integration.android.IntentIntegrator;
import com.microstep.android.onclick.OrganizationCategoryActivity.GetProductNameTask;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.util.Utils;
import com.microstep.onclick.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Seweryn Bidolach
 *
 */
public class OrganizationMenuActivity extends Activity implements OnClickListener {

	public final static int REQUEST_CATEGORY_LIST = 1;
	public final static int REQUEST_MENU 		  = 2;
	public final static int REQUEST_PDF_LIST 	  = 3;
	public final static int REQUEST_PROM_LIST 	  = 4;
	
	private Organization organization = null;    
    private Shop shop = null;
    
	private Button bList = null;
	private Button bBack = null;
	private TextView topTextView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.organizationmenu);
		
		Bundle bundle = getIntent().getExtras();
        this.organization = (Organization) bundle.getSerializable("organization");
        this.shop = (Shop) bundle.getSerializable("shop");
        
        this.bList = (Button) this.findViewById(R.id.bn_shopping_list);
		this.topTextView = (TextView) this.findViewById(R.id.text_title_top);
		this.bList.setOnClickListener(this);
		this.topTextView.setText(this.organization.getName());
		this.bBack = (Button) this.findViewById(R.id.bn_back);
		this.bBack.setOnClickListener(this);	
        
		int news = organization.getNewsletterSize();
		int prod = organization.getProductSize();
		int prom = organization.getPromotionSize();
	
		Button bNews = (Button) this.findViewById(R.id.org_menu_newsletter);
		bNews.setOnClickListener(this);
		if(news == 0) bNews.setVisibility(View.GONE);
		Button bProd = (Button) this.findViewById(R.id.org_menu_products);
		bProd.setOnClickListener(this);
		if(prod == 0) bProd.setVisibility(View.GONE);
		Button bProm = (Button) this.findViewById(R.id.org_menu_promotions);
		bProm.setOnClickListener(this);
		if(prom == 0) bProm.setVisibility(View.GONE);
		
		if(this.shop != null){
			this.bList.setText(getResources().getString(R.string.menu_shopping_list));
			TextView textItem = (TextView) this.findViewById(R.id.products_basket);
			TextView textPrice = (TextView) this.findViewById(R.id.products_price);
			textItem.setText(String.format(getResources().getString(R.string.label_product_amount_items), 
					this.shop.getProducts().size()));
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			textPrice.setText(Utils.getTotalPrice(this.shop.getProducts(), 
					prefs.getString("currencyPref", Utils.getDefaulCurrency(this)), getResources()));
		}else{
			this.bList.setText(getResources().getString(R.string.menu_shops));
			LinearLayout bottomPanel = (LinearLayout) this.findViewById(R.id.product_bottom_panel);
			bottomPanel.setVisibility(View.GONE);	
		}

	}	
	
	@Override
	public void onClick(View v) {		
		Intent intent = null;
		switch(v.getId()){
			case R.id.bn_back:	
				finish();
				break;
			case R.id.bn_shopping_list:
				finish();
				break;	
			case R.id.org_menu_newsletter:
				intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationMenuActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_PDF_LIST);
				break;
			case R.id.org_menu_products:
				intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationMenuActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_CATEGORY_LIST);
				break;
			case R.id.org_menu_promotions:
				intent = new Intent();
				intent.putExtra("organization", organization);
				intent.putExtra("shop", this.shop);
				intent.setClass(OrganizationMenuActivity.this, OrganizationCategoryActivity.class);
				startActivityForResult(intent, REQUEST_PROM_LIST);
				break;
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent = null;
		if(requestCode == REQUEST_CATEGORY_LIST || requestCode == REQUEST_PROM_LIST){
			if(resultCode == OrganizationListActivity.ACTION_LIST){
				finish();
			}else if(resultCode == OrganizationListActivity.ACTION_ADD_ITEM){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationListActivity.ACTION_ADD_ITEM, intent);
				finish();
			}else if(resultCode == OrganizationListActivity.ACTION_ADD_BASKET){
				intent = new Intent();
				Product product = (Product) data.getExtras().getSerializable("product");
				intent.putExtra("product", product);
				setResult(OrganizationListActivity.ACTION_ADD_BASKET, intent);
				finish();
			}
		}
	}
	
}
