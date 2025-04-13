error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2731.java
text:
```scala
H@@ashtable lProperties = (Hashtable) pParent.getKeyPropertyList().clone();

/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.management.j2ee;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Hashtable;

import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import javax.management.j2ee.EventProvider;
import javax.management.j2ee.StateManageable;
import javax.management.j2ee.StatisticsProvider;

import org.jboss.system.ServiceMBeanSupport;

/**
* JBoss specific implementation.
*
* @author Marc Fleury
**/
public abstract class J2EEManagedObject
   extends ServiceMBeanSupport
   implements javax.management.j2ee.J2EEManagedObject, Serializable
{
   // -------------------------------------------------------------------------
   // Static
   // -------------------------------------------------------------------------  
   
   private static String sDomainName = null;
//   protected static MBeanServer sServer = null;
   
   public static String getDomainName() {
      return sDomainName;
   }
   
/*   
   public static MBeanServer getServer() {
      return sServer;
   }
*/
   
   // -------------------------------------------------------------------------
   // Members
   // -------------------------------------------------------------------------  

   private ObjectName mParent = null;
   private ObjectName mName = null;

   // -------------------------------------------------------------------------
   // Constructors
   // -------------------------------------------------------------------------

   /**
   * Constructor for the root J2EEManagement object
   *
   * @param pDomainName Name of the domain
   * @param pType Type of the Managed Object which must be defined
   * @param pName Name of the Managed Object which must be defined
   *
   * @throws InvalidParameterException If the given Domain Name, Type or Name is null
   **/
   public J2EEManagedObject( String pDomainName, String pType, String pName )
      throws
         MalformedObjectNameException
   {
      if( pDomainName == null ) {
         throw new InvalidParameterException( "Domain Name must be set" );
      }
      sDomainName = pDomainName;
      Hashtable lProperties = new Hashtable();
      lProperties.put( "type", pType );
      lProperties.put( "name", pName );
      mName = new ObjectName( getDomainName(), lProperties );
      System.out.println( "J2EEManagedObject(), create root with name: " + mName );
   }
   
   /**
   * Constructor for any Managed Object except the root J2EEMangement.
   *
   * @param pType Type of the Managed Object which must be defined
   * @param pName Name of the Managed Object which must be defined
   * @param pParent Object Name of the parent of this Managed Object
   *                which must be defined
   *
   * @throws InvalidParameterException If the given Type, Name or Parent is null
   **/
   public J2EEManagedObject( String pType, String pName, ObjectName pParent )
      throws
         MalformedObjectNameException,
         InvalidParentException
   {
      try {
      Hashtable lProperties = pParent.getKeyPropertyList();
      System.out.println( "J2EEManagedObject(), parent properties: " + lProperties );
      System.out.println( "J2EEManagedObject(), parent type: " + lProperties.get( "type" ) );
      System.out.println( "J2EEManagedObject(), parent name: " + lProperties.get( "name" ) );
      lProperties.put( lProperties.get( "type" ), lProperties.get( "name" ) );
      lProperties.put( "type", pType );
      lProperties.put( "name", pName );
      mName = new ObjectName( getDomainName(), lProperties );
      System.out.println( "J2EEManagedObject(), properties: " + lProperties );
      setParent( pParent );
      }
      catch( Exception e ) {
         e.printStackTrace();
      }
   }

   // -------------------------------------------------------------------------
   // Properties (Getters/Setters)
   // -------------------------------------------------------------------------  

   public String getName() {
      return mName.toString();
   }
   
   public ObjectName getObjectName() {
      System.out.println( "J2EEManagedObject.getObjectName(), name: " + mName );
      return mName;
   }

   public ObjectName getObjectName( MBeanServer pServer, ObjectName pName ) {
      return getObjectName();
   }

   public boolean isStateManageable() {
      return this instanceof StateManageable;
   }

   public boolean isStatisticsProvider() {
      return this instanceof StatisticsProvider;
   }
   
   public boolean isEventProvider() {
      return this instanceof EventProvider;
   }
   
   public String toString() {
      return "J2EEManagedObject [ name: " + mName + ", parent: " + mParent + " ];";
   }
   
   public ObjectName getParent() {
      return mParent;
   }
   
   public void setParent( ObjectName pParent )
      throws
         InvalidParentException
   {
      if( pParent == null ) {
         throw new InvalidParameterException( "Parent must be set" );
      }
      mParent = pParent;
   }
   
   public void postRegister( java.lang.Boolean pRegistrationDone ) {
      System.out.println( "J2EEManagedObject.postRegister(), parent: " + mParent );
      if( pRegistrationDone.booleanValue() && mParent != null ) {
         try {
            // Notify the parent about its new child
            getServer().invoke(
               mParent,
               "addChild",
               new Object[] { mName },
               new String [] { ObjectName.class.getName() }
            );
            super.postRegister( pRegistrationDone );
         }
         catch( JMException jme ) {
            jme.printStackTrace();
            // Stop it because of the error
            super.postRegister( new Boolean( false ) );
         }
      }
   }
   
   public void addChild( ObjectName pChild ) {
      //AS ToDo: Remove later is just here to compile
   }
   public void removeChild( ObjectName pChild ) {
      //AS ToDo: Remove later is just here to compile
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2731.java