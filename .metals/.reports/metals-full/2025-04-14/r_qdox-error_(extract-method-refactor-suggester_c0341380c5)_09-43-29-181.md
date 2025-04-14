error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15711.java
text:
```scala
a@@ddClass(classfile.getName().getInternalName(), in2);

/*
 * Closure.java
 * 
 * Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License Version 1.0
 * (the "License"). You may not use this file except in compliance with the
 * License. A copy of the License is available at http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original Code is
 * Sun Microsystems, Inc. Portions Copyright 2000-2001 Sun Microsystems, Inc.
 * All Rights Reserved.
 * 
 * Contributor(s): Thomas Ball
 * 
 * Version: $Revision$
 */

package wapplet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.netbeans.modules.classfile.ClassFile;
import org.netbeans.modules.classfile.ClassName;
import org.netbeans.modules.classfile.ConstantPool;

import wicket.WicketRuntimeException;

/**
 * Closure: report all classes which a given class references in one way or
 * another. Note: this utility won't find classes which are dynamically loaded.
 * 
 * @author Thomas Ball
 * @author Jonathan Locke
 */
// FIXME General: Add this to wicket.util.lang 
public class ClassClosure
{
	/** Closure of classes referenced by the class passed to the constructor */
	Set/* <String> */closure = new HashSet();

	/**
	 * Construct.
	 * 
	 * @param classes
	 *            The root classes to find the closure of
	 * @param includeJDK
	 *            True to include JDK classes
	 */
	public ClassClosure(final List/* <Class> */classes, final boolean includeJDK)
	{
		final Set visited = new HashSet();
		final Stack stack = new Stack();

		for (final Iterator iterator = classes.iterator(); iterator.hasNext();)
		{
			final Class c = (Class)iterator.next();
			final ClassName classname = ClassName.getClassName(c.getName().replace('.', '/'));
			stack.push(classname);
			visited.add(classname.getExternalName());
		}

		while (!stack.empty())
		{
			// Add class to closure.
			ClassName classname = (ClassName)stack.pop();
			InputStream in = inputStreamForClassName(classname.getType());
			try
			{
				ClassFile classfile = new ClassFile(in);
				closure.add(classfile.getName().getExternalName());

				final ConstantPool pool = classfile.getConstantPool();
				final Iterator references = pool.getAllClassNames().iterator();
				while (references.hasNext())
				{
					final ClassName classnameReference = (ClassName)references.next();
					final String name = classnameReference.getExternalName();
					if (name.indexOf('[') != -1)
					{
						// skip arrays
					}
					else if (!includeJDK
							&& (name.startsWith("java.") || name.startsWith("javax.")
 name.startsWith("sun.") || name.startsWith("com.sun.corba")
 name.startsWith("com.sun.image")
 name.startsWith("com.sun.java.swing")
 name.startsWith("com.sun.naming") || name
									.startsWith("com.sun.security")))
					{
						// if directed, skip JDK references
					}
					else
					{
						final boolean isNew = visited.add(name);
						if (isNew)
						{
							stack.push(classnameReference);
						}
					}
				}
				
				// Get the input stream a second time to add to JAR
				final InputStream in2 = inputStreamForClassName(classname.getType());
				try
				{
					addClass(classfile.getName().getExternalName(), in2);
				}
				finally
				{
					in2.close();
				}
			}
			catch (IOException e)
			{
				throw new WicketRuntimeException(e);
			}
			finally
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @return Iterator over all class names in the closure
	 */
	public Iterator/* <String> */dependencies()
	{
		return closure.iterator();
	}

	/**
	 * Called for each class added to the closure
	 * 
	 * @param name
	 *            The class name
	 * @param is
	 *            The input stream to the class
	 */
	protected void addClass(String name, InputStream is)
	{
	}

	/**
	 * @param classname
	 *            The class name
	 * @return Class contents as a stream
	 */
	protected InputStream inputStreamForClassName(String classname)
	{
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(
				classname + ".class");
		if (in == null)
		{
			throw new IllegalStateException("Unable to find class " + classname);
		}
		return in;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15711.java