# Battleship

![alt text](https://i.gyazo.com/6b573dcdcbc0f8fe7e9bb12459b196ee.png)

BattleShip is a MindSwap BootCamp game created by Joaquim Verde, José Vieira, Pedro Soares and Tiago Gomes.

This game is a strategy type guessing game for two players.
It is played on ruled grids (board) on which each player's fleet of warships are marked.
The locations of the fleets are concealed from the other player.
Players alternate turns calling "shots" at the other player's ships, and the objective of the game is to destroy the
opposing player's fleet.

Instructions to start:

	It's necessary 2 contestants to start the game.
	At the beginning of the game a board is generated randomly between the choices made by the 2 players.
	The players also choose the character they want to play with.
	The players have to place their ships in the board to start the game.
	The player that sunk all the opponent ships wins the game.

Ship types:

    Big Ship -> B B B B B   Carrier -> C C C C  Carrier -> C C C C  Submarine -> S S S

    Destroyer -> D D Cruiser -> Z Z   BattleCruiser -> P P P  
                                  Z Z                    P P
                   K
    CrossShip -> K K K
                   K

Instruction of BattleShip:

	Choose Map:
		Choose between one of the 3 maps available, by pressing “1”, “2” or “3”;

    1 - * * * * * * * * * * * * * *   	 2 - * * * * * * * * * * * * * * * * * * * * * * * * * * * *   	 3 - * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 1 	     * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 1 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 1
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 2 	     * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 2 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 2
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 3 	     * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 3 	     * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 3
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 4 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 4 	     * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ * 4
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5 	     * ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 5
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 6
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 7 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 7 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ * 7
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 8 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 8 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ * 8
        * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9 	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 9
        * * * * * * * * * * * * * *   	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 10	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 10
        * A B C D E F G H I J K L *   	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 11	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 11
                                                 * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 12	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 12
                                                 * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 13	     * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 13
                                                 * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 14	     * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 14
                                                 * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 15	     * ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * ~ ~ ~ ~ ~ ~ ~ * 15
                                                 * * * * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * * * * 16	     * ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ * 16
                                                 * * * * * * * * * * * * * * * * * * * * * * * * * * * *   	     * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                                                 * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z *   	     * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z *
	
	Choose Character:
		Choose one of the 3 characters available by pressing “1”, “2” or “3”;
		Each character have specified set of battleships;

    Character 1:
        Ships:

        Special:
            Shoots an entire row of the map.

    Character 2:
        Ships:

        Special:
            Shoots in a big cross.

    Character 3:
        Ships:

        Special:
            Shoots in a big plus.
	
	Place Your Ships:
		Each player has to place their ships in the board;
		Place: To place the ships, type “place” followed by the number of the ship, the row number and the column letter
		(ex: place 1 7 A – this command sets the number one ship to the row 7, column A);
		The ship will be placed from that coordinate to the east side of the board;


	Rotate:
		If you which to position the ship vertical, type “rotate” and the number of the ship (ex: rotate 1).
		Then, you can go ahead and place the ship on the board;
		The ship will be placed from that coordinate to the south side of the board;

	Ready: 
		Type “ready” after you place all your ships;

Play game:

	Type “shoot” followed by row number and column letter (ex: shoot 8 B);

	Player points:

	You win player points:
		- turn played: 1 point;
		- hit opponent ship: 1 point;
		- sink ship: 2 points;

Special abilities:

	To use special abilities you loose player points;

	Mine: place a mine on your board and if your opponent hits it he´s shoot back randomly;
	costs 2 player points; type “mine” followed by row and column;

	Sonar: use a sonar on the opponent map to reveal the spaces around that coordinate;
	costs 5 player points; type “sonar” followed by row and column;

	Bomb: send a bomb to your opponent, and it will hit 4 coordinates instead of 1;
	costs 4 points; type “bomb” followed by row and column;

	Special: costs 7 points; uses the selected character special ability.

Instructions to run:

    Compile the server and run it;
    Compile the Client and run it 2 times;
    Enjoy the game 😊
