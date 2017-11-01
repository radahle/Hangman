package com.example.rudiandre.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by RudiAndre on 19.09.2017.
 */

public class RulesActivity extends AppCompatActivity {

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
        setContentView(R.layout.rules);

    }

    public void changeLanguage(Context context, String language, String country){

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
