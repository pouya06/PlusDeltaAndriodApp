package com.example.pouyakarimi.testappone.objects;

/**
 * Created by pouyakarimi on 10/7/15.
 */
public class Note {

    private int _id;
    private String _text;
    private boolean _isPlus;

    public Note(){
    }

    public Note(String text, boolean isItPlus) {
        this._text = text;
        this._isPlus = isItPlus;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public boolean isItPlus() {
        return _isPlus;
    }

    public void setIsItPlus(boolean isItPlus) {
        this._isPlus = isItPlus;
    }

}
