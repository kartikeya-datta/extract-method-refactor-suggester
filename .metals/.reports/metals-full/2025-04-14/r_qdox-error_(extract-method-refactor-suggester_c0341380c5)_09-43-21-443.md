error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15281.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15281.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15281.java
text:
```scala
i@@f (!unit.getName().endsWith(".war") && EjbDeploymentMarker.isEjbDeployment(unit)) {

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

package org.jboss.as.webservices.injection;

import static org.jboss.as.server.deployment.Attachments.ANNOTATION_INDEX;
import static org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT;
import static org.jboss.as.server.deployment.Attachments.RESOURCE_ROOTS;
import static org.jboss.as.webservices.WSMessages.MESSAGES;
import static org.jboss.as.webservices.util.ASHelper.isJaxwsService;
import static org.jboss.as.webservices.util.DotNames.HANDLER_CHAIN_ANNOTATION;
import static org.jboss.as.webservices.util.DotNames.WEB_SERVICE_ANNOTATION;
import static org.jboss.as.webservices.util.DotNames.WEB_SERVICE_PROVIDER_ANNOTATION;
import static org.jboss.as.webservices.util.WSAttachmentKeys.WS_ENDPOINT_HANDLERS_MAPPING_KEY;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.ejb3.deployment.EjbDeploymentMarker;
import org.jboss.as.server.deployment.AttachmentList;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.vfs.VirtualFile;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerChainMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerChainsMetaData;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerChainsMetaDataParser;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerMetaData;

/**
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public final class WSHandlerChainAnnotationProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit unit = phaseContext.getDeploymentUnit();
        if (DeploymentTypeMarker.isType(DeploymentType.EAR, unit)) {
            return;
        }
        // wars define resource roots
        AttachmentList<ResourceRoot> resourceRoots = unit.getAttachment(RESOURCE_ROOTS);
        if (EjbDeploymentMarker.isEjbDeployment(unit)) {
            // ejb archives don't define resource roots, using root resource
            resourceRoots = new AttachmentList<ResourceRoot>(ResourceRoot.class);
            final ResourceRoot root = unit.getAttachment(DEPLOYMENT_ROOT);
            resourceRoots.add(root);
        }
        if (resourceRoots == null) {
            return;
        }

        final WSEndpointHandlersMapping mapping = new WSEndpointHandlersMapping();
        Index index = null;
        for (final ResourceRoot resourceRoot : resourceRoots) {
            index = resourceRoot.getAttachment(ANNOTATION_INDEX);
            if (index != null) {
                // process @HandlerChain annotations
                processHandlerChainAnnotations(resourceRoot, index, mapping);
            }
        }
        if (!mapping.isEmpty()) {
            unit.putAttachment(WS_ENDPOINT_HANDLERS_MAPPING_KEY, mapping);
        }
    }

    @Override
    public void undeploy(final DeploymentUnit context) {
        // noop
    }

    private static void processHandlerChainAnnotations(final ResourceRoot resourceRoot, final Index index, final WSEndpointHandlersMapping mapping) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> handlerChainAnnotations = index.getAnnotations(HANDLER_CHAIN_ANNOTATION);

        for (final AnnotationInstance handlerChainAnnotation : handlerChainAnnotations) {
            final AnnotationTarget annotationTarget = handlerChainAnnotation.target();

            if (annotationTarget instanceof ClassInfo) {
                final ClassInfo classInfo = (ClassInfo) annotationTarget;
                if (isJaxwsEndpoint(classInfo, index)) {
                    final String endpointClass = classInfo.name().toString();
                    processHandlerChainAnnotation(resourceRoot, handlerChainAnnotation, endpointClass, mapping);
                }
            } else {
                // We ignore fields & methods annotated with @HandlerChain.
                // These are used always in combination with @WebServiceRef
                // which are always referencing JAXWS client proxies only.
            }
        }
    }

    private static void processHandlerChainAnnotation(final ResourceRoot resourceRoot, final AnnotationInstance handlerChainAnnotation, final String endpointClass, final WSEndpointHandlersMapping mapping) throws DeploymentUnitProcessingException {
        final String handlerChainConfigFile = handlerChainAnnotation.value("file").asString();
        InputStream is = null;
        try {
            is = getInputStream(resourceRoot, handlerChainConfigFile, endpointClass);
            final Set<String> endpointHandlers = getHandlers(is);
            if (endpointHandlers.size() > 0) {
                mapping.registerEndpointHandlers(endpointClass, endpointHandlers);
            }
        } catch (final IOException e) {
            throw new DeploymentUnitProcessingException(e);
        } finally {
            if (is != null) {
                try { is.close(); } catch (final IOException ignore) {}
            }
        }
    }

    private static InputStream getInputStream(final ResourceRoot resourceRoot, final String handlerChainConfigFile, final String annotatedClassName) throws IOException {
        if (handlerChainConfigFile.startsWith("file://") || handlerChainConfigFile.startsWith("http://")) {
            return new URL(handlerChainConfigFile).openStream();
        } else {
            URI classURI = null;
            try {
                classURI = new URI(annotatedClassName.replace('.', '/'));
            } catch (final URISyntaxException ignore) {}
            final String handlerChaingConfigFileResourcePath = classURI.resolve(handlerChainConfigFile).toString();
            final VirtualFile config = resourceRoot.getRoot().getChild(handlerChaingConfigFileResourcePath);
            if (config.exists() && config.isFile()) {
                return config.openStream();
            }

            throw MESSAGES.missingHandlerChainConfigFile(handlerChaingConfigFileResourcePath, resourceRoot);
        }
    }

    private static Set<String> getHandlers(final InputStream is) throws IOException {
        final Set<String> retVal = new HashSet<String>();

        final UnifiedHandlerChainsMetaData handlerChainsUMDM = UnifiedHandlerChainsMetaDataParser.parse(is);
        for (final UnifiedHandlerChainMetaData handlerChainUMDM : handlerChainsUMDM.getHandlerChains()) {
            for (final UnifiedHandlerMetaData handlerUMDM : handlerChainUMDM.getHandlers()) {
                retVal.add(handlerUMDM.getHandlerClass());
            }
        }

        return retVal;
    }

    private static boolean isJaxwsEndpoint(final ClassInfo clazz, final Index index) {
        // assert JAXWS endpoint class flags
        final short flags = clazz.flags();
        if (Modifier.isInterface(flags)) return false;
        if (Modifier.isAbstract(flags)) return false;
        if (!Modifier.isPublic(flags)) return false;
        if (isJaxwsService(clazz, index)) return false;
        if (Modifier.isFinal(flags)) return false;
        final boolean isWebService = clazz.annotations().containsKey(WEB_SERVICE_ANNOTATION);
        final boolean isWebServiceProvider = clazz.annotations().containsKey(WEB_SERVICE_PROVIDER_ANNOTATION);
        return isWebService || isWebServiceProvider;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15281.java