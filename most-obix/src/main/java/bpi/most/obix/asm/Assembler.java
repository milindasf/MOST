/*
 * This code licensed to public domain
 */
package bpi.most.obix.asm;

import java.util.ArrayList;

/**
 * @author Brian Frank, Alexej Strelzow
 * @version $Revision$ $Date: 29/12/2012
 * @creation 15 Mar 00
 */
public class Assembler {

    private final int thisClass;
    private final int superClass;
    private final int[] interfaces;
    private final int accessFlags;
    private final ConstantPool cp = new ConstantPool();

    private ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
    private ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();
    private ArrayList<AttributeInfo> attributes = new ArrayList<AttributeInfo>();

    public Assembler(String thisClass, Class superClass,
                     int accessFlags, Class[] interfaces) {
        this.thisClass = cp.cls(thisClass);
        this.superClass = (superClass == null) ?
                0 :                 // java.lang.Object
                cp.cls(superClass); // anything else

        this.accessFlags = accessFlags;
        this.interfaces = new int[interfaces == null ? 0 : interfaces.length];

        for (int i = 0; i < this.interfaces.length; ++i)      {
            this.interfaces[i] = cp.cls(interfaces[i]);
        }
    }

    public void addField(FieldInfo f) {
        fields.add(f);
    }

    public void addMethod(MethodInfo m) {
        methods.add(m);
    }

    public void addAttribute(AttributeInfo a) {
        attributes.add(a);
    }

    public int getThisClass() {
        return thisClass;
    }

    public int getSuperClass() {
        return superClass;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public ConstantPool getCp() {
        return cp;
    }

    public Buffer compile() {
        Buffer buf = new Buffer();

        buf.u4(Jvm.MAGIC);          // magic
        buf.u2(Jvm.MINOR_VERSION);  // minor version
        buf.u2(Jvm.MAJOR_VERSION);  // major version
        buf.u2(cp.count + 1);         // constant pool count
        buf.append(cp.buf);         // contant pool
        buf.u2(accessFlags);        // access flags
        buf.u2(thisClass);          // this class
        buf.u2(superClass);         // super class

        // interfaces
        buf.u2(interfaces.length);
        for (int i = 0; i < interfaces.length; ++i) {
            buf.u2(interfaces[i]);
        }

        // fields
        buf.u2(fields.size());
        for (int i = 0; i < fields.size(); ++i) {
            ((FieldInfo) fields.get(i)).compile(buf);
        }

        // methods
        buf.u2(methods.size());
        for (int i = 0; i < methods.size(); ++i) {
            ((MethodInfo) methods.get(i)).compile(buf);
        }

        // attributes
        buf.u2(attributes.size());
        for (int i = 0; i < attributes.size(); ++i) {
            ((AttributeInfo) attributes.get(i)).compile(buf);
        }

        return buf;
    }

}
