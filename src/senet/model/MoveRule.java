/**
 *  MoveRule
 *  Contains the logic for the Senet move
 * 
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 10 Mar 2012
 */

package senet.model;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class MoveRule implements Rule { 

	//variable assignment here
	private String errorMessage = "move invalid";

	//constructors here
	public MoveRule()
	{
		//variables passed in

		//variables not passed in
	}

	//gets here

	//sets here

	//methods here
	/**
	 * Checks if the move is a valid move for the 'snaking' board.
	 * 
	 * @param board The board the move belongs to
	 * @param command The move command that is being checked
	 * @return true if the move is valid for this rule, false if it isn't
	 */
	@Override
	public boolean checkValid(Board board, Command command) 
	{
		boolean ret = false;
		SenetBoard senetBoard = (SenetBoard) board;
		SenetCommand senetCommand = (SenetCommand) command;
		int modDestTileNumber = senetCommand.getDestinationTile().getTileNumber();
		int modPrevTileNumber = senetCommand.getPreviousTile().getTileNumber();
		if(senetCommand.getDestinationTile().getTileNumber() > 9 && senetCommand.getDestinationTile().getTileNumber() < 20) //changes the second line of the board around so it 'snakes'
		{
			modDestTileNumber = (9-((senetCommand.getDestinationTile().getTileNumber()-10) * 2))+senetCommand.getDestinationTile().getTileNumber();
		}
		
		if(senetCommand.getPreviousTile().getTileNumber() > 9 && senetCommand.getPreviousTile().getTileNumber() < 20) //changes the second line of the board around so it 'snakes'
		{
			modPrevTileNumber = (9-((senetCommand.getPreviousTile().getTileNumber()-10) * 2))+senetCommand.getPreviousTile().getTileNumber();
		}
		
		if (modDestTileNumber == modPrevTileNumber+senetBoard.getSticksThrowValue()) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Returns the error message for this rule.
	 * 
	 * @return the error message for this rule as a String
	 */
	@Override
	public String getInvalidMsg() 
	{
		return errorMessage;
	}
}