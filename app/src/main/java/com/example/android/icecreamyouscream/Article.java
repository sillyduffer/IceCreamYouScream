package com.example.android.icecreamyouscream;

public class Article {

    private String mDate;
    private String mTitle;
    private String mUrl;
    private String mSection;

    public Article(String title, String date, String url, String section) {

        mTitle = title;
        mUrl = url;
        mDate = date;
        mSection = section;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmDate() {
        return mDate;
    }
}
