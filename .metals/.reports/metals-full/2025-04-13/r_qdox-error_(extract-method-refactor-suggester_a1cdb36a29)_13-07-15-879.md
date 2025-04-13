error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3781.java
text:
```scala
S@@tring name = "JBOSS_HOME" + file.getPathName().substring(this.jbossHome.length());

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
package org.jboss.as.jdr.util;

import org.jboss.as.jdr.commands.JdrEnvironment;
import org.jboss.vfs.VirtualFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import static org.jboss.as.jdr.logger.JdrLogger.ROOT_LOGGER;

/**
 * Abstracts the zipfile used for packaging the JDR Report.
 */
public class JdrZipFile {

    ZipOutputStream zos;
    String jbossHome;
    JdrEnvironment env;
    String name;
    String baseName;

    public JdrZipFile(JdrEnvironment env) throws Exception {
        this.env = env;
        this.jbossHome = this.env.getJbossHome();
        SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd_hh-mm-ss");
        baseName = "jdr_" + fmt.format(new Date());

        if (this.env.getHostControllerName() != null) {
            this.baseName += "." + this.env.getHostControllerName();
        }

        if (this.env.getServerName() != null) {
            this.baseName += "_" + this.env.getServerName();
        }

        this.name = this.env.getOutputDirectory() +
                java.io.File.separator +
                baseName + ".zip";

        zos = new ZipOutputStream(new FileOutputStream(this.name));
    }

    /**
     * @return the full pathname to the zipfile on disk
     */
    public String name() {
        return this.name;
    }

    /**
     * Adds the contents of the {@link InputStream} to the path in the zip.
     *
     * This method allows for absolute control of the destination of the content to be stored.
     * It is not common to use this method.
     * @param is content to write
     * @param path destination to write to in the zip file
     */
    public void add(InputStream is, String path) {
        byte [] buffer = new byte[1024];

        try {
            String entryName = this.baseName + "/" + path;
            ZipEntry ze = new ZipEntry(entryName);
            zos.putNextEntry(ze);
            int bytesRead = is.read(buffer);
            while( bytesRead > -1 ) {
                zos.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
        }
        catch (ZipException ze) {
            ROOT_LOGGER.debugf(ze, "%s is already in the zip", path);
        }
        catch (Exception e) {
            ROOT_LOGGER.debugf(e, "Error when adding %s", path);
        }
        finally {
            try {
                zos.closeEntry();
            }
            catch (Exception e) {
                ROOT_LOGGER.debugf(e, "Error when closing entry for %s", path);
            }
        }
    }

    /**
     * Adds the content of the {@link InputStream} to the zip in a location that mirrors where {@link VirtualFile file} is located.
     *
     * For example if {@code file} is at {@code /tmp/foo/bar} and {@code $JBOSS_HOME} is {@code tmp} then the destination will be {@code JBOSSHOME/foo/bar}
     *
     * @param file {@link VirtualFile} where metadata is read from
     * @param is content to write to the zip file
     * @throws Exception
     */
    public void add(VirtualFile file, InputStream is) throws Exception {
        String name = "JBOSS_HOME" + file.getPhysicalFile().getAbsolutePath().substring(this.jbossHome.length());
        this.add(is, name);
    }

    /**
     * Adds content to the zipfile at path
     *
     * path is prepended with the directory reserved for generated text files in JDR
     *
     * @param content
     * @param path
     * @throws Exception
     */
    public void add(String content, String path) throws Exception {
        String name = "sos_strings/as7/" + path;
        this.add(new ByteArrayInputStream(content.getBytes()), name);
    }


    /**
     * Adds content to the zipfile in a file named logName
     *
     * path is prepended with the directory reserved for JDR log files
     *
     * @param content
     * @param logName
     * @throws Exception
     */
    public void addLog(String content, String logName) throws Exception {
        String name = "sos_logs/" + logName;
        this.add(new ByteArrayInputStream(content.getBytes()), name);
    }

    public void close() throws Exception {
        this.zos.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3781.java