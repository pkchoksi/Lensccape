package com.pkchoksi.pkc.lenscape;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by pkcho on 1/8/2016.
 */
@ParseClassName("pictures")
public class Pictures extends ParseObject {
    public Pictures(){

    }
    public String getTitle(){
        return getString("title");
    }
    public void setTitle(String title){
        put("title", title);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public void setLikes(int likes){
        put("likes",likes);
    }
    public int getLikes(){
        return getInt("likes");
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

}
