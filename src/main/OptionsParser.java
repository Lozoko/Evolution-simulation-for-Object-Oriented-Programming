package main;

public class OptionsParser {
    public MapDirection parse(int direction){
        MapDirection mdirection;
        switch (direction){
            case 0  -> mdirection = MapDirection.NORTH;
            case 2 -> mdirection = MapDirection.EAST;
            case 6 -> mdirection = MapDirection.WEST;
            case 4 -> mdirection = MapDirection.SOUTH;
            case 5 -> mdirection = MapDirection.SOUTHWEST;
            case 3 -> mdirection = MapDirection.SOUTHEAST;
            case 7 -> mdirection = MapDirection.NORTHWEST;
            case 1 -> mdirection = MapDirection.NORTHEAST;
            default -> mdirection = null;
        }
        return mdirection;

    }
}
