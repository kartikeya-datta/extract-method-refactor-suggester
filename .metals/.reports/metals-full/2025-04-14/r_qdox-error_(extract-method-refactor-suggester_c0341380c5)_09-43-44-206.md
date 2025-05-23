error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/409.java
text:
```scala
i@@f (!jSQL.get(i).equalsIgnoreCase(expectedSQL)) {

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
 package org.apache.openjpa.persistence.criteria;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import junit.framework.TestCase;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.DerbyDictionary;
import org.apache.openjpa.jdbc.sql.HSQLDictionary;
import org.apache.openjpa.jdbc.sql.OracleDictionary;
import org.apache.openjpa.lib.jdbc.AbstractJDBCListener;
import org.apache.openjpa.lib.jdbc.JDBCEvent;
import org.apache.openjpa.lib.jdbc.JDBCListener;
import org.apache.openjpa.lib.jdbc.ReportingSQLException;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.test.AllowFailure;

public abstract class AbstractCriteriaTestCase extends TestCase {

    protected abstract SQLAuditor getAuditor();

    protected abstract OpenJPAEntityManagerFactorySPI getEntityManagerFactory();
    
    protected abstract EntityManager getEntityManager();
    private DBDictionary dict = null;

    public DBDictionary getDictionary() {
        return dict;
    }

    /**
     * Create an entity manager factory for persistence unit <code>pu</code>. Put {@link #CLEAR_TABLES} in this list to
     * tell the test framework to delete all table contents before running the tests.
     * 
     * @param props
     *            list of persistent types used in testing and/or configuration values in the form
     *            key,value,key,value...
     */
    protected OpenJPAEntityManagerFactorySPI createNamedEMF(Class<?>... types) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true," + "SchemaAction='add')");
        map.put("openjpa.jdbc.QuerySQLCache", "false");
        map.put("openjpa.DynamicEnhancementAgent", "false");
        map.put("openjpa.RuntimeUnenhancedClasses", "unsupported");
        map.put("openjpa.Compatibility", "QuotedNumbersInQueries=true");
        map.put("openjpa.jdbc.JDBCListeners", new JDBCListener[] { getAuditor() });

        StringBuffer buf = new StringBuffer();
        for (Class<?> c : types) {
            if (buf.length() > 0)
                buf.append(";");
            buf.append(c.getName());
        }

        map.put("openjpa.MetaDataFactory", "jpa(Types=" + buf.toString() + ")");

        Map<Object, Object> config = new HashMap<Object, Object>(System.getProperties());
        config.putAll(map);
        return (OpenJPAEntityManagerFactorySPI) Persistence.createEntityManagerFactory("test", config);
    }

    void setDictionary() {
        JDBCConfiguration conf = (JDBCConfiguration) getEntityManagerFactory().getConfiguration();
        dict = conf.getDBDictionaryInstance();
        if (dict instanceof DerbyDictionary || dict instanceof HSQLDictionary) {
            dict.requiresCastForComparisons = false;
            dict.requiresCastForMathFunctions = false;
        } else if (dict instanceof OracleDictionary) {
            dict.setJoinSyntax("sql92");
        } 
    }

    /**
     * Executes the given CriteriaQuery and JPQL string and compare their respective SQLs for equality.
     */
    void assertEquivalence(CriteriaQuery<?> c, String jpql, String expectedSQL) {
        assertEquivalence(c, jpql, null, null, expectedSQL);
    }

    /**
     * Executes the given CriteriaQuery and JPQL string and compare their respective SQLs for equality.
     */
    void assertEquivalence(CriteriaQuery<?> c, String jpql) {
        System.err.println("JPQL :" + jpql);
        System.err.println("CJQL :" + ((CriteriaQueryImpl<?>)c).toCQL());
        assertEquivalence(c, jpql, null, null, null);
    }

    /**
     * Executes the given CriteriaQuery and JPQL string and compare their respective SQLs for equality. Sets the
     * supplied parameters, if any.
     */
    void assertEquivalence(CriteriaQuery<?> c, String jpql, String[] paramNames, Object[] params) {
        assertEquivalence(c, jpql, paramNames, params, null);
    }
    
    /**
     * Executes the given CriteriaQuery and JPQL string and compare their respective SQLs for equality. Sets the
     * supplied parameters, if any.
     */
    void assertEquivalence(CriteriaQuery<?> c, String jpql, String[] paramNames, Object[] params,
        String expectedSQL) {
        Query cQ = getEntityManager().createQuery(c);
        Query jQ = getEntityManager().createQuery(jpql);
        setParameters(cQ, paramNames, params);
        setParameters(jQ, paramNames, params);

        executeAndCompareSQL(jpql, cQ, jQ, expectedSQL);
    }

    /**
     * Executes the given CriteriaQuery and JPQL string and compare their respective SQLs for equality.
     */
    void assertEquivalence(CriteriaQuery<?> c, String jpql, Object[] params) {
        Query jQ = getEntityManager().createQuery(jpql);
        Query cQ = getEntityManager().createQuery(c);
        setParameters(cQ, params);
        setParameters(jQ, params);

        executeAndCompareSQL(jpql, cQ, jQ, null);
    }

    /**
     * Execute the two given queries. The first query originated from a JPQL string must be well-formed. The second
     * query originated from a Criteria is being tested.
     * 
     * @param sqls
     *            The target SQL for the queries will be filled-in the given array.
     * @return true if both queries execute successfully.
     */
    void executeAndCompareSQL(String jpql, Query cQ, Query jQ, String expectedSQL) {
        List<String> jSQL = null;
        List<String> cSQL = null;
        try {
            jSQL = executeQueryAndCollectSQL(jQ);
        } catch (Exception e) {
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            fail("JPQL " + jpql + " failed to execute\r\n" + w);
        }
        getEntityManager().clear();
        try {
            cSQL = executeQueryAndCollectSQL(cQ);
        } catch (Exception e) {
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            fail("CriteriaQuery corresponding to " + jpql + " failed to execute\r\n" + w);
        }

        if (jSQL.size() != cSQL.size()) {
            printSQL("Target SQL for JPQL", jSQL);
            printSQL("Target SQL for CriteriaQuery", cSQL);
            assertEquals("No. of SQL generated for JPQL and CriteriaQuery for " + jpql + " is different", jSQL.size(),
                cSQL.size());
        }

        if (!(dict instanceof DerbyDictionary))
            return;

        for (int i = 0; i < jSQL.size(); i++) {
            if (!jSQL.get(i).equalsIgnoreCase(cSQL.get(i))) {
                printSQL("Target SQL for JPQL", jSQL);
                printSQL("Target SQL for CriteriaQuery", cSQL);
                assertTrue(i + "-th SQL for JPQL and CriteriaQuery for " + jpql + " is different", 
                        jSQL.get(i).equalsIgnoreCase(cSQL.get(i)));
            }
        }

        if (expectedSQL != null) {
            assertTrue("SQL for JPQL " + jpql + " is different than expecetd " + expectedSQL, 
                    jSQL.get(0).equalsIgnoreCase(expectedSQL));
            
        }
    }

    void executeAndCompareSQL(String jpql, String expectedSQL) {
        Query jQ = getEntityManager().createQuery(jpql);

        List<String> jSQL = null;
        try {
            jSQL = executeQueryAndCollectSQL(jQ);
        } catch (Exception e) {
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            fail("JPQL " + jpql + " failed to execute\r\n" + w);
        }

        if (!(dict instanceof DerbyDictionary))
            return;

        for (int i = 0; i < jSQL.size(); i++) {
            if (!jSQL.get(i).equals(expectedSQL)) {
                printSQL("SQL for JPQL", jSQL.get(i));
                printSQL("Expected SQL", expectedSQL);
                assertTrue(i + "-th SQL for JPQL: " + jpql + " are different than Expected SQL " + expectedSQL, 
                    expectedSQL.equalsIgnoreCase(jSQL.get(i)));
            }
        }
    }

    void executeAndCompareSQL(Query jQ, String expectedSQL) {
        List<String> jSQL = null;
        try {
            jSQL = executeQueryAndCollectSQL(jQ);
        } catch (Exception e) {
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            fail(w.toString());
        }

        if (!(dict instanceof DerbyDictionary))
            return;

        String jSql = jSQL.get(0).trim();
        if (jSql.indexOf("optimize for 1 row") != -1)
            jSql = jSql.substring(0, jSql.indexOf("optimize for 1 row")).trim();

        if (!jSql.equalsIgnoreCase(expectedSQL)) {
            printSQL("SQL for JPQL", jSql);
            assertTrue("SQL for JPQL " + jSql + " is different than expecetd " + expectedSQL,
                    expectedSQL.equalsIgnoreCase(jSql));
        }
    }

    void executeExpectFail(CriteriaQuery<?> c, String jpql) {
        try {
            Query cQ = getEntityManager().createQuery(c);
            executeQueryAndCollectSQL(cQ);
            fail("CriteriaQuery corresponding to " + jpql + " is expected to fail\r\n");
        } catch (Exception e) {
            // expected
        }
    }

    void executeExpectFail(CriteriaQuery<?> c, String jpql, String[] paramNames, Object[] params) {
        try {
            Query cQ = getEntityManager().createQuery(c);
            for (int i = 0; i < params.length; i++)
                cQ.setParameter(paramNames[i], params[i]);
            executeQueryAndCollectSQL(cQ);
            fail("CriteriaQuery corresponding to " + jpql + " is expected to fail\r\n");
        } catch (Exception e) {
            // expected
        }
    }

    void executeExpectFail(String jpql) {
        try {
            Query jQ = getEntityManager().createQuery(jpql);
            executeQueryAndCollectSQL(jQ);
            fail("JPQL " + jpql + " is expected to Failed to execute\r\n");
        } catch (Exception e) {
            // expected
        }
    }

    void executeExpectFail(String jpql, String[] paramNames, Object[] params) {
        try {
            Query jQ = getEntityManager().createQuery(jpql);
            for (int i = 0; i < params.length; i++)
                jQ.setParameter(paramNames[i], params[i]);
            executeQueryAndCollectSQL(jQ);
            fail("JPQL " + jpql + " is expected to Failed to execute\r\n");
        } catch (Exception e) {
            // expected
        }
    }

    void printSQL(String header, String sql) {
        System.err.println(header);
        System.err.println(sql);
    }

    void printSQL(String header, List<String> sqls) {
        System.err.println(header);
        for (int i = 0; sqls != null && i < sqls.size(); i++) {
            System.err.println(i + ":" + sqls.get(i));
        }
    }

    void setParameters(Query q, String[] paramNames, Object[] params) {
        for (int i = 0; paramNames != null && i < paramNames.length; i++)
            q.setParameter(paramNames[i], params[i]);
    }

    void setParameters(Query q, Object[] params) {
        for (int i = 0; params != null && i < params.length; i++)
            q.setParameter(i + 1, params[i]);
    }

    /**
     * Execute the given query and return the generated SQL. If the query execution fail because the generated SQL is
     * ill-formed, then raised exception should carry the ill-formed SQL for diagnosis.
     */
    List<String> executeQueryAndCollectSQL(Query q) {
        getAuditor().clear();
        try {
            q.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(extractSQL(e), e);
        }
        assertFalse(getAuditor().getSQLs().isEmpty());
        return getAuditor().getSQLs();
    }

    void executeAndCompareSQL(CriteriaQuery<?> q, String expectedSQL) {
        executeAndCompareSQL(getEntityManager().createQuery(q), expectedSQL);
    }

    String extractSQL(Exception e) {
        Throwable t = e.getCause();
        if (t instanceof ReportingSQLException) {
            return ((ReportingSQLException) t).getSQL();
        }
        return "Can not extract SQL from exception " + e;
    }

    @Override
    public void runBare() throws Throwable {
        try {
            super.runBare();
        } catch (Throwable t) {
            AllowFailure allowFailure = getAllowFailure();
            if (allowFailure != null && allowFailure.value()) {
                System.err.println("*** FAILED (but ignored): " + this);
                System.err.println("***              Reason : " + allowFailure.message());
                System.err.println("Stacktrace of failure");
                t.printStackTrace();
            } else {
                throw t;
            }
        }
    }

    /**
     * Affirms if the test case or the test method is annotated with
     * 
     * @AllowFailure. Method level annotation has higher precedence than Class level annotation.
     * 
     *                Set -DIgnoreAllowFailure=true to ignore this directive altogether.
     */
    protected AllowFailure getAllowFailure() {
        if (Boolean.getBoolean("IgnoreAllowFailure")) {
            return null;
        }
        try {
            Method runMethod = getClass().getMethod(getName(), (Class[]) null);
            AllowFailure anno = runMethod.getAnnotation(AllowFailure.class);
            if (anno != null) {
                return anno;
            }
        } catch (SecurityException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return getClass().getAnnotation(AllowFailure.class);
    }

    public class SQLAuditor extends AbstractJDBCListener {
        private List<String> sqls = new ArrayList<String>();

        @Override
        public void beforeExecuteStatement(JDBCEvent event) {
            if (event.getSQL() != null && sqls != null) {
                sqls.add(event.getSQL());
            }
        }

        void clear() {
            sqls.clear();
        }

        List<String> getSQLs() {
            return new ArrayList<String>(sqls);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/409.java