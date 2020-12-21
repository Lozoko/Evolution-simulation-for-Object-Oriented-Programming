package main;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class Statistics {
    private int numberOfAliveAnimals;
    private int numberOfCarrots;
    private int numberOfDeadAnimals;
    private float averageEnergyLevel;
    private float averageLifeTime;
    private float averageNumberOfChildren;
    private int[] dominatingGenotype;
    private int numberOfAnimalWithDominatingGenotype;
    public Statistics(){
        this.numberOfAliveAnimals = 0;
        this.numberOfCarrots = 0;
        this.numberOfDeadAnimals = 0;
        this.averageEnergyLevel = 0;
        this.averageLifeTime = 0;
        this.averageNumberOfChildren = 0;
    }
    public void update(List<Animal> animals, int numberOfCarrots, int totalLifeTimeCycle,
                       int totalNumberOfDeadAnimalsCycle, Pair<Animal, Integer> dominatingGenotype){
        this.numberOfAliveAnimals = animals.size();
        this.numberOfCarrots += numberOfCarrots;
        if(totalNumberOfDeadAnimalsCycle != 0){
            this.averageLifeTime = ((float)(this.averageLifeTime * this.numberOfDeadAnimals) + totalLifeTimeCycle) /
                    (float)(this.numberOfDeadAnimals + totalNumberOfDeadAnimalsCycle);
        }
        this.numberOfDeadAnimals += totalNumberOfDeadAnimalsCycle;
        float energySum = 0;
        int numberOfChildren = 0;
        for (Animal animal : animals){
            energySum += animal.getEnergy();
            numberOfChildren += animal.getChildren();
        }
        if(animals.size() == 0){
            this.averageEnergyLevel = 0;
            this.averageNumberOfChildren = 0;
        }
        else{
            this.averageEnergyLevel = energySum/animals.size();
            this.averageNumberOfChildren = (float)numberOfChildren / (float)(animals.size());
        }
        if(dominatingGenotype == null){
            this.dominatingGenotype = new int[]{0};
            this.numberOfAnimalWithDominatingGenotype = 0;
        }
        else{
            this.dominatingGenotype = dominatingGenotype.getKey().getGenotype();
            this.numberOfAnimalWithDominatingGenotype = dominatingGenotype.getValue();
        }
    }

    public int getNumberOfAliveAnimals() {
        return numberOfAliveAnimals;
    }

    public int getNumberOfCarrots() {
        return numberOfCarrots;
    }

    public float getAverageEnergyLevel() {
        return averageEnergyLevel;
    }

    public float getAverageLifeTime() {
        return averageLifeTime;
    }

    public float getAverageNumberOfChildren() {
        return averageNumberOfChildren;
    }

    public int[] getDominatingGenotype() {
        return dominatingGenotype;
    }

    public int getNumberOfAnimalWithDominatingGenotype() {
        return numberOfAnimalWithDominatingGenotype;
    }
}
