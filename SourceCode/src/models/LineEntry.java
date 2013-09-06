package models;

import difflib.Delta;

public class LineEntry implements java.io.Serializable
{
    private Delta m_deltaEntry;
    private String m_codeName;
    
    public LineEntry(Delta entry, String keyCode)
    {
        m_deltaEntry = entry;
        m_codeName = keyCode;
    }
    
    @Override
    public String toString()
    {
        if (m_deltaEntry.getType() == Delta.TYPE.INSERT) {
            return "New entry - '" + m_codeName + "'";
        } else {
            return "Changed entry - '" + m_codeName + "'";
        }
    }
}
