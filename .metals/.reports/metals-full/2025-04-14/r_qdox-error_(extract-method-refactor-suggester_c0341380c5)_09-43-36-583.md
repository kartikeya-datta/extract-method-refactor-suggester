error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3699.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3699.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3699.java
text:
```scala
i@@f( "J2EEApplication".equals( lType ) ) {

package org.jboss.management.j2ee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
* Is the access point to management information for a single J2EE Server
* Core implementation representing a logical core server of one instance
* of a J2EE platform product.
*
* @author <a href="mailto:andreas@jboss.org">Andreas Schaefer</a>
**/
public class J2EEServer
  extends J2EEManagedObject
  implements J2EEServerMBean
{
   // -------------------------------------------------------------------------
   // Members
   // -------------------------------------------------------------------------
   
   private List mApplications = new ArrayList();
   
   private List mResources = new ArrayList();
   
   private List mNodes = new ArrayList();
   
   private List mJVMs = new ArrayList();
   
   private String mJ2eeVendor = null;
   
   // -------------------------------------------------------------------------
   // Constructors
   // -------------------------------------------------------------------------
   
   public J2EEServer( String pName, ObjectName pDomain, String pJ2eeVendor )
      throws
         MalformedObjectNameException,
         InvalidParentException
   {
      super( "J2EEServer", pName, pDomain );
      mJ2eeVendor = pJ2eeVendor;
   }
   
   public J2EEServer(
      String pName,
      ObjectName pDomain,
      ObjectName[] pApplications,
      ObjectName[] pResources,
      ObjectName[] pNodes,
      ObjectName[] pJVMs,
      String pJ2eeVendor
   )
      throws
         MalformedObjectNameException,
         InvalidParentException
   {
      super( "J2EEServer", pName, pDomain );
      mApplications.addAll( Arrays.asList( pApplications ) );
      mResources.addAll( Arrays.asList( pResources ) );
      mNodes.addAll( Arrays.asList( pNodes ) );
      mJVMs.addAll( Arrays.asList( pJVMs ) );
      mJ2eeVendor = pJ2eeVendor;
   }
   
   // -------------------------------------------------------------------------
   // Properties (Getters/Setters)
   // -------------------------------------------------------------------------  

   public ObjectName[] getApplications() {
      return (ObjectName[]) mApplications.toArray( new ObjectName[ 0 ] );
   }

   public ObjectName getApplication( int pIndex ) {
      if( pIndex >= 0 && pIndex < mApplications.size() ) {
         return (ObjectName) mApplications.get( pIndex );
      }
      return null;
   }

   public ObjectName[] getNodes() {
      return (ObjectName[]) mNodes.toArray( new ObjectName[ 0 ] );
   }

   public ObjectName getNode( int pIndex ) {
      if( pIndex >= 0 && pIndex < mNodes.size() ) {
         return (ObjectName) mNodes.get( pIndex );
      }
      return null;
   }

   public ObjectName[] getResources() {
      return (ObjectName[]) mResources.toArray( new ObjectName[ 0 ] );
   }

   public ObjectName getResource( int pIndex ) {
      if( pIndex >= 0 && pIndex < mResources.size() ) {
         return (ObjectName) mResources.get( pIndex );
      }
      return null;
   }

   public ObjectName[] getJavaVMs() {
      return (ObjectName[]) mJVMs.toArray( new ObjectName[ 0 ] );
   }

   public ObjectName getJavaVM( int pIndex ) {
      if( pIndex >= 0 && pIndex < mJVMs.size() ) {
         return (ObjectName) mJVMs.get( pIndex );
      }
      return null;
   }

   public String getJ2eeVendor() {
      return mJ2eeVendor;
   }
   
   public void addChild( ObjectName pChild ) {
      Hashtable lProperties = pChild.getKeyPropertyList();
      String lType = lProperties.get( "type" ) + "";
      if( "Application".equals( lType ) ) {
         mApplications.add( pChild );
      } else if( "Node".equals( lType ) ) {
         mNodes.add( pChild );
      } else if( "JVM".equals( lType ) ) {
         mJVMs.add( pChild );
      } else if( "Resource".equals( lType ) ) {
         mResources.add( pChild );
      }
   }
   
   public void removeChild( ObjectName pChild ) {
      //AS ToDo
   }

   public String toString() {
      return "J2EEServer { " + super.toString() + " } [ " +
         "applications: " + mApplications +
         ", resources: " + mResources +
         ", nodes: " + mNodes +
         ", JVMs: " + mJVMs +
         ", J2EE vendor: " + mJ2eeVendor +
         " ]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3699.java