error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8264.java
text:
```scala
B@@inaryTypeBinding binding = locator.cacheBinaryType(binaryType, info);

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;

public class ClassFileMatchLocator implements IIndexConstants {

public static char[] convertClassFileFormat(char[] name) {
	return CharOperation.replaceOnCopy(name, '/', '.');
}

boolean checkDeclaringType(IBinaryType enclosingBinaryType, char[] simpleName, char[] qualification, boolean isCaseSensitive) {
	if (simpleName == null && qualification == null) return true;
	if (enclosingBinaryType == null) return true;

	char[] declaringTypeName = convertClassFileFormat(enclosingBinaryType.getName());
	return checkTypeName(simpleName, qualification, declaringTypeName, isCaseSensitive);
}
boolean checkParameters(char[] methodDescriptor, char[][] parameterSimpleNames, char[][] parameterQualifications, boolean isCaseSensitive) {
	char[][] arguments = Signature.getParameterTypes(methodDescriptor);
	int parameterCount = parameterSimpleNames.length;
	if (parameterCount != arguments.length) return false;
	for (int i = 0; i < parameterCount; i++)
		if (!checkTypeName(parameterSimpleNames[i], parameterQualifications[i], Signature.toCharArray(arguments[i]), isCaseSensitive))
			return false;
	return true;
}
boolean checkTypeName(char[] simpleName, char[] qualification, char[] fullyQualifiedTypeName, boolean isCaseSensitive) {
	// NOTE: if case insensitive then simpleName & qualification are assumed to be lowercase
	char[] wildcardPattern = PatternLocator.qualifiedPattern(simpleName, qualification);
	if (wildcardPattern == null) return true;
	return CharOperation.match(wildcardPattern, fullyQualifiedTypeName, isCaseSensitive);
}
/**
 * Locate declaration in the current class file. This class file is always in a jar.
 */
public void locateMatches(MatchLocator locator, ClassFile classFile, IBinaryType info) throws CoreException {
	// check class definition
	SearchPattern pattern = locator.pattern;
	BinaryType binaryType = (BinaryType) classFile.getType();
	if (matchBinary(pattern, info, null))
		locator.reportBinaryMemberDeclaration(null, binaryType, info, SearchMatch.A_ACCURATE);

	int accuracy = SearchMatch.A_ACCURATE;
	if (((InternalSearchPattern)pattern).mustResolve) {
		try {
			BinaryTypeBinding binding = locator.cacheBinaryType(binaryType);
			if (binding != null) {
				// filter out element not in hierarchy scope
				if (!locator.typeInHierarchy(binding)) return;

				MethodBinding[] methods = binding.methods();
				for (int i = 0, l = methods.length; i < l; i++) {
					MethodBinding method = methods[i];
					if (locator.patternLocator.resolveLevel(method) == PatternLocator.ACCURATE_MATCH) {
						IMethod methodHandle = binaryType.getMethod(
							new String(method.isConstructor() ? binding.compoundName[binding.compoundName.length-1] : method.selector),
							CharOperation.toStrings(Signature.getParameterTypes(convertClassFileFormat(method.signature()))));
						locator.reportBinaryMemberDeclaration(null, methodHandle, info, SearchMatch.A_ACCURATE);
					}
				}

				FieldBinding[] fields = binding.fields();
				for (int i = 0, l = fields.length; i < l; i++) {
					FieldBinding field = fields[i];
					if (locator.patternLocator.resolveLevel(field) == PatternLocator.ACCURATE_MATCH) {
						IField fieldHandle = binaryType.getField(new String(field.name));
						locator.reportBinaryMemberDeclaration(null, fieldHandle, info, SearchMatch.A_ACCURATE);
					}
				}

				// no need to check binary info since resolve was successful
				return;
			}
		} catch (AbortCompilation e) { // if compilation was aborted it is a problem with the class path
		}
		// report as a potential match if binary info matches the pattern		
		accuracy = SearchMatch.A_INACCURATE;
	}

	IBinaryMethod[] methods = info.getMethods();
	if (methods != null) {
		for (int i = 0, l = methods.length; i < l; i++) {
			IBinaryMethod method = methods[i];
			if (matchBinary(pattern, method, info)) {
				char[] name;
				if (method.isConstructor()) {
					name = info.getName();
					int lastSlash = CharOperation.lastIndexOf('/', name);
					if (lastSlash != -1) {
						name = CharOperation.subarray(name, lastSlash+1, name.length);
					}
				} else {
					name = method.getSelector();
				}
				IMethod methodHandle = binaryType.getMethod(
					new String(name),
					CharOperation.toStrings(Signature.getParameterTypes(convertClassFileFormat(method.getMethodDescriptor()))));
				locator.reportBinaryMemberDeclaration(null, methodHandle, info, accuracy);
			}
		}
	}

	IBinaryField[] fields = info.getFields();
	if (fields != null) {
		for (int i = 0, l = fields.length; i < l; i++) {
			IBinaryField field = fields[i];
			if (matchBinary(pattern, field, info)) {
				IField fieldHandle = binaryType.getField(new String(field.getName()));
				locator.reportBinaryMemberDeclaration(null, fieldHandle, info, accuracy);
			}
		}
	}
}
/**
 * Finds out whether the given binary info matches the search pattern.
 * Default is to return false.
 */
boolean matchBinary(SearchPattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	switch (((InternalSearchPattern)pattern).kind) {
		case CONSTRUCTOR_PATTERN :
			return matchConstructor((ConstructorPattern) pattern, binaryInfo, enclosingBinaryType);
		case FIELD_PATTERN :
			return matchField((FieldPattern) pattern, binaryInfo, enclosingBinaryType);
		case METHOD_PATTERN :
			return matchMethod((MethodPattern) pattern, binaryInfo, enclosingBinaryType);
		case SUPER_REF_PATTERN :
			return matchSuperTypeReference((SuperTypeReferencePattern) pattern, binaryInfo, enclosingBinaryType);
		case TYPE_DECL_PATTERN :
			return matchTypeDeclaration((TypeDeclarationPattern) pattern, binaryInfo, enclosingBinaryType);
		case OR_PATTERN :
			SearchPattern[] patterns = ((OrPattern) pattern).patterns;
			for (int i = 0, length = patterns.length; i < length; i++)
				if (matchBinary(patterns[i], binaryInfo, enclosingBinaryType)) return true;
	}
	return false;
}
boolean matchConstructor(ConstructorPattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	if (!pattern.findDeclarations) return false; // only relevant when finding declarations
	if (!(binaryInfo instanceof IBinaryMethod)) return false;

	IBinaryMethod method = (IBinaryMethod) binaryInfo;
	if (!method.isConstructor()) return false;
	if (!checkDeclaringType(enclosingBinaryType, pattern.declaringSimpleName, pattern.declaringQualification, pattern.isCaseSensitive()))
		return false;
	if (pattern.parameterSimpleNames != null) {
		char[] methodDescriptor = convertClassFileFormat(method.getMethodDescriptor());
		if (!checkParameters(methodDescriptor, pattern.parameterSimpleNames, pattern.parameterQualifications, pattern.isCaseSensitive()))
			return false;
	}
	return true;
}
boolean matchField(FieldPattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	if (!pattern.findDeclarations) return false; // only relevant when finding declarations
	if (!(binaryInfo instanceof IBinaryField)) return false;

	IBinaryField field = (IBinaryField) binaryInfo;
	if (!pattern.matchesName(pattern.name, field.getName())) return false;
	if (!checkDeclaringType(enclosingBinaryType, pattern.declaringSimpleName, pattern.declaringQualification, pattern.isCaseSensitive()))
		return false;

	char[] fieldTypeSignature = Signature.toCharArray(convertClassFileFormat(field.getTypeName()));
	return checkTypeName(pattern.typeSimpleName, pattern.typeQualification, fieldTypeSignature, pattern.isCaseSensitive());
}
boolean matchMethod(MethodPattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	if (!pattern.findDeclarations) return false; // only relevant when finding declarations
	if (!(binaryInfo instanceof IBinaryMethod)) return false;

	IBinaryMethod method = (IBinaryMethod) binaryInfo;
	if (!pattern.matchesName(pattern.selector, method.getSelector())) return false;
	if (!checkDeclaringType(enclosingBinaryType, pattern.declaringSimpleName, pattern.declaringQualification, pattern.isCaseSensitive()))
		return false;

	// look at return type only if declaring type is not specified
	boolean checkReturnType = pattern.declaringSimpleName == null && (pattern.returnSimpleName != null || pattern.returnQualification != null);
	boolean checkParameters = pattern.parameterSimpleNames != null;
	if (checkReturnType || checkParameters) {
		char[] methodDescriptor = convertClassFileFormat(method.getMethodDescriptor());
		if (checkReturnType) {
			char[] returnTypeSignature = Signature.toCharArray(Signature.getReturnType(methodDescriptor));
			if (!checkTypeName(pattern.returnSimpleName, pattern.returnQualification, returnTypeSignature, pattern.isCaseSensitive()))
				return false;
		}
		if (checkParameters &&  !checkParameters(methodDescriptor, pattern.parameterSimpleNames, pattern.parameterQualifications, pattern.isCaseSensitive()))
			return false;
	}
	return true;
}
boolean matchSuperTypeReference(SuperTypeReferencePattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	if (!(binaryInfo instanceof IBinaryType)) return false;

	IBinaryType type = (IBinaryType) binaryInfo;
	if (!pattern.checkOnlySuperinterfaces) {
		char[] vmName = type.getSuperclassName();
		if (vmName != null) {
			char[] superclassName = convertClassFileFormat(vmName);
			if (checkTypeName(pattern.superSimpleName, pattern.superQualification, superclassName, pattern.isCaseSensitive()))
				return true;
		}
	}

	char[][] superInterfaces = type.getInterfaceNames();
	if (superInterfaces != null) {
		for (int i = 0, max = superInterfaces.length; i < max; i++) {
			char[] superInterfaceName = convertClassFileFormat(superInterfaces[i]);
			if (checkTypeName(pattern.superSimpleName, pattern.superQualification, superInterfaceName, pattern.isCaseSensitive()))
				return true;
		}
	}
	return false;
}
boolean matchTypeDeclaration(TypeDeclarationPattern pattern, Object binaryInfo, IBinaryType enclosingBinaryType) {
	if (!(binaryInfo instanceof IBinaryType)) return false;

	IBinaryType type = (IBinaryType) binaryInfo;
	char[] fullyQualifiedTypeName = convertClassFileFormat(type.getName());
	if (pattern.enclosingTypeNames == null || pattern instanceof QualifiedTypeDeclarationPattern) {
		if (!checkTypeName(pattern.simpleName, pattern.pkg, fullyQualifiedTypeName, pattern.isCaseSensitive())) return false;
	} else {
		char[] enclosingTypeName = CharOperation.concatWith(pattern.enclosingTypeNames, '.');
		char[] patternString = pattern.pkg == null
			? enclosingTypeName
			: CharOperation.concat(pattern.pkg, enclosingTypeName, '.');
		if (!checkTypeName(pattern.simpleName, patternString, fullyQualifiedTypeName, pattern.isCaseSensitive())) return false;
	}

	switch (pattern.classOrInterface) {
		case CLASS_SUFFIX:
			return !type.isInterface();
		case INTERFACE_SUFFIX:
			return type.isInterface();
		case TYPE_SUFFIX: // nothing
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8264.java