/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import bpi.most.obix.objects.Enum;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * IOTest verifies ObixEncoder and ObixDecoder
 *
 * @author Haslinger Christian
 * @version $Revision$ $Date$
 * @creation 13 Jan 13
 */
public class IOTest
        extends TestCase {

////////////////////////////////////////////////////////////////
// Test
////////////////////////////////////////////////////////////////

	@Test
    public void testIO() throws Exception {
        // single obj
        verify(new Obj());
        verify(new Obj("foo"));

        // single bool
        verify(new Bool());
        verify(new Bool("foo"));
        verify(new Bool(true));
        verify(new Bool("foo", true));

        // single enum
        verify(new Enum());
        verify(new Enum("foo", ""));
        verify(new Enum("foo", "bar"));

        // single int
        verify(new Int());
        verify(new Int("foo"));
        verify(new Int(77));
        verify(new Int("foo", 77));

        // single real
        verify(new Real());
        verify(new Real("foo"));
        verify(new Real(1.7));
        verify(new Real("foo", 1.7));

        // single str
        verify(new Str());
        verify(new Str("foo", ""));
        verify(new Str("foo", "hello world"));

        // single abstime
        verify(new Abstime());
        verify(new Abstime("foo"));
        verify(new Abstime("foo", System.currentTimeMillis()));

        // single reltime
        verify(new Reltime());
        verify(new Reltime("foo"));
        verify(new Reltime("foo", 60000L));

        // single uri
        verify(new Uri());
        verify(new Uri("foo", ""));
        verify(new Uri("foo", "obix:foo"));

        // single op
        verify(new Op());
        verify(new Op("foo"));
        verify(new Op("foo", new Contract("obix:Foo"), new Contract("obix:Bar")));
        verify(new Op("foo", new Contract("obix:Foo obix:Ray"), new Contract("obix:Bar obix:Boo")));

        // single feed
        verify(new Feed());
        verify(new Feed("foo"));
        verify(new Feed("foo", new Contract("obix:Foo"), null));
        verify(new Feed("foo", null, new Contract("obix:Bar")));
        verify(new Feed("foo", new Contract("obix:Foo"), new Contract("obix:Bar")));

        // single ref
        verify(new Ref());
        verify(new Ref("foo"));
        verify(new Ref("foo", new Uri("/obix/foo")));

        // single err
        verify(new Err());
        verify(new Err("foo"));

        // href
        Obj x = new Obj();
        x.setHref(new Uri("/obix/boo"));
        verify(x);

        // is
        x = new Obj();
        x.setIs(new Contract("/obix/boo obix:Hanover"));
        verify(x);

        // single list
        verify(new List());
        verify(new List("coolList"));
        verify(new List("coolList", new Contract("obix:Uri")));

        // complex
        x = new Obj();
        x.add(new Obj("foo"));
        x.add(new Int("bar", 1972));
        x.add(new Real("moo", 0.5));
        x.add(new Str("description", "Pretty cool"));
        x.add(new List("items", new Contract("obix:Str")));
        x.add(new Op("doIt", new Contract("obix:Str"), new Contract("obix:Int")));
        x.add(new Feed("yourFired", new Contract("obix:Nil"), new Contract("obix:Int")));
        verify(x);

        // verify facets: display
        x = new Obj();
        x.setDisplay("da display");
        verify(x);

        // verify facets: displayName
        x = new Obj();
        x.setDisplayName("da displayName");
        verify(x);

        // verify facets: null
        x = new Obj();
        x.setNull(true);
        verify(x);

        // verify facets: icon
        x = new Obj();
        x.setIcon(new Uri("/icon.png"));
        verify(x);


        // verify facets: writable
        x = new Obj();
        x.setWritable(true);
        verify(x);

        // verify facets: status
        x = new Obj();
        for (int i = 0; i < Status.list().length; ++i) {
            x.setStatus(Status.list()[i]);
            verify(x);
        }

        // verify facets: Bool.range
        Bool b = new Bool();
        b.setRange(new Uri("/myrange"));
        verify(b);

        // verify facets: Int.min
        Int i = new Int();
        i.setMin(0);
        verify(i);

        // verify facets: Int.max
        i = new Int();
        i.setMax(88);
        verify(i);

        // verify facets: Int.unit
        i = new Int();
        i.setUnit(new Uri("obix:Celsius"));
        verify(i);

        // verify facets: Real.min
        Real r = new Real();
        r.setMin(0);
        verify(r);

        // verify facets: Real.max
        r = new Real();
        r.setMax(88);
        verify(r);

        // verify facets: Real.unit
        r = new Real();
        r.setUnit(new Uri("obix:Celsius"));
        verify(r);

        // verify facets: Real.precision
        r = new Real();
        r.setPrecision(3);
        verify(r);

        // verify facets: Enum.range
        Enum e = new Enum();
        e.setRange(new Uri("/myrange"));
        verify(e);

        // verify facets: Str.min
        Str s = new Str();
        s.setMin(1);
        verify(s);

        // verify facets: Str.max
        s = new Str();
        s.setMax(20);
        verify(s);

        // verify facets: Reltime.min
        Reltime rt = new Reltime();
        rt.setMin(new Reltime(99));
        verify(rt);

        // verify facets: Reltime.max
        rt = new Reltime();
        rt.setMax(new Reltime(-8));
        verify(rt);

        // verify facets: Abstime.min
        Abstime a = new Abstime();
        a.setMin(new Abstime(System.currentTimeMillis()));
        verify(a);

        // verify facets: Abstime.max
        a = new Abstime();
        a.setMax(new Abstime(System.currentTimeMillis()));
        verify(a);

        // verify facets: List.min
        List l = new List();
        l.setMin(1);
        verify(l);

        // verify facets: List.max
        l = new List();
        l.setMax(20);
        verify(l);

        // verify with unknown elements
        Obj obj = make("<obj><what/><int name='i' val='3'/></obj>");
        assertTrue(obj.size() == 1);
        assertTrue(obj.get("i").getInt() == 3);
    }

////////////////////////////////////////////////////////////////
// Utils
////////////////////////////////////////////////////////////////

    public void verify(Obj orig) throws Exception {
        roundtrip(orig);
    }

    public Obj make(String xml) throws Exception {
        return make(xml, true);
    }

    public Obj make(String xml, boolean useContracts) throws Exception {
        ObixDecoder decoder = new ObixDecoder(new ByteArrayInputStream(xml.getBytes()));
        decoder.setUseContracts(useContracts);
        return decoder.decodeDocument();
    }
    
    public Obj roundtrip(Obj orig)
            throws Exception {
        // encode to byte array
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObixEncoder out = new ObixEncoder(bout);
        out.encodeDocument(orig);
        out.flush();
        out.close();

        byte[] buf = bout.toByteArray();

        // decode from byte array
        ByteArrayInputStream bin = new ByteArrayInputStream(buf);
        ObixDecoder in = new ObixDecoder(bin);
        Obj piped = in.decodeDocument();

        // verify original is same as that piped thru IO
        if (verbose) {
            System.out.println("============");
            System.out.write(buf);
            ObixEncoder.dump(piped);
        }
        verifyEquivalent(orig, piped);
        return piped;
    }
    
    public void verifyEquivalent(Obj a, Obj b) {
        if (verbose) {
			System.out.println("  " + a + " ?= " + b);
		}

        // identity
        assertTrue(a.getClass() == b.getClass());
        verify(a.getName(), b.getName());
        verify(a.getHref(), b.getHref());
        verify(a.getIs(), b.getIs());

        // value
        if (a instanceof Val) {
			verify(((Val) a).encodeVal(), ((Val) b).encodeVal());
		}

        // list
        if (a instanceof List) {
            verify(((List) a).getOf(), ((List) b).getOf());
        }

        // op
        if (a instanceof Op) {
            verify(((Op) a).getIn(), ((Op) b).getIn());
            verify(((Op) a).getOut(), ((Op) b).getOut());
        }

        // event
        if (a instanceof Feed) {
            verify(((Feed) a).getIn(), ((Feed) b).getIn());
            verify(((Feed) a).getOf(), ((Feed) b).getOf());
        }

        // facets
        verify(a.getDisplay(), b.getDisplay());
        verify(a.getDisplayName(), b.getDisplayName());
        verify(a.getIcon(), b.getIcon());
        assertTrue(a.getStatus() == b.getStatus());
        assertTrue(a.isNull() == b.isNull());
        assertTrue(a.isWritable() == b.isWritable());
        if (a instanceof Bool) {
            verify(((Bool) a).getRange(), ((Bool) b).getRange());
        } else if (a instanceof Enum) {
            verify(((Enum) a).getRange(), ((Enum) b).getRange());
        } else if (a instanceof Int) {
        	assertTrue(((Int) a).getMin() == ((Int) b).getMin());
        	assertTrue(((Int) a).getMax() == ((Int) b).getMax());
            verify(((Int) a).getUnit(), ((Int) b).getUnit());
        } else if (a instanceof Real) {
        	assertTrue(((Real) a).getMin() == ((Real) b).getMin());
        	assertTrue(((Real) a).getMax() == ((Real) b).getMax());
            verify(((Real) a).getUnit(), ((Real) b).getUnit());
            assertTrue(((Real) a).getPrecision() == ((Real) b).getPrecision());
        } else if (a instanceof Str) {
        	assertTrue(((Str) a).getMin() == ((Str) b).getMin());
        	assertTrue(((Str) a).getMax() == ((Str) b).getMax());
        } else if (a instanceof Reltime) {
            verify(((Reltime) a).getMin(), ((Reltime) b).getMin());
            verify(((Reltime) a).getMax(), ((Reltime) b).getMax());
        } else if (a instanceof Abstime) {
            verify(((Abstime) a).getMin(), ((Abstime) b).getMin());
            verify(((Abstime) a).getMax(), ((Abstime) b).getMax());
        } else if (a instanceof List) {
        	assertTrue(((List) a).getMin() == ((List) b).getMin());
        	assertTrue(((List) a).getMax() == ((List) b).getMax());
        }

        // recurse children
        Obj[] akids = a.list();
        Obj[] bkids = b.list();
        assertTrue(akids.length == bkids.length);
        for (int i = 0; i < akids.length; ++i) {
			verifyEquivalent(akids[i], bkids[i]);
		}
    }

    public void verify(Object a, Object b) {
        if (a == null) {
			assertTrue(b == null);
		} else if (b == null) {
			assertTrue(a == null);
		} else {
			assertTrue(a.equals(b));
		}
    }

	static boolean verbose;

}
