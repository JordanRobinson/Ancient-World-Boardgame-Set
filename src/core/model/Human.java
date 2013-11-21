/**
 *  Human Class
 *  Representation of a human player of any of the games.
 *  Extends Player class.
 *  
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.5.6 15 Feb 2012
 */


package core.model;

public class Human extends Player {

	//variable assignment here
	private static final long serialVersionUID = 3L;
	
	//constructors here
	public Human(String name) {
		//variables passed in
		super(name);
		//variables not passed in

	}

	//gets here

	//sets here

	//methods here
	@Override
	public boolean isAI() {
		return false;
	}

}
