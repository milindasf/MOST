/*
 * This code licensed to public domain     
 */
package bpi.most.obix.asm;

/**
 * @author Brian Frank
 * @version $Revision: 1$ $Date: 6/21/00 2:32:07 PM$
 * @creation 15 Mar 00
 */
public class FieldInfo
        extends MemberInfo {

    public FieldInfo(Assembler asm, int name, int type, int accessFlags) {
        super(asm, name, type, accessFlags);
    }

    public FieldInfo(Assembler asm, String name, String type, int accessFlags) {
        super(asm, name, type, accessFlags);
    }

}
