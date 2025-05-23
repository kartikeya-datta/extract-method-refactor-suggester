error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14269.java
text:
```scala
public D@@eploymentUtils(Archive<?> archive) throws UnknownHostException {

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
package org.jboss.as.demos;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.standalone.DuplicateDeploymentNameException;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.jboss.as.protocol.StreamUtils.safeClose;

/**
 * Used to deploy/undeploy deployments to a running <b>standalone</b> application server
 *
 * TODO Use the real deployment API once that is complete
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DeploymentUtils implements Closeable {

    public static final long DEFAULT_TIMEOUT = 15000;

    private final List<AbstractDeployment> deployments = new ArrayList<AbstractDeployment>();
    private final ModelControllerClient client;
    private final ServerDeploymentManager manager;
    private long timeout = DEFAULT_TIMEOUT;

    public DeploymentUtils() throws UnknownHostException {
        client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999);
        manager = ServerDeploymentManager.Factory.create(client);
    }

    public DeploymentUtils(String archiveName, Package... pkg) throws UnknownHostException {
        this();
        addDeployment(archiveName, pkg);
    }

    public DeploymentUtils(Archive archive) throws UnknownHostException {
        this();
        deployments.add(new ArbitraryDeployment(archive,false));
    }

    public DeploymentUtils(String archiveName, boolean show, Package... pkgs) throws UnknownHostException {
        this();
        addDeployment(archiveName, show, pkgs);
    }

    public synchronized void addDeployment(String archiveName, Package... pkgs) {
        addDeployment(archiveName, false, pkgs);
    }

    public synchronized void addDeployment(String archiveName,  boolean show, Package... pkgs) {
        deployments.add(new Deployment(archiveName, pkgs, show));
    }

    public synchronized void addWarDeployment(String archiveName, Package... pkgs) {
        addWarDeployment(archiveName, false, pkgs);
    }

    public synchronized void addWarDeployment(String archiveName, boolean show, Package... pkgs) {
        deployments.add(new WarDeployment(archiveName, pkgs, show));
    }

    public synchronized void deploy()  throws DuplicateDeploymentNameException, IOException, ExecutionException, InterruptedException, TimeoutException  {
        DeploymentPlanBuilder builder = manager.newDeploymentPlan().withRollback();
        for (AbstractDeployment deployment : deployments) {
            builder = deployment.addDeployment(manager, builder);
        }

        try {
            manager.execute(builder.build()).get(timeout, TimeUnit.MILLISECONDS);
        } finally {
            markDeploymentsDeployed();
        }
    }

    private void markDeploymentsDeployed() {
        for (AbstractDeployment deployment : deployments) {
            deployment.deployed = true;
        }
    }

    public synchronized void undeploy() throws ExecutionException, InterruptedException, TimeoutException {
        DeploymentPlanBuilder builder = manager.newDeploymentPlan();
        for (AbstractDeployment deployment : deployments) {
            builder = deployment.removeDeployment(builder);
        }
        DeploymentPlan plan = builder.build();
        if (plan.getDeploymentActions().size() > 0) {
            manager.execute(builder.build()).get(timeout, TimeUnit.MILLISECONDS);
        }
    }

    public MBeanServerConnection getConnection() throws Exception {
        return JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1090/jmxrmi"),
                new HashMap<String, Object>()).getMBeanServerConnection();
    }

    public String showJndi() throws Exception {
        return (String)getConnection().invoke(new ObjectName("jboss:type=JNDIView"), "list", new Object[] {true}, new String[] {"boolean"});
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void close() throws IOException {
        safeClose(client);
    }

    private abstract class AbstractDeployment{

        boolean deployed;
        String deployment;

        public synchronized DeploymentPlanBuilder addDeployment(ServerDeploymentManager manager, DeploymentPlanBuilder builder) throws DuplicateDeploymentNameException, IOException, ExecutionException, InterruptedException {
            deployment = getRealArchive().getName();
            System.out.println("Deploying " + deployment);
            return builder.add(deployment, getRealArchive()).deploy(deployment);
        }

        public synchronized DeploymentPlanBuilder removeDeployment(DeploymentPlanBuilder builder) {
            if (deployed) {
                System.out.println("Undeploying " + deployment);
                return builder.undeploy(deployment).remove(deployment);
            }
            else {
                return builder;
            }
        }

        protected void addFiles(ResourceContainer<?> archive, File dir, ArchivePath dest) {
            for (String name : dir.list()) {
                File file = new File(dir, name);
                if (file.isDirectory()) {
                    addFiles(archive, file, ArchivePaths.create(dest, name));
                } else {
                    archive.addResource(file, ArchivePaths.create(dest, name));
                }
            }
        }

        protected File getSourceMetaInfDir(String archiveName) {
            String name = "archives/" + archiveName + "/META-INF/MANIFEST.MF";

            URL url = Thread.currentThread().getContextClassLoader().getResource(name);
            if (url == null) {
                throw new IllegalArgumentException("No resource called " + name);
            }
            try {
                File file = new File(url.toURI());
                return file.getParentFile();
            } catch (URISyntaxException e) {
                throw new RuntimeException("Could not get file for " + url);
            }
        }

        protected File getSourceWebInfDir(String archiveName) {
           String name = "archives/" + archiveName + "/WEB-INF";

           URL url = Thread.currentThread().getContextClassLoader().getResource(name);
           if (url == null) {
              return null;
           }
           try {
               return new File(url.toURI());
           } catch (URISyntaxException e) {
               throw new RuntimeException("Could not get file for " + url);
           }
       }

        protected File getOutputDir() {
            File file = new File("target");
            if (!file.exists()) {
                throw new IllegalStateException("target/ does not exist");
            }
            if (!file.isDirectory()) {
                throw new IllegalStateException("target/ is not a directory");
            }
            file = new File(file, "archives");
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IllegalStateException("target/archives/ already exists and is not a directory");
                }
            } else {
                file.mkdir();
            }
            return file.getAbsoluteFile();
        }

        protected File createArchive(Archive<?> archive) {
            File realArchive = new File(getOutputDir(), archive.getName());
            archive.as(ZipExporter.class).exportZip(realArchive, true);
            return realArchive;
        }

        protected abstract File getRealArchive();
    }

    private class Deployment extends AbstractDeployment {
        final File realArchive;

        public Deployment(String archiveName, Package[] pkgs, boolean show) {

            ArchivePath metaInf = ArchivePaths.create("META-INF");

            JavaArchive archive = ShrinkWrap.create(JavaArchive.class, archiveName);
            for(Package pkg : pkgs) {
                archive.addPackage(pkg);
            }

            File sourceMetaInf = getSourceMetaInfDir(archiveName);
            addFiles(archive, sourceMetaInf, metaInf);

            System.out.println(archive.toString(show));
            realArchive = createArchive(archive);
        }

        @Override
        protected File getRealArchive() {
            return realArchive;
        }
    }


    private class WarDeployment extends AbstractDeployment {
        final File realArchive;

        public WarDeployment(String archiveName, Package[] pkgs, boolean show) {

            ArchivePath metaInf = ArchivePaths.create("META-INF");


            WebArchive archive = ShrinkWrap.create(WebArchive.class, archiveName);
            for(Package pkg : pkgs) {
                archive.addPackage(pkg);
            }

            File sourceMetaInf = getSourceMetaInfDir(archiveName);
            addFiles(archive, sourceMetaInf, metaInf);

            File sourceWebInf = getSourceWebInfDir(archiveName);
            if (sourceWebInf != null) {
               addFiles(archive, sourceWebInf, ArchivePaths.create("WEB-INF"));
            }

            System.out.println(archive.toString(show));
            realArchive = createArchive(archive);
        }

        @Override
        protected File getRealArchive() {
            return realArchive;
        }
    }

    private class ArbitraryDeployment extends AbstractDeployment {
        final File realArchive;

        public ArbitraryDeployment(Archive archive,  boolean show) {

            ArchivePath metaInf = ArchivePaths.create("META-INF");

            System.out.println(archive.toString(show));
            realArchive = createArchive(archive);
        }

        @Override
        protected File getRealArchive() {
            return realArchive;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14269.java