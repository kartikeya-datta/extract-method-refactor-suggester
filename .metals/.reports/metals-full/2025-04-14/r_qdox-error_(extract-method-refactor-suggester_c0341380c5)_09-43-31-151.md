error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/486.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/486.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/486.java
text:
```scala
i@@f (binding == ArrayBinding.ArrayLength) {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.IJavaSearchResultCollector;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.core.index.IEntryResult;
import org.eclipse.jdt.internal.core.index.impl.IndexInput;
import org.eclipse.jdt.internal.core.index.impl.IndexedFile;
import org.eclipse.jdt.internal.core.search.IIndexSearchRequestor;
import org.eclipse.jdt.internal.core.search.indexing.AbstractIndexer;

public class FieldReferencePattern extends MultipleSearchPattern {

	// selector	
	protected char[] name;
	
	// declaring type
	protected char[] declaringQualification;
	protected char[] declaringSimpleName;

	// type
	protected char[] typeQualification;
	protected char[] typeSimpleName;
	
	// read/write access
	protected boolean readAccess = true;
	protected boolean writeAccess = true;

	protected char[] decodedName;

	private static char[][] REF_TAGS = { FIELD_REF, REF };
	private static char[][] REF_AND_DECL_TAGS = { FIELD_REF, REF, FIELD_DECL };

public FieldReferencePattern(
	char[] name, 
	int matchMode, 
	boolean isCaseSensitive,
	char[] declaringQualification,
	char[] declaringSimpleName,	
	char[] typeQualification, 
	char[] typeSimpleName,
	boolean readAccess,
	boolean writeAccess) {

	super(matchMode, isCaseSensitive);

	this.name = isCaseSensitive ? name : CharOperation.toLowerCase(name);
	this.declaringQualification = isCaseSensitive ? declaringQualification : CharOperation.toLowerCase(declaringQualification);
	this.declaringSimpleName = isCaseSensitive ? declaringSimpleName : CharOperation.toLowerCase(declaringSimpleName);
	this.typeQualification = isCaseSensitive ? typeQualification : CharOperation.toLowerCase(typeQualification);
	this.typeSimpleName = isCaseSensitive ? typeSimpleName : CharOperation.toLowerCase(typeSimpleName);
	this.readAccess = readAccess;
	this.writeAccess = writeAccess;

	this.needsResolve = true; // always resolve (in case of a simple name reference being a potential match)
}
/**
 * Either decode ref/name, fieldRef/name 
 */ 
public void decodeIndexEntry(IEntryResult entryResult){

	char[] word = entryResult.getWord();
	int size = word.length;
	int tagLength = currentTag.length;
	int nameLength = CharOperation.indexOf(SEPARATOR, word, tagLength);
	if (nameLength < 0) nameLength = size;
	decodedName = CharOperation.subarray(word, tagLength, nameLength);}
/**
 * see SearchPattern.feedIndexRequestor
 */
public void feedIndexRequestor(IIndexSearchRequestor requestor, int detailLevel, int[] references, IndexInput input, IJavaSearchScope scope) throws IOException {
	if (currentTag == REF) {
		foundAmbiguousIndexMatches = true;
	}
	for (int i = 0, max = references.length; i < max; i++) {
		int reference = references[i];
		if (reference != -1) { // if the reference has not been eliminated
			IndexedFile file = input.getIndexedFile(reference);
			String path;
			if (file != null && scope.encloses(path = IndexedFile.convertPath(file.getPath()))) {
				requestor.acceptFieldReference(path, decodedName);
			}
		}
	}
}
protected char[][] getPossibleTags() {
	if (this.writeAccess && !this.readAccess) {
		return REF_AND_DECL_TAGS;
	} else {
		return REF_TAGS;
	}
}
/**
 * @see AndPattern#hasNextQuery
 */
protected boolean hasNextQuery() {
	return false;
}
/**
 * see SearchPattern.indexEntryPrefix()
 */
public char[] indexEntryPrefix(){

	return AbstractIndexer.bestReferencePrefix(
			currentTag,
			name,
			matchMode, 
			isCaseSensitive);
}
/**
 * @see SearchPattern#matchCheck(AstNode, MatchingNodeSet)
 */
protected void matchCheck(AstNode node, MatchingNodeSet set) {
	if (this.readAccess) {
		super.matchCheck(node, set);
	}
	if (node instanceof Assignment) {
		AstNode lhs = ((Assignment)node).lhs;
		if (this.writeAccess) {
			super.matchCheck(lhs, set);
		} else if (!(node instanceof CompoundAssignment)){
			// the lhs may have been added when checking if it was a read access
			set.removePossibleMatch(lhs);
			set.removeTrustedMatch(lhs);
		}	
	} else if (node instanceof FieldDeclaration) {
		super.matchCheck(node, set);
	}
}
/**
 * @see SearchPattern#matchContainer()
 */
protected int matchContainer() {
	int matchContainer = METHOD | FIELD;
	if (this.writeAccess && !this.readAccess) {
		matchContainer |= CLASS;
	}
	return matchContainer;
}
/**
 * @see SearchPattern#matchIndexEntry
 */
protected boolean matchIndexEntry() {

	/* check name matches */
	if (name != null){
		switch(matchMode){
			case EXACT_MATCH :
				if (!CharOperation.equals(name, decodedName, isCaseSensitive)){
					return false;
				}
				break;
			case PREFIX_MATCH :
				if (!CharOperation.prefixEquals(name, decodedName, isCaseSensitive)){
					return false;
				}
				break;
			case PATTERN_MATCH :
				if (!CharOperation.match(name, decodedName, isCaseSensitive)){
					return false;
				}
		}
	}
	return true;
}
/**
 * @see SearchPattern#matchReportReference
 */
protected void matchReportReference(AstNode reference, IJavaElement element, int accuracy, MatchLocator locator) throws CoreException {
	if (reference instanceof QualifiedNameReference) {
		QualifiedNameReference qNameRef = (QualifiedNameReference)reference;
		int length = qNameRef.tokens.length;
		int[] accuracies = new int[length];
		Binding binding = qNameRef.binding;
		int indexOfFirstFieldBinding = qNameRef.indexOfFirstFieldBinding > 0 ? qNameRef.indexOfFirstFieldBinding-1 : 0;
		for (int i = 0; i < indexOfFirstFieldBinding; i++) {
			accuracies[i] = -1;
		}
		// first token
		if (this.matchesName(this.name, qNameRef.tokens[indexOfFirstFieldBinding])
				&& !(binding instanceof LocalVariableBinding)) {
			FieldBinding fieldBinding =
				binding instanceof FieldBinding ?
					 (FieldBinding)binding :
					 null;
			if (fieldBinding == null) {
				accuracies[indexOfFirstFieldBinding] = accuracy;
			} else {
				int level = this.matchLevel(fieldBinding);
				switch (level) {
					case ACCURATE_MATCH:
						accuracies[indexOfFirstFieldBinding] = IJavaSearchResultCollector.EXACT_MATCH;
						break;
					case INACCURATE_MATCH:
						accuracies[indexOfFirstFieldBinding] = IJavaSearchResultCollector.POTENTIAL_MATCH;
						break;
					default:
						accuracies[indexOfFirstFieldBinding] = -1;
				}
			}
		} else {
			accuracies[indexOfFirstFieldBinding] = -1;
		}
		// other tokens
		for (int i = indexOfFirstFieldBinding+1; i < length; i++){
			char[] token = qNameRef.tokens[i];
			if (this.matchesName(this.name, token)) {
				FieldBinding otherBinding = qNameRef.otherBindings == null ? null : qNameRef.otherBindings[i-(indexOfFirstFieldBinding+1)];
				if (otherBinding == null) {
					accuracies[i] = accuracy;
				} else {
					int level = this.matchLevel(otherBinding);
					switch (level) {
						case ACCURATE_MATCH:
							accuracies[i] = IJavaSearchResultCollector.EXACT_MATCH;
							break;
						case INACCURATE_MATCH:
							accuracies[i] = IJavaSearchResultCollector.POTENTIAL_MATCH;
							break;
						default:
							accuracies[i] = -1;
					}
				}
			} else {
				accuracies[i] = -1;
			}
		}
		locator.reportAccurateReference(
			reference.sourceStart, 
			reference.sourceEnd, 
			qNameRef.tokens, 
			element, 
			accuracies);
	} else {
		locator.reportAccurateReference(
			reference.sourceStart, 
			reference.sourceEnd, 
			new char[][] {this.name}, 
			element, 
			accuracy);
	}
}
/**
 * @see AndPattern#resetQuery
 */
protected void resetQuery() {
}
public String toString(){

	StringBuffer buffer = new StringBuffer(20);
	buffer.append("FieldReferencePattern: "); //$NON-NLS-1$
	if (declaringQualification != null) buffer.append(declaringQualification).append('.');
	if (declaringSimpleName != null) 
		buffer.append(declaringSimpleName).append('.');
	else if (declaringQualification != null) buffer.append("*."); //$NON-NLS-1$
	if (name != null) {
		buffer.append(name);
	} else {
		buffer.append("*"); //$NON-NLS-1$
	}
	if (typeQualification != null) 
		buffer.append(" --> ").append(typeQualification).append('.'); //$NON-NLS-1$
	else if (typeSimpleName != null) buffer.append(" --> "); //$NON-NLS-1$
	if (typeSimpleName != null) 
		buffer.append(typeSimpleName);
	else if (typeQualification != null) buffer.append("*"); //$NON-NLS-1$
	buffer.append(", "); //$NON-NLS-1$
	switch(matchMode){
		case EXACT_MATCH : 
			buffer.append("exact match, "); //$NON-NLS-1$
			break;
		case PREFIX_MATCH :
			buffer.append("prefix match, "); //$NON-NLS-1$
			break;
		case PATTERN_MATCH :
			buffer.append("pattern match, "); //$NON-NLS-1$
			break;
	}
	if (isCaseSensitive)
		buffer.append("case sensitive"); //$NON-NLS-1$
	else
		buffer.append("case insensitive"); //$NON-NLS-1$
	return buffer.toString();
}

/**
 * @see SearchPattern#matchLevel(AstNode, boolean)
 */
public int matchLevel(AstNode node, boolean resolve) {
	if (node instanceof FieldReference) {
		return this.matchLevel((FieldReference)node, resolve);
	} else if (node instanceof NameReference) {
		return this.matchLevel((NameReference)node, resolve);
	} else if (node instanceof FieldDeclaration) {
		return this.matchLevel((FieldDeclaration)node, resolve);
	} 
	return IMPOSSIBLE_MATCH;
}

/**
 * Returns whether this field reference pattern matches the given field reference.
 * Look at resolved information only if specified.
 */
private int matchLevel(FieldReference fieldRef, boolean resolve) {	
	// field name
	if (!this.matchesName(this.name, fieldRef.token))
		return IMPOSSIBLE_MATCH;

	if (resolve) {
		// receiver type and field type
		return this.matchLevel(fieldRef.binding);
	} else {
		return this.needsResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
	}
}
/**
 * Returns whether this field reference pattern matches the given field declaration in
 * write access.
 * Look at resolved information only if specified.
 */
private int matchLevel(FieldDeclaration fieldDecl, boolean resolve) {
	// nedd to be a write only access	
	if (!this.writeAccess || this.readAccess) return IMPOSSIBLE_MATCH;
	
	// need have an initialization
	if (fieldDecl.initialization == null) return IMPOSSIBLE_MATCH;
	
	// field name
	if (!this.matchesName(this.name, fieldDecl.name))
		return IMPOSSIBLE_MATCH;

	if (resolve) {
		// receiver type and field type
		return this.matchLevel(fieldDecl.binding);
	} else {
		return this.needsResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
	}
}

/**
 * Returns whether this field reference pattern matches the given name reference.
 * Look at resolved information only if specified.
 */
private int matchLevel(NameReference nameRef, boolean resolve) {	
	if (!resolve) {
		// field name
		if (this.name == null) {
			return this.needsResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
		} else {
			if (nameRef instanceof SingleNameReference) {
				if (this.matchesName(this.name, ((SingleNameReference)nameRef).token)) {
					// can only be a possible match since resolution is needed 
					// to find out if it is a field ref
					return POSSIBLE_MATCH;
				} else {
					return IMPOSSIBLE_MATCH;
				}
			} else { // QualifiedNameReference
				QualifiedNameReference qNameRef = (QualifiedNameReference)nameRef;
				char[][] tokens = qNameRef.tokens;
				if (this.writeAccess && !this.readAccess) {
					// in the case of the assigment of a qualified name reference, the match must be on the last token
					if (this.matchesName(this.name, tokens[tokens.length-1])) {
						// can only be a possible match since resolution is needed 
						// to find out if it is a field ref
						return POSSIBLE_MATCH;
					}
				} else {
					for (int i = 0, max = tokens.length; i < max; i++){
						if (this.matchesName(this.name, tokens[i])) {
							// can only be a possible match since resolution is needed 
							// to find out if it is a field ref
							return POSSIBLE_MATCH;
						}
					}
				}
				return IMPOSSIBLE_MATCH;
			}				
		} 
	} else {
		Binding binding = nameRef.binding;
		if (binding == null) {
			return INACCURATE_MATCH;
		} else {
			if (nameRef instanceof SingleNameReference){
				if (binding instanceof FieldBinding){
					return this.matchLevel((FieldBinding) binding);
				} else {
					return IMPOSSIBLE_MATCH; // must be a field binding
				}
			} else { // QualifiedNameReference
				QualifiedNameReference qNameRef = (QualifiedNameReference)nameRef;
				FieldBinding fieldBinding = null;
				if (binding instanceof FieldBinding) {
					fieldBinding = (FieldBinding)binding;
					char[] bindingName = fieldBinding.name;
					int lastDot = CharOperation.lastIndexOf('.', bindingName);
					if (lastDot > -1) {
						bindingName = CharOperation.subarray(bindingName, lastDot+1, bindingName.length);
					}
					if (this.matchesName(this.name, bindingName)) {
						return this.matchLevel(fieldBinding);
					} 
				} 
				int otherLevel = IMPOSSIBLE_MATCH;
				int otherMax = qNameRef.otherBindings == null ? 0 : qNameRef.otherBindings.length;
				for (int i = 0; i < otherMax && (otherLevel == IMPOSSIBLE_MATCH); i++){
					char[] token = qNameRef.tokens[i + qNameRef.indexOfFirstFieldBinding];
					if (this.matchesName(this.name, token)) {
						FieldBinding otherBinding = qNameRef.otherBindings[i];
						otherLevel = this.matchLevel(otherBinding);
					}
				}
				return otherLevel;
			}
		}
	}
}

/**
 * Returns whether this field reference pattern matches the given field binding.
 */
private int matchLevel(FieldBinding binding) {
	if (binding == null) return INACCURATE_MATCH;
	int level;
	
	// receiver type
	ReferenceBinding receiverBinding = binding.declaringClass;
	if (receiverBinding == null) {
		if (binding == ArrayBinding.LengthField) {
			// optimized case for length field of an array
			if (this.declaringQualification == null && this.declaringSimpleName == null) {
				return ACCURATE_MATCH;
			} else {
				return IMPOSSIBLE_MATCH;
			}
		} else {
			return INACCURATE_MATCH;
		}
	} else {
		// Note there is no dynamic lookup for field access
		level = this.matchLevelForType(this.declaringSimpleName, this.declaringQualification, receiverBinding);
		if (level == IMPOSSIBLE_MATCH) {
			return IMPOSSIBLE_MATCH;
		}
	}

	// look at field type only if declaring type is not specified
	if (this.declaringSimpleName == null) {
		int newLevel = this.matchLevelForType(this.typeSimpleName, this.typeQualification, binding.type);
		switch (newLevel) {
			case IMPOSSIBLE_MATCH:
				return IMPOSSIBLE_MATCH;
			case ACCURATE_MATCH: // keep previous level
				break;
			default: // ie. INACCURATE_MATCH
				level = newLevel;
				break;
		}
	}

	return level;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/486.java