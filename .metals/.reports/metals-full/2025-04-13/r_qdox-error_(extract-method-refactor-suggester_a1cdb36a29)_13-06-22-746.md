error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1521.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1521.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1521.java
text:
```scala
c@@tx1.init();

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


package org.apache.tomcat.request;

import org.apache.tomcat.core.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.util.depend.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This interceptor deals with context reloading.
 *  This should be "AT_END" - just after the context is mapped, it
 *  will determine if the context needs reload.
 *
 *  This interceptor supports multiple forms of reloading.
 */
public class ReloadInterceptor extends  BaseInterceptor
{
    // Stop and start the context.
    boolean fullReload=true;
    
    public ReloadInterceptor() {
    }

    /** A full reload will stop and start the context, without
     *  saving any state. It's the cleanest form of reload, equivalent
     *  with (partial) server restart.
     */
    public void setFullReload( boolean full ) {
	fullReload=full;
    }
    
    /** Example of adding web.xml to the dependencies.
     *  JspInterceptor can add all taglib descriptors.
     */
    public void contextInit( Context context)
	throws TomcatException
    {
        ContextManager cm = context.getContextManager();
	DependManager dm=context.getDependManager();
	if( dm==null ) {
	    dm=new DependManager();
	    context.setDependManager( dm );
	}

	File inf_xml = new File(context.getAbsolutePath() +
				"/WEB-INF/web.xml");
	if( inf_xml.exists() ) {
	    Dependency dep=new Dependency();
	    dep.setTarget("web.xml");
	    dep.setOrigin( inf_xml );
	    // if change after now, we'll reload the context
	    dep.setLastModified( System.currentTimeMillis() );
	    dm.addDependency( dep );
	}
    }

    
    public int contextMap( Request request ) {
	Context ctx=request.getContext();
	if( ctx==null) return 0;
	
	// XXX This interceptor will be added per/context.
	if( ! ctx.getReloadable() ) return 0;

	if( ! ctx.shouldReload() ) return 0;

	if( debug> 0 )
	    log( "Detected changes in " + ctx.toString());

	try {
	    // Reload context.	
	    ContextManager cm=ctx.getContextManager();
	    
	    if( fullReload ) {
		Enumeration e;
		// Need to find all the "config" that
		// was read from server.xml.
		// So far we work as if the admin interface was
		// used to remove/add the context.
		// Or like the deploytool in J2EE.
		Context ctx1=new Context();
		ctx1.setContextManager( cm );
		ctx1.setPath(ctx.getPath());
		ctx1.setDocBase(ctx.getDocBase());
		ctx1.setReloadable( ctx.getReloadable());
		ctx1.setDebug( ctx.getDebug());
		ctx1.setHost( ctx.getHost());
		e=ctx.getHostAliases();
		while( e.hasMoreElements())
		    ctx1.addHostAlias( (String)e.nextElement());

		cm.removeContext( ctx );

		cm.addContext( ctx1 );

		cm.initContext( ctx1 );

		// XXX Make sure ctx is destroyed - we may have
		// undetected leaks 

	    } else {
		// This is the old ( buggy) behavior
		// ctx.reload() has some fixes - it removes most of the
		// user servlets, but still need work XXX.

		// we also need to save context attributes.

		Enumeration sE=ctx.getServletNames();
		while( sE.hasMoreElements() ) {
		    try {
			String sN=(String)sE.nextElement();
			Handler sw=ctx.getServletByName( sN );
			sw.reload();
		    } catch( Exception ex ) {
			log( "Reload exception: " + ex);
		    }
		}

		// send notification to all interceptors
		// They may try to save up the state or take
		// the right actions


		if( debug>0 ) log( "Reloading hooks for context " + ctx.toString());

		// Call reload hook in context manager
		BaseInterceptor cI[]=ctx.getContainer().getInterceptors();
		for( int i=0; i< cI.length; i++ ) {
		    cI[i].reload(  request, ctx );
		}
	    }
	} catch( TomcatException ex) {
	    log( "Error reloading " + ex );
	}
	return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1521.java