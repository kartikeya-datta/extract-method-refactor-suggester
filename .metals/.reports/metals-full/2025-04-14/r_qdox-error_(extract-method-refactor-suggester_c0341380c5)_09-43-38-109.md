error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6807.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6807.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6807.java
text:
```scala
i@@f (owner != null && !owner.isEmbeddable() && !owner.isAbstract())

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.jdbc.meta;

import java.lang.reflect.Modifier;

import org.apache.openjpa.jdbc.meta.strats.NoneFieldStrategy;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ValueMetaData;
import org.apache.openjpa.util.MetaDataException;

/**
 * Installer that attempts to use the given mapping information, and
 * fails if it does not work.
 *
 * @author Abe White
 * @nojavadoc
 * @since 0.4.0
 */
public class RuntimeStrategyInstaller
    extends StrategyInstaller {

    private static final Localizer _loc = Localizer.forPackage
        (RuntimeStrategyInstaller.class);

    /**
     * Constructor; supply configuration.
     */
    public RuntimeStrategyInstaller(MappingRepository repos) {
        super(repos);
    }

    public void installStrategy(ClassMapping cls) {
        if ((cls.getSourceMode() & cls.MODE_MAPPING) == 0)
            throw new MetaDataException(_loc.get("no-mapping", cls));

        ClassStrategy strat = repos.namedStrategy(cls);
        if (strat == null)
            strat = repos.defaultStrategy(cls, false);
        cls.setStrategy(strat, Boolean.FALSE);
    }

    public void installStrategy(FieldMapping field) {
        FieldStrategy strategy = null;
        ClassMapping owner = getOutermostDefiningMapping(field); 
        if (owner != null && !owner.isEmbeddable())
            strategy = repos.namedStrategy(field, true);
        if (strategy == null) {
            try {
                strategy = repos.defaultStrategy(field, true, false);
            } catch (MetaDataException mde) {
                // if the parent class is abstract and field is unmapped,
                // allow it to pass (assume subclasses map the field)
                Class cls = field.getDefiningMetaData().getDescribedType();
                if (!Modifier.isAbstract(cls.getModifiers())
 field.getMappedBy() != null
 field.getMappingInfo().hasSchemaComponents()
 field.getValueInfo().hasSchemaComponents()
 field.getElementMapping().getValueInfo().hasSchemaComponents()
 field.getKeyMapping().getValueInfo().hasSchemaComponents())
                    throw mde;

                strategy = NoneFieldStrategy.getInstance();
            }
        }
        field.setStrategy(strategy, Boolean.FALSE);
    }
    
    private ClassMapping getOutermostDefiningMapping(ValueMetaData vm) {
        if (vm instanceof FieldMapping) {
            ClassMapping owner = ((FieldMapping)vm).getDefiningMapping();
            ValueMetaData val = owner.getEmbeddingMetaData();
            if (val == null)
                return owner; 
            return getOutermostDefiningMapping(val);
        } else if (vm instanceof ValueMappingImpl) {
            FieldMapping owner = ((ValueMappingImpl)vm).getFieldMapping();
            return getOutermostDefiningMapping(owner);
        }
        return null;
    }

    public void installStrategy(Version version) {
        VersionStrategy strat = repos.namedStrategy(version);
        if (strat == null)
            strat = repos.defaultStrategy(version, false);
        version.setStrategy(strat, Boolean.FALSE);
    }

    public void installStrategy(Discriminator discrim) {
        DiscriminatorStrategy strat = repos.namedStrategy(discrim);
        if (strat == null)
            strat = repos.defaultStrategy(discrim, false);
        discrim.setStrategy(strat, Boolean.FALSE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6807.java