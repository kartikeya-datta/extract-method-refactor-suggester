error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2548.java
text:
```scala
public static S@@et<State> reverse(Automaton a) {

/*
 * dk.brics.automaton
 * 
 * Copyright (c) 2001-2009 Anders Moeller
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.apache.lucene.util.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.util.BytesRef;

/**
 * Special automata operations.
 * 
 * @lucene.experimental
 */
final public class SpecialOperations {
  
  private SpecialOperations() {}
  
  /**
   * Finds the largest entry whose value is less than or equal to c, or 0 if
   * there is no such entry.
   */
  static int findIndex(int c, int[] points) {
    int a = 0;
    int b = points.length;
    while (b - a > 1) {
      int d = (a + b) >>> 1;
      if (points[d] > c) b = d;
      else if (points[d] < c) a = d;
      else return d;
    }
    return a;
  }
  
  /**
   * Returns true if the language of this automaton is finite.
   */
  public static boolean isFinite(Automaton a) {
    if (a.isSingleton()) return true;
    return isFinite(a.initial, new HashSet<State>());
  }
  
  /**
   * Checks whether there is a loop containing s. (This is sufficient since
   * there are never transitions to dead states.)
   */
  // TODO: not great that this is recursive... in theory a
  // large automata could exceed java's stack
  private static boolean isFinite(State s, HashSet<State> path) {
    path.add(s);
    for (Transition t : s.getTransitions())
      if (path.contains(t.to) || !isFinite(t.to, path)) return false;
    path.remove(s);
    return true;
  }
  
  /**
   * Returns the longest string that is a prefix of all accepted strings and
   * visits each state at most once.
   * 
   * @return common prefix
   */
  public static String getCommonPrefix(Automaton a) {
    if (a.isSingleton()) return a.singleton;
    StringBuilder b = new StringBuilder();
    HashSet<State> visited = new HashSet<State>();
    State s = a.initial;
    boolean done;
    do {
      done = true;
      visited.add(s);
      if (!s.accept && s.numTransitions() == 1) {
        Transition t = s.getTransitions().iterator().next();
        if (t.min == t.max && !visited.contains(t.to)) {
          b.appendCodePoint(t.min);
          s = t.to;
          done = false;
        }
      }
    } while (!done);
    return b.toString();
  }
  
  // TODO: this currently requites a determinized machine,
  // but it need not -- we can speed it up by walking the
  // NFA instead.  it'd still be fail fast.
  public static BytesRef getCommonPrefixBytesRef(Automaton a) {
    if (a.isSingleton()) return new BytesRef(a.singleton);
    BytesRef ref = new BytesRef(10);
    HashSet<State> visited = new HashSet<State>();
    State s = a.initial;
    boolean done;
    do {
      done = true;
      visited.add(s);
      if (!s.accept && s.numTransitions() == 1) {
        Transition t = s.getTransitions().iterator().next();
        if (t.min == t.max && !visited.contains(t.to)) {
          ref.grow(++ref.length);
          ref.bytes[ref.length - 1] = (byte)t.min;
          s = t.to;
          done = false;
        }
      }
    } while (!done);
    return ref;
  }
  
  /**
   * Returns the longest string that is a suffix of all accepted strings and
   * visits each state at most once.
   * 
   * @return common suffix
   */
  public static String getCommonSuffix(Automaton a) {
    if (a.isSingleton()) // if singleton, the suffix is the string itself.
      return a.singleton;
    
    // reverse the language of the automaton, then reverse its common prefix.
    Automaton r = a.clone();
    reverse(r);
    r.determinize();
    return new StringBuilder(SpecialOperations.getCommonPrefix(r)).reverse().toString();
  }
  
  public static BytesRef getCommonSuffixBytesRef(Automaton a) {
    if (a.isSingleton()) // if singleton, the suffix is the string itself.
      return new BytesRef(a.singleton);
    
    // reverse the language of the automaton, then reverse its common prefix.
    Automaton r = a.clone();
    reverse(r);
    r.determinize();
    BytesRef ref = SpecialOperations.getCommonPrefixBytesRef(r);
    reverseBytes(ref);
    return ref;
  }
  
  private static void reverseBytes(BytesRef ref) {
    if (ref.length <= 1) return;
    int num = ref.length >> 1;
    for (int i = ref.offset; i < ( ref.offset + num ); i++) {
      byte b = ref.bytes[i];
      ref.bytes[i] = ref.bytes[ref.offset * 2 + ref.length - i - 1];
      ref.bytes[ref.offset * 2 + ref.length - i - 1] = b;
    }
  }
  
  /**
   * Reverses the language of the given (non-singleton) automaton while returning
   * the set of new initial states.
   */
  static Set<State> reverse(Automaton a) {
    a.expandSingleton();
    // reverse all edges
    HashMap<State, HashSet<Transition>> m = new HashMap<State, HashSet<Transition>>();
    State[] states = a.getNumberedStates();
    Set<State> accept = new HashSet<State>();
    for (State s : states)
      if (s.isAccept())
        accept.add(s);
    for (State r : states) {
      m.put(r, new HashSet<Transition>());
      r.accept = false;
    }
    for (State r : states)
      for (Transition t : r.getTransitions())
        m.get(t.to).add(new Transition(t.min, t.max, r));
    for (State r : states) {
      Set<Transition> tr = m.get(r);
      r.setTransitions(tr.toArray(new Transition[tr.size()]));
    }
    // make new initial+final states
    a.initial.accept = true;
    a.initial = new State();
    for (State r : accept)
      a.initial.addEpsilon(r); // ensures that all initial states are reachable
    a.deterministic = false;
    a.clearNumberedStates();
    return accept;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2548.java