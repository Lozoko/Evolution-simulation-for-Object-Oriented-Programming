package main;

import java.util.List;
import java.util.Map;

public interface IWorldMap {

    /**
     * Place a animal on the map.
     *
     * @param element
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(IMapElement element);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
    List<IMapElement> objectAt(Vector2d position);
    Vector2d getLowermost();
    Vector2d getUpright();

    void deleteDeadAnimal(Animal animal);
}
