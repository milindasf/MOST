/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import bpi.most.obix.asm.ObixAssembler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ContractRegistry serves a central database for mapping
 * contract URIs to Contract definitions.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public class ContractRegistry {

    private static HashMap<String, String> map = new HashMap<String, String>();   // URI -> className
    private static HashMap<String, Class> cache = new HashMap<String, Class>(); // Contract.toString -> Class
    private static Class NotFound = ContractRegistry.class;

    private static HashMap<String, String> baseContracts = new HashMap<String, String>();

    static {
        baseContracts.put("obix:obj", "bpi.most.obix.objects.Obj");
        baseContracts.put("obix:bool", "bpi.most.obix.objects.Bool");
        baseContracts.put("obix:int", "bpi.most.obix.objects.Int");
        baseContracts.put("obix:real", "bpi.most.obix.objects.Real");
        baseContracts.put("obix:str", "bpi.most.obix.objects.Str");
        baseContracts.put("obix:enum", "bpi.most.obix.objects.Enum");
        baseContracts.put("obix:abstime", "bpi.most.obix.objects.Abstime");
        baseContracts.put("obix:reltime", "bpi.most.obix.objects.Reltime");
        baseContracts.put("obix:uri", "bpi.most.obix.objects.Uri");
        baseContracts.put("obix:list", "bpi.most.obix.objects.List");
        baseContracts.put("obix:op", "bpi.most.obix.objects.Op");
        baseContracts.put("obix:event", "bpi.most.obix.objects.Event");
        baseContracts.put("obix:ref", "bpi.most.obix.objects.Ref");
        baseContracts.put("obix:err", "bpi.most.obix.objects.Err");
        // my extension
        baseContracts.put("obix:dp", "bpi.most.obix.objects.Dp");
        baseContracts.put("obix:dpData", "bpi.most.obix.objects.DpData");
        baseContracts.put("obix:zone", "bpi.most.obix.objects.Zone");

        bpi.most.obix.contracts.ContractInit.init();
    }

////////////////////////////////////////////////////////////////
// Access
////////////////////////////////////////////////////////////////

    /**
     * Convenience for <code>toClass(base, contract).newInstance()<code>.
     */
    public static Obj toObj(Class base, Contract contract) {
        try {
            return (Obj) toClass(base, contract).newInstance();
        } catch (Exception e) {
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
    public static Class toClass(Class base, Contract contract) {
        // short circuit if contract is null/empty; we also
        // never created "typed" version of Ref because it
        // would be confusing (consider the Lobby.about Ref
        // as actually implementing About)
        if (contract == null || contract.size() == 0 ||
                contract.containsOnlyObj() || base == Ref.class) {
            return base;
        }

        // first check cache
        String key = base.getName() + ": " + contract.toString();
        Class cls = (Class) cache.get(key);

        // we use my own class as a special placeholder
        // for "not found", so we don't repeat the expensive
        // calculations below
        if (cls == NotFound) {
            return base;
        }

        // if we found it then cool beans
        if (cls != null) {
            return cls;
        }

        // if we didn't find a class, then try to compile
        // one for this contract list
        try {
            cls = compile(base, contract);
            if (cls == null) {
                cache.put(key, NotFound);
                return base;
            }

            cache.put(key, cls);
            return cls;
        } catch (Exception e) {
            throw new RuntimeException("Cannot compile contract: " + key, e);
        }
    }

    /**
     * Search for a registered interface for each URI in the
     * contract list.  Then dynamically assemble the bytecode
     * for a class which implements all the interfaces.
     */
    private static Class compile(Class base, Contract contract)
            throws Exception {
        // try to map each URI in the contract list to an interface
        Uri[] list = contract.list();
        ArrayList<Class> acc = new ArrayList<Class>();
        for (int i = 0; i < list.length; ++i) {
            // check for Java interface
            String className = (String) map.get(list[i].get());
            if (className != null) {
                Class cls = Class.forName(className);
                acc.add(cls);
            }

            // check for base like obix:int
            String baseClassName = (String) baseContracts.get(list[i].get());
            if (baseClassName != null) {
                if (base != Obj.class && !base.getName().equals(baseClassName)) {
                    throw new IllegalArgumentException("Base conflicts with contract: " + base.getName() + " and " + list[i].get());
                }
                base = Class.forName(baseClassName);
            }
        }

        // if no interfaces found for contract URIs then bail
        if (acc.size() == 0) {
            return base;
        }
        Class[] interfaces = (Class[]) acc.toArray(new Class[acc.size()]);

        // compile a class
        Class cls = ObixAssembler.compile(base, interfaces);
        System.out.println("-- Compile: " + base.getName() + ": " + contract + " -> " + cls.getName());
        return cls;
    }

    /**
     * Convenience for <code>put(new Uri(href), className)</code>.
     */
    public static void put(String href, String className) {
        put(new Uri(href), className);
    }

    /**
     * Register a subclass of Obj for the specified contract uri.
     */
    public static void put(Uri href, String className) {
        if (map.get(href.get()) != null) {
            throw new IllegalStateException("The specified href is already mapped: " + href);
        }
        map.put(href.get(), className);
        cache.clear(); // clear cache
    }
}
