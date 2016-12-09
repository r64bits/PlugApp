package com.users.plugapp;

/**
 * Created by Andrade on 08/12/2016.
 */
public interface IService {
    void onServiceSuccess(String serviceName,String response);
    void onServiceError(String serviceName,String messageError);
}
