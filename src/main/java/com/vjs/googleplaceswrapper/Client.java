package com.vjs.googleplaceswrapper;

public class Client {

	public static void main(String args[])
	{
		GooglePlaces client = new GooglePlaces("AIzaSyACbAdqeSntVZC0a6CcfZ70OttFwzp4NhE");
		
		
		client.getPlacesByRadar(51.503186,-0.126446, 50000, (new Param("types")).value("museum"));
	}
}
