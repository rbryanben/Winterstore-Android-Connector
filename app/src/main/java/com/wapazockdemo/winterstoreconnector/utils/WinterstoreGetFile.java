package com.wapazockdemo.winterstoreconnector.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WinterstoreGetFile {

    // Variables
    private String TOKEN, FILE_ID;
    private File FILE_CONTAINER;
    private GetFileInterface GET_FILE_INTERFACE;

    // Constructor
    public WinterstoreGetFile(String TOKEN,String FILE_ID, File fileContainer, GetFileInterface getFileInterface) {
        this.TOKEN = TOKEN;
        this.FILE_ID = FILE_ID;
        this.GET_FILE_INTERFACE = getFileInterface;
        this.FILE_CONTAINER = fileContainer;

        // Get the file
        getFile(FILE_ID,FILE_CONTAINER);
    }

    // get file by id
    // returns the bytes of that file
    public void getFile(String id, File file){
        // client
        OkHttpClient client = new OkHttpClient();

        // request
        Request request = new Request.Builder()
                .url(compileDownloadURL(id))
                .addHeader("Authorization","Token " + TOKEN)
                .build();

        // request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               GET_FILE_INTERFACE.result(false,null,"Connection Error",FILE_ID);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if response is not successful, server unreachable
                if (!response.isSuccessful()) {
                    GET_FILE_INTERFACE.result(false,null,"Server Unreachable",FILE_ID);
                }

                //keep bytes
                byte[] result = response.body().bytes();

                // stream
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(result);
                    stream.close();
                    GET_FILE_INTERFACE.result(true,file,"Successful",FILE_ID);
                } catch (FileNotFoundException e) {
                    GET_FILE_INTERFACE.result(false,null,"File Not Found",FILE_ID);
                } catch (IOException e) {
                    GET_FILE_INTERFACE.result(false,null, "Failed To Write File",FILE_ID);
                }

            }
        });
    }

    // Get URL - Given a file id, returns the final URL for the file
    private String compileDownloadURL(String id){
        return  Shared.downloadURL + id ;
    }

    // Get File Interface
    public interface GetFileInterface{
        void result(Boolean wasSuccessful, File file, String result,String given_file_id);
    }

}
