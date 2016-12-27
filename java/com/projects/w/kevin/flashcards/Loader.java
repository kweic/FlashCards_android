package com.projects.w.kevin.flashcards;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by kw on 12/9/2016.
 */

class Loader{
    private String path;
    private String TAG = "flashcards";
    private Context context;
    private int arrayID;


    Loader(String path, Context context, int arrayID){
        this.context = context;
        this.path = path;
        this.arrayID = arrayID;
        Log.i(TAG, "loader, path set to: "+path);
    }


    ArrayList<FlashCard> getFlashCards(){
        ArrayList<FlashCard> flashCards = loadFile();
        Log.i(TAG, " flashcards created, size: "+flashCards.size());

        return flashCards;
    }

    private ArrayList<FlashCard> loadFile(){
        ArrayList<FlashCard> loadedList = new ArrayList<>();
        FileInputStream fileInputStream;

        try {
            fileInputStream = context.openFileInput(path);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            loadedList = (ArrayList<FlashCard>) ois.readObject();
            ois.close();
            Log.i(TAG, "loadfile successful in try! Finished, size: "+loadedList.size());
        } catch (Exception e) {
            Log.i(TAG, "failed on try to load");
            e.printStackTrace();
        }
        if(loadedList == null || loadedList.isEmpty()){
            Log.i(TAG, "about to do loaded list from resources");
            loadedList = convertStringListToFlashCards(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(arrayID))));
        }

        return loadedList;
    }

    ArrayList<FlashCard> loadFreshFile(){
        return convertStringListToFlashCards(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(arrayID))));
    }

    private ArrayList<FlashCard> convertStringListToFlashCards(ArrayList<String> loadedList){
        Log.i(TAG, "doing convert from string to flashcards");
         ArrayList<FlashCard> flashCards = new ArrayList<>();
        int scoreLine;
        for(int i = 0; i < loadedList.size(); i+=3){
            String qLine = loadedList.get(i);
            String aLine = loadedList.get(i+1);
            scoreLine = Integer.parseInt(loadedList.get(i+2));
            flashCards.add(makeFlashCard(qLine, aLine, scoreLine));
        }
        return flashCards;
    }

    private FlashCard makeFlashCard(String question, String answer, int count){
        FlashCard flashCard = new FlashCard();
        flashCard.setAnswer(answer);
        flashCard.setQuestion(question);
        flashCard.setCount(count);
        return flashCard;
    }
}
