error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9965.java
text:
```scala
t@@estUpdateProperties(kernelServices, address, AbstractHandlerDefinition.FORMATTER, "[test] %d{HH:mm:ss,SSS} %-5p [%c] %s%E%n");

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

import static org.jboss.as.subsystem.test.SubsystemOperations.OperationBuilder;
import static org.junit.Assert.*;

import java.io.IOException;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.SubsystemOperations;
import org.jboss.dmr.ModelNode;
import org.junit.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class HandlerLegacyOperationsTestCase extends AbstractOperationsTestCase {

    @Override
    protected void standardSubsystemTest(final String configId) throws Exception {
        // do nothing as this is not a subsystem parsing test
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("/empty-subsystem.xml");
    }

    @Test
    public void testOperations() throws Exception {
        final KernelServices kernelServices = boot();

        testAsyncHandler(kernelServices, null);
        testAsyncHandler(kernelServices, PROFILE);

        testConsoleHandler(kernelServices, null);
        testConsoleHandler(kernelServices, PROFILE);

        testFileHandler(kernelServices, null);
        testFileHandler(kernelServices, PROFILE);

        testPeriodicRotatingFileHandler(kernelServices, null);
        testPeriodicRotatingFileHandler(kernelServices, PROFILE);

        testSizeRotatingFileHandler(kernelServices, null);
        testPeriodicRotatingFileHandler(kernelServices, PROFILE);
    }

    private void testAsyncHandler(final KernelServices kernelServices, final String profileName) throws Exception {
        final ModelNode address = createAsyncHandlerAddress(profileName, "async").toModelNode();

        // Add a console handler for subhandler tests
        final ModelNode consoleAddress = createConsoleHandlerAddress(profileName, "CONSOLE").toModelNode();
        executeOperation(kernelServices, SubsystemOperations.createAddOperation(consoleAddress));
        final ModelNode subhandlers = new ModelNode().setEmptyList().add("CONSOLE");

        // Add the handler
        final ModelNode addOp = OperationBuilder.createAddOperation(address)
                .addAttribute(AsyncHandlerResourceDefinition.QUEUE_LENGTH, 10)
                .addAttribute(AsyncHandlerResourceDefinition.OVERFLOW_ACTION, "DISCARD")
                .build();
        executeOperation(kernelServices, addOp);

        // Test common legacy attributes
        testWriteCommonAttributes(kernelServices, address);
        testUndefineCommonAttributes(kernelServices, address);

        // Test common operations
        testCommonOperations(kernelServices, address);

        // Assign the subhandler
        ModelNode op = OperationBuilder.create(AsyncHandlerResourceDefinition.LEGACY_ADD_HANDLER, address)
                .addAttribute(CommonAttributes.HANDLER_NAME, "CONSOLE")
                .build();
        executeOperation(kernelServices, op);
        // The console handler should be present
        ModelNode result = executeOperation(kernelServices, SubsystemOperations.createReadAttributeOperation(address,
                AsyncHandlerResourceDefinition.SUBHANDLERS));
        assertTrue("Subhandlers should be empty: " + result, SubsystemOperations.readResultAsList(result)
                .contains("CONSOLE"));

        // Unassign the subhandler
        op = OperationBuilder.create(AsyncHandlerResourceDefinition.LEGACY_REMOVE_HANDLER, address)
                .addAttribute(CommonAttributes.HANDLER_NAME, "CONSOLE")
                .build();
        executeOperation(kernelServices, op);
        // There should be no subhandlers
        result = executeOperation(kernelServices, SubsystemOperations.createReadAttributeOperation(address,
                AsyncHandlerResourceDefinition.SUBHANDLERS));
        assertTrue("Subhandlers should be empty: " + result, SubsystemOperations.readResultAsList(result).isEmpty());

        // Write each attribute and check the value
        testUpdateCommonHandlerAttributes(kernelServices, address);
        testUpdateProperties(kernelServices, address, AsyncHandlerResourceDefinition.OVERFLOW_ACTION, "BLOCK");
        testUpdateProperties(kernelServices, address, AsyncHandlerResourceDefinition.SUBHANDLERS, subhandlers);
        testUpdateProperties(kernelServices, address, AsyncHandlerResourceDefinition.QUEUE_LENGTH, 20);

        // Clean-up
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(consoleAddress));
        verifyRemoved(kernelServices, consoleAddress);
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(address));
        verifyRemoved(kernelServices, address);
    }

    private void testConsoleHandler(final KernelServices kernelServices, final String profileName) throws Exception {
        final ModelNode address = createConsoleHandlerAddress(profileName, "CONSOLE").toModelNode();

        // Add the handler
        final ModelNode addOp = SubsystemOperations.createAddOperation(address);
        executeOperation(kernelServices, addOp);

        // Test common legacy attributes
        testWriteCommonAttributes(kernelServices, address);
        testUndefineCommonAttributes(kernelServices, address);

        // Test the common operations
        testCommonOperations(kernelServices, address);

        // Write each attribute and check the value
        testUpdateCommonHandlerAttributes(kernelServices, address);
        testUpdateProperties(kernelServices, address, CommonAttributes.AUTOFLUSH, false);
        testUpdateProperties(kernelServices, address, ConsoleHandlerResourceDefinition.TARGET, "System.err");

        // Clean-up
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(address));
        verifyRemoved(kernelServices, address);
    }


    private void testFileHandler(final KernelServices kernelServices, final String profileName) throws Exception {
        final ModelNode address = createFileHandlerAddress(profileName, "FILE").toModelNode();
        final String filename = "test-file.log";
        final String newFilename = "new-test-file.log";

        final ModelNode defaultFile = createFileValue("jboss.server.log.dir", filename);

        // Add the handler
        final ModelNode addOp = OperationBuilder.createAddOperation(address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .build();
        executeOperation(kernelServices, addOp);
        verifyFile(filename);

        // Test common legacy attributes
        testWriteCommonAttributes(kernelServices, address);
        testUndefineCommonAttributes(kernelServices, address);

        // Test the common operations
        testCommonOperations(kernelServices, address);

        // Write each attribute and check the value
        testUpdateCommonHandlerAttributes(kernelServices, address);
        testUpdateProperties(kernelServices, address, CommonAttributes.APPEND, false);
        testUpdateProperties(kernelServices, address, CommonAttributes.AUTOFLUSH, false);

        final ModelNode newFile = createFileValue("jboss.server.log.dir", newFilename);
        testUpdateProperties(kernelServices, address, CommonAttributes.FILE, newFile);
        verifyFile(newFilename);

        // Test the change-file operation
        removeFile(filename);
        ModelNode op = OperationBuilder.create(AbstractFileHandlerDefinition.CHANGE_FILE, address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .build();
        executeOperation(kernelServices, op);
        op = SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.FILE);
        ModelNode result = executeOperation(kernelServices, op);
        assertEquals(defaultFile, SubsystemOperations.readResult(result));
        verifyFile(filename);

        // Clean-up
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(address));
        verifyRemoved(kernelServices, address);
        removeFile(filename);
        removeFile(newFilename);
    }

    private void testPeriodicRotatingFileHandler(final KernelServices kernelServices, final String profileName) throws Exception {
        final ModelNode address = createPeriodicRotatingFileHandlerAddress(profileName, "FILE").toModelNode();
        final String filename = "test-file.log";
        final String newFilename = "new-test-file.log";

        final ModelNode defaultFile = createFileValue("jboss.server.log.dir", filename);

        // Add the handler
        final ModelNode addOp = OperationBuilder.createAddOperation(address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .addAttribute(PeriodicHandlerResourceDefinition.SUFFIX, ".yyyy-MM-dd")
                .build();
        executeOperation(kernelServices, addOp);
        verifyFile(filename);

        // Test common legacy attributes
        testWriteCommonAttributes(kernelServices, address);
        testUndefineCommonAttributes(kernelServices, address);

        // Test the common operations
        testCommonOperations(kernelServices, address);

        // Write each attribute and check the value
        testUpdateCommonHandlerAttributes(kernelServices, address);
        testUpdateProperties(kernelServices, address, CommonAttributes.APPEND, false);
        testUpdateProperties(kernelServices, address, CommonAttributes.AUTOFLUSH, false);

        final ModelNode newFile = createFileValue("jboss.server.log.dir", newFilename);
        testUpdateProperties(kernelServices, address, CommonAttributes.FILE, newFile);
        verifyFile(newFilename);

        testUpdateProperties(kernelServices, address, PeriodicHandlerResourceDefinition.SUFFIX, ".yyyy-MM-dd-HH");

        // Test the change-file operation
        removeFile(filename);
        ModelNode op = OperationBuilder.create(AbstractFileHandlerDefinition.CHANGE_FILE, address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .build();
        executeOperation(kernelServices, op);
        op = SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.FILE);
        ModelNode result = executeOperation(kernelServices, op);
        assertEquals(defaultFile, SubsystemOperations.readResult(result));
        verifyFile(filename);

        // Clean-up
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(address));
        verifyRemoved(kernelServices, address);
        removeFile(filename);
        removeFile(newFilename);
    }

    private void testSizeRotatingFileHandler(final KernelServices kernelServices, final String profileName) throws Exception {
        final ModelNode address = createSizeRotatingFileHandlerAddress(profileName, "FILE").toModelNode();
        final String filename = "test-file.log";
        final String newFilename = "new-test-file.log";

        final ModelNode defaultFile = createFileValue("jboss.server.log.dir", filename);

        // Add the handler
        final ModelNode addOp = OperationBuilder.createAddOperation(address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .build();
        executeOperation(kernelServices, addOp);
        verifyFile(filename);

        // Test common legacy attributes
        testWriteCommonAttributes(kernelServices, address);
        testUndefineCommonAttributes(kernelServices, address);

        // Test the common operations
        testCommonOperations(kernelServices, address);

        // Write each attribute and check the value
        testUpdateCommonHandlerAttributes(kernelServices, address);
        testUpdateProperties(kernelServices, address, CommonAttributes.APPEND, false);
        testUpdateProperties(kernelServices, address, CommonAttributes.AUTOFLUSH, false);

        final ModelNode newFile = createFileValue("jboss.server.log.dir", newFilename);
        testUpdateProperties(kernelServices, address, CommonAttributes.FILE, newFile);
        verifyFile(newFilename);

        testUpdateProperties(kernelServices, address, SizeRotatingHandlerResourceDefinition.MAX_BACKUP_INDEX, 20);
        testUpdateProperties(kernelServices, address, SizeRotatingHandlerResourceDefinition.ROTATE_SIZE, "50m");

        // Test the change-file operation
        removeFile(filename);
        ModelNode op = OperationBuilder.create(AbstractFileHandlerDefinition.CHANGE_FILE, address)
                .addAttribute(CommonAttributes.FILE, defaultFile)
                .build();
        executeOperation(kernelServices, op);
        op = SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.FILE);
        ModelNode result = executeOperation(kernelServices, op);
        assertEquals(defaultFile, SubsystemOperations.readResult(result));
        verifyFile(filename);

        // Clean-up
        executeOperation(kernelServices, SubsystemOperations.createRemoveOperation(address));
        verifyRemoved(kernelServices, address);
        removeFile(filename);
        removeFile(newFilename);
    }

    protected void testUpdateCommonHandlerAttributes(final KernelServices kernelServices, final ModelNode address) throws Exception {
        testUpdateProperties(kernelServices, address, CommonAttributes.LEVEL, "TRACE");
        testUpdateProperties(kernelServices, address, CommonAttributes.ENABLED, false);
        testUpdateProperties(kernelServices, address, CommonAttributes.ENCODING, "utf-8");
        testUpdateProperties(kernelServices, address, CommonAttributes.FORMATTER, "[test] %d{HH:mm:ss,SSS} %-5p [%c] %s%E%n");
        testUpdateProperties(kernelServices, address, CommonAttributes.FILTER_SPEC, "deny");
    }

    private void testUpdateProperties(final KernelServices kernelServices, final ModelNode address, final AttributeDefinition attribute, final boolean value) throws Exception {
        final ModelNode original = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        ModelNode op = OperationBuilder.create(AbstractHandlerDefinition.UPDATE_OPERATION_NAME, address)
                .addAttribute(attribute, value)
                .build();
        executeOperation(kernelServices, op);
        // Value should be changed
        op = SubsystemOperations.createReadAttributeOperation(address, attribute);
        final ModelNode result = executeOperation(kernelServices, op);
        assertEquals(value, SubsystemOperations.readResult(result).asBoolean());

        // Compare the updated model with the original model, slightly modified
        final ModelNode newModel = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        // Replace the value in the original model, all other attributes should match
        original.get(attribute.getName()).set(value);
        compare(original, newModel);
    }

    private void testUpdateProperties(final KernelServices kernelServices, final ModelNode address, final AttributeDefinition attribute, final int value) throws Exception {
        final ModelNode original = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        ModelNode op = OperationBuilder.create(AbstractHandlerDefinition.UPDATE_OPERATION_NAME, address)
                .addAttribute(attribute, value)
                .build();
        executeOperation(kernelServices, op);
        // Value should be changed
        op = SubsystemOperations.createReadAttributeOperation(address, attribute);
        final ModelNode result = executeOperation(kernelServices, op);
        assertEquals(value, SubsystemOperations.readResult(result).asInt());

        // Compare the updated model with the original model, slightly modified
        final ModelNode newModel = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        // Replace the value in the original model, all other attributes should match
        original.get(attribute.getName()).set(value);
        compare(original, newModel);
    }

    private void testUpdateProperties(final KernelServices kernelServices, final ModelNode address, final AttributeDefinition attribute, final String value) throws Exception {
        final ModelNode original = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        ModelNode op = OperationBuilder.create(AbstractHandlerDefinition.UPDATE_OPERATION_NAME, address)
                .addAttribute(attribute, value)
                .build();
        executeOperation(kernelServices, op);
        // Value should be changed
        op = SubsystemOperations.createReadAttributeOperation(address, attribute);
        final ModelNode result = executeOperation(kernelServices, op);
        assertEquals(value, SubsystemOperations.readResultAsString(result));

        // Compare the updated model with the original model, slightly modified
        final ModelNode newModel = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        // Replace the value in the original model, all other attributes should match
        original.get(attribute.getName()).set(value);
        // filter requires special handling
        if (attribute.getName().equals(CommonAttributes.FILTER_SPEC.getName())) {
            original.get(CommonAttributes.FILTER.getName()).set(newModel.get(CommonAttributes.FILTER.getName()));
        }
        compare(original, newModel);
    }

    private void testUpdateProperties(final KernelServices kernelServices, final ModelNode address, final AttributeDefinition attribute, final ModelNode value) throws Exception {
        final ModelNode original = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        ModelNode op = OperationBuilder.create(AbstractHandlerDefinition.UPDATE_OPERATION_NAME, address)
                .addAttribute(attribute, value)
                .build();
        executeOperation(kernelServices, op);
        // Value should be changed
        op = SubsystemOperations.createReadAttributeOperation(address, attribute);
        final ModelNode result = executeOperation(kernelServices, op);
        assertEquals(value, SubsystemOperations.readResult(result));

        // Compare the updated model with the original model, slightly modified
        final ModelNode newModel = SubsystemOperations.readResult(executeOperation(kernelServices, SubsystemOperations.createReadResourceOperation(address, true)));
        // Replace the value in the original model, all other attributes should match
        original.get(attribute.getName()).set(value);
        compare(original, newModel);
    }

    private void testCommonOperations(final KernelServices kernelServices, final ModelNode address) throws Exception {
        ModelNode op = OperationBuilder.create(AbstractHandlerDefinition.CHANGE_LEVEL, address)
                .addAttribute(CommonAttributes.LEVEL, "DEBUG")
                .build();
        executeOperation(kernelServices, op);
        // Read the level
        ModelNode result = executeOperation(kernelServices, SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.LEVEL));
        assertEquals("DEBUG", SubsystemOperations.readResultAsString(result));

        // Disable the handler
        op = SubsystemOperations.createOperation(AbstractHandlerDefinition.DISABLE_HANDLER.getName(), address);
        executeOperation(kernelServices, op);
        // Read the enabled attribute
        result = executeOperation(kernelServices, SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.ENABLED));
        assertFalse("Handler should be disabled", SubsystemOperations.readResult(result).asBoolean());

        // Enable the handler
        op = SubsystemOperations.createOperation(AbstractHandlerDefinition.ENABLE_HANDLER.getName(), address);
        executeOperation(kernelServices, op);
        // Read the enabled attribute
        result = executeOperation(kernelServices, SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.ENABLED));
        assertTrue("Handler should be enabled", SubsystemOperations.readResult(result).asBoolean());
    }

    protected void testWriteCommonAttributes(final KernelServices kernelServices, final ModelNode address) throws Exception {
        // filter attribute not on logging profiles
        if (!LoggingProfileOperations.isLoggingProfileAddress(PathAddress.pathAddress(address))) {
            final ModelNode filter = new ModelNode().setEmptyObject();
            filter.get(CommonAttributes.ACCEPT.getName()).set(true);
            testWrite(kernelServices, address, CommonAttributes.FILTER, filter);
            // filter-spec should be "accept"
            final ModelNode op = SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.FILTER_SPEC);
            final ModelNode result = executeOperation(kernelServices, op);
            assertEquals("accept", SubsystemOperations.readResultAsString(result));
        }
    }

    protected void testUndefineCommonAttributes(final KernelServices kernelServices, final ModelNode address) throws Exception {
        if (!LoggingProfileOperations.isLoggingProfileAddress(PathAddress.pathAddress(address))) {
            testUndefine(kernelServices, address, CommonAttributes.FILTER);
            // filter-spec should be undefined
            final ModelNode op = SubsystemOperations.createReadAttributeOperation(address, CommonAttributes.FILTER_SPEC);
            final ModelNode result = executeOperation(kernelServices, op);
            assertFalse(SubsystemOperations.readResult(result).isDefined());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9965.java