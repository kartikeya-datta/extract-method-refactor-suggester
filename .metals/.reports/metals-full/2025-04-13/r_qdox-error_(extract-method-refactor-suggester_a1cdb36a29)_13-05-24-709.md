error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3516.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3516.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3516.java
text:
```scala
C@@lass wrapperClass = loader.loadClass("org.jboss.jca.adapters.jdbc.StatementAccess");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.cmp.jdbc.keygen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.EJBException;
import org.jboss.as.cmp.CmpMessages;
import static org.jboss.as.cmp.CmpMessages.MESSAGES;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCIdentityColumnCreateCommand;
import org.jboss.as.cmp.jdbc.JDBCStoreManager;
import org.jboss.as.cmp.jdbc.JDBCUtil;
import org.jboss.as.cmp.jdbc.metadata.JDBCEntityCommandMetaData;

/**
 * Create command for MySQL that uses the driver's getGeneratedKeys method
 * to retrieve AUTO_INCREMENT values.
 *
 * @author <a href="mailto:jeremy@boynes.com">Jeremy Boynes</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 81030 $
 */
public class JDBCMySQLCreateCommand extends JDBCIdentityColumnCreateCommand {
    private String className;
    private String methodName;
    private Method method;
    private Method getUnderlyingStatement;

    public void init(JDBCStoreManager manager) {
        super.init(manager);
        ClassLoader loader = GetTCLAction.getContextClassLoader();
        try {
            Class psClass = loader.loadClass(className);
            method = psClass.getMethod(methodName);
        } catch (ClassNotFoundException e) {
            throw MESSAGES.failedToLoadDriverClass(className, e);
        } catch (NoSuchMethodException e) {
            throw MESSAGES.driverDoesNotHaveMethod(className, methodName);
        }

        try {
            Class wrapperClass = loader.loadClass("org.jboss.resource.adapter.jdbc.StatementAccess");
            getUnderlyingStatement = wrapperClass.getMethod("getUnderlyingStatement");
        } catch (ClassNotFoundException e) {
            throw MESSAGES.couldNotLoadStatementAccess(e);
        } catch (NoSuchMethodException e) {
            throw MESSAGES.getUnderlyingStatementNotFound(e);
        }
    }

    protected void initEntityCommand(JDBCEntityCommandMetaData entityCommand) {
        super.initEntityCommand(entityCommand);
        className = entityCommand.getAttribute("class-name");
        if (className == null) {
            className = "com.mysql.jdbc.PreparedStatement";
        }
        methodName = entityCommand.getAttribute("method");
        if (methodName == null) {
            methodName = "getGeneratedKeys";
        }
    }

    protected int executeInsert(int paramIndex, PreparedStatement ps, CmpEntityBeanContext ctx) throws SQLException {
        int rows = ps.executeUpdate();

        // remove any JCA wrappers
        Statement stmt = ps;
        do {
            try {
                Object[] args = {};
                stmt = (Statement) getUnderlyingStatement.invoke(stmt, args);
            } catch (IllegalAccessException e) {
                SQLException ex = new SQLException("Failed to invoke getUnderlyingStatement");
                ex.initCause(e);
                throw ex;
            } catch (InvocationTargetException e) {
                SQLException ex = new SQLException("Failed to invoke getUnderlyingStatement");
                ex.initCause(e);
                throw ex;
            }
        } while (stmt != null && method.getDeclaringClass().isInstance(stmt) == false);

        ResultSet rs = null;
        try {
            rs = (ResultSet) method.invoke(stmt);
            if (!rs.next()) {
                throw MESSAGES.getGeneratedKeysEmptyResultSet();
            }
            pkField.loadInstanceResults(rs, 1, ctx);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw MESSAGES.errorExtractingGeneratedKey(e);
        } finally {
            JDBCUtil.safeClose(rs);
        }
        return rows;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3516.java