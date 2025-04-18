error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2976.java
text:
```scala
w@@rapper.handleRequest(realRequest, realResponse);

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
import org.apache.tomcat.core.Constants;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author Jason Hunter [jch@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 */

public class InvokerServlet extends HttpServlet {
    private Context context;
    
    public void init() throws ServletException {
	ServletContextFacade facade =
	    (ServletContextFacade)getServletContext();
        context = facade.getRealContext();
    }
    
    public void service(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException
    {
        String requestPath = request.getRequestURI();
	String pathInfo = (String)request.getAttribute(
            Constants.ATTRIBUTE_PathInfo);

	if (pathInfo == null) {
	    pathInfo = request.getPathInfo();
	}

	String includedRequestURI = (String)request.getAttribute(
	    Constants.ATTRIBUTE_RequestURI);
	boolean inInclude = false;

	// XXX XXX XXX in the new model we are _never_ inInclude
	if (includedRequestURI != null) {
	    inInclude = true;
	} else {
	    inInclude = false;
	}

        String servletName = "";
        String newServletPath = "";
        String newPathInfo = "";

        // XXX
        // yet another example of substring overkill -- we can do
        // this better....

        if (pathInfo != null &&
            pathInfo.startsWith("/") &&
	    pathInfo.length() > 2) {
            servletName = pathInfo.substring(1, pathInfo.length());

            if (servletName.indexOf("/") > -1) {
                servletName =
		    servletName.substring(0, servletName.indexOf("/"));
            }

	    if (! inInclude) {
		newServletPath = request.getServletPath() +
		    "/" + servletName;
	    } else {
		newServletPath = (String)request.getAttribute
		    (Constants.ATTRIBUTE_ServletPath)  + "/" +
                    servletName;
	    }
	    
            // XXX
            // oh, very sloppy here just catching the exception... Do
            // this for real...

            try {
		if (inInclude) {
		    newPathInfo = includedRequestURI.substring(
			newServletPath.length(),
			includedRequestURI.length());
		} else {
		    newPathInfo = requestPath.substring(
			context.getPath().length() +
			    newServletPath.length(),
			requestPath.length());
		}
		
		int i = newPathInfo.indexOf("?");

		if (i > -1) {
		    newPathInfo = newPathInfo.substring(0, i);
		}

		if (newPathInfo.length() < 1) {
		    newPathInfo = null;
		}
            } catch (Exception e) {
                newPathInfo = null;
            }
	} else {
            // theres not enough information here to invoke a servlet
            doError(response,"Not enough information " + request.getRequestURI() + " " + pathInfo);

            return;
        }

        // try the easy one -- lookup by name
        ServletWrapper wrapper = context.getServletByName(servletName);
	//	System.out.println("Invoker: getServletByName " + servletName + "=" + wrapper);

        if (wrapper == null) {
	    // Moved loadServlet here //loadServlet(servletName);
	    wrapper = new ServletWrapper();
	    wrapper.setContext(context);
	    wrapper.setServletClass(servletName);
	    wrapper.setServletName(servletName); // XXX it can create a conflict !

	    try {
		context.addServlet( wrapper );
	    } catch(TomcatException ex ) {
		ex.printStackTrace();
	    }

	    // XXX add mapping - if the engine supports dynamic changes in mappings,
	    // we'll avoid the extra parsing in Invoker !!!

	    // XXX Invoker can be avoided easily - it's a special mapping, easy to
	    // support
        }

	// System.out.println("CL: " + context.getServletLoader().getClassLoader() +
	//	   " wrapper: " + wrapper);
	

	// Can't be null - loadServlet creates a new wrapper .
	//         if (wrapper == null) {
	//             // we are out of luck
	//             doError(response, "Wrapper is null - " + servletName);
	//             return;
	//         }

        HttpServletRequestFacade requestfacade =
	    (HttpServletRequestFacade)request;
        HttpServletResponseFacade responsefacade =
	    (HttpServletResponseFacade)response;
	Request realRequest = requestfacade.getRealRequest();
	Response realResponse = realRequest.getResponse();

	// The saved servlet path, path info are for cases in which a
	// request dispatcher forwards through the invoker. This is
	// some seriously sick code here that needs to be done
	// better, but this will do the trick for now.
	String savedServletPath=null;
	String savedPathInfo =null;


	// XXX XXX XXX need to be removed after the include hacks are out
	if( ! inInclude )  {
	    savedPathInfo=realRequest.getPathInfo();
	    savedServletPath=realRequest.getServletPath();
	} else {
	    savedServletPath = (String)realRequest.getAttribute(
			       Constants.ATTRIBUTE_ServletPath);
	    savedPathInfo = (String)realRequest.getAttribute(
			       Constants.ATTRIBUTE_PathInfo);
	}
	
	if (! inInclude) {
	    realRequest.setServletPath(newServletPath);
	    realRequest.setPathInfo(newPathInfo);
	} else {
	    if (newServletPath != null) {
		realRequest.setAttribute(
                    Constants.ATTRIBUTE_ServletPath, newServletPath);
	    }

	    if (newPathInfo != null) {
		realRequest.setAttribute(
                    Constants.ATTRIBUTE_PathInfo, newPathInfo);
	    }

	    if (newPathInfo == null) {
		// Can't store a null, so remove for same effect

		realRequest.removeAttribute(
                    Constants.ATTRIBUTE_PathInfo);
	    }
	}

        wrapper.handleRequest(requestfacade, responsefacade);
	
	if (!inInclude) {
	    realRequest.setServletPath( savedServletPath);
	    realRequest.setPathInfo(savedPathInfo);
	} else {
	    if (savedServletPath != null) {
		realRequest.setAttribute(
                    Constants.ATTRIBUTE_ServletPath, savedServletPath);
	    } else {
		realRequest.removeAttribute(
                    Constants.ATTRIBUTE_ServletPath);
	    }

	    if (savedPathInfo != null) {
		realRequest.setAttribute(
                    Constants.ATTRIBUTE_PathInfo, savedPathInfo);
	    } else {
		realRequest.removeAttribute(
                    Constants.ATTRIBUTE_PathInfo);
	    }
	}
    }

    public void doError(HttpServletResponse response, String msg)
	throws ServletException, IOException
    {
	response.sendError(404, msg);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2976.java