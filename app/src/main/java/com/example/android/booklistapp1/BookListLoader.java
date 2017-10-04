package com.example.android.booklistapp1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by AgiChrisPC on 03/07/2017.
 */

public class BookListLoader extends AsyncTaskLoader<List<BookListParcel>> {
    private String mUrl;
    private List<BookListParcel> bookListParcel;

    public BookListLoader(Context c, String url) {
        super(c);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookListParcel> loadInBackground() {
        if (mUrl == null) return null;

        bookListParcel = QueryUtils.fetchBookData(mUrl);
        return bookListParcel;
    }
}

