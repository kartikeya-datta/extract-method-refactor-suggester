error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9376.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9376.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9376.java
text:
```scala
i@@f (fieldBinding.type != null && fieldBinding.type.isParameterizedType() && this.pattern.hasTypeArguments()) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.util.SimpleSet;

public class FieldLocator extends VariableLocator {

protected boolean isDeclarationOfAccessedFieldsPattern;

public FieldLocator(FieldPattern pattern) {
	super(pattern);

	this.isDeclarationOfAccessedFieldsPattern = this.pattern instanceof DeclarationOfAccessedFieldsPattern;
}
public int match(ASTNode node, MatchingNodeSet nodeSet) {
	int declarationsLevel = IMPOSSIBLE_MATCH;
	if (this.pattern.findReferences) {
		if (node instanceof ImportReference) {
			// With static import, we can have static field reference in import reference
			ImportReference importRef = (ImportReference) node;
			int length = importRef.tokens.length-1;
			if (importRef.isStatic() && !importRef.onDemand && matchesName(this.pattern.name, importRef.tokens[length])) {
				char[][] compoundName = new char[length][];
				System.arraycopy(importRef.tokens, 0, compoundName, 0, length);
				FieldPattern fieldPattern = (FieldPattern) this.pattern;
				char[] declaringType = CharOperation.concat(fieldPattern.declaringQualification, fieldPattern.declaringSimpleName, '.');
				if (matchesName(declaringType, CharOperation.concatWith(compoundName, '.'))) {
					declarationsLevel = ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
				}
			}
		}
	}
	return nodeSet.addMatch(node, declarationsLevel);
}
//public int match(ConstructorDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
public int match(FieldDeclaration node, MatchingNodeSet nodeSet) {
	int referencesLevel = IMPOSSIBLE_MATCH;
	if (this.pattern.findReferences)
		// must be a write only access with an initializer
		if (this.pattern.writeAccess && !this.pattern.readAccess && node.initialization != null)
			if (matchesName(this.pattern.name, node.name))
				referencesLevel = ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;

	int declarationsLevel = IMPOSSIBLE_MATCH;
	if (this.pattern.findDeclarations) {
		switch (node.getKind()) {
			case AbstractVariableDeclaration.FIELD :
			case AbstractVariableDeclaration.ENUM_CONSTANT :
				if (matchesName(this.pattern.name, node.name))
					if (matchesTypeReference(((FieldPattern)this.pattern).typeSimpleName, node.type))
						declarationsLevel = ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
				break;
		}
	}
	return nodeSet.addMatch(node, referencesLevel >= declarationsLevel ? referencesLevel : declarationsLevel); // use the stronger match
}
//public int match(MethodDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(MessageSend node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(TypeDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(TypeReference node, MatchingNodeSet nodeSet) - SKIP IT

protected int matchContainer() {
	if (this.pattern.findReferences) {
		// need to look everywhere to find in javadocs and static import
		return ALL_CONTAINER;
	}
	return CLASS_CONTAINER;
}
protected int matchField(FieldBinding field, boolean matchName) {
	if (field == null) return INACCURATE_MATCH;

	if (matchName && !matchesName(this.pattern.name, field.readableName())) return IMPOSSIBLE_MATCH;

	FieldPattern fieldPattern = (FieldPattern)this.pattern;
	ReferenceBinding receiverBinding = field.declaringClass;
	if (receiverBinding == null) {
		if (field == ArrayBinding.ArrayLength)
			// optimized case for length field of an array
			return fieldPattern.declaringQualification == null && fieldPattern.declaringSimpleName == null
				? ACCURATE_MATCH
				: IMPOSSIBLE_MATCH;
		return INACCURATE_MATCH;
	}

	// Note there is no dynamic lookup for field access
	int declaringLevel = resolveLevelForType(fieldPattern.declaringSimpleName, fieldPattern.declaringQualification, receiverBinding);
	if (declaringLevel == IMPOSSIBLE_MATCH) return IMPOSSIBLE_MATCH;

	// look at field type only if declaring type is not specified
	if (fieldPattern.declaringSimpleName == null) return declaringLevel;

	// get real field binding
	FieldBinding fieldBinding = field;
	if (field instanceof ParameterizedFieldBinding) {
		fieldBinding = ((ParameterizedFieldBinding) field).originalField;
	}

	int typeLevel = resolveLevelForType(fieldBinding.type);
	return declaringLevel > typeLevel ? typeLevel : declaringLevel; // return the weaker match
}
/* (non-Javadoc)
 * @see org.eclipse.jdt.internal.core.search.matching.PatternLocator#matchLevelAndReportImportRef(org.eclipse.jdt.internal.compiler.ast.ImportReference, org.eclipse.jdt.internal.compiler.lookup.Binding, org.eclipse.jdt.internal.core.search.matching.MatchLocator)
 * Accept to report match of static field on static import
 */
protected void matchLevelAndReportImportRef(ImportReference importRef, Binding binding, MatchLocator locator) throws CoreException {
	if (importRef.isStatic() && binding instanceof FieldBinding) {
		super.matchLevelAndReportImportRef(importRef, binding, locator);
	}
}
protected int matchReference(Reference node, MatchingNodeSet nodeSet, boolean writeOnlyAccess) {
	if (node instanceof FieldReference) {
		if (matchesName(this.pattern.name, ((FieldReference) node).token))
			return nodeSet.addMatch(node, ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH);
		return IMPOSSIBLE_MATCH;
	}
	return super.matchReference(node, nodeSet, writeOnlyAccess);
}
protected void matchReportReference(ASTNode reference, IJavaElement element, Binding elementBinding, int accuracy, MatchLocator locator) throws CoreException {
	if (this.isDeclarationOfAccessedFieldsPattern) {
		// need exact match to be able to open on type ref
		if (accuracy != SearchMatch.A_ACCURATE) return;

		// element that references the field must be included in the enclosing element
		DeclarationOfAccessedFieldsPattern declPattern = (DeclarationOfAccessedFieldsPattern) this.pattern; 
		while (element != null && !declPattern.enclosingElement.equals(element))
			element = element.getParent();
		if (element != null) {
			if (reference instanceof FieldReference) {
				reportDeclaration(((FieldReference) reference).binding, locator, declPattern.knownFields);
			} else if (reference instanceof QualifiedNameReference) {
				QualifiedNameReference qNameRef = (QualifiedNameReference) reference;
				Binding nameBinding = qNameRef.binding;
				if (nameBinding instanceof FieldBinding)
					reportDeclaration((FieldBinding)nameBinding, locator, declPattern.knownFields);
				int otherMax = qNameRef.otherBindings == null ? 0 : qNameRef.otherBindings.length;
				for (int i = 0; i < otherMax; i++)
					reportDeclaration(qNameRef.otherBindings[i], locator, declPattern.knownFields);
			} else if (reference instanceof SingleNameReference) {
				reportDeclaration((FieldBinding)((SingleNameReference) reference).binding, locator, declPattern.knownFields);
			}
		}
	} else if (reference instanceof ImportReference) {
		ImportReference importRef = (ImportReference) reference;
		long[] positions = importRef.sourcePositions;
		int lastIndex = importRef.tokens.length - 1;
		int start = (int) ((positions[lastIndex]) >>> 32);
		int end = (int) positions[lastIndex];
		match = locator.newFieldReferenceMatch(element, elementBinding, accuracy, start, end-start+1, importRef);
		locator.report(match);
	} else if (reference instanceof FieldReference) {
		FieldReference fieldReference = (FieldReference) reference;
		long position = fieldReference.nameSourcePosition;
		int start = (int) (position >>> 32);
		int end = (int) position;
		match = locator.newFieldReferenceMatch(element, elementBinding, accuracy, start, end-start+1, fieldReference);
		locator.report(match);
	} else if (reference instanceof SingleNameReference) {
		int offset = reference.sourceStart;
		match = locator.newFieldReferenceMatch(element, elementBinding, accuracy, offset, reference.sourceEnd-offset+1, reference);
		locator.report(match);
	} else if (reference instanceof QualifiedNameReference) {
		QualifiedNameReference qNameRef = (QualifiedNameReference) reference;
		int length = qNameRef.tokens.length;
		SearchMatch[] matches = new SearchMatch[length];
		Binding nameBinding = qNameRef.binding;
		int indexOfFirstFieldBinding = qNameRef.indexOfFirstFieldBinding > 0 ? qNameRef.indexOfFirstFieldBinding-1 : 0;

		// first token
		if (matchesName(this.pattern.name, qNameRef.tokens[indexOfFirstFieldBinding]) && !(nameBinding instanceof LocalVariableBinding)) {
			FieldBinding fieldBinding = nameBinding instanceof FieldBinding ? (FieldBinding) nameBinding : null;
			if (fieldBinding == null) {
				matches[indexOfFirstFieldBinding] = locator.newFieldReferenceMatch(element, elementBinding, accuracy, -1, -1, reference);
			} else {
				switch (matchField(fieldBinding, false)) {
					case ACCURATE_MATCH:
						matches[indexOfFirstFieldBinding] = locator.newFieldReferenceMatch(element, elementBinding, SearchMatch.A_ACCURATE, -1, -1, reference);
						break;
					case INACCURATE_MATCH:
						match = locator.newFieldReferenceMatch(element, elementBinding, SearchMatch.A_INACCURATE, -1, -1, reference);
						if (fieldBinding.type.isParameterizedType() && this.pattern.hasTypeArguments()) {
							updateMatch((ParameterizedTypeBinding) fieldBinding.type, this.pattern.getTypeArguments(), locator);
						}
						matches[indexOfFirstFieldBinding] = match;
						break;
				}
			}
		}

		// other tokens
		for (int i = indexOfFirstFieldBinding+1; i < length; i++) {
			char[] token = qNameRef.tokens[i];
			if (matchesName(this.pattern.name, token)) {
				FieldBinding otherBinding = qNameRef.otherBindings == null ? null : qNameRef.otherBindings[i-(indexOfFirstFieldBinding+1)];
				if (otherBinding == null) {
					matches[i] = locator.newFieldReferenceMatch(element, elementBinding, accuracy, -1, -1, reference);
				} else {
					switch (matchField(otherBinding, false)) {
						case ACCURATE_MATCH:
							matches[i] = locator.newFieldReferenceMatch(element, elementBinding, SearchMatch.A_ACCURATE, -1, -1, reference);
							break;
						case INACCURATE_MATCH:
							match = locator.newFieldReferenceMatch(element, elementBinding, SearchMatch.A_INACCURATE, -1, -1, reference);
							if (otherBinding.type.isParameterizedType() && this.pattern.hasTypeArguments()) {
								updateMatch((ParameterizedTypeBinding) otherBinding.type, this.pattern.getTypeArguments(), locator);
							}
							matches[indexOfFirstFieldBinding] = match;
							break;
					}
				}
			}
		}
		locator.reportAccurateFieldReference(matches, qNameRef);
	}
}
/* (non-Javadoc)
 * Overridden to reject unexact matches.
 * @see org.eclipse.jdt.internal.core.search.matching.PatternLocator#updateMatch(org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding, char[][][], org.eclipse.jdt.internal.core.search.matching.MatchLocator)
 *
 */
protected void updateMatch(ParameterizedTypeBinding parameterizedBinding, char[][][] patternTypeArguments, MatchLocator locator) {
	// We can only refine if locator has an unit scope.
	if (locator.unitScope == null) return;
	updateMatch(parameterizedBinding, patternTypeArguments, false, 0, locator);
	if (!match.isExact()) {
		// cannot accept neither erasure nor compatible match
		match.setRule(0);
	}
}
protected void reportDeclaration(FieldBinding fieldBinding, MatchLocator locator, SimpleSet knownFields) throws CoreException {
	// ignore length field
	if (fieldBinding == ArrayBinding.ArrayLength) return;
	
	ReferenceBinding declaringClass = fieldBinding.declaringClass;
	IType type = locator.lookupType(declaringClass);
	if (type == null) return; // case of a secondary type

	char[] bindingName = fieldBinding.name;
	IField field = type.getField(new String(bindingName));
	if (knownFields.includes(field)) return;

	knownFields.add(field);
	IResource resource = type.getResource();
	boolean isBinary = type.isBinary();
	IBinaryType info = null;
	if (isBinary) {
		if (resource == null)
			resource = type.getJavaProject().getProject();
		info = locator.getBinaryInfo((org.eclipse.jdt.internal.core.ClassFile) type.getClassFile(), resource);
		locator.reportBinaryMemberDeclaration(resource, field, fieldBinding, info, SearchMatch.A_ACCURATE);
	} else {
		if (declaringClass instanceof ParameterizedTypeBinding)
			declaringClass = ((ParameterizedTypeBinding) declaringClass).type;
		ClassScope scope = ((SourceTypeBinding) declaringClass).scope;
		if (scope != null) {
			TypeDeclaration typeDecl = scope.referenceContext;
			FieldDeclaration fieldDecl = null;
			FieldDeclaration[] fieldDecls = typeDecl.fields;
			for (int i = 0, length = fieldDecls.length; i < length; i++) {
				if (CharOperation.equals(bindingName, fieldDecls[i].name)) {
					fieldDecl = fieldDecls[i];
					break;
				}
			} 
			if (fieldDecl != null) {
				int offset = fieldDecl.sourceStart;
				match = new FieldDeclarationMatch(((JavaElement) field).resolved(fieldBinding), SearchMatch.A_ACCURATE, offset, fieldDecl.sourceEnd-offset+1, locator.getParticipant(), resource);
				locator.report(match);
			}
		}
	}
}
protected int referenceType() {
	return IJavaElement.FIELD;
}
public int resolveLevel(ASTNode possiblelMatchingNode) {
	if (this.pattern.findReferences) {
		if (possiblelMatchingNode instanceof FieldReference)
			return matchField(((FieldReference) possiblelMatchingNode).binding, true);
		else if (possiblelMatchingNode instanceof NameReference)
			return resolveLevel((NameReference) possiblelMatchingNode);
	}
	if (possiblelMatchingNode instanceof FieldDeclaration)
		return matchField(((FieldDeclaration) possiblelMatchingNode).binding, true);
	return IMPOSSIBLE_MATCH;
}
public int resolveLevel(Binding binding) {
	if (binding == null) return INACCURATE_MATCH;
	if (!(binding instanceof FieldBinding)) return IMPOSSIBLE_MATCH;

	return matchField((FieldBinding) binding, true);
}
protected int resolveLevel(NameReference nameRef) {
	if (nameRef instanceof SingleNameReference)
		return resolveLevel(nameRef.binding);

	Binding binding = nameRef.binding;
	QualifiedNameReference qNameRef = (QualifiedNameReference) nameRef;
	FieldBinding fieldBinding = null;
	if (binding instanceof FieldBinding) {
		fieldBinding = (FieldBinding) binding;
		char[] bindingName = fieldBinding.name;
		int lastDot = CharOperation.lastIndexOf('.', bindingName);
		if (lastDot > -1)
			bindingName = CharOperation.subarray(bindingName, lastDot+1, bindingName.length);
		if (matchesName(this.pattern.name, bindingName)) {
			int level = matchField(fieldBinding, false);
			if (level != IMPOSSIBLE_MATCH) return level;
		}
	} 
	int otherMax = qNameRef.otherBindings == null ? 0 : qNameRef.otherBindings.length;
	for (int i = 0; i < otherMax; i++) {
		char[] token = qNameRef.tokens[i + qNameRef.indexOfFirstFieldBinding];
		if (matchesName(this.pattern.name, token)) {
			FieldBinding otherBinding = qNameRef.otherBindings[i];
			int level = matchField(otherBinding, false);
			if (level != IMPOSSIBLE_MATCH) return level;
		}
	}
	return IMPOSSIBLE_MATCH;
}
/* (non-Javadoc)
 * Resolve level for type with a given binding.
 */
protected int resolveLevelForType(TypeBinding typeBinding) {
	FieldPattern fieldPattern = (FieldPattern) this.pattern;
	TypeBinding fieldTypeBinding = typeBinding;
	if (fieldTypeBinding != null && fieldTypeBinding.isParameterizedType()) {
		fieldTypeBinding = typeBinding.erasure();
	}
	return resolveLevelForType(
			fieldPattern.typeSimpleName,
			fieldPattern.typeQualification,
			fieldPattern.getTypeArguments(),
			0,
			fieldTypeBinding);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9376.java