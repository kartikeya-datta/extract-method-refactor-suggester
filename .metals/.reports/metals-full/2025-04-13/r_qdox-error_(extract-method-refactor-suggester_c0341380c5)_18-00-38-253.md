error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4586.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4586.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4586.java
text:
```scala
public static final l@@ong PRIORITY = DeploymentPhases.PARSE_DESCRIPTORS.plus(100L);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.deployment.processor;

import org.jboss.as.deployment.DeploymentPhases;
import org.jboss.as.deployment.attachment.Dependencies;
import org.jboss.as.deployment.item.ModuleDeploymentItem;
import org.jboss.as.deployment.unit.DeploymentUnitContext;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.msc.service.Location;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import java.io.IOException;
import java.util.jar.Manifest;

import static org.jboss.as.deployment.attachment.VirtualFileAttachment.getVirtualFileAttachment;

/**
 * DeploymentUnitProcessor that will extract module dependencies from an archive. 
 *
 * @author John E. Bailey
 */
public class ModuleDependencyProcessor implements DeploymentUnitProcessor {
    private static final long MODULE_DEPENDENCY_PROCESSOR_ORDER = DeploymentPhases.PARSE_DESCRIPTORS.plus(100L);

    @Override
    public void processDeployment(DeploymentUnitContext context) throws DeploymentUnitProcessingException {
        final VirtualFile deploymentRoot = getVirtualFileAttachment(context);
        final Manifest manifest;
        try {
            manifest = VFSUtils.getManifest(deploymentRoot);
        } catch(IOException e) {
            throw new DeploymentUnitProcessingException("Failed to get manifest for deployment " + deploymentRoot, e, new Location(e.getStackTrace()[0].getFileName(), e.getStackTrace()[0].getLineNumber(), -1, null));
        }
        if(manifest == null)
            return;

        final String dependencyString = manifest.getMainAttributes().getValue("Dependencies");
        final String[] dependencyDefs = dependencyString.split(",");
        for(String dependencyDef : dependencyDefs) {
            final String[] dependencyParts = dependencyDef.split(" ");
            final int dependencyPartsLength = dependencyParts.length;
            if(dependencyPartsLength == 0)
                throw new RuntimeException("Invalid dependency: " + dependencyString);

            final ModuleIdentifier dependencyId = ModuleIdentifier.fromString(dependencyParts[0]);
            boolean export = parseOptionalExportParams(dependencyParts, "export");
            boolean optional = parseOptionalExportParams(dependencyParts, "export");
            ModuleDeploymentItem.Dependency dependency = new ModuleDeploymentItem.Dependency(dependencyId, true, optional, export);
            Dependencies.addDependency(context, dependency);
        }
    }

    private boolean parseOptionalExportParams(final String[] parts, final String expected) {
        if(parts.length > 1) {
            final String part = parts[1];
            if(expected.equals(part))
                return true;
        }
        if(parts.length > 2) {
            final String part = parts[2];
            if(expected.equals(part))
                return true;
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4586.java