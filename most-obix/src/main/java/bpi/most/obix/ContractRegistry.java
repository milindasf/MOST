/*
 * This code licensed to public domain
 */
package bpi.most.obix;    

import java.util.ArrayList;
import java.util.HashMap;

import bpi.most.obix.asm.ObixAssembler;

/**
 * ContractRegistry serves a central database for mapping
 * contract URIs to Contract definitions.
 *
 * @author    Brian Frank
 * @creation  27 Apr 05
 * @version   $Revision$ $Date$
 */
public class ContractRegistry
{ 

////////////////////////////////////////////////////////////////
// Access
////////////////////////////////////////////////////////////////

  /**
   * Convenience for <code>toClass(base, contract).newInstance()<code>.
   */
  public static Obj toObj(Class base, Contract contract)    
  {             
    try
    {          
      return (Obj)toClass(base, contract).newInstance();
    }
    catch(Exception e)
    {                                          
      e.printStackTrace();
      throw new RuntimeException(e.toString());
    }
  }
  
  /**
   * Lookup a Class which best supports the specified 
   * contract (set of URIs).  The returned class will
   * be subclassed from base and implement any interfaces
   * registered for URIs in the specified contract list.
   */
  public static Class toClass(Class base, Contract contract)
  {              
    // short circuit if contract is null/empty; we also
    // never created "typed" version of Ref because it
    // would be confusing (consider the Lobby.about Ref
    // as actually implementing About)
    if (contract == null || contract.size() == 0 || 
        contract.containsOnlyObj() || base == Ref.class)
      return base;          
                
    // first check cache                             
    String key = base.getName() + ": " + contract.toString();
    Class cls = (Class)cache.get(key);
    
    // we use my own class as a special placeholder
    // for "not found", so we don't repeat the expensive
    // calculations below   
    if (cls == NotFound)
      return base;       
    
    // if we found it then cool beans
    if (cls != null)
      return cls;                
    
    // if we didn't find a class, then try to compile
    // one for this contract list
    try
    {                            
      cls = compile(base, contract);
      if (cls == null) 
      {
        cache.put(key, NotFound);
        return base;
      }
      
      cache.put(key, cls);
      return cls;
    }
    catch(Exception e)
    {
      throw new RuntimeException("Cannot compile contract: " + key, e);
    } 
  }
  
  /**
   * Search for a registered interface for each URI in the
   * contract list.  Then dynamically assemble the bytecode 
   * for a class which implements all the interfaces.
   */
  private static Class compile(Class base, Contract contract)
    throws Exception
  { 
    // try to map each URI in the contract list to an interface
    Uri[] list = contract.list();
    ArrayList acc = new ArrayList();
    for (int i=0; i<list.length; ++i)
    {                       
      // check for Java interface
      String className = (String)map.get(list[i].get());
      if (className != null)      
      {                     
        Class cls = Class.forName(className);
        acc.add(cls);
      }                          
      
      // check for base like obix:int
      String baseClassName = (String)baseContracts.get(list[i].get());
      if (baseClassName != null)
      {
        if (base != Obj.class && !base.getName().equals(baseClassName))
          throw new IllegalArgumentException("Base conflicts with contract: " + base.getName() + " and " + list[i].get());
        base = Class.forName(baseClassName);        
      }
    } 
    
    // if no interfaces found for contract URIs then bail  
    if (acc.size() == 0) return base;
    Class[] interfaces = (Class[])acc.toArray(new Class[acc.size()]);
    
    // compile a class   
    Class cls = ObixAssembler.compile(base, interfaces);
    System.out.println("-- Compile: " + base.getName() + ": " + contract + " -> " + cls.getName());
    return cls;
  }

  /**
   * Convenience for <code>put(new Uri(href), className)</code>.
   */
  public static void put(String href, String className)
  {                                                     
    put(new Uri(href), className);
  }
  
  /**
   * Register a subclass of Obj for the specified contract uri.
   */
  public static void put(Uri href, String className)
  {                      
    if (map.get(href.get()) != null)
      throw new IllegalStateException("The specified href is already mapped: " + href);
    map.put(href.get(), className);     
    cache.clear(); // clear cache
  }                                
  
////////////////////////////////////////////////////////////////
// Fields
////////////////////////////////////////////////////////////////

  static HashMap map = new HashMap();   // URI -> className
  static HashMap cache = new HashMap(); // Contract.toString -> Class
  static Class NotFound = ContractRegistry.class;
  
  static HashMap baseContracts = new HashMap();
  static 
  { 
    baseContracts.put("obix:obj",     "obix.Obj");
    baseContracts.put("obix:bool",    "obix.Bool");
    baseContracts.put("obix:int",     "obix.Int");
    baseContracts.put("obix:real",    "obix.Real");
    baseContracts.put("obix:str",     "obix.Str");
    baseContracts.put("obix:enum",    "obix.Enum");
    baseContracts.put("obix:abstime", "obix.Abstime");
    baseContracts.put("obix:reltime", "obix.Reltime");
    baseContracts.put("obix:uri",     "obix.Uri");
    baseContracts.put("obix:list",    "obix.List");
    baseContracts.put("obix:op",      "obix.Op");
    baseContracts.put("obix:event",   "obix.Event");
    baseContracts.put("obix:ref",     "obix.Ref");
    baseContracts.put("obix:err",     "obix.Err");
    bpi.most.obix.contracts.ContractInit.init(); 
  }
}
