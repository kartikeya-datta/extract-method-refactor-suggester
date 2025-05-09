error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16365.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16365.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16365.java
text:
```scala
&@@&("1222".equals(errorState) || "HY008".equals(errorState) || "HYT00".equals(errorState)))

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.openjpa.jdbc.sql;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.kernel.exps.FilterValue;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.util.StoreException;


/**
 * Dictionary for Microsoft SQL Server.
 */
public class SQLServerDictionary extends AbstractSQLServerDictionary {

    public static final String VENDOR_MICROSOFT = "microsoft";
    public static final String VENDOR_NETDIRECT = "netdirect";
    public static final String VENDOR_JTDS = "jtds";

    private static final Localizer _loc =
        Localizer.forPackage(SQLServerDictionary.class);

    private String schemaCase = SCHEMA_CASE_PRESERVE;
    /**
     * Flag whether to treat UNIQUEIDENTIFIER as VARBINARY or VARCHAR
     */
    public boolean uniqueIdentifierAsVarbinary = true;

    public SQLServerDictionary() {
        platform = "Microsoft SQL Server";
        // SQLServer locks on a table-by-table basis
        forUpdateClause = null;
        tableForUpdateClause = "WITH (UPDLOCK)";
        supportsNullTableForGetColumns = false;
        requiresAliasForSubselect = true;
        stringLengthFunction = "LEN({0})";
    }

    public void connectedConfiguration(Connection conn) throws SQLException {
        super.connectedConfiguration(conn);
        boolean requiresWarnings = true;
        DatabaseMetaData meta = conn.getMetaData();
        String driverName = meta.getDriverName();
        String url = meta.getURL();
        if (driverVendor == null) {
            // serverMajorVersion of 8==2000, 9==2005, 10==2008
            if (meta.getDatabaseMajorVersion() >= 9)
                setSupportsXMLColumn(true);
            if (meta.getDatabaseMajorVersion() >= 10) {
                // MSSQL 2008 supports new date, time and datetime2 types
                // Use DATETIME2 which has 100ns vs. 3.333msec precision
                dateTypeName = "DATETIME2";
                timeTypeName = "DATETIME2";
                timestampTypeName = "DATETIME2";
                datePrecision = MICRO / 10;
            }
            if (driverName != null) {
                if (driverName.startsWith("Microsoft SQL Server")) {
                    // v1.1, 1.2, 2.0 or 3.0 driver
                    driverVendor = VENDOR_MICROSOFT;
                    if (meta.getDriverMajorVersion() >= 2) {
                        // see http://blogs.msdn.com/jdbcteam/archive/2007/05/\
                        // 02/what-is-adaptive-response-buffering-and-why-\
                        // should-i-use-it.aspx
                        // 2.0 driver connectURL automatically includes
                        // responseBuffering=adaptive
                        // and disableStatementPooling=true
                        requiresWarnings = false;
                    }
                } else {
                    if ("NetDirect JSQLConnect".equals(driverName))
                        driverVendor = VENDOR_NETDIRECT;
                    else if (driverName.startsWith("jTDS"))
                        driverVendor = VENDOR_JTDS;
                    else if ("SQLServer".equals(driverName)) {
                        if (url != null &&
                            url.startsWith("jdbc:microsoft:sqlserver:"))
                            driverVendor = VENDOR_MICROSOFT;
                        else if (url != null &&
                            url.startsWith("jdbc:datadirect:sqlserver:"))
                            driverVendor = VENDOR_DATADIRECT;
                        else
                            driverVendor = VENDOR_OTHER;
                    }
                    // old way of determining xml support
                    if (driverName.indexOf(platform) != -1) {
                        String versionString =
                            driverName.substring(platform.length() + 1);
                        if (versionString.indexOf(" ") != -1)
                            versionString = versionString.substring(0,
                                versionString.indexOf(" "));
                        int version = Integer.parseInt(versionString);
                        if (version >= 2005)
                            setSupportsXMLColumn(true);
                    }
                }
            } else {
                driverVendor = VENDOR_OTHER;
            }
        }

        // warn about not using cursors for pre-2.0 MS driver
        // as connectURL includes selectMethod=direct
        if (((VENDOR_MICROSOFT.equalsIgnoreCase(driverVendor) &&
            requiresWarnings) || 
            VENDOR_DATADIRECT.equalsIgnoreCase(driverVendor)) &&
            (url.toLowerCase().indexOf("selectmethod=cursor") == -1))
            log.warn(_loc.get("sqlserver-cursor", url));

        // warn about prepared statement caching if using pre-2.0 MS drivers
        // as connectURL includes responseBuffering=full
        String props = conf.getConnectionFactoryProperties();
        if ((props != null) &&
            VENDOR_MICROSOFT.equalsIgnoreCase(driverVendor) &&
            requiresWarnings &&
            (props.toLowerCase().indexOf("maxcachedstatements=0") == -1))
            log.warn(_loc.get("sqlserver-cachedstmnts"));
    }

    public Column[] getColumns(DatabaseMetaData meta, String catalog,
        String schemaName, String tableName, String columnName, Connection conn)
        throws SQLException {
        return getColumns(meta, DBIdentifier.newCatalog(catalog),
            DBIdentifier.newSchema(schemaName),
            DBIdentifier.newTable(tableName),
            DBIdentifier.newColumn(columnName),
            conn);
    }

    public Column[] getColumns(DatabaseMetaData meta, DBIdentifier catalog,
        DBIdentifier schemaName, DBIdentifier tableName, DBIdentifier columnName, Connection conn)
        throws SQLException {

        Column[] cols = super.getColumns(meta, catalog, schemaName, tableName,
            columnName, conn);

        // for opta driver, which reports nvarchar as unknown type
        for (int i = 0; cols != null && i < cols.length; i++) {
            String typeName = cols[i].getTypeIdentifier().getName();
            if (typeName == null)
                continue;

            typeName = typeName.toUpperCase();
            if ("NVARCHAR".equals(typeName))
                cols[i].setType(Types.VARCHAR);
            else if ("UNIQUEIDENTIFIER".equals(typeName)) {
                if (uniqueIdentifierAsVarbinary)
                    cols[i].setType(Types.VARBINARY);
                else
                    cols[i].setType(Types.VARCHAR);
            } else if ("NCHAR".equals(typeName))
                cols[i].setType(Types.CHAR);
            else if ("NTEXT".equals(typeName))
                cols[i].setType(Types.CLOB);
        }
        return cols;
    }

    protected void appendLength(SQLBuffer buf, int type) {
        if (type == Types.VARCHAR)
            buf.append("(").append(Integer.toString(characterColumnSize))
                .append(")");
    }

    /**
     * If this dictionary supports XML type, use this method to append xml
     * predicate.
     * 
     * @param buf the SQL buffer to write the comparison
     * @param op the comparison operation to perform
     * @param lhs the left hand side of the comparison
     * @param rhs the right hand side of the comparison
     * @param lhsxml indicates whether the left operand maps to xml
     * @param rhsxml indicates whether the right operand maps to xml
     */
    public void appendXmlComparison(SQLBuffer buf, String op, FilterValue lhs,
        FilterValue rhs, boolean lhsxml, boolean rhsxml) {
        super.appendXmlComparison(buf, op, lhs, rhs, lhsxml, rhsxml);
        if (lhsxml && rhsxml)
            appendXmlComparison2(buf, op, lhs, rhs);
        else if (lhsxml)
            appendXmlComparison1(buf, op, lhs, rhs);
        else
            appendXmlComparison1(buf, op, rhs, lhs);
    }

    /**
     * Append an xml comparison predicate
     * 
     * @param buf the SQL buffer to write the comparison
     * @param op the comparison operation to perform
     * @param lhs the left hand side of the comparison (maps to xml column)
     * @param rhs the right hand side of the comparison
     */
    private void appendXmlComparison1(SQLBuffer buf, String op,
        FilterValue lhs, FilterValue rhs) {
        boolean castrhs = rhs.isConstant();
        if (castrhs)
            appendXmlValue(buf, lhs);
        else
            appendXmlExist(buf, lhs);
        buf.append(" ").append(op).append(" ");
        if (castrhs)
            rhs.appendTo(buf);
        else {
            buf.append("sql:column(\"");
            rhs.appendTo(buf);
            buf.append("\")").append("]') = 1");
        }
    }

    private void appendXmlExist(SQLBuffer buf, FilterValue lhs) {
        buf.append(lhs.getColumnAlias(lhs.getFieldMapping().getColumns()[0]))
            .append(".exist('").append("/*[");
        lhs.appendTo(buf);
    }

    /**
     * Append an xml comparison predicate (both operands map to xml column)
     * 
     * @param buf the SQL buffer to write the comparison
     * @param op the comparison operation to perform
     * @param lhs the left hand side of the comparison (maps to xml column)
     * @param rhs the right hand side of the comparison (maps to xml column)
     */
    private void appendXmlComparison2(SQLBuffer buf, String op,
        FilterValue lhs, FilterValue rhs) {
        appendXmlValue(buf, lhs);
        buf.append(" ").append(op).append(" ");
        appendXmlValue(buf, rhs);
    }

    private void appendXmlValue(SQLBuffer buf, FilterValue val) {
        Class<?> rc = Filters.wrap(val.getType());
        int type = getJDBCType(JavaTypes.getTypeCode(rc), false);
        boolean isXmlAttribute = (val.getXmlMapping() == null)
            ? false : val.getXmlMapping().isXmlAttribute();
        buf.append(val.getColumnAlias(val.getFieldMapping().getColumns()[0]))
            .append(".value(").append("'(/*/");
        val.appendTo(buf);
        if (!isXmlAttribute)
            buf.append("/text()");
        buf.append(")[1]','").append(getTypeName(type));
        appendLength(buf, type);
        buf.append("')");
    }

    /**
     * Return DB specific schemaCase
     */
    public String getSchemaCase() {
        return schemaCase;
    }

    @Override
    public void setSupportsXMLColumn(boolean b) {
        super.setSupportsXMLColumn(b);
        // MS SQL Server requires XML data in UTF-16 or UCS-2 instead of JAXB default of UTF-8
        super.setXMLTypeEncoding("UTF-16");
    }
    
    @Override
    public boolean isFatalException(int subtype,  SQLException ex) {
        String errorState = ex.getSQLState();
        if ((subtype == StoreException.LOCK  || subtype == StoreException.QUERY)
          &&("1222".equals(errorState) || "HY008".equals(errorState)))
         return false;
        return super.isFatalException(subtype, ex);
    }

    /**
     * Obtain an {@link InputStream} by using {@link ResultSet#getBlob(int)} and
     * {@link Blob#getBinaryStream()}.
     * Unfortunately this will load entire BLOB into memory.
     * The alternative {@link ResultSet#getBinaryStream(int)} provides true streaming but
     * the stream can be consumed only as long as {@link ResultSet} is open.
     */
    @Override
    public InputStream getLOBStream(JDBCStore store, ResultSet rs, int column) throws SQLException {
        Blob blob = rs.getBlob(column);
        if (blob == null) {
            return null;
        }
        return blob.getBinaryStream();
    }

    /**
     * Obtain a {@link Reader} by using {@link ResultSet#getClob(int)} and
     * {@link Clob#getCharacterStream()}.
     * Unfortunately this will load entire CLOB into memory.
     * The alternative {@link ResultSet#getCharacterStream(int)} provides true streaming but
     * the stream can be consumed only as long as {@link ResultSet} is open.
     */
    @Override
    public Reader getCharacterStream(ResultSet rs, int column) throws SQLException {
        Clob clob = rs.getClob(column);
        if (clob == null) {
            return null;
        }
        return clob.getCharacterStream();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16365.java