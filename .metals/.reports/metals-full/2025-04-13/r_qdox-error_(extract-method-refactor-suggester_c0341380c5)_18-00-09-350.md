error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14041.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14041.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14041.java
text:
```scala
r@@eturn instantiator.makeInstance(desc,handler, clazzes, args);

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.comm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.ecf.core.comm.provider.ISynchAsynchConnectionInstantiator;
import org.eclipse.ecf.core.util.AbstractFactory;
import org.eclipse.ecf.internal.core.Trace;

public class ConnectionFactory {
    private static Hashtable connectiontypes = new Hashtable();
    private static Trace debug = Trace.create("connectionfactory");

    public final static ConnectionDescription addDescription(
            ConnectionDescription scd) {
        debug("addDescription(" + scd + ")");
        return addDescription0(scd);
    }

    protected static ConnectionDescription addDescription0(
            ConnectionDescription n) {
        if (n == null)
            return null;
        return (ConnectionDescription) connectiontypes.put(n.getName(), n);
    }

    public final static boolean containsDescription(ConnectionDescription scd) {
        debug("containsDescription(" + scd + ")");
        return containsDescription0(scd);
    }

    protected static boolean containsDescription0(ConnectionDescription scd) {
        if (scd == null)
            return false;
        return connectiontypes.containsKey(scd.getName());
    }

    private static void debug(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    private static void dumpStack(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg);
        }
    }

    public final static ConnectionDescription getDescription(
            ConnectionDescription scd) {
        debug("getDescription(" + scd + ")");
        return getDescription0(scd);
    }

    protected static ConnectionDescription getDescription0(
            ConnectionDescription scd) {
        if (scd == null)
            return null;
        return (ConnectionDescription) connectiontypes.get(scd.getName());
    }

    protected static ConnectionDescription getDescription0(String name) {
        if (name == null)
            return null;
        return (ConnectionDescription) connectiontypes.get(name);
    }

    public final static ConnectionDescription getDescriptionByName(String name) {
        debug("getDescriptionByName(" + name + ")");
        return getDescription0(name);
    }

    public static final List getDescriptions() {
        debug("getDescriptions()");
        return getDescriptions0();
    }

    protected static List getDescriptions0() {
        return new ArrayList(connectiontypes.values());
    }

    public static ISynchAsynchConnection makeSynchAsynchConnection(
            ISynchAsynchConnectionEventHandler handler,
            ConnectionDescription desc, Object[] args)
            throws ConnectionInstantiationException {
        if (handler == null)
            throw new ConnectionInstantiationException(
                    "ISynchAsynchConnectionEventHandler cannot be null");
        return makeSynchAsynchConnection(handler, desc, null, args);
    }

    public static ISynchAsynchConnection makeSynchAsynchConnection(
            ISynchAsynchConnectionEventHandler handler,
            ConnectionDescription desc, String[] argTypes, Object[] args)
            throws ConnectionInstantiationException {
        debug("makeSynchAsynchConnection(" + handler + "," + desc + ","
                + Trace.convertStringAToString(argTypes) + ","
                + Trace.convertObjectAToString(args) + ")");
        if (handler == null)
            throw new ConnectionInstantiationException(
                    "ISynchAsynchConnectionEventHandler cannot be null");
        if (desc == null)
            throw new ConnectionInstantiationException(
                    "ConnectionDescription cannot be null");
        ConnectionDescription cd = desc;
        if (cd == null)
            throw new ConnectionInstantiationException("ConnectionDescription "
                    + desc.getName() + " not found");
        ISynchAsynchConnectionInstantiator instantiator = null;
        Class clazzes[] = null;
        try {
            instantiator = (ISynchAsynchConnectionInstantiator) cd
                    .getInstantiator();
            clazzes = AbstractFactory.getClassesForTypes(argTypes, args, cd
                    .getClassLoader());
            if (instantiator == null)
                throw new InstantiationException(
                        "Instantiator for ConnectionDescription "
                                + cd.getName() + " is null");
        } catch (Exception e) {
            throw new ConnectionInstantiationException(
                    "Exception getting instantiator for '" + desc.getName()
                            + "'", e);
        }
        debug("makeSynchAsynchConnection:got instantiator:" + instantiator);
        // Ask instantiator to actually create instance
        return instantiator.makeInstance(handler, clazzes, args);
    }

    public static ISynchAsynchConnection makeSynchAsynchConnection(
            ISynchAsynchConnectionEventHandler handler, String descriptionName)
            throws ConnectionInstantiationException {
        if (handler == null)
            throw new ConnectionInstantiationException(
                    "ISynchAsynchConnectionEventHandler cannot be null");
        ConnectionDescription desc = getDescriptionByName(descriptionName);
        if (desc == null)
            throw new ConnectionInstantiationException(
                    "Connection type named '" + descriptionName + "' not found");
        return makeSynchAsynchConnection(handler, desc, null, null);
    }

    public static ISynchAsynchConnection makeSynchAsynchConnection(
            ISynchAsynchConnectionEventHandler handler, String descriptionName,
            Object[] args) throws ConnectionInstantiationException {
        if (handler == null)
            throw new ConnectionInstantiationException(
                    "ISynchAsynchConnectionEventHandler cannot be null");
        ConnectionDescription desc = getDescriptionByName(descriptionName);
        if (desc == null)
            throw new ConnectionInstantiationException(
                    "Connection type named '" + descriptionName + "' not found");
        return makeSynchAsynchConnection(handler, desc, args);
    }

    public static ISynchAsynchConnection makeSynchAsynchConnection(
            ISynchAsynchConnectionEventHandler handler, String descriptionName,
            String[] argTypes, Object[] args)
            throws ConnectionInstantiationException {
        if (handler == null)
            throw new ConnectionInstantiationException(
                    "ISynchAsynchConnectionEventHandler cannot be null");
        ConnectionDescription desc = getDescriptionByName(descriptionName);
        if (desc == null)
            throw new ConnectionInstantiationException(
                    "Connection type named '" + descriptionName + "' not found");
        return makeSynchAsynchConnection(handler, desc, argTypes, args);
    }

    public final static ConnectionDescription removeDescription(
            ConnectionDescription scd) {
        debug("removeDescription(" + scd + ")");
        return removeDescription0(scd);
    }

    protected static ConnectionDescription removeDescription0(
            ConnectionDescription n) {
        if (n == null)
            return null;
        return (ConnectionDescription) connectiontypes.remove(n.getName());
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14041.java