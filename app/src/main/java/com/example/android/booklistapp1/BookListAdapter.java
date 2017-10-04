package com.example.android.booklistapp1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AgiChrisPC on 03/07/2017.
 */

public class BookListAdapter extends ArrayAdapter<BookListParcel> {

    static class ViewHolder {
        private TextView titleTV;
        private TextView authTV;
        private TextView publTV;
        private TextView pubDTV;
        private TextView descTV;
    }

    public BookListAdapter(Activity c, ArrayList<BookListParcel> bookListParcels) {
        super(c, 0, bookListParcels);
    }

    @Override
    public View getView(int pos, @Nullable View cV, @NonNull ViewGroup parent) {
        View vListItem = cV;

        final BookListParcel currentBook = getItem(pos);
        ViewHolder vHol;

        if (vListItem == null) {
            vListItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            vHol = new ViewHolder();
            vHol.titleTV = vListItem.findViewById(R.id.bookTitle);
            vHol.authTV = vListItem.findViewById(R.id.bookAuthors);
            vHol.publTV = vListItem.findViewById(R.id.bookPubl);
            vHol.pubDTV = vListItem.findViewById(R.id.bookPubD);
            vHol.descTV = vListItem.findViewById(R.id.bookDesc);
            vListItem.setTag(vHol);
        } else vHol = (ViewHolder) vListItem.getTag();


        vHol.titleTV.setText(currentBook.getTitle());
        vHol.authTV.setText(currentBook.getAuth());
        vHol.publTV.setText(currentBook.getPubl());
        vHol.pubDTV.setText(currentBook.getPubD());
        vHol.descTV.setText(currentBook.getDesc());

        return vListItem;
    }
}
