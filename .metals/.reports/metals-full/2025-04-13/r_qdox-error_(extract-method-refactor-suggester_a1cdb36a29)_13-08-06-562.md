error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12667.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12667.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12667.java
text:
```scala
s@@eeAlsoLocations,declared,problem.getID());

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
 *     PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.ajdt.internal.core.builder;

import java.io.File;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.SourceLocation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

/**
 * 
 */
public class EclipseAdapterUtils {

    //XXX some cut-and-paste from eclipse sources
    public static String makeLocationContext(ICompilationUnit compilationUnit, IProblem problem) {
        //extra from the source the innacurate     token
        //and "highlight" it using some underneath ^^^^^
        //put some context around too.

        //this code assumes that the font used in the console is fixed size

        //sanity .....
        int startPosition = problem.getSourceStart();
        int endPosition = problem.getSourceEnd();
        
        if ((startPosition > endPosition)
 ((startPosition <= 0) && (endPosition <= 0))
 compilationUnit==null)
            //return Util.bind("problem.noSourceInformation"); //$NON-NLS-1$
        	return "(no source information available)";

        final char SPACE = '\u0020';
        final char MARK = '^';
        final char TAB = '\t';
        char[] source = compilationUnit.getContents();
        //the next code tries to underline the token.....
        //it assumes (for a good display) that token source does not
        //contain any \r \n. This is false on statements ! 
        //(the code still works but the display is not optimal !)

        //compute the how-much-char we are displaying around the inaccurate token
        int begin = startPosition >= source.length ? source.length - 1 : startPosition;
        int relativeStart = 0;
        int end = endPosition >= source.length ? source.length - 1 : endPosition;
        int relativeEnd = 0;
        label : for (relativeStart = 0;; relativeStart++) {
            if (begin == 0)
                break label;
            if ((source[begin - 1] == '\n') || (source[begin - 1] == '\r'))
                break label;
            begin--;
        }
        label : for (relativeEnd = 0;; relativeEnd++) {
            if ((end + 1) >= source.length)
                break label;
            if ((source[end + 1] == '\r') || (source[end + 1] == '\n')) {
                break label;
            }
            end++;
        }
        //extract the message form the source
        char[] extract = new char[end - begin + 1];
        System.arraycopy(source, begin, extract, 0, extract.length);
        char c;
        //remove all SPACE and TAB that begin the error message...
        int trimLeftIndex = 0;
        while (((c = extract[trimLeftIndex++]) == TAB) || (c == SPACE)) {
        };
        System.arraycopy(
            extract,
            trimLeftIndex - 1,
            extract = new char[extract.length - trimLeftIndex + 1],
            0,
            extract.length);
        relativeStart -= trimLeftIndex;
        //buffer spaces and tabs in order to reach the error position
        int pos = 0;
        char[] underneath = new char[extract.length]; // can't be bigger
        for (int i = 0; i <= relativeStart; i++) {
            if (extract[i] == TAB) {
                underneath[pos++] = TAB;
            } else {
                underneath[pos++] = SPACE;
            }
        }
        //mark the error position
        for (int i = startPosition + trimLeftIndex;  // AMC if we took stuff off the start, take it into account!
            i <= (endPosition >= source.length ? source.length - 1 : endPosition);
            i++)
            underneath[pos++] = MARK;
        //resize underneathto remove 'null' chars
        System.arraycopy(underneath, 0, underneath = new char[pos], 0, pos);

        return new String(extract) + "\n" + new String(underneath); //$NON-NLS-2$ //$NON-NLS-1$
    }
    
    /** 
     * Extract source location file, start and end lines, and context.
     * Column is not extracted correctly.
     * @return ISourceLocation with correct file and lines but not column.
     */
    public static ISourceLocation makeSourceLocation(ICompilationUnit unit, IProblem problem) {
        int line = problem.getSourceLineNumber();
        File file = new File(new String(problem.getOriginatingFileName()));
        String context = makeLocationContext(unit, problem);
        // XXX 0 column is wrong but recoverable from makeLocationContext
        return new SourceLocation(file, line, line, 0, context);
    }

    /** 
     * Extract message text and source location, including context. 
     */
    public static IMessage makeMessage(ICompilationUnit unit, IProblem problem) { 
        ISourceLocation sourceLocation = makeSourceLocation(unit, problem);
        IProblem[] seeAlso = problem.seeAlso();
        ISourceLocation[] seeAlsoLocations = new ISourceLocation[seeAlso.length];
        for (int i = 0; i < seeAlso.length; i++) {
        	seeAlsoLocations[i] = new SourceLocation(new File(new String(seeAlso[i].getOriginatingFileName())),
        											 seeAlso[i].getSourceLineNumber());
													 
		}
		// We transform messages from AJ types to eclipse IProblems
		// and back to AJ types.  During their time as eclipse problems,
		// we remember whether the message originated from a declare
		// in the extraDetails.
		String extraDetails = problem.getSupplementaryMessageInfo();
		boolean declared = false;
		if (extraDetails!=null && extraDetails.endsWith("[deow=true]")) {
			declared = true;
			extraDetails = extraDetails.substring(0,extraDetails.length()-"[deow=true]".length());
		}
		
        IMessage msg = new Message(problem.getMessage(), 
        						   extraDetails,
								   problem.isError() ? IMessage.ERROR : IMessage.WARNING,
								   sourceLocation, 
								   null,
								   seeAlsoLocations,declared);
        return msg;
    }               

    public static IMessage makeErrorMessage(ICompilationUnit unit, String text, Exception ex) {
    	ISourceLocation loc = new SourceLocation(new File(new String(unit.getFileName())),
    												0,0,0,"");
    	IMessage msg = new Message(text,IMessage.ERROR,ex,loc);
    	return msg;
    }

	public static IMessage makeErrorMessage(String srcFile, String text, Exception ex) {
		ISourceLocation loc = new SourceLocation(new File(srcFile),
													0,0,0,"");
		IMessage msg = new Message(text,IMessage.ERROR,ex,loc);
		return msg;
	}
    
	private EclipseAdapterUtils() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12667.java