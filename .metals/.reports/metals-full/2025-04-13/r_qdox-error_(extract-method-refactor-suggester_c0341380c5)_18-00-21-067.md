error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5146.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5146.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5146.java
text:
```scala
public static final A@@ttachmentKey<ModuleConfig> ATTACHMENT_KEY = AttachmentKey.create(ModuleConfig.class);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.deployment.descriptor;

import org.jboss.as.deployment.AttachmentKey;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.vfs.VirtualFile;

import java.io.Serializable;

/**
 * A config object capturing the required information to construct a module.
 *
 * @author John E. Bailey
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ModuleConfig implements Serializable {

    public static final AttachmentKey<ModuleConfig> ATTACHMENT_KEY = new AttachmentKey<ModuleConfig>(ModuleConfig.class);

    private static final long serialVersionUID = 210753378958448029L;

    private final ModuleIdentifier identifier;
    private final Dependency[] dependencies;
    private final ResourceRoot[] resources;

    public ModuleConfig(final ModuleIdentifier identifier, final Dependency[] dependencies, final ResourceRoot[] resources) {
        this.identifier = identifier;
        this.dependencies = dependencies;
        this.resources = resources;
    }

    public ModuleIdentifier getIdentifier() {
        return identifier;
    }

    public Dependency[] getDependencies() {
        return dependencies;
    }

    public ResourceRoot[] getResources() {
        return resources;
    }

    public static final class Dependency implements Serializable {

        private static final long serialVersionUID = 2749276798703740853L;

        private final ModuleIdentifier identifier;
        private final boolean export;
        private final boolean optional;
        private final boolean staticModule;

        // todo - add import/export filtering, etc.

        public Dependency(final ModuleIdentifier identifier, final boolean staticModule, final boolean optional, final boolean export) {
            this.identifier = identifier;
            this.staticModule = staticModule;
            this.optional = optional;
            this.export = export;
        }

        public ModuleIdentifier getIdentifier() {
            return identifier;
        }

        public boolean isOptional() {
            return optional;
        }

        public boolean isExport() {
            return export;
        }

        public boolean isStatic() {
            return staticModule;
        }
    }

    public static final class ResourceRoot implements Serializable {

        private static final long serialVersionUID = 3458831155403388498L;

        private final String rootName;
        private final VirtualFile root;

        public ResourceRoot(final VirtualFile root) {
            this(root.getName(), root);
        }

        public ResourceRoot(final String rootName, final VirtualFile root) {
            this.rootName = rootName;
            this.root = root;
        }

        public String getRootName() {
            return rootName;
        }

        public VirtualFile getRoot() {
            return root;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5146.java