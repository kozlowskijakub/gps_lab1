package com.example.lab_gps2;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	public TextView lattitude;
	public TextView longitude;
	public TextView direction;
	public TextView speed;
//	public TextView poiDistance;
	public TextView poiDistance2;
	public TextView poiDirection;
	public TextView poiNumber;
	public static final Double Radius = 6371000d;

	Poi currentPoi;
	List<Poi> listPoi;

	Button btn_addPoint;
	Integer clicks = 0;
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.btn_addPoint = (Button) findViewById(R.id.btn_addPoint);
		this.lattitude = (TextView) findViewById(R.id.tv_lattitude);
		this.longitude = (TextView) findViewById(R.id.tv_longitude);
		this.direction = (TextView) findViewById(R.id.tv_direction);
		this.speed = (TextView) findViewById(R.id.tv_speed);
		this.poiDirection = (TextView) findViewById(R.id.tv_poi_direction);
//		this.poiDistance = (TextView) findViewById(R.id.tv_poi_distance);
		this.poiDistance2 = (TextView) findViewById(R.id.tv_poi_distance2);
		this.poiNumber = (TextView) findViewById(R.id.tv_points_number);

		currentPoi = new Poi();
		listPoi = new ArrayList<Poi>();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addPoi(View v) {
		Poi poi = this.currentPoi.clone();
		this.listPoi.add(poi);
		this.poiNumber.setText(String.valueOf(this.clicks + 1));

		if (clicks > 0) {
			try {
//				this.poiDistance.setText(String.valueOf(countDistance(this.currentPoi,
//						listPoi.get(listPoi.size() - 2))));
				this.poiDistance2.setText(String.valueOf(countDistance2(this.currentPoi,
						listPoi.get(listPoi.size() - 2))));
				
				this.poiDirection.setText(String.valueOf(countBearing(
						this.currentPoi, listPoi.get(listPoi.size() - 2))));
			} catch (Exception e) {
				// Log.e("parseException", e.toString());
				Toast.makeText(MainActivity.this, e.toString(),
						Toast.LENGTH_LONG).show();
			}
		}
		clicks++;
	}

	public Double countDistance2(Poi poi1, Poi poi2) {

		Double dLat = Math.toRadians(poi2.lattitude - poi1.lattitude);
		Double dLon = Math.toRadians(poi2.longitude - poi1.longitude);
		Double lat1 = Math.toRadians(poi1.lattitude);
		Double lat2 = Math.toRadians(poi2.lattitude);

		Double x = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat1);
		Double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
		Double distance = Radius*y;

		return distance;
	}

//	public Double countDistance(Poi poi1, Poi poi2) {
//		Double distance;
//		Double x, y;
//		x = (poi1.longitude - poi2.longitude)
//				* Math.cos((poi1.longitude + poi2.longitude) / 2);
//		y = (poi1.lattitude - poi2.lattitude);
//		distance = Radius * Math.sqrt((x * x) + (y * y));
//		return distance;
//	}

	public double countBearing(Poi poi1, Poi poi2) {
		Double lat1 = poi1.lattitude;
		Double long1 = poi1.longitude;
		Double lat2 = poi2.lattitude;
		Double long2 = poi2.longitude;

		Double dLon = (long2 - long1);
		Double y = Math.sin(dLon) * Math.cos(lat2);
		Double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		Double bearing = Math.atan2(y, x);
		bearing = Math.toDegrees(bearing);
		bearing = (bearing + 360) % 360;
		bearing = 360 - bearing;
		return bearing;
	}

	@Override
	public void onLocationChanged(Location location) {
		this.lattitude.setText(String.valueOf(location.getLatitude()));
		this.longitude.setText(String.valueOf(location.getLongitude()));
		this.speed.setText(String.valueOf(location.getSpeed()));
		this.direction.setText(String.valueOf(location.getBearing()));

		this.currentPoi.lattitude = location.getLatitude();
		this.currentPoi.longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}
