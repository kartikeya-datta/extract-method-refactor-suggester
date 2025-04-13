error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10224.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10224.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1109
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10224.java
text:
```scala
abstract static class HistoryProcessor implements PatchingValidationErrorHandler {

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

p@@ackage org.jboss.as.patching.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.patching.PatchMessages;
import org.jboss.as.patching.PatchingException;
import org.jboss.as.patching.installation.InstalledIdentity;

/**
 * Utility class to validate the patched state and history.
 *
 * @author Emanuel Muckenhuber
 */
public final class PatchHistoryValidations {

    private PatchHistoryValidations() {
        //
    }

    /**
     * Validate the consistency of patches to the point we rollback.
     *
     * @param patchID the patch id which gets rolled back
     * @param identity the installed identity
     * @throws PatchingException
     */
    public static void validateRollbackState(final String patchID, final InstalledIdentity identity) throws PatchingException {
        final Set<String> validHistory = processRollbackState(patchID, identity);
        if (patchID != null && !validHistory.contains(patchID)) {
            throw PatchMessages.MESSAGES.patchNotFoundInHistory(patchID);
        }
    }

    private static Set<String> processRollbackState(final String patchID, final InstalledIdentity identity) throws PatchingException {
        final Set<String> validHistory = new HashSet<String>();
        final PatchHistoryIterator.Builder builder = PatchHistoryIterator.Builder.create(identity);
        final HistoryProcessor processor = new HistoryProcessor() {

            boolean includeCurrent = true;
            boolean proceed = true;
            boolean found = false;

            @Override
            protected boolean includeCurrent() {
                return includeCurrent;
            }

            @Override
            protected boolean canProceed() {
                return proceed;
            }

            @Override
            protected <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> boolean handleError(PatchingArtifact<P, S> artifact, S state) {
                if (artifact == PatchingArtifacts.HISTORY_DIR
 artifact == PatchingArtifacts.PATCH_XML
 artifact == PatchingArtifacts.ROLLBACK_XML
 artifact == PatchingArtifacts.MISC_BACKUP) {
                    // If parts of the history are is missing we can rollback to this patch, but no further
                    proceed = false;
                    return found;
                } else {
                    includeCurrent = false;
                    proceed = false;
                    return false;
                }
            }

            @Override
            protected void processedPatch(String patch) {
                validHistory.add(patch);
                if (patch.equals(patchID)) {
                    // We found the patch we are rolling back, and continue to the next patch
                    found = true;
                } else if (found) {
                    proceed = false;
                }
            }
        };
        processor.process(builder.iterator());
        return validHistory;
    }


    abstract static class HistoryProcessor implements PatchingArtifactValidationContext {

        private final List<String> errors = new ArrayList<String>();

        /**
         * Whether the current patch is valid.
         *
         * @return {@code true} if the current patch is valid, {@code false} otherwise
         */
        protected abstract boolean includeCurrent();

        /**
         * Whether we can proceed processing the next patch.
         *
         * @return {@code true} if the next patch can be processed, {@code false} otherwise
         */
        protected abstract boolean canProceed();

        /**
         * Handle all errors.
         *
         * @param artifact the processed artifact in error
         * @param state    the artifact state
         * @param <P>
         * @param <S>
         * @param {@code true} if the error can be ignored, {@code false otherwise}
         */
        protected abstract <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> boolean handleError(PatchingArtifact<P, S> artifact, S state);

        /**
         * Callback for valid patches.
         *
         * @param patch the patch id
         */
        protected abstract void processedPatch(String patch);

        @Override
        public <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> void addError(PatchingArtifact<P, S> artifact, S state) {
            if (!handleError(artifact, state)) {
                errors.add(PatchMessages.MESSAGES.artifactInError(state));
            }
        }

        @Override
        public <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> void addInconsistent(PatchingArtifact<P, S> artifact, S current) {
            if (!handleError(artifact, current)) {
                errors.add(PatchMessages.MESSAGES.inconsistentArtifact(current));
            }
        }

        @Override
        public <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> void addMissing(PatchingArtifact<P, S> artifact, S state) {
            if (!handleError(artifact, state)) {
                errors.add(PatchMessages.MESSAGES.missingArtifact(state));
            }
        }

        protected void process(PatchHistoryIterator iterator) throws PatchingException {
             while (iterator.hasNext() && canProceed()) {
                 final String patch = iterator.next(this);
                 if (includeCurrent()) {
                     processedPatch(patch);
                 }
             }
            // If there are errors, fail
            if (!errors.isEmpty()) {
                throw new PatchingException(errors.toString());
            }
        }

    }

    static class PatchingArtifactStateHandlers {

        // A map of artifact state handlers for each artifact. Generics FTW!
        private final Map<PatchingArtifact<? extends PatchingArtifact.ArtifactState, ? extends PatchingArtifact.ArtifactState>, PatchingArtifactStateHandler<? extends PatchingArtifact.ArtifactState>> handlers
                = new HashMap<PatchingArtifact<? extends PatchingArtifact.ArtifactState, ? extends PatchingArtifact.ArtifactState>, PatchingArtifactStateHandler<? extends PatchingArtifact.ArtifactState>>();


        <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> void put(PatchingArtifact<P, S> artifact, PatchingArtifactStateHandler<S> handler) {
            assert !handlers.containsKey(artifact);
            handlers.put(artifact, handler);
        }

        <P extends PatchingArtifact.ArtifactState, S extends PatchingArtifact.ArtifactState> PatchingArtifactStateHandler<S> get(final PatchingArtifact<P, S> artifact) {
            return (PatchingArtifactStateHandler<S>) handlers.get(artifact);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10224.java