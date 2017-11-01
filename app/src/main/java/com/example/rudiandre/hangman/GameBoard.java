package com.example.rudiandre.hangman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by RudiAndre on 05.09.2017.
 */

public class GameBoard extends AppCompatActivity {


    GridView buttonGrid;
    TextView wordView;
    ImageView hangMan;
    private char value = 'A';
    protected static int tries;
    protected static String gameWordString;
    protected static int loss;
    protected static int wins;
    static boolean newGamePressed = false;

    String language;
    String country;
    SharedPreferences languagePreferences;
    SharedPreferences btnStatePreferences;

    ArrayList<Integer> btnImages = new ArrayList<>();
    ArrayList<Character> btnValue = new ArrayList<>();
    protected static ArrayList<String> usedWords = new ArrayList<>();

    char[] persistValue;
    char[] gameWord;
    char[] hiddenWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnStatePreferences = getSharedPreferences("BTN_STATE_PREFS", MODE_PRIVATE);
        if (btnStatePreferences.contains("btnStateKey") && savedInstanceState != null) {
            ButtonGrid.btnStateString = btnStatePreferences.getString("btnStateKey", null);
            for (int i = 0; i < ButtonGrid.btnStateString.length(); i++) {
                ButtonGrid.btnState[i] = Character.getNumericValue(ButtonGrid.btnStateString.charAt(i));
            }
        }
        newGamePressed = false;

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

        setContentView(R.layout.game_board);

        wordView = (TextView) findViewById(R.id.textView);
        hangMan = (ImageView) findViewById(R.id.hangMan);

        if(savedInstanceState == null) {
            tries = 6;
            randWord();

        }  else {
            getHangManState();

            gameWord = savedInstanceState.getCharArray("gameWord");
            hiddenWord = savedInstanceState.getCharArray("hiddenWord");

            setWordView(hiddenWord, gameWord);

        }

        addImages();

        buttonGrid = (GridView) findViewById(R.id.gridView);
        buttonGrid.setAdapter(new ButtonGrid(this, btnValue, btnImages, gameWord, hiddenWord, wordView, hangMan));
    }

    @Override
    protected void onPause() {
        super.onPause();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ButtonGrid.btnState.length; i++) {
            sb.append(ButtonGrid.btnState[i]);
        }
        ButtonGrid.btnStateString = sb.toString();

        SharedPreferences.Editor btnEditor = btnStatePreferences.edit();
        btnEditor.putString("btnStateKey", ButtonGrid.btnStateString);
        btnEditor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("gameWord", gameWord);
        outState.putCharArray("hiddenWord", hiddenWord);

        persistValue = new char[btnValue.size()];
        for(int i = 0; i < persistValue.length; i++) {
            persistValue[i] = btnValue.get(i);
        }
        outState.putCharArray("persistValue", persistValue);
    }

    public void addImages() {
        btnImages.add(R.drawable.a); btnImages.add(R.drawable.b); btnImages.add(R.drawable.c); btnImages.add(R.drawable.d);
        btnImages.add(R.drawable.e); btnImages.add(R.drawable.f); btnImages.add(R.drawable.g); btnImages.add(R.drawable.h);
        btnImages.add(R.drawable.i); btnImages.add(R.drawable.j); btnImages.add(R.drawable.k); btnImages.add(R.drawable.l);
        btnImages.add(R.drawable.m); btnImages.add(R.drawable.n); btnImages.add(R.drawable.o); btnImages.add(R.drawable.p);
        btnImages.add(R.drawable.q); btnImages.add(R.drawable.r); btnImages.add(R.drawable.s); btnImages.add(R.drawable.t);
        btnImages.add(R.drawable.u); btnImages.add(R.drawable.v); btnImages.add(R.drawable.w); btnImages.add(R.drawable.x);
        btnImages.add(R.drawable.y); btnImages.add(R.drawable.z);

        for (int i = 0; i < btnImages.size(); i++) {
            if (i > 25) {
                break;
            }
            btnValue.add(value++);
        }

        if(getLocale().equals("nb_NO")) {
            btnImages.add(R.drawable.ae);btnImages.add(R.drawable.oe);btnImages.add(R.drawable.aa);
              btnValue.add('Æ');
              btnValue.add('Ø');
              btnValue.add('Å');
        }
    }

    public void randWord() {
        String[] temp = getResources().getStringArray(R.array.word_array);

        if(usedWords.size() != temp.length) {

        String randString;

        do {
            randString = temp[new Random().nextInt(temp.length)];
        } while (usedWords.contains(randString));

        usedWords.add(randString);
        randString = randString.toUpperCase();

        gameWord = new char[randString.length()];
        hiddenWord = new char[randString.length()];

        int i;
        for (i = 0; i < randString.length(); i++) {
            gameWord[i] = randString.charAt(i);
            hiddenWord[i] = '_';
        }
        setWordView(hiddenWord, gameWord);
        } else {
            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.error_title));
            alertDialog.setMessage(getResources().getString(R.string.error_message));

            alertDialog.show();
        }
    }

    public void getHangManState() {
        switch (GameBoard.tries) {
            case 5:
                hangMan.setImageResource(R.drawable.feil_1);
                break;
            case 4:
                hangMan.setImageResource(R.drawable.feil_2);
                break;
            case 3:
                hangMan.setImageResource(R.drawable.feil_3);
                break;
            case 2:
                hangMan.setImageResource(R.drawable.feil_4);
                break;
            case 1:
                hangMan.setImageResource(R.drawable.feil_5);
                break;
            case 0:
                hangMan.setImageResource(R.drawable.feil_6);
                break;
        }
    }

    public void setWordView(char[] word, char[] gameWord) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbWord = new StringBuilder();

        for(int i = 0; i < word.length; i++) {
            sb.append(word[i] + " ");
            sbWord.append(gameWord[i]);
        }
        gameWordString = sbWord.toString();
        wordView.setText(sb.toString());
    }


    public void changeLanguage(Context context, String language, String country){

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }


    public String getLocale() {
        Resources resources = this.getResources();

        Configuration configuration = resources.getConfiguration();
        return configuration.getLocales().get(0).toString();
    }

    @Override
    public void onBackPressed() {
        Intent startMenuIntent = new Intent(this, StartMenuActivity.class);
        startActivity(startMenuIntent);
    }
}
