error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8222.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8222.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8222.java
text:
```scala
a@@ssertEquals(expected, "string2");

/*
 * @(#) MultiProtocolTests.java	1.0 02/07/15
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
package org.objectweb.carol.jtests.conform.basic.clients;

// java import
import java.rmi.Remote;

// javax import 
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;

// junit import 
import junit.framework.TestSuite;
import junit.framework.Test; 
import junit.framework.TestCase;

// carol tests import 
import org.objectweb.carol.jtests.conform.basic.server.BasicObjectItf;
import org.objectweb.carol.jtests.conform.basic.server.BasicMultiObjectItf;
import org.objectweb.carol.jtests.conform.basic.server.BasicObjectRef;

/*
 * Class <code>MultiProtocolTests</code> is a Junit BasicTest Test :
 * Test The InitialContext and the PortableRemoteObject situation with remote object
 * 
 * @author  Guillaume Riviere (Guillaume.Riviere@inrialpes.fr)
 * @version 1.0, 15/07/2002   
 */
public class MultiProtocolTests extends TestCase {

    /**
     * Name of the basic remote object (in all name services)
     */
    private String  basicName = null;
    
    /**
     * Name of the basic multi remote object (in all name services)
     */  
    private String  basicMultiName = null;    

    /**
     * Name of the basic object ref (in all name services)
     */  
    private String  basicRefName = null; 

    /**
     * Initial Contexts
     */
    private InitialContext  ic = null;

    /**
     * TheBasicObject
     */
    private BasicObjectItf ba = null;

    /**
     * TheBasicMultiObject
     */
    private BasicMultiObjectItf bma = null;
    

    /**
     * Constructor
     * @param String Name for this test
     */
    public  MultiProtocolTests(String name) {
	super(name);
    }   

    /**
     * Setup Method
     */
    public void setUp() {

	
	try {
	    	    
	    // set the object name 
	    basicName = "basicname";
	    basicMultiName = "basicmultiname";
	    basicRefName = "basicrefname";
	    
	    // lookup to the remote objects
	    ba = (BasicObjectItf)PortableRemoteObject.narrow(ic.lookup(basicName), BasicObjectItf.class);
	    bma = (BasicMultiObjectItf)PortableRemoteObject.narrow(ic.lookup(basicMultiName), BasicMultiObjectItf.class);

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("SetUp() Fail" + e);
	}
	
    }

    /**
     * set the initial context for this test
     * @param ic the initial context to set 
     */
    public void setInitialContext(InitialContext ic) {
	this.ic = ic;
    }
    
    
    /**
     * tearDown method
     */
    public void tearDown() {
	try {
	    basicName = null;
	    basicMultiName = null;
	    ba = null;
	    bma = null;
	    super.tearDown();
	} catch (Exception e) {
	    fail("tearDown() Fail" + e);
	} 
    } 

    /**
     * Test Method ,
     * Test an access on a remote object
     * The default orb is used for this access
     */
    public void testString() {
	try {
	    String expected = ba.getString();
	    assertEquals(expected, "string");
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Can't get string" + e);  
	}
    }

    /**
     * Test Method ,
     * Test an access on a remote object which also access
     * to remote object, This tests use 2 call via default protocol
     * The default orb is used for this access
     */
    public void testMultiString() {
	try {
	    String expected = bma.getMultiString();
	    assertEquals(expected, "multi string call: " + "string");
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Can't get multi string" + e);  	    
	}
    }

    /**
     * Test Method ,
     * Test an access on a refearence 
     */
    public void testReferenceString() {
	try {
	    String expected = bma.getBasicRefString();
	    assertEquals(expected, "string");
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Can't get ref string" + e);  	    
	}
    }


    /**
     * Test Method ,
     * Test an access on a remote object
     * The default orb is used for this access
     */
    public void testStub() {
	try {
	    BasicObjectItf ob = (BasicObjectItf)PortableRemoteObject.narrow(bma.getBasicObject(), BasicObjectItf.class);
	    String expected = ob.getString();
	    assertEquals(expected, "string");
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Can't narrow Remote Object :" + e);  
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8222.java