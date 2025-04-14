error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4679.java
text:
```scala
r@@eturn combine(ImmutableSet.copyOf(modules));

/**
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticsearch.common.inject.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.elasticsearch.common.inject.*;
import org.elasticsearch.common.inject.spi.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Static utility methods for creating and working with instances of {@link Module}.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 * @since 2.0
 */
public final class Modules {
    private Modules() {
    }

    public static final Module EMPTY_MODULE = new Module() {
        public void configure(Binder binder) {
        }
    };

    /**
     * Returns a builder that creates a module that overlays override modules over the given
     * modules. If a key is bound in both sets of modules, only the binding from the override modules
     * is kept. This can be used to replace the bindings of a production module with test bindings:
     * <pre>
     * Module functionalTestModule
     *     = Modules.override(new ProductionModule()).with(new TestModule());
     * </pre>
     * <p/>
     * <p>Prefer to write smaller modules that can be reused and tested without overrides.
     *
     * @param modules the modules whose bindings are open to be overridden
     */
    public static OverriddenModuleBuilder override(Module... modules) {
        return new RealOverriddenModuleBuilder(Arrays.asList(modules));
    }

    /**
     * Returns a builder that creates a module that overlays override modules over the given
     * modules. If a key is bound in both sets of modules, only the binding from the override modules
     * is kept. This can be used to replace the bindings of a production module with test bindings:
     * <pre>
     * Module functionalTestModule
     *     = Modules.override(getProductionModules()).with(getTestModules());
     * </pre>
     * <p/>
     * <p>Prefer to write smaller modules that can be reused and tested without overrides.
     *
     * @param modules the modules whose bindings are open to be overridden
     */
    public static OverriddenModuleBuilder override(Iterable<? extends Module> modules) {
        return new RealOverriddenModuleBuilder(modules);
    }

    /**
     * Returns a new module that installs all of {@code modules}.
     */
    public static Module combine(Module... modules) {
        return combine(ImmutableSet.of(modules));
    }

    /**
     * Returns a new module that installs all of {@code modules}.
     */
    public static Module combine(Iterable<? extends Module> modules) {
        final Set<Module> modulesSet = ImmutableSet.copyOf(modules);
        return new Module() {
            public void configure(Binder binder) {
                binder = binder.skipSources(getClass());
                for (Module module : modulesSet) {
                    binder.install(module);
                }
            }
        };
    }

    /**
     * See the EDSL example at {@link Modules#override(Module[]) override()}.
     */
    public interface OverriddenModuleBuilder {

        /**
         * See the EDSL example at {@link Modules#override(Module[]) override()}.
         */
        Module with(Module... overrides);

        /**
         * See the EDSL example at {@link Modules#override(Module[]) override()}.
         */
        Module with(Iterable<? extends Module> overrides);
    }

    private static final class RealOverriddenModuleBuilder implements OverriddenModuleBuilder {
        private final ImmutableSet<Module> baseModules;

        private RealOverriddenModuleBuilder(Iterable<? extends Module> baseModules) {
            this.baseModules = ImmutableSet.copyOf(baseModules);
        }

        public Module with(Module... overrides) {
            return with(Arrays.asList(overrides));
        }

        public Module with(final Iterable<? extends Module> overrides) {
            return new AbstractModule() {
                @Override
                public void configure() {
                    final List<Element> elements = Elements.getElements(baseModules);
                    final List<Element> overrideElements = Elements.getElements(overrides);

                    final Set<Key> overriddenKeys = Sets.newHashSet();
                    final Set<Class<? extends Annotation>> overridesScopeAnnotations = Sets.newHashSet();

                    // execute the overrides module, keeping track of which keys and scopes are bound
                    new ModuleWriter(binder()) {
                        @Override
                        public <T> Void visit(Binding<T> binding) {
                            overriddenKeys.add(binding.getKey());
                            return super.visit(binding);
                        }

                        @Override
                        public Void visit(ScopeBinding scopeBinding) {
                            overridesScopeAnnotations.add(scopeBinding.getAnnotationType());
                            return super.visit(scopeBinding);
                        }

                        @Override
                        public Void visit(PrivateElements privateElements) {
                            overriddenKeys.addAll(privateElements.getExposedKeys());
                            return super.visit(privateElements);
                        }
                    }.writeAll(overrideElements);

                    // execute the original module, skipping all scopes and overridden keys. We only skip each
                    // overridden binding once so things still blow up if the module binds the same thing
                    // multiple times.
                    final Map<Scope, Object> scopeInstancesInUse = Maps.newHashMap();
                    final List<ScopeBinding> scopeBindings = Lists.newArrayList();
                    new ModuleWriter(binder()) {
                        @Override
                        public <T> Void visit(Binding<T> binding) {
                            if (!overriddenKeys.remove(binding.getKey())) {
                                super.visit(binding);

                                // Record when a scope instance is used in a binding
                                Scope scope = getScopeInstanceOrNull(binding);
                                if (scope != null) {
                                    scopeInstancesInUse.put(scope, binding.getSource());
                                }
                            }

                            return null;
                        }

                        @Override
                        public Void visit(PrivateElements privateElements) {
                            PrivateBinder privateBinder = binder.withSource(privateElements.getSource())
                                    .newPrivateBinder();

                            Set<Key<?>> skippedExposes = Sets.newHashSet();

                            for (Key<?> key : privateElements.getExposedKeys()) {
                                if (overriddenKeys.remove(key)) {
                                    skippedExposes.add(key);
                                } else {
                                    privateBinder.withSource(privateElements.getExposedSource(key)).expose(key);
                                }
                            }

                            // we're not skipping deep exposes, but that should be okay. If we ever need to, we
                            // have to search through this set of elements for PrivateElements, recursively
                            for (Element element : privateElements.getElements()) {
                                if (element instanceof Binding
                                        && skippedExposes.contains(((Binding) element).getKey())) {
                                    continue;
                                }
                                element.applyTo(privateBinder);
                            }

                            return null;
                        }

                        @Override
                        public Void visit(ScopeBinding scopeBinding) {
                            scopeBindings.add(scopeBinding);
                            return null;
                        }
                    }.writeAll(elements);

                    // execute the scope bindings, skipping scopes that have been overridden. Any scope that
                    // is overridden and in active use will prompt an error
                    new ModuleWriter(binder()) {
                        @Override
                        public Void visit(ScopeBinding scopeBinding) {
                            if (!overridesScopeAnnotations.remove(scopeBinding.getAnnotationType())) {
                                super.visit(scopeBinding);
                            } else {
                                Object source = scopeInstancesInUse.get(scopeBinding.getScope());
                                if (source != null) {
                                    binder().withSource(source).addError(
                                            "The scope for @%s is bound directly and cannot be overridden.",
                                            scopeBinding.getAnnotationType().getSimpleName());
                                }
                            }
                            return null;
                        }
                    }.writeAll(scopeBindings);

                    // TODO: bind the overridden keys using multibinder
                }

                private Scope getScopeInstanceOrNull(Binding<?> binding) {
                    return binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Scope>() {
                        public Scope visitScope(Scope scope) {
                            return scope;
                        }
                    });
                }
            };
        }
    }

    private static class ModuleWriter extends DefaultElementVisitor<Void> {
        protected final Binder binder;

        ModuleWriter(Binder binder) {
            this.binder = binder;
        }

        @Override
        protected Void visitOther(Element element) {
            element.applyTo(binder);
            return null;
        }

        void writeAll(Iterable<? extends Element> elements) {
            for (Element element : elements) {
                element.acceptVisitor(this);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4679.java