package main;

public enum MapDirection {
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    public MapDirection next(){
        return MapDirection.values()[(this.ordinal()+1)%MapDirection.values().length];
    }
    public MapDirection previous(){
        return MapDirection.values()[(this.ordinal()+MapDirection.values().length-1)%MapDirection.values().length];
    }
    public Vector2d toUnitVector(){
        return switch (this){
            case SOUTH -> new Vector2d(0,-1);
            case NORTH -> new Vector2d(0,1);
            case WEST -> new Vector2d(-1,0);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }
}
