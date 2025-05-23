error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4849.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4849.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4849.java
text:
```scala
H@@andler wrapper = context.getServletByName( name );

/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
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
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
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
 * [Additional notices, if required by prior licensing conditions]
 *
 */ 


package org.apache.tomcat.facade;

import org.apache.tomcat.core.*;
import org.apache.tomcat.core.Constants;
import org.apache.tomcat.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;


/**
 * Implementation of the javax.servlet.ServletContext interface that
 * servlets see. Having this as a Facade class to the Context class
 * means that we can split up some of the work.
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author Jason Hunter [jch@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Harish Prabandham
 */
final class ServletContextFacade implements ServletContext {
    // Use the strings from core
    private StringManager sm = StringManager.getManager("org.apache.tomcat.core");
    private ContextManager contextM;
    private Context context;

    ServletContextFacade(ContextManager server, Context context) {
        this.contextM = server;
        this.context = context;
    }

    Context getRealContext() {
	return context;
    }

    // -------------------- Public facade methods --------------------
    public ServletContext getContext(String path) {
        Context target=context.getContext(path);
	return target.getFacade();
    }

    
    public Object getAttribute(String name) {
        return context.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return context.getAttributeNames();
    }

    public void setAttribute(String name, Object object) {
        context.setAttribute(name, object);
    }

    public void removeAttribute(String name) {
        context.removeAttribute(name);
    } 
    
    public int getMajorVersion() {
        return Constants.SERVLET_MAJOR;
    }

    public int getMinorVersion() {
        return Constants.SERVLET_MINOR;
    }

    public String getMimeType(String filename) {
        return context.getMimeMap().getContentTypeFor(filename);
    }

    public String getRealPath(String path) {
	return context.getRealPath( path );
    }

    public InputStream getResourceAsStream(String path) {
        InputStream is = null;
        try {
            URL url = getResource(path);
            URLConnection con = url.openConnection();
            con.connect();
            is = con.getInputStream();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
	return is;
    }

    public URL getResource(String path)	throws MalformedURLException {
	return context.getResource( path );
    }

    public RequestDispatcher getRequestDispatcher(String path) {
	if ( path == null  || ! path.startsWith("/")) {
	    return null; // spec say "return null if we can't return a dispather
	}
	RequestDispatcherImpl rD=new RequestDispatcherImpl( context );
	rD.setPath( path );
	
	return rD;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        if (name == null)
	    return null;

	// We need to do the checks
	ServletWrapper wrapper = context.getServletByName( name );
	if (wrapper == null)
	    return null;
	RequestDispatcherImpl rD=new RequestDispatcherImpl( context );
	rD.setName( name );

	return rD;
    }

    public String getServerInfo() {
        return context.getEngineHeader();
    }

    public void log(String msg) {
	context.logServlet( msg, null );
    }

    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    public Enumeration getInitParameterNames() {
	return context.getInitParameterNames();
    }

    public void log(String msg, Throwable t) {
	context.logServlet(msg, t);
    }

    /**
     *
     * @deprecated This method is deprecated in the
     *             javax.servlet.ServletContext interface
     */
    public void log(Exception e, String msg) {
        log(msg, e);
    }

    /**
     *
     * @deprecated This method is deprecated in the
     *             javax.servlet.ServletContext interface
     */
    public Servlet getServlet(String name) throws ServletException {
        return null;
    }

    /**
     * This method has been deprecated in the public api and always
     * return an empty enumeration.
     *
     * @deprecated
     */
    public Enumeration getServlets() {
	// silly hack to get an empty enumeration
	Vector v = new Vector();
	return v.elements();
    }
    
    /**
     * This method has been deprecated in the public api and always
     * return an empty enumeration.
     *
     * @deprecated
     */
    public Enumeration getServletNames() {
	// silly hack to get an empty enumeration
	Vector v = new Vector();
	return v.elements();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4849.java