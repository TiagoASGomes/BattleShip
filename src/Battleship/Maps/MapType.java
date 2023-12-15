package Battleship.Maps;

public enum MapType {
    SQUARE_MAP("""                
            * * * * * * * * * * * * * *
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 1
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 2
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 3
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 4
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 7
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 8
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9
            * * * * * * * * * * * * * *
            * A B C D E F G H I J K L *    
             """);

    private final String MAP;

    MapType(String MAP) {
        this.MAP = MAP;
    }

    public String getMAP() {
        return MAP;
    }
}
