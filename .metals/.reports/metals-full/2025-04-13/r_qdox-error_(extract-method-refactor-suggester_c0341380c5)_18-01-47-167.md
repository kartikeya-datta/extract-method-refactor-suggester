error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1017.java
text:
```scala
private static final S@@tring BM_SCRIPT_PATH = "participant_completion_coordinator_rules.btm";

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.as.test.xts.simple.wsba.participantcompletion.client;

import javax.inject.Inject;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.as.test.xts.simple.BMScript;
import org.jboss.as.test.xts.simple.wsba.participantcompletion.jaxws.SetServiceBA;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WSBAParticipantCompletionTestCase {
    private static final Logger log = Logger.getLogger(WSBAParticipantCompletionTestCase.class);

    public static final String DEPLOYMENT_NAME = "wsba-participant-completion";
    private static final String BM_SCRIPT_PATH = "../test-classes/participant_completion_coordinator_rules.btm";

    @Inject
    @ClientStub
    public SetServiceBA client;

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, DEPLOYMENT_NAME + ".war")
                .addPackages(false, "org.jboss.as.test.xts.simple.wsba")
                .addPackages(true, "org.jboss.as.test.xts.simple.wsba.participantcompletion")
                .addClass(TestSuiteEnvironment.class)
                .addAsResource("context-handlers.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsManifestResource(new StringAsset("Dependencies: org.jboss.xts,org.jboss.jts\n"),"MANIFEST.MF");
    }

    @BeforeClass()
    public static void submitBytemanScript() throws Exception {
        BMScript.submit(BM_SCRIPT_PATH);
    }

    @AfterClass()
    public static void removeBytemanScript() {
        BMScript.remove(BM_SCRIPT_PATH);
    }

    @Before
    public void clientReset() {
        client.clear();
    }

    /**
     * Test the simple scenario where an item is added to the set within a Business Activity which is closed successfully.
     *
     * @throws Exception if something goes wrong.
     */
    @Test
    public void testSuccess() throws Exception {

        log.info("[CLIENT] Creating a new Business Activity");
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value = "1";

            log.info("[CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA wil be included in this activity)");
            uba.begin();

            log.info("[CLIENT] invoking addValueToSet(1) on WS");
            client.addValueToSet(value);

            log.info("[CLIENT] Closing Business Activity (This will cause the BA to complete successfully)");
            uba.close();

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value));
        } finally {
            cancelIfActive(uba);
        }
    }

    /**
     * Tests the scenario where an item is added to the set with in a business activity that is later cancelled. The test checks
     * that the item is in the set after invoking addValueToSet on the Web service. After cancelling the Business Activity, the
     * work should be compensated and thus the item should no longer be in the set.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testCancel() throws Exception {
        
        log.info("[CLIENT] Creating a new Business Activity");
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value = "1";

            log.info("[CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA will be included in this activity)");
            uba.begin();

            log.info("[CLIENT] invoking addValueToSet(1) on WS");
            client.addValueToSet(value);

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value));

            log.info("[CLIENT] Cancelling Business Activity (This will cause the work to be compensated)");
            uba.cancel();

            Assert.assertTrue("Expected value to not be in the set, but it was", !client.isInSet(value));

        } finally {
            cancelIfActive(uba);
        }
    }

    /**
     * Utility method for cancelling a Business Activity if it is currently active.
     *
     * @param uba The User Business Activity to cancel.
     */
    private void cancelIfActive(UserBusinessActivity uba) {
        try {
            uba.cancel();
        } catch (Throwable th2) {
            // do nothing, already closed
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1017.java