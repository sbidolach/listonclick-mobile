package com.microstep.android.onclick.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

public class PhoneLocation {

	private static transient Location currentLocation;
	private transient Context context;
	private transient LocationManager lm;
	private transient Handler handler = new Handler();
	private transient boolean isGps;
	private transient boolean isNetworkLocation;

	/**
	 * GPS location listener.
	 */
	public final LocationListener gpsLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}

	};

	/**
	 * Network location listener.
	 */
	public final LocationListener networkLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			isNetworkLocation = true;
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}

	};

	/**
	 * Method return current location for mobile phone
	 * 
	 * @return current location
	 */
	public static Location getCurrentLocation(Context context) {
		if(currentLocation == null){
			PhoneLocation phoneLocation = new PhoneLocation();
			phoneLocation.init(context);
		}
		return currentLocation;
	}

	public void init(Context context) {
		this.context = context;
		this.lm = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
		this.isGps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// turning on location updates
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);

		// starting best location finder loop
		handler.postDelayed(showTime, 500);
	}

	private Runnable showTime = new Runnable() {

		public void run() {			
			if(isNetworkLocation){
				currentLocation = getNetworkLocation(context);				
				if(currentLocation == null){
					System.out.println("BestLocation not ready, continue to wait");
					handler.postDelayed(this, 500);
				}else{
					// if best location is known, calculate if we need to continue
					// to look for better location
					// if gps is enabled and min satellites count has not been
					// connected or min check count is smaller then 4 (2 sec)
					System.out.println("#########################################");
					System.out.println("BestLocation finded return result to main");
					System.out.println("#########################################");
	
					// removing all updates and listeners
					lm.removeUpdates(gpsLocationListener);
					lm.removeUpdates(networkLocationListener);
				}
			}else{
				// update last best location
				currentLocation = getLocation(context);
	
				// if location is not ready or don`t exists, try again
				if (currentLocation == null && isGps) {
					System.out.println("BestLocation not ready, continue to wait");
					handler.postDelayed(this, 500);
				} else {
					// if best location is known, calculate if we need to continue
					// to look for better location
					// if gps is enabled and min satellites count has not been
					// connected or min check count is smaller then 4 (2 sec)
					System.out.println("#########################################");
					System.out.println("BestLocation finded return result to main");
					System.out.println("#########################################");
	
					// removing all updates and listeners
					lm.removeUpdates(gpsLocationListener);
					lm.removeUpdates(networkLocationListener);
				}
			}
		}
	};

	/**
	 * Returns best location using LocationManager.getBestProvider()
	 * 
	 * @param context
	 * @return Location|null
	 */
	private static Location getLocation(Context context) {

		// fetch last known location and update it
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			String strLocationProvider = lm.getBestProvider(criteria, true);

			System.out.println("strLocationProvider=" + strLocationProvider);
			return lm.getLastKnownLocation(strLocationProvider);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Location getNetworkLocation(Context context){
		
		try {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
