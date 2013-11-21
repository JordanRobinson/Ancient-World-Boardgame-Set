/**
 *	LandOnZeroRule Class
 *  Class containing the logic to check if a player has landed on a tile with no pieces in, the benefits of this are to be enacted elsewhere
 *
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version 3.0.3.3 20 Apr 2012
 */
package mancala.model;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class LandOnZeroRule implements Rule {

	//variable assignment here
	private String errorMessage; //the message to return if the rule is invalid. In this case however this is not required, and only included to conform to the interface for rules

	//constructors here
	protected LandOnZeroRule()
	{
		//variables passed in
		//variables not passed in
		errorMessage = null; //set to null since we don't need it in this rule
	}

	//gets here

	//sets here

	//methods here
	/**
	 * Method for which any moves made in mancala should be checked against. If the rule is proven valid, the player should get the pieces from the opposite tile added to their mancala, although this is to be enacted elsewhere
	 * 
	 * @param board The board on which to check that the move is valid
	 * @param command The specific command containing the details of the move
	 * @returns A boolean value denoting if the move to check is valid or not
	 */
	@Override
	public boolean checkValid(Board board, Command command) 
	{
		MancalaCommand mancalaCommand = (MancalaCommand) command; //casting the command to a mancala command so we can use the mancala specific methods
		
		int start = mancalaCommand.getSelectedTile().getTileNumber(); //setting the variables needed to calculate if the rule is valid or not
		int distance = mancalaCommand.getSelectedTile().getPieces().size();
		int destination = (start - distance);
		
		while (destination < 0) { //if the destination is under 0, then we need to increment it to reflect the circular nature of the mancala board
			destination += 11;
		}
		
		if(board.getPlayers().indexOf(board.getActivePlayer()) == 1 && destination > 5) { //we need to check that the zero tile is on the player's side, as detailed in the rule
			if(((MancalaBoard) board).getPotTiles()[destination].getPieces().size() == 0) { //then if the destination tile has no pieces in it
				return true; //then the rule can be proved true, so we need to return as suck
			}
		}

		else if(board.getPlayers().indexOf(board.getActivePlayer()) == 0 && destination < 6) { //this does the same as above, but checking for the other player
			if(((MancalaBoard) board).getPotTiles()[destination].getPieces().size() == 0){
				return true;
			}
		}
		return false; //if we get here, neither of the above checks are true, so we return false
	}

	public String getInvalidMsg() {
		return errorMessage;
	}
}
