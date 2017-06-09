package com.example.vkaryagin.yaapplication.DatabaseTest.Schema;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.example.vkaryagin.yaapplication.Core.Languages.Language;
import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.Core.YaResponseCodes;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for {@link HistoryTranslateEntry}. 
 */

@RunWith(AndroidJUnit4.class)
public class HistoryTranslateEntryTest {

    private HistoryTranslateEntry entry;
    private static final long ENTRY_ID = Long.MAX_VALUE - 1;

    public static class HistoryTranslateEntryTestLanguageGetter {
        public static final String RU_LANG_CODE = "ru";
        public static final String RU_LANG_NAME = "Russian";
        public static final String EN_LANG_CODE = "en";
        public static final String EN_LANG_NAME = "English";


        @NonNull
        public static Language getRussianLanguage() {
            return new Language(RU_LANG_CODE, RU_LANG_NAME);
        }

        @NonNull
        public static Language getEnglishLanguage() {
            return new Language(EN_LANG_CODE, EN_LANG_NAME);
        }
    }

    private void checkTranslateLanguage(Language language) {
        assertEquals("failure - translate language code not equals set", entry.translateCode, language.getLanguageCode());
        assertEquals("failure - translate language name not equals set", entry.translateLang, language.getLanguageName());
    }

    private void checkTranslatedLanguage(Language language) {
        assertEquals("failure - translated language code not equals set", entry.translatedCode, language.getLanguageCode());
        assertEquals("failure - translated language name not equals set", entry.translatedLang, language.getLanguageName());
    }

    public static class HistoryTranslateEntryTestTranslateGetter {
        public static final String TRANSLATE_TEXT = "Some text";

        private static final Translate.Params params = new Translate.Params(
                TRANSLATE_TEXT,
                HistoryTranslateEntryTestLanguageGetter.getRussianLanguage(),
                HistoryTranslateEntryTestLanguageGetter.getEnglishLanguage()
        );

        public static Translate getRussian2EnglishTranslate() throws Exception {
            Translate translate = new Translate(params);
            translate.init(getRussian2EnglishTranslateInitString());
            return translate;
        }

        private static String getRussian2EnglishTranslateInitString() throws Exception {
            JSONObject json = new JSONObject();
            json.put("code", YaResponseCodes.SUCCESS);
            json.put("text", HistoryTranslateEntryTranslatedTextGetter.
                    getTranslatedTextsJsonArray(HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_ONE));
            return json.toString();
        }
    }

    public static class HistoryTranslateEntryTranslatedTextGetter {
        public static final String[] TRANSLATED_TEXT_ONE = {"Some translated text"};
        public static final String[] TRANSLATED_TEXT_MULTIPLE = {"Some first translated_text", "Some second translated text"};

        public static String getOneTranslatedText() throws Exception {
            return getTranslatedTexts(TRANSLATED_TEXT_ONE);
        }

        public static String getMultipleTranslatedTexts() throws Exception {
            return getTranslatedTexts(TRANSLATED_TEXT_MULTIPLE);
        }

        public static String getTranslatedTexts(String[] arrayOfTexts) throws Exception {
            JSONArray jsonArray = getTranslatedTextsJsonArray(arrayOfTexts);
            return jsonArray.toString();
        }

        @NonNull
        public static JSONArray getTranslatedTextsJsonArray(String[] arrayOfTexts) throws Exception {
            return new JSONArray(arrayOfTexts);
        }
    }

    private void checkTranslatedTexts(String[] texts) {
        ArrayList<String> expectedTexts = (ArrayList<String>) entry.getTranslatedTexts();
        assertEquals("failure - translated texts count not equal", texts.length, expectedTexts.size());
        for (int i = 0; i < texts.length; i++) {
            String expectedText = expectedTexts.get(i);
            String actualText = texts[i];
            assertEquals(String.format("failure - index of string : %d, expected: %s, actual: %s",
                    i, expectedText, actualText), expectedText, actualText);
        }
    }

    @Before
    public void setUp() throws Exception{
        entry = new HistoryTranslateEntry();
    }

    @Test
    public void testSetId() {
        entry.setId(ENTRY_ID);
        assertEquals("failure - setID(long) don`t setup id", entry.id, ENTRY_ID);
    }

    @Test
    public void testGetTableName() {
        assertEquals("failure - get wrong table name", entry.getTableName(), HistoryTranslateEntry.TABLE_NAME);
    }

    @Test
    public void testSetTranslateLanguage() {
        Language russianLang = HistoryTranslateEntryTestLanguageGetter.getRussianLanguage();
        entry.setTranslateLanguage(russianLang);
        this.checkTranslateLanguage(russianLang);
    }

    @Ignore
    @Test
    public void testGetTranslateLanguage() {
        Language language = HistoryTranslateEntryTestLanguageGetter.getRussianLanguage();
        // entry.getTranslateLanguage()
    }

    @Test
    public void testSetTranslatedLanguage() {
        Language englishLang = HistoryTranslateEntryTestLanguageGetter.getEnglishLanguage();
        entry.setTranslatedLanguage(englishLang);
        checkTranslatedLanguage(englishLang);
    }

    @Ignore
    @Test
    public void testGetTranslatedLanguage() {
        //entry.getTranslatedLanguage()
    }

    @Test
    public void testGetTranslatedTextsOne() throws Exception {
        entry.translatedText = HistoryTranslateEntryTranslatedTextGetter.getOneTranslatedText();
        checkTranslatedTexts(HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_ONE);
    }

    @Test
    public void testGetTranslatedTextMultiple() throws Exception {
        entry.translatedText = HistoryTranslateEntryTranslatedTextGetter.getMultipleTranslatedTexts();
        checkTranslatedTexts(HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_MULTIPLE);
    }

    @Test
    public void testGetTranslateFirstOne() throws Exception {
        entry.translatedText = HistoryTranslateEntryTranslatedTextGetter.getOneTranslatedText();
        String firstElement = HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_ONE[0];
        assertEquals("failure - get first element of array with one object", entry.getTranslateFirst(), firstElement);
    }

    @Test
    public void testGetTranslateFirstMultiple() throws Exception {
        entry.translatedText = HistoryTranslateEntryTranslatedTextGetter.getMultipleTranslatedTexts();
        String firstElement = HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_MULTIPLE[0];
        assertEquals("failure - get first element of array with one object", entry.getTranslateFirst(), firstElement);
    }

    @Test
    public void testSetTranslate() throws Exception {
        Translate translate = HistoryTranslateEntryTestTranslateGetter.getRussian2EnglishTranslate();
        entry.setTranslate(translate);
        checkTranslateLanguage(HistoryTranslateEntryTestLanguageGetter.getRussianLanguage());
        checkTranslatedLanguage(HistoryTranslateEntryTestLanguageGetter.getEnglishLanguage());
        checkTranslatedTexts(HistoryTranslateEntryTranslatedTextGetter.TRANSLATED_TEXT_ONE);
        assertEquals("failure - translate text not equal", entry.translateText, HistoryTranslateEntryTestTranslateGetter.TRANSLATE_TEXT);
    }

    @Ignore
    @Test
    public void testGetContentValue() {

    }
}
