error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9834.java
text:
```scala
l@@ast = (importRef.bits & ASTNode.OnDemand) != 0 ? positions.length : positions.length - 1;

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.util.Util;

public class PackageReferenceLocator extends PatternLocator {

protected PackageReferencePattern pattern;

// check that referenced type is actually defined in this package fragment
public static boolean isDeclaringPackageFragment(IPackageFragment packageFragment, ReferenceBinding typeBinding) {
	char[] fileName = typeBinding.getFileName();
	if (fileName != null) {
		// retrieve the actual file name from the full path (sources are generally only containing it already)
		fileName = CharOperation.replaceOnCopy(fileName, '/', '\\'); // ensure to not do any side effect on file name (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=136016)
		fileName = CharOperation.lastSegment(fileName, '\\');
		
		try { 
			switch (packageFragment.getKind()) {
				case IPackageFragmentRoot.K_SOURCE :
					if (!org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(fileName) || !packageFragment.getCompilationUnit(new String(fileName)).exists()) {
						return false; // unit doesn't live in selected package
					}
					break;
				case IPackageFragmentRoot.K_BINARY :
//					if (Util.isJavaFileName(fileName)) { // binary with attached source
//						int length = fileName.length;
//						System.arraycopy(fileName, 0, fileName = new char[length], 0, length - 4); // copy all but extension
//						System.arraycopy(SuffixConstants.SUFFIX_class, 0, fileName, length - 4, 4);
//					}
					if (!Util.isClassFileName(fileName) || !packageFragment.getClassFile(new String(fileName)).exists()) {
						return false; // classfile doesn't live in selected package
					}
					break;
			}
		} catch(JavaModelException e) {
			// unable to determine kind; tolerate this match
		}
	}
	return true; // by default, do not eliminate 
}

public PackageReferenceLocator(PackageReferencePattern pattern) {
	super(pattern);

	this.pattern = pattern;
}
public int match(ASTNode node, MatchingNodeSet nodeSet) { // interested in ImportReference
	if (!(node instanceof ImportReference)) return IMPOSSIBLE_MATCH;

	return nodeSet.addMatch(node, matchLevel((ImportReference) node));
}
//public int match(ConstructorDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(Expression node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(FieldDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(MethodDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(MessageSend node, MatchingNodeSet nodeSet) - SKIP IT
public int match(Reference node, MatchingNodeSet nodeSet) { // interested in QualifiedNameReference
	if (!(node instanceof QualifiedNameReference)) return IMPOSSIBLE_MATCH;

	return nodeSet.addMatch(node, matchLevelForTokens(((QualifiedNameReference) node).tokens));
}
//public int match(TypeDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
public int match(TypeReference node, MatchingNodeSet nodeSet) { // interested in QualifiedTypeReference only
	if (node instanceof JavadocSingleTypeReference) {
		char[][] tokens = new char[][] { ((JavadocSingleTypeReference) node).token };
		return nodeSet.addMatch(node, matchLevelForTokens(tokens));
	}
	if (!(node instanceof QualifiedTypeReference)) return IMPOSSIBLE_MATCH;
	return nodeSet.addMatch(node, matchLevelForTokens(((QualifiedTypeReference) node).tokens));
}

protected int matchLevel(ImportReference importRef) {
	return matchLevelForTokens(importRef.tokens);
}
protected int matchLevelForTokens(char[][] tokens) {
	if (this.pattern.pkgName == null) return ACCURATE_MATCH;

	char[] packageName = null;
	if (this.isCamelCase) {
		packageName = CharOperation.concatWith(tokens, '.');
		if (CharOperation.camelCaseMatch(this.pattern.pkgName, packageName)) {
			return POSSIBLE_MATCH;
		}
	}
	switch (this.matchMode) {
		case SearchPattern.R_EXACT_MATCH:
		case SearchPattern.R_PREFIX_MATCH:
			if (packageName==null) packageName = CharOperation.concatWith(tokens, '.');
			if (CharOperation.prefixEquals(this.pattern.pkgName, packageName, this.isCaseSensitive)) {
				return POSSIBLE_MATCH;
			}
			break;
		case SearchPattern.R_PATTERN_MATCH:
			char[] patternName = this.pattern.pkgName[this.pattern.pkgName.length - 1] == '*'
				? this.pattern.pkgName
				: CharOperation.concat(this.pattern.pkgName, ".*".toCharArray()); //$NON-NLS-1$
			if (packageName==null) packageName = CharOperation.concatWith(tokens, '.');
			if (CharOperation.match(patternName, packageName, this.isCaseSensitive)) {
				return POSSIBLE_MATCH;
			}
			break;
		case SearchPattern.R_REGEXP_MATCH :
			// TODO (frederic) implement regular expression match
			break;
	}
	return IMPOSSIBLE_MATCH;
}
/* (non-Javadoc)
 * @see org.eclipse.jdt.internal.core.search.matching.PatternLocator#matchLevelAndReportImportRef(org.eclipse.jdt.internal.compiler.ast.ImportReference, org.eclipse.jdt.internal.compiler.lookup.Binding, org.eclipse.jdt.internal.core.search.matching.MatchLocator)
 */
protected void matchLevelAndReportImportRef(ImportReference importRef, Binding binding, MatchLocator locator) throws CoreException {
	Binding refBinding = binding;
	if (importRef.isStatic()) {
		// for static import, binding can be a field binding or a member type binding
		// verify that in this case binding is static and use declaring class for fields
		if (binding instanceof FieldBinding) {
			FieldBinding fieldBinding = (FieldBinding) binding;
			if (!fieldBinding.isStatic()) return;
			refBinding = fieldBinding.declaringClass;
		} else if (binding instanceof MethodBinding) {
			MethodBinding methodBinding = (MethodBinding) binding;
			if (!methodBinding.isStatic()) return;
			refBinding = methodBinding.declaringClass;
		} else if (binding instanceof MemberTypeBinding) {
			MemberTypeBinding memberBinding = (MemberTypeBinding) binding;
			if (!memberBinding.isStatic()) return;
		}
	}
	super.matchLevelAndReportImportRef(importRef, refBinding, locator);
}
protected void matchReportImportRef(ImportReference importRef, Binding binding, IJavaElement element, int accuracy, MatchLocator locator) throws CoreException {
	if (binding == null) {
		this.matchReportReference(importRef, element, null/*no binding*/, accuracy, locator);
	} else {
		if (locator.encloses(element)) {
			long[] positions = importRef.sourcePositions;
			int last = positions.length - 1;
			if (binding instanceof ProblemReferenceBinding)
				binding = ((ProblemReferenceBinding) binding).closestMatch();
			if (binding instanceof ReferenceBinding) {
				PackageBinding pkgBinding = ((ReferenceBinding) binding).fPackage;
				if (pkgBinding != null)
					last = pkgBinding.compoundName.length;
			}
			if (binding instanceof PackageBinding)
				last = ((PackageBinding) binding).compoundName.length;
			int start = (int) (positions[0] >>> 32);
			int end = (int) positions[last - 1];
			match = locator.newPackageReferenceMatch(element, accuracy, start, end-start+1, importRef);
			locator.report(match);
		}
	}
}
protected void matchReportReference(ASTNode reference, IJavaElement element, Binding elementBinding, int accuracy, MatchLocator locator) throws CoreException {
	long[] positions = null;
	int last = -1;
	if (reference instanceof ImportReference) {
		ImportReference importRef = (ImportReference) reference;
		positions = importRef.sourcePositions;
		last = importRef.onDemand ? positions.length : positions.length - 1;
	} else {
		TypeBinding typeBinding = null;
		if (reference instanceof QualifiedNameReference) {
			QualifiedNameReference qNameRef = (QualifiedNameReference) reference;
			positions = qNameRef.sourcePositions;
			switch (qNameRef.bits & ASTNode.RestrictiveFlagMASK) {
				case Binding.FIELD : // reading a field
					typeBinding = qNameRef.actualReceiverType;
					break;
				case Binding.TYPE : //=============only type ==============
					if (qNameRef.binding instanceof TypeBinding)
						typeBinding = (TypeBinding) qNameRef.binding;
					break;
				case Binding.VARIABLE : //============unbound cases===========
				case Binding.TYPE | Binding.VARIABLE :
					Binding binding = qNameRef.binding; 
					if (binding instanceof TypeBinding) {
						typeBinding = (TypeBinding) binding;
					} else if (binding instanceof ProblemFieldBinding) {
						typeBinding = qNameRef.actualReceiverType;
						last = qNameRef.tokens.length - (qNameRef.otherBindings == null ? 2 : qNameRef.otherBindings.length + 2);
					} else if (binding instanceof ProblemBinding) {
						ProblemBinding pbBinding = (ProblemBinding) binding;
						typeBinding = pbBinding.searchType;
						last = CharOperation.occurencesOf('.', pbBinding.name);
					}
					break;					
			}
		} else if (reference instanceof QualifiedTypeReference) {
			QualifiedTypeReference qTypeRef = (QualifiedTypeReference) reference;
			positions = qTypeRef.sourcePositions;
			typeBinding = qTypeRef.resolvedType;
		} else if (reference instanceof JavadocSingleTypeReference) {
			JavadocSingleTypeReference jsTypeRef = (JavadocSingleTypeReference) reference;
			positions = new long[1];
			positions[0] = (((long)jsTypeRef.sourceStart) << 32) + jsTypeRef.sourceEnd;
			typeBinding = jsTypeRef.resolvedType;
		}
		if (positions == null) return;
		if (typeBinding instanceof ArrayBinding)
			typeBinding = ((ArrayBinding) typeBinding).leafComponentType;
		if (typeBinding instanceof ProblemReferenceBinding)
			typeBinding = ((ProblemReferenceBinding) typeBinding).closestMatch();
		if (typeBinding instanceof ReferenceBinding) {
			PackageBinding pkgBinding = ((ReferenceBinding) typeBinding).fPackage;
			if (pkgBinding != null)
				last = pkgBinding.compoundName.length;
		}
		// Do not report qualified references which are only enclosing type
		// (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=91078)
		ReferenceBinding enclosingType = typeBinding == null ? null: typeBinding.enclosingType();
		if (enclosingType != null) {
			int length = positions.length;
			while (enclosingType != null && length > 0) {
				length--;
				enclosingType = enclosingType.enclosingType();
			}
			if (length <= 1) return;
		}
	}
	if (last == -1) {
		last = this.pattern.segments.length;
	}
	if (last == 0) return;
	if (last > positions.length) last = positions.length;
	int sourceStart = (int) (positions[0] >>> 32);
	int sourceEnd = ((int) positions[last - 1]);
	match = locator.newPackageReferenceMatch(element, accuracy, sourceStart, sourceEnd-sourceStart+1, reference);
	locator.report(match);
}
protected int referenceType() {
	return IJavaElement.PACKAGE_FRAGMENT;
}
public int resolveLevel(ASTNode node) {
	if (node instanceof JavadocQualifiedTypeReference) {
		JavadocQualifiedTypeReference qualifRef = (JavadocQualifiedTypeReference) node;
		if (qualifRef.packageBinding != null)
			return resolveLevel(qualifRef.packageBinding);
		return resolveLevel(qualifRef.resolvedType);
	}
	if (node instanceof JavadocSingleTypeReference) {
		JavadocSingleTypeReference singleRef = (JavadocSingleTypeReference) node;
		if (singleRef.packageBinding != null)
			return resolveLevel(singleRef.packageBinding);
		return IMPOSSIBLE_MATCH;
	}
	if (node instanceof QualifiedTypeReference)
		return resolveLevel(((QualifiedTypeReference) node).resolvedType);
	if (node instanceof QualifiedNameReference)
		return this.resolveLevel((QualifiedNameReference) node);
//	if (node instanceof ImportReference) - Not called when resolve is true, see MatchingNodeSet.reportMatching(unit)
	return IMPOSSIBLE_MATCH;
}
public int resolveLevel(Binding binding) {
	if (binding == null) return INACCURATE_MATCH;

	char[][] compoundName = null;
	if (binding instanceof ImportBinding) {
		compoundName = ((ImportBinding) binding).compoundName;
	} else if (binding instanceof PackageBinding) {
		compoundName = ((PackageBinding) binding).compoundName;
	} else {
		if (binding instanceof ArrayBinding)
			binding = ((ArrayBinding) binding).leafComponentType;
		if (binding instanceof ProblemReferenceBinding)
			binding = ((ProblemReferenceBinding) binding).closestMatch();
		if (binding == null) return INACCURATE_MATCH;

		if (binding instanceof ReferenceBinding) {
			PackageBinding pkgBinding = ((ReferenceBinding) binding).fPackage;
			if (pkgBinding == null) return INACCURATE_MATCH;
			compoundName = pkgBinding.compoundName;
		}
	}
	if (compoundName != null && matchesName(this.pattern.pkgName, CharOperation.concatWith(compoundName, '.'))) {
		if (((InternalSearchPattern) this.pattern).focus instanceof IPackageFragment && binding instanceof ReferenceBinding) {
			// check that type is located inside this instance of a package fragment
			if (!isDeclaringPackageFragment((IPackageFragment)((InternalSearchPattern) this.pattern).focus, (ReferenceBinding)binding))
				return IMPOSSIBLE_MATCH;
		}				
		return ACCURATE_MATCH;
	} else {
		return IMPOSSIBLE_MATCH;
	}
}
protected int resolveLevel(QualifiedNameReference qNameRef) {
	TypeBinding typeBinding = null;
	switch (qNameRef.bits & ASTNode.RestrictiveFlagMASK) {
		case Binding.FIELD : // reading a field
			if (qNameRef.tokens.length < (qNameRef.otherBindings == null ? 3 : qNameRef.otherBindings.length + 3))
				return IMPOSSIBLE_MATCH; // must be at least p1.A.x
			typeBinding = qNameRef.actualReceiverType;
			break;
		case Binding.LOCAL : // reading a local variable
			return IMPOSSIBLE_MATCH; // no package match in it
		case Binding.TYPE : //=============only type ==============
			if (qNameRef.binding instanceof TypeBinding)
				typeBinding = (TypeBinding) qNameRef.binding;
			break;
		/*
		 * Handling of unbound qualified name references. The match may reside in the resolved fragment,
		 * which is recorded inside the problem binding, along with the portion of the name until it became a problem.
		 */
		case Binding.VARIABLE : //============unbound cases===========
		case Binding.TYPE | Binding.VARIABLE :
			Binding binding = qNameRef.binding; 
			if (binding instanceof ProblemReferenceBinding) {
				typeBinding = (TypeBinding) binding;
			} else if (binding instanceof ProblemFieldBinding) {
				if (qNameRef.tokens.length < (qNameRef.otherBindings == null ? 3 : qNameRef.otherBindings.length + 3))
					return IMPOSSIBLE_MATCH; // must be at least p1.A.x
				typeBinding = qNameRef.actualReceiverType;
			} else if (binding instanceof ProblemBinding) {
				ProblemBinding pbBinding = (ProblemBinding) binding;
				if (CharOperation.occurencesOf('.', pbBinding.name) <= 0) // index of last bound token is one before the pb token
					return INACCURATE_MATCH;
				typeBinding = pbBinding.searchType;
			}
			break;					
	}
	return resolveLevel(typeBinding);
}
public String toString() {
	return "Locator for " + this.pattern.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9834.java