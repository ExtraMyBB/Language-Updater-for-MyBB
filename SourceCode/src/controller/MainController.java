package controller;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import gui.FileTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.tree.TreePath;
import models.LineEntry;
import obspattern.SubjectListener;
import obspattern.UIListener;

public class MainController implements SubjectListener 
{

    private static MainController m_instance;
    private FileTree m_fileTree;
    private String m_olderName;
    private Properties config;
    private ArrayList<LineEntry> m_entries;

    /**
     * Singleton pattern needs default constructor to be private!
     */
    private MainController() 
    {
        m_entries = new ArrayList<>();
    }

    /**
     * Returns an instance of MainController class.
     *
     * @return
     */
    public static MainController getInstance() {
        if (m_instance == null) {
            m_instance = new MainController();
        }

        return m_instance;
    }

    public void setFileTree(FileTree fileTree) {
        m_fileTree = fileTree;
    }

    public FileTree getFileTree() {
        return m_fileTree;
    }

    public void setOlderName(String name) {
        m_olderName = name;
    }
    
    public List<LineEntry> getEntries()
    {
        return m_entries;
    }

    /**
     * Checks if a given fie or directory exists on server.
     *
     * @param name the path tested
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
        if (!fileExists("config.ini")) {
            throw new Exception("Config file does not exists!");
        }

        // Load config properties
        config = new Properties();
        config.load(new FileReader("config.ini"));
    }

    /**
     * Returns the value for a requested configuration key.
     *
     * @param key which entry is requested?
     * @param defaultValue in case of entry not founded, a default value can be
     * returned
     * @return
     */
    public String getConfigProperty(String key, String defaultValue) {
        if (config == null) {
            return null;
        }

        return config.getProperty(key, defaultValue);
    }

    /**
     * Gives the possibility to user to save some settings.
     *
     * @param key which entry will be changed?
     * @param newValue the new value for the entry
     * @return
     */
    public Object setConfigProperty(String key, String newValue) {
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

    public String pathToString(TreePath path) 
    {
        String winPath = ".";
        for (Object obj : path.getPath()) {
            winPath += "/" + obj;
        }
        return winPath;
    }

    public void newFileSelected(TreePath path) 
    {
        // Rebuild all entries
        m_entries.clear();
        
        // Get final path to the file selected
        String stringPath = pathToString(path);
        
        File newer = new File(stringPath);
        // Only files can be edited!
        if ( ! newer.isFile()) {
            notifyOnListUpdated();
            return;
        }

        // Check if older file exists?
        File older = new File(stringPath.replace(m_olderName, config.getProperty("OldDirectory", "old")));
        if ( ! older.exists()) {
            // We need to create it
            // TO DO
        } else {
            List<String> original = fileToLines(older);
            List<String> revised = fileToLines(newer);

            // Compute diff. Get the Patch object. Patch is the container for computed deltas.
            Patch patch = DiffUtils.diff(original, revised);
            List<Delta> deltas = patch.getDeltas();

            String out, out1;
            Matcher matcher, matcher1;
            Pattern pattern = Pattern.compile("^\\$l\\[[\"|\'](.*)[\"|\']\\][\\s\\t]*=[\\s\\t]*(.*)[\\s\\t]*;[\\s\\t]*$");
            for (int i = 0; i < deltas.size(); i++) {
                Delta d = deltas.get(i);
                Chunk org = d.getOriginal();
                Chunk rev = d.getRevised();
                // Delete query?
                if (d.getType() == Delta.TYPE.DELETE) {
                    continue;
                }

                out = (String)rev.getLines().get(0);
                matcher = pattern.matcher(out);
                // Original size can be 0 or 1
                if ( ! matcher.find()) {
                    if (org.size() > 0) {
                        out1 = (String)org.getLines().get(0);
                        matcher1 = pattern.matcher(out1);
                        if ( ! matcher1.find()) {
                            deltas.remove(i);
                            continue;
                        } else {
                            m_entries.add(new LineEntry(d, matcher.group(1)));
                        }
                    } else {
                        m_entries.add(new LineEntry(d, "Unknown"));
                    }
                } else {
                    m_entries.add(new LineEntry(d, matcher.group(1)));
                }
                deltas.remove(i);
            }
        }
        
        // Notify listeners to update
        notifyOnListUpdated();
    }

    private static List<String> fileToLines(File path) 
    {
        List<String> lines = new LinkedList<>();
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e1) {
            // TO DO
        } catch (IOException e2) {
            // TO DO
        }
        return lines;
    }

    @Override
    public void addUIListener(UIListener uiListener) 
    {
        listeners.add(uiListener);
    }

    @Override
    public void notifyOnListUpdated() 
    {
        for (UIListener ui : listeners) {
            ui.listUpdated();
        }
    }
}
