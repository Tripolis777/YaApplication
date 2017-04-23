package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is response object for Translate method from Yandex Translate API. It also cantain
 * parameters with which it was called.
 * You need call {@link #init(String)} method with json object string that initialize his.
 */
public class Translate implements Initiable, Serializable {

    private List<String> translatedTexts;
    private String language;
    private final Params requestParams;
    private String translatedJSONString;
    private YaResponseCodes.YaResponse response;

    public Translate(Params requestParams) {
        this.requestParams = requestParams;
        translatedTexts = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * <p> JSON Example
     {  "code": 200,
        "lang": "en-ru",
        "text": [
           "Здравствуй, Мир!"
         ]
     }</p>
     */
    public void init(String jsonString) {
        try {
            JSONObject res = new JSONObject(jsonString);
            int responseCode = res.getInt("code");
            response = new YaResponseCodes.YaResponse(responseCode);
            if (!YaResponseCodes.isSuccess(response)) return;

            JSONArray translateVariants = res.getJSONArray("text");
            translatedJSONString = translateVariants.toString();
            for (int i = 0; i < translateVariants.length(); i++)
                translatedTexts.add(translateVariants.getString(i));

            language = res.getString("lang");
        } catch(JSONException e) {
            Log.println(Log.ERROR, "JSON", "Translate, json cant parse.");
            e.printStackTrace();
        }
    }

    /**
     * Allows get {@link List} of translated texts that was taken.
     * @return {@link List} of strings
     */
    public List<String> getTranslatedTexts() { return translatedTexts; }

    /**
     * Allows get list of translated texts as JSON Array string.
     * @return {@link String} like JSON Array
     */
    public String getTranslatedJSONString() { return translatedJSONString; }

    /**
     * Allows get original text that was translate.
     * @return {@link String}
     */
    public String getTranslateText() { return requestParams.getText(); }

    /**
     * Allows get original text's language
     * @return {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} original language
     */
    public Languages.Language getTranslateLanguage() { return requestParams.getTranslateLang(); }

    /**
     * Allows get translated text's language
     * @return {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} language translations
     */
    public Languages.Language getTranslatedLanguage() { return requestParams.getTranslatedLang(); }

    /**
     * It's check that list of translated texts is empty. It's like {@link List#isEmpty()}
     * @return boolean value
     */
    public boolean isEmpty() { return translatedTexts.isEmpty(); }

    @Override
    public YaResponseCodes.YaResponse getResponse() { return response; }

    /**
     * This class allows set need parameters for translate request to Yandex Translate API. It
     * contains both general parameters and optional.
     */
    public static class Params {
        private String text;
        private String language;
        private String format;
        private String options;

        private Languages.Language translateLang;
        private Languages.Language translatedLang;

        /**
         * Constructor set general parameters as language and text this will be translated.
         * If you want sets optional parameters you need call {@link #setOptions(String)} and
         * {@link #setFormat(String)} methods
         * @param text {@link String} text that will be translated
         * @param translateLang {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} original language
         * @param translatedLang {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} language translations
         */
        public Params(String text, Languages.Language translateLang, Languages.Language translatedLang) {
            this.text = text;
            this._setLanguage(translateLang, translatedLang);
            this.format = new String();
            this.options = new String();
        }

        private void _setLanguage(Languages.Language translateLang, Languages.Language translatedLang) {
            this.language = translateLang.getLanguageCode() + "-" + translatedLang.getLanguageCode();
            this.translateLang = translateLang;
            this.translatedLang = translatedLang;
        }

        /**
         * Allows set text that will be translated.
         * @param text {@link String} text
         * @return this object
         */
        public Params setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * Allows set language translation. Note this you need give also original text language. This method
         * convert two languages into one string like "ru-en". You can get her through {@link #getLanguage()}
         * @param translateLang {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} original language
         * @param translatedLang {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} language translations
         * @return this object
         */
        public Params setLanguage(Languages.Language translateLang, Languages.Language translatedLang) {
            this._setLanguage(translateLang, translatedLang);
            return this;
        }

        /**
         * Allows set format for request to Yandex Translation API. Can take the following values:
         * <br> "plain" - text without markup. default value
         * <br> "html" - text in HTML format
         *
         * @param format {@link String} with format value
         * @return this object
         */
        public Params setFormat(String format) {
            this.format = format;
            return this;
        }

        /**
         * Allows set options for request to Yandex Translation API. Currently, the only option is
         * available - a sign of the inclusion in the answer of the automatically defined language
         * of the translated text. This corresponds to a value of 1 for this parameter.
         * @param options {@link String} of options
         * @return this object
         */
        public Params setOptions(String options) {
            this.options = options;
            return this;
        }

        /**
         * Allows get text that will be translated and requested to Yandex Translation API.
         * @return {@link String} of text
         */
        public String getText() { return this.text; }

        /**
         * Allows get converted language string like "ru-en" that will be requested to Yandex Translation API.
         * @return {@link String} converted language like "ru-en"
         */
        public String getLanguage() { return this.language; }

        /**
         * Allows get format for request to Yandex Translation API
         * @return {@link String} of format
         */
        public String getFormat() { return this.format; }

        /**
         * Allows get options for request to Ayndex Translation API.
         * @return {@link String} of options
         */
        public String getOptions() { return this.options; }

        /**
         * Allows get original text's language.
         * @return {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} original language
         */
        public Languages.Language getTranslateLang() { return this.translateLang; }

        /**
         * Allows get language translations
         * @return {@link com.example.vkaryagin.yaapplication.Core.Languages.Language} language translations
         */
        public Languages.Language getTranslatedLang() { return this.translatedLang; }
    }
}
