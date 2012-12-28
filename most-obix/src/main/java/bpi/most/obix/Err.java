/*
 * This code licensed to public domain
 */
package bpi.most.obix;      

import java.net.*;
import java.util.*;

/**
 * Err models an error object.
 *
 * @author    Brian Frank
 * @creation  30 Mar 06
 * @version   $Revision$ $Date$
 */
public class Err
  extends Obj
{ 

////////////////////////////////////////////////////////////////
// Construction
////////////////////////////////////////////////////////////////
  
  /**
   * Construct named Err.
   */
  public Err(String name) 
  {                
    super(name);
  }                 
    
  /**
   * Construct unnamed Err.
   */
  public Err() 
  { 
  }

////////////////////////////////////////////////////////////////
// Obj
////////////////////////////////////////////////////////////////

  /**
   * Return "err".
   */
  public String getElement()
  {
    return "err";
  }

////////////////////////////////////////////////////////////////
// Format
////////////////////////////////////////////////////////////////
  
  /**
   * Format the error for human display.
   */
  public String format()
  {                               
    // TODO - displayName, is, etc
dump();  
    return this.toString();
  }
    
////////////////////////////////////////////////////////////////
// Fields
////////////////////////////////////////////////////////////////
    
}
