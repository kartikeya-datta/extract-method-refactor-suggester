error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5155.java
text:
```scala
l@@ong ts = isHostOsUnix() ? localFileHeader.dateTimeModified * 1000l

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.compress.archivers.arj;

import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipUtil;

/**
 * An entry in an ARJ archive.
 * 
 * @NotThreadSafe
 * @since 1.6
 */
public class ArjArchiveEntry implements ArchiveEntry {
    private final LocalFileHeader localFileHeader;
    
    public ArjArchiveEntry() {
        localFileHeader = new LocalFileHeader();
    }
    
    ArjArchiveEntry(final LocalFileHeader localFileHeader) {
        this.localFileHeader = localFileHeader;
    }

    /**
     * Get this entry's name.
     *
     * @return This entry's name.
     */
    public String getName() {
        if ((localFileHeader.arjFlags & LocalFileHeader.Flags.PATHSYM) != 0) {
            return localFileHeader.name.replaceAll("/",
                    Matcher.quoteReplacement(File.separator));
        } else {
            return localFileHeader.name;
        }
    }

    /**
     * Get this entry's file size.
     *
     * @return This entry's file size.
     */
    public long getSize() {
        return localFileHeader.originalSize;
    }

    /** True if the entry refers to a directory */
    public boolean isDirectory() {
        return localFileHeader.fileType == LocalFileHeader.FileTypes.DIRECTORY;
    }

    /**
     * The last modified date of the entry.
     *
     * <p>Note the interpretation of time is different depending on
     * the HostOS that has created the archive.  While an OS that is
     * {@link #isHostOsUnix considered to be Unix} stores time in a
     * timezone independent manner, other platforms only use the local
     * time.  I.e. if an archive has been created at midnight UTC on a
     * machine in timezone UTC this method will return midnight
     * regardless of timezone if the archive has been created on a
     * non-Unix system and a time taking the current timezone into
     * account if the archive has beeen created on Unix.</p>
     */
    public Date getLastModifiedDate() {
        long ts = isHostOsUnix() ? (localFileHeader.dateTimeModified * 1000l)
            : ZipUtil.dosToJavaTime(0xFFFFFFFFL & localFileHeader.dateTimeModified);
        return new Date(ts);
    }

    /**
     * File mode of this entry.
     *
     * <p>The format depends on the host os that created the entry.</p>
     */
    public int getMode() {
        return localFileHeader.fileAccessMode;
    }

    /**
     * File mode of this entry as Unix stat value.
     *
     * <p>Will only be non-zero of the host os was UNIX.
     */
    public int getUnixMode() {
        return isHostOsUnix() ? getMode() : 0;
    }

    /**
     * The operating system the archive has been created on.
     * @see HostOs
     */
    public int getHostOs() {
        return localFileHeader.hostOS;
    }

    /**
     * Is the operating system the archive has been created on one
     * that is considered a UNIX OS by arj?
     */
    public boolean isHostOsUnix() {
        return getHostOs() == HostOs.UNIX || getHostOs() == HostOs.NEXT;
    }

    /**
     * The known values for HostOs.
     */
    public static class HostOs {
        public static final int DOS = 0;
        public static final int PRIMOS = 1;
        public static final int UNIX = 2;
        public static final int AMIGA = 3;
        public static final int MAC_OS = 4;
        public static final int OS_2 = 5;
        public static final int APPLE_GS = 6;
        public static final int ATARI_ST = 7;
        public static final int NEXT = 8;
        public static final int VAX_VMS = 9;
        public static final int WIN95 = 10;
        public static final int WIN32 = 11;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5155.java