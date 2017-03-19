package com.example.vkaryagin.yaapplication.Core;

/**
 * Created by tripo on 3/19/2017.
 */

public class Language {

    private String languageCode;
    private String languageName;

    public Language(String code, String name) {
        this.languageCode = code;
        this.languageName = name;
    }

    public String getLanguageCode() { return languageCode; }
    public String getLanguageName() { return languageName; }

    @Override
    public String toString() {
        return languageName;
    }

}
