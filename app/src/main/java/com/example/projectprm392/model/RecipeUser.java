package com.example.projectprm392.model;

public class RecipeUser {
private String title;
private int ava_chap;

    public RecipeUser(String title, int ava_chap) {
        this.title = title;
        this.ava_chap = ava_chap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAva_chap() {
        return ava_chap;
    }

    public void setAva_chap(int ava_chap) {
        this.ava_chap = ava_chap;
    }
}
