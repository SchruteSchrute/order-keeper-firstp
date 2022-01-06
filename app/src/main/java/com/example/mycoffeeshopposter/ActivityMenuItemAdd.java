package com.example.mycoffeeshopposter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycoffeeshopposter.objects.Item;

import java.util.ArrayList;

public class ActivityMenuItemAdd extends AppCompatActivity {
    EditText edName, edPrice;
    String addNAME, addPricestr, addGroup;
    int addPrice;
    DBHelper dbHelper;
    SQLiteDatabase db;

    String[] groups = {
            "Напитки",
            "Добавки",
            "Десерты",
            "Выпечка",
            "Солёное",
            "Другое"};

    Spinner spinGroup;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        edName = findViewById(R.id.editName);
        edPrice = findViewById(R.id.editPrice);
        spinGroup = findViewById(R.id.spinGroup);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinGroup.setAdapter(adapter);
        spinGroup.setSelection(0);
        spinGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addGroup = groups[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void addNamePrice (View view){
        addNAME = edName.getText().toString();
        addPricestr = edPrice.getText().toString();
        if (isDataCorrect()){
            dbHelper = new DBHelper(this);

            addPrice = Integer.parseInt(addPricestr);

            ContentValues cv = new ContentValues();

            db = dbHelper.getWritableDatabase();

            cv.put(DBHelper.NAME, addNAME);
            cv.put(DBHelper.PRICE, addPrice);
            cv.put(DBHelper.GROUP, addGroup);

            db.insert(DBHelper.TABLE_NAME, null, cv);

            Toast.makeText(this, "Пункт добавлен: " + addNAME + " "
                    + addPrice, Toast.LENGTH_LONG).show();

            cv.clear();
            dbHelper.close();
            db.close();

        } else  Toast.makeText(this, "Некорректные данные", Toast.LENGTH_LONG).show();
    }
    private boolean isDataCorrect (){
        return !(!TextUtils.isDigitsOnly(addPricestr) || addNAME == null || addPricestr == null || addGroup == null ||
                addNAME.isEmpty() || addPricestr.isEmpty() || addGroup.isEmpty());
    }

    public void deleteAll(View view) {
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, null, null);
        Toast.makeText(this, "Меню удалено", Toast.LENGTH_SHORT).show();
        dbHelper.close();
        db.close();
    }
}

