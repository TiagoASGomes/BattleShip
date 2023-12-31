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
             """, "Square Map"),

    HEXA_MAP("""
            * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 1
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 2
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 3
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 4
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 7
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 8
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 10
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 11
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 12
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 13
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 14
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 15
            * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 16
            * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z *
            """, "Hexa Map"),


    ROCK_MAP("""
            * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 1
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 2
            * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 3
            * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 4
            * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ * 7
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ * 8
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 10
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 11
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 12
            * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 13
            * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 14
            * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 15
            * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 16
            * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z *
            """, "Rock Map");


    private final String MAP;
    private final String MAP_NAME;

    /**
     * Constructs a new MapType with the specified string representation.
     *
     * @param MAP The string representation of the map type.
     */

    MapType(String MAP, String MAP_NAME) {
        this.MAP = MAP;
        this.MAP_NAME = MAP_NAME;
    }

    /**
     * Retrieves the string representation of the map type.
     *
     * @return The string representation of the map type.
     */
    public String getMAP() {
        return MAP;
    }

    /**
     * Retrieves the name of the map.
     *
     * @return The string representation of the name of the map type.
     */
    public String getMAP_NAME() {
        return MAP_NAME;
    }
}
