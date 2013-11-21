/**
 *  SenetCommand
 *  Contains all the information for a Senet move and how to execute and undo that move.
 * 
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 9 Mar 2012
 */

package senet.model;

import core.model.Command;

public class SenetCommand implements Command{
	
	//variable assignment here
	private SenetBoard board;
	private SenetTile previousTile;
	private SenetTile destinationTile;
	private SenetPiece piece;
	
	//constructors here
	/**
	 * default constructor for SenetCommand
	 * 
	 *  @param board The board for this command
	 *  @param previousTile The tile the move originates from
	 *  @param destinationTile The tile the move ends at
	 */
	public SenetCommand(SenetBoard board, SenetTile previousTile, SenetTile destinationTile)
	{
		//variables passed in
		this.board = board;
		this.previousTile = previousTile;
		this.destinationTile = destinationTile;
		//variables not passed in
	}

	//gets here
	/**
	 * Gets the board for this command
	 * 
	 * @return the board for this command
	 */
	public SenetBoard getBoard()
	{
		return board;
	}
	
	/**
	 * Gets the previous tile for this command
	 * 
	 * @return the previous tile for this command
	 */
	public SenetTile getPreviousTile()
	{
		return previousTile;
	}
	
	/**
	 * Gets the destination tile for this command
	 * 
	 * @return the destination tile for this command
	 */
	public SenetTile getDestinationTile()
	{
		return destinationTile;
	}

	//sets here

	//methods here
	/**
	 * Executes the move for Senet when called
	 */
	@Override
	public void execute() {
		if(destinationTile.getPiece() != null && destinationTile.getPiece().getOwner() != board.getActivePlayer())
		{
			piece = previousTile.getPiece();
			previousTile.setPiece(destinationTile.getPiece());
			destinationTile.setPiece(piece);
		}
		else
		{
			piece = previousTile.getPiece();
			previousTile.setPiece(null);
			destinationTile.setPiece(piece);
		}
	}

	/**
	 * This method undoes what what done in the execute method for this command
	 */
	@Override
	public void undo() {
		SenetPiece otherPiece = previousTile.getPiece();
		destinationTile.setPiece(otherPiece);
		previousTile.setPiece(piece);
		board.nextPlayer();
	}
}