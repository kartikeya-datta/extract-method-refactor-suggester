error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12650.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12650.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12650.java
text:
```scala
r@@esult = new DistributionItemImpl(result, s[i], IoUtils.NO_CONTENT, IoUtils.NO_CONTENT, !dir);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.patching.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.as.patching.IoUtils;

/**
 * @author Brian Stansberry
 * @author Emanuel Muckenhuber
 */
class DistributionStructureImpl implements DistributionStructure {

    private static final String PATH_DELIMITER = "/";
    private final DistributionContentItem ROOT;
    private final DistributionContentItem MODULES;
    private final DistributionContentItem BUNDLES;

    private final Set<DistributionContentItem> moduleSearchPath = new TreeSet<DistributionContentItem>();
    private final Set<DistributionContentItem> bundleSearchPath = new TreeSet<DistributionContentItem>();
    private final List<DistributionContentItem.Filter> ignored = new ArrayList<DistributionContentItem.Filter>();

    protected DistributionStructureImpl(final DistributionContentItem root) {

        ROOT = root;
        MODULES = createMiscItem(root, "modules");
        BUNDLES = createMiscItem(root, "bundles");
        moduleSearchPath.add(MODULES);
        bundleSearchPath.add(BUNDLES);

        // Ignore the identity owned files, they should never be changed by diff
        registerIgnoredPath("bin/product.conf");
        registerIgnoredPath("modules/layers.conf");
        registerIgnoredPath("bundles/layers.conf");

        // Ignore configuration and runtime locations
        registerIgnoredPath("appclient/configuration**");
        registerIgnoredPath("appclient/data**");
        registerIgnoredPath("appclient/log**");
        registerIgnoredPath("appclient/tmp**");
        registerIgnoredPath("domain/configuration**");
        registerIgnoredPath("domain/data**");
        registerIgnoredPath("domain/log**");
        registerIgnoredPath("domain/servers**");
        registerIgnoredPath("domain/tmp**");
        registerIgnoredPath("standalone/configuration**");
        registerIgnoredPath("standalone/data**");
        registerIgnoredPath("standalone/log**");
        registerIgnoredPath("standalone/tmp**");
    }

    @Override
    public void registerStandardModuleSearchPath(String name, String standardPath) {
        moduleSearchPath.add(createMiscItem(ROOT, standardPath));
    }

    @Override
    public void excludeDefaultModuleRoot() {
        moduleSearchPath.remove(MODULES);
    }

    @Override
    public void registerStandardBundleSearchPath(String name, String standardPath) {
        bundleSearchPath.add(createMiscItem(ROOT, standardPath));
    }

    @Override
    public void excludeDefaultBundleRoot() {
        bundleSearchPath.remove(BUNDLES);
    }

    @Override
    public void registerIgnoredPath(String path) {
        final DistributionContentItem.Filter filter = new DistributionContentItem.GlobPathFilter(path);
        ignored.add(filter);
    }

    @Override
    public boolean isModuleLookupPath(DistributionContentItem item) {
        return moduleSearchPath.contains(item);
    }

    @Override
    public boolean isBundleLookupPath(DistributionContentItem item) {
        return bundleSearchPath.contains(item);
    }

    @Override
    public boolean isIgnored(final DistributionContentItem item) {
        for (final DistributionContentItem.Filter filter : ignored) {
            if (filter.accept(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCompatibleWith(DistributionStructure other) {
        return (getClass().equals(other.getClass()));
    }

    static DistributionContentItem createMiscItem(final DistributionContentItem parent, final String path) {
        DistributionContentItem result = parent;
        final String[] s = path.split(PATH_DELIMITER);
        final int length = s.length;
        for (int i = 0; i < length; i++) {
            boolean dir = i < length - 1;
            result = new DistributionItemImpl(parent, s[i], IoUtils.NO_CONTENT, IoUtils.NO_CONTENT, !dir);
        }
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12650.java