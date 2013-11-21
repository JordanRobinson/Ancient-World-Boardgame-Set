/**
 *	SenetGameView
 *	Graphical representation of everything other than the board
 *
 * @package senet.view
 * @author Jordan Robinson, Ryan Carter
 * @version 16 Feb 2012
 */
package senet.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import senet.model.SenetBoard;
import senet.view.SenetBoardView;

import core.model.Board;
import core.model.Game;
import core.model.Resources;
import core.view.AbstractView;
import core.view.Menu;

@SuppressWarnings("serial") //This class should never be saved, as it is a GUI component or similar
public class SenetGameView extends AbstractView implements Observer{
	//variable assignment here
	private SenetBoardView boardView;
	private StickView stickView;
	private SenetBoard board;
	
	//constructors here
	/**
	 * Default constructor for SenetGameView.
	 */
	public SenetGameView()
	{
		//variables passed in

		//variables not passed in
		board = new SenetBoard();
		boardView = new SenetBoardView(board);
		stickView = new StickView();
		draw();		
	}
	
	/**
	 * Network play version of the constructor also used for loading games.
	 * 
	 * @param board the board to set for the game view
	 */
	public SenetGameView(Board board)
	{
		//variables passed in

		//variables not passed in
		SenetBoard senetBoard = (SenetBoard) board;
		this.board = senetBoard;
		boardView = new SenetBoardView(senetBoard);
		stickView = new StickView();
		draw();
	}
	//gets here

	//sets here

	//methods here
	/**
	 * Saves the board so a game can be resumed at a later time.
	 * 
	 * @param fileName the file name of the file to save
	 */
	protected void saveGame(String fileName)
	{
		board.saveBoard(fileName);
	}
	
	/**
	 * Draws the game view and sets up the menu items in the menu and assigns action listeners to them.
	 */
	public void draw()
	{
		super.draw();
		boardView.setPreferredSize(new Dimension(750,300));
		
		midPanel.add(boardView);
		botPanel.add(stickView);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(botPanel, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		
		if (board.getLocalPlayer() == null) {
			JMenuItem newGame = new JMenuItem("New Game");
			JMenuItem saveGame = new JMenuItem("Save Game");
			JMenuItem undo = new JMenuItem("Undo");
			JMenuItem hint = new JMenuItem("Hint");
			newGame.addActionListener(new NewGameButtonListener());
			saveGame.addActionListener(new SaveGameButtonListener());
			undo.addActionListener(new UndoButtonListener());
			hint.addActionListener(new HintButtonListener());
			menu.add(newGame);
			menu.add(saveGame);
			menu.add(undo);
			menu.add(hint);
		}
				
		JMenuItem quit = new JMenuItem("Quit");
		
		botPanel.addMouseListener(new ThrowSticksListener());

		quit.addActionListener(new QuitButtonListener());
		
		frame.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(quit);
		
		frame.pack();
		frame.setLocationRelativeTo(null); //centres window
		frame.setVisible(true);
	}
	
	/**
	 * Listener for the new game option of the JMenu.
	 */
	private class NewGameButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false); //hide the old game
			frame.dispose(); //and dispose of it
			Menu menu = new Menu(); //then create a new menu
			menu.setGame(Game.SENET); //so that we can set the game of the new menu
			menu.newGame(); //which allows us to call the new game method, which should in turn draw a new game
		}
	}
	
	/**
	 * Listener for the throw sticks panel.
	 */
	private class ThrowSticksListener extends MouseAdapter {
		public void mouseClicked(MouseEvent arg0) {
			if (!boardView.getBoard().getThrowTime()) {
				return;
			}
			boardView.getBoard().throwSticks();
			stickView.setSticks(boardView.getBoard().getSticksThrowValue());
			boardView.getBoard().setThrowTime(false);
			System.out.println("Throw sticks pressed, got: " + boardView.getBoard().getSticksThrowValue());
		}
	}
	
	/**
	 * Graphical view of the sticks, sets appropriate sticks dependent upon roll.
	 */
	private class StickView extends JComponent {
		private boolean stick1, stick2, stick3, stick4;
		private BufferedImage stickCut, stickBark;
		
		/**
		 * Default constructor for stickView.
		 */
		private StickView() {
			stickCut = (BufferedImage) Resources.load("src/resources/senetSticks-cut.png","image");
			stickBark = (BufferedImage) Resources.load("src/resources/senetSticks-bark.png","image");
			stick1=stick2=stick3=stick4 = false;
			setPreferredSize(new Dimension(stickCut.getWidth()*2, stickCut.getHeight()*2));
			setVisible(true);
		}
		
		/**
		 * Sets the appropriate stick images dependent upon the roll.
		 * 
		 * @param roll The roll value to set the stick images
		 */
		private void setSticks(int roll) {
			switch(roll) {
			case 1: stick1 = true; stick2 = false; stick3 = false; stick4 = false; break;
			case 2: stick1 = true; stick2 = true; stick3 = false; stick4 = false; break;
			case 3: stick1 = true; stick2 = true; stick3 = true; stick4 = false; break;
			case 4: stick1 = true; stick2 = true; stick3 = true; stick4 = true; break;
			case 5: stick1 = false; stick2 = false; stick3 = false; stick4 = false; break;
			}
			repaint();
		}
		
		/**
		 * Overrides the paintComponent in JComponent.
		 * 
		 * @param g The graphics object to paint on
		 */
		protected void paintComponent(Graphics g) {
			if(stick1==true) {g.drawImage(stickCut, 0, 0, null);}
			else {g.drawImage(stickBark, 0, 0, null);}
			if(stick2==true) {g.drawImage(stickCut, 375, 0, null);}
			else {g.drawImage(stickBark, 375, 0, null);}
			if(stick3==true) {g.drawImage(stickCut, 0, 40, null);}
			else {g.drawImage(stickBark, 0, 40, null);}
			if(stick4==true) {g.drawImage(stickCut, 375, 40, null);}
			else {g.drawImage(stickBark, 375, 40, null);}
			super.paintComponents(g);
		}
	}
	
	/**
	 * Listener for the undo option of the JMenu.
	 */
	private class UndoButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boardView.getBoard().getCommandInvoker().undo(); //undo using the command invoker
			boardView.repaint(); //then repaint, as the status of the model has changed
		}	
	}
	
	/**
	 * Listener for the hint option for the JMenu.
	 */
	private class HintButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boardView.drawHints();
		}	
	}

	/**
	 * Listener for the save game option of the JMenu.
	 */
	private class SaveGameButtonListener extends AbstractView.SaveGameButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Senet game save file", "snt"); //setting the filter and default file to show in the JFileChooser
			File defaultFile = new File("Senet.snt");
			
			super.setFilter(filter); //once we set these, the abstractView implementation should be able to do the rest
			super.setFile(defaultFile);
			super.actionPerformed(e);
		}
	}
}
