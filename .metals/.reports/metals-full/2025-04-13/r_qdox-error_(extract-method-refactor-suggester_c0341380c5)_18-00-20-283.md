error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2437.java
text:
```scala
r@@eturn this.tags.containsKey(tagName.toLowerCase());

package org.apache.lucene.queryParser.core.nodes;

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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.lucene.messages.NLS;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.util.StringUtils;

/**
 * A {@link QueryNodeImpl} is the default implementation of the interface
 * {@link QueryNode}
 */
public abstract class QueryNodeImpl implements QueryNode, Cloneable {

  /* index default field */
  // TODO remove PLAINTEXT_FIELD_NAME replacing it with configuration APIs
  public static final String PLAINTEXT_FIELD_NAME = "_plain";

  private boolean isLeaf = true;

  private Hashtable<String, Object> tags = new Hashtable<String, Object>();

  private List<QueryNode> clauses = null;

  protected void allocate() {

    if (this.clauses == null) {
      this.clauses = new ArrayList<QueryNode>();

    } else {
      this.clauses.clear();
    }

  }

  public final void add(QueryNode child) {

    if (isLeaf() || this.clauses == null || child == null) {
      throw new IllegalArgumentException(NLS
          .getLocalizedMessage(QueryParserMessages.NODE_ACTION_NOT_SUPPORTED));
    }

    this.clauses.add(child);
    ((QueryNodeImpl) child).setParent(this);

  }

  public final void add(List<QueryNode> children) {

    if (isLeaf() || this.clauses == null) {
      throw new IllegalArgumentException(NLS
          .getLocalizedMessage(QueryParserMessages.NODE_ACTION_NOT_SUPPORTED));
    }

    for (QueryNode child : children) {
      add(child);
    }

  }

  public boolean isLeaf() {
    return this.isLeaf;
  }

  public final void set(List<QueryNode> children) {

    if (isLeaf() || this.clauses == null) {
      ResourceBundle bundle = ResourceBundle
          .getBundle("org.apache.lucene.queryParser.messages.QueryParserMessages");
      String message = bundle.getObject("Q0008E.NODE_ACTION_NOT_SUPPORTED")
          .toString();

      throw new IllegalArgumentException(message);

    }

    // reset parent value
    for (QueryNode child : children) {

      ((QueryNodeImpl) child).setParent(null);

    }

    // allocate new children list
    allocate();

    // add new children and set parent
    for (QueryNode child : children) {
      add(child);
    }
  }

  public QueryNode cloneTree() throws CloneNotSupportedException {
    QueryNodeImpl clone = (QueryNodeImpl) super.clone();
    clone.isLeaf = this.isLeaf;

    // Reset all tags
    clone.tags = new Hashtable<String, Object>();

    // copy children
    if (this.clauses != null) {
      List<QueryNode> localClauses = new ArrayList<QueryNode>();
      for (QueryNode clause : this.clauses) {
        localClauses.add(clause.cloneTree());
      }
      clone.clauses = localClauses;
    }

    return clone;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return cloneTree();
  }

  protected void setLeaf(boolean isLeaf) {
    this.isLeaf = isLeaf;
  }

  /**
   * @return a List for QueryNode object. Returns null, for nodes that do not
   *         contain children. All leaf Nodes return null.
   */
  public final List<QueryNode> getChildren() {
    if (isLeaf() || this.clauses == null) {
      return null;
    }
    return this.clauses;
  }

  public void setTag(String tagName, Object value) {
    this.tags.put(tagName.toLowerCase(), value);
  }

  public void unsetTag(String tagName) {
    this.tags.remove(tagName.toLowerCase());
  }

  /** verify if a node contains a tag */
  public boolean containsTag(String tagName) {
    return this.tags.containsKey(tagName);
  }

  public Object getTag(String tagName) {
    return this.tags.get(tagName.toString().toLowerCase());
  }

  private QueryNode parent = null;

  private void setParent(QueryNode parent) {
    this.parent = parent;
  }

  public QueryNode getParent() {
    return this.parent;
  }

  protected boolean isRoot() {
    return getParent() == null;
  }

  /**
   * If set to true the the method toQueryString will not write field names
   */
  protected boolean toQueryStringIgnoreFields = false;

  /**
   * This method is use toQueryString to detect if fld is the default field
   * 
   * @param fld - field name
   * @return true if fld is the default field
   */
  // TODO: remove this method, it's commonly used by {@link
  // #toQueryString(org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax)}
  // to figure out what is the default field, however, {@link
  // #toQueryString(org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax)}
  // should receive the default field value directly by parameter
  protected boolean isDefaultField(CharSequence fld) {
    if (this.toQueryStringIgnoreFields)
      return true;
    if (fld == null)
      return true;
    if (QueryNodeImpl.PLAINTEXT_FIELD_NAME.equals(StringUtils.toString(fld)))
      return true;
    return false;
  }

  /**
   * Every implementation of this class should return pseudo xml like this:
   * 
   * For FieldQueryNode: <field start='1' end='2' field='subject' text='foo'/>
   * 
   * @see org.apache.lucene.queryParser.core.nodes.QueryNode#toString()
   */
  @Override
  public String toString() {
    return super.toString();
  }

  /**
   * Returns a map containing all tags attached to this query node.
   * 
   * @return a map containing all tags attached to this query node
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> getTagMap() {
    return (Map<String, Object>) this.tags.clone();
  }

} // end class QueryNodeImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2437.java