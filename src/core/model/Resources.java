/**
 *  Resources Class
 *  Class for managing and storing the resources used by the games, such as images or any other required files.
 * 
 * @package core.model
 * @author Jordan Robinson, Ryan Carter
 * @version v2.0.4.4 5 Mar 2012
 */

package core.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import core.view.AbstractView;


public class Resources {

	//variable assignment here
	private static Resources instance = new Resources(); //This class uses the singleton pattern, and as such this should be the only instance ever created
	private static HashMap<String, Object> resources = new HashMap<String,Object>(); //contains all the loaded resources, to be checked so that no resource is ever loaded twice
	
	//constructors here
	/**
	 * Private constructor, so that the class can be a singleton.
	 */
	private Resources()
	{}

	//gets here
	
	/**
	 * This returns the instance of this class, necessary since this class uses the singleton pattern
	 */
	public static Resources getInstance()
	{
		if (instance == null) { //if there isn't currently a created instance
			instance = new Resources(); //create it
		}
		return instance; //and then return it
	}

	//sets here

	//methods here
	
	/**
	 * Loads a resource, using the inner classes of this class, depending on the type of resource.
	 * 
	 * @param fileName The filename to use when loading in the file
	 * @param resourceType The type of resource to load
	 */
	public static Object load(String fileName, String resourceType) //pass file name and type of resource i.e "image"
	{
		if(resources.containsKey(fileName) == false)// if resource not already in hashmap load it in
		{
			ResourceFactory.createResource(resourceType).loadResource(fileName);
		}
		return resources.get(fileName);
	}
	
	/**
	 * This inner class uses the factory pattern to load in the various resources required by the program
	 */
	private static class ResourceFactory// creates type of resource depending on the type specified
	{
		/**
		 * Loads in a resource into the hashmap used by this class, and checks the type so that it can load it in the correct manner
		 * 
		 * @param type The type of file to load into the hashmap
		 */
		private static LoadResource createResource(String type)
		{
			LoadResource resource = null;
			if(type.equals("image"))	//if more types of resource were needed they can be added here
			{							//and another inner class that knows how to build it
				resource = instance.new LoadImage();
			}
			else if (type.equals("imageIcon")) {
				resource = instance.new LoadImageIcon();
			}
			else if (type.equals("textFile")) {
				resource = instance.new LoadTextFile();
			}
			return resource;
		}
	}
	
	/**
	 * Loads the properties file required for setting options and the like.
	 */
	public static void loadProperties()
	{
        FileInputStream propFile = null; //set up the inputStream to prepare for loading in a file
		try {
			propFile = new FileInputStream("src/resources/config.properties"); //load the properties file using the path to where it should be located
		} catch (FileNotFoundException e1) {
			AbstractView.displayError("Can't find the properties file!\nLooked in dir: 'src/resources/config.properties' ");
			return;
		}
        Properties p = new Properties(System.getProperties()); //get the current properties of the system
        try {
			p.load(propFile); //then try to add the properties loaded from file into the running properties
		} catch (IOException e) {
			AbstractView.displayError("Problem loading in the properties file!/nexiting...");
			return;
		}
        System.setProperties(p);
	}
	
	/**
	 * Interface for which the inner classes for each type of file should use
	 */
	private interface LoadResource
	{
		abstract void loadResource(String id);
	}
	
	/**
	 * Class for loading a file of type BufferedImage into the system.
	 */
	private class LoadImage implements LoadResource// inner class that knows how to load an image
	{
		@Override
		public void loadResource(String id)
		{
			BufferedImage image = null; //set up a new buffered image
			try {
				image = ImageIO.read(new File(id)); //load it in using the id given (note that the id is the path, and also the id for the hashmap)
			} catch (IOException e) {
				AbstractView.displayError("Problem loading in an image!/nIs the image directory present?");
			}
			resources.put(id, image); //now that we have the image, load it in to the resources hashmap, from which it will need to be casted to be used correctly
		}
	}
	
	/**
	 * Class for loading a file of type ImageIcon into the system.
	 * 
	 * Note that this is used for any animated images, since they cannot be casted to the BufferedImage class.
	 */
	private class LoadImageIcon implements LoadResource
	{
		@Override
		public void loadResource(String id)
		{
			ImageIcon image = null;
			image = new ImageIcon(id);
			resources.put(id, image);
		}
	}
	
	/**
	 * Class for loading a text file into the system
	 * 
	 */
	private class LoadTextFile implements LoadResource
	{
		@Override
		public void loadResource(String id) 
		{
			File file = null;
			file = new File(id); 
			resources.put(id, file);
		}
	}
}