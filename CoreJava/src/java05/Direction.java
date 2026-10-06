package java05;

public enum Direction{
    EAST,WEST,NORTH,SOUTH;
    @Override
    public String toString(){
        return name().toLowerCase()+" @ "+ordinal();
    }
}

