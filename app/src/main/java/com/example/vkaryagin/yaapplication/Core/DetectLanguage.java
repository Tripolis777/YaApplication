package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tripo on 3/28/2017.
 */

public class DetectLanguage implements Initiable {

    private String langCode;
    private YaResponseCodes.YaResponse response;

    public DetectLanguage() {
        this.langCode = new String();
    }

    public void init(String jsonString) {

        /** Response Example
         * {
            "code": 200,
            "lang": "en"
         }
         */

        try {
            JSONObject res = new JSONObject(jsonString);
            int responseCode = res.getInt("code");
            response = new YaResponseCodes.YaResponse(responseCode);
            if (!YaResponseCodes.isSuccess(responseCode)) return;

            this.langCode = res.getString("lang");

        } catch (JSONException e) {
            Log.println(Log.ERROR, "JSON", "Detect Language: json cant parse.");
            e.printStackTrace();
        }
    }

    public String getLang() { return this.langCode; }

    @Override
    public YaResponseCodes.YaResponse getResponse() { return response; }
}
