package edu.utsa.cs3443.blackjack2.model;



public class Card {
    private String suit;
    private String value;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "_of_" + suit;
    }

    public int getValue() {
        switch(value) {
            case "ace":
                return 11;
            case "king":
            case "queen":
            case "jack":
            case "ten":
                return 10;
            case "nine":
                return 9;
            case "eight":
                return 8;
            case "seven":
                return 7;
            case "six":
                return 6;
            case "five":
                return 5;
            case "four":
                return 4;
            case "three":
                return 3;
            case "two":
                return 2;
            default:
                return 0;
        }
    }

    public boolean isAce() {
        return value == "ace";
    }
}
