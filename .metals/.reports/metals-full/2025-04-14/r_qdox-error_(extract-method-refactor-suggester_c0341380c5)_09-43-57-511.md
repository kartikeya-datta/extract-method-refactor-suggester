error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5373.java
text:
```scala
t@@his.driver = driver.trim();

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Locale;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * Handles JDBC configuration needed by SQL type tasks.
 * <p>
 * The following example class prints the contents of the first column of each row in TableName.
 *</p>
 *<code><pre>
package examples;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.JDBCTask;

public class SQLExampleTask extends JDBCTask {

    private String tableName;

    public void execute() throws BuildException {
        Connection conn = getConnection();
        Statement stmt=null;
        try {
            if (tableName == null) {
                throw new BuildException("TableName must be specified",location);
            }
            String sql = "SELECT * FROM "+tableName;
            stmt= conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                log(rs.getObject(1).toString());
            }
        } catch (SQLException e) {

        } finally {
            if (stmt != null) {
                try {stmt.close();}catch (SQLException ingore) {}
            }
            if (conn != null) {
                try {conn.close();}catch (SQLException ingore) {}
            }
        }
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}


</pre></code>



 * @since Ant 1.5
 *
 */

public abstract class JDBCTask extends Task {


    /**
     * Used for caching loaders / driver. This is to avoid
     * getting an OutOfMemoryError when calling this task
     * multiple times in a row.
     */
    private static Hashtable loaderMap = new Hashtable(3);

    private boolean caching = true;

    private Path classpath;

    private AntClassLoader loader;

    /**
     * Autocommit flag. Default value is false
     */
    private boolean autocommit = false;

    /**
     * DB driver.
     */
    private String driver = null;

    /**
     * DB url.
     */
    private String url = null;

    /**
     * User name.
     */
    private String userId = null;

    /**
     * Password
     */
    private String password = null;

    /**
     * RDBMS Product needed for this SQL.
     **/
    private String rdbms = null;

    /**
     * RDBMS Version needed for this SQL.
     **/
    private String version = null;

    /**
     * Sets the classpath for loading the driver.
     * @param classpath The classpath to set
     */
    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    /**
     * Caching loaders / driver. This is to avoid
     * getting an OutOfMemoryError when calling this task
     * multiple times in a row; default: true
     * @param enable a <code>boolean</code> value
     */
    public void setCaching(boolean enable) {
        caching = enable;
    }

    /**
     * Add a path to the classpath for loading the driver.
     * @return a path to be configured
     */
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    /**
     * Set the classpath for loading the driver
     * using the classpath reference.
     * @param r a reference to a classpath
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    /**
     * Class name of the JDBC driver; required.
     * @param driver The driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Sets the database connection URL; required.
     * @param url The url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets the password; required.
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Auto commit flag for database connection;
     * optional, default false.
     * @param autocommit The autocommit to set
     */
    public void setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    /**
     * Execute task only if the lower case product name
     * of the DB matches this
     * @param rdbms The rdbms to set
     */
    public void setRdbms(String rdbms) {
        this.rdbms = rdbms;
    }

    /**
     * Sets the version string, execute task only if
     * rdbms version match; optional.
     * @param version The version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Verify we are connected to the correct RDBMS
     * @param conn the jdbc connection
     * @return true if we are connected to the correct RDBMS
     */
    protected boolean isValidRdbms(Connection conn) {
        if (rdbms == null && version == null) {
            return true;
        }

        try {
            DatabaseMetaData dmd = conn.getMetaData();

            if (rdbms != null) {
                String theVendor = dmd.getDatabaseProductName().toLowerCase();

                log("RDBMS = " + theVendor, Project.MSG_VERBOSE);
                if (theVendor == null || theVendor.indexOf(rdbms) < 0) {
                    log("Not the required RDBMS: " + rdbms, Project.MSG_VERBOSE);
                    return false;
                }
            }

            if (version != null) {
                String theVersion = dmd.getDatabaseProductVersion().toLowerCase(Locale.ENGLISH);

                log("Version = " + theVersion, Project.MSG_VERBOSE);
                if (theVersion == null
 !(theVersion.startsWith(version)
 theVersion.indexOf(" " + version) >= 0)) {
                    log("Not the required version: \"" + version + "\"", Project.MSG_VERBOSE);
                    return false;
                }
            }
        } catch (SQLException e) {
            // Could not get the required information
            log("Failed to obtain required RDBMS information", Project.MSG_ERR);
            return false;
        }

        return true;
    }

    /**
     * Get the cache of loaders and drivers.
     * @return a hashtable
     */
    protected static Hashtable getLoaderMap() {
        return loaderMap;
    }

    /**
     * Get the classloader used to create a driver.
     * @return the classloader
     */
    protected AntClassLoader getLoader() {
        return loader;
    }

    /**
     * Creates a new Connection as using the driver, url, userid and password
     * specified.
     *
     * The calling method is responsible for closing the connection.
     *
     * @return Connection the newly created connection.
     * @throws BuildException if the UserId/Password/Url is not set or there
     * is no suitable driver or the driver fails to load.
     */
    protected Connection getConnection() throws BuildException {
        if (userId == null) {
            throw new BuildException("UserId attribute must be set!", getLocation());
        }
        if (password == null) {
            throw new BuildException("Password attribute must be set!", getLocation());
        }
        if (url == null) {
            throw new BuildException("Url attribute must be set!", getLocation());
        }
        try {

            log("connecting to " + getUrl(), Project.MSG_VERBOSE);
            Properties info = new Properties();
            info.put("user", getUserId());
            info.put("password", getPassword());
            Connection conn = getDriver().connect(getUrl(), info);

            if (conn == null) {
                // Driver doesn't understand the URL
                throw new SQLException("No suitable Driver for " + url);
            }

            conn.setAutoCommit(autocommit);
            return conn;
        } catch (SQLException e) {
            throw new BuildException(e, getLocation());
        }

    }

    /**
     * Gets an instance of the required driver.
     * Uses the ant class loader and the optionally the provided classpath.
     * @return Driver
     * @throws BuildException
     */
    private Driver getDriver() throws BuildException {
        if (driver == null) {
            throw new BuildException("Driver attribute must be set!", getLocation());
        }

        Driver driverInstance = null;
        try {
            Class dc;
            if (classpath != null) {
                // check first that it is not already loaded otherwise
                // consecutive runs seems to end into an OutOfMemoryError
                // or it fails when there is a native library to load
                // several times.
                // this is far from being perfect but should work
                // in most cases.
                synchronized (loaderMap) {
                    if (caching) {
                        loader = (AntClassLoader) loaderMap.get(driver);
                    }
                    if (loader == null) {
                        log("Loading " + driver
                            + " using AntClassLoader with classpath "
                            + classpath, Project.MSG_VERBOSE);
                        loader = getProject().createClassLoader(classpath);
                        if (caching) {
                            loaderMap.put(driver, loader);
                        }
                    } else {
                        log("Loading " + driver
                            + " using a cached AntClassLoader.",
                                Project.MSG_VERBOSE);
                    }
                }
                dc = loader.loadClass(driver);
            } else {
                log("Loading " + driver + " using system loader.",
                    Project.MSG_VERBOSE);
                dc = Class.forName(driver);
            }
            driverInstance = (Driver) dc.newInstance();
        } catch (ClassNotFoundException e) {
            throw new BuildException(
                    "Class Not Found: JDBC driver " + driver + " could not be loaded",
                    e,
                    getLocation());
        } catch (IllegalAccessException e) {
            throw new BuildException(
                    "Illegal Access: JDBC driver " + driver + " could not be loaded",
                    e,
                    getLocation());
        } catch (InstantiationException e) {
            throw new BuildException(
                    "Instantiation Exception: JDBC driver " + driver + " could not be loaded",
                    e,
                    getLocation());
        }
        return driverInstance;
    }


    /**
     * Set the caching attribute.
     * @param value a <code>boolean</code> value
     */
    public void isCaching(boolean value) {
        caching = value;
    }

    /**
     * Gets the classpath.
     * @return Returns a Path
     */
    public Path getClasspath() {
        return classpath;
    }

    /**
     * Gets the autocommit.
     * @return Returns a boolean
     */
    public boolean isAutocommit() {
        return autocommit;
    }

    /**
     * Gets the url.
     * @return Returns a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the userId.
     * @return Returns a String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user name for the connection; required.
     * @param userId The userId to set
     */
    public void setUserid(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the password.
     * @return Returns a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the rdbms.
     * @return Returns a String
     */
    public String getRdbms() {
        return rdbms;
    }

    /**
     * Gets the version.
     * @return Returns a String
     */
    public String getVersion() {
        return version;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5373.java