error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1102
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5981.java
text:
```scala
public class DataSourceCfgMetricUnitTestCase  extends JCAMetrictsTestBase {

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

p@@ackage org.jboss.as.test.integration.jca.metrics;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.integration.management.jca.DsMgmtTestBase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Datasource configuration and metrics unit test.
 *
 * @author <a href="mailto:vrastsel@redhat.com">Vladimir Rastseluev</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class DataSourceCfgMetricUnitTestCase  extends DsMgmtTestBase{

	@BeforeClass
    public static void before() {
    	setBaseAddress("data-source", "DS");
    }

    @Test
    public void testDefaultDsAttributes()throws Exception {
    	setModel("basic-attributes.xml");
        assertTrue(readAttribute(baseAddress,"use-ccm").asBoolean());
        assertTrue(readAttribute(baseAddress,"jta").asBoolean());
        assertTrue(readAttribute(baseAddress,"use-java-context").asBoolean());
        assertFalse(readAttribute(baseAddress,"spy").asBoolean());
        removeDs();
    }

    @Test(expected=Exception.class)
    public void testDsWithNoDriver()throws Exception {
    	setBadModel("wrong-no-driver.xml");
    }

    @Test(expected=Exception.class)
    public void testDsWithWrongTransactionIsolationType()throws Exception {
    	setBadModel("wrong-transaction-isolation-type.xml");
    }

    @Test
    public void testValidationDefaultProperties()throws Exception {
    	setModel("validation-properties.xml");
        assertFalse(readAttribute(baseAddress,"use-fast-fail").asBoolean());
        removeDs();
    }

    @Test(expected=Exception.class)
    public void testWrongValidationProperties()throws Exception {
    	setBadModel("wrong-validation-properties.xml");
    }

    @Test
    public void testTimeoutDefaultProperties()throws Exception {
    	setModel("timeout-properties.xml");
        assertFalse(readAttribute(baseAddress,"set-tx-query-timeout").asBoolean());
        removeDs();
    }

    @Test(expected=Exception.class)
    public void testWrongBlckgTimeoutProperty()throws Exception {
    	setBadModel("wrong-blckg-timeout-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongIdleMinsProperty()throws Exception {
    	setBadModel("wrong-idle-mins-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongUseTryLockProperty()throws Exception {
    	setBadModel("wrong-use-try-lock-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongAllocRetryProperty()throws Exception {
    	setBadModel("wrong-alloc-retry-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongAllocRetryWaitProperty()throws Exception {
    	setBadModel("wrong-alloc-retry-wait-property.xml");
    }

    @Test
    public void testStatementDefaultProperties()throws Exception {
    	setModel("statement-properties.xml");
        assertEquals("NOWARN",readAttribute(baseAddress,"track-statements").asString());
        assertFalse(readAttribute(baseAddress,"share-prepared-statements").asBoolean());
        removeDs();
    }

    @Test(expected=Exception.class)
    public void testWrongTrckStmtProperty()throws Exception {
    	setBadModel("wrong-trck-stmt-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongStmtCacheSizeProperty()throws Exception {
    	setBadModel("wrong-stmt-cache-size-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongFlushStrategyProperty()throws Exception {
    	setBadModel("wrong-flush-strategy-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongMinPoolSizeProperty()throws Exception {
    	setBadModel("wrong-min-pool-size-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongMaxPoolSizeProperty()throws Exception {
    	setBadModel("wrong-max-pool-size-property.xml");
    }

    @Test(expected=Exception.class)
    public void testWrongMaxLessMinPoolSizeProperty()throws Exception {
    	setBadModel("wrong-max-less-min-pool-size-property.xml");
    }

    @Test
    public void testStatistics() throws Exception {
        super.testStatistics("basic-attributes.xml");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5981.java