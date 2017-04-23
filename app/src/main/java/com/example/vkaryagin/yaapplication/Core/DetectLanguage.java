package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is response object for Detect Language method from Yandex Translate API.
 * You need call {@link #init(String)} method with json object string that initialize his.
 */
public class DetectLanguage implements Initiable {

    private String langCode;
    private YaResponseCodes.YaResponse response;

    public DetectLanguage() {
        this.langCode = new String();
    }

    /**
     * {@inheritDoc}
     * <p> JSON Example
     * {
            "code": 200,
            "lang": "en"
        }
     */
    public void init(String jsonString) {
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

    /**
     * Allows get language code as code from {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} object.
     * If something went wrong ans response code was not equal {@linkplain YaResponseCodes#SUCCESS} language code will be empty.
     *
     * @return {@link String} language code or empty string
     * @see com.example.vkaryagin.yaapplication.Core.Languages.Language
     */
    public String getLang() { return this.langCode; }


    @Override
    public YaResponseCodes.YaResponse getResponse() { return response; }
}
