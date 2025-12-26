/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author CUSPIDE4
 */
public class RestClient {

    public static StringBuilder clientPost(JSONObject data, String url) throws IOException, JSONException {
        StringBuilder content = new StringBuilder();
        Gson gson = new Gson();
        try {
            URL objUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) objUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            // Not required
            // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

            // Writes the JSON parsed as string to the connection
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(data.toString().getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
        return content;
    }

    public static StringBuilder clientPost2(JSONObject data, String url) throws IOException, JSONException {

        StringBuilder content = null;

        try {
            URL objUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) objUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            // Not required
            // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

            // Writes the JSON parsed as string to the connection
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(data.toString().getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
        return content;
    }


    public static String clientGet(URL url) {
        String output = "";

        try {
            //creamos una URL donde esta nuestro webservice
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //indicamos por que verbo HTML ejecutaremos la solicitud
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                //si la respuesta del servidor es distinta al codigo 200 lanzaremos una Exception
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            //creamos un StringBuilder para almacenar la respuesta del web service
            StringBuilder sb = new StringBuilder();
            int cp;

            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }

            //en la cadena output almacenamos toda la respuesta del servidor
            output = sb.toString();

            conn.disconnect();
            return output;
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static StringBuilder clientPut(JSONObject data, String url) throws IOException, JSONException {

        StringBuilder content = null;

        try {
            URL objUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) objUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            // Not required
            // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

            // Writes the JSON parsed as string to the connection
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(data.toString().getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
        return content;
    }
}
