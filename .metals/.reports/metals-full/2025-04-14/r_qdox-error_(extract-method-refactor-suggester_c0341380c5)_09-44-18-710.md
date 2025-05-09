error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1538.java
text:
```scala
{"1.5", "1.6", "1.7"}@@;

/*

   Derby - Class org.apache.derby.client.am.Configuration

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

package org.apache.derby.client.am;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import org.apache.derby.iapi.services.info.ProductGenusNames;
import org.apache.derby.iapi.services.info.ProductVersionHolder;
import org.apache.derby.shared.common.reference.SQLState;

public class Configuration {


    public static int traceFileSuffixIndex__ = 0;

    public static int traceLevel__ = org.apache.derby.jdbc.ClientBaseDataSource.TRACE_ALL;

    public static String traceFile__ = null;

    public static String traceDirectory__ = null;

    public static boolean traceFileAppend__ = false;
    public static final String jreLevel;// = "1.3.0"; // default level if unable to read
    public static final int jreLevelMajor;// = 1;
    public static final int jreLevelMinor;// = 3;

    private Configuration() {
    }

    public static boolean traceSuspended__;

    public static boolean[] enableConnectivityToTargetServer__;
    public static boolean jvmSupportsMicrosClock__ = false;

    // -------------------------- versioning -------------------------------------

    private static ProductVersionHolder dncProductVersionHolder__;

    public static ProductVersionHolder getProductVersionHolder() {
        return dncProductVersionHolder__;
    }


    // for DatabaseMetaData.getDriverName()
    public final static String dncDriverName = "Apache Derby Network Client JDBC Driver";


    // Hard-wired for JDBC
    //
    // Currently ASCII hex value of "SYSLVL01".
    public final static byte[] dncPackageConsistencyToken =
            {0x53, 0x59, 0x53, 0x4c, 0x56, 0x4c, 0x30, 0x31};

    // We will not set package VERSION in the initial release.
    // If we have to change the package version in the future then we can.
    public static final String dncPackageVersion = null;

    // for Driver.jdbcCompliant()
    public final static boolean jdbcCompliant = true;

    // for Driver.getCompatibileJREVersions()
    public final static String[] dncCompatibleJREVersions =
            {"1.4", "1.5", "1.6"};

    //---------------------- database URL protocols ------------------------------

    // For DatabaseMetaData.getURL()
    public final static String jdbcDerbyNETProtocol = "jdbc:derby://";

    // -------------------------- metrics ----------------------
    // Not currently used by production builds.
    // We can't really use this stuff with tracing enabled, the results are not accurate.

    // -------------------------- compiled in properties -------------------------

    public final static boolean enableNetConnectionPooling = true;

    final static boolean rangeCheckCrossConverters = true;

    // Define different levels of bug checking, for now turn all bits on.
    final static int bugCheckLevel = 0xff;

    // --------------------------- connection defaults ---------------------------

    // This is the DERBY default and maps to DERBY's "Cursor Stability".
    public final static int defaultIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED;

    // ---------------------------- statement defaults----------------------------

    public static final int defaultFetchSize = 64;

    // Prepare attribute constants
    public static final String cursorAttribute_SensitiveStatic = "SENSITIVE STATIC SCROLL ";
    public static final String cursorAttribute_SensitiveStaticRowset = cursorAttribute_SensitiveStatic;
    public static final String cursorAttribute_SensitiveDynamic = "SENSITIVE DYNAMIC SCROLL ";
    public static final String cursorAttribute_SensitiveDynamicRowset = "SENSITIVE DYNAMIC SCROLL WITH ROWSET POSITIONING ";
    public static final String cursorAttribute_Insensitive = "INSENSITIVE SCROLL ";
    public static final String cursorAttribute_InsensitiveRowset = cursorAttribute_Insensitive;

    // uncomment the following when we want to use multi-row fetch to support sensitive static and
    // insensitve cursors whenever the server has support for it.
    //public static final String cursorAttribute_SensitiveStaticRowset = "SENSITIVE STATIC SCROLL WITH ROWSET POSITIONING ";
    //public static final String cursorAttribute_InsensitiveRowset = "INSENSITIVE SCROLL WITH ROWSET POSITIONING ";

    public static final String cursorAttribute_ForUpdate = "FOR UPDATE ";
    public static final String cursorAttribute_ForReadOnly = "FOR READ ONLY ";

    public static final String cursorAttribute_WithHold = "WITH HOLD ";

    // -----------------------Load resource bundles for the driver asap-----------

    private static final String packageNameForDNC = "org.apache.derby.client"; // NOTUSED

    public static SqlException exceptionsOnLoadResources = null; // used by ClientDriver to accumulate load exceptions

    static {
        try {
            loadProductVersionHolder();
        } catch (SqlException e) {
            exceptionsOnLoadResources = e;
        }
        String _jreLevel;
        try {
            _jreLevel = System.getProperty("java.version");
        } catch (SecurityException e) {
            _jreLevel = "1.3.0";
        } // ignore it, assume 1.3.0
        jreLevel = _jreLevel;
        java.util.StringTokenizer st = new java.util.StringTokenizer(jreLevel, ".");
        int jreState = 0;
        int _jreLevelMajor = 1;
        int _jreLevelMinor = 3;
        while (st.hasMoreTokens()) {
            int i;
            try {
                i = java.lang.Integer.parseInt(st.nextToken()); // get int value
            } catch (NumberFormatException e) {
                i = 0;
            }
            switch (jreState++) {
            case 0:
                _jreLevelMajor = i; // state 0, this is the major version
                break;
            case 1:
                _jreLevelMinor = i; // state 1, this is the minor version
                break;
            default:
                break; // state >1, ignore
            }
        }
        jreLevelMajor = _jreLevelMajor;
        jreLevelMinor = _jreLevelMinor;
    }

    /**
     * load product version information and accumulate exceptions
     */
    private static void loadProductVersionHolder() throws SqlException {
        try {
            dncProductVersionHolder__ = buildProductVersionHolder();
        } catch (java.security.PrivilegedActionException e) {
            throw new SqlException(null, 
                    new ClientMessageId (SQLState.ERROR_PRIVILEGED_ACTION),
                    e.getException());                    
        } catch (java.io.IOException ioe) {
            throw SqlException.javaException(null, ioe);
        }
    }


    // Create ProductVersionHolder in security block for Java 2 security.
    private static ProductVersionHolder buildProductVersionHolder() throws
            java.security.PrivilegedActionException, IOException {
        ProductVersionHolder myPVH = null;
        myPVH = AccessController.doPrivileged(
                new PrivilegedExceptionAction<ProductVersionHolder>() {

                    public ProductVersionHolder run() throws IOException {
                        InputStream versionStream = getClass().getResourceAsStream(ProductGenusNames.DNC_INFO);

                        return ProductVersionHolder.getProductVersionHolderFromMyEnv(versionStream);
                    }
                });

        return myPVH;
    }
    
    /**
     * Check to see if the jvm version is such that JDBC 4.0 is supported
     */
    
    public static boolean supportsJDBC40() {
        // use reflection to identify whether we support JDBC40
        try {
            Class.forName("java.sql.SQLXML");
            return true;
        } catch (Exception e) {
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1538.java