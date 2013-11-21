/**
 *  Tile Class
 *  Abstract class representing a tile, to be extended upon by the various games.
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.0.4 15 Feb 2012
 */

package core.model;

import java.io.Serializable;
import java.util.Observable;

@SuppressWarnings("serial") //since this is an abstract class, only subclasses of this will make use of Serializable, as such they should have their own version IDs
public abstract class Tile extends Observable implements Serializable {
	
	//variable assignment here
	//constructors here
		
		//variables passed in
		//variables not passed in

	//gets here
	//sets here
	//methods here
}