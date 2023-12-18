package Battleship;

public class PlayerPoints {
    // acertar num barco 1 ponto
    // afundar um barco 2 pontos

    private int playerPoints;
    static final int pointForHit = 1;
    static final int pointForSinking = 2;

    public PlayerPoints() {
        this.playerPoints = 0;

    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }
}
