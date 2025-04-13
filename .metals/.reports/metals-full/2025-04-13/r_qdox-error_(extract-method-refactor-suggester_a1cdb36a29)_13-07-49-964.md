error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8430.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8430.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8430.java
text:
```scala
C@@lassLoader newLoader = ctx.getClassLoader();

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


package org.apache.tomcat.session;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.*;
import org.apache.tomcat.core.*;

/**
 * This is the adapter between tomcat and a StandardManager.
 * A session manager should not depend on tomcat internals - so you can
 * use it in other engines and back. All you need to do is
 * create an adapter ( tomcat Interceptor).
 *
 * You can even have multiple session managers per context - the first that
 * recognize the "requestedSessionId" will create it's own HttpSession object.
 * By using standard tomcat interceptor mechanisms you can plug in one or
 * many session managers per context or context manager ( or even per
 * URL - but that's not standard API feature ).
 * 
 * It must be inserted after SessionInterceptor, which does common
 * session stuff ( cookie, rewrite, etc)
 *
 * @author costin@eng.sun.com
 */
public final class StandardSessionInterceptor  extends BaseInterceptor {
    int manager_note;
    
    public StandardSessionInterceptor() {
    }

    // -------------------- Internal methods --------------------
    private StandardManager getManager( Context ctx ) {
	return (StandardManager)ctx.getContainer().getNote(manager_note);
    }

    private void setManager( Context ctx, StandardManager sm ) {
	ctx.getContainer().setNote( manager_note, sm );
    }

    // -------------------- Tomcat request events --------------------
    public void engineInit( ContextManager cm ) throws TomcatException {
	// set-up a per/container note for StandardManager
	manager_note = cm.getNoteId( ContextManager.CONTAINER_NOTE, "tomcat.standardManager");
    }

    /**
     *  StandardManager will set the HttpSession if one is found.
     *  
     */
    public int requestMap(Request request ) {
	String sessionId = null;
	Context ctx=request.getContext();
	if( ctx==null ) {
	    log( "Configuration error in StandardSessionInterceptor - no context " + request );
	    return 0;
	}

	// "access" it and set HttpSession if valid
	sessionId=request.getRequestedSessionId();

	if (sessionId != null && sessionId.length()!=0) {
	    // GS, We are in a problem here, we may actually get
	    // multiple Session cookies (one for the root
	    // context and one for the real context... or old session
	    // cookie. We must check for validity in the current context.
	    StandardManager sM = getManager( ctx );    
	    HttpSession sess= sM.findSession( sessionId );
	    if(null != sess) {
		//		log( "Found session");
		// set it only if nobody else did !
		if( null == request.getSession( false ) ) {
		    request.setSession( sess );
		    //    log("Session set ");
		}
	    }
	    return 0;
	}
	//	log( "No session ");
	return 0;
    }
    
    public void reload( Request req, Context ctx ) {
	ClassLoader newLoader = ctx.getServletLoader().getClassLoader();
	StandardManager sM = getManager( ctx );    
	sM.handleReload(req, newLoader);
    }
    
    public int newSessionRequest( Request request, Response response) {
	Context ctx=request.getContext();
	if( ctx==null ) return 0;
	
	StandardManager sM = getManager( ctx );    

	if( request.getSession( false ) != null )
	    return 0; // somebody already set the session
	HttpSession newS=sM.getNewSession();
	request.setSession( newS );
	return 0;
    }

    /** Called after request - we need to release the session object.
     *  This is used to prevent removal of session objects during execution,
     *	and may be used by interceptors that want to limit or count the
     *	sessions.
     */
    public int postService(  Request rrequest, Response response ) {
	Context ctx=rrequest.getContext();
	if( ctx==null ) return 0; 

	StandardManager sm= getManager( ctx );
	HttpSession sess=rrequest.getSession(false);
	if( sess == null ) return 0;
	
	sm.release( sess );
	return 0;
    }


    
    //--------------------  Tomcat context events --------------------
    
    /** Init session management stuff for this context. 
     */
    public void contextInit(Context ctx) throws TomcatException {
	// Defaults !!
	StandardManager sm= getManager( ctx );
	
	if( sm == null ) {
	    sm=new StandardManager();
	    setManager(ctx, sm);
	}

	// init is called after all context properties are set.
	sm.setSessionTimeOut( ctx.getSessionTimeOut() );
	sm.setDistributable( ctx.isDistributable() );

	try {
	    sm.start();
	} catch(IllegalStateException ex ) {
	    throw new TomcatException( ex );
	}
    }
    
    /** Notification of context shutdown.
     *  We should clean up any resources that are used by our
     *  session management code. 
     */
    public void contextShutdown( Context ctx )
	throws TomcatException
    {
	if( ctx.getDebug() > 0 ) ctx.log("Removing sessions from " + ctx );
	StandardManager sm=getManager(ctx);
	try {
	    if( sm != null )
		sm.stop();
	} catch(IllegalStateException ex ) {
	    throw new TomcatException( ex );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8430.java