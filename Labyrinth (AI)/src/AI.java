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
	private int treasure; // add 10 for every treasure that can be reached with a move
	private int playersBlocked; // add 5 for every player blocked from reaching any treasure next move
	
	
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

	public void move() {
		
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
		
		int treasuresAvailable = 0;
		
		//Analyze the game state
		
	}
	
	//This method is used to copy the board from the GUI
	private void analyzeGameState() {
		
		//If you have more than 2 cards and another player(s) has 2 or less, try to block that player(s)
		if(getHand().size() > 2 && (handSizes[0] <= 2 || handSizes[1] <= 2 || handSizes[2] <= 2)) {
			
		} else if(getHand().size() <= 2 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you have more than 1 card and another player(s) has 1, try to block that player(s)
			
		} else if(getHand().size() == 1 && (handSizes[0] == 1 || handSizes[1] == 1 || handSizes[2] == 1)) { //If you and another player(s) has 1 card
			
			//Try collect last treasure but if you can't, block that player(s)
			
		} else {
			
			//Check if there's a direct path to treasure
			//If there's a direct path to treasure, try to create a path that allows you to collect another treasure next turn
			//If you can't setup a path for next turn, try to block other players
			
			//Check if you can create a direct path to treasure
			//Choose the option that will create a path to the most treasures and go for the one closest to the middle
			
			//Check if there are no treasures that can be reached this move
			//Try to create a path that will allow you to get a treasure next move
			//If that can't be done, try to block the player in the lead
			
			
			
		}
		
	}

	
	private void goToTreasure() {
		
	}
	
	private void setupTreasureRoute(int row, int col) {
		
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
	
}
