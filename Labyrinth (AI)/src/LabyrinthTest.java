/* Name: Dylan, Rishi, Sam
 * Date: Nov 21, 2019
 * Course code: ICS 4U1
 * This is the class is used to run the program
  */
 
public class LabyrinthTest {
	
	public static void main(String[] args) {
		Assets.imageInitializer();
		Tile.setupTileDeck();
		Card.deckSetup();
		new LabyrinthTitleScreen();
	}
	
}
