error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11270.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11270.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11270.java
text:
```scala
private static final S@@impleDateFormat OLD_FILE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.as.controller.audit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.protocol.StreamUtils;
import org.xnio.IoUtils;

/**
 *  All methods on this class should be called with {@link ManagedAuditLoggerImpl}'s lock taken.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class FileAuditLogHandler extends AuditLogHandler {
    //SimpleDateFormat is not good to store among threads, since it stores intermediate results in its fields
    //Methods on this class will only ever be called from one thread (see class javadoc) so although it looks shared here it is not
    private static final SimpleDateFormat OLD_FILE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final byte[] LINE_TERMINATOR = String.format("%n").getBytes();
    private final PathManagerService pathManager;
    private final String path;
    private final String relativeTo;
    private volatile File file;

    public FileAuditLogHandler(String name, String formatterName, int maxFailureCount, PathManagerService pathManager, String path, String relativeTo) {
        super(name, formatterName, maxFailureCount);
        this.pathManager = pathManager;
        this.path = path;
        this.relativeTo = relativeTo;
        this.file = file;
    }

    @Override
    void initialize() {
        if (file != null){
            return;
        }
        File file = new File(pathManager.resolveRelativePathEntry(path, relativeTo));
        if (file.exists() && file.isDirectory()) {
            throw ControllerMessages.MESSAGES.resolvedFileDoesNotExistOrIsDirectory(file);
        }
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (file.exists()) {
            File backup = new File(file.getParentFile(), file.getName() + OLD_FILE_FORMATTER.format(new Date()));
            try {
                rename(file, backup);
            } catch (IOException e) {
                throw ControllerMessages.MESSAGES.couldNotBackUp(e, file.getAbsolutePath(), backup.getAbsolutePath());
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        this.file = file;
    }

    @Override
    void stop() {
        file = null;
    }

    @Override
    void writeLogItem(String formattedItem) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file, true);
        final BufferedOutputStream output = new BufferedOutputStream(fos);
        try {
            output.write(formattedItem.getBytes());
            output.write(LINE_TERMINATOR);

            //Flush and force the file to sync
            output.flush();
            fos.getFD().sync();
        } finally {
            IoUtils.safeClose(output);
        }
    }

    boolean isDifferent(AuditLogHandler other){
        if (other instanceof FileAuditLogHandler == false){
            return true;
        }
        FileAuditLogHandler otherHandler = (FileAuditLogHandler)other;
        if (!name.equals(otherHandler.name)){
            return true;
        }
        if (!getFormatterName().equals(otherHandler.getFormatterName())) {
            return true;
        }
        if (!path.equals(otherHandler.path)){
            return true;
        }
        if (!compare(relativeTo, otherHandler.relativeTo)){
            return true;
        }
        return false;
    }

    private boolean compare(Object one, Object two){
        if (one == null && two == null){
            return true;
        }
        if (one == null && two != null){
            return false;
        }
        if (one != null && two == null){
            return false;
        }
        return one.equals(two);
    }

    private void copyFile(final File file, final File backup) throws IOException {
        final InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            final FileOutputStream fos = new FileOutputStream(backup);
            final BufferedOutputStream output = new BufferedOutputStream(fos);
            try {
                StreamUtils.copyStream(in, output);

                //Flush and force the file to sync
                output.flush();
                fos.getFD().sync();
                fos.close();
            } finally {
                StreamUtils.safeClose(output);
            }
        } finally {
            StreamUtils.safeClose(in);
        }
    }

    private void rename(File file, File to) throws IOException {
        if (!file.renameTo(to) && file.exists()) {
            copyFile(file, to);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11270.java