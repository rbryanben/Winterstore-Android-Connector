package com.wapazockdemo.winterstoreconnector.utils;

import okhttp3.MediaType;

public class Shared {
    // Server URLs
    public static String serverURL = "https://cloudwinterstore.co.zw";
    public static String getTokenURL =  serverURL + "/api/get-token/";
    public static String downloadURL = serverURL + "/api/download/";
    public static String createFolderURL = serverURL + "/api/create-client-folder";
    public static String uploadURL = serverURL + "/api/upload-file/";
    public static String deleteURL = serverURL + "/api/client-delete-index-object/";
    public static String giveKeyURL = serverURL + "/api/client-give-key/";
    public static String removeKeysURL = serverURL + "/api/client-remove-keys/";
    public static String getFolderURL = serverURL + "/api/client-get-folder/";
    public static String fileInfoURL = serverURL + "/api/client-file-info/";

    // Standards
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
}
