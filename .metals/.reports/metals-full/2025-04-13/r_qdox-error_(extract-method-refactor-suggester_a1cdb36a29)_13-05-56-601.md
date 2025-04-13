error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9677.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9677.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9677.java
text:
```scala
a@@nnotations[i] = Util.getAnnotation(this, binaryAnnotations[i], null);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.IBinaryAnnotation;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Common functionality for Binary member handles.
 */
public abstract class BinaryMember extends NamedMember {
		
/*
 * Constructs a binary member.
 */
protected BinaryMember(JavaElement parent, String name) {
	super(parent, name);
}
/*
 * @see ISourceManipulation
 */
public void copy(IJavaElement container, IJavaElement sibling, String rename, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
protected IAnnotation[] getAnnotations(IBinaryAnnotation[] binaryAnnotations, long tagBits) {
	IAnnotation[] standardAnnotations = getStandardAnnotations(tagBits);
	if (binaryAnnotations == null)
		return standardAnnotations;
	int length = binaryAnnotations.length;
	int standardLength = standardAnnotations.length;
	IAnnotation[] annotations = new IAnnotation[length + standardLength];
	for (int i = 0; i < length; i++) {
		annotations[i] = Util.getAnnotation(this, binaryAnnotations[i]);
	}
	System.arraycopy(standardAnnotations, 0, annotations, length, standardLength);
	return annotations;
}
private IAnnotation getAnnotation(char[][] annotationName) {
	return new Annotation(this, new String(CharOperation.concatWith(annotationName, '.')));
}
private IAnnotation[] getStandardAnnotations(long tagBits) {
	if ((tagBits & TagBits.AllStandardAnnotationsMask) == 0)
		return Annotation.NO_ANNOTATIONS;
	ArrayList annotations = new ArrayList();

	if ((tagBits & TagBits.AnnotationTargetMASK) != 0) {
		annotations.add(getAnnotation(TypeConstants.JAVA_LANG_ANNOTATION_TARGET));
	}
	if ((tagBits & TagBits.AnnotationRetentionMASK) != 0) {
		annotations.add(getAnnotation(TypeConstants.JAVA_LANG_ANNOTATION_RETENTION));
	}
	if ((tagBits & TagBits.AnnotationDeprecated) != 0) {
		annotations.add(getAnnotation(TypeConstants.JAVA_LANG_DEPRECATED));
	}
	if ((tagBits & TagBits.AnnotationDocumented) != 0) {
		annotations.add(getAnnotation(TypeConstants.JAVA_LANG_ANNOTATION_DOCUMENTED));
	}
	if ((tagBits & TagBits.AnnotationInherited) != 0) {
		annotations.add(getAnnotation(TypeConstants.JAVA_LANG_ANNOTATION_INHERITED));
	}
	// note that JAVA_LANG_SUPPRESSWARNINGS and JAVA_LANG_OVERRIDE cannot appear in binaries
	return (IAnnotation[]) annotations.toArray(new IAnnotation[annotations.size()]);
}

public String[] getCategories() throws JavaModelException {
	SourceMapper mapper= getSourceMapper();
	if (mapper != null) {
		// ensure the class file's buffer is open so that categories are computed
		((ClassFile)getClassFile()).getBuffer();

		if (mapper.categories != null) {
			String[] categories = (String[]) mapper.categories.get(this);
			if (categories != null)
				return categories;
		}
	}
	return CharOperation.NO_STRINGS;
}
public String getKey() {
	try {
		return getKey(false/*don't open*/);
	} catch (JavaModelException e) {
		// happen only if force open is true
		return null;
	}
}
/**
 * @see org.eclipse.jdt.internal.compiler.lookup.Binding#computeUniqueKey()
 */
public abstract String getKey(boolean forceOpen) throws JavaModelException;
/*
 * @see ISourceReference
 */
public ISourceRange getNameRange() throws JavaModelException {
	SourceMapper mapper= getSourceMapper();
	if (mapper != null) {
		// ensure the class file's buffer is open so that source ranges are computed
		((ClassFile)getClassFile()).getBuffer();

		return mapper.getNameRange(this);
	} else {
		return SourceMapper.UNKNOWN_RANGE;
	}
}
/*
 * @see ISourceReference
 */
public ISourceRange getSourceRange() throws JavaModelException {
	SourceMapper mapper= getSourceMapper();
	if (mapper != null) {
		// ensure the class file's buffer is open so that source ranges are computed
		((ClassFile)getClassFile()).getBuffer();

		return mapper.getSourceRange(this);
	} else {
		return SourceMapper.UNKNOWN_RANGE;
	}
}
/*
 * @see IMember
 */
public boolean isBinary() {
	return true;
}
/*
 * @see IJavaElement
 */
public boolean isStructureKnown() throws JavaModelException {
	return ((IJavaElement)getOpenableParent()).isStructureKnown();
}
/*
 * @see ISourceManipulation
 */
public void move(IJavaElement container, IJavaElement sibling, String rename, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/*
 * @see ISourceManipulation
 */
public void rename(String newName, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/*
 * Sets the contents of this element.
 * Throws an exception as this element is read only.
 */
public void setContents(String contents, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9677.java