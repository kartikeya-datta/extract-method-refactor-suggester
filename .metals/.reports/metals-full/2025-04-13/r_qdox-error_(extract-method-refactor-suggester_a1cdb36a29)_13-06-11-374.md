error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5766.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5766.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5766.java
text:
```scala
t@@his.qualifyingType = parent.qualifyingType;

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajdt.internal.compiler.ast;

import org.aspectj.weaver.AdviceKind;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;


public class Proceed extends MessageSend {
	public boolean inInner = false;
	
	public Proceed(MessageSend parent) {
		super();
		
		this.receiver = parent.receiver;
		this.selector  = parent.selector;
		this.arguments  = parent.arguments;
		this.binding  = parent.binding;
		this.codegenBinding = parent.codegenBinding;

		this.nameSourcePosition = parent.nameSourcePosition;
		this.receiverType = parent.receiverType;
		this.qualifyingType = qualifyingType;
		
		this.sourceStart = parent.sourceStart;
		this.sourceEnd = parent.sourceEnd;
	}

	public TypeBinding resolveType(BlockScope scope) {
		// find out if I'm really in an around body or not
		//??? there is a small performance issue here
		AdviceDeclaration aroundDecl = findEnclosingAround(scope);
		
		if (aroundDecl == null) {
			return super.resolveType(scope);
		}
		


		constant = NotAConstant;
		binding = codegenBinding = aroundDecl.proceedMethodBinding;
		
		this.qualifyingType = this.receiverType = binding.declaringClass;
		
		int baseArgCount = 0;
		if (arguments != null) {
			baseArgCount = arguments.length;
			Expression[] newArguments = new Expression[baseArgCount + 1];
			System.arraycopy(arguments, 0, newArguments, 0, baseArgCount);
			arguments = newArguments;
		} else {
			arguments = new Expression[1];
		}
		
		arguments[baseArgCount] = AstUtil.makeLocalVariableReference(aroundDecl.extraArgument.binding);
		
		int declaredParameterCount = aroundDecl.getDeclaredParameterCount();
		if (baseArgCount < declaredParameterCount) {
			scope.problemReporter().signalError(this.sourceStart, this.sourceEnd, 
								"too few arguments to proceed, expected " + declaredParameterCount);
			aroundDecl.ignoreFurtherInvestigation = true;
			return null; //binding.returnType;
		}
		
		if (baseArgCount > declaredParameterCount) {
			scope.problemReporter().signalError(this.sourceStart, this.sourceEnd, 
								"too many arguments to proceed, expected " + declaredParameterCount);
			aroundDecl.ignoreFurtherInvestigation = true;
			return null; //binding.returnType;
		}

		
		for (int i=0, len=arguments.length; i < len; i++) {
			Expression arg = arguments[i];
			TypeBinding argType = arg.resolveType(scope);
			if (argType != null) {
				TypeBinding paramType = binding.parameters[i];
				if (!scope.areTypesCompatible(argType, paramType)) {
					scope.problemReporter().typeMismatchError(argType, paramType, arg);
				}
				arg.implicitWidening(binding.parameters[i], argType);
			}
		}
	
		
		return binding.returnType;
	}

	private AdviceDeclaration findEnclosingAround(Scope scope) {
		if (scope == null) return null;
		
		if (scope instanceof MethodScope) {
			MethodScope methodScope = (MethodScope)scope;
			ReferenceContext context = methodScope.referenceContext;
			if (context instanceof AdviceDeclaration) {
				AdviceDeclaration adviceDecl = (AdviceDeclaration)context;
				if (adviceDecl.kind == AdviceKind.Around) {
					adviceDecl.proceedCalls.add(this);
					return adviceDecl;
				} else {
					return null;
				}
			}
		} else if (scope instanceof ClassScope) {
			inInner = true;
		}
		
		return findEnclosingAround(scope.parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5766.java