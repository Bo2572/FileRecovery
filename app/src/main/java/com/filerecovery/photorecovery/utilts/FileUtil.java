package com.filerecovery.photorecovery.utilts;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;


public final class FileUtil {
    public static final String MIME_TYPE_JSON = "application/json";

    public static String getMimeType(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            return context.getContentResolver().getType(uri);
        }
        String extension = getExtension(uri.toString());
        if (extension == null) {
            return null;
        }
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        return mimeTypeFromExtension == null ? handleMiscFileExtensions(extension) : mimeTypeFromExtension;
    }

    private static String getExtension(String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            char[] charArray = str.toCharArray();
            for (int length = charArray.length - 1; length > 0; length--) {
                if (charArray[length] == '.') {
                    return str.substring(length + 1, str.length());
                }
            }
        }
        return null;
    }

    private static String handleMiscFileExtensions(String str) {
        if (str.equals("json")) {
            return "application/json";
        }
        return null;
    }
}
