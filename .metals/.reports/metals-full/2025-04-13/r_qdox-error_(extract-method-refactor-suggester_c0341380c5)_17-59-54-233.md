error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 901
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2233.java
text:
```scala
public class ContextedRuntimeException extends RuntimeException implements ExceptionContext {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
p@@ackage org.apache.commons.lang3.exception;

import java.util.Set;

/**
 * <p>
 * A runtime exception that provides an easy and safe way to add contextual information.
 * </p><p>
 * An exception trace itself is often insufficient to provide rapid diagnosis of the issue.
 * Frequently what is needed is a select few pieces of local contextual data.
 * Providing this data is tricky however, due to concerns over formatting and nulls.
 * </p><p>
 * The contexted exception approach allows the exception to be created together with a
 * map of context values. This additional information is automatically included in the
 * message and printed stack trace.
 * </p><p>
 * An checked version of this exception is provided by ContextedException.
 * </p>
 * <p>
 * To use this class write code as follows:
 * </p>
 * <pre>
 *   try {
 *     ...
 *   } catch (Exception e) {
 *     throw new ContextedException("Error posting account transaction", e)
 *          .addValue("accountNumber", accountNumber)
 *          .addValue("amountPosted", amountPosted)
 *          .addValue("previousBalance", previousBalance)
 *   }
 * }
 * </pre>
 * </p><p>
 * The output in a printStacktrace() (which often is written to a log) would look something like the following:
 * <pre>
 * org.apache.commons.lang3.exception.ContextedRuntimeException: java.lang.Exception: Error posting account transaction
 *  Exception Context:
 *  [accountNumber=null]
 *  [amountPosted=100.00]
 *  [previousBalance=-2.17]
 *
 *  ---------------------------------
 *  at org.apache.commons.lang3.exception.ContextedRuntimeExceptionTest.testAddValue(ContextedExceptionTest.java:88)
 *  ..... (rest of trace)
 * </pre>
 * </p>
 * 
 * @see ContextedException
 * @author Apache Software Foundation
 * @author D. Ashmore
 * @author J&ouml;rg Schaible
 * @since 3.0
 */
public class ContextedRuntimeException extends Exception implements ExceptionContext {

    /** The serialization version. */
    private static final long serialVersionUID = 1459691936045811817L;
    /** The context where the data is stored. */
    private final ExceptionContext exceptionContext;

    /**
     * Instantiates ContextedRuntimeException without message or cause.
     * <p>
     * The context information is stored using a default implementation.
     */
    public ContextedRuntimeException() {
        super();
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ContextedRuntimeException with message, but without cause.
     * <p>
     * The context information is stored using a default implementation.
     * 
     * @param message  the exception message, may be null
     */
    public ContextedRuntimeException(String message) {
        super(message);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ContextedRuntimeException with cause, but without message.
     * <p>
     * The context information is stored using a default implementation.
     * 
     * @param cause  the underlying cause of the exception, may be null
     */
    public ContextedRuntimeException(Throwable cause) {
        super(cause);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ContextedRuntimeException with cause and message.
     * <p>
     * The context information is stored using a default implementation.
     * 
     * @param message  the exception message, may be null
     * @param cause  the underlying cause of the exception, may be null
     */
    public ContextedRuntimeException(String message, Throwable cause) {
        super(message, cause);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ContextedRuntimeException with cause, message, and ExceptionContext.
     * 
     * @param message  the exception message, may be null
     * @param cause  the underlying cause of the exception, may be null
     * @param context  the context used to store the additional information, null uses default implementation
     */
    public ContextedRuntimeException(String message, Throwable cause, ExceptionContext context) {
        super(message, cause);
        if (context == null) {
            context = new DefaultExceptionContext();
        }
        exceptionContext = context;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds information helpful to a developer in diagnosing and correcting
     * the problem.  For the information to be meaningful, the value passed
     * should have a reasonable toString() implementation.
     * <p>
     * Note: This exception is only serializable if the object added is serializable.
     * 
     * @param label  a textual label associated with information, null not recommended
     * @param value  information needed to understand exception, may be null
     * @return this, for method chaining
     */
    public ContextedRuntimeException addValue(String label, Object value) {        
        exceptionContext.addValue(label, value);
        return this;
    }

    /**
     * Retrieves a contextual data value associated with the label.
     * 
     * @param label  the label to get the contextual value for, may be null
     * @return the contextual value associated with the label, may be null
     */
    public Object getValue(String label) {
        return exceptionContext.getValue(label);
    }

    /**
     * Retrieves the labels defined in the contextual data.
     * 
     * @return the set of labels, never null
     */
    public Set<String> getLabelSet() {
        return exceptionContext.getLabelSet();
    }

    /**
     * Provides the message explaining the exception, including the contextual data.
     * 
     * @see java.lang.Throwable#getMessage()
     * @return the message, never null
     */
    @Override
    public String getMessage(){
        return getFormattedExceptionMessage(super.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    public String getFormattedExceptionMessage(String baseMessage) {
        return exceptionContext.getFormattedExceptionMessage(baseMessage);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2233.java