error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8.java
text:
```scala
t@@hrow new DeploymentUnitProcessingException("Could not process @WebServlet on " + target, e);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.web.deployment;

import static org.jboss.as.web.deployment.WarDeploymentMarker.isWarDeployment;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.AnnotationIndexUtils;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.metadata.annotation.creator.DeclareRolesProcessor;
import org.jboss.metadata.annotation.creator.web.MultipartConfigProcessor;
import org.jboss.metadata.annotation.creator.web.RunAsProcessor;
import org.jboss.metadata.annotation.creator.web.ServletSecurityProcessor;
import org.jboss.metadata.annotation.creator.web.WebFilterProcessor;
import org.jboss.metadata.annotation.creator.web.WebListenerProcessor;
import org.jboss.metadata.annotation.creator.web.WebServletProcessor;
import org.jboss.metadata.annotation.finder.AnnotationFinder;
import org.jboss.metadata.annotation.finder.DefaultAnnotationFinder;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.metadata.web.spec.AnnotationsMetaData;
import org.jboss.metadata.web.spec.Web30MetaData;
import org.jboss.metadata.web.spec.WebMetaData;
import org.jboss.modules.Module;

/**
 * Web annotation deployment processor.
 *
 * @author Emanuel Muckenhuber
 * @author Remy Maucherat
 */
public class WarAnnotationDeploymentProcessor implements DeploymentUnitProcessor {

    private static final DotName webFilter = DotName.createSimple(WebFilter.class.getName());
    private static final DotName webListener = DotName.createSimple(WebListener.class.getName());
    private static final DotName webServlet = DotName.createSimple(WebServlet.class.getName());
    private static final DotName runAs = DotName.createSimple(RunAs.class.getName());
    private static final DotName declareRoles = DotName.createSimple(DeclareRoles.class.getName());
    private static final DotName multipartConfig = DotName.createSimple(MultipartConfig.class.getName());
    private static final DotName servletSecurity = DotName.createSimple(ServletSecurity.class.getName());

    /**
     * Process web annotations.
     */
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if(!isWarDeployment(deploymentUnit)) {
            return; // Skip non web deployments
        }

        WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        assert warMetaData != null;
        Map<String, WebMetaData> annotationsMetaData = warMetaData.getAnnotationsMetaData();
        if (annotationsMetaData == null) {
            annotationsMetaData = new HashMap<String, WebMetaData>();
            warMetaData.setAnnotationsMetaData(annotationsMetaData);
        }
        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);
        if (module == null) {
            throw new DeploymentUnitProcessingException("failed to resolve module for " + deploymentUnit);
        }
        final ClassLoader classLoader = module.getClassLoader();

        Map<ResourceRoot, Index> indexes = AnnotationIndexUtils.getAnnotationIndexes(deploymentUnit);

        // Process lib/*.jar
        for (final Entry<ResourceRoot, Index> entry : indexes.entrySet()) {
            final Index jarIndex = entry.getValue();
            annotationsMetaData.put(entry.getKey().getRootName(), processAnnotations(jarIndex, classLoader));
        }
    }

    public void undeploy(final DeploymentUnit context) {
    }

    /**
     * Process a single index.
     *
     * @param index the annotation index
     * @param classLoader the module classloader
     * @throws DeploymentUnitProcessingException
     */
    protected WebMetaData processAnnotations(Index index, ClassLoader classLoader)
    throws DeploymentUnitProcessingException {
        Web30MetaData metaData = new Web30MetaData();
        AnnotationFinder<AnnotatedElement> finder = new DefaultAnnotationFinder<AnnotatedElement>();
        // @WebServlet
        final List<AnnotationInstance> webServletAnnotations = index.getAnnotations(webServlet);
        if (webServletAnnotations != null && webServletAnnotations.size() > 0) {
            WebServletProcessor processor = new WebServletProcessor(finder);
            for (final AnnotationInstance annotation : webServletAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@WebServlet is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @WebServlet on " + target);
                }
                if (type != null) {
                    processor.process(metaData, type);
                }
            }
        }
        // @WebFilter
        final List<AnnotationInstance> webFilterAnnotations = index.getAnnotations(webFilter);
        if (webFilterAnnotations != null && webFilterAnnotations.size() > 0) {
            WebFilterProcessor processor = new WebFilterProcessor(finder);
            for (final AnnotationInstance annotation : webFilterAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@WebFilter is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @WebFilter on " + target);
                }
                if (type != null) {
                    processor.process(metaData, type);
                }
            }
        }
        // @WebListener
        final List<AnnotationInstance> webListenerAnnotations = index.getAnnotations(webListener);
        if (webListenerAnnotations != null && webListenerAnnotations.size() > 0) {
            WebListenerProcessor processor = new WebListenerProcessor(finder);
            for (final AnnotationInstance annotation : webListenerAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@WebListener is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @WebListener on " + target);
                }
                if (type != null) {
                    processor.process(metaData, type);
                }
            }
        }
        // @RunAs
        final List<AnnotationInstance> runAsAnnotations = index.getAnnotations(runAs);
        if (runAsAnnotations != null && runAsAnnotations.size() > 0) {
            RunAsProcessor processor = new RunAsProcessor(finder);
            AnnotationsMetaData annotations = metaData.getAnnotations();
            if (annotations == null) {
               annotations = new AnnotationsMetaData();
               metaData.setAnnotations(annotations);
            }
            for (final AnnotationInstance annotation : runAsAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@RunAs is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @RunAs on " + target);
                }
                if (type != null) {
                    processor.process(annotations, type);
                }
            }
        }
        // @DeclareRoles
        final List<AnnotationInstance> declareRolesAnnotations = index.getAnnotations(declareRoles);
        if (declareRolesAnnotations != null && declareRolesAnnotations.size() > 0) {
            DeclareRolesProcessor processor = new DeclareRolesProcessor(finder);
            SecurityRolesMetaData securityRoles = metaData.getSecurityRoles();
            if (securityRoles == null) {
               securityRoles = new SecurityRolesMetaData();
               metaData.setSecurityRoles(securityRoles);
            }
            for (final AnnotationInstance annotation : declareRolesAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@DeclareRoles is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @DeclareRoles on " + target);
                }
                if (type != null) {
                    processor.process(securityRoles, type);
                }
            }
        }
        // @MultipartConfig
        final List<AnnotationInstance> multipartConfigAnnotations = index.getAnnotations(multipartConfig);
        if (multipartConfigAnnotations != null && multipartConfigAnnotations.size() > 0) {
            MultipartConfigProcessor processor = new MultipartConfigProcessor(finder);
            AnnotationsMetaData annotations = metaData.getAnnotations();
            if (annotations == null) {
               annotations = new AnnotationsMetaData();
               metaData.setAnnotations(annotations);
            }
            for (final AnnotationInstance annotation : multipartConfigAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@MultipartConfig is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @MultipartConfig on " + target);
                }
                if (type != null) {
                    processor.process(annotations, type);
                }
            }
        }
        // @ServletSecurity
        final List<AnnotationInstance> servletSecurityAnnotations = index.getAnnotations(servletSecurity);
        if (servletSecurityAnnotations != null && servletSecurityAnnotations.size() > 0) {
            ServletSecurityProcessor processor = new ServletSecurityProcessor(finder);
            AnnotationsMetaData annotations = metaData.getAnnotations();
            if (annotations == null) {
               annotations = new AnnotationsMetaData();
               metaData.setAnnotations(annotations);
            }
            for (final AnnotationInstance annotation : servletSecurityAnnotations) {
                AnnotationTarget target = annotation.target();
                if (!(target instanceof ClassInfo)) {
                    throw new DeploymentUnitProcessingException("@ServletSecurity is only allowed at class level " + target);
                }
                ClassInfo classInfo = ClassInfo.class.cast(target);
                Class<?> type = null;
                try {
                    type = classLoader.loadClass(classInfo.name().toString());
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException("Could not process @ServletSecurity on " + target);
                }
                if (type != null) {
                    processor.process(annotations, type);
                }
            }
        }
        return metaData;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8.java