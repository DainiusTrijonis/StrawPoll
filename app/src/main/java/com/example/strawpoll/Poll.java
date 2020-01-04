package com.example.strawpoll;

import java.io.Serializable;
import java.util.List;

public class Poll implements Serializable {

    private Boolean expired;
    private String title;
    private String userId;
    private List<PollOption> pollOption;

    public Poll() {
    }

    public Poll(Boolean expired, String title, String userId, List<PollOption> pollOption) {
        this.expired = expired;
        this.title = title;
        this.userId = userId;
        this.pollOption = pollOption;
    }
    public Boolean getExpired() {
        return expired;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public List<PollOption> getPollOption() {
        return pollOption;
    }
}
