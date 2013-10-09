/*
 * This code licensed to public domain
 */
package bpi.most.obix.objects;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * UriTest ensures Uri follows URI RFC 2396 and Obix rules
 *
 * @author    Bittermann David
 * @creation  13 Jan 13
 * @version   $Revision$ $Date$
 */
public class UriTest
        extends TestCase
{

	@Test
    public void testUri()
            throws Exception
    {
        verifyAbs("http://foo/",     "http",  "foo",     -1, "/");
        verifyAbs("http://foo:81/",  "http",  "foo",     81, "/");
        verifyAbs("https://foo/",    "https", "foo",     -1, "/");
        verifyAbs("http://foo.com/", "http",  "foo.com", -1, "/");
        verifyAbs("http://foo/x",    "http",  "foo",     -1, "/x");

        verifyAbs("http://foo/x#f",   "http", "foo", -1, "/x", null, "f");
        verifyAbs("http://foo/x?q",   "http", "foo", -1, "/x", "q",   null);
        verifyAbs("http://foo/x?q#f", "http", "foo", -1, "/x", "q",  "f");
        verifyAbs("http://foo/x#f?q", "http", "foo", -1, "/x", null, "f?q"); // weird, but I believe correct

        verifyRel("/",      "/",   null,  null);
        verifyRel("/x",     "/x",  null,  null);
        verifyRel("",       "",    null,  null);
        verifyRel("x",      "x",   null,  null);
        verifyRel("/x?q",   "/x",  "q",   null);
        verifyRel("x?q",    "x",   "q",   null);
        verifyRel("?q",     "",    "q",   null);
        verifyRel("#f",     "",    null,  "f");
        verifyRel("/x?q#f", "/x",  "q",   "f");
        verifyRel("x?q#f",  "x",   "q",   "f");
        verifyRel("?q#f",   "",    "q",   "f");

        verifyRel(".",       ".",       null,  null);
        verifyRel("..",      "..",      null,  null);
        verifyRel("../x",    "../x",    null,  null);
        verifyRel("../..",   "../..",   null,  null);
        verifyRel("../../x", "../../x", null,  null);

        verifyNorm("http://foo/",     "/",       "http://foo/");
        verifyNorm("http://foo/",     "/x",      "http://foo/x");
        verifyNorm("http://foo/x",    "/y",      "http://foo/y");
        verifyNorm("http://foo/x",    "y",       "http://foo/y");
        verifyNorm("http://foo/x/",   "y",       "http://foo/x/y");
        verifyNorm("http://foo/x/y",  "/z",      "http://foo/z");
        verifyNorm("http://foo/x/y",  "z",       "http://foo/x/z");
        verifyNorm("http://foo/x/y/", "z",       "http://foo/x/y/z");
        verifyNorm("http://foo/",     "#f",      "http://foo/#f");
        verifyNorm("http://foo/x",    "#f",      "http://foo/x#f");
        verifyNorm("http://foo/x",    "/y#f",    "http://foo/y#f");
        verifyNorm("http://foo/x",    "y#f",     "http://foo/y#f");
        verifyNorm("http://foo/x/",   "y#f",     "http://foo/x/y#f");
        verifyNorm("http://foo/x/",   "..",      "http://foo/");
        verifyNorm("http://foo/x/y",  "..",      "http://foo/");
        verifyNorm("http://foo/x/y/", "..",      "http://foo/x/");
        verifyNorm("http://foo/x/y",  "../z",    "http://foo/z");
        verifyNorm("http://foo/x/y/", "../z",    "http://foo/x/z");
        verifyNorm("http://foo/x/y/", "../..",   "http://foo/");
        verifyNorm("http://foo/x/y/", "../../z", "http://foo/z");
        verifyNorm("http://foo/",     "obix:obj","obix:obj");

        verifyAuth("http://foo/",           "http://foo/");
        verifyAuth("http://foo/file",       "http://foo/");
        verifyAuth("http://foo/bar#frag",   "http://foo/");
        verifyAuth("http://foo/bar?query",  "http://foo/");
        verifyAuth("http://foo:81/",        "http://foo:81/");
        verifyAuth("http://foo:81/x",       "http://foo:81/");
        verifyAuth("http://www.acme.com/x", "http://www.acme.com/");
        verifyAuth("ftp://www.acme.com/x",  "ftp://www.acme.com/");

        assertTrue((contains("http://foo/",      "http://foo/")));
        assertTrue(!contains("http://foo/",     "http://foox/"));
        assertTrue(contains("http://foo:81/",   "http://foo:81/"));
        assertTrue(!contains("http://foo/",     "http://foo:81/"));
        assertTrue(contains("http://foo/",      "http://foo/x"));
        assertTrue(contains("http://foo/",      "http://foo/x#frag"));
        assertTrue(contains("http://foo/",      "http://foo/x/y.txt"));
        assertTrue(!contains("http://foo:81/",  "http://foox/x"));
        assertTrue(!contains("http://foo/",     "http://foox/x"));
        assertTrue(!contains("http://foo/",     "http://foo:81/x"));
        assertTrue(contains("http://foo/",      ""));
        assertTrue(contains("http://foo/",      ".."));
        assertTrue(contains("http://foo/",      "/"));
        assertTrue(contains("http://foo/",      "/x"));
        assertTrue(contains("http://foo/",      "x"));
        assertTrue(contains("http://foo/",      "x#frag"));

        verifyParent("http://foo/",     null);
        verifyParent("http://foo/x",    "http://foo/");
        verifyParent("http://foo/x/",   "http://foo/");
        verifyParent("http://foo/x/y",  "http://foo/x");
        verifyParent("http://foo/x/y/", "http://foo/x");
        verifyParent("http://foo/x/y/z?query", "http://foo/x/y");

        verifyQuery("k=v", new String[] { "k", "v" });
        verifyQuery("a=b&c=d", new String[] { "a", "b", "c", "d" });
        verifyQuery("foo=bar&darn=cat&rock=roll", new String[] { "foo", "bar", "darn", "cat", "rock", "roll" });
        verifyQuery("v", new String[] { "v", "true" });
        verifyQuery("v=false", new String[] { "v", "false" });
        verifyQuery("a&b=false&c", new String[] { "a", "true", "b", "false", "c", "true" });

        verifyAddQuery("http://foo/",             "v", "true", "http://foo/?v=true");
        verifyAddQuery("http://foo/obix",         "v", "true", "http://foo/obix?v=true");
        verifyAddQuery("http://foo/obix?a=b",     "v", "true", "http://foo/obix?a=b&v=true");
        verifyAddQuery("http://foo/obix?a=b&c",   "v", "true", "http://foo/obix?a=b&c=true&v=true");
        verifyAddQuery("http://foo/obix?v=false", "v", "true", "http://foo/obix?v=true");
        verifyAddQuery("http://foo/obix?v",       "v", "foo",  "http://foo/obix?v=foo");
        verifyAddQuery("http://foo/obix?a=b&v=x", "v", "foo",  "http://foo/obix?a=b&v=foo");
        verifyAddQuery("http://foo/obix?v=x&a=b", "v", "foo",  "http://foo/obix?v=foo&a=b");

        verifyAddQuery("http://foo/obix",         "v", null,   "http://foo/obix");
        verifyAddQuery("http://foo/obix?v=x",     "v", null,   "http://foo/obix");
        verifyAddQuery("http://foo/obix?a=b",     "v", null,   "http://foo/obix?a=b");
        verifyAddQuery("http://foo/obix?a=b&v=x", "v", null,   "http://foo/obix?a=b");
        verifyAddQuery("http://foo/obix?v=x&a=b", "v", null,   "http://foo/obix?a=b");
    }

    public void verifyAbs(String uriStr, String scheme, String host, int port, String path)
            throws Exception
    {
        verifyAbs(uriStr, scheme, host, port, path, null, null);
    }

    public void verifyAbs(String uriStr, String scheme, String host, int port, String path, String query, String frag)
            throws Exception
    {
        Uri uri = new Uri(uriStr);

        assertTrue(uri.isAbsolute());
        assertTrue(!uri.isRelative());
        assertTrue(uri.getScheme().equals(scheme));
        assertTrue(uri.getHost().equals(host));
        assertTrue(uri.getPort() == port);
        if (port == -1)
        	assertTrue(uri.getAddress().equals(host));
        else
        	assertTrue(uri.getAddress().equals(host+":"+port));
        assertTrue(uri.getPath().equals(path));
        Uri.Query q = uri.getQuery();
        if (q == null)
        	assertTrue(query == null);
        else
        	assertEquals(q.toString(), query);
        assertEquals(uri.getFragment(), frag);
    }

    public void verifyRel(String uriStr, String path, String query, String frag)
            throws Exception
    {
        Uri uri = new Uri(uriStr);

        assertTrue(!uri.isAbsolute());
        assertTrue(uri.isRelative());
        assertTrue(uri.getScheme() == null);
        assertTrue(uri.getHost() == null);
        assertTrue(uri.getPort() == -1);
        assertTrue(uri.getAuthority() == null);
        assertTrue(uri.getPath().equals(path));
        Uri.Query q = uri.getQuery();
        if (q == null)
        	assertTrue(query == null);
        else
        	assertEquals(q.toString(), query);
        assertEquals(uri.getFragment(), frag);
    }

    public void verifyNorm(String base, String rel, String norm)
            throws Exception
    {
        Uri uri = new Uri(rel).normalize(new Uri(base));

        assertTrue(uri.toString().equals(norm));
    }

    public void verifyAuth(String uriStr, String authStr)
            throws Exception
    {
        Uri uri = new Uri(uriStr);
        Uri auth = uri.getAuthorityUri();

        assertTrue(uri.getAuthority().equals(authStr));
        assertTrue(auth.toString().equals(authStr));
    }

    public boolean contains(String base, String sub)
    {
        return new Uri(base).contains(new Uri(sub));
    }

    public void verifyParent(String uri, String parentStr)
    {
        Uri parent = new Uri(uri).parent();

        if (parent == null) assertTrue(parentStr == null);
        else assertTrue(parent.toString().equals(parentStr));
    }

    public void verifyQuery(String qstr, String[] pairs)
    {
        Uri.Query q = new Uri("http://foo/?" + qstr).getQuery();

        assertTrue(q.toString().equals(qstr));
        String[] keys = q.keys();
        assertTrue(keys.length == pairs.length/2);
        for(int i=0; i<keys.length; ++i)
        {
            String key = keys[i];

            assertTrue(key.equals(pairs[i*2]));
            assertTrue(q.get(key, null).equals(pairs[i*2+1]));
        }
    }

    public void verifyAddQuery(String base, String key, String val, String result)
    {
        Uri actual = new Uri(base).addQueryParam(key, val);

        assertTrue(actual.toString().equals(result));
    }

}
