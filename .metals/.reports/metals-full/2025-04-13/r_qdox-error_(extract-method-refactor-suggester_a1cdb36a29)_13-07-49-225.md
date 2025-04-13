error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8621.java
text:
```scala
O@@bject newS = ObjectSerializer.doSerialization( newLoader, orig);

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

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tomcat.util.*;
import org.apache.tomcat.util.threads.*;
import org.apache.tomcat.core.*;

// XXX
// DEPRECATED
// REPLACED BY org.apache.tomcat.modules.session.SimpleSessionStore


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

    int checkInterval = 60;
    int maxActiveSessions = -1;
    String randomClass=null;
    
    public StandardSessionInterceptor() {
    }

    // -------------------- Configuration properties --------------------

    /**
     * Set the check interval (in seconds) for this Manager.
     *
     * @param checkInterval The new check interval
     */
    public void setCheckInterval( int secs ) {
	checkInterval=secs;
    }

    public void setMaxActiveSessions( int count ) {
	maxActiveSessions=count;
    }

    public final void setRandomClass(String randomClass) {
	this.randomClass=randomClass;
	System.setProperty(ContextManager.RANDOM_CLASS_PROPERTY, randomClass);
    }

    
    // -------------------- Tomcat request events --------------------
    public void engineInit( ContextManager cm ) throws TomcatException {
	// set-up a per/container note for StandardManager
	manager_note = cm.getNoteId( ContextManager.CONTAINER_NOTE,
				     "tomcat.standardManager");
    }

    /**
     *  StandardManager will set the HttpSession if one is found.
     */
    public int requestMap(Request request ) {
	String sessionId = null;
	Context ctx=request.getContext();
	if( ctx==null ) {
	    log( "Configuration error in StandardSessionInterceptor " +
		 " - no context " + request );
	    return 0;
	}

	// "access" it and set HttpSession if valid
	sessionId=request.getRequestedSessionId();

	if (sessionId != null && sessionId.length()!=0) {
	    // GS, We are in a problem here, we may actually get
	    // multiple Session cookies (one for the root
	    // context and one for the real context... or old session
	    // cookie. We must check for validity in the current context.
	    ServerSessionManager sM = getManager( ctx );    
	    ServerSession sess= sM.findSession( sessionId );
	    //	    log("Try to find: " + sessionId );
	    if(null != sess) {
		sess.getTimeStamp().touch( System.currentTimeMillis() );
		//log("Session found " + sessionId );
		// set it only if nobody else did !
		if( null == request.getSession( false ) ) {
		    request.setSession( sess );
		    // XXX use MessageBytes!
		    request.setSessionId( sessionId );
		    //log("Session set " + sessionId );
		}
	    }
	    return 0;
	}
	return 0;
    }
    
    public void reload( Request req, Context ctx ) {
	ClassLoader newLoader = ctx.getClassLoader();
	ServerSessionManager sM = getManager( ctx );    

	// remove all non-serializable objects from session
	Enumeration sessionEnum=sM.getSessions().keys();
	while( sessionEnum.hasMoreElements() ) {
	    ServerSession session = (ServerSession)sessionEnum.nextElement();
	    Enumeration e = session.getAttributeNames();
	    while( e.hasMoreElements() )   {
		String key = (String) e.nextElement();
		Object value = session.getAttribute(key);
		// XXX XXX We don't have to change loader for objects loaded
		// by the parent loader ?
		if (! ( value instanceof Serializable)) {
		    session.removeAttribute( key );
		    // XXX notification!!
		}
	    }
	}

	// XXX We should change the loader for each object, and
	// avoid accessing object's internals
	// XXX PipeStream !?!
	Hashtable orig= sM.getSessions();
	Object newS = SessionSerializer.doSerialization( newLoader, orig);
	sM.setSessions( (Hashtable)newS );
	
	// Update the request session id
	String reqId=req.getRequestedSessionId();
	ServerSession sS=sM.findSession( reqId );
	if ( sS != null) {
	    req.setSession(sS);
	    req.setSessionId( reqId );
	}
    }
    
    public int newSessionRequest( Request request, Response response) {
	Context ctx=request.getContext();
	if( ctx==null ) return 0;
	
	ServerSessionManager sM = getManager( ctx );    

	if( request.getSession( false ) != null )
	    return 0; // somebody already set the session
	ServerSession newS=sM.getNewSession();
	request.setSession( newS );
	request.setSessionId( newS.getId().toString());
	return 0;
    }

    //--------------------  Tomcat context events --------------------
    
    /** Init session management stuff for this context. 
     */
    public void contextInit(Context ctx) throws TomcatException {
	// Defaults !!
	ServerSessionManager sm= getManager( ctx );
	
	if( sm == null ) {
	    sm=new ServerSessionManager();
	    ctx.getContainer().setNote( manager_note, sm );
	    sm.setMaxInactiveInterval( (long)ctx.getSessionTimeOut() *
				       60 * 1000 );
	}
	sm.setMaxActiveSessions( maxActiveSessions );

	Expirer expirer=new Expirer();
	expirer.setCheckInterval( checkInterval );
	expirer.setExpireCallback( new Expirer.ExpireCallback() {
		public void expired(TimeStamp o ) {
		    ServerSession sses=(ServerSession)o.getParent();
		    ServerSessionManager ssm=sses.getSessionManager();
		    ssm.removeSession( sses );
		}
	    });
	expirer.start();
	sm.setExpirer(expirer);
    }
    
    /** Notification of context shutdown.
     *  We should clean up any resources that are used by our
     *  session management code. 
     */
    public void contextShutdown( Context ctx )
	throws TomcatException
    {
	if( debug > 0 ) ctx.log("Removing sessions from " + ctx );
	ServerSessionManager sm=getManager(ctx);
	sm.getExpirer().stop();
	sm.removeAllSessions();
    }

    // -------------------- Internal methods --------------------
    private ServerSessionManager getManager( Context ctx ) {
	return (ServerSessionManager)ctx.getContainer().getNote(manager_note);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8621.java