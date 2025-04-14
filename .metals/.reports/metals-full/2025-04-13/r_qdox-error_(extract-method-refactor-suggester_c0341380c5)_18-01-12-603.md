error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12434.java
text:
```scala
final M@@oduleDependency dependency = new ModuleDependency(dependencyLoader, dependencyId, optional, export, services, true);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server.deployment.module;

import java.util.List;
import java.util.jar.Manifest;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentUtils;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.filter.PathFilters;

/**
 * Deployment unit processor that will extract module dependencies from an and attach them.
 *
 * @author John E. Bailey
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 * @author Stuart Douglas
 */
public final class ManifestDependencyProcessor implements DeploymentUnitProcessor {

    private static final String DEPENDENCIES_ATTR = "Dependencies";
    private static final String EXPORT_PARAM = "export";
    private static final String OPTIONAL_PARAM = "optional";
    private static final String SERVICES_PARAM = "services";
    private static final String ANNOTATIONS_PARAM = "annotations";
    private static final String META_INF = "meta-inf";

    /**
     * Process the deployment root for module dependency information.
     *
     * @param phaseContext the deployment unit context
     * @throws org.jboss.as.server.deployment.DeploymentUnitProcessingException
     */
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ServiceModuleLoader deploymentModuleLoader = deploymentUnit.getAttachment(Attachments.SERVICE_MODULE_LOADER);
        final List<ResourceRoot> allResourceRoots = DeploymentUtils.allResourceRoots(deploymentUnit);

        for (final ResourceRoot resourceRoot : allResourceRoots) {
            final Manifest manifest = resourceRoot.getAttachment(Attachments.MANIFEST);
            if (manifest == null)
                continue;

            final String dependencyString = manifest.getMainAttributes().getValue(DEPENDENCIES_ATTR);
            if (dependencyString == null)
                continue;

            final String[] dependencyDefs = dependencyString.split(",");
            for (final String dependencyDef : dependencyDefs) {
                final String[] dependencyParts = dependencyDef.trim().split(" ");
                if (dependencyParts.length == 0) {
                    throw new RuntimeException("Invalid dependency: " + dependencyString);
                }

                final ModuleIdentifier dependencyId = ModuleIdentifier.fromString(dependencyParts[0]);
                final boolean export = containsParam(dependencyParts, EXPORT_PARAM);
                final boolean optional = containsParam(dependencyParts, OPTIONAL_PARAM);
                final boolean services = containsParam(dependencyParts, SERVICES_PARAM);
                final boolean annotations = containsParam(dependencyParts, ANNOTATIONS_PARAM);
                final boolean metaInf = containsParam(dependencyParts, META_INF);
                final ModuleLoader dependencyLoader;
                if (dependencyId.getName().startsWith("deployment.")) {
                    dependencyLoader = deploymentModuleLoader;
                } else {
                    dependencyLoader = Module.getBootModuleLoader();
                }
                if(annotations) {
                    deploymentUnit.addToAttachmentList(Attachments.ADDITIONAL_ANNOTATION_INDEXES, dependencyId);
                }

                final ModuleDependency dependency = new ModuleDependency(dependencyLoader, dependencyId, optional, export, services);
                if(metaInf) {
                    dependency.addImportFilter(PathFilters.getMetaInfSubdirectoriesFilter(), true);
                }
                deploymentUnit.addToAttachmentList(Attachments.MANIFEST_DEPENDENCIES, dependency);
            }
        }

    }

    public void undeploy(final DeploymentUnit context) {
    }

    private boolean containsParam(final String[] parts, final String expected) {
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                if (expected.equals(parts[i])) {
                    return true;
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12434.java