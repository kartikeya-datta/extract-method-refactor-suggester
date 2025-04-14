error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15098.java
text:
```scala
M@@etaDataRepository repos = conf.getMetaDataRepositoryInstance().

/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.openjpa.persistence;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PCClassFileTransformer;
import org.apache.openjpa.kernel.Bootstrap;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.meta.MetaDataModes;
import org.apache.openjpa.meta.MetaDataRepository;


/**
 * Bootstrapping class that allows the creation of a stand-alone
 * {@link EntityManager}.
 *
 * @see Persistence#createEntityManagerFactory(String,Map)
 */
public class PersistenceProviderImpl
    implements PersistenceProvider {

    static final String CLASS_TRANSFORMER_OPTIONS =
        "openjpa.ClassTransformerOptions";

    /**
     * Loads the entity manager specified by <code>name</code>, applying
     * the properties in <code>m</code> as overrides to the properties defined
     * in the XML configuration file for <code>name</code>. If <code>name</code>
     * is <code>null</code>, this method loads the XML in the resource
     * identified by <code>resource</code>, and uses the first resource found
     * when doing this lookup, regardless of the name specified in the XML
     * resource or the name of the jar that the resource is contained in.
     *  This does no pooling of EntityManagersFactories.
     */
    public EntityManagerFactory createEntityManagerFactory(String name,
        String resource, Map m) {
        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        try {
            if (cp.load(name, resource, m))
                return OpenJPAPersistence.toEntityManagerFactory(
                    Bootstrap.newBrokerFactory(cp, cp.getClassLoader()));
            else
                return null;
        } catch (Exception e) {
            throw PersistenceExceptions.toPersistenceException(e);
        }
    }

    public EntityManagerFactory createEntityManagerFactory(String name, Map m) {
        return createEntityManagerFactory(name, null, m);
    }

    public EntityManagerFactory createContainerEntityManagerFactory(
        PersistenceUnitInfo pui, Map map) {
        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        try {
            if (cp.load(pui, map)) {
                OpenJPAEntityManagerFactory emf =
                    OpenJPAPersistence.toEntityManagerFactory(
                        Bootstrap.newBrokerFactory(cp, cp.getClassLoader()));
                Properties p = pui.getProperties();
                String ctOpts = null;
                if (p != null)
                    ctOpts = p.getProperty(CLASS_TRANSFORMER_OPTIONS);
                pui.addTransformer(new ClassTransformerImpl(
                    emf.getConfiguration(), ctOpts,
                    pui.getNewTempClassLoader()));
                return emf;
            } else
                return null;
        } catch (Exception e) {
            throw PersistenceExceptions.toPersistenceException(e);
        }
    }

    /**
     * Java EE 5 class transformer.
     */
    private static class ClassTransformerImpl
        implements ClassTransformer {

        private final ClassFileTransformer _trans;

        private ClassTransformerImpl(OpenJPAConfiguration conf, String options,
            ClassLoader tempClassLoader) {
            MetaDataRepository repos = conf.getMetaDataRepository().
                newInstance();
            repos.setResolve(MetaDataModes.MODE_MAPPING, false);
            _trans = new PCClassFileTransformer(repos,
                Configurations.parseProperties(options), tempClassLoader);
        }

        public byte[] transform(ClassLoader cl, String name,
            Class<?> previousVersion, ProtectionDomain pd, byte[] bytes)
            throws IllegalClassFormatException {
            return _trans.transform(cl, name, previousVersion, pd, bytes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15098.java