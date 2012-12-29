/*
 * This code licensed to public domain
 */
package bpi.most.obix.asm;

/**
 * @author Brian Frank
 * @version $Revision: 1$ $Date: 6/21/00 2:32:08 PM$
 * @creation 15 Mar 00
 */
public class MethodInfo
        extends MemberInfo {

    public MethodInfo(Assembler asm, int name, int type, int accessFlags) {
        super(asm, name, type, accessFlags);
    }

    public MethodInfo(Assembler asm, String name, String type, int accessFlags) {
        super(asm, name, type, accessFlags);
    }

    public MethodInfo(Assembler asm, int name, int type, int accessFlags, Code code) {
        super(asm, name, type, accessFlags);
        addAttribute(code);
    }

    public MethodInfo(Assembler asm, int name, String type, int accessFlags, Code code) {
        super(asm, name, type, accessFlags);
        addAttribute(code);
    }

    public MethodInfo(Assembler asm, String name, String type, int accessFlags, Code code) {
        super(asm, name, type, accessFlags);
        addAttribute(code);
    }
}
