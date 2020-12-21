package main;

public class Carrot implements IMapElement, Comparable< IMapElement >{
    private final Vector2d position;
    private final float energy;
    Carrot(Vector2d position,float energy){
        this.position = position;
        this.energy = energy;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    @Override
    public float getEnergy() {
        return this.energy;
    }
    @Override
    public int compareTo(IMapElement o) {
        return Integer.compare(((int) this.getEnergy()), ((int) o.getEnergy()));
    }

}