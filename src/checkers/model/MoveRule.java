/**
 *  MoveRule Class
 *  Class containing the logic for the move rule of chinese checkers.
 * 
 * @package checkers.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.2.4 27 Feb 2012
 */

package checkers.model;

import java.util.ArrayList;
import java.io.Serializable;

import core.model.Board;
import core.model.Command;
import core.model.Rule;

public class MoveRule implements Rule, Serializable {

	//variable assignment here
	private String errorMessage = "invalid move";
	private static final long serialVersionUID = 3L;

	//constructors here
		//variables passed in
		//variables not passed in

	//gets here

	//sets here

	//methods here
	
	/**
	 * Method for which any moves made in chinese checkers should be checked against.
	 *
	 * @param board The board on which to check that the move is valid
	 * @param command The specific command containing the details of the move
	 * @returns A boolean value denoting if the move to check is valid or not
	 */
	@Override
	public boolean checkValid(Board board, Command command)
	{
		boolean ret = false; //initially set the return value to false, since it hasn't yet been proven valid
		CheckersBoard checkersBoard = (CheckersBoard) board; //casting the board and command, so that the checkers specific gets and methods can be used
		CheckersCommand checkersCommand = (CheckersCommand) command;

		ArrayList<CheckersTile[]> viableMoves = checkersBoard.findMoves(checkersCommand.getPreviousTile()); //this uses the same method as the checkers AI to retrieve a list of all valid moves from the seed tile
		
		for (int i = 0; i < viableMoves.size(); i++) { //from here we simply need to check if the move is located in the returned list
			if (viableMoves.get(i)[1] == checkersCommand.getDestinationTile()) { //note we only need to check the destination tile, since the source tile has already been used to get the list of viable moves
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Getter for the errorMessage field
	 *
	 * @returns The error message field as a String
	 */
	@Override
	public String getInvalidMsg() {
		return errorMessage;
	}
}
