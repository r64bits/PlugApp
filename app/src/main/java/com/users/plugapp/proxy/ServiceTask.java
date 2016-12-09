package com.users.plugapp.proxy;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by ricardo.andrade on 01/11/2016.
 */
public class ServiceTask extends AsyncHttpClient {
    public String baseUrl;

    public ServiceTask(String service){
        this.baseUrl = "https://api.quickblox.com/" + service;
    }
}
