error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9010.java
text:
```scala
r@@eturn map.get(key);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.MathException;

/**
 * This TansformerMap automates the transformation of mixed object types.
 * It provides a means to set NumberTransformers that will be selected 
 * based on the Class of the object handed to the Maps
 * <code>double transform(Object o)</code> method.
 * @version $Revision$ $Date$
 */
public class TransformerMap implements NumberTransformer, Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = 4605318041528645258L;

    /**
     * A default Number Transformer for Numbers and numeric Strings.
     */
    private NumberTransformer defaultTransformer = null;

    /**
     * The internal Map.
     */
    private Map<Class<?>, NumberTransformer> map = null;

    /**
     * Build a map containing only the default transformer.
     */
    public TransformerMap() {
        map = new HashMap<Class<?>, NumberTransformer>();
        defaultTransformer = new DefaultTransformer();
    }

    /**
     * Tests if a Class is present in the TransformerMap.
     * @param key Class to check
     * @return true|false
     */
    public boolean containsClass(Class<?> key) {
        return map.containsKey(key);
    }

    /**
     * Tests if a NumberTransformer is present in the TransformerMap.
     * @param value NumberTransformer to check
     * @return true|false
     */
    public boolean containsTransformer(NumberTransformer value) {
        return map.containsValue(value);
    }

    /**
     * Returns the Transformer that is mapped to a class
     * if mapping is not present, this returns null.
     * @param key The Class of the object
     * @return the mapped NumberTransformer or null.
     */
    public NumberTransformer getTransformer(Class<?> key) {
        return (NumberTransformer) map.get(key);
    }

    /**
     * Sets a Class to Transformer Mapping in the Map. If
     * the Class is already present, this overwrites that
     * mapping.
     * @param key The Class
     * @param transformer The NumberTransformer
     * @return the replaced transformer if one is present
     */
    public NumberTransformer putTransformer(Class<?> key, NumberTransformer transformer) {
        return map.put(key, transformer);
    }

    /**
     * Removes a Class to Transformer Mapping in the Map.
     * @param key The Class
     * @return the removed transformer if one is present or
     * null if none was present.
     */
    public NumberTransformer removeTransformer(Class<?> key) {
        return map.remove(key);
    }

    /**
     * Clears all the Class to Transformer mappings.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns the Set of Classes used as keys in the map.
     * @return Set of Classes
     */
    public Set<Class<?>> classes() {
        return map.keySet();
    }

    /**
     * Returns the Set of NumberTransformers used as values 
     * in the map.
     * @return Set of NumberTransformers
     */
    public Collection<NumberTransformer> transformers() {
        return map.values();
    }

    /**
     * Attempts to transform the Object against the map of
     * NumberTransformers. Otherwise it returns Double.NaN.
     * 
     * @param o the Object to be transformed.
     * @return the double value of the Object.
     * @throws MathException if the Object can not be transformed into a Double. 
     * @see org.apache.commons.math.util.NumberTransformer#transform(java.lang.Object)
     */
    public double transform(Object o) throws MathException {
        double value = Double.NaN;

        if (o instanceof Number || o instanceof String) {
            value = defaultTransformer.transform(o);
        } else {
            NumberTransformer trans = getTransformer(o.getClass());
            if (trans != null) {
                value = trans.transform(o);
            }
        }

        return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9010.java