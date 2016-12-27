package com.projects.w.kevin.flashcards;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.R.string.yes;

public class MainActivity extends AppCompatActivity {
    public final String FLASHCARDS_KEY = "kw.Flashcards_loadkey";
    private Loader loader;
    private Saver saver;

    private String TAG = "flashcards";
    private ArrayList<FlashCard> flashCards;

    Button yesBtn;
    Button noBtn;
    Button nextBtn;
    TextView answerText;
    TextView questionText;
    TextView scoreText;

    private Random random;
    private int draw;
    private int saveFrequencyCount;
    private int saveFrequency = 3;
    private int known;

    private boolean buttonPressed;
    private boolean showAnswerBtn;
    private boolean fileSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate, mainactivity");
        setContentView(R.layout.activity_main);
      //  getSupportActionBar().hide();
        loader = new Loader(FLASHCARDS_KEY, getBaseContext(), R.array.JavaQuestions);
        saver  = new Saver(FLASHCARDS_KEY, getBaseContext());
        random = new Random();

        loadFlashCards(); //assign flashcards to arraylist
       // setCumulativeScore(); //not used right now

        yesBtn = (Button)findViewById(R.id.btnKnow);
        noBtn = (Button)findViewById(R.id.btnDontKnow);
        nextBtn = (Button)findViewById(R.id.btnShow);
        questionText = (TextView)findViewById(R.id.tvQuestion);
        answerText = (TextView)findViewById(R.id.tvAnswer);
        scoreText = (TextView)findViewById(R.id.tvScore);

        attachYesNoButton(true, yesBtn);
        attachYesNoButton(false, noBtn);
        attachNextButton();
        Log.i(TAG, "finished onCreate");
    }

    private void loadFlashCards() {
        flashCards = loader.getFlashCards();
        fileSet = true;
    }

    private void attachYesNoButton(final boolean yes, Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "button clicked, "+yes+" button");
                setButtonPressed(yes);
                turnOffButton(noBtn);
                turnOffButton(yesBtn);
            }
        });
    }

    private void attachNextButton(){
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, " next button clicked");
                nextButton();
            }
        });
    }

    public void nextButton(){
        if(!fileSet)return;

        if(!showAnswerBtn){
            runNext();
            turnOnButton(yesBtn);
            turnOnButton(noBtn);
            showAnswerBtn = true;
            //TODO: replace hardcoded text with R.string
            nextBtn.setText(getString(R.string.show_answer));
        }else{ //button is now a show answer button
            setBtnToNext();//make programLoop show the answer, switch back to a "next" button
        }
    }

    private void turnOnButton(Button btn){
        btn.setEnabled(true);
        btn.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.activeButton));
        btn.setTextColor(Color.WHITE);
    }

    private void turnOffButton(Button btn){
        btn.setEnabled(false);
        btn.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.inactiveButton)); //dim the button
        btn.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.activeButton)); //dim the text
    }

    private void setBtnToNext(){
        printQandA();
        showAnswerBtn = false;
        nextBtn.setText("Next");
    }

    private void setKnownAmount(){
        //todo: can implement way to check what count is on draw and change score without reiterating
        known = 0;
        for(FlashCard card: flashCards){
            if(card.getCount() > 0) known++;
        }
        scoreText.setText(getString(R.string.precedesScore)+known+" / "+flashCards.size());
    }

    public void runNext(){
        if(flashCards != null && flashCards.size() > 0){
            setKnownAmount();
            buttonPressed = false;
            Collections.sort(flashCards);
            clearTextboxes();

            //TODO: prevent the same card from popping up twice in a row
            //TODO: consider tightening up the range, a large set may feel a little too random and less focused study
            draw = random.nextInt(random.nextInt(flashCards.size())+1); //random in random for weighted random
            questionOrAnswer(random.nextInt(2));
        }
    }

    private void clearTextboxes(){
        answerText.setText("");
        questionText.setText("");
    }

    private void saveFile(){
        if(saveFrequencyCount >= saveFrequency){ //tally for less frequent saves
            saver.saveFile(flashCards);
            saveFrequencyCount = 0;
        }
        saveFrequencyCount += 1;
    }

    public void setButtonPressed(boolean yesOrNo){
        if(!fileSet)return;

        if(!buttonPressed){
            buttonPressed = true;
            flashCards.get(draw).answer(yesOrNo);
            setKnownAmount();
            printQandA();
            saveFile();
            setBtnToNext();
        }
    }

    private void questionOrAnswer(int questOrAns){
        if(questOrAns == 0){ //display the question
            questionText.setText(flashCards.get(draw).getQuestion());
            questionText.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.withQuestionBackground));
            answerText.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.blankTextBackground));
        }else{ //display the answer
            answerText.setText(flashCards.get(draw).getAnswer());
            answerText.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.withQuestionBackground));
            questionText.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.blankTextBackground));
        }
    }

    public void printQandA(){
        questionText.setText(flashCards.get(draw).getQuestion());
        answerText.setText(flashCards.get(draw).getAnswer());
    }

    private void resetAllCards(){
        flashCards = loader.loadFreshFile();
        for(FlashCard card: flashCards){
            card.setCount(0);
        }
        runNext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reset) {
            resetAllCards();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
