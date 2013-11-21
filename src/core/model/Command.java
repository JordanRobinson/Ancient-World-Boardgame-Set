/**
 * Command Interface
 * Simple interface which enables the use of undo, and similar features.
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.0.0 21 Apr 2012
 */


package core.model;

public interface Command {
	
	//variable assignment here
	
	//constructors here
		
		//variables passed in

		//variables not passed in

	//gets here

	//sets here

	//methods here
	void execute();
	void undo();

}
