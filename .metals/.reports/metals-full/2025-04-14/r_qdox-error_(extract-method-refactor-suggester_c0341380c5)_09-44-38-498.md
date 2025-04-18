error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16891.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16891.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16891.java
text:
```scala
A@@tAspectJAnnotationFactory.addAnnotation(this, adviceAnnotation,this.scope);

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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.ajdt.internal.compiler.lookup.AjTypeConstants;
import org.aspectj.ajdt.internal.compiler.lookup.EclipseFactory;
import org.aspectj.ajdt.internal.compiler.lookup.PrivilegedHandler;
import org.aspectj.bridge.context.CompilationAndWeavingContext;
import org.aspectj.bridge.context.ContextToken;
import org.aspectj.org.eclipse.jdt.core.compiler.CharOperation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ClassFile;
import org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Argument;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.UnresolvedType;

/**
 * Represents before, after and around advice in an aspect.
 * Will generate a method corresponding to the body of the advice with an
 * attribute including additional information.
 * 
 * @author Jim Hugunin
 */
public class AdviceDeclaration extends AjMethodDeclaration {
	public PointcutDesignator pointcutDesignator;  // set during parsing
	int baseArgumentCount;                               // referenced by IfPseudoToken.makeArguments
	
	public Argument extraArgument;                    // set during parsing, referenced by Proceed
	
	public AdviceKind kind;									// set during parsing, referenced by Proceed and AsmElementFormatter
	private int extraArgumentFlags = 0;
	
	public MethodBinding proceedMethodBinding;   // set during this.resolveStaments, referenced by Proceed
	public List proceedCalls = new ArrayList(2);    // populated during Proceed.findEnclosingAround
	
	private boolean proceedInInners;
	private ResolvedMember[] proceedCallSignatures;
	private boolean[] formalsUnchangedToProceed;
	private UnresolvedType[] declaredExceptions;
	
	
	public AdviceDeclaration(CompilationResult result, AdviceKind kind) {
		super(result);
		this.returnType = TypeReference.baseTypeReference(T_void, 0);
		this.kind = kind;
	}

	// override
	protected int generateInfoAttributes(ClassFile classFile) {
		List l = new ArrayList(1);
		l.add(new EclipseAttributeAdapter(makeAttribute()));
		addDeclarationStartLineAttribute(l,classFile);

		return classFile.generateMethodInfoAttribute(binding, false, l);
	}
	
	private AjAttribute makeAttribute() {
		if (kind == AdviceKind.Around) {
			return new AjAttribute.AdviceAttribute(kind, pointcutDesignator.getPointcut(), 
					extraArgumentFlags, sourceStart, sourceEnd, null,
					proceedInInners, proceedCallSignatures, formalsUnchangedToProceed, 
					declaredExceptions);
		} else {
			return new AjAttribute.AdviceAttribute(kind, pointcutDesignator.getPointcut(), 
					extraArgumentFlags, sourceStart, sourceEnd, null);
		}
	}

	// override
	public void resolveStatements() {
		if (binding == null || ignoreFurtherInvestigation) return;
		
		ClassScope upperScope = (ClassScope)scope.parent;  //!!! safety
		
		modifiers = checkAndSetModifiers(modifiers, upperScope);
		int bindingModifiers = (modifiers | (binding.modifiers & AccGenericSignature));
		binding.modifiers = bindingModifiers;
		
		if (kind == AdviceKind.AfterThrowing && extraArgument != null) {
			TypeBinding argTb = extraArgument.binding.type;
			TypeBinding expectedTb = upperScope.getJavaLangThrowable();
			if (!argTb.isCompatibleWith(expectedTb)) {
				scope.problemReporter().typeMismatchError(argTb, expectedTb, extraArgument);
				ignoreFurtherInvestigation = true;
				return;
			}
		}
		
		
		pointcutDesignator.finishResolveTypes(this, this.binding, 
			baseArgumentCount, upperScope.referenceContext.binding);
		
		if (binding == null || ignoreFurtherInvestigation) return;
		
		if (kind == AdviceKind.Around) {
			ReferenceBinding[] exceptions = 
				new ReferenceBinding[] { upperScope.getJavaLangThrowable() };
			proceedMethodBinding = new MethodBinding(Modifier.STATIC,
				"proceed".toCharArray(), binding.returnType,
				resize(baseArgumentCount+1, binding.parameters),
				exceptions, binding.declaringClass);
			proceedMethodBinding.selector =
				CharOperation.concat(selector, proceedMethodBinding.selector);
		}
		
		super.resolveStatements(); //upperScope);
		if (binding != null) determineExtraArgumentFlags();
		
		if (kind == AdviceKind.Around) {
			int n = proceedCalls.size();
//			EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(upperScope);
						
			//System.err.println("access to: " + Arrays.asList(handler.getMembers()));
			
			//XXX set these correctly
			formalsUnchangedToProceed = new boolean[baseArgumentCount];
			proceedCallSignatures = new ResolvedMember[0];
			proceedInInners = false;
			declaredExceptions = new UnresolvedType[0];
			
			for (int i=0; i < n; i++) {
				Proceed call = (Proceed)proceedCalls.get(i);
				if (call.inInner) {
					//System.err.println("proceed in inner: " + call);
					proceedInInners = true;
					//XXX wrong
					//proceedCallSignatures[i] = world.makeResolvedMember(call.binding);
				}
			}
			
			// ??? should reorganize into AspectDeclaration
			// if we have proceed in inners we won't ever be inlined so the code below is unneeded
			if (!proceedInInners) {
				PrivilegedHandler handler = (PrivilegedHandler)upperScope.referenceContext.binding.privilegedHandler;
				if (handler == null) {
					handler = new PrivilegedHandler((AspectDeclaration)upperScope.referenceContext);
					//upperScope.referenceContext.binding.privilegedHandler = handler;
				}
				
				this.traverse(new MakeDeclsPublicVisitor(), (ClassScope)null);
				
				AccessForInlineVisitor v = new AccessForInlineVisitor((AspectDeclaration)upperScope.referenceContext, handler);
				ContextToken tok = CompilationAndWeavingContext.enteringPhase(CompilationAndWeavingContext.ACCESS_FOR_INLINE, selector);
				this.traverse(v, (ClassScope) null);
				CompilationAndWeavingContext.leavingPhase(tok);
				
				// ??? if we found a construct that we can't inline, set
				//     proceedInInners so that we won't try to inline this body
				if (!v.isInlinable) proceedInInners = true;
			}
		}
	}

     // called by Proceed.resolveType
	public int getDeclaredParameterCount() {
		// this only works before code generation
		return this.arguments.length - 3 - ((extraArgument == null) ? 0 : 1);
		//Advice.countOnes(extraArgumentFlags);
	}

	private void generateProceedMethod(ClassScope classScope, ClassFile classFile) {
		MethodBinding binding = (MethodBinding)proceedMethodBinding;
		
		classFile.generateMethodInfoHeader(binding);
		int methodAttributeOffset = classFile.contentsOffset;
		int attributeNumber = classFile.generateMethodInfoAttribute(binding, false, AstUtil.getAjSyntheticAttribute());
		int codeAttributeOffset = classFile.contentsOffset;
		classFile.generateCodeAttributeHeader();
		CodeStream codeStream = classFile.codeStream;
		codeStream.reset(this, classFile);
		
		// push the closure
		int nargs = binding.parameters.length;
		int closureIndex = 0;
		for (int i=0; i < nargs-1; i++) {
			closureIndex += AstUtil.slotsNeeded(binding.parameters[i]);
		}
		
		
		codeStream.loadObject(closureIndex);
		
		// build the Object[]

		codeStream.generateInlinedValue(nargs-1);
		codeStream.newArray(
				new ArrayBinding(
						classScope.getType(TypeBinding.JAVA_LANG_OBJECT, 
								TypeBinding.JAVA_LANG_OBJECT.length), 
								1,
								classScope.environment()));
		
		int index = 0;
		for (int i=0; i < nargs-1; i++) {
			TypeBinding type = binding.parameters[i];
			codeStream.dup();
			codeStream.generateInlinedValue(i);
			codeStream.load(type, index);
			index += AstUtil.slotsNeeded(type);
			if (type.isBaseType()) {
				codeStream.invokestatic(AjTypeConstants.getConversionMethodToObject(classScope, type));
			}
			
			codeStream.aastore();
		}
		
		// call run
		ReferenceBinding closureType = (ReferenceBinding)binding.parameters[nargs-1];
		MethodBinding runMethod = closureType.getMethods("run".toCharArray())[0];
		codeStream.invokevirtual(runMethod);

		TypeBinding returnType = binding.returnType;
		if (returnType.isBaseType()) {
			codeStream.invokestatic(AjTypeConstants.getConversionMethodFromObject(classScope, returnType));
		} else {
			codeStream.checkcast(returnType);
		}
		AstUtil.generateReturn(returnType, codeStream);
		
		classFile.completeCodeAttribute(codeAttributeOffset);
		attributeNumber++;
		classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);
	}


	// override
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		if (ignoreFurtherInvestigation) return;
		
		super.generateCode(classScope, classFile);
		if (proceedMethodBinding != null) {
			generateProceedMethod(classScope, classFile);
		}
	}


	private void determineExtraArgumentFlags() {
		if (extraArgument != null) extraArgumentFlags |= Advice.ExtraArgument;
		
		ThisJoinPointVisitor tjp = new ThisJoinPointVisitor(this);
		extraArgumentFlags |= tjp.removeUnusedExtraArguments();
	}
	
	private static TypeBinding[] resize(int newSize, TypeBinding[] bindings) {
		int len = bindings.length;
		TypeBinding[] ret = new TypeBinding[newSize];
		System.arraycopy(bindings, 0, ret, 0, Math.min(newSize, len));
		return ret;
	}

	/**
	 * Add either the @Before, @After, @Around, @AfterReturning or @AfterThrowing annotation
	 */
	public void addAtAspectJAnnotations() {
		Annotation adviceAnnotation = null;
		String pointcutExpression = pointcutDesignator.getPointcut().toString();
		String extraArgumentName = "";
		if (extraArgument != null) {
			extraArgumentName = new String(extraArgument.name);
		}
		
		if (kind == AdviceKind.Before) {
			adviceAnnotation = AtAspectJAnnotationFactory.createBeforeAnnotation(pointcutExpression,declarationSourceStart);
		} else if (kind == AdviceKind.After) {
			adviceAnnotation = AtAspectJAnnotationFactory.createAfterAnnotation(pointcutExpression,declarationSourceStart);			
		} else if (kind == AdviceKind.AfterReturning) {
			adviceAnnotation = AtAspectJAnnotationFactory.createAfterReturningAnnotation(pointcutExpression,extraArgumentName,declarationSourceStart);
		} else if (kind == AdviceKind.AfterThrowing) {
			adviceAnnotation = AtAspectJAnnotationFactory.createAfterThrowingAnnotation(pointcutExpression,extraArgumentName,declarationSourceStart);
		} else if (kind == AdviceKind.Around) {
			adviceAnnotation = AtAspectJAnnotationFactory.createAroundAnnotation(pointcutExpression,declarationSourceStart);
		}
		AtAspectJAnnotationFactory.addAnnotation(this, adviceAnnotation);
	}
	
	// override, Called by ClassScope.postParse
	public void postParse(TypeDeclaration typeDec) {
		AspectDeclaration aspectDecl = (AspectDeclaration)typeDec;
		int adviceSequenceNumberInType = aspectDecl.adviceCounter++;
		
		StringBuffer stringifiedPointcut = new StringBuffer(30);
		pointcutDesignator.print(0,stringifiedPointcut);
		this.selector =
			NameMangler.adviceName(
			  EclipseFactory.getName(typeDec.binding).replace('.', '_'),
			  kind, 
			  adviceSequenceNumberInType,
			  stringifiedPointcut.toString().hashCode()).toCharArray();
		if (arguments != null) {
			baseArgumentCount = arguments.length;
		}
		
		if (kind == AdviceKind.Around) {
			extraArgument = makeFinalArgument("ajc_aroundClosure",
					AjTypeConstants.getAroundClosureType());
		}
		
		int addedArguments = 3;
		if (extraArgument != null) {
			addedArguments += 1;
		}
		
		arguments = extendArgumentsLength(arguments, addedArguments);
		
		int index = baseArgumentCount;
		if (extraArgument != null) {
			arguments[index++] = extraArgument;
		}
		
		arguments[index++] = makeFinalArgument("thisJoinPointStaticPart", AjTypeConstants.getJoinPointStaticPartType());
		arguments[index++] = makeFinalArgument("thisJoinPoint", AjTypeConstants.getJoinPointType());
		arguments[index++] = makeFinalArgument("thisEnclosingJoinPointStaticPart", AjTypeConstants.getJoinPointStaticPartType());
		
		if (pointcutDesignator.isError()) {
			this.ignoreFurtherInvestigation = true;
		}
		pointcutDesignator.postParse(typeDec, this);
	}

	private int checkAndSetModifiers(int modifiers, ClassScope scope) {
		if (modifiers == 0) return Modifier.PUBLIC;
		else if (modifiers == Modifier.STRICT) return Modifier.PUBLIC | Modifier.STRICT;
		else {
			tagAsHavingErrors();
			scope.problemReporter().signalError(declarationSourceStart, sourceStart-1, "illegal modifier on advice, only strictfp is allowed");
			return Modifier.PUBLIC;
		}
	}

	// called by IfPseudoToken
	public static Argument[]  addTjpArguments(Argument[] arguments) {
		int index = arguments.length;
		arguments = extendArgumentsLength(arguments, 3);
		
		arguments[index++] = makeFinalArgument("thisJoinPointStaticPart", AjTypeConstants.getJoinPointStaticPartType());
		arguments[index++] = makeFinalArgument("thisJoinPoint", AjTypeConstants.getJoinPointType());
		arguments[index++] = makeFinalArgument("thisEnclosingJoinPointStaticPart", AjTypeConstants.getJoinPointStaticPartType());
	
		return arguments;
	}
	
	

	private static Argument makeFinalArgument(String name, TypeReference typeRef) {
		long pos = 0; //XXX encode start and end location
		return new Argument(name.toCharArray(), pos, typeRef, Modifier.FINAL);
	}


	private static Argument[] extendArgumentsLength(Argument[] args, int addedArguments) {
		if (args == null) {
			return new Argument[addedArguments];
		}
		int len = args.length;
		Argument[] ret = new Argument[len + addedArguments];
		System.arraycopy(args, 0, ret, 0, len);
		return ret;
	}

	
//	public String toString(int tab) {
//		String s = tabString(tab);
//		if (modifiers != AccDefault) {
//			s += modifiersString(modifiers);
//		}
//
//		if (kind == AdviceKind.Around) {
//			s += returnTypeToString(0);
//		}
//
//		s += new String(selector) + "("; //$NON-NLS-1$
//		if (arguments != null) {
//			for (int i = 0; i < arguments.length; i++) {
//				s += arguments[i].toString(0);
//				if (i != (arguments.length - 1))
//					s = s + ", "; //$NON-NLS-1$
//			};
//		};
//		s += ")"; //$NON-NLS-1$
//		
//		if (extraArgument != null) {
//			s += "(" + extraArgument.toString(0) + ")";
//		}
//		
//		
//		
//		if (thrownExceptions != null) {
//			s += " throws "; //$NON-NLS-1$
//			for (int i = 0; i < thrownExceptions.length; i++) {
//				s += thrownExceptions[i].toString(0);
//				if (i != (thrownExceptions.length - 1))
//					s = s + ", "; //$NON-NLS-1$
//			};
//		};
//		
//		s += ": ";
//		if (pointcutDesignator != null) {
//			s += pointcutDesignator.toString(0);
//		}
//
//		s += toStringStatements(tab + 1);
//		return s;
//	}

	public StringBuffer printBody(int indent, StringBuffer output) {
		output.append(": ");
		if (pointcutDesignator != null) {
			output.append(pointcutDesignator.toString());
		}
		return super.printBody(indent,output);
	}

	public StringBuffer printReturnType(int indent, StringBuffer output) {
		if (this.kind == AdviceKind.Around) {
			return super.printReturnType(indent,output);
		}
		return output;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16891.java