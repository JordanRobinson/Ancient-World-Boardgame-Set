/**
 * AI Class
 * Contains the logic for an artificial intelligence that can play chinese checkers, mancala and senet
 *  
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 15 Feb 2012
 */

package core.model;

import java.util.ArrayList;
import java.util.Random;

import senet.model.*;
import checkers.model.*;
import mancala.model.*;

public class AI extends Player {

	//variable assignment here
	private ArrayList<CheckersTile> finishedTiles; //for use in checkers, so that the AI has an idea of which tiles it should still send pieces towards, and which tiles it has filled
	private int difficulty; //int representing the difficulty level of the AI
	private Random rand; //for decisions requiring some randomness
	private static final long serialVersionUID = 2L;

	//constructors here
	/**
	 * Default constructor for the AI class. Sets up the required variables and sets the name field to the supplied parameter.
	 * 
	 * @param name The name of the AI
	 */
	public AI(String name) 
	{
		//variables passed in
		super(name); //sets the name, using the superclass constructor
		//variables not passed in
		isAI = true; //this is an AI, so we set it as such
		finishedTiles = new ArrayList<CheckersTile>(); 
		rand = new Random();
	}

	//gets here

	//sets here
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}

	//methods here
	/**
	 * Finds a move on the given board, by calculating a good move to take on the board based on the pieces and the opponent's pieces.
	 * 
	 * @param board The board on which to find a move
	 */
	public void checkersMove(CheckersBoard board)
	{
		CheckersTile goalTile = null;
		CheckersTile[] goalTiles = new CheckersTile[15]; //the tiles to use as a goal
		CheckersTile previousTile = null, destinationTile = null;
		ArrayList<CheckersTile[]> possibleMoves = new ArrayList<CheckersTile[]>();
		int bestMove = 0; //this is the position in the possibleMoves array that is considered the optimal move to take

		//finding the goal tiles
		switch (board.players.indexOf(this)) {
		case 0:
			CheckersTile[] oneTiles = {board.getTiles()[6][16], board.getTiles()[5][15],
					board.getTiles()[6][15], board.getTiles()[5][14], board.getTiles()[6][14],
					board.getTiles()[7][14], board.getTiles()[4][13], board.getTiles()[5][13],
					board.getTiles()[6][13], board.getTiles()[7][13], board.getTiles()[7][12],
					board.getTiles()[6][12], board.getTiles()[5][12], board.getTiles()[4][12]};
			goalTiles = oneTiles; break;
		case 1:
			CheckersTile[] twoTiles = {board.getTiles()[6][0], board.getTiles()[5][1],
					board.getTiles()[6][1], board.getTiles()[5][2], board.getTiles()[6][2],
					board.getTiles()[7][2], board.getTiles()[4][3], board.getTiles()[5][3],
					board.getTiles()[6][3], board.getTiles()[7][3], board.getTiles()[4][4],
					board.getTiles()[5][4], board.getTiles()[6][4], board.getTiles()[7][4]};
			goalTiles = twoTiles; break;
		case 2:
			CheckersTile[] threeTiles = {board.getTiles()[12][12], board.getTiles()[11][12],
					board.getTiles()[11][10], board.getTiles()[10][12], board.getTiles()[10][11],
					board.getTiles()[10][10], board.getTiles()[11][9], board.getTiles()[9][12],
					board.getTiles()[9][9], board.getTiles()[9][10], board.getTiles()[9][11],
					board.getTiles()[9][12], board.getTiles()[10][8], board.getTiles()[10][7]};
			goalTiles = threeTiles; break;			
		case 3:
			CheckersTile[] fourTiles = {board.getTiles()[0][4], board.getTiles()[1][4],
					board.getTiles()[0][5], board.getTiles()[2][4], board.getTiles()[2][5],
					board.getTiles()[1][6], board.getTiles()[3][4], board.getTiles()[3][5],
					board.getTiles()[2][6], board.getTiles()[2][7], board.getTiles()[2][8],
					board.getTiles()[3][8], board.getTiles()[3][9], board.getTiles()[3][10]};
			goalTiles = fourTiles; break;
		case 4:
			CheckersTile[] fiveTiles = {board.getTiles()[0][12], board.getTiles()[1][11],
					board.getTiles()[1][12], board.getTiles()[1][10], board.getTiles()[2][11],
					board.getTiles()[2][12], board.getTiles()[1][9], board.getTiles()[2][10],
					board.getTiles()[3][11], board.getTiles()[3][12], board.getTiles()[3][10],
					board.getTiles()[3][9], board.getTiles()[3][8], board.getTiles()[4][10]};
			goalTiles = fiveTiles; break;
		case 5:
			CheckersTile[] sixTiles = {board.getTiles()[12][4], board.getTiles()[11][4],
					board.getTiles()[11][5], board.getTiles()[10][4], board.getTiles()[10][5],
					board.getTiles()[11][6], board.getTiles()[9][4], board.getTiles()[9][5],
					board.getTiles()[10][6], board.getTiles()[10][7], board.getTiles()[8][4],
					board.getTiles()[8][5], board.getTiles()[8][6], board.getTiles()[8][7]};
			goalTiles = sixTiles; break;
		default:break;
		}

		//determining which goal tile is currently optimal
		for (int i = 0; i < goalTiles.length; i++) {
			if (goalTiles[i].getPiece() == null) {
				goalTile = goalTiles[i];
				break;
			}
		}

		//this gets the available moves for each piece, and also checks that the piece is not already in a goal tile
		for (int y = 0;y<17;y++) {
			for (int x = 0;x<13;x++) {
				if (board.getTiles()[x][y].getPiece() != null && board.getTiles()[x][y].getPiece().getOwner() == this) {

					if (finishedTiles.contains(board.getTiles()[x][y])) {
						continue;
					}
					possibleMoves.addAll(board.findMoves(board.getTiles()[x][y]));
				}
			}
		}

		if (possibleMoves.isEmpty()) {
			board.relayMessage("msg-"+"no moves for "+ name);
			return;
		}
		else {
			switch (difficulty) {
			case 1: //easy picks a viable move at random
				bestMove = rand.nextInt(possibleMoves.size());
				break;
			case 2: //medium calculates the distance each move would result in, and picks the one with the longest distance
				int distance = 99999;
				for (int i = 0; i<possibleMoves.size(); i++) {

					int xDistance = possibleMoves.get(i)[1].getX() - goalTile.getX();
					int yDistance = possibleMoves.get(i)[1].getY() - goalTile.getY();
					int currentXDistance = possibleMoves.get(i)[0].getX() - goalTile.getX();
					int currentYDistance = possibleMoves.get(i)[0].getY() - goalTile.getY();

					if (xDistance < 0) {
						xDistance *= -1;
					}
					if (yDistance < 0) {
						yDistance *= -1;
					}
					if (currentXDistance < 0) {
						currentXDistance *= -1;
					}
					if (currentYDistance < 0) {
						currentYDistance *= -1;
					}

					if (xDistance + yDistance < distance && currentXDistance + currentYDistance > xDistance + yDistance) {
						distance = xDistance +yDistance;
						bestMove = i;
					}
				}
				if (distance == 99999) { //still no good moves, so try moving around
					bestMove = rand.nextInt(possibleMoves.size());
				}
				break;
			default:
				break;
			}
		}

		previousTile = possibleMoves.get(bestMove)[0];
		destinationTile = possibleMoves.get(bestMove)[1];

		if (destinationTile.getX() == goalTile.getX() && destinationTile.getY() == goalTile.getY()) {
			finishedTiles.add(destinationTile);
		}

		board.move(previousTile, destinationTile);
	}

	@Override
	public boolean isAI() {
		return true;
	}

	/**
	 * Finds a move on the given board, by calculating a good move to take on the board based on the pieces and the opponent's pieces.
	 * 
	 * @param board The board on which to find a move
	 */
	public void mancalaMove(MancalaBoard board)
	{
		board.relayMessage("msg-"+name + " is taking his turn");
		PotTile selectedTile = null;
		boolean foundTurn = false;

		board.relayMessage("msg-"+name + " is thinking.");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		board.relayMessage("msg-"+name + " is thinking..");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		board.relayMessage("msg-"+name + " is thinking...");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();

		if (board.getMancalaTiles()[0].getPlayer() == this) {
			for (int i = 0; i < 6; i++) {
				if (board.getPotTiles()[i].getPieces().size() > 0) {					
					possibleMoves.add(i);
					foundTurn = true;
				}
			}
		}
		else {
			for (int i = board.getPotTiles().length-1; i > 5; i--) {
				if (board.getPotTiles()[i].getPieces().size() > 0) {
					possibleMoves.add(i);
					foundTurn = true;
				}
			}
		}

		if (!foundTurn){
			board.relayMessage("msg-"+name + " can't find a valid turn, and that makes him an unhappy robot");
		}
		else {
			switch (difficulty) {
			case 0: //easy picks a viable move at random
				selectedTile = board.getPotTiles()[possibleMoves.get(rand.nextInt(possibleMoves.size()))];
				break;

			case 1: //picks the next available viable move where the tile is furthest from their mancala
				selectedTile = board.getPotTiles()[possibleMoves.get(0)];
				break;
			default:
				break;
			}
			board.relayMessage("msg-"+name + " selected a tile");
			board.relayMessage("AISelect"+"-"+selectedTile.getTileNumber());
			board.move(selectedTile);
		}
	}

	/**
	 * Finds a move on the given board, by calculating a good move to take on the board based on the pieces and the opponent's pieces.
	 * 
	 * @param board The board on which to find a move
	 */
	public void senetMove(SenetBoard board)
	{
		board.relayMessage("msg-"+name + " is taking his turn");

		board.throwSticks();
		int throwValue = board.getSticksThrowValue();;
		boolean foundTurn = false;
		SenetTile selectedPrevious = null;
		SenetTile selectedDestination = null;

		board.relayMessage("msg-"+name + " got a throw of " + throwValue);
		board.relayMessage("msg-"+name + " is thinking.");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		board.relayMessage("msg-"+name + " is thinking..");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		board.relayMessage("msg-"+name + " is thinking...");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		ArrayList<Integer> posPreviousTiles = new ArrayList<Integer>();
		ArrayList<Integer> posDestinationTiles = new ArrayList<Integer>();
		
		for (int i = 0; i < board.getTiles().length; i++) {
			if (board.getTiles()[i].getPiece() != null && board.getTiles()[i].getPiece().getOwner() == this) {
				if ((i+throwValue < 10 || i+throwValue > 19) && i+throwValue < 30  && (i < 10 || i >19) && board.getTiles()[i+throwValue].getPiece() == null) {
					posPreviousTiles.add(i);
					posDestinationTiles.add(i+throwValue);
					foundTurn = true;
					continue;
				}
				if(i-throwValue > 9 && i-throwValue < 20 && i > 9 && i < 20)
				{
					posPreviousTiles.add(i);
					posDestinationTiles.add(i-throwValue);
					foundTurn = true;
					continue;
				}
				if(i-throwValue < 10 && i > 9 && i < 20)
				{
					int a = 10 - (i-throwValue);
					int destination = 19+a;
					posPreviousTiles.add(i);
					posDestinationTiles.add(destination);
					foundTurn = true;
					continue;
				}
				if(i+throwValue > 9 && i+throwValue < 20)
				{
					int destination = i+throwValue;
					destination += 9-((destination-10)*2);
					if(board.getTiles()[destination].getPiece() == null)
					{
						posPreviousTiles.add(i);
						posDestinationTiles.add(destination);
						foundTurn = true;
						continue;
					}
				}
			}
		}

		if (!foundTurn){
			board.notifyObservers("msg-"+name + " can't find a valid turn, and that makes him an unhappy robot");
			board.move();
		}
		else
		{
			switch(difficulty)
			{
				case 0: //random move
					int number = rand.nextInt(posPreviousTiles.size());
					selectedPrevious = board.getTiles()[posPreviousTiles.get(number)];
					selectedDestination = board.getTiles()[posDestinationTiles.get(number)];
					break;
				case 1: // move selected with piece farthest away from the end of the board
					selectedPrevious = board.getTiles()[posPreviousTiles.get(0)];
					selectedDestination = board.getTiles()[posDestinationTiles.get(0)];
				default: 
					break;
			}
			board.relayMessage("msg-"+name +" has made his turn");
			board.move(selectedPrevious, selectedDestination);
		}
	}

}
