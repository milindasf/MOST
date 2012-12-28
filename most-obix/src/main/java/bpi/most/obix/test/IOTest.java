/*
 * This code licensed to public domain
 */
package bpi.most.obix.test;

import java.io.ByteArrayInputStream;

import bpi.most.obix.Abstime;
import bpi.most.obix.Bool;
import bpi.most.obix.Contract;
import bpi.most.obix.Enum;
import bpi.most.obix.Err;
import bpi.most.obix.Feed;
import bpi.most.obix.Int;
import bpi.most.obix.List;
import bpi.most.obix.Obj;
import bpi.most.obix.Op;
import bpi.most.obix.Real;
import bpi.most.obix.Ref;
import bpi.most.obix.Reltime;
import bpi.most.obix.Status;
import bpi.most.obix.Str;
import bpi.most.obix.Uri;
import bpi.most.obix.io.ObixDecoder;

/**
 * IOTest verifies ObixEncoder and ObixDecoder
 *
 * @author    Brian Frank
 * @creation  27 Apr 05
 * @version   $Revision$ $Date$
 */
public class IOTest
  extends Test
{ 

////////////////////////////////////////////////////////////////
// Test
////////////////////////////////////////////////////////////////

  public void run()
    throws Exception
  { 
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
    for (int i=0; i<Status.list().length; ++i)
    {
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
    verify (obj.size() == 1);
    verify (obj.get("i").getInt() == 3);    
  }       

////////////////////////////////////////////////////////////////
// Utils
////////////////////////////////////////////////////////////////
  
  public void verify(Obj orig)
    throws Exception         
  {            
    roundtrip(orig); 
  }

  public Obj make(String xml)
    throws Exception
  {
    return make(xml, true);
  }

  public Obj make(String xml, boolean useContracts)
    throws Exception
  {            
    ObixDecoder decoder = new ObixDecoder(new ByteArrayInputStream(xml.getBytes()));
    decoder.setUseContracts(useContracts);
    return decoder.decodeDocument();
  }
  
}
