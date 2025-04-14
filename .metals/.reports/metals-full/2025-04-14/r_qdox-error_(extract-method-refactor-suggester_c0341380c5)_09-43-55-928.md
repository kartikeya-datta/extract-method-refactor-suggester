error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6306.java
text:
```scala
I@@IOPCosNaming.this.stop();

/*
 * @(#) IIOPCosNaming.java	1.0 02/07/15
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
 */
package org.objectweb.carol.jndi.ns;

import javax.naming.InitialContext;
import java.util.Properties;
import java.io.InputStream;

import org.objectweb.carol.util.configuration.TraceCarol;
/*
 * Class <code>IIOPCosNaming</code> Start in a separated process
 * (see the sun orbd documentation)
 *
 * @author  Guillaume Riviere (Guillaume.Riviere@inrialpes.fr)
 * @version 1.0, 15/01/2003
 */
public class IIOPCosNaming implements NameService {

    /**
     * port number ( 12350 for default)
     */
    public int port=12350;

    /**
     * process of the cosnaming
     */
    public Process cosNamingProcess = null;

    /**
     * start Method, Start a new NameService or do nothing if the name service is all ready start
     * @param int port is port number
     * @throws NameServiceException if a problem occure 
     */
    public void start() throws NameServiceException {	
	if (TraceCarol.isDebugJndiCarol()) {
            TraceCarol.debugJndiCarol("IIOPCosNaming.start() on port:" + port);
        }
	try {
	    if (!isStarted()) {
		// start a new orbd procees
		if (port >= 0) {
		    cosNamingProcess = Runtime.getRuntime().exec(System.getProperty("java.home") +
								 System.getProperty("file.separator") +
								 "bin" + System.getProperty("file.separator") +
								 "orbd -ORBInitialPort " + port);
		    // wait for starting
		    Thread.sleep(2000);

		    // trace the start execution
		    InputStream cosError = cosNamingProcess.getErrorStream();
		    if (cosError.available() != 0) {
			byte [] b = new byte[cosError.available()];
			cosError.read(b);
			cosError.close();
			throw new NameServiceException("can not start cosnaming daemon:" + new String(b));
		    } 

		    InputStream cosOut = cosNamingProcess.getInputStream();
		    if (cosOut.available() != 0) {
			byte [] b = new byte[cosOut.available()];
			cosOut.read(b);
			cosOut.close();
			if (TraceCarol.isDebugJndiCarol()) {
			    TraceCarol.debugJndiCarol("IIOPCosNaming:");			    
			    TraceCarol.debugJndiCarol(new String(b));
			}
		    } 
		    
		    // add a shudown hook for this process
		    Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() {
				try {
				    stop();
				} catch (Exception e) {
				    TraceCarol.error("IIOPCosNaming ShutdownHook problem" ,e);
				}
			    }
			});
		} else {		  		
		    if (TraceCarol.isDebugJndiCarol()) {
			TraceCarol.debugJndiCarol("Can't start IIOPCosNaming, port="+port+" is < 0");
		    }
		}
	    } else {		
		if (TraceCarol.isDebugJndiCarol()) {
		    TraceCarol.debugJndiCarol("IIOPCosNaming is already start on port:" + port);
		}
	    }
	} catch (Exception e) {	    
	    throw new NameServiceException("can not start cosnaming daemon: " +e);
	}
    }

    /**
     * stop Method, Stop a NameService or do nothing if the name service is all ready stop
     * @throws NameServiceException if a problem occure 
     */
    public void stop() throws NameServiceException {
	if (TraceCarol.isDebugJndiCarol()) {
            TraceCarol.debugJndiCarol("IIOPCosNaming.stop()");
        }
	try {
	    // stop orbd procees
	    if (cosNamingProcess!=null) cosNamingProcess.destroy();
	    cosNamingProcess = null;
	} catch (Exception e) {	    
	    throw new NameServiceException("can not stop cosnaming daemon: " +e);
	}
    }

    /**
     * isStarted Method, check if a name service is started
     * @return boolean true if the name service is started
     */
    public boolean isStarted() {
	if (cosNamingProcess != null) return true;
	Properties prop = new Properties();
	prop.put("java.naming.factory.initial","com.sun.jndi.cosnaming.CNCtxFactory");
	prop.put("java.naming.provider.url","iiop://localhost:"+port);
	try {
	    InitialContext ic = new InitialContext(prop); 
	} catch (javax.naming.CommunicationException jcm) {
	    return false;
	} catch (Exception e) {
	    return true;
	}
	return true;
    }

    /**
     * set port method, set the port for the name service
     * @param int port number
     */
    public void setPort(int p) {
	if (TraceCarol.isDebugJndiCarol()) {
            TraceCarol.debugJndiCarol("IIOPCosNaming.setPort("+p+")");
        }
	if (p!= 0) {
	    port = p;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6306.java