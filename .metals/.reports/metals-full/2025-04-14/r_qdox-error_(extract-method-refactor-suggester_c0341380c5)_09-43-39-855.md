error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3185.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3185.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3185.java
text:
```scala
C@@LIWrapper cw = new CLIWrapper(false, new String[] {"--version"});

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
package org.jboss.as.test.integration.management.cli;

import java.io.File;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.management.base.AbstractCliTestBase;
import org.jboss.as.test.integration.management.util.CLIWrapper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CliArgumentsTestCase extends AbstractCliTestBase {
    
    private static final String tempDir = System.getProperty("java.io.tmpdir");
    
    @ArquillianResource
    private ManagementClient managementClient;
    
    @Deployment
    public static Archive<?> getDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "dummy.jar");
        ja.addClass(GlobalOpsTestCase.class);
        return ja;
    }
    
    @Test
    public void testVersionArgument() throws Exception {
        CLIWrapper cw = new CLIWrapper(true, new String[] {"--version"});
        String output = cw.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue(output.contains("JBOSS_HOME"));
        Assert.assertTrue(cw.hasQuit());
    }

    @Test
    public void testCommandArgument() throws Exception {
        CLIWrapper cw = new CLIWrapper(false, new String[] {"--command=version"});
        String output = cw.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue(output.contains("JBOSS_HOME"));
        Assert.assertTrue(cw.hasQuit());
    }

    @Test
    public void testCommandsArgument() throws Exception {
        CLIWrapper cli = new CLIWrapper(false, new String[] {getControllerString(), "--commands=version,connect,ls"});
        String output = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue("CLI Output contains JBOSS_HOME: " + output, output.contains("JBOSS_HOME"));
        Assert.assertTrue("CLI Output contains sybsystem:  " + output, output.contains("subsystem"));
        Assert.assertTrue("CLI Output contains extension:  " + output, output.contains("extension"));
        
        Assert.assertTrue(cli.hasQuit());
    }
    
    @Test
    public void testFileArgument() throws Exception {
        
        // prepare file
        File cliScriptFile = new File(tempDir, "testScript.cli");
        if (cliScriptFile.exists()) Assert.assertTrue(cliScriptFile.delete());
        FileUtils.writeStringToFile(cliScriptFile, "version" + System.getProperty("line.separator"));
        
        // pass it to CLI
        CLIWrapper cw = new CLIWrapper(false, new String[] {"--file=" + cliScriptFile.getAbsolutePath()});
        String output = cw.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue("output.contains(\"JBOSS_HOME\")", output.contains("JBOSS_HOME"));
        Assert.assertTrue("cli.hasQuit()", cw.hasQuit());      
        
        cliScriptFile.delete();
    }
    
    @Test
    public void testConnectArgument() throws Exception {
        CLIWrapper cli = new CLIWrapper(false, new String[] {getControllerString(), "--commands=version,connect,ls"});
        String output = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue(output.contains("JBOSS_HOME"));
        Assert.assertTrue(output.contains("subsystem"));
        Assert.assertTrue(output.contains("extension"));        
        Assert.assertTrue(cli.hasQuit());
    }    
    
    @Test
    public void testControlerArgument() throws Exception {
        String controller = getControllerString();
        CLIWrapper cli = new CLIWrapper(false, new String[] {controller});
        cli.sendLine("connect");
        cli.sendLine("ls");
        String output = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        Assert.assertTrue(output.contains("subsystem"));
        Assert.assertTrue(output.contains("extension"));        
        cli.quit();
        
        cli = new CLIWrapper(false, new String[] {getControllerString(-1)});
        cli.sendLine("connect");
        output = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT * 10);
        Assert.assertTrue(output.contains("The controller is not available"));
        cli.quit();        
    }

    private String getControllerString() {
        return getControllerString(0);
    }

    private String getControllerString(int portOffset) {
        String mgmtAddr1 = managementClient.getMgmtAddress();
        int mgmtPort1 = managementClient.getMgmtPort();
        return "--controller=" + mgmtAddr1 + ":" + String.valueOf(mgmtPort1 + portOffset);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3185.java