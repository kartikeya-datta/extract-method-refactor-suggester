error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6255.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6255.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6255.java
text:
```scala
i@@f (!getContext().isGroupManager()) {

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.example.collab.share.url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.SharedObjectDescription;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.example.collab.Trace;
import org.eclipse.ecf.example.collab.share.GenericSharedObject;


public class ExecURL extends GenericSharedObject {
	
	public static Trace debug = Trace.create("urlsharedobject");
	
	ID receiver;
	String url;
	
	// Host
	public ExecURL(ID rcvr, String url) {
		this.receiver = rcvr;
		this.url = url;
	}
	public ExecURL() {
        
    }
    public void init(ISharedObjectConfig config) throws SharedObjectInitException {
        super.init(config);
        Map props = config.getProperties();
        debug("props is "+props);
        Object [] args = (Object []) props.get("args");
        debug("args is "+args);
        if (args != null && args.length > 1) {
            receiver = (ID) args[0];
            url = (String) args[1];
            debug("url is now "+url);
        }
    }
	public ExecURL(String url) {
		this.url = url;
	}
	protected void debug(String msg) {
		if (Trace.ON && debug != null) {
			debug.msg(msg);
		}
	}
	protected void debug(Throwable t, String msg) {
		if (Trace.ON && debug != null) {
			debug.dumpStack(t,msg);
		}
	}
	protected String getURL() {
		return url;
	}
	protected SharedObjectDescription getReplicaDescription(ID remoteMember)
	{
		String types[] = { ID.class.getName(), String.class.getName()}; 
		Object args[] = { receiver, url };
        HashMap map = new HashMap();
        map.put("args",args);
        map.put("types",types);
        return new SharedObjectDescription(getHomeContainerID(),
                                        getClass().getName(),
                                        map,
                                        replicateID++);
	}
	
	protected void replicate(ID remoteMember)
	{
        debug("replicateSelf("+remoteMember+")");
        // If we don't have a specific receiver, simply allow superclass to handle replication.
        if (receiver == null) { super.replicate(remoteMember); return; }
        // If we do have a specific receiver, only send create message to the specific receiver
        // if we're replicating on activation
        else if (remoteMember == null) {
            try {
                SharedObjectDescription createInfo = getReplicaDescription(receiver);
                if (createInfo != null) {
                     getContext().sendCreate(receiver, createInfo);
                }
            } catch (IOException e) {
                traceDump("Exception in replicateSelf",e);
                return;
            }
        }
	}
	
	public void activated(ID [] others) {
		debug("activated()");
		try {
			if (!getContext().isGroupServer()) {
				GetExec.showURL(url,true);
			} else {
				debug("Not executing commands because is server");
			}
		} catch (Exception e) {
			debug(e, "Exception on startup()");
			/*
			 * For now, just ignore failure.
			 */
		}
		super.activated(others);
		destroySelfLocal();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6255.java