error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/415.java
text:
```scala
r@@eturn OK;

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

package org.apache.tomcat.modules.loggers;

import org.apache.tomcat.core.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.util.hooks.*;
import java.io.*;
import java.net.*;
import java.util.*;

/** Log all hook events during tomcat execution.
 *  Use debug>0 to log addContainer ( since this generates a lot of
 *  output )
 */
public class LogEvents extends BaseInterceptor {
    boolean enabled=false;
    
    public LogEvents() {
    }

    public void setEnabled( boolean b ) {
	enabled=b;
    }
    
    public int registerHooks( Hooks hooks, ContextManager cm, Context ctx ) {
	if( enabled || cm.getDebug() > 5 ) {
	    enabled=true;
	    log( "Adding LogEvents, cm.debug=" + cm.getDebug() + " "
		 + enabled);
	    hooks.addModule( this );
	}
	return DECLINED;
    }
    
    // -------------------- Request notifications --------------------
    public int requestMap(Request request ) {
	log( "requestMap " + request);
	return 0;
    }

    public int contextMap( Request request ) {
	log( "contextMap " + request);
	return 0;
    }

    public int preService(Request request, Response response) {
	log( "preService " + request);
	return 0;
    }

    public int authenticate(Request request, Response response) {
	log( "authenticate " + request);
	return DECLINED;
    }

    public int authorize(Request request, Response response,
			 String reqRoles[])
    {
	StringBuffer sb=new StringBuffer();
	appendSA( sb, reqRoles, " ");
	log( "authorize " + request + " " + sb.toString() );
	return DECLINED;
    }

    public int beforeBody( Request request, Response response ) {
	log( "beforeBody " + request);
	return 0;
    }

    public int beforeCommit( Request request, Response response) {
	log( "beforeCommit " + request);
	return 0;
    }


    public int afterBody( Request request, Response response) {
	log( "afterBody " + request);
	return 0;
    }

    public int postRequest( Request request, Response response) {
	log( "postRequest " + request);
	return 0;
    }

    public int handleError( Request request, Response response, Throwable t) {
	log( "handleError " + request +  " " + t);
	return 0;
    }

    public int postService(Request request, Response response) {
	log( "postService " + request);
	return 0;
    }

    public int newSessionRequest( Request req, Response res ) {
	log( "newSessionRequest " + req );
	return 0;
    }
    
    // -------------------- Context notifications --------------------
    public void contextInit(Context ctx) throws TomcatException {
	log( "contextInit " + ctx);
    }

    public void contextShutdown(Context ctx) throws TomcatException {
	log( "contextShutdown " + ctx);
    }

    /** Notify when a new servlet is added
     */
    public void addServlet( Context ctx, Handler sw) throws TomcatException {
	log( "addServlet " + ctx + " " + sw );
    }
    
    /** Notify when a servlet is removed from context
     */
    public void removeServlet( Context ctx, Handler sw) throws TomcatException {
	log( "removeServlet " + ctx + " " + sw);
    }

    public void addMapping( Context ctx, String path, Handler servlet)
	throws TomcatException
    {
	log( "addMapping " + ctx + " " + path + "->" + servlet);
    }


    public void removeMapping( Context ctx, String path )
	throws TomcatException
    {
	log( "removeMapping " + ctx + " " + path);
    }

    private void appendSA( StringBuffer sb, String s[], String sep) {
	for( int i=0; i<s.length; i++ ) {
	    sb.append( sep ).append( s[i] );
	}
    }
    
    /** 
     */
    public void addSecurityConstraint( Context ctx, String path[],
				       String methods[], String transport,
				       String roles[] )
	throws TomcatException
    {
	StringBuffer sb=new StringBuffer();
	sb.append("addSecurityConstraint " + ctx + " " );
	if( methods!=null ) {
	    sb.append("Methods: ");
	    appendSA( sb, methods, " " );
	}
	if( path!=null) {
	    sb.append(" Paths: ");
	    appendSA( sb, path, " " );
	}
	if( roles!=null) {
	    sb.append(" Roles: ");
	    appendSA( sb, roles, " " );
	}
	sb.append(" Transport " + transport );
	log(sb.toString());
    }

    public void addInterceptor( ContextManager cm, Context ctx,
				BaseInterceptor i )
	throws TomcatException
    {
	if( ! enabled ) return;
	if( ctx==null)
	    log( "addInterceptor " + i );
	else {
	    log( "addInterceptor " + ctx + " " + i);
	}
    }
    
    /** Called when the ContextManger is started
     */
    public void engineInit(ContextManager cm) throws TomcatException {
	log( "engineInit ");
    }

    /** Called before the ContextManager is stoped.
     *  You need to stop any threads and remove any resources.
     */
    public void engineShutdown(ContextManager cm) throws TomcatException {
	log( "engineShutdown ");
    }


    /** Called when a context is added to a CM
     */
    public void addContext( ContextManager cm, Context ctx )
	throws TomcatException
    {
	log( "addContext " + ctx );
    }

    public void addContainer( Container ct )
	throws TomcatException
    {
	if( debug > 0 )
	    log( "addContainer " + ct.getContext() + " " + ct );
    }

    public void engineState( ContextManager cm , int state )
	throws TomcatException
    {
	log( "engineState " + state );
    }

    public void engineStart( ContextManager cm )
	throws TomcatException
    {
	log( "engineStart " );
    }

    /** Called when a context is removed from a CM
     */
    public void removeContext( ContextManager cm, Context ctx )
	throws TomcatException
    {
	log( "removeContext" + ctx);
    }

    /** Servlet Init  notification
     */
    public void preServletInit( Context ctx, Handler sw )
	throws TomcatException
    {
	log( "preServletInit " + ctx + " " + sw);
    }

    
    public void postServletInit( Context ctx, Handler sw )
	throws TomcatException
    {
	log( "postServletInit " + ctx + " " + sw);
    }

    /** Servlet Destroy  notification
     */
    public void preServletDestroy( Context ctx, Handler sw )
	throws TomcatException
    {
	log( "preServletDestroy " + ctx + " " + sw);
    }

    
    public void postServletDestroy( Context ctx, Handler sw )
	throws TomcatException
    {
	log( "postServletDestroy " + ctx +  " " + sw);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/415.java