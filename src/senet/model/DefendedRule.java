/**
 *	DefendedRule
 *	Contains the logic to check if a tile is defended in Senet for a given move
 *
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.model;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class DefendedRule implements Rule {


	//variable assignment here
	private String errorMessage = "Tile is defended.";
	//constructors here

	//variables passed in

	//variables not passed in

	//gets here

	//sets here

	//methods here
	/**
	 * Checks if the tile is defended or not.
	 * 
	 * @param board The board the move belongs to
	 * @param command The move command that is being checked
	 * @return returns true if the tile isn't defended and false if the tile is defended
	 */
	@Override
	public boolean checkValid(Board board, Command command) 
	{
		boolean ret = true;
		SenetCommand senetCommand = (SenetCommand)command;
		SenetBoard senetBoard = (SenetBoard) board;
		if(senetCommand.getDestinationTile().getPiece() != null)
		{
		if(senetCommand.getDestinationTile().getPiece().getOwner() != senetBoard.getActivePlayer())
		{
			SenetTile[] tiles = senetBoard.getTiles();
			if(tiles[senetCommand.getDestinationTile().getTileNumber()+1].getPiece() != null) // if the adjacent tile is empty then getOwner() will throw a null pointer exception
			{
				if(tiles[senetCommand.getDestinationTile().getTileNumber()+1].getPiece().getOwner() != senetBoard.getActivePlayer())
					return false; // there is a piece next to this piece that the player owns so it is defended
			}
			if(tiles[senetCommand.getDestinationTile().getTileNumber()-1].getPiece() != null) // if the adjacent tile is empty then getOwner() will throw a null pointer exception
			{
				if(tiles[senetCommand.getDestinationTile().getTileNumber()-1].getPiece().getOwner() != senetBoard.getActivePlayer())
					return false; // there is a piece next to this piece that the player owns so it is defended
			}
		}
		}
		return ret;
	}

	/**
	 * Returns the error message for this rule.
	 * 
	 * @return the error message for this rule as a String
	 */
	@Override
	public String getInvalidMsg() {
		return errorMessage;
	}
}
