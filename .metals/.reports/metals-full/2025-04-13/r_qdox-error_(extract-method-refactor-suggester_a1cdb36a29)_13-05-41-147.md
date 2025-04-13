error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2710.java
text:
```scala
<@@LI> UNICODE BIT/BOOLEAN - not nullable.  Always true.

/*

   Derby - Class org.apache.derby.diag.StatementCache

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

package org.apache.derby.diag;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.reference.Limits;
import org.apache.derby.iapi.services.cache.CacheManager;
import org.apache.derby.iapi.services.context.ContextService;
import org.apache.derby.iapi.sql.ResultColumnDescriptor;
import org.apache.derby.iapi.sql.conn.LanguageConnectionContext;
import org.apache.derby.iapi.util.StringUtil;
import org.apache.derby.impl.jdbc.EmbedResultSetMetaData;
import org.apache.derby.impl.sql.GenericPreparedStatement;
import org.apache.derby.impl.sql.GenericStatement;
import org.apache.derby.impl.sql.conn.CachedStatement;
import org.apache.derby.vti.VTITemplate;

/**
	StatementCache is a virtual table that shows the contents of the SQL statement cache.
	
	This virtual table can be invoked by calling it directly.
	<PRE> select * from new org.apache.derby.diag.StatementCache() t</PRE>


	<P>The StatementCache virtual table has the following columns:
	<UL>
	<LI> ID CHAR(36) - not nullable.  Internal identifier of the compiled statement.
	<LI> SCHEMANAME VARCHAR(128) - nullable.  Schema the statement was compiled in.
	<LI> SQL_TEXT VARCHAR(32672) - not nullable.  Text of the statement
	<LI> UNICODE BIT/BOOLEAN - not nullable.  True if the statement is compiled as a pure unicode string, false if it handled unicode escapes.
	<LI> VALID BIT/BOOLEAN - not nullable.  True if the statement is currently valid, false otherwise
	<LI> COMPILED_AT TIMESTAMP nullable - time statement was compiled, requires STATISTICS TIMING to be enabled.


	</UL>
	<P>
	The internal identifier of a cached statement matches the toString() method of a PreparedStatement object for a Derby database.

	<P>
	This class also provides a static method to empty the statement cache, StatementCache.emptyCache()

*/
public final class StatementCache extends VTITemplate {

	private int position = -1;
	private Vector data;
	private GenericPreparedStatement currentPs;
	private boolean wasNull;

	public StatementCache() throws StandardException {

        DiagUtil.checkAccess();
        
        LanguageConnectionContext lcc = (LanguageConnectionContext)
            ContextService.getContextOrNull(LanguageConnectionContext.CONTEXT_ID);

        CacheManager statementCache =
            lcc.getLanguageConnectionFactory().getStatementCache();

		if (statementCache != null) {
			final Collection values = statementCache.values();
			data = new Vector(values.size());
			for (Iterator i = values.iterator(); i.hasNext(); ) {
				final CachedStatement cs = (CachedStatement) i.next();
				final GenericPreparedStatement ps =
					(GenericPreparedStatement) cs.getPreparedStatement();
				data.add(ps);
			}
		}
	}

	public boolean next() {

		if (data == null)
			return false;

		position++;

		for (; position < data.size(); position++) {
			currentPs = (GenericPreparedStatement) data.get(position);
	
			if (currentPs != null)
				return true;
		}

		data = null;
		return false;
	}

	public void close() {
		data = null;
		currentPs = null;
	}


	public String getString(int colId) {
		wasNull = false;
		switch (colId) {
		case 1:
			return currentPs.getObjectName();
		case 2:
			return ((GenericStatement) currentPs.statement).getCompilationSchema();
		case 3:
			String sql = currentPs.getSource();
			sql = StringUtil.truncate(sql, Limits.DB2_VARCHAR_MAXWIDTH);
			return sql;
		default:
			return null;
		}
	}

	public boolean getBoolean(int colId) {
		wasNull = false;
		switch (colId) {
		case 4:
			// was/is UniCode column, but since Derby 10.0 all
			// statements are compiled and submitted as UniCode.
			return true;
		case 5:
			return currentPs.isValid();
		default:
			return false;
		}
	}

	public Timestamp getTimestamp(int colId) {

		Timestamp ts = currentPs.getEndCompileTimestamp();
		wasNull = (ts == null);
		return ts;
	}

	public boolean wasNull() {
		return wasNull;
	}

	/*
	** Metadata
	*/
	private static final ResultColumnDescriptor[] columnInfo = {

		EmbedResultSetMetaData.getResultColumnDescriptor("ID",		  Types.CHAR, false, 36),
		EmbedResultSetMetaData.getResultColumnDescriptor("SCHEMANAME",    Types.VARCHAR, true, 128),
		EmbedResultSetMetaData.getResultColumnDescriptor("SQL_TEXT",  Types.VARCHAR, false, Limits.DB2_VARCHAR_MAXWIDTH),
		EmbedResultSetMetaData.getResultColumnDescriptor("UNICODE",   Types.BIT, false),
		EmbedResultSetMetaData.getResultColumnDescriptor("VALID",  Types.BIT, false),
		EmbedResultSetMetaData.getResultColumnDescriptor("COMPILED_AT",  Types.TIMESTAMP, true),

	};
	
	private static final ResultSetMetaData metadata = new EmbedResultSetMetaData(columnInfo);

	public ResultSetMetaData getMetaData() {

		return metadata;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2710.java