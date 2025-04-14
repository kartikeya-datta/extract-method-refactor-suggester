error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,36]

error in qdox parser
file content:
```java
offset: 36
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10877.java
text:
```scala
/*RmpSocketListener management = */n@@ew RmpSocketListener();

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *   Alexandre Vasseur         initial implementation (derivative from AspectWerkz)
 *******************************************************************************/
package org.aspectj.weaver.loadtime;

import com.bea.jvm.JVMFactory;
import com.jrockit.management.rmp.RmpSocketListener;

/**
 * JRockit (tested with 7SP4 and 8.1) preprocessor Adapter based on JMAPI <p/>JRockit has a low
 * level API for hooking ClassPreProcessor, allowing the use of online weaving at full speed.
 * Moreover, JRockit does not allow java.lang.ClassLoader overriding thru -Xbootclasspath/p option.
 * <p/>The ClassPreProcessor
 * implementation and all third party jars CAN reside in the standard classpath. <p/>The command
 * line will look like:
 * <code>"%JAVA_COMMAND%" -Xmanagement:class=org.aspectj.weaver.loadtime.JRockitAgent -cp ...</code>
 * Note: there can be some NoClassDefFoundError due to classpath limitation - as described in
 * http://edocs.bea.com/wls/docs81/adminguide/winservice.html <p/>In order to use the BEA JRockit
 * management server (for further connection of management console or runtime analyzer), the regular
 * option -Xmanagement will not have any effect prior to JRockit 8.1 SP2. Instead, use <code>-Dmanagement</code>.
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class JRockitAgent implements com.bea.jvm.ClassPreProcessor {

    /**
     * Concrete preprocessor
     */
    private final static ClassPreProcessor s_preProcessor;

    private static boolean START_RMP_SERVER = false;

    static {
        START_RMP_SERVER = System.getProperties().containsKey("management");
        try {
            s_preProcessor = new Aj();
            s_preProcessor.initialize();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("could not initialize JRockitAgent preprocessor due to: " + e.toString());
        }
    }

    /**
     * The JMAPI ClassPreProcessor must be self registrating
     */
    public JRockitAgent() {
        if (START_RMP_SERVER) {
            // the management server will be spawned in a new thread
            RmpSocketListener management = new RmpSocketListener();
        }
        JVMFactory.getJVM().getClassLibrary().setClassPreProcessor(this);
    }

    /**
     * Weave a class
     *
     * @param caller   classloader
     * @param name     of the class to weave
     * @param bytecode original
     * @return bytecode weaved
     */
    public byte[] preProcess(ClassLoader caller, String name, byte[] bytecode) {
        if (caller == null || caller.getParent() == null) {
            return bytecode;
        } else {
            return s_preProcessor.preProcess(name, bytecode, caller);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10877.java