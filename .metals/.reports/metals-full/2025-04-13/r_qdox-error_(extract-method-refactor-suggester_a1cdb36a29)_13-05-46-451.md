error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2493.java
text:
```scala
public I@@nstallationModification modifyInstallation(final ModificationCompletionCallback callback) {

package org.jboss.as.patching.installation;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.patching.DirectoryStructure;

/**
 * The installation manager.
 *
 * @author Emanuel Muckenhuber
 */
public class InstallationManagerImpl extends InstallationManager {

    // The basic concurrency contract is copy on write
    private volatile InstalledIdentity installedIdentity;
    // TODO track this state a better way
    private final AtomicBoolean writable = new AtomicBoolean(true);
    private final InstalledImage installedImage;

    /**
     * This field is set to true when a patch is applied/rolled back at runtime.
     * It prevents another patch to be applied and overrides the modifications brought by the previous one
     * unless the process is restarted first
     *
     * This field has to be {@code static} in order to survive server reloads.
     */
    private static final AtomicBoolean restartRequired = new AtomicBoolean(false);

    public InstallationManagerImpl(InstalledIdentity installedIdentity) {
        this.installedIdentity = installedIdentity;
        this.installedImage = installedIdentity.getInstalledImage();
    }

    @Override
    public Identity getIdentity() {
        return installedIdentity.getIdentity();
    }

    @Override
    public List<String> getLayerNames() {
        return installedIdentity.getLayerNames();
    }

    @Override
    public Layer getLayer(String layerName) {
        return installedIdentity.getLayer(layerName);
    }

    @Override
    public List<Layer> getLayers() {
        return installedIdentity.getLayers();
    }

    @Override
    public Collection<String> getAddOnNames() {
        return installedIdentity.getAddOnNames();
    }

    @Override
    public AddOn getAddOn(String addOnName) {
        return installedIdentity.getAddOn(addOnName);
    }

    @Override
    public Collection<AddOn> getAddOns() {
        return installedIdentity.getAddOns();
    }

    @Override
    public InstalledImage getInstalledImage() {
        return installedImage;
    }

    @Override
    public InstallationModification modifyInstallation(final ModificationCompletion callback) {
        if (! writable.compareAndSet(true, false)) {
            // This should be guarded by the OperationContext.lock
            throw new IllegalStateException();
        }
        try {
            // Load the state
            final InstalledIdentity installedIdentity = this.installedIdentity;
            final Identity identity = installedIdentity.getIdentity();
            final PatchableTarget.TargetInfo identityInfo = identity.loadTargetInfo();
            final InstallationModificationImpl.InstallationState state = load(installedIdentity);

            return new InstallationModificationImpl(identityInfo, identity.getName(), identity.getVersion(), state) {

                @Override
                public InstalledIdentity getUnmodifiedInstallationState() {
                    return installedIdentity;
                }

                @Override
                public void complete() {
                    try {
                        // Update the state
                        InstallationManagerImpl.this.installedIdentity = updateState(identity.getName(), this, internalComplete());
                        writable.set(true);
                    } catch (Exception e) {
                        cancel();
                        throw new RuntimeException(e);
                    }
                    if (callback != null) {
                        callback.completed();
                    }
                }

                @Override
                public void cancel() {
                    try {
                        if (callback != null) {
                            callback.canceled();
                        }
                    } finally {
                        writable.set(true);
                    }
                }
            };
        } catch (Exception e) {
            writable.set(true);
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the installation state based on the identity
     *
     * @param installedIdentity the installed identity
     * @return the installation state
     * @throws IOException
     */
    protected static InstallationModificationImpl.InstallationState load(final InstalledIdentity installedIdentity) throws IOException {
        final InstallationModificationImpl.InstallationState state = new InstallationModificationImpl.InstallationState();
        for (final Layer layer : installedIdentity.getLayers()) {
            state.putLayer(layer);
        }
        for (final AddOn addOn : installedIdentity.getAddOns()) {
            state.putAddOn(addOn);
        }
        return state;
    }

    /**
     * Update the installed identity using the modified state from the modification.
     *
     * @param name the identity name
     * @param modification the modification
     * @param state the installation state
     * @return the installed identity
     */
    protected InstalledIdentity updateState(final String name, final InstallationModificationImpl modification, final InstallationModificationImpl.InstallationState state) {
        final PatchableTarget.TargetInfo identityInfo = modification.getModifiedState();
        final Identity identity = new Identity() {
            @Override
            public String getVersion() {
                return modification.getVersion();
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public TargetInfo loadTargetInfo() throws IOException {
                return identityInfo;
            }

            @Override
            public DirectoryStructure getDirectoryStructure() {
                return modification.getDirectoryStructure();
            }
        };

        final InstalledIdentityImpl installedIdentity = new InstalledIdentityImpl(identity, installedImage);
        for (final Map.Entry<String, MutableTargetImpl> entry : state.getLayers().entrySet()) {
            final String layerName = entry.getKey();
            final MutableTargetImpl target = entry.getValue();
            installedIdentity.putLayer(layerName, new LayerInfo(layerName, target.getModifiedState(), target.getDirectoryStructure()));
        }
        for (final Map.Entry<String, MutableTargetImpl> entry : state.getAddOns().entrySet()) {
            final String addOnName = entry.getKey();
            final MutableTargetImpl target = entry.getValue();
            installedIdentity.putAddOn(addOnName, new LayerInfo(addOnName, target.getModifiedState(), target.getDirectoryStructure()));
        }
        return installedIdentity;
    }


    public boolean requiresRestart() {
        return restartRequired.get();
    }

    public boolean restartRequired() {
        return restartRequired.compareAndSet(false, true);
    }

    public void clearRestartRequired() {
        restartRequired.set(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2493.java