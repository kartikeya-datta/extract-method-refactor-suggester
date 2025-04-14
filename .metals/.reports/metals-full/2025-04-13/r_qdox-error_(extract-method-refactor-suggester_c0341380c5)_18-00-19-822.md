error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3010.java
text:
```scala
c@@li.sendLine("xa-data-source add --name=TestXADS --jndi-name=java:jboss/datasources/TestXADS --driver-name=h2 --xa-datasource-properties=ServerName=localhost,PortNumber=50011");

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

import java.net.URL;
import java.util.Hashtable;
import org.jboss.as.test.integration.management.base.AbstractCliTestBase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.common.JndiServlet;
import org.jboss.as.test.integration.management.util.CLIOpResult;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class DataSourceTestCase extends AbstractCliTestBase {

    @ArquillianResource URL url;
    
    private static final String[][] DS_PROPS = new String[][] {
        {"idle-timeout-minutes", "5"}
    };
    
    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "DataSourceTestCase.war");
        war.addClass(DataSourceTestCase.class);
        war.addClass(JndiServlet.class);
        return war;
    }

    @BeforeClass
    public static void before() throws Exception {
        AbstractCliTestBase.initCLI();
    }

    @AfterClass
    public static void after() throws Exception {
        AbstractCliTestBase.closeCLI();
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
        cli.sendLine("data-source add --name=TestDS --jndi-name=java:jboss/datasources/TestDS --driver-name=h2 --connection-url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        // check the data source is listed
        cli.sendLine("cd /subsystem=datasources/data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(ls.contains("TestDS"));

        // enable data source
        cli.sendLine("data-source enable --name=TestDS");                
        cli.waitForPrompt(WAIT_TIMEOUT);

        // check that it is available through JNDI        
        String jndiClass = JndiServlet.lookup(url.toString(), "java:jboss/datasources/TestDS");
        Assert.assertEquals("org.jboss.jca.adapters.jdbc.WrapperDataSource", jndiClass);

    }

    private void testRemoveDataSource() throws Exception {
                
        // disable data source
        //cli.sendLine("data-source disable --name=TestDS");
                
        // remove data source
        //cli.sendLine("data-source remove --name=TestDS");
        cli.sendLine("/subsystem=datasources/data-source=TestDS:remove{allow-resource-service-restart=true}");

        //check the data source is not listed
        cli.sendLine("cd /subsystem=datasources/data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertFalse(ls.contains("java:jboss/datasources/TestDS"));        

        // check that it is not available through JNDI        
        String jndiClass = JndiServlet.lookup(url.toString(), "java:jboss/datasources/TestDS");
        Assert.assertEquals(JndiServlet.NOT_FOUND, jndiClass);                       

    }

    private void testModifyDataSource() throws Exception {
        StringBuilder cmd = new StringBuilder("data-source --name=TestDS");
        for (String[] props : DS_PROPS) {
            cmd.append(" --");
            cmd.append(props[0]);
            cmd.append("=");
            cmd.append(props[1]);
        }
        cli.sendLine(cmd.toString());

        // check that datasource was modified
        cli.sendLine("/subsystem=datasources/data-source=TestDS:read-resource(recursive=true)");
        CLIOpResult result = cli.readAllAsOpResult(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(result.getResult().toString(), result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map dsProps = (Map) result.getResult();
        for (String[] props : DS_PROPS) assertTrue(dsProps.get(props[0]).equals(props[1]));

    }

    private void testAddXaDataSource() throws Exception {

        // add data source
        cli.sendLine("xa-data-source add --name=TestXADS --jndi-name=java:jboss/datasources/TestXADS --driver-name=h2");

        //check the data source is listed
        cli.sendLine("cd /subsystem=datasources/xa-data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(ls.contains("TestXADS"));
        
        // add URL property
        cli.sendLine(
                "/subsystem=datasources/xa-data-source=TestXADS/xa-datasource-properties=URL:add(value=\"jdbc:h2:mem:test\")");        
        
        // enable data source
        cli.sendLine("xa-data-source enable --name=TestXADS");                
        cli.waitForPrompt(WAIT_TIMEOUT);
        
        // check that it is available through JNDI        
        String jndiClass = JndiServlet.lookup(url.toString(), "java:jboss/datasources/TestXADS");
        Assert.assertEquals("org.jboss.jca.adapters.jdbc.WrapperDataSource", jndiClass);
        

    }

    private void testRemoveXaDataSource() throws Exception {

        // remove data source
        cli.sendLine("xa-data-source remove --name=TestXADS");

        //check the data source is not listed
        cli.sendLine("cd /subsystem=datasources/xa-data-source");
        cli.sendLine("ls");
        String ls = cli.readAllUnformated(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertFalse(ls.contains("TestXADS"));
        
        // check that it is no more available through JNDI        
        String jndiClass = JndiServlet.lookup(url.toString(), "java:jboss/datasources/TestXADS");
        Assert.assertEquals(JndiServlet.NOT_FOUND, jndiClass);        

    }

    private void testModifyXaDataSource() throws Exception {
        StringBuilder cmd = new StringBuilder("xa-data-source --name=TestXADS");
        for (String[] props : DS_PROPS) {
            cmd.append(" --");
            cmd.append(props[0]);
            cmd.append("=");
            cmd.append(props[1]);
        }
        cli.sendLine(cmd.toString());

        // check that datasource was modified
        cli.sendLine("/subsystem=datasources/xa-data-source=TestXADS:read-resource(recursive=true)");
        CLIOpResult result = cli.readAllAsOpResult(WAIT_TIMEOUT, WAIT_LINETIMEOUT);
        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map dsProps = (Map) result.getResult();
        for (String[] props : DS_PROPS) assertTrue(dsProps.get(props[0]).equals(props[1]));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3010.java