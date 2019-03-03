package app.helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class Requests {
    public static JSONArray getRequest(String str, String authHeader) {
        try {
            String response = getJSON(str, authHeader);
            return new JSONArray(response);
        }
        catch(IOException ex) {
            return new JSONArray();
        }
    }

    private static String   getJSON(String str, String authHeader) throws  IOException {
        URL urlForGetRequest = new URL(str);
        String readLine;
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", authHeader);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            return response.toString();
        }
        return  "";
    }

    public static String postJSON(String str, String authHeader, Map<String, Object> params) throws  IOException {
        StringBuilder post_params = new StringBuilder("{");
        for (Map.Entry<String, Object> pair : params.entrySet()) {
            post_params.append("\"").append(pair.getKey()).append("\":");
            try {
                post_params.append((double) pair.getValue()).append(",");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                post_params.append("\"").append(pair.getValue()).append("\",");

            }
        }
        post_params = new StringBuilder(post_params.substring(0, post_params.length() - 1));
        post_params.append("}");
//        System.out.println(post_params);
        URL obj = new URL(str);
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setRequestProperty("Authorization", authHeader);
        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(post_params.toString().getBytes());
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();
        System.out.println(responseCode);
        if (responseCode != 400) {
            BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            System.out.println(response.toString());
            return (response.toString());
        } else {
            return "";
        }
    }

    public static JSONObject getRequestObj(String str, String authHeader) {
        try {
//            String authHeader = "f60263f34d94232927e525e96892586dbde4eb1457cf2807b05018197fcafff2";
            String response = getJSON(str, authHeader);
//            System.out.println(response);
//            System.out.println(str);
            return new JSONObject(response);
        }
        catch(IOException ex) {
            return new JSONObject();
        }
    }

    public static JSONObject postRequestObj(String str, String authHeader, Map<String, Object> params) {
        try {
            String response = postJSON(str, authHeader, params);
            return new JSONObject(response);
        }
        catch(Exception ex) {
            return new JSONObject();
        }
    }

    public static JSONArray postRequestArray(String str, String authHeader, Map<String, Object> params) {
        try {
            String response = postJSON(str, authHeader, params);
            return new JSONArray(response);
        }
        catch(Exception ex) {
            return new JSONArray();
        }
    }


}
