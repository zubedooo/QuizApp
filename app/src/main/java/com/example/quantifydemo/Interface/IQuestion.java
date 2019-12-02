package com.example.quantifydemo.Interface;

import com.example.quantifydemo.CurrentQuestion;

public interface IQuestion {

    CurrentQuestion getSelectedAnswer();

    void showCurrentAnswer();
    void disableAnswer();
    void resetQuestion();

}
