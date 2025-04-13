error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3786.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3786.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3786.java
text:
```scala
r@@eturn _id;

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
package org.apache.openjpa.util;

import java.security.AccessController;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import serp.util.Numbers;

/**
 * Datastore identity type. Implementations may choose to use this type,
 * or choose to use their own datastore identity values.
 *
 * @author Abe White
 */
public final class Id
    extends OpenJPAId {

    private static final Localizer _loc = Localizer.forPackage(Id.class);

    private final long _id;

    /**
     * Create an id from the given type and value; the value might be an
     * id instnace, a stringified id, or a primary key value.
     */
    public static Id newInstance(Class cls, Object val) {
        if (val instanceof Id)
            return (Id) val;
        if (val instanceof String)
            return new Id(cls, (String) val);
        if (val instanceof Number)
            return new Id(cls, ((Number) val).longValue());
        if (val == null)
            return new Id(cls, 0L);
        throw new UserException(_loc.get("unknown-oid", cls, val,
            val.getClass()));
    }

    /**
     * Create an id from the result of a {@link #toString} call on another
     * instance.
     */
    public Id(String str) {
        this(str, (ClassLoader) null);
    }

    /**
     * Create an id from the result of an {@link #toString} call on another
     * instance.
     */
    public Id(String str, OpenJPAConfiguration conf, ClassLoader brokerLoader) {
        this(str, (conf == null) ? brokerLoader : conf.
            getClassResolverInstance().getClassLoader(Id.class, brokerLoader));
    }

    /**
     * Create an id from the result of an {@link #toString} call on another
     * instance.
     */
    public Id(String str, ClassLoader loader) {
        if (loader == null)
            loader = AccessController.doPrivileged(
                J2DoPrivHelper.getContextClassLoaderAction());

        if (str == null)
            _id = 0L;
        else {
            int dash = str.indexOf('-');
            try {
                type = Class.forName(str.substring(0, dash), true, loader);
            } catch (Throwable t) {
                throw new UserException(_loc.get("string-id", str), t);
            }
            _id = Long.parseLong(str.substring(dash + 1));
        }
    }

    /**
     * Construct from the result of a {@link #toString} call on another
     * instance.
     */
    public Id(Class cls, String key) {
        super(cls);

        if (key == null)
            _id = 0L;
        else {
            // allow either stringified long or result of Id.toString
            int dash = key.indexOf('-');
            if (dash > 0) // don't check for -1; might be negative number
                key = key.substring(dash + 1);
            _id = Long.parseLong(key);
        }
    }

    /**
     * Construct from key value.
     */
    public Id(Class cls, Long key) {
        this(cls, (key == null) ? 0L : key.longValue());
    }

    /**
     * Construct from key value.
     */
    public Id(Class cls, long key) {
        super(cls);
        _id = key;
    }

    /**
     * Construct from key value.
     */
    public Id(Class cls, long key, boolean subs) {
        super(cls, subs);
        _id = key;
    }

    /**
     * Primary key.
     */
    public long getId() {
        return _id;
    }

    public Object getIdObject() {
        return Numbers.valueOf(_id);
    }

    protected int idHash() {
        return (int) (_id ^ (_id >>> 32));
    }

    protected boolean idEquals(OpenJPAId other) {
        return _id == ((Id) other)._id;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3786.java