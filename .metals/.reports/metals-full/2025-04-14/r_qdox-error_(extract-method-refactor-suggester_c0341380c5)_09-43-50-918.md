error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9972.java
text:
```scala
o@@utput.append(this.typeParameter.toString());

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.internal.compiler.env.AccessRuleSet;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.index.Index;
import org.eclipse.jdt.internal.core.search.IndexQueryRequestor;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Pattern to search type parameters.
 *
 * @since 3.1
 */
public class TypeParameterPattern extends JavaSearchPattern {

	protected boolean findDeclarations;
	protected boolean findReferences;
	protected char[] name;
	protected ITypeParameter typeParameter;
	protected char[] declaringMemberName;
	protected char[] methodDeclaringClassName;
	protected char[][] methodArgumentTypes;

	/**
	 * @param findDeclarations
	 * @param findReferences
	 * @param typeParameter
	 * @param matchRule
	 */
	public TypeParameterPattern(boolean findDeclarations, boolean findReferences, ITypeParameter typeParameter, int matchRule) {
		super(TYPE_PARAM_PATTERN, matchRule);

		this.findDeclarations = findDeclarations; // set to find declarations & all occurences
		this.findReferences = findReferences; // set to find references & all occurences
		this.typeParameter = typeParameter;
		this.name = typeParameter.getElementName().toCharArray(); // store type parameter name
		IMember member = typeParameter.getDeclaringMember();
		this.declaringMemberName = member.getElementName().toCharArray(); // store type parameter declaring member name
		
		// For method type parameter, store also declaring class name and parameters type names
		if (member instanceof IMethod) {
			IMethod method = (IMethod) member;
			this.methodDeclaringClassName = method.getParent().getElementName().toCharArray();
			String[] parameters = method.getParameterTypes();
			int length = parameters.length;
			this.methodArgumentTypes = new char[length][];
			for (int i=0; i<length; i++) {
				this.methodArgumentTypes[i] = Signature.toCharArray(parameters[i].toCharArray());
			}
		}
	}

	/*
	 * Same than LocalVariablePattern.
	 */
	public void findIndexMatches(Index index, IndexQueryRequestor requestor, SearchParticipant participant, IJavaSearchScope scope, IProgressMonitor progressMonitor) {
	    IPackageFragmentRoot root = (IPackageFragmentRoot) this.typeParameter.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
		String documentPath;
		String relativePath;
	    if (root.isArchive()) {
 	    	IType type = (IType) this.typeParameter.getAncestor(IJavaElement.TYPE);
    	    relativePath = (type.getFullyQualifiedName('$')).replace('.', '/') + SuffixConstants.SUFFIX_STRING_class;
	        documentPath = root.getPath() + IJavaSearchScope.JAR_FILE_ENTRY_SEPARATOR + relativePath;
	    } else {
			IPath path = this.typeParameter.getPath();
	        documentPath = path.toString();
			relativePath = Util.relativePath(path, 1/*remove project segment*/);
	    }
	
		if (scope instanceof JavaSearchScope) {
			JavaSearchScope javaSearchScope = (JavaSearchScope) scope;
			// Get document path access restriction from java search scope
			// Note that requestor has to verify if needed whether the document violates the access restriction or not
			AccessRuleSet access = javaSearchScope.getAccessRuleSet(relativePath, index.containerPath);
			if (access != JavaSearchScope.NOT_ENCLOSED) { // scope encloses the path
				if (!requestor.acceptIndexMatch(documentPath, this, participant, access)) 
					throw new OperationCanceledException();
			}
		} else if (scope.encloses(documentPath)) {
			if (!requestor.acceptIndexMatch(documentPath, this, participant, null)) 
				throw new OperationCanceledException();
		}
	}

	protected StringBuffer print(StringBuffer output) {
		if (this.findDeclarations) {
			output.append(this.findReferences
				? "TypeParamCombinedPattern: " //$NON-NLS-1$
				: "TypeParamDeclarationPattern: "); //$NON-NLS-1$
		} else {
			output.append("TypeParamReferencePattern: "); //$NON-NLS-1$
		}
		output.append(typeParameter.toString());
		return super.print(output);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9972.java