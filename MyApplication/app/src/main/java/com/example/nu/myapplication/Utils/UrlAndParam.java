package com.example.nu.myapplication.Utils;

import java.util.HashMap;

/**
 * Created by EVILUTION on 6/2/2560.
 */

public class UrlAndParam {
    HashMap<String, String> hashMap = null;
    String url = GlobalVariables.baseURL;

    public UrlAndParam(String url,HashMap<String,String> hashMap){
        this.hashMap = hashMap;
        this.url += url;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }
}
