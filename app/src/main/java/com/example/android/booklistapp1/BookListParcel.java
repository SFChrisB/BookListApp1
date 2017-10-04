package com.example.android.booklistapp1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AgiChrisPC on 03/07/2017.
 */

public class BookListParcel implements Parcelable {

    private String mTitle, mAuth, mPubl, mPubD, mDesc, mBUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mAuth);
        out.writeString(mPubl);
        out.writeString(mPubD);
        out.writeString(mDesc);
        out.writeString(mBUrl);
    }

    public static final Parcelable.Creator<BookListParcel> CREATOR
            = new Parcelable.Creator<BookListParcel>() {
        public BookListParcel createFromParcel(Parcel in) {
            return new BookListParcel(in);
        }
        public BookListParcel[] newArray(int size) {
            return new BookListParcel[size];
        }
    };

    private BookListParcel(Parcel in) {
        mTitle = in.readString();
        mAuth = in.readString();
        mPubl = in.readString();
        mPubD = in.readString();
        mDesc = in.readString();
        mBUrl = in.readString();
    }

    public BookListParcel(String title, String auth, String publ, String pubD, String desc, String bUrl) {

        mTitle = title;
        mAuth = auth;
        mPubl = publ;
        mPubD = pubD;
        mDesc = desc;
        mBUrl = bUrl;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getAuth() {
        return mAuth;
    }
    public String getPubl() {
        return mPubl;
    }
    public String getPubD() {
        return mPubD;
    }
    public String getDesc() {return mDesc;}
    public String getBUrl() {
        return mBUrl;
    }


}
