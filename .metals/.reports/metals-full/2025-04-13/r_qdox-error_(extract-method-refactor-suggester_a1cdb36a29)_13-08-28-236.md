error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/17.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/17.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/17.java
text:
```scala
i@@f(ctx == eI[i].getContext()) sI.addElement(eI[i]);

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

package org.apache.tomcat.modules.mappers;

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
 *  Configuration. Must be set after LoaderInterceptor
 */
public class ReloadInterceptor extends  BaseInterceptor
{
    // Stop and start the context.
    boolean fullReload=true;
    int dependManagerNote=-1;
    
    public ReloadInterceptor() {
    }

    public void engineInit( ContextManager cm ) throws TomcatException {
	dependManagerNote=cm.getNoteId(ContextManager.CONTAINER_NOTE,
				       "DependManager");
    }
    
    /** A full reload will stop and start the context, without
     *  saving any state. It's the cleanest form of reload, equivalent
     *  with (partial) server restart.
     */
    public void setFullReload( boolean full ) {
	fullReload=full;
    }

    public void addContext( ContextManager cm, Context context)
	throws TomcatException
    {
	DependManager dm=(DependManager)context.getContainer().
	    getNote("DependManager");
	if( dm==null ) {
	    dm=new DependManager();
	    context.getContainer().setNote("DependManager", dm);
	}
	if( debug > 0 ) {
	    dm.setDebug( debug );
	}
    }
    
    /** Example of adding web.xml to the dependencies.
     *  JspInterceptor can add all taglib descriptors.
     */
    public void contextInit( Context context)
	throws TomcatException
    {
        ContextManager cm = context.getContextManager();
	DependManager dm=(DependManager)context.getContainer().
	    getNote("DependManager");

	File inf_xml = new File(context.getAbsolutePath() +
				"/WEB-INF/web.xml");
	if( inf_xml.exists() ) {
	    Dependency dep=new Dependency();
	    dep.setTarget("web.xml");
	    dep.setOrigin( inf_xml );
	    dep.setLastModified( inf_xml.lastModified() );
	    dm.addDependency( dep );
	}

	// Use a DependClassLoader to autmatically record class loader
	// deps
	loaderHook(dm, context);
    }
    
    public void reload( Request req, Context context) throws TomcatException {

	DependManager dm=(DependManager)context.getContainer().
	    getNote("DependManager");

	if( dm!=null ) {
	    // we are using a util.depend for reloading
	    dm.reset();
	}
	loaderHook(dm, context);
	log( "Reloading context " + context );
    }

    
    protected void  loaderHook( DependManager dm, Context context ) {
	// ReloadInterceptor must be configured _after_ LoaderInterceptor
	ClassLoader cl=context.getClassLoader();
	
	ClassLoader loader=DependClassLoader.getDependClassLoader( dm, cl,
		     context.getAttribute( Context.ATTRIB_PROTECTION_DOMAIN), debug);

	context.setClassLoader(loader);
	context.setAttribute( "org.apache.tomcat.classloader", loader);
    }

    
    public int contextMap( Request request ) {
	Context ctx=request.getContext();
	if( ctx==null) return 0;
	
	// XXX This interceptor will be added per/context.
	if( ! ctx.getReloadable() ) return 0;

	// We are remapping ?
	if( request.getAttribute("tomcat.ReloadInterceptor")!=null)
	    return DECLINED;
	
	DependManager dm=(DependManager)ctx.getContainer().
	    getNote(dependManagerNote);
	if( ! dm.shouldReload() ) return 0;

	if( debug> 0 )
	    log( "Detected changes in " + ctx.toString());

	try {
	    // Reload context.	
	    ContextManager cm=ctx.getContextManager();
	    
	    if( fullReload ) {
		Vector sI=new Vector();  // saved local interceptors
		BaseInterceptor[] eI;    // all exisiting interceptors

		// save the ones with the same context, they are local
		eI=ctx.getContainer().getInterceptors();
		for(int i=0; i < eI.length ; i++)
		    if(ctx == eI[i].getContext()) sI.add(eI[i]);
                
		Enumeration e;
		// Need to find all the "config" that
		// was read from server.xml.
		// So far we work as if the admin interface was
		// used to remove/add the context.
		// Or like the deploytool in J2EE.
		Context ctx1=cm.createContext();
		ctx1.setContextManager( cm );
		ctx1.setPath(ctx.getPath());
		ctx1.setDocBase(ctx.getDocBase());
		ctx1.setReloadable( ctx.getReloadable());
		ctx1.setDebug( ctx.getDebug());
		ctx1.setHost( ctx.getHost());
		ctx1.setTrusted( ctx.isTrusted());
		e=ctx.getHostAliases();
		while( e.hasMoreElements())
		    ctx1.addHostAlias( (String)e.nextElement());

		cm.removeContext( ctx );

		cm.addContext( ctx1 );

		// put back saved local interceptors
		e=sI.elements();
		while(e.hasMoreElements()){
		    BaseInterceptor savedI=(BaseInterceptor)e.nextElement();

		    ctx1.addInterceptor(savedI);
		    savedI.setContext(ctx1);
		    savedI.reload(request,ctx1);
		}

		ctx1.init();

		// remap the request
		request.setAttribute("tomcat.ReloadInterceptor", this);
		BaseInterceptor ri[]=
		    cm.getContainer().getInterceptors(Container.H_contextMap);
		
		for( int i=0; i< ri.length; i++ ) {
		    if( ri[i]==this ) break;
		    int status=ri[i].contextMap( request );
		    if( status!=0 ) return status;
		}

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


		if( debug>0 ) log( "Reloading hooks for context " +
				   ctx.toString());

		// Call reload hook in context manager
		BaseInterceptor cI[]=ctx.getContainer().getInterceptors();
		for( int i=0; i< cI.length; i++ ) {
		    cI[i].reload(  request, ctx );
		    ctx.getContainer().setNote( "oldLoader", null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/17.java