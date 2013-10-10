package com.example.lab_gps2;

public class Poi implements Cloneable {
	public Double lattitude ;
	public Double longitude;
	
	public Poi clone(){
		try{
			return (Poi) super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
