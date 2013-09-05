package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class MainController {
    private static MainController instance;
    
    private Properties config;
    
    /**
     * Singleton pattern needs default constructor to be private!
     */
    private MainController() 
    {
        
    }
    
    /**
     * Returns an instance of MainController class.
     * 
     * @return 
     */
    public static MainController getInstance() 
    {
        if (instance == null) {
            instance = new MainController();
        }
        
        return instance;
    }
    
    /**
     * Checks if a given fie or directory exists on server.
     * 
     * @param name          the path tested
     * @return
     */
    public boolean fileExists(String name) 
    {
        File f = new File(name);
        return f.exists();
    }
    
    /**
     * Some inits must be done before displaying the main frame.
     * 
     * @throws Exception 
     */
    public void loadApp() throws Exception 
    {
        // Check if config file exists!
        if ( ! fileExists("config.ini")) {
            throw new Exception("Config file does not exists!");
        }
        
        // Load config properties
        config = new Properties();
        config.load(new FileReader("config.ini"));
    }
    
    /**
     * Returns the value for a requested configuration key.
     * 
     * @param key           which entry is requested?
     * @param defaultValue  in case of entry not founded, a default value can be
     *                      returned
     * @return 
     */
    public String getConfigProperty(String key, String defaultValue) 
    {
        if (config == null) {
            return null;
        }
        
        return config.getProperty(key, defaultValue);  
    }
    
    /**
     * Gives the possibility to user to save some settings.
     * 
     * @param key           which entry will be changed?
     * @param newValue      the new value for the entry
     * @return 
     */
    public Object setConfigProperty(String key, String newValue) 
    {
        if (config == null) {
            return null;
        }
        
        return config.setProperty(key, newValue);
    }
    
    /**
     * After changing some config properties, for saving them is necessary to
     * call this function.
     * 
     * @throws Exception 
     */
    public void storeConfigProperties() throws Exception
    {
        if (config == null) {
            throw new Exception();
        }
        
        config.store(new FileWriter("config.ini"), null);
    }
    
}
