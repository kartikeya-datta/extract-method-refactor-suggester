error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1072
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5970.java
text:
```scala
public final class EEModuleClassDescription {

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

p@@ackage org.jboss.as.ee.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.MethodInterceptorFactory;
import org.jboss.invocation.proxy.MethodIdentifier;
import org.jboss.msc.value.ConstructedValue;
import org.jboss.msc.value.Value;

import static org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX;

/**
 * The description of a (possibly annotated) class in an EE module.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class EEModuleClassDescription extends LifecycleCapableDescription {

    private static final DefaultConfigurator DEFAULT_CONFIGURATOR = new DefaultConfigurator();

    private final String className;
    private final Map<MethodIdentifier, ResourceInjectionConfiguration> resourceInjections = new HashMap<MethodIdentifier, ResourceInjectionConfiguration>();
    private final Deque<ClassConfigurator> configurators = new ArrayDeque<ClassConfigurator>();
    private MethodIdentifier postConstructMethod;
    private MethodIdentifier preDestroyMethod;
    private MethodIdentifier aroundInvokeMethod;

    public EEModuleClassDescription(final String className) {
        this.className = className;
        configurators.addFirst(DEFAULT_CONFIGURATOR);
    }

    /**
     * Get the class name of this EE module class.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get the method, if any, which has been marked as an around-invoke interceptor.
     *
     * @return the around-invoke method or {@code null} for none
     */
    public MethodIdentifier getAroundInvokeMethod() {
        return aroundInvokeMethod;
    }

    /**
     * Set the method which has been marked as an around-invoke interceptor.
     *
     * @param aroundInvokeMethod the around-invoke method or {@code null} for none
     */
    public void setAroundInvokeMethod(final MethodIdentifier aroundInvokeMethod) {
        this.aroundInvokeMethod = aroundInvokeMethod;
    }

    /**
     * Get the method, if any, which has been marked as a post-construct interceptor.
     *
     * @return the post-construct method or {@code null} for none
     */
    public MethodIdentifier getPostConstructMethod() {
        return postConstructMethod;
    }

    /**
     * Set the method which has been marked as a post-construct interceptor.
     *
     * @param postConstructMethod the post-construct method or {@code null} for none
     */
    public void setPostConstructMethod(final MethodIdentifier postConstructMethod) {
        this.postConstructMethod = postConstructMethod;
    }

    /**
     * Get the method, if any, which has been marked as a pre-destroy interceptor.
     *
     * @return the pre-destroy method or {@code null} for none
     */
    public MethodIdentifier getPreDestroyMethod() {
        return preDestroyMethod;
    }

    /**
     * Set the method which has been marked as a pre-destroy interceptor.
     *
     * @param preDestroyMethod the pre-destroy method or {@code null} for none
     */
    public void setPreDestroyMethod(final MethodIdentifier preDestroyMethod) {
        this.preDestroyMethod = preDestroyMethod;
    }

    /**
     * Add the resource injection description for the given method.
     *
     * @param identifier the method identifier
     * @return the resource injection description
     */
    public void putResourceInjection(MethodIdentifier identifier, ResourceInjectionConfiguration injectionDescription) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier is null");
        }
        if (injectionDescription == null) {
            throw new IllegalArgumentException("injectionDescription is null");
        }
        if (resourceInjections.containsKey(identifier)) {
            throw new IllegalArgumentException(identifier.toString() + " is already defined");
        }
        resourceInjections.put(identifier, injectionDescription);
    }

    /**
     * Get the resource injection description for the given method.  If no such method has been described, {@code null} is returned.
     *
     * @param identifier the method identifier
     * @return the resource injection description or {@code null} if there is none
     */
    public ResourceInjectionConfiguration getResourceInjection(MethodIdentifier identifier) {
        return resourceInjections.get(identifier);
    }

    /**
     * Get the configurators for this class.
     *
     * @return the configurators
     */
    public Deque<ClassConfigurator> getConfigurators() {
        return configurators;
    }

    private static class DefaultConfigurator implements ClassConfigurator {

        private static final Class<?>[] NO_CLASSES = new Class<?>[0];

        public void configure(final DeploymentPhaseContext context, final EEModuleClassDescription description, final EEModuleClassConfiguration configuration) throws DeploymentUnitProcessingException {
            DeploymentReflectionIndex index = context.getDeploymentUnit().getAttachment(REFLECTION_INDEX);
            Class<?> moduleClass = configuration.getModuleClass();
            ClassReflectionIndex<?> classIndex = index.getClassIndex(moduleClass);
            // Use the basic instantiator if none was set up
            if (configuration.getInstantiator() == null) {
                Constructor<?> constructor = classIndex.getConstructor(NO_CLASSES);
                if (constructor == null) {
                    throw new DeploymentUnitProcessingException("No acceptable constructor found for " + moduleClass);
                }
                configuration.setInstantiator(new ValueManagedReferenceFactory(createConstructedValue(constructor)));
            }
        }

        private static <T> ConstructedValue<T> createConstructedValue(final Constructor<T> constructor) {
            return new ConstructedValue<T>(constructor, Collections.<Value<?>>emptyList());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5970.java