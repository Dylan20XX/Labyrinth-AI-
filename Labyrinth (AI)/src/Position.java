
//This is the class for a position object to be used in the Labyrinth game
public class Position {
	
	private int row;
	private int col;
	
	//Constructor Method
	public Position(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	
	//Getters and Setters
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
}
