package com.example.vkaryagin.yaapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.vkaryagin.yaapplication.ApplicationUtils;
import com.example.vkaryagin.yaapplication.Callable;
import com.example.vkaryagin.yaapplication.Core.DetectLanguage;
import com.example.vkaryagin.yaapplication.Core.Languages;
import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.Core.YaResponseCodes;
import com.example.vkaryagin.yaapplication.Core.YaTranslateManager;
import com.example.vkaryagin.yaapplication.Database.HistoryTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Response;
import com.example.vkaryagin.yaapplication.Adapters.TranslateListAdapter;

/**
 * General fragment that provides interface for translate text.
 */
public class TranslateFragment extends BaseFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TRANSLATED_TEXT = "translated_text";

    private EditText translateText;
    private ListView translateList;
    private Button translateButton;
    private ToggleButton favoriteButton;
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
    public static TranslateFragment newInstance(Bundle state, YaAppDBOpenHelper dbOpenHelper) {
        TranslateFragment fragment = new TranslateFragment();
        fragment.setSavedState(state);
        fragment.setDBOpenHelper(dbOpenHelper);
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
        YaTranslateManager translateManager = YaTranslateManager.getInstance();

        languagesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        translatedAdapter = new TranslateListAdapter(context);

        autoDetect = false;

        // Initialize
        translateText   = (EditText) rootView.findViewById(R.id.translateTextEdit);
        translateList   = (ListView) rootView.findViewById(R.id.translateList);
        translateButton = (Button) rootView.findViewById(R.id.translateButton);
        favoriteButton = (ToggleButton) rootView.findViewById(R.id.favoriteButton);
        toLanguageSpinner = (Spinner) rootView.findViewById(R.id.toLanguageSpinner);
        fromLanguageSpinner = (Spinner) rootView.findViewById(R.id.fromLanguageSpinner);

        ((TextView) rootView.findViewById(R.id.yaCreditsText)).setMovementMethod(LinkMovementMethod.getInstance());

        if (state != null && !state.isEmpty()) {
            HistoryTranslateEntry rec = (HistoryTranslateEntry) state.getSerializable(ARG_TRANSLATED_TEXT);

            if(rec != null) {
                translatedAdapter.setTranslateEntry(rec);
                translatedAdapter.notifyDataSetChanged();

                favoriteButton.setEnabled(true);
                favoriteButton.setChecked(rec.favorite);
            }
        } else {
            Log.w("TranslateFragment", "[onCreate] savedInstanceState is nullable.");
        }

        toLanguageSpinner.setAdapter(languagesAdapter);
        fromLanguageSpinner.setAdapter(languagesAdapter);
        translateList.setAdapter(translatedAdapter);

        translateButton.setOnClickListener(new OnClickTranslateButtonListener(context));
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
            public void error(final Response res) {
                Log.e("TranslateFragment", res.toString(context));
                ApplicationUtils.throwAlertDialog(context,
                        YaResponseCodes.getTitle(context), res.getMessage(context));
            }
        });

        return rootView;
    }

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

    private class OnChangeTranslateText implements TextWatcher {
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
                    public void error(final Response res) {
                        Log.e("TranslateFragment", "[DetectLanguage] " + res.toString());
                        ApplicationUtils.throwAlertDialog(context,
                                YaResponseCodes.getTitle(context), res.getMessage(context));
                    }
                    });
            }
        }

        @Override
        public void afterTextChanged(final Editable editable) {}
    }

    /**
     * Create button behavior class
     */
    private class OnClickTranslateButtonListener implements View.OnClickListener {
        private final Context context;

        public OnClickTranslateButtonListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            String text = translateText.getText().toString();

            if (text.isEmpty()) { return; }
            if (toLanguageSpinner.getAdapter().isEmpty() ||
                    fromLanguageSpinner.getAdapter().isEmpty()) {
                ApplicationUtils.throwAlertDialog(this.context, R.string.language_list_empty_title, R.string.language_list_empty_message);
                return;
            }

            Languages.Language langOut = (Languages.Language) toLanguageSpinner.getSelectedItem();
            Languages.Language langIn = (Languages.Language) fromLanguageSpinner.getSelectedItem();

            YaTranslateManager translateManager = YaTranslateManager.getInstance();
            translateManager.executeTranslate(
                    new Translate.Params(text, langIn, langOut),
                    this.context,
                    new Callable<Translate>() {
                        @Override
                        public void done(Translate value) {
                            HistoryTranslate hs = new HistoryTranslate(getDbOpenHelper());
                            HistoryTranslateEntry rec = hs.create(value, false);

                            translatedAdapter.clear();
                            translatedAdapter.setTranslateEntry(rec);
                            translatedAdapter.notifyDataSetChanged();
                            favoriteButton.setEnabled(true);

                            state.putSerializable(ARG_TRANSLATED_TEXT, rec);
                        }

                        @Override
                        public void error(final Response res) {
                            String responseDescription =  res.getMessage(context);
                            Log.e("TranslateFragment", "[Translate] " + responseDescription);
                            ApplicationUtils.throwAlertDialog(context,
                                        YaResponseCodes.getTitle(context), responseDescription);
                        }
                    }
            );
        }
    }

    private class OnClickFavoriteButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            TranslateListAdapter adapter = (TranslateListAdapter) translateList.getAdapter();
            if (adapter == null || adapter.isEmpty()) {
                error("Adapter didn't init or empty!");
                return;
            }

            HistoryTranslateEntry entry = adapter.getTranslateEntry();
            HistoryTranslate historyTranslate = new HistoryTranslate(TranslateFragment.this.getDbOpenHelper());
            historyTranslate.setFavorite(entry, ((ToggleButton) view).isChecked());
        }

        private void error (String errorMsg) {
            Log.e("TranslateFragment",  "[FAVORITE BUTTON] error: " + errorMsg);
            Toast.makeText(TranslateFragment.this.getContext(), errorMsg, Toast.LENGTH_LONG).show();
        }
    }
}
