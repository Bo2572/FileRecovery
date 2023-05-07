package com.filerecovery.photorecovery.helpers.additions.server;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomRequest extends Request<JSONObject> {
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String str, Map<String, String> map, Response.Listener<JSONObject> listener2, Response.ErrorListener errorListener) {
        super(0, str, errorListener);
        this.listener = listener2;
        this.params = map;
    }

    public CustomRequest(int i, String str, Map<String, String> map, Response.Listener<JSONObject> listener2, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.listener = listener2;
        this.params = map;
    }


    public Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }


    public Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers))), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            if (!(networkResponse == null || networkResponse.data == null)) {
                String str = new String(networkResponse.data);
                PrintStream printStream = System.out;
                printStream.println("jsonError " + str);
            }
            return Response.error(new ParseError((Throwable) e));
        } catch (JSONException e2) {
            if (!(networkResponse == null || networkResponse.data == null)) {
                String str2 = new String(networkResponse.data);
                PrintStream printStream2 = System.out;
                printStream2.println("jsonError " + str2);
            }
            return Response.error(new ParseError((Throwable) e2));
        }
    }


    public void deliverResponse(JSONObject jSONObject) {
        this.listener.onResponse(jSONObject);
    }
}
