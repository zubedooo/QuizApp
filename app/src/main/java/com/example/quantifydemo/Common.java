package com.example.quantifydemo;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static final int TOTAL_TIME=1*60*1000;
    public static List<TestQuestion> questionList=new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList=new ArrayList<>();
    public static int right_answer_count=0;
    public static int wrong_answer_count=0;
    public static CountDownTimer countDownTimer;
    public static Tests selectedTest;
    public static ArrayList<QuestionFragment> fragmentList=new ArrayList<>();
    public static String selected_values="";

    public enum ANSWER_TYPE{
        NO_ANSWER,
        RIGHT_ANSWER,
        WRONG_ANSWER
    }

}
