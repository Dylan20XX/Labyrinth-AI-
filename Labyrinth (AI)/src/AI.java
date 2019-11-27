import java.util.Arrays;

import javax.swing.ImageIcon;

public class AI extends Player{
	
	/*
	 * AI gets treasures in hand (logical priority) that already have a direct pathway - 
	 * (AI will insert the free tile in a location that does not destroy the pathway)
	 * 
	 * AI inserts the free tile logically - 
	 * to create a direct pathway to a treasure in its hand (both location and rotation) and gets the treasure
	 * 
	 * AI inserts the free tile logically - 
	 * to block an opponent’s direct pathway to a treasure
	 * 
	 * AI inserts the free tile logically - 
	 * to create a pathway that will enhance a future move and moves to a logical location
	 * 
	 * AI plays plays according to the current game status in order to create a victory
	 */
	
	private Board board = new Board();
	private Tile tileInHand = new Tile();
	private Player players[] = new Player[4];
	private int playerNum;
	
	private int handSizes[] = new int[3];
	
	//Movement decision variables
	private int treasuresAvailable; // add 10 for every treasure that can be reached with a move
	private int playersBlocked; // add 5 for every player blocked from reaching any treasure next move
	
	private int push;
	private int rotation;
	private int selectedRow;
	private int selectedCol;
	private Position position = new Position();
	
	
	public AI(ImageIcon image, int row, int col, int playerNum) {
		super(image, row, col);
		this.playerNum = playerNum;
		
		//Initialize the player array
		for(int i = 0; i < 4; i++) {
			players[i] = new Player();
		}
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	
	//This move returns a position to move the AI player to
	public Position move() {
		
		//Copy the board and find available paths
		copyBoard(LabyrinthGUI.board);
		board.pathfind(getRow(), getCol());
		
		//Copy tile in hand
		tileInHand.copy(LabyrinthGUI.tileInHand);
		
		//Copy players and hand sizes
		int handSizeIndex = 0;
		for(int i = 0; i < 4; i++) {
			players[i].copy(LabyrinthGUI.players[i]);
			if(i != playerNum) {
				handSizes[handSizeIndex] = LabyrinthGUI.players[i].getHand().size();
				handSizeIndex++;
			}
		}
			
		
		//Analyze the game state to chose best move
		analyzeGameState();
		
		//Make the best chosen move
		
		//Return the move object
		return position;
		
	}
	
	//This method is used to copy the board from the GUI
	private void analyzeGameState() {
		
		board.pathfind(getRow(), getCol());
		//treasuresAvailable = 0;
		treasuresAvailable = board.checkDirectTreasurePath(getHand());
		
		//If you have more than 2 cards and another player(s) has 2 or less, try to block that player(s)
		if(getHand().size() > 2 && (handSizes[0] <= 2 || handSizes[1] <= 2 || handSizes[2] <= 2)) {
			
		} else if(getHand().size() <= 2 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you have more than 1 card and another player(s) has 1, try to block that player(s)
			
		} else if(getHand().size() == 1 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you and another player(s) has 1 card
			
			//Try collect last treasure but if you can't, block that player(s)
			
		} else {
			
			//Check if there's a direct path to treasure - *potential problem - all possible moves may block path
			//If there's a direct path to treasure, try to create a path that allows you to collect another treasure next turn
			//If you can't setup a path for next turn, try to block other players
			if(treasuresAvailable > 0) { 
				
			}
			
			//Check if you can create a direct path to treasure
			//Choose the option that will create a path to the most treasures and go for the one closest to the middle
			else {
				
				checkIndirectTreasurePaths(tileInHand);
				
				//Check if you can create a direct path to treasure
				//Choose the option that will create a path to the most treasures and go for the one with more treasures nearby
				if(treasuresAvailable > 0) { 
					
					
					
				//Check if there are no treasures that can be reached this move
				//Try to create a path that will allow you to get a treasure next move
				//If that can't be done, try to block the player in the lead
				} else {
					
					
					
				}
			
				
			}
			
			//Check if there are no treasures that can be reached this move
			//Try to create a path that will allow you to get a treasure next move
			//If that can't be done, try to block the player in the lead
			
			
			
		}
		
	}
	
	//This is a method to be used to check if a tile placement can allow a treasure to be collected
	private void checkIndirectTreasurePaths(Tile tileInHand) {
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		
		for(int c = 0; c < 12; c++) {
			
			//if(c == lastPush)
			//	continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
						//Check if more treasures can be reached
						board.pathfind(getRow(), getCol());
						if(board.checkDirectTreasurePath(getHand()) > 0 && board.checkDirectTreasurePath(getHand()) > treasuresAvailable) { 
							treasuresAvailable = board.checkDirectTreasurePath(getHand());
							
						}
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
						//Check if more treasures can be reached
						board.pathfind(getRow(), getCol());
						if(board.checkDirectTreasurePath(getHand()) > 0 && board.checkDirectTreasurePath(getHand()) > treasuresAvailable) { 
							treasuresAvailable = board.checkDirectTreasurePath(getHand());
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						
						//Check if more treasures can be reached
						board.pathfind(getRow(), getCol());
						if(board.checkDirectTreasurePath(getHand()) > 0 && board.checkDirectTreasurePath(getHand()) > treasuresAvailable) { 
							treasuresAvailable = board.checkDirectTreasurePath(getHand());
						}
						
					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						
						//Check if more treasures can be reached
						board.pathfind(getRow(), getCol());
						if(board.checkDirectTreasurePath(getHand()) > 0 && board.checkDirectTreasurePath(getHand()) > treasuresAvailable) { 
							treasuresAvailable = board.checkDirectTreasurePath(getHand());
						}
						
					}

				}
			}
			
		}
		
	}	
	
	//This method simulates each possible move that the AI player can make
	private void simulate(Tile tileInHand) {
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		
		for(int c = 0; c < 12; c++) {
			
			//if(c == lastPush)
			//	continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);

					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
					
					}

				}
			}
			
		}
		
	}	
	
	//This method is used to copy the board from the GUI
	private void copyBoard(Board boardToCopy) {
		
		//Copy the board variables
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				
				board.getBoard()[row][col].copy(boardToCopy.getBoard()[row][col]);
				
			}
		}
		
	}
	
	//This method pushes a column down
	private void pushColDown(int col, Tile tile) {
		
		board.getBoard()[0][col].copy(tile);
		board.pushColDown(col);

		tile.copy(board.getBoard()[8][col]);

		board.getBoard()[8][col] = new Tile();

		//Move a player if they are on the selected column
		for(int j = 0; j < 4; j++) {
			if(players[j].getCol() == col) {
				players[j].setRow(players[j].getRow() + 1);
				if(players[j].getRow() == 8)
					players[j].setRow(1);
			}
		}
		
	}
	
	//This method pushes a column up
	private void pushColUp(int col, Tile tile) {
		
		board.getBoard()[8][col].copy(tile);
		board.pushColUp(col);

		tile.copy(board.getBoard()[0][col]);

		board.getBoard()[0][col] = new Tile();

		//Move a player if they are on the selected column
		for(int j = 0; j < 4; j++) {
			if(players[j].getCol() == col) {
				players[j].setRow(players[j].getRow() - 1);
				if(players[j].getRow() == 0)
					players[j].setRow(7);
				//updatePlayerLocation(j);
			}
		}
		
	}
	
	//This method pushes a column to the right
	private void pushRowRight(int row, Tile tile) {
		
		board.getBoard()[row][0].copy(tile);
		board.pushRowRight(row);

		tile.copy(board.getBoard()[row][8]);

		board.getBoard()[row][8] = new Tile();

		//Move a player if they are on the selected row
		for(int j = 0; j < 4; j++) {
			if(players[j].getRow() == row) {
				players[j].setCol(players[j].getCol() + 1);
				if(players[j].getCol() == 8)
					players[j].setCol(1);
				//updatePlayerLocation(j);
			}
		}
		
	}
	
	//This method pushes a column to the left
	private void pushRowLeft(int row, Tile tile) {
		
		board.getBoard()[row][8].copy(tile);
		board.pushRowLeft(row);

		tile.copy(board.getBoard()[row][0]);

		board.getBoard()[row][0] = new Tile();

		//Move a player if they are on the selected row
		for(int j = 0; j < 4; j++) {
			if(players[j].getRow() == row) {
				players[j].setCol(players[j].getCol() - 1);
				if(players[j].getCol() == 0)
					players[j].setCol(7);
				//updatePlayerLocation(j);
			}
		}
		
	}
	
	//This method checks the placements of players depending on how many cards they each have
	private int[] checkPlacement() {
		
		//Create the array to be returned and fill it with -1
		int[] placements = new int[3];
		Arrays.fill(placements, -1);
		
		while(placements[0] == -1 || placements[1] == -1 || placements[2] == -1) {
		
			for(int i = 0; i < 4; i++) {

				if(i == playerNum || i == placements[0] || i == placements[1] || i == placements[2]) {
					continue;
				}
				
				if(placements[0] != -1 && players[i].getHand().size() < players[placements[0]].getHand().size()) {
					
					swap(placements, 1, 2);
					swap(placements, 0, 1);
					placements[0] = i;
					
				} else if(placements[1] != -1 && players[i].getHand().size() < players[placements[1]].getHand().size()) {
					
					swap(placements, 1, 2);
					placements[1] = i;
					
				} else {
					
					placements[2] = i;
					
				}

			}
		
		}
		
		return placements;
		
	}
	
	//This method swaps the values of array indexes
	private void swap(int[] data, int index1, int index2) {

		int temp = data[index1];
		data[index1] = data[index2];
		data[index2] = temp;

	}
	
	//This method blocks the selected player by trying to prevent the targeted player from having any path to collect a treasure
	private void block(int player) { //Player number from 0-3
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		
		for(int c = 0; c < 12; c++) {
			
			//if(c == lastPush)
			//	continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
						//Check if the targeted player was blocked
						board.pathfind(players[player].getRow(), players[player].getCol());
						if(board.checkDirectTreasurePath(players[player].getHand()) == 0) { 
							push = c;
							//Can check if the move still allows a treasure can be obtained on the targeted player's next turn
							return;
						}
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
						//Check if the targeted player was blocked
						board.pathfind(players[player].getRow(), players[player].getCol());
						if(board.checkDirectTreasurePath(players[player].getHand()) == 0) { 
							push = c;
							return;
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						
						//Check if the targeted player was blocked
						board.pathfind(players[player].getRow(), players[player].getCol());
						if(board.checkDirectTreasurePath(players[player].getHand()) == 0) { 
							push = c;
							return;
						}
						
					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						
						//Check if the targeted player was blocked
						board.pathfind(players[player].getRow(), players[player].getCol());
						if(board.checkDirectTreasurePath(players[player].getHand()) == 0) { 
							push = c;
							return;
						}
						
					}

				}
			}
			
		}
		
	}
	
}
