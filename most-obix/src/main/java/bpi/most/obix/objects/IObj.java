/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;


/**
 * IObj is the base interface for contract interfaces.  IObj should
 * only be implemented by subclasses of Obj.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 27 Apr 05
 */
public interface IObj {

	////////////////////////////////////////////////////////////////
	// Identity
	////////////////////////////////////////////////////////////////

	String getName();

	Obj getParent();

	Obj getRoot();

	Uri getHref();

	Uri getNormalizedHref();

	void setHref(Uri href);

	Contract getIs();

	void setIs(Contract is);

	////////////////////////////////////////////////////////////////
	// Convenience
	////////////////////////////////////////////////////////////////

	boolean isVal();

	boolean isBool();

	boolean isInt();

	boolean isReal();

	boolean isEnum();

	boolean isStr();

	boolean isAbstime();

	boolean isReltime();

	boolean isUri();

	boolean isList();

	boolean isOp();

	boolean isRef();

	boolean isFeed();

	boolean isErr();

	boolean getBool();

	long getInt();

	double getReal();

	String getStr();

	void setBool(boolean val);

	void setInt(long val);

	void setReal(double val);

	void setStr(String val);

	////////////////////////////////////////////////////////////////
	// Facets
	////////////////////////////////////////////////////////////////

	String toDisplayString();

	String getDisplay();

	void setDisplay(String display);

	String toDisplayName();

	String getDisplayName();

	void setDisplayName(String displayName);

	Uri getIcon();

	void setIcon(Uri icon);

	Status getStatus();

	void setStatus(Status status);

	boolean isNull();

	void setNull(boolean isNull);

	boolean isWritable();

	void setWritable(boolean writable);

	void setWritable(boolean writable, boolean recursive);

	////////////////////////////////////////////////////////////////
	// Children
	////////////////////////////////////////////////////////////////

	Obj get(String name);

	int size();

	Obj[] list();

	Obj add(Obj kid);

	Obj addAll(Obj[] kid);

	void remove(Obj kid);

	void replace(Obj oldObj, Obj newObj);

	void removeThis();

}
