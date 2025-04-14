error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7740.java
text:
```scala
public static final S@@tring MARKER_FOR_IS_DELETE_LINE = "MARKER_FOR_XPAND_ISDELETELINE_59&21?%5&6<#_ENDMARKER";

/*
Copyright (c) 2008 Arno Haase, André Arnold.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
    André Arnold
 */
package org.eclipse.xtend.middleend.xtend.internal.xtendlib;


/**
 * This class supports the "isDeleteLine" feature of Xpand, i.e. the "-" at the end of a statement that deletes 
 *  whitespace both backward and forward.<br>
 *  
 * Since it is non-local functionality, it requires global postprocessing. For this purpose, a marker string is inserted wherever 
 *  this deletion of whitespace should be performed.<br>
 *  
 * Since this postprocessing requires transformation of the entire contents of a file into a flat string, this feature precludes
 *  streaming. Therefore a flag is introduced to indicate if the feature was actually used. This requires resetting at the beginning
 *  of each FILE statement.
 *  
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public class XpandIsDeleteLine {
    public static final String MARKER_FOR_IS_DELETE_LINE = "MARKER_FOR_XPAND_ISDELETELINE_\ufa92Ä\u9988\u7123ä\u9881\u5499\u9284\u9934ÄÖÜß\ufa92Ä\u9988\u7123ä\u9882\u5499\u9284\u9934_ENDMARKER";

    private boolean _isInScope = false;
    private boolean _hasDeleteLine = false;
    
    
    public void XpandInitNewScope () {
        if (_isInScope)
            throw new IllegalStateException ("nested FILE statements are not permitted");
        _isInScope = true;
        _hasDeleteLine = false;
    }
    
    public void XpandRegisterDeleteLine() {
        _hasDeleteLine = true;
    }
    
    public CharSequence XpandPostprocess (CharSequence s) {
        try {
            if (! _hasDeleteLine)
                return s;

            String result = s.toString();
            int indMarker = result.indexOf (MARKER_FOR_IS_DELETE_LINE);
            
            while (indMarker >= 0) {
                // if and only if there is nothing but whitespace between the marker and the previous newline, delete this whitespace (leaving the newline alone)
                final int startOfDelete = indBeginDelete (result, indMarker);
                
                // delete all whitespace after the marker up to, and including, the subsequent newline - or nothing, if there is anything but whitespace between the marker and the subsequent newline
                final int endOfDelete = indEndDelete (result, indMarker);
                
                result = result.substring(0, startOfDelete) + result.substring (endOfDelete);
                indMarker = result.indexOf (MARKER_FOR_IS_DELETE_LINE);
            }
            
            return result;
        }
        finally {
            _isInScope = false;
            _hasDeleteLine = false;
        }
    }
    
    private boolean isNewLine(char c) {
        return c == '\n' || c == '\r';
    }

    private int indEndDelete (String buffer, int indMarker) {
        boolean wsOnly = true;
        int result = indMarker + MARKER_FOR_IS_DELETE_LINE.length();

        while (result < buffer.length() && wsOnly) {
            final char c = buffer.charAt (result);
            wsOnly = Character.isWhitespace(c);
            if (wsOnly && isNewLine(c)) {
                if (c == '\r' && result + 1 < buffer.length() && buffer.charAt (result + 1) == '\n')
                    result++;
                return result + 1;
            }
            
            result++;
        }
        
        return indMarker + MARKER_FOR_IS_DELETE_LINE.length();
    }
    
    private int indBeginDelete (String buffer, int indMarker) {
        boolean wsOnly = true;
        int result = indMarker;
        
        while (result > 0 && wsOnly) {
            final char c = buffer.charAt (result - 1);
            wsOnly = Character.isWhitespace(c);
            if (wsOnly && isNewLine (c))
                return result;
            
            result--;
        }
        
        if (wsOnly)
            return 0;
        else 
            return indMarker;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7740.java