import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * This class displays the victory screen for the game and gives an option to restart.
 * 
 */

public class LabyrinthVictoryScreen extends JFrame implements ActionListener {

	//Frame components
	private JPanel panel = new JPanel();
	private JLabel winnerLabel = new JLabel();
	private JLabel gameImageLabel = new JLabel(Assets.titleScreen);
	private JButton backButton = new JButton("Back");

	private int winner;

	//Constructor method
	public LabyrinthVictoryScreen(int winner){
		
		this.winner = winner;
		
		panelSetup();
		buttonSetup();
		frameSetup();
		cursorSetup();

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
	
	//This method sets up the title screen panel
	private void panelSetup(){

		panel.setBounds(0,0,1280,720);
		panel.setLayout(null);
		panel.setBackground(new Color(191,231,247));
		
		//Setup the labels
		winnerLabel.setText(String.format("Player %d Wins!", winner));
		winnerLabel.setBounds(275, 10, 800, 150);
		winnerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 105));
		panel.add(winnerLabel);
		
		gameImageLabel.setBounds(375, 160, 500, 500);
		gameImageLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 105));
		panel.add(gameImageLabel);
		
		//Revalidate and repaint the panel so the image labels appear
		panel.revalidate();
		panel.repaint();
		
		add(panel);

	}
	
	//This method sets up the back button
	private void buttonSetup() {
		
		backButton.setBounds(1000, 550, 200, 100);
		backButton.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
		backButton.addActionListener(this);
		panel.add(backButton);

	}

	//This method sets up the frame
	private void frameSetup(){
		
		//Setup the frame
		setLayout(null);
		setTitle("Labyrinth");
		setSize(1280,720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	//This method detects when a button is pressed
	@Override
	public void actionPerformed(ActionEvent event) {

		//Return to the title screen when one of the back buttons is pressed
		if(event.getSource() == backButton) {

			LabyrinthTitleScreen.m.StopMusic(); 
			
			MusicPlayer m2 = new MusicPlayer();
			m2.playMusic("Audio/sound.wav");
			
			LabyrinthTest.main(null);
			this.dispose();
			
		}
		
	}

}
