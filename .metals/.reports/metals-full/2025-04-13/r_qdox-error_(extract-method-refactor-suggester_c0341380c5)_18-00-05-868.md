error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6999.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6999.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6999.java
text:
```scala
r@@eturn LoggingTestEnvironment.get();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.logging;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.SubsystemOperations;
import org.jboss.dmr.ModelNode;
import org.jboss.logmanager.LogContext;
import org.jboss.logmanager.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class RootSubsystemOperationsTestCase extends AbstractOperationsTestCase {
    private final String msg = "Test message ";

    @Before
    public void clearLogDir() {
        final File dir = LoggingTestEnvironment.get().getLogDir();
        deleteRecursively(dir);
    }

    @Override
    protected void standardSubsystemTest(final String configId) throws Exception {
        // do nothing as this is not a subsystem parsing test
    }

    @Override
    protected AdditionalInitialization createAdditionalInitialization() {
        return LoggingTestEnvironment.getManagementInstance();
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("/simple-subsystem.xml");
    }

    @Test
    public void testListLogFiles() throws Exception {
        final KernelServices kernelServices = boot();

        // Subsystem address
        final ModelNode address = SUBSYSTEM_ADDRESS.toModelNode();
        final ModelNode op = SubsystemOperations.createOperation("list-log-files", address);
        ModelNode result = executeOperation(kernelServices, op);
        List<ModelNode> logFiles = SubsystemOperations.readResult(result).asList();

        // Should only be one file
        // TODO (jrp) can be tested when LOGMGR-83 is committed and the logmanager is updated
        // assertEquals("Found: " + logFiles, 1, logFiles.size());

        // Should only be a simple.log file
        boolean found = false;
        for (ModelNode fileInfo : logFiles) {
            if ("simple.log".equals(fileInfo.get("file-name").asString())) {
                found = true;
                break;
            }
        }
        assertTrue("simple.log file was not found", found);

        // Change the permissions on the file so read is not allowed
        final File file = new File(LoggingTestEnvironment.get().getLogDir(), "simple.log");
        // The file should exist
        assertTrue("File does not exist", file.exists());

        // Only test if successfully set
        if (file.setReadable(false)) {
            result = executeOperation(kernelServices, op);
            logFiles = SubsystemOperations.readResult(result).asList();
            assertTrue("Read permission was found to be true on the file.", logFiles.isEmpty());
            // Reset the file permissions
            assertTrue("Could not reset file permissions", file.setReadable(true));
        }
    }

    @Test
    public void testReadLogFile() throws Exception {
        final KernelServices kernelServices = boot();

        // Log 50 records
        final Logger logger = getLogger();
        for (int i = 0; i < 50; i++) {
            logger.info(msg + i);
        }

        // Subsystem address
        final ModelNode address = SUBSYSTEM_ADDRESS.toModelNode();
        final ModelNode op = SubsystemOperations.createOperation("read-log-file", address);
        op.get("name").set("simple.log");
        ModelNode result = executeOperation(kernelServices, op);
        List<String> logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(10, logLines.size());
        checkLogLines(logLines, 40);

        // Read from top
        op.get("tail").set(false);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(10, logLines.size());
        checkLogLines(logLines, 0);

        // Read more lines from top
        op.get("lines").set(20);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(20, logLines.size());
        checkLogLines(logLines, 0);

        // Read from bottom
        op.get("tail").set(true);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(20, logLines.size());
        checkLogLines(logLines, 30);

        // Skip lines from bottom
        op.get("tail").set(true);
        op.get("skip").set(5);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(20, logLines.size());
        checkLogLines(logLines, 25);

        // Skip lines from top
        op.get("tail").set(false);
        op.get("skip").set(5);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(20, logLines.size());
        checkLogLines(logLines, 5);

        // Read all lines
        op.get("tail").set(false);
        op.get("lines").set(-1);
        op.remove("skip");
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(50, logLines.size());
        checkLogLines(logLines, 0);

        // Read all lines, but 5 lines
        op.get("tail").set(false);
        op.get("lines").set(-1);
        op.get("skip").set(5);
        result = executeOperation(kernelServices, op);
        logLines = SubsystemOperations.readResultAsList(result);
        assertEquals(45, logLines.size());
        checkLogLines(logLines, 5);

        // Change the permissions on the file so read is not allowed
        final File file = new File(LoggingTestEnvironment.get().getLogDir(), op.get("name").asString());
        // The file should exist
        assertTrue("File does not exist", file.exists());

        // Only test if successfully set
        if (file.setReadable(false)) {
            result = kernelServices.executeOperation(op);
            assertFalse("Should have failed due to denial of read permissions on the file.", SubsystemOperations.isSuccessfulOutcome(result));
            // Reset the file permissions
            assertTrue("Could not reset file permissions", file.setReadable(true));
        }

        // Test an invalid file
        op.get("name").set("invalid");
        result = kernelServices.executeOperation(op);
        assertFalse("Should have failed due to invalid file.", SubsystemOperations.isSuccessfulOutcome(result));
    }

    private void checkLogLines(final List<String> logLines, final int start) {
        int index = start;
        for (String line : logLines) {
            final String lineMsg = msg + index;
            assertTrue(String.format("Expected line containing '%s', found '%s", lineMsg, line), line.contains(msg + index));
            index++;
        }
    }

    private Logger getLogger() {
        return LogContext.getSystemLogContext().getLogger("org.jboss.as.logging.test");
    }

    static void deleteRecursively(final File dir) {
        if (dir.isDirectory()) {
            final File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteRecursively(file);
                }
                file.delete();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6999.java