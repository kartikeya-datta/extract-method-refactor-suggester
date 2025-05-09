error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1178.java
text:
```scala
i@@f (clazz == null && clazz.length() != 0) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;


/**
 * Assembles the constants declared in a Java class in
 * <code>key1=value1(line separator)key2=value2</code>
 * format.
 *<p>
 * Notes:
 * <ol>
 * <li>This filter uses the BCEL external toolkit.
 * <li>This assembles only those constants that are not created
 * using the syntax <code>new whatever()</code>
 * <li>This assembles constants declared using the basic datatypes
 * and String only.</li>
 * <li>The access modifiers of the declared constants do not matter.</li>
 *</ol>
 * Example:<br>
 * <pre>&lt;classconstants/&gt;</pre>
 * Or:
 * <pre>&lt;filterreader
 *    classname=&quot;org.apache.tools.ant.filters.ClassConstants&quot;/&gt;</pre>
 */
public final class ClassConstants
    extends BaseFilterReader
    implements ChainableReader {
    /** Data that must be read from, if not null. */
    private String queuedData = null;

    /** Helper Class to be invoked via reflection. */
    private static final String JAVA_CLASS_HELPER =
        "org.apache.tools.ant.filters.util.JavaClassHelper";

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public ClassConstants() {
        super();
    }

    /**
     * Creates a new filtered reader. The contents of the passed-in reader
     * are expected to be the name of the class from which to produce a
     * list of constants.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public ClassConstants(final Reader in) {
        super(in);
    }

    /**
     * Reads and assembles the constants declared in a class file.
     *
     * @return the next character in the list of constants, or -1
     * if the end of the resulting stream has been reached
     *
     * @exception IOException if the underlying stream throws an IOException
     * during reading, or if the constants for the specified class cannot
     * be read (for example due to the class not being found).
     */
    public int read() throws IOException {

        int ch = -1;

        if (queuedData != null && queuedData.length() == 0) {
            queuedData = null;
        }

        if (queuedData != null) {
            ch = queuedData.charAt(0);
            queuedData = queuedData.substring(1);
            if (queuedData.length() == 0) {
                queuedData = null;
            }
        } else {
            final String clazz = readFully();
            if (clazz == null) {
                ch = -1;
            } else {
                final byte[] bytes = clazz.getBytes("ISO-8859-1");
                try {
                    final Class javaClassHelper =
                        Class.forName(JAVA_CLASS_HELPER);
                    if (javaClassHelper != null) {
                        final Class[] params = {
                            byte[].class
                        };
                        final Method getConstants =
                            javaClassHelper.getMethod("getConstants", params);
                        final Object[] args = {
                            bytes
                        };
                        // getConstants is a static method, no need to
                        // pass in the object
                        final StringBuffer sb = (StringBuffer)
                                getConstants.invoke(null, args);
                        if (sb.length() > 0) {
                            queuedData = sb.toString();
                            return read();
                        }
                    }
                } catch (NoClassDefFoundError ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    throw ex;
                } catch (InvocationTargetException ex) {
                    Throwable t = ex.getTargetException();
                    if (t instanceof NoClassDefFoundError) {
                        throw (NoClassDefFoundError) t;
                    }
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    }
                    throw new BuildException(t);
                } catch (Exception ex) {
                    throw new BuildException(ex);
                }
            }
        }
        return ch;
    }

    /**
     * Creates a new ClassConstants using the passed in
     * Reader for instantiation.
     *
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     *
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public Reader chain(final Reader rdr) {
        ClassConstants newFilter = new ClassConstants(rdr);
        return newFilter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1178.java