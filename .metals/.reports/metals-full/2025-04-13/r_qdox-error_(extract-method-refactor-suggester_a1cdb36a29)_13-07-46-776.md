error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3419.java
text:
```scala
public static final S@@impleAttributeDefinition POOL_FLUSH_STRATEGY = new SimpleAttributeDefinitionBuilder(FLUSH_STRATEGY_NAME, ModelType.STRING)

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.connector.subsystems.common.pool;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.jca.common.api.metadata.Defaults;
import org.jboss.jca.common.api.metadata.common.CommonPool;
import org.jboss.jca.common.api.metadata.common.FlushStrategy;
import org.jboss.jca.common.api.metadata.ds.TimeOut;
import org.jboss.jca.common.api.metadata.ds.Validation;

/**
 * @author @author <a href="mailto:stefano.maestri@redhat.com">Stefano
 *         Maestri</a>
 */
public class Constants {

    private static final String MIN_POOL_SIZE_NAME = "min-pool-size";

    private static final String MAX_POOL_SIZE_NAME = "max-pool-size";

    private static final String POOL_PREFILL_NAME = "pool-prefill";

    private static final String POOL_USE_STRICT_MIN_NAME = "pool-use-strict-min";

    private static final String BACKGROUNDVALIDATIONMILLIS_NAME = "background-validation-millis";

    private static final String BACKGROUNDVALIDATION_NAME = "background-validation";

    private static final String USE_FAST_FAIL_NAME = "use-fast-fail";

    private static final String BLOCKING_TIMEOUT_WAIT_MILLIS_NAME = "blocking-timeout-wait-millis";

    private static final String IDLETIMEOUTMINUTES_NAME = "idle-timeout-minutes";

    private static final String FLUSH_STRATEGY_NAME = "flush-strategy";


    public static final SimpleAttributeDefinition BLOCKING_TIMEOUT_WAIT_MILLIS = new SimpleAttributeDefinition(BLOCKING_TIMEOUT_WAIT_MILLIS_NAME, TimeOut.Tag.BLOCKING_TIMEOUT_MILLIS.getLocalName(),  new ModelNode(), ModelType.LONG, true , true, MeasurementUnit.MILLISECONDS);

    public static final SimpleAttributeDefinition IDLETIMEOUTMINUTES = new SimpleAttributeDefinition(IDLETIMEOUTMINUTES_NAME, TimeOut.Tag.IDLE_TIMEOUT_MINUTES.getLocalName(),  new ModelNode(), ModelType.LONG, true, true, MeasurementUnit.MINUTES);

    public static final SimpleAttributeDefinition BACKGROUNDVALIDATIONMILLIS = new SimpleAttributeDefinition(BACKGROUNDVALIDATIONMILLIS_NAME, Validation.Tag.BACKGROUND_VALIDATION_MILLIS.getLocalName(),  new ModelNode(), ModelType.LONG, true, true, MeasurementUnit.MILLISECONDS, new IntRangeValidator(1, true, true));

    public static final SimpleAttributeDefinition BACKGROUNDVALIDATION = new SimpleAttributeDefinition(BACKGROUNDVALIDATION_NAME, Validation.Tag.BACKGROUND_VALIDATION.getLocalName(), new ModelNode(Defaults.BACKGROUND_VALIDATION), ModelType.BOOLEAN, true, true, MeasurementUnit.NONE);

    public static final SimpleAttributeDefinition USE_FAST_FAIL = new SimpleAttributeDefinition(USE_FAST_FAIL_NAME, Validation.Tag.USE_FAST_FAIL.getLocalName(), new ModelNode(Defaults.USE_FAST_FAIL), ModelType.BOOLEAN, true, true, MeasurementUnit.NONE);

    public static final SimpleAttributeDefinition MAX_POOL_SIZE = new SimpleAttributeDefinition(MAX_POOL_SIZE_NAME, CommonPool.Tag.MAX_POOL_SIZE.getLocalName(), new ModelNode(Defaults.MAX_POOL_SIZE), ModelType.INT, true, true, MeasurementUnit.NONE);

    public static final SimpleAttributeDefinition MIN_POOL_SIZE = new SimpleAttributeDefinition(MIN_POOL_SIZE_NAME, CommonPool.Tag.MIN_POOL_SIZE.getLocalName(), new ModelNode(Defaults.MIN_POOL_SIZE), ModelType.INT, true, true, MeasurementUnit.NONE);

    public static final SimpleAttributeDefinition POOL_PREFILL = new SimpleAttributeDefinition(POOL_PREFILL_NAME, CommonPool.Tag.PREFILL.getLocalName(), new ModelNode(Defaults.PREFILL), ModelType.BOOLEAN, true, true, MeasurementUnit.NONE);

    public static final SimpleAttributeDefinition POOL_USE_STRICT_MIN = new SimpleAttributeDefinition(POOL_USE_STRICT_MIN_NAME, CommonPool.Tag.USE_STRICT_MIN.getLocalName(), new ModelNode(Defaults.USE_STRICT_MIN), ModelType.BOOLEAN, true, true, MeasurementUnit.NONE);

    public static SimpleAttributeDefinition POOL_FLUSH_STRATEGY = new SimpleAttributeDefinitionBuilder(FLUSH_STRATEGY_NAME, ModelType.STRING)
            .setDefaultValue(new ModelNode(Defaults.FLUSH_STRATEGY.getName()))
            .setXmlName(CommonPool.Tag.FLUSH_STRATEGY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .setValidator(new EnumValidator<FlushStrategy>(FlushStrategy.class, true, true))
            .build();

    public static final SimpleAttributeDefinition[] POOL_ATTRIBUTES = {BLOCKING_TIMEOUT_WAIT_MILLIS, IDLETIMEOUTMINUTES, BACKGROUNDVALIDATIONMILLIS,
            BACKGROUNDVALIDATION, USE_FAST_FAIL, MAX_POOL_SIZE, MIN_POOL_SIZE, POOL_PREFILL, POOL_USE_STRICT_MIN, POOL_FLUSH_STRATEGY};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3419.java