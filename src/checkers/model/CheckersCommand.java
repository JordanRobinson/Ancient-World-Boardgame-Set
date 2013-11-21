/**
 *  CheckersCommand Class
 *  Class representing an action taken in chinese checkers
 * 
 * @package checkers.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.2.4 8 Mar 2012
 */

package checkers.model;

import core.model.Command;

public class CheckersCommand implements Command {
	
	//variable assignment here
	private CheckersBoard board; //the board on which the actions have been performed
	private CheckersTile previousTile; //the tile from which the piece is to be moved from
	private CheckersTile destinationTile; //the tile from which the piece is to be moved to
	private CheckersPiece piece; //the piece to move
	
	//constructors here
	/**
	 * Default constructor for the Checkers Command class, sets the three fields required to function correctly as a checkers command
	 * 
	 * @param board The board on which the command is to be performed
	 * @param previousTile The source tile of the piece to move
	 * @param destinationTile The destination tile of the piece to move
	 */
	protected CheckersCommand(CheckersBoard board, CheckersTile previousTile, CheckersTile destinationTile) 
	{
		//variables passed in
		this.board = board; //set the fields to the parameters given
		this.previousTile = previousTile;
		this.destinationTile = destinationTile;
		
		//variables not passed in
	}
		
	//gets here
	public CheckersBoard getBoard()
	{
		return board;
	}
	public CheckersTile getPreviousTile()
	{
		return previousTile;
	}	
	public CheckersTile getDestinationTile()
	{
		return destinationTile;
	}
	
	//sets here
	//methods here
	/**
	 * Carry out the command using the fields of this object
	 */
	@Override
	public void execute() {
		piece = previousTile.getPiece(); //get the piece from the previous tile
		previousTile.setPiece(null); //set the old tile to not have a piece
		destinationTile.setPiece(piece); //set the new tile to have the piece
	}

	/**
	 * Undo the command using the fields of this object
	 */
	@Override
	public void undo() {
		piece = destinationTile.getPiece(); //get the piece
		destinationTile.setPiece(null); //remove the piece from the destination tile
		previousTile.setPiece(piece); //and put it in the source tile
	}
}
