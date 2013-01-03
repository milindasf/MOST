/*
 * This code licensed to public domain
 */
package bpi.most.obix.asm;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * @author Brian Frank, Alexej Strelzow
 * @version $Revision$ $Date: 29/12/2012
 * @creation 15 Mar 00
 */
public class ConstantPool {

    private Buffer buf = new Buffer();
    private int count = 0;

    private Hashtable<String, Integer> utfTable = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> classTable = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> stringTable = new Hashtable<String, Integer>();
    private Hashtable<Float, Integer> floatTable = new Hashtable<Float, Integer>();
    private Hashtable<Double, Integer> doubleTable = new Hashtable<Double, Integer>();
    private Hashtable<Long, Integer> longTable = new Hashtable<Long, Integer>();
    private IntHashMap integerTable = new IntHashMap();
    private IntHashMap ntTable = new IntHashMap();
    private IntHashMap fieldTable = new IntHashMap();
    private IntHashMap methodTable = new IntHashMap();
    private IntHashMap ifaceTable = new IntHashMap();

////////////////////////////////////////////////////////////////
// Index Access
////////////////////////////////////////////////////////////////

    public int utf(String str) {
        Integer ref = utfTable.get(str);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Utf8);
        buf.utf(str);

        count++;
        utfTable.put(str, count);
        return count;
    }

    public int cls(String className) {
        Integer ref = classTable.get(className);
        if (ref != null) {
			return ref.intValue();
		}

        int i = utf(className);
        buf.u1(Jvm.CONSTANT_Class);
        buf.u2(i);

        count++;
        classTable.put(className, count);
        return count;
    }

    public int string(String str) {
        Integer ref = stringTable.get(str);
        if (ref != null) {
			return ref.intValue();
		}

        int i = utf(str);
        buf.u1(Jvm.CONSTANT_String);
        buf.u2(i);

        count++;
        stringTable.put(str, count);
        return count;
    }

    public int integer(int i) {
        Integer ref = (Integer) integerTable.get(i);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Integer);
        buf.u4(i);

        count++;
        integerTable.put(i, Integer.valueOf(count));
        return count;
    }

    public int floatConst(float f) {
        Float key = f;
        Integer ref = floatTable.get(key);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Float);
        buf.u4(Float.floatToIntBits(f));

        count++;
        floatTable.put(key, count);
        return count;
    }

    public int doubleConst(double d) {
        Double key = d;
        Integer ref = (Integer) doubleTable.get(key);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Double);
        buf.u8(Double.doubleToLongBits(d));

        count++;
        doubleTable.put(key, count);
        count++; // float entries use two slots
        return count - 1;
    }

    public int longConst(long lng) {
        Long key = lng;
        Integer ref = longTable.get(key);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Long);
        buf.u8(lng);

        count++;
        longTable.put(key, count);
        count++; // long entries use two slots
        return count - 1;
    }

    public int nt(int name, int type) {
        int hash = name << 16 | type;
        Integer ref = (Integer) fieldTable.get(hash);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_NameAndType);
        buf.u2(name);
        buf.u2(type);

        count++;
        ntTable.put(hash, count);
        return count;
    }

    public int field(int cls, int nt) {
        int hash = cls << 16 | nt;
        Integer ref = (Integer) fieldTable.get(hash);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Fieldref);
        buf.u2(cls);
        buf.u2(nt);

        count++;
        fieldTable.put(hash, count);
        return count;
    }

    public int method(int cls, int nt) {
        int hash = cls << 16 | nt;
        Integer ref = (Integer) methodTable.get(hash);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_Methodref);
        buf.u2(cls);
        buf.u2(nt);

        count++;
        methodTable.put(hash, count);
        return count;
    }

    public int iface(int cls, int nt) {
        int hash = cls << 16 | nt;
        Integer ref = (Integer) ifaceTable.get(hash);
        if (ref != null) {
			return ref.intValue();
		}

        buf.u1(Jvm.CONSTANT_InterfaceMethodref);
        buf.u2(cls);
        buf.u2(nt);

        count++;
        ifaceTable.put(hash, count);
        return count;
    }

////////////////////////////////////////////////////////////////
// Convenience
////////////////////////////////////////////////////////////////  

    public int cls(Class c) {
        return cls(c.getName().replace('.', '/'));
    }

    public int nt(int name, String type) {
        return nt(name, utf(type));
    }

    public int nt(String name, String type) {
        return nt(utf(name), utf(type));
    }

    public int method(int cls, int name, String type) {
        return method(cls, nt(name, type));
    }

    public int method(int cls, String name, String type) {
        return method(cls, nt(name, type));
    }

    public int method(String cls, String name, String type) {
        return method(cls(cls), nt(name, type));
    }

    public int method(Method m) {
        int cls = cls(m.getDeclaringClass());
        int name = utf(m.getName());
        int type = utf(Jvm.methodDescriptor(m.getParameterTypes(), m.getReturnType()));
        return method(cls, nt(name, type));
    }

    public int iface(int cls, String name, String type) {
        return iface(cls, nt(name, type));
    }

    public int field(int cls, int name, String type) {
        return field(cls, nt(name, type));
    }

    public int field(int cls, String name, String type) {
        return field(cls, nt(name, type));
    }

    public int field(FieldInfo fi) {
        return field(fi.getAsm().getThisClass(), nt(fi.getName(), fi.getType()));
    }

    public Buffer getBuf() {
        return buf;
    }

    public int getCount() {
        return count;
    }
}
