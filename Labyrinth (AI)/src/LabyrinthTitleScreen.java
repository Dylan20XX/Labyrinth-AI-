import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/*
 * This class displays the title screen for the game.
 * It allows the player to select to start a new game or load a game,
 * and start playing the game.
 */

public class LabyrinthTitleScreen extends JFrame implements ActionListener {

	//Frame components
		private JPanel titleScreenPanel = new JPanel();
		private JPanel instructionsScreenPanel = new JPanel();
		private JPanel savedGamePanel = new JPanel();
		private JLabel titleLabel = new JLabel("LABYRINTH");
		//private JLabel titleImageLabel = new JLabel(Assets.titleScreen);
		private JLabel instructionsTitleLabel = new JLabel("HOW TO PLAY");
		private JLabel treasureCardExLabel = new JLabel(Assets.cardExample);
		private JLabel mazeTileExLabel = new JLabel(Assets.tileExample);
		private JLabel cardLabel = new JLabel("Card Example");
		private JLabel tileLabel = new JLabel("Tile Example");
		private JLabel savedGamesLabel = new JLabel("SAVED GAMES");
		private JButton playButton = new JButton("Play");
		private JButton instructionsButton = new JButton("How To Play");
		private JButton startButton = new JButton("Start");
		private JButton deleteButton = new JButton("Delete");
		private JButton savedGameButton = new JButton("Saved Games");
		private JButton backButton = new JButton("Back");
		private JButton backButton1 = new JButton("Back");
		private JPanel savedGameButtonPanel = new JPanel();
		private JScrollPane savedGamesScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		private ArrayList<JButton> savedGames= new ArrayList<JButton>();
	
	private JLabel instructionsLabel = new JLabel("<html>" + "Welcome to Labyrinth! The objective of the game is to collect all 5 of the treasures on your "
			+ "treasure cards." + "<br />" + "" + "<br />" + " To reach the treasure, you must first shift the walls of the maze. This is done by pushing the tiles down with the"
			+ "extra maze tile so you can move as far as you wish along its open passageways. The places that a tile can be added are indicated by "
			+ "the yellow buttons on the edges of the maze. The maze tile that is pushed out will be the next player's way of shifting the maze. "
			+ "You must move the maze before each turn, even if you don't need to. This way you can wall in other players! After you place the "
			+ "maze tile, you can choose where you want to move by left clicking the mouse. If you collect a treasure, that treasure card will "
			+ "dissapear from your hand and you must collect the following one.");

	public static MusicPlayer m = new MusicPlayer();
	
	private ArrayList<File> savedGameFiles = new ArrayList<File>();

	private int state = 0;//Stores which panel is active
	private String selectedGame;

	//Constructor method
	public LabyrinthTitleScreen(){
		
		titleScreenPanelSetup();
		instructionsScreenPanelSetup();
		savedGamePanelSetup();
		buttonSetup();
		playTheMusic();
		frameSetup();
		cursorSetup();
		setupFileList();

	}
	
	//By KADRI Soft on YouTube (https://www.youtube.com/watch?v=UnzpZj77hYE)
	//Makes a custom cursor image
	public void cursorSetup() {
		
		Toolkit t1 = Toolkit.getDefaultToolkit();
		Image img = t1.getImage("Images/mycursor.png");
		Point p = new Point(0,0);
		Cursor c = t1.createCustomCursor(img, p, "cursor.png");
		setCursor(c);
		
	}
	
	public void playTheMusic(){
		
		m.loopMusic("Audio/BlazerRail.wav");
		
	}

	//This method sets up the title screen panel
	private void titleScreenPanelSetup(){

		//Setup the panel
		titleScreenPanel.setBounds(0,0,1280,720);
		titleScreenPanel.setLayout(null);
		titleScreenPanel.setBackground(new Color(191,231,247));

		//Setup the label
		titleLabel.setBounds(375, 30, 700, 120);
		titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 105));
		titleScreenPanel.add(titleLabel);
		
		//titleImageLabel.setBounds(650, 100, 500, 500);
		//titleScreenPanel.add(titleImageLabel);

		//Revalidate and repaint the panel so the image labels appear
		titleScreenPanel.revalidate();
		titleScreenPanel.repaint();

	}
	
	//This method sets up the instructions screen panel
		private void instructionsScreenPanelSetup(){

			//Setup the panel
			instructionsScreenPanel.setBounds(0,0,1280,720);
			instructionsScreenPanel.setLayout(null);
			instructionsScreenPanel.setBackground(new Color(191,231,247));
			instructionsScreenPanel.setVisible(false);

			//Setup the label
			instructionsTitleLabel.setBounds(425, 30, 600, 60);
			instructionsTitleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
			instructionsScreenPanel.add(instructionsTitleLabel);
			
			//Label that says the instructions
			instructionsLabel.setBounds(100, 110, 1080, 300);
			instructionsLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			instructionsScreenPanel.add(instructionsLabel);
			
			treasureCardExLabel.setBounds(250, 475, 130, 150);
			instructionsScreenPanel.add(treasureCardExLabel);
			
			//Label that says Example Card
			cardLabel.setBounds(250, 425, 200, 50);
			cardLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			instructionsScreenPanel.add(cardLabel);
			
			mazeTileExLabel.setBounds(500, 475, 150, 150);
			instructionsScreenPanel.add(mazeTileExLabel);
			
			//Label that says the Example Tile
			tileLabel.setBounds(515, 425, 200, 50);
			tileLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			instructionsScreenPanel.add(tileLabel);

			//Revalidate and repaint the panel so the image labels appear
			titleScreenPanel.revalidate();
			titleScreenPanel.repaint();

		}

	//This method sets up the saved game panel
	private void savedGamePanelSetup() {

		//Setup the panel
		savedGamePanel.setBounds(0,0,1280,720);
		savedGamePanel.setLayout(null);
		savedGamePanel.setBackground(new Color(191,231,247));
		savedGamePanel.setVisible(false); //Initially hide the highscore panel 

		//Setup the label
		savedGamesLabel.setBounds(480, 30, 400, 50);
		savedGamesLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
		savedGamePanel.add(savedGamesLabel);
		
		//Setup the button panel
		savedGameButtonPanel.setLayout(new BoxLayout(savedGameButtonPanel, BoxLayout.Y_AXIS));
		//savedGameButtonPanel.setBounds(220, 100 ,250, 550);
		savedGameButtonPanel.setBackground(Color.DARK_GRAY);
				
		//Add the scroll pane
		savedGamesScrollPane = new JScrollPane(savedGameButtonPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		savedGamesScrollPane.setBounds(220,100,400,550);
		savedGamePanel.add(savedGamesScrollPane);

	}
	
	//This method sets up the list of saved game buttons
	private void setupFileList() {
		
		//reset the array lists
		savedGameFiles.clear();
		
		//setup the world select buttons
		File files = new File("SavedGames");
		System.out.println(files.listFiles().length);
		
		for(int i = 0; i < files.listFiles().length; i++) {
			savedGameFiles.add(files.listFiles()[i]);
			savedGames.add(new JButton(files.listFiles()[i].getName()));
			savedGames.get(i).addActionListener(this);
			savedGames.get(i).setSize(400, 150);
			savedGames.get(i).setMaximumSize(savedGames.get(i).getSize());
			savedGames.get(i).setMinimumSize(savedGames.get(i).getSize());
			savedGames.get(i).setPreferredSize(savedGames.get(i).getSize());
			savedGames.get(i).setFont(new Font("Comic Sans MS", Font.BOLD, 36));
			savedGameButtonPanel.add(savedGames.get(i));
		}

	}

	//This method sets up the buttons for each panel
	private void buttonSetup() {

		//Setup buttons for title screen
		playButton.setBounds(500, 200, 300, 100);
		playButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		playButton.setBackground(Color.ORANGE);
		playButton.addActionListener(this);
		titleScreenPanel.add(playButton);

		savedGameButton.setBounds(500, 350, 300, 100);
		savedGameButton.setBackground(Color.ORANGE);
		savedGameButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		savedGameButton.addActionListener(this);
		titleScreenPanel.add(savedGameButton);
		
		instructionsButton.setBounds(500, 500, 300, 100);
		instructionsButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		instructionsButton.setBackground(Color.ORANGE);
		instructionsButton.addActionListener(this);
		titleScreenPanel.add(instructionsButton);
		
		startButton.setBounds(700, 100, 250, 150);
		startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		startButton.addActionListener(this);
		savedGamePanel.add(startButton);

		deleteButton.setBounds(700, 350, 250, 150);
		deleteButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		deleteButton.addActionListener(this);
		savedGamePanel.add(deleteButton);
		
		backButton.setBounds(1000, 550, 200, 100);
		backButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		backButton.addActionListener(this);
		savedGamePanel.add(backButton);
		
		backButton1.setBounds(1000, 550, 200, 100);
		backButton1.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
		backButton1.addActionListener(this);
		instructionsScreenPanel.add(backButton1);

	}

	//This method sets up the frame
	private void frameSetup(){
		
		add(titleScreenPanel);
		add(instructionsScreenPanel);
		add(savedGamePanel);
		setLayout(null);
		setTitle("Labyrinth");
		setSize(1280,720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	//This method updates the state of the frame by changing which panels are visible and hidden
	public void updateState(){

		if(state == 0) {
			titleScreenPanel.setVisible(true);
			instructionsScreenPanel.setVisible(false);
			savedGamePanel.setVisible(false);
		}else if(state == 1) {
			titleScreenPanel.setVisible(false);
			instructionsScreenPanel.setVisible(false);
			savedGamePanel.setVisible(true);
		}else if(state == 2) {
			titleScreenPanel.setVisible(false);
			instructionsScreenPanel.setVisible(true);
			savedGamePanel.setVisible(false);

		}

	}

	//Getters and Setters
	public void setState(int state){
		this.state = state;
	}

	public int getState(){
		return state;
	}

	//This method detects when a button is pressed
	@Override
	public void actionPerformed(ActionEvent event) {

		//Enter the settings screen if the play button is pressed
		if(event.getSource() == playButton) {
			
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			new LabyrinthGUI();
			this.dispose();

		}


		//Enter the instructions screen if the instructions button is pressed
		if(event.getSource() == savedGameButton) {
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			
			setState(1);
			updateState();

		}
		
		if(event.getSource() == instructionsButton) {
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			
			setState(2);
			updateState();
		}

		//Return to the title screen when one of the back buttons is pressed
		if(event.getSource() == backButton) {
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			selectedGame = null;
			
			setState(0);
			updateState();
		}
		
		if(event.getSource() == backButton1) {
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			selectedGame = null;

			setState(0);
			updateState();
		}
		
		//Select a save game to play or delete
		for(int i = 0; i < savedGames.size(); i++) {
			if(event.getSource() == savedGames.get(i)) {
				
				//Change the background of the selected saved game
				for(int j = 0; j < savedGames.size(); j++) {
					savedGames.get(j).setBackground(new JButton().getBackground());
				}
				savedGames.get(i).setBackground(Color.YELLOW);
				
				selectedGame = savedGames.get(i).getText();
				
			}
		}
		
		if(selectedGame != null) {
			
			//Load or delete the selected game when the appropriate button is pressed
			if(event.getSource() == startButton) {
				
				new LabyrinthGUI(selectedGame);
				this.dispose();
				
			} else if(event.getSource() == deleteButton) {
				
				for(int i = 0; i < savedGames.size(); i++) {
					if(savedGames.get(i).getText().equals(selectedGame)) {
						savedGameButtonPanel.remove(savedGames.remove(i));
						savedGameButtonPanel.repaint();
						savedGameButtonPanel.revalidate();
					}	
				}
				
				deleteGame();
				
			}
			
		}
		
	}
	
	//This method is used to delete saved games
	private void deleteGame() {
		
		String gameData = String.format("SavedGames/%s", selectedGame);
		File gameDataFilepath = new File(gameData);

		if(gameDataFilepath.exists()) {
			gameDataFilepath.delete();
		}
		
		selectedGame = null;
		
	}

}
