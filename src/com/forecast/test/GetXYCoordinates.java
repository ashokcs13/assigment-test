package com.forecast.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;


public class GetXYCoordinates {


    public final static Logger log = Logger.getLogger("GetXYCoordinates");

    private static GetXYCoordinates instance = null;

	@SuppressWarnings("unused")
	private JSONParser jsonParser;

    public GetXYCoordinates() {
        jsonParser = new JSONParser();
    }

    public static GetXYCoordinates getInstance() {
        if (instance == null) {
            instance = new GetXYCoordinates();
        }
        return instance;
    }

    private String getRequest(String url) throws Exception {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();      

        return response.toString();
    }

    public Map<String, Double> getFromLocation(Double latitude,Double longitude) {
        Map<String, Double> res;
        StringBuffer query;
        String queryResult = null;

        query = new StringBuffer();
        res = new HashMap<String, Double>();

        query.append("https://api.weather.gov/points/");
        query.append(latitude);
        query.append(',');
        query.append(longitude);
		
        log.debug("Point Query:" + query);

        try {
            queryResult = getRequest(query.toString());
        } catch (Exception e) {
            log.error("Error when trying to get data with the following query " + query);
        }

        if (queryResult == null) {
            return null;
        }
        
		Object obj = JSONValue.parse(queryResult);
        log.debug("obj=" + obj);

        if (obj instanceof JSONObject) {
        	JSONObject array = (JSONObject) obj;
            if (array.size() > 0) {
                JSONObject jsonObject = (JSONObject) array.get("properties");
                log.debug("jsonObject=" + jsonObject);
                String office = (String) jsonObject.get("cwa");
                Long x = (Long) jsonObject.get("gridX");
                Long y = (Long) jsonObject.get("gridY");
                
               log.debug("office=" + office);
               log.debug("gridX=" + x);
               log.debug("gridY=" + y);
			   
               getForecastLocation( x,y,office);

            }
        }

        return res;
    }
    
    /*get forcast*/
    public Map<String, Long> getForecastLocation(Long x,Long y,String office) {
        Map<String, Long> resf;
        StringBuffer queryf;
        String queryResultf = null;

        queryf = new StringBuffer();
        resf = new HashMap<String, Long>();

        queryf.append("https://api.weather.gov/gridpoints/");
        queryf.append(office);
        queryf.append("/");
        queryf.append(x);
        queryf.append(",");
        queryf.append(y);
        queryf.append("/forecast");
		
        log.debug("Forecast Query:" + queryf);

        try {
            queryResultf = getRequest(queryf.toString());
        } catch (Exception e) {
            log.error("Error when trying to get data with the following query " + queryf);
        }

        if (queryResultf == null) {
            return null;
        }
        
		Object objf = JSONValue.parse(queryResultf);
        log.debug("obj forecast=" + objf);

        if (objf instanceof JSONObject) {
        	JSONObject arrayf = (JSONObject) objf;
            if (arrayf.size() > 0) {
                JSONObject jsonObjectf = (JSONObject) arrayf.get("properties");
                log.debug("jsonObject=" + jsonObjectf);
                JSONArray officef=(JSONArray)jsonObjectf.get("periods");
                for(int i=0;i<officef.size();i++){
                    JSONObject jsonObject = (JSONObject) officef.get(i);

                    String detailedForecast = (String) jsonObject.get("detailedForecast");
                    Long number = (Long) jsonObject.get("number");
                    String name = (String) jsonObject.get("name");
                    if(number%2!=0 && number>1 && number<13) {
                        System.out.println(name+" : "+detailedForecast);
                        }

                }

            }
        }

        return resf;
    }

}
