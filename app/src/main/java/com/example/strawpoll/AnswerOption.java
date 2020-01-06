package com.example.strawpoll;

import java.io.Serializable;

public class AnswerOption implements Serializable {
    private String answer;

    public AnswerOption(String answer) {
        this.answer = answer;
    }

    public AnswerOption() {
    }

    public String getAnswer() {
        return answer;
    }

}
