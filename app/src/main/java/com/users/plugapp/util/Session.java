package com.users.plugapp.util;

import android.content.Context;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.users.plugapp.IService;
import com.users.plugapp.proxy.ServiceTask;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Andrade on 07/12/2016.
 */
public class  Session {
    private static String ServiceName = "session.json";
    private static String TOKEN;
    private static final String APP_ID = "50705";
    private static final String AUTH_KEY = "A7gAgwah9DxDs8Q";
    private  static final String AUTH_SECRET = "WXEMFASb4VeupDk";
    private static String SIGNATURE = "";
    private static long TimeStamp;
    private static String NONCE;
    private static IService IService;

    public static String getToken(){
        return TOKEN;
    }

    public  static void CreateSession(Context context, IService _iService){
        TimeStamp = Math.round((new Date()).getTime() / 1000);
        NONCE = Double.toString( Math.floor(Math.random() * 1000));

        IService = _iService;

        generateSignature();
        createSession(context);
    }


    private static void generateSignature() {
        String data = "application_id="+APP_ID+"&auth_key="+ AUTH_KEY +"&nonce="+NONCE+"&timestamp="+TimeStamp+"&user[login]=r64bits&user[password]=12345678";
        try {
            SIGNATURE = calculateRFC2104HMAC(data,AUTH_SECRET);
        } catch (Exception e) {
        }
    }

    private static void createSession(Context context) {
        ServiceTask service = new ServiceTask(ServiceName);
        service.addHeader("Content-Type", "application/json");
        service.addHeader("QuickBlox-REST-API-Version", "0.1.0");

        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            params.put("application_id", APP_ID);
            params.put("auth_key", AUTH_KEY);
            params.put("timestamp", TimeStamp);
            params.put("nonce", NONCE);
            params.put("signature", SIGNATURE);

            JSONObject u = new JSONObject();
            u.put("login","r64bits");
            u.put("password","12345678");

            params.put("user",u);
            entity = new StringEntity(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        service.post(context, service.baseUrl, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");

                    JSONObject r = new JSONObject(response);
                    TOKEN = r.getJSONObject("session").getString("token");

                    IService.onServiceSuccess(ServiceName,response);

                } catch (Exception e) {
                    IService.onServiceError(ServiceName,e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    IService.onServiceError(ServiceName,response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static String calculateRFC2104HMAC(String data, String key){
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            sha256_HMAC.init(secret_key);
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
