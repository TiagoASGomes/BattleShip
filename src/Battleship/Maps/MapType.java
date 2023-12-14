package Battleship.Maps;

public enum MapType {
    SQUARE_MAP("""                
      A B C D E F G H I J K L
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
    * * * * * * * * * * * * * *""");

    private final String BOARD_SIZE;

    MapType(String BOARD_SIZE) {
        this.BOARD_SIZE = BOARD_SIZE;
    }

    public String getBOARD_SIZE() {
        return BOARD_SIZE;
    }
}