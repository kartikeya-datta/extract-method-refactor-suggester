error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10818.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10818.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10818.java
text:
```scala
M@@ODULE(Constants.DRIVER, ModelType.STRING, true),

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.connector.subsystems.datasources;

import org.jboss.dmr.ModelType;

/**
 * Definition of a data-source attribute.
 *
 * @author John Bailey
 */
public enum AttributeDefinition {
    CONNECTION_URL(Constants.CONNECTION_URL, ModelType.STRING, true),
    DRIVER_CLASS(Constants.DRIVER_CLASS, ModelType.STRING, true),
    JNDINAME(Constants.JNDINAME, ModelType.STRING, true),
    MODULE(Constants.MODULE, ModelType.STRING, true),
    NEW_CONNECTION_SQL(Constants.NEW_CONNECTION_SQL, ModelType.STRING, false),
    POOLNAME(Constants.POOLNAME, ModelType.STRING, false),
    URL_DELIMITER(Constants.URL_DELIMITER, ModelType.STRING, false),
    URL_SELECTOR_STRATEGY_CLASS_NAME(Constants.URL_SELECTOR_STRATEGY_CLASS_NAME, ModelType.STRING, false),
    USE_JAVA_CONTEXT(Constants.USE_JAVA_CONTEXT, ModelType.BOOLEAN, false),
    ENABLED(Constants.ENABLED, ModelType.STRING, false),
    MAX_POOL_SIZE(Constants.MAX_POOL_SIZE, ModelType.INT, false),
    MIN_POOL_SIZE(Constants.MIN_POOL_SIZE, ModelType.INT, false),
    POOL_PREFILL(Constants.POOL_PREFILL, ModelType.BOOLEAN, false),
    POOL_USE_STRICT_MIN(Constants.POOL_USE_STRICT_MIN, ModelType.BOOLEAN, false),
    USERNAME(Constants.USERNAME, ModelType.STRING, false),
    PASSWORD(Constants.PASSWORD, ModelType.STRING, false),
    PREPAREDSTATEMENTSCACHESIZE(Constants.PREPAREDSTATEMENTSCACHESIZE, ModelType.LONG, false),
    SHAREPREPAREDSTATEMENTS(Constants.SHAREPREPAREDSTATEMENTS, ModelType.BOOLEAN, false),
    TRACKSTATEMENTS(Constants.TRACKSTATEMENTS, ModelType.STRING, false),
    ALLOCATION_RETRY(Constants.ALLOCATION_RETRY, ModelType.INT, false),
    ALLOCATION_RETRY_WAIT_MILLIS(Constants.ALLOCATION_RETRY_WAIT_MILLIS, ModelType.LONG, false),
    BLOCKING_TIMEOUT_WAIT_MILLIS(Constants.BLOCKING_TIMEOUT_WAIT_MILLIS, ModelType.LONG, false),
    IDLETIMEOUTMINUTES(Constants.IDLETIMEOUTMINUTES, ModelType.LONG, false),
    QUERYTIMEOUT(Constants.QUERYTIMEOUT, ModelType.LONG, false),
    USETRYLOCK(Constants.USETRYLOCK, ModelType.LONG, false),
    SETTXQUERYTIMEOUT(Constants.SETTXQUERYTIMEOUT, ModelType.BOOLEAN, false),
    TRANSACTION_ISOLOATION(Constants.TRANSACTION_ISOLOATION, ModelType.STRING, false),
    CHECKVALIDCONNECTIONSQL(Constants.CHECKVALIDCONNECTIONSQL, ModelType.STRING, false),
    EXCEPTIONSORTERCLASSNAME(Constants.EXCEPTIONSORTERCLASSNAME, ModelType.STRING, false),
    STALECONNECTIONCHECKERCLASSNAME(Constants.STALECONNECTIONCHECKERCLASSNAME, ModelType.STRING, false),
    VALIDCONNECTIONCHECKERCLASSNAME(Constants.VALIDCONNECTIONCHECKERCLASSNAME, ModelType.STRING, false),
    BACKGROUNDVALIDATIONMINUTES(Constants.BACKGROUNDVALIDATIONMINUTES, ModelType.LONG, false),
    BACKGROUNDVALIDATION(Constants.BACKGROUNDVALIDATION, ModelType.BOOLEAN, false),
    USE_FAST_FAIL(Constants.USE_FAST_FAIL, ModelType.BOOLEAN, false),
    VALIDATEONMATCH(Constants.VALIDATEONMATCH, ModelType.BOOLEAN, false),
    SPY(Constants.SPY, ModelType.BOOLEAN, false),
    XADATASOURCECLASS(Constants.SPY, ModelType.STRING, true),
    INTERLIVING(Constants.SPY, ModelType.BOOLEAN, false),
    NOTXSEPARATEPOOL(Constants.SPY, ModelType.BOOLEAN, false),
    PAD_XID(Constants.SPY, ModelType.BOOLEAN, false),
    SAME_RM_OVERRIDE(Constants.SPY, ModelType.BOOLEAN, false),
    WRAP_XA_DATASOURCE(Constants.SPY, ModelType.BOOLEAN, false),
    XA_RESOURCE_TIMEOUT(Constants.SPY, ModelType.INT, false);

    private final String propertyName;
    private final ModelType modelType;
    private final boolean required;

    private AttributeDefinition(String propertyName, ModelType modelType, boolean required) {
        this.propertyName = propertyName;
        this.modelType = modelType;
        this.required = required;
    }

    /**
     * Get the model attribute name.
     *
     * @return the name
     */
    public String getName() {
        return propertyName;
    }

    /**
     * Get the model type.
     *
     * @return the type
     */
    public ModelType getModelType() {
        return modelType;
    }

    /**
     * Is the attribute required.
     *
     * @return true if the attribute is required, false otherwise
     */
    public boolean isRequired() {
        return required;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10818.java