package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.DetectLanguage;
import com.example.vkaryagin.yaapplication.Core.Languages;
import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.Core.YaTranslateManager;
import com.example.vkaryagin.yaapplication.Core.YaTranslateTask;
import com.example.vkaryagin.yaapplication.Database.FavoriteTranslate;
import com.example.vkaryagin.yaapplication.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Cache
//TODO: Refactoring

public class TranslateFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_TRANSLATE_TEXT = "translate_text";
    private static final String ARG_TRANSLATE_LANG = "translate_lang";
    private static final String ARG_TRANSLATED_LANG = "translated_lang";
    private static final String ARG_TRANSLATED_TEXT = "translated_text";

    private EditText translateText;
    private ListView translateList;
    private Button translateButton;
    private ImageButton favoriteButton;
    private Spinner toLanguageSpinner;
    private Spinner fromLanguageSpinner;

    private ArrayAdapter<Languages.Language> languagesAdapter;
    private ArrayAdapter<String> translatedAdapter;
    private boolean autoDetect;

    private Context context;

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
        Log.d("Fragments", "newInstance");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        context = this.getContext();

        YaTranslateManager translateManager = YaTranslateManager.getInstance();

        languagesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        translatedAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);

        autoDetect = false;

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        favoriteButton = (ImageButton) rootView.findViewById(R.id.favoroteButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        if (savedInstanceState != null) {
            translateText.setText(savedInstanceState.getString(ARG_TRANSLATE_TEXT));
            Log.d("onCreate", "Bundle state. Translated_tetx" + savedInstanceState.getStringArray(ARG_TRANSLATED_TEXT).toString());
            translatedAdapter.addAll(savedInstanceState.getStringArray(ARG_TRANSLATED_TEXT));
            translatedAdapter.notifyDataSetChanged();
        } else {
            Log.w("onCreate", "savedInstanceState is nullable.");
        }

        toLanguageSpinner.setAdapter(languagesAdapter);
        fromLanguageSpinner.setAdapter(languagesAdapter);
        translateList.setAdapter(translatedAdapter);

        translateButton.setOnClickListener(new OnClickTranslateButtonListener());
        translateText.addTextChangedListener(new OnChangeTranslateText());

        favoriteButton.setOnClickListener(new OnClickFavoriteButtonListener());
        
        fromLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("OnItemSelected", String.format("i: %d, l: %d", i, l));
                autoDetect = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        translateManager.executeLanguages(context, new Callable<Languages>() {
            @Override
            public void done(Languages value) {
                TranslateFragment.this.initSpinnersAdapter(value);
                TranslateFragment.this.setInputLanguage(value.getLanguageNumber(
                        context.getResources().getConfiguration().locale.getLanguage()
                ));
                autoDetect = true;
            }

            @Override
            public void error(final YaTranslateTask.Response res) {
                Toast.makeText(context, "Cant connect to get languages. " + res.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TRANSLATE_TEXT, translateText.getText().toString());
        outState.putString(ARG_TRANSLATE_LANG,((Languages.Language)
                fromLanguageSpinner.getSelectedItem()).getLanguageCode());
        outState.putString(ARG_TRANSLATED_LANG,((Languages.Language)
                toLanguageSpinner.getSelectedItem()).getLanguageCode());
        outState.putStringArray(ARG_TRANSLATED_TEXT, getArguments().getStringArray(ARG_TRANSLATED_TEXT));
    }

    public void initSpinnersAdapter(Languages langs) {
        if (!languagesAdapter.isEmpty()) languagesAdapter.clear();
        languagesAdapter.addAll(langs.getLanguages());
        languagesAdapter.notifyDataSetChanged();

        // Restore data if exists
        Bundle args = getArguments();
        if (args != null) {
            String translateLang = args.getString(ARG_TRANSLATE_LANG);
            if (translateLang != null && !translateLang.isEmpty())
                fromLanguageSpinner.setSelection(langs.getLanguageNumber(translateLang));
            String translatedLang = args.getString(ARG_TRANSLATED_LANG);
            if (translatedLang != null && !translatedLang.isEmpty())
                toLanguageSpinner.setSelection(langs.getLanguageNumber(translatedLang));
        }
    }

    public void setInputLanguage(int langNumber) {
        fromLanguageSpinner.setSelection(langNumber);
    }
    public void setOutputLanguage(int langNumber) {
        toLanguageSpinner.setSelection(langNumber);
    }

    private class OnChangeTranslateText implements TextWatcher {

        public OnChangeTranslateText() {}

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (i1 == 0 && i != 0 && autoDetect) {
                final YaTranslateManager yaTranslateManager = YaTranslateManager.getInstance();
                yaTranslateManager.executeDetect(charSequence.toString(), context,
                        new Callable<DetectLanguage>() {
                    @Override
                    public void done(DetectLanguage value) {
                        TranslateFragment.this.setInputLanguage(
                                yaTranslateManager.getLanguages().getLanguageNumber(
                                        value.getLang()
                                ));
                        TranslateFragment.this.setOutputLanguage(
                                yaTranslateManager.getLanguages().getLanguageNumber(
                                        context.getResources().getConfiguration().locale.getLanguage()
                                )
                        );
                        autoDetect = true;
                    }

                    @Override
                    public void error(final YaTranslateTask.Response res) {
                        Toast.makeText(context, "Cont connect to detect language. " + res.toString(), Toast.LENGTH_SHORT).show();
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
                        public void done(Translate value) {
                            if (value.checkResponseCode()) return;

                            translatedAdapter.clear();
                            translatedAdapter.addAll(value.getTranslatedText());
                            translatedAdapter.notifyDataSetChanged();

                            getArguments().putStringArrayList(ARG_TRANSLATED_TEXT,
                                    (ArrayList<String>) value.getTranslatedText());
                        }

                        @Override
                        public void error(final YaTranslateTask.Response res) {
                            Log.e("TRANSLATE", "Cant translate text. " + res.toString());
                            Toast.makeText(context, "Cant translate text. " + res.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private class OnClickFavoriteButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String translateText = TranslateFragment.this.translateText.getText().toString();
            if (translateText.isEmpty()) { error("Translate text is empty!"); return; }

            ListAdapter adapter = translateList.getAdapter();
            if (adapter == null || adapter.isEmpty()) { error("Adapter didn't init or empty!"); return; }
            String translatedText = (String) adapter.getItem(0);

            if (languagesAdapter.isEmpty()) return;
            Languages.Language translateLang = (Languages.Language)
                    fromLanguageSpinner.getSelectedItem();
            Languages.Language translatedLang = (Languages.Language)
                    toLanguageSpinner.getSelectedItem();
            if (translateLang == null || translatedLang == null) { error("Language is not initialize!"); return;}

            FavoriteTranslate favoriteTranslate = new FavoriteTranslate(TranslateFragment.this.getContext());
            favoriteTranslate.insert(translateText, translatedText, translateLang.getLanguageName(),
                    translatedLang.getLanguageName(), translateLang.getLanguageCode(), translatedLang.getLanguageCode());
        }

        private void error (String errorMsg) {
            //if (!BuildConfig.DEBUG) return;
            Log.e("FAVORITE BUTTON", errorMsg);
            Toast.makeText(TranslateFragment.this.getContext(), errorMsg, Toast.LENGTH_LONG).show();
        }
    }
}
