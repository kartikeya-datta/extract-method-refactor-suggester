error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5718.java
text:
```scala
i@@f (previousValueKind != -1 && memberValuePair.valueKind != previousValueKind) {

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.HashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Literal;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;
import org.eclipse.jdt.internal.core.util.Util;


public class LocalVariable extends SourceRefElement implements ILocalVariable {

	String name;
	public int declarationSourceStart, declarationSourceEnd;
	public int nameStart, nameEnd;
	String typeSignature;
	public IAnnotation[] annotations;
	
	public LocalVariable(
			JavaElement parent, 
			String name, 
			int declarationSourceStart, 
			int declarationSourceEnd,
			int nameStart, 
			int nameEnd,
			String typeSignature,
			org.eclipse.jdt.internal.compiler.ast.Annotation[] astAnnotations) {
		
		super(parent);
		this.name = name;
		this.declarationSourceStart = declarationSourceStart;
		this.declarationSourceEnd = declarationSourceEnd;
		this.nameStart = nameStart;
		this.nameEnd = nameEnd;
		this.typeSignature = typeSignature;
		this.annotations = getAnnotations(astAnnotations);
	}

	protected void closing(Object info) {
		// a local variable has no info
	}

	protected Object createElementInfo() {
		// a local variable has no info
		return null;
	}

	public boolean equals(Object o) {
		if (!(o instanceof LocalVariable)) return false;
		LocalVariable other = (LocalVariable)o;
		return 
			this.declarationSourceStart == other.declarationSourceStart 
			&& this.declarationSourceEnd == other.declarationSourceEnd
			&& this.nameStart == other.nameStart
			&& this.nameEnd == other.nameEnd
			&& super.equals(o);
	}
	
	public boolean exists() {
		return this.parent.exists(); // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=46192
	}

	protected void generateInfos(Object info, HashMap newElements, IProgressMonitor pm) {
		// a local variable has no info
	}
	
	public IAnnotation getAnnotation(String annotationName) {
		for (int i = 0, length = this.annotations.length; i < length; i++) {
			IAnnotation annotation = this.annotations[i];
			if (annotation.getElementName().equals(annotationName))
				return annotation;
		}
		return super.getAnnotation(annotationName);
	}

	public IAnnotation[] getAnnotations() throws JavaModelException {
		return this.annotations;
	}
	
	private IAnnotation[] getAnnotations(org.eclipse.jdt.internal.compiler.ast.Annotation[] astAnnotations) {
		int length;
		if (astAnnotations == null || (length = astAnnotations.length) == 0)
			return Annotation.NO_ANNOTATIONS;
		IAnnotation[] result = new IAnnotation[length];
		for (int i = 0; i < length; i++) {
			result[i] = getAnnotation(astAnnotations[i], this);
		}
		return result;
	}
	
	private IAnnotation getAnnotation(final org.eclipse.jdt.internal.compiler.ast.Annotation annotation, JavaElement parentElement) {
		class LocalVarAnnotation extends Annotation {
			IMemberValuePair[] memberValuePairs;
			public LocalVarAnnotation(JavaElement localVar, String elementName) {
				super(localVar, elementName);
			}
			public IMemberValuePair[] getMemberValuePairs() throws JavaModelException {
				return this.memberValuePairs;
			}
			public boolean exists() {
				return this.parent.exists();
			}
		}
		String annotationName = new String(CharOperation.concatWith(annotation.type.getTypeName(), '.'));
		LocalVarAnnotation localVarAnnotation = new LocalVarAnnotation(parentElement, annotationName);
		org.eclipse.jdt.internal.compiler.ast.MemberValuePair[] astMemberValuePairs = annotation.memberValuePairs();
		int length;
		IMemberValuePair[] memberValuePairs;
		if (astMemberValuePairs == null || (length = astMemberValuePairs.length) == 0) {
			memberValuePairs = Annotation.NO_MEMBER_VALUE_PAIRS;
		} else {
			memberValuePairs = new IMemberValuePair[length];
			for (int i = 0; i < length; i++) {
				org.eclipse.jdt.internal.compiler.ast.MemberValuePair astMemberValuePair = astMemberValuePairs[i];
				MemberValuePair memberValuePair = new MemberValuePair(new String(astMemberValuePair.name));
				memberValuePair.value = getAnnotationMemberValue(memberValuePair, astMemberValuePair.value, localVarAnnotation);
				memberValuePairs[i] = memberValuePair;
			}
		}
		localVarAnnotation.memberValuePairs = memberValuePairs;
		return localVarAnnotation;
	}
	
	/*
	 * Creates the value wrapper from the given expression, and sets the valueKind on the given memberValuePair
	 */
	private Object getAnnotationMemberValue(MemberValuePair memberValuePair, Expression expression, JavaElement parentElement) {
		if (expression instanceof Literal) {
			((Literal) expression).computeConstant();
			return Util.getAnnotationMemberValue(memberValuePair, expression.constant);
		} else if (expression instanceof org.eclipse.jdt.internal.compiler.ast.Annotation) {
			memberValuePair.valueKind = IMemberValuePair.K_ANNOTATION;
			return getAnnotation((org.eclipse.jdt.internal.compiler.ast.Annotation) expression, parentElement);
		} else if (expression instanceof ClassLiteralAccess) {
			ClassLiteralAccess classLiteral = (ClassLiteralAccess) expression;
			char[] typeName = CharOperation.concatWith(classLiteral.type.getTypeName(), '.');
			memberValuePair.valueKind = IMemberValuePair.K_CLASS;
			return new String(typeName);
		} else if (expression instanceof QualifiedNameReference) {
			char[] qualifiedName = CharOperation.concatWith(((QualifiedNameReference) expression).tokens, '.');
			memberValuePair.valueKind = IMemberValuePair.K_QUALIFIED_NAME;
			return new String(qualifiedName);		
		} else if (expression instanceof ArrayInitializer) {
			memberValuePair.valueKind = -1; // modified below by the first call to getMemberValue(...)
			Expression[] expressions = ((ArrayInitializer) expression).expressions;
			int length = expressions == null ? 0 : expressions.length;
			Object[] values = new Object[length];
			for (int i = 0; i < length; i++) {
				int previousValueKind = memberValuePair.valueKind;
				Object value = getAnnotationMemberValue(memberValuePair, expressions[i], parentElement);
				if (previousValueKind != IMemberValuePair.K_UNKNOWN && memberValuePair.valueKind != previousValueKind) {
					// values are heterogeneous, value kind is thus unknown
					memberValuePair.valueKind = IMemberValuePair.K_UNKNOWN;
				}
				values[i] = value;
			}
			if (memberValuePair.valueKind == -1)
				memberValuePair.valueKind = IMemberValuePair.K_UNKNOWN;
			return values;
		} else {
			memberValuePair.valueKind = IMemberValuePair.K_UNKNOWN;
			return null;
		}
	}

	public IJavaElement getHandleFromMemento(String token, MementoTokenizer memento, WorkingCopyOwner owner) {
		switch (token.charAt(0)) {
			case JEM_COUNT:
				return getHandleUpdatingCountFromMemento(memento, owner);
		}
		return this;
	}

	/*
	 * @see JavaElement#getHandleMemento(StringBuffer)
	 */
	protected void getHandleMemento(StringBuffer buff) {
		((JavaElement)getParent()).getHandleMemento(buff);
		buff.append(getHandleMementoDelimiter());
		buff.append(this.name);
		buff.append(JEM_COUNT);
		buff.append(this.declarationSourceStart);
		buff.append(JEM_COUNT);
		buff.append(this.declarationSourceEnd);
		buff.append(JEM_COUNT);
		buff.append(this.nameStart);
		buff.append(JEM_COUNT);
		buff.append(this.nameEnd);
		buff.append(JEM_COUNT);
		buff.append(this.typeSignature);
		if (this.occurrenceCount > 1) {
			buff.append(JEM_COUNT);
			buff.append(this.occurrenceCount);
		}
	}

	protected char getHandleMementoDelimiter() {
		return JavaElement.JEM_LOCALVARIABLE;
	}

	public IResource getCorrespondingResource() {
		return null;
	}
	
	public String getElementName() {
		return this.name;
	}

	public int getElementType() {
		return LOCAL_VARIABLE;
	}

	public ISourceRange getNameRange() {
		return new SourceRange(this.nameStart, this.nameEnd-this.nameStart+1);
	}
	
	public IPath getPath() {
		return this.parent.getPath();
	}

	public IResource getResource() {
		return this.parent.getResource();
	}

	/**
	 * @see ISourceReference
	 */
	public String getSource() throws JavaModelException {
		IOpenable openable = this.parent.getOpenableParent();
		IBuffer buffer = openable.getBuffer();
		if (buffer == null) {
			return null;
		}
		ISourceRange range = getSourceRange();
		int offset = range.getOffset();
		int length = range.getLength();
		if (offset == -1 || length == 0 ) {
			return null;
		}
		try {
			return buffer.getText(offset, length);
		} catch(RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * @see ISourceReference
	 */
	public ISourceRange getSourceRange() {
		return new SourceRange(this.declarationSourceStart, this.declarationSourceEnd-this.declarationSourceStart+1);
	}
	
	public String getTypeSignature() {
		return this.typeSignature;
	}

	public IResource getUnderlyingResource() throws JavaModelException {
		return this.parent.getUnderlyingResource();
	}

	public int hashCode() {
		return Util.combineHashCodes(this.parent.hashCode(), this.nameStart);
	}
	
	public boolean isStructureKnown() throws JavaModelException {
        return true;
    }
	
	protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
		buffer.append(this.tabString(tab));
		if (info != NO_INFO) {
			buffer.append(Signature.toString(this.getTypeSignature()));
			buffer.append(" "); //$NON-NLS-1$
		}
		toStringName(buffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5718.java