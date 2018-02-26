package com.factory.dao.impl;


public class NVPair
{
    private String field;
    private Object value;
    private String symbolicName;
    
    /**
     * Constructor for a pair of Objects.
     *  
     * @param field First object in the pair.
     * @param value Second object in the pair.
     */
    public NVPair(String field, Object value)
    {
      this.field = field;
      this.value = value;
      this.symbolicName = this.field;
    }
    
    /**
     * Returns the field object in the pair.
     * @return The field object.
     */
    public String getField()
    {
      return field;
    }
    
    public void setField(String field)
    {
      this.field = field;
    }
    
    /**
     * Returns the value object in the pair.
     * @return The value object.
     */
    public Object getValue()
    {
      return value; 
    }
    
    public void setValue(Object value)
    {
      this.value = value;
    }

    protected void setSymbolicName(String symName)
    {
      this.symbolicName = symName;
    }

    public String getSymbolicName()
    {
      return this.symbolicName;
    }
    

}

