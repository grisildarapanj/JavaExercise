package com.hackerrank.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// Exercise 2 part
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import netscape.javascript.JSObject;

import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootApplication
public class SampleApplication {
    // Exercise 2 part
    static void  getMovieTitles(String substr) {
        try{
            String urlString = "https://jsonmock.hackerrank.com/api/moviesdata/search/?Title="+ substr +"&page=1";
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
                JSONArray all_data = jsonObject.getJSONArray("data");
                ArrayList titles = new ArrayList<>();
                for(int i=0; i<all_data.length(); i++){
                    JSONObject value = new JSONObject(all_data.get(i).toString());
                    titles.add(value.getString("Title"));
                }
                Collections.sort(titles);   
                System.out.println(titles);
            }catch(IOException ex){
                System.out.println("Here");
            }
        }catch(MalformedURLException ex){
            System.out.println("Here");
        }
    }
    // End exercise 2 part
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
        getMovieTitles("spiderman");
    }

}
