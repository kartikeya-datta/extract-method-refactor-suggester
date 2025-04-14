error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5519.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5519.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5519.java
text:
```scala
s@@etInt.invoke(instance, new Object[] { "portnum", Integer.valueOf(serverport) });//$NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Implements a BeanShell server to allow access to JMeter variables and
 * methods.
 *
 * To enable, define the JMeter property: beanshell.server.port (see
 * JMeter.java) beanshell.server.file (optional, startup file)
 *
 */
public class BeanShellServer implements Runnable {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private final int serverport;

    private final String serverfile;

    /**
     *
     */
    public BeanShellServer(int port, String file) {
        super();
        serverfile = file;// can be the empty string
        serverport = port;
    }

    // For use by the server script
    static String getprop(String s) {
        return JMeterUtils.getPropDefault(s, s);
    }

    // For use by the server script
    static void setprop(String s, String v) {
        JMeterUtils.getJMeterProperties().setProperty(s, v);
    }

    public void run() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            Class<?> Interpreter = loader.loadClass("bsh.Interpreter");//$NON-NLS-1$
            Object instance = Interpreter.newInstance();
            Class<String> string = String.class;
            Class<Object> object = Object.class;

            Method eval = Interpreter.getMethod("eval", new Class[] { string });//$NON-NLS-1$
            Method setObj = Interpreter.getMethod("set", new Class[] { string, object });//$NON-NLS-1$
            Method setInt = Interpreter.getMethod("set", new Class[] { string, int.class });//$NON-NLS-1$
            Method source = Interpreter.getMethod("source", new Class[] { string });//$NON-NLS-1$

            setObj.invoke(instance, new Object[] { "t", this });//$NON-NLS-1$
            setInt.invoke(instance, new Object[] { "portnum", new Integer(serverport) });//$NON-NLS-1$

            if (serverfile.length() > 0) {
                try {
                    source.invoke(instance, new Object[] { serverfile });
                } catch (InvocationTargetException e1) {
                    log.warn("Could not source " + serverfile);
                    Throwable t= e1.getCause();
                    if (t != null) {
                        log.warn(t.toString());
                    }
                }
            }
            eval.invoke(instance, new Object[] { "setAccessibility(true);" });//$NON-NLS-1$
            eval.invoke(instance, new Object[] { "server(portnum);" });//$NON-NLS-1$

        } catch (ClassNotFoundException e) {
            log.error("Beanshell Interpreter not found");
        } catch (Exception e) {
            log.error("Problem starting BeanShell server ", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5519.java