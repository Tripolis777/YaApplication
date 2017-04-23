package com.example.vkaryagin.yaapplication.Core;

/**
 * You need call {@link #init(String)} method with json object string that initialize object.
 */
public interface Initiable {

    /**
     * Allow initialize object fields from JSON
     *
     * @param jsonObject JSON string as JSON Object
     */
    void init(String jsonObject);

    /**
     * This method allows get response object for further check response code and
     * give code description if it's need.
     *
     * @return {@link com.example.vkaryagin.yaapplication.Core.YaResponseCodes.YaResponse} object.
     * If response object doesn't exists it return null.
     */
    YaResponseCodes.YaResponse getResponse();
}
