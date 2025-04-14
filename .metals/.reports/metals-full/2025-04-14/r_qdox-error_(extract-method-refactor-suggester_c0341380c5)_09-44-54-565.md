error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/767.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/767.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/767.java
text:
```scala
i@@nt mid= list[left + (right - left) / 2];

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
package org.eclipse.jdt.internal.compiler.parser.diagnose;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;

public class RangeUtil {
	
	// flags
	public static final int NO_FLAG = 0;
	public static final int LBRACE_MISSING = 1;
	public static final int IGNORE = 2;
	
	static class RangeResult {
		private static final int INITIAL_SIZE = 10;
		int pos;
		int[] intervalStarts;
		int[] intervalEnds;
		int[] intervalFlags;
		
		RangeResult() {
			this.pos = 0;
			this.intervalStarts = new int[INITIAL_SIZE];
			this.intervalEnds = new int[INITIAL_SIZE];
			this.intervalFlags = new int[INITIAL_SIZE];
		}
		
		void addInterval(int start, int end){
			addInterval(start, end, NO_FLAG);
		}
		
		void addInterval(int start, int end, int flags){
			if(pos >= intervalStarts.length) {
				System.arraycopy(intervalStarts, 0, intervalStarts = new int[pos * 2], 0, pos);
				System.arraycopy(intervalEnds, 0, intervalEnds = new int[pos * 2], 0, pos);
				System.arraycopy(intervalFlags, 0, intervalFlags = new int[pos * 2], 0, pos);
			}
			intervalStarts[pos] = start;
			intervalEnds[pos] = end;
			intervalFlags[pos] = flags;
			pos++;
		}
		
		int[][] getRanges() {
			int[] resultStarts = new int[pos];
			int[] resultEnds = new int[pos];
			int[] resultFlags = new int[pos];
			
			System.arraycopy(intervalStarts, 0, resultStarts, 0, pos);
			System.arraycopy(intervalEnds, 0, resultEnds, 0, pos);
			System.arraycopy(intervalFlags, 0, resultFlags, 0, pos);

			if (resultStarts.length > 1) {
				quickSort(resultStarts, resultEnds, resultFlags, 0, resultStarts.length - 1);
			}
			return new int[][]{resultStarts, resultEnds, resultFlags};
		}
		
		private void quickSort(int[] list, int[] list2, int[] list3, int left, int right) {
			int original_left= left;
			int original_right= right;
			int mid= list[(left + right) / 2];
			do {
				while (compare(list[left], mid) < 0) {
					left++;
				}
				while (compare(mid, list[right]) < 0) {
					right--;
				}
				if (left <= right) {
					int tmp= list[left];
					list[left]= list[right];
					list[right]= tmp;
					
					tmp = list2[left];
					list2[left]= list2[right];
					list2[right]= tmp;
					
					tmp = list3[left];
					list3[left]= list3[right];
					list3[right]= tmp;
					
					left++;
					right--;
				}
			} while (left <= right);
			
			if (original_left < right) {
				quickSort(list, list2, list3, original_left, right);
			}
			if (left < original_right) {
				quickSort(list, list2, list3, left, original_right);
			}
		}
		
		private int compare(int i1, int i2) {
			return i1 - i2;
		}
	}
	
	
	
	public static boolean containsErrorInSignature(AbstractMethodDeclaration method){
		return method.sourceEnd + 1 == method.bodyStart	|| method.bodyEnd == method.declarationSourceEnd;
	}

	public static int[][] computeDietRange(TypeDeclaration[] types) {
		if(types == null || types.length == 0) {
			return new int[3][0];
		} else {
			RangeResult result = new RangeResult();
			computeDietRange0(types, result);
			return result.getRanges();
		}
	}
	
	private static void computeDietRange0(TypeDeclaration[] types, RangeResult result) {
		for (int j = 0; j < types.length; j++) {
			//members
			TypeDeclaration[] memberTypeDeclarations = types[j].memberTypes;
			if(memberTypeDeclarations != null && memberTypeDeclarations.length > 0) {
				computeDietRange0(types[j].memberTypes, result);
			}
			//methods
			AbstractMethodDeclaration[] methods = types[j].methods;
			if (methods != null) {
				int length = methods.length;
				for (int i = 0; i < length; i++) {
					AbstractMethodDeclaration method = methods[i];
					if(containsIgnoredBody(method)) {
						if(containsErrorInSignature(method)) {
							method.errorInSignature = true;
							result.addInterval(method.declarationSourceStart, method.declarationSourceEnd, IGNORE);
						} else {
							int flags = method.sourceEnd + 1 == method.bodyStart ? LBRACE_MISSING : NO_FLAG;
							result.addInterval(method.bodyStart, method.bodyEnd, flags);
						}
					}
				}
			}
	
			//initializers
			FieldDeclaration[] fields = types[j].fields;
			if (fields != null) {
				int length = fields.length;
				for (int i = 0; i < length; i++) {
					if (fields[i] instanceof Initializer) {
						Initializer initializer = (Initializer)fields[i];
						if(initializer.declarationSourceEnd == initializer.bodyEnd && initializer.declarationSourceStart != initializer.declarationSourceEnd){
							initializer.errorInSignature = true;
							result.addInterval(initializer.declarationSourceStart, initializer.declarationSourceEnd, IGNORE);
						} else {
							result.addInterval(initializer.bodyStart, initializer.bodyEnd);
						}
					}
				}
			}
		}
	}
		
	public static boolean containsIgnoredBody(AbstractMethodDeclaration method){
		return !method.isDefaultConstructor()
			&& !method.isClinit()
			&& (method.modifiers & ExtraCompilerModifiers.AccSemicolonBody) == 0;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/767.java