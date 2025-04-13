error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9705.java
text:
```scala
L@@OGGER.debugf("BundleContext not available for management operation");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.osgi.management;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.osgi.parser.ModelConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.framework.BundleManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author David Bosschaert
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class OSGiRuntimeResource implements Resource {

    private final InjectedValue<BundleManager> injectedBundleManager;
    private final Resource delegate;

    public OSGiRuntimeResource() {
        this(Resource.Factory.create(), new InjectedValue<BundleManager>());
    }

    private OSGiRuntimeResource(Resource resource, InjectedValue<BundleManager> injectedBundleManager) {
        this.injectedBundleManager = injectedBundleManager;
        this.delegate = resource;
    }

    public InjectedValue<BundleManager> getInjectedBundleManager() {
        return injectedBundleManager;
    }

    @Override
    public ModelNode getModel() {
        return delegate.getModel();
    }

    @Override
    public void writeModel(ModelNode newModel) {
        delegate.writeModel(newModel);
    }

    @Override
    public boolean isModelDefined() {
        return delegate.isModelDefined();
    }

    @Override
    public boolean hasChild(PathElement element) {
        if (ModelConstants.BUNDLE.equals(element.getKey()))
            return hasBundle(element);
        else
            return delegate.hasChild(element);
    }

    @Override
    public Resource getChild(PathElement element) {
        if (ModelConstants.BUNDLE.equals(element.getKey()))
            return hasBundle(element) ? OSGiBundleResource.INSTANCE : null;
        else
            return delegate.getChild(element);
    }

    @Override
    public Resource requireChild(PathElement element) {
        if (ModelConstants.BUNDLE.equals(element.getKey())) {
            if (hasBundle(element)) {
                return OSGiBundleResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else {
            return delegate.requireChild(element);
        }
    }

    @Override
    public boolean hasChildren(String childType) {
        if (ModelConstants.BUNDLE.equals(childType))
            return getChildrenNames(ModelConstants.BUNDLE).size() > 0;
        else
            return delegate.hasChildren(childType);
    }

    @Override
    public Resource navigate(PathAddress address) {
        if (address.size() > 0 && ModelConstants.BUNDLE.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return OSGiBundleResource.INSTANCE;
        } else {
            return delegate.navigate(address);
        }
    }

    @Override
    public Set<String> getChildTypes() {
        Set<String> result = new HashSet<String>(delegate.getChildTypes());
        result.add(ModelConstants.BUNDLE);
        return result;
    }

    @Override
    public Set<String> getChildrenNames(String childType) {
        if (ModelConstants.BUNDLE.equals(childType))
            return getBundleIDs();
        else
            return delegate.getChildrenNames(childType);
    }

    @Override
    public Set<ResourceEntry> getChildren(String childType) {
        if (ModelConstants.BUNDLE.equals(childType)) {
            Set<ResourceEntry> result = new TreeSet<Resource.ResourceEntry>();
            for (String id : getBundleIDs()) {
                result.add(new OSGiBundleResource.OSGiBundleResourceEntry(id));
            }
            return result;
        } else {
            return delegate.getChildren(childType);
        }
    }

    @Override
    public void registerChild(PathElement address, Resource resource) {
        if (ModelConstants.BUNDLE.equals(address.getKey())) {
            throw new UnsupportedOperationException();
        } else {
            delegate.registerChild(address, resource);
        }
    }

    @Override
    public Resource removeChild(PathElement address) {
        if (ModelConstants.BUNDLE.equals(address.getKey())) {
            throw new UnsupportedOperationException();
        } else {
            return delegate.removeChild(address);
        }
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean isProxy() {
        return false;
    }

    @Override
    public Resource clone() {
        return new OSGiRuntimeResource(delegate.clone(), injectedBundleManager);
    }

    private boolean hasBundle(PathElement element) {
        boolean result = false;
        BundleManager bundleManager = injectedBundleManager.getOptionalValue();
        if (bundleManager != null) {
            Bundle bundle;
            try {
                Long bundleId = Long.parseLong(element.getValue());
                bundle = bundleManager.getBundleById(bundleId);
            } catch (NumberFormatException ex) {
                bundle = bundleManager.getBundleByLocation(element.getValue());
            }
            result = (bundle != null);
        }
        return result;
    }

    private Set<String> getBundleIDs() {
        Set<String> result = new TreeSet<String>();
        BundleContext context = getBundleContext();
        if (context != null) {
            for (Bundle b : context.getBundles()) {
                result.add(Long.toString(b.getBundleId()));
            }
        }
        return result;
    }

    private BundleContext getBundleContext() {
        BundleManager bundleManager = injectedBundleManager.getOptionalValue();
        BundleContext context = bundleManager != null ? bundleManager.getSystemBundle().getBundleContext() : null;
        if (context == null) {
            LOGGER.warnBundleContextNotAvailable();
        }
        return context;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9705.java