package com.example.android.icecreamyouscream;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int Article_LOADER_ID = 1;

    private ArticleAdapter mAdapter;

    private TextView mEmptyView;

    private View mLoadingIndicator;

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String searchUrl = "http://content.guardianapis.com/search?q=icecream&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ListView articleListView = (ListView) findViewById(R.id.list);

        mLoadingIndicator = findViewById(R.id.progress_bar);

        mEmptyView = (TextView) findViewById(R.id.empty_view);

        articleListView.setEmptyView(mEmptyView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = mAdapter.getItem(position);
                Uri ArticleUri = Uri.parse(currentArticle.getmUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, ArticleUri);

                startActivity(websiteIntent);
            }
        });

        if (isConnected) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(Article_LOADER_ID, null, MainActivity.this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet_alert);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        else{
            mEmptyView.setText(R.string.no_results_message);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}

