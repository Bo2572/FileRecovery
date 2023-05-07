package com.filerecovery.photorecovery.model;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.filerecovery.photorecovery.conf.MyProperties;
import com.filerecovery.photorecovery.helpers.additions.server.CustomRequest;
import com.filerecovery.photorecovery.helpers.additions.server.MySingleton;
import com.filerecovery.photorecovery.helpers.additions.server.Promise;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralModel {
    Context ctx;

    public GeneralModel(Context context) {
        this.ctx = context;
    }

    public Promise CallAPI(String str, HashMap hashMap) {
        Promise promise = new Promise();
        Log.i("CallAPI", str);
        hashMap.put("AppVersion", MyProperties.getInstance().AppVersion);
        hashMap.put("Store", MyProperties.getInstance().Store);
        CustomRequest customRequest = new CustomRequest(1, MyProperties.getInstance().BaseURL + str, hashMap, createRequestSuccessListener(promise), createRequestErrorListener(promise, str));
        customRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(30), 1, 1.0f));
        MySingleton.getInstance(this.ctx).addToRequestQueue(customRequest);
        return promise;
    }

    private Response.Listener<JSONObject> createRequestSuccessListener(final Promise promise) {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                JSONObject jSONObject2 = new JSONObject();
                try {
                    if (jSONObject.optJSONObject("MSGs") != null) {
                        jSONObject2.put("Data", jSONObject.getJSONObject("MSGs"));
                    } else {
                        jSONObject2.put("Data", new JSONObject());
                    }
                    if (jSONObject.getString("Status").equals("1")) {
                        jSONObject2.put("S", "1");
                    } else {
                        jSONObject2.put("S", "0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    promise.reject(e);
                }
                promise.resolve(jSONObject2);
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener(final Promise promise, final String str) {
        return new Response.ErrorListener() {


            public void onErrorResponse(com.android.volley.VolleyError r4) {

                throw new UnsupportedOperationException("Method not decompiled: com.narmx.photosrecovery.model.GeneralModel.AnonymousClass2.onErrorResponse(com.android.volley.VolleyError):void");
            }
        };
    }
}
