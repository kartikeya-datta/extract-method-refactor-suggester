error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10381.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10381.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10381.java
text:
```scala
r@@eturn JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remoting-jmx://localhost:9999")).getMBeanServerConnection();

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

package org.jboss.as.test.integration.sar.unit;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.test.integration.sar.SarWithinEarService;
import org.jboss.as.test.integration.sar.SarWithinEarServiceMBean;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.as.arquillian.container.Authentication.getCallbackHandler;

/**
 * Test that a service configured in a .sar within a .ear deployment works fine, both when the .ear contains a application.xml
 * and when it doesn't.
 * <p/>
 * User: Jaikiran Pai
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SarWithinEarTestCase {

    private static final String EAR_WITHOUT_APPLICATION_XML = "sar-within-ear-without-application-xml.ear";

    private static final String EAR_WITH_APPLICATION_XML = "sar-within-ear-with-application-xml.ear";
    private ModelControllerClient client;


    @Before
    public void init() throws UnknownHostException {
        client = ModelControllerClient.Factory.create("localhost", 9999, getCallbackHandler());
    }

    @After
    public void destroy() throws IOException {
        client.close();
    }
    /**
     * Create a .ear, without an application.xml, with a nested .sar deployment
     *
     * @return
     */
    @Deployment(name = "ear-without-application-xml", testable = false)
    public static EnterpriseArchive getEarWithoutApplicationDotXml() {
        final JavaArchive sar = ShrinkWrap.create(JavaArchive.class, "simple-sar.sar");
        sar.addClasses(SarWithinEarServiceMBean.class, SarWithinEarService.class);
        sar.addAsManifestResource("sar/jboss-service-without-application-xml.xml", "jboss-service.xml");

        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, EAR_WITHOUT_APPLICATION_XML);
        ear.addAsModule(sar);
        return ear;
    }

    /**
     * Create a .ear with an application.xml and a nested .sar deployment
     *
     * @return
     */
    @Deployment(name = "ear-with-application-xml", testable = false)
    public static EnterpriseArchive getEarWithApplicationDotXml() {
        final JavaArchive sar = ShrinkWrap.create(JavaArchive.class, "simple-sar.sar");
        sar.addClasses(SarWithinEarServiceMBean.class, SarWithinEarService.class);
        sar.addAsManifestResource("sar/jboss-service-with-application-xml.xml", "jboss-service.xml");

        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, EAR_WITH_APPLICATION_XML);
        ear.addAsModule(sar);
        ear.addAsManifestResource("sar/application.xml", "application.xml");
        return ear;
    }

    /**
     * Tests that invocation on a service deployed within a .sar, inside a .ear without an application.xml, is successful.
     *
     * @throws Exception
     */
    @OperateOnDeployment("ear-without-application-xml")
    @Test
    public void testSarWithinEarWithoutApplicationXml() throws Exception {
        this.testSarWithinEar("jboss:name=service-in-sar-within-a-ear-without-application-xml");
    }

    /**
     * Tests that invocation on a service deployed within a .sar, inside a .ear with an application.xml, is successful.
     *
     * @throws Exception
     */
    @OperateOnDeployment("ear-with-application-xml")
    @Test
    public void testSarWithinEarWithApplicationXml() throws Exception {
        this.testSarWithinEar("jboss:name=service-in-sar-within-a-ear-with-application-xml");
    }

    private void testSarWithinEar(final String serviceName) throws Exception {
        final MBeanServerConnection mBeanServerConnection = this.getMBeanServerConnection();
        final ObjectName mbeanObjectName = new ObjectName(serviceName);
        final int num1 = 3;
        final int num2 = 4;
        // invoke the operation on MBean
        Integer sum = (Integer) mBeanServerConnection.invoke(mbeanObjectName, "add", new Object[]{num1, num2}, new String[]{Integer.TYPE.getName(), Integer.TYPE.getName()});
        Assert.assertEquals("Unexpected return value from MBean: " + mbeanObjectName, num1 + num2, (int) sum);
    }

    private MBeanServerConnection getMBeanServerConnection() throws IOException {
        return JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remote://localhost:9999")).getMBeanServerConnection();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10381.java