package com.pkchoksi.pkc.lenscape;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by pkcho on 1/19/2016.
 */
@ParseClassName("follow")
public class Follow extends ParseObject{
    public Follow(){

    }
    public ParseUser getfrom() {
        return getParseUser("from");
    }

    public void setfrom(ParseUser user) {
        put("from", user);
    }
    public ParseUser getto() {
        return getParseUser("to");
    }

    public void setto(ParseUser user) {
        put("to", user);
    }
}
