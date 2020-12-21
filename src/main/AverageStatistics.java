package main;

import javafx.util.Pair;

import java.util.List;

public class AverageStatistics {
    private int totalNumberOfAliveAnimals;
    private int totalNumberOfCarrots;
    private int numberOfDeadAnimals;
    private float sumOfAverageEnergyLevel;
    private float averageLifeTime;
    private float sumOfAverageNumberOfChildren;
    private int[] dominatingGenotype;
    private int numberOfAnimalWithDominatingGenotype;
    private int numberOfCycle;
    AverageStatistics(){
        this.numberOfDeadAnimals = 0;
        this.sumOfAverageEnergyLevel = 0;
        this.averageLifeTime = 0;
        this.sumOfAverageNumberOfChildren = 0;
        this.totalNumberOfAliveAnimals = 0;
        this.totalNumberOfCarrots = 0;
    }
    public void update(List<Animal> animals, int numberOfCarrots, int totalLifeTimeCycle,
                       int totalNumberOfDeadAnimalsCycle, Pair<Animal, Integer> dominatingGenotype,
                       int numberOfCycle){
        this.numberOfCycle = numberOfCycle;
        this.totalNumberOfAliveAnimals += animals.size();
        this.totalNumberOfCarrots += numberOfCarrots;
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
        if(animals.size() != 0){
            this.sumOfAverageEnergyLevel += energySum/animals.size();
            this.sumOfAverageNumberOfChildren += (float)numberOfChildren / (float)(animals.size());
        }
        if(dominatingGenotype == null){
            this.dominatingGenotype = new int[]{0};
            this.numberOfAnimalWithDominatingGenotype = 0;
        }
        else if(this.numberOfAnimalWithDominatingGenotype < dominatingGenotype.getValue()){
            this.dominatingGenotype = dominatingGenotype.getKey().getGenotype();
            this.numberOfAnimalWithDominatingGenotype = dominatingGenotype.getValue();
        }
    }

    public float getAverageNumberOfAliveAnimals() {
        return (float)totalNumberOfAliveAnimals/this.numberOfCycle;
    }

    public float getAverageNumberOfCarrots() {
        return (float)totalNumberOfCarrots/this.numberOfCycle;
    }

    public float getAverageEnergyLevel() {
        return sumOfAverageEnergyLevel/this.numberOfCycle;
    }

    public float getAverageLifeTime() {
        return averageLifeTime;
    }

    public float getAverageNumberOfChildren() {
        return sumOfAverageNumberOfChildren/this.numberOfCycle;
    }

    public int[] getDominatingGenotype() {
        return dominatingGenotype;
    }

    public int getNumberOfAnimalWithDominatingGenotype() {
        return numberOfAnimalWithDominatingGenotype;
    }
}
