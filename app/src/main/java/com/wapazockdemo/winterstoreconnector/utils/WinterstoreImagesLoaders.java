package com.wapazockdemo.winterstoreconnector.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class WinterstoreImagesLoaders {

    private String TOKEN;
    private Context CONTEXT;


    // Constructor
    public WinterstoreImagesLoaders(String TOKEN, Context CONTEXT) {
        this.TOKEN = TOKEN;
        this.CONTEXT = CONTEXT;
    }

    // Load Image: Given an object to display the image, assigns the given
    // imageID as the image received from the server
    public void loadImage(ImageView imageView, String imageID, ImageLoaderInterface imageLoaderInterface){
        // Custom Headers for Glide Request
        GlideUrl url = new GlideUrl(compileDownloadURL(imageID), new LazyHeaders.Builder()
                .addHeader("Authorization", "Token " + TOKEN)
                .build());

        // Load the image
        Glide.with(CONTEXT)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageLoaderInterface.result(false,imageID);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaderInterface.result(true,imageID);
                        return false;
                    }
                })
                .into(imageView);
    }

    // Load Image: Given an object to display the image, assigns the given
    // imageID as the image received from the server
    public void loadImage(ImageView imageView, String imageID, int placeholder, ImageLoaderInterface imageLoaderInterface){
        // Custom Headers for Glide Request
        GlideUrl url = new GlideUrl(compileDownloadURL(imageID), new LazyHeaders.Builder()
                .addHeader("Authorization", "Token " + TOKEN)
                .build());

        // Load the image
        Glide.with(CONTEXT)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageLoaderInterface.result(false,imageID);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaderInterface.result(true,imageID);
                        return false;
                    }
                })
                .into(imageView);
    }



    // Load Image: Given an object to display the image, assigns the given
    // imageID as the image received from the server, and caches it
    public void loadImageAndCache(ImageView imageView, String imageID, ImageLoaderInterface imageLoaderInterface){
        // Custom Headers for Glide Request
        GlideUrl url = new GlideUrl(compileDownloadURL(imageID), new LazyHeaders.Builder()
                .addHeader("Authorization", "Token " + TOKEN)
                .build());

        // Load the image
        Glide.with(CONTEXT)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageLoaderInterface.result(false,imageID);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaderInterface.result(true,imageID);
                        return false;
                    }
                })
                .into(imageView);
    }

    // Load Image: Given an object to display the image, assigns the given
    // imageID as the image received from the server, and caches it
    public void loadImageAndCache(ImageView imageView, String imageID, int placeHolderImage, ImageLoaderInterface imageLoaderInterface){
        // Custom Headers for Glide Request
        GlideUrl url = new GlideUrl(compileDownloadURL(imageID), new LazyHeaders.Builder()
                .addHeader("Authorization", "Token " + TOKEN)
                .build());

        // Load the image
        Glide.with(CONTEXT)
                .load(url)
                .placeholder(placeHolderImage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageLoaderInterface.result(false,imageID);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaderInterface.result(true,imageID);
                        return false;
                    }
                })
                .into(imageView);
    }


    // Get URL - Given a file id, returns the final URL for the file
    private String compileDownloadURL(String id){
        return  Shared.downloadURL + id ;
    }

    // load image interface
    public interface ImageLoaderInterface {
        void result(Boolean wasSuccessful,String imageID);
    }

}
