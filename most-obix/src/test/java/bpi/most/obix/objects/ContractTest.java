/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import bpi.most.obix.contracts.About;
import bpi.most.obix.contracts.AckAlarm;
import bpi.most.obix.contracts.Alarm;
import bpi.most.obix.contracts.Dimension;
import bpi.most.obix.contracts.History;
import bpi.most.obix.contracts.Lobby;
import bpi.most.obix.contracts.Point;
import bpi.most.obix.contracts.PointAlarm;
import bpi.most.obix.contracts.Unit;
import bpi.most.obix.contracts.WritablePoint;
import bpi.most.obix.io.ObixDecoder;
import bpi.most.obix.io.ObixEncoder;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * ContractTest tests contracts and using them to map to specified types.
 *
 * @author Brian Frank
 * @version $Revision$ $Date$
 * @creation 21 Sept 05
 */
public class ContractTest
        extends TestCase {

////////////////////////////////////////////////////////////////
// Main
////////////////////////////////////////////////////////////////  

    @Test
    public void testContract()
            throws Exception {
        /*
        // verify !useContracts flag
        assertTrue(!(make("<obj is='obix:Point'/>", false) instanceof Point));
        assertTrue(!(make("<obj is='obix:History obix:Point'/>", false) instanceof Point));
        assertTrue(!(make("<obj is='obix:History obix:Point'/>", false) instanceof History));
        assertTrue(!(make("<real is='obix:History obix:Point'/>", false) instanceof History));
        assertTrue((make("<real is='obix:History obix:Point'/>", false) instanceof Real));

        // verify using single marker contract
        assertTrue(make("<obj is='obix:Point'/>") instanceof Point);
        assertTrue(make("<obj is='obix:Point'/>") instanceof Obj);
        assertTrue(make("<obj is='obix:Point'/>").getClass().getSuperclass() == Obj.class);

        // verify using single marker contract with base covariance
        assertTrue(make("<real is='obix:Point'/>") instanceof Point);
        assertTrue(make("<real is='obix:Point'/>") instanceof Obj);
        assertTrue(make("<real is='obix:Point'/>") instanceof Real);
        assertTrue(make("<int  is='obix:Point'/>").getClass().getSuperclass() == Int.class);

        // verify structure - Dimension should have 7 Int
        // children added in it's constructor along with
        // their associated getter
        Dimension dim = (Dimension) make("<obj is='obix:Dimension'/>");
        assertTrue(dim.getClass().getSuperclass() == Obj.class);
        assertTrue(dim.size() == 7);
        assertTrue(dim.kg().get() == 0L);
        assertTrue(dim.kg().getClass() == Int.class);
        assertTrue(dim.m().get() == 0L);
        assertTrue(dim.sec().get() == 0L);
        assertTrue(dim.k().get() == 0L);
        assertTrue(dim.a().get() == 0L);
        assertTrue(dim.mol().get() == 0L);
        assertTrue(dim.cd().get() == 0L);
        dim.m().set(1);
        dim.sec().set(-2);
        dim = (Dimension) roundtrip((Obj) dim);
        assertTrue(dim.m().get() == 1);
        assertTrue(dim.sec().get() == -2);

        // verify contract (Unit) which contains child with
        // another contract (Dimension)
        Unit unit = (Unit) make("<obj is='obix:Unit'/>");
        assertTrue(unit.getClass().getSuperclass() == Obj.class);
        assertTrue(unit.size() == 4);
        assertTrue(unit.symbol().get().equals(""));
        assertTrue(unit.offset().get() == 0d);
        assertTrue(unit.scale().get() == 1d); // default val=1
        assertTrue(unit.dimension() instanceof Dimension); // is='obix:Dimension'
        assertTrue(unit.dimension() instanceof Obj);
        assertTrue(unit.dimension().getIs().toString().equals("obix:Dimension"));

        // now try parsing a unit with children filled in
        unit = (Unit) make("<obj is='obix:Unit'>" +
                "<str name='symbol' val='\u00baC'/>" +
                "<obj name='dimension'><int name='K' val='1'/></obj>" +
                "<real name='offset' val='273'/>" +
                "</obj>");
        assertTrue(unit.symbol().get().equals("\u00baC"));
        assertTrue(unit.offset().get() == 273);
        assertTrue(unit.scale().get() == 1d); // default val=1
        assertTrue(unit.dimension().k().get() == 1);
        assertTrue(unit.dimension().kg().get() == 0);

        // round IO trip the unit we just did
        unit.dimension().k().set(2);
        unit.dimension().kg().set(-8);
        unit.scale().set(77d);
        unit = (Unit) roundtrip((Obj) unit);
        assertTrue(unit.symbol().get().equals("\u00baC"));
        assertTrue(unit.offset().get() == 273);
        assertTrue(unit.scale().get() == 77d);
        assertTrue(unit.dimension().k().get() == 2);
        assertTrue(unit.dimension().kg().get() == -8);


        // verify we can take a sophisticated nested contract
        // like Unit and apply it to a different base element type
        unit = (Unit) make("<str is='obix:Unit' val='just messing with you'/>");
        assertTrue(unit.getClass().getSuperclass() == Str.class);
        assertTrue(unit.getStr().equals("just messing with you"));

        // verify some facets defined in a contract
        History history = (History) make("<obj is='obix:History'/>");
        assertTrue(history.count().getMin() == 0);

        // verify op in/out defined in a contract
        WritablePoint wpt = (WritablePoint) make("<obj is='obix:WritablePoint'/>");
        assertEquals(wpt.writePoint().getIn(), new Contract("obix:WritePointIn"));
        assertEquals(wpt.writePoint().getOut(), new Contract("obix:Point"));

        // verify mixins
        Enum pt = (Enum) make("<enum is='obix:Point obix:History' val='off'/>");
        assertEquals(pt.get(), "off");
        assertTrue(pt instanceof Point);
        assertTrue(pt instanceof History);
        assertEquals(((History) pt).count().get(), 0L);

        // verify list of
        List list = (List) make("<list of='obix:Unit'><obj><str name='symbol' val='xyz'/></obj></list>");
        Obj[] listVals = list.list();
        assertTrue(listVals.length == 1);
        assertTrue(listVals[0] instanceof Unit);
        assertEquals(((Unit) listVals[0]).symbol().get(), "xyz");

        // verify list of mixed
        list = (List) make("<list of='obix:Point'><obj/><int is='obix:Unit obix:Point'/></list>");
        listVals = list.list();
        assertTrue(listVals.length == 2);
        assertEquals(listVals[0].getClass().getSuperclass(), Obj.class);
        assertTrue(listVals[0] instanceof Point);
        assertEquals(listVals[1].getClass().getSuperclass(), Int.class);
        assertTrue(listVals[1] instanceof Int);
        assertTrue(listVals[1] instanceof Point);
        assertTrue(listVals[1] instanceof Unit);

        // verify an instance which covariantly changes it's
        // element type from an inherited contract
        PointAlarm ptAlarm = (PointAlarm) make("<obj is='obix:PointAlarm'><real name='alarmValue' val='-1'/></obj>");
        assertTrue(ptAlarm instanceof Alarm);
        assertTrue(ptAlarm.timestamp() instanceof Abstime);
        assertTrue(ptAlarm.alarmValue() instanceof Real);
        assertTrue(ptAlarm.alarmValue().getReal() == -1d);
        assertEquals(ptAlarm.alarmValue().getName(), "alarmValue");
        assertTrue(ptAlarm.alarmValue().getParent() == ptAlarm);

        // verify a ref with an is attribute
        Ref ref = (Ref) make("<ref is='obix:Point'/>");
        assertEquals(ref.getIs(), new Contract("obix:Point"));

        // verify a Lobby contract with ref to About
        Lobby lobby = (Lobby) make("<obj is='obix:Lobby'/>");
        assertTrue(lobby.about() instanceof Ref);
        assertEquals(lobby.about().getIs(), new Contract("obix:About"));
        assertTrue(!(lobby.about() instanceof About));

        // verify null is cleared implicitly with val
        AckAlarm alarm = (AckAlarm) make("<obj is='obix:AckAlarm'/>");
        assertTrue(alarm.ackTimestamp().isNull());
        assertTrue(alarm.ackTimestamp().getMillis() == 0);
        assertTrue(alarm.ackUser().isNull());
        assertTrue(alarm.ackUser().get().equals(""));
        alarm = (AckAlarm) make("<obj is='obix:AckAlarm'>" +
                "<abstime name='ackTimestamp' val='2005-09-21T13:14:02.12Z'/>" +
                "<str name='ackUser' val='Fred'/></obj>");
        assertTrue(!alarm.ackTimestamp().isNull());
        assertTrue(alarm.ackTimestamp().getMillis() > 0);
        assertTrue(!alarm.ackUser().isNull());
        assertTrue(alarm.ackUser().get().equals("Fred"));

        // verify using primitive contracts
        Real real = (Real) make("<obj is='obix:real'/>");
        real = (Real) make("<real is='obix:real'/>");
        Exception ex = null;
        try {
            make("<int is='obix:real'/>");
        } catch (Exception e) {
            ex = e;
        }
        assertTrue(ex != null);
        */
    }

////////////////////////////////////////////////////////////////
// Decode
////////////////////////////////////////////////////////////////  

    public Obj make(String xml)
            throws Exception {
        return make(xml, true);
    }

    public Obj make(String xml, boolean useContracts)
            throws Exception {
        ObixDecoder decoder = new ObixDecoder(new ByteArrayInputStream(xml.getBytes("UTF-8")));
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
        assertEquals(orig, piped);
        return piped;
    }

}
