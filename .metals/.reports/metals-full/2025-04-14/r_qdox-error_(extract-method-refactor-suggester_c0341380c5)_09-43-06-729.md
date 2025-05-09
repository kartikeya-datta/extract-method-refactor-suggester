error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,13]

error in qdox parser
file content:
```java
offset: 13
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3737.java
text:
```scala
{@link #push}@@ should call this method before exiting. Otherwise,

//      Copyright 1996-1999, International Business Machines 
//      Corporation. All Rights Reserved.
//
//      See the LICENCE file for the terms of usage and distribution.

package org.apache.log4j;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.helpers.LogLog;

/**
   The NDC class implements <i>nested diagnostic contexts</i> as
   defined by Neil Harrison in the article "Patterns for Logging
   Diagnostic Messages" part of the book "<i>Pattern Languages of
   Program Design 3</i>" edited by Martin et al.

   <p>A Nested Diagnostic Context, or NDC in short, is an instrument
   to distinguish interleaved log output from different sources. Log
   output is typically interleaved when a server handles multiple
   clients near-simultaneously.

   <p>Interleaved log output can still be meaningful if each log entry
   from different contexts had a distinctive stamp. This is where NDCs
   come into play.

   <p><em><b>Note that NDCs are managed on a per thread
   basis</b></em>. NDC operations such as {@link #push push}, {@link
   #pop}, {@link #clear}, {@link #getDepth} and {@link #setMaxDepth}
   affect the NDC of the <em>current</em> thread only. NDCs of other
   threads remain unaffected.

   <p>For example, a servlet can build a per client request NDC
   consisting the clients host name and other information contained in
   the the request. <em>Cookies</em> are another source of distinctive
   information. To build an NDC one uses the {@link #push push}
   operation. Simply put,

   <p><ul>
     <li>Contexts can be nested.

     <p><li>When entering a context, call <code>NDC.push</code>. As a
     side effect, if there is no nested diagnostic context for the
     current thread, this method will create it.

     <p><li>When leaving a context, call <code>NDC.pop</code>.

     <p><li><b>When exiting a thread make sure to call {@link #remove
     NDC.remove()}</b>.  
   </ul>
   
   <p>There is no penalty for forgetting to match each
   <code>push</code> operation with a corresponding <code>pop</code>,
   except the obvious mismatch between the real application context
   and the context set in the NDC.

   <p>If configured to do so, {@link PatternLayout} and {@link
   TTCCLayout} instances automatically retrieve the nested diagnostic
   context for the current thread without any user intervention.
   Hence, even if a servlet is serving multiple clients
   simultaneously, the logs emanating from the same code (belonging to
   the same category) can still be distinguished because each client
   request will have a different NDC tag.

   <p>Heavy duty systems should call the {@link #remove} method when
   leaving the run method of a thread. This ensures that the memory
   used by the thread can be freed by the Java garbage
   collector. There is a mechanism to lazily remove references to dead
   threads. In practice, this means that you can be a little sloppy
   and sometimes forget to call {@link #remove} before exiting a
   thread.
   
   <p>A thread may inherit the nested diagnostic context of another
   (possibly parent) thread using the {@link #inherit inherit}
   method. A thread may obtain a copy of its NDC with the {@link
   #cloneStack cloneStack} method and pass the reference to any other
   thread, in particular to a child.
   
   @author Ceki G&uuml;lc&uuml;
   @since log4j v0.7.0
  
*/
 
public class NDC {

  // The synchronized keyword is not used in this class. This may seem
  // dangerous, especially since the class will be used by
  // multiple-threads. In particular, all threads share the same
  // hashtable (the "ht" variable). This is OK since java hashtables
  // are thread safe. Same goes for Stacks.

  // More importantly, when inheriting diagnostic contexts the child
  // thread is handed a clone of the parent's NDC.  It follows that
  // each thread has its own NDC (i.e. stack).

  static Hashtable ht = new Hashtable();

  static int pushCounter = 0; // the number of times push has been called
                              // after the latest call to lazyRemove

  // The number of times we allow push to be called before we call lazyRemove
  // 5 is a relatively small number. As such, lazyRemove is not called too
  // frequently. We thus avoid the cost of creating an Enumeration too often.
  // The higher this number, the longer is the avarage period for which all
  // logging calls in all threads are blocked.
  static final int REAP_THRESHOLD = 5;
  
  // No instances allowed.
  private NDC() {}


  /**
     Clear any nested diagnostic information if any. This method is
     useful in cases where the same thread can be potentially used
     over and over in different unrelated contexts.

     <p>This method is equivalent to calling the {@link #setMaxDepth}
     method with a zero <code>maxDepth</code> argument.
     
     @since 0.8.4c */
  public
  static
  void clear() {
    Stack stack = (Stack) ht.get(Thread.currentThread());    
    if(stack != null) 
      stack.setSize(0);    
  }

  
  /**
     Clone the diagnostic context for the current thread.

     <p>Internally a diagnostic context is represented as a stack.  A
     given thread can supply the stack (i.e. diagnostic context) to a
     child thread so that the child can inherit the parent thread's
     diagnostic context.

     <p>The child thread uses the {@link #inherit inherit} method to
     inherit the parent's diagnostic context.
     
     @return Stack A clone of the current thread's  diagnostic context.

  */
  public
  static
  Stack cloneStack() {
    Object o = ht.get(Thread.currentThread());
    if(o == null)
      return null;
    else {
      Stack stack = (Stack) o;
      return (Stack) stack.clone();
    }
  }

  
  /**
     Inherit the diagnostic context of another thread.

     <p>The parent thread can obtain a reference to its diagnostic
     context using the {@link #cloneStack} method.  It should
     communicate this information to its child so that it may inherit
     the parent's diagnostic context.

     <p>The parent's diagnostic context is cloned before being
     inherited. In other words, once inherited, the two diagnostic
     contexts can be managed independently.
     
     <p>In java, a child thread cannot obtain a reference to its
     parent, unless it is directly handed the reference. Consequently,
     there is no client-transparent way of inheriting diagnostic
     contexts. Do you know any solution to this problem?

     @param stack The diagnostic context of the parent thread.

  */
  public
  static
  void inherit(Stack stack) {
    if(stack != null)
      ht.put(Thread.currentThread(), stack);
  }


  /**
     Used when printing the diagnostic context.
  */
  static
  public
  String get() {
    Stack s = (Stack) ht.get(Thread.currentThread());
    if(s != null && !s.isEmpty()) 
      return ((DiagnosticContext) s.peek()).fullMessage;
    else
      return null;
  }
  
  /**
     Get the current nesting depth of this diagnostic context.

     @see #setMaxDepth
     @since 0.7.5
   */
  public
  static
  int getDepth() {
    Stack stack = (Stack) ht.get(Thread.currentThread());          
    if(stack == null)
      return 0;
    else
      return stack.size();      
  }

  private
  static
  void lazyRemove() {
     
    // The synchronization on ht is necessary to prevent JDK 1.2.x from
    // throwing ConcurrentModificationExceptions at us. This sucks BIG-TIME.
    // One solution is to write our own hashtable implementation.
    Vector v;
    
    synchronized(ht) {
      // Avoid calling clean-up too often.
      if(++pushCounter >= REAP_THRESHOLD) {
	return; // We release the lock ASAP.
      } else {
	pushCounter = 0; // OK let's do some work.
      }

      int misses = 0;
      v = new Vector(); 
      Enumeration enum = ht.keys();
      // We give up after 4 straigt missses. That is 4 consecutive
      // inspected threads in 'ht' that turn out to be alive.
      // The higher the proportion on dead threads in ht, the higher the
      // chances of removal.
      while(enum.hasMoreElements() && (misses <= 4)) {
	Thread t = (Thread) enum.nextElement();
	if(t.isAlive()) {
	  misses++;
	} else {
	  misses = 0;
	  v.addElement(t);
	}
      }
    } // synchronized

    int size = v.size();
    for(int i = 0; i < size; i++) {
      Thread t = (Thread) v.elementAt(i);
      LogLog.debug("Lazy NDC removal for thread [" + t.getName() + "] ("+ 
		   ht.size() + ").");
      ht.remove(t);
    }
  }

  /**
     Clients should call this method before leaving a diagnostic
     context.

     <p>The returned value is the value that was pushed last. If no
     context is available, then the empty string "" is returned.
     
     @return String The innermost diagnostic context.
     
     */
  public
  static
  String pop() {
    Thread key = Thread.currentThread();
    Stack stack = (Stack) ht.get(key);
    if(stack != null && !stack.isEmpty()) 
      return ((DiagnosticContext) stack.pop()).message;
    else
      return "";
  }

  /**
     Looks at the last diagnostic context at the top of this NDC
     without removing it.

     <p>The returned value is the value that was pushed last. If no
     context is available, then the empty string "" is returned.
     
     @return String The innermost diagnostic context.
     
     */
  public
  static
  String peek() {
    Thread key = Thread.currentThread();
    Stack stack = (Stack) ht.get(key);
    if(stack != null && !stack.isEmpty())
      return ((DiagnosticContext) stack.peek()).message;
    else
      return "";
  }
  
  /**
     Push new diagnostic context information for the current thread.

     <p>The contents of the <code>message</code> parameter is
     determined solely by the client.  
     
     @param message The new diagnostic context information.  */
  public
  static
  void push(String message) {
    Thread key = Thread.currentThread();
    Stack stack = (Stack) ht.get(key);
      
    if(stack == null) {
      DiagnosticContext dc = new DiagnosticContext(message, null);      
      stack = new Stack();
      ht.put(key, stack);
      stack.push(dc);
    } else if (stack.isEmpty()) {
      DiagnosticContext dc = new DiagnosticContext(message, null);            
      stack.push(dc);
    } else {
      DiagnosticContext parent = (DiagnosticContext) stack.peek();
      stack.push(new DiagnosticContext(message, parent));
    }    
  }

  /**
     Remove the diagnostic context for this thread.

     <p>Each thread that created a diagnostic context by calling
     {@link #psuh} should call this method before exiting. Otherwise,
     the memory used by the <b>thread</b> cannot be reclaimed by the
     VM.

     <p>As this is such an important problem in heavy duty systems and
     because it is difficult to always guarantee that the remove
     method is called before exiting a thread, this method has been
     augmented to lazily remove references to dead threads. In
     practice, this means that you can be a little sloppy and
     occasionally forget to call {@link #remove} before exiting a
     thread. However, you must call <code>remove</code> sometime. If
     you never call it, then your application is sure to run out of
     memory.
     
  */
  static
  public
  void remove() {
    ht.remove(Thread.currentThread());
    
    // Lazily remove dead-thread references in ht.
    lazyRemove();    
  }

  /**
     Set maximum depth of this diagnostic context. If the current
     depth is smaller or equal to <code>maxDepth</code>, then no
     action is taken.

     <p>This method is a convenient alternative to multiple {@link
     #pop} calls. Moreover, it is often the case that at the end of
     complex call sequences, the depth of the NDC is
     unpredictable. The <code>setMaxDepth</code> method circumvents
     this problem.

     <p>For example, the combination
     <pre>
       void foo() {
       &nbsp;  int depth = NDC.getDepth();

       &nbsp;  ... complex sequence of calls

       &nbsp;  NDC.setMaxDepth(depth);
       }
     </pre>

     ensures that between the entry and exit of foo the depth of the
     diagnostic stack is conserved.
     
     @see #getDepth
     @since 0.7.5 */
  static
  public
  void setMaxDepth(int maxDepth) {
    Stack stack = (Stack) ht.get(Thread.currentThread());    
    if(stack != null && maxDepth < stack.size()) 
      stack.setSize(maxDepth);
  }
  
  // =====================================================================
   private static class DiagnosticContext {

    String fullMessage;
    String message;
    
    DiagnosticContext(String message, DiagnosticContext parent) {
      this.message = message;
      if(parent != null) {
	fullMessage = parent.fullMessage + ' ' + message;
      } else {
	fullMessage = message;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3737.java