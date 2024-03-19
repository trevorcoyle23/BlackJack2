package edu.utsa.cs3443.blackjack2.model;

public class Player {
    private String name;
    private int chipCount;
    public Player(String name, int chipCount) {
        this.name = name;
        this.chipCount = chipCount;
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void setChipCount(int chipCount) {
        this.chipCount += chipCount;
    }

    public void betChips(int chipBet) {
        this.chipCount -= chipBet;
    }

    @Override
    public String toString() {
        return "Player: " + name + ", $ " + chipCount;
    }
}
