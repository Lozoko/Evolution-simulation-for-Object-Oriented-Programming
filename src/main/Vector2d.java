package main;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString(Vector2d this){
        return '('+Integer.toString(this.x)+','+Integer.toString(this.y)+')';
    }
    public boolean precedes(Vector2d other){
        return (this.x <= other.x && this.y <= other.y);
    }
    public boolean follows(Vector2d other){
        return (this.x >= other.x && this.y >= other.y);
    }
    public Vector2d add(Vector2d other){
        return new Vector2d(this.x+ other.x,this.y+other.y);
    }
    public boolean equals(Object other){
        if(other==null) return false;
        if(this.getClass() != other.getClass()) return false;
        if(this==other) return true;
        Vector2d other2 = (Vector2d)other;
        return (this.x == ((Vector2d)other).x && this.y == ((Vector2d)other).y);
    }
    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }
}
