error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9192.java
text:
```scala
S@@tring principalName = ctx.getCallerPrincipal().getName();

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package org.jboss.ejb.plugins.cmp.jdbc;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.management.MalformedObjectNameException;
import javax.sql.DataSource;

import org.jboss.deployment.DeploymentException;
import org.jboss.ejb.EntityEnterpriseContext;
import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCCMPFieldBridge;
import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCCMRFieldBridge;
import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCEntityBridge;
import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCFieldBridge;
import org.jboss.ejb.plugins.cmp.jdbc.metadata.JDBCEntityCommandMetaData;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.security.AuthenticationManager;

/**
 * Base class for create commands that drives the operation sequence.
 *
 * @author <a href="mailto:jeremy@boynes.com">Jeremy Boynes</a>
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public abstract class JDBCAbstractCreateCommand implements JDBCCreateCommand
{
   protected Logger log;
   protected boolean debug;
   protected boolean trace;
   protected JDBCEntityBridge entity;
   protected AuthenticationManager securityManager;
   protected boolean createAllowed;
   protected SQLExceptionProcessorMBean exceptionProcessor;
   protected String insertSQL;
   protected JDBCFieldBridge[] insertFields;
   protected boolean insertAfterEjbPostCreate;

   // Generated fields
   private JDBCCMPFieldBridge createdPrincipal;
   private JDBCCMPFieldBridge createdTime;
   private JDBCCMPFieldBridge updatedPrincipal;
   private JDBCCMPFieldBridge updatedTime;

   public void init(JDBCStoreManager manager) throws DeploymentException
   {
      log = Logger.getLogger(getClass().getName() + '.' + manager.getMetaData().getName());
      debug = log.isDebugEnabled();
      trace = log.isTraceEnabled();

      entity = manager.getEntityBridge();
      securityManager = manager.getContainer().getSecurityManager();

      insertAfterEjbPostCreate = manager.getContainer()
         .getBeanMetaData().getContainerConfiguration().isInsertAfterEjbPostCreate();

      // set create allowed
      createAllowed = true;
      JDBCCMPFieldBridge[] pkFields = entity.getPrimaryKeyFields();
      for(int i = 0; i < pkFields.length; i++)
      {
         if(pkFields[i].isReadOnly())
         {
            createAllowed = false;
            log.debug("Create will not be allowed because pk field "
               + pkFields[i].getFieldName() + "is read only.");
            break;
         }
      }

      initGeneratedFields();

      JDBCEntityCommandMetaData entityCommand = manager.getMetaData().getEntityCommand();
      if(entityCommand == null)
      {
         throw new DeploymentException("entity-command is null");
      }
      initEntityCommand(entityCommand);

      initInsertFields();
      initInsertSQL();
   }

   protected void initEntityCommand(JDBCEntityCommandMetaData entityCommand) throws DeploymentException
   {
      String objectName = entityCommand.getAttribute("SQLExceptionProcessor");
      if(objectName != null)
      {
         try
         {
            exceptionProcessor = (SQLExceptionProcessorMBean) MBeanProxyExt.create(SQLExceptionProcessorMBean.class, objectName);
         }
         catch(MalformedObjectNameException e)
         {
            throw new DeploymentException("Invalid object name for SQLExceptionProcessor: ", e);
         }
      }
   }

   public Object execute(Method m, Object[] args, EntityEnterpriseContext ctx) throws CreateException
   {
      // TODO: implement this logic nicer
      if(insertAfterEjbPostCreate)
      {
         if(!entity.isEjbCreateDone(ctx))
         {
            checkCreateAllowed();
            generateFields(ctx);
            entity.setEjbCreateDone(ctx);
         }
         else
         {
            beforeInsert(ctx);
            performInsert(ctx);
            afterInsert(ctx);
            entity.setCreated(ctx);
         }
      }
      else
      {
         checkCreateAllowed();
         generateFields(ctx);
         beforeInsert(ctx);
         performInsert(ctx);
         afterInsert(ctx);
         entity.setCreated(ctx);
      }
      return getPrimaryKey(ctx);
   }

   protected void checkCreateAllowed() throws CreateException
   {
      if(!createAllowed)
      {
         throw new CreateException("Creation is not allowed because a primary key field is read only.");
      }
   }

   protected JDBCCMPFieldBridge getGeneratedPKField() throws DeploymentException
   {
      // extract the pk field to be generated
      JDBCCMPFieldBridge pkField = null;
      JDBCCMPFieldBridge[] pkFields = entity.getPrimaryKeyFields();
      for(int i = 0; i < pkFields.length; ++i)
      {
         if(pkField != null)
            throw new DeploymentException("Generation only supported with single PK field.");
         pkField = pkFields[i];
      }
      return pkField;
   }

   protected void initGeneratedFields() throws DeploymentException
   {
      createdPrincipal = entity.getCreatedPrincipalField();
      if(securityManager == null && createdPrincipal != null)
      {
         throw new DeploymentException("No security-domain configured but created-by specified");
      }
      updatedPrincipal = entity.getUpdatedPrincipalField();
      if(securityManager == null && updatedPrincipal != null)
      {
         throw new DeploymentException("No security-domain configured but updated-by specified");
      }
      createdTime = entity.getCreatedTimeField();
      updatedTime = entity.getUpdatedTimeField();
   }

   protected void generateFields(EntityEnterpriseContext ctx) throws CreateException
   {
      // Audit principal fields
      if(securityManager != null)
      {
         String principalName = ctx.getEJBContext().getCallerPrincipal().getName();
         if(createdPrincipal != null && createdPrincipal.getInstanceValue(ctx) == null)
         {
            createdPrincipal.setInstanceValue(ctx, principalName);
         }
         /*
         if(updatedPrincipal != null && updatedPrincipal.getInstanceValue(ctx) == null)
         {
            updatedPrincipal.setInstanceValue(ctx, principalName);
         }
         */
      }

      // Audit time fields
      Date date = null;
      if(createdTime != null && createdTime.getInstanceValue(ctx) == null)
      {
         date = new Date();
         createdTime.setInstanceValue(ctx, date);
      }
      /*
      if(updatedTime != null && updatedTime.getInstanceValue(ctx) == null)
      {
         if(date == null)
            date = new Date();
         updatedTime.setInstanceValue(ctx, date);
      }
      */
   }

   protected void initInsertFields()
   {
      JDBCCMPFieldBridge[] fields = entity.getTableFields();
      List insertFieldsList = new ArrayList(fields.length);
      for(int i = 0; i < fields.length; i++)
      {
         JDBCFieldBridge field = fields[i];
         if(isInsertField(field))
            insertFieldsList.add(field);
      }

      insertFields = (JDBCFieldBridge[]) insertFieldsList.toArray(new JDBCFieldBridge[insertFieldsList.size()]);
   }

   protected boolean isInsertField(JDBCFieldBridge field)
   {
      boolean result =
         !(field instanceof JDBCCMRFieldBridge)
         && field.getJDBCType() != null
         && !field.isReadOnly();
      if(field instanceof JDBCCMPFieldBridge)
         result = result && !((JDBCCMPFieldBridge) field).isRelationTableField();
      return result;
   }

   protected void initInsertSQL()
   {
      StringBuffer sql = new StringBuffer(250);
      sql.append(SQLUtil.INSERT_INTO)
         .append(entity.getTableName())
         .append(" (");

      SQLUtil.getColumnNamesClause(insertFields, sql);

      sql.append(')')
         .append(SQLUtil.VALUES).append('(');
      SQLUtil.getValuesClause(insertFields, sql)
         .append(')');
      insertSQL = sql.toString();

      if(debug)
         log.debug("Insert Entity SQL: " + insertSQL);
   }

   protected void beforeInsert(EntityEnterpriseContext ctx) throws CreateException
   {
   }

   protected void performInsert(EntityEnterpriseContext ctx) throws CreateException
   {
      Connection c = null;
      PreparedStatement ps = null;
      try
      {
         if(debug)
            log.debug("Executing SQL: " + insertSQL);

         DataSource dataSource = entity.getDataSource();
         c = dataSource.getConnection();
         ps = prepareStatement(c, insertSQL, ctx);

         // set the parameters
         int index = 1;
         for(int fieldInd = 0; fieldInd < insertFields.length; ++fieldInd)
         {
            index = insertFields[fieldInd].setInstanceParameters(ps, index, ctx);
         }

         // execute statement
         int rowsAffected = executeInsert(ps, ctx);
         if(rowsAffected != 1)
         {
            throw new CreateException("Expected one affected row but update returned" + rowsAffected +
               " for id=" + ctx.getId());
         }
      }
      catch(SQLException e)
      {
         if(exceptionProcessor != null && exceptionProcessor.isDuplicateKey(e))
         {
            throw new DuplicateKeyException("Entity with primary key already exists");
         }
         else
         {
            log.error("Could not create entity", e);
            throw new CreateException("Could not create entity:" + e);
         }
      }
      finally
      {
         JDBCUtil.safeClose(ps);
         JDBCUtil.safeClose(c);
      }

      // Mark the inserted fields as clean.
      for(int fieldInd = 0; fieldInd < insertFields.length; ++fieldInd)
      {
         insertFields[fieldInd].setClean(ctx);
      }
   }

   protected PreparedStatement prepareStatement(Connection c, String sql, EntityEnterpriseContext ctx) throws SQLException
   {
      return c.prepareStatement(sql);
   }

   protected int executeInsert(PreparedStatement ps, EntityEnterpriseContext ctx) throws SQLException
   {
      return ps.executeUpdate();
   }

   protected void afterInsert(EntityEnterpriseContext ctx) throws CreateException
   {
   }

   protected Object getPrimaryKey(EntityEnterpriseContext ctx)
   {
      return entity.extractPrimaryKeyFromInstance(ctx);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9192.java