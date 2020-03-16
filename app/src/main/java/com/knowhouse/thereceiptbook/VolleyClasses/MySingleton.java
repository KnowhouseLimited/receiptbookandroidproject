package com.knowhouse.thereceiptbook.VolleyClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context context;


    private MySingleton(Context context){
        this.context = context;

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
            private final LruCache<String,Bitmap>
                    cache = new LruCache<String, Bitmap>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url,bitmap);
                    }
                });
    }


    public static synchronized MySingleton getInstance(Context context){
        if(instance == null){
            instance = new MySingleton(context);
        }
        return  instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            //getApplicationContext() is key, it keeps you from leaking the
            //Activity or BroadcastReceiver if someone passes one in.

            Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache,network);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
