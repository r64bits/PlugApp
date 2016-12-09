package com.users.plugapp.service;

import android.content.Context;
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
public class UpdateUserService {
    private IService IService;
    private String ServiceName;
    private Context Context;

    public UpdateUserService(Context context, IService iService,String serviceName){
        this.IService = iService;
        this.Context = context;
        this.ServiceName = serviceName;
    }


    public void Update(String fullName,String email){
        ServiceTask service = new ServiceTask(ServiceName);
        service.addHeader("QuickBlox-REST-API-Version","0.1.0");
        service.addHeader("Content-Type","application/json");
        service.addHeader("QB-Token", Session.getToken());
        StringEntity entity = null;



        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject jsonAdd = new JSONObject();
            jsonAdd.put("full_name", fullName);
            jsonAdd.put("email", email);

            jsonObj.put("user",jsonAdd);
            entity = new StringEntity(jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        service.put(this.Context, service.baseUrl, entity, "application/json", new AsyncHttpResponseHandler() {
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
                String response;
                try {
                    response = new String(responseBody, "UTF-8");
                    IService.onServiceError(ServiceName,response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private  class user{
        public String full_name;
        public String email;

        public user(String f,String e){
            this.full_name =f;
            this.email =e;
        }
    }
}
