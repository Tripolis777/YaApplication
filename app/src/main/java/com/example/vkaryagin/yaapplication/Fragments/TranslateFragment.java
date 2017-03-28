package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.DetectLanguage;
import com.example.vkaryagin.yaapplication.Core.Languages;
import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.Core.YaTranslateManager;
import com.example.vkaryagin.yaapplication.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Вывод перевода
//TODO: Автовыбор языка приложения (системы?) для вводы переводимого текста
//TODO: Английский - как дефолтный язык перевода
//TODO: Cache
//TODO: Refactoring

public class TranslateFragment extends Fragment {

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
    //private YaTranslateManager translateManager;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        context = this.getContext();

        YaTranslateManager translateManager = YaTranslateManager.getInstance();

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        translateButton.setOnClickListener(new OnClickTranslateButtonListener());
        translateText.addTextChangedListener(new OnChangeTranslateText());

        fromLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("OnItemSelected", String.format("i: %d, l: %d", i, l));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        translateManager.executeLanguages(context, new Callable<Languages>() {
            @Override
            public void callback(Languages value) {
                TranslateFragment.this.initSpinnersAdapter(value);
                TranslateFragment.this.setInputLanguage(value.getLanguageNumber(
                        context.getResources().getConfiguration().locale.getLanguage()
                ));
            }
        });

        return rootView;
    }

    public void initSpinnersAdapter(Languages langs) {
        ArrayAdapter<Languages.Language> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, langs.getLanguages());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toLanguageSpinner.setAdapter(spinnerAdapter);
        fromLanguageSpinner.setAdapter(spinnerAdapter);
    }

    public void setInputLanguage(int langNumber) {
        fromLanguageSpinner.setSelection(langNumber);
    }
    public void setOutputLanguage(int langNumber) {
        toLanguageSpinner.setSelection(langNumber);
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private class OnChangeTranslateText implements TextWatcher {

        public OnChangeTranslateText() {}

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (i1 == 0 && i != 0) {
                final YaTranslateManager yaTranslateManager = YaTranslateManager.getInstance();
                yaTranslateManager.executeDetect(charSequence.toString(), context,
                        new Callable<DetectLanguage>() {
                    @Override
                    public void callback(DetectLanguage value) {
                        TranslateFragment.this.setInputLanguage(
                                yaTranslateManager.getLanguages().getLanguageNumber(
                                        value.getLang()
                                ));
                        TranslateFragment.this.setOutputLanguage(
                                yaTranslateManager.getLanguages().getLanguageNumber(
                                        context.getResources().getConfiguration().locale.getLanguage()
                                )
                        );
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(final Editable editable) {

        }
    }

    //TODO: Возможно стоит это вынести
    /**
     * Create button behavior class
     */
    private class OnClickTranslateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String text = translateText.getText().toString();

            if (text.isEmpty()) { return; }
            if (toLanguageSpinner.getAdapter().isEmpty() ||
                    fromLanguageSpinner.getAdapter().isEmpty()) {
                return;
            }

            Languages.Language langOut = (Languages.Language) toLanguageSpinner.getSelectedItem();
            Languages.Language langIn = (Languages.Language) fromLanguageSpinner.getSelectedItem();

            YaTranslateManager translateManager = YaTranslateManager.getInstance();
            translateManager.executeTranslate(
                    new Translate.Params(text, langIn.getLanguageCode(), langOut.getLanguageCode()),
                    context,
                    new Callable<Translate>() {
                        @Override
                        public void callback(Translate value) {

                            if (value.checkResponseCode()) return;

                            ArrayAdapter<String> translatedAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1, value.getTranslatedText());

                            translateList.setAdapter(translatedAdapter);
                        }
                    }
            );
        }
    }
}
