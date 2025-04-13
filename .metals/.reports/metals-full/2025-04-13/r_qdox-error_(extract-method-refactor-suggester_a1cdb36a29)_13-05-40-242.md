error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/519.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/519.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/519.java
text:
```scala
"BIGINT",@@ "INTEGER", "TEXT"

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
package org.apache.openjpa.jdbc.sql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.openjpa.util.StoreException;

/**
 * Dictionary for Apache Derby (formerly Cloudscape).
 */
public class DerbyDictionary
    extends AbstractDB2Dictionary {

    /**
     * If true, we will shutdown the embedded database when closing
     * the DataSource.
     */
    public boolean shutdownOnClose = true;
    
    public DerbyDictionary() {
        platform = "Apache Derby";
        validationSQL = "VALUES(1)";
        stringLengthFunction = "LENGTH({0})";
        substringFunctionName = "SUBSTR";
        toUpperCaseFunction = "UPPER(CAST({0} AS VARCHAR(" + varcharCastLength + ")))";
        toLowerCaseFunction = "LOWER(CAST({0} AS VARCHAR(" + varcharCastLength + ")))";

        // Derby name length restriction has been relaxed 
        //http://www.archivum.info/derby-dev@db.apache.org/2004-12/msg00270.html
        maxConstraintNameLength = 128;
        maxIndexNameLength = 128;
        maxColumnNameLength = 128;
        maxTableNameLength = 128;

        useGetBytesForBlobs = true;
        useSetBytesForBlobs = true;

        allowsAliasInBulkClause = false;
        supportsDeferredConstraints = false;
        supportsParameterInSelect = false;
        supportsSelectForUpdate = true;
        supportsDefaultDeleteAction = false;
        requiresCastForMathFunctions = true;
        requiresCastForComparisons = true;
        supportsSimpleCaseExpression = false;
        supportsNullUniqueColumn = false;
        
        supportsComments = true;

        fixedSizeTypeNameSet.addAll(Arrays.asList(new String[]{
            "BIGINT", "INTEGER",
        }));
        reservedWordSet.addAll(Arrays.asList(new String[]{
            "BOOLEAN", "CALL", "ENDEXEC", "EXPLAIN", "FUNCTION",
            "GET_CURRENT_CONNECTION", "INOUT", "LONGINT", "LTRIM", "NONE",
            "NVARCHAR", "OFF", "OUT", "RTRIM", "SUBSTR", "XML", "XMLEXISTS",
            "XMLPARSE", "XMLSERIALIZE",
        }));

        // reservedWordSet subset that CANNOT be used as valid column names
        // (i.e., without surrounding them with double-quotes)
        invalidColumnWordSet.addAll(Arrays.asList(new String[] {
            "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", "ASC",
            "ASSERTION", "AT", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", 
            "BIT", "BOOLEAN", "BOTH", "BY", "CALL", "CASCADE", "CASCADED", 
            "CASE", "CAST", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHECK",
            "CLOSE", "COALESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONNECT",
            "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONTINUE", "CONVERT",
            "CORRESPONDING", "CREATE", "CURRENT", "CURRENT_DATE", "CURRENT_ROLE",
            "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR",
            "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE",
            "DEFERRED", "DELETE", "DESC", "DESCRIBE", "DIAGNOSTICS", 
            "DISCONNECT", "DISTINCT", "DOUBLE", "DROP", "ELSE", "END", 
            "END-EXEC", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE",
            "EXISTS", "EXPLAIN", "EXTERNAL", "FALSE", "FETCH", "FIRST", "FLOAT",
            "FOR", "FOREIGN", "FOUND", "FROM", "FULL", "FUNCTION", "GET", 
            "GETCURRENTCONNECTION", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HAVING", "HOUR",
            "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER",
            "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INT", "INTEGER",
            "INTERSECT", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LAST",
            "LEADING", "LEFT", "LIKE", "LOWER", "LTRIM", "MATCH", "MAX", "MIN",
            "MINUTE", "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NONE", "NOT", 
            "NULL", "NULLIF", "NUMERIC", "NVARCHAR", "OF", "ON", "ONLY", "OPEN",
            "OPTION", "OR", "ORDER", "OUT", "OUTER", "OUTPUT", "OVER", "OVERLAPS", 
            "PAD", "PARTIAL", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR",
            "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES",
            "RELATIVE", "RESTRICT", "REVOKE", "RIGHT", "ROLLBACK", "ROWS",
            "ROW_NUMBER", "RTRIM", "SCHEMA", "SCROLL", "SECOND", "SELECT", "SESSION_USER",
            "SET", "SMALLINT", "SOME", "SPACE", "SQL", "SQLCODE", "SQLERROR",
            "SQLSTATE", "SUBSTR", "SUBSTRING", "SUM", "SYSTEM_USER", "TABLE",
            "TEMPORARY", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING",
            "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIM", "TRUE", "UNION",
            "UNIQUE", "UNKNOWN", "UPDATE", "UPPER", "USER", "USING", "VALUES",
            "VARCHAR", "VARYING", "VIEW", "WHENEVER", "WHERE", "WITH", "WORK",
            "WRITE", "XML", "XMLEXISTS", "XMLPARSE", "XMLQUERY", "XMLSERIALIZE", "YEAR",
        }));
    }

    public void closeDataSource(DataSource dataSource) {
        super.closeDataSource(dataSource);

        if (!shutdownOnClose)
            return;

        // as well as closing the DataSource, we also need to
        // shut down the instance if we are using an embedded database, which
        // can only be done by connecting to the same URL with the
        // ";shutdown=true" string appended to the end
        // see: http://db.apache.org/derby/docs/dev/devguide/tdevdvlp40464.html
        if (conf != null && conf.getConnectionDriverName() != null &&
            conf.getConnectionDriverName().indexOf("EmbeddedDriver") != -1) {
            try {
                DriverManager.getConnection(conf.getConnectionURL()
                    + ";shutdown=true");
            } catch (SQLException e) {
                // we actually expect a SQLException to be thrown here:
                // Derby strangely uses that as a mechanism to report
                // a successful shutdown
            }
        }
    }
    
    @Override
    public boolean isFatalException(int subtype, SQLException ex) {
        int errorCode = ex.getErrorCode();
        if ((subtype == StoreException.LOCK ||
             subtype == StoreException.QUERY) && errorCode <= 30000) {
            return false;
        }
        return super.isFatalException(subtype, ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/519.java