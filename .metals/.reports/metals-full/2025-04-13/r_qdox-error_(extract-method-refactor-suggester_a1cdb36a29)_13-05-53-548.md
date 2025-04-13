error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2985.java
text:
```scala
i@@f ( Interpreter.DEBUG ) Interpreter.debug("array base type = "+type);

/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is BeanShell. The Initial Developer of the Original    *
 *  Code is Pat Niemeyer. Portions created by Pat Niemeyer are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Learning Java, O'Reilly & Associates                           *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/


package bsh;

import java.lang.reflect.Array;

/**
	The name of this class is somewhat misleading.  This covers both the case
	where there is an array initializer and 
*/
class BSHArrayDimensions extends SimpleNode
{
	public Class baseType;
    private int arrayDims;

	/** The Length in each dimension.  This value set by the eval() */
	// is it ok to cache this here?
	// it can't change, right?
	public int [] dimensions;  

    BSHArrayDimensions(int id) { super(id); }

    public void addArrayDimension() { arrayDims++; }

    public Object eval( 
			Class type, CallStack callstack, Interpreter interpreter ) 
		throws EvalError 
	{
		Interpreter.debug("array base type = "+type);
		baseType = type;
		return eval( callstack, interpreter );
	}

	/**
		Evaluate the structure of the array in one of two ways:

			a) an initializer exists, evaluate it and return
			the fully constructed array object, also record the dimensions
			of that array
			
			b) evaluate and record the lengths in each dimension and 
			return void.

		The structure of the array dims is maintained in dimensions.
	*/
    public Object eval( CallStack callstack, Interpreter interpreter )  
		throws EvalError
    {
		SimpleNode child = (SimpleNode)jjtGetChild(0);

		if (child instanceof BSHArrayInitializer)
		// evaluate the initializer and the dimensions it returns
		{
			if ( baseType == null )
				throw new EvalError( 
					"Internal Array Eval err:  unknown base type", this);

			Object initValue = ((BSHArrayInitializer)child).eval(
				baseType, arrayDims, callstack, interpreter);

			Class arrayClass = initValue.getClass();
			dimensions = new int[
				Reflect.getArrayDimensions(arrayClass) ];

			// compare with number of dimensions explicitly specified
			if (dimensions.length != arrayDims)
				throw new EvalError(
				"Incompatible initializer. Allocation calls for a " + 
				arrayDims + " dimensional array, but initializer is a " +
					dimensions.length + " dimensional array", this);

			// fill in dimensions[] lengths
			Object arraySlice = initValue;
			for(int i = 0; i < dimensions.length; i++) {
				dimensions[i] = Array.getLength( arraySlice );
				if ( dimensions[i] > 0 )
					arraySlice = Array.get(arraySlice, 0);
			}

			return initValue;
		}
		else 
		// evaluate the dimensions of the array
		{
			dimensions = new int[ jjtGetNumChildren() ];
			for(int i = 0; i < dimensions.length; i++)
			{
				try {
					Object length = ((SimpleNode)jjtGetChild(i)).eval(
						callstack, interpreter);
					dimensions[i] = ((Primitive)length).intValue();
				}
				catch(Exception e)
				{
					throw new EvalError(
						"Array index: " + i + 
						" does not evaluate to an integer", this);
				}
			}
		}

        return Primitive.VOID;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2985.java