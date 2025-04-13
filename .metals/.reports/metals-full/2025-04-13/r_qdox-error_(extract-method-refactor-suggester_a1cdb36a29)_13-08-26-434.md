error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/252.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/252.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/252.java
text:
```scala
public static final S@@tring DEFAULT_PACKAGE_NAME = ""/*nonNLS*/;

package org.eclipse.jdt.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.resources.IResource;

/**
 * A package fragment is a portion of the workspace corresponding to an entire package,
 * or to a portion thereof. The distinction between a package fragment and a package
 * is that a package with some name is the union of all package fragments in the class path
 * which have the same name.
 * <p>
 * Package fragments elements need to be opened before they can be navigated or manipulated.
 * The children are of type <code>ICompilationUnit</code> (representing a source file) or 
 * <code>IClassFile</code> (representing a binary class file).
 * The children are listed in no particular order.
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IPackageFragment extends IParent, IJavaElement, IOpenable, ISourceManipulation {

	/**	
	 * <p>
	 * The name of package fragment for the default package (value: the empty 
	 * string, <code>""</code>).
	 * </p>
 	*/
	public static final String DEFAULT_PACKAGE_NAME = "";
/**
 * Returns whether this fragment contains at least one Java resource.
 */
boolean containsJavaResources() throws JavaModelException;
/**
 * Creates and returns a compilation unit in this package fragment 
 * with the specified name and contents. No verification is performed
 * on the contents.
 *
 * <p>It is possible that a compilation unit with the same name already exists in this 
 * package fragment.
 * The value of the <code>force</code> parameter effects the resolution of
 * such a conflict:<ul>
 * <li> <code>true</code> - in this case the compilation is created with the new contents</li>
 * <li> <code>false</code> - in this case a <code>JavaModelException</code> is thrown</li>
 * </ul>
 *
 * @exception JavaModelException if the element could not be created. Reasons include:
 * <ul>
 * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
 * <li> A <code>CoreException</code> occurred while creating an underlying resource
 * <li> The name is not a valid compilation unit name (INVALID_NAME)
 * <li> The contents are <code>null</code> (INVALID_CONTENTS)
 * </ul>
 */
ICompilationUnit createCompilationUnit(String name, String contents, boolean force, IProgressMonitor monitor) throws JavaModelException;
/**
 * Returns the class file with the specified name
 * in this package (for example, <code>"Object.class"</code>).
 * The ".class" suffix is required.
 * This is a handle-only method.  The class file may or may not be present.
 */
IClassFile getClassFile(String name);
/**
 * Returns all of the class files in this package fragment.
 *
 * <p>Note: it is possible that a package fragment contains only
 * compilation units (i.e. its kind is <code>K_SOURCE</code>), in
 * which case this method returns an empty collection.
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 */
IClassFile[] getClassFiles() throws JavaModelException;
/**
 * Returns the compilation unit with the specified name
 * in this package (for example, <code>"Object.java"</code>).
 * The ".java" suffix is required.
 * This is a handle-only method.  The compilation unit may or may not be present.
 */
ICompilationUnit getCompilationUnit(String name);
/**
 * Returns all of the compilation units in this package fragment.
 *
 * <p>Note: it is possible that a package fragment contains only
 * class files (i.e. its kind is <code>K_BINARY</code>), in which
 * case this method returns an empty collection.
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 */
ICompilationUnit[] getCompilationUnits() throws JavaModelException;
/**
 * Returns the dot-separated package name of this fragment, for example
 * <code>"java.lang"</code>, or <code>""</code> (the empty string),
 * for the default package.
 */
String getElementName();
/**
 * Returns this package fragment's root kind encoded as an integer.
 * A package fragment can contain <code>.java</code> source files,
 * or <code>.class</code> files. This is a convenience method.
 *
 * @see IPackageFragmentRoot#K_SOURCE
 * @see IPackageFragmentRoot#K_BINARY
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 *
 */
int getKind() throws JavaModelException;
/**
 * Returns an array of non-Java resources contained in this package fragment.
 */
Object[] getNonJavaResources() throws JavaModelException;
/**
 * Returns whether this package fragment's name is
 * a prefix of other package fragments in this package fragment's
 * root.
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 */
boolean hasSubpackages() throws JavaModelException;
/**
 * Returns whether this package fragment is a default package.
 * This is a handle-only method.
 */
boolean isDefaultPackage();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/252.java