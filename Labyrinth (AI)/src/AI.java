import java.util.ArrayList;
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
	private ArrayList<String> directTreasures = new ArrayList<String>();
	private int treasuresAvailable; // add 10 for every treasure that can be reached with a move
	
	private boolean treasurePath = false; //set in the movePoints() method
	
	//Row and col at start of turn
	private int startRow;
	private int startCol;
	
	private int push;
	private int rotation;
	private int selectedRow;
	private int selectedCol;
	
	private boolean pathSelected = false;
	
	private int shortestTreasureDis;
	private int numTreasuresShortestDis;
	
	private Position position = new Position();
	
	
	public AI(ImageIcon image, int row, int col, int playerNum) {
		super(image, row, col);
		this.playerNum = playerNum;
		
		startRow = row;
		startCol = col;
		
		//Initialize the player array
		for(int i = 0; i < 4; i++) {
			players[i] = new Player();
		}
		
		//Initialize the board tiles
		board.fillEmptyBoard();
		
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
	
	public int getPush() {
		return push;
	}

	public void setPush(int push) {
		this.push = push;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
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
			
		//Record current position on the board
		startRow = getRow();
		startCol = getCol();
		
		//Analyze the game state to chose best move
		choseActionPriority();
		
		//Make the best chosen move
		position.setRow(selectedRow);
		position.setCol(selectedCol);
		
		copyStartPosition();
		System.out.println("starting position " + startRow + " " + startCol);
		
		//Return the move object
		return position;
		
	}
	
	private void choseActionPriority() {
		
		board.pathfind(getRow(), getCol());
		//treasuresAvailable = 0;
		treasuresAvailable = board.checkDirectTreasurePath(getHand());
		directTreasures = board.listDirectTreasurePath(getHand());
		treasurePath = false;
		
		//If you have more than )2 cards and another player(s) has 2 or less, try to block that player(s)
		if(getHand().size() > 2 && (handSizes[0] <= 2 || handSizes[1] <= 2 || handSizes[2] <= 2)) {
			
			block(tileInHand);
			
		} else if(getHand().size() <= 2 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you have more than 1 card and another player(s) has 1, try to block that player(s)
			
			block(tileInHand);
			
		} else if(getHand().size() == 1 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you and another player(s) has 1 card
			
			//Try collect last treasure but if you can't, block that player(s)
			block(tileInHand);
			
		} else {
			
			checkIndirectTreasurePaths(tileInHand);
			
			//Check if there's a direct path to treasure - *potential problem - all possible moves may block path
			//If there's a direct path to treasure, try to create a path that allows you to collect another treasure next turn
			//If you can't setup a path for next turn, try to block other players
			//if(treasuresAvailable > 0) { 
				
				//treasurePath = true;
				
			//}
			
			//Check if you can create a direct path to treasure
			//Choose the option that will create a path to the most treasures and go for the one closest to the middle
			//else {
				
				//checkIndirectTreasurePaths(tileInHand);
				
				//Check if you can create a direct path to treasure
				//Choose the option that will create a path to the most treasures and go for the one with more treasures nearby
				//if(treasuresAvailable > 0) { 
					
					
					
				//Check if there are no treasures that can be reached this move
				//Try to create a path that will allow you to get a treasure next move
				//If that can't be done, try to block the player in the lead
				//} else {
					
					
					
				//}
			
				
			//}
			
			//Check if there are no treasures that can be reached this move
			//Try to create a path that will allow you to get a treasure next move
			//If that can't be done, try to block the player in the lead
			
			
			
		}
		
	}
	
	//This is a method to be used to check if a tile placement can allow a treasure to be collected
	private void checkIndirectTreasurePaths(Tile tileInHand) {
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		int points = 0;
		
		shortestTreasureDis = 50;
		numTreasuresShortestDis = 0;
		
		for(int c = 0; c < 12; c++) {
			
			if(c == LabyrinthGUI.lastPush)
				continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
						//Award points to the created game state
						if(movePoints() > points && treasurePath) {
							points = movePoints();
							push = c;
							rotation = k;
						}
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
						//Award points to the created game state
						if(movePoints() > points && treasurePath) {
							points = movePoints();
							push = c;
							rotation = k;
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						
						//Award points to the created game state
						if(movePoints() > points && treasurePath) {
							points = movePoints();
							push = c;
							rotation = k;
						}
						
					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						
						//Award points to the created game state
						if(movePoints() > points && treasurePath) {
							points = movePoints();
							push = c;
							rotation = k;
						}
						
					}

				}
			}
			
		}
		
		//If a treasure couldn't be collected, plan for next move
		if(!treasurePath) {
			System.out.println("no treasure paths");
			twoMoveSimulation(tileInHand);
		} 
		else {
			selectMoveTile(); //select the tile to move to - treasure tile
		}
		
	}	
	
	private void selectMoveTile() {
		
		copyBoard(LabyrinthGUI.board); //Copy the current game board and original player position
		copyStartPosition(); //Copy position at start of turn
		
		//Copy tile in hand and set its rotation
		tileInHand.copy(LabyrinthGUI.tileInHand);
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		tile.setRotation(rotation);
		
		if(push >= 0 && push < 3) {
			int col = (push + 1) * 2;
			pushColDown(col, tile);
		} else if(push >= 3 && push < 6) {
			int row = (push - 2) * 2;
			pushRowLeft(row, tile);
		} else if(push >= 6 && push < 9) {
			int col = (9 - push) * 2;
			pushColUp(col, tile);
		} else if(push >= 9 && push < 12) {
			int row = (12 - push) * 2;
			pushRowRight(row, tile);
		}
		
		System.out.println("push = " + push + " rotation = " + rotation);
		simulateTileMoveSelection(tile);
		
	}
	
	//This method simulates two move into the future and is used when a treasure can't be reached in one move
	private void twoMoveSimulation(Tile tileInHand) {
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		int points = 0;
		
		for(int c = 0; c < 12; c++) {
			
			if(c == LabyrinthGUI.lastPush)
				continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						pathSelected = true;
						
						System.out.println("sim push = " + i + " sim rotation = " + k);
						int movePoints = simulateTileMoveSelection(tile, points, c, k);
						if(pathSelected) {
							System.out.println("selected move");
							points = movePoints;
//							push = c;
//							rotation = k;
						}
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						pathSelected = true;
						
						System.out.println("sim push = " + i + " sim rotation = " + k);
						int movePoints = simulateTileMoveSelection(tile, points, c, k);
						if(pathSelected) {
							System.out.println("selected move");
							points = movePoints;
//							push = c;
//							rotation = k;
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						pathSelected = true;
						
						System.out.println("sim push = " + i + " sim rotation = " + k);
						int movePoints = simulateTileMoveSelection(tile, points, c, k);
						if(pathSelected) {
							System.out.println("selected move");
							points = movePoints;
//							push = c;
//							rotation = k;
						}

					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						pathSelected = true;
						
						System.out.println("sim push = " + i + " sim rotation = " + k);
						int movePoints = simulateTileMoveSelection(tile, points, c, k);
						if(pathSelected) {
							System.out.println("selected move");
							points = movePoints;
//							push = c;
//							rotation = k;
						}
						
					}

				}
			}
			
		}
		
	}
	
	//This method simulates movements to each tile and returns a point value
	private void simulateTileMoveSelection(Tile tileInHand) {
		
		Position originalPosition = new Position(getRow(), getCol());
		
		//Find paths from the current position on the board
		board.pathfind(getRow(), getCol());
//		selectedRow = getRow();
//		selectedCol = getCol();
		int points = 0; //Max points from move the selected tile placement
		
		System.out.println("simulating move selection");
		//printBoard();
		printVis();
		
		//Loop through the board's visited array
		for(int i = 1; i < 50; i++) {
			if(board.getVis()[i]) {
				//Move to tiles with a direct path to them
				setRow(Board.nodeNumToRow(i));
				setCol(Board.nodeNumToCol(i));
				
				//Add points if a treasure is collected on this tile
				int treasurePoints = 0;
				for(Card currentCard: getHand())
					if(board.getBoard()[Board.nodeNumToRow(i)][Board.nodeNumToCol(i)].getTreasure().equalsIgnoreCase(currentCard.getTreasure())) {
						treasurePoints = 100;
					}
				
				//Simulate next moves and add points if a treasure can be collected next turn
				int awardedPoints = checkNextMoves(tileInHand, points);
				if(awardedPoints + treasurePoints > points) {
					points = awardedPoints + treasurePoints ;
					selectedRow = Board.nodeNumToRow(i);
					selectedCol = Board.nodeNumToCol(i);
					System.out.println("selecting " + selectedRow + " " + selectedCol);
				} else if(awardedPoints == 1 && points <= 1) { //This means that the current tile placement allowed for a move that puts player closest to another treasure(s)
					points = 1;
					selectedRow = Board.nodeNumToRow(i);
					selectedCol = Board.nodeNumToCol(i);
					System.out.println("selecting " + selectedRow + " " + selectedCol);
				}
				
			}
		}
		
		if(points == 0) {
			selectedRow = originalPosition.getRow();
			selectedCol = originalPosition.getCol();
		}
		
	}
	
	private int simulateTileMoveSelection(Tile tileInHand, int points, int selectedPush, int selectedRotation) {
		
		//Find paths from the current position on the board
		board.pathfind(getRow(), getCol());
//		selectedRow = getRow();
//		selectedCol = getCol();
		
		System.out.println("simulating move selection");
		//printBoard();
		printVis();
		
		//Loop through the board's visited array
		for(int i = 1; i < 50; i++) {
			if(board.getVis()[i]) {
				//Move to tiles with a direct path to them
				setRow(Board.nodeNumToRow(i));
				setCol(Board.nodeNumToCol(i));
				
				//Add points if a treasure is collected on this tile
				int treasurePoints = 0;
				for(Card currentCard: getHand())
					if(board.getBoard()[Board.nodeNumToRow(i)][Board.nodeNumToCol(i)].getTreasure().equalsIgnoreCase(currentCard.getTreasure())) {
						treasurePoints = 100;
					}
				
				//Simulate next moves and add points if a treasure can be collected next turn
				int awardedPoints = checkNextMoves(tileInHand, points);
				if(awardedPoints + treasurePoints > points) {
					points = awardedPoints + treasurePoints ;
					selectedRow = Board.nodeNumToRow(i);
					selectedCol = Board.nodeNumToCol(i);
					push = selectedPush;
					rotation = selectedRotation;
					System.out.println("selecting " + selectedRow + " " + selectedCol);
				} else if(awardedPoints == 1 && points <= 1) { //This means that the current tile placement allowed for a move that puts player closest to another treasure(s)
					points = 1;
					selectedRow = Board.nodeNumToRow(i);
					selectedCol = Board.nodeNumToCol(i);
					push = selectedPush;
					rotation = selectedRotation;
					System.out.println("selecting with 1 pt" + selectedRow + " " + selectedCol);
				} else {
					points = 0; //No better path was found
					pathSelected = false;
				}
				
			}
		}
		
		return points;
		
	}
	
	//This method simulates each possible move that the AI player can make next turn
	private int checkNextMoves(Tile tileInHand, int points) {
		
		if(points == 1)
			points = 0;
		
		Player simPlayers[] = new Player[4];
		
		for(int i = 0; i < 4; i++) {
			simPlayers[i] = new Player();
			simPlayers[i].copy(players[i]);
		}
		
		int simRow = getRow();
		int simCol = getCol();
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		Board boardStart = new Board();
		boardStart.fillEmptyBoard();
		boardStart.copyBoard(board);
		
		for(int c = 0; c < 12; c++) {
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(boardStart); //Copy the current game board
						setRow(simRow); //Copy the original row
						setCol(simCol); //Copy the original col
						copyPlayers(simPlayers); //Copy the original player positions
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
						int secondMovePoints = secondMovePoints();
						if(secondMovePoints > points) {
							points = secondMovePoints;
						} else if(points == 0) {
							//If the distance to a treasure is shortest from this position, or equal to shortest distance but has more treasures with this distance
							// and points are at 0, award it 1 point;
							int treasureDis = findShortestTreasureDis();
							int numTreasures = findNumTreasuresShortestDis();
							if(treasureDis < shortestTreasureDis || (treasureDis == shortestTreasureDis && numTreasures > numTreasuresShortestDis)) {
								shortestTreasureDis = treasureDis;
								numTreasuresShortestDis = numTreasures;
								points = 1;
							}
						}
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(boardStart); //Copy the current game board
						setRow(simRow); //Copy the original row
						setCol(simCol); //Copy the original col
						copyPlayers(simPlayers); //Copy the original player positions
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
						int secondMovePoints = secondMovePoints();
						if(secondMovePoints > points) {
							points = secondMovePoints;
						} else if(points == 0) {
							//If the distance to a treasure is shortest from this position, or equal to shortest distance but has more treasures with this distance
							// and points are at 0, award it 1 point;
							int treasureDis = findShortestTreasureDis();
							int numTreasures = findNumTreasuresShortestDis();
							if(treasureDis < shortestTreasureDis || (treasureDis == shortestTreasureDis && numTreasures > numTreasuresShortestDis)) {
								shortestTreasureDis = treasureDis;
								numTreasuresShortestDis = numTreasures;
								points = 1;
							}
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(boardStart); //Copy the current game board
						setRow(simRow); //Copy the original row
						setCol(simCol); //Copy the original col
						copyPlayers(simPlayers); //Copy the original player positions
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						
						int secondMovePoints = secondMovePoints();
						if(secondMovePoints > points) {
							points = secondMovePoints;
						} else if(points == 0) {
							//If the distance to a treasure is shortest from this position, or equal to shortest distance but has more treasures with this distance
							// and points are at 0, award it 1 point;
							int treasureDis = findShortestTreasureDis();
							int numTreasures = findNumTreasuresShortestDis();
							if(treasureDis < shortestTreasureDis || (treasureDis == shortestTreasureDis && numTreasures > numTreasuresShortestDis)) {
								shortestTreasureDis = treasureDis;
								numTreasuresShortestDis = numTreasures;
								points = 1;
							}
						}
						
					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(boardStart); //Copy the current game board
						setRow(simRow); //Copy the original row
						setCol(simCol); //Copy the original col
						copyPlayers(simPlayers); //Copy the original player positions
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						
						int secondMovePoints = secondMovePoints();
						if(secondMovePoints > points) {
							points = secondMovePoints;
						} else if(points == 0) {
							//If the distance to a treasure is shortest from this position, or equal to shortest distance but has more treasures with this distance
							// and points are at 0, award it 1 point;
							int treasureDis = findShortestTreasureDis();
							int numTreasures = findNumTreasuresShortestDis();
							if(treasureDis < shortestTreasureDis || (treasureDis == shortestTreasureDis && numTreasures > numTreasuresShortestDis)) {
								shortestTreasureDis = treasureDis;
								numTreasuresShortestDis = numTreasures;
								points = 1;
							}
						}
						
					}

				}
			}
			
		}
		
		copyBoard(boardStart);
		setRow(simRow); //Copy the original row
		setCol(simCol); //Copy the original col
		
		tileInHand.copy(tile);
		
		for(int i = 0; i < 4; i++) {
			players[i].copy(simPlayers[i]);
		}
		
		return points;
		
	}	
	
	//This method is used to copy the board from the GUI
	private void copyBoard(Board boardToCopy) {
		
		//Copy the board variables
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				
				board.getBoard()[row][col].copy(boardToCopy.getBoard()[row][col]);
				//System.out.println("copying " + row + " " + col);
				
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
		
		//Move self if on selected column
		if(getCol() == col) {
			setRow(getRow() + 1);
			if(getRow() == 8)
				setRow(1);
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
			}
		}
		
		//Move self if on selected column
		if(getCol() == col) {
			setRow(getRow() - 1);
			if(getRow() == 0)
				setRow(7);
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
			}
		}
		
		//Move self if on selected row
		if(getRow() == row) {
			setCol(getCol() + 1);
			if(getCol() == 8)
				setCol(1);
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
			}
		}
		
		//Move self if on selected row
		if(getRow() == row) {
			setCol(getCol() - 1);
			if(getCol() == 0)
				setCol(7);
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
	private void block(Tile tileInHand) {
		
		Tile tile = new Tile();
		tile.copy(tileInHand);
		int blockPoints = 0;
		
		for(int c = 0; c < 12; c++) {
			
			if(c == LabyrinthGUI.lastPush)
				continue;
			
			//Push a column down
			for(int i = 0; i < 3; i++) { 

				if(c == i) {
					
					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (i + 1) * 2;
						pushColDown(col, tile);
						
						//Award points to the created game state from this tile placement
						int points = blockPoints();
						if(points > blockPoints && treasurePath) {
							blockPoints = points;
							push = c;
							rotation = k;
						}
						
					}

				}
			}

			//Push a row left
			for(int i = 3; i < 6; i++) { 

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (i - 2) * 2;
						pushRowLeft(row, tile);
						
						//Award points to the created game state from this tile placement
						int points = blockPoints();
						if(points > blockPoints && treasurePath) {
							blockPoints = points;
							push = c;
							rotation = k;
						}
						
					}
					
				}
			}

			//Push a column up
			for(int i = 6; i < 9; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int col = (9 - i) * 2;
						pushColUp(col, tile);
						
						//Award points to the created game state from this tile placement
						int points = blockPoints();
						if(points > blockPoints && treasurePath) {
							blockPoints = points;
							push = c;
							rotation = k;
						}
						
					}
					
				}
			}

			//Push a row right
			for(int i = 9; i < 12; i++) {

				if(c == i) {

					for(int k = 0; k < 4; k++) { //Loop through each tile rotation
						
						copyBoard(LabyrinthGUI.board); //Copy the current game board
						copyStartPosition(); //Copy position at start of turn
						copyPlayers(); //Copy player position at start of turn
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
						
						//Award points to the created game state from this tile placement
						int points = blockPoints();
						if(points > blockPoints && treasurePath) {
							blockPoints = points;
							push = c;
							rotation = k;
						}
						
					}

				}
			}
			
		}
		
		selectMoveTile();
		
	}
	
	//This method awards points to a move intended to block a player 
	private int blockPoints() {
		
		int points = 0;
		int treasures = 0;
		
		//Add 20 points for every treasure path opened
		board.pathfind(getRow(), getCol());
		treasures = board.checkDirectTreasurePath(getHand());
		points += (20 * treasures);
		
		//Add 120 points if the player is blocked
		//Add 15 points for every other player blocked
		for(int i = 0; i < 4; i++) {
			
			if(i == playerNum || !players[i].isInGame())
				continue;
			
			board.pathfind(players[i].getRow(), players[i].getCol());
			if(board.checkDirectTreasurePath(players[i].getHand()) == 0 && players[i].getHand().size() == 1) 
				points += 100;
			else if(board.checkDirectTreasurePath(players[i].getHand()) == 0) 
				points += 15;
			
		}
		
		return points;
		
	}
	
	//This method awards points to a move intended to collect a treasure
	private int movePoints() {
		
		int points = 0;
		
		//Add 100 points for each direct treasure path that was not destroyed
		board.pathfind(getRow(), getCol());
		for(String treasure: directTreasures) {
			if(board.checkDirectTreasurePath(treasure))
				points += 100;
		}
		
		//Add 30 points for each treasure path created
		points += (board.checkDirectTreasurePath(getHand()) * 30);
		
		//Set the treasure path to true if treasures can be obtained
		if(points > 0 && !treasurePath)
			treasurePath = true;
		
		//Add 10 points for every other player blocked
		for(int i = 0; i < 4; i++) {
			
			if(i == playerNum || !players[i].isInGame())
				continue;
			
			//System.out.println("Player row and col = " + players[i].getRow() + " " + players[i].getCol() + " \nPlayer num = " + i);
			//System.out.println("Player row and col = " + LabyrinthGUI.players[i].getRow() + " " + LabyrinthGUI.players[i].getCol() + " \nPlayer num = " + i);
			board.pathfind(players[i].getRow(), players[i].getCol());
			if(board.checkDirectTreasurePath(players[i].getHand()) == 0) 
				points += 10;
			
		}
		
		return points;
		
	}
	
	//This method awards points if a move allows a treasure to be collected next turn
	private int secondMovePoints() {
		
		int points = 0;
		
		//Add 50 points for each treasure path created
		points += (board.checkDirectTreasurePath(getHand()) * 30);
		
		return points;
		
	}
	
	//This method returns the shortest distance to a treasure on the board
	private int findShortestTreasureDis() {
		
		
		ArrayList<Integer> distances = board.pathfindDis(getRow(), getCol(), getHand());
		
		int shortestDis = 50;
		
		if(distances.size() > 0)
			shortestDis = distances.get(0);
		
		return shortestDis;
		
	}
	
	//This method returns the number of treasures with shortest distance from the player
	private int findNumTreasuresShortestDis() {
		
		ArrayList<Integer> distances = board.pathfindDis(getRow(), getCol(), getHand());
		
		int shortestDis = 50;
		int numTreasures = 0;
		
		if(distances.size() > 0) {
			shortestDis = distances.get(0);
			numTreasures = 1;
		}
		
		for(int i = 1; i < distances.size(); i++) {
			if(distances.get(i) == shortestDis) {
				numTreasures++;
			} else {
				break;
			}
		}
		
		return numTreasures;
		
	}
	
	//This method copies the row and column values from the start of the turn
	private void copyStartPosition() {
		setRow(startRow);
		setCol(startCol);
	}
	
	private void copyPlayers() {
		
		for(int i = 0; i < 4; i++) {
			players[i].copy(LabyrinthGUI.players[i]);
		}
		
	}
	
	private void copyPlayers(Player[] simPlayers) {
		
		for(int i = 0; i < 4; i++) {
			players[i].copy(simPlayers[i]);
		}
		
	}
	
	private void printBoard() {
		
		//Copy the board variables
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				
				if(row == getRow() && col == getCol())
					System.out.print("X" + board.getBoard()[row][col].getType() + " ");
				else
					System.out.print(board.getBoard()[row][col].getType() + "  ");
				
			}
			System.out.println();
		}
		
	}

	private void printVis() {
		

			for(int row = 1; row < 8; row++) {
				for(int col = 1; col < 8; col++) {
					
					int i = (row - 1) * 7 + col;
					
					if(board.getVis()[i]) {
						System.out.print("X ");
					} else {
						System.out.print("O ");
					}
					
				}
				
				System.out.println();
				
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
						tile.copy(tileInHand); //Copy the tile in hand
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
						tile.copy(tileInHand); //Copy the tile in hand
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
						tile.copy(tileInHand); //Copy the tile in hand
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
						tile.copy(tileInHand); //Copy the tile in hand
						tile.setRotation(k); //Set the tile rotation
						int row = (12 - i) * 2;
						pushRowRight(row, tile);
					
					}

				}
			}
			
		}
		
	}	
	
}
