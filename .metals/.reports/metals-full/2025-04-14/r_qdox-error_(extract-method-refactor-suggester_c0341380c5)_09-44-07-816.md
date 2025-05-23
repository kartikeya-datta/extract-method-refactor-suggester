error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5034.java
text:
```scala
P@@ath p = getClasspath().concatSystemClasspath("ignore");

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
package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;

import java.io.InputStream;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * A Resource representation of anything that is accessed via a Java classloader.
 * The core methods to set/resolve the classpath are provided.
 * @since Ant 1.8.0
 *
 */

public abstract class AbstractClasspathResource extends Resource {
    private Path classpath;
    private Reference loader;
    private boolean parentFirst = true;

    /**
     * Set the classpath to use when looking up a resource.
     * @param classpath to add to any existing classpath
     */
    public void setClasspath(Path classpath) {
        checkAttributesAllowed();
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
        setChecked(false);
    }

    /**
     * Add a classpath to use when looking up a resource.
     * @return The classpath to be configured
     */
    public Path createClasspath() {
        checkChildrenAllowed();
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        setChecked(false);
        return classpath.createPath();
    }

    /**
     * Set the classpath to use when looking up a resource,
     * given as reference to a &lt;path&gt; defined elsewhere
     * @param r The reference value
     */
    public void setClasspathRef(Reference r) {
        checkAttributesAllowed();
        createClasspath().setRefid(r);
    }

    /**
     * get the classpath used by this <code>LoadProperties</code>.
     * @return The classpath
     */
    public Path getClasspath() {
        if (isReference()) {
            return ((AbstractClasspathResource) getCheckedRef()).getClasspath();
        }
        dieOnCircularReference();
        return classpath;
    }

    /**
     * Get the loader.
     * @return the loader.
     */
    public Reference getLoader() {
        if (isReference()) {
            return ((AbstractClasspathResource) getCheckedRef()).getLoader();
        }
        dieOnCircularReference();
        return loader;
    }

    /**
     * Use the reference to locate the loader. If the loader is not
     * found, taskdef will use the specified classpath and register it
     * with the specified name.
     *
     * This allow multiple taskdef/typedef to use the same class loader,
     * so they can be used together. It eliminate the need to
     * put them in the CLASSPATH.
     *
     * @param r the reference to locate the loader.
     */
    public void setLoaderRef(Reference r) {
        checkAttributesAllowed();
        loader = r;
    }

    /**
     * Whether to consult the parent classloader first.
     *
     * <p>Only relevant if a classpath has been specified.</p>
     *
     * @since Ant 1.8.0
     */
    public void setParentFirst(boolean b) {
        parentFirst = b;
    }

    /**
     * Overrides the super version.
     * @param r the Reference to set.
     */
    public void setRefid(Reference r) {
        if (loader != null || classpath != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Learn whether this resource exists. This implementation opens the input stream
     * as the test.
     * @return true if this resource exists.
     */
    public boolean isExists() {
        if (isReference()) {
            return  ((Resource) getCheckedRef()).isExists();
        }
        dieOnCircularReference();
        InputStream is = null;
        try {
            is = getInputStream();
            return is != null;
        } catch (IOException ex) {
            return false;
        } finally {
            FileUtils.close(is);
        }
    }

    /**
     * Return an InputStream for reading the contents of this Resource.
     * @return an InputStream object.
     * @throws IOException if an error occurs.
     */
    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getInputStream();
        }
        dieOnCircularReference();
        ClassLoader cl = null;
        if (loader != null) {
            cl = (ClassLoader) loader.getReferencedObject();
        }
        if (cl == null) {
            if (getClasspath() != null) {
                Path p = getClasspath().concatSystemClasspath();
                if (parentFirst) {
                    cl = getProject().createClassLoader(p);
                } else {
                    cl = AntClassLoader.newAntClassLoader(getProject()
                                                          .getCoreLoader(),
                                                          getProject(),
                                                          p, false);
                }
            } else {
                cl = JavaResource.class.getClassLoader();
            }
            if (loader != null && cl != null) {
                getProject().addReference(loader.getRefId(), cl);
            }
        }

        return openInputStream(cl);
    }

    /**
     * open the inpout stream from a specific classloader
     * @param cl the classloader to use. Will be null if the system classloader is used
     * @return an open input stream for the resource
     * @throws IOException if an error occurs.
     */
    protected abstract InputStream openInputStream(ClassLoader cl) throws IOException;

    protected synchronized void dieOnCircularReference(Stack stk, Project p) {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
        } else {
            if (classpath != null) {
                pushAndInvokeCircularReferenceCheck(classpath, stk, p);
            }
            setChecked(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5034.java