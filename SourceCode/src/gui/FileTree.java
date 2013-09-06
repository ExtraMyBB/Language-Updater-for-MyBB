package gui;

import controller.MainController;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class FileTree extends JTree implements TreeSelectionListener
{
    private int m_files, m_directories;
    
    public FileTree(File directory) 
    {       
        // Reset counters
        resetCounters();
        
        // Set the model
        DefaultTreeModel model = new DefaultTreeModel(addNodes(null, directory));
        setModel(model);

        // Add a listener
        addTreeSelectionListener(this);
    }
    
    public int getNumberOFiles()
    {
        return m_files;
    }
    
    public int getNumberOfDirs()
    {
        return m_directories;
    }
    
    public void setNewModel(File directory)
    {
        // Reset counters
        resetCounters();
        
        // Set a new model
        DefaultTreeModel model = new DefaultTreeModel(addNodes(null, directory));
        setModel(model);
    }
    
    private void resetCounters() 
    {
        m_files = m_directories = 0;
    }

    DefaultMutableTreeNode addNodes(DefaultMutableTreeNode top, File directory) 
    {
        String currentPath = directory.getPath();
        DefaultMutableTreeNode currentDir = new DefaultMutableTreeNode(currentPath);
        
        // Should only be null at root
        if (top != null) { 
            top.add(currentDir);
        }
        
        Vector ol = new Vector();
        String[] tmp = directory.list();
        for (int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        
        File f;
        Vector files = new Vector();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (currentPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = currentPath + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {
                m_directories++;
                addNodes(currentDir, f);
            } else {
                m_files++;
                files.addElement(thisObject);
            }
        }
        
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++) {
            currentDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
        }
        
        return currentDir;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e != null) {
            MainController.getInstance().newFileSelected(e.getPath());
        }
    }
}