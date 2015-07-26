package com.microstep.android.onclick;

import com.microstep.android.onclick.dao.FactoryDAO;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.model.ShopType;
import com.microstep.onclick.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	
	private Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		StringBuilder buf = new StringBuilder();
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] msgs = new SmsMessage[pdus.length];
			String adress[] = new String[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				adress[i] = msgs[i].getOriginatingAddress();
				buf.append(msgs[i].getMessageBody().toString());
			}

			if(buf.toString().startsWith(SmsActivity.SMS_UNIQUE_ID)){
				
				
				try{
				
					this.context = context;
					String[] tab = buf.toString().split("!");
					String title = tab[1];
					Shop shop = new Shop();
					shop.setSmsPerson(adress[0]);
					shop.setTitle(title);
					shop.setType(ShopType.RECEIVE);
					shop.setFinished(false);					
					long shopId = FactoryDAO.getShopDAO(this.context).insert(shop);
					shop.setId(shopId);
					String[] products = tab[2].split(";");
					for(int i=0; i < products.length; i++){
						String[] content = products[i].split(":");
						Product p = new Product();
						p.setName(content[0]);
						p.setSelected(false);
						p.setCount(Double.parseDouble(content[1]));
						p.setShop(shop);
						p.setPrice(0D);
						FactoryDAO.getProductDAO(this.context).insert(p);
					}
					
					NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);			
					int icon = R.drawable.ic_stat_example;
					CharSequence tickerText = "onClick";
					long when = System.currentTimeMillis();
					Notification notification = new Notification(icon, tickerText, when);					
					CharSequence contentTitle = title;
					String text = String.format(context.getResources().getString(R.string.label_send_recived), adress[0]); 
					Intent notificationIntent = new Intent(context, MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, Notification.FLAG_AUTO_CANCEL);					
					notification.ledARGB = 0xff00ff00;
					notification.ledOnMS = 300;
					notification.ledOffMS = 1000;
					notification.flags |= Notification.FLAG_SHOW_LIGHTS;
					notification.defaults |= Notification.DEFAULT_SOUND;
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					notification.setLatestEventInfo(context.getApplicationContext(), contentTitle, text, contentIntent);
					nm.notify(350, notification);					
					abortBroadcast();				
				}catch (Exception e) {
					Log.e(SmsReceiver.class.toString(), "error", e);
				}

			}

		}

	}

}
