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
public abstract class MemberInfo {

////////////////////////////////////////////////////////////////
// Constructors
////////////////////////////////////////////////////////////////

    MemberInfo(Assembler asm, int name, int type, int accessFlags) {
        this.asm = asm;
        this.cp = asm.cp;
        this.name = name;
        this.type = type;
        this.accessFlags = accessFlags;
    }

    MemberInfo(Assembler asm, String name, String type, int accessFlags) {
        this.asm = asm;
        this.cp = asm.cp;
        this.name = cp.utf(name);
        this.type = cp.utf(type);
        this.accessFlags = accessFlags;
    }

    MemberInfo(Assembler asm, int name, String type, int accessFlags) {
        this.asm = asm;
        this.cp = asm.cp;
        this.name = name;
        this.type = cp.utf(type);
        this.accessFlags = accessFlags;
    }

////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////  

    public void addAttribute(AttributeInfo ai) {
        if (attributes == null) attributes = new ArrayList<AttributeInfo>(5);
        attributes.add(ai);
    }

////////////////////////////////////////////////////////////////
// Compile
////////////////////////////////////////////////////////////////  

    void compile(Buffer buf) {
        int attrLen = attributes == null ? 0 : attributes.size();

        buf.u2(accessFlags);
        buf.u2(name);
        buf.u2(type);
        buf.u2(attrLen);
        for (int i = 0; i < attrLen; ++i)
            ((AttributeInfo) attributes.get(i)).compile(buf);
    }

////////////////////////////////////////////////////////////////
// Fields
////////////////////////////////////////////////////////////////

    public final Assembler asm;
    public final ConstantPool cp;
    public final int name;
    public final int type;
    public final int accessFlags;
    ArrayList<AttributeInfo> attributes;
}
