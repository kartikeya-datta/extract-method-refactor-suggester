error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2801.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2801.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2801.java
text:
```scala
C@@lass<?>[] classes = clazz.getInterfaces();

package org.apache.lucene.queryParser.core.builders;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.FieldableNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.util.StringUtils;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

/**
 * This class should be used when there is a builder for each type of node.
 * 
 * The type of node may be defined in 2 different ways: - by the field name,
 * when the node implements the {@link FieldableNode} interface - by its class,
 * it keeps checking the class and all the interfaces and classes this class
 * implements/extends until it finds a builder for that class/interface
 * 
 * This class always check if there is a builder for the field name before it
 * checks for the node class. So, field name builders have precedence over class
 * builders.
 * 
 * When a builder is found for a node, it's called and the node is passed to the
 * builder. If the returned built object is not <code>null</code>, it's tagged
 * on the node using the tag {@link QueryTreeBuilder#QUERY_TREE_BUILDER_TAGID}.
 * 
 * The children are usually built before the parent node. However, if a builder
 * associated to a node is an instance of {@link QueryTreeBuilder}, the node is
 * delegated to this builder and it's responsible to build the node and its
 * children.
 * 
 * @see QueryBuilder
 */
public class QueryTreeBuilder implements QueryBuilder {

  /**
   * This tag is used to tag the nodes in a query tree with the built objects
   * produced from their own associated builder.
   */
  public static final String QUERY_TREE_BUILDER_TAGID = QueryTreeBuilder.class
      .getName();

  private HashMap<Class<? extends QueryNode>, QueryBuilder> queryNodeBuilders;

  private HashMap<String, QueryBuilder> fieldNameBuilders;

  /**
   * {@link QueryTreeBuilder} constructor.
   */
  public QueryTreeBuilder() {
    // empty constructor
  }

  /**
   * Associates a field name with a builder.
   * 
   * @param fieldName the field name
   * @param builder the builder to be associated
   */
  public void setBuilder(String fieldName, QueryBuilder builder) {

    if (this.fieldNameBuilders == null) {
      this.fieldNameBuilders = new HashMap<String, QueryBuilder>();
    }

    this.fieldNameBuilders.put(fieldName.toString(), builder);

  }

  /**
   * Associates a field name with a builder.
   * 
   * @param fieldName the field name
   * @param builder the builder to be associated
   * 
   * @deprecated use {@link #setBuilder(String, QueryBuilder)} instead
   */
  @Deprecated
  public void setBuilder(CharSequence fieldName, QueryBuilder builder) {
    setBuilder(StringUtils.toString(fieldName), builder);
  }

  /**
   * Associates a class with a builder
   * 
   * @param queryNodeClass the class
   * @param builder the builder to be associated
   */
  public void setBuilder(Class<? extends QueryNode> queryNodeClass,
      QueryBuilder builder) {

    if (this.queryNodeBuilders == null) {
      this.queryNodeBuilders = new HashMap<Class<? extends QueryNode>, QueryBuilder>();
    }

    this.queryNodeBuilders.put(queryNodeClass, builder);

  }

  private void process(QueryNode node) throws QueryNodeException {

    if (node != null) {
      QueryBuilder builder = getBuilder(node);

      if (!(builder instanceof QueryTreeBuilder)) {
        List<QueryNode> children = node.getChildren();

        if (children != null) {

          for (QueryNode child : children) {
            process(child);
          }

        }

      }

      processNode(node, builder);

    }

  }

  private QueryBuilder getBuilder(QueryNode node) {
    QueryBuilder builder = null;

    if (this.fieldNameBuilders != null && node instanceof FieldableNode) {
      builder = this.fieldNameBuilders.get(StringUtils
          .toString(((FieldableNode) node).getField()));
    }

    if (builder == null && this.queryNodeBuilders != null) {

      Class<?> clazz = node.getClass();

      do {
        builder = getQueryBuilder(clazz);

        if (builder == null) {
          Class<?>[] classes = node.getClass().getInterfaces();

          for (Class<?> actualClass : classes) {
            builder = getQueryBuilder(actualClass);

            if (builder != null) {
              break;
            }

          }

        }

      } while (builder == null && (clazz = clazz.getSuperclass()) != null);

    }

    return builder;

  }

  private void processNode(QueryNode node, QueryBuilder builder)
      throws QueryNodeException {

    if (builder == null) {

      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, node
              .toQueryString(new EscapeQuerySyntaxImpl()), node.getClass()
              .getName()));

    }

    Object obj = builder.build(node);

    if (obj != null) {
      node.setTag(QUERY_TREE_BUILDER_TAGID, obj);
    }

  }

  private QueryBuilder getQueryBuilder(Class<?> clazz) {

    if (QueryNode.class.isAssignableFrom(clazz)) {
      return this.queryNodeBuilders.get(clazz);
    }

    return null;

  }

  /**
   * Builds some kind of object from a query tree. Each node in the query tree
   * is built using an specific builder associated to it.
   * 
   * @param queryNode the query tree root node
   * 
   * @return the built object
   * 
   * @throws QueryNodeException if some node builder throws a
   *         {@link QueryNodeException} or if there is a node which had no
   *         builder associated to it
   */
  public Object build(QueryNode queryNode) throws QueryNodeException {
    process(queryNode);

    return queryNode.getTag(QUERY_TREE_BUILDER_TAGID);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2801.java