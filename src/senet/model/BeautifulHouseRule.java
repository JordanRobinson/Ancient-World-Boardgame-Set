/**
 *	BeautifulHouseRule
 *	Contains the logic to determine whether a given move is valid when trying to go beyond the beautiful house rule
 *
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.model;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class BeautifulHouseRule implements Rule {


	//variable assignment here
	private String errorMessage = "Beautiful House tile needs exact throw";
	//constructors here

	//variables passed in

	//variables not passed in

	//gets here

	//sets here

	//methods here
	/**
	 * Checks to see if the given move is valid
	 * 
	 * @param board The board for this move
	 * @param command The command for this move
	 * @return true if the move is valid for this rule, false if it isn't
	 */
	@Override
	public boolean checkValid(Board board, Command command) {
		if(((SenetCommand) command).getDestinationTile().getTileNumber() < 25)
		{
			return true; // if tile destination is less than Beautiful house tile move is valid
		}
		if(((SenetCommand) command).getPreviousTile().getTileNumber() < 25 && ((SenetBoard)board).getSticksThrowValue()+
				((SenetCommand) command).getPreviousTile().getTileNumber() > 25)
		{
			return false; // doesn't let a throw move further than the beautiful house tile
		}
		
		return true; // the piece is either landing on the beautiful house tile with a correct throw or is already past the 
					 //tile and so the rule is irrelevant
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
