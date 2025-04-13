error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7085.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7085.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7085.java
text:
```scala
.@@append(entity.getQualifiedTableName())

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package org.jboss.ejb.plugins.cmp.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;

import org.jboss.ejb.EntityEnterpriseContext;
import org.jboss.deployment.DeploymentException;

/**
 * Base class for create commands that actually insert the primary key value.
 * If an exception processor is not supplied, this command will perform an
 * additional query to determine if a DuplicateKeyException should be thrown.
 *
 * @author <a href="mailto:jeremy@boynes.com">Jeremy Boynes</a>
 */
public abstract class JDBCInsertPKCreateCommand extends JDBCAbstractCreateCommand
{
   protected String existsSQL;

   public void init(JDBCStoreManager manager) throws DeploymentException
   {
      super.init(manager);

      // if no exception processor is defined, we will perform a existance
      // check before trying the insert to report duplicate key
      if(exceptionProcessor == null)
      {
         initExistsSQL();
      }
   }

   protected void initExistsSQL()
   {
      StringBuffer sql = new StringBuffer(300);
      sql.append(SQLUtil.SELECT).append("COUNT(*)").append(SQLUtil.FROM)
         .append(entity.getTableName())
         .append(SQLUtil.WHERE);
      SQLUtil.getWhereClause(entity.getPrimaryKeyFields(), sql);
      existsSQL = sql.toString();
      if(debug)
      {
         log.debug("Entity Exists SQL: " + existsSQL);
      }
   }

   protected void beforeInsert(EntityEnterpriseContext ctx) throws CreateException
   {
      // are we checking existance by query?
      if(existsSQL != null)
      {
         Connection c = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         try
         {
            if(debug)
               log.debug("Executing SQL: " + existsSQL);

            c = entity.getDataSource().getConnection();
            ps = c.prepareStatement(existsSQL);

            // bind PK
            // @todo add a method to EntityBridge that binds pk fields directly
            Object pk = entity.extractPrimaryKeyFromInstance(ctx);
            entity.setPrimaryKeyParameters(ps, 1, pk);

            rs = ps.executeQuery();
            if(!rs.next())
            {
               throw new CreateException("Error checking if entity with primary pk " + pk + "exists: SQL returned no rows");
            }
            if(rs.getInt(1) > 0)
            {
               throw new DuplicateKeyException("Entity with primary key " + pk + " already exists");
            }
         }
         catch(SQLException e)
         {
            log.error("Error checking if entity exists", e);
            throw new CreateException("Error checking if entity exists:" + e);
         }
         finally
         {
            JDBCUtil.safeClose(rs);
            JDBCUtil.safeClose(ps);
            JDBCUtil.safeClose(c);
         }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7085.java