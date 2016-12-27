package com.projects.w.kevin.flashcards;

/**
 * Created by kw on 12/9/2016.
 */

class FlashCard implements Comparable<FlashCard>, java.io.Serializable{

    private String question;
    private String answer;
    private int correctAnswers;

    FlashCard(){

    }

    void setQuestion(String question){
        this.question = question;
    }

    void setAnswer(String answer){
        this.answer = answer;
    }

    void answer(boolean answer){
        if(answer){
            correctAnswers++;
        }else{
            correctAnswers--;
        }
    }

    void setCount(int count){
        this.correctAnswers = count;
    }

    int getCount(){
        return correctAnswers;
    }

    String getQuestion(){
        return question;
    }

    String getAnswer(){
        return answer;
    }

    @Override
    public int compareTo(FlashCard arg0) {
        return this.getCount() - arg0.getCount();
    }

}
