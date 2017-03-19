package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.vkaryagin.yaapplication.Core.Language;
import com.example.vkaryagin.yaapplication.Core.Translator;
import com.example.vkaryagin.yaapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tripo on 3/19/2017.
 */

public class TranslateFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText translateText;
    private ListView translateList;
    private Button translateButton;
    private Spinner toLanguageSpinner;
    private Spinner fromLanguageSpinner;

    private Context context;
  //  private ArrayAdapter<String> spinnerAdapter;

    public TranslateFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TranslateFragment newInstance(int sectionNumber) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        context = this.getContext();

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        //Translator.getInstance().setLanguages(languagesSpinner);
        translateButton.setOnClickListener(new OnClickTranslateButtonListener());

        new GetLanguagesTask(this).execute(Translator.getLanguagesLink());

        return rootView;
    }

    private void initSpinnersAdapter(ArrayList<Language> values) {
        ArrayAdapter<Language> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toLanguageSpinner.setAdapter(spinnerAdapter);
        fromLanguageSpinner.setAdapter(spinnerAdapter);

        // TODO: Delete Magic
        fromLanguageSpinner.setSelection(66);
    }

    private class GetLanguagesTask extends  AsyncTask<String, Integer, ArrayList<Language>> {

        private TranslateFragment fragment;

        public GetLanguagesTask(TranslateFragment fragment) { super(); this.fragment = fragment; }

        @Override
        protected ArrayList<Language> doInBackground(String... strings) {
            ArrayList<Language> result = new ArrayList<>();
            for (int i = 0; i < strings.length; i++) {
                String link = strings[i];

                try {
                    URL url = new URL(link);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                    Log.println(Log.INFO, "CONNECTION", String.format("Response code: %d", connection.getResponseCode()));

                    if(connection.getResponseCode() != 200) return result;

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    while ((line = br.readLine()) != null){
                        stringBuilder.append(line);
                    }
                    br.close();

                    String response = stringBuilder.toString();
                    JSONObject res = new JSONObject(response);
                    JSONObject langs = res.getJSONObject("langs");

                    for (Iterator<String> it = langs.keys(); it.hasNext(); ) {
                        String langCode = it.next();
                        String lang = langs.getString(langCode);
                        Log.println(Log.DEBUG, "LANGUAGE", String.format("Lang code: %s, Lang Desc: %s", langCode, lang));

                        result.add(new Language(langCode, lang));
                    }

                } catch (MalformedURLException e) {
                    Log.println(Log.ERROR, "URL", e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.println(Log.ERROR, "IO", e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.println(Log.ERROR, "JSON", e.getMessage());
                    e.printStackTrace();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Language> result) {
            fragment.initSpinnersAdapter(result);
        }
    }

    /**
     * Create button behavior class
     */
    private class OnClickTranslateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }

        /**
         * Async class for use Translation API (need create own API in future)
         */
        private class TranslateTask extends AsyncTask<URL, Integer, String> {

            @Override
            protected String doInBackground(URL... urls) {
                return null;
            }
        }
    }
}
