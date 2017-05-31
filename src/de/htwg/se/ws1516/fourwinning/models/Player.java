package de.htwg.se.ws1516.fourwinning.models;

public class Player implements PlayerInterface
{
    String name;
    int zuege;
    boolean active;
    boolean gewonnen = false;
    
    public Player(String name, int zuege){
        this.name = name;
        this.zuege = 0;
    }

    public Player(){}
    
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void setActive(boolean zustand){
        active = zustand;
    }

    @Override
    public boolean getActive(){
        return active;
    }

    @Override
    public void chipSetted(){
        zuege++;
    }

    @Override
    public int getZuege(){
        return zuege;
    }

    public void resetZuege() {this.zuege = 0; }
}
