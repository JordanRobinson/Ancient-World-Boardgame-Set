/**
 *	SenetPiece
 *	A class representing a Senet piece. Contains information on who owns this piece.
 *
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.model;

import core.model.Piece;
import core.model.Player;

public class SenetPiece extends Piece {
	//variable assignment here
	private Player owner;
	private static final long serialVersionUID = 3L;
	//constructors here
	/**
	 * Default constructor for SenetPiece.
	 * 
	 * @param owner The owner of this piece
	 */
	public SenetPiece(Player owner)
	{
		//variables passed in
		this.owner = owner;

		//variables not passed in

	}

	//gets here
	/**
	 * Gets the owner of this piece;
	 * 
	 * @return the owner of this piece
	 */
	public Player getOwner()
	{
		return owner;
	}

	//sets here

	//methods here
}
