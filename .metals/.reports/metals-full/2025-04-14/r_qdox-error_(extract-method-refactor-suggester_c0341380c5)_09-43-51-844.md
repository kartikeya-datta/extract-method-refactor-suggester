error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3837.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3837.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3837.java
text:
```scala
r@@equest.getContext().log( exceptionString(e));

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
package org.apache.tomcat.servlets;

import org.apache.tomcat.util.*;
import org.apache.tomcat.core.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
  Right now it is not well-integrated, and it's not configurable.
  If someone needs to customize this it should be easy to
  take the steps to do Class.forName() and all rest.

  This code was originally hardcoded in ServletWrapper ( where
  service() is called.
 */

/**
 *  Report an error - if no other error page was set up or
 *  if an error happens in an error page.
 */
public class DefaultErrorPage extends HttpServlet {

    public void service(HttpServletRequest requestH,
			HttpServletResponse responseH)
	throws ServletException, IOException
    {
	Request request=((HttpServletRequestFacade)requestH).getRealRequest();
	Response response=request.getResponse();

	// use internal APIs - we can avoid them,but it's easier and faster
	int status=response.getStatus();
	String msg=(String)request.getAttribute("javax.servlet.error.message");

	Throwable e= (Throwable)request.getAttribute("tomcat.servlet.error.throwable");
	if( e!=null ) {
	    e.printStackTrace();
	    sendPrivateError(request, response, 500, exceptionString( e ));
	    return;
	}

	if( status==HttpServletResponse.SC_MOVED_TEMPORARILY) {
	    redirect( request, response, msg);
	} else {
	    sendPrivateError( request, response, status, msg);

	}
    }

    // -------------------- Default error page --------------------
    private void sendPrivateError(Request request, Response response, int sc, String msg) throws IOException {
	response.setContentType("text/html");

	response.setStatus( sc );
	StringBuffer buf = new StringBuffer();
	if( response.isIncluded() ) {
	    buf.append("<h1>Included servlet error: " );
	}  else {
	    buf.append("<h1>Error: ");
	    }
	buf.append( sc + "</h1>\r\n");
	// More info - where it happended"
	buf.append("<h2>Location: " + request.getRequestURI() + "</h2>");
	buf.append(msg + "\r\n");

	if( response.isUsingStream() ) {
	    ServletOutputStream out = response.getOutputStream();
	    out.print(buf.toString());
	} else {
	    PrintWriter out = response.getWriter();
	    out.print(buf.toString());
	}
    }

    // -------------------- Redirect page --------------------
    public void redirect(Request request, Response response, String location) throws IOException {
        location = makeAbsolute(request, location);
	response.setContentType("text/html");	// ISO-8859-1 default
	response.setHeader("Location", location);

	StringBuffer buf = new StringBuffer();
	buf.append("<head><title>Document moved</title></head>\r\n");
	buf.append("<body><h1>Document moved</h1>\r\n");
	buf.append("This document has moved <a href=\"");
	buf.append(location);
	buf.append("\">here</a>.<p>\r\n");
	buf.append("</body>\r\n");

	String body = buf.toString();

	response.setContentLength(body.length());

	if( response.isUsingStream() ) {
	    ServletOutputStream out = response.getOutputStream();
	    out.print(body);
	} else {
	    PrintWriter out = response.getWriter();
	    out.print(body);
	}
    }

    private String makeAbsolute(Request request, String location) {
        URL url = null;
        try {
	    // Try making a URL out of the location
	    // Throws an exception if the location is relative
            url = new URL(location);
	} catch (MalformedURLException e) {
	    String requrl = HttpUtils.getRequestURL(request.getFacade()).toString();
	    try {
	        url = new URL(new URL(requrl), location);
	    }
	    catch (MalformedURLException ignored) {
	        // Give up
	        return location;
	    }
	}
        return url.toString();
    }

    // -------------------- Internal error page  --------------------
    public String exceptionString( Throwable e) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	pw.println("<b>Internal Servlet Error:</b><br>");
        pw.println("<pre>");
	if( e != null ) 
	    e.printStackTrace(pw);
	pw.println("</pre>");

        if (e instanceof ServletException) {
	    printRootCause((ServletException) e, pw);
	}
	return sw.toString();
    }
	
	
    /** A bit of recursion - print all traces in the stack
     */
    void printRootCause(ServletException e, PrintWriter out) {
        Throwable cause = e.getRootCause();

	if (cause != null) {
	    out.println("<b>Root cause:</b>");
	    out.println("<pre>");
	    cause.printStackTrace(out);
	    out.println("</pre>");

	    if (cause instanceof ServletException) {
		printRootCause((ServletException)cause, out);  // recurse
	    }
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3837.java