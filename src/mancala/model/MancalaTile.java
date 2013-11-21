/**
 * MancalaTile Class
 * Class representing a mancala tile on the mancala board.
 * 
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version 2.0.0.1 16 Apr 2012
 */
package mancala.model;

import java.util.ArrayList;

import core.model.Player;
import core.model.Tile;

public class MancalaTile extends Tile {
	
	//variable assignment here
	private ArrayList<MancalaPiece> pieces; //the pieces of the tile
	private Player player; //the owner of the tile
	private static final long serialVersionUID = 1L;
	
	//constructors here
	/**
	 * Default constructor for the mancala tile, sets up the arrayList of pieces, and sets the player to the player provided.
	 * 
	 * @param player The player who owns the tile
	 */
	protected MancalaTile(Player player)
	{
		//variables passed in
		this.player = player;

		//variables not passed in
		pieces = new ArrayList<MancalaPiece>();
	}

	//gets here
	public ArrayList<MancalaPiece> getPieces()
	{
		return pieces;
	}
	public Player getPlayer()
	{
		return player;
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
		pieces.removeAll(pieces);
	}
}
