error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15306.java
text:
```scala
private static final S@@tring ROOT_PROPERTY = "org.jboss.model.test.cache.root";

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.model.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.xnio.IoUtils;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ChildFirstClassLoaderBuilder {

    //Use this property on the lightning runs to make sure that people have set the root and cache properties
    private static final String STRICT_PROPERTY = "org.jboss.model.test.cache.strict";
    private static final String ROOT_PROPERTY = "org.jboss.model.test.root";
    private static final String CACHE_FOLDER_PROPERTY = "org.jboss.model.test.classpath.cache";


    private final File cache;
    private final List<URL> classloaderURLs = new ArrayList<URL>();
    private final List<Pattern> parentFirst = new ArrayList<Pattern>();
    private final List<Pattern> childFirst = new ArrayList<Pattern>();


    public ChildFirstClassLoaderBuilder() {
        String root = System.getProperty(ROOT_PROPERTY);
        String cacheFolderName = System.getProperty(CACHE_FOLDER_PROPERTY);
        if (root == null && cacheFolderName == null) {
            if (System.getProperty(STRICT_PROPERTY) != null) {
                throw new IllegalStateException("Please use the " + ROOT_PROPERTY + " and " + CACHE_FOLDER_PROPERTY + " system properties to take advantage of cached classpaths");
            }
            cache = new File("target", "cached-classloader");
            cache.mkdirs();
            if (!cache.exists()) {
                throw new IllegalStateException("Could not create cache file");
            }
            System.out.println("To optimize this test use the " + ROOT_PROPERTY + " and " + CACHE_FOLDER_PROPERTY + " system properties to take advantage of cached classpaths");
        } else if (root != null && cacheFolderName != null){
            if (cacheFolderName.indexOf('/') != -1 && cacheFolderName.indexOf('\\') != -1){
                throw new IllegalStateException("Please use either '/' or '\\' as a file separator");
            }
            File file = new File(".").getAbsoluteFile();
            while (file != null && !file.getName().equals(root)) {
                file = file.getParentFile();
            }
            if (file == null) {
                throw new IllegalStateException("Could not find a parent file called '" + root + "'");
            }
            String separator = cacheFolderName.indexOf("/") != -1 ? "/" : "\\";
            for (String part : cacheFolderName.split(separator)) {
                file = new File(file, part);
                if (file.exists()) {
                    if (!file.isDirectory()) {
                        throw new IllegalStateException(file.getAbsolutePath() + " is not a directory");
                    }
                } else {
                    if (!file.mkdir()) {
                        if (!file.exists()) {
                            throw new IllegalStateException(file.getAbsolutePath() + " could not be created");
                        }
                    }
                }
            }
            cache = file;
        } else {
            throw new IllegalStateException("You must either set both " + ROOT_PROPERTY + " and " + CACHE_FOLDER_PROPERTY + ", or none of them");
        }
    }

    public ChildFirstClassLoaderBuilder addURL(URL url) {
        classloaderURLs.add(url);
        return this;
    }

    public ChildFirstClassLoaderBuilder addSimpleResourceURL(String resource) throws MalformedURLException {
        URL url = ChildFirstClassLoader.class.getResource(resource);
        if (url == null) {
            ClassLoader cl = ChildFirstClassLoader.class.getClassLoader();
            if (cl == null) {
                cl = ClassLoader.getSystemClassLoader();
            }
            url = cl.getResource(resource);
            if (url == null) {
                File file = new File(resource);
                if (file.exists()) {
                    url = file.toURI().toURL();
                }
            }
        }
        if (url == null) {
            throw new IllegalArgumentException("Could not find resource " + resource);
        }
        classloaderURLs.add(url);
        return this;
    }

    public ChildFirstClassLoaderBuilder addMavenResourceURL(String artifactGav) throws IOException, ClassNotFoundException {
        final String name = "maven-" + escape(artifactGav);
        final File file = new File(cache, name);
        if (file.exists()) {
            System.out.println("Using cached maven url for " + artifactGav + " from " + file.getAbsolutePath());
            final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                classloaderURLs.add((URL)in.readObject());
            } catch (Exception e) {
                System.out.println("Error loading cached maven url for " + artifactGav + " from " + file.getAbsolutePath());
                throw e;
            } finally {
                IoUtils.safeClose(in);
            }
        } else {
            System.out.println("No cached maven url for " + artifactGav + " found. " + file.getAbsolutePath() + " does not exist.");
            final URL url = MavenUtil.createMavenGavURL(artifactGav);
            classloaderURLs.add(url);
            final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            try {
                out.writeObject(url);
            } catch (Exception e) {
                System.out.println("Error writing cached maven url for " + artifactGav + " to " + file.getAbsolutePath());
                throw e;
            } finally {
                IoUtils.safeClose(out);
            }
        }
        return this;
    }

    public ChildFirstClassLoaderBuilder addRecursiveMavenResourceURL(String artifactGav)
            throws DependencyCollectionException, DependencyResolutionException, IOException, ClassNotFoundException {
        final String name = "maven-recursive-" + escape(artifactGav);
        final File file = new File(cache, name);
        if (file.exists()) {
            System.out.println("Using cached recursive maven urls for " + artifactGav + " from " + file.getAbsolutePath());
            final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                classloaderURLs.addAll((List<URL>)in.readObject());
            } catch (Exception e) {
                System.out.println("Error loading cached recursive maven urls for " + artifactGav + " from " + file.getAbsolutePath());
                throw e;
            } finally {
                IoUtils.safeClose(in);
            }
        } else {
            System.out.println("No cached recursive maven urls for " + artifactGav + " found. " + file.getAbsolutePath() + " does not exist.");
            final List<URL> urls = MavenUtil.createMavenGavRecursiveURLs(artifactGav);
            classloaderURLs.addAll(urls);
            final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            try {
                out.writeObject(urls);
            } catch (Exception e) {
                System.out.println("Error writing cached recursive maven urls for " + artifactGav + " to " + file.getAbsolutePath());
                throw e;
            } finally {
                IoUtils.safeClose(out);
            }
        }

        return this;
    }

    public ChildFirstClassLoaderBuilder addParentFirstClassPattern(String pattern) {
        parentFirst.add(compilePattern(pattern));
        return this;
    }

    public ChildFirstClassLoaderBuilder addChildFirstClassPattern(String pattern) {
        childFirst.add(compilePattern(pattern));
        return this;
    }

    private String escape(String artifactGav) {
        return artifactGav.replaceAll(":", "-x-");
    }

    public ClassLoader build() {
        ClassLoader parent = this.getClass().getClassLoader() != null ? this.getClass().getClassLoader() : null;
        return new ChildFirstClassLoader(parent, parentFirst, childFirst, classloaderURLs.toArray(new URL[classloaderURLs.size()]));
    }

    private Pattern compilePattern(String pattern) {
        return Pattern.compile(pattern.replace(".", "\\.").replace("*", ".*"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15306.java