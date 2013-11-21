/**
 * MancalaBoardView Class
 * The graphical representation of the mancala board.
 *
 * @package mancala.view
 * @author Jordan Robinson, Ryan Carter
 * @version 3.0.0.1 16 Feb 2012
 */
package mancala.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.MouseInputAdapter;

import mancala.model.MancalaBoard;
import mancala.model.MancalaPiece;

import core.model.Resources;
import core.view.AbstractView;

@SuppressWarnings("serial") //should never be saved since it's a view
public class MancalaBoardView extends AbstractView implements Observer {

	//variable assignment here
	private int xStart = 155; //the horizontal position at which the interactive section of the board begins
	private int yStart = 90; //the vertical position at which the interactive section of the board begins
	private int tileHeight = 100; //the height of a pot tile
	private int tileWidth = 65; //the width of a pot tile
	private int xPadding = 10; //the horizontal padding between tiles
	private int yPadding = 20; //the vertical padding between tiles
	private MancalaBoard board; //the logical representation of the board
	private BufferedImage selectedImage;
	private BufferedImage hoverBox;
	private BufferedImage gemImage;
	private int currentHoverValue; //the value currently shown by the hover box
	private int[] lastSelectedTile; //the last selected tile (used for the AI so it can select tiles like a human player)

	//constructors here
	/**
	 * Default constructor for the Mancala Board View
	 */
	protected MancalaBoardView(MancalaBoard board)
	{
		//variables passed in
		this.board = board;

		//variables not passed in
		background = (BufferedImage) Resources.load("src/resources/mancalaBoard.png", "image");
		selectedImage = (BufferedImage) Resources.load("src/resources/mancalaSelected.png", "image");
		hoverBox = (BufferedImage) Resources.load("src/resources/hoverBox.png", "image");
		addMouseListener(new MouseGridListener());
		board.addObserver(this);
		addMouseMotionListener(new MouseHoverListener());
		currentHoverValue = -1; //-1 so that it's changed on the first check
		lastSelectedTile = new int[2];
		lastSelectedTile[0] = -1;
		lastSelectedTile[1] = -1;
	}


	//gets here
	public MancalaBoard getBoard()
	{
		return board;
	}
	//sets here

	//methods here
	/**
	 * Converts an int given that represents a tile number into the x and y position of the tile
	 * 
	 * @param tileNumber The tile number to convert
	 * @return The converted tile number as an array of integers
	 */
	private int[] convertFromTileNumber(int tileNumber)
	{
		int xPos = 0, yPos = 0;
	
		if (tileNumber > 5) { //if the number is over 5, we invert it, since the board is logically circular
			switch (tileNumber) { 
			case 11: xPos = 0; break;
			case 10: xPos = 1; break;
			case 9: xPos = 2; break;
			case 8: xPos = 3; break;
			case 7: xPos = 4; break;
			case 6: xPos = 5; break;
			}
			yPos = 1; //and if the number is over 5, we're on the second row, so change yPos as appropriate
		}
		else {
			xPos = tileNumber; //otherwise all that needs changing is xPos to tile number
		}
	
		int[] ret = {xPos,yPos}; //initialise the array to return
		return ret;
	}
	
	/**
	 * Takes the x and y position and converts it to the number needed to reference that tile in the board
	 * 
	 * @param xPos The x position of the tile to convert
	 * @param yPos The y position of the tile to convert
	 * @return The converted tile number
	 */
	private int convertToTileNumber(int xPos, int yPos)
	{
		int ret = 0;
		if (yPos > 0) {
			switch (xPos) {
			case 0: ret = 11; break;
			case 1: ret = 10; break;
			case 2: ret = 9; break;
			case 3: ret = 8; break;
			case 4: ret = 7; break;
			case 5: ret = 6; break;
			default:break;
			}
		}
		else {
			ret = xPos;
		}
	
		return ret;
	}

	/**
	 * Determine the colour of a piece based on its memory address.
	 * 
	 * This allows for the colours of the pieces to be persistent (as in, not determined again on every draw) without storing an extra variable, such as colour.
	 * 
	 * @param piece The piece to determine the colour of
	 */
	private void determineColour(MancalaPiece piece)
	{
		String pieceString = piece.toString(); //to string so we can get the memory address
		char rand = pieceString.charAt(pieceString.length()-1); //the last character is random enough for our purposes
	
		switch (rand) { //then switch the possible values for colours
		case '0': 
		case '6':
		case 'c':
			gemImage = (BufferedImage) Resources.load("src/resources/whitePiece.png", "image"); break;
		case '1':
		case '7':
		case 'd':
			gemImage = (BufferedImage) Resources.load("src/resources/redPiece.png", "image"); break;
		case '2':
		case '8':
		case 'e':
			gemImage = (BufferedImage) Resources.load("src/resources/bluePiece.png", "image"); break;
		case '3':
		case '9':
		case 'f':
			gemImage = (BufferedImage) Resources.load("src/resources/greenPiece.png", "image"); break;
		case '4':
		case 'a':
			gemImage = (BufferedImage) Resources.load("src/resources/blackPiece.png", "image"); break;
		case '5':
		case 'b':
			gemImage = (BufferedImage) Resources.load("src/resources/yellowPiece.png", "image"); break;
		default:break;
		}
	}


	/**
	 * Determine the position of a piece in a tile based on its memory address.
	 * 
	 * This allows for the colours of the pieces to be persistent (as in, not determined again on every draw) without storing an extra variable, such as position.
	 * 
	 * @param piece The piece to determine the position of
	 */
	private int[] determinePosition(MancalaPiece piece)
	{
		String pieceString = piece.toString(); //get the memory address
		char rand = pieceString.charAt(pieceString.length()-1); //the last character should be random enough for our purposes
		int[] ret = new int[2];
	
		for (int i = 0; i < 2; i++) { //setting the x value
			switch (rand) {
			case '0': ret[i] = 0; break;
			case '1': ret[i] = 3; break;
			case '2': ret[i] = 6; break;
			case '3': ret[i] = 9; break;
			case '4': ret[i] = 12; break;
			case '5': ret[i] = 15; break;
			case '6': ret[i] = 18; break;
			case '7': ret[i] = 21; break;
			case '8': ret[i] = 24; break;
			case '9': ret[i] = 27; break;
			case 'a': ret[i] = 30; break;	
			case 'b': ret[i] = 32; break;
			case 'c': ret[i] = 34; break;
			case 'd': ret[i] = 36; break;
			case 'e': ret[i] = 38; break;
			case 'f': ret[i] = 40; break;			
			default:break;
			}
			rand = pieceString.charAt(pieceString.length()-2); //get the second last character, for the y value
		}
		return ret;
	}

	/**
	 * Draw a piece located in one of the mancala tiles
	 * 
	 * @param mancalaNumber The number denoting which mancala to draw the piece in 
	 * @param piece The piece to draw in the mancala
	 * @param g The graphics object that performs the actual drawing of the images
	 */
	private void drawMancalaTilePiece(int mancalaNumber, MancalaPiece piece, Graphics g)
	{
		int xCoord = 0, yCoord = 0;
	
		if (mancalaNumber == 0) {
			xCoord = 55; //setting the pixel value to the location of the mancala
		}
		else if (mancalaNumber == 1) {
			xCoord = 605;
		}
	
		yCoord = 130;
	
		xCoord += (determinePosition(piece)[0])+10; //getting the position and accounting for the fact we're drawing in a mancala rather than a pot tile
		yCoord += (determinePosition(piece)[1])*2.2; //roughly centre the pieces within each tile
		g.drawImage(gemImage, xCoord, yCoord, null); //actually draw the image using the parameters given
	}

	/**
	 * Draws a mancala piece within a pot tile
	 * 
	 * @param tileX The x representation of the tile
	 * @param tileY The y representation of the tile
	 * @param piece The piece to draw
	 * @param g The graphics object that performs the actual drawing of the images
	 */
	private void drawPotTilePiece(int tileX, int tileY, MancalaPiece piece, Graphics g)
	{	
		int xCoord = xStart, yCoord = yStart; //set the coordinates to the start of the interactive area of the board
	
		xCoord += (((tileX*tileWidth)+tileX*xPadding)+determinePosition(piece)[0]);
		yCoord += (((tileY*tileHeight)+tileY*yPadding)+(determinePosition(piece)[1]+5)*1.5); //roughly centre the pieces within each tile
		g.drawImage(gemImage, xCoord, yCoord, null); //draw the pieces using the variables obtained
	}

	/**
	 * Draw the tile that was selected last turn
	 * 
	 * @param xPos The x position of the tile that was selected
	 * @param yPos The y position of the tile that was selected
	 */
	private void drawSelected(int xPos, int yPos)
	{
		if (xPos < 0 || yPos < 0) { //if this has happened, something went wrong, so just return
			return;
		}
	
		lastSelectedTile[0] = xPos; //assign the last selected tiles (this is for the AI)
		lastSelectedTile[1] = yPos;
	
		int wid = xStart + (xPos * tileWidth + (xPos*xPadding)); //perform the calculation to change the x and y positions into pixel values
		int hig = yStart + (yPos * tileHeight + (yPos*yPadding));
	
		getGraphics().drawImage(selectedImage, wid, hig, null); //get the graphics object of this object, and use it to draw the image
	}

	/**
	 * Paints the mancala board, using the model representation as a guide
	 * 
	 * @param g The graphics object used to draw images
	 */
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(background, 0, 0, null); //draw the background image

		//draw pieces start
		int y = 0;
		int x = 0;

		for (x = 0; x < 6; x++) { //draw the pot tiles pieces
			for (y = 0; y < 2; y++) {
				int tileNumber = convertToTileNumber(x, y);				
				for (int i = 0; i < board.getPotTiles()[tileNumber].getPieces().size(); i++) {					
					determineColour(board.getPotTiles()[tileNumber].getPieces().get(i));
					drawPotTilePiece(x,y,board.getPotTiles()[tileNumber].getPieces().get(i),g);
				}
			}
		}

		for (int i = 0; i < board.getMancalaTiles().length; i++) { //draw the mancala tiles pieces
			for (int j = 0; j < board.getMancalaTiles()[i].getPieces().size(); j++) {
				determineColour(board.getMancalaTiles()[i].getPieces().get(j));
				drawMancalaTilePiece(i, board.getMancalaTiles()[i].getPieces().get(j), g);
			}
		}
		//draw pieces end
		drawSelected(lastSelectedTile[0], lastSelectedTile[1]);
		displayMessage(getGraphics());
	}

	/**
	 * Overrides update in AbstractView. Updates this graphical component.
	 * 
	 * @param arg0 The object that is being observed
	 * @param arg1 The message passed from the observed object
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		super.update(arg0, arg1); //call the superclass method with the parameters, this should filter out anything relevant to all the games, such as the message box updating
		
		if(arg1 != null && arg1.equals("turnChange")) { //if a turn change happens, this message should come in
			drawTurnChange(board.getActivePlayer().getName()); //so we draw it accordingly
		}
		
		paint(getGraphics()); //then redraw this board

		if(arg1 != null && ((String) arg1).charAt(0) == 'A') { //This section is for drawing the selected tiles when the AI has selected them
			
			StringBuilder placeholder = new StringBuilder((String) arg1); //the message should contain the phrase 'AISelect-' followed by the tile that the AI selected
			int tileNumber = Integer.parseInt(placeholder.substring(9, placeholder.length())); //get the tile number from the message
			int[] coords = convertFromTileNumber(tileNumber); //then get the x y coordinates of the tile
			drawSelected(coords[0],coords[1]); //so that we can draw the selected tile
		}
	}

	/**
	 * Mouse listener that performs the tasks needed for the user to interact with the graphical interface
	 */
	private class MouseGridListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {

			if (board.getPlayers().get(0).isAI() && board.getPlayers().get(1).isAI()) { //if both players are AIs, then we want to call next player, since we can't call it before the board has been drawn
				board.nextPlayer();
			}

			if (board.getLocalPlayer() != null && board.getActivePlayer() != board.getLocalPlayer()) {  //if the local player has been set, and the active player isn't the player who owns this board, we want to wait for a move
				board.waitForMove();
				return;
			}

			int xPos, yPos;

			if (!(e.getX() > xStart && e.getX() < 600 && e.getY() > yStart && e.getY() < 315)) { //user clicked outside of the game area
				return;
			}

			xPos = ((e.getX()-xStart)/((tileWidth+xPadding)/5)+1); //converting from the pixel values to the tile position
			yPos = ((e.getY()-yStart)/((tileHeight+yPadding)/5)+1);

			if (xPos % 5 == 0 || yPos % 5 == 0) { //the user clicked on the padding inbetween tiles
				return;
			}

			xPos /= 5; //the user didn't click on padding, so divide to get the final x and y position
			yPos /= 5;

			int tileNumber = convertToTileNumber(xPos, yPos); //convert the position to a tile number

			if(board.getPotTiles()[tileNumber].getOwner() == board.getActivePlayer()) { //check for the correct owner
				board.move(board.getPotTiles()[tileNumber]); //and move
			}
			drawSelected(xPos, yPos); //draw that the tile was selected
			update(getGraphics()); //and update the board
		}
	}
	/**
	 * Mouse listener that allows the user to hover over a tile, and then be shown the number of pieces within that tile
	 */
	private class MouseHoverListener extends MouseInputAdapter {
		@Override
		public void mouseMoved(MouseEvent e) { //every time the mouse moves

			int xPos, yPos, newValue;

			if (e.getX() > xStart && e.getX() < 600 && e.getY() > yStart && e.getY() < 315) { //inside of the game area and within the pot tile area

				xPos = ((e.getX()-xStart)/((tileWidth+xPadding)/5)+1); //caculate which tile from pixel values
				yPos = ((e.getY()-yStart)/((tileHeight+yPadding)/5)+1);

				if (xPos % 5 == 0 || yPos % 5 == 0) { //tile padding
					return;
				}

				xPos /= 5;
				yPos /= 5;

				int tileNumber = convertToTileNumber(xPos, yPos); //convert from the positional values to the tile number

				newValue = board.getPotTiles()[tileNumber].getPieces().size(); //get the number of pieces
			}
			else if (e.getX() > 55 && e.getX() < 135 && e.getY() > 130 && e.getY() < 270) { //area for the first mancala
				newValue = board.getMancalaTiles()[0].getPieces().size();
			}
			else if (e.getX() > 605 && e.getX() < 685 && e.getY() > 130 && e.getY() < 270) { //area for the second mancala
				newValue = board.getMancalaTiles()[1].getPieces().size();
			}
			else {
				return; //outside the game area
			}				

			if (newValue != currentHoverValue) { //if the value has changed
				setIgnoreRepaint(true);
				currentHoverValue = newValue;
				Graphics g = getGraphics();
				update(g); //then we repaint
				g.drawImage(hoverBox, 10, background.getHeight() - 35, null); //with the new value
				g.setFont(new Font("Arial", Font.PLAIN, 15));
				g.drawString("Pieces: "+newValue, 20, background.getHeight() - 15);
			}
		}
	}
}