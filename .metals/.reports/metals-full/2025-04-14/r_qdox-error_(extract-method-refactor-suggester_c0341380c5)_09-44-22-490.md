error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2680.java
text:
```scala
i@@f (node.actor.getY() <= high) selectedNodes.add(node);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

/** A tree widget where each node has an icon, actor, and child nodes.
 * <p>
 * The preferred size of the tree is determined by the preferred size of the actors for the expanded nodes.
 * <p>
 * {@link ChangeEvent} is fired when the selected node changes.
 * @author Nathan Sweet */
public class Tree extends WidgetGroup {
	static boolean isMac = System.getProperty("os.name").contains("Mac");

	TreeStyle style;
	final Array<Node> rootNodes = new Array();
	final Array<Node> selectedNodes = new Array();
	float ySpacing = 4, iconSpacingLeft = 2, iconSpacingRight = 2, padding = 0, indentSpacing;
	private float leftColumnWidth, prefWidth, prefHeight;
	private boolean sizeInvalid = true;
	boolean multiSelect = true;
	boolean toggleSelect = true;
	private Node foundNode;
	Node overNode;
	private ClickListener clickListener;

	public Tree (Skin skin) {
		this(skin.get(TreeStyle.class));
	}

	public Tree (Skin skin, String styleName) {
		this(skin.get(styleName, TreeStyle.class));
	}

	public Tree (TreeStyle style) {
		setStyle(style);
		initialize();
	}

	private void initialize () {
		addListener(clickListener = new ClickListener() {
			private boolean isCtrlPressed () {
				if (isMac)
					return Gdx.input.isKeyPressed(Keys.SYM);
				else
					return Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT);
			}

			public void clicked (InputEvent event, float x, float y) {
				Node node = getNodeAt(y);
				if (node == null) return;
				if (node != getNodeAt(getTouchDownY())) return;
				if (multiSelect && selectedNodes.size > 0
					&& (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))) {
					// Select range (shift/ctrl).
					float low = selectedNodes.first().actor.getY();
					float high = node.actor.getY();
					if (!isCtrlPressed()) selectedNodes.clear();
					if (low > high)
						selectNodes(rootNodes, high, low);
					else
						selectNodes(rootNodes, low, high);
					fireChangeEvent();
					return;
				}
				if (!multiSelect || !isCtrlPressed()) {
					if (node.children.size > 0) {
						// Toggle expanded.
						float rowX = node.actor.getX();
						if (node.icon != null) rowX -= iconSpacingRight + node.icon.getMinWidth();
						if (x < rowX) {
							node.setExpanded(!node.expanded);
							return;
						}
					}
					if (!node.isSelectable()) return;
					boolean unselect = toggleSelect && selectedNodes.size == 1 && selectedNodes.contains(node, true);
					selectedNodes.clear();
					if (unselect) {
						fireChangeEvent();
						return;
					}
				} else if (!node.isSelectable()) //
					return;
				// Select single (ctrl).
				if (!selectedNodes.removeValue(node, true)) selectedNodes.add(node);
				fireChangeEvent();
			}

			public boolean mouseMoved (InputEvent event, float x, float y) {
				setOverNode(getNodeAt(y));
				return false;
			}

			public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (toActor == null || !toActor.isDescendantOf(Tree.this)) setOverNode(null);
			}
		});
	}

	public void setStyle (TreeStyle style) {
		this.style = style;
		indentSpacing = Math.max(style.plus.getMinWidth(), style.minus.getMinWidth()) + iconSpacingLeft;
	}

	public void add (Node node) {
		insert(rootNodes.size, node);
	}

	public void insert (int index, Node node) {
		remove(node);
		node.parent = null;
		rootNodes.insert(index, node);
		node.addToTree(this);
		invalidateHierarchy();
	}

	public void remove (Node node) {
		if (node.parent != null) {
			node.parent.remove(node);
			return;
		}
		rootNodes.removeValue(node, true);
		node.removeFromTree(this);
		invalidateHierarchy();
	}

	/** Removes all tree nodes. */
	public void clearChildren () {
		super.clearChildren();
		rootNodes.clear();
		selectedNodes.clear();
		setOverNode(null);
		fireChangeEvent();
	}

	void fireChangeEvent () {
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		fire(changeEvent);
		Pools.free(changeEvent);
	}

	public Array<Node> getNodes () {
		return rootNodes;
	}

	public void invalidate () {
		super.invalidate();
		sizeInvalid = true;
	}

	private void computeSize () {
		sizeInvalid = false;
		prefWidth = style.plus.getMinWidth();
		prefWidth = Math.max(prefWidth, style.minus.getMinWidth());
		prefHeight = getHeight();
		leftColumnWidth = 0;
		computeSize(rootNodes, indentSpacing);
		leftColumnWidth += iconSpacingLeft + padding;
		prefWidth += leftColumnWidth + padding;
		prefHeight = getHeight() - prefHeight;
	}

	private void computeSize (Array<Node> nodes, float indent) {
		float ySpacing = this.ySpacing;
		float spacing = iconSpacingLeft + iconSpacingRight;
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			float rowWidth = indent + iconSpacingRight;
			Actor actor = node.actor;
			if (actor instanceof Layout) {
				Layout layout = (Layout)actor;
				rowWidth += layout.getPrefWidth();
				node.height = layout.getPrefHeight();
				layout.pack();
			} else {
				rowWidth += actor.getWidth();
				node.height = actor.getHeight();
			}
			if (node.icon != null) {
				rowWidth += spacing + node.icon.getMinWidth();
				node.height = Math.max(node.height, node.icon.getMinHeight());
			}
			prefWidth = Math.max(prefWidth, rowWidth);
			prefHeight -= node.height + ySpacing;
			if (node.expanded) computeSize(node.children, indent + indentSpacing);
		}
	}

	public void layout () {
		if (sizeInvalid) computeSize();
		layout(rootNodes, leftColumnWidth + indentSpacing + iconSpacingRight, getHeight() - ySpacing / 2);
	}

	private float layout (Array<Node> nodes, float indent, float y) {
		float ySpacing = this.ySpacing;
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			Actor actor = node.actor;
			float x = indent;
			if (node.icon != null) x += node.icon.getMinWidth();
			y -= node.height;
			node.actor.setPosition(x, y);
			y -= ySpacing;
			if (node.expanded) y = layout(node.children, indent + indentSpacing, y);
		}
		return y;
	}

	public void draw (SpriteBatch batch, float parentAlpha) {
		Color color = getColor();
		if (style.background != null) {
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
			batch.setColor(Color.WHITE);
		}
		draw(batch, rootNodes, leftColumnWidth);
		super.draw(batch, parentAlpha); // Draw actors.
	}

	/** Draws selection, icons, and expand icons. */
	private void draw (SpriteBatch batch, Array<Node> nodes, float indent) {
		Drawable plus = style.plus, minus = style.minus;
		float x = getX(), y = getY();
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			Actor actor = node.actor;

			if (selectedNodes.contains(node, true) && style.selection != null) {
				style.selection.draw(batch, x, y + actor.getY() - ySpacing / 2, getWidth(), node.height + ySpacing);
			} else if (node == overNode && style.over != null) {
				style.over.draw(batch, x, y + actor.getY() - ySpacing / 2, getWidth(), node.height + ySpacing);
			}

			if (node.icon != null) {
				float iconY = actor.getY() + Math.round((node.height - node.icon.getMinHeight()) / 2);
				batch.setColor(actor.getColor());
				node.icon.draw(batch, x + node.actor.getX() - iconSpacingRight - node.icon.getMinWidth(), y + iconY,
					node.icon.getMinWidth(), node.icon.getMinHeight());
				batch.setColor(Color.WHITE);
			}

			if (node.children.size == 0) continue;

			Drawable expandIcon = node.expanded ? minus : plus;
			float iconY = actor.getY() + Math.round((node.height - expandIcon.getMinHeight()) / 2);
			expandIcon.draw(batch, x + indent - iconSpacingLeft, y + iconY, expandIcon.getMinWidth(), expandIcon.getMinHeight());
			if (node.expanded) draw(batch, node.children, indent + indentSpacing);
		}
	}

	/** @return May be null. */
	public Node getNodeAt (float y) {
		foundNode = null;
		getNodeAt(rootNodes, y, getHeight());
		return foundNode;
	}

	private float getNodeAt (Array<Node> nodes, float y, float rowY) {
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			if (y >= rowY - node.height - ySpacing && y < rowY) {
				foundNode = node;
				return -1;
			}
			rowY -= node.height + ySpacing;
			if (node.expanded) {
				rowY = getNodeAt(node.children, y, rowY);
				if (rowY == -1) return -1;
			}
		}
		return rowY;
	}

	void selectNodes (Array<Node> nodes, float low, float high) {
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			if (node.actor.getY() < low) break;
			if (!node.isSelectable()) continue;
			if (node.actor.getY() <= high && !selectedNodes.contains(node, true)) selectedNodes.add(node);
			if (node.expanded) selectNodes(node.children, low, high);
		}
	}

	public Array<Node> getSelection () {
		return selectedNodes;
	}

	public void setSelection (Node node) {
		selectedNodes.clear();
		if (node != null) selectedNodes.add(node);
		fireChangeEvent();
	}

	public void setSelection (Array<Node> nodes) {
		selectedNodes.clear();
		selectedNodes.addAll(nodes);
		fireChangeEvent();
	}

	public void addSelection (Node node) {
		if (node == null) return;
		selectedNodes.add(node);
		fireChangeEvent();
	}

	public void clearSelection () {
		selectedNodes.clear();
		fireChangeEvent();
	}

	public TreeStyle getStyle () {
		return style;
	}

	public Array<Node> getRootNodes () {
		return rootNodes;
	}

	public Node getOverNode () {
		return overNode;
	}

	public void setOverNode (Node overNode) {
		this.overNode = overNode;
	}

	/** Sets the amount of horizontal space between the nodes and the left/right edges of the tree. */
	public void setPadding (float padding) {
		this.padding = padding;
	}

	/** Sets the amount of vertical space between nodes. */
	public void setYSpacing (float ySpacing) {
		this.ySpacing = ySpacing;
	}

	/** Sets the amount of horizontal space between the node actors and icons. */
	public void setIconSpacing (float left, float right) {
		this.iconSpacingLeft = left;
		this.iconSpacingRight = right;
	}

	public float getPrefWidth () {
		if (sizeInvalid) computeSize();
		return prefWidth;
	}

	public float getPrefHeight () {
		if (sizeInvalid) computeSize();
		return prefHeight;
	}

	public void findExpandedObjects (Array objects) {
		findExpandedObjects(rootNodes, objects);
	}

	public void restoreExpandedObjects (Array objects) {
		for (int i = 0, n = objects.size; i < n; i++) {
			Node node = findNode(objects.get(i));
			if (node != null) {
				node.setExpanded(true);
				node.expandTo();
			}
		}
	}

	static boolean findExpandedObjects (Array<Node> nodes, Array objects) {
		boolean expanded = false;
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			if (node.expanded && !findExpandedObjects(node.children, objects)) objects.add(node.object);
		}
		return expanded;
	}

	/** Returns the node with the specified object, or null. */
	public Node findNode (Object object) {
		if (object == null) throw new IllegalArgumentException("object cannot be null.");
		return findNode(rootNodes, object);
	}

	static Node findNode (Array<Node> nodes, Object object) {
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			if (object.equals(node.object)) return node;
		}
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			Node found = findNode(node.children, object);
			if (found != null) return found;
		}
		return null;
	}

	public void collapseAll () {
		collapseAll(rootNodes);
	}

	static void collapseAll (Array<Node> nodes) {
		for (int i = 0, n = nodes.size; i < n; i++) {
			Node node = nodes.get(i);
			node.setExpanded(false);
			collapseAll(node.children);
		}
	}

	public void expandAll () {
		expandAll(rootNodes);
	}

	static void expandAll (Array<Node> nodes) {
		for (int i = 0, n = nodes.size; i < n; i++)
			nodes.get(i).expandAll();
	}

	/** Returns the click listener the tree uses for clicking on nodes and the over node. */
	public ClickListener getClickListener () {
		return clickListener;
	}

	public void setMultiSelect (boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public void setToggleSelect (boolean toggleSelect) {
		this.toggleSelect = toggleSelect;
	}

	static public class Node {
		Actor actor;
		Node parent;
		final Array<Node> children = new Array(0);
		boolean selectable = true;
		boolean expanded;
		Drawable icon;
		float height;
		Object object;

		public Node (Actor actor) {
			if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
			this.actor = actor;
		}

		public void setExpanded (boolean expanded) {
			if (expanded == this.expanded) return;
			this.expanded = expanded;
			if (children.size == 0) return;
			Tree tree = getTree();
			if (tree == null) return;
			if (expanded) {
				for (int i = 0, n = children.size; i < n; i++)
					children.get(i).addToTree(tree);
			} else {
				for (int i = 0, n = children.size; i < n; i++)
					children.get(i).removeFromTree(tree);
			}
			tree.invalidateHierarchy();
		}

		/** Called to add the actor to the tree when the node's parent is expanded. */
		protected void addToTree (Tree tree) {
			tree.addActor(actor);
			if (!expanded) return;
			for (int i = 0, n = children.size; i < n; i++)
				children.get(i).addToTree(tree);
		}

		/** Called to remove the actor from the tree when the node's parent is collapsed. */
		protected void removeFromTree (Tree tree) {
			tree.removeActor(actor);
			if (!expanded) return;
			for (int i = 0, n = children.size; i < n; i++)
				children.get(i).removeFromTree(tree);
		}

		public void add (Node node) {
			insert(children.size, node);
		}

		public void addAll (Array<Node> nodes) {
			for (int i = 0, n = nodes.size; i < n; i++)
				insert(children.size, nodes.get(i));
		}

		public void insert (int index, Node node) {
			node.parent = this;
			children.insert(index, node);
			updateChildren();
		}

		public void remove () {
			Tree tree = getTree();
			if (tree != null)
				tree.remove(this);
			else if (parent != null) //
				parent.remove(this);
		}

		public void remove (Node node) {
			children.removeValue(node, true);
			if (!expanded) return;
			Tree tree = getTree();
			if (tree == null) return;
			node.removeFromTree(tree);
			if (children.size == 0) expanded = false;
		}

		public void removeAll () {
			Tree tree = getTree();
			if (tree != null) {
				for (int i = 0, n = children.size; i < n; i++)
					children.get(i).removeFromTree(tree);
			}
			children.clear();
		}

		/** Returns the tree this node is currently in, or null. */
		public Tree getTree () {
			Group parent = actor.getParent();
			if (!(parent instanceof Tree)) return null;
			return (Tree)parent;
		}

		public Actor getActor () {
			return actor;
		}

		public boolean isExpanded () {
			return expanded;
		}

		/** If the children order is changed, {@link #updateChildren()} must be called. */
		public Array<Node> getChildren () {
			return children;
		}

		public void updateChildren () {
			if (!expanded) return;
			Tree tree = getTree();
			if (tree == null) return;
			for (int i = 0, n = children.size; i < n; i++)
				children.get(i).addToTree(tree);
		}

		/** @return May be null. */
		public Node getParent () {
			return parent;
		}

		/** Sets an icon that will be drawn to the left of the actor. */
		public void setIcon (Drawable icon) {
			this.icon = icon;
		}

		public Object getObject () {
			return object;
		}

		/** Sets an application specific object for this node. */
		public void setObject (Object object) {
			this.object = object;
		}

		public Drawable getIcon () {
			return icon;
		}

		/** Returns this node or the child node with the specified object, or null. */
		public Node findNode (Object object) {
			if (object == null) throw new IllegalArgumentException("object cannot be null.");
			if (object.equals(this.object)) return this;
			return Tree.findNode(children, object);
		}

		/** Collapses all nodes under and including this node. */
		public void collapseAll () {
			setExpanded(false);
			Tree.collapseAll(children);
		}

		/** Expands all nodes under and including this node. */
		public void expandAll () {
			setExpanded(true);
			if (children.size > 0) Tree.expandAll(children);
		}

		/** Expands all parent nodes of this node. */
		public void expandTo () {
			Node node = parent;
			while (node != null) {
				node.setExpanded(true);
				node = node.parent;
			}
		}

		public boolean isSelectable () {
			return selectable;
		}

		public void setSelectable (boolean selectable) {
			this.selectable = selectable;
		}

		public void findExpandedObjects (Array objects) {
			if (expanded && !Tree.findExpandedObjects(children, objects)) objects.add(object);
		}

		public void restoreExpandedObjects (Array objects) {
			for (int i = 0, n = objects.size; i < n; i++) {
				Node node = findNode(objects.get(i));
				if (node != null) {
					node.setExpanded(true);
					node.expandTo();
				}
			}
		}
	}

	/** The style for a {@link Tree}.
	 * @author Nathan Sweet */
	static public class TreeStyle {
		public Drawable plus, minus;
		/** Optional. */
		public Drawable over, selection, background;

		public TreeStyle () {
		}

		public TreeStyle (Drawable plus, Drawable minus, Drawable selection) {
			this.plus = plus;
			this.minus = minus;
			this.selection = selection;
		}

		public TreeStyle (TreeStyle style) {
			this.plus = style.plus;
			this.minus = style.minus;
			this.selection = style.selection;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2680.java