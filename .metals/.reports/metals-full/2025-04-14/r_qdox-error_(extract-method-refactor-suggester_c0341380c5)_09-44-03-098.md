error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13034.java
text:
```scala
_@@out.println ("</oaw-profile>");

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.internal.xtend.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This class serves as a ThreadLocal collector for profiling information.
 * 
 * @author arno
 *
 */
public class ProfileCollector {
    private ProfileCollector () {}
    private static final ThreadLocal<ProfileCollector> _instance = new ThreadLocal<ProfileCollector>() {
        protected ProfileCollector initialValue() {
            return new ProfileCollector();
        };
    };
    
    private PrintWriter _out;
    private final Stack<Triplet<String, Long, Long>> _contextStack = new Stack<Triplet<String, Long, Long>>();
    private final Map<String, ProfileEntry> _profile = new HashMap<String, ProfileEntry>();
    
    public static ProfileCollector getInstance () {
        return _instance.get();
    }
    
    
    /**
     * This method tells the ProfileCollector to perform detailed logging and store it in the Writer passed
     *  as a parameter. 
     */
    public void setDetailedLoggingWriter (OutputStream out) {
        try {
            _out = new PrintWriter (new OutputStreamWriter (out, "utf-8"));

            _out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            _out.println("<oaw-profile>");
        }
        catch (UnsupportedEncodingException exc) {}
    }

    /**
     * This method must be called by the application to indicate that logging is finished. Once this method
     *  was called, enter() and leave() must not be called.
     */
    public void finish () {
        if (_out != null) {
            _out.println ("</m2t-profile>");
            _out.flush();
            _out = null;
        }
    }
    
    public Collection<ProfileEntry> getProfile () {
        return _profile.values();
    }
    
    public void enter (String context) {
        _contextStack.push (new Triplet<String, Long, Long>(context, System.currentTimeMillis(), 0L));
        if (_out != null)
            _out.println("<call context=\"" + context + "\">");
    }
    
    /**
     * the leave() method does not need to be passed the context because it keeps a stack of all contexts and
     *  pops the topmost.
     *
     */
    public void leave () {
        final Triplet<String, Long, Long> entry = _contextStack.pop();
        final long duration = System.currentTimeMillis() - entry.getSecond();
        final long timeInChildren = entry.getThird();
        
        if (! _contextStack.isEmpty()) {
            // add this duration to the "duration in children" field of the parent
            final Triplet<String, Long, Long> parent = _contextStack.peek();
            parent.setThird(parent.getThird() + duration);
        }
        
        if (_profile.containsKey(entry.getFirst())) {
            _profile.get(entry.getFirst()).registerCall(duration, duration - timeInChildren);
        }
        else {
            _profile.put (entry.getFirst(), new ProfileEntry (entry.getFirst(), duration, duration - timeInChildren));
        }
        
        if (_out != null) {
            _out.println ("<duration millis=\"" + duration + "\"/>");
            _out.println ("</call>");
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder ();

        result.append ("context\tnum\ttotal\tnet\tmin\tnet\tmax\tnet\n");
        
        for (ProfileEntry e: _profile.values()) {
            result.append (e.getContextName() + "\t" + e.getNumCalls() + "\t" + e.getTotalTimeGross() + "\t" + e.getTotalTimeNet() + "\t" + e.getMinTimeGross() + "\t" + e.getMinTimeNet() + "\t" + e.getMaxTimeGross() + "\t" + e.getMaxTimeNet() + "\n");
        }
        
        return result.toString();
    }
    
    public static class ProfileEntry {
        private final String _contextName;
        private int _numCalls = 1;
        private long _totalTimeGross;
        private long _totalTimeNet;
        private long _minTimeGross;
        private long _minTimeNet;
        private long _maxTimeGross;
        private long _maxTimeNet;

        public ProfileEntry (String contextName, long grossDuration, long netDuration) {
            _contextName = contextName;

            _totalTimeGross += grossDuration;
            _totalTimeNet += netDuration;

            _minTimeGross = grossDuration;
            _maxTimeGross = grossDuration;
            _minTimeNet = netDuration;
            _maxTimeNet = netDuration;
        }

        public void registerCall (long grossDuration, long netDuration) {
            _numCalls++;
            
            _totalTimeGross += grossDuration;
            _totalTimeNet += netDuration;
            
            if (grossDuration < _minTimeGross)
                _minTimeGross = grossDuration;
            if (grossDuration > _maxTimeGross)
                _maxTimeGross = grossDuration;
            if (netDuration < _minTimeNet)
                _minTimeNet = netDuration;
            if (netDuration > _maxTimeNet)
                _maxTimeNet = netDuration;
        }

        public String getContextName() {
            return _contextName;
        }

        public long getMaxTimeGross() {
            return _maxTimeGross;
        }

        public long getMaxTimeNet() {
            return _maxTimeNet;
        }

        public long getMinTimeGross() {
            return _minTimeGross;
        }

        public long getMinTimeNet() {
            return _minTimeNet;
        }

        public int getNumCalls() {
            return _numCalls;
        }

        public long getTotalTimeGross() {
            return _totalTimeGross;
        }

        public long getTotalTimeNet() {
            return _totalTimeNet;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13034.java