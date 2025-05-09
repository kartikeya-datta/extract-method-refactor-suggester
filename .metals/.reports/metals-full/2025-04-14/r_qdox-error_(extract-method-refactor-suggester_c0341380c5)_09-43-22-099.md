error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/255.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/255.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/255.java
text:
```scala
S@@tring docComment = GeneratorJava.generateConstraintEnrichedDocComment(mAttribute,false,GeneratorJava.INDENT);

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
   This code piece represents an attribute. Even though the code can
   handle several attributes in the same statement, the code generated
   will be separate statements and initialization code for all but the
   last will be removed.
*/
public class AttributeCodePiece extends NamedCodePiece
{
    /** The code piece this attribute represents. */
    private CompositeCodePiece attributeDef;

    /** The names of declared attributes. */
    private Vector attributeNames;

    /** Indicating that the type name is fully qualified in the
        original source code. */
    private boolean typeFullyQualified;

    /**
       Constructor.

       @param modifiers The code piece for modifiers.
       @param type The code piece for the type.
       @param names Vector with attribute names.
    */
    public AttributeCodePiece(CodePiece modifiers,
                              CodePiece type,
                              Vector names)
    {
	attributeNames = new Vector();
	attributeDef = new CompositeCodePiece(modifiers);
	attributeDef.add(type);
	for(Iterator i = names.iterator(); i.hasNext(); ) {
	    CodePiece cp = (CodePiece)i.next();
	    String cpText = cp.getText().toString();
	    attributeDef.add(cp);
	    int pos=0;
	    if((pos=cpText.indexOf('[')) != -1) {
		attributeNames.add(cpText.substring(0, pos));
	    }
	    else {
		attributeNames.add(cpText);
	    }
	}
	typeFullyQualified = (type.getText().toString().indexOf('.') != -1);
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return attributeDef.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return attributeDef.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return attributeDef.getEndPosition();
    }

    /**
	Return the start line
    */
    public int getStartLine()
    {
	return attributeDef.getStartLine();
    }

    /**
	Return the end line
    */
    public int getEndLine()
    {
	return attributeDef.getEndLine();
    }

    /**
       Write the code this piece represents to file. Remove this
       feature from the top vector in newFeaturesStack.
    */
    public void write(Writer writer,
                      Stack parseStateStack,
                      int column)
	throws Exception
    {
	ParseState parseState = (ParseState)parseStateStack.peek();
	Vector features = parseState.getNewFeatures();

	for(Iterator i = attributeNames.iterator(); i.hasNext(); ) {
	    String name = (String)i.next();
	    for(Iterator j = features.iterator(); j.hasNext(); ) {
		MFeature mFeature = (MFeature)j.next();
		if(mFeature.getName().equals(name)) {
		    parseState.newFeature(mFeature);
		    MAttribute mAttribute = (MAttribute)mFeature;

		    String docComment = GeneratorJava.generateConstraintEnrichedDocComment(mAttribute);
		    if(docComment != null) {
              writer.write (docComment);
              writer.write ("\n");
			  for(int k=0; k<column; k++) {
			    writer.write(" ");
			  }
		    }

		    if(mAttribute.getChangeability() ==
		       MChangeableKind.FROZEN) {
			writer.write("final ");
		    }
		    if(mAttribute.getOwnerScope() ==
		       MScopeKind.CLASSIFIER) {
			writer.write("static ");
		    }
		    if(mAttribute.getVisibility() ==
		       MVisibilityKind.PUBLIC) {
			writer.write("public ");
		    }
		    else if(mAttribute.getVisibility() ==
			    MVisibilityKind.PROTECTED) {
			writer.write("protected ");
		    }
		    else if(mAttribute.getVisibility() ==
			    MVisibilityKind.PRIVATE) {
			writer.write("private ");
		    }

		    if(typeFullyQualified) {
			writer.write(mAttribute.getType()
				     .getNamespace().getName() + ".");
		    }
		    writer.write(mAttribute.getType().getName() + " " +
				     mAttribute.getName());
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/255.java