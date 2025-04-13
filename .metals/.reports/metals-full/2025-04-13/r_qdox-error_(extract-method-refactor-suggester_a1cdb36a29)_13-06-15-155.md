error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1425.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1425.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1425.java
text:
```scala
@version $@@Revision: 1.7 $

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

package org.jboss.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/** An implementation of a timed cache. This is a cache whose entries have a
    limited lifetime with the ability to refresh their lifetime. The entries
    managed by the cache implement the TimedCachePolicy.TimedEntry interface. If
    an object inserted into the cache does not implement this interface, it will
    be wrapped in a DefaultTimedEntry and will expire without the possibility of
    refresh after getDefaultLifetime() seconds.

    This is a lazy cache policy in that objects are not checked for expiration
    until they are accessed.

    @author <a href="mailto:Scott_Stark@displayscape.com">Scott Stark</a>.
    @version $Revision: 1.6 $
*/
public class TimedCachePolicy
   extends TimerTask
   implements CachePolicy
{
   /** The interface that cache entries support.
    */
   public static interface TimedEntry
   {
      /** Initializes an entry with the current cache time. This is called when
          the entry is first inserted into the cache so that entries do not
          have to know the absolute system time.
      */
      void init(long now);
      
      /** Is the entry still valid basis the current time
          @return true if the entry is within its lifetime, false if it is expired.
      */
      boolean isCurrent(long now);
      
      /** Attempt to extend the entry lifetime by refreshing it.
          @return true if the entry was refreshed successfully, false otherwise.
      */
      boolean refresh();
      
      /** Get the value component of the TimedEntry. This may or may not
          be the TimedEntry implementation.
      */
      Object getValue();
   }

   protected static Timer resolutionTimer = new Timer(true);

   /** The map of cached TimedEntry objects. */
   protected Map entryMap;
   /** The lifetime in seconds to use for objects inserted
       that do not implement the TimedEntry interface. */
   protected int defaultLifetime;
   /** A flag indicating if entryMap should be synchronized */
   protected boolean threadSafe;
   /** The caches notion of the current time */
   protected long now;
   /** The resolution in seconds of the cach current time */
   protected int resolution;

   /** Creates a new TimedCachePolicy with a default entry lifetime of 30 mins
       that does not synchronized access to its policy store and uses a 60
       second resolution.
   */
   public TimedCachePolicy() 
   {
      this(30*60, false, 0);
   }
   /** Creates a new TimedCachePolicy with the given default entry lifetime
       that does not synchronized access to its policy store and uses a 60
       second resolution.
   */
   public TimedCachePolicy(int defaultLifetime)
   {
      this(defaultLifetime, false, 0);
   }
   /** Creates a new TimedCachePolicy with the given default entry lifetime
       that does/does not synchronized access to its policy store depending
       on the value of threadSafe.
       @param defaultLifetime, the lifetime in seconds to use for objects inserted
       that do not implement the TimedEntry interface.
       @param threadSafe, a flag indicating if the cach store should be synchronized
       to allow correct operation under multi-threaded access. If true, the
       cache store is synchronized. If false the cache store is unsynchronized and
       the cache is not thread safe.
       @param resolution, the resolution in seconds of the cache timer. A cache does
       not query the system time on every get() invocation. Rather the cache
       updates its notion of the current time every 'resolution' seconds.
   */
   public TimedCachePolicy(int defaultLifetime, boolean threadSafe, int resolution)
   {
      this.defaultLifetime = defaultLifetime;
      this.threadSafe = threadSafe;
      if( resolution <= 0 )
         resolution = 60;
      this.resolution = resolution;
   }

   // Service implementation ----------------------------------------------
   /** Initializes the cache for use. Prior to this the cache has no store.
    */
   public void create()
   {
      if( threadSafe )
         entryMap = new HashMap();
      else
         entryMap = Collections.synchronizedMap(new HashMap());
      now = System.currentTimeMillis();
   }
   /** Schedules this with the class resolutionTimer Timer object for
       execution every resolution seconds.
   */
   public void start()
   {
      resolutionTimer.scheduleAtFixedRate(this, 0, 1000*resolution);
   }
   /** Stop cancels the resolution timer and flush()es the cache.
    */
   public void stop() 
   {
      super.cancel();
      flush();
   }
   /** Clears the cache of all entries.
    */
   public void destroy() 
   {
      entryMap.clear();
   }

   // --- Begin CachePolicy interface methods
   /** Get the cache value for key if it has not expired.
       @returns the TimedEntry value or the original value if it was not an
       instancee of TimedEntry if key is in the cache, null otherwise.
   */
   public Object get(Object key) 
   {
      TimedEntry entry = (TimedEntry) entryMap.get(key);
      if( entry == null )
         return null;

      if( entry.isCurrent(now) == false )
      {   // Try to refresh the entry
         if( entry.refresh() == false )
         {   // Failed, remove the entry and return null
            entryMap.remove(key);
            return null;
         }
      }
      Object value = entry.getValue();
      return value;
   }
   /** Get the cache value for key. This method does not check to see if
       the entry has expired.
       @returns the TimedEntry value or the original value if it was not an
       instancee of TimedEntry if key is in the cache, null otherwise.
   */
   public Object peek(Object key) 
   {
      TimedEntry entry = (TimedEntry) entryMap.get(key);
      Object value = null;
      if( entry != null )
         value = entry.getValue();
      return value;
   }
   /** Insert a value into the cache. In order to have the cache entry
       reshresh itself value would have to implement TimedEntry and
       implement the required refresh() method logic.
       @param key, the key for the cache entry
       @param value, Either an instance of TimedEntry that will be inserted without
       change, or an abitrary value that will be wrapped in a non-refreshing
       TimedEntry.
   */
   public void insert(Object key, Object value) 
   {
      if( entryMap.containsKey(key) )
         throw new IllegalStateException("Attempt to insert duplicate entry");
      TimedEntry entry = null;
      if( (value instanceof TimedEntry) == false )
      {   // Wrap the value in a DefaultTimedEntry
         entry = new DefaultTimedEntry(defaultLifetime, value);
      }
      else
      {
         entry = (TimedEntry) value;
      }
      entry.init(now);
      entryMap.put(key, entry);
   }
   /** Remove the entry associated with key.
    */
   public void remove(Object key) 
   {
      entryMap.remove(key);
   }
   /** Remove all entries from the cache.
    */
   public void flush() 
   {
      entryMap.clear();
   }

   public int size() {
      return entryMap.size();
   }
   // --- End CachePolicy interface methods

   /** The TimerTask run method. It updates the cache time to the
       current system time.
   */
   public void run()
   {
      now = System.currentTimeMillis();
   }

   /** Get the cache time.
       @return the cache time last obtained from System.currentTimeMillis()
   */
   public long currentTimeMillis()
   {
      return now;
   }

   /** Get the raw TimedEntry for key without performing any expiration check.
       @return the TimedEntry value associated with key if one exists, null otherwise.
   */
   public TimedEntry peekEntry(Object key) 
   {
      TimedEntry entry = (TimedEntry) entryMap.get(key);
      return entry;
   }

   /** The default implementation of TimedEntry used to wrap non-TimedEntry
       objects inserted into the cache.
   */
   static class DefaultTimedEntry implements TimedEntry
   {
      long expirationTime;
      Object value;

      DefaultTimedEntry(long lifetime, Object value)
      {
         this.expirationTime = 1000 * lifetime;
         this.value = value;
      }
      public void init(long now)
      {
         expirationTime += now;
      }
      public boolean isCurrent(long now)
      {
         return expirationTime > now;
      }
      public boolean refresh()
      {
         return false;
      }
      public Object getValue()
      {
         return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1425.java