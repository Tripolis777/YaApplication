package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.example.vkaryagin.yaapplication.Views.TranslateListAdapter;

/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Cache
//TODO: Refactoring
//TODO: перенести все ошибки в константы  и сделать обработчик по response code

public class TranslateFragment extends BaseFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String NAME = "favorite_fragment";

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
    private TranslateListAdapter translatedAdapter;
    private boolean autoDetect;

    private Context context;
    private Bundle state;

    public TranslateFragment() {
    }

    /**
     * Create a new instance of a TranslateFragment with the given data state.
     *
     * @param state Bundle of arguments to supply to the fragment, which it
     * can retrieve with {@link #setSavedState(Bundle)} ()}.  May be null.
     * @return Returns a new fragment instance.
     */
    public static TranslateFragment newInstance(Bundle state) {
        TranslateFragment fragment = new TranslateFragment();
        fragment.setSavedState(state);
        Log.e("TraslateFragment", "Instance new!");
        return fragment;
    }

    /**
     * Set savedState this object. Like {@link #setArguments(Bundle)}
     *
     * @param state Bundle of data, that set to object's fields
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    public void setSavedState(Bundle state) {
        this.state = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        context = this.getContext();
        Log.e("TranslateFragment", "onCreateView");
        YaTranslateManager translateManager = YaTranslateManager.getInstance();

        languagesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        translatedAdapter = new TranslateListAdapter(context);

        autoDetect = false;

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        favoriteButton = (ImageButton) rootView.findViewById(R.id.favoroteButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        if (state != null && !state.isEmpty()) {
            translateText.setText(state.getString(ARG_TRANSLATE_TEXT));
            Log.d("TranslateFragment", "[onCreate] Bundle state. Translated_text" + state.getSerializable(ARG_TRANSLATED_TEXT).toString());
            translatedAdapter.setTranslate((Translate) state.getSerializable(ARG_TRANSLATED_TEXT));
            translatedAdapter.notifyDataSetChanged();
        } else {
            Log.w("TranslateFragment", "[onCreate] savedInstanceState is nullable.");
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
                Log.d("TranslateFragment", "[OnItemSelected] " + String.format("i: %d, l: %d", i, l));
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
                Log.e("TranslateFragment", "[LANGUAGES] Cants get languages. Res: " + res.toString());
                Toast.makeText(context, "Cant get languages.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.w("SaveInstanceState", "Put into ourState data!");
//        state.putString(ARG_TRANSLATE_TEXT, translateText.getText().toString());
//        state.putString(ARG_TRANSLATE_LANG,((Languages.Language)
//                fromLanguageSpinner.getSelectedItem()).getLanguageCode());
//        state.putString(ARG_TRANSLATED_LANG,((Languages.Language)
//                toLanguageSpinner.getSelectedItem()).getLanguageCode());
//        //state.putStringArray(ARG_TRANSLATED_TEXT, getArguments().getStringArray(ARG_TRANSLATED_TEXT));
//    }

    /**
     * Initialize {@link #toLanguageSpinner} and {@link #fromLanguageSpinner} spinners
     * by all available languages
     *
     * @param langs Object {@linkplain Languages} that contains {@linkplain java.util.ArrayList} of
     * {@linkplain com.example.vkaryagin.yaapplication.Core.Languages.Language} objects
     */
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

    /**
     * Set language for {@link #fromLanguageSpinner}
     *
     * @param langNumber Language's index in init languages list
     */
    public void setInputLanguage(int langNumber) {
        fromLanguageSpinner.setSelection(langNumber);
    }

    /**
     * Set language for {@link #toLanguageSpinner}
     *
     * @param langNumber Language's index in init languages list
     */
    public void setOutputLanguage(int langNumber) {
        toLanguageSpinner.setSelection(langNumber);
    }

    @Override
    public void checkMessageQueue() {

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
                        Log.e("TranslateFragment", "[DETECT] Cant connect to detect language. " + res.toString());
                        Toast.makeText(context, "Cant connect to detect language.", Toast.LENGTH_SHORT).show();

                    }
                    });
            }
        }

        @Override
        public void afterTextChanged(final Editable editable) {}
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
                            translatedAdapter.setTranslate(value);
                            translatedAdapter.notifyDataSetChanged();

                            state.putSerializable(ARG_TRANSLATED_TEXT, value);
                        }

                        @Override
                        public void error(final YaTranslateTask.Response res) {
                            Log.e("TranslateFragment",  "[TRANSLATE] Cant translate text. " + res.toString());
                           Toast.makeText(context, "Cant translate text.", Toast.LENGTH_SHORT).show();
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

            TranslateListAdapter adapter = (TranslateListAdapter) translateList.getAdapter();
            if (adapter == null || adapter.isEmpty()) { error("Adapter didn't init or empty!"); return; }
            final Translate translate = adapter.getTranslate();
            String translatedText = translate.getTranslatedTexts().get(0);

            if (languagesAdapter.isEmpty()) return;
            Languages.Language translateLang = (Languages.Language)
                    fromLanguageSpinner.getSelectedItem();
            Languages.Language translatedLang = (Languages.Language)
                    toLanguageSpinner.getSelectedItem();
            if (translateLang == null || translatedLang == null) { error("Language is not initialize!"); return;}

            FavoriteTranslate favoriteTranslate = new FavoriteTranslate(TranslateFragment.this.getContext());
            favoriteTranslate.insert(translateText, translatedText, translateLang.getLanguageName(),
                    translatedLang.getLanguageName(), translateLang.getLanguageCode(), translatedLang.getLanguageCode());
            // TODO: translateText may be not equal translated
            sendFavoriteTranslateMsg(translateText, translatedText, translateLang, translatedLang);
        }

        private void error (String errorMsg) {
            Log.e("TranslateFragment",  "[FAVORITE BUTTON] error: " + errorMsg);
            Toast.makeText(TranslateFragment.this.getContext(), errorMsg, Toast.LENGTH_LONG).show();
        }

        private void sendFavoriteTranslateMsg(String translateText, String translatedText,
                                        Languages.Language translateLang, Languages.Language translatedLang) {
            Bundle msg = new Bundle();
            msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATE_TEXT, translateText);
            msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATED_TEXT, translatedText);
            msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATE_LANG, translateLang.getLanguageName());
            msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATED_LANG, translatedLang.getLanguageName());

            Bundle data = new Bundle();
            data.putBundle(FavoriteFragment.COMMUNICATE_FAVORITE_KEY, msg);
            FragmentsCommutator.getInstance().addData(FavoriteFragment.TAG, data);
        }
    }
}
