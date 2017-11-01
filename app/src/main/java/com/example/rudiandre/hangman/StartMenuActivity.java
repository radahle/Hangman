package com.example.rudiandre.hangman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

public class StartMenuActivity extends AppCompatActivity implements View.OnClickListener {

    String language;
    String country;

    SharedPreferences languagePreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagePreferences = getSharedPreferences("LANG_PREFS", MODE_PRIVATE);
         if(languagePreferences.contains("langKey")) {
            language = languagePreferences.getString("langKey", Locale.getDefault().getLanguage());
        }
        if(languagePreferences.contains("countryKey")) {
            country = languagePreferences.getString("countryKey", Locale.getDefault().getCountry());
        }
        if(language != null && country != null) {
            changeLanguage(this, language, country);
        }


        setContentView(R.layout.start_menu);

        final ImageButton startBtn = (ImageButton) findViewById(R.id.start);
        startBtn.setOnClickListener(this);
        final ImageButton rulesBtn = (ImageButton) findViewById(R.id.rules);
        rulesBtn.setOnClickListener(this);
        final ImageButton statisticsBtn = (ImageButton) findViewById(R.id.statistics);
        statisticsBtn.setOnClickListener(this);
        final ImageButton noFlagBtn = (ImageButton) findViewById(R.id.no_flag);
        noFlagBtn.setOnClickListener(this);
        final ImageButton enFlagBtn = (ImageButton) findViewById(R.id.en_flag);
        enFlagBtn.setOnClickListener(this);

        if(getLocale().equals("nb_NO")) {
            noFlagBtn.setImageResource(R.drawable.no_button_pressed);
            noFlagBtn.setAlpha(.6f);
            noFlagBtn.setEnabled(false);
            enFlagBtn.setEnabled(true);
        } else {
            enFlagBtn.setImageResource(R.drawable.en_button_pressed);
            enFlagBtn.setAlpha(.6f);
            noFlagBtn.setEnabled(true);
            enFlagBtn.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = languagePreferences.edit();
        editor.putString("langKey", language);
        editor.putString("countryKey", country);
        editor.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                ButtonGrid.btnStateString = null;

                Intent startIntent = new Intent(this, GameBoard.class);
                startActivity(startIntent);
                break;

            case R.id.rules:
                Intent rulesIntent = new Intent(this, RulesActivity.class);
                startActivity(rulesIntent);
                break;

            case R.id.statistics:
                Intent statisticsIntent = new Intent(this, StatisticsActivity.class);
                startActivity(statisticsIntent);
                break;

            case R.id.no_flag:
                changeLanguage(this, "nb", "NO");
                GameBoard.usedWords.clear();
                SharedPreferences.Editor noEditor = getSharedPreferences("LANG_PREFS", MODE_PRIVATE).edit();
                noEditor.putString("langKey", language);
                noEditor.putString("countryKey", country);
                noEditor.commit();
                restartActivity();
                break;

            case R.id.en_flag:
                changeLanguage(this, "en", "GB");
                GameBoard.usedWords.clear();
                SharedPreferences.Editor enEditor = getSharedPreferences("LANG_PREFS", MODE_PRIVATE).edit();
                enEditor.putString("langKey", language);
                enEditor.putString("countryKey", country);
                enEditor.commit();
                restartActivity();
                break;

            default:
                break;

        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void changeLanguage(Context context, String language, String country){

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        setLocaleStrings(locale.toString());
    }

    public void setLocaleStrings(String locale) {
        String[] parts = locale.split("_");
        language = parts[0];
        country = parts[1];
    }

    public String getLocale() {
        Resources resources = this.getResources();

        Configuration configuration = resources.getConfiguration();
        return configuration.getLocales().get(0).toString();
    }
}
