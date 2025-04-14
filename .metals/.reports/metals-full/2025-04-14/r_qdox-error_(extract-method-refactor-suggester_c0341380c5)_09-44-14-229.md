error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13826.java
text:
```scala
protected synchronized v@@oid dieOnCircularReference(Stack<Object> stk, Project p)

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.MergingMapper;

/**
 * Wrapper around a resource collections that maps the names of the
 * other collection using a configured mapper.
 * @since Ant 1.8.0
 */
public class MappedResourceCollection
        extends DataType implements ResourceCollection, Cloneable {

    private ResourceCollection nested = null;
    private Mapper mapper = null;
    private boolean enableMultipleMappings = false;
    private boolean cache = false;
    private Collection<Resource> cachedColl = null;

    /**
     * Adds the required nested ResourceCollection.
     * @param c the ResourceCollection to add.
     * @throws BuildException on error.
     */
    public synchronized void add(ResourceCollection c) throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (nested != null) {
            throw new BuildException("Only one resource collection can be"
                                     + " nested into mappedresources",
                                     getLocation());
        }
        setChecked(false);
        cachedColl = null;
        nested = c;
    }

    /**
     * Define the mapper to map source to destination files.
     * @return a mapper to be configured.
     * @exception BuildException if more than one mapper is defined.
     */
    public Mapper createMapper() throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (mapper != null) {
            throw new BuildException("Cannot define more than one mapper",
                                     getLocation());
        }
        setChecked(false);
        mapper = new Mapper(getProject());
        cachedColl = null;
        return mapper;
    }

    /**
     * Add a nested filenamemapper.
     * @param fileNameMapper the mapper to add.
     * @since Ant 1.6.3
     */
    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    /**
     * Set method of handling mappers that return multiple
     * mappings for a given source path.
     * @param enableMultipleMappings If true the type will
     *        use all the mappings for a given source path, if
     *        false, only the first mapped name is
     *        processed.
     *        By default, this setting is false to provide backward
     *        compatibility with earlier releases.
     * @since Ant 1.8.1
     */
    public void setEnableMultipleMappings(boolean enableMultipleMappings) {
        this.enableMultipleMappings = enableMultipleMappings;
    }

    /**
     * Set whether to cache collections.
     * @since Ant 1.8.1
     */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return ((MappedResourceCollection) getCheckedRef())
                .isFilesystemOnly();
        }
        checkInitialized();
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        if (isReference()) {
            return ((MappedResourceCollection) getCheckedRef()).size();
        }
        checkInitialized();
        return cacheCollection().size();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return ((MappedResourceCollection) getCheckedRef()).iterator();
        }
        checkInitialized();
        return cacheCollection().iterator();
    }

    /**
     * Overrides the base version.
     * @param r the Reference to set.
     */
    public void setRefid(Reference r) {
        if (nested != null || mapper != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Implement clone.  The nested resource collection and mapper are copied.
     * @return a cloned instance.
     */
    public Object clone() {
        try {
            MappedResourceCollection c =
                (MappedResourceCollection) super.clone();
            c.nested = nested;
            c.mapper = mapper;
            c.cachedColl = null;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Overrides the version of DataType to recurse on all DataType
     * child elements that may have been added.
     * @param stk the stack of data types to use (recursively).
     * @param p   the project to use to dereference the references.
     * @throws BuildException on error.
     */
    protected synchronized void dieOnCircularReference(Stack stk, Project p)
        throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
        } else {
            checkInitialized();
            if (mapper != null) {
                pushAndInvokeCircularReferenceCheck(mapper, stk, p);
            }
            if (nested instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) nested, stk, p);
            }
            setChecked(true);
        }
    }

    private void checkInitialized() {
        if (nested == null) {
            throw new BuildException("A nested resource collection element is"
                                     + " required", getLocation());
        }
        dieOnCircularReference();
    }

    private synchronized Collection<Resource> cacheCollection() {
        if (cachedColl == null || !cache) {
            cachedColl = getCollection();
        }
        return cachedColl;
    }

    private Collection<Resource> getCollection() {
        Collection<Resource> collected = new ArrayList<Resource>();
        FileNameMapper m =
            mapper != null ? mapper.getImplementation() : new IdentityMapper();
        for (Resource r : nested) {
            if (enableMultipleMappings) {
                String[] n = m.mapFileName(r.getName());
                if (n != null) {
                    for (int i = 0; i < n.length; i++) {
                        collected.add(new MappedResource(r,
                                                         new MergingMapper(n[i]))
                                      );
                    }
                }
            } else {
                collected.add(new MappedResource(r, m));
            }
        }
        return collected;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13826.java