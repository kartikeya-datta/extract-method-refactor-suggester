error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14423.java
text:
```scala
c@@onfiguration.fluent().mode(mode);

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

package org.jboss.as.clustering.infinispan.subsystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.util.EnumMap;
import java.util.Map;

import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.config.InfinispanConfiguration;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.util.loading.ContextClassLoaderSwitcher;
import org.jboss.util.loading.ContextClassLoaderSwitcher.SwitchContext;

/**
 * Service that provides infinispan cache configuration defaults per cache mode.
 * @author Paul Ferraro
 */
public class EmbeddedCacheManagerDefaultsService implements Service<EmbeddedCacheManagerDefaults> {
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append(InfinispanExtension.SUBSYSTEM_NAME, "config", "defaults");

    private static final String DEFAULTS = "infinispan-defaults.xml";
    private static final Logger log = Logger.getLogger(CacheContainerAdd.class.getPackage().getName());

    @SuppressWarnings("unchecked")
    private static final ContextClassLoaderSwitcher switcher = (ContextClassLoaderSwitcher) AccessController.doPrivileged(ContextClassLoaderSwitcher.INSTANTIATOR);

    private volatile EmbeddedCacheManagerDefaults defaults;
    private final String resource;

    public EmbeddedCacheManagerDefaultsService() {
        this(DEFAULTS);
    }

    public EmbeddedCacheManagerDefaultsService(String resource) {
        this.resource = resource;
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.msc.value.Value#getValue()
     */
    @Override
    public EmbeddedCacheManagerDefaults getValue() throws IllegalStateException, IllegalArgumentException {
        return this.defaults;
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.msc.service.Service#start(org.jboss.msc.service.StartContext)
     */
    @Override
    public void start(StartContext context) throws StartException {
        InfinispanConfiguration config = load(this.resource);
        Defaults defaults = new Defaults(config.parseGlobalConfiguration());
        Configuration defaultConfig = config.parseDefaultConfiguration();
        Map<String, Configuration> namedConfigs = config.parseNamedConfigurations();
        for (Configuration.CacheMode mode: Configuration.CacheMode.values()) {
            Configuration configuration = defaultConfig.clone();
            Configuration overrides = namedConfigs.get(mode.name());
            if (overrides != null) {
                configuration.applyOverrides(overrides);
            }
            configuration.setCacheMode(mode);
            defaults.add(mode, configuration);
        }
        this.defaults = defaults;
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.msc.service.Service#stop(org.jboss.msc.service.StopContext)
     */
    @Override
    public void stop(StopContext context) {
        this.defaults = null;
    }

    private static InfinispanConfiguration load(String resource) throws StartException {
        URL url = find(resource, InfinispanExtension.class.getClassLoader());
        log.debugf("Loading Infinispan defaults from %s", url.toString());
        try {
            InputStream input = url.openStream();
            SwitchContext context = switcher.getSwitchContext(InfinispanConfiguration.class.getClassLoader());
            try {
                return InfinispanConfiguration.newInfinispanConfiguration(input);
            } finally {
                context.reset();
                try {
                    input.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new StartException(String.format("Failed to parse %s", url), e);
        }
    }

    private static URL find(String resource, ClassLoader... loaders) throws StartException {
        for (ClassLoader loader: loaders) {
            if (loader != null) {
                URL url = loader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        throw new StartException(String.format("Failed to locate %s", resource));
    }

    class Defaults implements EmbeddedCacheManagerDefaults {
        private final GlobalConfiguration global;
        private final Map<Configuration.CacheMode, Configuration> configs = new EnumMap<Configuration.CacheMode, Configuration>(Configuration.CacheMode.class);

        Defaults(GlobalConfiguration global) {
            this.global = global;
        }

        void add(Configuration.CacheMode mode, Configuration config) {
            this.configs.put(mode, config);
        }

        /**
         * {@inheritDoc}
         * @see org.jboss.as.clustering.infinispan.subsystem.EmbeddedCacheManagerDefaults#getGlobalConfiguration()
         */
        @Override
        public GlobalConfiguration getGlobalConfiguration() {
            return this.global;
        }

        /**
         * {@inheritDoc}
         * @see org.jboss.as.clustering.infinispan.subsystem.EmbeddedCacheManagerDefaults#getDefaultConfiguration(org.infinispan.config.Configuration.CacheMode)
         */
        @Override
        public Configuration getDefaultConfiguration(CacheMode mode) {
            return this.configs.get(mode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14423.java