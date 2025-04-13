error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6317.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6317.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6317.java
text:
```scala
i@@f ( (source[1] == 'x') || (source[1] == 'X') )

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class LongLiteral extends NumberLiteral {
	long value;
	
	static final Constant FORMAT_ERROR = new DoubleConstant(1.0/0.0); // NaN;	
		
public LongLiteral(char[] token, int s,int e) {
	super(token, s,e);
}
public LongLiteral(char[] token, int s,int e, long value) {
	this(token, s,e);
	this.value = value;
}
public void computeConstant() {
	//the overflow (when radix=10) is tested using the fact that
	//the value should always grow during its computation

	int length = source.length - 1; //minus one because the last char is 'l' or 'L'
	
	long computedValue ;
	if (source[0] == '0')
	{	if (length == 1) { 	constant = Constant.fromValue(0L);	return;	}
		final int shift,radix;
		int j ;
		if ( (source[1] == 'x') | (source[1] == 'X') )
		{	shift = 4 ; j = 2; radix = 16;}
		else
		{	shift = 3 ; j = 1; radix = 8;}
		int nbDigit = 0;
		while (source[j]=='0') 
		{	j++; //jump over redondant zero
			if ( j == length)
			{	//watch for 0000000000000L
				constant = Constant.fromValue(value = 0L);
				return ;}}
				
		int digitValue ;
		if ((digitValue = Character.digit(source[j++],radix))	< 0 ) 	
		{	constant = FORMAT_ERROR; return ;}
		if (digitValue >= 8) nbDigit = 4;
		else 	if (digitValue >= 4) nbDigit = 3;
				else 	if (digitValue >= 2) nbDigit = 2;
						else nbDigit = 1; //digitValue is not 0
		computedValue = digitValue ;
		while (j<length)
		{	if ((digitValue = Character.digit(source[j++],radix))	< 0 ) 	
			{	constant = FORMAT_ERROR; return ;}
			if ((nbDigit += shift) > 64) return /*constant stays null*/ ;
			computedValue = (computedValue<<shift) | digitValue ;}}

	else
	{	//-----------case radix=10-----------------
		long previous = computedValue = 0;
		for (int i = 0 ; i < length; i++) 
		{	int digitValue ;	
			if ((digitValue = Character.digit(source[i], 10)) < 0 ) return /*constant stays null*/ ;
			previous = computedValue;
			computedValue = 10 * computedValue + digitValue ;
			if (previous > computedValue) return /*constant stays null*/;}}
	
	constant = Constant.fromValue(value = computedValue);
}
/**
 * Code generation for long literal
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */ 
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	if (valueRequired)
		if ((implicitConversion >> 4) == T_long)
			codeStream.generateInlinedValue(value);
		else
			codeStream.generateConstant(constant, implicitConversion);
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}
public TypeBinding literalType(BlockScope scope) {
	return LongBinding;
}
public final boolean mayRepresentMIN_VALUE(){
	//a special autorized int literral is 9223372036854775808L
	//which is ONE over the limit. This special case 
	//only is used in combinaison with - to denote
	//the minimal value of int -9223372036854775808L

	return ((source.length == 20) &&
			(source[0] == '9') &&
			(source[1] == '2') &&
			(source[2] == '2') &&
			(source[3] == '3') &&			
			(source[4] == '3') &&
			(source[5] == '7') &&
			(source[6] == '2') &&
			(source[7] == '0') &&			
			(source[8] == '3') &&
			(source[9] == '6') &&
			(source[10] == '8') &&
			(source[11] == '5') &&
			(source[12] == '4') &&			
			(source[13] == '7') &&
			(source[14] == '7') &&
			(source[15] == '5') &&
			(source[16] == '8') &&			
			(source[17] == '0') &&
			(source[18] == '8'));}
public TypeBinding resolveType(BlockScope scope) {
	// the format may be incorrect while the scanner could detect
	// such error only on painfull tests...easier and faster here

	TypeBinding tb = super.resolveType(scope);
	if (constant == FORMAT_ERROR) {
		constant = NotAConstant;
		scope.problemReporter().constantOutOfFormat(this);
		this.resolvedType = null;
		return null;
	}
	return tb;
}
public void traverse(ASTVisitor visitor, BlockScope scope) {
	visitor.visit(this, scope);
	visitor.endVisit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6317.java