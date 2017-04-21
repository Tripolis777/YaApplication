package com.example.vkaryagin.yaapplication.Core;

/**
 * Created by v.karyagin on 29.03.2017.
 */

public interface Initiable {

    void init(String jsonObject);
    /**
     * This method allows get response code for further check it
     * @return Object's response code
     */
    YaResponseCodes.YaResponse getResponse();
}
