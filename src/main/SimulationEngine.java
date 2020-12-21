package main;

import javafx.util.Pair;
import java.util.*;
import java.util.stream.Collectors;

class SimulationEngine implements IEngine{
    private final IWorldMap map;
    private final List<Animal> animals;
    private final Statistics statisticsForMap;
    private final AverageStatistics averageStatisticsForMap;
    private int numberOfNewCarrots;
    private final int CarrotEnergy;
    private final int startEnergy;
    private final int moveEnergy;
    private int numberOfCycle;
    private int totalLifeTimeCycle;
    private int totalNumberOfDeadAnimalsCycle;
    private final Map<Animal, Pair<Animal,Integer>> genotypeMap;
    private FocusedObserver focusedObserver;
    public SimulationEngine(IWorldMap map, int startEnergy, int moveEnergy, int plantEnergy, int initialNumber){
        this.map = map;
        this.genotypeMap = new HashMap<>();
        this.animals = new LinkedList<>();
        this.startEnergy = startEnergy;
        this.CarrotEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.initialAnimals(initialNumber);
        this.statisticsForMap = new Statistics();
        this.averageStatisticsForMap = new AverageStatistics();
        Pair<Animal, Integer> dominatingGenotype = null;
        if(this.genotypeMap.size() != 0) {
            Map<Animal, Pair<Animal,Integer>> result = sortByValueCount(this.genotypeMap);
            dominatingGenotype = result.entrySet().iterator().next().getValue();
        }
        this.statisticsForMap.update(this.animals, 0,0,
                0,dominatingGenotype);
        this.averageStatisticsForMap.update(this.animals, 0,0,
                0,dominatingGenotype,0);
    }

    @Override
    public void run() {
        this.totalLifeTimeCycle = 0;
        this.totalNumberOfDeadAnimalsCycle = 0;
        this.numberOfNewCarrots = 0;
        this.cleanDeadAnimals();
        this.move();
        this.eatingPhase();
        this.breeding();
        this.addCarrots();
        this.numberOfCycle++;
        Pair<Animal, Integer> dominatingGenotype = this.getDominatingGenotype();
        this.statisticsForMap.update(this.animals,this.numberOfNewCarrots, this.totalLifeTimeCycle,
                this.totalNumberOfDeadAnimalsCycle, dominatingGenotype);
        this.averageStatisticsForMap.update(this.animals,this.statisticsForMap.getNumberOfCarrots(), this.totalLifeTimeCycle,
                this.totalNumberOfDeadAnimalsCycle, dominatingGenotype,this.numberOfCycle);
    }
    private void cleanDeadAnimals(){
        int l = this.animals.size();
        for (int i = 0; i < l; i++) {
            Animal animal = this.animals.get(i % l);
            if (animal.getEnergy() <= 0) {
                this.totalLifeTimeCycle += animal.getLifeTime();
                this.totalNumberOfDeadAnimalsCycle++;
                if(animal.isMainAncestor()){
                    this.focusedObserver.setDayOfDead(this.numberOfCycle);
                }
                this.map.deleteDeadAnimal(animal);
                this.animals.remove(i);
                Pair<Animal,Integer> pair = this.genotypeMap.get(animal);
                Pair<Animal,Integer> newPair = new Pair<Animal,Integer>(pair.getKey(),pair.getValue()-1);
                this.genotypeMap.remove(animal);
                this.genotypeMap.put(animal, newPair);
                i--;
                l--;
            }
        }
    }
    private void move(){
        int l = this.animals.size();
        for (int i = 0; i < l; i++) {
            Animal animal = this.animals.get(i % l);
            animal.move();
        }
    }
    private void eatingPhase(){
        for(Vector2d position : ((WorldMap)this.map).eatingPositions()){
            List<IMapElement> elements = this.map.objectAt(position);
            elements.sort(Collections.reverseOrder());
            for(IMapElement element : elements){
                if(element.getClass() == Carrot.class){
                    ((WorldMap) this.map).elements.get(position).remove(element);
                    break;
                }
            }
            Animal animal = null;
            int countStrongestAnimals = 1;
            for(IMapElement element : elements){
                if(animal == null)
                    animal = (Animal)element;
                else if((int)element.getEnergy() == (int)element.getEnergy())
                    countStrongestAnimals++;
                else
                    break;

            }
            for(int i=0; i<countStrongestAnimals;i++){
                ((Animal)((WorldMap) this.map).elements.get(position).get(i)).
                        setEnergy((float)this.CarrotEnergy/countStrongestAnimals);
            }
            this.numberOfNewCarrots--;
        }

    }
    private void breeding(){
        for(Vector2d position : ((WorldMap)this.map).occupiedPositions()){
            float energy = 0;
            int[] genotype1 = null;
            int[] genotype2 = null;
            Vector2d center;
            List<IMapElement> elements = this.map.objectAt(position);
            elements.sort(Collections.reverseOrder());
            int i = 0;
            boolean underObservation = false;
            boolean mainAncestorChild = false;
            for(IMapElement element : elements) {
                if (element.getClass() == Animal.class) {
                    energy += element.getEnergy() / 4;
                    center = element.getPosition();
                    if (genotype1 == null) {
                        genotype1 = ((Animal) element).getGenotype();
                    } else {
                        genotype2 = ((Animal) element).getGenotype();
                    }
                    if(((Animal) element).isObserved()){
                        underObservation = true;
                    }
                    if(((Animal) element).isMainAncestor()){
                        mainAncestorChild = true;
                    }

                    ((Animal) element).setEnergy(0);
                    ((Animal) element).addChildren();
                    i++;
                    if(i == 2) {
                        this.addNewAnimal(center, genotype1, genotype2, energy,
                                underObservation, mainAncestorChild);
                        break;
                    }
                }
            }


        }
    }
    private Vector2d rightPosition(Vector2d center, Vector2d position){
        Vector2d rightposition = center.add(position);
        if(rightposition.x>this.map.getUpright().x)
            rightposition = new Vector2d(this.map.getLowermost().x,rightposition.y);
        if(rightposition.y>this.map.getUpright().y)
            rightposition = new Vector2d(rightposition.x,this.map.getLowermost().y);
        if(rightposition.x<this.map.getLowermost().x)
            rightposition = new Vector2d(this.map.getUpright().x,rightposition.y);
        if(rightposition.y<this.map.getLowermost().y)
            rightposition = new Vector2d(rightposition.x,this.map.getUpright().y);
        return rightposition;
    }
    private void addNewAnimal(Vector2d center, int[] genotype1, int[] genotype2, float energy,
                              boolean underObservation, boolean mainAncestorChild){
        int[] genotype = Randoms.createGenotype(genotype1,genotype2);
        List<Integer> positions = new ArrayList<Integer>();
        for(int i=0;i<8;i++){
            positions.add(i);
        }
        Collections.shuffle(positions);
        OptionsParser parser = new OptionsParser();
        for(int i : positions){
            Vector2d position = parser.parse(i).toUnitVector();
            position = this.rightPosition(center,position);
            if(this.map.objectAt(position) == null){
                Animal animal = new Animal(this.map,position,genotype,energy,this.moveEnergy);
                this.animals.add(animal);
                this.addAnimalGenotype(animal);
                if(underObservation){
                    animal.startObservation();
                    this.focusedObserver.addDescendant();
                }
                if(mainAncestorChild){
                    this.focusedObserver.addChildren();
                }
                return;
            }
        }
        Vector2d position = parser.parse(positions.get(0)).toUnitVector();
        position = this.rightPosition(center,position);
        Animal animal = new Animal(this.map,position,genotype,energy,this.moveEnergy);
        this.animals.add(animal);
        this.addAnimalGenotype(animal);

    }
    private void addAnimalGenotype(Animal animal){
        if (this.genotypeMap.get(animal) != null) {
            Pair<Animal,Integer> pair = this.genotypeMap.get(animal);
            Pair<Animal,Integer> newPair = new Pair<Animal,Integer>(pair.getKey(),pair.getValue()+1);
            this.genotypeMap.remove(animal);
            this.genotypeMap.put(animal, newPair);
        } else {
            this.genotypeMap.put(animal, new Pair<Animal,Integer>(animal,1));
        }
    }
    private void initialAnimals(int initialNumber){
        Collections.shuffle(((WorldMap)this.map).getJungleXCoordinates());
        Collections.shuffle(((WorldMap)this.map).getJungleYCoordinates());
        int i = 0;
            for (int x : ((WorldMap) this.map).getJungleXCoordinates()) {
                for (int y : ((WorldMap) this.map).getJungleYCoordinates()) {
                    Vector2d position = new Vector2d(x, y);
                    int[] genotype = Randoms.createStandardGenotype();
                    if (!this.map.isOccupied(position)) {
                        if(i == initialNumber) break;
                        Animal animal = new Animal(this.map,position,genotype,(float)this.startEnergy,this.moveEnergy);
                        this.animals.add(animal);
                        //this.genotypeMap.put(animal, new Pair<Animal,Integer>(animal,1));
                        this.addAnimalGenotype(animal);
                        i++;
                    }
                }
                if (i == initialNumber) {
                    return;
                }

            }
    }
    private void addCarrots(){
        Collections.shuffle(((WorldMap)this.map).getJungleXCoordinates());
        Collections.shuffle(((WorldMap)this.map).getJungleYCoordinates());
        Collections.shuffle(((WorldMap)this.map).getBorderXCoordinates());
        Collections.shuffle(((WorldMap)this.map).getBorderYCoordinates());
        boolean addedJungleCarrot = false;
        for(int x : ((WorldMap)this.map).getJungleXCoordinates()){
            for(int y : ((WorldMap)this.map).getJungleYCoordinates()){
                Vector2d position = new Vector2d(x,y);
                if(this.map.place(new Carrot(position,this.CarrotEnergy))){
                    addedJungleCarrot = true;
                    this.numberOfNewCarrots++;
                    break;
                }
            }
            if(addedJungleCarrot) break;

        }
        for(int x : ((WorldMap)this.map).getBorderXCoordinates()){
            for(int y : ((WorldMap)this.map).getBorderYCoordinates()){
                Vector2d position = new Vector2d(x,y);
                boolean follow = position.follows(((WorldMap) this.map).getJungleLowermost());
                boolean precedes = position.precedes(((WorldMap) this.map).getJungleUpright());
                if(follow && precedes ) {
                    continue;
                }
                if(this.map.place(new Carrot(position,this.CarrotEnergy))){
                    this.numberOfNewCarrots++;
                    return;
                }
            }
        }
    }
    public boolean startObserving(Vector2d position){
        for(Animal animal : this.animals) {
            animal.stopObservation();
            animal.cancelMainAncestor();
        }
        if(!this.map.isOccupied(position)) return false;
        IMapElement element = this.getStrongestAnimalOrCarrot(position);
        if(element.getClass() != Animal.class) return false;
        ((Animal) element).startObservation();
        ((Animal) element).setMainAncestor();
        this.focusedObserver = new FocusedObserver();
        return true;
    }

    public Statistics getStatisticsForMap() {
        return this.statisticsForMap;
    }
    public AverageStatistics getAverageStatisticsForMap() {
        return this.averageStatisticsForMap;
    }

    public FocusedObserver getFocusedObserver() {
        return focusedObserver;
    }

    public IMapElement getStrongestAnimalOrCarrot(Vector2d position){
        if(!this.map.isOccupied(position)) return null;
        List<IMapElement> elements = this.map.objectAt(position);
        elements.sort(Collections.reverseOrder());
        for(IMapElement element : elements){
            if(element.getClass() == Animal.class)
                return element;
        }
        return elements.get(0);
    }
    //https://stackoverflow.com/questions/59930903/java-sort-map-based-on-count-of-value-of-list
    private Pair<Animal, Integer> getDominatingGenotype(){
        Pair<Animal, Integer> dominatingGenotype = null;
        if(this.genotypeMap.size() != 0) {
            Map<Animal, Pair<Animal,Integer>> result = sortByValueCount(this.genotypeMap);
            dominatingGenotype = result.entrySet().iterator().next().getValue();
        }
        return dominatingGenotype;
    }
    //https://stackoverflow.com/questions/59930903/java-sort-map-based-on-count-of-value-of-list
    public static Map<Animal, Pair<Animal,Integer>> sortByValueCount(final Map<Animal,Pair<Animal,Integer>> genotypeMap) {
        return genotypeMap.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().getValue(), e1.getValue().getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
