error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7389.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7389.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7389.java
text:
```scala
T@@raceCarol.error("ProtocolCurrent() Exception", e);

/*
 * @(#) JUnicastServerRef.java	1.0 02/07/15
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
package org.objectweb.carol.util.multi;

//javax import
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.CORBA.PortableRemoteObjectDelegate;

import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;

//carol import
import org.objectweb.carol.util.configuration.RMIConfiguration;
import org.objectweb.carol.util.configuration.CarolConfiguration; 
import org.objectweb.carol.util.configuration.TraceCarol; 

/**
 * Class <code>ProtocolCurrent</code>For handling the association Rmi/ Thread
 * 
 * @author  Guillaume Riviere (Guillaume.Riviere@inrialpes.fr)
 * @version 1.0, 15/07/2002
 *
 */

public class ProtocolCurrent {

    /**
     * Protocols Portale Remote Object Delegate 
     */
    private static Hashtable prodHashtable = null;    

    /**
     * Context Array for each protocol
     */
    private static Hashtable icHashtable = null;

    /**
     * Protocol Number for default
     */
    private static String defaultRMI;

    /**
     * Thread Local for protocol context propagation
     */
    private static InheritableThreadLocal threadCtx;
     
    /**
     * private constructor for singleton
     */
    private static ProtocolCurrent current = new ProtocolCurrent () ;

    /**
     * private constructor for unicicity
     */
    private ProtocolCurrent() {

	try {

	    threadCtx = new InheritableThreadLocal(); 
	    prodHashtable = new Hashtable();
	    icHashtable = new Hashtable();
	    //get rmi configuration  hashtable 	    
	    Hashtable allRMIConfiguration = CarolConfiguration.getAllRMIConfiguration();	    
	    int nbProtocol = allRMIConfiguration.size();
	    for (Enumeration e = allRMIConfiguration.elements() ; e.hasMoreElements() ;) {
		RMIConfiguration currentConf = (RMIConfiguration)e.nextElement();
		String rmiName = currentConf.getName();
		// get the PRO 
		prodHashtable.put(rmiName, (PortableRemoteObjectDelegate)Class.forName(currentConf.getPro()).newInstance());
		icHashtable.put(rmiName,  new InitialContext(currentConf.getJndiProperties()));
	    }
	    defaultRMI = CarolConfiguration.getDefaultProtocol().getName();
	    // set the default protocol
	    threadCtx.set(defaultRMI) ;
	    
	    // trace Protocol current
	    if (TraceCarol.isDebugCarol()) {
		TraceCarol.debugCarol("ProtocolCurrent.ProtocolCurrent()");
		TraceCarol.debugCarol("Number of rmi:" + icHashtable.size());
		TraceCarol.debugCarol("Default:"+ defaultRMI);
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Method getCurrent
     *
     * @return ProtocolCurrent return the current 
     *
     */
    public static ProtocolCurrent getCurrent() {
	return current ;
    }


    /**
     * This method if for setting one rmi context
     * 
     * @param s the rmi name
     */
    public void setRMI (String s) {
	threadCtx.set(s) ;
    }

    /**
     * set the default protocol
     */
    public void setDefault () {
	threadCtx.set(defaultRMI) ;
    }

   /**
    * Get the Portable Remote Object Hashtable
    * @return  Hashtable the hashtable of PROD
    */
    public  Hashtable getPortableRemoteObjectHashtable() {
	return prodHashtable;
    }

   /**
    * Get the Context Hashtable
    * @return Hashtable the hashtable of Context 
    */
    public Hashtable getContextHashtable() {
	return icHashtable;
    }    

    /**
     * Get current protocol PROD
     * @return PortableRemoteObjectDelegate the portable remote object
     */
    public PortableRemoteObjectDelegate getCurrentPortableRemoteObject() {
	if (threadCtx.get() == null) {
	    return (PortableRemoteObjectDelegate)prodHashtable.get(defaultRMI); 
	} else {
	    return (PortableRemoteObjectDelegate)prodHashtable.get((String)threadCtx.get());
	}
    }

    /**
     * Get current protocol Initial Context
     * @return InitialContext the initial Context
     */
    public Context getCurrentInitialContext() {
	if (threadCtx.get() == null) {
	    return (Context)icHashtable.get(defaultRMI); 
	} else {
	    return (Context)icHashtable.get((String)threadCtx.get());
	}
    }

    /**
     * Get current protocol RMI name
     * @return String the RMI name
     */
    public String getCurrentRMIName() {
	if (threadCtx.get() == null) {
	    return defaultRMI; 
	} else {
	    return (String)threadCtx.get();
	}
    }

    /**
     * To string method 
     */
    public String toString() {
	return "\nnumber of rmi:" + icHashtable.size() 
	    + "\ndefault:"+ defaultRMI;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7389.java