error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/257.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/257.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/257.java
text:
```scala
t@@his.patchType = Patch.PatchType.CUMULATIVE;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.as.patching.metadata.Patch;
import org.jboss.as.patching.metadata.PatchBuilder;

/**
 * {@link PatchConfig} implementation.
 *
 * @author Brian Stansberry (c) 2012 Red Hat Inc.
 */
class PatchConfigBuilder {

    public static enum AffectsType {
        UPDATED,
        ORIGINAL,
        BOTH,
        NONE
    }

    private String patchId;
    private String description;
    private String resultingVersion;
    private Patch.PatchType patchType;
    private boolean generateByDiff;
    private List<String> appliesTo = new ArrayList<String>();
    private Set<String> runtimeUseItems = new HashSet<String>();
//    private final Map<DistributionContentItem.Type, Map<ModificationType, SortedSet<DistributionContentItem>>> modifications =
//            new HashMap<DistributionContentItem.Type, Map<ModificationType, SortedSet<DistributionContentItem>>>();

    private DistributionStructure updatedStructure;
    private DistributionStructure appliesToStructure;

    PatchConfigBuilder setPatchId(String patchId) {
        this.patchId = patchId;

        return this;
    }

    PatchConfigBuilder setDescription(String description) {
        this.description = description;

        return this;
    }

    PatchConfigBuilder setCumulativeType(String appliesToVersion, String resultingVersion) {
        this.patchType = Patch.PatchType.UPGRADE;
        this.appliesTo = Collections.singletonList(appliesToVersion);
        this.resultingVersion = resultingVersion;
        this.updatedStructure = DistributionStructure.Factory.create(resultingVersion);
        this.appliesToStructure = DistributionStructure.Factory.create(appliesToVersion);
        return this;
    }

    PatchConfigBuilder setOneOffType(List<String> appliesTo) {
        assert appliesTo != null : "appliesTo is null";
        assert appliesTo.size() > 0 : "appliesTo is empty";

        this.patchType = Patch.PatchType.ONE_OFF;
        this.appliesTo = Collections.unmodifiableList(appliesTo);
        String structureVersion = null;
        for (String version : appliesTo) {
            if (appliesToStructure == null) {
                appliesToStructure = DistributionStructure.Factory.create(version);
                updatedStructure = DistributionStructure.Factory.create(version);
                structureVersion = version;
            } else {
                DistributionStructure structure = DistributionStructure.Factory.create(version);
                if (!appliesToStructure.isCompatibleWith(structure)) {
                    throw new IllegalStateException(DistributionStructure.class.getSimpleName() + " for version " + version
                            + " is incompatible with version " + structureVersion + ". The same patch cannot apply to both versions.");
                }
            }
        }

        return this;
    }

    PatchConfigBuilder setGenerateByDiff(boolean generateByDiff) {
        this.generateByDiff = generateByDiff;
        return this;
    }

    PatchConfigBuilder addRuntimeUseItem(String item) {
        this.runtimeUseItems.add(item);
        return this;
    }

//    PatchConfigBuilder addBundleModification(String name, String slot, String searchPath, ModificationType modificationType) {
//
//        DistributionContentItem item = appliesToStructure.getBundleRootContentItem(name, slot, searchPath);
//        return addModification(item, modificationType);
//    }
//
//    PatchConfigBuilder addModuleModification(String name, String slot, String searchPath, ModificationType modificationType) {
//        DistributionContentItem item = appliesToStructure.getModuleRootContentItem(name, slot, searchPath);
//        return addModification(item, modificationType);
//    }
//
//    PatchConfigBuilder addMiscModification(String path, boolean directory, boolean inRuntimeUse, ModificationType modificationType) {
//        DistributionContentItem item = appliesToStructure.getMiscContentItem(path, directory);
//        addModification(item, modificationType);
//        if (inRuntimeUse) {
//            addRuntimeUseItem(item);
//        }
//        return this;
//    }

    PatchConfigBuilder addModuleSearchPath(final String name, final String standardPath, AffectsType affectsType) {
        switch (affectsType) {
            case ORIGINAL:
                appliesToStructure.registerStandardModuleSearchPath(name, standardPath);
                break;
            case UPDATED:
                updatedStructure.registerStandardModuleSearchPath(name, standardPath);
                break;
            case BOTH:
                appliesToStructure.registerStandardModuleSearchPath(name, standardPath);
                updatedStructure.registerStandardModuleSearchPath(name, standardPath);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }

    PatchConfigBuilder setDefaultModuleSearchPathExclusion(AffectsType affectsType) {
        switch (affectsType) {
            case ORIGINAL:
                appliesToStructure.excludeDefaultModuleRoot();
                break;
            case UPDATED:
                updatedStructure.excludeDefaultModuleRoot();
                break;
            case BOTH:
                appliesToStructure.excludeDefaultModuleRoot();
                updatedStructure.excludeDefaultModuleRoot();
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }

    PatchConfigBuilder setDefaultBundleSearchPathExclusion(AffectsType affectsType) {
        switch (affectsType) {
            case ORIGINAL:
                appliesToStructure.excludeDefaultBundleRoot();
                break;
            case UPDATED:
                updatedStructure.excludeDefaultBundleRoot();
                break;
            case BOTH:
                appliesToStructure.excludeDefaultBundleRoot();
                updatedStructure.excludeDefaultBundleRoot();
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }

    PatchConfigBuilder addBundleSearchPath(final String name, final String standardPath, AffectsType affectsType) {
        switch (affectsType) {
            case ORIGINAL:
                appliesToStructure.registerStandardBundleSearchPath(name, standardPath);
                break;
            case UPDATED:
                updatedStructure.registerStandardBundleSearchPath(name, standardPath);
                break;
            case BOTH:
                appliesToStructure.registerStandardBundleSearchPath(name, standardPath);
                updatedStructure.registerStandardBundleSearchPath(name, standardPath);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }

    PatchConfigBuilder addIgnoredPath(final String path, AffectsType affectsType) {
        switch (affectsType) {
            case ORIGINAL:
                appliesToStructure.registerIgnoredPath(path);
                break;
            case UPDATED:
                updatedStructure.registerIgnoredPath(path);
                break;
            case BOTH:
                appliesToStructure.registerIgnoredPath(path);
                updatedStructure.registerIgnoredPath(path);
                break;
            case NONE:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }

    PatchConfig build() {
        return new PatchConfigImpl();
    }

//    private PatchConfigBuilder addModification(DistributionContentItem item, ModificationType modificationType) {
//        Map<ModificationType, SortedSet<DistributionContentItem>> typeMap;
//        DistributionContentItem.Type itemType = item.getType();
//        switch (itemType) {
//            case MODULE_ROOT:
//            case BUNDLE_ROOT:
//            case MISC:
//                typeMap = modifications.get(itemType);
//                if (typeMap == null) {
//                    typeMap = new HashMap<ModificationType, SortedSet<DistributionContentItem>>();
//                    modifications.put(itemType, typeMap);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException(itemType + " is not a valid content item type for a modification");
//        }
//
//        SortedSet<DistributionContentItem> items = typeMap.get(modificationType);
//        if (items == null) {
//            items = new TreeSet<DistributionContentItem>();
//            typeMap.put(modificationType, items);
//        }
//
//        items.add(item);
//
//        return this;
//    }

    private class PatchConfigImpl implements PatchConfig {

        @Override
        public String getPatchId() {
            return patchId;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Patch.PatchType getPatchType() {
            return patchType;
        }

        @Override
        public String getResultingVersion() {
            return resultingVersion;
        }

        @Override
        public List<String> getAppliesTo() {
            return appliesTo;
        }

        @Override
        public Set<String> getInRuntimeUseItems() {
            return Collections.unmodifiableSet(runtimeUseItems);
        }

        @Override
        public boolean isGenerateByDiff() {
            return generateByDiff;
        }

//        @Override
//        public Map<DistributionContentItem.Type, Map<ModificationType, SortedSet<DistributionContentItem>>> getSpecifiedContent() {
//            return Collections.unmodifiableMap(modifications);
//        }

        @Override
        public DistributionStructure getOriginalDistributionStructure() {
            return appliesToStructure;
        }

        @Override
        public DistributionStructure getUpdatedDistributionStructure() {
            return updatedStructure;
        }

        @Override
        public PatchBuilder toPatchBuilder() {
            PatchBuilder pb = PatchBuilder.create()
                    .setPatchId(getPatchId())
                    .setDescription(getDescription());
            if (patchType == Patch.PatchType.ONE_OFF) {
              //  pb.setOneOffType(appliesTo);
            } else {
              //  pb.setCumulativeType(appliesTo.iterator().next(), resultingVersion);
            }

            return pb;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/257.java