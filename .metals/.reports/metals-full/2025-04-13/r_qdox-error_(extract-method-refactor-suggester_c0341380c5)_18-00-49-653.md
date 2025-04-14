error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15901.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15901.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15901.java
text:
```scala
(@@(InterTypeDeclaration)interTypeDecl).setOnType(onType);

/*******************************************************************************
 * Copyright (c) 2002,2003 Palo Alto Research Center, Incorporated (PARC).
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     PARC initial implementation 
 *     IBM Corporation 
 *******************************************************************************/
package org.aspectj.ajdt.internal.compiler.parser;

import org.aspectj.ajdt.internal.compiler.ast.AdviceDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AjConstructorDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AjMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.AspectDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.DeclareAnnotationDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.DeclareDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.IfPseudoToken;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeConstructorDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeFieldDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.InterTypeMethodDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.PointcutDeclaration;
import org.aspectj.ajdt.internal.compiler.ast.PointcutDesignator;
import org.aspectj.ajdt.internal.compiler.ast.Proceed;
import org.aspectj.ajdt.internal.compiler.ast.PseudoToken;
import org.aspectj.ajdt.internal.compiler.ast.PseudoTokens;
import org.aspectj.ajdt.internal.core.builder.EclipseSourceContext;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.DeclareAnnotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Argument;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.Expression;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.aspectj.org.eclipse.jdt.internal.compiler.parser.Parser;
import org.aspectj.org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory;

/**
 * @author colyer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DeclarationFactory implements IDeclarationFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createMethodDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createMethodDeclaration(CompilationResult result) {
		return new AjMethodDeclaration(result);
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createConstructorDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public ConstructorDeclaration createConstructorDeclaration(CompilationResult result) {
		return new AjConstructorDeclaration(result);
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createProceed(org.eclipse.jdt.internal.compiler.ast.MessageSend)
	 */
	public MessageSend createProceed(MessageSend m) {
		return new Proceed(m);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createAspect(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public TypeDeclaration createAspect(CompilationResult result) {
		return new AspectDeclaration(result);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setPrivileged(org.eclipse.jdt.internal.compiler.ast.TypeDeclaration, boolean)
	 */
	public void setPrivileged(TypeDeclaration aspectDecl, boolean isPrivileged) {
		((AspectDeclaration)aspectDecl).isPrivileged = isPrivileged;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setPerClauseFrom(org.eclipse.jdt.internal.compiler.ast.TypeDeclaration, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public void setPerClauseFrom(TypeDeclaration aspectDecl, ASTNode pseudoTokens, Parser parser) {
		AspectDeclaration aspect = (AspectDeclaration) aspectDecl;
		PseudoTokens tok = (PseudoTokens) pseudoTokens;
		aspect.perClause = tok.parsePerClause(parser);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setDominatesPatternFrom(org.eclipse.jdt.internal.compiler.ast.TypeDeclaration, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public void setDominatesPatternFrom(TypeDeclaration aspectDecl, ASTNode pseudoTokens, Parser parser) {
		AspectDeclaration aspect = (AspectDeclaration) aspectDecl;
		PseudoTokens tok = (PseudoTokens) pseudoTokens;
		aspect.dominatesPattern = tok.maybeParseDominatesPattern(parser);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createPseudoTokensFrom(org.eclipse.jdt.internal.compiler.ast.ASTNode[], org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public ASTNode createPseudoTokensFrom(ASTNode[] tokens, CompilationResult result) {
		PseudoToken[] psts = new PseudoToken[tokens.length];
		for (int i = 0; i < psts.length; i++) {
			psts[i] = (PseudoToken)tokens[i];
		}
		return new PseudoTokens(psts,new EclipseSourceContext(result));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createPointcutDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createPointcutDeclaration(CompilationResult result) {
		return new PointcutDeclaration(result);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createAroundAdviceDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createAroundAdviceDeclaration(CompilationResult result) {
		return new AdviceDeclaration(result,AdviceKind.Around);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createAfterAdviceDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createAfterAdviceDeclaration(CompilationResult result) {
		return new AdviceDeclaration(result,AdviceKind.After);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createBeforeAdviceDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createBeforeAdviceDeclaration(CompilationResult result) {
		return new AdviceDeclaration(result,AdviceKind.Before);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createPointcutDesignator(org.eclipse.jdt.internal.compiler.parser.Parser, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public ASTNode createPointcutDesignator(Parser parser, ASTNode pseudoTokens) {
		return new PointcutDesignator(parser,(PseudoTokens)pseudoTokens);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setPointcutDesignator(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public void setPointcutDesignatorOnAdvice(MethodDeclaration adviceDecl, ASTNode des) {
		((AdviceDeclaration)adviceDecl).pointcutDesignator = (PointcutDesignator)des;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setPointcutDesignator(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public void setPointcutDesignatorOnPointcut(MethodDeclaration pcutDecl, ASTNode des) {
		((PointcutDeclaration)pcutDecl).pointcutDesignator = (PointcutDesignator)des;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setExtraArgument(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, org.eclipse.jdt.internal.compiler.ast.Argument)
	 */
	public void setExtraArgument(MethodDeclaration adviceDeclaration, Argument arg) {
		((AdviceDeclaration)adviceDeclaration).extraArgument = arg;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#isAfterAdvice(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration)
	 */
	public boolean isAfterAdvice(MethodDeclaration adviceDecl) {		
		return ((AdviceDeclaration)adviceDecl).kind != AdviceKind.After;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setAfterThrowingAdviceKind(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration)
	 */
	public void setAfterThrowingAdviceKind(MethodDeclaration adviceDecl) {
		((AdviceDeclaration)adviceDecl).kind = AdviceKind.AfterThrowing;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setAfterReturningAdviceKind(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration)
	 */
	public void setAfterReturningAdviceKind(MethodDeclaration adviceDecl) {
		((AdviceDeclaration)adviceDecl).kind = AdviceKind.AfterReturning;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createDeclareDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult, org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public MethodDeclaration createDeclareDeclaration(CompilationResult result, ASTNode pseudoTokens, Parser parser) {
		Declare declare = ((PseudoTokens)pseudoTokens).parseDeclare(parser);
		return new DeclareDeclaration(result,declare); 
	}

	/* (non-Javadoc)
	 * @see org.aspectj.org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createDeclareAnnotationDeclaration(org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult, org.aspectj.org.eclipse.jdt.internal.compiler.ast.ASTNode, org.aspectj.org.eclipse.jdt.internal.compiler.ast.Annotation, org.aspectj.org.eclipse.jdt.internal.compiler.parser.Parser)
	 */
	public MethodDeclaration createDeclareAnnotationDeclaration(
			CompilationResult result, ASTNode pseudoTokens,
			Annotation annotation, Parser parser) {
		DeclareAnnotation declare = (DeclareAnnotation) ((PseudoTokens)pseudoTokens).parseAnnotationDeclare(parser);
		DeclareAnnotationDeclaration decl = new DeclareAnnotationDeclaration(result,declare,annotation);
		return decl;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createInterTypeFieldDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult, org.eclipse.jdt.internal.compiler.ast.TypeReference)
	 */
	public MethodDeclaration createInterTypeFieldDeclaration(CompilationResult result, TypeReference onType) {
		return new InterTypeFieldDeclaration(result,onType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createInterTypeMethodDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createInterTypeMethodDeclaration(CompilationResult result) {
		return new InterTypeMethodDeclaration(result,null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createInterTypeConstructorDeclaration(org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public MethodDeclaration createInterTypeConstructorDeclaration(CompilationResult result) {
		return new InterTypeConstructorDeclaration(result,null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setSelector(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, char[])
	 */
	public void setSelector(MethodDeclaration interTypeDecl, char[] selector) {
		((InterTypeDeclaration)interTypeDecl).setSelector(selector);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setDeclaredModifiers(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, int)
	 */
	public void setDeclaredModifiers(MethodDeclaration interTypeDecl, int modifiers) {
		((InterTypeDeclaration)interTypeDecl).setDeclaredModifiers(modifiers);	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setInitialization(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, org.eclipse.jdt.internal.compiler.ast.Expression)
	 */
	public void setInitialization(MethodDeclaration itdFieldDecl, Expression initialization) {
		((InterTypeFieldDeclaration)itdFieldDecl).setInitialization(initialization);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setOnType(org.eclipse.jdt.internal.compiler.ast.MethodDeclaration, org.eclipse.jdt.internal.compiler.ast.TypeReference)
	 */
	public void setOnType(MethodDeclaration interTypeDecl, TypeReference onType) {
		((InterTypeDeclaration)interTypeDecl).onType = onType;	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createPseudoToken(org.eclipse.jdt.internal.compiler.parser.Parser, java.lang.String, boolean)
	 */
	public ASTNode createPseudoToken(Parser parser, String value, boolean isIdentifier) {
		return new PseudoToken(parser,value,isIdentifier);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#createIfPseudoToken(org.eclipse.jdt.internal.compiler.parser.Parser, org.eclipse.jdt.internal.compiler.ast.Expression)
	 */
	public ASTNode createIfPseudoToken(Parser parser, Expression expr) {
		return new IfPseudoToken(parser,expr);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#setLiteralKind(org.eclipse.jdt.internal.compiler.ast.ASTNode, java.lang.String)
	 */
	public void setLiteralKind(ASTNode pseudoToken, String string) {
		((PseudoToken)pseudoToken).literalKind = string;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser.IDeclarationFactory#shouldTryToRecover(org.eclipse.jdt.internal.compiler.ast.ASTNode)
	 */
	public boolean shouldTryToRecover(ASTNode node) {
		return !(node instanceof AspectDeclaration || 
			 	 node instanceof PointcutDeclaration || 
				 node instanceof AdviceDeclaration) ;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15901.java