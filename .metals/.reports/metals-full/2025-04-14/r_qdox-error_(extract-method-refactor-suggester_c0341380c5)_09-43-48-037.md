error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8065.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8065.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8065.java
text:
```scala
public b@@oolean hasAnnotation(TypeX ofType) {

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


package org.aspectj.ajdt.internal.compiler.lookup;

import java.util.*;

import org.aspectj.ajdt.internal.compiler.ast.*;
import org.aspectj.ajdt.internal.core.builder.EclipseSourceContext;
import org.aspectj.bridge.IMessage;
//import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.*;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.PerSingleton;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.*;

/**
 * Supports viewing eclipse TypeDeclarations/SourceTypeBindings as a ResolvedTypeX
 * 
 * @author Jim Hugunin
 */
public class EclipseSourceType extends ResolvedTypeX.ConcreteName {
	protected ResolvedPointcutDefinition[] declaredPointcuts = null;
	protected ResolvedMember[] declaredMethods = null;
	protected ResolvedMember[] declaredFields = null;
	
	public List declares = new ArrayList();
	public List typeMungers = new ArrayList();
	
	private EclipseFactory factory;
	
	private SourceTypeBinding binding;
	private TypeDeclaration declaration;
	
	protected EclipseFactory eclipseWorld() {
		return factory;
	}

	public EclipseSourceType(ResolvedTypeX.Name resolvedTypeX, EclipseFactory factory,
								SourceTypeBinding binding, TypeDeclaration declaration)
	{
		super(resolvedTypeX, true);
		this.factory = factory;
		this.binding = binding;
		this.declaration = declaration;
		
		resolvedTypeX.setSourceContext(new EclipseSourceContext(declaration.compilationResult));
		resolvedTypeX.setStartPos(declaration.sourceStart);
		resolvedTypeX.setEndPos(declaration.sourceEnd);
	}


	public boolean isAspect() {
		return declaration instanceof AspectDeclaration;
	}

	public WeaverStateInfo getWeaverState() {
		return null;
	}
	
	public ResolvedTypeX getSuperclass() {
		if (binding.isInterface()) return getResolvedTypeX().getWorld().getCoreType(TypeX.OBJECT);
		//XXX what about java.lang.Object
		return eclipseWorld().fromEclipse(binding.superclass());
	}
	
	public ResolvedTypeX[] getDeclaredInterfaces() {
		return eclipseWorld().fromEclipse(binding.superInterfaces());
	}


	protected void fillDeclaredMembers() {
		List declaredPointcuts = new ArrayList();
		List declaredMethods = new ArrayList();
		List declaredFields = new ArrayList();
		
		binding.methods();  // the important side-effect of this call is to make sure bindings are completed
		AbstractMethodDeclaration[] methods = declaration.methods;
		if (methods != null) {
			for (int i=0, len=methods.length; i < len; i++) {
				AbstractMethodDeclaration amd = methods[i];
				if (amd == null || amd.ignoreFurtherInvestigation) continue;
				if (amd instanceof PointcutDeclaration) {
					PointcutDeclaration d = (PointcutDeclaration)amd;
					ResolvedPointcutDefinition df = d.makeResolvedPointcutDefinition();
					declaredPointcuts.add(df);
				} else if (amd instanceof InterTypeDeclaration) {
					// these are handled in a separate pass
					continue;
				} else if (amd instanceof DeclareDeclaration) {
					// these are handled in a separate pass
					continue;
				} else if (amd instanceof AdviceDeclaration) {
					// these are ignored during compilation and only used during weaving
					continue;
				} else {
					if (amd.binding == null || !amd.binding.isValidBinding()) continue;
					declaredMethods.add(EclipseFactory.makeResolvedMember(amd.binding));
				}
			}
		}

		FieldBinding[] fields = binding.fields();
		for (int i=0, len=fields.length; i < len; i++) {
			FieldBinding f = fields[i];
			declaredFields.add(EclipseFactory.makeResolvedMember(f));
		}
			
		this.declaredPointcuts = (ResolvedPointcutDefinition[])
			declaredPointcuts.toArray(new ResolvedPointcutDefinition[declaredPointcuts.size()]);
		this.declaredMethods = (ResolvedMember[])
			declaredMethods.toArray(new ResolvedMember[declaredMethods.size()]);
		this.declaredFields = (ResolvedMember[])
			declaredFields.toArray(new ResolvedMember[declaredFields.size()]);
	}


	public ResolvedMember[] getDeclaredFields() {
		if (declaredFields == null) fillDeclaredMembers();
		return declaredFields;
	}

	public ResolvedMember[] getDeclaredMethods() {
		if (declaredMethods == null) fillDeclaredMembers();
		return declaredMethods;
	}

	public ResolvedMember[] getDeclaredPointcuts() {
		if (declaredPointcuts == null) fillDeclaredMembers();
		return declaredPointcuts;
	}

	
	public int getModifiers() {
		// only return the real Java modifiers, not the extra eclipse ones
		return binding.modifiers & CompilerModifiers.AccJustFlag;
	}
	
	public String toString() {
		return "EclipseSourceType(" + new String(binding.sourceName()) + ")";
	}


	//XXX make sure this is applied to classes and interfaces
	public void checkPointcutDeclarations() {
		ResolvedMember[] pointcuts = getDeclaredPointcuts();
		boolean sawError = false;
		for (int i=0, len=pointcuts.length; i < len; i++) {
			if (pointcuts[i].isAbstract()) {
				if (!this.isAspect()) {
					eclipseWorld().showMessage(IMessage.ERROR,
						"abstract pointcut only allowed in aspect" + pointcuts[i].getName(),
						pointcuts[i].getSourceLocation(), null);
					sawError = true;
				} else if (!binding.isAbstract()) {
					eclipseWorld().showMessage(IMessage.ERROR,
						"abstract pointcut in concrete aspect" + pointcuts[i],
						pointcuts[i].getSourceLocation(), null);
					sawError = true;
				}
			}
				
			for (int j=i+1; j < len; j++) {
				if (pointcuts[i].getName().equals(pointcuts[j].getName())) {
					eclipseWorld().showMessage(IMessage.ERROR,
						"duplicate pointcut name: " + pointcuts[j].getName(),
						pointcuts[i].getSourceLocation(), pointcuts[j].getSourceLocation());
					sawError = true;
				}
			}
		}
		
		//now check all inherited pointcuts to be sure that they're handled reasonably
		if (sawError || !isAspect()) return;
		

		
		// find all pointcuts that override ones from super and check override is legal
		//    i.e. same signatures and greater or equal visibility
		// find all inherited abstract pointcuts and make sure they're concretized if I'm concrete
		// find all inherited pointcuts and make sure they don't conflict
		getResolvedTypeX().getExposedPointcuts();  //??? this is an odd construction

	}
	
	//???
//	public CrosscuttingMembers collectCrosscuttingMembers() {
//		return crosscuttingMembers;
//	}

//	public ISourceLocation getSourceLocation() {
//		TypeDeclaration dec = binding.scope.referenceContext;
//		return new EclipseSourceLocation(dec.compilationResult, dec.sourceStart, dec.sourceEnd);
//	}

	public boolean isInterface() {
		return binding.isInterface();
	}

	// XXXAJ5: Should be constants in the eclipse compiler somewhere, once it supports 1.5
	public final static short ACC_ANNOTATION   = 0x2000;
	public final static short ACC_ENUM         = 0x4000;
	
	public boolean isEnum() {
		return (binding.getAccessFlags() & ACC_ENUM)!=0;
	}
	
	public boolean isAnnotation() {
		return (binding.getAccessFlags() & ACC_ANNOTATION)!=0;
	}
	
	public boolean hasAnnotation(ResolvedTypeX ofType) {
		throw new RuntimeException("How to implement this?  Needs to ask eclipse!");
	}
	
	public ResolvedTypeX[] getAnnotationTypes() {
		throw new RuntimeException("How to implement this?  Needs to ask eclipse!");
	}

	public PerClause getPerClause() {
		//should probably be: ((AspectDeclaration)declaration).perClause;
		// but we don't need this level of detail, and working with real per clauses
		// at this stage of compilation is not worth the trouble
		return new PerSingleton(); 
	}
	
	protected Collection getDeclares() {
		return declares;
	}

	protected Collection getPrivilegedAccesses() {
		return Collections.EMPTY_LIST;
	}

	protected Collection getTypeMungers() {
		return typeMungers;
	}

	public boolean doesNotExposeShadowMungers() {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8065.java