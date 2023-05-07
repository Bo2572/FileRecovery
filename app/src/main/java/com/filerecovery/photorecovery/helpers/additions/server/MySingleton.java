package com.filerecovery.photorecovery.helpers.additions.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.filerecovery.photorecovery.ui.App;

public class MySingleton {
    public static final String TAG = "mutee";
    private static Context mCtx;
    private static MySingleton mInstance;
    private ImageLoader mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
        private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

        public void putBitmap(String str, Bitmap bitmap) {
            this.mCache.put(str, bitmap);
        }

        public Bitmap getBitmap(String str) {
            return this.mCache.get(str);
        }
    });
    private RequestQueue mRequestQueue = getRequestQueue();

    private MySingleton(Context context) {
        mCtx = context;
    }

    public static synchronized MySingleton getInstance(Context context) {
        MySingleton mySingleton;
        synchronized (MySingleton.class) {
            if (mInstance == null) {
                mInstance = new MySingleton(context);
            }
            mySingleton = mInstance;
        }
        return mySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(App.mContext);
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request, String str) {
        if (TextUtils.isEmpty(str)) {
            str = TAG;
        }
        request.setTag(str);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object obj) {
        RequestQueue requestQueue = this.mRequestQueue;
        if (requestQueue != null) {
            requestQueue.cancelAll(obj);
        }
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }
}
