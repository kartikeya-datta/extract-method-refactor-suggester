error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2.java
text:
```scala
public static final S@@tring ATT_CATEGORY = "category"; //$NON-NLS-1$

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
package org.eclipse.ui.internal.registry;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The CategorizedPageRegistryReader is the abstract super class
 * of registry readers for page that have categorization.
 */
public abstract class CategorizedPageRegistryReader extends RegistryReader {

	static final String ATT_CATEGORY = "category"; //$NON-NLS-1$

	static final String PREFERENCE_SEPARATOR = "/"; //$NON-NLS-1$

	List topLevelNodes;

	private static final Comparator comparer = new Comparator() {
		private Collator collator = Collator.getInstance();

		public int compare(Object arg0, Object arg1) {
			String s1 = ((CategoryNode) arg0).getFlatCategory();
			String s2 = ((CategoryNode) arg1).getFlatCategory();
			return collator.compare(s1, s2);
		}
	};

	/**
	 * Internal class used to sort all the preference page nodes
	 * based on the category.
	 */
	abstract class CategoryNode {
		/**
		 * Comment for <code>reader</code>
		 */
		private final CategorizedPageRegistryReader reader;

		//private WorkbenchPreferenceNode node;

		private String flatCategory;

		/**
		 * Default constructor
		 */
		public CategoryNode(CategorizedPageRegistryReader reader) {
			this.reader = reader;
		}

		/**
		 * Return the flatten category
		 */
		public String getFlatCategory() {
			if (flatCategory == null) {
				initialize();
				if (flatCategory == null)
					flatCategory = getLabelText();
			}
			return flatCategory;
		}

		/**
		 * Get the label text for this node.
		 * @return String
		 */
		abstract String getLabelText();

		/*
		 * Initialize the flat category to include the parents'
		 * category names and the current node's label
		 */
		private void initialize() {
			String category = reader.getCategory(getNode());
			if (category == null)
				return;

			StringBuffer sb = new StringBuffer();
			StringTokenizer stok = new StringTokenizer(category, PREFERENCE_SEPARATOR);
			Object immediateParent = null;
			while (stok.hasMoreTokens()) {
				String pathID = stok.nextToken();
				immediateParent = this.reader.findNode(pathID);
				if (immediateParent == null)
					return;
				if (sb.length() > 0)
					sb.append(PREFERENCE_SEPARATOR);
				sb.append(getLabelText(immediateParent));
			}

			if (sb.length() > 0)
				sb.append(PREFERENCE_SEPARATOR);
			sb.append(getLabelText());
			flatCategory = sb.toString();
		}

		/**
		 * Return the label text for the passed element.
		 * @param element
		 * @return String
		 */
		abstract String getLabelText(Object element);

		/**
		 * Get the node the receiver represents.
		 * @return Object
		 */
		abstract Object getNode();
	}

	/**
	 * Create a new instance of the receiver.
	 */
	public CategorizedPageRegistryReader() {
		super();
	}

	/**
	 * Process the preference page nodes.
	 */
	void processNodes() {
		topLevelNodes = new ArrayList();
		//root nodes (which contain subnodes)

		//Add root nodes to the contributions vector	
		StringTokenizer tokenizer;
		String currentToken;

		// Make the advisor's favorite the first category
		Object favorite = null;
		String favoriteId = getFavoriteNodeId();
		if (favoriteId != null) {
			favorite = findNode(favoriteId);
		}
		if (favorite != null) {
			topLevelNodes.add(favorite);
		}

		// Sort nodes based on flattened display path composed of
		// actual labels of nodes referenced in category attribute.
		Object[] sortedNodes = sortByCategories(getNodes());
		for (int i = 0; i < sortedNodes.length; i++) {
			//Iterate through all the nodes
			CategoryNode categoryNode = (CategoryNode) sortedNodes[i];
			Object node = categoryNode.getNode();
			if (node == favorite) {
				// skip it - favorite already at the top of the list
				continue;
			}
			String category = getCategory(node);
			if (category == null) {
				topLevelNodes.add(node);
				continue;
			}
			// has category
			tokenizer = new StringTokenizer(category, PREFERENCE_SEPARATOR);
			Object parent = null;
			while (tokenizer.hasMoreElements()) {
				currentToken = tokenizer.nextToken();
				Object child = null;
				if (parent == null)
					child = findNode(currentToken);
				else
					child = findNode(parent, currentToken);
				if (child == null) {
					parent = null;
					break;
				} else {
					parent = child;
				}
			}
			if (parent != null) {
				add(parent, node);
			} else {
				//Could not find the parent - log
				WorkbenchPlugin
						.log("Invalid preference page path: " + categoryNode.getFlatCategory()); //$NON-NLS-1$
				topLevelNodes.add(node);
			}
		}
	}

	/**
	 * Get the category for the node if there is one. If there
	 * isn't return <code>null</code>.
	 * @param node
	 * @return String or <code>null</code>.
	 */
	abstract String getCategory(Object node);

	/**
	 * Add the node to the parent.
	 * @param parent
	 * @param node
	 */
	abstract void add(Object parent, Object node);

	/**
	 * Get the nodes for the receiver.
	 * @return Collection of Object
	 */
	abstract Collection getNodes();

	/**
	 * Return the id of the favorite node or <code>null</code>
	 * if there isn't one.
	 * @return String
	 */
	abstract String getFavoriteNodeId();

	/**
	 * Sort the nodes based on full category + name. Category used for sorting
	 * is created by substituting node IDs with labels of the referenced
	 * nodes. workbench node is excluded from sorting because it always
	 * appears first in the dialog.
	 */
	Object[] sortByCategories(Collection nodesToCategorize) {
		//sort by categories
		List nodes = new ArrayList();

		Iterator nodesIterator = nodesToCategorize.iterator();
		while (nodesIterator.hasNext()) {
			nodes.add(createCategoryNode(this, nodesIterator.next()));
		}

		Collections.sort(nodes, comparer);
		return nodes.toArray();
	}

	/**
	 * Create a node for categorization from the reader 
	 * and the supplied object.
	 * @param reader
	 * @param object
	 * @return CategoryNode
	 */
	abstract CategoryNode createCategoryNode(CategorizedPageRegistryReader reader, Object object);

	/**
	 * Searches for the top-level node with the given id.
	 * @param id
	 * @return Object of the type being categorized or
	 * <code>null</code>
	 */
	abstract Object findNode(String id);

	/**
	 * Find the node with the given parent with the id
	 * of currentToken.
	 * @param parent
	 * @param currentToken
	 * @return
	 */
	abstract Object findNode(Object parent, String currentToken);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2.java