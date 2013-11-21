/**
 *  Rule Interface
 *  Interface to standardise the way that rules work.
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.2.4 15 Feb 2012
 */


package core.model;

public interface Rule {
	
	//variable assignment here
	
	//constructors here
		
		//variables passed in

		//variables not passed in

	//gets here

	//sets here

	//methods here
	/**
	 * The method that checks whether or not the rule itself can be proven valid given the parameters given.
	 * 
	 * @param board The board upon which the rule is being enacted
	 * @param command The specific command that the rule is being checked against
	 * @returns A boolean value giving the state of the rule
	 */
	abstract boolean checkValid(Board board, Command command); 
	abstract String getInvalidMsg(); //the error message to return should the rule be proven false
}