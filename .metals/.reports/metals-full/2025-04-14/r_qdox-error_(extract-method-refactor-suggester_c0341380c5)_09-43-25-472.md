error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4156.java
text:
```scala
final I@@njectionTarget targetDescription = new FieldInjectionTarget(fieldInfo.declaringClass().name().toString(), fieldName, injectionType);

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

package org.jboss.as.ejb3.deployment.processors;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.ClassConfigurator;
import org.jboss.as.ee.component.ComponentTypeInjectionSource;
import org.jboss.as.ee.component.EEModuleClassConfiguration;
import org.jboss.as.ee.component.EEModuleClassDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.FieldInjectionTarget;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.InjectionTarget;
import org.jboss.as.ee.component.LookupInjectionSource;
import org.jboss.as.ee.component.MethodInjectionTarget;
import org.jboss.as.ee.component.ResourceInjectionConfiguration;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Logger;

import javax.ejb.EJB;
import java.util.List;

/**
 * Deployment processor responsible for processing @EJB annotations within components.  Each @EJB annotation will be registered
 * as an injection binding for the component.
 *
 * @author John Bailey
 */
public class EjbResourceInjectionAnnotationProcessor implements DeploymentUnitProcessor {
    private static final DotName EJB_ANNOTATION_NAME = DotName.createSimple(EJB.class.getName());

    private static final Logger logger = Logger.getLogger(EjbResourceInjectionAnnotationProcessor.class);

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final CompositeIndex index = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.COMPOSITE_ANNOTATION_INDEX);
        final List<AnnotationInstance> resourceAnnotations = index.getAnnotations(EJB_ANNOTATION_NAME);
        for (AnnotationInstance annotation : resourceAnnotations) {
            final AnnotationTarget annotationTarget = annotation.target();
            final EJBResourceWrapper annotationWrapper = new EJBResourceWrapper(annotation);
            if (annotationTarget instanceof FieldInfo) {
                processField(eeModuleDescription, annotationWrapper, (FieldInfo) annotationTarget);
            } else if (annotationTarget instanceof MethodInfo) {
                processMethod(eeModuleDescription, annotationWrapper, (MethodInfo) annotationTarget);
            } else if (annotationTarget instanceof ClassInfo) {
                processClass(eeModuleDescription, annotationWrapper, (ClassInfo) annotationTarget);
            }
        }
    }

    public void undeploy(DeploymentUnit context) {
    }

    private void processField(final EEModuleDescription eeModuleDescription, final EJBResourceWrapper annotation, final FieldInfo fieldInfo) {
        final String fieldName = fieldInfo.name();
        final String injectionType = isEmpty(annotation.beanInterface()) || annotation.beanInterface().equals(Object.class.getName()) ? fieldInfo.type().name().toString() : annotation.beanInterface();
        final InjectionTarget targetDescription = new FieldInjectionTarget(fieldName, fieldInfo.declaringClass().name().toString(), injectionType);
        final String localContextName = isEmpty(annotation.name()) ? fieldInfo.declaringClass().name().toString() + "/" + fieldInfo.name() : annotation.name();
        process(eeModuleDescription, targetDescription.getClassName(), annotation.beanName(), annotation.lookup(), fieldInfo.declaringClass(), targetDescription, localContextName);
    }

    private void processMethod(final EEModuleDescription eeModuleDescription, final EJBResourceWrapper annotation, final MethodInfo methodInfo) {
        final String methodName = methodInfo.name();
        if (!methodName.startsWith("set") || methodInfo.args().length != 1) {
            throw new IllegalArgumentException("@EJB injection target is invalid.  Only setter methods are allowed: " + methodInfo);
        }
        final String injectionType = isEmpty(annotation.beanInterface()) || annotation.beanInterface().equals(Object.class.getName()) ? methodInfo.args()[0].name().toString() : annotation.beanInterface();
        final InjectionTarget targetDescription = new MethodInjectionTarget(methodInfo.declaringClass().name().toString(), methodName, injectionType);

        final String localContextName = isEmpty(annotation.name()) ? methodInfo.declaringClass().name().toString() + "/" + methodName.substring(3, 4).toLowerCase() + methodName.substring(4) : annotation.name();
        process(eeModuleDescription, targetDescription.getClassName(), annotation.beanName(), annotation.lookup(), methodInfo.declaringClass(), targetDescription, localContextName);
    }

    private void processClass(final EEModuleDescription eeModuleDescription, final EJBResourceWrapper annotation, final ClassInfo classInfo) throws DeploymentUnitProcessingException {
        if (isEmpty(annotation.name())) {
            throw new DeploymentUnitProcessingException("@EJB attribute 'name' is required fo class level annotations.");
        }
        if (isEmpty(annotation.beanInterface())) {
            throw new DeploymentUnitProcessingException("@EJB attribute 'beanInterface' is required fo class level annotations.");
        }
        process(eeModuleDescription, annotation.beanInterface(), annotation.beanName(), annotation.lookup(), classInfo, null, annotation.name());
    }

    private void process(final EEModuleDescription eeModuleDescription, final String beanInterface, final String beanName, final String lookup, final ClassInfo classInfo, final InjectionTarget targetDescription, final String localContextName) {

        if (!isEmpty(lookup) && !isEmpty(beanName)) {
            logger.debug("Both beanName = " + beanName + " and lookup = " + lookup + " have been specified in @EJB annotation." +
                    " lookup will be given preference");
        }

        final EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(classInfo.name().toString());

        final InjectionSource valueSource;
        //give preference to lookup
        if (!isEmpty(lookup)) {
            valueSource = new LookupInjectionSource(lookup);
        } else if (!isEmpty(beanName)) {
            valueSource = new EjbBeanNameInjectionSource(beanName, beanInterface);
        } else {
            valueSource = new ComponentTypeInjectionSource(beanInterface);
        }

        // our injection comes from the local lookup, no matter what.
        final ResourceInjectionConfiguration injectionConfiguration = targetDescription != null ?
                new ResourceInjectionConfiguration(targetDescription, new LookupInjectionSource(localContextName)) : null;

        // Create the binding from whence our injection comes.
        final BindingConfiguration bindingConfiguration = new BindingConfiguration(localContextName, valueSource);

        // TODO: class hierarchies? shared bindings?
        classDescription.getConfigurators().add(new ClassConfigurator() {
            public void configure(final DeploymentPhaseContext context, final EEModuleClassDescription description, final EEModuleClassConfiguration configuration) throws DeploymentUnitProcessingException {
                configuration.getBindingConfigurations().add(bindingConfiguration);
                if (injectionConfiguration != null) {
                    configuration.getInjectionConfigurations().add(injectionConfiguration);
                }
            }
        });
    }

    private boolean isEmpty(final String string) {
        return string == null || string.isEmpty();
    }

    private class EJBResourceWrapper {
        private final String name;
        private final String beanInterface;
        private final String beanName;
        private final String lookup;
        private final String description;

        private EJBResourceWrapper(final AnnotationInstance annotation) {
            name = stringValueOrNull(annotation, "name");
            beanInterface = classValueOrNull(annotation, "beanInterface");
            beanName = stringValueOrNull(annotation, "beanName");
            lookup = stringValueOrNull(annotation, "lookup");
            description = stringValueOrNull(annotation, "description");
        }

        private String name() {
            return name;
        }

        private String beanInterface() {
            return beanInterface;
        }

        private String beanName() {
            return beanName;
        }

        private String lookup() {
            return lookup;
        }

        private String description() {
            return description;
        }

        private String stringValueOrNull(final AnnotationInstance annotation, final String attribute) {
            final AnnotationValue value = annotation.value(attribute);
            return value != null ? value.asString() : null;
        }

        private String classValueOrNull(final AnnotationInstance annotation, final String attribute) {
            final AnnotationValue value = annotation.value(attribute);
            return value != null ? value.asClass().name().toString() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4156.java