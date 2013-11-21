/**
 *  MancalaCommand Class
 *  Class representing an action taken in mancala
 * 
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version v3.0.1.2 9 Mar 2012
 */

package mancala.model;

import java.util.ArrayList;

import core.model.Command;

public class MancalaCommand implements Command {

	//variable assignment here
	private MancalaBoard board; //the board that the action is being performed on
	private PotTile selectedTile; //the tile that has been selected by the user
	private String signature; //the signature of the move, this is so the undo can work

	//constructors here
	/**
	 * Default constructor for a mancala command, simply sets the fields to the parameters passed in
	 */
	protected MancalaCommand(MancalaBoard board, PotTile selectedTile) 
	{
		//variables passed in
		this.board = board;
		this.selectedTile = selectedTile;

		//variables not passed in
	}

	//gets here
	public MancalaBoard getBoard()
	{
		return board;
	}
	public PotTile getSelectedTile()
	{
		return selectedTile;
	}
	/**
	 * Returns the tile that the player should land on if this command were to be executed
	 * 
	 * @return The destination tile that the player should land on if the command is executed
	 */
	protected PotTile getDestinationTile()
	{
		int destination = (selectedTile.getTileNumber() - selectedTile.getPieces().size()); //calculate the destination tile

		if (destination < 0) { //then check to see if the destination is less than 0, since that would mean that we've cycled around the board
			destination += 11; //and if so add on 11, to simulate cycling round the board
		}
		return board.getPotTiles()[destination]; //then return the tile found from the calculation
	}
	
	//methods here
	/**
	 * Writes a representation of the current board to a String, so that this move can be undone later.
	 * 
	 * @return The signature of the board in its current state
	 */
	private String writeSignature() 
	{
		String ret = null;
		
		ret = ""+board.getMancalaTiles()[0].getPieces().size() + board.getMancalaTiles()[1].getPieces().size(); //write the situation of the mancala tiles to the start of the String
		
		for (int i = 0; i < board.getPotTiles().length; i++) { //then write the status of the pot tiles to the String
			ret += board.getPotTiles()[i].getPieces().size();
		}		
		return ret; //and finally return the String
	}

	/**
	 * Carry out the command using the fields of this object
	 */
	@Override
	public void execute() {
		signature = writeSignature(); //write the signature so we can undo at a later date
		
		ArrayList<MancalaPiece> pieces = selectedTile.getPieces(); //getting the pieces from the selected tile
		int position = -1; //the position of the tile on the board, initially set to an arbitrary value

		for (int i = 0; i < board.getPotTiles().length; i++) { //getting the tile number of the selected tile
			if (selectedTile.equals(board.getPotTiles()[i])) {
				position = i;
			}
		}

		while(pieces.size() > 0) { //while the placeholder array of pieces is not empty
			position--; //move along the board
			if (position == -1) { //this means we found a mancala
				board.getMancalaTiles()[0].addPiece(pieces.get(pieces.size()-1)); //so add a piece to the mancala
				pieces.remove(pieces.size()-1); //and remove it from the placeholder array
				position = 12; //set the position since we've cycled round the board
				continue; //then continue round the board
			}
			else if (position == 5) { //this means we found the other mancala
				board.getMancalaTiles()[1].addPiece(pieces.get(pieces.size()-1)); //so we add a piece to it
				pieces.remove(pieces.size()-1); //remove from the placeholder array
				if (pieces.size() == 0) { //then if there aren't any more pieces in the placeholder array, we continue, so as to not do the below since we have no pieces for it
					continue;
				}
			}
			board.getPotTiles()[position].addPiece(pieces.get(pieces.size()-1)); //if we get here, this means that we've hit a pot tile, so add a piece to it
			pieces.remove(pieces.size()-1); //and then remove that piece from the placeholder array
		}
	}

	/**
	 * Undo the command using the signature of the command.
	 * 
	 * Note that this undo method differs from the other undo methods in commands, since due to the way that mancalas rules and board work, this is by far an easier and more efficient way of undoing the command.
	 */
	@Override
	public void undo() {		
		char[] values = signature.toCharArray(); //convert the String signature into an array of chars, so we can iterate through it
		
		if (board.getMancalaTiles()[0].getPieces().size() != Character.getNumericValue(values[0])) { //if the current state of the tile is different from the state reflected in the signature
			board.getMancalaTiles()[0].removeAllPieces(); //then remove what's in there
			for (int i = 0; i < Character.getNumericValue(values[0]); i++) { //and replace it with a new number of pieces to reflect the value shown in the signature
				board.getMancalaTiles()[0].addPiece(new MancalaPiece()); 
			}
		}
		
		if (board.getMancalaTiles()[1].getPieces().size() != Character.getNumericValue(values[1])) { //same as above, but for the second mancala tile
			board.getMancalaTiles()[1].removeAllPieces();
			for (int j = 0; j < Character.getNumericValue(values[1]); j++) {
				board.getMancalaTiles()[1].addPiece(new MancalaPiece());
			}
		}
		
		for (int k = 2; k < values.length; k++) { //then do the same for the pot tiles
			if (board.getPotTiles()[k-2].getPieces().size() == Character.getNumericValue(values[k])) { //doing nothing here since the value in the signature is the same as the value in the tile
				continue;
			}
			board.getPotTiles()[k-2].removeAllPieces();
			for (int l = 0; l < Character.getNumericValue(values[k]); l++) {
				board.getPotTiles()[k-2].addPiece(new MancalaPiece());
			}
		}
	}
}