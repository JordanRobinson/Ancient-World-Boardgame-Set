/**
 *	SenetBoard
 *	Contains the logic for the Senet game
 *
 * @package senet.model
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.model;

import java.util.ArrayList;
import java.util.Random;
import core.model.AI;
import core.model.Board;
import core.model.Player;

public class SenetBoard extends Board {

	//variable assignment here
	private SenetTile[] tiles; // 30 tiles
	private Random rngen;
	private boolean throwTime;
	private int sticksThrowValue;
	private static final long serialVersionUID = 3L;

	//constructors here
	/**
	 * Default constructor to set up game board. 
	 */
	public SenetBoard()
	{
		//variables passed in

		//variables not passed in
		tiles = new SenetTile[30];
		sticksThrowValue = 0;

		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new SenetTile(i);
		}
		
		if (System.getProperty("senet.playerOne.type").equals("0")) {
			addPlayer(System.getProperty("playerOne.name"),false);
		}
		else {
			addPlayer(System.getProperty("playerOne.name"),true);
			((AI) players.get(0)).setDifficulty(Integer.parseInt(System.getProperty("senet.playerOne.difficulty")));
		}
		if (System.getProperty("senet.playerTwo.type").equals("0")) {
			addPlayer(System.getProperty("playerTwo.name"),false);
		}
		else {
			addPlayer(System.getProperty("playerTwo.name"),true);
			((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("senet.playerOne.difficulty")));
		}

		
		rngen = new Random();
		setupPieces();
		throwTime = true;
		
		//if the first player is an ai, we need to make sure that the moves are made after the board is drawn, so we call nextplayer on the first click of the game instead
		if (!getPlayers().get(0).isAI()) {
			nextPlayer();
		}
		
		ruleset.add(new MatchRollRule());
		ruleset.add(new DefendedRule());
		ruleset.add(new MoveRule());
		ruleset.add(new BeautifulHouseRule());
	}

	//gets here
	/**
	 * Gets the active player.
	 * 
	 * @return the active player
	 */
	public Player getActivePlayer()
	{
		return activePlayer;
	}
	/**
	 * Gets throwTime to see if the AI needs to throw the sticks.
	 * 
	 * @return true if the AI needs to throw, false if not
	 */
	public boolean getThrowTime()
	{
		return throwTime;
	}
	/**
	 * Gets the throwSticks value
	 * 
	 * @return the last value thrown by throwSticks
	 */
	public int getSticksThrowValue()
	{
		return sticksThrowValue;
	}
	
	/**
	 * Gets the tiles held in this board and returns them as an array
	 * 
	 * @return a SenetTile array of the tiles held in by this board
	 */
	public SenetTile[] getTiles()
	{
		return tiles;
	}

	//sets here
	/**
	 * Sets throwTime to see if AI needs to throw.
	 * 
	 * @param throwTime the value to setThrow time as
	 */
	public void setThrowTime(boolean throwTime) 
	{
		this.throwTime = throwTime;
	}	

	//methods here
	
	/**
	 * Finds all the players pieces and returns them in an ArrayList.
	 * 
	 * @return ArrayList of the tiles that the players pieces are currently on
	 */
	public ArrayList<SenetTile> findHint()
	{
		ArrayList<SenetTile> hints = new ArrayList<SenetTile>();
		for(int i = 0; i < tiles.length; i++)
		{
			if(tiles[i].getPiece() != null)
			{
				if(tiles[i].getPiece().getOwner() == activePlayer)
				{
					hints.add(tiles[i]);
				}
			}
		}
		return hints;
	}
	
	/**
	 * Finds the destination tile for the move so it can be highlighted.
	 * 
	 * @param selectedTile the tile selected by the player to move
	 * @return the number of the destination tile to highlight
	 */
	public int findMove(SenetTile selectedTile)
	{
		int selectedTileNumber = selectedTile.getTileNumber();
		 int destination = selectedTile.getTileNumber() + sticksThrowValue;
		 if(destination > 9 && destination < 20) //changes the second line of the board around so it 'snakes'
			{
				destination = (9-((destination-10) * 2))+destination;
			}
		 if(selectedTile.getTileNumber() > 9 && selectedTile.getTileNumber() < 20) //changes the second line of the board around so it 'snakes'
			{
			 	selectedTileNumber = (9-((selectedTile.getTileNumber()-10) * 2))+selectedTile.getTileNumber();
			 	destination = selectedTileNumber + sticksThrowValue;
			 	if(destination<20)
			 	{
			 		destination= (9-((destination-10) * 2))+destination;
			 	}
			}
		 return destination;
	}
	
	/**
	 * Takes two tiles and attempts to move the piece held in the previousTile into the destinationTile.
	 * Error checking and rules are checked to see if the move is a legal move or not.
	 * 
	 * @param previousTile the tile being moved from
	 * @param destinationTile the tile being moved to
	 */
	public void move(SenetTile previousTile, SenetTile destinationTile)
	{
		if (previousTile.getPiece() == null) {
			System.out.println("invalid - no piece");
			setChanged();
			notifyObservers("msg-" + getActivePlayer().getName() + " no piece");
			return;
		}
		
		if (previousTile.getPiece().getOwner() != activePlayer) {
			System.out.println("not your piece");
			setChanged();
			notifyObservers("msg-" + getActivePlayer().getName() + " not your piece!");
			return;
		}
		
		if(destinationTile.getPiece() != null && destinationTile.getPiece().getOwner() == activePlayer) {
			System.out.println("you own this piece!");
			setChanged();
			notifyObservers("msg-" + getActivePlayer().getName() + " you own this piece!");
			return;
		}	

		SenetCommand command = new SenetCommand(this, previousTile, destinationTile);

		for (int i = 0; i< ruleset.size(); i++) {
			if (!ruleset.get(i).checkValid(this, command)) {
				setChanged();
				notifyObservers("msg-" + getActivePlayer().getName() +" "+ ruleset.get(i).getInvalidMsg());
				System.out.println(ruleset.get(i).getInvalidMsg());
				return;
			}
		}
		commandInvoker.addCommand(command);
		commandInvoker.executeCommand();
		
		tiles[29].setPiece(null);
		
		setChanged();
		notifyObservers();
		
		if (checkWinCondition()) {
			System.out.println(activePlayer.getName() + " Wins!");
			setChanged();
			notifyObservers("msg-" + getActivePlayer().getName() + " Wins!");
			return;
		}
		
		if (activePlayer.equals(localPlayer)) {
			super.setOutboundMessage(command.getPreviousTile().getTileNumber() + "," + command.getDestinationTile().getTileNumber() + "," + sticksThrowValue);
		}
		
		nextPlayer();
		
		if (localPlayer != null && activePlayer != localPlayer) {
			waitForMove();
		}
	}
	
	/**
	 * If the AI can't find a move it will call this method and pass the turn.
	 */
	public void move() 
	{
		nextPlayer();
	}
	
	
	/**
	 * Sets the next player in the queue and if the player is an AI it calls the AI's move method.
	 */
	public void nextPlayer()
	{
		super.nextPlayer();
		if (activePlayer.isAI()) {
			AI bot = (AI) activePlayer;
			bot.senetMove(this);
		}
		else {
			throwTime = true;
		}
	}

	/**
	 * Checks to see if the win condition has been met.
	 * 
	 * @return returns true if the win condition is met, false if it isn't
	 */
	public boolean checkWinCondition()
	{
		boolean firstPlayerHasPieces = false;
		boolean secondPlayerHasPieces = false;
		boolean ret = true;
		
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getPiece() != null) {
				if (tiles[i].getPiece().getOwner() == players.get(0)) {
					firstPlayerHasPieces = true;
				}
				else if (tiles[i].getPiece().getOwner() == players.get(1)) {
					secondPlayerHasPieces = true;
				}
			}			
		}
		
		if (firstPlayerHasPieces && secondPlayerHasPieces) {
			ret = false;
		}
		return ret;
	}

	/**
	 * Randomly generates a number from one to five and sets the sticksThrowValue.
	 */
	public void throwSticks()
	{
		sticksThrowValue = rngen.nextInt(5)+1;
		setChanged();
		notifyObservers("msg-" + getActivePlayer().getName() + " threw a " + sticksThrowValue + "!");
	}

	/**
	 * Sets up the pieces in the board, arranged on the top row with alternate pieces for each player.
	 */
	private void setupPieces()
	{
		boolean flipflop = true;

		for (int i = 0; i < 10; i++) {
			if (flipflop) {
				tiles[i].setPiece(new SenetPiece(players.get(0)));
				flipflop = !flipflop;
			}
			else {
				tiles[i].setPiece(new SenetPiece(players.get(1)));
				flipflop = !flipflop;
			}
		}
	}
	
	/**
	 * Waits for the move from the networked opponent, then performs the move once it is received.
	 */
	public void waitForMove() {
		relayMessage("Waiting for opponent move");
		
		while (activePlayer != localPlayer) { //while the active player is not the player whose turn it currently is
			while (inboundMessage == null) {} //while the inbound message has not yet been received, we wait
			String[] split = inboundMessage.split(","); //once the message is received we need to parse it
			SenetTile source = getTiles()[Integer.parseInt(split[0])]; //getting the source and destination tiles from the message received
			SenetTile dest = getTiles()[Integer.parseInt(split[1])];
			sticksThrowValue = Integer.parseInt(split[2]);
			inboundMessage = null; //then set the inbound message to null for next time we need to wait for a message
			move(source, dest); //then we make the move, which should switch over the active player
			return; //this return is here just in case for whatever reason the active player has not yet changed, so that we don't perform the same move twice
		}
	}
}