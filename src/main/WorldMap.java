package main;

import java.util.*;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    public Map<Vector2d,List<IMapElement>> elements = new LinkedHashMap<>();
    protected Vector2d borderLowerMost;
    protected Vector2d borderUpright;
    protected Vector2d jungleLowermost;
    protected Vector2d jungleUpright;
    private final float minEnergyToBreeding;

    private List<Integer> xJungleCoordinates;
    private List<Integer> yJungleCoordinates;

    private List<Integer> xBorderCoordinates;
    private List<Integer> yBorderCoordinates;

    public WorldMap(int width, int height, int jungleRatio, int minEnergyToBreeding){
        this.borderUpright = new Vector2d(width,height);
        this.borderLowerMost = new Vector2d(0,0);
        this.jungleUpright = new Vector2d((int)((width/2) + (width*jungleRatio/200)),(int)((height/2) + (height*jungleRatio/200)));
        this.jungleLowermost = new Vector2d((int)((width/2) - (width*jungleRatio/200)),(int)((height/2) - (height*jungleRatio/200)));
        this.setCarrotsCoordinates();
        this.minEnergyToBreeding = minEnergyToBreeding;
    }
    @Override
    public boolean place(IMapElement element) {
        if (element == null) throw new IllegalArgumentException("element is null");
        List<IMapElement> objects = this.objectAt(element.getPosition());
        if(element.getClass() == Animal.class && objects != null) {
            this.elements.get(element.getPosition()).add(element);
            return true;
        }
        if(element.getClass() == Carrot.class && objects != null) return false;
        List<IMapElement> list = new LinkedList<IMapElement>();
        list.add(element);
        this.elements.put(element.getPosition(),list);
        return true;
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        return (objectAt(position) != null);
    }

    @Override
    public List<IMapElement> objectAt(Vector2d position) {
        if(!this.elements.containsKey(position)) return null;
        return this.elements.get(position);
    }
    public Map<Vector2d,List<IMapElement>> getElements(){
        return this.elements;
    }
    public Vector2d getLowermost() {
        return this.borderLowerMost;
    }
    public Vector2d getUpright() {
        return this.borderUpright;
    }
    public Vector2d getJungleLowermost() {
        return this.jungleLowermost;
    }
    public Vector2d getJungleUpright() {
        return this.jungleUpright;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        this.elements.get(oldPosition).remove(animal);
        if(this.elements.get(oldPosition).size() == 0){
            this.elements.remove(oldPosition);
        }
        if(!this.elements.containsKey(newPosition)){
            List<IMapElement> list = new LinkedList<IMapElement>();
            this.elements.put(newPosition,list);
        }
        this.elements.get(newPosition).add(animal);
    }
    public void deleteDeadAnimal(Animal animal){
        this.elements.get(animal.getPosition()).remove(animal);
        if(this.elements.get(animal.getPosition()).size()==0){
            this.elements.remove(animal.getPosition());
        }
    }
    public List<Vector2d> occupiedPositions(){
        List<Vector2d> breedingPosition = new LinkedList<Vector2d>();
        for(Vector2d position : this.elements.keySet()){
            int i = 0;
            for(IMapElement element : this.elements.get(position)){
                if(element.getClass() == Animal.class && element.getEnergy() >= this.minEnergyToBreeding){
                    i++;
                    if (i == 2){
                        breedingPosition.add(position);
                        break;
                    }
                }
            }
        }
        return breedingPosition;
    }
    public List<Vector2d> eatingPositions(){
        List<Vector2d> eatingPosition = new LinkedList<Vector2d>();
        for(Vector2d position : this.elements.keySet()){
            boolean animal = false;
            boolean Carrot = false;
            for(IMapElement element : this.elements.get(position)){
                if(element.getClass() == Animal.class){
                    animal = true;
                }
                if(element.getClass() == Carrot.class){
                    Carrot = true;
                }
                if (animal && Carrot){
                    eatingPosition.add(position);
                    break;
                }
            }
        }
        return eatingPosition;
    }
    private void setCarrotsCoordinates(){
        int x = this.getJungleUpright().x - this.getJungleLowermost().x;
        int y = this.getJungleUpright().y - this.getJungleLowermost().y;
        this.xJungleCoordinates = new ArrayList<Integer>(x+1);
        this.yJungleCoordinates = new ArrayList<Integer>(y+1);
        for(int i = this.jungleLowermost.x;i<=this.jungleUpright.x;i++)
            xJungleCoordinates.add(i);
        for(int i = this.jungleLowermost.y;i<=this.jungleUpright.y;i++)
            yJungleCoordinates.add(i);

        x = this.getUpright().x - this.getLowermost().x;
        y = this.getUpright().y - this.getLowermost().y;
        this.xBorderCoordinates = new ArrayList<Integer>(x+1);
        this.yBorderCoordinates = new ArrayList<Integer>(y+1);
        for(int i = this.borderLowerMost.x;i<=this.borderUpright.x;i++)
            xBorderCoordinates.add(i);
        for(int i = this.borderLowerMost.y;i<=this.borderUpright.y;i++)
            yBorderCoordinates.add(i);
    }
    public List<Integer> getJungleXCoordinates() {return this.xJungleCoordinates;}
    public List<Integer> getJungleYCoordinates() {return this.yJungleCoordinates;}
    public List<Integer> getBorderXCoordinates() {return this.xBorderCoordinates;}
    public List<Integer> getBorderYCoordinates() {return this.yBorderCoordinates;}

}
