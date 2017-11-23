package de.htwg.se.ws1516.fourwinning.models;

public class Player implements PlayerInterface
{
    String name;
    int zuege;
    boolean active;
    boolean gewonnen = false;
    private int id;

    public Player(String name, int zuege, int id){
        this.name = name;
        this.zuege = 0;
        this.id = id;
    }

    public Player(){}
    
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void setName(String name){
        this.name = name;
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

    @Override
    public int getIdentification(){
        return this.id;
    }
}
