error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7522.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7522.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7522.java
text:
```scala
public static final M@@ap<String, String> FIXED_LOCATIONS;

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

package org.jboss.as.ee.component;

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

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deployment processor responsible for analyzing each attached {@link ComponentDescription} instance to configure
 * required resource injection configurations.
 *
 * @author John Bailey
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class ResourceInjectionAnnotationParsingProcessor implements DeploymentUnitProcessor {
    private static final DotName RESOURCE_ANNOTATION_NAME = DotName.createSimple(Resource.class.getName());
    private static final DotName RESOURCES_ANNOTATION_NAME = DotName.createSimple(Resources.class.getName());
    private static final Map<String, String> FIXED_LOCATIONS;

    static {
        final Map<String, String> locations = new HashMap<String, String>();
        locations.put("javax.transaction.UserTransaction", "java:comp/UserTransaction");
        locations.put("javax.transaction.TransactionSynchronizationRegistry", "java:comp/TransactionSynchronizationRegistry");
        locations.put("javax.enterprise.inject.spi.BeanManager", "java:comp/BeanManager");
        locations.put("javax.validation.Validator", "java:comp/Validator");
        locations.put("javax.validation.ValidationFactory", "java:comp/ValidationFactory");
        locations.put("javax.ejb.EJBContext", "java:comp/EJBContext");
        locations.put("javax.ejb.SessionContext", "java:comp/EJBContext");
        locations.put("org.omg.CORBA.ORB", "java:comp/ORB");
        FIXED_LOCATIONS = Collections.unmodifiableMap(locations);
    }

    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final CompositeIndex index = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.COMPOSITE_ANNOTATION_INDEX);
        final List<AnnotationInstance> resourceAnnotations = index.getAnnotations(RESOURCE_ANNOTATION_NAME);
        for (AnnotationInstance annotation : resourceAnnotations) {
            final AnnotationTarget annotationTarget = annotation.target();
            final AnnotationValue nameValue = annotation.value("name");
            final String name = nameValue != null ? nameValue.asString() : null;
            final AnnotationValue typeValue = annotation.value("type");
            final String type = typeValue != null ? typeValue.asClass().name().toString() : null;
            if (annotationTarget instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) annotationTarget;
                ClassInfo classInfo = fieldInfo.declaringClass();
                EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(classInfo.name().toString());
                processFieldResource(fieldInfo, name, type, classDescription, annotation, eeModuleDescription);
            } else if (annotationTarget instanceof MethodInfo) {
                MethodInfo methodInfo = (MethodInfo) annotationTarget;
                ClassInfo classInfo = methodInfo.declaringClass();
                EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(classInfo.name().toString());
                processMethodResource(methodInfo, name, type, classDescription, annotation, eeModuleDescription);
            } else if (annotationTarget instanceof ClassInfo) {
                ClassInfo classInfo = (ClassInfo) annotationTarget;
                EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(classInfo.name().toString());
                processClassResource(name, type, classDescription, annotation, eeModuleDescription);
            }
        }
        final List<AnnotationInstance> resourcesAnnotations = index.getAnnotations(RESOURCES_ANNOTATION_NAME);
        for (AnnotationInstance outerAnnotation : resourcesAnnotations) {
            final AnnotationTarget annotationTarget = outerAnnotation.target();
            if (annotationTarget instanceof ClassInfo) {
                final ClassInfo classInfo = (ClassInfo) annotationTarget;
                final AnnotationInstance[] values = outerAnnotation.value("value").asNestedArray();
                for (AnnotationInstance annotation : values) {
                    final AnnotationValue nameValue = annotation.value("name");
                    final String name = nameValue != null ? nameValue.asString() : null;
                    final AnnotationValue typeValue = annotation.value("type");
                    final String type = typeValue != null ? typeValue.asClass().name().toString() : null;
                    EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(classInfo.name().toString());
                    processClassResource(name, type, classDescription, annotation, eeModuleDescription);
                }
            }
        }
    }

    public void undeploy(final DeploymentUnit context) {
    }

    private void processFieldResource(final FieldInfo fieldInfo, final String name, final String type, final EEModuleClassDescription classDescription, final AnnotationInstance annotation, final EEModuleDescription eeModuleDescription) {
        final String fieldName = fieldInfo.name();
        final String injectionType = isEmpty(type) || type.equals(Object.class.getName()) ? fieldInfo.type().name().toString() : type;
        final String localContextName = isEmpty(name) ? fieldInfo.declaringClass().name().toString() + "/" + fieldName : name;

        final InjectionTarget targetDescription = new FieldInjectionTarget(fieldInfo.declaringClass().name().toString(), fieldName, injectionType);
        process(classDescription, annotation, injectionType, localContextName, targetDescription, eeModuleDescription);
    }

    private void processMethodResource(final MethodInfo methodInfo, final String name, final String type, final EEModuleClassDescription classDescription, final AnnotationInstance annotation, final EEModuleDescription eeModuleDescription) {
        final String methodName = methodInfo.name();
        if (!methodName.startsWith("set") || methodInfo.args().length != 1) {
            throw new IllegalArgumentException("@Resource injection target is invalid.  Only setter methods are allowed: " + methodInfo);
        }

        final String contextNameSuffix = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        final String localContextName = isEmpty(name) ? methodInfo.declaringClass().name().toString() + "/" + contextNameSuffix : name;

        final String injectionType = isEmpty(type) || type.equals(Object.class.getName()) ? methodInfo.args()[0].name().toString() : type;
        final InjectionTarget targetDescription = new MethodInjectionTarget(methodInfo.declaringClass().name().toString(), methodName, methodInfo.args()[0].name().toString());
        process(classDescription, annotation, injectionType, localContextName, targetDescription, eeModuleDescription);
    }

    private void processClassResource(final String name, final String type, final EEModuleClassDescription classDescription, final AnnotationInstance annotation, final EEModuleDescription eeModuleDescription) {
        if (isEmpty(name)) {
            throw new IllegalArgumentException("Class level @Resource annotations must provide a name.");
        }
        if (isEmpty(type) || type.equals(Object.class.getName())) {
            throw new IllegalArgumentException("Class level @Resource annotations must provide a type.");
        }
        process(classDescription, annotation, type, name, null, eeModuleDescription);
    }

    private void process(final EEModuleClassDescription classDescription, final AnnotationInstance annotation, final String injectionType, final String localContextName, final InjectionTarget targetDescription, EEModuleDescription eeModuleDescription) {
        final AnnotationValue lookupAnnotation = annotation.value("lookup");
        String lookup = lookupAnnotation == null ? null : lookupAnnotation.asString();

        if (isEmpty(lookup) && FIXED_LOCATIONS.containsKey(injectionType)) {
            lookup = FIXED_LOCATIONS.get(injectionType);
        }
        final InjectionSource valueSource;
        // TODO: check for primitives, connection factories, blah blah
        if (isEmpty(lookup)) {
            eeModuleDescription.addLazyResourceInjection(new LazyResourceInjection(targetDescription, localContextName, classDescription));
            return;
        } else {
            valueSource = new LookupInjectionSource(lookup);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7522.java