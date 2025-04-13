error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13676.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13676.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13676.java
text:
```scala
I@@nstallationState internalComplete() throws Exception {

package org.jboss.as.patching.installation;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.patching.PatchLogger;
import org.jboss.as.patching.metadata.IdentityPatch;

/**
 * @author Emanuel Muckenhuber
 */
abstract class InstallationModificationImpl extends MutableTargetImpl implements InstallationManager.InstallationModification {

    private final String version;
    private final InstallationState installationState;
    private final AtomicBoolean done = new AtomicBoolean();

    protected InstallationModificationImpl(final PatchableTarget.TargetInfo identity,
                                           final String version, final InstallationState installationState) {
        super(identity);
        this.version = version;
        this.installationState = installationState;
    }

    @Override
    public InstallationManager.MutablePatchingTarget resolve(String name, IdentityPatch.LayerType type) {
        if (type == IdentityPatch.LayerType.Layer) {
            return installationState.layers.get(name);
        } else {
            return installationState.addOns.get(name);
        }
    }

    @Override
    public String getVersion() {
        return version;
    }

    InstallationState internalCommit() throws Exception {
        try {
            installationState.persist();
        } catch (Exception e) {
            installationState.restore();
            throw e;
        }
        try {
            super.persist();
        } catch (Exception e) {
            installationState.restore();
        }
        return installationState;
    }

    static class InstallationState {

        private final Map<String, MutableTargetImpl> layers = new LinkedHashMap<String, MutableTargetImpl>();
        private final Map<String, MutableTargetImpl> addOns = new LinkedHashMap<String, MutableTargetImpl>();

        protected void putLayer(final Layer layer) throws IOException {
            putPatchableTarget(layer.getName(), layer, layers);
        }

        protected void putAddOn(final AddOn addOn) throws IOException {
            putPatchableTarget(addOn.getName(), addOn, addOns);
        }

        protected void putPatchableTarget(final String name, final PatchableTarget target, Map<String, MutableTargetImpl> map) throws IOException {
            final PatchableTarget.TargetInfo info = target.loadTargetInfo();
            map.put(name, new MutableTargetImpl(info));
        }

        Map<String, MutableTargetImpl> getLayers() {
            return layers;
        }

        Map<String, MutableTargetImpl> getAddOns() {
            return addOns;
        }

        protected void persist() throws IOException {
            for (final MutableTargetImpl target : layers.values()) {
                target.persist();
            }
            for (final MutableTargetImpl target : addOns.values()) {
                target.persist();
            }
        }

        private void restore() {
            for (final MutableTargetImpl target : layers.values()) {
                try {
                    target.restore();
                } catch (IOException e) {
                    PatchLogger.ROOT_LOGGER.debugf(e, "failed to restore original state for layer %s", target);
                }
            }
            for (final MutableTargetImpl target : addOns.values()) {
                try {
                    target.restore();
                } catch (IOException e) {
                    PatchLogger.ROOT_LOGGER.debugf(e, "failed to restore original state for add-on %s", target);
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13676.java