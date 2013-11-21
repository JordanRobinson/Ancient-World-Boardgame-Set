/**
 *  CheckersPiece Class
 *  Simple class containing the differences from a generic piece that chinese checkers reqires it its pieces
 * 
 * @package checkers.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.2.8 22 Mar 2012
 */

package checkers.model;

import core.model.Piece;
import core.model.Player;

public class CheckersPiece extends Piece {
	
	//variable assignment here
	private Player owner;
	private static final long serialVersionUID = 1L;
	//constructors here
	protected CheckersPiece(Player owner)
	{		
		//variables passed in
		this.owner = owner;
		//variables not passed in
	}


	//gets here
	public Player getOwner()
	{
		return owner;
	}

	//sets here

	//methods here
}
