/**
 *	SenetTile
 *	Class representing a Senet Tile
 *
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.model;

import core.model.Tile;

public class SenetTile extends Tile {
	//variable assignment here
	private SenetPiece piece;
	private int tileNumber;
	private static final long serialVersionUID = 3L;
	//constructors here
	/**
	 * Default constructor for SenetTile.
	 * 
	 * @param tileNumber The position of this tile on the board
	 */
	public SenetTile(int tileNumber)
	{
		//variables passed in
		this.tileNumber = tileNumber;

		//variables not passed in
	}

	//gets here
	/**
	 * Gets the piece held by this tile.
	 * 
	 * @return the piece held by this tile
	 */
	public SenetPiece getPiece() 
	{
		return piece;
	}
	
	/**
	 * Gets the tile number of this tile.
	 * 
	 * @return the number of this tile representing the position in the board
	 */
	public int getTileNumber()
	{
		return tileNumber;
	}

	//sets here
	/**
	 * Sets a piece to be held by this tile.
	 * 
	 * @param piece The piece to be held
	 */
	public void setPiece(SenetPiece piece)
	{
		this.piece = piece;
	}
	//methods here
}
