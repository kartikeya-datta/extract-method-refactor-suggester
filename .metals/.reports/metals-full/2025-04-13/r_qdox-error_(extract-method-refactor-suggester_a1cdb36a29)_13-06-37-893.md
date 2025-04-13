error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6986.java
text:
```scala
J@@Initializer jinit = (JInitializer) Thread.currentThread().getContextClassLoader().loadClass(ins[i]).newInstance();

/*
 * @(#) JInterceptorStore.java	1.0 02/07/15
 *
 * Copyright (C) 2002 - INRIA (www.inria.fr)
 *
 * CAROL: Common Architecture for RMI ObjectWeb Layer
 *
 * This library is developed inside the ObjectWeb Consortium,
 * http://www.objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 *
 */
package org.objectweb.carol.rmi.jrmp.interceptor;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import org.objectweb.carol.util.configuration.TraceCarol;

/**
 * Class <code>JInterceptorStore</code> is the CAROL JRMP Client and Server Interceptors
 * Storage System
 * 
 * @author  Guillaume Riviere (Guillaume.Riviere@inrialpes.fr)
 * @version 1.0, 10/03/2003
 */
public class JInterceptorStore {

    /**
     * Initilazer class prefix
     */
    public static String INTIALIZER_PREFIX = "org.objectweb.PortableInterceptor.JRMPInitializerClass";

    /**
     * private boolean for intialisation 
     */
    private static boolean init = false;

	/**
	 * private Interceptor for Context propagation
	 */
	private static JServerRequestInterceptor [] sis = null;

   /**
	* private Interceptor for Context propagation
	*/
   private static JClientRequestInterceptor [] cis = null;
   
   /**
	* private remote Interceptor cache for Context propagation
	*/
   private static JClientRequestInterceptor [] rcis = null; 
   
   /**
	* private remote UID for cache
	*/
   private static UID uid = null;
   
   /**
    * private remote addr for cache 
    */
   private static byte [] address = null;
	
    /**
     * private Interceptors Initializers for Context propagation
     */
    private static String [] initializers = null;
    
    /**
     *  
     * JRMPINfo Impl
     */
	private static JRMPInitInfoImpl jrmpInfo = new JRMPInitInfoImpl();	
    
    /**
     * Intialize interceptors for a carol server
     */
    static {
	if (!init) {
	    // Load the Interceptors
	    try {
		JInitInfo jrmpInfo = new JRMPInitInfoImpl();
		String [] ins = getJRMPInitializers();
		for (int i = 0; i < ins.length ; i ++) {
		    JInitializer jinit = (JInitializer) Class.forName(ins[i]).newInstance();
		    jinit.pre_init(jrmpInfo);
		    jinit.post_init(jrmpInfo);
		}	    
		sis = jrmpInfo.getServerRequestInterceptors();
		cis = jrmpInfo.getClientRequestInterceptors();
		// fisrt remote reference = local reference
		rcis = cis;
		uid = JInterceptorHelper.getSpaceID();
		address=JInterceptorHelper.getInetAddress();
	    } catch ( Exception e) {
		//we did not found the interceptor do nothing but a trace ?
		TraceCarol.error("JrmpPRODelegate(), No interceptors found", e);
	    }	
	}

    }

    /**
     * get the local server interceptor
     */
    public static JServerRequestInterceptor [] getLocalServerInterceptors() {
	return sis;
    }

    /**
     * get the local client interceptor
     */
    public static JClientRequestInterceptor [] getLocalClientInterceptors() {
	return cis;
    }

     /**
     * Get Intializers method
     * @return JRMP Initializers enuumeration
     */
    public static String [] getJRMPInitializers() {
	if (!init) {
	    ArrayList ins =  new ArrayList();
	    Properties sys = System.getProperties();
	    for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements() ;) {
		String pkey = (String)e.nextElement();
		if (pkey.startsWith(INTIALIZER_PREFIX)) {
		    ins.add(pkey.substring(INTIALIZER_PREFIX.length() + 1));
		}
	    }
	    int sz = ins.size();
	    initializers=new String [sz];
	    for (int i = 0; i < sz; i++) {
		initializers[i] = (String)ins.get(i);
	    }	 
	    return initializers;
	} else {
	    return initializers;
	}
    }

	/**
	 * Get interceptor if exist
	 * @param raddr The remote adress (later)
	 * @param ruid The remote uid (later)
	 * @param ia iterceptors initializers
	 * @return JClientRequestInterceptors [] , the interceptors
	 */
	public synchronized static JClientRequestInterceptor []  setRemoteInterceptors(byte [] raddr,UID ruid,String [] ia) {
		if ((Arrays.equals(raddr, address))
		&&(ruid.equals(uid))) {
				return rcis;
		} else {
			jrmpInfo.clear();	
		    for (int i = 0; i < ia.length ; i ++) {
			JInitializer jinit = null;
			try {
				jinit = (JInitializer) Class.forName(ia[i]).newInstance();
				jinit.pre_init(jrmpInfo);
				jinit.post_init(jrmpInfo);
			} catch (Exception e) {
				TraceCarol.error("can not load interceptors", e);
			} 
		}	
		ruid=uid;
		address=raddr;
		rcis = jrmpInfo.getClientRequestInterceptors();
		return rcis;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6986.java