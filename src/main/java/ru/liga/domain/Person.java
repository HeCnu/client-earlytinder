package ru.liga.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private String uniqueID;
    private Boolean male;
    private String login;
    private String password;
    private String title;
    private List<String> likeList = new ArrayList<>();

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> list) {
        this.likeList = list;
    }

    public void addLikeToList(String id) {
        likeList.add(id);
    }

    public void removeLikeFromList(String id) {
        likeList.remove(id);
    }

    public Boolean isEmpty() {
        if (uniqueID.equals("") && login.equals(""))
            return true;
        return false;
    }

}
