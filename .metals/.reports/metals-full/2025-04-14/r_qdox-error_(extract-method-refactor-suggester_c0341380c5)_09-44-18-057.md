error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17328.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17328.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17328.java
text:
```scala
r@@eturn WildTypePattern.splitNames(string,true);

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

import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.patterns.WildTypePattern;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Argument;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Expression;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Statement;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.aspectj.org.eclipse.jdt.internal.compiler.env.IConstants;
import org.aspectj.org.eclipse.jdt.internal.compiler.impl.Constant;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.BaseTypes;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.aspectj.org.eclipse.jdt.core.compiler.CharOperation;

public class AstUtil {

	private AstUtil() {}

	public static void addMethodBinding(SourceTypeBinding sourceType, MethodBinding method) {
		int len = sourceType.methods.length;
		MethodBinding[] temp = new MethodBinding[len + 1];
		System.arraycopy(sourceType.methods, 0, temp, 0, len);
		temp[len] = method;
		sourceType.methods = temp;
	}
	
	
	public static void addMethodDeclaration(TypeDeclaration typeDec, AbstractMethodDeclaration dec) {
		AbstractMethodDeclaration[] methods = typeDec.methods;
		int len = methods.length;
		AbstractMethodDeclaration[] newMethods = new AbstractMethodDeclaration[len+1];
		System.arraycopy(methods, 0, newMethods, 0, len);
		newMethods[len] = dec;
		typeDec.methods = newMethods;
	}
	
	
	public static Argument makeFinalArgument(char[] name, TypeBinding typeBinding) {
		long pos = 0; //XXX encode start and end location
		LocalVariableBinding binding =
			new LocalVariableBinding(name, typeBinding, Modifier.FINAL, true);
		Argument ret = new Argument(name, pos, makeTypeReference(typeBinding), Modifier.FINAL);
		ret.binding = binding;
		return ret;
	}
	
	public static TypeReference makeTypeReference(TypeBinding binding) {
		// ??? does this work for primitives	
		QualifiedTypeReference ref =
			new QualifiedTypeReference(new char[][] {binding.sourceName()}, new long[] {0}); //???
		ref.resolvedType = binding;
		ref.constant = Constant.NotAConstant;
		return ref;
	}
	
	
	public static NameReference makeNameReference(TypeBinding binding) {
		
			char[][] name = new char[][] {binding.sourceName()};
			long[] dummyPositions = new long[name.length];
			QualifiedNameReference ref = 
			new QualifiedNameReference(name, dummyPositions, 0, 0);
		ref.binding = binding;		ref.constant = Constant.NotAConstant;
		return ref;
	}
	
	

	
	
	public static ReturnStatement makeReturnStatement(Expression expr) {
		return new ReturnStatement(expr, 0, 0);
	}
	
	public static MethodDeclaration makeMethodDeclaration(
			MethodBinding binding)
	{
		MethodDeclaration ret = new MethodDeclaration(null);
		ret.binding = binding;
		int nargs = binding.parameters.length;
		ret.arguments = new Argument[nargs];
		for (int i=0; i < nargs; i++) {
			ret.arguments[i] = makeFinalArgument(("arg"+i).toCharArray(),
								binding.parameters[i]);
		}
		return ret;
	}
	
	public static void setStatements(
			MethodDeclaration ret, List statements)
	{
		ret.statements =
			(Statement[])statements.toArray(new Statement[statements.size()]);
	}
	
	public static SingleNameReference makeLocalVariableReference(
			LocalVariableBinding binding)
	{
		SingleNameReference ret = new SingleNameReference(binding.name, 0);
		ret.binding = binding;
		ret.codegenBinding = binding;
		ret.constant = ASTNode.NotAConstant;
		ret.bits &= ~ASTNode.RestrictiveFlagMASK;  // clear bits
		ret.bits |= Binding.VARIABLE; 
		return ret;
	}
	
	public static SingleNameReference makeResolvedLocalVariableReference(
			LocalVariableBinding binding)
	{
		SingleNameReference ret = new SingleNameReference(binding.name, 0);
		ret.binding = binding;
		ret.codegenBinding = binding;
		ret.constant = ASTNode.NotAConstant;
		ret.bits &= ~ASTNode.RestrictiveFlagMASK;  // clear bits
		ret.bits |= Binding.LOCAL; 
		return ret;
	}
	
	public static int makePublic(int modifiers) {
		return makePackageVisible(modifiers) | IConstants.AccPublic;
	}

	public static int makePackageVisible(int modifiers) {
		modifiers &= ~(IConstants.AccPublic | IConstants.AccPrivate | IConstants.AccProtected);
		return modifiers;
	}

	public static CompilationUnitScope getCompilationUnitScope(Scope scope) {
		if (scope instanceof CompilationUnitScope) {
			return (CompilationUnitScope)scope;
		}
		return getCompilationUnitScope(scope.parent);
	}
	
	
	public static void generateParameterLoads(TypeBinding[] parameters, CodeStream codeStream) {
		int paramIndex = 0;
		int varIndex = 0;
		while (paramIndex < parameters.length) {
			TypeBinding param = parameters[paramIndex++];
			codeStream.load(param, varIndex);
			varIndex += slotsNeeded(param);
		}
	}

	
	public static void generateReturn(TypeBinding returnType, CodeStream codeStream) {
		if (returnType.id == TypeIds.T_void) {
			codeStream.return_();
		} else if (returnType.isBaseType()) {
			switch (returnType.id) {
				case TypeBinding.T_boolean :
				case TypeBinding.T_int :
				case TypeBinding.T_byte :
				case TypeBinding.T_short :
				case TypeBinding.T_char :
					codeStream.ireturn();
					break;
				case TypeBinding.T_float :
					codeStream.freturn();
					break;
				case TypeBinding.T_long :
					codeStream.lreturn();
					break;
				case TypeBinding.T_double :
					codeStream.dreturn();
					break;
				default :
					throw new RuntimeException("huh");
			}
		} else {
			codeStream.areturn();
		}
	}
	
	//XXX this could be inconsistent for wierd case, i.e. a class named "java_lang_String"
	public static char[] makeMangledName(ReferenceBinding type) {
		return CharOperation.concatWith(type.compoundName, '_');
	}
	
	

	public static final char[] PREFIX = "ajc".toCharArray();

	//XXX not efficient
	public static char[] makeAjcMangledName(char[] kind, ReferenceBinding type, char[] name) {
		return CharOperation.concat(
			CharOperation.concat(PREFIX, new char[] {'$'}, kind), '$', makeMangledName(type), '$', name);
	}

	public static char[] makeAjcMangledName(char[] kind, char[] p, char[] name) {
		return CharOperation.concat(
			CharOperation.concat(PREFIX, new char[] {'$'}, kind), '$', p, '$', name);
	}

	public static List getAjSyntheticAttribute() {
		ArrayList ret = new ArrayList(1);
		ret.add(new EclipseAttributeAdapter(new AjAttribute.AjSynthetic()));
		return ret;
	}

	public static long makeLongPos(int start, int end) {
		return (long)end | ((long)start << 32);
	}
	public static char[][] getCompoundName(String string) {
		return WildTypePattern.splitNames(string);
	}

	public static TypeBinding[] insert(
		TypeBinding first,
		TypeBinding[] rest) {
		if (rest == null) {
			return new TypeBinding[] {first};
		}
		
		int len = rest.length;
		TypeBinding[] ret = new TypeBinding[len+1];
		ret[0] = first;
		System.arraycopy(rest, 0, ret, 1, len);
		return ret;
	}
	public static Argument[] insert(
		Argument first,
		Argument[] rest) {
		if (rest == null) {
			return new Argument[] {first};
		}
		
		int len = rest.length;
		Argument[] ret = new Argument[len+1];
		ret[0] = first;
		System.arraycopy(rest, 0, ret, 1, len);
		return ret;
	}
	public static Argument[] copyArguments(Argument[] inArgs) {
		if (inArgs == null) return new Argument[] {};
		int len = inArgs.length;
		Argument[] outArgs = new Argument[len];
		//??? we're not sure whether or not copying these is okay
		System.arraycopy(inArgs, 0, outArgs, 0, len);
		return outArgs;
	}

	public static Statement[] remove(int i, Statement[] statements) {
		int len = statements.length;
		Statement[] ret = new Statement[len-1];
		System.arraycopy(statements, 0, ret, 0, i);
		System.arraycopy(statements, i+1, ret, i, len-i-1);
		return ret;
	}
	public static int slotsNeeded(TypeBinding type) {
		if (type == BaseTypes.DoubleBinding || type == BaseTypes.LongBinding) return 2;
		else return 1;
	}
	
	public static void replaceMethodBinding(MessageSend send, MethodBinding newBinding) {
		send.binding = send.codegenBinding = newBinding;
		send.setActualReceiverType(newBinding.declaringClass);
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17328.java