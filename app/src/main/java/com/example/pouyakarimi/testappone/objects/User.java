package com.example.pouyakarimi.testappone.objects;

/**
 * Created by pouyakarimi on 10/27/15.
 */
public class User {

    private int _id;
    private String _name;
    private String _email;
    private boolean _isPrimary;

    public User() {
    }

    public User(String _name, String _email, boolean _isPrimary) {
        this._name = _name;
        this._email = _email;
        this._isPrimary = _isPrimary;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public boolean isItPrimary() {
        return _isPrimary;
    }

    public void setisItPrimary(boolean _isPrimary) {
        this._isPrimary = _isPrimary;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return _id == user._id;

    }

    @Override
    public int hashCode() {
        return _id;
    }
}
