error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1358.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1358.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1358.java
text:
```scala
public final P@@reparedStatement getPreparedStatement() throws SQLException {

/*

   Derby - Class org.apache.derby.iapi.jdbc.BrokeredCallableStatement

   Copyright 2003, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.jdbc;

import java.sql.*;
import java.math.BigDecimal;

import java.util.Calendar;
import java.util.Map;


/**
	JDBC 2 brokered CallableStatement
 */
public class BrokeredCallableStatement extends BrokeredPreparedStatement
          implements CallableStatement
{

	public BrokeredCallableStatement(BrokeredStatementControl control, int jdbcLevel, String sql) throws SQLException {
		super(control, jdbcLevel, sql);
	}

    public final void registerOutParameter(int parameterIndex,
                                     int sqlType)
        throws SQLException
    {
        getCallableStatement().registerOutParameter( parameterIndex, sqlType);
    }

    public final void registerOutParameter(int parameterIndex,
                                     int sqlType,
                                     int scale)
        throws SQLException
    {
        getCallableStatement().registerOutParameter( parameterIndex, sqlType, scale);
    }

    public final boolean wasNull()
        throws SQLException
    {
        return getCallableStatement().wasNull();
    }

    public final String getString(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getString( parameterIndex);
    }

    public final boolean getBoolean(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getBoolean( parameterIndex);
    }

    public final byte getByte(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getByte( parameterIndex);
    }

    public final short getShort(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getShort( parameterIndex);
    }

    public final int getInt(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getInt( parameterIndex);
    }

    public final long getLong(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getLong( parameterIndex);
    }

    public final float getFloat(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getFloat( parameterIndex);
    }

    public final double getDouble(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getDouble( parameterIndex);
    }

    public final BigDecimal getBigDecimal(int parameterIndex,
                                              int scale)
        throws SQLException
    {
        return getCallableStatement().getBigDecimal( parameterIndex, scale);
    }

    public final byte[] getBytes(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getBytes( parameterIndex);
    }

    public final Date getDate(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getDate( parameterIndex);
    }

    public final Date getDate(int parameterIndex,
                        Calendar cal)
        throws SQLException
    {
        return getCallableStatement().getDate( parameterIndex, cal);
    }

    public final Time getTime(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getTime( parameterIndex);
    }

    public final Timestamp getTimestamp(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getTimestamp( parameterIndex);
    }

    public final Object getObject(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getObject( parameterIndex);
    }

    public final BigDecimal getBigDecimal(int parameterIndex)
        throws SQLException
    {
        return getCallableStatement().getBigDecimal( parameterIndex);
    }

    public final Object getObject(int i,
                            Map map)
        throws SQLException
    {
        return getCallableStatement().getObject( i, map);
    }

    public final Ref getRef(int i)
        throws SQLException
    {
        return getCallableStatement().getRef( i);
    }

    public final Blob getBlob(int i)
        throws SQLException
    {
        return getCallableStatement().getBlob( i);
    }

    public final Clob getClob(int i)
        throws SQLException
    {
        return getCallableStatement().getClob( i);
    }

    public final Array getArray(int i)
        throws SQLException
    {
        return getCallableStatement().getArray( i);
    }

    public final Time getTime(int parameterIndex,
                        Calendar cal)
        throws SQLException
    {
        return getCallableStatement().getTime( parameterIndex, cal);
    }

    public final Timestamp getTimestamp(int parameterIndex,
                                  Calendar cal)
        throws SQLException
    {
        return getCallableStatement().getTimestamp( parameterIndex, cal);
    }

    public final void registerOutParameter(int paramIndex,
                                     int sqlType,
                                     String typeName)
        throws SQLException
    {
        getCallableStatement().registerOutParameter( paramIndex, sqlType, typeName);
    }

	/*
	** Control methods
	*/

	protected final CallableStatement getCallableStatement() throws SQLException {
		return control.getRealCallableStatement();
	}
	protected final PreparedStatement getPreparedStatement() throws SQLException {
		return getCallableStatement();
	}
	/**
		Create a duplicate CalableStatement to this, including state, from the passed in Connection.
	*/
	public CallableStatement createDuplicateStatement(Connection conn, CallableStatement oldStatement) throws SQLException {

		CallableStatement newStatement = conn.prepareCall(sql, resultSetType, resultSetConcurrency);

		setStatementState(oldStatement, newStatement);

		return newStatement;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1358.java