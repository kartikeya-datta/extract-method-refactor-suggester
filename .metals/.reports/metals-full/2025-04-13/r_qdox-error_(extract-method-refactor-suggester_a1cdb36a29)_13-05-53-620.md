error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1754.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1754.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1754.java
text:
```scala
n@@ew SelectionEngine(environment, requestor, JavaModelManager.getOptions());

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.core.resources.*;
import java.util.Vector;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.codeassist.*;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ConfigurableOption;
import org.eclipse.jdt.core.jdom.IDOMNode;

/**
 * Handle for a source type. Info object is a SourceTypeElementInfo.
 *
 * Note: Parent is either an IClassFile, an ICompilationUnit or an IType.
 *
 * @see IType
 */

public class SourceType extends Member implements IType {
	/**
	 * An empty list of Strings
	 */
	protected static final String[] fgEmptyList= new String[] {};
protected SourceType(IJavaElement parent, String name) {
	super(TYPE, parent, name);
	Assert.isTrue(name.indexOf('.') == -1);
}
/**
 * @see IType
 */
public IField createField(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	CreateFieldOperation op = new CreateFieldOperation(this, contents, force);
	if (sibling != null) {
		op.createBefore(sibling);
	}
	runOperation(op, monitor);
	return (IField) op.getResultElements()[0];
}
/**
 * @see IType
 */
public IInitializer createInitializer(String contents, IJavaElement sibling, IProgressMonitor monitor) throws JavaModelException {
	CreateInitializerOperation op = new CreateInitializerOperation(this, contents);
	if (sibling != null) {
		op.createBefore(sibling);
	}
	runOperation(op, monitor);
	return (IInitializer) op.getResultElements()[0];
}
/**
 * @see IType
 */
public IMethod createMethod(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	CreateMethodOperation op = new CreateMethodOperation(this, contents, force);
	if (sibling != null) {
		op.createBefore(sibling);
	}
	runOperation(op, monitor);
	return (IMethod) op.getResultElements()[0];
}
/**
 * @see IType
 */
public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	CreateTypeOperation op = new CreateTypeOperation(this, contents, force);
	if (sibling != null) {
		op.createBefore(sibling);
	}
	runOperation(op, monitor);
	return (IType) op.getResultElements()[0];
}
/**
 * @see JavaElement#equalsDOMNode
 */
protected boolean equalsDOMNode(IDOMNode node) throws JavaModelException {
	return (node.getNodeType() == IDOMNode.TYPE) && super.equalsDOMNode(node);
}
/**
 * @see IMember
 */
public IType getDeclaringType() {
	IJavaElement parent = getParent();
	while (parent != null) {
		if (parent.getElementType() == IJavaElement.TYPE) {
			return (IType) parent;
		} else
			if (parent instanceof IMember) {
				parent = parent.getParent();
			} else {
				return null;
			}
	}
	return null;
}
/**
 * @see IType#getField
 */
public IField getField(String name) {
	return new SourceField(this, name);
}
/**
 * @see IType
 */
public IField[] getFields() throws JavaModelException {
	Vector v= getChildrenOfType(FIELD);
	IField[] array= new IField[v.size()];
	v.copyInto(array);
	return array;
}
/**
 * @see IType
 */
public String getFullyQualifiedName() {
	String packageName = getPackageFragment().getElementName();
	if (packageName.equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
		return getTypeQualifiedName();
	}
	return packageName + '.' + getTypeQualifiedName();
}
/**
 * @see IType
 */
public IInitializer getInitializer(int occurrenceCount) {
	return new Initializer(this, occurrenceCount);
}
/**
 * @see IType
 */
public IInitializer[] getInitializers() throws JavaModelException {
	Vector v= getChildrenOfType(INITIALIZER);
	IInitializer[] array= new IInitializer[v.size()];
	v.copyInto(array);
	return array;
}
/**
 * @see IType#getMethod
 */
public IMethod getMethod(String name, String[] parameterTypeSignatures) {
	return new SourceMethod(this, name, parameterTypeSignatures);
}
/**
 * @see IType
 */
public IMethod[] getMethods() throws JavaModelException {
	Vector v= getChildrenOfType(METHOD);
	IMethod[] array= new IMethod[v.size()];
	v.copyInto(array);
	return array;
}
/**
 * @see IType
 */
public IPackageFragment getPackageFragment() {
	IJavaElement parent = fParent;
	while (parent != null) {
		if (parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
			return (IPackageFragment) parent;
		}
		else {
			parent = parent.getParent();
		}
	}
	Assert.isTrue(false);  // should not happen
	return null;
}
/**
 * @see IType
 */
public String getSuperclassName() throws JavaModelException {
	SourceTypeElementInfo info = (SourceTypeElementInfo) getElementInfo();
	char[] superclassName= info.getSuperclassName();
	if (superclassName == null) {
		return null;
	}
	return new String(superclassName);
}
/**
 * @see IType
 */
public String[] getSuperInterfaceNames() throws JavaModelException {
	SourceTypeElementInfo info = (SourceTypeElementInfo) getElementInfo();
	char[][] names= info.getInterfaceNames();
	if (names == null) {
		return fgEmptyList;
	}
	String[] strings= new String[names.length];
	for (int i= 0; i < names.length; i++) {
		strings[i]= new String(names[i]);
	}
	return strings;
}
/**
 * @see IType
 */
public IType getType(String name) {
	return new SourceType(this, name);
}
/**
 * @see IType
 */
public String getTypeQualifiedName() {
	if (fParent.getElementType() == IJavaElement.COMPILATION_UNIT) {
		return fName;
	} else {
		return ((IType) fParent).getTypeQualifiedName() + '$' + fName;
	}
}
/**
 * @see IType
 */
public IType[] getTypes() throws JavaModelException {
	Vector v= getChildrenOfType(TYPE);
	IType[] array= new IType[v.size()];
	v.copyInto(array);
	return array;
}
/**
 * @see IParent 
 */
public boolean hasChildren() throws JavaModelException {
	return getChildren().length > 0;
}
/**
 * @see IType
 */
public boolean isClass() throws JavaModelException {
	return !isInterface();
}
/**
 * @see IType
 */
public boolean isInterface() throws JavaModelException {
	SourceTypeElementInfo info = (SourceTypeElementInfo) getElementInfo();
	return info.isInterface();
}
/**
 * @see IType
 */
public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, SearchEngine.createWorkspaceScope(), false);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * @see IType
 */
public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, SearchEngine.createWorkspaceScope(), true);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * @see IType
 */
public ITypeHierarchy newTypeHierarchy(IJavaProject project, IProgressMonitor monitor) throws JavaModelException {
	if (project == null) {
		throw new IllegalArgumentException(Util.bind("hierarchy.nullProject"/*nonNLS*/));
	}
	
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(
		this, 
		SearchEngine.createJavaSearchScope(new IResource[]{ project.getProject() }), 
		true);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * See ISourceType.resolveType(...)
 */

 public String[][] resolveType(String typeName) throws JavaModelException {
	ISourceType info = (ISourceType) this.getElementInfo();
	ISearchableNameEnvironment environment = ((JavaProject)getJavaProject()).getSearchableNameEnvironment();

	class TypeResolveRequestor implements ISelectionRequestor {
		String[][] answers = null;
		void acceptType(String[] answer){
			if (answers == null) answers = new String[][]{ answer };
			// grow
			int length = answers.length;
			System.arraycopy(answers, 0, answers = new String[length+1][], 0, length);
			answers[length] = answer;
		}
		public void acceptClass(char[] packageName, char[] className, boolean needQualification) {
			acceptType(new String[]  { new String(packageName), new String(className) });
		}
		
		public void acceptInterface(char[] packageName, char[] interfaceName, boolean needQualification) {
			acceptType(new String[]  { new String(packageName), new String(interfaceName) });
		}

		public void acceptError(IProblem error) {}
		public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name) {}
		public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames) {}
		public void acceptPackage(char[] packageName){}

	}
	TypeResolveRequestor requestor = new TypeResolveRequestor();
	SelectionEngine engine = 
		new SelectionEngine(environment, requestor, JavaModelManager.convertConfigurableOptions(JavaCore.getOptions()));
		
	engine.selectType(info, typeName.toCharArray());
	return requestor.answers;
}
/**
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info) {
	if (info == null) {
		buffer.append(this.getElementName());
		buffer.append(" (not open)"/*nonNLS*/);
	} else {
		try {
			if (this.isInterface()) {
				buffer.append("interface "/*nonNLS*/);
			} else {
				buffer.append("class "/*nonNLS*/);
			}
			buffer.append(this.getElementName());
		} catch (JavaModelException e) {
			buffer.append("<JavaModelException in toString of "/*nonNLS*/ + getElementName());
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1754.java