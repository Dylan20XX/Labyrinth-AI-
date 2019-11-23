import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//This is the class for a card object to be used in the Labyrinth game
public class Card extends JLabel{
	
	private String treasure;
	private int id;
	
	public static Card[] cards = new Card[24];
	
	public static ArrayList<Card> deck = new ArrayList<Card>();
	
	//Non-movable Treasures - labeled in order that they appear on the gam board from top right
	public static Card bookCard = new Card("book", Assets.treasureCards[0], 0);
	public static Card bagCard = new Card("bag", Assets.treasureCards[1], 1);
	public static Card paintingCard = new Card("painting", Assets.treasureCards[2], 2);
	public static Card crownCard = new Card("crown", Assets.treasureCards[3], 3);
	public static Card keysCard = new Card("keys", Assets.treasureCards[4], 4);
	public static Card skullCard = new Card("skull", Assets.treasureCards[5], 5);
	public static Card ringCard = new Card("ring", Assets.treasureCards[6], 6);
	public static Card bottleCard = new Card("bottle", Assets.treasureCards[7], 7);
	public static Card greenCard = new Card("green", Assets.treasureCards[8], 8);
	public static Card swordCard = new Card("sword", Assets.treasureCards[9], 9);
	public static Card candlesCard = new Card("candles", Assets.treasureCards[10], 10);
	public static Card monsterCard = new Card("monster", Assets.treasureCards[11], 11);
	
	//Movable Treasures
	public static Card batCard = new Card("bat", Assets.treasureCards[12], 12);
	public static Card dragonCard = new Card("dragon", Assets.treasureCards[13], 13);
	public static Card ghostBottleCard = new Card("ghostBottle", Assets.treasureCards[14], 14);
	public static Card ghostWavingCard = new Card("ghostWaving", Assets.treasureCards[15], 15);
	public static Card ladyPigCard = new Card("ladyPig", Assets.treasureCards[16], 16);
	public static Card lizardCard = new Card("lizard", Assets.treasureCards[17], 17);
	public static Card mothCard = new Card("moth", Assets.treasureCards[18], 18);
	public static Card owlCard = new Card("owl", Assets.treasureCards[19], 19);
	public static Card ratCard = new Card("rat", Assets.treasureCards[20], 20);
	public static Card scarabCard = new Card("scarab", Assets.treasureCards[21], 21);
	public static Card sorceressCard = new Card("sorceress", Assets.treasureCards[22], 22);
	public static Card spiderCard = new Card("spider", Assets.treasureCards[23], 23);
	
	//Constructor Method
	public Card(String treasure, ImageIcon image, int id) {
		super();
		this.treasure = treasure;
		this.id = id;
		setIcon(image);
		cards[id] = this;
	}
	
	//Empty Constructor Method
	public Card() {
	}
	
	//Getter and Setters
	public String getTreasure() {
		return treasure;
	}
	
	public void setTreasure(String treasure) {
		this.treasure = treasure;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static void deckSetup() {
		
		for(int i = 0; i < cards.length; i++) {
			deck.add(cards[i]);
		}
		
	}
	
}
