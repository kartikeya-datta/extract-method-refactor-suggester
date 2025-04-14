error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12115.java
text:
```scala
s@@b.append("" + startLine + "-" + endLine);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.bridge;


import java.io.File;

import org.aspectj.util.LangUtil;

/**
 * Immutable source location.
 * This guarantees that the source file is not null
 * and that the numeric values are positive and line <= endLine.
 * @see org.aspectj.lang.reflect.SourceLocation
 * @see org.aspectj.compiler.base.parser.SourceInfo
 * @see org.aspectj.tools.ide.SourceLine
 * @see org.aspectj.testing.harness.ErrorLine
 */
public class SourceLocation implements ISourceLocation, java.io.Serializable {
    
    /** used when SourceLocation is not available */
    public static final ISourceLocation UNKNOWN 
        = new SourceLocation(ISourceLocation.NO_FILE, 0, 0, 0);
    
    /** @throws IllegalArgumentException if the input would not be a valid line */
    public static final void validLine(int line) {
        if (line < 0) {
            throw new IllegalArgumentException("negative line: " + line);
        } else if (line > ISourceLocation.MAX_LINE) {
            throw new IllegalArgumentException("line too large: " + line);
        }
    }
    
    /** @throws IllegalArgumentException if the input would not be a valid column */
    public static final void validColumn(int column) {
        if (column < 0) {
            throw new IllegalArgumentException("negative column: " + column);
        } else if (column > ISourceLocation.MAX_COLUMN) {
            throw new IllegalArgumentException("column too large: " + column);
        }
    }

    private final File sourceFile;
    private final int startLine;
    private final int column;
    private final int endLine;
    private boolean noColumn;

    /** 
     * Same as SourceLocation(file, line, line, 0),
     * except that column is not rendered during toString()
     */    
    public SourceLocation(File file, int line) {
        this(file, line, line, NO_COLUMN);
    }

    /** same as SourceLocation(file, line, endLine, ISourceLocation.NO_COLUMN) */
    public SourceLocation(File file, int line, int endLine) {
        this(file, line, endLine, ISourceLocation.NO_COLUMN);
    }
    
    /**
     * @param file File of the source; if null, use ISourceLocation.NO_FILE, not null
     * @param line int starting line of the location - positive number
     * @param endLine int ending line of the location - <= starting line
     * @param column int character position of starting location - positive number
     */
    public SourceLocation(File file, int line, int endLine, int column) {
        if (column == NO_COLUMN) {
            column = 0;
            noColumn = true;
        }
        if (null == file) {
            file = ISourceLocation.NO_FILE;
        }
        validLine(line);
        validLine(endLine);
        LangUtil.throwIaxIfFalse(line <= endLine , line + " > " + endLine);
        LangUtil.throwIaxIfFalse(column >= 0, "negative column: " + column);
        this.sourceFile = file;
        this.startLine = line;
        this.column = column;
        this.endLine = endLine;
    }
    
    public File getSourceFile() {
        return sourceFile;
    }
    public int getLine() {
        return startLine;
    }
    
    /**
     * @return int actual column or 0 if not available per constructor treatment
     *         of ISourceLocation.NO_COLUMN 
     */
    public int getColumn() {
        return column;
    }
    
    public int getEndLine() {
        return endLine;
    }
    
    /** @return String {file:}line{:column} */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        if (sourceFile != ISourceLocation.NO_FILE) {
            sb.append(sourceFile.getPath());
            sb.append(":");
        }
        sb.append("" + startLine);
        if (!noColumn) {
            sb.append(":" + column);
        }
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12115.java