package com.projects.w.kevin.flashcards;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import static android.R.attr.path;
import static android.content.ContentValues.TAG;

/**
 * Created by kw on 12/9/2016.
 */

public class Saver{
    String fileName;
    Context context;

    public Saver(String fileName, Context context){
        this.fileName = fileName;
        this.context = context;

    }

    public void saveFile(ArrayList<FlashCard> flashCards){
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(flashCards);
            oos.close();
            Log.i("flashcards", "save finished");
        } catch (Exception e) {
            Log.i("flashcards", "save failed");
        }
    }
}
