package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.vkaryagin.yaapplication.Core.Language;
import com.example.vkaryagin.yaapplication.Core.Tasks.GetLanguagesTask;
import com.example.vkaryagin.yaapplication.Core.Translator;
import com.example.vkaryagin.yaapplication.Core.WithLanguageSpinner;
import com.example.vkaryagin.yaapplication.R;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Вывод перевода
//TODO: Автовыбор языка приложения (системы?) для вводы переводимого текста
//TODO: Английский - как дефолтный язык перевода
//TODO: Cache
//TODO: Refactoring

public class TranslateFragment extends Fragment implements WithLanguageSpinner {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText translateText;
    private ListView translateList;
    private Button translateButton;                          //TODO: Можем ли мы обойтись без хлеба?
    private Spinner toLanguageSpinner;
    private Spinner fromLanguageSpinner;

    private Context context;
    private Translator translator;

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

        translator = new Translator(context);

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        translateButton.setOnClickListener(new OnClickTranslateButtonListener());

        new GetLanguagesTask(this).execute(translator.getLanguagesLink());

        return rootView;
    }

    @Override
    public void initSpinnersAdapter(ArrayList<Language> values) {
        ArrayAdapter<Language> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toLanguageSpinner.setAdapter(spinnerAdapter);
        fromLanguageSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void setInputLanguage(int langNumber) {
        fromLanguageSpinner.setSelection(langNumber);
    }

    @Override
    public void setInputLanguage(String langCode) {
        //TODO: Create
    }

    //TODO: Возможно стоит это вынести
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
        // TODO: Этому классу тут явно не место. (Висит тут, чтобы не забыть истинное предназначение листенера)
        private class TranslateTask extends AsyncTask<URL, Integer, String> {

            @Override
            protected String doInBackground(URL... urls) {
                return null;
            }
        }
    }
}
