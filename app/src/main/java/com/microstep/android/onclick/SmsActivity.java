package com.microstep.android.onclick;

import java.util.Iterator;
import java.util.List;

import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.model.ShopType;
import com.microstep.onclick.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsActivity extends Activity {

	private Shop shop = null;
	
	public final static String SMS_UNIQUE_ID = "#12#";
	
	private BroadcastReceiver sendSms = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()){
                case Activity.RESULT_OK:
                	SmsActivity.this.shop.setType(ShopType.SEND);
                	FactoryDAO.getShopDAO(SmsActivity.this).update(SmsActivity.this.shop);
                	Toast.makeText(getBaseContext(), R.string.label_sms_status_sent, Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
	
    private BroadcastReceiver devileredSms = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    SmsActivity.this.setResult(Activity.RESULT_OK);
                    SmsActivity.this.finish();
                    break;
                case Activity.RESULT_CANCELED:
                    SmsActivity.this.setResult(Activity.RESULT_CANCELED);
                    SmsActivity.this.finish();
                    break;                        
            }
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		Bundle bundle = getIntent().getExtras();
		String number = bundle.getString("number");
		this.shop = (Shop) bundle.getSerializable("shop");
		this.shop.setSmsPerson(number);
		sendSMS(number, createSmsMessage(this.shop));		
	}
	
	public void sendSMS(String phoneNumber, String message){
		
		String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(sendSms, new IntentFilter(SENT));
        registerReceiver(devileredSms, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(sendSms);
		unregisterReceiver(devileredSms);
		
	}
	
	private String createSmsMessage(Shop shop){
		StringBuffer buffer = new StringBuffer();
		buffer.append(SMS_UNIQUE_ID);
		buffer.append("!");
		buffer.append(shop.getTitle());
		buffer.append("!");
		List<Product> products = shop.getProducts();
		Iterator<Product> iter = products.iterator();
		while(iter.hasNext()){
			Product p = iter.next();
			buffer.append(p.getName());
			buffer.append(":");
			buffer.append(p.getCount());
			buffer.append(";");
		}
		return buffer.toString();
	}
	
}
