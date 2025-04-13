error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1427.java
text:
```scala
t@@hrow new IllegalArgumentException("Installation manager service controller is null"); // internal wrong usage, no i18n

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

package org.jboss.as.patching.management;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.as.controller.registry.AbstractModelResource;
import org.jboss.as.controller.registry.PlaceholderResource;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.registry.ResourceProvider;
import org.jboss.as.patching.installation.InstallationManager;
import org.jboss.as.patching.installation.InstalledIdentity;
import org.jboss.as.patching.installation.PatchableTarget;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

/**
 * @author Alexey Loubyansky
 */
class PatchResource extends AbstractModelResource {


    /**
     * The local model.
     */
    private final ModelNode model = new ModelNode();

    protected PatchResource(ServiceController<InstallationManager> imController) {
        super.registerResourceProvider("layer", new LayerResourceProvider(imController));
        super.registerResourceProvider("addon", new AddOnResourceProvider(imController));
    }

    @Override
    public ModelNode getModel() {
        return model;
    }

    @Override
    public void writeModel(ModelNode newModel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isModelDefined() {
        return model.isDefined();
    }

    @Override
    public boolean isRuntime() {
        return true;
    }

    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    @Override
    public Resource clone() {
        return this;
    }

    class LayerResourceProvider extends ElementProviderResourceProvider {

        LayerResourceProvider(ServiceController<InstallationManager> imController) {
            super(imController);
        }

        @Override
        protected Collection<? extends PatchableTarget> getChildTargets(InstalledIdentity identity) {
            return identity.getLayers();
        }

    }

    class AddOnResourceProvider extends ElementProviderResourceProvider {

        AddOnResourceProvider(ServiceController<InstallationManager> imController) {
            super(imController);
        }

        @Override
        protected Collection<? extends PatchableTarget> getChildTargets(InstalledIdentity identity) {
            return identity.getAddOns();
        }

    }

    abstract class ElementProviderResourceProvider implements ResourceProvider {

        protected final ServiceController<InstallationManager> imController;

        ElementProviderResourceProvider(ServiceController<InstallationManager> imController) {
            if (imController == null) {
                throw new IllegalArgumentException("Installation manager service controller is null");
            }
            this.imController = imController;
        }

        protected abstract Collection<? extends PatchableTarget> getChildTargets(InstalledIdentity identity);

        @Override
        public boolean has(String name) {
            return children().contains(name);
        }

        @Override
        public Resource get(String name) {
            return PlaceholderResource.INSTANCE;
        }

        @Override
        public boolean hasChildren() {
            return !children().isEmpty();
        }

        @Override
        public Set<String> children() {
            final InstallationManager manager = imController.getValue();
            final Collection<? extends PatchableTarget> targets = getChildTargets(manager);
            if (targets.isEmpty()) {
                return Collections.emptySet();
            }
            if (targets.size() == 1) {
                final PatchableTarget target = targets.iterator().next();
                return Collections.singleton(target.getName());
            }
            final Set<String> names = new HashSet<String>(targets.size());
            for (PatchableTarget target : targets) {
                names.add(target.getName());
            }
            return names;
        }

        @Override
        public void register(String name, Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Resource remove(String name) {
            throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1427.java