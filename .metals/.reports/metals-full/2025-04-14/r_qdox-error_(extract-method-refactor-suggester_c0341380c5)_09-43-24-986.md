error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5077.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5077.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5077.java
text:
```scala
i@@f (stripUpTo > 0 && stripUpTo < region.length()) {

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

package org.jboss.as.jpa.hibernate4.management;

import static org.jboss.as.jpa.hibernate4.management.HibernateDescriptionConstants.COLLECTION;
import static org.jboss.as.jpa.hibernate4.management.HibernateDescriptionConstants.ENTITY;
import static org.jboss.as.jpa.hibernate4.management.HibernateDescriptionConstants.ENTITYCACHE;
import static org.jboss.as.jpa.hibernate4.management.HibernateDescriptionConstants.QUERYCACHE;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.stat.Statistics;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.PlaceholderResource;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.jpa.spi.PersistenceUnitService;
import org.jboss.as.jpa.spi.PersistenceUnitServiceRegistry;
import org.jboss.dmr.ModelNode;

/**
 * Resource representing a JPA PersistenceUnit (from a persistence.xml) deployment.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class HibernateStatisticsResource extends PlaceholderResource.PlaceholderResourceEntry {

    private final String puName;
    private final PersistenceUnitServiceRegistry persistenceUnitRegistry;
    private final ModelNode model = new ModelNode();
    private final String providerLabel;

    public HibernateStatisticsResource(final String puName, final PersistenceUnitServiceRegistry persistenceUnitRegistry, final String providerLabel) {
        super(providerLabel, puName);
        this.puName = puName;
        this.persistenceUnitRegistry = persistenceUnitRegistry;
        this.providerLabel = providerLabel;
    }

    @Override
    public ModelNode getModel() {
        return model;
    }

    @Override
    public boolean isModelDefined() {
        return model.isDefined();
    }

    @Override
    public boolean hasChild(PathElement element) {
        if (ENTITYCACHE.equals(element.getKey())) {
            return hasCacheRegion(element);
        } else if (ENTITY.equals(element.getKey())) {
            return hasEntity(element);
        } else if (COLLECTION.equals(element.getKey())) {
            return hasCollection(element);
        } else if (QUERYCACHE.equals(element.getKey())) {
            return hasQuery(element);
        } else {
            return super.hasChild(element);
        }
    }

    @Override
    public Resource getChild(PathElement element) {
        if (ENTITYCACHE.equals(element.getKey())) {
            return hasCacheRegion(element) ? PlaceholderResource.INSTANCE : null;
        } else if (ENTITY.equals(element.getKey())) {
            return hasEntity(element) ? PlaceholderResource.INSTANCE : null;
        } else if (COLLECTION.equals(element.getKey())) {
            return hasCollection(element) ? PlaceholderResource.INSTANCE : null;
        } else if (QUERYCACHE.equals(element.getKey())) {
            return hasQuery(element) ? PlaceholderResource.INSTANCE : null;
        } else {
            return super.getChild(element);
        }
    }

    @Override
    public Resource requireChild(PathElement element) {
        if (ENTITYCACHE.equals(element.getKey())) {
            if (hasCacheRegion(element)) {
                return PlaceholderResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else if (ENTITY.equals(element.getKey())) {
            if (hasEntity(element)) {
                return PlaceholderResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else if (COLLECTION.equals(element.getKey())) {
            if (hasCollection(element)) {
                return PlaceholderResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else if (QUERYCACHE.equals(element.getKey())) {
            if (hasQuery(element)) {
                return PlaceholderResource.INSTANCE;
            }
            throw new NoSuchResourceException(element);
        } else {
            return super.requireChild(element);
        }
    }

    @Override
    public boolean hasChildren(String childType) {
        if (ENTITYCACHE.equals(childType)) {
            return getChildrenNames(ENTITYCACHE).size() > 0;
        } else if (ENTITY.equals(childType)) {
            return getChildrenNames(ENTITY).size() > 0;
        } else if (COLLECTION.equals(childType)) {
            return getChildrenNames(COLLECTION).size() > 0;
        } else if (QUERYCACHE.equals(childType)) {
            return getChildrenNames(QUERYCACHE).size() > 0;
        } else {
            return super.hasChildren(childType);
        }
    }

    @Override
    public Resource navigate(PathAddress address) {
        if (address.size() > 0 && ENTITYCACHE.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return PlaceholderResource.INSTANCE;
        } else if (address.size() > 0 && ENTITY.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return PlaceholderResource.INSTANCE;
        } else if (address.size() > 0 && COLLECTION.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return PlaceholderResource.INSTANCE;
        } else if (address.size() > 0 && QUERYCACHE.equals(address.getElement(0).getKey())) {
            if (address.size() > 1) {
                throw new NoSuchResourceException(address.getElement(1));
            }
            return PlaceholderResource.INSTANCE;
        } else {
            return super.navigate(address);
        }
    }

    @Override
    public Set<String> getChildTypes() {
        Set<String> result = new HashSet<String>(super.getChildTypes());
        result.add(ENTITYCACHE);
        result.add(ENTITY);
        result.add(COLLECTION);
        result.add(QUERYCACHE);
        return result;
    }

    @Override
    public Set<String> getChildrenNames(String childType) {
        if (ENTITYCACHE.equals(childType)) {
            return getCacheRegionNames();
        } else if (ENTITY.equals(childType)) {
            return getEntityNames();
        } else if (COLLECTION.equals(childType)) {
            return getCollectionNames();
        } else if (QUERYCACHE.equals(childType)) {
            return getQueryNames();
        } else {
            return super.getChildrenNames(childType);
        }
    }

    @Override
    public Set<ResourceEntry> getChildren(String childType) {
        if (ENTITYCACHE.equals(childType)) {
            Set<ResourceEntry> result = new HashSet<ResourceEntry>();
            for (String name : getCacheRegionNames()) {
                result.add(new PlaceholderResource.PlaceholderResourceEntry(ENTITYCACHE, name));
            }
            return result;
        } else if (ENTITY.equals(childType)) {
            Set<ResourceEntry> result = new HashSet<ResourceEntry>();
            for (String name : getEntityNames()) {
                result.add(new PlaceholderResource.PlaceholderResourceEntry(ENTITY, name));
            }
            return result;
        } else if (COLLECTION.equals(childType)) {
            Set<ResourceEntry> result = new HashSet<ResourceEntry>();
            for (String name : getCollectionNames()) {
                result.add(new PlaceholderResource.PlaceholderResourceEntry(COLLECTION, name));
            }
            return result;
        } else if (QUERYCACHE.equals(childType)) {
            Set<ResourceEntry> result = new HashSet<ResourceEntry>();
            for (String name : getQueryNames()) {
                result.add(new PlaceholderResource.PlaceholderResourceEntry(QUERYCACHE, name));
            }
            return result;
        } else {
            return super.getChildren(childType);
        }
    }

    @Override
    public void registerChild(PathElement address, Resource resource) {
        if (ENTITYCACHE.equals(address.getKey()) ||
            ENTITY.equals(address.getKey()) ||
            COLLECTION.equals(address.getKey()) ||
            QUERYCACHE.equals(address.getKey())) {
            throw new UnsupportedOperationException(String.format("Resources of type %s cannot be registered", address.getKey()));
        } else {
            super.registerChild(address, resource);
        }
    }

    @Override
    public Resource removeChild(PathElement address) {
        if (ENTITYCACHE.equals(address.getKey()) ||
            ENTITY.equals(address.getKey()) ||
            COLLECTION.equals(address.getKey()) ||
            QUERYCACHE.equals(address.getKey())) {
            throw new UnsupportedOperationException(String.format("Resources of type %s cannot be removed", address.getKey()));
        } else {
            return super.removeChild(address);
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
    public HibernateStatisticsResource clone() {
        return new HibernateStatisticsResource(puName, persistenceUnitRegistry, providerLabel);
    }

    private boolean hasEntity(PathElement element) {
        boolean result = false;
        final Statistics stats = getStatistics();
        if (stats != null) {
            final String entityName = element.getValue();
            result = stats.getEntityStatistics(entityName) != null;
        }
        return result;
    }

    private Set<String> getEntityNames() {
        final Statistics stats = getStatistics();
        if (stats == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            String[] entityNames = stats.getEntityNames();
            if (entityNames != null) {
                for (String entity : entityNames) {
                    result.add(entity);
                }
            }
            return result;
        }
    }

    private boolean hasCacheRegion(PathElement element) {
        boolean result = false;
        final PersistenceUnitService puService = persistenceUnitRegistry.getPersistenceUnitService(puName);
        final Statistics stats = getStatistics();
        if (stats != null && puService != null) {
            final String scopedPUName = puService.getScopedPersistenceUnitName();
            final String unqualifiedRegionName = element.getValue();
            final String qualifiedRegionName = scopedPUName + "." + unqualifiedRegionName;
            result = stats.getSecondLevelCacheStatistics(qualifiedRegionName) != null;
        }
        return result;
    }

    private Set<String> getCacheRegionNames() {
        final Statistics stats = getStatistics();
        if (stats == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            String[] cacheRegionNames = stats.getSecondLevelCacheRegionNames();
            if (cacheRegionNames != null) {
                for (String region : cacheRegionNames) {

                    // example regionName = "jpa_SecondLevelCacheTestCase.jar#mypc.org.jboss.as.test.integration.jpa.hibernate.Employee"
                    // remove the scoped PU name plus one for '.' the separator character added to it.
                    // and replace period with underscore.  Filtered region name will be "org_jboss_as_testsuite_integration_jpa_hibernate_Employee"
                    int stripUpTo = puName.length() + 1;
                    if (stripUpTo > 0) {
                        result.add(region.substring(stripUpTo));
                    }
                    else { // if puName is missing, just use the region as is (AS7-3858)
                        result.add(region);
                    }
                }
            }
            return result;
        }
    }

    private boolean hasQuery(PathElement element) {
        boolean result = false;
        final PersistenceUnitService puService = persistenceUnitRegistry.getPersistenceUnitService(puName);
        final Statistics stats = getStatistics();
        if (stats != null && puService != null) {
            final String scopedPUName = puService.getScopedPersistenceUnitName();
            final String unqualifiedQueryName = element.getValue();
            final String queryName = scopedPUName + "." + unqualifiedQueryName;
            result = stats.getQueryStatistics(queryName) != null;
        }
        return result;
    }


    private Set<String> getQueryNames() {
        final Statistics stats = getStatistics();
        if (stats == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            String[] queries = stats.getQueries();
            if (queries != null) {
                for (String query : queries) {
                    result.add(QueryName.queryName(query).getDisplayName());
                }
            }
            return result;
        }
    }

    private boolean hasCollection(PathElement element) {
        boolean result = false;
        final Statistics stats = getStatistics();
        if (stats != null) {
            final String collectionName = element.getValue();
            result = stats.getCollectionStatistics(collectionName) != null;
        }
        return result;
    }

    private Set<String> getCollectionNames() {
        final Statistics stats = getStatistics();
        if (stats == null) {
            return Collections.emptySet();
        } else {
            Set<String> result = new HashSet<String>();
            String[] collectionNames = stats.getCollectionRoleNames();
            if (collectionNames != null) {
                for (String entity : collectionNames) {
                    result.add(entity);
                }
            }
            return result;
        }
    }


    private Statistics getStatistics() {
        ManagementLookup stats = ManagementLookup.create(persistenceUnitRegistry, puName);
        if (stats != null) {
            return stats.getStatistics();
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5077.java