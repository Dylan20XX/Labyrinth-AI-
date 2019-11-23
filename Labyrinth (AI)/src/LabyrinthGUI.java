import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

//This class contains the GUI for the Labyrinth game
public class LabyrinthGUI extends JFrame implements ActionListener{

	private JLabel titleLabel = new JLabel("Labyrinth");

	//60x60 tiles 540x540 board
	private JLayeredPane boardPanel = new JLayeredPane();
	private Board board = new Board();

	//Card Panel
	private JPanel cardPanel = new JPanel();
	private JLabel player1CardHeading = new JLabel("P1");
	private JLabel player2CardHeading = new JLabel("P2");
	private JLabel player3CardHeading = new JLabel("P3");
	private JLabel player4CardHeading = new JLabel("P4");
	private Player[] players = new Player[4];

	private Tile tileInHand;
	private JLabel tileInHandLabel = new JLabel();
	private int turn; //Determines whose turn it is
	private int phase; // 0 = time to place tile & 1 = time to move character
	private int lastPush = -1; //Holds the index of the last push button that was used
	private int selectedPush = -1; //Holds the index of the last push button that was used

	private JLabel turnLabel = new JLabel(); //Displays whose turn it is
	private JLabel phaseLabel = new JLabel(); //Displays which phase of the turn the player is on (tile placement or movement)

	//Buttons
	private JButton[] pushButton = new JButton[12];
	// Push button index layout
	//    0 1 2
	// 11       3
	// 10       4
	// 9        5
	//    8 7 6
	private JButton rotateRightButton = new JButton("Rotate Right");
	private JButton rotateLeftButton = new JButton("Rotate Left");
	private JButton confirmButton = new JButton("Confirm");
	private JButton saveButton = new JButton("Save Game");

	//Saving instructions label and text area
	private JLabel saveInstruction = new JLabel("Enter Save Name");
	private JTextArea saveName = new JTextArea();

	//Player animation variables
	private ArrayList<Position> shortestPath;
	private Timer moveTimer = new Timer(10, this);
	private int moveTime = 0;
	private int nextMove;
	private boolean moving = false;

	private int selectedRow = 0;
	private int selectedCol = 0;
	
	private int winner = 5;

	//Constructor for loading a new game
	public LabyrinthGUI() {

		//Setup the frame and panels
		buttonSetup();
		cardPanelSetup();
		boardPanelSetup();
		placePlayer();
		frameSetup();
		cursorSetup();
		
		//Start the first turn of the game
		startTurn(0, 0);

	}

	//Constructor for loading a saved game
	public LabyrinthGUI(String filepath) {

		//Setup the frame and panels
		buttonSetup();
		cardPanelSetup();
		boardPanelSetup();
		placePlayer();
		frameSetup();

		//Read data from the saved game file
		try {

			//Take input from the highscore file
			Scanner input = new Scanner(new File(String.format("SavedGames/%s", filepath))); 

			//Read the turn and phase
			turn = input.nextInt(); 
			phase = input.nextInt();

			//Copy the board variables
			for(int row = 1; row < 8; row++) {
				for(int col = 1; col < 8; col++) {

					int id = input.nextInt();
					int rotation = input.nextInt();

					board.getBoard()[row][col].copy(Tile.tiles[id]);
					board.getBoard()[row][col].setRotation(rotation);

				}
			}

			//Read the tile in hand
			int inHandId = input.nextInt();
			int inHandRotation = input.nextInt();

			tileInHand = new Tile();
			tileInHand.copy(Tile.tiles[inHandId]);
			tileInHand.setRotation(inHandRotation);

			//Read in the players' cards
			for(int i = 0; i < 4; i++) {

				int numCards = input.nextInt();

				//Remove the cards automatically given to the player when initialized from the card panel
				for(int j = 0; j < 5; j++) {
					cardPanel.remove(players[i].getHand().remove(0));
				}

				//Read in the data for each card in the player's hand
				for(int j = 0; j < numCards; j++) {

					int id = input.nextInt();

					Card card = new Card();

					card.setTreasure(Card.cards[id].getTreasure());
					card.setIcon(Card.cards[id].getIcon());
					card.setId(Card.cards[id].getId());

					players[i].getHand().add(card);
				}

			}

			//Call these methods since components are removed
			repaint();
			validate();

			displayCards();

			for(int i = 0; i < 4; i++) {

				int row = input.nextInt();
				int col = input.nextInt();

				players[i].setRow(row);
				players[i].setCol(col);
				updatePlayerLocation(i);
			}

			//Read the last push
			lastPush = input.nextInt();

			//Highlight the appropriate last push button
			if(lastPush != -1 && phase == 0)
				pushButton[lastPush].setIcon(Assets.tileHighlightRed);
			
			//Read which players have collected all of their treasures
			for(int i = 0; i < players.length; i++) {
				players[i].setInGame(input.nextBoolean());
				
				if(!players[i].isInGame())
					boardPanel.remove(players[i]);
			}
			
			//Read if a winner has been decided
			winner = input.nextInt();

			input.close();//Close the scanner

		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}

		startTurn(turn, phase);

	}

	public void cursorSetup() {

		Toolkit t1 = Toolkit.getDefaultToolkit();
		Image img = t1.getImage("Images/mycursor.png");
		Point p = new Point(0,0);
		Cursor c = t1.createCustomCursor(img, p, "cursor.png");
		setCursor(c);

	}

	//This method places the player on the board
	private void placePlayer() {

		for(int i = 0; i < 4; i++) {
			players[i].setSize(30, 30);
			updatePlayerLocation(i);
			boardPanel.add(players[i], new Integer(3));
		}

	}

	//This method sets up the buttons used to rotate and place tiles and save button
	private void buttonSetup() {

		rotateLeftButton.setBounds(650, 540, 120, 60);
		rotateLeftButton.addActionListener(this);
		add(rotateLeftButton);
		
		rotateRightButton.setBounds(650, 610, 120, 60);
		rotateRightButton.addActionListener(this);
		add(rotateRightButton);

		confirmButton.setBounds(1070, 540, 120, 60);
		confirmButton.addActionListener(this);
		add(confirmButton);

		saveButton.setBounds(90, 20, 120, 60);
		saveButton.addActionListener(this);
		add(saveButton);

	}

	//This method sets up the panel that contains the cards
	private void cardPanelSetup() {

		//Set the bounds of the card panel
		cardPanel.setLayout(null);
		cardPanel.setBounds(650, 90, Tile.TILE_SIZE * 9, 400);
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		cardPanel.setBackground(Color.BLACK);
		
		//Setup the headings for each player's cards
		player1CardHeading.setBounds(20, 35, 50, 30);
		player1CardHeading.setFont(new Font("Ultra", Font.BOLD, 36));
		player1CardHeading.setForeground(Color.red);
		cardPanel.add(player1CardHeading);

		player2CardHeading.setBounds(20, 135, 50, 30);
		player2CardHeading.setFont(new Font("Ultra", Font.BOLD, 36));
		player2CardHeading.setForeground(Color.yellow);
		cardPanel.add(player2CardHeading);

		player3CardHeading.setBounds(20, 235, 50, 30);
		player3CardHeading.setFont(new Font("Ultra", Font.BOLD, 36));
		player3CardHeading.setForeground(Color.green);
		cardPanel.add(player3CardHeading);

		player4CardHeading.setBounds(20, 335, 50, 30);
		player4CardHeading.setFont(new Font("Ultra", Font.BOLD, 36));
		player4CardHeading.setForeground(Color.blue);
		cardPanel.add(player4CardHeading);

		//Initialize the player objects so that their hands are drawn
		players[0] = new Player(Assets.p1, 1, 1);
		players[1] = new Player(Assets.p2, 1, 7);
		players[2] = new Player(Assets.p3, 7, 1);
		players[3] = new Player(Assets.p4, 7, 7);

		//Display the cards
		displayCards();

	}

	//This method displays the cards on card panel
	private void displayCards() {

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < players[i].getHand().size(); j++) {
				players[i].getHand().get(j).setBounds(100 + (j * 80), i * 100, 80, 100);
				cardPanel.add(players[i].getHand().get(j));
			}
		}

	}

	//This method sets up the panel that contains the board
	private void boardPanelSetup() {

		//Set the bounds of the board panel
		boardPanel.setLayout(null);
		boardPanel.setBounds(90, 90, Tile.TILE_SIZE * 9, Tile.TILE_SIZE * 9);
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		//Load the board
		loadBoard();

	}

	//This method loads the game board
	private void loadBoard(){

		//Fill the board
		board.fillBoard();

		int buttonIndex = 0;
		//Placeholder image - yellowTile
		//Add arrows to the top and bottom of the board
		for(int col = 1; col < 8; col++) {

			if(col == 2 || col == 4 || col == 6) {

				pushButton[buttonIndex] = new JButton(Assets.tileHighlightYellow);
				pushButton[buttonIndex].setBounds(col * Tile.TILE_SIZE, 0 * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				pushButton[buttonIndex].addActionListener(this);
				boardPanel.add(pushButton[buttonIndex], new Integer(1));

				pushButton[8 - buttonIndex] = new JButton(Assets.tileHighlightYellow);
				pushButton[8 - buttonIndex].setBounds(col * Tile.TILE_SIZE, 8 * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				pushButton[8 - buttonIndex].addActionListener(this);
				boardPanel.add(pushButton[8 - buttonIndex], new Integer(1));

				buttonIndex++;

			}

		}

		//Add arrows on the sides of the board
		for(int row = 1; row < 8; row++) {

			if(row == 2 || row == 4 || row == 6) {

				pushButton[14 - buttonIndex] = new JButton(Assets.tileHighlightYellow);
				pushButton[14 - buttonIndex].setBounds(0 * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				pushButton[14 - buttonIndex].addActionListener(this);
				boardPanel.add(pushButton[14 - buttonIndex], new Integer(1));

				pushButton[buttonIndex] = new JButton(Assets.tileHighlightYellow);
				pushButton[buttonIndex].setBounds(8 * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
				pushButton[buttonIndex].addActionListener(this);
				boardPanel.add(pushButton[buttonIndex], new Integer(1));

				buttonIndex++;

			}

		}

		//Add the rest of the tiles
		for(int row = 1; row < 8; row++) {
			for(int col = 1; col < 8; col++) {
				board.getBoard()[row][col].addActionListener(this);
				boardPanel.add(board.getBoard()[row][col], new Integer(1));
				boardPanel.add(board.getHighlight()[row][col], new Integer(2));
			}
		}

	}

	//This method sets up the frame
	private void frameSetup() {

		//Set the title and frame size
		setTitle("Labyrinth");
		setSize(1280, 720);
		setLayout(null);
		getContentPane().setBackground(new Color(191,231,247));

		//Add the panels
		add(boardPanel);
		add(cardPanel);

		//Add the turn label
		turnLabel.setFont(new Font("Ultra", Font.BOLD, 30));
		turnLabel.setBounds(840, 35, 200, 40);
		add(turnLabel);

		//Add the tileInHand label
		tileInHandLabel.setBounds(870, 540, 100, 100);
		add(tileInHandLabel);

		//Add the tileInHand label
		phaseLabel.setFont(new Font("Ultra", Font.BOLD, 30));
		phaseLabel.setBounds(770, 495, 300, 40);
		phaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(phaseLabel);

		//Add the title label
		titleLabel.setFont(new Font("Ultra", Font.BOLD, 36));
		titleLabel.setBounds(540, 15, 200, 40);
		add(titleLabel);

		//Add the save instruction label
		saveInstruction.setBounds(220, 20, 120, 30);
		saveInstruction.setFont(new Font("Arial", Font.BOLD, 14));
		add(saveInstruction);

		//Add the save name text area
		saveName.setBounds(220, 50, 120, 30);
		saveName.setFont(new Font("Arial", Font.BOLD, 18));
		add(saveName);

		//Stop the program from running when the frame is closed, prevent the 
		//frame from being resized, and make the frame visible
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	//This method starts the first turn of the game
	private void startTurn(int playerIndex, int phaseOfTurn) {

		turn = playerIndex;
		phase = phaseOfTurn;

		turnLabel.setText(String.format("P%d's Turn", playerIndex + 1));

		if(phase == 0) {
			board.removeHighlight();
			phaseLabel.setText("Place the tile");
		} else if(phase == 1) {
			phaseLabel.setText("Move your character");
			board.pathfind(players[turn].getRow(), players[turn].getCol());
		}

		if(tileInHand == null)
			tileInHand = Tile.tileDeck.remove(0);

		tileInHandLabel.setIcon(new ImageIcon(((ImageIcon) tileInHand.getIcon()).
				getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	//This method starts the next turn or phase of the game
	private void nextPhase() {

		if(phase == 0) {
			phase = 1;
			phaseLabel.setText("Move your character");
			board.pathfind(players[turn].getRow(), players[turn].getCol());
		} else if(phase == 1) {
			phase = 0;
			
			//Skip a player's turn if they are out of the game
			do {
				turn++;
				if(turn == 4)
					turn = 0;
			} while(!players[turn].isInGame());

			phaseLabel.setText("Place the tile");
			board.removeHighlight();
			turnLabel.setText(String.format("P%d's Turn", turn + 1));
		}

	}

	//This method begins movement of a player
	private void movePlayer(int playerTurn, int selectedRow, int selectedCol) {
		shortestPath = board.findShortestPath(players[playerTurn].getRow(), players[playerTurn].getCol(), selectedRow, selectedCol);
		moving = true;
		move(playerTurn);
	}

	//This method determines the next movement that the player makes and starts the movement timer
	private void move(int playerTurn) {

		if(!shortestPath.isEmpty()) {

			System.out.println("Row = " + shortestPath.get(0).getRow() + " Col = " + shortestPath.get(0).getCol());

			//Set the next move variable
			if(shortestPath.get(0).getRow() == players[playerTurn].getRow() - 1) { //Up
				nextMove = 0;
			} else if(shortestPath.get(0).getCol() == players[playerTurn].getCol() + 1) { //Right
				nextMove = 1;
			} else if(shortestPath.get(0).getRow() == players[playerTurn].getRow() + 1) { //Left
				nextMove = 2;
			} else if(shortestPath.get(0).getCol() == players[playerTurn].getCol() - 1) { //Down
				nextMove = 3;
			}

			players[playerTurn].setRow(shortestPath.get(0).getRow());
			players[playerTurn].setCol(shortestPath.get(0).getCol());

			shortestPath.remove(0);

		} else {
			nextMove = -1;
		}

		moveTimer.start();

	}

	//This method is to be used to update the player's location on the board
	//after a row or column of tiles is moved while the player is on the row or column
	private void updatePlayerLocation(int player) {
		players[player].setLocation(board.getBoard()[players[player].getRow()][players[player].getCol()].getX() + 
				15, board.getBoard()[players[player].getRow()][players[player].getCol()].getY() + 15);
	}

	//This method displays the player victory screen after the game is won
	private void playerVictory(int player) {
		System.out.printf("P%d Wins", player);
		new LabyrinthVictoryScreen(player);
		this.dispose();
	}

	//This method detects when a timer ticks or when a button is pressed
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == moveTimer) {

			if(nextMove == 0) { //Up
				players[turn].setLocation(players[turn].getX(), players[turn].getY() - 1);
			} else if(nextMove == 1) { //Right
				players[turn].setLocation(players[turn].getX() + 1, players[turn].getY());
			} else if(nextMove == 2) { //Down
				players[turn].setLocation(players[turn].getX(), players[turn].getY() + 1);
			} else if(nextMove == 3) { //Left
				players[turn].setLocation(players[turn].getX() - 1, players[turn].getY());
			}

			moveTime++;

			if(moveTime >= 60) {

				moveTime = 0;

				//If there are still more moves to be made, call the player method
				if(!shortestPath.isEmpty()) {
					move(turn);
				} else { //When animation is finished, check if a treasure is collected and move onto the next phase

					moving = false;
					moveTimer.stop(); //Stop the timer

					players[turn].setRow(selectedRow);
					players[turn].setCol(selectedCol);

					//					System.out.println("treasure needed = " + players[turn].getHand().get(0).getTreasure());
					//					System.out.println("treasure found = " + board.getBoard()[players[turn].getRow()][players[turn].getCol()].getTreasure());

					//Remove cards if treasure is collected
					for(int i = 0; i < players[turn].getHand().size(); i++) {

						if(players[turn].getHand().get(i).getTreasure().equalsIgnoreCase(
								board.getBoard()[players[turn].getRow()][players[turn].getCol()].getTreasure())) {

							cardPanel.remove(players[turn].getHand().remove(i));
							repaint();
							validate();

							displayCards();
							
							//Check if all cards in hand have been collected
							if(players[turn].getHand().isEmpty()) {
								
								//If no one has won yet, set the winner
								if(winner == 5)
									winner = turn + 1;
								
								//Remove the player from the game
								players[turn].setInGame(false);
								boardPanel.remove(players[turn]);
								
								//Check how many players are still in the game
								int numPlayers = 0;
								
								for(int j = 0; j < players.length; j++) {
									
									if(players[j].isInGame())
										numPlayers++;
									
								}
								
								//If there is one player remaining, end the game
								if(numPlayers <= 1)
									playerVictory(winner);
								
							}
								
							break;

						}

					}

					//Reset selectedRow and columnVariables;
					selectedRow = 0;
					selectedCol = 0;

					//Start the next phase and highlight the unusable push button
					nextPhase();
					for(int i = 0; i < 12; i++) {
						pushButton[i].setIcon(Assets.tileHighlightYellow);
					}
					pushButton[lastPush].setIcon(Assets.tileHighlightRed);
				}
			}
		}

		if(!moving) { //Other buttons can't be pressed while player is moving

			if(phase == 1) {
				for(int row = 1; row < 8; row++) {
					for(int col = 1; col < 8; col++) {
						if(e.getSource() == board.getBoard()[row][col]) {

							//Highlight available paths
							board.highlightTiles();

							if(board.getVis()[board.getBoard()[row][col].getNodeNum()]) {
								board.getHighlight()[row][col].setIcon(Assets.tileHighlightGreen);
								selectedRow = row;
								selectedCol = col;
							} else {
								board.getHighlight()[row][col].setIcon(Assets.tileHighlightRed);
								selectedRow = 0;
								selectedCol = 0;
							}

						}
					}
				}
			}

			//Detect which push button was pressed
			for(int i = 0; i < 12; i++) {
				if(e.getSource() == pushButton[i] && phase == 0) {
					selectedPush = i;
					for(int j = 0; j < 12; j++) {
						//Change the button image and revert all other button images to normal
						if(j == i) {
							pushButton[j].setIcon(Assets.tileHighlightBlue);
						} else {
							pushButton[j].setIcon(Assets.tileHighlightYellow);
						}

						//Change colour of the button opposite to the last row or column pushed
						if(j == lastPush)
							pushButton[j].setIcon(Assets.tileHighlightRed); 
					}
				}
			}
			
			//Allow the player to rotate the tile during the tile placement phase
			if(e.getSource() == rotateLeftButton && phase == 0) {

				if(tileInHand.getRotation() != 0)
					tileInHand.setRotation(tileInHand.getRotation()-1);
				else
					tileInHand.setRotation(3);

				tileInHandLabel.setIcon(new ImageIcon(((ImageIcon) tileInHand.getIcon()).
						getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

			}
			
			//Allow the player to rotate the tile during the tile placement phase
			if(e.getSource() == rotateRightButton && phase == 0) {

				if(tileInHand.getRotation() != 3)
					tileInHand.setRotation(tileInHand.getRotation()+1);
				else
					tileInHand.setRotation(0);

				tileInHandLabel.setIcon(new ImageIcon(((ImageIcon) tileInHand.getIcon()).
						getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

			}

			if(e.getSource() == confirmButton) {

				if(selectedPush >= 0 && selectedPush != lastPush && phase == 0) {

					//Push a column down
					for(int i = 0; i < 3; i++) { 

						if(selectedPush == i) {

							int col = (i + 1) * 2;

							board.getBoard()[0][col].copy(tileInHand);
							board.pushColDown(col);

							tileInHand.copy(board.getBoard()[8][col]);

							board.getBoard()[8][col] = new Tile();

							//Move a player if they are on the selected column
							for(int j = 0; j < 4; j++) {
								if(players[j].getCol() == col) {
									players[j].setRow(players[j].getRow() + 1);
									if(players[j].getRow() == 8)
										players[j].setRow(1);
									updatePlayerLocation(j);
								}
							}

						}
					}

					//Push a row left
					for(int i = 3; i < 6; i++) { 

						if(selectedPush == i) {

							int row = (i - 2) * 2;

							board.getBoard()[row][8].copy(tileInHand);
							board.pushRowLeft(row);

							tileInHand.copy(board.getBoard()[row][0]);

							board.getBoard()[row][0] = new Tile();

							//Move a player if they are on the selected row
							for(int j = 0; j < 4; j++) {
								if(players[j].getRow() == row) {
									players[j].setCol(players[j].getCol() - 1);
									if(players[j].getCol() == 0)
										players[j].setCol(7);
									updatePlayerLocation(j);
								}
							}

						}
					}

					//Push a column up
					for(int i = 6; i < 9; i++) {

						if(selectedPush == i) {

							int col = (9 - i) * 2;

							board.getBoard()[8][col].copy(tileInHand);
							board.pushColUp(col);

							tileInHand.copy(board.getBoard()[0][col]);

							board.getBoard()[0][col] = new Tile();

							//Move a player if they are on the selected column
							for(int j = 0; j < 4; j++) {
								if(players[j].getCol() == col) {
									players[j].setRow(players[j].getRow() - 1);
									if(players[j].getRow() == 0)
										players[j].setRow(7);
									updatePlayerLocation(j);
								}
							}

						}
					}

					//Push a row right
					for(int i = 9; i < 12; i++) {

						if(selectedPush == i) {

							int row = (12 - i) * 2;

							board.getBoard()[row][0].copy(tileInHand);
							board.pushRowRight(row);

							tileInHand.copy(board.getBoard()[row][8]);

							board.getBoard()[row][8] = new Tile();

							//Move a player if they are on the selected row
							for(int j = 0; j < 4; j++) {
								if(players[j].getRow() == row) {
									players[j].setCol(players[j].getCol() + 1);
									if(players[j].getCol() == 8)
										players[j].setCol(1);
									updatePlayerLocation(j);
								}
							}

						}
					}

					//Update the label with the tile in hand image
					tileInHandLabel.setIcon(new ImageIcon(((ImageIcon) tileInHand.getIcon()).
							getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

					//return the push buttons to normal
					for(int j = 0; j < 12; j++) {
						//Change the button image and revert all other button images to normal
						pushButton[j].setIcon(null);
					}

					//Set last push variable
					if(selectedPush == 0) {
						lastPush = 8;
					} else if(selectedPush == 1) {
						lastPush = 7;
					} else if(selectedPush == 2) {
						lastPush = 6;
					} else if(selectedPush == 3) {
						lastPush = 11;
					} else if(selectedPush == 4) {
						lastPush = 10;
					} else if(selectedPush == 5) {
						lastPush = 9;
					} else if(selectedPush == 6) {
						lastPush = 2;
					} else if(selectedPush == 7) {
						lastPush = 1;
					} else if(selectedPush == 8) {
						lastPush = 0;
					} else if(selectedPush == 9) {
						lastPush = 5;
					} else if(selectedPush == 10) {
						lastPush = 4;
					} else if(selectedPush == 11) {
						lastPush = 3;
					}
					//Reset selected push variable
					selectedPush = -1;

					//Move to player movement phase
					nextPhase();

				} else if (phase == 1) {

					//Move the player if a valid move has been selected
					if(selectedRow != 0 && selectedCol != 0 && board.getVis()[board.getBoard()[selectedRow][selectedCol].getNodeNum()]) {
						movePlayer(turn, selectedRow, selectedCol);
					}

				}

			}

			//Respond to when save game button is pressed
			if(e.getSource() == saveButton && !saveName.getText().equals("")) {
				System.out.println("Saving");
				saveGame(saveName.getText());
				saveName.setText("");
			}

		}

	}

	//This saves the game as a file with a name given by the user
	private void saveGame(String name) {

		String file = String.format("SavedGames/%s", name);
		File filepath = new File(file);

		if(!(filepath.exists() && !filepath.isDirectory())) {

			try {

				//Write data to a file
				PrintWriter pr = new PrintWriter(file);

				pr.println(turn + " " + phase); //Print turn and phase on the first line
				pr.println();

				//Save the board
				for(int row = 1; row < 8; row++) {
					for(int col = 1; col < 8; col++) {
						pr.println(board.getBoard()[row][col].getId() + " " + board.getBoard()[row][col].getRotation());
					}
					pr.println();
				}

				pr.println(tileInHand.getId() + " " + tileInHand.getRotation());
				pr.println();

				for(int i = 0; i < 4; i++) {
					pr.println(players[i].getHand().size()); //Print the size of the hand then the id of each card
					for(Card currentCard : players[i].getHand()) {
						pr.println(currentCard.getId());
					}
					pr.println();
				}

				//Print the row and column of each player
				for(int i = 0; i < 4; i++) {
					pr.println(players[i].getRow() + " " + players[i].getCol());
				}
				pr.println();

				//Print the last push
				pr.println(lastPush);
				
				//Print the game status of each player
				for(Player currentPlayer: players) {
					pr.println(currentPlayer.isInGame());
				}
				
				//Print the winner
				pr.println(winner);
				
				pr.close();

			} catch (FileNotFoundException e) {
				System.out.println("Save Failed");
			}

		}

	}

}
