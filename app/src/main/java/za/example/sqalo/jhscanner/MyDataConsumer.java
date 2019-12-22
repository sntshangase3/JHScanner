package za.example.sqalo.jhscanner;

import android.content.Context;

import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;


public class MyDataConsumer {
    MainActivity activity = MainActivity.instance;

    public JsonObject GetMyAuthenticatedRestData( String myUrlStr ) {
        JsonObject myRestData = new JsonObject();
        try{
            URL myUrl = new URL(myUrlStr);
            HttpURLConnection urlCon = (HttpURLConnection)myUrl.openConnection();
            urlCon.setRequestProperty("Method", "POST");
            urlCon.setRequestProperty("Accept", "application/json");
            urlCon.setConnectTimeout(5000);
            urlCon.setDoInput(true);

            int responseCode = urlCon.getResponseCode();
            //set the basic auth of the hashed value of the user to connect
          //  urlCon.setRequestProperty("Authorization", GetMyCredentials() );
            String urlParameters = "api_key=28664f6f56b69885e0ceeffb716a8cb954ab6750&id_number=8510256057084";
            OutputStreamWriter writer = new OutputStreamWriter(urlCon.getOutputStream());
            writer.write(urlParameters);
            writer.flush();

            InputStream is = urlCon.getInputStream();
            InputStreamReader isR = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isR);
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while( (line = reader.readLine()) != null ){
                buffer.append(line);
            }
            reader.close();
            JsonParser parser = new JsonParser();
            myRestData = (JsonObject) parser.parse(buffer.toString());

            return myRestData;

        }catch( MalformedURLException e ){
            e.printStackTrace();
            myRestData.addProperty("error", e.toString());
            return myRestData;
        }catch( IOException e ){
            e.printStackTrace();
            myRestData.addProperty("error", e.toString());
            return myRestData;
        }
    }


    /**
     * Uses the Apache Commons codec binary Base64 package for encoding
     * of credentials, so none transmit 'in the open'.
     *
     * @return String of credentials for use with authenticated REST source
     */
    private String GetMyCredentials () {
        String rawUser = "dafabet2";
        String rawPass = "Password1!";
        String rawCred = rawUser+":"+rawPass;
        String myCred = javax.xml.bind.DatatypeConverter.printBase64Binary(rawCred.getBytes());
        return "Basic "+myCred;
    }

}
