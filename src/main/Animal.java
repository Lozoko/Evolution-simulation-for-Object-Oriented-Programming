package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Animal implements IMapElement, Comparable< IMapElement >{
    private MapDirection orientation;
    private Vector2d position;
    private final IWorldMap map;
    private final List<IPositionChangeObserver> observers = new LinkedList<>();
    private final Vector2d leftmost;
    private final Vector2d upright;
    private final int[] genotype;
    private float energy;
    private final float moveEnergyCost;
    private int lifeTime;
    private int numberOfChildren;
    private boolean underObservation;
    private boolean mainAncestor;
    Animal(IWorldMap map, Vector2d initialPosition, int[] genotype, float energy, float moveEnergyCost){
        this.map = map;
        this.position = initialPosition;
        this.orientation = Randoms.randomMapDirection();
        this.upright = this.map.getUpright();
        this.leftmost = this.map.getLowermost();
        this.genotype = genotype;
        this.energy = energy;
        this.moveEnergyCost = moveEnergyCost;
        this.lifeTime = 1;
        this.numberOfChildren = 0;
        this.underObservation = false;
        this.mainAncestor = false;
        this.map.place(this);
        this.addObserver((IPositionChangeObserver)this.map);
    }
    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    @Override
    public float getEnergy() {
        return this.energy;
    }

    public void setEnergy(float energy) {
        if(energy == 0)
            this.energy -= this.energy/4;
        else
            this.energy += energy;
    }
    public int[] getGenotype(){
        return this.genotype;
    }
    public int getLifeTime(){return this.lifeTime;}
    public void addChildren(){this.numberOfChildren++;}
    public int getChildren(){return this.numberOfChildren;}
    public void startObservation(){this.underObservation = true;}
    public void stopObservation(){this.underObservation = false;}
    public boolean isObserved(){return this.underObservation;}
    public void setMainAncestor(){this.mainAncestor = true;}
    public void cancelMainAncestor(){this.mainAncestor = false;}
    public boolean isMainAncestor(){return this.mainAncestor;}

    private void moveForward(){
        Vector2d positionChanged = this.getPosition();
        Vector2d f = this.position.add(this.orientation.toUnitVector());
        if(f.x>this.upright.x)
            f = new Vector2d(this.leftmost.x,f.y);
        if(f.y>this.upright.y)
            f = new Vector2d(f.x,this.leftmost.y);
        if(f.x<this.leftmost.x)
            f = new Vector2d(this.upright.x,f.y);
        if(f.y<this.leftmost.y)
            f = new Vector2d(f.x,this.upright.y);
        this.position = f;
        this.positionChanged(positionChanged, this.getPosition());

    }

    public void move(){
        this.energy -= this.moveEnergyCost;
        this.lifeTime++;
        MoveDirection direction = Randoms.randomDirection(this.genotype);
        switch (direction){
            case FORWARDRIGHT:
                this.orientation = this.orientation.next();
                this.moveForward();
                break;
            case RIGHT:
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.moveForward();
                break;
            case LEFT:
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                this.moveForward();
                break;
            case FORWARD:
                this.moveForward();
                break;
            case BACKWARDRIGHT:
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.moveForward();
                break;
            case BACKWARD:
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.moveForward();
                break;
            case BACKWARDLEFT:
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                this.moveForward();
                break;
            case FORWARDLEFT:
                this.orientation = this.orientation.previous();
                this.moveForward();
                break;
        }
    }
    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observers : this.observers) observers.positionChanged(oldPosition, newPosition, this);
    }
    public Vector2d getPosition(){
        return this.position;
    }
    @Override
    public int compareTo(IMapElement o) {
        return Integer.compare(((int) this.getEnergy()), ((int) o.getEnergy()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Arrays.equals(genotype, animal.getGenotype());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genotype);
    }
}
