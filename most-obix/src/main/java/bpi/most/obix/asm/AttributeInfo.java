/*
 * This code licensed to public domain
 */
package bpi.most.obix.asm;

/**
 * @author Brian Frank, Alexej Strelzow
 * @version $Revision$ $Date: 29/12/2012
 * @creation 15 Mar 00
 */
public class AttributeInfo {

    private static byte[] EMPTY = new byte[0];

    private final Assembler asm;
    private final int name;
    private byte[] info = EMPTY;

    public AttributeInfo(Assembler asm, int name, byte[] info) {
        this.asm = asm;
        this.name = name;
        this.info = info.clone();
    }

    public AttributeInfo(Assembler asm, String name, byte[] info) {
        this.asm = asm;
        this.name = asm.getCp().utf(name);
        this.info = info.clone();
    }

    public AttributeInfo(Assembler asm, String name, String value) {
        this.asm = asm;
        this.name = asm.getCp().utf(name);

        int v = asm.getCp().utf(value);
        this.info = new byte[2];
        info[0] = (byte) ((v >>> 8) & 0xFF);
        info[1] = (byte) ((v >>> 0) & 0xFF);
    }

    public AttributeInfo(Assembler asm, int name) {
        this.asm = asm;
        this.name = name;
    }

    public AttributeInfo(Assembler asm, String name) {
        this.asm = asm;
        this.name = asm.getCp().utf(name);
    }

    void compile(Buffer buf) {
        buf.u2(name);
        buf.u4(info.length);
        buf.append(info);
    }

    public Assembler getAsm() {
        return asm;
    }

    public int getName() {
        return name;
    }
}
