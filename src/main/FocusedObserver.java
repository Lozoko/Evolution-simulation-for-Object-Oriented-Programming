package main;

public class FocusedObserver {
    private int numberOfChildren;
    private int numberOfDescendants;
    private int dayOfDeath;
    FocusedObserver(){
        this.numberOfChildren = 0;
        this.numberOfDescendants = 0;
        this.dayOfDeath = -1;
    }
    public void addChildren(){
        this.numberOfChildren++;
    }
    public void addDescendant(){
        this.numberOfDescendants++;
    }
    public void setDayOfDead(int day){
        this.dayOfDeath = day;
    }

    public int getDayOfDeath() {
        return this.dayOfDeath;
    }

    public int getNumberOfDescendants() {
        return this.numberOfDescendants;
    }

    public int getNumberOfChildren() {
        return this.numberOfChildren;
    }
}
