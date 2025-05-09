error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/137.java
text:
```scala
t@@his.nsName = (nsName != null && nsName.length() != 0) ? nsName : null;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.dom;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class implements the DOM's NodeList behavior for
 * Element.getElementsByTagName()
 * <P>
 * The DOM describes NodeList as follows:
 * <P>
 * 1) It may represent EITHER nodes scattered through a subtree (when
 * returned by Element.getElementsByTagName), or just the immediate
 * children (when returned by Node.getChildNodes). The latter is easy,
 * but the former (which this class addresses) is more challenging.
 * <P>
 * 2) Its behavior is "live" -- that is, it always reflects the
 * current state of the document tree. To put it another way, the
 * NodeLists obtained before and after a series of insertions and
 * deletions are effectively identical (as far as the user is
 * concerned, the former has been dynamically updated as the changes
 * have been made).
 * <P>
 * 3) Its API accesses individual nodes via an integer index, with the
 * listed nodes numbered sequentially in the order that they were
 * found during a preorder depth-first left-to-right search of the tree.
 * (Of course in the case of getChildNodes, depth is not involved.) As
 * nodes are inserted or deleted in the tree, and hence the NodeList,
 * the numbering of nodes that follow them in the NodeList will
 * change.
 * <P>
 * It is rather painful to support the latter two in the
 * getElementsByTagName case. The current solution is for Nodes to
 * maintain a change count (eventually that may be a Digest instead),
 * which the NodeList tracks and uses to invalidate itself.
 * <P>
 * Unfortunately, this does _not_ respond efficiently in the case that
 * the dynamic behavior was supposed to address: scanning a tree while
 * it is being extended. That requires knowing which subtrees have
 * changed, which can become an arbitrarily complex problem.
 * <P>
 * We save some work by filling the ArrayList only as we access the
 * item()s... but I suspect the same users who demanded index-based
 * access will also start by doing a getLength() to control their loop,
 * blowing this optimization out of the water.
 * <P>
 * NOTE: Level 2 of the DOM will probably _not_ use NodeList for its
 * extended search mechanisms, partly for the reasons just discussed.
 * 
 * @xerces.internal
 *
 * @version $Id$
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeepNodeListImpl 
    implements NodeList {

    //
    // Data
    //

    protected NodeImpl rootNode; // Where the search started
    protected String tagName;   // Or "*" to mean all-tags-acceptable
    protected int changes=0;
    protected ArrayList nodes;
    
    protected String nsName;
    protected boolean enableNS = false;

    //
    // Constructors
    //

    /** Constructor. */
    public DeepNodeListImpl(NodeImpl rootNode, String tagName) {
        this.rootNode = rootNode;
        this.tagName  = tagName;
        nodes = new ArrayList();
    }  

    /** Constructor for Namespace support. */
    public DeepNodeListImpl(NodeImpl rootNode,
                            String nsName, String tagName) {
        this(rootNode, tagName);
        this.nsName = (nsName != null && !nsName.equals("")) ? nsName : null;
        enableNS = true;
    }
    
    //
    // NodeList methods
    //

    /** Returns the length of the node list. */
    public int getLength() {
        // Preload all matching elements. (Stops when we run out of subtree!)
        item(java.lang.Integer.MAX_VALUE);
        return nodes.size();
    }  

    /** Returns the node at the specified index. */
    public Node item(int index) {
    	Node thisNode;

        // Tree changed. Do it all from scratch!
    	if (rootNode.changes() != changes) {
            nodes   = new ArrayList();     
            changes = rootNode.changes();
    	}
    
        // In the cache
    	final int currentSize = nodes.size();
    	if (index < currentSize) {
    	    return (Node)nodes.get(index);
    	}
        // Not yet seen
    	else {
    
            // Pick up where we left off (Which may be the beginning)
    		if (currentSize == 0) { 
    		    thisNode = rootNode;
    		}
    		else {
    		    thisNode = (NodeImpl)(nodes.get(currentSize - 1));
    		}
    
    		// Add nodes up to the one we're looking for
    		while (thisNode != null && index >= nodes.size()) {
    		    thisNode = nextMatchingElementAfter(thisNode);
    		    if (thisNode != null) {
    		        nodes.add(thisNode);
    		    }
    		}

            // Either what we want, or null (not avail.)
		    return thisNode;           
	    }

    } // item(int):Node

    //
    // Protected methods (might be overridden by an extending DOM)
    //

    /** 
     * Iterative tree-walker. When you have a Parent link, there's often no
     * need to resort to recursion. NOTE THAT only Element nodes are matched
     * since we're specifically supporting getElementsByTagName().
     */
    protected Node nextMatchingElementAfter(Node current) {

	    Node next;
	    while (current != null) {
		    // Look down to first child.
		    if (current.hasChildNodes()) {
			    current = (current.getFirstChild());
		    }

		    // Look right to sibling (but not from root!)
		    else if (current != rootNode && null != (next = current.getNextSibling())) {
				current = next;
			}

			// Look up and right (but not past root!)
			else {
				next = null;
				for (; current != rootNode; // Stop when we return to starting point
					current = current.getParentNode()) {

					next = current.getNextSibling();
					if (next != null)
						break;
				}
				current = next;
			}

			// Have we found an Element with the right tagName?
			// ("*" matches anything.)
		    if (current != rootNode 
		        && current != null
		        && current.getNodeType() ==  Node.ELEMENT_NODE) {
			if (!enableNS) {
			    if (tagName.equals("*") ||
				((ElementImpl) current).getTagName().equals(tagName))
			    {
				return current;
			    }
			} else {
			    // DOM2: Namespace logic. 
			    if (tagName.equals("*")) {
				if (nsName != null && nsName.equals("*")) {
				    return current;
				} else {
				    ElementImpl el = (ElementImpl) current;
				    if ((nsName == null
					 && el.getNamespaceURI() == null)
 (nsName != null
					    && nsName.equals(el.getNamespaceURI())))
				    {
					return current;
				    }
				}
			    } else {
				ElementImpl el = (ElementImpl) current;
				if (el.getLocalName() != null
				    && el.getLocalName().equals(tagName)) {
				    if (nsName != null && nsName.equals("*")) {
					return current;
				    } else {
					if ((nsName == null
					     && el.getNamespaceURI() == null)
 (nsName != null &&
						nsName.equals(el.getNamespaceURI())))
					{
					    return current;
					}
				    }
				}
			    }
			}
		    }

		// Otherwise continue walking the tree
	    }

	    // Fell out of tree-walk; no more instances found
	    return null;

    } // nextMatchingElementAfter(int):Node

} // class DeepNodeListImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/137.java