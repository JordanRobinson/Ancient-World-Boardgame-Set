/**
 *  CommandInvoker Class
 *  Facilitates the undoing and executing of commands
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v1.0.5.0 15 Apr 2012
 */

package core.model;

import java.io.Serializable;
import java.util.Stack;

public class CommandInvoker implements Serializable {

	//variable assignment here
	private static Stack<Command> undoStack; //the stack upon which the commands are added
	private static final long serialVersionUID = 3L;

	//constructors here
	protected CommandInvoker()
	{
		//variables passed in
		//variables not passed in
		undoStack = new Stack<Command>();
	}
	//gets here
	//sets here

	//methods here
	/**
	 * Runs the command currently at the top of the undoStack. Note that this can throw a stack empty exception if the stack is empty.
	 * 
	 * As such this should usually be called directly after adding to the stack.
	 */
	public void executeCommand()
	{
		undoStack.peek().execute();
	}

	/**
	 * Adds a command to the undoStack.
	 */
	public void addCommand(Command command)
	{
		undoStack.push(command);
	}

	/**
	 * Undoes the top command of the undoStack. Also checks if the stack is empty, and if it is, simply returns without doing anything.
	 */
	public void undo()
	{
		if (!undoStack.empty()) {
			undoStack.pop().undo();
		}
	}
}