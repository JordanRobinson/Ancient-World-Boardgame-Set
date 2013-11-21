/**
 *  Menu Class
 *  The class in which the initial menu and submenu code is located, also should be the first thing to run since it creates instances of the other classes required to play the game.
 * 
 * @package core.view
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.1.1 19 Apr 2012
 */

package core.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import mancala.model.MancalaBoard;
import mancala.view.MancalaGameView;
import senet.model.SenetBoard;
import senet.view.SenetGameView;
import checkers.model.CheckersBoard;
import checkers.view.CheckersGameView;
import core.model.Board;
import core.model.Game;
import core.model.Resources;

@SuppressWarnings("serial") //This class should never be saved, as it is a GUI component or similar
public class Menu extends AbstractView {

	//variable assignment here
	private JButton mancalaButton; //all the following are graphical components required for the menus
	private JButton senetButton;
	private JButton chineseCheckersButton;
	private JButton exitButton;
	private JButton newGameButton;
	private JButton loadGameButton;
	private JButton netPlayButton;
	private JButton optionsButton;
	private JButton backButton;
	private JButton saveOptions;
	private JButton becomeServerButton;
	private JButton joinServerButton;

	private JComboBox playerOneTypeComboBox;	
	private JComboBox playerTwoTypeComboBox;
	private JComboBox playerThreeTypeComboBox;
	private JComboBox playerFourTypeComboBox;
	private JComboBox playerFiveTypeComboBox;
	private JComboBox playerSixTypeComboBox;

	private JFormattedTextField playerOneNameTextField;
	private JFormattedTextField playerTwoNameTextField;
	private JFormattedTextField playerThreeNameTextField;
	private JFormattedTextField playerFourNameTextField;
	private JFormattedTextField playerFiveNameTextField;
	private JFormattedTextField playerSixNameTextField;
	private JFormattedTextField iPTextField;
	private JFormattedTextField portTextField;

	private JComboBox playerOneDifficultyComboBox;
	private JComboBox playerTwoDifficultyComboBox;
	private JComboBox playerThreeDifficultyComboBox;
	private JComboBox playerFourDifficultyComboBox;
	private JComboBox playerFiveDifficultyComboBox;
	private JComboBox playerSixDifficultyComboBox;

	private Game game; //enum relating to the game selected by the user, for use by a few of the menus to see what game has been picked

	//constructors here
	public Menu()
	{
		//variables passed in

		//variables not passed in
		mancalaButton = new JButton("Mancala"); //setup the buttons for the various menus
		senetButton = new JButton("Senet");
		chineseCheckersButton = new JButton("Chinese Checkers");
		exitButton = new JButton("Exit");
		newGameButton = new JButton("New Game");
		netPlayButton = new JButton("Network Play");
		loadGameButton = new JButton("Load Game");
		optionsButton = new JButton("Options");
		backButton = new JButton("Back");
		saveOptions = new JButton("Save");

		becomeServerButton = new JButton("Create Server");
		joinServerButton = new JButton("Join Server");

		mancalaButton.addActionListener(new MancalaPressed()); //add the listeners to the buttons so that they perform the correct actions
		senetButton.addActionListener(new SenetPressed());
		chineseCheckersButton.addActionListener(new ChineseCheckersPressed());
		exitButton.addActionListener(new ExitPressed());
		newGameButton.addActionListener(new NewGamePressed());
		netPlayButton.addActionListener(new NetPlayPressed());
		loadGameButton.addActionListener(new LoadGamePressed());
		optionsButton.addActionListener(new OptionsPressed());
		backButton.addActionListener(new BackPressed());
		saveOptions.addActionListener(new SaveOptionsPressed());
		becomeServerButton.addActionListener(new BecomeServerPressed());
		joinServerButton.addActionListener(new JoinServerPressed());

		Resources.loadProperties(); //loads in the properties file, to set any preferences changed by the user at an earlier date

		draw(); //drawing the initial menu window
	}

	//gets here
	//sets here
	public void setGame(Game game)
	{
		this.game = game;
	}
	//methods here

	/**
	 * This draws the initial gui menu.
	 */
	protected void draw()
	{
		super.draw(); //calls the superclass method which resets the frame to a usable state

		topPanel.setLayout(new GridLayout(1,1)); //columns, rows
		JLabel picLabel = new JLabel(new ImageIcon((BufferedImage)Resources.load("src/resources/logo.png", "image"))); //set the main image of the menu
		topPanel.add(picLabel); //add the main image to the top panel of the frame

		botPanel.setLayout(new GridLayout(2,3,15,10)); //columns, rows, hgap, vgap
		botPanel.add(mancalaButton); //add on the required buttons
		botPanel.add(senetButton);
		botPanel.add(chineseCheckersButton);
		botPanel.add(new JPanel()); //spacer for bottom left of gridLayout
		botPanel.add(exitButton);
		botPanel.add(new JPanel()); //spacer for bottom right of gridLayout

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(botPanel, BorderLayout.SOUTH);

		frame.setSize(new Dimension(600,400));
		frame.setLocationRelativeTo(null); //centres window
		frame.setVisible(true);
	}

	/**
	 * Draws the options menu relating to checkers, allowing the user to set their preferences
	 */
	private void drawCheckersOptionsMenu()
	{
		frame.setVisible(false); //hide the current menu
		drawOptionsMenu(); //perform the tasks required of all the options menus

		topPanel.setLayout(new GridLayout(11,2,15,10)); //rows, columns, hgap, vgap

		String[] playerOptions = {"Human","AI","Off"}; //note that this allows the user to turn off a player, which is something unique to chinese checkers compared to the other games
		String[] difficultyOptions = {"Easy","Medium"}; //the level of difficulty for an AI player

		JLabel playerThreeOptionsLabel = new JLabel("Player Three");
		playerThreeOptionsLabel.setVerticalAlignment(JLabel.CENTER);
		playerThreeOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerThreeTypeComboBox = new JComboBox(playerOptions);
		
		JLabel playerThreeNameLabel = new JLabel("Name");
		playerThreeNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerThreeNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerThreeNameTextField = new JFormattedTextField();
		playerThreeNameTextField.setColumns(10);

		JLabel playerThreeDifficultyLabel = new JLabel("Difficulty: ");
		playerThreeDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerThreeDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerThreeDifficultyComboBox = new JComboBox(difficultyOptions);
		playerThreeDifficultyComboBox.setSelectedIndex(0);
		
		JLabel playerFourOptionsLabel = new JLabel("Player Four");
		playerFourOptionsLabel.setVerticalAlignment(JLabel.CENTER);
		playerFourOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFourTypeComboBox = new JComboBox(playerOptions);
		
		JLabel playerFourNameLabel = new JLabel("Name");
		playerFourNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerFourNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFourNameTextField = new JFormattedTextField();
		playerFourNameTextField.setColumns(10);

		JLabel playerFourDifficultyLabel = new JLabel("Difficulty: ");
		playerFourDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerFourDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFourDifficultyComboBox = new JComboBox(difficultyOptions);
		playerFourDifficultyComboBox.setSelectedIndex(0);

		JLabel playerFiveOptionsLabel = new JLabel("Player Five");
		playerFiveOptionsLabel.setVerticalAlignment(JLabel.CENTER);
		playerFiveOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFiveTypeComboBox = new JComboBox(playerOptions);

		JLabel playerFiveNameLabel = new JLabel("Name");
		playerFiveNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerFiveNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFiveNameTextField = new JFormattedTextField();
		playerFiveNameTextField.setColumns(10);

		JLabel playerFiveDifficultyLabel = new JLabel("Difficulty: ");
		playerFiveDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerFiveDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerFiveDifficultyComboBox = new JComboBox(difficultyOptions);
		playerFiveDifficultyComboBox.setSelectedIndex(0);
		
		JLabel playerSixOptionsLabel = new JLabel("Player Six");
		playerSixOptionsLabel.setVerticalAlignment(JLabel.CENTER);
		playerSixOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerSixTypeComboBox = new JComboBox(playerOptions);
		
		JLabel playerSixNameLabel = new JLabel("Name");
		playerSixNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerSixNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerSixNameTextField = new JFormattedTextField();
		playerSixNameTextField.setColumns(10);

		JLabel playerSixDifficultyLabel = new JLabel("Difficulty: ");
		playerSixDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerSixDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerSixDifficultyComboBox = new JComboBox(difficultyOptions);
		playerSixDifficultyComboBox.setSelectedIndex(0);

		topPanel.add(playerThreeOptionsLabel);
		topPanel.add(playerThreeTypeComboBox);
		topPanel.add(playerThreeNameLabel);
		topPanel.add(playerThreeNameTextField);
		topPanel.add(playerThreeDifficultyLabel);
		topPanel.add(playerThreeDifficultyComboBox);
		
		topPanel.add(playerFourOptionsLabel);
		topPanel.add(playerFourTypeComboBox);
		topPanel.add(playerFourNameLabel);
		topPanel.add(playerFourNameTextField);
		topPanel.add(playerFourDifficultyLabel);
		topPanel.add(playerFourDifficultyComboBox);

		topPanel.add(playerFiveOptionsLabel);
		topPanel.add(playerFiveTypeComboBox);
		topPanel.add(playerFiveNameLabel);
		topPanel.add(playerFiveNameTextField);
		topPanel.add(playerFiveDifficultyLabel);
		topPanel.add(playerFiveDifficultyComboBox);

		topPanel.add(playerSixOptionsLabel);
		topPanel.add(playerSixTypeComboBox);
		topPanel.add(playerSixNameLabel);
		topPanel.add(playerSixNameTextField);
		topPanel.add(playerSixDifficultyLabel);
		topPanel.add(playerSixDifficultyComboBox);

		topPanel.add(new JLabel());
		topPanel.add(saveOptions);
		topPanel.add(new JLabel());
		topPanel.add(new JLabel());
		topPanel.add(backButton);
		
		topPanel.setLayout(new GridLayout(7,9,15,20));

		playerOneTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerOne.type"))); //getting and setting the default values for the options from the properties file
		playerTwoTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerTwo.type")));
		playerThreeTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerThree.type")));
		playerFourTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerFour.type")));
		playerFiveTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerFive.type")));
		playerSixTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerSix.type")));

		playerOneDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerOne.difficulty")));
		playerTwoDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerTwo.difficulty")));
		playerThreeDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerThree.difficulty")));
		playerFourDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerFour.difficulty")));
		playerFiveDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerFive.difficulty")));
		playerSixDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("checkers.playerSix.difficulty")));

		playerThreeNameTextField.setText(System.getProperty("playerThree.name"));
		playerFourNameTextField.setText(System.getProperty("playerFour.name"));
		playerFiveNameTextField.setText(System.getProperty("playerFive.name"));
		playerSixNameTextField.setText(System.getProperty("playerSix.name"));
		
		frame.setSize(new Dimension(600,500));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);
	}

	/**
	 * Draws a loading screen to show the user that the program is waiting on an operation such as connecting to the client
	 * 
	 * @param loadingMessage The message to show the user on the loading screen
	 */
	private void drawLoadingScreen(String loadingMessage)
	{
		frame.setVisible(false);
		super.draw();
		topPanel.add(new JLabel("<html>Please wait while the client connects<br />" + loadingMessage+"</html>")); //note the html to break up the text into two lines

		ImageIcon loading = (ImageIcon) Resources.load("src/resources/loading.gif", "imageIcon"); //set up a new image for a loading animation
		JLabel picLabel = new JLabel();
		picLabel.setIcon(loading); //set the image to the label
		topPanel.add(picLabel); //and then add the label to the panel
		frame.add(topPanel); //finally adding the panel to the frame

		frame.setSize(new Dimension(450,80));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);
	}

	/**
	 * Draws the options menu relating to mancala, allowing the user to set their preferences
	 */
	private void drawMancalaOptionsMenu()
	{
		frame.setVisible(false); //hide the current menu
		drawOptionsMenu(); //perform the tasks required of all the options menus

		topPanel.add(saveOptions);
		topPanel.add(backButton);

		playerOneTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("mancala.playerOne.type"))); //getting and setting the default values for the options from the properties file
		playerTwoTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("mancala.playerTwo.type")));

		playerOneDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("mancala.playerOne.difficulty")));
		playerOneDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("mancala.playerTwo.difficulty")));

		frame.setSize(new Dimension(300,400));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);
	}

	/**
	 * Draws the options relating to network play, such as whether to act as a server or client.
	 */
	private void drawNetPlayOptions()
	{
		frame.setVisible(false); //hide the previous menu
		super.draw(); //reset the gui so we can reuse it

		JLabel netHeader = new JLabel("Network Play");
		netHeader.setVerticalAlignment(JLabel.CENTER); //formatting the header
		netHeader.setHorizontalAlignment(JLabel.CENTER);
		netHeader.setPreferredSize(new Dimension(100,50)); //since this is the main heading of the window, we want it spaced nicely

		JLabel iPLabel = new JLabel("Server IP:");
		JLabel portLabel = new JLabel("Port:");

		iPTextField = new JFormattedTextField("127.0.0.1"); //default value of localhost
		portTextField = new JFormattedTextField("4444"); //default arbitrary port, picked one that is likely to not be in use

		topPanel.add(iPLabel); //add all the components to the panel
		topPanel.add(iPTextField);
		topPanel.add(portLabel);
		topPanel.add(portTextField);
		topPanel.add(joinServerButton);
		topPanel.add(becomeServerButton);
		topPanel.add(new JLabel());
		topPanel.add(new JLabel());
		topPanel.add(new JLabel());
		frame.add(topPanel); //and add the panel to the frame

		iPTextField.setColumns(15); //this sets the size of the text boxes
		portTextField.setColumns(6);

		frame.setSize(new Dimension(500,100));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);		
	}

	/**
	 * Draws the options menu.
	 * 
	 * Calls each specific game's options menu after doing some standard things required by all of them.
	 * 
	 * Should not be called on its own, but instead called by a specific game's options menu, which should then add any needed sections.
	 */
	private void drawOptionsMenu()
	{
		super.draw(); //reset the gui so it can be reused

		JLabel options = new JLabel("Options"); //setting the header
		options.setVerticalAlignment(JLabel.CENTER); //formatting the header
		options.setHorizontalAlignment(JLabel.CENTER);
		options.setPreferredSize(new Dimension(100,50)); //since this is the main heading of the window, we want it spaced nicely

		String[] playerOptions = {"Human","AI"}; //options for the type of player. Note that because this only relates to things all the games can do, in this section there is no choice to turn off a player, since this can only be done in chinese checkers
		String[] difficultyOptions = {"Easy","Medium"}; //the level of difficulty for an AI player

		JLabel playerOneOptionsLabel = new JLabel("Player One"); //descriptive label for the user to see what the control does
		playerOneOptionsLabel.setVerticalAlignment(JLabel.CENTER); //formatting the descriptive label
		playerOneOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerOneTypeComboBox = new JComboBox(playerOptions); //setting the comboBox up to use the playerOptions
		playerOneTypeComboBox.setSelectedIndex(0); //setting the default (though this should later be changed to reflect what's in the properties file) 

		JLabel playerOneNameLabel = new JLabel("Name");
		playerOneNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerOneNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerOneNameTextField = new JFormattedTextField(); //for setting the name of the relevant player
		playerOneNameTextField.setColumns(10); //sets the size of the text box

		JLabel playerOneDifficultyLabel = new JLabel("Difficulty: ");
		playerOneDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerOneDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerOneDifficultyComboBox = new JComboBox(difficultyOptions); //for setting the difficulty if the player is an AI
		playerOneDifficultyComboBox.setSelectedIndex(0);

		JLabel playerTwoOptionsLabel = new JLabel("Player Two");
		playerTwoOptionsLabel.setVerticalAlignment(JLabel.CENTER);
		playerTwoOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		playerTwoTypeComboBox = new JComboBox(playerOptions);
		playerTwoTypeComboBox.setSelectedIndex(1);

		JLabel playerTwoNameLabel = new JLabel("Name");
		playerTwoNameLabel.setVerticalAlignment(JLabel.CENTER);
		playerTwoNameLabel.setHorizontalAlignment(JLabel.CENTER);
		playerTwoNameTextField = new JFormattedTextField();
		playerTwoNameTextField.setColumns(10);

		JLabel playerTwoDifficultyLabel = new JLabel("Difficulty: ");
		playerTwoDifficultyLabel.setVerticalAlignment(JLabel.CENTER);
		playerTwoDifficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		playerTwoDifficultyComboBox = new JComboBox(difficultyOptions);
		playerTwoDifficultyComboBox.setSelectedIndex(0);

		midPanel.setLayout(new GridLayout(2,1,30,20));

		topPanel.setLayout(new GridLayout(7,2,15,20)); //rows, columns, hgap, vgap
		topPanel.add(playerOneOptionsLabel); //add all the components that are there for all options menus
		topPanel.add(playerOneTypeComboBox);
		topPanel.add(playerOneNameLabel);
		topPanel.add(playerOneNameTextField);
		topPanel.add(playerOneDifficultyLabel);
		topPanel.add(playerOneDifficultyComboBox);
		topPanel.add(playerTwoOptionsLabel);
		topPanel.add(playerTwoTypeComboBox);
		topPanel.add(playerTwoNameLabel);
		topPanel.add(playerTwoNameTextField);
		topPanel.add(playerTwoDifficultyLabel);
		topPanel.add(playerTwoDifficultyComboBox);

		frame.setLayout(new BorderLayout());
		frame.add(options, BorderLayout.NORTH);
		frame.add(topPanel, BorderLayout.CENTER);

		playerOneNameTextField.setText(System.getProperty("playerOne.name"));
		playerTwoNameTextField.setText(System.getProperty("playerTwo.name"));

	}

	/**
	 * Draws the options menu relating to checkers, allowing the user to set their preferences
	 */
	private void drawSenetOptionsMenu()
	{
		frame.setVisible(false); //hide the current menu
		drawOptionsMenu(); //perform the tasks required of all the options menus

		playerOneTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("senet.playerOne.type"))); //getting and setting the default values for the options from the properties file
		playerTwoTypeComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("senet.playerTwo.type")));

		playerOneDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("senet.playerOne.difficulty")));
		playerOneDifficultyComboBox.setSelectedIndex(Integer.parseInt(System.getProperty("senet.playerTwo.difficulty")));

		topPanel.add(saveOptions);
		topPanel.add(backButton);

		frame.setSize(new Dimension(300,400));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);
	}

	/**
	 * Takes the game decided upon by using the enum, and from there does the required drawing to draw the game's submenu
	 */
	private void gameButtonPressed() 
	{
		frame.setVisible(false); //hide the old menu
		super.draw(); //reset the gui
		lPanel.setLayout(new GridLayout(1,1)); //columns, rows
		JLabel picLabel = null; //this should contain the logo for the game selected, and is set below

		if (game == null) {
			System.err.println("game has not been selected"); //this should never happen, but is here just in case
			draw(); //this should allow the system to recover reasonably gracefully
			return;
		}

		switch (game) { //setting the picLabel to the correct game's image
		case SENET:
			picLabel = new JLabel(new ImageIcon((BufferedImage)Resources.load("src/resources/senetLogo.png", "image")));
			break;
		case CHECKERS:
			picLabel = new JLabel(new ImageIcon((BufferedImage)Resources.load("src/resources/chineseCheckersLogo.png", "image")));
			break;
		case MANCALA:
			picLabel = new JLabel(new ImageIcon((BufferedImage)Resources.load("src/resources/mancalaLogo.png", "image")));
			break;
		default:
			displayError("Game not recognised.\nAre you using a newer versioned save file?"); //this should never happen, but is here just in case
			draw(); //this should allow the system to recover reasonably gracefully
			break;
		}

		lPanel.add(picLabel); //now that the image is set for this, add it to the panel

		rPanel.setLayout(new GridLayout(5,1,15,20)); //columns, rows, hgap, vgap
		rPanel.add(newGameButton);
		rPanel.add(loadGameButton);
		rPanel.add(netPlayButton);
		rPanel.add(optionsButton);
		rPanel.add(backButton);

		//all the subcomponents should be ready, so set the frame up
		frame.add(lPanel, BorderLayout.WEST);
		frame.add(rPanel, BorderLayout.EAST);			
		frame.setSize(new Dimension(300,300));
		frame.pack();
		frame.setLocationRelativeTo(null); //centres window
		frame.setVisible(true); //and display it
	}

	/**
	 * Should be called when loading a saved game.
	 * 
	 */
	private void loadGame()
	{
		frame.setVisible(false); //hide the menu since it's no longer needed.

		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".cch, .mnc, and .snt files", new String[] {"cch","mnc","snt"});
		fc.setFileFilter(filter);


		int result = fc.showOpenDialog(null);
		String fileName = null;
		String path = null;
		if(result == JFileChooser.APPROVE_OPTION)
		{
			fileName = fc.getSelectedFile().toString();
			path = fc.getSelectedFile().getAbsolutePath();

		}

		FileInputStream fis = null;
		ObjectInputStream in = null;
		Board savedBoard = null;

		if (fileName == null) { //the user cancelled the file dialog
			draw();
			return;
		}

		try {
			//loading in the board
			fis = new FileInputStream(path);
			in = new ObjectInputStream(fis);
			savedBoard = (Board)in.readObject();
			in.close();
		} catch (IOException e) {
			displayError("There seems to be a problem with the save file.");
			draw();
			return;
		} catch (ClassNotFoundException e) {
			displayError("The save file seems to be corrupted, or using an older version.");
			draw();
			return;
		}

		try {
			if (fileName.substring(fileName.length()-3, fileName.length()).equals("snt")) {
				new SenetGameView(savedBoard);
			}
			else if (fileName.substring(fileName.length()-3, fileName.length()).equals("mnc")) {
				new MancalaGameView(savedBoard);
			}
			else if (fileName.substring(fileName.length()-3, fileName.length()).equals("cch")) {
				new CheckersGameView(savedBoard);
			}
			else {
				displayError("Game doesn't seem to have been selected, returning to main menu."); //in theory, the program should never get to here, but this error handling code is here just in case something particularly strange happens
				draw(); //should allow the program to recover
				return;
			}
		} catch (ClassCastException e) {
			displayError("Problem with game save, returning to main menu."); //in theory, the program should never get to here, but this error handling code is here just in case something particularly strange happens
			draw(); //should allow the program to recover
			return;
		}
	}

	/**
	 * To be called when a new game is to be started, simply checks the game field of this class, and then calls the relevant constructor as a result.
	 */
	public void newGame() //Note this is changed from the diagram, since someone thought it would be a fantastic idea to have a method simply called new. Which of course is impossible.
	{
		frame.setVisible(false); //hide the menu since it's no longer needed.

		if (game.equals(Game.SENET)) {
			new SenetGameView();
		}
		else if (game.equals(Game.MANCALA)) {
			new MancalaGameView();
		}
		else if (game.equals(Game.CHECKERS)) {
			new CheckersGameView();
		}
		else {
			displayError("Game doesn't seem to have been selected, returning to main menu."); //in theory, the program should never get to here, but this error handling code is here just in case something particularly strange happens
			draw(); //should allow the program to recover
			return;
		}
	}

	/**
	 * ActionListener for the Back button of the options submenu
	 */
	private class BackPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false); //hide the current gui
			draw(); //redraw the main menu
		}
	}

	/**
	 * ActionListener for the Become Server button of the netPlay submenu
	 */
	private class BecomeServerPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			drawLoadingScreen("Give the other player the following information: " + iPTextField.getText() + ":" + portTextField.getText()); //let the player know that we're waiting for the client to connect
			SwingWorker<?,?> worker = new SwingWorker<Void,Void>(){  //note the capitalized V in Void, this is a workaround and not an error
				@Override 
				protected Void doInBackground() throws InterruptedException{	

					if (game == Game.CHECKERS) { 
						CheckersBoard board = new CheckersBoard(); //create a new board						
						board.setOutboundMessage("checkers");
						board.runServer(); //then use it to run the server thread, which should connect to the client and broadcast and receive moves
						board.setLocalPlayer(board.getPlayers().get(0)); //we set the local player, to denote who goes first

						while (!board.getConnected()) {} //while not connected, we wait

						frame.setVisible(false);
						new CheckersGameView(board);
					}
					else if (game == Game.SENET) {
						SenetBoard board = new SenetBoard();
						board.setOutboundMessage("senet");
						board.runServer();
						board.setLocalPlayer(board.getPlayers().get(0));

						while (!board.getConnected()) {} //while not connected, we wait

						frame.setVisible(false);
						new SenetGameView(board);
					}
					else if (game == Game.MANCALA) {
						MancalaBoard board = new MancalaBoard();
						board.setOutboundMessage("mancala");
						board.runServer();
						board.setLocalPlayer(board.getPlayers().get(0));

						while (!board.getConnected()) {} //while not connected, we wait

						frame.setVisible(false);
						new MancalaGameView(board);
					}
					return null;
				}
			};
			worker.execute(); //run the swingWorker, to 
		}
	}

	/**
	 * ActionListener for if the selection of Chinese Checkers is made on the main menu.
	 */
	private class ChineseCheckersPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			game = Game.CHECKERS;
			gameButtonPressed();
		}
	}

	/**
	 * ActionListener for the Exit button of the game submenu
	 */
	private class ExitPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * ActionListener for the Join Server button of the netPlay submenu
	 */
	private class JoinServerPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game == Game.CHECKERS) { 				
				CheckersBoard board = new CheckersBoard();
				board.setOutboundMessage("checkers"); //the initial outbound message is to check that both the client and server are playing the same game
				board.runClient(iPTextField.getText(), portTextField.getText()); //start the thread for the dedicated client, using the IP and port from the menu
				board.setLocalPlayer(board.getPlayers().get(1)); //set the local player to the second player
				frame.setVisible(false); //hide this frame
				new CheckersGameView(board); //so that we can draw the game view
			}
			else if (game == Game.SENET) {
				SenetBoard board = new SenetBoard();
				board.setOutboundMessage("senet");
				board.runClient(iPTextField.getText(), portTextField.getText());
				board.setLocalPlayer(board.getPlayers().get(1));
				frame.setVisible(false);
				new SenetGameView(board);
			}
			else if (game == Game.MANCALA) {
				MancalaBoard board = new MancalaBoard();
				board.setOutboundMessage("mancala");
				board.runClient(iPTextField.getText(), portTextField.getText());
				board.setLocalPlayer(board.getPlayers().get(1));
				frame.setVisible(false);
				new MancalaGameView(board);
			}
		}
	}

	/**
	 * ActionListener for Load Game button of the game submenu
	 */
	private class LoadGamePressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			loadGame();
		}
	}

	/**
	 * ActionListener for if the selection of Mancala is made on the main menu.
	 */
	private class MancalaPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			game = Game.MANCALA;
			gameButtonPressed();
		}
	}

	/**
	 * ActionListener for the Net Play button of the game submenu
	 */
	private class NetPlayPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			drawNetPlayOptions();
		}
	}

	/**
	 * ActionListener for the New Game button of the game submenu
	 */
	private class NewGamePressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) { //the new game button has been pressed
			newGame(); //so we start a new game
		}
	}

	/**
	 * ActionListener for the Options button of the game submenu
	 */
	private class OptionsPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game == Game.CHECKERS) { //checks which game has been selected and draws the relevant option menu
				drawCheckersOptionsMenu();
			}
			else if (game == Game.SENET) {
				drawSenetOptionsMenu();
			}
			else if (game == Game.MANCALA) {
				drawMancalaOptionsMenu();
			}
		}
	}

	/**
	 * ActionListener for the Save Game button of the options submenu
	 */
	private class SaveOptionsPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (game) { //saving any changed options to the currently loaded properties file
			case CHECKERS:
				System.setProperty("playerOne.name", playerOneNameTextField.getText());
				System.setProperty("playerTwo.name", playerTwoNameTextField.getText());
				System.setProperty("playerThree.name", playerThreeNameTextField.getText());
				System.setProperty("playerFour.name", playerFourNameTextField.getText());
				System.setProperty("playerFive.name", playerFiveNameTextField.getText());
				System.setProperty("playerSix.name", playerSixNameTextField.getText());
				System.setProperty("checkers.playerOne.type", playerOneTypeComboBox.getSelectedIndex()+""); //saving the option from what is reflected in the comboBox, converting to string using the +"" 
				System.setProperty("checkers.playerTwo.type", playerTwoTypeComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerThree.type", playerThreeTypeComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerFour.type", playerFourTypeComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerFive.type", playerFiveTypeComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerSix.type", playerSixTypeComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerOne.difficulty", playerOneDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerTwo.difficulty", playerTwoDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerThree.difficulty", playerThreeDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerFour.difficulty", playerFourDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerFive.difficulty", playerFiveDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("checkers.playerSix.difficulty", playerSixDifficultyComboBox.getSelectedIndex()+"");
				break;
			case SENET:
				System.setProperty("playerOne.name", playerOneNameTextField.getText());
				System.setProperty("playerTwo.name", playerTwoNameTextField.getText());
				System.setProperty("senet.playerOne.type", playerOneTypeComboBox.getSelectedIndex()+"");
				System.setProperty("senet.playerTwo.type", playerTwoTypeComboBox.getSelectedIndex()+"");
				System.setProperty("senet.playerOne.difficulty", playerOneDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("senet.playerTwo.difficulty", playerTwoDifficultyComboBox.getSelectedIndex()+"");
				break;
			case MANCALA:
				System.setProperty("playerOne.name", playerOneNameTextField.getText());
				System.setProperty("playerTwo.name", playerTwoNameTextField.getText());
				System.setProperty("mancala.playerOne.type", playerOneTypeComboBox.getSelectedIndex()+"");
				System.setProperty("mancala.playerTwo.type", playerTwoTypeComboBox.getSelectedIndex()+"");
				System.setProperty("mancala.playerOne.difficulty", playerOneDifficultyComboBox.getSelectedIndex()+"");
				System.setProperty("mancala.playerTwo.difficulty", playerTwoDifficultyComboBox.getSelectedIndex()+"");
				break;
			default:
				displayError("Seems to be a problem checking the game type\nRedrawing main menu...");
				draw();
				return;
			}

			File optionsFile = (File) Resources.load("src/resources/config.properties", "textFile"); 

			FileWriter writer; //writing the properties to a file
			try {
				writer = new FileWriter(optionsFile);
				System.getProperties().store(writer, "Saved by player"); //String parameter is the comment header
				writer.close();
			} catch (IOException ex) {
				displayError("Problems saving.\nAre you sure the disk isn't full?"); //calls the error displayer in the AbstractView, since something has gone wrong
			}

			frame.setVisible(false); //set this to invisible so we can show something else
			draw(); //go back to the main menu
		}
	}

	/**
	 * ActionListener for if the selection of Senet is made on the main menu.
	 */
	private class SenetPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			game = Game.SENET;
			gameButtonPressed();
		}
	}
}