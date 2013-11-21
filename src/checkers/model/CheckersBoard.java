/**
 *  CheckersBoard Class
 *  The class in which most of the logic relating to the Chinese checkers game is located.
 * 
 * @package checkers.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.2.9 15 Apr 2012
 */

package checkers.model;

import java.util.ArrayList;
import core.model.AI;
import core.model.Board;

public class CheckersBoard extends Board {

	//variable assignment here
	private CheckersTile[][] tiles; //the tiles of the board, represented by x, then y in terms of order
	private static final long serialVersionUID = 3L;
	
	//constructors here
	/**
	 * Default constructor that sets up the board so that it can be used for a new game of Chinese Checkers.
	 */
	public CheckersBoard()
	{
		super();
		//variables passed in

		//variables not passed in
		int[] accessible = { //this is to set up the accessible and inaccessible areas of the board

				0,0,0,0,0,0,1,0,0,0,0,0,0, //by setting the int here to a 0 or a 1, we can show the pattern required of the board
				0,0,0,0,0,1,1,0,0,0,0,0,0, //note that this int array is not shaped as such, and this is only arranged in this way for illustrative purposes
				0,0,0,0,0,1,1,1,0,0,0,0,0,
				0,0,0,0,1,1,1,1,0,0,0,0,0,
				1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,0,
				0,1,1,1,1,1,1,1,1,1,1,1,0,
				0,1,1,1,1,1,1,1,1,1,1,0,0,
				0,0,1,1,1,1,1,1,1,1,1,0,0,
				0,1,1,1,1,1,1,1,1,1,1,0,0,
				0,1,1,1,1,1,1,1,1,1,1,1,0,
				1,1,1,1,1,1,1,1,1,1,1,1,0,
				1,1,1,1,1,1,1,1,1,1,1,1,1,
				0,0,0,0,1,1,1,1,0,0,0,0,0,
				0,0,0,0,0,1,1,1,0,0,0,0,0,
				0,0,0,0,0,1,1,0,0,0,0,0,0,
				0,0,0,0,0,0,1,0,0,0,0,0,0,
		};
		tiles = new CheckersTile[13][17];

		int counter = 0;

		for (int y = 0;y<17;y++) { //this works by using the array of integers to decide which tiles are accessible, and then adding that tile to the array of tiles
			for (int x = 0;x<13;x++) {
				CheckersTile tile;
				if (accessible[counter] == 1) {
					tile = new CheckersTile(true,x,y); //accessible, x coord, y coord
				}
				else {
					tile = new CheckersTile(false,x,y);
				}
				tiles[x][y] = tile;
				counter++;
			}
		}

		setupPlayers();
		ruleset.add(new MoveRule());

		//if the first player is an ai, we need to make sure that the moves are made after the board is drawn, so we call nextplayer on the first click of the game instead
		if (!getPlayers().get(0).isAI()) {
			nextPlayer();
		}
	}

	//gets here
	public CheckersTile[][] getTiles()
	{
		return tiles;
	}

	//sets here
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
		boolean ret = false;

		switch (players.indexOf(activePlayer)) {
		case 0: 
			if (tiles[6][16].getPiece() != null && tiles[6][16].getPiece().getOwner() == players.get(0)) { //do a quick check to see if the farthest tile contains a piece belonging to the active player, to save checking all the goal tiles unnecessarily
				CheckersTile[] goalTiles = {tiles[5][15], tiles[6][15], tiles[5][14], tiles[6][14], tiles[7][14],
						tiles[4][13], tiles[5][13], tiles[6][13], tiles[7][13]}; //these are the tiles that need to occupied by the active player for them to win
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null || goalTiles[i].getPiece().getOwner() != players.get(0)) {
						ret = false;
						break; //break out since all the pieces in the goal tiles must be owned by the active player, and all must be in the goal tiles for the active player to have won, so no point checking any others
					}			
				}
			}
			break;

		case 1: 
			if (tiles[6][0].getPiece() != null && tiles[6][0].getPiece().getOwner() == players.get(1)) {
				CheckersTile[] goalTiles = {tiles[5][1], tiles[6][1], tiles[5][2], tiles[6][2], tiles[7][2],
						tiles[4][3], tiles[5][3], tiles[6][3], tiles[7][3]};
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null || goalTiles[i].getPiece().getOwner() != players.get(1)) {
						ret = false;
						break;
					}
				}
			}
			break;

		case 2: 
			if (tiles[12][12].getPiece() != null && tiles[12][12].getPiece().getOwner() == players.get(2)) {
				CheckersTile[] goalTiles = {tiles[11][12], tiles[11][11], tiles[11][10], tiles[10][12],
						tiles[10][11], tiles[10][10], tiles[11][9], tiles[9][12], tiles[12][11]};
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null || goalTiles[i].getPiece().getOwner() != players.get(2)) {
						ret = false;
						break;
					}		
				}
			}
			break;

		case 3: 
			if (tiles[0][4].getPiece() != null && tiles[0][4].getPiece().getOwner() == players.get(3)) {
				CheckersTile[] goalTiles = {tiles[1][4], tiles[0][5], tiles[2][4], tiles[2][5], tiles[1][6],
						tiles[3][4], tiles[3][5], tiles[2][6], tiles[2][7]};
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null) {
						ret = false;
						break;
					}
					else if (goalTiles[i].getPiece().getOwner() != players.get(3)) {
						ret = false;
						break;
					}			
				}
			}
			break;

		case 4: 
			if (tiles[0][12].getPiece() != null && tiles[0][12].getPiece().getOwner() == players.get(4)) {
				CheckersTile[] goalTiles = {tiles[0][12], tiles[1][11], tiles[1][12], tiles[1][10], tiles[2][11],
						tiles[2][12], tiles[1][9], tiles[2][10], tiles[3][11], tiles[3][12]};
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null) {
						ret = false;
						break;
					}
					else if (goalTiles[i].getPiece().getOwner() != players.get(4)) {
						ret = false;
						break;
					}			
				}
			}
			break;

		case 5: 
			if (tiles[12][4].getPiece() != null && tiles[12][4].getPiece().getOwner() == players.get(5)) {
				CheckersTile[] goalTiles = {tiles[12][4], tiles[11][4], tiles[11][5], tiles[10][4], tiles[10][5],
						tiles[11][6], tiles[9][4], tiles[9][5], tiles[10][6], tiles[10][7]};
				ret = true;
				for (int i = 0; i < goalTiles.length; i++) {
					if (goalTiles[i].getPiece() == null) {
						ret = false;
						break;
					}
					else if (goalTiles[i].getPiece().getOwner() != players.get(5)) {
						ret = false;
						break;
					}			
				}
			}
			break;
		default: break; //should never get here, but just in case
		}
		gameFinished = ret;
		return ret;
	}

	/**
	 * This method takes a CheckersTile as a seed, and returns all valid moves that can be made from that tile.
	 * 
	 * It takes into account jumping over tiles, and the structure of the board in terms of the locations of tiles.
	 * 
	 * It does NOT take into account whether or not the tile passed in is a valid tile, and as such may provide unexpected results if a tile not containing a piece, or an inaccessible tile is passed in.
	 * 
	 * @param start the seed tile to be passed in, this should contain a valid CheckersPiece
	 * @return an ArrayList of all moves that can be made from that tile, in the format of two CheckersTiles as an array, with the first position relating to the start tile, and the second (and final) relating to the destination 
	 */
	public ArrayList<CheckersTile[]> findMoves(CheckersTile start) {

		ArrayList<CheckersTile[]> foundMoves = new ArrayList<CheckersTile[]>();

		int xPos = start.getX();
		int yPos = start.getY();

		for (int xOffset = -1; xOffset < 2; xOffset++) {
			for(int yOffset = -1; yOffset < 2; yOffset++) {

				xPos = start.getX(); //later on in this method we have to modify x slightly, so it is reset to it's original value here

				boolean outOfBounds = (xPos + xOffset < 0 || yPos + yOffset < 0 || xPos + xOffset > 12 || yPos + yOffset > 16); //this is to make sure that the tile we're jumping into is within bounds of the array of tiles

				/*
				 * The following might look a little complex at first glance, but it is necessary.
				 * 
				 * Since the movement of a piece in chinese checkers is not in a square grid, but in a hex shape, we need to shave the corners off.
				 * However, since the board is comprised by shifting the rows to make it shaped correctly, this means that the corners that we need to
				 * shave off are different depending on whether the row is a 'shifted' one or not.
				 */

				boolean onModRow = (!(yPos%2 == 0)); //this checks if we are in a 'shifted' row 
				boolean modShift = (yPos + yOffset != yPos && (xPos + xOffset) == xPos-1); //if we are in a shifted row, the top left and bottom left cells should not be accessible
				boolean nonModShift = (yPos + yOffset != yPos && (xPos + xOffset) == xPos+1); //and if we aren't, the top right and bottom right cells should not be accessible

				if (outOfBounds || (onModRow && modShift) || (!onModRow && nonModShift)) {
					continue; //if any of those are true, we should ignore that cell, since no matter what, a piece cannot move there in one move
				}

				if (getTiles()[xPos + xOffset][yPos + yOffset].getPiece() == null && getTiles()[xPos + xOffset][yPos + yOffset].getAccessible()) { //if this passes it means that we've found an adjacent empty tile for the piece to move to, so we should add it to the list of pieces to return
					CheckersTile[] move = {start, getTiles()[xPos + xOffset][yPos + yOffset]};
					foundMoves.add(move);
				}
				else if (!(xPos + xOffset*2 < 0 || yPos + yOffset*2 < 0 || xPos + xOffset*2 > 12 || yPos + yOffset*2 > 16)) {
					//if we get this far then there should be pieces we can jump over, and still be in bounds of the board
					//first we need to modify the xPos accordingly to reflect jumping over pieces and the change that this has on the shifted tiles mentioned before
					if (onModRow && !((yPos + yOffset*2) == yPos)) {
						xPos -=1;
					}
					else if (yOffset == 0) { //destination is on same row so do nothing
					}
					else {
						xPos +=1;
					}

					boolean emptyDestination = (getTiles()[xPos + (xOffset*2)][yPos + (yOffset*2)].getPiece() == null); //this is to check that the space over the adjacent piece is free to jump into
					boolean accessible = (getTiles()[xPos + (xOffset*2)][yPos + (yOffset*2)].getAccessible()); //this checks that the space we're jumping into is an accessible cell
					if (emptyDestination && accessible) {
						foundMoves.add(new CheckersTile[] {start, getTiles()[xPos + (xOffset*2)][yPos + (yOffset*2)]}); //we found a valid move, so we add it to the list
					}
				}				
			}
		}
		return foundMoves;
	}

	/**
	 * Takes two tiles and moves the piece in the source tile to the destination tile.
	 * This has some basic error checking included, so should pick up if a piece has not been selected, or if the destination tile already contains a piece.
	 * 
	 * @param previousTile The source tile to move a piece from
	 * @param destinationTile The destination tile to move the piece to
	 */
	public void move(CheckersTile previousTile, CheckersTile destinationTile)
	{
		if (previousTile.getPiece() == null) { //needs a piece to move
			relayMessage("msg-"+"Please select a piece");
			return;
		}

		if (destinationTile.getPiece() != null) { //needs an empty tile to move into
			relayMessage("msg-"+"There's already a piece in that tile");
			return;
		}

		CheckersCommand command = new CheckersCommand(this, previousTile, destinationTile); //we have to make it into a command before we check it against the list of rules

		for (int i = 0; i< ruleset.size(); i++) { //check that it passes all rules
			if (!ruleset.get(i).checkValid(this, command)) {
				relayMessage("msg-"+ruleset.get(i).getInvalidMsg()); //let the user know that there's a problem with the move, using the notification window
				return;
			}
		}
		commandInvoker.addCommand(command); //all rules and checks have come back valid, so we add it and then carry out the move
		commandInvoker.executeCommand();

		setChanged(); //the board needs to redraw to show that the move has taken place
		notifyObservers();

		if (checkWinCondition()) { //after the move has happened, we need to check the win condition
			setChanged();
			notifyObservers("msg-"+activePlayer.getName()+" wins!"); //and let the players know if anyone has won
			return;
		}
		if (activePlayer.equals(localPlayer)) {
			super.setOutboundMessage(command.getPreviousTile().getCoords() + "," + command.getDestinationTile().getCoords());
		}

		nextPlayer(); //move is finished so we go to the next player
		
		if (localPlayer != null && activePlayer != localPlayer) {
			waitForMove();
		}
	}

	/**
	 * Facilitates the switching of players, and also checks if the next player is an AI, then if so, calls their move method.
	 */
	public void nextPlayer()
	{
		super.nextPlayer();

		if (activePlayer.isAI()) {
			AI bot = (AI) activePlayer;
			bot.checkersMove(this);			
		}		
	}

	/**
	 * Sets up the players so that a new game can commence. Also checks what players are human and AI, and sets up the pieces for all playing players.
	 * Checks are performed by using the properties of the system, which should be set up before calling this method.
	 */
	private void setupPlayers() 
	{
		if (localPlayer != null) { //if the local player is set, network mode is enabled
			addPlayer("Player One",false); //so we create the players as according to the requirements for network play (No AIs)
			tiles[6][0].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1))); //adding the pieces for the players
			tiles[5][1].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][1].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[4][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			addPlayer("Player Two",false);
			tiles[6][16].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][15].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][15].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[4][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			
			return; //return, since we don't need to do anything else with the players
		}
		
		if (!System.getProperty("checkers.playerOne.type").equals("2")) { //if it's not two then this player is enabled
			if (System.getProperty("checkers.playerOne.type").equals("0")) { //zero means human
				addPlayer(System.getProperty("playerOne.name"),false); //so we create it using the name stored in properties
			}
			else {
				addPlayer(System.getProperty("playerOne.name"),true); //otherwise the player type should only ever be 1, which equates to AI
				((AI) players.get(0)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerOne.difficulty"))); //the difficulty of the AI also needs to be set, as seen here
			}
			tiles[6][0].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1))); //after this all that remains is to actually add the pieces to the board
			tiles[5][1].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][1].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][2].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[4][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][3].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
		}

		if (!System.getProperty("checkers.playerTwo.type").equals("2")) {
			if (System.getProperty("checkers.playerTwo.type").equals("0")) {
				addPlayer(System.getProperty("playerTwo.name"),false);
			}
			else {
				addPlayer(System.getProperty("playerTwo.name"),true);
				((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerTwo.difficulty")));
			}
			tiles[6][16].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][15].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][15].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][14].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[4][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[5][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[6][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[7][13].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
		}

		if (!System.getProperty("checkers.playerThree.type").equals("2")) {
			if (System.getProperty("checkers.playerThree.type").equals("0")) {
				addPlayer(System.getProperty("playerThree.name"),false);
			}
			else {
				addPlayer(System.getProperty("playerThree.name"),true);
				((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerThree.difficulty")));
			}
			tiles[3][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][6].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][7].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][6].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[0][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[0][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));	
		}

		if (!System.getProperty("checkers.playerFour.type").equals("2")) {
			if (System.getProperty("checkers.playerFour.type").equals("0")) {
				addPlayer(System.getProperty("playerFour.name"),false);
			}
			else {
				addPlayer(System.getProperty("playerFour.name"),true);
				((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerFour.difficulty")));
			}
			tiles[10][9].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][10].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[9][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[9][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][10].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[12][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));		
		}

		if (!System.getProperty("checkers.playerFive.type").equals("2")) {
			if (System.getProperty("checkers.playerFive.type").equals("0")) {
				addPlayer(System.getProperty("playerFive.name"),false);
			}
			else {
				addPlayer(System.getProperty("playerFive.name"),true);
				((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerFive.difficulty")));
			}
			tiles[12][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[9][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[9][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][5].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[11][6].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][7].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][6].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[10][4].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));		
		}

		if (!System.getProperty("checkers.playerSix.type").equals("2")) {
			if (System.getProperty("checkers.playerSix.type").equals("0")) {
				addPlayer(System.getProperty("playerSix.name"),false);
			}
			else {
				addPlayer(System.getProperty("playerSix.name"),true);
				((AI) players.get(1)).setDifficulty(Integer.parseInt(System.getProperty("checkers.playerSix.difficulty")));
			}
			tiles[0][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[0][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][10].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[3][12].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][11].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[2][10].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));
			tiles[1][9].setPiece(new CheckersPiece(getPlayers().get(getPlayers().size()-1)));			
		}
	}
	
	/**
	 * Waits for the move from the networked opponent, then performs the move once it is received
	 */
	public void waitForMove() {
		relayMessage("Waiting for opponent move");
		
		while (activePlayer != localPlayer) { //while the active player is not the player whose turn it currently is
			while (inboundMessage == null) {} //while the inbound message has not yet been received, we wait
			String[] split = inboundMessage.split(","); //once the message is received we need to parse it
			CheckersTile source = getTiles()[Integer.parseInt(split[0])][Integer.parseInt(split[1])]; //getting the source and destination tiles from the message received
			CheckersTile dest = getTiles()[Integer.parseInt(split[2])][Integer.parseInt(split[3])];
			inboundMessage = null; //then set the inbound message to null for next time we need to wait for a message
			move(source, dest); //then we make the move, which should switch over the active player
			return; //this return is here just in case for whatever reason the active player has not yet changed, so that we don't perform the same move twice
		}
	}
}