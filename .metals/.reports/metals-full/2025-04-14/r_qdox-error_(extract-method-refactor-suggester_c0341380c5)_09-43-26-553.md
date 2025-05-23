error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7339.java
text:
```scala
public b@@oolean isFatalException(int subtype, SQLException ex) {

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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.kernel.exps.FilterValue;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.schema.Index;
import org.apache.openjpa.jdbc.schema.PrimaryKey;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.util.StoreException;

/**
 * Dictionary for MySQL.
 */
public class MySQLDictionary
    extends DBDictionary {

    public static final String SELECT_HINT = "openjpa.hint.MySQLSelectHint";

    public static final String DELIMITER_BACK_TICK = "`";
    
    /**
     * The MySQL table type to use when creating tables; defaults to innodb.
     */
    public String tableType = "innodb";

    /**
     * Whether to use clobs; defaults to true. Set this to false if you have an
     * old version of MySQL which does not handle clobs properly.
     */
    public boolean useClobs = true;

    /**
     * Whether the driver automatically deserializes blobs.
     */
    public boolean driverDeserializesBlobs = false;

    /**
     * Whether to inline multi-table bulk-delete operations into MySQL's 
     * combined <code>DELETE FROM foo, bar, baz</code> syntax. 
     * Defaults to false, since this may fail in the presence of InnoDB tables
     * with foreign keys.
     * @see http://dev.mysql.com/doc/refman/5.0/en/delete.html
     */
    public boolean optimizeMultiTableDeletes = false;

    public static final String tinyBlobTypeName = "TINYBLOB";
    public static final String mediumBlobTypeName = "MEDIUMBLOB";
    public static final String longBlobTypeName = "LONGBLOB";

    public MySQLDictionary() {
        platform = "MySQL";
        validationSQL = "SELECT NOW()";
        distinctCountColumnSeparator = ",";

        supportsDeferredConstraints = false;
        constraintNameMode = CONS_NAME_MID;
        supportsMultipleNontransactionalResultSets = false;
        requiresAliasForSubselect = true; // new versions
        requiresTargetForDelete = true;
        supportsSelectStartIndex = true;
        supportsSelectEndIndex = true;

        concatenateFunction = "CONCAT({0},{1})";

        maxTableNameLength = 64;
        maxColumnNameLength = 64;
        maxIndexNameLength = 64;
        maxConstraintNameLength = 64;
        maxIndexesPerTable = 32;
        schemaCase = SCHEMA_CASE_PRESERVE;

        supportsAutoAssign = true;
        lastGeneratedKeyQuery = "SELECT LAST_INSERT_ID()";
        autoAssignClause = "AUTO_INCREMENT";

        clobTypeName = "TEXT";
        longVarcharTypeName = "TEXT";
        longVarbinaryTypeName = "LONG VARBINARY";
        timestampTypeName = "DATETIME";
        xmlTypeName = "TEXT";
        fixedSizeTypeNameSet.addAll(Arrays.asList(new String[]{
            "BOOL", "LONG VARBINARY", "MEDIUMBLOB", "LONGBLOB",
            "TINYBLOB", "LONG VARCHAR", "MEDIUMTEXT", "LONGTEXT", "TEXT",
            "TINYTEXT", "DOUBLE PRECISION", "ENUM", "SET", "DATETIME",
        }));
        reservedWordSet.addAll(Arrays.asList(new String[]{
            "AUTO_INCREMENT", "BINARY", "BLOB", "CHANGE", "ENUM", "INFILE",
            "INT1", "INT2", "INT4", "FLOAT1", "FLOAT2", "FLOAT4", "LOAD",
            "MEDIUMINT", "OUTFILE", "REPLACE", "STARTING", "TEXT", "UNSIGNED", 
            "ZEROFILL",
        }));

        // reservedWordSet subset that CANNOT be used as valid column names
        // (i.e., without surrounding them with double-quotes)
        invalidColumnWordSet.addAll(Arrays.asList(new String[]{
            "ADD", "ALL", "ALTER", "AND", "AS", "ASC", "BETWEEN", "BINARY",
            "BLOB", "BOTH", "BY", "CASCADE", "CASE", "CHANGE", "CHAR", 
            "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONSTRAINT", "CONTINUE",
            "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DEC", "DECIMAL",
            "DECLARE", "DEFAULT", "DELETE", "DESC", "DESCRIBE", "DISTINCT",
            "DOUBLE", "DROP", "ELSE", "END-EXEC", "EXISTS", "FALSE", "FETCH",
            "FLOAT", "FLOAT4", "FOR", "FOREIGN", "FROM", "GRANT", "GROUP",
            "HAVING", "IN", "INFILE", "INNER", "INSENSITIVE", "INSERT", "INT",
            "INT1", "INT2", "INT4", "INTEGER", "INTERVAL", "INTO", "IS", "JOIN",
            "KEY", "LEADING", "LEFT", "LIKE", "LOAD", "MATCH", "MEDIUMINT",
            "NATURAL", "NOT", "NULL", "NUMERIC", "ON", "OPTION", "OR", "ORDER",
            "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "READ",
            "REAL", "REFERENCES", "REPLACE", "RESTRICT", "REVOKE", "RIGHT",
            "SCHEMA", "SELECT", "SET", "SMALLINT", "SQL", "SQLSTATE",
            "STARTING", "TABLE", "THEN", "TO", "TRAILING", "TRUE", "UNION",
            "UNIQUE", "UNSIGNED", "UPDATE", "USAGE", "USING", "VALUES",
            "VARCHAR", "VARYING", "WHEN", "WHERE", "WITH", "WRITE", "ZEROFILL",
        }));

        // MySQL requires double-escape for strings
        searchStringEscape = "\\\\";

        typeModifierSet.addAll(Arrays.asList(new String[] { "UNSIGNED",
            "ZEROFILL" }));

        setLeadingDelimiter(DELIMITER_BACK_TICK);
        setTrailingDelimiter(DELIMITER_BACK_TICK);
        
        fixedSizeTypeNameSet.remove("NUMERIC");
    }

    @Override
    public void connectedConfiguration(Connection conn) throws SQLException {
        super.connectedConfiguration(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        int maj = 0;
        int min = 0;
        if (isJDBC3) {
            maj = metaData.getDatabaseMajorVersion();
            min = metaData.getDatabaseMinorVersion();
        } else {
            try {
                // The product version looks like 4.1.3-nt or 5.1.30
                String productVersion = metaData.getDatabaseProductVersion();
                int[] versions = getMajorMinorVersions(productVersion);
                maj = versions[0];
                min = versions[1];
            } catch (IllegalArgumentException e) {
                // we don't understand the version format.
                // That is ok. We just take the default values.
                if (log.isWarnEnabled())
                    log.warn(e.toString(), e);
            }
        }
        if (maj < 4 || (maj == 4 && min < 1)) {
            supportsSubselect = false;
            allowsAliasInBulkClause = false;
            supportsForeignKeysComposite = false;
        }
        if (maj > 5 || (maj == 5 && min >= 1))
            supportsXMLColumn = true;

        if (metaData.getDriverMajorVersion() < 5)
            driverDeserializesBlobs = true;
    }

    @Override
    public Connection decorate(Connection conn)  throws SQLException {
        conn = super.decorate(conn);
        String driver = conf.getConnectionDriverName();
        if ("com.mysql.jdbc.ReplicationDriver".equals(driver))
            conn.setReadOnly(true);
        return conn;
    }
    
    private static int[] getMajorMinorVersions(String versionStr)
        throws IllegalArgumentException {
        int beginIndex = 0;

        versionStr = versionStr.trim();
        char[] charArr = versionStr.toCharArray();
        for (int i = 0; i < charArr.length; i++) {
            if (Character.isDigit(charArr[i])) {
                beginIndex = i;
                break;
            }
        }

        int endIndex = charArr.length;
        for (int i = beginIndex+1; i < charArr.length; i++) {
            if (charArr[i] != '.' && !Character.isDigit(charArr[i])) {
                endIndex = i;
                break;
            }
        }

        String[] arr = versionStr.substring(beginIndex, endIndex).split("\\.");
        if (arr.length < 2)
            throw new IllegalArgumentException();

        int maj = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);
        return new int[]{maj, min};
    }

    @Override
    public String[] getCreateTableSQL(Table table) {
        String[] sql = super.getCreateTableSQL(table);
        if (!StringUtils.isEmpty(tableType))
            sql[0] = sql[0] + " ENGINE = " + tableType;
        return sql;
    }

    @Override
    public String[] getDropIndexSQL(Index index) {
        return new String[]{ "DROP INDEX " + getFullName(index) + " ON "
            + getFullName(index.getTable(), false) };
    }

    /**
     * Return <code>ALTER TABLE &lt;table name&gt; DROP PRIMARY KEY</code>.
     */
    @Override
    public String[] getDropPrimaryKeySQL(PrimaryKey pk) {
        if (DBIdentifier.isNull(pk.getIdentifier()))
            return new String[0];
        return new String[]{ "ALTER TABLE "
            + getFullName(pk.getTable(), false)
            + " DROP PRIMARY KEY" };
    }

    /**
     * Return <code>ALTER TABLE &lt;table name&gt; DROP FOREIGN KEY
     * &lt;fk name&gt;</code>.
     */
    @Override
    public String[] getDropForeignKeySQL(ForeignKey fk, Connection conn) {
        if (DBIdentifier.isNull(fk.getIdentifier())) {
            DBIdentifier fkName = fk.loadIdentifierFromDB(this,conn);
            String[] retVal = (fkName == null) ?  new String[0] :
                new String[]{ "ALTER TABLE "
                + getFullName(fk.getTable(), false)
                + " DROP FOREIGN KEY " + toDBName(fkName) };
            return retVal;   
        }
        return new String[]{ "ALTER TABLE "
            + getFullName(fk.getTable(), false)
            + " DROP FOREIGN KEY " + toDBName(fk.getIdentifier()) };
    }

    @Override
    public String[] getAddPrimaryKeySQL(PrimaryKey pk) {
        String[] sql = super.getAddPrimaryKeySQL(pk);

        // mysql requires that a column be declared NOT NULL before
        // it can be made a primary key.
        Column[] cols = pk.getColumns();
        String[] ret = new String[cols.length + sql.length];
        for (int i = 0; i < cols.length; i++) {
            ret[i] = "ALTER TABLE " + getFullName(cols[i].getTable(), false)
                + " CHANGE " + toDBName(cols[i].getIdentifier())
                + " " + toDBName(cols[i].getIdentifier()) // name twice
                + " " + getTypeName(cols[i]) + " NOT NULL";
        }

        System.arraycopy(sql, 0, ret, cols.length, sql.length);
        return ret;
    }
    
    @Override
    public String[] getDeleteTableContentsSQL(Table[] tables,Connection conn) {
        // mysql >= 4 supports more-optimal delete syntax
        if (!optimizeMultiTableDeletes)
            return super.getDeleteTableContentsSQL(tables,conn);
        else {
            StringBuilder buf = new StringBuilder(tables.length * 8);
            buf.append("DELETE FROM ");
            for (int i = 0; i < tables.length; i++) {
                buf.append(toDBName(tables[i].getFullIdentifier()));
                if (i < tables.length - 1)
                    buf.append(", ");
            }
            return new String[] { buf.toString() };
        }
    }

    @Override
    protected void appendSelectRange(SQLBuffer buf, long start, long end,
        boolean subselect) {
        buf.append(" LIMIT ").appendValue(start).append(", ");
        if (end == Long.MAX_VALUE)
            buf.appendValue(Long.MAX_VALUE);
        else
            buf.appendValue(end - start);
    }

    @Override
    protected Column newColumn(ResultSet colMeta)
        throws SQLException {
        Column col = super.newColumn(colMeta);
        if (col.isNotNull() && "0".equals(col.getDefaultString()))
            col.setDefaultString(null);
        return col;
    }

    @Override
    public Object getBlobObject(ResultSet rs, int column, JDBCStore store)
        throws SQLException {
        // if the user has set a get-blob strategy explicitly or the driver
        // does not automatically deserialize, delegate to super
        if (useGetBytesForBlobs || useGetObjectForBlobs
 !driverDeserializesBlobs)
            return super.getBlobObject(rs, column, store);

        // most mysql drivers deserialize on getObject
        return rs.getObject(column);
    }

    @Override
    public int getPreferredType(int type) {
        if (type == Types.CLOB && !useClobs)
            return Types.LONGVARCHAR;
        return super.getPreferredType(type);
    }
    
    /**
     * Append XML comparison.
     * 
     * @param buf the SQL buffer to write the comparison
     * @param op the comparison operation to perform
     * @param lhs the left hand side of the comparison
     * @param rhs the right hand side of the comparison
     * @param lhsxml indicates whether the left operand maps to XML
     * @param rhsxml indicates whether the right operand maps to XML
     */
    @Override
    public void appendXmlComparison(SQLBuffer buf, String op, FilterValue lhs,
        FilterValue rhs, boolean lhsxml, boolean rhsxml) {
        super.appendXmlComparison(buf, op, lhs, rhs, lhsxml, rhsxml);
        if (lhsxml)
            appendXmlValue(buf, lhs);
        else
            lhs.appendTo(buf);
        buf.append(" ").append(op).append(" ");
        if (rhsxml)
            appendXmlValue(buf, rhs);
        else
            rhs.appendTo(buf);
    }
    
    /**
     * Append XML column value so that it can be used in comparisons.
     * 
     * @param buf the SQL buffer to write the value
     * @param val the value to be written
     */
    private void appendXmlValue(SQLBuffer buf, FilterValue val) {
        buf.append("ExtractValue(").
            append(val.getColumnAlias(val.getFieldMapping().getColumns()[0])).
            append(",'/*/");
        val.appendTo(buf);
        buf.append("')");
    }
    
    @Override
    public int getBatchFetchSize(int batchFetchSize) {
        return Integer.MIN_VALUE;
    }

    /**
     * Check to see if we have set the {@link #SELECT_HINT} in the
     * fetch configuration, and if so, append the MySQL hint after the
     * "SELECT" part of the query.
     */
    @Override
    public String getSelectOperation(JDBCFetchConfiguration fetch) {
        Object hint = fetch == null ? null : fetch.getHint(SELECT_HINT);
        String select = "SELECT";
        if (hint != null)
            select += " " + hint;
        return select;
    }
    
    @Override
    protected Collection<String> getSelectTableAliases(Select sel) {
        Set<String> result = new HashSet<String>();
        List<String> selects = sel.getIdentifierAliases();
        for (String s : selects) {
            String tableAlias = s.substring(0, s.indexOf('.'));
            result.add(tableAlias);
        }
        return result;
    }
    
    @Override
    protected boolean isFatalException(int subtype, SQLException ex) {
        if ((subtype == StoreException.LOCK  && ex.getErrorCode() == 1205)
(subtype == StoreException.QUERY && ex.getErrorCode() == 1317)) {
            return false;
        }
        return super.isFatalException(subtype, ex);
    }

    /**
     * OPENJPA-740 Special case for MySql special column types,
     * like LONGTEXT, LONGBLOG etc..
     * @see org.apache.openjpa.jdbc.sql.DBDictionary#getTypeName(org.apache.openjpa.jdbc.schema.Column)
     */
    @Override
    public String getTypeName(Column col) {
        if (col.getType() == Types.BLOB) {
            if (col.getSize() == 0)   // unknown size
                return blobTypeName;  // return old default of 64KB
            else if (col.getSize() <= 255)
                return tinyBlobTypeName;
            else if (col.getSize() <= 65535)
                return blobTypeName;  // old default of 64KB
            else if (col.getSize() <= 16777215)
                return mediumBlobTypeName;
            else
                return longBlobTypeName;
        } else {
            return super.getTypeName(col);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7339.java