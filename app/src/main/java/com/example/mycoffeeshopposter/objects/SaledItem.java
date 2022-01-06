package com.example.mycoffeeshopposter.objects;

public class SaledItem {
    int amount= 0;
    int sumfinal =0;
    String name;

    public void createItem (String name, int amount, int sum) {
        this.amount = amount;
        sumfinal = sum;
        this.name = name;
    }

    void plusAmount (int am){
        amount += am;
    }
    void plusSum(int sum){
        sumfinal += sum;
    }

    public int getAmount() {
        return amount;
    }

    public int getSumfinal() {
        return sumfinal;
    }

    public String getName() {
        return name;
    }
}
