package com.news.entity;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CardEntity {
    private String card_number;
    private String card_balance;
    private String balance_time;
    private String card_validity;
    private String current_time;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_balance() {
        return card_balance;
    }

    public void setCard_balance(String card_balance) {
        this.card_balance = card_balance;
    }

    public String getBalance_time() {
        return balance_time;
    }

    public void setBalance_time(String balance_time) {
        this.balance_time = balance_time;
    }

    public String getCard_validity() {
        return card_validity;
    }

    public void setCard_validity(String card_validity) {
        this.card_validity = card_validity;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
