error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3035.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3035.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3035.java
text:
```scala
r@@eturn load(root.getAbsoluteFile(), modulePath);

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

package org.jboss.as.boot;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emanuel Muckenhuber
 */
class ModulePathFactory {

    static File[] resolveModulePath(final File[] modulePath) {
        final String jbossHome = System.getProperty("jboss.home", System.getenv("JBOSS_HOME"));
        final File root = jbossHome == null ? new File(".") : new File(jbossHome); // TODO
        try {
            return load(root, modulePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static File[] load(final File jbossHome, final File[] modulePath) throws IOException {
        final DirectoryStructure structure = DirectoryStructure.createDefault(jbossHome);
        if(structure.getCumulativeLink().exists()) {
            final String ref = readRef(structure.getCumulativeLink());
            final List<String> patches = readRefs(structure.getCumulativeRefs(ref));
            final List<File> path = new ArrayList<File>(patches.size() + modulePath.length + 1);
            for(final String patch : patches) {
                // one-off patches
                path.add(structure.getModulePatchDirectory(patch));
            }
            // CP
            path.add(structure.getModulePatchDirectory(ref));
            // User defined module path
            for(final File file : modulePath) {
                path.add(file);
            }
            return path.toArray(new File[path.size()]);
        } else {
            return modulePath;
        }
    }

    static String readRef(final File file) throws IOException {
        final InputStream is = new FileInputStream(file);
        try {
            return readRef(is);
        } finally {
            safeClose(is);
        }
    }

    static List<String> readRefs(final File file) throws IOException {
        if(! file.exists()) {
            return Collections.emptyList();
        }
        final InputStream is = new FileInputStream(file);
        try {
            return readRefs(is);
        } finally {
            safeClose(is);
        }
    }

    static String readRef(final InputStream is) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        readLine(is, buffer);
        return buffer.toString();
    }

    static List<String> readRefs(final InputStream is) throws IOException {
        final List<String> refs = new ArrayList<String>();
        final StringBuffer buffer = new StringBuffer();
        do {
            if(buffer.length() > 0) {
                final String ref = buffer.toString().trim();
                if(ref.length() > 0) {
                    refs.add(ref);
                }
            }
        } while(readLine(is, buffer));
        return refs;
    }

    static boolean readLine(InputStream is, StringBuffer buffer) throws IOException {
        buffer.setLength(0);
        int c;
        for(;;) {
            c = is.read();
            switch(c) {
                case '\t':
                case '\r':
                    break;
                case -1: return false;
                case '\n': return true;
                default: buffer.append((char) c);
            }
        }
    }

    static void safeClose(final Closeable closeable) {
        if(closeable != null) try {
            closeable.close();
        } catch (IOException e) {
            //
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3035.java