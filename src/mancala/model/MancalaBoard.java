/**
 *  MancalaBoard Class
 *  The class in which most of the logic relating to the Mancala game is located.
 *
 * @package mancala.model
 * @author Jordan Robinson, Ryan Carter
 * @version 2.2.9.0 16 Feb 2012
 */

package mancala.model;

import core.model.AI;
import core.model.Board;
import core.model.CommandInvoker;

public class MancalaBoard extends Board {

	//variable assignment here
	private PotTile[] potTiles; //the interactive, smaller tiles of the board
	private MancalaTile[] mancalaTiles; //the larger tiles, that collect the pieces. Should be one per player
	private static final long serialVersionUID = 3L;
	//constructors here
	/**
	 * Default constructor for the mancala board. Sets up the required variables suce as the tiles and pieces.
	 */
	public MancalaBoard()
	{
		//variables passed in

		//variables not passed in
		setupPlayers(); //setup the list of players
		potTiles = new PotTile[12];
		mancalaTiles = new MancalaTile[2];

		for (int i = 0; i < potTiles.length; i++) { //setting up the pot tiles

			if (i < 6) {
				potTiles[i] = new PotTile(i, getPlayers().get(0)); //add tiles for player one
			}
			else {
				potTiles[i] = new PotTile(i, getPlayers().get(1)); //add tiles for player two
			}

			for (int j = 0; j < 4; j++) { //add the pieces for the newly added tile
				potTiles[i].addPiece(new MancalaPiece());
			}
		}
		mancalaTiles[0] = new MancalaTile(getPlayers().get(0)); //setting up the mancala tiles
		mancalaTiles[1] = new MancalaTile(getPlayers().get(1));


		//if the first player is an ai, we need to make sure that the moves are made after the board is finished drawing, so we call nextplayer on the first click of the game instead
		if (!getPlayers().get(0).isAI()) {
			nextPlayer();
		}
		ruleset.add(new LandOnZeroRule()); //add the rules needed for mancala
		ruleset.add(new LandOnMancalaRule());
	}

	//gets here
	public PotTile[] getPotTiles()
	{
		return potTiles;
	}
	public MancalaTile[] getMancalaTiles()
	{
		return mancalaTiles;
	}
	public CommandInvoker getCommandInvoker()
	{
		return commandInvoker;
	}
	/**
	 * Returns the tile considered opposite from the tile provided, in a positional sense
	 * 
	 * @param tile The tile to use for input
	 * @return The tile considered in the opposite position
	 */
	private PotTile getOppositeTile(PotTile tile)
	{
		int opposite = -1; //set initially to an impossible value

		switch(tile.getTileNumber()) { //then simply switch as per the tile opposite each tile
		case 0: opposite = 11; break;
		case 1: opposite = 10; break;
		case 2: opposite = 9; break;
		case 3: opposite = 8; break;
		case 4: opposite = 7; break;
		case 5: opposite = 6; break;
		case 6: opposite = 5; break;
		case 7: opposite = 4; break;
		case 8: opposite = 3; break;
		case 9: opposite = 2; break;
		case 10: opposite = 1; break;
		case 11: opposite = 0; break;
		}
		return getPotTiles()[opposite]; //and return the tile opposite now we know which one it is
	}

	//methods here
	/**
	 * As the name implies, this checks if the win condition of the
	 * game has been fulfilled, then returns a boolean value stating that fact.
	 * 
	 * Note that this does not check who has won the game, only if 
	 * it has been won or not. Also it checks based on the activePlayer,
	 * so it should be called after the user has taken their move, but 
	 * before the nextPlayer method is called.
	 * 
	 * @return True if the game has been won, False if it has not
	 */
	private boolean checkWinCondition()
	{	
		int pieces = 0; //initialise pieces as 0

		for (int i = 0; i < 6; i++) { //then add the contents of the tiles on one side to pieces
			pieces += getPotTiles()[i].getPieces().size();
		}

		if (pieces == 0) { //if pieces is still zero, then this means that all the tiles on one side are empty
			return true; //so the game has finished
		}

		pieces = 0;

		for (int i = 6; i < 12; i++) { //this does the same as above, but for the opposite side
			pieces += getPotTiles()[i].getPieces().size();
		}

		if (pieces == 0) {
			return true;
		}

		return false; //otherwise there are still pieces on each side, so the game has not finished yet
	}

	/**
	 * Performs a move within the confines of the rules of mancala.
	 * 
	 * Takes a pot as input, and then moves the pieces from that pot around the board. Also performs some basic validation
	 * 
	 * @param selectedTile The tile that has been selected for a move by the user
	 */
	public void move(PotTile selectedTile)
	{
		if (selectedTile.getPieces().size() == 0) { //if the tile selected has no pieces in, we have nothing to move, so tell the user as such
			setChanged();
			notifyObservers("msg-" + "No pieces in that tile");
			return; //nothing to do, so we return
		}

		MancalaCommand command = new MancalaCommand(this, selectedTile); //if we got here then the move passed some basic validation, so we create a command from it
		if (ruleset.get(0).checkValid(this, command)) { //checking land on zero rule

			PotTile destinationTile = command.getDestinationTile(); //if the land on zero rule is proven true, we need to move all the pieces from the opposite tile into our player's mancala
			moveAllPieces(getOppositeTile(destinationTile), getMancalaTiles()[players.indexOf(activePlayer)]); //like so
			relayMessage("msg-" + getActivePlayer().getName() + " landed on a zero tile!"); //then send a message saying it happened, which should also update the graphical representation of the board
		}

		if (ruleset.get(1).checkValid(this, command)) { //checking the land on mancala rule
			relayMessage("msg-" + getActivePlayer().getName() + " landed on a mancala!"); //let the player know that they landed on a mancala
			commandInvoker.addCommand(command); //execute the command from which this move was started
			commandInvoker.executeCommand();

			if (checkWinCondition()) { //if we've won, we don't need another turn, so go to the next player, where the win condition should get picked up
				nextPlayer();
				return;				
			}

			if (activePlayer.isAI()) { //if it's an AI we need to give it another move in this way, since otherwise it won't act after this turn
				AI bot = (AI) activePlayer;
				bot.mancalaMove(this);
			}
			setChanged(); //this is for a repaint
			notifyObservers();

			return;	//since we don't want to go to next player, we return here, allowing the user to take their extra turn
		}

		commandInvoker.addCommand(command); //after we're done processing the rules, we should carry out the move
		commandInvoker.executeCommand();

		setChanged(); //then repaint since the board has changed because we've executed the command
		notifyObservers();

		if (activePlayer.equals(localPlayer)) { //this section is for network play
			super.setOutboundMessage(command.getSelectedTile().getTileNumber()+""); //setting the message to send to the currently inactive player, this should then get picked up by the other thread
		}
		nextPlayer(); //our move is done, so we set it to the next player

		if (localPlayer != null && activePlayer != localPlayer) { //more network play, this checks if the current player is not the local player
			waitForMove(); //since if correct, we need to wait for the opponent's move to come from over the network
		}
	}

	/**
	 * Moves all the pieces from the input PotTile into the input MancalaTile.
	 * 
	 * @param source The tile to remove all pieces from
	 * @param destination The tile to put all the pieces from the previous tile into
	 */
	private void moveAllPieces(PotTile source, MancalaTile destination)
	{
		destination.getPieces().addAll(source.getPieces());

		for (int i = source.getPieces().size()-1; i > 0; i--) {
			destination.addPiece(source.getPieces().get(i));
		}
		source.removeAllPieces();
	}

	/**
	 * Facilitates the switching of players, and also checks if the next player is an AI, then if so, calls their move method, as well as checking the win condition.
	 */
	@Override
	public void nextPlayer()
	{
		if(checkWinCondition()) { //if the game is over
			if (getMancalaTiles()[0].getPieces().size() > getMancalaTiles()[1].getPieces().size()) { //check who won
				relayMessage("msg-"+players.get(0).getName() + " wins! With " + getMancalaTiles()[0].getPieces().size() + " pieces"); //and display as appropriate
			}
			else {
				relayMessage("msg-"+players.get(1).getName() + " wins! With " + getMancalaTiles()[1].getPieces().size() + " pieces");
			}
			return;
		}

		super.nextPlayer();

		if (activePlayer.isAI()) { //if the player is an AI, we need to tell it to move here, before we return so the user can make a move
			AI bot = (AI) activePlayer;
			bot.mancalaMove(this);
		}
	}

	/**
	 * Sets up the players so that a new game can commence. Also checks what players are human and AI.
	 * Checks are performed by using the properties of the system, which should be set up before calling this method.
	 */
	private void setupPlayers()
	{
		if (localPlayer != null) { //if the local player is set, network mode is enabled
			addPlayer("Player One",false); //so we create the players as according to the requirements for network play (No AIs)
			addPlayer("Player Two",false);
			return;
		}

		//adding the players, checking if they are AI or not
		if (System.getProperty("mancala.playerOne.type").equals("0")) { //this means the player is a human
			addPlayer(System.getProperty("playerOne.name"),false); //so we add the player as required
		}
		else { //otherwise the player is an AI
			addPlayer(System.getProperty("playerOne.name"),true);
			((AI) players.get(0)).setDifficulty(Integer.parseInt(System.getProperty("mancala.playerOne.difficulty"))); //so we need to set the difficulty
		}

		if (System.getProperty("mancala.playerTwo.type").equals("0")) {
			addPlayer(System.getProperty("playerTwo.name"),false);
		}
		else {
			addPlayer(System.getProperty("playerTwo.name"),true);
			((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("mancala.playerTwo.difficulty")));
		}
	}

	/**
	 * Waits for the move from the networked opponent, then performs the move once it is received
	 */
	public void waitForMove() 
	{
		relayMessage("Waiting for opponent move");

		while (activePlayer != localPlayer) { //while the active player is not the player whose turn it currently is
			while (inboundMessage == null) {} //while the inbound message has not yet been received, we wait
			PotTile selectedTile = getPotTiles()[Integer.parseInt(inboundMessage)]; //getting the tile to select from the message
			inboundMessage = null; //then set the inbound message to null for next time we need to wait for a message
			//take move, this should switch active player
			move(selectedTile); //then we make the move, which should switch over the active player
			return; //this return is here just in case for whatever reason the active player has not yet changed, so that we don't perform the same move twice
		}		
	}
}