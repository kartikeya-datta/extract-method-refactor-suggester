error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14127.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14127.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14127.java
text:
```scala
S@@tring ls = cli.readAllUnformated(WAIT_LINETIMEOUT, WAIT_LINETIMEOUT);

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
package org.jboss.as.test.integration.domain.management.cli;

import org.jboss.as.test.integration.domain.suites.CLITestSuite;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.as.test.integration.management.base.AbstractCliTestBase;
import org.jboss.as.test.integration.management.util.CLIOpResult;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
public class DataSourceTestCase extends AbstractCliTestBase {
    
    private static String[] profileNames;
    
    private static final String[][] DS_PROPS = new String[][] {
        {"idle-timeout-minutes", "5"}
    };
    
    @BeforeClass
    public static void before() throws Exception {      
        AbstractCliTestBase.initCLI();
    }    
    
    @AfterClass
    public static void after() throws Exception {
        AbstractCliTestBase.closeCLI();
    }
    
    @Before
    public void init() {
         profileNames = CLITestSuite.serverProfiles.keySet().toArray(new String[] {});
    }    

    @Test
    public void testDataSource() throws Exception {
        testAddDataSource();
        testModifyDataSource();
        testRemoveDataSource();
    }

    @Test
    public void testXaDataSource() throws Exception {        
        testAddXaDataSource();
        testModifyXaDataSource();
        testRemoveXaDataSource();         
    }

    private void testAddDataSource() throws Exception {

        // add data source
        cli.sendLine("data-source add --profile=" + profileNames[0] + " --jndi-name=java:jboss/datasources/TestDS --driver-name=h2 --connection-url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        // check the data source is listed
        cli.sendLine("cd /profile=" + profileNames[0] + "/subsystem=datasources/data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue("Datasource not found: " + ls, ls.contains("java:jboss/datasources/TestDS"));

        // check that it is available through JNDI
        // TODO implement when @ArquillianResource InitialContext is done

    }

    private void testRemoveDataSource() throws Exception {

        // remove data source
        cli.sendLine("data-source remove --profile=" + profileNames[0] + " --jndi-name=java:jboss/datasources/TestDS");

        //check the data source is not listed
        cli.sendLine("cd /profile=" + profileNames[0] + "/subsystem=datasources/data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertFalse(ls.contains("java:jboss/datasources/TestDS"));

    }

    private void testModifyDataSource() throws Exception {
        StringBuilder cmd = new StringBuilder("data-source --profile=" + profileNames[0] + " --jndi-name=java:jboss/datasources/TestDS");
        for (String[] props : DS_PROPS) {
            cmd.append(" --");
            cmd.append(props[0]);
            cmd.append("=");
            cmd.append(props[1]);
        }
        cli.sendLine(cmd.toString());

        // check that datasource was modified
        cli.sendLine("/profile=" + profileNames[0] + "/subsystem=datasources/data-source=java\\:jboss\\/datasources\\/TestDS:read-resource(recursive=true)");       
        CLIOpResult result = cli.readAllAsOpResult(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map dsProps = (Map) result.getResult();
        for (String[] props : DS_PROPS) assertTrue(dsProps.get(props[0]).equals(props[1]));

    }    

    private void testAddXaDataSource() throws Exception {

        // add data source
        cli.sendLine("xa-data-source add --profile=" + profileNames[0] + 
                " --jndi-name=java:jboss/datasources/TestXADS --driver-name=h2");

        //check the data source is listed
        cli.sendLine("cd /profile=" + profileNames[0] + "/subsystem=datasources/xa-data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(ls.contains("java:jboss/datasources/TestXADS"));

    }

    private void testModifyXaDataSource() throws Exception {
        StringBuilder cmd = new StringBuilder("xa-data-source --profile=" + profileNames[0] + " --jndi-name=java:jboss/datasources/TestXADS");
        for (String[] props : DS_PROPS) {
            cmd.append(" --");
            cmd.append(props[0]);
            cmd.append("=");
            cmd.append(props[1]);
        }
        cli.sendLine(cmd.toString());

        // check that datasource was modified
        cli.sendLine("/profile=" + profileNames[0] + "/subsystem=datasources/xa-data-source=java\\:jboss\\/datasources\\/TestXADS:read-resource(recursive=true)");       
        CLIOpResult result = cli.readAllAsOpResult(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map dsProps = (Map) result.getResult();
        for (String[] props : DS_PROPS) assertTrue(dsProps.get(props[0]).equals(props[1]));

    }    
    
    private void testRemoveXaDataSource() throws Exception {

        // remove data source
        cli.sendLine("xa-data-source remove  --profile=" + profileNames[0] + " --jndi-name=java:jboss/datasources/TestXADS");

        //check the data source is not listed
        cli.sendLine("cd /profile=" + profileNames[0] + "/subsystem=datasources/xa-data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertFalse(ls.contains("java:jboss/datasources/TestXADS"));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14127.java