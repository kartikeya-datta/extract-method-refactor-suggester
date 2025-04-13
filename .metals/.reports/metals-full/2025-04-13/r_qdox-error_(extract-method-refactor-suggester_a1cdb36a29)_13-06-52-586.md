error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6922.java
text:
```scala
- p@@roviderForMatching.getName().length() - 1;

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.incubator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @since 3.3
 * 
 */
public abstract class AbstractElement {

	private static final int[][] EMPTY_INDICES = new int[0][0];
	private AbstractProvider provider;

	/**
	 * @param provider
	 */
	public AbstractElement(AbstractProvider provider) {
		super();
		this.provider = provider;
	}

	/**
	 * @return a string containing the first character of every word for camel
	 *         case checking.
	 */
	private static String getCamelCase(String label) {
		StringTokenizer tokenizer = new StringTokenizer(label);
		StringBuffer camelCase = new StringBuffer();
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			camelCase.append(word.charAt(0));
		}
		return camelCase.toString().toLowerCase();
	}

	/**
	 * Returns the label to be displayed to the user.
	 * 
	 * @return the label
	 */
	public abstract String getLabel();

	/**
	 * Returns the image descriptor for this element.
	 * 
	 * @return an image descriptor, or null if no image is available
	 */
	public abstract ImageDescriptor getImageDescriptor();

	/**
	 * Returns the id for this element. The id has to be unique within the
	 * AbstractProvider that provided this element.
	 * 
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * Executes the associated action for this element.
	 */
	public abstract void execute();

	/**
	 * Return the label to be used for sorting and matching elements.
	 * 
	 * @return the sort label
	 */
	public String getSortLabel() {
		return getLabel();
	}

	/**
	 * @return Returns the provider.
	 */
	public AbstractProvider getProvider() {
		return provider;
	}

	/**
	 * @param filter
	 * @return
	 */
	public QuickAccessEntry match(String filter, AbstractProvider providerForMatching) {
		String sortLabel = getSortLabel().toLowerCase();
		int index = sortLabel.indexOf(filter);
		if (index != -1) {
			return new QuickAccessEntry(this, providerForMatching, new int[][] { {
					index, index + filter.length() - 1 } }, EMPTY_INDICES);
		}
		String combinedLabel = (providerForMatching.getName() + " " + getLabel()).toLowerCase(); //$NON-NLS-1$
		index = combinedLabel.indexOf(filter);
		if (index != -1) {
			int lengthOfElementMatch = index + filter.length()
					- providerForMatching.getName().length();
			if (lengthOfElementMatch > 0) {
				return new QuickAccessEntry(this, providerForMatching,
						new int[][] { { 0, lengthOfElementMatch - 1 } },
						new int[][] { { index, index + filter.length() - 1 } });
			}
			return new QuickAccessEntry(this, providerForMatching, EMPTY_INDICES,
					new int[][] { { index, index + filter.length() - 1 } });
		}
		String camelCase = getCamelCase(sortLabel);
		index = camelCase.indexOf(filter);
		if (index != -1) {
			int[][] indices = getCamelCaseIndices(sortLabel, index, filter
					.length());
			return new QuickAccessEntry(this, providerForMatching, indices,
					EMPTY_INDICES);
		}
		String combinedCamelCase = getCamelCase(combinedLabel);
		index = combinedCamelCase.indexOf(filter);
		if (index != -1) {
			String providerCamelCase = getCamelCase(providerForMatching
					.getName());
			int lengthOfElementMatch = index + filter.length()
					- providerCamelCase.length();
			if (lengthOfElementMatch > 0) {
				return new QuickAccessEntry(
						this,
						providerForMatching,
						getCamelCaseIndices(sortLabel, 0, lengthOfElementMatch),
						getCamelCaseIndices(providerForMatching.getName(), index, filter
								.length()
								- lengthOfElementMatch));
			}
			return new QuickAccessEntry(this, providerForMatching, EMPTY_INDICES,
					getCamelCaseIndices(providerForMatching.getName(), index, filter
							.length()));
		}
		return null;
	}

	/**
	 * @param camelCase
	 * @param filter
	 * @param index
	 * @return
	 */
	private int[][] getCamelCaseIndices(String original, int start, int length) {
		List result = new ArrayList();
		int index = 0;
		while (start > 0) {
			index = original.indexOf(' ', index);
			while (original.charAt(index) == ' ') {
				index++;
			}
			start--;
		}
		while (length > 0) {
			result.add(new int[] { index, index });
			index = original.indexOf(' ', index);
			if (index != -1) {
				while (index < original.length()
						&& original.charAt(index) == ' ') {
					index++;
				}
			}
			length--;
		}
		return (int[][]) result.toArray(new int[result.size()][]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6922.java