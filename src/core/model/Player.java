/**
 *  Player Class
 *  Generic class extended by the human and AI classes. Provides some methods to be used at points in which either an AI or a Human could be acceptable.
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 15 Feb 2012
 */

package core.model;

import java.io.Serializable;

@SuppressWarnings("serial") //Only the subclasses of this class should be saved, since this class should not be instantiated, and is only not abstract so that it can be used at various points such as the array of type player in board
public class Player implements Serializable {

	//variable assignment here
	protected String name; //the name of the player
	protected boolean isAI; //boolean representing whether the player is an AI or not

	//constructors here
	protected Player(String name) 
	{
		//variables passed in
		this.name = name;

		//variables not passed in
	}

	//gets here
	/**
	 * Returns the name of the player.
	 * 
	 * @returns The name of the player.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Method for determining whether or not a player is of type AI
	 * 
	 * @returns Boolean value to denote whether the object is an AI or not. For this class it should always be false. 
	 */
	public boolean isAI()
	{
		return false;
	}
	
	//sets here
	//methods here
}
