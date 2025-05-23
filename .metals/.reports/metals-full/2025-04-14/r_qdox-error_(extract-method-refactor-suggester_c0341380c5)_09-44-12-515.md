error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8338.java
text:
```scala
L@@ocalHomeObjectFactory.rebind(jndiName, container.getEjbModule(), container);

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ejb.plugins.local;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.ejb.EJBMetaData;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.AccessLocalException;
import java.rmi.AccessException;
import javax.ejb.NoSuchObjectLocalException;
import java.rmi.NoSuchObjectException;
import javax.ejb.TransactionRequiredLocalException;
import javax.transaction.TransactionRequiredException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.transaction.TransactionRolledbackException;

import javax.naming.Name;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.ejb.Container;
import org.jboss.ejb.ContainerInvokerContainer;
import org.jboss.ejb.Interceptor;
import org.jboss.ejb.LocalContainerInvoker;
import org.jboss.ejb.CacheKey;
import org.jboss.ejb.LocalHomeObjectFactory;
import org.jboss.deployment.DeploymentException;
import org.jboss.invocation.Invocation;
import org.jboss.invocation.MarshalledInvocation;
import org.jboss.logging.Logger;
import org.jboss.metadata.MetaData;
import org.jboss.metadata.EntityMetaData;
import org.jboss.metadata.SessionMetaData;
import org.jboss.naming.Util;
import org.jboss.security.SecurityAssociation;
import org.jboss.tm.TransactionPropagationContextFactory;


/** The LocalContainerInvoker implementation that handles local ejb interface
 *proxies.
 *
 * @author <a href="mailto:docodan@mvcsoft.com">Daniel OConnor</a>
 * @author <a href="mailto:scott.stark@jboss.org">Scott Stark</a>
 */
public class BaseLocalContainerInvoker implements LocalContainerInvoker
{
   // Attributes ----------------------------------------------------
   protected static Logger log = Logger.getLogger(BaseLocalContainerInvoker.class);
   protected Container container;
   protected String jndiName;
   protected TransactionManager transactionManager;
   // The home can be one.
   protected EJBLocalHome home;
   // The Stateless Object can be one.
   protected EJBLocalObject statelessObject;
   
   protected Map beanMethodInvokerMap;
   protected Map homeMethodInvokerMap;
   
   // Static --------------------------------------------------------
   
   private static TransactionPropagationContextFactory tpcFactory;
   
   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------
   
   
   public String getJndiName()
   {
      return jndiName;
   }
   
   // ContainerService implementation -------------------------------
   public void setContainer(Container con)
   {
      this.container = con;
   }

   public void create() throws Exception
   {
   }

   public void start()
      throws Exception
   {
      ContainerInvokerContainer invokerContainer = (ContainerInvokerContainer) container;
      if( invokerContainer.getLocalClass() == null )
         return;

      Context iniCtx = new InitialContext();
      jndiName = container.getBeanMetaData().getJndiName();

      // Set the transaction manager and transaction propagation
      // context factory of the GenericProxy class
      transactionManager = (TransactionManager) iniCtx.lookup("java:/TransactionManager");

      // Create method mappings for container invoker
      Method[] methods = invokerContainer.getLocalClass().getMethods();
      beanMethodInvokerMap = new HashMap();
      for (int i = 0; i < methods.length; i++)
      {
         long hash = MarshalledInvocation.calculateHash(methods[i]);
         beanMethodInvokerMap.put(new Long(hash), methods[i]);
      }

      methods = invokerContainer.getLocalHomeClass().getMethods();
      homeMethodInvokerMap = new HashMap();
      for (int i = 0; i < methods.length; i++)
         homeMethodInvokerMap.put(new Long(MarshalledInvocation.calculateHash(methods[i])), methods[i]);
      Class localHome = invokerContainer.getLocalHomeClass();
      if(localHome == null)
      {
         log.debug(container.getBeanMetaData().getEjbName() + " cannot be Bound, doesn't have local home.");
         return;
      }

      String jndiName = container.getBeanMetaData().getLocalJndiName();
      String beanName = container.getBeanMetaData().getEjbName();

      // setup local home object factory, which is used for non-serializable objects such as local home
      // create link from unique name to application and container
      LocalHomeObjectFactory.rebind(jndiName, container.getApplication(), container);

      // address used to lookup referance in LocalHomeObjectFactory
      StringRefAddr refAddr = new StringRefAddr("nns", jndiName);

      // create a jndi referance to LocalHomeObjectFactory
      Reference jndiRef = new Reference(container.getBeanMetaData().getLocalHome(),
         refAddr, LocalHomeObjectFactory.class.getName(), null );

      // bind that referance to my name
      Util.rebind(iniCtx, jndiName, jndiRef);
      log.debug("Bound Local " + beanName + " to " + jndiName);
   }

   public void stop()
   {
      if( container == null )
         return;

      Class localHome = ((ContainerInvokerContainer)container).getLocalHomeClass();
      if(localHome == null)
      {
         return;
      }
      
      try
      {
         InitialContext ctx = new InitialContext();
         ctx.unbind(container.getBeanMetaData().getLocalJndiName());
      }
      catch (Exception e)
      {
         // ignore.
      }
   }
   public void destroy()
   {
   }

   // ContainerInvoker implementation -------------------------------
   public EJBLocalHome getEJBLocalHome()
   {
      ContainerInvokerContainer cic = (ContainerInvokerContainer) container;
      return (EJBLocalHome) Proxy.newProxyInstance(
         cic.getLocalHomeClass().getClassLoader(),
         new Class[] {cic.getLocalHomeClass()}, new HomeProxy() );
   }

   public EJBLocalObject getStatelessSessionEJBLocalObject()
   {
      ContainerInvokerContainer cic = (ContainerInvokerContainer) container;
      return (EJBLocalObject) Proxy.newProxyInstance(
         cic.getLocalClass().getClassLoader(),
         new Class[] {cic.getLocalClass()}, new StatelessSessionProxy() );
   }

   public EJBLocalObject getStatefulSessionEJBLocalObject(Object id)
   {
      ContainerInvokerContainer cic = (ContainerInvokerContainer) container;
      return (EJBLocalObject) Proxy.newProxyInstance(
         cic.getLocalClass().getClassLoader(),
         new Class[] {cic.getLocalClass()}, new StatefulSessionProxy(id) );
   }
   
   public EJBLocalObject getEntityEJBLocalObject(Object id)
   {
      ContainerInvokerContainer cic = (ContainerInvokerContainer) container;
      return (EJBLocalObject) Proxy.newProxyInstance(
         cic.getLocalClass().getClassLoader(),
         new Class[] {cic.getLocalClass()}, new EntityProxy(id ) );
   }
   
   public Collection getEntityLocalCollection(Collection ids)
   {
      ArrayList list = new ArrayList( ids.size() );
      Iterator iter = ids.iterator();
      while (iter.hasNext())
      {
         list.add( getEntityEJBLocalObject(iter.next()) );
      }
      return list;
   }
   
   /**
    *  Invoke a Home interface method.
    */
   public Object invokeHome(Method m, Object[] args)
   throws Exception
   {
      // Set the right context classloader
      ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(container.getClassLoader());
      
      try
      {
         return container.invokeHome(new Invocation(null, m, args,
         getTransaction(), getPrincipal(), getCredential()));
      }
      catch (AccessException ae)
      {
         throw new AccessLocalException( ae.getMessage(), ae );
      }
      catch (NoSuchObjectException nsoe)
      {
         throw new NoSuchObjectLocalException( nsoe.getMessage(), nsoe );
      }
      catch (TransactionRequiredException tre)
      {
         throw new TransactionRequiredLocalException( tre.getMessage() );
      }
      catch (TransactionRolledbackException trbe)
      {
         throw new TransactionRolledbackLocalException( trbe.getMessage(), trbe );
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldCl);
      }
   }
   
   /**
    *  Return the principal to use for invocations with this proxy.
    */
   Principal getPrincipal()
   {
      return SecurityAssociation.getPrincipal();
   }
   
   /**
    *  Return the credentials to use for invocations with this proxy.
    */
   Object getCredential()
   {
      return SecurityAssociation.getCredential();
   }
   
   /**
    *  Return the transaction associated with the current thread.
    *  Returns <code>null</code> if the transaction manager was never
    *  set, or if no transaction is associated with the current thread.
    */
   Transaction getTransaction()
      throws javax.transaction.SystemException
   {
      return (transactionManager == null) ? null : transactionManager.getTransaction();
   }

   /**
    *  Invoke a local interface method.
    */
   public Object invoke(Object id, Method m, Object[] args )
      throws Exception
   {
      // Set the right context classloader
      ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(container.getClassLoader());
      
      try
      {
         return container.invoke(new Invocation(id, m, args, getTransaction(),
         getPrincipal(), getCredential()));
      }
      catch (AccessException ae)
      {
         throw new AccessLocalException( ae.getMessage(), ae );
      }
      catch (NoSuchObjectException nsoe)
      {
         throw new NoSuchObjectLocalException( nsoe.getMessage(), nsoe );
      }
      catch (TransactionRequiredException tre)
      {
         throw new TransactionRequiredLocalException( tre.getMessage() );
      }
      catch (TransactionRolledbackException trbe)
      {
         throw new TransactionRolledbackLocalException( trbe.getMessage(), trbe );
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldCl);
      }
   }

   class HomeProxy extends LocalHomeProxy
      implements InvocationHandler
   {
      protected String getJndiName()
      {
         return jndiName;
      }
      
      protected Object getId()
      {
         return jndiName;
      }
      
      public final Object invoke(final Object proxy,
         final Method m,
         Object[] args)
         throws Throwable
      {
         if (args == null)
            args = EMPTY_ARGS;
         
         Object retValue = super.invoke( proxy, m, args );
         if (retValue != null)
            return retValue;
         
         else if (m.equals(REMOVE_BY_PRIMARY_KEY))
         {
            // The trick is simple we trick the container in believe it
            // is a remove() on the instance
            Object id = new CacheKey(args[0]);
            return BaseLocalContainerInvoker.this.invoke(
            id, REMOVE_OBJECT, EMPTY_ARGS);
         }
         // If not taken care of, go on and call the container
         else
         {
            return BaseLocalContainerInvoker.this.invokeHome(
            m, args);
         }
      }
   }
   
   class EntityProxy extends LocalProxy
      implements InvocationHandler
   {
      CacheKey cacheKey;
      
      EntityProxy( Object id )
      {
         if (!(id instanceof CacheKey))
            id = new CacheKey( id );
         cacheKey = (CacheKey) id;
      }

      protected String getJndiName()
      {
         return jndiName;
      }
      
      protected Object getId()
      {
         return cacheKey.getId();
      }
      
      
      public final Object invoke(final Object proxy,
         final Method m,
         Object[] args)
         throws Throwable
      {
         if (args == null)
            args = EMPTY_ARGS;
         
         Object retValue = super.invoke( proxy, m, args );
         if (retValue != null)
            return retValue;
         // If not taken care of, go on and call the container
         else
         {
            return BaseLocalContainerInvoker.this.invoke(
            cacheKey, m, args);
         }
      }
      
   }
   
   class StatefulSessionProxy extends LocalProxy
      implements InvocationHandler
   {
      Object id;
      
      StatefulSessionProxy( Object id )
      {
         this.id = id;
      }
      
      protected String getJndiName()
      {
         return jndiName;
      }
      
      protected Object getId()
      {
         return id;
      }
      
      public final Object invoke(final Object proxy,
         final Method m,
         Object[] args)
         throws Throwable
      {
         if (args == null)
            args = EMPTY_ARGS;
         
         Object retValue = super.invoke( proxy, m, args );
         if (retValue != null)
            return retValue;
         // If not taken care of, go on and call the container
         else
         {
            return BaseLocalContainerInvoker.this.invoke(
            id, m, args);
         }
      }
   }
   
   class StatelessSessionProxy extends LocalProxy
      implements InvocationHandler
   {
      protected String getJndiName()
      {
         return jndiName;
      }
      
      protected Object getId()
      {
         return jndiName;
      }
      
      
      public final Object invoke(final Object proxy,
         final Method m,
         Object[] args)
         throws Throwable
      {
         if (args == null)
            args = EMPTY_ARGS;
         
         // Implement local methods
         if (m.equals(TO_STRING))
         {
            return jndiName + ":Stateless";
         }
         else if (m.equals(EQUALS))
         {
            return invoke(proxy, IS_IDENTICAL, args);
         }
         else if (m.equals(HASH_CODE))
         {
            // We base the stateless hash on the hash of the proxy...
            // MF XXX: it could be that we want to return the hash of the name?
            return new Integer(this.hashCode());
         }
         else if (m.equals(GET_PRIMARY_KEY))
         {
            // MF FIXME
            // The spec says that SSB PrimaryKeys should not be returned and the call should throw an exception
            // However we need to expose the field *somehow* so we can check for "isIdentical"
            // For now we use a non-spec compliant implementation and just return the key as is
            // See jboss1.0 for the PKHolder and the hack to be spec-compliant and yet solve the problem
            
            // This should be the following call
            //throw new RemoteException("Session Beans do not expose their keys, RTFS");
            
            // This is how it can be solved with a PKHolder (extends RemoteException)
            // throw new PKHolder("RTFS", name);
            
            // This is non-spec compliant but will do for now
            // We can consider the name of the container to be the primary key, since all stateless beans
            // are equal within a home
            return jndiName;
         }
         else if (m.equals(GET_EJB_HOME))
         {
            throw new UnsupportedOperationException();
         }
         else if (m.equals(IS_IDENTICAL))
         {
            // All stateless beans are identical within a home,
            // if the names are equal we are equal
            return isIdentical(args[0], jndiName);
         }
         // If not taken care of, go on and call the container
         else
         {
            return BaseLocalContainerInvoker.this.invoke(
            jndiName, m, args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8338.java