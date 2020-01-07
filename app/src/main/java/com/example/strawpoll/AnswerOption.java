package com.example.strawpoll;

import java.io.Serializable;
import java.util.List;

public class AnswerOption implements Serializable {
    private String answer;
    private List<String> votes;
    public AnswerOption(String answer, List<String> votes) {
        this.answer = answer;
        this.votes = votes;
    }

    public AnswerOption() {
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getVotes() {
        return votes;
    }
}
