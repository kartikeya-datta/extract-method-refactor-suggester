error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5063.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5063.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5063.java
text:
```scala
t@@hrow ServerMessages.MESSAGES.missingModulePrefix(identifier, ServiceModuleLoader.MODULE_PREFIX);

package org.jboss.as.server.moduleservice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.as.server.ServerMessages;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.msc.inject.InjectionException;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

import static org.jboss.msc.service.ServiceBuilder.DependencyType.OPTIONAL;
import static org.jboss.msc.service.ServiceBuilder.DependencyType.REQUIRED;

/**
 * Module phase resolve service. Basically this service attempts to resolve
 * every dynamic transitive dependency of a module, and allows the module resolved service
 * to start once this is complete.
 *
 * @author Stuart Douglas
 */
public class ModuleResolvePhaseService implements Service<ModuleResolvePhaseService> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("module", "resolve", "phase");

    private final ModuleIdentifier moduleIdentifier;

    private final Set<ModuleIdentifier> alreadyResolvedModules;

    private final int phaseNumber;

    /**
     * module specification that were resolved this phase. These are injected as the relevant spec services start.
     */
    private final Set<ModuleDefinition> moduleSpecs = Collections.synchronizedSet(new HashSet<ModuleDefinition>());

    public ModuleResolvePhaseService(final ModuleIdentifier moduleIdentifier, final Set<ModuleIdentifier> alreadyResolvedModules, final int phaseNumber) {
        this.moduleIdentifier = moduleIdentifier;
        this.alreadyResolvedModules = alreadyResolvedModules;
        this.phaseNumber = phaseNumber;
    }

    public ModuleResolvePhaseService(final ModuleIdentifier moduleIdentifier) {
        this.moduleIdentifier = moduleIdentifier;
        this.alreadyResolvedModules = Collections.emptySet();
        this.phaseNumber = 0;
    }

    @Override
    public void start(final StartContext startContext) throws StartException {
        Set<ModuleDependency> nextPhaseIdentifiers = new HashSet<>();
        final Set<ModuleIdentifier> nextAlreadySeen = new HashSet<>(alreadyResolvedModules);
        for (final ModuleDefinition spec : moduleSpecs) {
            if (spec != null) { //this can happen for optional dependencies
                for (ModuleDependency dep : spec.getDependencies()) {
                    if (ServiceModuleLoader.isDynamicModule(dep.getIdentifier())) {
                        if (!alreadyResolvedModules.contains(dep.getIdentifier())) {
                            nextAlreadySeen.add(dep.getIdentifier());
                            nextPhaseIdentifiers.add(dep);
                        }
                    }
                }
            }
        }
        if (nextPhaseIdentifiers.isEmpty()) {
            ServiceModuleLoader.installModuleResolvedService(startContext.getChildTarget(), moduleIdentifier);
        } else {
            installService(startContext.getChildTarget(), moduleIdentifier, phaseNumber + 1, nextPhaseIdentifiers, nextAlreadySeen);
        }
    }

    public static void installService(final ServiceTarget serviceTarget, final ModuleDefinition moduleDefinition) {
        final ModuleResolvePhaseService nextPhaseService = new ModuleResolvePhaseService(moduleDefinition.getModuleIdentifier(), Collections.singleton(moduleDefinition.getModuleIdentifier()), 0);
        nextPhaseService.getModuleSpecs().add(moduleDefinition);
        ServiceBuilder<ModuleResolvePhaseService> builder = serviceTarget.addService(moduleSpecServiceName(moduleDefinition.getModuleIdentifier(), 0), nextPhaseService);
        builder.install();
    }

    private static void installService(final ServiceTarget serviceTarget, final ModuleIdentifier moduleIdentifier, int phaseNumber, final Set<ModuleDependency> nextPhaseIdentifiers, final Set<ModuleIdentifier> nextAlreadySeen) {
        final ModuleResolvePhaseService nextPhaseService = new ModuleResolvePhaseService(moduleIdentifier, nextAlreadySeen, phaseNumber);
        ServiceBuilder<ModuleResolvePhaseService> builder = serviceTarget.addService(moduleSpecServiceName(moduleIdentifier, phaseNumber), nextPhaseService);
        for (ModuleDependency module : nextPhaseIdentifiers) {
            builder.addDependency(module.isOptional() ? OPTIONAL : REQUIRED, ServiceModuleLoader.moduleSpecServiceName(module.getIdentifier()), ModuleDefinition.class, new Injector<ModuleDefinition>() {

                ModuleDefinition definition;

                @Override
                public synchronized void inject(final ModuleDefinition o) throws InjectionException {
                    nextPhaseService.getModuleSpecs().add(o);
                    this.definition = o;
                }

                @Override
                public synchronized void uninject() {
                    nextPhaseService.getModuleSpecs().remove(definition);
                    this.definition = null;
                }
            });
        }
        builder.install();
    }

    @Override
    public void stop(final StopContext stopContext) {

    }

    @Override
    public ModuleResolvePhaseService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public Set<ModuleDefinition> getModuleSpecs() {
        return moduleSpecs;
    }

    public static ServiceName moduleSpecServiceName(ModuleIdentifier identifier, int phase) {
        if (!ServiceModuleLoader.isDynamicModule(identifier)) {
            ServerMessages.MESSAGES.missingModulePrefix(identifier, ServiceModuleLoader.MODULE_PREFIX);
        }
        return SERVICE_NAME.append(identifier.getName()).append(identifier.getSlot()).append("" + phase);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5063.java