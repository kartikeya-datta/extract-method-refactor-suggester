error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5777.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5777.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5777.java
text:
```scala
w@@hile (lastValidContext > 0 && !fValidContext[lastValidContext]) {

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.xerces.xinclude;

import org.apache.xerces.xni.NamespaceContext;

/**
 * This is an implementation of NamespaceContext which is intended to be used for
 * XInclude processing.  It enables each context to be marked as invalid, if necessary,
 * to indicate that the namespaces recorded on those contexts won't be apparent in the
 * resulting infoset.
 * 
 * @author Peter McCracken, IBM
 * 
 * @version $Id$
 */
public class XIncludeNamespaceSupport extends MultipleScopeNamespaceSupport {

    /**
     * This stores whether or not the context at the matching depth was valid.
     */
    private boolean[] fValidContext = new boolean[8];

    /**
     * 
     */
    public XIncludeNamespaceSupport() {
        super();
    }

    /**
     * @param context
     */
    public XIncludeNamespaceSupport(NamespaceContext context) {
        super(context);
    }

    /**
     * Pushes a new context onto the stack.
     */
    public void pushContext() {
        super.pushContext();
        if (fCurrentContext + 1 == fValidContext.length) {
            boolean[] contextarray = new boolean[fValidContext.length * 2];
            System.arraycopy(fValidContext, 0, contextarray, 0, fValidContext.length);
            fValidContext = contextarray;
        }

        fValidContext[fCurrentContext] = true;
    }

    /**
     * This method is used to set a context invalid for XInclude namespace processing.
     * Any context defined by an &lt;include&gt; or &lt;fallback&gt; element is not
     * valid for processing the include parent's [in-scope namespaces]. Thus, contexts
     * defined by these elements are set to invalid by the XInclude processor using
     * this method.
     */    
    public void setContextInvalid() {
        fValidContext[fCurrentContext] = false;
    }
    
    /**
     * This returns the namespace URI which was associated with the given pretext, in
     * the context that existed at the include parent of the current element.  The
     * include parent is the last element, before the current one, which was not set
     * to an invalid context using setContextInvalid()
     * 
     * @param prefix the prefix of the desired URI
     * @return the URI corresponding to the prefix in the context of the include parent
     */
    public String getURIFromIncludeParent(String prefix) {
        int lastValidContext = fCurrentContext - 1;
        while (!fValidContext[lastValidContext]) {
            lastValidContext--;
        }
        return getURI(prefix, lastValidContext);
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5777.java