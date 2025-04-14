error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7101.java
text:
```scala
i@@nt len = (o==null)?0:o.length;

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public final class Iterators {

    /** 
     * Private constructor, nobody should ever make one of these
     */
    private Iterators() {
        super();
    }

    /**
     * A getter represents a mapping function from Object to Iterator
     */
    public interface Getter {
        Iterator get(Object target);
    }

    /**
     * A filter represents a mapping function from Iterator to Iterator
     */
    public interface Filter {
        Iterator filter(Iterator in);
    }

    /**
     * Create a new filter F that, when wrapped around another iterator I, 
     * creates a new iterator I' that will return only those values of I
     * that have not yet been returned by I', discarding duplicates.
     */
    public static Filter dupFilter() {
        return new Filter() {
            final Set seen = new HashSet();  // should have weak ptrs?
            public Iterator filter(final Iterator in) {
                return new Iterator() {
                    boolean fresh = false;
                    Object peek;
                    public boolean hasNext() {
                        if (fresh) return true;
                        while (true) {
                            if (! in.hasNext()) return false;
                            peek = in.next();
                            if (! seen.contains(peek)) {
                                return fresh = true;
                            } else {
                                peek = null;  // garbage collection
                            }
                        }
                    }
                    public Object next() {
                        if (! hasNext()) throw new NoSuchElementException();
                        Object ret = peek;
                        peek = null;
                        fresh = false;
                        return ret;
                    }
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };                
            }
        };
    }

    /** 
     *  Creates an iterator that will return the elements of a specified array,
     * in order.  Like Arrays.asList(o).iterator(), without all that pesky safety.
     */

    public static Iterator array(final Object[] o) {
        return new Iterator() {
            int i = 0;
            int len = o.length;
            public boolean hasNext() {
                return i < len;
            }
            public Object next() {
                if (i < len) {
                    return o[i++];
                } else {
                    throw new NoSuchElementException();
                }
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    } 

    /** creates an iterator I based on a base iterator A and a getter G.  
     * I returns, in order, forall (i in I), G(i).
     */
    public static Iterator mapOver(final Iterator a, final Getter g) {
        return new Iterator() {
            Iterator delegate = 
                new Iterator() {
                    public boolean hasNext() { 
                        if (! a.hasNext()) return false;
                        Object o = a.next();
                        delegate = append1(g.get(o), this);
                        return delegate.hasNext();
                    }
                    public Object next() { 
                        if (! hasNext()) throw new UnsupportedOperationException();
                        return delegate.next();
                    }
                    public void remove() { throw new UnsupportedOperationException(); }            
                };                    
            public boolean hasNext() { 
                return delegate.hasNext();
            }
            public Object next() { 
                return delegate.next();
            }
            public void remove() { throw new UnsupportedOperationException(); }            
        };
    }

    /** creates an iterator I based on a base iterator A and a getter G.
     * I returns, in order, forall (i in I) i :: forall (i' in g(i)) recur(i', g)
     */
    public static Iterator recur(final Object a, final Getter g) {
        return new Iterator() {
            Iterator delegate = one(a);
            public boolean hasNext() {
                return delegate.hasNext();
            }
            public Object next() {
                Object next = delegate.next();
                delegate = append(g.get(next), delegate);
                return next;
            }
            public void remove() { throw new UnsupportedOperationException(); }            
        };        
    }
    /** creates an iterator I based on base iterators A and B.  Returns
     * the elements returned by A followed by those returned by B.  If
     * B is empty, simply returns A, and if A is empty, simply returns B.
     * Do NOT USE if b.hasNext() is not idempotent.  
     */
    public static Iterator append(final Iterator a, final Iterator b) {
        if (! b.hasNext()) return a;
        return append1(a, b);
    }
    /** creates an iterator I based on base iterators A and B.  Returns
     * the elements returned by A followed by those returned by B.  If A
     * is empty, simply returns B.  Guaranteed not to call B.hasNext() until
     * A is empty. 
     */
    public static Iterator append1(final Iterator a, final Iterator b) {
        if (! a.hasNext()) return b;
        return new Iterator() {
            public boolean hasNext() { return a.hasNext() || b.hasNext(); }
            public Object next() {
                if (a.hasNext()) return a.next();
                if (b.hasNext()) return b.next();
                throw new NoSuchElementException();
            }
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }
    /** creates an iterator I based on a base iterator A and an object O.  Returns
     * the elements returned by A, followed by O. 
     */
    public static Iterator snoc(final Iterator first, final Object last) {
        return new Iterator() {
            Object last1 = last;
            public boolean hasNext() { return first.hasNext() || last1 != null; }
            public Object next() { 
                if (first.hasNext()) return first.next();
                else if (last1 == null) throw new NoSuchElementException();
                Object ret = last1;
                last1 = null;
                return ret;
            }
            public void remove() { throw new UnsupportedOperationException(); }            
        };
    }
    /** creates an iterator I based on an object O.  Returns O, once. 
    */
    public static Iterator one(final Object it) {
        return new Iterator() {
            boolean avail = true;
            public boolean hasNext() { return avail; }
            public Object next() {
                if (! avail) throw new NoSuchElementException();
                avail = false;
                return it;
            }
            public void remove() { throw new UnsupportedOperationException(); }            
        };
    }            
    /** creates an empty iterator.
    */
    public static final Iterator EMPTY = new Iterator() {
        public boolean hasNext() { return false; }
        public Object next() { throw new NoSuchElementException(); }
        public void remove() { throw new UnsupportedOperationException(); }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7101.java