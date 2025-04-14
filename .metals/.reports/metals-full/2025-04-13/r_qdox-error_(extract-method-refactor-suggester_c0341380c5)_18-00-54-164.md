error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7156.java
text:
```scala
t@@his.world = inAspect.factory;

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

import org.aspectj.ajdt.internal.compiler.lookup.EclipseFactory;
import org.aspectj.ajdt.internal.compiler.lookup.InlineAccessFieldBinding;
import org.aspectj.ajdt.internal.compiler.lookup.InterTypeFieldBinding;
import org.aspectj.ajdt.internal.compiler.lookup.InterTypeMethodBinding;
import org.aspectj.ajdt.internal.compiler.lookup.PrivilegedFieldBinding;
import org.aspectj.ajdt.internal.compiler.lookup.PrivilegedHandler;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.ResolvedMember;
import org.eclipse.jdt.internal.compiler.AbstractSyntaxTreeVisitorAdapter;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/**
 * Walks the body of around advice
 * 
 * Makes sure that all member accesses are to public members.  Will
 * convert to use access methods when needed to ensure that.  This
 * makes it much simpler (and more modular) to inline the body of
 * an around.
 * 
 * ??? constructors are handled different and require access to the
 * target type.  changes to org.eclipse.jdt.internal.compiler.ast.AllocationExpression
 * would be required to fix this issue.
 * 
 * @author Jim Hugunin
 */

public class AccessForInlineVisitor extends AbstractSyntaxTreeVisitorAdapter {
	PrivilegedHandler handler;
	AspectDeclaration inAspect;
	EclipseFactory world; // alias for inAspect.world
	
	public AccessForInlineVisitor(AspectDeclaration inAspect, PrivilegedHandler handler) {
		this.inAspect = inAspect;
		this.world = inAspect.world;
		this.handler = handler;
	}
	
	
	public void endVisit(SingleNameReference ref, BlockScope scope) {
		if (ref.binding instanceof FieldBinding) {
			ref.binding = getAccessibleField((FieldBinding)ref.binding);
		}
	}

	public void endVisit(QualifiedNameReference ref, BlockScope scope) {
		if (ref.binding instanceof FieldBinding) {
			ref.binding = getAccessibleField((FieldBinding)ref.binding);
		}
	}

	public void endVisit(FieldReference ref, BlockScope scope) {
		if (ref.binding instanceof FieldBinding) {
			ref.binding = getAccessibleField((FieldBinding)ref.binding);
		}
	}
	public void endVisit(MessageSend send, BlockScope scope) {
		if (send instanceof Proceed) return;
		if (send.binding == null) return;
		
		if (send.isSuperAccess() && !send.binding.isStatic()) {
			send.receiver = new ThisReference();
			send.binding = send.codegenBinding = 
				getSuperAccessMethod((MethodBinding)send.binding);
		} else if (!isPublic(send.binding)) {
			send.syntheticAccessor = getAccessibleMethod((MethodBinding)send.binding);
		}
	}
	public void endVisit(AllocationExpression send, BlockScope scope) {
		if (send.binding == null) return;
		//XXX TBD
		if (isPublic(send.binding)) return;
		makePublic(send.binding.declaringClass);
		send.binding = handler.getPrivilegedAccessMethod(send.binding, send);
	}	
	public void endVisit(
		QualifiedTypeReference ref,
		BlockScope scope)
	{
		makePublic(ref.binding);
	}
	
	public void endVisit(
		SingleTypeReference ref,
		BlockScope scope)
	{
		makePublic(ref.binding);
	}
	
	private FieldBinding getAccessibleField(FieldBinding binding) {
		if (!binding.isValidBinding()) return binding;
		
		makePublic(binding.declaringClass);
		if (isPublic(binding)) return binding;
		if (binding instanceof PrivilegedFieldBinding) return binding;
		if (binding instanceof InterTypeFieldBinding) return binding;

		if (binding.isPrivate() &&  binding.declaringClass != inAspect.binding) {
			binding.modifiers = AstUtil.makePackageVisible(binding.modifiers);
		}
		
		ResolvedMember m = world.makeResolvedMember(binding);
		if (inAspect.accessForInline.containsKey(m)) return (FieldBinding)inAspect.accessForInline.get(m);
		FieldBinding ret = new InlineAccessFieldBinding(inAspect, binding);
		inAspect.accessForInline.put(m, ret);
		return ret;
	}
	
	private MethodBinding getAccessibleMethod(MethodBinding binding) {
		if (!binding.isValidBinding()) return binding;
		
		makePublic(binding.declaringClass);  //???
		if (isPublic(binding)) return binding;
		if (binding instanceof InterTypeMethodBinding) return binding;

		if (binding.isPrivate() &&  binding.declaringClass != inAspect.binding) {
			binding.modifiers = AstUtil.makePackageVisible(binding.modifiers);
		}

		
		ResolvedMember m = world.makeResolvedMember(binding);
		if (inAspect.accessForInline.containsKey(m)) return (MethodBinding)inAspect.accessForInline.get(m);
		MethodBinding ret = world.makeMethodBinding(
			AjcMemberMaker.inlineAccessMethodForMethod(inAspect.typeX, m)
			);
		inAspect.accessForInline.put(m, ret);
		return ret;
	}
	
	private MethodBinding getSuperAccessMethod(MethodBinding binding) {
		ResolvedMember m = world.makeResolvedMember(binding);
		if (inAspect.superAccessForInline.containsKey(m)) return (MethodBinding)inAspect.superAccessForInline.get(m);
		MethodBinding ret = world.makeMethodBinding(
			AjcMemberMaker.superAccessMethod(inAspect.typeX, m)
			);
		inAspect.superAccessForInline.put(m, ret);
		return ret;
	}
	
	private boolean isPublic(FieldBinding fieldBinding) {
		// these are always effectively public to the inliner
		if (fieldBinding instanceof InterTypeFieldBinding) return true;
		return fieldBinding.isPublic();
	}

	private boolean isPublic(MethodBinding methodBinding) {
		// these are always effectively public to the inliner
		if (methodBinding instanceof InterTypeMethodBinding) return true;
		return methodBinding.isPublic();
	}

	private void makePublic(TypeBinding binding) {
		if (binding instanceof ReferenceBinding) {
			ReferenceBinding rb = (ReferenceBinding)binding;
			if (!rb.isPublic()) handler.notePrivilegedTypeAccess(rb, null); //???
		} else if (binding instanceof ArrayBinding) {
			makePublic( ((ArrayBinding)binding).leafComponentType );
		} else {
			return;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7156.java