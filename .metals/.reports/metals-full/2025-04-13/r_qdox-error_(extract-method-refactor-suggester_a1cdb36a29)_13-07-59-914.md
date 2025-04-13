error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8280.java
text:
```scala
i@@f (this.depth > 0) return false;

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

/**
 * Context used during type inference for a generic method invocation
 */
public class InferenceContext {
	
	private TypeBinding[][][] collectedSubstitutes;
	MethodBinding genericMethod;
	int depth;
	int status;
	TypeBinding expectedType;
	boolean hasExplicitExpectedType; // indicates whether the expectedType (if set) was explicit in code, or set by default
	TypeBinding[] substitutes;
	final static int FAILED = 1;
	final static int RAW_SUBSTITUTION = 2;
	
public InferenceContext(MethodBinding genericMethod) {
	this.genericMethod = genericMethod;
	TypeVariableBinding[] typeVariables = genericMethod.typeVariables;
	int varLength = typeVariables.length;
	this.collectedSubstitutes = new TypeBinding[varLength][3][];
	this.substitutes = new TypeBinding[varLength];
}

public boolean checkRawSubstitution() {
	// only at first level, during inference from arguments
	if (depth > 0) return false;
//	if (this.argumentIndex < 0 || this.depth != 0) {
//		return false;
//	}
	this.status = RAW_SUBSTITUTION;
	return true;
}		

public TypeBinding[] getSubstitutes(TypeVariableBinding typeVariable, int constraint) {
	return this.collectedSubstitutes[typeVariable.rank][constraint];
}

/**
 * Returns true if any unresolved variable is detected, i.e. any variable is substituted with itself
 */
public boolean hasUnresolvedTypeArgument() {
	for (int i = 0, varLength = this.substitutes.length; i <varLength; i++) {
		if (this.substitutes[i] == null) {
			return true;
		}
	}
	return false;
}		

public void recordSubstitute(TypeVariableBinding typeVariable, TypeBinding actualType, int constraint) {
    TypeBinding[][] variableSubstitutes = this.collectedSubstitutes[typeVariable.rank];
    insertLoop: {
    	TypeBinding[] constraintSubstitutes = variableSubstitutes[constraint];
    	int length;
    	if (constraintSubstitutes == null) {
    		length = 0;
    		constraintSubstitutes = new TypeBinding[1];
    	} else {
    		length = constraintSubstitutes.length;
	        for (int i = 0; i < length; i++) {
	        	TypeBinding substitute = constraintSubstitutes[i];
	            if (substitute == actualType) return; // already there
	            if (substitute == null) {
	                constraintSubstitutes[i] = actualType;
	                break insertLoop;
	            }
	        }
	        // no free spot found, need to grow by one
	        System.arraycopy(constraintSubstitutes, 0, constraintSubstitutes = new TypeBinding[length+1], 0, length);
    	}
        constraintSubstitutes[length] = actualType;
        variableSubstitutes[constraint] = constraintSubstitutes;
    }
}
public String toString() {
	StringBuffer buffer = new StringBuffer(20);
	buffer.append("InferenceContex for ");//$NON-NLS-1$
	for (int i = 0, length = this.genericMethod.typeVariables.length; i < length; i++) {
		buffer.append(this.genericMethod.typeVariables[i]);
	}
	buffer.append(this.genericMethod); 
	buffer.append("\n\t[status=");//$NON-NLS-1$
	switch(this.status) {
		case 0 :
			buffer.append("ok]");//$NON-NLS-1$
			break;
		case FAILED :
			buffer.append("failed]");//$NON-NLS-1$
			break;
		case RAW_SUBSTITUTION :
			buffer.append("raw-subst]");//$NON-NLS-1$
			break;
	}
	if (this.expectedType == null) {
		buffer.append(" [expectedType=null]"); //$NON-NLS-1$
	} else {
		buffer.append(" [expectedType=").append(this.expectedType.shortReadableName()).append(']'); //$NON-NLS-1$
	}
	buffer.append(" [depth=").append(this.depth).append(']'); //$NON-NLS-1$
	buffer.append("\n\t[collected={");//$NON-NLS-1$
	for (int i = 0, length = this.collectedSubstitutes == null ? 0 : this.collectedSubstitutes.length; i < length; i++) {
		TypeBinding[][] collected = this.collectedSubstitutes[i];
		for (int j = TypeConstants.CONSTRAINT_EQUAL; j <= TypeConstants.CONSTRAINT_SUPER; j++) {
			TypeBinding[] constraintCollected = collected[j];
			if (constraintCollected != null) {
				for (int k = 0, clength = constraintCollected.length; k < clength; k++) {
					buffer.append("\n\t\t").append(this.genericMethod.typeVariables[i].sourceName); //$NON-NLS-1$
					switch (j) {
						case TypeConstants.CONSTRAINT_EQUAL :
							buffer.append("="); //$NON-NLS-1$
							break;
						case TypeConstants.CONSTRAINT_EXTENDS :
							buffer.append("<:"); //$NON-NLS-1$
							break;
						case TypeConstants.CONSTRAINT_SUPER :
							buffer.append(">:"); //$NON-NLS-1$
							break;
					}
					if (constraintCollected[k] != null) {
						buffer.append(constraintCollected[k].shortReadableName());
					}					
				}
			}
		}
	}
	buffer.append("}]");//$NON-NLS-1$
	buffer.append("\n\t[inferred=");//$NON-NLS-1$
	int count = 0;
	for (int i = 0, length = this.substitutes == null ? 0 : this.substitutes.length; i < length; i++) {
		if (this.substitutes[i] == null) continue;
		count++;
		buffer.append('{').append(this.genericMethod.typeVariables[i].sourceName);
		buffer.append("=").append(this.substitutes[i].shortReadableName()).append('}'); //$NON-NLS-1$
	}
	if (count == 0) buffer.append("{}"); //$NON-NLS-1$
	buffer.append(']');
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8280.java