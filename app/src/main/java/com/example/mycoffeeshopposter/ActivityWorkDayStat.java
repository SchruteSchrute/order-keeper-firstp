package com.example.mycoffeeshopposter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityWorkDayStat extends AppCompatActivity {

    int daySumNum, dayCountTables, dayCashAmount, dayTermAmount;

    TextView tvDaySum, tvDayTables, tvDayCash, tvDayTerm;

    DBHelper dbHelper;
    SQLiteDatabase db;
    LinearLayout llContainerMain, llContainerTableItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_workday_stat);
        llContainerMain = findViewById(R.id.tableCont);

        tvDayCash = findViewById(R.id.tvDayCash);
        tvDayTerm = findViewById(R.id.tvDayTerm);
        tvDaySum = findViewById(R.id.tvDaySum);
        tvDayTables = findViewById(R.id.tvDayTableCount);

        readTableStat(null);
    }

    public void deleteTableStat(View view) {
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.CO_TABLE_NAME, null, null);
        dbHelper.close();
        db.close();
        llContainerMain.removeAllViews();
        Toast.makeText(this, "История дня очищена", Toast.LENGTH_LONG).show();
    }

    public void readTableStat(View view) {
        daySumNum = 0;
        dayCashAmount = 0;
        dayTermAmount = 0;
        dayCountTables = 0;

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query(DBHelper.CO_TABLE_NAME, null, null, null, null, null, null);

        String nametableoritem, datetime;
        int priceSum, amount;
        int isCash, isTable;

        if (c.moveToFirst()) {
            LayoutInflater layoutInflater = getLayoutInflater();

            int IDisTable = c.getColumnIndex(DBHelper.CO_IS_TABLE);
            int IDisCash = c.getColumnIndex(DBHelper.CO_IS_CASH);
            int IDnamename = c.getColumnIndex(DBHelper.CO_NAME_NAME);
            int IDpricesum = c.getColumnIndex(DBHelper.CO_PRICE_SUM);
            int IDamount = c.getColumnIndex(DBHelper.CO_AMOUNT);
            int IDdatetime = c.getColumnIndex(DBHelper.CO_DATE_TIME);

            do {
                isTable = c.getInt(IDisTable);
                isCash = c.getInt(IDisCash);
                nametableoritem = c.getString(IDnamename);
                priceSum = c.getInt(IDpricesum);
                amount = c.getInt(IDamount);
                datetime = c.getString(IDdatetime);

                if (isTable == 1) {
                    dayCountTables++;

                    View tableHeader = layoutInflater.inflate(R.layout.stat_closed_table_header, llContainerMain, false);

                    ((TextView) tableHeader.findViewById(R.id.tvTablename)).setText(nametableoritem);
                    ((TextView) tableHeader.findViewById(R.id.tvTableSum)).setText(String.valueOf(priceSum));
                    ((TextView) tableHeader.findViewById(R.id.tvTableDateTime)).setText(datetime);

                    llContainerTableItem = tableHeader.findViewById(R.id.llcontainertable);

                    if (isCash ==1){
                        dayCashAmount += priceSum;
                        ((TextView) tableHeader.findViewById(R.id.tvIsCash)).setText("Нал.");
                    } else ((TextView) tableHeader.findViewById(R.id.tvIsCash)).setText("Терм."); dayTermAmount += priceSum;

                    llContainerMain.addView(tableHeader);

                } else {
                    View itemInfo = layoutInflater.inflate(R.layout.stat_item, llContainerTableItem, false);

                    ((TextView) itemInfo.findViewById(R.id.tvItemName)).setText(nametableoritem);
                    ((TextView) itemInfo.findViewById(R.id.tvItemSum)).setText(String.valueOf(priceSum*amount));
                    ((TextView) itemInfo.findViewById(R.id.tvItemCount)).setText(String.valueOf(amount));

                    llContainerTableItem.addView(itemInfo);
                }
            } while (c.moveToNext());
        } else Toast.makeText(this, "Пока ничего не продалось", Toast.LENGTH_LONG).show();

        dbHelper.close();
        db.close();
        c.close();

        tvDayTables.setText(String.valueOf(dayCountTables));
        tvDaySum.setText(String.valueOf(dayCashAmount + dayTermAmount));
        tvDayCash.setText(String.valueOf(dayCashAmount));
        tvDayTerm.setText(String.valueOf(dayTermAmount));
    }
}
