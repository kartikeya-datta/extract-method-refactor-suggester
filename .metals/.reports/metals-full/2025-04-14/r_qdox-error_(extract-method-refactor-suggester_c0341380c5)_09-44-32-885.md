error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3563.java
text:
```scala
R@@aised if, the Derby database connection is null.

/*

   Derby - Class org.apache.derby.impl.load.LoadError

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.load;

import java.sql.SQLException;
import org.apache.derby.iapi.error.ExceptionSeverity;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.error.PublicAPI;

/**
 * These exceptions are thrown by the import and export modules.
 * 
 *
	@see SQLException
 */
class LoadError {
	
	/**
	 Raised if, the Cloudscape database connection is null.
	*/

	static SQLException connectionNull() {
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.CONNECTION_NULL));
	}

	/**
	   Raised if, there is data found between the stop delimiter and field/record spearator.
	   @param lineNumber Found invalid data on this line number in the data file
	   @param columnNumber Found invalid data for this column number in the data file
	*/
	static SQLException dataAfterStopDelimiter(int lineNumber, int columnNumber) {
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.DATA_AFTER_STOP_DELIMITER,
											  new Integer(lineNumber),new Integer(columnNumber)));				 	
	}

	/**
	   Raised if, the passed data file can't be found.
	   @param fileName the data file name 
	*/
	static SQLException dataFileNotFound(String fileName) {

		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.DATA_FILE_NOT_FOUND, fileName));
	}

  
	/**
	   Raised if, null is passed for data file url.
	*/
	static SQLException dataFileNull() {
    return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.DATA_FILE_NULL));
	}

	/**
	   Raised if, the entity (ie table/view) for import/export is missing in the database.
	*/

	static SQLException entityNameMissing() {
    return PublicAPI.wrapStandardException(
		   StandardException.newException(SQLState.ENTITY_NAME_MISSING));

	}


	/**
	   Raised if, field & record separators are substring of each other.
	*/
	static SQLException fieldAndRecordSeparatorsSubset() {
		return  PublicAPI.wrapStandardException(
				StandardException.newException(SQLState.FIELD_IS_RECORD_SEPERATOR_SUBSET));
	}

	/**
	   Raised if, no column by given name is found in the resultset while importing.
	   @param columnName the resultset doesn't have this column name
	*/
	static SQLException invalidColumnName(String columnName) {
		return  PublicAPI.wrapStandardException(
				StandardException.newException(SQLState.INVALID_COLUMN_NAME , columnName));

	}


	/**
	   Raised if, no column by given number is found in the resultset while importing.
	   @param numberOfColumns the resultset doesn't have this column number
	*/
	static SQLException invalidColumnNumber(int numberOfColumns) {
		
		return PublicAPI.wrapStandardException(
				StandardException.newException(SQLState.INVALID_COLUMN_NUMBER,
											   new Integer(numberOfColumns)
											   ));
	}

	/**
	   Raised if, trying to export/import from an entity which has non supported
	   type columns in it.
	*/
	static SQLException nonSupportedTypeColumn(String columnName, String typeName) {
		return  PublicAPI.wrapStandardException(
				StandardException.newException(SQLState.UNSUPPORTED_COLUMN_TYPE,
											   columnName,
											   typeName));
	}


	/**
	   Raised if, in case of fixed format, don't find the record separator for a row in the data file.
	   @param lineNumber the line number with the missing record separator in the data file
	*/
	static SQLException recordSeparatorMissing(int lineNumber) {

		return  PublicAPI.wrapStandardException(
				StandardException.newException(SQLState.RECORD_SEPERATOR_MISSING,
											   new Integer(lineNumber)));
	}

	/**
	   Raised if, in case of fixed format, reach end of file before reading data for all the columns.
	*/
	static SQLException unexpectedEndOfFile(int lineNumber) {
    return  PublicAPI.wrapStandardException(
			StandardException.newException(SQLState.UNEXPECTED_END_OF_FILE,
										   new Integer(lineNumber)));
	}

	/**
	   Raised if, got IOException while writing data to the file.
	*/
	static SQLException errorWritingData() {
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.ERROR_WRITING_DATA));
	}


	/*
	 * Raised if period(.) is used a character delimiter
	 */
	static SQLException periodAsCharDelimiterNotAllowed()
	{
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.PERIOD_AS_CHAR_DELIMITER_NOT_ALLOWED));
	}

	/*
	 * Raised if same delimiter character is used for more than one delimiter
	 * type . For eg using ';' for both column delimter and character delimter
	 */
	static SQLException delimitersAreNotMutuallyExclusive()
	{
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.DELIMITERS_ARE_NOT_MUTUALLY_EXCLUSIVE));
	}


	static SQLException tableNotFound(String tableName)
	{
	
		return PublicAPI.wrapStandardException(
			   StandardException.newException(SQLState.TABLE_NOT_FOUND, tableName));
	}

		
	/* Wrapper to throw an unknown excepton duing Import/Export.
	 * Typically this can be some IO error which is not generic error
	 * like the above error messages. 
	 */

	static SQLException unexpectedError(Throwable t )
	{
		if (!(t instanceof SQLException))  
		{
			return PublicAPI.wrapStandardException(StandardException.plainWrapException(t));
		}
		else
			return (SQLException) t;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3563.java