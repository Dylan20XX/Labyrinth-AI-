import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JLabel;

//This is the class for a board object to be used in the Labyrinth game
public class Board {
	
	private Tile[][] board = new Tile[9][9];
	private JLabel[][] highlight = new JLabel[9][9];
	private Random r = new Random();
	
    private boolean vis[] = new boolean[50];
    private Queue<Integer> Q = new LinkedList<Integer>();
    private HashMap<Integer, Integer> parentNodes = new HashMap<Integer, Integer>();
	
	private ArrayList<Integer> adj[] = new ArrayList[50]; //Adjacency matrix
	
	//4 L corner tiles
	//12 T non-movable treasure tiles
	//6 T movable treasure tiles
	//6 L movable treasure tiles
	//10 L tiles
	//12 I tiles

	//Constructor Method
	public Board() {
		super();
		
		//Initialize array lists in the adjacency matrix
		for(int i = 1; i < adj.length; i++) {
            adj[i] = new ArrayList<Integer>();
        }
	}
	
	//Getters and Setters
	public Tile[][] getBoard() {
		return board;
	}

	public void setBoard(Tile[][] board) {
		this.board = board;
	}
	
	public JLabel[][] getHighlight() {
		return highlight;
	}

	public void setHighlight(JLabel[][] highlight) {
		this.highlight = highlight;
	}
	
	public boolean[] getVis() {
		return vis;
	}

	public void setVis(boolean[] vis) {
		this.vis = vis;
	}

	public HashMap<Integer, Integer> getParentNodes() {
		return parentNodes;
	}

	public void setParentNodes(HashMap<Integer, Integer> parentNodes) {
		this.parentNodes = parentNodes;
	}

	//This method randomly fills the board with tiles
	public void fillBoard() {
		
		//Fill non-movable tiles
		board[1][1] = Tile.tileP1Start; 
		board[1][3] = Tile.tileBook; 
		board[1][5] = Tile.tileBag; 
		board[1][7] = Tile.tileP2Start; 
		
		board[3][1] = Tile.tilePainting; 
		board[3][3] = Tile.tileCrown; 
		board[3][5] = Tile.tileKeys; 
		board[3][7] = Tile.tileSkull; 
		
		board[5][1] = Tile.tileRing; 
		board[5][3] = Tile.tileBottle; 
		board[5][5] = Tile.tileGreen; 
		board[5][7] = Tile.tileSword; 
		
		board[7][1] = Tile.tileP3Start; 
		board[7][3] = Tile.tileCandles; 
		board[7][5] = Tile.tileMonster; 
		board[7][7] = Tile.tileP4Start; 
		
		//Fill the board with random tiles
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				
				//Fill the spots that have no permanent tiles
				if(board[row][col] == null) {
					
					//Generate a number from 0 to tileDeck.size() and add the
					//tile at that index to the board
					int cardIndex = 0;
					
					if(Tile.tileDeck.size() > 1) {
						cardIndex = r.nextInt(Tile.tileDeck.size());
					}
					
					board[row][col] = Tile.tileDeck.remove(cardIndex);
					
				}
				
				//Set the node number of the tile
				board[row][col].setNodeNum((row - 1) * 7 + col);
				
				//Set the bounds of all tiles on the board
				board[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				
				//Set the bounds of the highlight labels
				highlight[row][col] = new JLabel();
				highlight[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				
			}
		}
		
		//Initialize the boarder tiles
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				if(board[row][col] == null) {
					board[row][col] = new Tile();
				}
			}
		}
		
	}
	
	//This method pushes a column down
	public void pushColDown(int col) {
		for(int row = 8; row > 0; row--) {
			
			board[row][col].copy(board[row - 1][col]);

			if(board[row][col] != null)
				board[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
			
		}
	}
	
	//This method pushes a column up
	public void pushColUp(int col) {
		for(int row = 0; row < 8; row++){
			
			board[row][col].copy(board[row + 1][col]);
			
			if(board[row][col] != null)
				board[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
	}
	
	//This method pushes a column to the right
	public void pushRowRight(int row) {
		for(int col = 8; col > 0; col-- ){
			
			board[row][col].copy(board[row][col - 1]);
			
			if(board[row][col] != null)
				board[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
		
	}
	
	//This method pushes a column to the left
	public void pushRowLeft(int row) {
		for(int col = 0; col < 8; col++ ){
			
			board[row][col].copy(board[row][col + 1]);
			
			if(board[row][col] != null)
				board[row][col].setBounds(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
	}
	
	//This method will be used to find pieces connected to the piece that the player is on
	public void pathfind(int row, int col) {
		
		//Build the adjacency matrix
		buildAdjacencyMatrix();
		
		int nodeNum = (row - 1) * 7 + col; //Start point
		
		//Reset the parent nodes map
		parentNodes.clear();
		
		//Reset the queue and visited array
		Q.clear();
		for(int i = 0; i < 50; i++)
			vis[i] = false;

        Q.add(nodeNum);
        vis[nodeNum] = true;
        
        //Run BFS to check for all pathways
        while(!Q.isEmpty()) {
            int cur = Q.poll();
            for(int v : adj[cur]) {
            	if(!vis[v]) {
            		parentNodes.put(v, cur); //The key is the unvisited node and the value is the current node
            		Q.add(v);
            		vis[v] = true;
            	}	
            }
        }
        
        //Highlight the tiles that can be traveled to;
        highlightTiles();
		
	}
	
	//This method builds the adjacency matrix to be used for path finding
	private void buildAdjacencyMatrix() {
		
		//Set the node number for each tile
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				board[row][col].setNodeNum((row - 1) * 7 + col);
			}
		}
		
		//Clear the adjacency matrix
		for(int i = 1; i < adj.length; i++) {
            adj[i].clear();
        }
		
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				
				//Check if piece to the right is connected
				if(board[row][col].getOpenings()[1] && board[row][col + 1] != null && board[row][col + 1].getOpenings()[3] && col != 7) {
					adj[board[row][col].getNodeNum()].add(board[row][col + 1].getNodeNum());
					adj[board[row][col + 1].getNodeNum()].add(board[row][col].getNodeNum()); //Undirected graph
				}
				
				//Check if piece below is connected
				if(board[row][col].getOpenings()[2] && board[row + 1][col] != null && board[row + 1][col].getOpenings()[0] && row != 7) {
					adj[board[row][col].getNodeNum()].add(board[row + 1][col].getNodeNum());
					adj[board[row + 1][col].getNodeNum()].add(board[row][col].getNodeNum()); //Undirected graph
				}
				
			}
		}
		
	}
	
	//This method highlights tiles
	public void highlightTiles() {
		
        //Highlight available tiles
		for(int i = 1; i < adj.length; i++) {
			
			//Convert the node number back to a row and column number
    		int row = i / 7 + 1;
    		int col = i % 7;
    		
    		//Error check since col number will be set to 0 if col is divisible by 7
    		//Row number will also be 1 higher than it should be
    		if(col == 0) {
    			col = 7;
    			row -= 1;
    		}
    		
    		//Error check since row number will be set to 8 on node 49
    		if(row == 8) {
    			row = 7;
    		}
    		
    		//If the tile can be reached, highlight it yellow
			if(vis[i]) {
        		highlight[row][col].setIcon(Assets.tileHighlightYellow);
            } else { 
            	//Highlight unreachable tiles with blue
            	highlight[row][col].setIcon(Assets.tileHighlightBlue);
            }
        }
		
	}
	
	//This method removes the highlights from tiles
	public void removeHighlight() {
		
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				highlight[row][col].setIcon(null);
			}
		}
		
	}
	
	//This method returns an array list of the nodes that you must travel through to reach an end node
	public ArrayList<Position> findShortestPath(int startRow, int startCol, int endRow, int endCol) {
		
		ArrayList<Position> shortestPath = new ArrayList<Position>();
		
		//Get the start and end node from the rows and columns
		int startNode = (startRow - 1) * 7 + startCol;
		int endNode = (endRow - 1) * 7 + endCol;
		
		int currentNode = endNode;
		
		//Use the map to add the shortest path positions to the array list
		while(currentNode != startNode) {
			
			//Convert node number to row and column
    		int row = currentNode / 7 + 1;
    		int col = currentNode % 7;
    		
    		//Error check since col number will be set to 0 if col is divisible by 7
    		//Row number will also be 1 higher than it should be
    		if(col == 0) {
    			col = 7;
    			row -= 1;
    		}
    		
    		//Error check since row number will be set to 8 on node 49
    		if(row == 8) {
    			row = 7;
    		}
			
			shortestPath.add(new Position(row, col)); //Add the current position to the array list
			currentNode = parentNodes.get(currentNode); //Set the current node to the one before it on the path
			
		}
		
		//Reverse the array list because nodes are entered in reverse order
		Collections.reverse(shortestPath);
		
		//Return the shortest path array list
		return shortestPath;
		
	}
	
}
