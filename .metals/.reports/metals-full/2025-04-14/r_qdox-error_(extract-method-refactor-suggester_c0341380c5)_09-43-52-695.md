error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9802.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9802.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9802.java
text:
```scala
S@@tring testKey = "testRequestScopePropertyNotPresent";

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.struts.taglib.logic;
 
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebRequest;
import org.apache.struts.util.LabelValueBean;

/**
 * Suite of unit tests for the
 * <code>org.apache.struts.taglib.logic.PresentTag</code> class.
 *
 * @author David Winterfeldt
 * @author James Mitchell
 */
public class TestPresentTag extends JspTestCase {
    protected final static String COOKIE_KEY = "org.apache.struts.taglib.logic.COOKIE_KEY";
    protected final static String HEADER_KEY = "org.apache.struts.taglib.logic.HEADER_KEY";
    protected final static String PARAMETER_KEY = "org.apache.struts.taglib.logic.PARAMETER_KEY";

    /**
     * Defines the testcase name for JUnit.
     *
     * @param theName the testcase's name.
     */
    public TestPresentTag(String theName) {
        super(theName);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs) {
        junit.awtui.TestRunner.main(new String[] {TestPresentTag.class.getName()});
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite() {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(TestPresentTag.class);
    }

    //----- Test initApplication() method --------------------------------------

    /**
     * Verify that there is an application scope object in scope using the <code>PresentTag</code>.
    */
    public void testApplicationScopeObjectPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testApplicationScopePresent";
        String testStringValue = "abc";
        
        pageContext.setAttribute(testKey, testStringValue, PageContext.APPLICATION_SCOPE);
        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("application");
	
        assertEquals("Value present (not null)", true, pt.condition(true));
    }

    /**
     * Verify that there is an application scope object is not in scope using the <code>PresentTag</code>.
    */
    public void testApplicationScopeObjectNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testApplicationScopeNotPresent";

        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("application");
	
        assertEquals("Value not present (null)", false, pt.condition(true));
    }
    
    /**
     * Verify that there is an session scope object in scope using the <code>PresentTag</code>.
    */
    public void testSessionScopeObjectPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testSessionScopePresent";
        String testStringValue = "abc";
        
        pageContext.setAttribute(testKey, testStringValue, PageContext.SESSION_SCOPE);
        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("session");
	
        assertEquals("Value present (not null)", true, pt.condition(true));
    }

    /**
     * Verify that there is an session scope object is not in scope using the <code>PresentTag</code>.
    */
    public void testSessionScopeObjectNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testSessionScopeNotPresent";

        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("session");
	
        assertEquals("Value present (not null)", false, pt.condition(true));
    }
    
    /**
     * Verify that there is an request scope object in scope using the <code>PresentTag</code>.
    */
    public void testRequestScopeObjectPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testRequestScopePresent";
        String testStringValue = "abc";
        pt.setScope("request");
        
        pageContext.setAttribute(testKey, testStringValue, PageContext.REQUEST_SCOPE);
        pt.setPageContext(pageContext);
	pt.setName(testKey);
	
        assertEquals("Value present (not null)", true, pt.condition(true));
    }

    /**
     * Verify that there is an request scope object is not in scope using the <code>PresentTag</code>.
    */
    public void testRequestScopeObjectNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testRequestScopeNotPresent";

        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("request");
	
        assertEquals("Value not present (null)", false, pt.condition(true));
    }
    
    /**
     * Verify that there is an page scope object in scope using the <code>PresentTag</code>.
    */
    public void testPageScopeObjectPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testPageScopePresent";
        String testStringValue = "abc";
        pt.setScope("page");
        
        pageContext.setAttribute(testKey, testStringValue, PageContext.PAGE_SCOPE);
        pt.setPageContext(pageContext);
	pt.setName(testKey);
	
        assertEquals("Value present (not null)", true, pt.condition(true));
    }

    /**
     * Verify that there is an page scope object is not in scope using the <code>PresentTag</code>.
    */
    public void testPageScopeObjectNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();
        String testKey = "testPageScopeNotPresent";

        pt.setPageContext(pageContext);
	pt.setName(testKey);
	pt.setScope("page");
	
        assertEquals("Value not present (null)", false, pt.condition(true));
    }
   
	/**
	 * Verify that there is a LabelValueBean in application scope 
	 * and test to see if it has a getValue() using the <code>PresentTag</code>.
	*/
	public void testApplicationScopePropertyPresent()
		throws ServletException, JspException {
		PresentTag pt = new PresentTag();
		String testKey = "testApplicationScopePropertyPresent";
		
		String testStringValue = "The Value";
		LabelValueBean lvb = new LabelValueBean("The Key", testStringValue);
		
		pageContext.setAttribute(
			testKey,
			lvb,
			PageContext.APPLICATION_SCOPE);
		pt.setPageContext(pageContext);
		pt.setName(testKey);
		pt.setScope("application");
		
		pt.setProperty("value");
		assertEquals("Property present (not null)", true, pt.condition(true));
	}

	/**
	 * Verify that there is a LabelValueBean in application scope 
	 * and test to see if it has a getValue() that returns null 
	 * using the <code>PresentTag</code>.
	*/
	public void testApplicationScopePropertyNotPresent()
		throws ServletException, JspException {
		PresentTag pt = new PresentTag();
		String testKey = "testApplicationScopePropertyPresent";
		
		String testStringValue = null;
		LabelValueBean lvb = new LabelValueBean("The Key", testStringValue);
		
		pageContext.setAttribute(
			testKey,
			lvb,
			PageContext.APPLICATION_SCOPE);
		pt.setPageContext(pageContext);
		pt.setName(testKey);
		pt.setScope("application");
		
		pt.setProperty("value");
		assertEquals("Property present (not null)", false, pt.condition(true));
	}

	/**
	 * Verify that there is a LabelValueBean in Request scope 
	 * and test to see if it has a getValue() using the <code>PresentTag</code>.
	*/
	public void testRequestScopePropertyPresent()
		throws ServletException, JspException {
		PresentTag pt = new PresentTag();
		String testKey = "testRequestScopePropertyPresent";
		
		String testStringValue = "The Value";
		LabelValueBean lvb = new LabelValueBean("The Key", testStringValue);
		
		pageContext.setAttribute(
			testKey,
			lvb,
			PageContext.REQUEST_SCOPE);
		pt.setPageContext(pageContext);
		pt.setName(testKey);
		pt.setScope("request");
		
		pt.setProperty("value");
		assertEquals("Property present (not null)", true, pt.condition(true));
	}

	/**
	 * Verify that there is a LabelValueBean in Request scope 
	 * and test to see if it has a getValue() that returns null 
	 * using the <code>PresentTag</code>.
	*/
	public void testRequestScopePropertyNotPresent()
		throws ServletException, JspException {
		PresentTag pt = new PresentTag();
		String testKey = "testRequestScopePropertyPresent";
		
		String testStringValue = null;
		LabelValueBean lvb = new LabelValueBean("The Key", testStringValue);
		
		pageContext.setAttribute(
			testKey,
			lvb,
			PageContext.REQUEST_SCOPE);
		pt.setPageContext(pageContext);
		pt.setName(testKey);
		pt.setScope("request");
		
		pt.setProperty("value");
		assertEquals("Property present (not null)", false, pt.condition(true));
	}

    /**
     * Create cookie for testCookiePresent method test.
    */
    public void beginCookiePresent(WebRequest testRequest) {
       testRequest.addCookie(COOKIE_KEY, "cookie value");
    }

    /**
     * Verify that there is an cookie using the <code>PresentTag</code>.
    */
    public void testCookiePresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setCookie(COOKIE_KEY);

        assertEquals("Cookie present", true, pt.condition(true));
    }

    /**
     * Verify that there isn't an cookie using the <code>PresentTag</code>.
    */
    public void testCookieNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setCookie(COOKIE_KEY);
	
        assertEquals("Cookie not present", false, pt.condition(true));
    }

    /**
     * Create header for testHeaderPresent method test.
    */
    public void beginHeaderPresent(WebRequest testRequest) {
       testRequest.addHeader(HEADER_KEY, "header value");
    }

    /**
     * Verify that there is an header using the <code>PresentTag</code>.
    */
    public void testHeaderPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setHeader(HEADER_KEY);
	
        assertEquals("Header present", true, pt.condition(true));
    }

    /**
     * Verify that there isn't an header using the <code>PresentTag</code>.
    */
    public void testHeaderNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setHeader(HEADER_KEY);
	
        assertEquals("Header not present", false, pt.condition(true));
    }

    /**
     * Create parameter for testParameterPresent method test.
    */
    public void beginParameterPresent(WebRequest testRequest) {
       testRequest.addParameter(PARAMETER_KEY, "parameter value");
    }

    /**
     * Verify that there is an parameter using the <code>PresentTag</code>.
    */
    public void testParameterPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setParameter(PARAMETER_KEY);

        assertEquals("Parameter present", true, pt.condition(true));
    }

    /**
     * Verify that there isn't an parameter using the <code>PresentTag</code>.
    */
    public void testParameterNotPresent() throws ServletException,  JspException {
        PresentTag pt = new PresentTag();

        pt.setPageContext(pageContext);
	pt.setParameter(PARAMETER_KEY);
	
        assertEquals("Parameter not present", false, pt.condition(true));
    }    
    
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9802.java