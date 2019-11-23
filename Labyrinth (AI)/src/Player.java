import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//This is the class for a player object to be used in the Labyrinth game
public class Player extends JLabel {
	
	private Random r = new Random();
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	private int row;
	private int col;
	private boolean inGame = true;
	
	//Constructor Method
	public Player(ImageIcon image, int row, int col) {
		
		this.row = row;
		this.col = col;
		setIcon(image);
		drawHand();
		
	}
	
	//Getters and Setters
	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}
	
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

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	//This method places 5 cards in the player's hand
	private void drawHand() {
		
		for(int i = 0; i < 5; i++) {
			
			//Generate a number from 0 to Card.deck.size() and add the
			//card at that index to the hand while removing it from the deck
			int cardIndex = 0;
			
			if(Card.deck.size() > 1) {
				cardIndex = r.nextInt(Card.deck.size());
			}
			
			hand.add(Card.deck.remove(cardIndex));
		}
		
	}
	
}
