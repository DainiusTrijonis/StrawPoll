package com.example.strawpoll;

import java.io.Serializable;

public class PollOption implements Serializable {

    private String[] Count;
    private String Title;

    public PollOption() {
    }

    public PollOption(String[] count, String title) {
        Count = count;
        Title = title;
    }

    public String[] getCount() {
        return Count;
    }

    public String getTitle() {
        return Title;
    }
}
