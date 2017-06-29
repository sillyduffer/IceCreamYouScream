package com.example.android.icecreamyouscream;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private static final String LOG_TAG = ArticleLoader.class.getName();

    private final String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() called ");
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() called ");
        if (mUrl == null) {
            return null;
        }

        return Utils.fetchArticleData(mUrl);
    }
}