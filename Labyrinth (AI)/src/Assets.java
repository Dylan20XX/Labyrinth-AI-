import java.awt.Image;

import javax.swing.ImageIcon;

//This class is used to set up image icons to be used for tiles, cards, and players
public class Assets {
	
	//4 L corner tiles
	//12 T non-movable treasure tiles
	//6 T movable treasure tiles
	//6 L movable treasure tiles
	//10 L tiles
	//12 I tiles
	
	// If there is tile in the picture name then it means the tile is permenant
	// 0 = down 1 = left 2 = up 3 = right
	
	public static ImageIcon p1 = tileImageSetup("Images/p1.png");
	public static ImageIcon p2 = tileImageSetup("Images/p2.png");
	public static ImageIcon p3 = tileImageSetup("Images/p3.png");
	public static ImageIcon p4 = tileImageSetup("Images/p4.png");
	
	public static ImageIcon titleScreen = new ImageIcon(new ImageIcon("Images/labyrinthTitleScreen.jpg").
			getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH));
	
	public static ImageIcon cardExample = new ImageIcon(new ImageIcon("Images/CardBook.png").
			getImage().getScaledInstance(130, 150, Image.SCALE_SMOOTH)); 
	
	public static ImageIcon tileExample = new ImageIcon(new ImageIcon("Images/BookTile.png").
			getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
	
	public static ImageIcon tileHighlightYellow = tileImageSetup("Images/YellowHighlight.png");
	public static ImageIcon tileHighlightBlue = tileImageSetup("Images/BlueHighlight.png");
	public static ImageIcon tileHighlightRed = tileImageSetup("Images/RedHighlight.png");
	public static ImageIcon tileHighlightGreen = tileImageSetup("Images/GreenHighlight.png");
	public static ImageIcon cardOutline = cardImageSetup("Images/CardOutline.png");

	//Movable treasure tiles
	public static ImageIcon tileL[] = new ImageIcon [4];
	public static ImageIcon tileI[] = new ImageIcon [4];
	public static ImageIcon tileDragon[] = new ImageIcon [4];
	public static ImageIcon tileGhostWaving[] = new ImageIcon [4];
	public static ImageIcon tileGhostBottle[] = new ImageIcon [4];
	public static ImageIcon tileLadyPig[] = new ImageIcon [4];
	public static ImageIcon tileLizard[] = new ImageIcon [4];
	public static ImageIcon tileMoth[] = new ImageIcon [4];
	public static ImageIcon tileSpider[] = new ImageIcon [4];
	public static ImageIcon tileSorceress[] = new ImageIcon [4];
	public static ImageIcon tileScarab[] = new ImageIcon [4];
	public static ImageIcon tileRat[] = new ImageIcon [4];
	public static ImageIcon tileOwl[] = new ImageIcon [4];
	public static ImageIcon tileBat[] = new ImageIcon [4];
	public static ImageIcon permenantTiles[] = new ImageIcon [16];
	public static ImageIcon treasureCards[] = new ImageIcon [24];
	//sddsb
	
	public static void imageInitializer(){
		//Have an array for each tile that is moving and there are 13 movable tiles in total
		
		tileL[0]= tileImageSetup("Images/L0.png");
		tileL[1]= tileImageSetup("Images/L1.png");
		tileL[2]= tileImageSetup("Images/L2.png");
		tileL[3]= tileImageSetup("Images/L3.png");
		
		tileI[0]= tileImageSetup("Images/I0.png");
		tileI[1]= tileImageSetup("Images/I1.png");
		tileI[2]= tileImageSetup("Images/I2.png");
		tileI[3]= tileImageSetup("Images/I3.png");
		
		tileDragon[0]= tileImageSetup("Images/Dragon0.png");
		tileDragon[1]= tileImageSetup("Images/Dragon1.png");
		tileDragon[2]= tileImageSetup("Images/Dragon2.png");
		tileDragon[3]= tileImageSetup("Images/Dragon3.png");
		
		tileGhostBottle[0]= tileImageSetup("Images/GhostBottle0.png");
		tileGhostBottle[1]= tileImageSetup("Images/GhostBottle1.png");
		tileGhostBottle[2]= tileImageSetup("Images/GhostBottle2.png");
		tileGhostBottle[3]= tileImageSetup("Images/GhostBottle3.png");
		
		tileGhostWaving[0] = tileImageSetup("Images/GhostWaving0.png");
		tileGhostWaving[1] = tileImageSetup("Images/GhostWaving1.png");
		tileGhostWaving[2] = tileImageSetup("Images/GhostWaving2.png");
		tileGhostWaving[3] = tileImageSetup("Images/GhostWaving3.png");
		
		tileLadyPig[0]= tileImageSetup("Images/LadyPig0.png");
		tileLadyPig[1]= tileImageSetup("Images/LadyPig1.png");
		tileLadyPig[2]= tileImageSetup("Images/LadyPig2.png");
		tileLadyPig[3]= tileImageSetup("Images/LadyPig3.png");
		
		tileLizard[0]= tileImageSetup("Images/Lizard0.png");
		tileLizard[1]= tileImageSetup("Images/Lizard1.png");
		tileLizard[2]= tileImageSetup("Images/Lizard2.png");
		tileLizard[3]= tileImageSetup("Images/Lizard3.png");
		
		tileMoth[0]= tileImageSetup("Images/Moth0.png");
		tileMoth[1]= tileImageSetup("Images/Moth1.png");
		tileMoth[2]= tileImageSetup("Images/Moth2.png");
		tileMoth[3]= tileImageSetup("Images/Moth3.png");
		
		tileSpider[0]= tileImageSetup("Images/Spider0.png");
		tileSpider[1]= tileImageSetup("Images/Spider1.png");
		tileSpider[2]= tileImageSetup("Images/Spider2.png");
		tileSpider[3]= tileImageSetup("Images/Spider3.png");
		
		tileSorceress[0]= tileImageSetup("Images/Sorceress0.png");
		tileSorceress[1]= tileImageSetup("Images/Sorceress1.png");
		tileSorceress[2]= tileImageSetup("Images/Sorceress2.png");
		tileSorceress[3]= tileImageSetup("Images/Sorceress3.png");
		
		tileScarab[0]= tileImageSetup("Images/Scarab0.png");
		tileScarab[1]= tileImageSetup("Images/Scarab1.png");
		tileScarab[2]= tileImageSetup("Images/Scarab2.png");
		tileScarab[3]= tileImageSetup("Images/Scarab3.png");
		
		tileRat[0]= tileImageSetup("Images/Rat0.png");
		tileRat[1]= tileImageSetup("Images/Rat1.png");
		tileRat[2]= tileImageSetup("Images/Rat2.png");
		tileRat[3]= tileImageSetup("Images/Rat3.png");
		
		tileOwl[0]= tileImageSetup("Images/Owl0.png");
		tileOwl[1]= tileImageSetup("Images/Owl1.png");
		tileOwl[2]= tileImageSetup("Images/Owl2.png");
		tileOwl[3]= tileImageSetup("Images/Owl3.png");
		
		tileBat[0]= tileImageSetup("Images/Bat0.png");
		tileBat[1]= tileImageSetup("Images/Bat1.png");
		tileBat[2]= tileImageSetup("Images/Bat2.png");
		tileBat[3]= tileImageSetup("Images/Bat3.png");
		
		permenantTiles[0] = tileImageSetup("Images/RedCornerTile.png");
		permenantTiles[1] = tileImageSetup("Images/BookTile.png");
		permenantTiles[2] = tileImageSetup("Images/BagTile.png");
		permenantTiles[3] = tileImageSetup("Images/YellowTileCorner.png");
		permenantTiles[4] = tileImageSetup("Images/PaintingTile.png");
		permenantTiles[5] = tileImageSetup("Images/CrownTile.png");
		permenantTiles[6] = tileImageSetup("Images/KeysTile.png");
		permenantTiles[7] = tileImageSetup("Images/SkullTile.png");
		permenantTiles[8] = tileImageSetup("Images/RingTile.png");
		permenantTiles[9] = tileImageSetup("Images/BottleTile.png");
		permenantTiles[10] = tileImageSetup("Images/GreenTile.png");
		permenantTiles[11] = tileImageSetup("Images/SwordTile.png");
		permenantTiles[12] = tileImageSetup("Images/GreenCornerTile.png");
		permenantTiles[13] = tileImageSetup("Images/CandlesTile.png");
		permenantTiles[14] = tileImageSetup("Images/MonsterTile.png");
		permenantTiles[15] = tileImageSetup("Images/BlueCornerTile.png");
		
		//Non-movable cards
		treasureCards[0] = cardImageSetup("Images/CardBook.png");
		treasureCards[1] = cardImageSetup("Images/CardBag.png");
		treasureCards[2] = cardImageSetup("Images/CardPainting.png");
		treasureCards[3] = cardImageSetup("Images/CardCrown.png");
		treasureCards[4] = cardImageSetup("Images/CardKeys.png");
		treasureCards[5] = cardImageSetup("Images/CardSkull.png");
		treasureCards[6] = cardImageSetup("Images/CardRing.png");
		treasureCards[7] = cardImageSetup("Images/CardBottle.png");
		treasureCards[8] = cardImageSetup("Images/CardGreen.png");
		treasureCards[9] = cardImageSetup("Images/CardSword.png");
		treasureCards[10] = cardImageSetup("Images/CardCandles.png");
		treasureCards[11] = cardImageSetup("Images/CardMonster.png");
		
		//Movable cards
		treasureCards[12] = cardImageSetup("Images/CardBat.png");
		treasureCards[13] = cardImageSetup("Images/CardDragon.png");
		treasureCards[14] = cardImageSetup("Images/CardGhostBottle.png");
		treasureCards[15] = cardImageSetup("Images/CardGhostWaving.png");
		treasureCards[16] = cardImageSetup("Images/CardLadyPig.png");
		treasureCards[17] = cardImageSetup("Images/CardLizard.png");
		treasureCards[18] = cardImageSetup("Images/CardMoth.png");
		treasureCards[19] = cardImageSetup("Images/CardOwl.png");
		treasureCards[20] = cardImageSetup("Images/CardRat.png");
		treasureCards[21] = cardImageSetup("Images/CardScarab.png");
		treasureCards[22] = cardImageSetup("Images/CardSorceress.png");
		treasureCards[23] = cardImageSetup("Images/CardSpider.png");
		
	}
	
	//This method returns a scaled tile image (60x60)
	private static ImageIcon tileImageSetup(String imagePath) {
		return new ImageIcon(new ImageIcon(imagePath).
				getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
	}
	
	//This method returns a scaled tile image (60x60)
	private static ImageIcon cardImageSetup(String imagePath) {
		return new ImageIcon(new ImageIcon(imagePath).
				getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH));
	}
	
	//This method returns a scaled player image (30x30)
	private static ImageIcon playerImageSetup(String imagePath) {
		return new ImageIcon(new ImageIcon(imagePath).
				getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
	}
	
	
}
