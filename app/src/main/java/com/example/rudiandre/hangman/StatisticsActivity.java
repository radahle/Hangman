package com.example.rudiandre.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by RudiAndre on 17.09.2017.
 */

public class StatisticsActivity extends AppCompatActivity {

    TextView textWin, textLoss;
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

        setContentView(R.layout.statistics);

        textWin = (TextView)findViewById(R.id.textWin);
        textWin.setText(String.valueOf(GameBoard.wins));
        textLoss = (TextView)findViewById(R.id.textLoss);
        textLoss.setText(String.valueOf(GameBoard.loss));

    }

    public void changeLanguage(Context context, String language, String country){

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
