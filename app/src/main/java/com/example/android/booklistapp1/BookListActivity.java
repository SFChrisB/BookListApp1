package com.example.android.booklistapp1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<BookListParcel>> {

    public static final String LOG_TAG = BookListActivity.class.getSimpleName();
    private static final String BOOKLIST_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?orderBy=newest&q=";
    private static final int BOOKLIST_LOADER_ID = 1;
    private BookListAdapter mBookListAdapter;
    private SearchView searchView;
    private ListView bookListView;
    private String seaQuery;
    private String reqUrl;
    private String defQuery = "java";
    private TextView mEmptyStateTextView;
    private ImageView mProgressBarImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        searchView = (SearchView) findViewById(R.id.searchView);
        bookListView = (ListView) findViewById(R.id.booksList);
        mBookListAdapter = new BookListAdapter(this, new ArrayList<BookListParcel>());
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        mProgressBarImageView = (ImageView) findViewById(R.id.progressSearchIm);

        if (isConnected()) {
            LoaderManager lm = getLoaderManager();
            lm.initLoader(BOOKLIST_LOADER_ID, null, this);
        } else {
            Log.v(LOG_TAG, "Internet not available");
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.nullInternet);
        }

        bookListView.setAdapter(mBookListAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int pos, long l) {
                BookListParcel currentBook = mBookListAdapter.getItem(pos);
                Uri bookListUri = Uri.parse(currentBook.getBUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookListUri);
                startActivity(websiteIntent);
            }
        });

        bookListView.setEmptyView(mEmptyStateTextView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnected()) {
                    //Removes visibility of the ListView and the emptyView,
                    //but shows the Progress bar.
                    mProgressBarImageView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    bookListView.setVisibility(View.INVISIBLE);
                    mEmptyStateTextView.setVisibility(View.GONE);

                    //Retrieves the data searched for. Replaces spaces to mean an
                    //additional search word has been entered.
                    seaQuery = searchView.getQuery().toString();
                    seaQuery = seaQuery.replace(" ", "+");

                    Log.v(LOG_TAG, "requested search of " + seaQuery);
                    getLoaderManager().restartLoader(BOOKLIST_LOADER_ID, null, BookListActivity.this);
                    searchView.clearFocus();
                } else {
                    //Removes visibility of progress bar and the ListView. Makse the emptyView visible
                    //and displays the message informing that there is no connection
                    //Adapter cleared
                    mBookListAdapter.clear();
                    bookListView.setVisibility(View.INVISIBLE);
                    mProgressBarImageView.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.nullInternet);
                    Log.v(LOG_TAG, "requested search of " + seaQuery);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nI = cm.getActiveNetworkInfo();
        return (nI != null && nI.isConnected());
    }

    @Override
    public Loader<List<BookListParcel>> onCreateLoader(int i, Bundle bundle) {
        reqUrl = "";

        //Searches for the user typed search query if there is one used.
        if (seaQuery != null && seaQuery != "") reqUrl = BOOKLIST_REQUEST_URL + seaQuery;
        else {
            //The default query that is loaded when the app is turned on
            reqUrl = BOOKLIST_REQUEST_URL + defQuery;
        }
        return new BookListLoader(this, reqUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookListParcel>> loader, List<BookListParcel> bookList) {
        mEmptyStateTextView.setText(R.string.nullBooks);
        mProgressBarImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mBookListAdapter.clear();

        if (bookList != null && !bookList.isEmpty()) {
            mBookListAdapter.addAll(bookList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookListParcel>> loader) {
        mBookListAdapter.clear();
    }

}