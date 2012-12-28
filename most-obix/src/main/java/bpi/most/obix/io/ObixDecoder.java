/*
 * This code licensed to public domain
 */
package bpi.most.obix.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import bpi.most.obix.Abstime;
import bpi.most.obix.Bool;
import bpi.most.obix.Contract;
import bpi.most.obix.ContractRegistry;
import bpi.most.obix.Enum;
import bpi.most.obix.Feed;
import bpi.most.obix.Int;
import bpi.most.obix.List;
import bpi.most.obix.Obj;
import bpi.most.obix.Op;
import bpi.most.obix.Real;
import bpi.most.obix.Reltime;
import bpi.most.obix.Status;
import bpi.most.obix.Str;
import bpi.most.obix.Uri;
import bpi.most.obix.Val;
import bpi.most.obix.xml.XElem;
import bpi.most.obix.xml.XException;
import bpi.most.obix.xml.XParser;

/**
 * ObixDecoder is used to deserialize an XML stream
 * into memory as a tree of Obj instances. 
 *
 * @author    Brian Frank
 * @creation  27 Apr 05
 * @version   $Revision$ $Date$
 */
public class ObixDecoder 
  extends XParser
{

////////////////////////////////////////////////////////////////
// Factory
////////////////////////////////////////////////////////////////
  
  /**
   * Decode an Obj from a String.
   */
  public static Obj fromString(String xml)                 
  {                 
    try
    {
      ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
      ObixDecoder decoder = new ObixDecoder(in);
      return decoder.decodeDocument();      
    }
    catch(Exception e)
    {
      throw new RuntimeException(e.toString());
    }
  }
                                             
////////////////////////////////////////////////////////////////
// Constructors
////////////////////////////////////////////////////////////////

  /**
   * Construct for specified input stream.
   */
  public ObixDecoder(InputStream in)
    throws Exception
  {                                
    super(in);
  }

////////////////////////////////////////////////////////////////
// Access
////////////////////////////////////////////////////////////////
  
  /**
   * Get the useContracts flags.  If this flag is true, then
   * we attempt to map contract attributes to predefined classes
   * using the ContractRegistry.  If the false, then we just
   * map to the built-in classes (Obj, Bool, Int, etc).  The
   * default for this flag is true.
   */
  public boolean getUseContracts()
  {                                                    
    return useContracts;
  }
  
  /**
   * Set the useContracts flag - see getUseContracts() for details.
   */
  public void setUseContracts(boolean useContracts)
  {
    this.useContracts = useContracts;
  }

////////////////////////////////////////////////////////////////
// Document
////////////////////////////////////////////////////////////////

  /**
   * Convenience for <code>decodeDocument(true)</code>.
   */
  public Obj decodeDocument()
    throws Exception
  {                            
    return decodeDocument(true);
  }

  /**
   * Decode the XML document into a Obj, and
   * optionally close the input stream.
   */
  public Obj decodeDocument(boolean close)
    throws Exception
  {
    try
    {
      // parse into memory
      XElem root = parse();
  
      // decode root recursively
      return decode(null, root, null);
    }
    finally
    {
      if (close) close();
    }
  }          
  
////////////////////////////////////////////////////////////////
// Object Decoding
////////////////////////////////////////////////////////////////
  
  /**
   * Recursively decode element into Obj instances.
   */
  private Obj decode(Obj parent, XElem x, Contract defaultContract)
    throws Exception
  {                              
    // attribute variables
    String name        = null;
    String val         = null;   
    String href        =  null;   
    String is          = null;    
    String of          = null;    
    String in          = null;
    String out         = null;
    String display     = null;
    String displayName = null;
    String icon        = null;
    String isNull      = null;
    String writable    = null;
    String status      = null;
    String range       = null;    
    String min         = null;
    String max         = null;
    String unit        = null;    
    String precision   = null;
    
    // fill in attributes found
    int attrSize = x.attrSize();
    for(int i=0; i<attrSize; ++i)
    {
      String attrName = x.attrName(i);
      String attrVal  = x.attrValue(i);
      if (attrName.equals("name"))             name  = attrVal;
      else if (attrName.equals("val"))         val   = attrVal;
      else if (attrName.equals("href"))        href  = attrVal;
      else if (attrName.equals("is"))          is    = attrVal;
      else if (attrName.equals("of"))          of    = attrVal;
      else if (attrName.equals("in"))          in    = attrVal;
      else if (attrName.equals("out"))         out   = attrVal;
      else if (attrName.equals("display"))     display   = attrVal;
      else if (attrName.equals("displayName")) displayName = attrVal;
      else if (attrName.equals("null"))        isNull    = attrVal;
      else if (attrName.equals("icon"))        icon      = attrVal;
      else if (attrName.equals("writable"))    writable  = attrVal;
      else if (attrName.equals("status"))      status    = attrVal;
      else if (attrName.equals("range"))       range     = attrVal;
      else if (attrName.equals("min"))         min       = attrVal;
      else if (attrName.equals("max"))         max       = attrVal;
      else if (attrName.equals("unit"))        unit      = attrVal;
      else if (attrName.equals("precision"))   precision = attrVal;
    }             
    
    // map element name to an Obj Class (Obj, Bool, Int, etc)
    String elemName = x.name();
    Class cls = Obj.toClass(elemName);
    if (cls == null)
    {
      System.out.println("WARNING: Unknown element: " + x + " [Line " + x.line() + "]");
      return null;
    }
    
    // if we have a contract specified, then parse it
    Contract contract = null;
    if (is != null)
      contract = new Contract(is);   
    
    // if a name was specified, check the parent for
    // an existing default object to use (this happens if
    // the parent was created from a contract list)
    Obj obj = null;
    if (parent != null && name != null)
      obj = parent.get(name);        
    
    // if obj wasn't found in parent then we need do 
    // go thru a process to figure out how to create it
    if (obj == null)
    {                                  
      // if the decoder is configured to use contracts 
      // and we have a contract available, then map
      // the contract to a class (otherwise we fallback
      // to the class we looked up for the elem name)
      if (useContracts)
      {
        if (contract != null) 
          cls = ContractRegistry.toClass(cls, contract);
        else if (defaultContract != null)
          cls = ContractRegistry.toClass(cls, defaultContract);
      }
      
      // instaniate an object from the class
      obj = (Obj)cls.newInstance();
    }   
        

    // If we are using an object from the parent, then
    // let's make sure the element name specified in the document
    // doesn't conflict with the contract's definition.  There
    // are two cases where this is actually ok:
    //   1) using ref to indicate reference to target object
    //   2) covariantly overridden obj to be something else
    // If we detect a mismatch that fits one of those two cases
    // then reallocate obj correctly.        
    //
    // TODO: this code isn't quite perfect, because technically by 
    //   allocating a new instance we might be throwing awaya type 
    //   we mapped from the ContractRegistery or facets the contract 
    //   had declared - but in practice I'm not sure it matters   
    else if (!elemName.equals(obj.getElement()))
    {
      if (elemName.equals("ref") || obj.getElement().equals("obj"))
      { 
        Obj newObj = Obj.toObj(elemName);
        if (newObj != null)
        {                               
          newObj.setName(name);
          if (obj.getParent() != null) obj.getParent().replace(obj, newObj);
          obj = newObj;
        }
      }
      else
      {
        throw err("Element name '" + elemName + "' conflicts with contract element '" + obj.getElement() + "'", x);
      }
    }
    
    // name
    if (name != null && obj.getName() == null)
      obj.setName(name);
            
    // href
    if (href != null)
      obj.setHref(new Uri(null, href));

    // is
    if (contract != null)
      obj.setIs(contract);
    
    // parse value
    if (val != null)       
    {       
      if (isNull == null) obj.setNull(false);
      try
      {
        ((Val)obj).decodeVal(val);
      }
      catch(Exception e)
      {
        throw err("Invalid val attribte '" + val + "' for " + obj.getElement(), x, e);
      }
    }           

    // facets
    if (display != null)     obj.setDisplay(display);
    if (displayName != null) obj.setDisplayName(displayName);
    if (icon != null)        obj.setIcon(new Uri(icon));
    if (status != null)      obj.setStatus(Status.parse(status));
    if (isNull != null)      obj.setNull(isNull.equals("true"));
    if (writable != null)    obj.setWritable(writable.equals("true"));
    
    // meta-data & Type specific facets                                           
    Contract childrenDefaultContract = null;
    if (obj instanceof List)
    {
      List list = (List)obj;
      if (of != null) list.setOf(childrenDefaultContract = new Contract(of));     
      if (min != null) list.setMin(Integer.parseInt(min));     
      if (max != null) list.setMax(Integer.parseInt(max));     
    }
    else if (obj instanceof Op)
    {                    
      Op op = (Op)obj;
      if (in != null)  op.setIn(new Contract(in));     
      if (out != null) op.setOut(new Contract(out));     
    }
    else if (obj instanceof Bool)
    {                         
      Bool b = (Bool)obj; 
      if (range != null) b.setRange(new Uri(range));     
    }
    else if (obj instanceof Int)
    {                         
      Int i = (Int)obj; 
      if (min != null) i.setMin(Long.parseLong(min));     
      if (max != null) i.setMax(Long.parseLong(max));     
      if (unit != null) i.setUnit(new Uri(unit));     
    }
    else if (obj instanceof Str)
    {                           
      Str s = (Str)obj;
      if (min != null) s.setMin(Integer.parseInt(min));     
      if (max != null) s.setMax(Integer.parseInt(max));     
    }
    else if (obj instanceof Real)
    {                         
      Real r = (Real)obj; 
      if (min != null) r.setMin(Double.parseDouble(min));     
      if (max != null) r.setMax(Double.parseDouble(max));     
      if (unit != null) r.setUnit(new Uri(unit));     
      if (precision != null) r.setPrecision(Integer.parseInt(precision));     
    }
    else if (obj instanceof Enum)
    {                         
      Enum e = (Enum)obj; 
      if (range != null) e.setRange(new Uri(range));     
    }
    else if (obj instanceof Reltime)
    {                         
      Reltime r = (Reltime)obj; 
      if (min != null) r.setMin(Reltime.parse(min));     
      if (max != null) r.setMax(Reltime.parse(max));     
    }
    else if (obj instanceof Abstime)
    {                         
      Abstime a = (Abstime)obj; 
      if (min != null) a.setMin(Abstime.parse(min));     
      if (max != null) a.setMax(Abstime.parse(max));     
    }
    else if (obj instanceof Feed)
    {                    
      Feed feed = (Feed)obj;
      if (in != null) feed.setIn(new Contract(in));     
      if (of != null) feed.setOf(new Contract(of));     
    }    
    
    // recurse
    XElem[] kids = x.elems();
    for(int i=0; i<kids.length; ++i)
    {
      Obj kid = decode(obj, kids[i], childrenDefaultContract);
      if (kid != null && kid.getParent() ==  null) 
      {
        try
        {
          obj.add(kid);
        }
        catch(Exception e)
        {
          throw err("Cannot add child '" + name + "'", kids[i], e);
        }
      }
    }
    
    // all done
    return obj;
  }

////////////////////////////////////////////////////////////////
// Error
////////////////////////////////////////////////////////////////

  XException err(String msg, XElem elem, Throwable cause)
  {
    return new XException(msg, elem, cause);
  }
  
  XException err(String msg, XElem elem)
  {
    return new XException(msg, elem);
  }

  void warning(String msg, XElem elem)
  {                                               
    String line = "";
    if (elem != null) line = " [line " + elem.line() + "]";
    System.out.println("WARNING: " + msg + line);
  }

////////////////////////////////////////////////////////////////
// Fields
////////////////////////////////////////////////////////////////

  private boolean useContracts = true;
  
} 
