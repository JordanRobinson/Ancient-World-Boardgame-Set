/**
 *	SenetBoardView
 *	Graphical representation of the SenetBoard model
 *
 * @package senet.view
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import senet.model.SenetBoard;
import senet.model.SenetTile;

import core.model.Resources;
import core.view.AbstractView;

@SuppressWarnings("serial") //This class should never be saved, as it is a GUI component or similar
public class SenetBoardView extends AbstractView implements Observer{
	//variable assignment here
	private int xStart = 84;
	private int yStart = 68;
	private int tileHeight = 45;
	private int tileWidth = 45;
	private int padding = 15;
	private SenetBoard board;
	private BufferedImage pieceImage;
	private SenetTile selectedTile;
	private BufferedImage selectedImage;
	private BufferedImage viableMoveImage;
	
	private int[] lastSelectedTile;

	//constructors here
	/**
	 * Default constructor for SenetBoardView.
	 * 
	 * @param board The board this view is representing
	 */
	public SenetBoardView(SenetBoard board)
	{
		//variables passed in
		this.board = board;
		board.addObserver(this);
		//variables not passed in
		background = (BufferedImage) Resources.load("src/resources/senetBoard.png", "image");
		selectedImage = (BufferedImage) Resources.load("src/resources/senetSelected.png", "image");
		viableMoveImage = (BufferedImage) Resources.load("src/resources/senetViable.png", "image");
		addMouseListener(new MouseGridListener());
		
		lastSelectedTile = new int[2];
		lastSelectedTile[0] = -1;
		lastSelectedTile[1] = -1;
	}

	//gets here
	/**
	 * Gets the board this view is representing.
	 * 
	 * @return the board being represented
	 */
	public SenetBoard getBoard()
	{
		return board;
	}
	//sets here

	//methods here
	/**
	 * Overrides update in AbstractView. Updates this graphical component.
	 * 
	 * @param arg0 The object that is being observed
	 * @param arg1 The message passed from the observed object
	 */
	public void update(Observable arg0, Object arg1) 
	{
		super.update(arg0, arg1); //call the superclass method with the parameters, this should filter out anything relevant to all the games, such as the message box updating
		
		if(arg1 != null && arg1.equals("turnChange")) { //if a turn change happens, this message should come in
			drawTurnChange(board.getActivePlayer().getName()); //so we draw it accordingly
		}
		
		paint(getGraphics()); //then redraw this board
	}
	
	/**
	 * Overrides the paint method of JComponent, to draw the necessities needed for the Senet Board.
	 * 
	 * @param g The graphics object used to draw the images required
	 */
	public void paint(Graphics g)
	{
		g.drawImage(background, 0, 0, null);

		//draw pieces start
		int wid = xStart;
		int hig = yStart;

		int y = 0;
		int x = 0;

		for (x = 0; x < 3; x++) {
			for (y = 0; y < 10; y++) {
			if(board.getTiles()[(x*10)+y].getPiece() != null) {
				switch(board.getPlayers().indexOf(board.getTiles()[(x*10)+y].getPiece().getOwner())){
				case 0: pieceImage = (BufferedImage) Resources.load("src/resources/senetPieceOne.png", "image");break;
				case 1: pieceImage = (BufferedImage) Resources.load("src/resources/senetPieceTwo.png", "image");break;
				default: break;
				}
				g.drawImage(pieceImage,wid,hig,null);
			}
			wid += tileWidth + padding;
		}
		hig += tileHeight + padding;
		wid = xStart;
		}
		//draw pieces end
		
		displayMessage(getGraphics());
	}
	
	/**
	 * Highlights the selected tile given by the xPos and yPos.
	 * 
	 * @param xPos x position of the click
	 * @param yPos y position of the click
	 */
	private void drawSelected(int xPos, int yPos)
	{
		if (xPos < 0 || yPos < 0) {
			return;
		}

		lastSelectedTile[0] = xPos;
		lastSelectedTile[1] = yPos;

		int wid = xStart + (xPos * tileWidth + (xPos*padding))-3;
		int hig = yStart + (yPos * tileHeight + (yPos*padding))-3;

		getGraphics().drawImage(selectedImage, wid, hig, null);
	}
	
	/**
	 * Highlights the tile where the selected piece can move to based on the throw value.
	 * 
	 * @param selectedTile the tile selected to move
	 * @param xPos x position of the click
	 * @param yPos y position of the click
	 */
	private void highlightMove(SenetTile selectedTile, int xPos, int yPos)
	{
		int tile = board.findMove(selectedTile);
		int tileY = tile/10;
		int tileX = tile%10;
		int x = xStart + (tileX * tileWidth + (tileX*padding))-3;
		int y = yStart + (tileY * tileHeight + (tileY*padding))-3;
		getGraphics().drawImage(viableMoveImage, x, y, null);
	}
	
	
	/**
	 * Finds all available hints for the players pieces and highlights all the suggested destination tiles.
	 */
	public void drawHints()
	{
		ArrayList<SenetTile> hints = board.findHint();
		if(!hints.isEmpty())
		{
			for(int i = 0; i < hints.size(); i++)
			{
				highlightMove(hints.get(i),1,1);
			}
		}
	}

	/**
	 * Mouse Listener for the view to allow the user to interact with the view.
	 */
	private class MouseGridListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			

			if (board.getPlayers().get(0).isAI() && board.getPlayers().get(1).isAI()) {
				board.nextPlayer();
			}
			
			if (board.getLocalPlayer() != null && board.getActivePlayer() != board.getLocalPlayer()) { //if the local player has been set, and the active player isn't the player who owns this board, we want to wait for a move
				board.waitForMove();
				return;
			}
				
			if(board.getActivePlayer() != null && board.getActivePlayer().isAI() || board.getThrowTime()) {
				return;
			}				
			
			int xPos, yPos;

			if(!(e.getX()>xStart && e.getX()<676 && e.getY()>yStart && e.getY()<240)){
				return;
			}

			xPos = ((e.getX()-xStart)/((tileWidth+padding)/4)+1);
			yPos = ((e.getY()-yStart)/((tileHeight+padding)/4)+1);

			if (xPos % 4 == 0 || yPos % 4 == 0) {
				System.out.println("padding");
				return;
			}

			xPos /= 4;
			yPos /= 4;
			
			int tileNumber = (xPos)+(yPos*10);

			if (selectedTile == null) {
				selectedTile = board.getTiles()[tileNumber];
				drawSelected(xPos, yPos);
				highlightMove(selectedTile, xPos, yPos);
				System.out.println(tileNumber + " selected");
			}
			else {
				board.move(selectedTile, board.getTiles()[tileNumber]);
				System.out.println("place");
				selectedTile = null;
			}					
		}
	}
}