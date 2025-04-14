error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8996.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8996.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8996.java
text:
```scala
P@@atchLogger.ROOT_LOGGER.cannotDeleteFile(workDir.getAbsolutePath());

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

package org.jboss.as.patching.runner;

import static org.jboss.as.patching.IoUtils.safeClose;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.jboss.as.patching.IoUtils;
import org.jboss.as.patching.PatchInfo;
import org.jboss.as.patching.PatchLogger;
import org.jboss.as.patching.PatchingException;
import org.jboss.as.patching.ZipUtils;
import org.jboss.as.patching.installation.Identity;
import org.jboss.as.patching.installation.InstallationManager;
import org.jboss.as.patching.installation.PatchableTarget;
import org.jboss.as.patching.metadata.PatchMetadataResolver;
import org.jboss.as.patching.metadata.PatchXml;
import org.jboss.as.patching.tool.ContentVerificationPolicy;
import org.jboss.as.patching.tool.PatchTool;
import org.jboss.as.patching.tool.PatchingResult;

/**
 * The default patch tool implementation.
 *
 * @author Emanuel Muckenhuber
 */
public class PatchToolImpl implements PatchTool {

    private final InstallationManager manager;
    private final InstallationManager.ModificationCompletionCallback callback;
    private final IdentityPatchRunner runner;

    public PatchToolImpl(final InstallationManager manager) {
        this.manager = manager;
        this.runner = new IdentityPatchRunner(manager.getInstalledImage());
        this.callback = runner;
    }

    public PatchToolImpl(final InstallationManager manager, final InstallationManager.ModificationCompletionCallback callback) {
        this.manager = manager;
        this.runner = new IdentityPatchRunner(manager.getInstalledImage());
        this.callback = callback;
    }

    @Override
    public PatchInfo getPatchInfo() {
        try {
            final Identity identity = manager.getIdentity();
            final PatchableTarget.TargetInfo info = manager.getIdentity().loadTargetInfo();
            return new PatchInfo() {
                @Override
                public String getVersion() {
                    return identity.getVersion();
                }

                @Override
                public String getCumulativePatchID() {
                    return info.getCumulativePatchID();
                }

                @Override
                public List<String> getPatchIDs() {
                    return info.getPatchIDs();
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PatchingResult applyPatch(final File file, final ContentVerificationPolicy contentPolicy) throws PatchingException {
        try {
            if (file.isDirectory()) {
                final File patchXml = new File(file, PatchXml.PATCH_XML);
                if (patchXml.exists()) {
                    // Shortcut exploded patches
                    return execute(file, contentPolicy);
                }
            }
            final InputStream is = new FileInputStream(file);
            try {
                return applyPatch(is, contentPolicy);
            } finally {
                if(is != null) try {
                    is.close();
                } catch (IOException e) {
                    PatchLogger.ROOT_LOGGER.debugf(e, "failed to close input stream");
                }
            }
        } catch (Exception e) {
            throw rethrowException(e);
        }
    }

    @Override
    public PatchingResult applyPatch(final URL url, final ContentVerificationPolicy contentPolicy) throws PatchingException {
        try {
            final InputStream is = url.openStream();
            try {
                return applyPatch(is, contentPolicy);
            } finally {
                if(is != null) try {
                    is.close();
                } catch (IOException e) {
                    PatchLogger.ROOT_LOGGER.debugf(e, "failed to close input stream");
                }
            }
        } catch (IOException e) {
            throw new PatchingException(e);
        }
    }

    @Override
    public PatchingResult applyPatch(final InputStream is, final ContentVerificationPolicy contentPolicy) throws PatchingException {
        File workDir = null;
        try {
            // Create a working dir
            workDir = IdentityPatchRunner.createTempDir();

            // Save the content
            final File cachedContent = new File(workDir, "content");
            IoUtils.copy(is, cachedContent);
            // Unpack to the work dir
            ZipUtils.unzip(cachedContent, workDir);

            // Execute
            return execute(workDir, contentPolicy);
        } catch (Exception e) {
            throw rethrowException(e);
        } finally {
            if (workDir != null && !IoUtils.recursiveDelete(workDir)) {
                PatchLogger.ROOT_LOGGER.debugf("failed to remove work directory (%s)", workDir);
            }
        }
    }

    @Override
    public PatchingResult rollback(final String patchId, final ContentVerificationPolicy contentPolicy,
                                   final boolean rollbackTo, final boolean resetConfiguration) throws PatchingException {
        // Rollback the patch
        final InstallationManager.InstallationModification modification = manager.modifyInstallation(runner);
        try {
            return runner.rollbackPatch(patchId, contentPolicy, rollbackTo, resetConfiguration, modification);
        } catch (Exception e) {
            modification.cancel();
            throw rethrowException(e);
        }
    }

    @Override
    public PatchingResult rollbackLast(final ContentVerificationPolicy contentPolicy, final boolean resetConfiguration) throws PatchingException {
        // Rollback the patch
        final InstallationManager.InstallationModification modification = manager.modifyInstallation(runner);
        try {
            return runner.rollbackLast(contentPolicy, resetConfiguration, modification);
        } catch (Exception e) {
            modification.cancel();
            throw rethrowException(e);
        }
    }

    protected PatchingResult execute(final File workDir, final ContentVerificationPolicy contentPolicy) throws PatchingException, IOException, XMLStreamException {

        // Parse the xml
        final PatchContentProvider contentProvider = PatchContentProvider.DefaultContentProvider.create(workDir);
        final File patchXml = new File(workDir, PatchXml.PATCH_XML);
        final InputStream patchIS = new FileInputStream(patchXml);
        final PatchMetadataResolver patchResolver;
        try {
            patchResolver = PatchXml.parse(patchIS);
            patchIS.close();
        } finally {
            safeClose(patchIS);
        }
        // Apply the patch
        final InstallationManager.InstallationModification modification = manager.modifyInstallation(callback);
        try {
            return runner.applyPatch(patchResolver, contentProvider, contentPolicy, modification);
        } catch (Exception e) {
            modification.cancel();
            throw rethrowException(e);
        }
    }

    static PatchingException rethrowException(final Exception e) {
        if (e instanceof PatchingException) {
            return (PatchingException) e;
        } else {
            return new PatchingException(e);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8996.java