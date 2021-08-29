package com.wapazockdemo.winterstoreconnector.utils;

import android.media.AudioManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
import java.sql.ClientInfoStatus;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WinterstoreAuthenticator {

    // Variables
    private String USERNAME, PASSWORD, INTEGRATION;
    private AuthenticationInterface AUTHENTICATION_INTERFACE;

    // Constructor
    public WinterstoreAuthenticator(String username, String password, String integration, AuthenticationInterface authenticationInterface){
        this.USERNAME = username;
        this.PASSWORD = password;
        this.AUTHENTICATION_INTERFACE = authenticationInterface;
        this.INTEGRATION = integration;

        // Get Token
        getToken();
    }

    // Get Token : On receiving token should callback the interface
    private void getToken(){
        // Client
        OkHttpClient client = new OkHttpClient();

        // Body
        JSONObject credentials =  new JSONObject();
            // Json object to send along with the request
        try {
            credentials.put("username",this.USERNAME);
            credentials.put("password",this.PASSWORD);
            credentials.put("integration",this.INTEGRATION);
        } catch (JSONException e) {
            e.printStackTrace();
            AUTHENTICATION_INTERFACE.result(false,"Failed To Created JSON Object");
        }
           // request body
        RequestBody clientBody = RequestBody.create(Shared.JSON,credentials.toString());


        // Request
        Request clientRequest = new Request.Builder()
                .url(Shared.getTokenURL)
                .post(clientBody)
                .build();


        // Send Request
        client.newCall(clientRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AUTHENTICATION_INTERFACE.result(false,"Connection Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if response is not successful, server unreachable
                if (!response.isSuccessful()) {
                    AUTHENTICATION_INTERFACE.result(false,"Request Failed");
                }

                //result from the server
                String result = response.body().string();

                //check response
                switch (result){
                    case "Invalid JSON":
                        AUTHENTICATION_INTERFACE.result(false,"Invalid JSON");
                        break;
                    case "denied":
                        AUTHENTICATION_INTERFACE.result(false,"Invalid Credentials");
                        break;
                    case "not found":
                        AUTHENTICATION_INTERFACE.result(false,"Invalid Integration");
                        break;
                    default:
                        AUTHENTICATION_INTERFACE.result(true, result);
                        break;
                }

            }
        });
    }

    // Callback interface for the class
    public interface AuthenticationInterface{
        void result(Boolean wasSuccessful, String result);
    }

}
