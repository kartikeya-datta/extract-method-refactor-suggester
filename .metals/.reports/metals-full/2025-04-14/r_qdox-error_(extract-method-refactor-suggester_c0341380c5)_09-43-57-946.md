error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6327.java
text:
```scala
i@@f(feature.getName().equals(name) && feature instanceof MOperation) { // fixed issue 1527

// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import java.io.*;
import java.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

/**
   This code piece represents an operation declaration.
*/
public class OperationCodePiece extends NamedCodePiece
{
    /** The code piece this operation represents. */
    private CodePiece operationDef;

    /** The name of the operation. */
    private String name;

    /** Indicates that the typenames are fully qualified in the
        source. */
    private boolean fullyQualifiedTypeNames;

    /**
       Constructor.

       @param javadocDef The code piece for the javadoc.
       @param operationDef The code piece this operation represents.
       @param name The name of the operation.
    */
    public OperationCodePiece(CodePiece javadocDef,
                              CodePiece operationDef,
                              String name)
    {
	this.name = name;
	fullyQualifiedTypeNames = (operationDef.getText().toString().indexOf('.') != -1);
	if(javadocDef != null) {
	    CompositeCodePiece cp = new CompositeCodePiece(javadocDef);
	    cp.add(operationDef);
	    this.operationDef = cp;
	}
	else {
	    this.operationDef = operationDef;
	}
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return operationDef.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return operationDef.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return operationDef.getEndPosition();
    }

    /**
	Return the start line
    */
    public int getStartLine()
    {
	return operationDef.getStartLine();
    }

    /**
	Return the end line
    */
    public int getEndLine()
    {
	return operationDef.getEndLine();
    }

    /**
       Write the code this piece represents to file. Remove this
       feature from the top vector in the stack newFeaturesStack.
    */
    public void write (BufferedReader reader,
                       BufferedWriter writer,
                       Stack parseStateStack) throws Exception
    {
        ParseState parseState = (ParseState)parseStateStack.peek();
        Vector features = parseState.getNewFeatures();
        boolean found = false;

        for(Iterator j=features.iterator(); j.hasNext() && !found; ) {
            MFeature feature = (MFeature)j.next();
            if(feature.getName().equals(name)) {
                found = true;
                parseState.newFeature(feature);
                MOperation mOperation = (MOperation)feature;
                writer.write (GeneratorJava.getInstance().generateOperation(mOperation, true));
            }
        }
        if(found) {
            // fast forward original code (overwriting)
            ffCodePiece(reader,null);
        } else {
            // not in model, so write the original code
            ffCodePiece(reader,writer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6327.java