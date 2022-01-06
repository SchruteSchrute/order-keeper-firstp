package com.example.mycoffeeshopposter.objects;

public class Item {
    String name;
    int price;
    int amount;
    public void item (String name, int price) {
        this.name = name;
        this.price = price;
        this.amount = 1;
    }
    public String getNameItem(){
        return name;
    }
    public int getPriceItem(){
        return price;
    }
    public int getAmountItem(){
        return amount;
    }
    public void minusAmount(){
        amount--;
    }
    public void PlusAmount(){
        amount++;
    }
}
