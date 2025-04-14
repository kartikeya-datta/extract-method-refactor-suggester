error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7144.java
text:
```scala
S@@ystem.out.println("        <datasource jndi-name=\"java:jboss/datasources/ExampleDS\" enabled=\"true\" use-java-context=\"true\" pool-name=\"H2DS\">");

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
package org.jboss.as.demos.ds.runner;

import static org.jboss.as.protocol.old.StreamUtils.safeClose;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.NameNotFoundException;

import org.jboss.as.demos.DeploymentUtils;
import org.jboss.as.demos.ds.mbean.Test;

/**
 * Runner for the datasource demo
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class ExampleRunner {

    public static void main(String[] args) throws Exception {
        DeploymentUtils utils = null;
        try {
            utils = new DeploymentUtils("ds-mbean.sar", true, Test.class.getPackage());

            utils.deploy();
            ObjectName objectName = new ObjectName("jboss:name=test,type=ds");

            MBeanServerConnection mbeanServer = utils.getConnection();
            System.out.println("Calling TestMBean.test() on server");
            String s = (String) mbeanServer.invoke(objectName, "test", new Object[0], new String[0]);
            System.out.println("Received reply: " + s);
        } catch (Exception e) {
            Throwable parent = e;
            while (parent != null) {
                if (parent instanceof NameNotFoundException && e.getMessage().indexOf("H2DS") > -1) {
                    usage(parent);
                    return;
                }
                parent = parent.getCause();
            }
            e.printStackTrace();
        } finally {
            if (utils != null) {
                utils.undeploy();
            }
            safeClose(utils);
        }
    }

    private static void usage(Throwable t) throws Exception {
        System.out.println("Caught " + t.toString());
        System.out.println("Please make sure your standalone.xml includes the H2DS datasource in its <profile> element.");
        System.out.println("An example configuration is as follows:\n");

        System.out.println("<subsystem xmlns=\"urn:jboss:domain:datasources:1.0\">");
        System.out.println("    <datasources>");
        System.out.println("        <datasource jndi-name=\"java:/H2DS\" enabled=\"true\" use-java-context=\"true\" pool-name=\"H2DS\">");
        System.out.println("            <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1</connection-url>");
        System.out.println("            <driver-class>org.h2.Driver</driver-class>");
        System.out.println("            <module>com.h2database.h2</module>");
        System.out.println("            <pool></pool>");
        System.out.println("            <security>");
        System.out.println("                <user-name>sa</user-name>");
        System.out.println("                <password>sa</password>");
        System.out.println("            </security>");
        System.out.println("            <validation></validation>");
        System.out.println("            <time-out></time-out>");
        System.out.println("            <statement></statement>");
        System.out.println("        </datasource>");
        System.out.println("    </datasources>");
        System.out.println("</subsystem>");

        System.out.println("\nIf your profile already includes other datasource configurations, just add the nested <datasource> element above next to them.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7144.java