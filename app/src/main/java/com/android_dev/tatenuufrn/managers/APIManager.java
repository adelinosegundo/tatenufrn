package com.android_dev.tatenuufrn.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by adelinosegundo on 11/22/15.
 */
public class APIManager {
    public static APIManager apiManager;
    private final String apiServerUrl = TatenuUFRNApplication.API_HOST;
    private final String apiPath = "/api/v1";

    private final String loginPath = "/auth/login";
    private final String logoutPath = "/auth/logout";
    private final String eventsPath = "/events";
    private final String tellIAmGoingPath = "/tell_i_am_going";
    private final String joinPath = "/join";
    private final String ratePath = "/rate";
    private final String likePath = "/like";
    private final String dislikePath = "/dislike";

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
    public void login(Context context, Response.Listener<String> responseListener){
        String url = apiServerUrl+apiPath+loginPath+"?login=mylogin";
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin:"+error.getMessage());
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
                Log.e("APIManager", "FailedLogin");
            }
        });

        queue.add(stringRequest);
    }

    public void events(Context context, Response.Listener<String> responseListener){
        SharedPreferences sharedPreferences = context.getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        String lastUpdated = sharedPreferences.getString("lastUpdated", "");

        String url = apiServerUrl+apiPath+eventsPath;
        if (!lastUpdated.equals(""))
            url += "?last_updated=" + lastUpdated;

        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin");
            }
        });

        queue.add(stringRequest);
    }

    public void tellIAmGoing(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+eventID+tellIAmGoingPath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin");
            }
        });

        queue.add(stringRequest);
    }
    public void join(Context context, Response.Listener<String> responseListener, String eventID){
        String url = apiServerUrl+apiPath+eventsPath+eventID+joinPath;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin");
            }
        });

        queue.add(stringRequest);
    }
    public void rate(Context context, Response.Listener<String> responseListener, String eventID, float rate){
        String url = apiServerUrl+apiPath+eventsPath+eventID+ratePath+"?rate="+String.valueOf(rate);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIManager", "FailedLogin");
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
                Log.e("APIManager", "FailedLogin");
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
                Log.e("APIManager", "FailedLogin");
            }
        });

        queue.add(stringRequest);
    }
}
