error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,27]

error in qdox parser
file content:
```java
offset: 27
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2880.java
text:
```scala
"SELECT COUNT(1) FROM " + p@@referenceTable + " tp1 INNER JOIN " + preferenceTable + " tp2 " +

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

package org.apache.mahout.cf.taste.impl.model.jdbc;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;

import javax.sql.DataSource;

/**
 * <p>A {@link DataModel} backed by a MySQL database and accessed via JDBC. It may work with other
 * JDBC databases. By default, this class assumes that there is a {@link DataSource} available under the
 * JNDI name "jdbc/taste", which gives access to a database with a "taste_preferences" table with the
 * following schema:</p>
 *
 * <table>
 * <tr><th>user_id</th><th>item_id</th><th>preference</th></tr>
 * <tr><td>ABC</td><td>123</td><td>0.9</td></tr>
 * <tr><td>ABC</td><td>456</td><td>0.1</td></tr>
 * <tr><td>DEF</td><td>123</td><td>0.2</td></tr>
 * <tr><td>DEF</td><td>789</td><td>0.3</td></tr>
 * </table>
 *
 * <p><code>user_id</code> must have a type compatible with the Java <code>String</code> type.
 * <code>item_id</code> must have a type compatible with the Java <code>String</code> type.
 * <code>preference</code> must have a type compatible with the Java <code>double</code> type.
 * For example, the following command sets up a suitable table in MySQL, complete with
 * primary key and indexes:</p>
 *
 * <pre>
 * CREATE TABLE taste_preferences (
 *   user_id VARCHAR(10) NOT NULL,
 *   item_id VARCHAR(10) NOT NULL,
 *   preference FLOAT NOT NULL,
 *   PRIMARY KEY (user_id, item_id),
 *   INDEX (user_id),
 *   INDEX (item_id)
 * )
 * </pre>
 *
 * <h3>Performance Notes</h3>
 *
 * <p>See the notes in {@link AbstractJDBCDataModel} regarding using connection pooling. It's pretty vital
 * to performance.</p>
 *
 * <p>Some experimentation suggests that MySQL's InnoDB engine is faster than MyISAM for these kinds of
 * applications. While MyISAM is the default and, I believe, generally considered the lighter-weight and faster
 * of the two engines, my guess is the row-level locking of InnoDB helps here. Your mileage may vary.</p>
 *
 * <p>Here are some key settings that can be tuned for MySQL, and suggested size for a data set of around
 * 1 million elements:</p>
 *
 * <ul>
 * <li>innodb_buffer_pool_size=64M</li>
 * <li>myisam_sort_buffer_size=64M</li>
 * <li>query_cache_limit=64M</li>
 * <li>query_cache_min_res_unit=512K</li>
 * <li>query_cache_type=1</li>
 * <li>query_cache_size=64M</li>
 * </ul>
 *
 * <p>Thanks to Amila Jayasooriya for contributing MySQL notes above as part of Google Summer of Code 2007.</p>
 */
public class MySQLJDBCDataModel extends AbstractJDBCDataModel {

  /**
   * <p>Creates a {@link MySQLJDBCDataModel} using the default {@link DataSource}
   * (named {@link #DEFAULT_DATASOURCE_NAME} and default table/column names.</p>
   *
   * @throws TasteException if {@link DataSource} can't be found
   */
  public MySQLJDBCDataModel() throws TasteException {
    this(DEFAULT_DATASOURCE_NAME);
  }

  /**
   * <p>Creates a {@link MySQLJDBCDataModel} using the default {@link DataSource}
   * found under the given name, and using default table/column names.</p>
   *
   * @param dataSourceName name of {@link DataSource} to look up
   * @throws TasteException if {@link DataSource} can't be found
   */
  public MySQLJDBCDataModel(String dataSourceName) throws TasteException {
    this(lookupDataSource(dataSourceName),
         DEFAULT_PREFERENCE_TABLE,
         DEFAULT_USER_ID_COLUMN,
         DEFAULT_ITEM_ID_COLUMN,
         DEFAULT_PREFERENCE_COLUMN);
  }

  /**
   * <p>Creates a {@link MySQLJDBCDataModel} using the given {@link DataSource}
   * and default table/column names.</p>
   *
   * @param dataSource {@link DataSource} to use
   */
  public MySQLJDBCDataModel(DataSource dataSource) {
    this(dataSource,
         DEFAULT_PREFERENCE_TABLE,
         DEFAULT_USER_ID_COLUMN,
         DEFAULT_ITEM_ID_COLUMN,
         DEFAULT_PREFERENCE_COLUMN);
  }

  /**
   * <p>Creates a {@link MySQLJDBCDataModel} using the given {@link DataSource}
   * and default table/column names.</p>
   *
   * @param dataSource {@link DataSource} to use
   * @param preferenceTable name of table containing preference data
   * @param userIDColumn user ID column name
   * @param itemIDColumn item ID column name
   * @param preferenceColumn preference column name
   */
  public MySQLJDBCDataModel(DataSource dataSource,
                            String preferenceTable,
                            String userIDColumn,
                            String itemIDColumn,
                            String preferenceColumn) {
    super(dataSource,
          preferenceTable,
          userIDColumn,
          itemIDColumn,
          preferenceColumn,
          // getUserSQL
          "SELECT " + itemIDColumn + ", " + preferenceColumn + " FROM " + preferenceTable +
          " WHERE " + userIDColumn + "=? ORDER BY " + itemIDColumn,
          // getNumItemsSQL
          "SELECT COUNT(DISTINCT " + itemIDColumn + ") FROM " + preferenceTable,
          // getNumUsersSQL
          "SELECT COUNT(DISTINCT " + userIDColumn + ") FROM " + preferenceTable,
          // setPreferenceSQL
          "INSERT INTO " + preferenceTable + '(' + userIDColumn + ',' + itemIDColumn + ',' + preferenceColumn + 
          ") VALUES (?,?,?) ON DUPLICATE KEY UPDATE " + preferenceColumn + "=?",
          // removePreference SQL
          "DELETE FROM " + preferenceTable + " WHERE " + userIDColumn + "=? AND " + itemIDColumn + "=?",
          // getUsersSQL
          "SELECT " + itemIDColumn + ", " + preferenceColumn + ", " + userIDColumn + " FROM " +
          preferenceTable + " ORDER BY " + userIDColumn + ", " + itemIDColumn,
          // getItemsSQL
          "SELECT DISTINCT " + itemIDColumn + " FROM " + preferenceTable + " ORDER BY " + itemIDColumn,
          // getItemSQL
          "SELECT 1 FROM " + preferenceTable + " WHERE " + itemIDColumn + "=?",
          // getPrefsForItemSQL
          "SELECT " + preferenceColumn + ", " + userIDColumn + " FROM " +
          preferenceTable + " WHERE " + itemIDColumn + "=? ORDER BY " + userIDColumn,
          // getNumPreferenceForItemSQL
          "SELECT COUNT(1) FROM " + preferenceTable + " WHERE " + itemIDColumn + "=?",
          // getNumPreferenceForItemsSQL
          "SELECT COUNT(1) FROM " + preferenceTable + " tp1 INNER JOIN " + preferenceColumn + " tp2 " +
          "ON (tp1." + userIDColumn + "=tp2." + userIDColumn + ") " +
          "WHERE tp1." + itemIDColumn + "=? and tp2." + itemIDColumn + "=?");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2880.java