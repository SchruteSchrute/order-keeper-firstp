package com.example.mycoffeeshopposter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycoffeeshopposter.objects.ClosedDay;
import com.example.mycoffeeshopposter.objects.Item;
import com.example.mycoffeeshopposter.objects.MenuGroupContainer;
import com.example.mycoffeeshopposter.objects.SaledItem;
import com.example.mycoffeeshopposter.objects.Table;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    // вся хуйня что касается столов
    View groupSel = null;

    Item currentItem;
    Table currentTable;
    Button btnMinus;
    RelativeLayout btnExpand;
    int TABLE_COUNTER = 1;
    TextView tvChild, tvCounter, tvParent;

    int deleteID;
    String TAG;
    String LOG_TAG = "logTAG";

    LinearLayout lLcontainer, llMenuMainCont;
    ConstraintLayout lLMenuContainer, clMenuGroupCont;
    TextView tvbtnName, tvbtnPrice;
    ImageView imVbtnItem;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Dialog dialog;

    Map<String, MenuGroupContainer> menuGroupKeeper;
    TextView tvGroupName;
    MenuGroupContainer MGC;
    Flow flowGroup;

    Map<String, SaledItem> mapClosedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        dialog = new Dialog(this);
        menuGroupKeeper = new HashMap<>();

        lLcontainer = findViewById(R.id.table_conteiner);
        llMenuMainCont = findViewById(R.id.llMainMenuCont);

        Log.d(LOG_TAG, "====== ==================== ======");
    }
    public void addItem (View view) {
        Intent intent = new Intent(this, ActivityMenuItemAdd.class);
        startActivity(intent);
    }
    public void showStat (View view) {
        Intent intent = new Intent(this, ActivityWorkDayStat.class);
        startActivity(intent);
    }
    //------------------------------- метод создания вью в меню, взятых с базы данных. Нужно перенести движуху в дбМенеджер
    private void createMenu(){
        llMenuMainCont.removeAllViews();
        menuGroupKeeper.clear();
        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            String itemGroup;
            String itemName;
            int itemPrice;
            int itemID;

            int IDgroup = cursor.getColumnIndex(DBHelper.GROUP);
            int IDname = cursor.getColumnIndex(DBHelper.NAME);
            int IDprice = cursor.getColumnIndex(DBHelper.PRICE);
            int ID = cursor.getColumnIndex("id");

            do {
                itemGroup = cursor.getString(IDgroup);
                itemName = cursor.getString(IDname);
                itemPrice = cursor.getInt(IDprice);
                itemID = cursor.getInt(ID);
                Item item = new Item();
                item.item(itemName, itemPrice);

                if (menuGroupKeeper.containsKey(itemGroup)){
                    MGC = menuGroupKeeper.get(itemGroup);
                    addItemToScreen(item, MGC, itemID);
                } else {
                    LayoutInflater inflaterGroup = getLayoutInflater();
                    View groupKeeper = inflaterGroup.inflate(R.layout.menu_group_container, llMenuMainCont, false);
                    clMenuGroupCont = groupKeeper.findViewById(R.id.clMenuGroupContainer);
                    flowGroup = groupKeeper.findViewById(R.id.widgetFlowGroup);
                    MenuGroupContainer mgc = new MenuGroupContainer();
                    mgc.makeBox(flowGroup, clMenuGroupCont);
                    menuGroupKeeper.put(itemGroup, mgc);

                    tvGroupName = groupKeeper.findViewById(R.id.tvGroupName);
                    tvGroupName.setText(itemGroup);

                    llMenuMainCont.addView(groupKeeper);
                    addItemToScreen(item, mgc, itemID);
                }

            } while (cursor.moveToNext());
        } else Toast.makeText(this, "=== MENU IS EMPTY ===", Toast.LENGTH_SHORT).show();

        dbHelper.close();
        db.close();
        cursor.close();
    }
    private void addItemToScreen(Item item, MenuGroupContainer menuGroupContainer, int dbID){
        clMenuGroupCont = menuGroupContainer.getClGroup();
        flowGroup = menuGroupContainer.getFlowGroup();
        LayoutInflater inflater = getLayoutInflater();
        View itemView = inflater.inflate(R.layout.btn_item_menu, clMenuGroupCont, false);
        itemView.setId(View.generateViewId());

        tvbtnName = itemView.findViewById(R.id.tvNamebtnItem);
        tvbtnName.setText(item.getNameItem());

        tvbtnPrice = itemView.findViewById(R.id.tvPricebtnItem);
        tvbtnPrice.setText(String.valueOf(item.getPriceItem()));

        imVbtnItem = itemView.findViewById(R.id.btnImageItem);
        imVbtnItem.setTag(R.id.tagItemKey, item);
        imVbtnItem.setTag(R.id.tagIDkey, dbID);
        registerForContextMenu(imVbtnItem);

        clMenuGroupCont.addView(itemView);
        flowGroup.addView(itemView);
    }

    private void createMenuOld(){
        lLMenuContainer.removeAllViews();
        LayoutInflater inflaterMenu = getLayoutInflater();
        View inflaterFlowView = inflaterMenu.inflate(R.layout.widget_flow_menu,
                lLMenuContainer, false);

        lLMenuContainer.addView(inflaterFlowView);
     //   widgetFlow = findViewById(R.id.widgetFlow);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
 //       dbHelper.createTableStat(db);
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()){

            String tagName;
            int tagPrice;
            int tagID;

            int IDname = cursor.getColumnIndex(DBHelper.NAME);
            int IDprice = cursor.getColumnIndex(DBHelper.PRICE);
            int ID = cursor.getColumnIndex("id");

            do {
                tagID = cursor.getInt(ID);
                tagName = cursor.getString(IDname);
                tagPrice = cursor.getInt(IDprice);

                Item item = new Item();
                item.item(tagName, tagPrice);

                View menuItemBtn = inflaterMenu.inflate(R.layout.btn_item_menu, lLMenuContainer, false);
                menuItemBtn.setId(View.generateViewId());

                tvbtnName = menuItemBtn.findViewById(R.id.tvNamebtnItem);
                tvbtnName.setText(tagName);

                tvbtnPrice = menuItemBtn.findViewById(R.id.tvPricebtnItem);
                tvbtnPrice.setText(String.valueOf(tagPrice));

                imVbtnItem = menuItemBtn.findViewById(R.id.btnImageItem);
                imVbtnItem.setTag(R.id.tagItemKey, item);
                imVbtnItem.setTag(R.id.tagIDkey, tagID);
                registerForContextMenu(imVbtnItem);

                lLMenuContainer.addView(menuItemBtn);
            //    widgetFlow.addView(menuItemBtn);
            } while (cursor.moveToNext());

            Toast.makeText(this, "--- MENU CREATED ---", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "--- NO ITEM FOUND ---", Toast.LENGTH_SHORT).show();

        dbHelper.close();
        db.close();
        cursor.close();
    }

    //-------------------   контекстное меню значится. Нужно дописать изменение имени и перенести методы в дбМенеджер

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        deleteID = (int) v.getTag(R.id.tagIDkey);
        currentItem = (Item) v.getTag(R.id.tagItemKey);

        menu.add(0, 1, 0, "Удалить");
        menu.add(0,2,0,"Переименовать");
        menu.add(0,3, 0,"Добавить пункт в меню");

    }
    public boolean onContextItemSelected(MenuItem item){
        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        switch (item.getItemId()) {
            case 1:
                db.execSQL("DELETE FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.NAME + " LIKE'" + currentItem.getNameItem() + "';");
                dbHelper.close();
                db.close();
                createMenu();
                Toast.makeText(this, "--- ITEM: " + currentItem.getNameItem() + " DELETED ---", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // TODO дописать замену
                AlertDialog.Builder alDialChangeName = new AlertDialog.Builder(this);
                final EditText editTextNewname = new EditText(this);
                editTextNewname.setText(currentItem.getNameItem());
                editTextNewname.setInputType(InputType.TYPE_CLASS_TEXT);
                alDialChangeName.setView(editTextNewname);

                alDialChangeName.setPositiveButton("Сохранить", (dialog, which) -> {
                    String newname = editTextNewname.getText().toString();
                    // НЕ РАБОТАЕТ ЗАМЕНА ИМЕНИ
                    db.execSQL("ALTER TABLE'" + DBHelper.TABLE_NAME + "' RENAME COLUMN'" + TAG + "' TO "+ newname);
                    dbHelper.close();
                    db.close();
                    createMenu();

                });

                alDialChangeName.setNegativeButton("Отмена", (dialog, which) -> {
                    dbHelper.close();
                    db.close();
                });
                alDialChangeName.show();
                break;

            case 3:
                addItem(null);
                break;

        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onResume() {
        createMenu();
        super.onResume();
    }
 //========================================================
    public void groupSelected(View view){
        if (groupSel==null) {
            scaleGroup(view, true);
            groupSel = view;
        } else if (groupSel.equals(view)) {
            scaleGroup(view, false);
            groupSel = null;
        } else {
            scaleGroup(groupSel, false);
            scaleGroup(view, true);
            groupSel = view;
        }
    }
    public void scaleGroup(View view, boolean isSelected){
        ViewGroup parentItems = ((ViewGroup) view.getParent().getParent()).findViewById(R.id.llParentItem);
        View parentView = (View) view.getParent();
        if (isSelected){
            parentItems.setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.drawable.ic_closetable_icon);
            parentView.setBackgroundResource(R.drawable.ic_openedtable_background);
            parentView.setScaleX((float) 1.0);
            parentView.setScaleY((float) 1.0);
        }else {
            parentItems.setVisibility(View.GONE);
            view.setBackgroundResource(R.drawable.ic_opentable_icon);
            parentView.setBackgroundResource(R.drawable.ic_closedtable_background);
            parentView.setScaleX((float) 0.95);
            parentView.setScaleY((float) 0.95);
        }
    }
    public void addChild(View view) {
        currentItem = (Item) view.getTag(R.id.tagItemKey);
        if (groupSel==null){
            addGroup();
        } else {
            addItemNorm();
        }
    }
    public void minusCounter(View view) {
        currentItem = (Item) view.getTag(R.id.tagItemKey);
        TextView tvMinus;
        ViewGroup parentView;
        parentView = (ViewGroup) view.getParent();
        tvMinus = parentView.findViewById(R.id.tvItemCount);

        ((Table) groupSel.getTag(R.id.tagTableConsistOf)).minusItem(currentItem);
        ((TextView)((ViewGroup)groupSel.getParent()).findViewById(R.id.tvsum)).setText(String.valueOf(currentTable.getSummary()));

        if (!currentTable.containsItem(currentItem)) {
            ((ViewGroup) parentView.getParent()).removeView(parentView);
            if (currentTable.isEmptyTable()) {
                lLcontainer.removeView((View)groupSel.getParent().getParent());
                groupSel = null;
            }
        } else {
            tvMinus.setText(String.valueOf(currentTable.getItemAmount(currentItem)));
        }
    }
    public void addGroup(){
        // делаем держатель заказов, добавляем пункт
        Table table = new Table();
        table.createTable("Стол: " + TABLE_COUNTER);
        // здесь вью и все дела
        LayoutInflater inflater = getLayoutInflater();
        View groupItem = inflater.inflate(R.layout.table_box, lLcontainer, false);
        tvParent = groupItem.findViewById(R.id.tvParent);
        tvParent.setText(table.getName());
        btnExpand = groupItem.findViewById(R.id.btnExpand);
        btnExpand.setTag(R.id.tagTableConsistOf, table);
        // лепим держатель во вью стола
        TABLE_COUNTER++;
        groupSel = btnExpand;
        lLcontainer.addView(groupItem);
        addItemNorm();
    }
    public void addItemNorm(){
        currentTable = (Table) groupSel.getTag(R.id.tagTableConsistOf);
        if (currentTable.containsItem(currentItem)) {

            currentTable.plusItem(currentItem);
            groupSel.setTag(R.id.tagTableConsistOf, currentTable);
            ((TextView)((ViewGroup)groupSel.getParent()).findViewById(R.id.tvsum)).setText(String.valueOf(currentTable.getSummary()));

            ((TextView)((ViewGroup)groupSel.getParent()
                    .getParent())
                    .findViewWithTag(currentItem.getNameItem()))
                    .setText(String.valueOf(currentTable.getItemAmount(currentItem)));

        } else {
            currentTable.plusItem(currentItem);
            ((TextView)((ViewGroup)groupSel.getParent()).findViewById(R.id.tvsum)).setText(String.valueOf(currentTable.getSummary()));
            groupSel.setTag(R.id.tagTableConsistOf, currentTable);

            ViewGroup parentItem = ((ViewGroup) groupSel.getParent().getParent()).findViewById(R.id.llParentItem);
            // создаем вью пункта
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(R.layout.table_item_layout, parentItem, false);
            tvChild = item.findViewById(R.id.tvItemName);
            tvChild.setText(currentItem.getNameItem());
            tvCounter = item.findViewById(R.id.tvItemCount);
            tvCounter.setText(String.valueOf(currentItem.getAmountItem()));
            tvCounter.setTag(currentItem.getNameItem());
            btnMinus = item.findViewById(R.id.btnMinus);
            btnMinus.setTag(R.id.tagItemKey, currentItem);
            parentItem.addView(item);
        }
    }
    public void removeTableGroupView (View view){
        ViewGroup parentView;
        parentView = (ViewGroup) view.getParent().getParent();
        if ((parentView.findViewById(R.id.btnExpand)).equals(groupSel)) {
            groupSel = null;
            currentTable = null;
        }
        lLcontainer.removeView(parentView);
    }
    @SuppressLint("NonConstantResourceId")
    public void tableCloseDelete(View view){
        Button btnOK, btnCancel, btnCash, btnTerm;
        TextView tvName;
        Table table = (Table) (((ViewGroup)view.getParent()).findViewById(R.id.btnExpand)).getTag(R.id.tagTableConsistOf);
        switch (view.getId()) {
            case R.id.btnDelTable:
                dialog.setContentView(R.layout.dialog_delete_table);
                tvName = dialog.findViewById(R.id.tvDelTabName);
                tvName.setText(table.getName());
                btnOK = dialog.findViewById(R.id.btnOK);
                btnCancel = dialog.findViewById(R.id.btnCancel);
                btnOK.setOnClickListener(v -> {
                    removeTableGroupView(view);
                    Toast.makeText(this, table.getName() + " Удален", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
                btnCancel.setOnClickListener(v -> dialog.cancel());
                dialog.show();
                break;
            case R.id.btnCloseTable:
                dialog.setContentView(R.layout.dialog_close_table);
                tvName = dialog.findViewById(R.id.tvDelTabName);
                tvName.setText(table.getName());
                btnCash = dialog.findViewById(R.id.btnCash);
                btnTerm = dialog.findViewById(R.id.btnTerm);
                btnCancel = dialog.findViewById(R.id.btnCancel);
                btnCash.setOnClickListener(v -> {
                    table.cash(true);
                    dialog.cancel();
                    removeTableGroupView(view);
                    saveTable(table);
                });
                btnTerm.setOnClickListener(v -> {
                    removeTableGroupView(view);
                    table.cash(false);
                    saveTable(table);
                    dialog.cancel();
                });
                btnCancel.setOnClickListener(v -> dialog.cancel());
                dialog.show();
                break;
            default:
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }
    public void saveTable (Table table){
        table.addDateTime();
          DBHelper dbHelper = new DBHelper(this);
          db = dbHelper.getWritableDatabase();
          ContentValues cv = new ContentValues();

          cv.put(DBHelper.CO_IS_TABLE, true);
          cv.put(DBHelper.CO_IS_CASH, table.isCash());
          cv.put(DBHelper.CO_NAME_NAME, table.getName());
          cv.put(DBHelper.CO_PRICE_SUM, table.getSummary());
          cv.put(DBHelper.CO_DATE_TIME, table.getDateTime());

          db.insert(DBHelper.CO_TABLE_NAME, null, cv);
        Map<String, Item> map = table.getMapItem();
       Collection<Item> col = map.values();
        for (Item item : col) {
           cv.clear();
           cv.put(DBHelper.CO_IS_TABLE, false);
           cv.put(DBHelper.CO_NAME_NAME, item.getNameItem());
           cv.put(DBHelper.CO_PRICE_SUM, item.getPriceItem());
           cv.put(DBHelper.CO_AMOUNT, item.getAmountItem());
           db.insert(DBHelper.CO_TABLE_NAME, null, cv);
       }
        cv.clear();
        dbHelper.close();
        db.close();
        Toast.makeText(this, "сохранен стол" + table.getName() + " summary: " + table.getSummary(), Toast.LENGTH_SHORT).show();
    }

    // TODO дописать закрытие дня
    //  запись статистики дня в следующие таблицы
    //  СУММАРНО ДЕНЬ + МЕСЯЦ
    //  СУММАРНО ПУНКТЫ
    //  СУММАРНО ПУНКТЫ В МЕСЯЦ
    private void closeCurrentDay(View view) {
        // создаем обьект закрытого дня
        // в него добавим количество столов, нал/терм, и инфу заказов - названия, количество и сумма
        ClosedDay todayClosed = new ClosedDay();
        todayClosed.createClosedDay();

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query(DBHelper.CO_TABLE_NAME, null,null,null,null,null,null);

        String nametableoritem;
        int priceSum, amount;
        int isCash, isTable;

        if (c.moveToFirst()) {

            int IDisTable = c.getColumnIndex(DBHelper.CO_IS_TABLE);
            int IDisCash = c.getColumnIndex(DBHelper.CO_IS_CASH);
            int IDnamename = c.getColumnIndex(DBHelper.CO_NAME_NAME);
            int IDpricesum = c.getColumnIndex(DBHelper.CO_PRICE_SUM);
            int IDamount = c.getColumnIndex(DBHelper.CO_AMOUNT);

            do {
                isTable = c.getInt(IDisTable);
                isCash = c.getInt(IDisCash);
                nametableoritem = c.getString(IDnamename);
                priceSum = c.getInt(IDpricesum);
                amount = c.getInt(IDamount);

                if (isTable == 1) {
                    // инфа стола
                    // добавляем инфу стола в закрытый день
                    // со стола нужен кеш/терминал и все
                    todayClosed.addTableAmount();
                    if (isCash ==1){
                        todayClosed.plusCashSum(priceSum);
                        // кеш естественно
                    } else {
                        todayClosed.plusTermSum(priceSum);
                        // и терминал
                    }
                } else {
                    //инфа пункта
                    // создаем обьект проданного пункта из количества, цены и имени
                    // добавляем это в закрытый день
                    SaledItem sItem = new SaledItem();
                    int itemSum = amount * priceSum;
                    sItem.createItem(nametableoritem, amount, itemSum);
                    todayClosed.addSaledItem(sItem);

                }
            } while (c.moveToNext());

            // здесь нужно передать обьект стола в метод, что распарсит его по таблицам статистики в БД
            c.close();
            saveClosedDayStat(todayClosed);

        } else {
            Toast.makeText(this, "Пока ничего не продалось", Toast.LENGTH_LONG).show();

            dbHelper.close();
            db.close();
            c.close();
        }
    }
    void saveClosedDayStat(ClosedDay closedDay){
        ContentValues cv = new ContentValues();
        mapClosedDay = closedDay.getSaledItems();
    }
}
