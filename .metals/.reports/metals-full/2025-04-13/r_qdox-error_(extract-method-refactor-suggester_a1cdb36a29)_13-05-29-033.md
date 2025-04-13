error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13505.java
text:
```scala
t@@empFile.delete();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.embedded.ejb3;

import org.jboss.as.embedded.StandaloneServer;
import org.jboss.logging.Logger;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class JBossStandaloneEJBContainer extends EJBContainer {
    private static final Logger log = Logger.getLogger(JBossStandaloneEJBContainer.class);

    private final StandaloneServer server;
    private final List<File> deployments = new LinkedList<File>();
    private final List<File> tempFiles = new LinkedList<File>();

    JBossStandaloneEJBContainer(final StandaloneServer server) {
        this.server = server;
    }

    @Override
    public void close() {
        for (File deployment : deployments) {
            try {
                server.undeploy(deployment);
            } catch (Exception e) {
                log.warnf(e, "Failed to undeploy %s", deployment);
            }
        }
        server.stop();
        for(File tempFile : tempFiles) {
            //tempFile.delete();
        }
    }

    void deploy(File deployment) throws IOException, ExecutionException, InterruptedException {
        server.deploy(deployment);
        deployments.add(deployment);
    }

    /**
     * Search the JVM classpath to find EJB modules and deploy them.
     *
     * @param properties
     */
    void init(final Map<?, ?> properties) throws IOException, ExecutionException, InterruptedException {
        // TODO: the ClassPathEjbJarScanner is not the optimal way to find EJBs, see TODOs in there.
        // ClassPathEjbJarScanner uses TCCL
        final String[] candidates = ClassPathEjbJarScanner.getEjbJars(properties);

        // TODO: use a DeploymentPlan
        if (candidates.length == 1) {
            deploy(new File(candidates[0]));
        } else if (candidates.length > 1) {
            //TODO: this is a massive hack, we build up an EAR and then deploy it as an EAR


            File tempEar;
            if (properties.containsKey(EJBContainer.APP_NAME)) {
                String tmpDir = AccessController.doPrivileged(new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty("java.io.tmpdir");
                    }
                });
                tempEar = new File(tmpDir + "/" + properties.get(EJBContainer.APP_NAME) + ".ear");
            } else {
                tempEar = File.createTempFile("ejb-embedded", ".ear");
            }
            FileOutputStream out = new FileOutputStream(tempEar);
            ZipOutputStream zip = new ZipOutputStream(out);
            try {
                byte[] buf = new byte[1024];
                for (final String candidate : candidates) {

                    File file = new File(candidate);
                    File zipTarget;
                    if (file.isDirectory()) {
                        zipTarget = createTempZip(file);
                    } else {
                        zipTarget = file;
                    }


                    InputStream stream = new FileInputStream(zipTarget);
                    try {
                        final String name;
                        if(file.getName().indexOf('.') == -1) {
                            name = file.getName() + ".jar";
                        } else {
                            name = file.getName();
                        }

                        ZipEntry entry = new ZipEntry(name);
                        zip.putNextEntry(entry);
                        int len;
                        while ((len = stream.read(buf)) > 0) {
                            zip.write(buf, 0, len);
                        }
                    } finally {
                        stream.close();
                    }
                }
                zip.flush();
            } finally {
                zip.close();
            }
            tempFiles.add(tempEar);
            deploy(tempEar);
        }
    }

    private static File createTempZip(File file) throws IOException {
        File ret = File.createTempFile("ejbTemp", ".jar");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(ret));
        try {
            zip(file, file, out);
        } finally {
            out.close();
        }
        return ret;
    }

    private static void zip(File directory, File base,
                            ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[1024];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                try {
                    ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                            base.getPath().length() + 1));
                    zos.putNextEntry(entry);
                    while (-1 != (read = in.read(buffer))) {
                        zos.write(buffer, 0, read);
                    }
                } finally {
                    in.close();
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return server.getContext();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13505.java