error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1513.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1513.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1513.java
text:
```scala
W@@orkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) groupsIterator.next();

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * WorkbenchPreferenceGroup is the representation of a category
 * in the workbench.
 */
public class WorkbenchPreferenceGroup {
	
	private String id;
	private String name;
	private String parentGroupId;
	private Collection childGroups = new ArrayList();
	private Collection pages = new ArrayList();
	private Collection pageIds;
	private ImageDescriptor imageDescriptor;
	private Image image;
	private boolean highlight = false;
	private Object lastSelection = null;

	/**
	 * Create a new instance of the receiver.
	 * @param uniqueID The unique id. Must be unique and non null.
	 * @param displayableName The human readable name
	 * @param parentId The id of the parent category.
	 * @param ids
	 * @param icon The ImageDescriptor for the icon for the
	 * receiver. May be <code>null</code>.
	 */
	public WorkbenchPreferenceGroup(String uniqueID, String displayableName, String parentId, Collection ids, ImageDescriptor icon) {
		id = uniqueID;
		name = displayableName;
		parentGroupId = parentId;
		imageDescriptor = icon;
		pageIds = ids;
	}

	/**
	 * Return the id of the parent
	 * @return String
	 */
	public String getParent() {
		return parentGroupId;
	}

	/**
	 * Add the category to the children.
	 * @param category
	 */
	public void addChild(WorkbenchPreferenceGroup category) {
		childGroups.add(category);
		
	}

	/**
	 * Return the id for the receiver.
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Add the node to the list of pages in this category.
	 * @param node
	 */
	public void addNode(WorkbenchPreferenceNode node) {
		pages.add(node);
		
	}
	
	/**
	 * Return the image for the receiver. Return a default
	 * image if there isn't one.
	 * @return Image
	 */
	public Image getImage() {
		
		if(imageDescriptor == null)
			return null;
		
		if(image == null)
			image = imageDescriptor.createImage();
		return image;
	}

	/**
	 * Return the name of the receiver.
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Dispose the resources for the receiver.
	 *
	 */
	public void disposeResources(){
		image.dispose();
		image = null;
	}

	/**
	 * Return the preference nodes in the receiver.
	 * @return IPreferenceNode[]
	 */
	public IPreferenceNode[] getPreferenceNodes() {
		IPreferenceNode[] nodes = new IPreferenceNode[pages.size()];
		pages.toArray(nodes);
		return nodes;
	}

	/**
	 * Return the pageIds for the receiver.
	 * @return Collection
	 */
	public Collection getPageIds() {
		return pageIds;
	}

	/**
	 * Return the children of the receiver.
	 * @return Collection
	 */
	public Collection getChildren() {
		return childGroups;
	}
	
	/**
	 * Return the all of the child groups and
	 * nodes.
	 * @return Collection
	 */
	public Object[] getGroupsAndNodes() {
		Collection allChildren = new ArrayList();
		allChildren.addAll(childGroups);
		allChildren.addAll(pages);
		return allChildren.toArray();
	}

	/**
	 * Add all of the children that match text to the
	 * highlight list.
	 * @param text
	 */
	public void highlightHits(String text) {
		Iterator pagesIterator = pages.iterator();
		Pattern pattern = Pattern.compile( ".*" +text + ".*");//$NON-NLS-1$//$NON-NLS-2$
		
		
		while(pagesIterator.hasNext()){
			WorkbenchPreferenceNode node = (WorkbenchPreferenceNode) pagesIterator.next();
			matchNode(pattern, node);
		}
		
		Iterator groupsIterator = childGroups.iterator();
		
		while(groupsIterator.hasNext()){
			WorkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) pagesIterator.next();
			Matcher m = pattern.matcher(group.getName());
			group.highlight = m.matches();
			group.highlightHits(text);
		}
	}

	/**
	 * Match the node to the pattern and highlight it if there is
	 * a match.
	 * @param pattern
	 * @param node
	 */
	private void matchNode(Pattern pattern, WorkbenchPreferenceNode node) {
		Matcher m = pattern.matcher(node.getLabelText());
		node.setHighlighted(m.matches());
		IPreferenceNode[] children = node.getSubNodes();
		for (int i = 0; i < children.length; i++) {
			matchNode(pattern,(WorkbenchPreferenceNode) children[i]);			
		}
	}

	/**
	 * Return whether or not the receiver is highlighted.
	 * @return Returns the highlight.
	 */
	public boolean isHighlighted() {
		return highlight;
	}
	
	/**
	 * Get the last selected object in this group.
	 * @return Object
	 */
	public Object getLastSelection() {
		return lastSelection;
	}
	/**
	 * Set the last selected object in this group.
	 * @param lastSelection WorkbenchPreferenceGroup
	 * or WorkbenchPreferenceNode.
	 */
	public void setLastSelection(Object lastSelection) {
		this.lastSelection = lastSelection;
	}

	/**
	 * Find the parent of element in the receiver.
	 * If there isn't one return <code>null</null>.
	 * @param element
	 * @return Object or <code>null</null>.
	 */
	public Object findParent(Object element) {
		return findParent(this,element);
	}

	/**
	 * Find the parent of this element starting at this group.
	 * @param group
	 * @param element
	 */
	private Object findParent(WorkbenchPreferenceGroup group, Object element) {
		Iterator pagesIterator = group.pages.iterator();
		while(pagesIterator.hasNext()){
			WorkbenchPreferenceNode next = (WorkbenchPreferenceNode) pagesIterator.next();
			if(next.equals(element))
				return group;
			Object parent = findParent(next,element);
			if(parent != null)
				return parent;
		}
		
		Iterator subGroupsIterator = group.childGroups.iterator();
		while(subGroupsIterator.hasNext()){
			WorkbenchPreferenceGroup next = (WorkbenchPreferenceGroup) subGroupsIterator.next();
			if(next.equals(element))
				return group;
			Object parent = findParent(next,element);
			if(parent != null)
				return parent;
		}
		
		return null;
		
	}

	/**
	 * Find the parent of this element starting at this node.
	 * @param node
	 * @param element
	 * @return Object or <code>null</code>.
	 */
	private Object findParent(IPreferenceNode node, Object element) {
		IPreferenceNode[] subs = node.getSubNodes();
		for (int i = 0; i < subs.length; i++) {
			IPreferenceNode subNode = subs[i];
			if(subNode.equals(element))
				return node;
			Object parent = findParent(subNode,element);
			if(parent != null)
				return parent;			
		}
		return null;
	}

	/**
	 * Add any page ids that match the filteredIds
	 * to the list of highlights.
	 * @param filteredIds
	 */
	public void highlightIds(String[] filteredIds) {
		for (int i = 0; i < filteredIds.length; i++) {
			checkId(filteredIds[i]);
		}
		
	}

	/**
	 * Check the passed id to see if it matches
	 * any of the receivers pages.
	 * @param id
	 */
	private void checkId(String id) {
		Iterator pagesIterator = pages.iterator();
		while(pagesIterator.hasNext()){
			WorkbenchPreferenceNode next = (WorkbenchPreferenceNode) pagesIterator.next();
			checkHighlightNode(id, next);
		}
		
		Iterator childIterator = childGroups.iterator();
		while(childIterator.hasNext()){
			WorkbenchPreferenceGroup group = (WorkbenchPreferenceGroup) childIterator.next();
			group.checkId(id);
		}
	}

	/**
	 * Check if the node matches id and needs to be highlighted.
	 * @param id
	 * @param node
	 * @return <code>true</code> if a match is found
	 */
	private boolean checkHighlightNode(String id, IPreferenceNode node) {
		if(node.getId().equals(id)){
			((WorkbenchPreferenceNode) node).setHighlighted(true);
			return true;
		}
		IPreferenceNode[] subNodes = node.getSubNodes();
		for (int i = 0; i < subNodes.length; i++) {
			if(checkHighlightNode(id,subNodes[i]))
				return true;			
		}
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1513.java