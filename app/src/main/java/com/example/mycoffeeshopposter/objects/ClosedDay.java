package com.example.mycoffeeshopposter.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClosedDay{
    String date;
    int cashDaySum = 0;
    int termDaySum = 0;
    int daySum = 0;
    int tablesAmount = 0;
    Map<String, SaledItem> mapSaledItems;

    public void createClosedDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        date = sdf.format(new Date(System.currentTimeMillis()));
        mapSaledItems = new HashMap<>();
    }

    public void addSaledItem(SaledItem sItem) {
        if (mapSaledItems.containsKey(sItem.getName())){
            Objects.requireNonNull(mapSaledItems.get(sItem.getName())).plusAmount(sItem.amount);
            Objects.requireNonNull(mapSaledItems.get(sItem.getName())).plusSum(sItem.sumfinal);
        }{
            mapSaledItems.put(sItem.getName(), sItem);
        }
    }
    public void plusCashSum(int cash){
        cashDaySum +=cash;
    }
    public void plusTermSum(int term){
        termDaySum+=term;
    }

    public void addTableAmount(){
        tablesAmount++;
    }

    public int getDaySum() {
        return daySum = cashDaySum + termDaySum;
    }

    public int getCashDaySum() {
        return cashDaySum;
    }

    public int getTermDaySum() {
        return termDaySum;
    }

    public String getDate() {
        return date;
    }

    public Map<String, SaledItem> getSaledItems() {
        return mapSaledItems;
    }
}
