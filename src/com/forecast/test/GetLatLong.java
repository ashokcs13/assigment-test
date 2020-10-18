package com.forecast.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;

public class GetLatLong {
	//static String latitude = "39.7456";
	//static String longitude = "-97.0892";
	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		GetXYCoordinates.log.info("This is Logger Info");
		System.out.println("Please Enter latitude & longitude i.e. 39.7456 -97.0892");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		String ab[] = reader.readLine().trim().split("\\s+"); 
		//Double a = Double.parseDouble(ab[0]); 
		//Double b = Double.parseDouble(ab[1]); 
		//System.out.println("value of a: "+a+" value of b: "+b);
//		log4j:WARN No appenders could be found for logger (OpenStreeMapUtils).
//		log4j:WARN Please initialize the log4j system properly.
//		log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
    	
        Map<String, Double> coords;
        coords = GetXYCoordinates.getInstance().getFromLocation(Double.parseDouble(ab[0]),Double.parseDouble(ab[1])); 
        
    }

}
