error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7155.java
text:
```scala
i@@nt redistribute = subtract(preferredWidth, total);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Randy Hudson <hudsonr@us.ibm.com>
 *     - Fix for bug 19524 - Resizing WorkbenchWindow resizes Views
 *     Cagatay Kavukcuoglu <cagatayk@acm.org>
 *     - Fix for bug 10025 - Resizing views should not use height ratios
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.util.ArrayList;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.IPageLayout;

/**
 * Implementation of a tree node. The node represents a
 * sash and it allways has two children.
 */
public class LayoutTreeNode extends LayoutTree {
	
	static class ChildSizes {
		int left;
		int right;
		
		public ChildSizes (int l, int r) {
			left = l;
			right = r;
		}
	};
	
    /* The node children witch may be another node or a leaf */
    private LayoutTree children[] = new LayoutTree[2];

    /* The sash's width when vertical and hight on horizontal */
    final static int SASH_WIDTH = 3;

    /**
     * Initialize this tree with its sash.
     */
    public LayoutTreeNode(LayoutPartSash sash) {
        super(sash);
    }

    /**
     * Traverses the tree to find the part that intersects the given point
     * 
     * @param toFind
     * @return the part that intersects the given point
     */
    public LayoutPart findPart(Point toFind) {
        if (!children[0].isVisible()) {
            if (!children[1].isVisible()) {
                return null;
            }

            return children[1].findPart(toFind);
        } else {
            if (!children[1].isVisible()) {
                return children[0].findPart(toFind);
            }
        }

        LayoutPartSash sash = getSash();

        Rectangle bounds = sash.getBounds();

        if (sash.isVertical()) {
            if (toFind.x < bounds.x + (bounds.width / 2)) {
                return children[0].findPart(toFind);
            }
            return children[1].findPart(toFind);
        } else {
            if (toFind.y < bounds.y + (bounds.height / 2)) {
                return children[0].findPart(toFind);
            }
            return children[1].findPart(toFind);
        }
    }

    /**
     * Add the relation ship between the children in the list
     * and returns the left children.
     */
    public LayoutPart computeRelation(ArrayList relations) {
        PartSashContainer.RelationshipInfo r = new PartSashContainer.RelationshipInfo();
        r.relative = children[0].computeRelation(relations);
        r.part = children[1].computeRelation(relations);
        r.left = getSash().getLeft();
        r.right = getSash().getRight();
        r.relationship = getSash().isVertical() ? IPageLayout.RIGHT
                : IPageLayout.BOTTOM;
        relations.add(0, r);
        return r.relative;
    }

    /**
     * Dispose all Sashs in this tree
     */
    public void disposeSashes() {
        children[0].disposeSashes();
        children[1].disposeSashes();
        getSash().dispose();
        flushCache();
    }

    /**
     * Find a LayoutPart in the tree and return its sub-tree. Returns
     * null if the child is not found.
     */
    public LayoutTree find(LayoutPart child) {
        LayoutTree node = children[0].find(child);
        if (node != null)
            return node;
        node = children[1].find(child);
        return node;
    }

    /**
     * Find the part that is in the bottom right position.
     */
    public LayoutPart findBottomRight() {
        if (children[1].isVisible())
            return children[1].findBottomRight();
        return children[0].findBottomRight();
    }

    /**
     * Go up in the tree finding a parent that is common of both children.
     * Return the subtree.
     */
    public LayoutTreeNode findCommonParent(LayoutPart child1, LayoutPart child2) {
        return findCommonParent(child1, child2, false, false);
    }

    /**
     * Go up in the tree finding a parent that is common of both children.
     * Return the subtree.
     */
    LayoutTreeNode findCommonParent(LayoutPart child1, LayoutPart child2,
            boolean foundChild1, boolean foundChild2) {
        if (!foundChild1)
            foundChild1 = find(child1) != null;
        if (!foundChild2)
            foundChild2 = find(child2) != null;
        if (foundChild1 && foundChild2)
            return this;
        if (parent == null)
            return null;
        return parent
                .findCommonParent(child1, child2, foundChild1, foundChild2);
    }

    /**
     * Find a sash in the tree and return its sub-tree. Returns
     * null if the sash is not found.
     */
    public LayoutTreeNode findSash(LayoutPartSash sash) {
        if (this.getSash() == sash)
            return this;
        LayoutTreeNode node = children[0].findSash(sash);
        if (node != null)
            return node;
        node = children[1].findSash(sash);
        if (node != null)
            return node;
        return null;
    }

    /**
     * Sets the elements in the array of sashes with the
     * Left,Rigth,Top and Botton sashes. The elements
     * may be null depending whether there is a shash
     * beside the <code>part</code>
     */
    void findSashes(LayoutTree child, PartPane.Sashes sashes) {
        Sash sash = (Sash) getSash().getControl();
        boolean leftOrTop = children[0] == child;
        if (sash != null) {
            LayoutPartSash partSash = getSash();
            //If the child is in the left, the sash 
            //is in the rigth and so on.
            if (leftOrTop) {
                if (partSash.isVertical()) {
                    if (sashes.right == null)
                        sashes.right = sash;
                } else {
                    if (sashes.bottom == null)
                        sashes.bottom = sash;
                }
            } else {
                if (partSash.isVertical()) {
                    if (sashes.left == null)
                        sashes.left = sash;
                } else {
                    if (sashes.top == null)
                        sashes.top = sash;
                }
            }
        }
        if (getParent() != null)
            getParent().findSashes(this, sashes);
    }

    /**
     * Returns the sash of this node.
     */
    public LayoutPartSash getSash() {
        return (LayoutPartSash) part;
    }

    /**
     * Returns true if this tree has visible parts otherwise returns false.
     */
    public boolean isVisible() {
        return children[0].isVisible() || children[1].isVisible();
    }

    /**
     * Remove the child and this node from the tree
     */
    LayoutTree remove(LayoutTree child) {
        getSash().dispose();
        if (parent == null) {
            //This is the root. Return the other child to be the new root.
            if (children[0] == child) {
                children[1].setParent(null);
                return children[1];
            }
            children[0].setParent(null);
            return children[0];
        }

        LayoutTreeNode oldParent = parent;
        if (children[0] == child)
            oldParent.replaceChild(this, children[1]);
        else
            oldParent.replaceChild(this, children[0]);
        return oldParent;
    }

    /**
     * Replace a child with a new child and sets the new child's parent.
     */
    void replaceChild(LayoutTree oldChild, LayoutTree newChild) {
        if (children[0] == oldChild)
            children[0] = newChild;
        else if (children[1] == oldChild)
            children[1] = newChild;
        newChild.setParent(this);
        if (!children[0].isVisible() || !children[0].isVisible())
            getSash().dispose();

        flushCache();
    }

    /**
     * Go up from the subtree and return true if all the sash are 
     * in the direction specified by <code>isVertical</code>
     */
    public boolean sameDirection(boolean isVertical, LayoutTreeNode subTree) {
        boolean treeVertical = getSash().isVertical();
        if (treeVertical != isVertical)
            return false;
        while (subTree != null) {
            if (this == subTree)
                return true;
            if (subTree.children[0].isVisible()
                    && subTree.children[1].isVisible())
                if (subTree.getSash().isVertical() != isVertical)
                    return false;
            subTree = subTree.getParent();
        }
        return true;
    }
    
    public int doComputePreferredSize(boolean width, int availableParallel, int availablePerpendicular, int preferredParallel) {
    	assertValidSize(availablePerpendicular);
    	assertValidSize(availableParallel);
    	assertValidSize(preferredParallel);
    	
    	// If one child is invisible, defer to the other child
    	if (!children[0].isVisible()) {
    		return children[1].computePreferredSize(width, availableParallel, availablePerpendicular, preferredParallel);
    	}
    	
    	if (!children[1].isVisible()) {
    		return children[0].computePreferredSize(width, availableParallel, availablePerpendicular, preferredParallel);
    	}
    	
    	if (availableParallel == 0) {
    		return 0;
    	}
    	
    	// If computing the dimension perpendicular to our sash
    	if (width == getSash().isVertical()) {
    		// Compute the child sizes
    		ChildSizes sizes = computeChildSizes(availableParallel, availablePerpendicular,
    				getSash().getLeft(), getSash().getRight(), preferredParallel);
    		
    		// Return the sum of the child sizes plus the sash size
    		return add(sizes.left, add(sizes.right, SASH_WIDTH));
    	} else {
    		// Computing the dimension parallel to the sash. We will compute and return the preferred size
    		// of whichever child is closest to the ideal size.
    		
    		ChildSizes sizes;
    		// First compute the dimension of the child sizes perpendicular to the sash
			sizes = computeChildSizes(availablePerpendicular, availableParallel,
				getSash().getLeft(), getSash().getRight(), availablePerpendicular);
    		
    		// Use this information to compute the dimension of the child sizes parallel to the sash.
    		// Return the preferred size of whichever child is closest to the ideal size
    		int leftSize = children[0].computePreferredSize(width, availableParallel, sizes.left, preferredParallel);
    		
    		// If the preferred size of the left child *IS* the ideal size, then there's no way the right child
    		// can do better. Return the ideal size.
    		if (leftSize == preferredParallel) {
    			assertValidSize(leftSize);
    			return leftSize;
    		}
    		
    		// Compute the preferred size of the right child
    		int rightSize = children[1].computePreferredSize(width, availableParallel, sizes.right, preferredParallel); 
    		
    		// Return leftSize or rightSize: whichever one is closest to the ideal size
    		int result = rightSize;
    		if (leftSize > rightSize) {
    			result = leftSize;
    		}

    		assertValidSize(result);
    		
    		return result;
    	}
    }
	
    /**
     * Computes the pixel sizes of this node's children, given the available
     * space for this node. Note that "width" and "height" actually refer
     * to the distance perpendicular and parallel to the sash respectively.
     * That is, their meaning is reversed when computing a horizontal sash. 
     * 
     * @param width the pixel width of a vertical node, or the pixel height
     * of a horizontal node (INFINITE if unbounded)
     * @param height the pixel height of a vertical node, or the pixel width
     * of a horizontal node (INFINITE if unbounded)
     * @return a struct describing the pixel sizes of the left and right children
     * (this is a width for horizontal nodes and a height for vertical nodes)
     */
    ChildSizes computeChildSizes(int width, int height, int left, int right, int preferredWidth) {
    	Assert.isTrue(children[0].isVisible());
    	Assert.isTrue(children[1].isVisible());
    	assertValidSize(width);
    	assertValidSize(height);
    	assertValidSize(preferredWidth);
    	Assert.isTrue(left >= 0);
    	Assert.isTrue(right >= 0);
    	Assert.isTrue(preferredWidth >= 0);
    	Assert.isTrue(preferredWidth <= width);
    	boolean vertical = getSash().isVertical();
    	
        if (width <= SASH_WIDTH) {
        	return new ChildSizes(0,0);
        }
        
        if (width == INFINITE) {
        	if (preferredWidth == INFINITE) {
        		return new ChildSizes(children[0].computeMaximumSize(vertical, height),
        				children[1].computeMaximumSize(vertical, height));
        	}
        	
        	if (preferredWidth == 0) {
        		return new ChildSizes(children[0].computeMinimumSize(vertical, height),
        				children[1].computeMinimumSize(vertical, height));
        	}
        }
        
        int total = left + right;

        // Use all-or-none weighting
        double wLeft = left, wRight = right;
        switch (getCompressionBias()) {
        case -1:
            wLeft = 0.0;
            break;
        case 1:
            wRight = 0.0;
            break;
        default:
            break;
        }
        double wTotal = wLeft + wRight;
                
        preferredWidth = Math.max(0, subtract(preferredWidth, SASH_WIDTH));
        width = Math.max(0, subtract(width, SASH_WIDTH));
        int redistribute = preferredWidth - total;
        
    	int leftMinimum = 0;
    	
    	if (children[0].hasSizeFlag(vertical, SWT.MIN)) {
    	    leftMinimum = children[0].computeMinimumSize(vertical, height);
    	}
    	
    	int rightMinimum = 0;
    	
    	if (children[1].hasSizeFlag(vertical, SWT.MIN)) {
    	    rightMinimum = children[1].computeMinimumSize(vertical, height);
    	}
    	
    	int leftMaximum = Math.max(0, subtract(width, rightMinimum));
    	int rightMaximum = Math.max(0, subtract(width, leftMinimum));

    	if (children[0].hasSizeFlag(vertical, SWT.MAX)) {
    		leftMaximum = Math.min(leftMaximum, children[0].computeMaximumSize(vertical, height));
    	}

    	if (children[1].hasSizeFlag(vertical, SWT.MAX)) {
    		rightMaximum = Math.min(rightMaximum, children[1].computeMaximumSize(vertical, height));
    	}
    	
        // First figure out the ideal sizes for each child
    	int idealLeft = Math.max(leftMinimum, Math.min(preferredWidth,  
    			left + (int) Math.round(redistribute * wLeft / wTotal)));
    	
    	idealLeft = Math.max(idealLeft, preferredWidth - rightMaximum);
    	idealLeft = Math.min(idealLeft, leftMaximum);

    	if (children[0].hasSizeFlag(vertical, SWT.FILL)) {
    		idealLeft = children[0].computePreferredSize(vertical, leftMaximum, height, idealLeft);
    	}
    	idealLeft = Math.max(idealLeft, leftMinimum);
    	
    	int idealRight = Math.max(rightMinimum, preferredWidth - idealLeft);
    	
		rightMaximum = Math.max(0, Math.min(rightMaximum, subtract(width, idealLeft)));
    	idealRight = Math.min(idealRight, rightMaximum);
    	
    	if (children[1].hasSizeFlag(vertical, SWT.FILL)) {
    		idealRight = children[1].computePreferredSize(vertical, rightMaximum, height, idealRight);
    	}
    	idealRight = Math.max(idealRight, rightMinimum);
    	
    	return new ChildSizes(idealLeft, idealRight);    	    	
    }
    
    protected int doGetSizeFlags(boolean width) {
        if (!children[0].isVisible()) {
            return children[1].getSizeFlags(width);
        }
        
        if (!children[1].isVisible()) {
            return children[0].getSizeFlags(width);
        }
        
        int leftFlags = children[0].getSizeFlags(width);
        int rightFlags = children[1].getSizeFlags(width);
        
        return ((leftFlags | rightFlags) & ~SWT.MAX) | (leftFlags & rightFlags & SWT.MAX);
    }
	
    /**
     * Resize the parts on this tree to fit in <code>bounds</code>.
     */
    public void doSetBounds(Rectangle bounds) {
        if (!children[0].isVisible()) {
            children[1].setBounds(bounds);
            return;
        }
        if (!children[1].isVisible()) {
            children[0].setBounds(bounds);
            return;
        }
        
        bounds = Geometry.copy(bounds);
        
        boolean vertical = getSash().isVertical();

        // If this is a horizontal sash, flip coordinate systems so 
        // that we can eliminate special cases
        if (!vertical) {
        	Geometry.flipXY(bounds);
        }

        ChildSizes childSizes = computeChildSizes(bounds.width, bounds.height, getSash().getLeft(), getSash().getRight(), bounds.width);
        
        Rectangle leftBounds = new Rectangle(bounds.x, bounds.y, childSizes.left, bounds.height);
        Rectangle sashBounds = new Rectangle(leftBounds.x + leftBounds.width, bounds.y, SASH_WIDTH, bounds.height);
        Rectangle rightBounds = new Rectangle(sashBounds.x + sashBounds.width, bounds.y, childSizes.right, bounds.height);
        
        if (!vertical) {
        	Geometry.flipXY(leftBounds);
        	Geometry.flipXY(sashBounds);
        	Geometry.flipXY(rightBounds);
        }
        
        getSash().setBounds(sashBounds);
        children[0].setBounds(leftBounds);
        children[1].setBounds(rightBounds);
    }

    //Added by hudsonr@us.ibm.com - bug 19524

    public boolean isCompressible() {
        return children[0].isCompressible() || children[1].isCompressible();
    }

    /**
     * Returns 0 if there is no bias. Returns -1 if the first child should be of
     * fixed size, and the second child should be compressed. Returns 1 if the
     * second child should be of fixed size.
     * @return the bias
     */
    public int getCompressionBias() {
        boolean left = children[0].isCompressible();
        boolean right = children[1].isCompressible();
        if (left == right)
            return 0;
        if (right)
            return -1;
        return 1;
    }
	
    boolean isLeftChild(LayoutTree toTest) {
        return children[0] == toTest;
    }

    LayoutTree getChild(boolean left) {
        int index = left ? 0 : 1;
        return (children[index]);
    }

    /**
     * Sets a child in this node
     */
    void setChild(boolean left, LayoutPart part) {
        LayoutTree child = new LayoutTree(part);
        setChild(left, child);
        flushCache();
    }

    /**
     * Sets a child in this node
     */
    void setChild(boolean left, LayoutTree child) {
        int index = left ? 0 : 1;
        children[index] = child;
        child.setParent(this);
        flushCache();
    }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        String s = "<null>\n";//$NON-NLS-1$
        if (part.getControl() != null)
            s = "<@" + part.getControl().hashCode() + ">\n";//$NON-NLS-2$//$NON-NLS-1$
        String result = "["; //$NON-NLS-1$
        if (children[0].getParent() != this)
            result = result + "{" + children[0] + "}" + s;//$NON-NLS-2$//$NON-NLS-1$
        else
            result = result + children[0] + s;

        if (children[1].getParent() != this)
            result = result + "{" + children[1] + "}]";//$NON-NLS-2$//$NON-NLS-1$
        else
            result = result + children[1] + "]";//$NON-NLS-1$
        return result;
    }

    /**
     * Create the sashes if the children are visible
     * and dispose it if they are not.
     */
    public void updateSashes(Composite parent) {
        if (parent == null)
            return;
        children[0].updateSashes(parent);
        children[1].updateSashes(parent);
        if (children[0].isVisible() && children[1].isVisible())
            getSash().createControl(parent);
        else
            getSash().dispose();
    }

    /**
     * Writes a description of the layout to the given string buffer.
     * This is used for drag-drop test suites to determine if two layouts are the
     * same. Like a hash code, the description should compare as equal iff the
     * layouts are the same. However, it should be user-readable in order to
     * help debug failed tests. Although these are english readable strings,
     * they should not be translated or equality tests will fail.
     * 
     * @param buf
     */
    public void describeLayout(StringBuffer buf) {
        if (!(children[0].isVisible())) {
            if (!children[1].isVisible()) {
                return;
            }

            children[1].describeLayout(buf);
            return;
        }

        if (!children[1].isVisible()) {
            children[0].describeLayout(buf);
            return;
        }

        buf.append("("); //$NON-NLS-1$
        children[0].describeLayout(buf);

        buf.append(getSash().isVertical() ? "|" : "-"); //$NON-NLS-1$ //$NON-NLS-2$

        children[1].describeLayout(buf);
        buf.append(")"); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7155.java