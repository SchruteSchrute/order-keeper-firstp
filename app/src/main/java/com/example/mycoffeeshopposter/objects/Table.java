package com.example.mycoffeeshopposter.objects;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Table {
    String datetimeClosed;
    String tableName;
    boolean isCash;
    int summary = 0;
    Map<String, Item> mapItems;

    public void createTable (String tableName){
        mapItems = new HashMap<>();
        this.tableName = tableName;
    }
    public int getSummary(){
        return summary;
    }
    public void cash (boolean cash){
        isCash = cash;
    }
    public boolean isCash (){
        return isCash;
    }
    public String getName (){
        return tableName;
    }
    public String getDateTime() {return datetimeClosed;}


    public boolean containsItem (Item item){
        return mapItems.containsKey(item.name);
    }

    public void minusItem (Item item) {
        String name = item.name;
        Objects.requireNonNull(mapItems.get(name)).minusAmount();
        summary = summary - (Objects.requireNonNull(mapItems.get(name))).price;
        if ((Objects.requireNonNull(mapItems.get(name))).amount <= 0) {
            mapItems.remove(name);
        }
    }

    public void plusItem(Item item){
        summary = summary + item.price;
        if (mapItems.containsKey(item.name)){
            (Objects.requireNonNull(mapItems.get(item.name))).PlusAmount();
        } else{
            Item newItem = new Item();
            newItem.item(item.name, item.price);
            mapItems.put(item.name, newItem);
        }
    }

    public void addDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM.YYYY");
        datetimeClosed = sdf.format(new Date(System.currentTimeMillis()));
    }
    public int getItemAmount (Item currentItem){
        return  Objects.requireNonNull(mapItems.get(currentItem.name)).amount;
    }
    public boolean isEmptyTable (){
        return mapItems.isEmpty();
    }

    public Map<String, Item> getMapItem() {
        return mapItems;
    }
}
