/**
 *	LandOnMancalaRule Class
 *	Class containing the logic to check if a player has landed on a mancala with their move
 *
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version 2.0.0.1 16 Feb 2012
 */
package mancala.model;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class LandOnMancalaRule implements Rule {

	//variable assignment here
	private String errorMessage; //the message to return if the rule is invalid. In this case however this is not required, and only included to conform to the interface for rules
	
	//constructors here
	protected LandOnMancalaRule()
	{
		//variables passed in
		//variables not passed in
		errorMessage = null; //set to null since we don't need it in this rule
	}

	//gets here

	//sets here

	//methods here
	/**
	 * Method for which any moves made in mancala should be checked against. If the rule is proven valid, the user should be given an extra turn, through means detailed elsewhere
	 *
	 * @param board The board on which to check that the move is valid
	 * @param command The specific command containing the details of the move
	 * @returns A boolean value denoting if the move to check is valid or not
	 */
	@Override
	public boolean checkValid(Board board, Command command) 
	{
		MancalaCommand mancalaCommand = (MancalaCommand) command; //set the command to a mancala command so that we can use mancala specific methods
		
		int start = mancalaCommand.getSelectedTile().getTileNumber(); //get the required variables to calculate if the player landed on a mancala
		int distance = mancalaCommand.getSelectedTile().getPieces().size();
		int destination = (start - distance);
		
		if (destination < -1) { //if the destination is under -1, then we need to increment it to reflect the circular nature of the mancala board
			destination += 11;
		}
		
		if (destination == -1 || destination == 5) { //if either of these are true, the player has indeed landed on a mancala
			return true; //so we return as such
		}
		return false;
	}

	@Override
	public String getInvalidMsg() {
		return errorMessage;
	}
}
