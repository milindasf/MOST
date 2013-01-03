/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import bpi.most.obix.io.ObixEncoder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Obj is the base class for representing Obix objects and
 * managing their tree structure.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public class Obj
        implements IObj {

    private String name;
    private Uri href;
    private Contract is;
    private Obj parent;
    private HashMap<String, Obj> kidsByName;
    private Obj kidsHead, kidsTail;
    private int kidsCount;
    private Obj prev, next;
    private Status status = Status.ok;
    private String display;
    private String displayName;
    private Uri icon;
    private boolean writable;
    private boolean isNull;

////////////////////////////////////////////////////////////////
// Factory
////////////////////////////////////////////////////////////////

    /**
     * Get an Obj Class for specified element name or return
     * null if not a oBIX element name.
     */
    public static Class toClass(String elemName) {
        return (Class) elemNameToClass.get(elemName);
    }

    /**
     * Convenience for <code>toClass(elemName).newInstance()</code>.
     * This method will return null if elemName is not a oBIX
     * element name.
     */
    public static Obj toObj(String elemName) {
        Class cls = toClass(elemName);
        if (cls == null) {
            return null;
        }
        try {
            return (Obj) cls.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }
    }

    static HashMap<String, Class> elemNameToClass = new HashMap<String, Class>();

    static {
        elemNameToClass.put("obj", Obj.class);
        elemNameToClass.put("str", Str.class);
        elemNameToClass.put("bool", Bool.class);
        elemNameToClass.put("int", Int.class);
        elemNameToClass.put("enum", Enum.class);
        elemNameToClass.put("list", List.class);
        elemNameToClass.put("real", Real.class);
        elemNameToClass.put("uri", Uri.class);
        elemNameToClass.put("abstime", Abstime.class);
        elemNameToClass.put("reltime", Reltime.class);
        elemNameToClass.put("op", Op.class);
        elemNameToClass.put("ref", Ref.class);
        elemNameToClass.put("err", Err.class);
        elemNameToClass.put("feed", Feed.class);
        // my extension
        elemNameToClass.put("dp", Dp.class);
        elemNameToClass.put("dpData", DpData.class);
        elemNameToClass.put("zone", Zone.class);
    }

////////////////////////////////////////////////////////////////
// Constructor
////////////////////////////////////////////////////////////////

    /**
     * Construct a named Obj.
     */
    public Obj(String name) {
        this.name = name;
    }

    /**
     * Construct an unnamed Obj.
     */
    public Obj() {
        this(null);
    }

////////////////////////////////////////////////////////////////
// Identity
////////////////////////////////////////////////////////////////

    /**
     * Get element type name of this object.
     */
    public String getElement() {
        return "obj";
    }

    /**
     * Get name of this Obj or null if unnamed.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of this Obj.  The name may only be set if
     * name is currently null and this Obj hasn't been added
     * a child to another Obj yet.
     */
    public void setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("name is already set");
        }
        if (this.parent != null) {
            throw new IllegalStateException("obj is already parented");
        }
        this.name = name;
    }

    /**
     * Return parent object or null if unparented.
     */
    public Obj getParent() {
        return parent;
    }

    /**
     * Get the root parent of this object.
     */
    public Obj getRoot() {
        if (parent == null) {
            return this;
        } else {
            return parent.getRoot();
        }
    }

    /**
     * Get uri of this object is well known otherwise return null.
     */
    public Uri getHref() {
        return href;
    }

    /**
     * Get the absolute normalized href of this object, based
     * on the href of this obj's root object.  Return null if this
     * object doesn't have an href.
     */
    public Uri getNormalizedHref() {
        if (href == null) {
            return null;
        }
        return href.normalize(getRoot().getHref());
    }

    /**
     * Set uri of this object.
     */
    public void setHref(Uri href) {
        this.href = href;
    }

    /**
     * Convenience for <code>is(new Uri(uri))</code>.
     */
    public boolean is(String uri) {
        return is(new Uri(uri));
    }

    /**
     * Return if the contract list defined by the is attribute
     * contains the specified URI.
     */
    public boolean is(Uri uri) {
        if (is == null) {
            return false;
        }
        return is.contains(uri);
    }

    /**
     * Get the Contracts list this obj supports.
     */
    public Contract getIs() {
        return is;
    }

    /**
     * Set the contracts list this obj supports.
     */
    public void setIs(Contract is) {
        this.is = is;
    }

////////////////////////////////////////////////////////////////
// Convenience
////////////////////////////////////////////////////////////////

    /**
     * Return if this is an instance of Val
     */
    public boolean isVal() {
        return this instanceof Val;
    }

    /**
     * Return if this is an instance of Bool
     */
    public boolean isBool() {
        return this instanceof Bool;
    }

    /**
     * Return if this is an instance of Int
     */
    public boolean isInt() {
        return this instanceof Int;
    }

    /**
     * Return if this is an instance of Real
     */
    public boolean isReal() {
        return this instanceof Real;
    }

    /**
     * Return if this is an instance of Enum
     */
    public boolean isEnum() {
        return this instanceof Enum;
    }

    /**
     * Return if this is an instance of Str
     */
    public boolean isStr() {
        return this instanceof Str;
    }

    /**
     * Return if this is an instance of Abstime
     */
    public boolean isAbstime() {
        return this instanceof Abstime;
    }

    /**
     * Return if this is an instance of Reltime
     */
    public boolean isReltime() {
        return this instanceof Reltime;
    }

    /**
     * Return if this is an instance of Uri
     */
    public boolean isUri() {
        return this instanceof Uri;
    }

    /**
     * Return if this is an instance of List
     */
    public boolean isList() {
        return this instanceof List;
    }

    /**
     * Return if this is an instance of Op
     */
    public boolean isOp() {
        return this instanceof Op;
    }

    /**
     * Return if this is an instance of Ref
     */
    public boolean isRef() {
        return this instanceof Ref;
    }

    /**
     * Return if this is an instance of Feed
     */
    public boolean isFeed() {
        return this instanceof Feed;
    }

    /**
     * Return if this is an instance of Err
     */
    public boolean isErr() {
        return this instanceof Err;
    }

    /**
     * Convenience for getting the value as if a Bool
     */
    public boolean getBool() {
        return ((Bool) this).get();
    }

    /**
     * Convenience for getting the value as if an Int
     */
    public long getInt() {
        return ((Int) this).get();
    }

    /**
     * Convenience for getting the value as if a Real
     */
    public double getReal() {
        return ((Real) this).get();
    }

    /**
     * Convenience for getting the value as if a Str
     */
    public String getStr() {
        return ((Str) this).get();
    }

    /**
     * Convenience for setting the value as if a Bool
     */
    public void setBool(boolean val) {
        ((Bool) this).set(val);
    }

    /**
     * Convenience for setting the value as if an Int
     */
    public void setInt(long val) {
        ((Int) this).set(val);
    }

    /**
     * Convenience for setting the value as if a Real
     */
    public void setReal(double val) {
        ((Real) this).set(val);
    }

    /**
     * Convenience for setting the value as if a Str
     */
    public void setStr(String val) {
        ((Str) this).set(val);
    }

////////////////////////////////////////////////////////////////
// Facets
////////////////////////////////////////////////////////////////

    /**
     * Get the display string for this obj.  If the display
     * facet is specified return it, otherwise return type
     * information.
     */
    public String toDisplayString() {
        if (display != null) {
            return display;
        }
        if (this instanceof Val) {
            return ((Val) this).encodeVal();
        }
        if (is != null && is.size() > 0) {
            return is.toString();
        }
        return "obix:" + getElement();
    }

    /**
     * Get display String or null if not specified.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Set display string or null if not specified.
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * If displayName is specified return it, otherwise
     * return name.
     */
    public String toDisplayName() {
        if (displayName != null) {
            return displayName;
        }
        if (name != null) {
            return name;
        }
        return getElement();
    }

    /**
     * Get displayName String or null if not specified.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set displayName string or null if not specified.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get icon URI or null if not specified.
     */
    public Uri getIcon() {
        return icon;
    }

    /**
     * Set icon URI or null if not specified.
     */
    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    /**
     * Get status for this object.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set status for this object.  If null is passed,
     * then status is to Status.ok.
     */
    public void setStatus(Status status) {
        if (status == null) {
            status = Status.ok;
        }
        this.status = status;
    }

    /**
     * Get null flag or default to false.
     */
    public boolean isNull() {
        return isNull;
    }

    /**
     * Set null flag.
     */
    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    /**
     * Get writable flag or default to false.
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * Convenience for <code>setWritable(writable, false)</code>.
     */
    public void setWritable(boolean writable) {
        setWritable(writable, false);
    }

    /**
     * Set writable flag.  If recursive is true, then
     * recursively call setWritable on all this object's
     * children.
     */
    public void setWritable(boolean writable, boolean recursive) {
        this.writable = writable;
        if (recursive) {
            Obj[] kids = list();
            for (int i = 0; i < kids.length; ++i) {
                kids[i].setWritable(writable, recursive);
            }
        }
    }

////////////////////////////////////////////////////////////////
// Children
////////////////////////////////////////////////////////////////

    /**
     * Return if this object has a sub object by the specified name.
     */
    public boolean has(String name) {
        if (kidsByName == null) {
            return false;
        }
        return kidsByName.get(name) != null;
    }

    /**
     * Get a sub object by name or return null.
     */
    public Obj get(String name) {
        if (kidsByName == null) {
            return null;
        }
        return (Obj) kidsByName.get(name);
    }

    /**
     * Lookup a sub object by name and return it's href.
     * If the child doesn't exist or has a null href, then
     * throw an exception.
     */
    public Uri getChildHref(String name) {
        Obj kid = get(name);
        if (kid == null) {
            throw new IllegalStateException("Missing child object: " + name);
        }
        if (kid.getHref() == null) {
            throw new IllegalStateException("Child missing href : " + name);
        }
        return kid.getNormalizedHref();
    }

    /**
     * Return number of child objects.
     */
    public int size() {
        return kidsCount;
    }

    /**
     * Get an array of all the children.
     */
    public Obj[] list() {
        Obj[] list = new Obj[kidsCount];
        int n = 0;
        for (Obj p = kidsHead; p != null; p = p.next) {
            list[n++] = p;
        }
        return list;
    }

    /**
     * Get all the children which are instances of the
     * specified class.  The return array will be of
     * the specified class.
     */
    public Object[] list(Class cls) {
        Object[] temp = new Object[kidsCount];
        int count = 0;
        for (Obj p = kidsHead; p != null; p = p.next) {
            if (cls.isInstance(p)) {
                temp[count++] = p;
            }
        }

        Object[] result = (Object[]) Array.newInstance(cls, count);
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    /**
     * Convenience for <code>kid.setName(name); add(kid);</code>.
     * The specified kid must be unnamed.  Return this.
     */
    public Obj add(String name, Obj kid) {
        kid.setName(name);
        return add(kid);
    }

    /**
     * Add a child Obj.  Return this.
     */
    public Obj add(Obj kid) {
        // sanity check
        if (kid.parent != null || kid.prev != null || kid.next != null) {
            throw new IllegalStateException("Child is already parented");
        }
        if (kid.name != null && kidsByName != null && kidsByName.containsKey(kid.name)) {
            throw new IllegalStateException("Duplicate child name '" + kid.name + "'");
        }

        // if named, add to name map
        if (kid.name != null) {
            if (kidsByName == null) {
                kidsByName = new HashMap<String, Obj>();
            }
            kidsByName.put(kid.name, kid);
        }

        // add to ordered linked list
        if (kidsTail == null) {
            kidsHead = kidsTail = kid;
        } else {
            kidsTail.next = kid;
            kid.prev = kidsTail;
            kidsTail = kid;
        }

        // update kid's references and count
        kid.parent = this;
        kidsCount++;
        return this;
    }

    /**
     * Add all the specified objects as my children. Return this.
     */
    public Obj addAll(Obj[] kids) {
        for (int i = 0; i < kids.length; ++i) {
            add(kids[i]);
        }
        return this;
    }

    /**
     * Remove the specified child Obj.
     */
    public void remove(Obj kid) {
        // sanity checks
        if (kid.parent != this) {
            throw new IllegalStateException("Not parented by me");
        }

        // remove from name map if applicable
        if (kid.name != null) {
            kidsByName.remove(kid.name);
        }

        // remove from linked list
        if (kidsHead == kid) {
            kidsHead = kid.next;
        } else {
            kid.prev.next = kid.next;
        }
        if (kidsTail == kid) {
            kidsTail = kid.prev;
        } else {
            kid.next.prev = kid.prev;
        }

        // clear kid's references and count
        kid.parent = null;
        kid.prev = null;
        kid.next = null;
        kidsCount--;
    }

    /**
     * Replace the old obj with the newObj (they must have
     * the same name).
     */
    public void replace(Obj oldObj, Obj newObj) {
        if (!oldObj.name.equals(newObj.name)) {
            throw new IllegalStateException("Mismatched names: " + oldObj.name + " != " + newObj.name);
        }

        // sanity checks
        if (oldObj.parent != this) {
            throw new IllegalStateException("oldObj not parented by me");
        }
        if (newObj.parent != null) {
            throw new IllegalStateException("newObj already parented");
        }

        // replace in map
        kidsByName.put(newObj.name, newObj);

        // replace in linked list
        newObj.parent = this;
        newObj.prev = oldObj.prev;
        if (newObj.prev != null) {
            newObj.prev.next = newObj;
        }
        newObj.next = oldObj.next;
        if (newObj.next != null) {
            newObj.next.prev = newObj;
        }
        if (kidsHead == oldObj) {
            kidsHead = newObj;
        }
        if (kidsTail == oldObj) {
            kidsTail = newObj;
        }

        // clear oldObj
        oldObj.parent = null;
        oldObj.prev = null;
        oldObj.next = null;
    }

    /**
     * Convenience for <code>getParent().remove(this)</code>.
     */
    public void removeThis() {
        if (parent == null) {
            throw new IllegalStateException("Not parented");
        }

        parent.remove(this);
    }

////////////////////////////////////////////////////////////////
// Debug
////////////////////////////////////////////////////////////////

    /**
     * Get a debug string.
     */
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("<").append(getElement());
        if (name != null) {
            s.append(" name=\"").append(name).append('"');
        }
        if (href != null) {
            s.append(" href=\"").append(href.get()).append('"');
        }
        if (this instanceof Val) {
            s.append(" val=\"").append(((Val) this).encodeVal()).append('"');
        }
        s.append("/>");
        return s.toString();
    }

    /**
     * Dump in XML to standard output.
     */
    public void dump() {
        ObixEncoder.dump(this);
    }

    @SuppressWarnings("unchecked")
    public <T> java.util.List<T> getKidsByClass(Class<T> clazz) {
        java.util.List<T> matches = new ArrayList<T>();

        for (Obj kid : kidsByName.values()) {
            if (kid.getClass().equals(clazz)) {
                matches.add((T)kid);
            }
        }

        return matches;
    }
}
