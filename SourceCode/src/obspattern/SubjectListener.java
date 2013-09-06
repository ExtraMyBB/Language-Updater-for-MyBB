package obspattern;

import java.util.ArrayList;

public interface SubjectListener 
{
    ArrayList<UIListener> listeners = new ArrayList<UIListener>();
    
    public void addUIListener(UIListener uiListener);
    public void notifyOnListUpdated();
}
