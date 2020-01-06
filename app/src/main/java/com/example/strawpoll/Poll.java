package com.example.strawpoll;

import java.io.Serializable;

public class Poll implements Serializable {

    private Boolean expired;
    private String title;
    private String user;

    public Poll() {
    }

    public Poll(Boolean expired, String title, String user) {
        this.expired = expired;
        this.title = title;
        this.user = user;
    }

    public Boolean getExpired() {
        return expired;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }
}
