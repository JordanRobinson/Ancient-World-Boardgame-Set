/**
 * PotTile Class
 * Class representing a pot tile on the mancala board
 *
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version 2.0.1.5 16 Feb 2012
 */
package mancala.model;

import java.util.ArrayList;

import core.model.Player;
import core.model.Tile;

public class PotTile extends Tile {
	
	//variable assignment here
	private int tileNumber; //the number of this tile
	private Player owner; //the owner of this tile
	private ArrayList<MancalaPiece> pieces; //the pieces of this tile
	private static final long serialVersionUID = 2L;
	
	//constructors here
	/**
	 * Default constructor for a mancala pot tile. Sets the fields to the parameters given, and initialises the pieces of this tile.
	 * 
	 * @param tileNumber The tile number of this tile
	 * @param owner The owner of this tile
	 */
	protected PotTile(int tileNumber, Player owner)
	{
		//variables passed in
		this.tileNumber = tileNumber;
		this.owner = owner;

		//variables not passed in
		pieces = new ArrayList<MancalaPiece>();
	}

	//gets here
	public ArrayList<MancalaPiece> getPieces() 
	{
		return pieces;
	}
	public int getTileNumber()
	{
		return tileNumber;
	}
	public Player getOwner()
	{
		return owner;
	}
	
	//sets here

	//methods here
	/**
	 * Adds the piece provided to this tile.
	 * 
	 * @param piece The piece to add to this tile
	 */
	protected void addPiece(MancalaPiece piece)
	{
		pieces.add(piece);
	}
	/**
	 * Removes the piece last added to this tile from this tile.
	 */
	protected void removePiece()
	{
		pieces.remove(pieces.size()-1);
	}
	/**
	 * Removes all pieces from this tile.
	 */
	protected void removeAllPieces()
	{
		pieces.clear();
	}
}
