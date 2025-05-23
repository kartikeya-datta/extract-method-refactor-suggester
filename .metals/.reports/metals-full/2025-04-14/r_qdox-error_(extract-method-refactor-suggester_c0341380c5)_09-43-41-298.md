error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7254.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7254.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7254.java
text:
```scala
t@@abWidth = Integer.parseInt((String) options.get(DefaultCodeFormatterConstants.FORMATTER_INDENTATION_SIZE));

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
package org.eclipse.jdt.internal.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.jdt.internal.core.dom.rewrite.Indents;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;

/**
 * Implements functionality common to
 * operations that create type members.
 */
public abstract class CreateTypeMemberOperation extends CreateElementInCUOperation {
	/**
	 * The source code for the new member.
	 */
	protected String source = null;
	/**
	 * The name of the <code>ASTNode</code> that may be used to
	 * create this new element.
	 * Used by the <code>CopyElementsOperation</code> for renaming
	 */
	protected String alteredName;
	/**
	 * The AST node representing the element that
	 * this operation created.
	 */
	 protected ASTNode createdNode;
/**
 * When executed, this operation will create a type member
 * in the given parent element with the specified source.
 */
public CreateTypeMemberOperation(IJavaElement parentElement, String source, boolean force) {
	super(parentElement);
	this.source = source;
	this.force = force;
}
protected StructuralPropertyDescriptor getChildPropertyDescriptor(ASTNode parent) {
	switch (parent.getNodeType()) {
		case ASTNode.COMPILATION_UNIT:
			return CompilationUnit.TYPES_PROPERTY;
		default:
			return TypeDeclaration.BODY_DECLARATIONS_PROPERTY;
	}
}
protected ASTNode generateElementAST(ASTRewrite rewriter, IDocument document, ICompilationUnit cu) throws JavaModelException {
	if (this.createdNode == null) {
		this.source = removeIndentAndNewLines(this.source, document, cu);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(this.source.toCharArray());
		parser.setProject(getCompilationUnit().getJavaProject());
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		ASTNode node = parser.createAST(this.progressMonitor);
		String createdNodeSource;
		if (node.getNodeType() != ASTNode.TYPE_DECLARATION) {
			createdNodeSource = generateSyntaxIncorrectAST();
			if (this.createdNode == null)
				throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_CONTENTS));
		} else {
			TypeDeclaration typeDeclaration = (TypeDeclaration) node;
			this.createdNode = (ASTNode) typeDeclaration.bodyDeclarations().iterator().next();
			createdNodeSource = this.source;
		}
		if (this.alteredName != null) {
			SimpleName newName = this.createdNode.getAST().newSimpleName(this.alteredName);
			SimpleName oldName = rename(this.createdNode, newName);
			int nameStart = oldName.getStartPosition();
			int nameEnd = nameStart + oldName.getLength();
			StringBuffer newSource = new StringBuffer();
			if (this.source.equals(createdNodeSource)) {
				newSource.append(createdNodeSource.substring(0, nameStart));
				newSource.append(this.alteredName);
				newSource.append(createdNodeSource.substring(nameEnd));
			} else {
				// syntacticaly incorrect source
				int createdNodeStart = this.createdNode.getStartPosition();
				int createdNodeEnd = createdNodeStart + this.createdNode.getLength();
				newSource.append(createdNodeSource.substring(createdNodeStart, nameStart));
				newSource.append(this.alteredName);
				newSource.append(createdNodeSource.substring(nameEnd, createdNodeEnd));
				
			}
			this.source = newSource.toString();
		}
	}
	if (rewriter == null) return this.createdNode;
	// return a string place holder (instead of the created node) so has to not lose comments and formatting
	return rewriter.createStringPlaceholder(this.source, this.createdNode.getNodeType());
}
private String removeIndentAndNewLines(String code, IDocument document, ICompilationUnit cu) {
	IJavaProject project = cu.getJavaProject();
	Map options = project.getOptions(true/*inherit JavaCore options*/);
	int tabWidth;
	try {
		tabWidth = Integer.parseInt((String) options.get(DefaultCodeFormatterConstants.FORMATTER_TAB_LENGTH));
	} catch (NumberFormatException e) {
		tabWidth = 4;
	}
	int indent = Indents.computeIndent(code, tabWidth);
	int firstNonWhiteSpace = -1;
	int length = code.length();
	while (firstNonWhiteSpace < length-1)
		if (!Character.isWhitespace(code.charAt(++firstNonWhiteSpace)))
			break;
	int lastNonWhiteSpace = length;
	while (lastNonWhiteSpace > 0)
		if (!Character.isWhitespace(code.charAt(--lastNonWhiteSpace)))
			break;
	String lineDelimiter = TextUtilities.getDefaultLineDelimiter(document);
	return Indents.changeIndent(code.substring(firstNonWhiteSpace, lastNonWhiteSpace+1), indent, tabWidth, "", lineDelimiter); //$NON-NLS-1$
}
/*
 * Renames the given node to the given name.
 * Returns the old name.
 */
protected abstract SimpleName rename(ASTNode node, SimpleName newName);
/**
 * Generates an <code>ASTNode</code> based on the source of this operation
 * when there is likely a syntax error in the source.
 * Returns the source used to generate this node.
 */
protected String generateSyntaxIncorrectAST() {
	//create some dummy source to generate an ast node
	StringBuffer buff = new StringBuffer();
	buff.append(Util.LINE_SEPARATOR + " public class A {" + Util.LINE_SEPARATOR); //$NON-NLS-1$
	buff.append(this.source);
	buff.append(Util.LINE_SEPARATOR).append('}');
	ASTParser parser = ASTParser.newParser(AST.JLS3);
	parser.setSource(buff.toString().toCharArray());
	CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
	TypeDeclaration typeDeclaration = (TypeDeclaration) compilationUnit.types().iterator().next();
	List bodyDeclarations = typeDeclaration.bodyDeclarations();
	if (bodyDeclarations.size() != 0)
		this.createdNode = (ASTNode) bodyDeclarations.iterator().next();
	return buff.toString();
}
/**
 * Returns the IType the member is to be created in.
 */
protected IType getType() {
	return (IType)getParentElement();
}
/**
 * Sets the name of the <code>ASTNode</code> that will be used to
 * create this new element.
 * Used by the <code>CopyElementsOperation</code> for renaming
 */
protected void setAlteredName(String newName) {
	this.alteredName = newName;
}
/**
 * Possible failures: <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - the parent element supplied to the operation is
 * 		<code>null</code>.
 *	<li>INVALID_CONTENTS - The source is <code>null</code> or has serious syntax errors.
  *	<li>NAME_COLLISION - A name collision occurred in the destination
 * </ul>
 */
public IJavaModelStatus verify() {
	IJavaModelStatus status = super.verify();
	if (!status.isOK()) {
		return status;
	}
	if (this.source == null) {
		return new JavaModelStatus(IJavaModelStatusConstants.INVALID_CONTENTS);
	}
	if (!force) {
		//check for name collisions
		try {
			ICompilationUnit cu = getCompilationUnit();
			generateElementAST(null, getDocument(cu), cu);
		} catch (JavaModelException jme) {
			return jme.getJavaModelStatus();
		}
		return verifyNameCollision();
	}
	
	return JavaModelStatus.VERIFIED_OK;
}
/**
 * Verify for a name collision in the destination container.
 */
protected IJavaModelStatus verifyNameCollision() {
	return JavaModelStatus.VERIFIED_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7254.java