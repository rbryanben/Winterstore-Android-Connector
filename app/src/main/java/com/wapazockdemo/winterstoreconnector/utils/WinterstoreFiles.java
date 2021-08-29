package com.wapazockdemo.winterstoreconnector.utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WinterstoreFiles {

    private String TOKEN;

    public WinterstoreFiles(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    // Create Folder: Creates a folder in the client's project given
    // the parentID and the FolderName
    public void createFolder(String parentID,String folderName, CreateFolderInterface createFolderInterface){
        // Client
        OkHttpClient client = new OkHttpClient();

        // Data to send
        JSONObject newDataObject = new JSONObject();
        try {
            newDataObject.put("parentID", parentID);
            newDataObject.put("folderName", folderName);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Body
        RequestBody body = RequestBody.create(Shared.JSON,newDataObject.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.createFolderURL)
                .post(body)
                .addHeader("Authorization","Token " + TOKEN)
                .build();

        // Send request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                createFolderInterface.result(false,folderName,parentID,"Connection Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if response is not successful, server unreachable
                if (!response.isSuccessful()) {
                    createFolderInterface.result(false,folderName,parentID, "Connection Error");
                }

                //result
                String result = response.body().string();


                switch (result) {
                    case "Invalid JSON":
                        createFolderInterface.result(false,folderName,parentID,"Invalid JSON");
                        break;
                    case  "1701":
                        createFolderInterface.result(false,folderName,parentID,"Folder Already Exists");
                    case "denied":
                        createFolderInterface.result(false,folderName,parentID,result);
                        break;
                    default:
                        createFolderInterface.result(true,folderName,parentID,result);
                }

            }
        });
    }

    // Upload File: Uploads to a folder in the client's project, given
    // a file, name, parentFolder, accessControl and integration
    public void uploadFile(File file, String name, Boolean allowAllUsersWrite, Boolean allowAllUsersRead, Boolean allowKeyUsersRead,
                           Boolean allowKeyUsersWrite, String integration, String parent, String uploadID, UploadFileInterface uploadFileInterface)
    {
        //calculate size
        long size = file.getAbsoluteFile().length();

        // client
        OkHttpClient client = new OkHttpClient();

        // multipart form body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/csv"), file))
                .addFormDataPart("name", name)
                .addFormDataPart("allowAllUsersWrite", String.valueOf(allowAllUsersWrite))
                .addFormDataPart("allowAllUsersRead", String.valueOf(allowAllUsersRead))
                .addFormDataPart("allowKeyUsersWrite", String.valueOf(allowKeyUsersWrite))
                .addFormDataPart("allowKeyUsersRead", String.valueOf(allowKeyUsersRead))
                .addFormDataPart("integration",integration)
                .addFormDataPart("parent",parent)
                .addFormDataPart("size", String.valueOf(size))
                .build();

        // request
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Shared.uploadURL)
                .addHeader("Authorization","Token " + TOKEN)
                .build();

        // send request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                uploadFileInterface.result(false,uploadID,"Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()){
                    uploadFileInterface.result(false,uploadID,"Server Unreachable");
                }

                //keep the result in a variable
                String result = response.body().string();

                switch (result) {
                    case "woahh - does'nt seem like the data we need -- Invalid form data":
                        uploadFileInterface.result(false,uploadID,"Invalid Form");
                        break;
                    case  "1703":
                        uploadFileInterface.result(false,uploadID,"Filename Exist");
                        break;
                    case "500":
                        uploadFileInterface.result(false,uploadID,"Error");
                        break;
                    case "1702":
                        uploadFileInterface.result(false,uploadID, "Bad Filename");
                        break;
                    case "not found":
                        uploadFileInterface.result(false,uploadID,"Invalid Integration");
                        break;
                    default:
                        uploadFileInterface.result(true,uploadID,result);
                        break;
                }

            }
        });
    }

    // Delete Index Object: Deletes an index object given the id
    //  of the object
    public void deleteIndexObject(String id, DeleteIndexObjectInterface deleteIndexObjectInterface){
        //Client
        OkHttpClient client = new OkHttpClient();

        //data to send
        JSONObject data = new JSONObject();
        try{
            data.put("id",id);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Request Body
        RequestBody body = RequestBody.create(Shared.JSON,data.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.deleteURL)
                .addHeader("Authorization","Token " + TOKEN)
                .post(body)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                deleteIndexObjectInterface.result(false,id, "Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()){
                    //server unreachable
                    deleteIndexObjectInterface.result(false,id,"Server Unreachable");
                }

                //keep the result in a variable
                String result = response.body().string();


                switch (result){
                    case  "Hey! This does'nt look like the json file we need -- The JSON supplied is invalid":
                        deleteIndexObjectInterface.result(false,id,"Invalid JSON");
                        break;
                    case "Not Found":
                        deleteIndexObjectInterface.result(false,id,"not found");
                        break;
                    case "denied":
                        deleteIndexObjectInterface.result(false,id,"denied");
                        break;
                    default:
                        deleteIndexObjectInterface.result(true,id,id);
                }


            }
        });
    }


    // Give Key: This method gives a key to another client
    public void giveKey(String clientAccount, String fileID, KeysInterface keysInterface){
        //Client
        OkHttpClient client = new OkHttpClient();

        //data to send
        JSONObject data = new JSONObject();
        try{
            data.put("account",clientAccount);
            data.put("file",fileID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Request Body
        RequestBody body = RequestBody.create(Shared.JSON,data.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.giveKeyURL)
                .addHeader("Authorization","Token " + TOKEN)
                .post(body)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                keysInterface.result(false,clientAccount, fileID,"Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()) {
                    //server unreachable
                    keysInterface.result(false, clientAccount, fileID, "Server Unreachable");
                }

                //keep the result in a variable
                String result = response.body().string();


                switch (result) {
                    case "500":
                        keysInterface.result(false,clientAccount, fileID,  "Invalid JSON");
                        break;
                    case "not found":
                        keysInterface.result( false,clientAccount,fileID, "not found");
                        break;
                    case "denied":
                        keysInterface.result( false,clientAccount,fileID, "denied");
                        break;
                    default:
                        keysInterface.result(true,clientAccount,fileID, clientAccount);
                }

            }
        });
    }

    // Give Key: This method gives a key to another client
    public void removeKeys(String clientAccount, String fileID, KeysInterface keysInterface){
        //Client
        OkHttpClient client = new OkHttpClient();

        //data to send
        JSONObject data = new JSONObject();
        try{
            data.put("account",clientAccount);
            data.put("file",fileID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Request Body
        RequestBody body = RequestBody.create(Shared.JSON,data.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.removeKeysURL)
                .addHeader("Authorization","Token " + TOKEN)
                .post(body)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                keysInterface.result(false,clientAccount,fileID, "Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()){
                    //server unreachable
                    keysInterface.result(false,clientAccount, fileID,"Server Unreachable");

                }

                //keep the result in a variable
                String result = response.body().string();


                switch (result){
                    case  "500":
                        keysInterface.result(false,clientAccount,fileID,"Invalid JSON");
                        break;
                    case "not found":
                        keysInterface.result(false,clientAccount,fileID,"not found");
                        break;
                    case "denied":
                        keysInterface.result(false,clientAccount,fileID,"denied");
                        break;
                    default:
                        keysInterface.result(true,clientAccount,fileID,"200");
                }

            }
        });
    }

    // File Info: Gets information on a file given a file id
    public void fileInfo(String fileID, FileInfoInterface fileInfoInterface){
        //Client
        OkHttpClient client = new OkHttpClient();

        //data to send
        JSONObject data = new JSONObject();
        try{
            data.put("id",fileID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Request Body
        RequestBody body = RequestBody.create(Shared.JSON,data.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.fileInfoURL)
                .addHeader("Authorization","Token " + TOKEN)
                .post(body)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                fileInfoInterface.result(false,fileID,"Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()){
                    //server unreachable
                    fileInfoInterface.result(false,fileID,"Server Unreachable");
                }

                //keep the result in a variable
                String result = response.body().string();


                switch (result){
                    case  "Does'nt seem like the json we need":
                        fileInfoInterface.result(false,fileID,"Invalid JSON");
                        break;
                    case "not found":
                        fileInfoInterface.result(false,fileID,"not found");
                        break;
                    case "denied":
                        fileInfoInterface.result(false,fileID,"denied");
                        break;
                    case "Not File":
                        fileInfoInterface.result(false,fileID,"Not File");
                        break;
                    default:
                        fileInfoInterface.result(true,fileID,result);
                }

            }
        });
    }

    // File Info: Gets information on a file given a file id
    public void listFolder(String folderID, ListFolderInterface listFolderInterface){
        //Client
        OkHttpClient client = new OkHttpClient();

        //data to send
        JSONObject data = new JSONObject();
        try{
            data.put("folderID",folderID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Request Body
        RequestBody body = RequestBody.create(Shared.JSON,data.toString());

        // Request
        Request request = new Request.Builder()
                .url(Shared.getFolderURL)
                .addHeader("Authorization","Token " + TOKEN)
                .post(body)
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listFolderInterface.result(false,folderID,"Network Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If the upload was not successful
                if (!response.isSuccessful()){

                    //server unreachable
                    listFolderInterface.result(false,folderID,"Server Unreachable");

                }

                //keep the result in a variable
                String result = response.body().string();


                switch (result){
                    case  "doesn't seem like json data":
                        listFolderInterface.result(false,folderID,"Invalid JSON");
                        break;
                    case "Folder Not Found":
                        listFolderInterface.result(false,folderID,"not found");
                        break;
                    case "denied":
                        listFolderInterface.result(false,folderID,"denied");
                        break;
                    default:
                        listFolderInterface.result(true,folderID,result);
                }

            }
        });
    }


    // Create folder interface
    public interface CreateFolderInterface {
        void result(Boolean wasSuccessful,String folderName,String folderParentID, String result);
    }

    // Upload file interface
    public interface UploadFileInterface {
        void result(Boolean wasSuccessful, String uploadID,String result);
    }

    // Delete index object interface
    public interface DeleteIndexObjectInterface {
        void result(Boolean wasSuccessful, String id, String result);
    }

    // Keys interface
    public interface KeysInterface {
        void result(Boolean wasSuccessful, String clientAccount, String fileID, String result);
    }

    // File info interface
    public interface FileInfoInterface {
        void result(Boolean wasSuccessful, String fileID, String result);
    }

    // List folder interface
    public interface ListFolderInterface {
        void result(Boolean wasSuccessful, String folderID, String result);
    }
}
