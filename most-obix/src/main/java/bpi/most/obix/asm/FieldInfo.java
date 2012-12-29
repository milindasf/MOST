/*
 * This code licensed to public domain     
 */
package bpi.most.obix.asm;

/**
 * @author Brian Frank, Alexej Strelzow
 * @version $Revision$ $Date: 29/12/2012
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
