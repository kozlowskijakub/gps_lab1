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

public class MainActivity extends Activity implements LocationListener {

	public TextView lattitude;
	public TextView longitude;
	public TextView direction;
	public TextView speed;
	public TextView poiDistance;
	public TextView poiDirection;
	public static final Double Radius = 6371d;

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

		if (clicks > 0) {
			this.poiDistance.setText(String.valueOf(countDistance(
					this.currentPoi, listPoi.get(listPoi.size() - 2))));
			this.poiDirection.setText(String.valueOf(countBearing(
					this.currentPoi, listPoi.get(listPoi.size() - 2))));
		}
		clicks++;
	}

	public Double countDistance(Poi poi1, Poi poi2) {
		Double distance;
		Double x, y;
		x = (poi1.longitude - poi2.longitude)
				* Math.cos((poi1.longitude + poi2.longitude) / 2);
		y = (poi1.lattitude - poi2.lattitude);
		distance = Radius * Math.sqrt((x * x) + (y * y));
		return distance;
	}

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
