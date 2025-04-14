error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13881.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13881.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13881.java
text:
```scala
S@@ERVER_DEPLOYMENT_LOGGER.debugf("Injection for a member with static modifier is only acceptable on application clients, ignoring injection for target %s",injectionConfiguration.getTarget());

package org.jboss.as.ee.component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndexUtil;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.Interceptors;
import org.jboss.msc.value.InjectedValue;

import static org.jboss.as.ee.EeLogger.SERVER_DEPLOYMENT_LOGGER;

/**
 * @author Stuart Douglas
 */
public class AbstractComponentConfigurator {
    protected static InterceptorFactory weaved(final Collection<InterceptorFactory> interceptorFactories) {
        if(interceptorFactories == null) {
            return null;
        }
        return new InterceptorFactory() {
            @Override
            public Interceptor create(InterceptorFactoryContext context) {
                final Interceptor[] interceptors = new Interceptor[interceptorFactories.size()];
                final Iterator<InterceptorFactory> factories = interceptorFactories.iterator();
                for (int i = 0; i < interceptors.length; i++) {
                    interceptors[i] = factories.next().create(context);
                }
                return Interceptors.getWeavedInterceptor(interceptors);
            }
        };
    }

    /**
     * Sets up all resource injections for a class. This takes into account injections that have been specified in the module and component deployment descriptors
     * <p/>
     * Note that this does not take superclasses into consideration, only injections on the current class
     *
     * @param clazz             The class or superclass to perform injection for
     * @param actualClass       The actual component or interceptor class
     * @param classDescription  The class description, may be null
     * @param moduleDescription The module description
     * @param description       The component description
     * @param configuration     The component configuration
     * @param context           The phase context
     * @param injectors         The list of injectors for the current component
     * @param instanceKey       The key that identifies the instance to inject in the interceptor context
     * @param uninjectors       The list of uninjections for the current component
     * @throws org.jboss.as.server.deployment.DeploymentUnitProcessingException
     *
     */
    protected void mergeInjectionsForClass(final Class<?> clazz, final Class<?> actualClass, final EEModuleClassDescription classDescription, final EEModuleDescription moduleDescription, final DeploymentReflectionIndex deploymentReflectionIndex, final ComponentDescription description, final ComponentConfiguration configuration, final DeploymentPhaseContext context, final Deque<InterceptorFactory> injectors, final Object instanceKey, final Deque<InterceptorFactory> uninjectors, boolean metadataComplete) throws DeploymentUnitProcessingException {
        final Map<InjectionTarget, ResourceInjectionConfiguration> mergedInjections = new HashMap<InjectionTarget, ResourceInjectionConfiguration>();
        if (classDescription != null && !metadataComplete) {
            mergedInjections.putAll(classDescription.getInjectionConfigurations());
        }
        mergedInjections.putAll(moduleDescription.getResourceInjections(clazz.getName()));
        mergedInjections.putAll(description.getResourceInjections(clazz.getName()));

        for (final ResourceInjectionConfiguration injectionConfiguration : mergedInjections.values()) {
            if(!moduleDescription.isAppClient() && injectionConfiguration.getTarget().isStatic(context.getDeploymentUnit())) {
                SERVER_DEPLOYMENT_LOGGER.ignoringStaticInjectionTarget(injectionConfiguration.getTarget());
                continue;
            }
            if(injectionConfiguration.getTarget() instanceof MethodInjectionTarget) {
                //we need to make sure that if this is a method injection it has not been overriden
                final MethodInjectionTarget mt = (MethodInjectionTarget)injectionConfiguration.getTarget();
                Method method = mt.getMethod(deploymentReflectionIndex, clazz);
                if(!isNotOverriden(clazz, method, actualClass, deploymentReflectionIndex)) {
                    continue;
                }
            }

            final Object valueContextKey = new Object();
            final InjectedValue<ManagedReferenceFactory> managedReferenceFactoryValue = new InjectedValue<ManagedReferenceFactory>();
            configuration.getStartDependencies().add(new ComponentDescription.InjectedConfigurator(injectionConfiguration, configuration, context, managedReferenceFactoryValue));
            injectors.addFirst(injectionConfiguration.getTarget().createInjectionInterceptorFactory(instanceKey, valueContextKey, managedReferenceFactoryValue, context.getDeploymentUnit(), injectionConfiguration.isOptional()));
            uninjectors.addLast(new ManagedReferenceReleaseInterceptorFactory(valueContextKey));
        }
    }

    protected boolean isNotOverriden(final Class<?> clazz, final Method method, final Class<?> actualClass, final DeploymentReflectionIndex deploymentReflectionIndex) throws DeploymentUnitProcessingException {
        return Modifier.isPrivate(method.getModifiers()) || ClassReflectionIndexUtil.findRequiredMethod(deploymentReflectionIndex, actualClass, method).getDeclaringClass() == clazz;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13881.java