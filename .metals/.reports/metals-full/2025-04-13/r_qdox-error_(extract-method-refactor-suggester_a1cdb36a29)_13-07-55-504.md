error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3918.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3918.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3918.java
text:
```scala
public static final i@@nt INITIAL_HOOKS=24;

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

package org.apache.tomcat.util.hooks;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tomcat.util.IntrospectionUtils;

/** Hooks support. Hooks implement a chain-of-command pattern, and
 * are commonly used in most web servers as a mechanism of extensibility.
 *
 * This class doesn't deal with hook invocation - the program is expected
 * to use interfaces or base classes with the hooks beeing methods that
 * are invoked. Reflection-based invocation is very expensive and shouldn't 
 * be used.
 * 
 * The Hooks class will provide support for registering and maintaining
 * a list of modules implementing each hook.
 *
 * The module can be added automatically to the right lists by using
 * introspection ( if the module implements a certain method, it'll
 * be added to the chain for the hook with the same name ). It is also
 * possible for a module to explicitely register hooks.
 *
 * This class is modeled after Apache2.0 hooks - most web servers are using
 * this pattern, but so far A2.0 has the most flexible and powerfull
 * implementation
 */
public class Hooks {
    public static final int INITIAL_HOOKS=20;
    int hookCount;
    String hookNames[];
    Vector hooksV[];

    Object hooks[][];

    Vector allModulesV;
    Object allModules[];

    private static final int dL=0;

    public Hooks() {
	this.hookCount=INITIAL_HOOKS; // XXX TODO: resizing
	hooksV=new Vector[hookCount];
	for( int i=0; i<hookCount ; i++ )
	    hooksV[i]=new Vector();
	allModulesV=new Vector();
	hookNames=new String[hookCount];
	hooks=new Object[hookCount][];
    }

    /** Allow direct access to hooks. You must call resetCache()
	if you change the hooks
    */
    public Vector getHooksVector( int type ) {
	return hooksV[type];
    }

    public Vector getHooksVector() {
	return allModulesV;
    }

    public void resetCache() {
	for( int i=0; i<hookCount; i++ )
	    hooks[i]=null;
	allModules=null;
    }
    
    public int registerHook( String name ) {
	for( int i=0; i<hookNames.length; i++ ) {
	    if( hookNames[i]==null ) {
		hookNames[i]=name;
		return i;
	    }
	}
	throw new RuntimeException("Too many hooks");
    }

    public void registerHook( String name, int id ) {
	hookNames[id]=name;
    }

    public String getHookName( int id ) {
	return hookNames[id];
    }

    public int getHookId( String hookName ) {
	for( int i=0; i< hookNames.length; i++ ) {
	    if( hookName.equals(hookNames[i]))
		return i;
	}
	return -1;
    }
    

    /** Add the module to all the hook chains it's implements
     *  The hook name should match a method defined in the module
     *  ( not inherited - explicitely defined there )
     */
    public void addModule( Object bi ) {
	// the module wants to register the hooks itself
	for( int i=0; i< hookNames.length ; i++ ) {
	    if( hookNames[i]==null ) continue;
	    if( hasHook( bi, hookNames[i] )) {
		if( dL > 0 ) debug( "Adding " + hookNames[i] + " " +bi );
		hooksV[i].addElement( bi );
		hooks[i]=null;
	    }
	}
	allModules=null;
	allModulesV.addElement( bi );
    }

    public void addModule( String type, Object bi ) {
	int typeId=getHookId( type );
	hooksV[typeId].addElement( bi );
	hooks[typeId]=null;
    }

    public void removeModule( Object bi ) {
	for( int i=0; i<hookNames.length; i++ ) {
	    if( hooksV[i].contains( bi )) {
		hooksV[i].removeElement( bi );
		hooks[i]=null;
	    }
	}
	allModulesV.removeElement( bi );
	allModules=null;
    }

    public Object[] getModules( int type )
    {
	if( hooks[type] != null ) {
	    return hooks[type];
	}

	hooks[type]=new Object[hooksV[type].size()];
	hooksV[type].copyInto( hooks[type] );
	return hooks[type];
    }
    
    /** Get all interceptors
     */
    public Object[] getModules()
    {
	if( allModules!=null ) {
	    return allModules;
	}
	allModules=new Object[allModulesV.size()];
	allModulesV.copyInto( allModules );
	return allModules;
    }

    private void debug(String s ) {
	System.out.println("Hooks: " + s );
    }

    /** Test if the interceptor implements a particular
     *  method
     */
    private static boolean hasHook( Object obj, String methodN ) {
	if( hookFinder==null ) 
	    return true;
	return hookFinder.hasHook( obj, methodN );

    }

    // -------------------- hook for hook detection --------------------
    
    /** Interface that decouples the Hooks from the introspection code.
	We want to allow future modes that are not based on introspection -
	for example declarative	( using modules.xml declarations ) or
	based on code generation ( introspection done at deploy time ).
    */
    public static interface HookFinder {
	public boolean hasHook( Object obj, String hookName );
    }
    
    // 
    static HookFinder hookFinder=null;
    
    public static void setHookFinder( HookFinder hf ) {
	hookFinder=hf;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3918.java