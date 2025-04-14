error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9283.java
text:
```scala
S@@impleJdbcCallOperations returningResultSet(String parameterName, RowMapper<?> rowMapper);

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jdbc.core.simple;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Interface specifying the API for a Simple JDBC Call implemented by {@link SimpleJdbcCall}.
 * This interface is not often used directly, but provides the
 * option to enhance testability, as it can easily be mocked or stubbed.
 *
 * @author Thomas Risberg
 * @since 2.5
 */
public interface SimpleJdbcCallOperations {

	/**
	 * Specify the procedure name to be used - this implies that we will be calling a stored procedure.
	 * @param procedureName the name of the stored procedure
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withProcedureName(String procedureName);

	/**
	 * Specify the procedure name to be used - this implies that we will be calling a stored function.
	 * @param functionName the name of the stored function
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withFunctionName(String functionName);

	/**
	 * Optionally, specify the name of the schema that contins the stored procedure.
	 * @param schemaName the name of the schema
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withSchemaName(String schemaName);

	/**
	 * Optionally, specify the name of the catalog that contins the stored procedure.
	 * To provide consistency with the Oracle DatabaseMetaData, this is used to specify the package name if
	 * the procedure is declared as part of a package.
	 * @param catalogName the catalog or package name
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withCatalogName(String catalogName);

	/**
	 * Indicates the procedure's return value should be included in the results returned.
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withReturnValue();

	/**
	 * Specify one or more parameters if desired. These parameters will be supplemented with any
	 * parameter information retrieved from the database meta data.
	 * Note that only parameters declared as {@code SqlParameter} and {@code SqlInOutParameter}
	 * will be used to provide input values.  This is different from the {@code StoredProcedure} class
	 * which for backwards compatibility reasons allows input values to be provided for parameters declared
	 * as {@code SqlOutParameter}.
	 * @param sqlParameters the parameters to use
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations declareParameters(SqlParameter... sqlParameters);

	/** Not used yet */
	SimpleJdbcCallOperations useInParameterNames(String... inParameterNames);

	/**
	 * Used to specify when a ResultSet is returned by the stored procedure and you want it mapped
	 * by a RowMapper. The results will be returned using the parameter name specified. Multiple
	 * ResultSets must be declared in the correct order. If the database you are using uses ref cursors
	 * then the name specified must match the name of the parameter declared for the procedure in the
	 * database.
	 * @param parameterName the name of the returned results and/or the name of the ref cursor parameter
	 * @param rowMapper the RowMapper implementation that will map the data returned for each row
	 * */
	SimpleJdbcCallOperations returningResultSet(String parameterName, RowMapper rowMapper);

	/**
	 * Turn off any processing of parameter meta data information obtained via JDBC.
	 * @return the instance of this SimpleJdbcCall
	 */
	SimpleJdbcCallOperations withoutProcedureColumnMetaDataAccess();


	/**
	 * Execute the stored function and return the results obtained as an Object of the specified return type.
	 * @param returnType the type of the value to return
	 * @param args optional array containing the in parameter values to be used in the call.
	 * Parameter values must be provided in the same order as the parameters are defined
	 * for the stored procedure.
	 */
	<T> T executeFunction(Class<T> returnType, Object... args);

	/**
	 * Execute the stored function and return the results obtained as an Object of the specified return type.
	 * @param returnType the type of the value to return
	 * @param args Map containing the parameter values to be used in the call.
	 */
	<T> T executeFunction(Class<T> returnType, Map<String, ?> args);

	/**
	 * Execute the stored function and return the results obtained as an Object of the specified return type.
	 * @param returnType the type of the value to return
	 * @param args MapSqlParameterSource containing the parameter values to be used in the call.
	 */
	<T> T executeFunction(Class<T> returnType, SqlParameterSource args);

	/**
	 * Execute the stored procedure and return the single out parameter as an Object of the specified return type.
	 * In the case where there are multiple out parameters, the first one is returned and additional out parameters
	 * are ignored.
	 * @param returnType the type of the value to return
	 * @param args optional array containing the in parameter values to be used in the call. Parameter values must
	 * be provided in the same order as the parameters are defined for the stored procedure.
	 */
	<T> T executeObject(Class<T> returnType, Object... args);

	/**
	 * Execute the stored procedure and return the single out parameter as an Object of the specified return type.
	 * In the case where there are multiple out parameters, the first one is returned and additional out parameters
	 * are ignored.
	 * @param returnType the type of the value to return
	 * @param args Map containing the parameter values to be used in the call.
	 */
	<T> T executeObject(Class<T> returnType, Map<String, ?> args);

	/**
	 * Execute the stored procedure and return the single out parameter as an Object of the specified return type.
	 * In the case where there are multiple out parameters, the first one is returned and additional out parameters
	 * are ignored.
	 * @param returnType the type of the value to return
	 * @param args MapSqlParameterSource containing the parameter values to be used in the call.
	 */
	<T> T executeObject(Class<T> returnType, SqlParameterSource args);

	/**
	 * Execute the stored procedure and return a map of output params, keyed by name as in parameter declarations.
	 * @param args optional array containing the in parameter values to be used in the call. Parameter values must
	 * be provided in the same order as the parameters are defined for the stored procedure.
	 * @return map of output params.
	 */
	Map<String, Object> execute(Object... args);

	/**
	 * Execute the stored procedure and return a map of output params, keyed by name as in parameter declarations..
	 * @param args Map containing the parameter values to be used in the call.
	 * @return map of output params.
	 */
	Map<String, Object> execute(Map<String, ?> args);

	/**
	 * Execute the stored procedure and return a map of output params, keyed by name as in parameter declarations..
	 * @param args SqlParameterSource containing the parameter values to be used in the call.
	 * @return map of output params.
	 */
	Map<String, Object> execute(SqlParameterSource args);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9283.java