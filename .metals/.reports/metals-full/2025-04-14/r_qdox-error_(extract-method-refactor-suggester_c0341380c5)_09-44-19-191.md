error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6614.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6614.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6614.java
text:
```scala
e@@ntity = (JDBCEntityBridge)cmrField.getEntity();

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ejb.plugins.cmp.jdbc;

import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCCMRFieldBridge;
import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCEntityBridge;
import org.jboss.ejb.plugins.cmp.jdbc.metadata.JDBCRelationshipRoleMetaData;
import org.jboss.ejb.EntityEnterpriseContext;
import org.jboss.logging.Logger;
import org.jboss.deployment.DeploymentException;

import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public abstract class CascadeDeleteStrategy
{
   /**
    * No cascade-delete strategy.
    */
   public static final class NoneCascadeDeleteStrategy
      extends CascadeDeleteStrategy
   {
      public NoneCascadeDeleteStrategy(JDBCCMRFieldBridge cmrField)
      {
         super(cmrField);
      }

      public boolean removeFromRelations(EntityEnterpriseContext ctx, Object[] oldRelationsRef)
      {
         boolean removed = false;
         Object value = cmrField.getInstanceValue(ctx);
         if(cmrField.isCollectionValued())
         {
            Set c = (Set)value;
            if(!c.isEmpty())
            {
               removed = true;
               cmrField.setInstanceValue(ctx, null);
            }
         }
         else
         {
            if(value != null)
            {
               removed = true;
               cmrField.setInstanceValue(ctx, null);
            }
         }
         return removed;
      }

      public void cascadeDelete(EntityEnterpriseContext ctx, List oldValues)
         throws RemoveException
      {
         boolean debug = log.isDebugEnabled();
         for(int i = 0; i < oldValues.size(); ++i)
         {
            EJBLocalObject oldValue = (EJBLocalObject)oldValues.get(i);
            if(relatedManager.uncheduledCascadeDelete(oldValue))
            {
               if(debug)
                  log.debug("Removing " + oldValue);

               oldValue.remove();
            }
            else
            {
               if(debug)
                  log.debug(oldValue + " already removed");
            }
         }
      }
   }

   /**
    * Specification compliant cascade-delete strategy, i.e. one DELETE per child
    */
   public static final class DefaultCascadeDeleteStrategy
      extends CascadeDeleteStrategy
   {
      public DefaultCascadeDeleteStrategy(JDBCCMRFieldBridge cmrField)
      {
         super(cmrField);
      }

      public boolean removeFromRelations(EntityEnterpriseContext ctx, Object[] oldRelationsRef)
      {
         boolean removed = false;
         Object value = cmrField.getInstanceValue(ctx);
         if(cmrField.isCollectionValued())
         {
            Set c = (Set)value;
            if(!c.isEmpty())
            {
               removed = true;
               cmrField.scheduleChildrenForCascadeDelete(ctx);
               scheduleCascadeDelete(oldRelationsRef, new ArrayList(c));
               cmrField.setInstanceValue(ctx, null);
            }
         }
         else
         {
            if(value != null)
            {
               removed = true;
               cmrField.scheduleChildrenForCascadeDelete(ctx);
               scheduleCascadeDelete(oldRelationsRef, Collections.singletonList(value));
               cmrField.setInstanceValue(ctx, null);
            }
         }
         return removed;
      }

      public void cascadeDelete(EntityEnterpriseContext ctx, List oldValues) throws RemoveException
      {
         boolean debug = log.isDebugEnabled();
         for(int i = 0; i < oldValues.size(); ++i)
         {
            EJBLocalObject oldValue = (EJBLocalObject)oldValues.get(i);
            if(relatedManager.uncheduledCascadeDelete(oldValue))
            {
               if(debug)
                  log.debug("Removing " + oldValue);

               oldValue.remove();
            }
            else
            {
               if(debug)
                  log.debug(oldValue + " already removed");
            }
         }
      }
   }

   /**
    * Batch cascade-delete strategy. Deletes children with one statement of the form
    * DELETE FROM RELATED_TABLE WHERE FOREIGN_KEY = ?
    */
   public static final class BatchCascadeDeleteStrategy
      extends CascadeDeleteStrategy
   {
      private final String batchCascadeDeleteSql;

      public BatchCascadeDeleteStrategy(JDBCCMRFieldBridge cmrField)
         throws DeploymentException
      {
         super(cmrField);

         if(cmrField.hasForeignKey())
         {
            throw new DeploymentException(
               "Batch cascade-delete was setup for the role with a foreign key: relationship "
               + cmrField.getMetaData().getRelationMetaData().getRelationName()
               + ", role " + cmrField.getMetaData().getRelationshipRoleName()
               + ". Batch cascade-delete supported only for roles with no foreign keys."
            );
         }

         StringBuffer buf = new StringBuffer(100);
         buf.append("DELETE FROM ")
            .append(cmrField.getRelatedJDBCEntity().getTableName())
            .append(" WHERE ");
         SQLUtil.getWhereClause(cmrField.getRelatedCMRField().getForeignKeyFields(), buf);
         batchCascadeDeleteSql = buf.toString();

         log.debug(
            cmrField.getMetaData().getRelationMetaData().getRelationName() + " batch cascade delete SQL: "
            + batchCascadeDeleteSql
         );
      }

      public boolean removeFromRelations(EntityEnterpriseContext ctx, Object[] oldRelationsRef)
      {
         boolean removed = false;
         Object value = cmrField.getInstanceValue(ctx);
         if(cmrField.isCollectionValued())
         {
            Set c = (Set)value;
            if(!c.isEmpty())
            {
               removed = true;
               cmrField.scheduleChildrenForBatchCascadeDelete(ctx);
               scheduleCascadeDelete(oldRelationsRef, new ArrayList(c));
            }
         }
         else
         {
            if(value != null)
            {
               removed = true;
               cmrField.scheduleChildrenForBatchCascadeDelete(ctx);
               scheduleCascadeDelete(oldRelationsRef, Collections.singletonList(value));
            }
         }

         return removed;
      }

      public void cascadeDelete(EntityEnterpriseContext ctx, List oldValues) throws RemoveException
      {
         boolean didDelete = false;
         boolean debug = log.isDebugEnabled();
         for(int i = 0; i < oldValues.size(); ++i)
         {
            EJBLocalObject oldValue = (EJBLocalObject)oldValues.get(i);
            if(relatedManager.uncheduledCascadeDelete(oldValue))
            {
               if(debug)
                  log.debug("Removing " + oldValue);
               oldValue.remove();
               didDelete = true;
            }
            else
            {
               if(debug)
                  log.debug(oldValue + " already removed");
            }
         }

         if(didDelete)
         {
            executeDeleteSQL(batchCascadeDeleteSql, ctx.getId());
         }
      }
   }

   public static CascadeDeleteStrategy getCascadeDeleteStrategy(JDBCCMRFieldBridge cmrField)
      throws DeploymentException
   {
      CascadeDeleteStrategy result;
      JDBCRelationshipRoleMetaData relatedRole = cmrField.getMetaData().getRelatedRole();
      if(relatedRole.isBatchCascadeDelete())
      {
         result = new BatchCascadeDeleteStrategy(cmrField);
      }
      else if(relatedRole.isCascadeDelete())
      {
         result = new DefaultCascadeDeleteStrategy(cmrField);
      }
      else
      {
         result = new NoneCascadeDeleteStrategy(cmrField);
      }
      return result;
   }

   protected final JDBCCMRFieldBridge cmrField;
   protected final JDBCEntityBridge entity;
   protected final JDBCStoreManager relatedManager;
   protected final Logger log;

   public CascadeDeleteStrategy(JDBCCMRFieldBridge cmrField)
   {
      this.cmrField = cmrField;
      entity = cmrField.getEntity();
      relatedManager = cmrField.getRelatedManager();
      log = Logger.getLogger(getClass().getName() + "." + cmrField.getEntity().getEntityName());
   }

   public abstract boolean removeFromRelations(EntityEnterpriseContext ctx, Object[] oldRelationsRef);

   public abstract void cascadeDelete(EntityEnterpriseContext ctx, List oldValues) throws RemoveException;

   protected void scheduleCascadeDelete(Object[] oldRelationsRef, List values)
   {
      Map oldRelations = (Map)oldRelationsRef[0];
      if(oldRelations == null)
      {
         oldRelations = new HashMap();
         oldRelationsRef[0] = oldRelations;
      }
      oldRelations.put(cmrField, values);
      relatedManager.scheduleCascadeDelete(values);
   }

   protected void executeDeleteSQL(String sql, Object key) throws RemoveException
   {
      Connection con = null;
      PreparedStatement ps = null;
      int rowsAffected = 0;
      try
      {
         if(log.isDebugEnabled())
            log.debug("Executing SQL: " + sql);

         // get the connection
         con = entity.getDataSource().getConnection();
         ps = con.prepareStatement(sql);

         // set the parameters
         entity.setPrimaryKeyParameters(ps, 1, key);

         // execute statement
         rowsAffected = ps.executeUpdate();
      }
      catch(Exception e)
      {
         log.error("Could not remove " + key, e);
         throw new RemoveException("Could not remove " + key);
      }
      finally
      {
         JDBCUtil.safeClose(ps);
         JDBCUtil.safeClose(con);
      }

      // check results
      if(rowsAffected == 0)
      {
         log.error("Could not remove entity " + key);
         throw new RemoveException("Could not remove entity");
      }

      if(log.isDebugEnabled())
         log.debug("Remove: Rows affected = " + rowsAffected);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6614.java