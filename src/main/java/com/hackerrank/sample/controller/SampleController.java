package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

// Exercise 2 part
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import netscape.javascript.JSObject;
import java.net.*;

@RestController
public class SampleController {

	
	   final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	   RestTemplate restTemplate = new RestTemplate();
	   String result = restTemplate.getForObject(uri, String.class);			
	   JSONObject root = new JSONObject(result);
	   
	   JSONArray data = root.getJSONArray("data");
	   
	   @CrossOrigin
	   @GetMapping("/exercise2/{substr}")
	   private ResponseEntity< ArrayList > exercise2(@PathVariable("substr") String substr)   
	   {  
		try{
            String urlString = "https://jsonmock.hackerrank.com/api/moviesdata/search/?Title="+ substr;
            URL url = new URL(urlString);
            try{
                URLConnection conn = url.openConnection();
                BufferedReader reader;
                String line;
                StringBuilder responseContent = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
                JSONObject jsonObject = new JSONObject(responseContent.toString());
				Integer total_pages = jsonObject.getInt("total_pages");
				ArrayList titles = new ArrayList<>();

				for(int i=1; i <= total_pages; i++){
					String pageUrlString = "https://jsonmock.hackerrank.com/api/moviesdata/search/?Title="+ substr +"&page="+ i;
					URL pageUrl = new URL(pageUrlString);
					URLConnection connData = pageUrl.openConnection();
					StringBuilder responseDataContent = new StringBuilder();
					reader = new BufferedReader(new InputStreamReader(connData.getInputStream()));
					while ((line = reader.readLine()) != null) {
						responseDataContent.append(line);
					}
					reader.close();
					JSONObject jsonDataObject = new JSONObject(responseDataContent.toString());
					JSONArray all_data = jsonDataObject.getJSONArray("data");
					for(int j=0; j<all_data.length(); j++){
						JSONObject value = new JSONObject(all_data.get(j).toString());
						titles.add(value.getString("Title"));
					}
				}
                Collections.sort(titles);   
                return new ResponseEntity<ArrayList>(titles, HttpStatus.OK);
            }catch(Exception E){
				System.out.println("Error encountered : "+E.getMessage());
				return new ResponseEntity<ArrayList>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception E){
			System.out.println("Error encountered : "+E.getMessage());
			return new ResponseEntity<ArrayList>(HttpStatus.NOT_FOUND);
        }
	   } 

	   @CrossOrigin
		@GetMapping("/filter/price/{initial_price}/{final_price}")  
		private ResponseEntity< ArrayList<FilteredProducts> > filtered_books(@PathVariable("initial_price") int init_price , @PathVariable("final_price") int final_price)   
		{  
			
			try {
				
			
					ArrayList<FilteredProducts> books = new ArrayList<FilteredProducts>();

					//Iterating the data object which contains product information
					for (int i = 0; i < data.length(); ++i) {
						JSONObject rec = data.getJSONObject(i);
						int price = rec.getInt("price");
						String barCode = rec.getString("barcode");
						//Check price is between two prices given
						if(init_price < price && price < final_price){
							FilteredProducts item = new FilteredProducts(barCode);
							books.add(item);
						}
					}

					if(books.size() > 0){
						return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.OK);
					}else{
						return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.BAD_REQUEST);
					}
   
			}catch(Exception E)
				{
					System.out.println("Error encountered : "+E.getMessage());
					return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
		@CrossOrigin
		@GetMapping("/sort/price")  
		private ResponseEntity<SortedProducts[]> sorted_books()   
		{  
			System.out.println(data);
			try {
				
		        SortedProducts[] ans=new SortedProducts[data.length()];
				JSONArray sortedJsonArray = new JSONArray();

				List<JSONObject> dataList = new ArrayList<JSONObject>();
				for (int i = 0; i < data.length(); i++)
					dataList.add(data.getJSONObject(i));

				Collections.sort(dataList, new Comparator<JSONObject>() {
					@Override
					public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
						int compare = 0;
						try
						{
							int keyA = jsonObjectA.getInt("price");
							int keyB = jsonObjectB.getInt("price");
							compare = Integer.compare(keyA, keyB);
						}
						catch(JSONException e)
						{
							e.printStackTrace();
						}
						return compare;
					}
				});

				for (int i = 0; i < data.length(); i++) {
					sortedJsonArray.put(dataList.get(i));
				}
				
				for (int i = 0; i < sortedJsonArray.length(); ++i) {
					JSONObject rec = sortedJsonArray.getJSONObject(i);
					String barCode = rec.getString("barcode");
					SortedProducts item = new SortedProducts(barCode);
					ans[i] = item;
					
				}

			    return new ResponseEntity<SortedProducts[]>(ans, HttpStatus.OK);
			    
			}catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<SortedProducts[]>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
	
}
