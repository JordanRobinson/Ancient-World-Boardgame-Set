/**
 *  AbstractView Class
 *  Abstract class containing methods used frequently by the graphical oriented classes.
 * 
 * @package core.view
 * @author Jordan Robinson, Ryan Carter
 * @version v3.0.1.1 26 Apr 2012
 */

package core.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.model.Board;
import core.model.Resources;

@SuppressWarnings("serial") //This class should never be saved, as it is a GUI component or similar
public abstract class AbstractView extends JPanel implements Observer {

	//variable assignment here
	protected JFrame frame;
	protected JPanel topPanel;
	protected JPanel botPanel;
	protected JPanel lPanel;
	protected JPanel rPanel;
	protected JPanel midPanel;
	protected BufferedImage background; //the image of the board
	private BufferedImage messageBox = (BufferedImage) Resources.load("src/resources/messageBox.png", "image"); //the messaging window used to display messages to the user
	private String message = " "; //the default message for the messageBox
	private Board board = null; //here so that the saveGame method can reference a board. When extended it should reference the correct overridden board.

	//constructors here
	//variables passed in
	//variables not passed in
	//gets here

	//sets here
	protected void setMessage(String message)
	{
		if (message.length() > 50) { //the message is too long, and would overflow if displayed on the messageBox
			return; //we don't want this to happen, so we return without doing anything
		}
		this.message = message;
	}

	//methods here
	/**
	 * Displays a prompt box with error details for the user.
	 * 
	 * Static since there is no benefit to it being instanced, and it also may need to be called by classes that do not have access to subclass of this class
	 * 
	 * @param errorMessage the message to display to the user
	 */
	public static void displayError(String errorMessage)
	{
		JOptionPane.showMessageDialog(new JFrame(), errorMessage);
	}

	/**
	 * Shows the user a message in the display box, to be used for general messages relayed from the board, or AIs
	 * 
	 * @param g The graphics object used to display the box and text
	 */
	public void displayMessage(Graphics g)
	{
		g.drawImage(messageBox, background.getWidth() - 310, background.getHeight() - 35, null); //drawing the background message box image
		g.setFont(new Font("Arial", Font.PLAIN, 15)); //setting the style of the text to make sure it fits correctly
		g.drawString(message, background.getWidth() - 300, background.getHeight() - 15); //draw the actual message onto the message box image
	}

	/**
	 * Performs the basic necessities needed for each redraw of any windowed component, such as setting the close operation.
	 */
	protected void draw()
	{
		frame = new JFrame("Ancient World Board Game Compendium");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = (Image) Resources.load("src/resources/icon.png", "image");
		frame.setIconImage(icon);

		topPanel = new JPanel();
		botPanel = new JPanel();
		lPanel = new JPanel();
		rPanel = new JPanel();
		midPanel = new JPanel();
	}

	/**
	 * Animation to denote the changing of players, shows the new player's name in the bottom left.
	 * 
	 * @param newPlayer The new player's name to show on the turn change screen
	 */
	protected void drawTurnChange(String newPlayer)
	{
		Graphics g = getGraphics();

		for (int i = 0; i < 15; i += 1) { //draws a translucent filter over the screen 15 times to give the impression of animation
			g.setColor(new Color(55, 55, 55, i));
			g.fillRect(0, 0, background.getWidth(), background.getHeight());

			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() < start+40) {} //waits 40 milliseconds, so that the animation doesn't complete too quickly
		}

		g.setFont(new Font("Arial", Font.PLAIN, 26)); //this and the below draws the new player's name in the bottom left of the screen, and styles it appropriately
		g.setColor(new Color(255,255,255));
		g.drawLine(0, (int) (background.getHeight()/1.5)+10, 350, (int) (background.getHeight()/1.5)+10);
		g.drawString(newPlayer+"'s turn", 20, (int) (background.getHeight()/1.5));

		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() < start+1700) {} //waits 1700 milliseconds, so that the player has enough time to read the new player's name

		paint(getGraphics()); //now that the animation has completed, we repaint the board to get rid of any effects left
	}

	/**
	 * Saves the game by calling the save game method in the relevant board
	 * 
	 * @param filename The filename to save the game under
	 */
	protected void saveGame(String fileName)
	{
		board.saveBoard(fileName);
	}

	/**
	 * Overrides update in Observer. Checks for any messages that the system could be sending, then displays them if necessary.
	 * 
	 * @param arg0 The object that is being observed
	 * @param arg1 The message passed from the observed object
	 */
	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg1 != null) { //we don't want to check if it's null, since that would mean there should just be a redraw, otherwise we want to check if the message starts with a prefix
			StringBuilder build = new StringBuilder((String) arg1); //so we create a stringBuilder to split up the string
			String messageCheck = build.substring(0, 4); //split it up to see the first section
			String message = build.substring(4, build.length()); //and then assign the rest to the message to send

			if (messageCheck.equals("msg-")) { //if this is true it's a normal message
				setMessage(message); //so we simply set it, which should then be picked up by the redraw
				return;
			}
			else if (messageCheck.equals("err-")) { //otherwise it's an error, so we show the user the message using a pop up box
				JOptionPane.showMessageDialog(frame,message,"An error has occurred",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * ActionListener for the save game JMenu button
	 * 
	 * Performs the action of saving the current game to a file, to be loaded at a later date
	 */
	public class SaveGameButtonListener implements ActionListener {

		FileNameExtensionFilter filter;
		File defaultFile;

		/**
		 * Sets the filter to be used for the fileChooser
		 * 
		 * @param filter The filter to be set
		 */
		protected void setFilter(FileNameExtensionFilter filter) {
			this.filter = filter;
		}

		/**
		 * Sets the default file to be used
		 * 
		 * @param defaultFile The default file to be set, this allows for a default value in the JFileChooser
		 */
		protected void setFile(File defaultFile) {
			this.defaultFile = defaultFile;
		}

		@Override
		public void actionPerformed(ActionEvent e) {		
			JFileChooser fc = new JFileChooser(){ //creating a new JFileChooser
				@Override
				public void approveSelection(){ //Overriding the default method so that we can have a confirm dialog in case of the possibility that the user tries to overwrite a file
					File f = getSelectedFile();
					if(f.exists() && getDialogType() == SAVE_DIALOG){
						int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
						switch(result){
						case JOptionPane.YES_OPTION:
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
						}
					}
					super.approveSelection();
				}
			};

			fc.setSelectedFile(defaultFile); //sets selected file so that the file chooser has a default value
			fc.setFileFilter(filter); //sets the filter so that only the correct type of file can be created
			int result = fc.showSaveDialog(null);
			String fileName = null;
			if(result == JFileChooser.APPROVE_OPTION)
			{
				fileName = fc.getSelectedFile().getAbsolutePath();
				saveGame(fileName); //note that this is in the if block, since otherwise there is the chance that it will be called without the filename being set
			}		
		}
	}

	/**
	 * ActionListener for the quit JMenu button
	 * 
	 * Performs the action of quitting the current game
	 */
	public class QuitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false); //hide the current frame
			new Menu(); //redraw the main menu
		}
	}
}