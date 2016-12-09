package com.users.plugapp.service;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.users.plugapp.IService;
import com.users.plugapp.proxy.ServiceTask;
import com.users.plugapp.util.Session;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Andrade on 08/12/2016.
 */
public class UserListService {
    private IService IService;
    private String ServiceName = "users.json";
    private Context Context;

    public UserListService(Context context, IService iService){
        this.IService = iService;
        this.Context = context;
    }

    public void GetAllUsers(){
        ServiceTask service = new ServiceTask(ServiceName);
        service.addHeader("QuickBlox-REST-API-Version","0.1.0");
        service.addHeader("QB-Token", Session.getToken());


        service.get(this.Context, service.baseUrl, null, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    IService.onServiceSuccess(ServiceName,response);
                } catch (Exception e) {
                    IService.onServiceError(ServiceName,e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                IService.onServiceError(ServiceName,error.getMessage());
            }
        });

    }
}
