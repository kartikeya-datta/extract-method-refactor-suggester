error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12533.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12533.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 835
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12533.java
text:
```scala
public class ClassPathUtils {

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
p@@ackage org.apache.commons.lang3;

/**
 * Operations regarding the classpath.
 *
 * <p>The methods of this class do not allow {@code null} inputs.</p>
 *
 * @since 3.3
 * @version $Id$
 */
//@Immutable
public final class ClassPathUtils {

    /**
     * <p>{@code ClassPathUtils} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code ClassPathUtils.toFullyQualifiedName(MyClass.class, "MyClass.properties");}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public ClassPathUtils() {
        super();
    }

    /**
     * Returns the fully qualified name for the resource with name {@code resourceName} relative to the given context.
     *
     * <p>Note that this method does not check whether the resource actually exists.
     * It only constructs the name.
     * Null inputs are not allowed.</p>
     *
     * <pre>
     * ClassPathUtils.toFullyQualifiedName(StringUtils.class, "StringUtils.properties") = "org.apache.commons.lang3.StringUtils.properties"
     * </pre>
     *
     * @param context The context for constructing the name.
     * @param resourceName the resource name to construct the fully qualified name for.
     * @return the fully qualified name of the resource with name {@code resourceName}.
     * @throws java.lang.NullPointerException if either {@code context} or {@code resourceName} is null.
     */
    public static String toFullyQualifiedName(final Class context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context" );
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        return toFullyQualifiedName(context.getPackage(), resourceName);
    }

    /**
     * Returns the fully qualified name for the resource with name {@code resourceName} relative to the given context.
     *
     * <p>Note that this method does not check whether the resource actually exists.
     * It only constructs the name.
     * Null inputs are not allowed.</p>
     *
     * <pre>
     * ClassPathUtils.toFullyQualifiedName(StringUtils.class.getPackage(), "StringUtils.properties") = "org.apache.commons.lang3.StringUtils.properties"
     * </pre>
     *
     * @param context The context for constructing the name.
     * @param resourceName the resource name to construct the fully qualified name for.
     * @return the fully qualified name of the resource with name {@code resourceName}.
     * @throws java.lang.NullPointerException if either {@code context} or {@code resourceName} is null.
     */
    public static String toFullyQualifiedName(final Package context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context" );
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        StringBuilder sb = new StringBuilder();
        sb.append(context.getName());
        sb.append(".");
        sb.append(resourceName);
        return sb.toString();
    }

    /**
     * Returns the fully qualified path for the resource with name {@code resourceName} relative to the given context.
     *
     * <p>Note that this method does not check whether the resource actually exists.
     * It only constructs the path.
     * Null inputs are not allowed.</p>
     *
     * <pre>
     * ClassPathUtils.toFullyQualifiedPath(StringUtils.class, "StringUtils.properties") = "org/apache/commons/lang3/StringUtils.properties"
     * </pre>
     *
     * @param context The context for constructing the path.
     * @param resourceName the resource name to construct the fully qualified path for.
     * @return the fully qualified path of the resource with name {@code resourceName}.
     * @throws java.lang.NullPointerException if either {@code context} or {@code resourceName} is null.
     */
    public static String toFullyQualifiedPath(final Class context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context" );
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        return toFullyQualifiedPath(context.getPackage(), resourceName);
    }


    /**
     * Returns the fully qualified path for the resource with name {@code resourceName} relative to the given context.
     *
     * <p>Note that this method does not check whether the resource actually exists.
     * It only constructs the path.
     * Null inputs are not allowed.</p>
     *
     * <pre>
     * ClassPathUtils.toFullyQualifiedPath(StringUtils.class.getPackage(), "StringUtils.properties") = "org/apache/commons/lang3/StringUtils.properties"
     * </pre>
     *
     * @param context The context for constructing the path.
     * @param resourceName the resource name to construct the fully qualified path for.
     * @return the fully qualified path of the resource with name {@code resourceName}.
     * @throws java.lang.NullPointerException if either {@code context} or {@code resourceName} is null.
     */
    public static String toFullyQualifiedPath(final Package context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context" );
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        StringBuilder sb = new StringBuilder();
        sb.append(context.getName().replace('.', '/'));
        sb.append("/");
        sb.append(resourceName);
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12533.java