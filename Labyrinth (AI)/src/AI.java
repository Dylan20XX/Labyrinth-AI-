
public class AI {
	
	private Board board = new Board();
	private Player players[] = new Player[4];
	
	public AI() {
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
