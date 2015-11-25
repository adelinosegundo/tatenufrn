package com.android_dev.tatenuufrn.managers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.User;
import com.raizlabs.android.dbflow.structure.container.JSONModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by adelinosegundo on 11/22/15.
 */
public class APIManager {
    private static APIManager apiManager;
    private final String apiServerUrl = TatenuUFRNApplication.API_HOST;
    private final String apiPath = "/api/v1";

    private final String loginPath = "/auth/login";
    private final String logoutPath = "/auth/logout";
    private final String eventsPath = "/events";
    private final String goingPath = "/going";
    private final String arrivePath = "/arrive";
    private final String ratePath = "/rate";
    private final String likePath = "/like";
    private final String dislikePath = "/dislike";
    private final String eventUsersPath = "/event_users";

    private User user;
    private CookieManager cookieManage;

    private APIManager(){
        cookieManage = new CookieManager();
        CookieHandler.setDefault(cookieManage);
    }
    public static APIManager getInstance(){
        if (apiManager == null)
            apiManager = new APIManager();
        return apiManager;
    }

    public String getAuthenticatedUserId(){
        return user.getId();
    }

    public void login(final Context context, String login, final Intent intent){
        String url = apiServerUrl+apiPath+loginPath+"?login="+login;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONModel<User> jsonModel = new JSONModel<>(object, User.class);
                    user = jsonModel.toModel();
                    user.save();
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin: " + error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    public void logout(Context context, Response.Listener<String> responseListener){
        String url = apiServerUrl+apiPath+logoutPath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogout: "+error.getMessage());
            }
        });
        user = null;
        queue.add(stringRequest);
    }

    public void getEventUserData(Context context, Response.Listener<String> responseListener){
        String url = apiServerUrl+apiPath+eventsPath+eventUsersPath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedGetUSerData: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    public void going(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+eventID+goingPath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedGoing: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
    public void arrive(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+"/"+eventID+arrivePath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedJoin: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
    public void rate(Context context, Response.Listener<String> responseListener, String eventID, float rate){
        String url = apiServerUrl+apiPath+eventsPath+"/"+eventID+ratePath+"?rate="+String.valueOf(rate);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedRate: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
    public void like(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+"/"+eventID+likePath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLike: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
    public void dislike(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+"/"+eventID+dislikePath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedDislike: "+error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
}
