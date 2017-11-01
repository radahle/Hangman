package com.example.rudiandre.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by RudiAndre on 30.08.2017.
 */

public class ButtonGrid extends BaseAdapter {

    ArrayList<Character> btnValue;
 //   ArrayList<Character> selectedLetters;
    ArrayList<Integer> btnImages;
    List<Boolean> btnStateList;
    Context gameBoardActivityContext;
    TextView wordView;
    ImageView hangMan;

    char[] gameWord;
    char[] hiddenWord;
    protected static int[] btnState;
    protected static String btnStateString;

    protected static LayoutInflater inflater = null;

    public ButtonGrid(GameBoard gameBoardActivity, ArrayList<Character> btnValue, ArrayList<Integer> btnImages,
                      char[] gameWord, char[] hiddenWord, TextView wordView, ImageView hangMan) {
        this.gameBoardActivityContext = gameBoardActivity;
        this.wordView = wordView;
        this.hangMan = hangMan;
        this.btnValue = btnValue;
        this.btnImages = btnImages;
        this.gameWord = gameWord;
        this.hiddenWord = hiddenWord;
        inflater = (LayoutInflater) this.gameBoardActivityContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        btnState = new int[btnImages.size()];
        for(int i = 0; i < btnState.length; i++) {
            if(btnStateString != null && btnStateString.charAt(i) == '2') {
                btnState[i] = 2;
            } else {
                btnState[i] = 1;
            }
        }
    }


    @Override
    public int getCount() {
        return btnValue.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View buttonView;
        buttonView = inflater.inflate(R.layout.button_grid, null);
        final ImageButton imgBtn = (ImageButton) buttonView.findViewById(R.id.imageButton);

        imgBtn.setImageResource(btnImages.get(position));
        if(btnState[position] == 2) {
            imgBtn.setAlpha(.2f);
            imgBtn.setEnabled(false);

            for(int i = 0; i < hiddenWord.length; i++) {
                if (btnValue.get(position).equals(hiddenWord[i])) {
                    imgBtn.setBackgroundResource(R.drawable.correct);
                    break;
                } else {
                    imgBtn.setBackgroundResource(R.drawable.wrong);
                }
            }
        }


        imgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (GameBoard.tries != 0 && !Arrays.equals(hiddenWord, gameWord) && imgBtn.isEnabled()) {

                   imgBtn.setAlpha(.2f);
                   imgBtn.setEnabled(false);
                   char chosenLetter = btnValue.get(position);
                   btnState[position] = 2;
        //           selectedLetters.add(chosenLetter);
                   checkGuess(chosenLetter, imgBtn);
               }
           }
       });
       return buttonView;
   }


   public void checkGuess(char guess, ImageButton imgBtn) {
           StringBuilder sb = new StringBuilder();
           int count = 0;
           for (int i = 0; i < gameWord.length; i++) {
               if (gameWord[i] == guess) {
                   hiddenWord[i] = gameWord[i];
                   count++;
               }
               sb.append(hiddenWord[i] + " ");
           }
           if (count == 0) {
               imgBtn.setBackgroundResource(R.drawable.wrong);
               GameBoard.tries--;
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
                       GameBoard.loss++;
                       AlertDialog alertDialog = new AlertDialog.Builder(gameBoardActivityContext).create();
                       alertDialog.setTitle(gameBoardActivityContext.getResources().getString(R.string.lost_title));
                       alertDialog.setMessage(gameBoardActivityContext.getResources().getString(R.string.lost_message) + " " + GameBoard.gameWordString);
                       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, gameBoardActivityContext.getResources().getString(R.string.to_start),
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       Intent intent = new Intent(gameBoardActivityContext, StartMenuActivity.class);
                                       gameBoardActivityContext.startActivity(intent);


                                   }
                               });
                       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, gameBoardActivityContext.getResources().getString(R.string.new_game),
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       for(int i = 0; i < btnState.length; i++) {
                                           btnState[i] = 1;
                                       }
                                       Intent i = new Intent(gameBoardActivityContext, GameBoard.class);
                                       gameBoardActivityContext.startActivity(i);
                                   }
                               });
                       alertDialog.show();
                       break;
               }
           } else {
               imgBtn.setBackgroundResource(R.drawable.correct);
               if (Arrays.equals(hiddenWord, gameWord)) {
                   GameBoard.wins++;
                   AlertDialog alertDialog = new AlertDialog.Builder(gameBoardActivityContext).create();
                   alertDialog.setTitle(gameBoardActivityContext.getResources().getString(R.string.win_title));
                   alertDialog.setMessage(gameBoardActivityContext.getResources().getString(R.string.win_message));
                   alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, gameBoardActivityContext.getResources().getString(R.string.to_start),
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent intent = new Intent(gameBoardActivityContext, StartMenuActivity.class);
                                   gameBoardActivityContext.startActivity(intent);

                               }
                           });
                   alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, gameBoardActivityContext.getResources().getString(R.string.new_game),
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   for(int i = 0; i < btnState.length; i++) {
                                       btnState[i] = 1;
                                   }
                                   Intent i = new Intent(gameBoardActivityContext, GameBoard.class);
                                   gameBoardActivityContext.startActivity(i);
                               }
                           });
                   alertDialog.show();
               }

           }

           wordView.setText(sb.toString());
   }
}