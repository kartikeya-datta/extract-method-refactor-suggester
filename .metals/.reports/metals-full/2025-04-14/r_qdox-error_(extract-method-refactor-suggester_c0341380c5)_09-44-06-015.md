error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12847.java
text:
```scala
r@@eturn b.getExactMethod(template.selector, template.parameters,null);

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

import org.aspectj.weaver.Advice;
import org.aspectj.org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Expression;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/**
 * Takes a method that already has the three extra parameters
 * thisJoinPointStaticPart, thisJoinPoint and thisEnclosingJoinPointStaticPart
 */

public class ThisJoinPointVisitor extends ASTVisitor {
	boolean needsDynamic = false;
	boolean needsStatic = false;
	boolean needsStaticEnclosing = false;
	boolean hasEffectivelyStaticRef = false;

	LocalVariableBinding thisJoinPointDec;
	LocalVariableBinding thisJoinPointStaticPartDec;
	LocalVariableBinding thisEnclosingJoinPointStaticPartDec;
	
	LocalVariableBinding thisJoinPointDecLocal;
	LocalVariableBinding thisJoinPointStaticPartDecLocal;
	LocalVariableBinding thisEnclosingJoinPointStaticPartDecLocal;

	boolean replaceEffectivelyStaticRefs = false;
	
	AbstractMethodDeclaration method;

	ThisJoinPointVisitor(AbstractMethodDeclaration method) {
		this.method = method;
		int index = method.arguments.length - 3;
		
		thisJoinPointStaticPartDecLocal = method.scope.locals[index];
		thisJoinPointStaticPartDec = method.arguments[index++].binding;
		thisJoinPointDecLocal = method.scope.locals[index];
		thisJoinPointDec = method.arguments[index++].binding;
		thisEnclosingJoinPointStaticPartDecLocal = method.scope.locals[index];
		thisEnclosingJoinPointStaticPartDec = method.arguments[index++].binding;
	}

	public void computeJoinPointParams() {
		// walk my body to see what is needed
		method.traverse(this, (ClassScope) null);

		//??? add support for option to disable this optimization
		//System.err.println("walked: " + method);
		//System.err.println("check:  "+ hasEffectivelyStaticRef + ", " + needsDynamic);
		if (hasEffectivelyStaticRef && !needsDynamic) {
			// replace effectively static refs with thisJoinPointStaticPart
			replaceEffectivelyStaticRefs = true;
			needsStatic = true;
			method.traverse(this, (ClassScope) null);
		}
		//System.err.println("done: " + method);
	}

	boolean isRef(NameReference ref, Binding binding) {
		//System.err.println("check ref: " + ref + " is " + System.identityHashCode(ref));
		return ref.binding == binding;
	}

	boolean isRef(Expression expr, Binding binding) {
		return expr != null
			&& expr instanceof NameReference
			&& isRef((NameReference) expr, binding);
	}

	public void endVisit(SingleNameReference ref, BlockScope scope) {
		if (isRef(ref, thisJoinPointDec)) {
			needsDynamic = true;
		} else if (isRef(ref, thisJoinPointStaticPartDec)) {
			needsStatic = true;
		} else if (isRef(ref, thisEnclosingJoinPointStaticPartDec)) {
			needsStaticEnclosing = true;
		}
	}

	boolean canTreatAsStatic(String id) {
		return id.equals("toString")
 id.equals("toShortString")
 id.equals("toLongString")
 id.equals("getKind")
 id.equals("getSignature")
 id.equals("getSourceLocation");
			//TODO: This is a good optimization, but requires more work than the above
			//      we have to replace a call with a direct reference, not just a different call
			//|| id.equals("getStaticPart");
	}

	//        boolean canTreatAsStatic(VarExpr varExpr) {
	//            ASTObject parent = varExpr.getParent();
	//            if (parent instanceof CallExpr) {
	//                Method calledMethod = ((CallExpr)parent).getMethod();
	//                return canTreatAsStatic(calledMethod);
	//
	//                //??? should add a case here to catch
	//                //??? tjp.getEnclosingExecutionJoinPoint().STATIC_METHOD()
	//            } else if (parent instanceof BinopExpr) {
	//                BinopExpr binop = (BinopExpr)parent;
	//                if (binop.getType().isEquivalent(this.getTypeManager().getStringType())) {
	//                    return true;
	//                } else {
	//                    return false;
	//                }
	//            } else {
	//                return false;
	//            }
	//        }

	boolean inBlockThatCantRun = false;

	public boolean visit(MessageSend call, BlockScope scope) {
		Expression receiver = call.receiver;
		if (isRef(receiver, thisJoinPointDec)) {
			if (canTreatAsStatic(new String(call.selector))) {
				if (replaceEffectivelyStaticRefs) {
					replaceEffectivelyStaticRef(call);
				} else {
					//System.err.println("has static reg");
					hasEffectivelyStaticRef = true;
					if (call.arguments != null) {
						int argumentsLength = call.arguments.length;
						for (int i = 0; i < argumentsLength; i++)
							call.arguments[i].traverse(this, scope);
					}
					return false;
				}
			}
		}

		return super.visit(call, scope);
	}

	private void replaceEffectivelyStaticRef(MessageSend call) {
		NameReference receiver = (NameReference) call.receiver;
		
		// Don't continue if the call binding is null, as we are going to report an error about this line of code!
		if (call.binding==null) return;	
		
		//System.err.println("replace static ref: " + receiver + " is " + System.identityHashCode(receiver));
		receiver.binding = thisJoinPointStaticPartDecLocal; //thisJoinPointStaticPartDec;
		receiver.codegenBinding = thisJoinPointStaticPartDecLocal;
		
		ReferenceBinding thisJoinPointStaticPartType = 
			(ReferenceBinding)thisJoinPointStaticPartDec.type;
			
		receiver.actualReceiverType =
			receiver.resolvedType = thisJoinPointStaticPartType;
			
		call.setActualReceiverType(thisJoinPointStaticPartType);

		AstUtil.replaceMethodBinding(call, getEquivalentStaticBinding(call.binding));
	}
	
	private MethodBinding getEquivalentStaticBinding(MethodBinding template) {
		ReferenceBinding b = (ReferenceBinding)thisJoinPointStaticPartDec.type;
		return b.getExactMethod(template.selector, template.parameters);
	}

	public int removeUnusedExtraArguments() {
		int extraArgumentFlags = 0;
		
		this.computeJoinPointParams();
		MethodBinding binding = method.binding;
		
		
		int index = binding.parameters.length - 3;
		if (needsStaticEnclosing) {
			extraArgumentFlags |= Advice.ThisEnclosingJoinPointStaticPart;
		} else {
			removeParameter(index+2);
		}
		
		if (needsDynamic) {
			extraArgumentFlags |= Advice.ThisJoinPoint;
		} else {
			removeParameter(index+1);
		}
		
		if (needsStatic) {
			extraArgumentFlags |= Advice.ThisJoinPointStaticPart;
		} else {
			removeParameter(index+0);
		}
		
		return extraArgumentFlags;
	}

	private void removeParameter(int indexToRemove) {
//		TypeBinding[] parameters = method.binding.parameters;
		method.scope.locals = removeLocalBinding(indexToRemove, method.scope.locals);
		method.scope.localIndex -= 1;
		method.binding.parameters = removeParameter(indexToRemove, method.binding.parameters);
	}

	
	private static TypeBinding[] removeParameter(int index, TypeBinding[] bindings) {
		int len = bindings.length;
		TypeBinding[] ret = new TypeBinding[len-1];
		System.arraycopy(bindings, 0, ret, 0, index);
		System.arraycopy(bindings, index+1, ret, index, len-index-1);
		return ret;
	}

	private static LocalVariableBinding[] removeLocalBinding(int index, LocalVariableBinding[] bindings) {
		int len = bindings.length;
		//??? for performance we should do this in-place
		LocalVariableBinding[] ret = new LocalVariableBinding[len-1];
		System.arraycopy(bindings, 0, ret, 0, index);
		System.arraycopy(bindings, index+1, ret, index, len-index-1);
		return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12847.java