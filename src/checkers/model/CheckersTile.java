/**
 *  CheckersTile Class
 *  Simple class containing the differences from a generic tile that chinese checkers needs to be able to use it as a class.
 * 
 * @package checkers.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.3.5 15 Mar 2012
 */

package checkers.model;

import core.model.Tile;

public class CheckersTile extends Tile {
	
	//variable assignment here
	private CheckersPiece piece; //the piece contained in the tile, null representing no piece
	private boolean accessible; //since the board is made up of a grid, there should be some tiles that are inaccessible, to follow with the overall shape of the Chinese checkers board
	private int x, y; //the x and y coordinates of the tile in the 2D array, here since this makes a few things elsewhere easier and more efficient
	private static final long serialVersionUID = 2L;
	
	//constructors here
	protected CheckersTile(boolean accessible, int x, int y)
	{
		//variables passed in
		this.accessible = accessible;
		this.x = x;
		this.y = y;
		
		//variables not passed in
	}

	//gets here
	public boolean getAccessible()
	{
		return accessible;
	}
	public CheckersPiece getPiece()
	{
		return piece; //should be null if no piece
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	/**
	 * Simple getter to return the coordinates of the tile as a string. This saves always having to get the x and y and then concatenate them as a string elsewhere.
	 * 
	 * @returns A string denoting the x and y coordinates of this tile, separated by a comma
	 */
	public String getCoords() 
	{
		return x + "," + y;
	}
	
	//sets here
	protected void setPiece(CheckersPiece piece)
	{
		this.piece = piece;
	}

	//methods here
}
