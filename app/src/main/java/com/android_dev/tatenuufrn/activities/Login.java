package com.android_dev.tatenuufrn.activities;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.managers.APIManager;
import com.android_dev.tatenuufrn.vendor.OAuthTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.wuman.android.auth.OAuthManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Login extends Activity {
    private Button loginButton;
    private String jsonResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        sharedPreferences.getString("userToken", "");

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                login("mylogin"); // LOGIN FROM INSIDE THE UFRN
                sigaa_login();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("Result", "reqCode: " + String.valueOf(requestCode) + " resCode: " + String.valueOf(resultCode));
    }


    public void sigaa_login(){
        OAuthTokenRequest.getInstance().getTokenCredential(this, "http://apitestes.info.ufrn.br/authz-server", "taten-ufrn-id", "tatenufrn", new OAuthManager.OAuthCallback<Credential>() {
            @Override public void run(OAuthManager.OAuthFuture<Credential> future) {
                try {
                    Credential credential = future.getResult();
                    OAuthTokenRequest.getInstance().setCredentials(credential);
                    if (credential != null) {
                        getUserData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // make API queries with credential.getAccessToken()
            }
        });
    }

    public void login(String login){
        final Context context = this;
        Intent intent = new Intent(context, ListEvents.class);
        APIManager.getInstance().login(this, login, intent);
    }

    public void getUserData(){
        final Context context = this;

        String urlJsonObj = "http://apitestes.info.ufrn.br/usuario-services/services/usuario/info";

        OAuthTokenRequest.getInstance().resourceRequest(this, Request.Method.GET, urlJsonObj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nome = jsonObject.getString("nome");
                    String login = jsonObject.getString("login");

                    jsonResponse = "";
                    jsonResponse += "Name: " + nome + "\n\n";
                    jsonResponse += "Login: " + login + "\n\n";

                    VolleyLog.d("SAID", "UserData", response);
                    Log.i("USERDATA", response);
                    login(login);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("SAIDA", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
