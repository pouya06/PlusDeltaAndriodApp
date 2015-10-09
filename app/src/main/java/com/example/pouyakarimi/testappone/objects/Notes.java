package com.example.pouyakarimi.testappone.objects;

import java.io.Serializable;

/**
 * Created by pouyakarimi on 10/7/15.
 */
public class Notes implements Serializable {

    private String text;
    private boolean isItPlus;

    public Notes(){
    }

    public Notes(String id, String text, boolean isItPlus) {
        this.text = text;
        this.isItPlus = isItPlus;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isItPlus() {
        return isItPlus;
    }

    public void setIsItPlus(boolean isItPlus) {
        this.isItPlus = isItPlus;
    }

}
