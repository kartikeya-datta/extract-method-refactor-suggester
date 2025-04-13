error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2137.java
text:
```scala
public static final J@@DBCClient DERBYNETCLIENT= new JDBCClient(

/*
 *
 * Derby - Class JDBCClient
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.apache.derbyTesting.junit;

import junit.framework.Assert;

/**
 * Type-safe enumerator of valid JDBC clients.
 * Each JDBC client definition consists of the client name, the name of the
 * JDBC driver class, the name of a DataSource class and the base JDBC url.
 */
public final class JDBCClient {

    /**
     * The embedded JDBC client.
     */
    public static final JDBCClient EMBEDDED_30= new JDBCClient(
            "Embedded_30", 
            "org.apache.derby.jdbc.EmbeddedDriver", 
            "org.apache.derby.jdbc.EmbeddedDataSource", 
            "org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource",
            "org.apache.derby.jdbc.EmbeddedXADataSource",
            "jdbc:derby:");
    
    /**
     * The embedded JDBC client for JDBC 4.0.
     */
    static final JDBCClient EMBEDDED_40 = new JDBCClient(
            "Embedded_40", 
            "org.apache.derby.jdbc.EmbeddedDriver", 

            JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.EmbeddedDataSource40":
            "org.apache.derby.jdbc.BasicEmbeddedDataSource40",

            JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource40":
            "org.apache.derby.jdbc.BasicEmbeddedConnectionPoolDataSource40",

            JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.EmbeddedXADataSource40":
            "org.apache.derby.jdbc.BasicEmbeddedXADataSource40",

            "jdbc:derby:");
    
    /**
     * The embedded JDBC client for JSR 169
     */
    private static final JDBCClient EMBEDDED_169 = new JDBCClient(
            "Embedded_169", 
            null, // No driver
            "org.apache.derby.jdbc.EmbeddedSimpleDataSource", 
            null, // No connection pooling
            null, // No XA
            null); // No JDBC URLs
    
    /**
     * Return the default embedded client for this JVM.
     */
    static JDBCClient getDefaultEmbedded()
    {
        if (JDBC.vmSupportsJDBC4())
            return EMBEDDED_40;
        if (JDBC.vmSupportsJDBC3())
            return EMBEDDED_30;
        if (JDBC.vmSupportsJSR169())
            return EMBEDDED_169;
        
        Assert.fail("Unknown JVM environment");
        return null;
    }
    
    /**
     * The Derby network client.
     */
    static final JDBCClient DERBYNETCLIENT= new JDBCClient(
            "DerbyNetClient",
            "org.apache.derby.jdbc.ClientDriver",

            JDBC.vmSupportsJDBC4() ?
            (JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.ClientDataSource40" :
            "org.apache.derby.jdbc.BasicClientDataSource40") :
             "org.apache.derby.jdbc.ClientDataSource",

            JDBC.vmSupportsJDBC4() ?
            (JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.ClientConnectionPoolDataSource40" :
            "org.apache.derby.jdbc.BasicClientConnectionPoolDataSource40") :
            "org.apache.derby.jdbc.ClientConnectionPoolDataSource",

            JDBC.vmSupportsJDBC4() ?
            (JDBC.vmSupportsJNDI() ?
            "org.apache.derby.jdbc.ClientXADataSource40" :
            "org.apache.derby.jdbc.BasicClientXADataSource40") :
            "org.apache.derby.jdbc.ClientXADataSource",

            "jdbc:derby://");
    
    static final JDBCClient DERBYNETCLIENT_30 = new JDBCClient(
            "DerbyNetClient",
            "org.apache.derby.jdbc.ClientDriver",
            "org.apache.derby.jdbc.ClientDataSource",
            "org.apache.derby.jdbc.ClientConnectionPoolDataSource",
            "org.apache.derby.jdbc.ClientXADataSource",
            "jdbc:derby://");

    /**
     * The DB2 Universal JDBC network client.
     * AKA: JCC or DB2 client (was called DerbyNet earlier, the "old net"
     * client for Derby).
     */
    static final JDBCClient DB2CLIENT= new JDBCClient(
            "DB2Client",
            "com.ibm.db2.jcc.DB2Driver",
            null, null, null,
            "jdbc:derby:net://");
    
    /**
     * Is this the embdded client.
    */
    public boolean isEmbedded()
    {
    	return getName().startsWith("Embedded");
    }
    /**
     * Is this Derby's network client.
     */
    public boolean isDerbyNetClient()
    {
    	return getName().equals(DERBYNETCLIENT.getName());
    }
    /**
     * Is this DB2's Universal JDBC 
     */
    public boolean isDB2Client()
    {
    	return getName().equals(DB2CLIENT.getName());
    }
    
    /**
     * Get the name of the client
     */
    public String getName()
    {
    	return frameWork;
    }
    
    /**
     * Get JDBC driver class name.
     * 
     * @return class name for JDBC driver.
     */
    public String getJDBCDriverName() {
        return driverClassName;
    }

    /**
     * Get DataSource class name.
     * 
     * @return class name for DataSource implementation.
     */
    public String getDataSourceClassName() {
        return dsClassName;
    }

    /**
     * Get ConnectionPoolDataSource class name.
     *
     * @return class name for ConnectionPoolDataSource implementation.
     */
    public String getConnectionPoolDataSourceClassName() {
        return poolDsClassName;
    }

    /**
     * Get XADataSource class name.
     *
     * @return class name for XADataSource implementation.
     */
    public String getXADataSourceClassName() {
        return xaDsClassName;
    }

    /**
     * Return the base JDBC url.
     * The JDBC base url specifies the protocol and possibly the subprotcol
     * in the JDBC connection string.
     * 
     * @return JDBC base url.
     */
    public String getUrlBase() {
        return urlBase;
    }
    
    /**
     * Return string representation of this object.
     * 
     * @return string representation of this object.
     */
    public String toString() {
        return frameWork;
    }
    
    /**
     * Create a JDBC client definition.
     */
    private JDBCClient(String frameWork, String driverClassName,
                       String dataSourceClassName,
                       String connectionPoolDataSourceClassName,
                       String xaDataSourceClassName,
                       String urlBase) {
        this.frameWork          = frameWork;
        this.driverClassName    = driverClassName;
        this.dsClassName        = dataSourceClassName;
        this.poolDsClassName    = connectionPoolDataSourceClassName;
        this.xaDsClassName      = xaDataSourceClassName;
        this.urlBase            = urlBase;
    }
    
    private final String frameWork;
    private final String driverClassName;
    private final String dsClassName;
    private final String poolDsClassName;
    private final String xaDsClassName;
    private final String urlBase;
    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2137.java