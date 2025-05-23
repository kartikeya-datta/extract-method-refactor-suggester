error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14240.java
text:
```scala
A@@ssert.assertTrue("result exists", response.hasDefined(RESULT));

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

package org.jboss.as.test.manualmode.logging;

import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.as.test.integration.management.util.MgmtOperationException;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

/**
 * @author <a href="mailto:pkremens@redhat.com">Petr Kremensky</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SizeAppenderRestartTestCase {
    private static Logger log = Logger.getLogger(SizeAppenderRestartTestCase.class);
    public static final String CONTAINER = "default-jbossas";
    public static final String DEPLOYMENT = "logging-deployment";
    private static final String FILE_NAME = "sizeAppenderRestartTestCase.log";
    private static final String SIZE_HANDLER_NAME = "sizeAppenderRestartTestCase";
    private static final ModelNode SIZE_HANDLER_ADDRESS = new ModelNode();
    private static final ModelNode ROOT_LOGGER_ADDRESS = new ModelNode();
    private static File logFile;
    @ArquillianResource
    private ContainerController container;
    @ArquillianResource
    private Deployer deployer;
    private final ModelControllerClient client = TestSuiteEnvironment.getModelControllerClient();
    private final ManagementClient managementClient = new ManagementClient(client, TestSuiteEnvironment.getServerAddress(), TestSuiteEnvironment.getServerPort(), "http-remoting");


    @Deployment(name = DEPLOYMENT, managed = false, testable = false)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, DEPLOYMENT + ".war");
        archive.addClasses(LoggingServlet.class);
        return archive;
    }

    @Test
    @InSequence(-1)
    public void startContainer() throws Exception {
        SIZE_HANDLER_ADDRESS.add(SUBSYSTEM, "logging")
                .add("size-rotating-file-handler", SIZE_HANDLER_NAME);
        ROOT_LOGGER_ADDRESS.add(SUBSYSTEM, "logging")
                .add("root-logger", "ROOT");

        // Start the server
        container.start(CONTAINER);
        Assert.assertTrue("Container is not started", managementClient.isServerInRunningState());
        // Deploy the servlet
        deployer.deploy(DEPLOYMENT);

        logFile = getAbsoluteLogFilePath(client);
        clearLogs(logFile);

        // Create the size-rotating handler
        ModelNode op = Operations.createAddOperation(SIZE_HANDLER_ADDRESS);
        ModelNode file = new ModelNode();
        file.get("path").set(logFile.getAbsolutePath());
        op.get(FILE).set(file);
        validateResponse(op);

        // Add the handler to the root-logger
        op = Operations.createOperation("add-handler", ROOT_LOGGER_ADDRESS);
        op.get(NAME).set(SIZE_HANDLER_NAME);
        validateResponse(op);
    }

    @Test
    @InSequence(1)
    public void stopContainer() throws Exception {
        // Remove the servlet
        deployer.undeploy(DEPLOYMENT);

        // Remove the handler from the root-logger
        ModelNode op = Operations.createOperation("remove-handler", ROOT_LOGGER_ADDRESS);
        op.get(NAME).set(SIZE_HANDLER_NAME);
        validateResponse(op);

        // Remove the size-rotating handler
        op = Operations.createRemoveOperation(SIZE_HANDLER_ADDRESS);
        validateResponse(op);

        // Remove log files
        clearLogs(logFile);

        // Stop the container
        container.stop(CONTAINER);
        Assert.assertFalse("Container is not stopped", managementClient.isServerInRunningState());

        safeClose(client);
        safeClose(managementClient);
    }

    /*
     * append = true:    restart -> logs are appended to same log file, unless it reach rotate-size
     */
    @Test
    public void appendTrueTest(@ArquillianResource URL url) throws Exception {
        long fileSize = appendTest("true", url);
        log.info("original file size  = " + fileSize);
        log.info("new file size       = " + logFile.length());
        Assert.assertTrue("Size of log file should be bigger after reload", fileSize < logFile.length());
    }

    /*
     * append = false:   restart -> original log file is rewritten
     */
    @Test
    public void appendFalseTest(@ArquillianResource URL url) throws Exception {
        long fileSize = appendTest("false", url);
        log.info("original file size  = " + fileSize);
        log.info("new file size       = " + logFile.length());
        Assert.assertTrue("Size of log file should be smaller after reload", fileSize > logFile.length());
    }

    private long appendTest(String append, URL url) throws Exception {
        final String message = "SizeAppenderRestartTestCase - This is my dummy message which is gonna fill my log file";
        long fileSize = 0L;
        // set append attribute & reload server
        ModelNode op = Operations.createWriteAttributeOperation(SIZE_HANDLER_ADDRESS, "append", append);
        validateResponse(op);
        clearLogs(logFile);
        restartServer();

        // make some (more than server start) logs, remember the size of log file, reload server, check new size of file
        op = Operations.createReadResourceOperation(SIZE_HANDLER_ADDRESS);
        validateResponse(op);
        for (int i = 0; i < 100; i++) {
            makeLog(message, url);
        }
        checkLogs(message, true);
        fileSize = logFile.length();
        restartServer();

        // logFile.getParentFile().listFiles().length creates array with length of 3
        int count = 0;
        for (File f : logFile.getParentFile().listFiles()) {
            if (f.getName().contains(logFile.getName())) {
                count++;
            }
        }
        Assert.assertEquals("There should be only one log file", 1, count);
        return fileSize;
    }

    /*
     * TODO https://issues.jboss.org/browse/PRODMGT-290
     * append = true: restart -> logs are appended to same log file, unless it reach rotate-size
     * <rotate-on-restart> = true:   restart -> log file is rotated, logs are written to new file
     * <rotate-on-restart> = false:  restart -> same as before
     * TODO How about combinations: append=true + rotate-on-restart=true etc.???
     */
    @Test
    @Ignore
    public void rotateFileOnRestartTest(@ArquillianResource URL url) throws Exception {
        // set append=false, rotate-on-restart=true & reload server
        // name of attribute has yet to be resolved
        String ROTATE_ON_RESTART = "";
        final String oldMessage = "SizeAppenderRestartTestCase - This is old message";
        final String newMessage = "SizeAppenderRestartTestCase - This is new message";
        long fileSize = 0L;
        ModelNode op = Operations.createWriteAttributeOperation(SIZE_HANDLER_ADDRESS, "append", false);
        validateResponse(op);
        op = Operations.createWriteAttributeOperation(SIZE_HANDLER_ADDRESS, ROTATE_ON_RESTART, true);
        validateResponse(op);
        clearLogs(logFile);
        restartServer();

        // make some logs, remember file size, restart
        for (int i = 0; i < 100; i++) {
            makeLog(oldMessage, url);
        }
        checkLogs(oldMessage, true);
        fileSize = logFile.length();
        restartServer();

        // make log to new rotated log file
        makeLog(newMessage, url);
        checkLogs(newMessage, true);

        // verify that log file was rotated and size of original file equals to size of rotated file
        int count = 0;
        for (File file : logFile.getParentFile().listFiles()) {
            if (file.getName().contains(logFile.getName())) {
                count++;
                if (file.getName().equals(logFile.getName() + ".1")) {
                    Assert.assertEquals("Size of file after rotation should not differ", fileSize, file.length());
                    checkLogs(oldMessage, true, file);
                }
            }
        }
        Assert.assertEquals("There should be two log files", 2, count);
    }

    private void restartServer() {
        Assert.assertTrue("Container is not runnig", managementClient.isServerInRunningState());
        // Stop the container
        container.stop(CONTAINER);
        // Start the server again
        container.start(CONTAINER);
        Assert.assertTrue("Container is not started", managementClient.isServerInRunningState());
    }

    private void checkLogs(final String msg, final boolean expected) throws Exception {
        checkLogs(msg, expected, logFile);
    }

    /*
     * Search file for message
     */
    private void checkLogs(final String msg, final boolean expected, File file) throws Exception {
        BufferedReader reader = null;
        // check logs
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            boolean logFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains(msg)) {
                    logFound = true;
                    break;
                }
            }
            Assert.assertTrue("Message: \"" + msg + "\" was not found in file: " + file.getName(), logFound == expected);
        } finally {
            safeClose(reader);
        }
        log.info("Message: \"" + msg + "\" was found in file: " + file.getName());
    }

    private void makeLog(String msg, URL url) throws Exception {
        int statusCode = getResponse(new java.net.URL(url, "logger?msg=" + URLEncoder.encode(msg, "utf-8")));
        Assert.assertTrue("Invalid response statusCode: " + statusCode, statusCode == HttpServletResponse.SC_OK);
    }

    private int getResponse(URL url) throws IOException {
        return ((HttpURLConnection) url.openConnection()).getResponseCode();
    }

    private ModelNode validateResponse(final ModelNode operation) throws Exception {
        return validateResponse(operation, false);
    }

    private ModelNode validateResponse(ModelNode operation, boolean validateResult) throws Exception {
        ModelNode response = null;
        log.info(operation.asString());
        response = client.execute(operation);
        log.info(response.asString());
        if (!SUCCESS.equals(response.get(OUTCOME).asString())) {
            Assert.fail(response.get(FAILURE_DESCRIPTION).toString());
        }
        if (validateResult) {
            Assert.assertTrue("result exists", response.has(RESULT));
        }
        if (response == null) {
            Assert.fail("response was null");
        }
        return response.get(RESULT);
    }

    private void clearLogs(File file) {
        for (File f : file.getParentFile().listFiles()) {
            if (f.getName().contains(logFile.getName())) {
                f.delete();
                log.info("Deleted: " + f.getAbsolutePath());
                if (f.exists()) {
                    Assert.fail("Unable to delete file: " + f.getName());
                }
            }
        }
    }

    static File getAbsoluteLogFilePath(final ModelControllerClient client) throws IOException, MgmtOperationException {
        final ModelNode address = new ModelNode().setEmptyList();
        address.add(PATH, "jboss.server.log.dir");
        final ModelNode op = Operations.createReadAttributeOperation(address, PATH);
        final ModelNode result = client.execute(op);
        if (Operations.isSuccessfulOutcome(result)) {
            return new File(Operations.readResult(result).asString(), FILE_NAME);
        }
        throw new MgmtOperationException("Failed to read the path resource", op, result);
    }

    static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (Exception ignore) {
            // ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14240.java