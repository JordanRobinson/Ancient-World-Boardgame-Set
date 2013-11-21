/**
 *  MancalaGameView Class
 *  Graphical component representing everything required of a graphical program not including the board itself. As such if necessary this should include menus, score etc.
 * 
 * @package checkers.view
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 15 Feb 2012
 */
package mancala.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import mancala.model.MancalaBoard;

import core.model.Board;
import core.model.Game;
import core.view.AbstractView;
import core.view.Menu;

@SuppressWarnings("serial") //This class should never be saved, as it is a GUI component or similar
public class MancalaGameView  extends AbstractView {
	
	//variable assignment here
	private MancalaBoardView boardView;
	private MancalaBoard board; //a field rather than anonymous so that saving and loading works
	
	//constructors here
	/**
	 * Default constructor for the game view, to be called when netplay is not enabled.
	 */
	public MancalaGameView()
	{	
		//variables passed in

		//variables not passed in
		this.board = new MancalaBoard(); //this is so that saving/loading works; by containing the board here we can set it to a file from here too
		boardView = new MancalaBoardView(board);
		draw();
	}
	
	/**
	 * NetPlay constructor for the game view, also used when loading games
	 */
	public MancalaGameView(Board board)
	{	
		//variables passed in

		//variables not passed in
		MancalaBoard mancalaBoard = (MancalaBoard) board; //because we set various things in the board for netplay, we need to use the same board rather than creating a new one
		this.board = mancalaBoard;
		boardView = new MancalaBoardView(this.board);
		draw();
	}

	//gets here
	//sets here
	//methods here
	/**
	 * Saves the game to a file, to be loaded later. Note that by overriding the abstractView class method, this should use the board in this subclass, rather than the generic one in the abstract view.
	 * 
	 * @param fileName The filename for the resulting file to save
	 */
	protected void saveGame(String fileName)
	{
		board.saveBoard(fileName);
	}
	/**
	 * Draws the game view, and sets parameters such as the size of the frame, as well as adding on the required menu options for this game
	 */
	protected void draw()
	{		
		super.draw(); //resetting the gui so it can be reused
		boardView.setPreferredSize(new Dimension(750,400)); //setting the frame size
		frame.add(boardView); //adding the board to the game view
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Game"); //Setting up the drop down menu		
		frame.setJMenuBar(menuBar);
		menuBar.add(menu);
		if (board.getLocalPlayer() == null) { //quick check to see if the game is networked or not
			JMenuItem newGame = new JMenuItem("New Game"); //if it is a local game, then these options should be available
			newGame.addActionListener(new NewGameButtonListener()); //but they cannot be used over the network, for obvious reasons
			menu.add(newGame);
			JMenuItem saveGame = new JMenuItem("Save Game");
			saveGame.addActionListener(new SaveGameButtonListener());
			menu.add(saveGame);
			JMenuItem undo = new JMenuItem("Undo");
			undo.addActionListener(new UndoButtonListener());
			menu.add(undo);
		}
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitButtonListener());
		menu.add(quit);
		
		frame.pack();
		frame.setLocationRelativeTo(null); //centres window
		frame.setVisible(true); //show the window, now that everything is ready
		
	}
	
	/**
	 * Listener for the undo option of the JMenu
	 */
	private class UndoButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boardView.getBoard().getCommandInvoker().undo(); //undo using the command invoker
			boardView.repaint(); //then repaint, as the status of the model has changed
		}	
	}
	
	/**
	 * Listener for the new game option of the JMenu
	 */
	private class NewGameButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false); //hide the old game
			frame.dispose(); //and dispose of it
			Menu menu = new Menu(); //then create a new menu
			menu.setGame(Game.MANCALA); //so that we can set the game of the new menu
			menu.newGame(); //which allows us to call the new game method, which should in turn draw a new game
		}
	}
	
	/**
	 * Listener for the save game option of the JMenu
	 */
	private class SaveGameButtonListener extends AbstractView.SaveGameButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Mancala game save file", "mnc"); //setting the filter and default file to show in the JFileChooser
			File defaultFile = new File("Mancala.mnc");
			
			super.setFilter(filter); //once we set these, the abstractView implementation should be able to do the rest
			super.setFile(defaultFile);
			super.actionPerformed(e);
		}
	}	
}