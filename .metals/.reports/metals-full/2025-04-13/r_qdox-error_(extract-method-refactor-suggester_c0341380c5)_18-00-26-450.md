error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14519.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14519.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14519.java
text:
```scala
final I@@nstalledIdentity identity = processor.getValidationContext().getInstalledIdentity();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

package org.jboss.as.patching.validation;

import java.util.List;

import org.jboss.as.patching.Constants;
import org.jboss.as.patching.DirectoryStructure;
import org.jboss.as.patching.installation.InstalledIdentity;
import org.jboss.as.patching.installation.PatchableTarget;
import org.jboss.as.patching.metadata.ContentModification;
import org.jboss.as.patching.metadata.ContentType;
import org.jboss.as.patching.metadata.ModificationType;
import org.jboss.as.patching.metadata.Patch;
import org.jboss.as.patching.metadata.PatchElement;
import org.jboss.as.patching.metadata.PatchElementProvider;

/**
 * Artifact validating the state of a {@code PatchableTarget}.
 *
 * @author Emanuel Muckenhuber
 */
class PatchableTargetsArtifact extends AbstractArtifact<PatchingXmlArtifact.XmlArtifactState<Patch>, PatchableTargetsArtifact.PatchableTargetState> {

    PatchableTargetsArtifact(PatchingArtifact<PatchableTargetState, ? extends ArtifactState>... artifacts) {
        super(artifacts);
    }

    @Override
    public boolean process(PatchingXmlArtifact.XmlArtifactState<Patch> parent, PatchingArtifactProcessor processor) {
        final InstalledIdentity identity = processor.getValidationContext().getCurrentPatchIdentity();
        final Patch patch = parent.getPatch();
        if (Constants.BASE.equals(patch.getPatchId())) {
            return true;
        }
        final List<PatchElement> elements = patch.getElements();
        boolean valid = true;
        if (elements != null && !elements.isEmpty()) {
            for (final PatchElement element : elements) {
                final String patchID = element.getId();
                final PatchElementProvider provider = element.getProvider();
                final String layerName = provider.getName();
                final PatchableTarget target = provider.isAddOn() ? identity.getAddOn(layerName) : identity.getLayer(layerName);
                boolean modules = false;
                boolean bundles = false;
                for (final ContentModification modification : element.getModifications()) {
                    if (modules && bundles) {
                        break;
                    }
                    if (modification.getItem().getContentType() == ContentType.BUNDLE) {
                        bundles = true;
                    } else if (modification.getItem().getContentType() == ContentType.MODULE) {
                        modules = true;
                    }
                }
                final PatchableTargetState state = new PatchableTargetState(patchID, layerName, target, bundles, modules);
                if (!processor.process(PatchingArtifacts.LAYER, state)) {
                    valid = false;
                }
            }
        }

        // In case there are misc content modifications also validate that misc backup exists
        for (final ContentModification modification : patch.getModifications()) {
            if (modification.getType() != ModificationType.ADD && modification.getItem().getContentType() == ContentType.MISC) {
                final PatchingHistoryDirArtifact.DirectoryArtifactState history = processor.getParentArtifact(PatchingArtifacts.HISTORY_DIR);
                PatchingArtifacts.MISC_BACKUP.process(history, processor);
                break;
            }
        }
        return valid;
    }

    static class PatchableTargetState implements PatchingArtifact.ArtifactState {

        private final String patchID;
        private final String layerName;
        private final PatchableTarget target;
        private final boolean checkBundles;
        private final boolean checkModules;

        PatchableTargetState(String patchID, String layerName, PatchableTarget target, boolean checkBundles, boolean checkModules) {
            this.patchID = patchID;
            this.layerName = layerName;
            this.target = target;
            this.checkBundles = checkBundles;
            this.checkModules = checkModules;
        }

        public String getPatchID() {
            return patchID;
        }

        public DirectoryStructure getStructure() {
            return target.getDirectoryStructure();
        }

        public boolean isCheckBundles() {
            return checkBundles;
        }

        public boolean isCheckModules() {
            return checkModules;
        }

        @Override
        public boolean isValid(PatchingArtifactValidationContext context) {
            if (target == null) {
                context.getErrorHandler().addMissing(PatchingArtifacts.PATCH_CONTENTS, this);
            }
            return true;
        }

        @Override
        public String toString() {
            return layerName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14519.java