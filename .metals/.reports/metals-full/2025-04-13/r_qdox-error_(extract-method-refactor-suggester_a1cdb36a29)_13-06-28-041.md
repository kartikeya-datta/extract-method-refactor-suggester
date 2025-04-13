error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 854
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2990.java
text:
```scala
public final class ExtensionUtil {

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
p@@ackage org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * A set of useful methods relating to extensions.
 *
 */
public class ExtensionUtil {
    /**
     * Class is not meant to be instantiated.
     */
    private ExtensionUtil() {
        //all methods static
    }

    /**
     * Convert a list of extensionAdapter objects to extensions.
     *
     * @param adapters the list of ExtensionAdapterss to add to convert
     * @throws BuildException if an error occurs
     */
    static ArrayList toExtensions(final List adapters)
        throws BuildException {
        final ArrayList results = new ArrayList();

        final int size = adapters.size();
        for (int i = 0; i < size; i++) {
            final ExtensionAdapter adapter =
                (ExtensionAdapter) adapters.get(i);
            final Extension extension = adapter.toExtension();
            results.add(extension);
        }

        return results;
    }

    /**
     * Generate a list of extensions from a specified fileset.
     *
     * @param libraries the list to add extensions to
     * @param fileset the filesets containing librarys
     * @throws BuildException if an error occurs
     */
    static void extractExtensions(final Project project,
                                   final List libraries,
                                   final List fileset)
        throws BuildException {
        if (!fileset.isEmpty()) {
            final Extension[] extensions = getExtensions(project,
                                                          fileset);
            for (int i = 0; i < extensions.length; i++) {
                libraries.add(extensions[ i ]);
            }
        }
    }

    /**
     * Retrieve extensions from the specified libraries.
     *
     * @param libraries the filesets for libraries
     * @return the extensions contained in libraries
     * @throws BuildException if failing to scan libraries
     */
    private static Extension[] getExtensions(final Project project,
                                              final List libraries)
        throws BuildException {
        final ArrayList extensions = new ArrayList();
        final Iterator iterator = libraries.iterator();
        while (iterator.hasNext()) {
            final FileSet fileSet = (FileSet) iterator.next();

            boolean includeImpl = true;
            boolean includeURL = true;

            if (fileSet instanceof LibFileSet) {
                LibFileSet libFileSet = (LibFileSet) fileSet;
                includeImpl = libFileSet.isIncludeImpl();
                includeURL = libFileSet.isIncludeURL();
            }

            final DirectoryScanner scanner = fileSet.getDirectoryScanner(project);
            final File basedir = scanner.getBasedir();
            final String[] files = scanner.getIncludedFiles();
            for (int i = 0; i < files.length; i++) {
                final File file = new File(basedir, files[ i ]);
                loadExtensions(file, extensions, includeImpl, includeURL);
            }
        }
        return (Extension[]) extensions.toArray(new Extension[extensions.size()]);
    }

    /**
     * Load list of available extensions from specified file.
     *
     * @param file the file
     * @param extensionList the list to add available extensions to
     * @throws BuildException if there is an error
     */
    private static void loadExtensions(final File file,
                                        final List extensionList,
                                        final boolean includeImpl,
                                        final boolean includeURL)
        throws BuildException {
        try {
            final JarFile jarFile = new JarFile(file);
            final Extension[] extensions =
                Extension.getAvailable(jarFile.getManifest());
            for (int i = 0; i < extensions.length; i++) {
                final Extension extension = extensions[ i ];
                addExtension(extensionList, extension, includeImpl, includeURL);
            }
        } catch (final Exception e) {
            throw new BuildException(e.getMessage(), e);
        }
    }

    /**
     * Add extension to list.
     * If extension should not have implementation details but
     * does strip them. If extension should not have url but does
     * then strip it.
     *
     * @param extensionList the list of extensions to add to
     * @param originalExtension the extension
     * @param includeImpl false to exclude implementation details
     * @param includeURL false to exclude implementation URL
     */
    private static void addExtension(final List extensionList,
                                      final Extension originalExtension,
                                      final boolean includeImpl,
                                      final boolean includeURL) {
        Extension extension = originalExtension;
        if (!includeURL
            && null != extension.getImplementationURL()) {
            extension =
                new Extension(extension.getExtensionName(),
                               extension.getSpecificationVersion().toString(),
                               extension.getSpecificationVendor(),
                               extension.getImplementationVersion().toString(),
                               extension.getImplementationVendor(),
                               extension.getImplementationVendorID(),
                               null);
        }

        final boolean hasImplAttributes =
            null != extension.getImplementationURL()
 null != extension.getImplementationVersion()
 null != extension.getImplementationVendorID()
 null != extension.getImplementationVendor();

        if (!includeImpl && hasImplAttributes) {
            extension =
                new Extension(extension.getExtensionName(),
                               extension.getSpecificationVersion().toString(),
                               extension.getSpecificationVendor(),
                               null,
                               null,
                               null,
                               extension.getImplementationURL());
        }

        extensionList.add(extension);
    }

    /**
     * Retrieve manifest for specified file.
     *
     * @param file the file
     * @return the manifest
     * @throws BuildException if errror occurs (file doesn't exist,
     *         file not a jar, manifest doesn't exist in file)
     */
    static Manifest getManifest(final File file)
        throws BuildException {
        try {
            final JarFile jarFile = new JarFile(file);
            Manifest m = jarFile.getManifest();
            if (m == null) {
                throw new BuildException(file + " doesn't have a MANIFEST");
            }
            return m;
        } catch (final IOException ioe) {
            throw new BuildException(ioe.getMessage(), ioe);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2990.java