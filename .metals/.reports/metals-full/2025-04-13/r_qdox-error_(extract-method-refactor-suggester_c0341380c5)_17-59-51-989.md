error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7250.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7250.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7250.java
text:
```scala
C@@lass psClass = GetTCLAction.getContextClassLoader().loadClass(className);

/***************************************
 *                                     *
 *  JBoss: The OpenSource J2EE WebOS   *
 *                                     *
 *  Distributable under LGPL license.  *
 *  See terms of license at gnu.org.   *
 *                                     *
 ***************************************/
package org.jboss.ejb.plugins.cmp.jdbc.keygen;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.lang.reflect.Method;

import javax.ejb.EJBException;

import org.jboss.ejb.plugins.cmp.jdbc.JDBCIdentityColumnCreateCommand;
import org.jboss.ejb.plugins.cmp.jdbc.WrappedStatement;
import org.jboss.ejb.plugins.cmp.jdbc.JDBCStoreManager;
import org.jboss.ejb.plugins.cmp.jdbc.metadata.JDBCEntityCommandMetaData;
import org.jboss.ejb.EntityEnterpriseContext;
import org.jboss.deployment.DeploymentException;

/**
 * Create command for Informix that uses the driver's getSerial method
 * to retrieve SERIAL values. Also supports SERIAL8 columns if method
 * attribute of entity-command is set to "getSerial8"
 *
 * @author <a href="mailto:jeremy@boynes.com">Jeremy Boynes</a>
 */
public class JDBCInformixCreateCommand extends JDBCIdentityColumnCreateCommand
{
   private static final String NAME = "class-name";
   private static final String DEFAULT_CLASS = "com.informix.jdbc.IfxStatement";
   private static final String METHOD = "method";
   private static final String DEFAULT_METHOD = "getSerial";

   private String className;
   private String methodName;
   private Method method;

   public void init(JDBCStoreManager manager) throws DeploymentException
   {
      super.init(manager);
      try
      {
         Class psClass = Thread.currentThread().getContextClassLoader().loadClass(className);
         method = psClass.getMethod(methodName, null);
      }
      catch(ClassNotFoundException e)
      {
         throw new DeploymentException("Could not load driver class: " + className, e);
      }
      catch(NoSuchMethodException e)
      {
         throw new DeploymentException("Driver does not have method: " + methodName + "()");
      }
   }

   protected void initEntityCommand(JDBCEntityCommandMetaData entityCommand) throws DeploymentException
   {
      super.initEntityCommand(entityCommand);
      className = entityCommand.getAttribute(NAME);
      if(className == null)
      {
         className = DEFAULT_CLASS;
      }
      methodName = entityCommand.getAttribute(METHOD);
      if(methodName == null)
      {
         methodName = DEFAULT_METHOD;
      }
   }

   protected int executeInsert(int paramIndex, PreparedStatement ps, EntityEnterpriseContext ctx) throws SQLException
   {
      int rows = ps.executeUpdate();

      // remove any JCA wrappers
      while(ps instanceof WrappedStatement)
      {
         ps = (PreparedStatement)((WrappedStatement)ps).getUnderlyingStatement();
      }
      try
      {
         Number pk = (Number)method.invoke(ps, null);
         pkField.setInstanceValue(ctx, pk);
         return rows;
      }
      catch(RuntimeException e)
      {
         throw e;
      }
      catch(Exception e)
      {
         // throw EJBException to force a rollback as the row has been inserted
         throw new EJBException("Error extracting generated keys", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7250.java