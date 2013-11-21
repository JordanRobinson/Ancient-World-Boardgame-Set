/**
 *  Board Class
 *  To be extended upon by each respective game's board. Also contains the code for netplay between games
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.2.4 15 Apr 2012
 */

package core.model;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class Board extends Observable implements Serializable {

	//variable assignment here
	private static final long serialVersionUID = 3L;
	protected List<Rule> ruleset; //the rules for each game, to be used by the subclasses of this class
	protected ArrayList<Player> players; //players, which should also be used by the subclasses of this class
	protected CommandInvoker commandInvoker;
	protected Player activePlayer; //the player who's turn it currently is
	protected boolean gameFinished; 
	protected Player localPlayer; //this and the below for network play
	protected volatile String outboundMessage;
	protected volatile String inboundMessage;
	protected volatile boolean connected;
	protected String serverIp;
	protected int serverPort;

	//constructors here
	/**
	 * Default and only constructor for board.
	 */
	protected Board()
	{
		//variables passed in

		//variables not passed in
		players = new ArrayList<Player>();
		ruleset = new ArrayList<Rule>();
		commandInvoker = new CommandInvoker();
		activePlayer = null;
		gameFinished = false;
		outboundMessage = null;
		inboundMessage = null;
		connected = false;
	}

	//gets here
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	public CommandInvoker getCommandInvoker()
	{
		return commandInvoker;
	}
	public Player getActivePlayer()
	{
		return activePlayer;
	}
	public String getOutboundMessage() {
		return outboundMessage;
	}
	public boolean getConnected() {
		return connected;
	}
	public Player getLocalPlayer() {
		return localPlayer;
	}

	//sets here
	public void setOutboundMessage(String outboundMessage) {
		this.outboundMessage = outboundMessage;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public void setLocalPlayer(Player localPlayer) {
		this.localPlayer = localPlayer;
	}
	public void setInbound(String inbound) {
		this.inboundMessage = inbound;
	}

	//methods here
	/**
	 * Adds a player to the current list of players
	 * 
	 * Note that while it may be possible to add a lot of players, it may not be a good idea to add more than two outside of checkers.
	 * 
	 * @param playerName the name of the player to add to the player list
	 * @param AI denotes whether or not the player is an AI
	 */
	protected void addPlayer(String playerName, boolean AI)
	{
		if (AI) { //the new player has to be either an AI or a human
			players.add(new AI(playerName));
		}
		else {
			players.add(new Human(playerName));
		}
	}

	/**
	 * Sets the current active player to the next player in the list of players, and sends the message that a turn change has occurred so it can be reflected in the view.
	 */
	protected void nextPlayer()
	{		
		int next = players.indexOf(activePlayer)+1;

		if (next > players.size()-1) { //we're at the end of the list
			next = 0; //so we go back to the start
		}
		activePlayer = players.get(next);
		relayMessage("msg-"+activePlayer.getName()+ "'s turn"); //show that the active player has changed, and the name of the new player
		relayMessage("turnChange");
	}

	/**
	 * Relays a message from objects outside of this class to the observers of this class. In particular used by AI to relay the messages it has to show the player.
	 * 
	 * The benefits of this method are twofold: setChanged is protected, so it would have to be overridden in this class. As well as this it condenses AI having to call both setChanged and notifyObservers, since instead they only need one call to this method.
	 * 
	 * @param message The message to be relayed to the observers of this class
	 */
	public void relayMessage(String message) 
	{
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Runs the client for netplay, by creating a new thread and a new instance of the server
	 * 
	 * @param IP the IP address of the server to connect to
	 * @param port the port on which to connect to on the server
	 */
	public void runClient(String IP, String port) {
		serverIp = IP;
		serverPort = Integer.parseInt(port);
		Thread t = new Thread(new Board.NetPlayClient());
		t.start();		
	}

	/**
	 * Runs the server for netplay, by creating a new thread and a new instance of the server
	 */
	public void runServer() {
		Thread t = new Thread(new Board.NetPlayServer());
		t.start();
	}

	/**
	 * Saves the board to a file, to be loaded later by the corresponding method.
	 * 
	 * @param fileName The filename for which to save the file with
	 */
	public void saveBoard(String fileName) 
	{
		{
			FileOutputStream fos = null; //get the outputStream ready for saving
			ObjectOutputStream out = null;
			try {
				fos = new FileOutputStream(fileName); //set the outputStream to use the file relating to the filename passed in
				out = new ObjectOutputStream(fos); //set the object stream to use the file stream
				out.writeObject(this); //and write the objects to the stream
				out.close();
				fos.close();
			} 
			catch (IOException e) { //if we get here, it means we were unable to save the file, most likely no permissions to save there, or the disk is full
				relayMessage("err-"+"Unable to save the file, is the disk full?");
			}
		}
	}

	/**
	 * Net Play Server, this runs alongside the main game once started, broadcasting to and receiving moves from the server on the other side.
	 * 
	 * Runs in it's own thread, and relies on some of the variables contained in Board to function
	 */
	private class NetPlayServer implements Runnable
	{
		@Override
		public void run() {

			ServerSocket serverSocket = null;

			try {
				serverSocket = new ServerSocket(4444); //default port for this has been chosen as 4444 since it's something that is not usually used
			} catch (IOException e) {
				relayMessage("err-"+"Couldn't listen on the port\nExiting..."); //this means we couldn't listen on the port, likely that something else is listening on that port
				System.exit(1);
			}
			Socket clientSocket = null; //setup the socket for the client
			try {
				clientSocket = serverSocket.accept(); //then assign it by connecting to this server's socket
			} catch (IOException e) { //if this fires, something indeterminate went wrong, could be a number of network problems
				relayMessage("err-"+"Problem connecting to client/nDo they have a firewall blocking communication?");
				System.exit(1);
			}
			setConnected(true); //if we got here, we're connected, so we set this to get rid of the loading screen and start up the game

			PrintWriter out = null; //get the printWriter ready so it can write messages to the client
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true); //connect the printWriter
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //setup the BufferedReader to receive input from the client

				String inbound = null; //setting up the placeholder variables so that we can check if messages sent and received are new or not
				String lastInbound = null;
				String outbound = null;
				String lastOutbound = null;
				
				out.println(outboundMessage); //sends the outbound message from the board, which at this point should denote the gametype of this game
				String clientGame = null;
				while (clientGame == null) { //get the gametype of the server
					clientGame = in.readLine();
				}
				if (!outboundMessage.equals(clientGame)) { //if our game isn't the same as the opponent's game, we need to exit
					relayMessage("Opponent not playing the same game!\nExiting...");
					System.exit(1);
				}
				setOutboundMessage(null); //if we get here the gametype is the same so we want to reset outbound message for it's true purpose of passing moves

				while (!gameFinished) { //while the game is still running, we want to keep broadcasting and receiving
					while (outbound == lastOutbound) { //while the outbound message hasn't changed since we last received a different one
						outbound = outboundMessage; //we set it to the message from the board
						Thread.sleep(3000); //and wait a few seconds so we aren't constantly assigning the same variable
					}
					lastOutbound = outbound; //then we set the placeholder so we can check it next time we're sending a message
					out.println(outbound); //and send the message to the client, so that it can take a move from it
					while (inbound == lastInbound) { //while the inbound message is the same as the last one we received
						inbound = in.readLine(); //get the message from the client
						Thread.sleep(3000); //and wait a few seconds so we aren't constantly polling the client
					}
					setInbound(inbound); //set the message in Board so it can be parsed
					lastInbound = inbound; //and set the placeholder message so that we can check next time we receive a message
				}
				out.close(); //now the game is over, we have no reason for the following, so we close them all
				in.close();
				clientSocket.close();
				serverSocket.close();

			} catch (IOException e) { //if this fires we lost connection, so tell the user as such
				relayMessage("err-"+"Connection lost!");
			} catch (InterruptedException e) {
				relayMessage("err-"+"Something went wrong!\nLost connection!");//this should never fire, so we don't really have anything to tell the user if it does
			}
		}
	}

	/**
	 * Net Play Client, this runs alongside the main game once started, broadcasting to and receiving moves from the server on the other side.
	 * 
	 * Runs in it's own thread, and relies on some of the variables contained in Board to function
	 */
	private class NetPlayClient implements Runnable
	{
		@Override
		public void run() {

			Socket socket = null; //set up the socket so we can connect
			PrintWriter out = null;
			BufferedReader in = null;

			try {
				socket = new Socket(serverIp,serverPort); //get the server IP and port number from the Board, which in turn should have been set by the user through the menu
				out = new PrintWriter(socket.getOutputStream(), true); //set the printWriter to write to the server
				in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //set the BufferedReader to receive messages from the server
			} catch (UnknownHostException e) { //this means that the IP is probably incorrect
				relayMessage("err-"+"Couldn't find host: "+serverIp+"\nExiting...");
				System.exit(1); //because we're in another thread, we don't have access to draw the main menu, as such we need to exit since there's not a good way to recover from this
			} catch (IOException e) { //this means we found the IP, but something else went wrong, such as the server wasn't running
				relayMessage("err-"+"Couldn't connect to the port on host: "+serverIp+"\nExiting...");
				System.exit(1);
			}
			setConnected(true); //if we get here, we found the server and we are now connected to them

			String inbound = null; //setup placeholder variables so we can check the value of the incoming and outgoing messages
			String lastInbound = null;
			String outbound = null;
			String lastOutbound = null;
			
			out.println(outboundMessage); //sends the outbound message from the board, which at this point should denote the gametype of this game
			String serverGame = null;
			while (serverGame == null) { //get the gametype of the server
				try {
					serverGame = in.readLine();
				} catch (IOException e) {
					relayMessage("err-"+"Connection lost!"); //account for the possibility of the server closing before we can get the gametype
				}
			}
			if (!outboundMessage.equals(serverGame)) { //if our game isn't the same as the opponent's game, we need to exit
				relayMessage("Opponent not playing the same game!\nExiting...");
				System.exit(1);
			}
			setOutboundMessage(null); //if we get here the gametype is the same so we want to reset outbound message for it's true purpose of passing moves

			try {			
				while (!gameFinished) { //until the game ends, we want to keep broadcasting and receiving depending on whose turn it is

					while (inbound == lastInbound) { //because we are the client, we receive first
						inbound = in.readLine();
						Thread.sleep(3000); //break up the checks by waiting three seconds, so that we aren't constantly checking
					}
					setInbound(inbound); //now that the inbound message is a new one, we set it in the board so we can then parse it to create a move
					lastInbound = inbound; //also set the placeholder value for next time we're the client
					while (outbound == lastOutbound) { //this should be true until we have made a move
						outbound = outboundMessage;
						Thread.sleep(3000); //wait so that we aren't constantly assigning the same variable
					}
					lastOutbound = outbound; //set the placeholder for next time we're the server
					out.println(outbound); //send the move to the server
				}
				out.close(); //we don't need the connection any more, since the game is over, so we close everything to be responsible
				in.close();
				socket.close();
			} catch (IOException e) {
				relayMessage("err-"+"Connection lost!");
			} catch (InterruptedException e) {
			}
		}
	}
}