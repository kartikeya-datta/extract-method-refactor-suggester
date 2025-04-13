error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[112,2]

error in qdox parser
file content:
```java
offset: 4596
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1972.java
text:
```scala
import org.eclipse.ecf.internal.provider.Trace;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.generic;

import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.provider.Trace;

public class ContainerInstantiator implements
        IContainerInstantiator {
    public static final String TCPCLIENT_NAME = "ecf.generic.client";
    public static final String TCPSERVER_NAME = "ecf.generic.server";

    public static final Trace debug = Trace.create("containerfactory");
    public ContainerInstantiator() {
        super();
    }
    protected void debug(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }
    protected void dumpStack(String msg, Throwable t) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(t,msg);
        }
    }
    protected ID getIDFromArg(Class type, Object arg)
            throws IDCreateException {
        if (arg instanceof ID) return (ID) arg;
        if (arg instanceof String) {
            String val = (String) arg;
            if (val == null || val.equals("")) {
                return IDFactory.getDefault().createGUID();
            } else return IDFactory.getDefault().createStringID((String) arg);
        } else if (arg instanceof Integer) {
            return IDFactory.getDefault().createGUID(((Integer) arg).intValue());
        } else
            return IDFactory.getDefault().createGUID();
    }

    protected Integer getIntegerFromArg(Class type, Object arg)
            throws NumberFormatException {
        if (arg instanceof Integer)
            return (Integer) arg;
        else if (arg != null) {
            return new Integer((String) arg);
        } else return new Integer(-1);
    }

    public IContainer createInstance(
            ContainerTypeDescription description, Class[] argTypes,
            Object[] args) throws ContainerCreateException {
        boolean isClient = true;
        if (description.getName().equals(TCPSERVER_NAME)) {
            debug("creating server");
            isClient = false;
        } else {
            debug("creating client");
        }
        ID newID = null;
        try {
            String [] argDefaults = description.getArgDefaults();
            newID = (argDefaults==null||argDefaults.length==0)?null:getIDFromArg(String.class,
                    description.getArgDefaults()[0]);
            Integer ka = (argDefaults==null||argDefaults.length < 2)?null:getIntegerFromArg(String.class, description
                    .getArgDefaults()[1]);
            if (args != null) {
                if (args.length > 0) {
                    newID = getIDFromArg(argTypes[0], args[0]);
                    if (args.length > 1) {
                        ka = getIntegerFromArg(argTypes[1],args[1]);
                    }
                }
            }
            debug("id="+newID+";keepAlive="+ka);
            // new ID must not be null
            if (newID == null)
                throw new ContainerCreateException(
                        "id must be provided");
            if (isClient) {
                return new TCPClientSOContainer(new SOContainerConfig(newID),
                        ka.intValue());
            } else {
                return new TCPServerSOContainer(new SOContainerConfig(newID),
                        ka.intValue());
            }
        } catch (ClassCastException e) {
            dumpStack("ClassCastException",e);
            throw new ContainerCreateException(
                    "Parameter type problem creating container", e);
        } catch (Exception e) {
            dumpStack("Exception",e);
            throw new ContainerCreateException(
                    "Exception creating generic container with id "+newID, e);
        }
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1972.java