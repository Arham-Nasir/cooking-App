package com.ass3.project;

import java.io.Serializable;

public class Description implements Serializable {
    public String details;

    public Description()
    {

    }
    public Description(String details) {
        this.details = details;

    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
