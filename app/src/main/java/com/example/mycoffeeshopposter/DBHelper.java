package com.example.mycoffeeshopposter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "dbmenu";

    // closed orders table
    static final String CO_TABLE_NAME = "table_stat";
    static final String CO_IS_TABLE = "isTable";
    static final String CO_IS_CASH = "isCash";
    static final String CO_NAME_NAME = "nametableoritem";
    static final String CO_PRICE_SUM = "pricesum";
    static final String CO_AMOUNT = "amount";
    static final String CO_DATE_TIME = "datetime";

    //menu data table

    static final String GROUP = "groupname";
    static final String NAME = "name";
    static final String PRICE = "price";
    static final String TABLE_NAME = "mainMenu";

    // all time days summary stat.

    static final String TABLE_ALLTIMEDAYMONTHSUM = "table_alldayssummary";
    static final String DAYM_IS_MONTH = "ismonth";
    static final String DAYM_DATE = "day_date";
    static final String DAYM_SUMMARY_CASH = "day_cash";
    static final String DAYM_SUMMARY_TERM = "day_term";
    static final String DAYM_MIDDLE_ORDER = "day_mid_ord";

    // all time items summary stat.

    static final String TABLE_ALLTIMEITEMSSTAT = "table_alltimeitems_summary";
    static final String ITEMNAME = "item_name";
    static final String ITEMAMOUNT = "items_amount";
    static final String ITEMSUMMARY = "items_summary";

    // all time items summary every month

    static final String TABLE_ALLTIMEITEMMONTHEVERY = "table_alltimeitem_everymonth";
    static final String EVMI_IS_MONTH = "evmi_ismonth";
    static final String EVMI_ITEM_NAME = "evmi_name";
    static final String EVMI_ITEM_AMOUNT = "evmi_it_amount";
    static final String EVMI_ITEM_SUMMARY = "evmi_it_SUM";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // menu data
        db.execSQL("create table " + TABLE_NAME + " ( id integer primary key autoincrement, "
                + GROUP + " text, "
                + NAME + " text, "
                + PRICE + " int"
                + " );");

        // current work day table (operative) all orders
        db.execSQL("create table "
                + CO_TABLE_NAME + " ( id integer primary key autoincrement, "
                + CO_IS_TABLE +  " boolean,"
                + CO_IS_CASH + " boolean,"
                + CO_NAME_NAME + " String,"
                + CO_PRICE_SUM + " integer,"
                + CO_AMOUNT + " integer,"
                + CO_DATE_TIME + " String"
                + " );");

        // all time days + months summary stat.
        db.execSQL("create table " + TABLE_ALLTIMEDAYMONTHSUM + " ( id integer primary key autoincrement, "
                + DAYM_IS_MONTH + " boolean, "
                + DAYM_DATE + " text, "
                + DAYM_SUMMARY_CASH + " int, "
                + DAYM_SUMMARY_TERM + " int, "
                + DAYM_MIDDLE_ORDER + " int"
                + " );");

        // all time items summary stat.
        db.execSQL("create table " + TABLE_ALLTIMEITEMSSTAT + " ( id integer primary key autoincrement, "
                + ITEMNAME + " text, "
                + ITEMAMOUNT + " int, "
                + ITEMSUMMARY + " int"
                + " );");

        // all time items summary every month
        db.execSQL( "create table " + TABLE_ALLTIMEITEMMONTHEVERY + "( id integer primary key autoincrement, "
                + EVMI_IS_MONTH + " boolean, "
                + EVMI_ITEM_NAME + " text, "
                + EVMI_ITEM_AMOUNT + " int, "
                + EVMI_ITEM_SUMMARY + " int "
                + ");");

        //TODO current week table (operative) all orders
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
