error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4981.java
text:
```scala
u@@nicastClass = Thread.currentThread().getContextClassLoader().loadClass(className);

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
package org.objectweb.carol.rmi.multi;

//java import
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.rmi.CORBA.PortableRemoteObjectDelegate;
import org.objectweb.carol.util.configuration.CarolDefaultValues;


/**
 * class <code>JeremiePRODelegate</code> for the mapping between Jeremie UnicastRemoteObject and PortableRemoteObject
 * 
 * @author  Guillaume Riviere (Guillaume.Riviere@inrialpes.fr)
 * @version 1.0, 15/07/2002  
 */
public class JeremiePRODelegate implements PortableRemoteObjectDelegate {

	/**
	 * port number 
	 */
	private int port;
	
    /**
     * UnicastRemoteObjectClass
     */
    private static String className = "org.objectweb.jeremie.libs.binding.moa.UnicastRemoteObject";

    /**
     * Instance object for this UnicastRemoteObject
     */
    private static Class unicastClass = null;

    /**
     * Empty constructor for instanciate this class
     */
    public JeremiePRODelegate() throws Exception {
	// class for name
	unicastClass = Class.forName(className);
	this.port=new Integer(System.getProperty(CarolDefaultValues.PORT_NUMBER_PROPERTY, "0")).intValue();
    }


    /**
     * Export a Remote Object 
     * @param Remote object to export
     * @exception RemoteException exporting remote object problem 
     */
    public void exportObject(Remote obj) throws RemoteException {
	try {
	    Method exportO = unicastClass.getMethod("exportObject",  new Class [] { Remote.class, Integer.TYPE });
	    exportO.invoke(unicastClass, (new Object[] { obj , new Integer(port)} ));
	} catch (Exception e) {
	    throw new RemoteException(e.toString());
	}
    }

    
    /**
     * Method for unexport object
     * @param Remote obj object to unexport 
     * @exception NoSuchObjectException if the object is not currently exported
     */
    public void unexportObject(Remote obj) throws NoSuchObjectException {
	try {
	    Method unexportO = unicastClass.getMethod("unexportObject",  new Class [] { Remote.class, Boolean.TYPE });
	    unexportO.invoke(unicastClass, (new Object[] { obj, Boolean.TRUE } ));
	} catch (Exception e) {
	    throw new NoSuchObjectException (e.toString());
	}
    }

    /**
     * Connection method
     * @param target a remote object;
     * @param source another remote object; 
     * @exception RemoteException if the connection fail
     */ 
    public void connect(Remote target,Remote source) throws RemoteException {
	// do nothing
    }

	
    /**
     * Narrow method
     * @param Remote obj the object to narrow 
     * @param Class newClass the expected type of the result  
     * @return an object of type newClass
     * @exception ClassCastException if the obj class is not compatible with a newClass cast 
     */
    public Object narrow(Object obj, Class newClass ) throws ClassCastException {
	if (newClass.isAssignableFrom(obj.getClass())) {
	    return obj;
	} else {
	    throw new ClassCastException("Can't cast "+obj.getClass().getName()+" in "+newClass.getName());
	}
    }

    /**
     * To stub method
     * @return the stub object    
     * @param Remote object to unexport    
     * @exception NoSuchObjectException if the object is not currently exported
     */
    public Remote toStub(Remote obj) throws NoSuchObjectException {
	try {
	    Method exportO = unicastClass.getMethod("toStub",  new Class [] { Remote.class });
	    return (Remote)exportO.invoke(unicastClass, (new Object[] { obj } ));
	} catch (Exception e) {
	    throw new NoSuchObjectException(e.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4981.java