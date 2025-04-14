error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 611
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6428.java
text:
```scala
public static class BinaryMethodSkeleton implements IBinaryMethod {

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
p@@ackage org.eclipse.jdt.internal.eval;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.IBinaryAnnotation;
import org.eclipse.jdt.internal.compiler.env.IBinaryField;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryNestedType;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * The skeleton of the class 'org.eclipse.jdt.internal.eval.target.CodeSnippet'
 * used at compile time. Note that the method run() is declared to
 * throw Throwable so that the user can write a code snipet that
 * throws checked exceptio without having to catch those.
 */
public class CodeSnippetSkeleton implements IBinaryType, EvaluationConstants {
	IBinaryMethod[] methods = new IBinaryMethod[] {
		new BinaryMethodSkeleton(
			"<init>".toCharArray(), //$NON-NLS-1$
			"()V".toCharArray(), //$NON-NLS-1$
			new char[][] {},
			true
		),
		new BinaryMethodSkeleton(
			"run".toCharArray(), //$NON-NLS-1$
			"()V".toCharArray(), //$NON-NLS-1$
			new char[][] {"java/lang/Throwable".toCharArray()}, //$NON-NLS-1$
			false
		),
		new BinaryMethodSkeleton(
			"setResult".toCharArray(), //$NON-NLS-1$
			"(Ljava/lang/Object;Ljava/lang/Class;)V".toCharArray(), //$NON-NLS-1$
			new char[][] {},
			false
		)
	};

	public class BinaryMethodSkeleton implements IBinaryMethod {
		char[][] exceptionTypeNames;
		char[] methodDescriptor;
		char[] selector;
		boolean isConstructor;
		
		public BinaryMethodSkeleton(char[] selector, char[] methodDescriptor, char[][] exceptionTypeNames, boolean isConstructor) {
			this.selector = selector;
			this.methodDescriptor = methodDescriptor;
			this.exceptionTypeNames = exceptionTypeNames;
			this.isConstructor = isConstructor;
		}
		public char[][] getExceptionTypeNames() {
			return this.exceptionTypeNames;
		}
		public char[] getMethodDescriptor() {
			return this.methodDescriptor;
		}
		public int getModifiers() {
			return ClassFileConstants.AccPublic;
		}
		public char[] getSelector() {
			return this.selector;
		}
		public boolean isClinit() {
			return false;
		}
		public boolean isConstructor() {
			return this.isConstructor;
		}
		public char[][] getArgumentNames() {
			return null;
		}
		public char[] getGenericSignature() {
			return null;
		}
		public long getTagBits() {
			return 0;
		}
		public IBinaryAnnotation[] getAnnotations() {
			return null;
		}
		public IBinaryAnnotation[] getParameterAnnotations(int index) {
			return null;
		}
		public Object getDefaultValue() {
			return null;
		}
}
	
/**
 * CodeSnippetSkeleton constructor comment.
 */
public CodeSnippetSkeleton() {
	super();
}
public char[] getEnclosingTypeName() {
	return null;
}
public IBinaryField[] getFields() {
	return null;
}
/**
 * @see org.eclipse.jdt.internal.compiler.env.IDependent#getFileName()
 */
public char[] getFileName() {
	return CharOperation.concat(CODE_SNIPPET_NAME, Util.defaultJavaExtension().toCharArray());
}
public char[] getGenericSignature() {
	return null;
}
public char[][] getInterfaceNames() {
	return null;
}
public IBinaryNestedType[] getMemberTypes() {
	return null;
}
public IBinaryMethod[] getMethods() {
	return this.methods;
}
public int getModifiers() {
	return ClassFileConstants.AccPublic;
}
public char[] getName() {
	return CODE_SNIPPET_NAME;
}
public char[] getSourceName() {
	return ROOT_CLASS_NAME;
}
public char[] getSuperclassName() {
	return null;
}
public boolean isAnonymous() {
	return false;
}
public boolean isBinaryType() {
	return true;
}
public boolean isLocal() {
	return false;
}
public boolean isMember() {
	return false;
}
public char[] sourceFileName() {
	return null;
}
public IBinaryAnnotation[] getAnnotations() {
	return null;
}
public long getTagBits() {
	return 0;
}
public String getJavadocContents(IProgressMonitor monitor, String defaultEncoding) throws JavaModelException {
	return null;
}
public String getJavadocContents() {
	return null;
}
public String getURLContents(String docUrlValue, String defaultEncoding) {
	return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6428.java