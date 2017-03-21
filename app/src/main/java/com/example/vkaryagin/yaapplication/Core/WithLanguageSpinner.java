package com.example.vkaryagin.yaapplication.Core;

import java.util.ArrayList;

/**
 * Created by v.karyagin on 3/21/17.
 */

public interface WithLanguageSpinner {

    void initSpinnersAdapter(ArrayList<Language> values);
    void setInputLanguage(int langNumber);
    void setInputLanguage(String langCode);
}
