error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7411.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7411.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 588
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7411.java
text:
```scala
public static final class PropertyLocation {

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
p@@ackage org.eclipse.jdt.internal.core.dom.rewrite;

import java.util.*;

import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer;
import org.eclipse.text.edits.TextEditGroup;


/**
 * Stores all rewrite events, descriptions of events and knows which nodes
 * are copy or move sources or tracked.
 */
public final class RewriteEventStore {
	

	public final class PropertyLocation {
		private final ASTNode parent;
		private final StructuralPropertyDescriptor property;
		
		public PropertyLocation(ASTNode parent, StructuralPropertyDescriptor property) {
			this.parent= parent;
			this.property= property;
		}

		public ASTNode getParent() {
			return this.parent;
		}

		public StructuralPropertyDescriptor getProperty() {
			return this.property;
		}
		
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass().equals(this.getClass())) {
				PropertyLocation other= (PropertyLocation) obj;
				return other.getParent().equals(getParent()) && other.getProperty().equals(getProperty());
			}
			return false;
		}
		
		public int hashCode() {
			return getParent().hashCode() + getProperty().hashCode();
		}
		
	}
	
	/**
	 * Interface that allows to override the way how children are accessed from
	 * a parent. Use this interface when the rewriter is set up on an already
	 * modified AST's (as it is the case in the old ASTRewrite infrastructure)
	 */
	public static interface INodePropertyMapper {
		/**
		 * Returns the node attribute for a given property name. 
		 * @param parent The parent node
		 * @param childProperty The child property to access 
		 * @return The child node at the given property location.
		 */
		Object getOriginalValue(ASTNode parent, StructuralPropertyDescriptor childProperty);
	}
	
	/*
	 * Store element to associate event and node position/
	 */
	private static class EventHolder {
		public final ASTNode parent;
		public final StructuralPropertyDescriptor childProperty;
		public final RewriteEvent event;
		
		public EventHolder(ASTNode parent, StructuralPropertyDescriptor childProperty, RewriteEvent change) {
			this.parent= parent;
			this.childProperty= childProperty;
			this.event= change;
		}
		
		public String toString() {
			StringBuffer buf= new StringBuffer();
			buf.append(this.parent).append(" - "); //$NON-NLS-1$
			buf.append(this.childProperty.getId()).append(": "); //$NON-NLS-1$
			buf.append(this.event).append('\n');
			return buf.toString();
		}
	}
	
	public static class CopySourceInfo implements Comparable {
		public final PropertyLocation location; // can be null, only used to mark as removed on move
		private final ASTNode node;
		public final boolean isMove;
		
		public CopySourceInfo(PropertyLocation location, ASTNode node, boolean isMove) {
			this.location= location;
			this.node= node;
			this.isMove= isMove;
		}
		
		public ASTNode getNode() {
			return this.node;
		}
		
		public int compareTo(Object o2) {
			CopySourceInfo r2= (CopySourceInfo) o2;
		
			int startDiff= this.getNode().getStartPosition() - r2.getNode().getStartPosition();
			if (startDiff != 0) {
				return startDiff; // insert before if start node is first
			}

			if (r2.isMove != this.isMove) {
				return this.isMove ? -1 : 1; // first move then copy
			}
			return 0;
		}
		
		public String toString() {
			StringBuffer buf= new StringBuffer();
			if (this.isMove) {
				buf.append("move source: "); //$NON-NLS-1$
			} else {
				buf.append("copy source: "); //$NON-NLS-1$
			}
			buf.append(this.node);
			return buf.toString();
		}
	}
	
	private static class NodeRangeInfo implements Comparable {
		private final ASTNode first;
		private final ASTNode last;
		public final CopySourceInfo copyInfo; // containing the internal placeholder and the 'isMove' flag
		public final ASTNode replacingNode;
		public final TextEditGroup editGroup;
		
		public NodeRangeInfo(ASTNode parent, StructuralPropertyDescriptor childProperty, ASTNode first, ASTNode last, CopySourceInfo copyInfo, ASTNode replacingNode, TextEditGroup editGroup) {
			this.first= first;
			this.last= last;
			this.copyInfo= copyInfo;
			this.replacingNode= replacingNode;
			this.editGroup= editGroup;
		}
		
		public ASTNode getStartNode() {
			return this.first;
		}
		
		public ASTNode getEndNode() {
			return this.last;
		}
		
		public boolean isMove() {
			return this.copyInfo.isMove;
		}
		
		public Block getInternalPlaceholder() {
			return (Block) this.copyInfo.getNode();
		}
				
		public int compareTo(Object o2) {
			NodeRangeInfo r2= (NodeRangeInfo) o2;
		
			int startDiff= this.getStartNode().getStartPosition() - r2.getStartNode().getStartPosition();
			if (startDiff != 0) {
				return startDiff; // insert before if start node is first
			}
			int endDiff= this.getEndNode().getStartPosition() - r2.getEndNode().getStartPosition();
			if (endDiff != 0) {
				return -endDiff; // insert before if length is longer
			}
			if (r2.isMove() != this.isMove()) {
				return this.isMove() ? -1 : 1; // first move then copy
			}
			return 0;
		}
		
		public void updatePlaceholderSourceRanges(TargetSourceRangeComputer sourceRangeComputer) {
			TargetSourceRangeComputer.SourceRange startRange= sourceRangeComputer.computeSourceRange(getStartNode());
			TargetSourceRangeComputer.SourceRange endRange= sourceRangeComputer.computeSourceRange(getEndNode());
			int startPos= startRange.getStartPosition();
			int endPos= endRange.getStartPosition() + endRange.getLength();
			
			Block internalPlaceholder= getInternalPlaceholder();
			internalPlaceholder.setSourceRange(startPos, endPos - startPos);
		}
		
		public String toString() {
			StringBuffer buf= new StringBuffer();
			if (this.first != this.last) {
				buf.append("range ");  //$NON-NLS-1$
			}
			if (isMove()) {
				buf.append("move source: "); //$NON-NLS-1$
			} else {
				buf.append("copy source: "); //$NON-NLS-1$
			}
			buf.append(this.first);
			buf.append(" - "); //$NON-NLS-1$
			buf.append(this.last);
			return buf.toString();
		}


	}
	
	/**
	 * Iterates over all event parent nodes, tracked nodes and all copy/move sources 
	 */
	private class ParentIterator implements Iterator {
		
		private Iterator eventIter;
		private Iterator sourceNodeIter;
		private Iterator rangeNodeIter;
		private Iterator trackedNodeIter;
		
		public ParentIterator() {
			this.eventIter= RewriteEventStore.this.events.iterator();
			if (RewriteEventStore.this.nodeCopySources != null) {
				this.sourceNodeIter= RewriteEventStore.this.nodeCopySources.iterator();
			} else {
				this.sourceNodeIter= Collections.EMPTY_LIST.iterator();
			}
			if (RewriteEventStore.this.nodeRangeInfos != null) {
				this.rangeNodeIter= RewriteEventStore.this.nodeRangeInfos.keySet().iterator();
			} else {
				this.rangeNodeIter= Collections.EMPTY_LIST.iterator();
			}
			if (RewriteEventStore.this.trackedNodes != null) {
				this.trackedNodeIter= RewriteEventStore.this.trackedNodes.keySet().iterator();
			} else {
				this.trackedNodeIter= Collections.EMPTY_LIST.iterator();
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return this.eventIter.hasNext() || this.sourceNodeIter.hasNext() || this.rangeNodeIter.hasNext() || this.trackedNodeIter.hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public Object next() {
			if (this.eventIter.hasNext()) {
				return ((EventHolder) this.eventIter.next()).parent;
			}
			if (this.sourceNodeIter.hasNext()) {
				return ((CopySourceInfo) this.sourceNodeIter.next()).getNode();
			}
			if (this.rangeNodeIter.hasNext()) {
				return ((PropertyLocation) this.rangeNodeIter.next()).getParent();
			}
			return this.trackedNodeIter.next();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public final static int NEW= 1;
	public final static int ORIGINAL= 2;
	public final static int BOTH= NEW | ORIGINAL;
		
	
	/** all events */
	final List events;
	
	/** cache for last accessed event */
	private EventHolder lastEvent;
	
	/** Maps events to group descriptions */
	private Map editGroups;
		
	/** Stores which nodes are source of a copy or move (list of CopySourceInfo)*/
	List nodeCopySources;
	
	/** Stores node ranges that are used to copy or move (map of <PropertyLocation, CopyRangeInfo>)*/
	Map nodeRangeInfos;
	
	/** Stores which nodes are tracked and the corresponding edit group*/
	Map trackedNodes;
	
	/** Stores which inserted nodes bound to the previous node. If not, a node is
	 * always bound to the next node */
	private Set insertBoundToPrevious;
	
	/** optional mapper to allow fix already modified AST trees */
	private INodePropertyMapper nodePropertyMapper;
	
	private static final String INTERNAL_PLACEHOLDER_PROPERTY= "rewrite_internal_placeholder"; //$NON-NLS-1$
		
	public RewriteEventStore() {
		this.events= new ArrayList();
		this.lastEvent= null;
		
		this.editGroups= null; // lazy initialization
		
		this.trackedNodes= null;
		this.insertBoundToPrevious= null;
		
		this.nodePropertyMapper= null;
		this.nodeCopySources= null;
		this.nodeRangeInfos= null;
	}
	
	/**
	 * Override the default way how to access children from a parent node.
	 * @param nodePropertyMapper The new <code>INodePropertyMapper</code> or
	 * <code>null</code>. to use the default.
	 */
	public void setNodePropertyMapper(INodePropertyMapper nodePropertyMapper) {
		this.nodePropertyMapper= nodePropertyMapper;
	}
	
	public void clear() {
		this.events.clear();
		this.lastEvent= null;
		this.trackedNodes= null;
		
		this.editGroups= null; // lazy initialization
		this.insertBoundToPrevious= null;
		this.nodeCopySources= null;
	}
	
	public void addEvent(ASTNode parent, StructuralPropertyDescriptor childProperty, RewriteEvent event) {
		validateHasChildProperty(parent, childProperty);
		
		if (event.isListRewrite()) {
			validateIsListProperty(childProperty);
		}
		
		EventHolder holder= new EventHolder(parent, childProperty, event);
		
		// check if already in list
		for (int i= 0; i < this.events.size(); i++) {
			EventHolder curr= (EventHolder) this.events.get(i);
			if (curr.parent == parent && curr.childProperty == childProperty) {
				this.events.set(i, holder);
				this.lastEvent= null;
				return;
			}
		}
		this.events.add(holder);
	}
	
	public RewriteEvent getEvent(ASTNode parent, StructuralPropertyDescriptor property) {
		validateHasChildProperty(parent, property);
		
		if (this.lastEvent != null && this.lastEvent.parent == parent && this.lastEvent.childProperty == property) {
			return this.lastEvent.event;
		}
		
		for (int i= 0; i < this.events.size(); i++) {
			EventHolder holder= (EventHolder) this.events.get(i);
			if (holder.parent == parent && holder.childProperty == property) {
				this.lastEvent= holder;
				return holder.event;
			}
		}
		return null;
	}
	
	public NodeRewriteEvent getNodeEvent(ASTNode parent, StructuralPropertyDescriptor childProperty, boolean forceCreation) {
		validateIsNodeProperty(childProperty);
		NodeRewriteEvent event= (NodeRewriteEvent) getEvent(parent, childProperty);
		if (event == null && forceCreation) {
			Object originalValue= accessOriginalValue(parent, childProperty);
			event= new NodeRewriteEvent(originalValue, originalValue);
			addEvent(parent, childProperty, event);
		}
		return event;		
	}
	
	public ListRewriteEvent getListEvent(ASTNode parent, StructuralPropertyDescriptor childProperty, boolean forceCreation) {
		validateIsListProperty(childProperty);
		ListRewriteEvent event= (ListRewriteEvent) getEvent(parent, childProperty);
		if (event == null && forceCreation) {
			List originalValue= (List) accessOriginalValue(parent, childProperty);
			event= new ListRewriteEvent(originalValue);
			addEvent(parent, childProperty, event);
		}
		return event;
	}
	
	public Iterator getChangeRootIterator() {
		return new ParentIterator();
	}
	
	
	public boolean hasChangedProperties(ASTNode parent) {
		for (int i= 0; i < this.events.size(); i++) {
			EventHolder holder= (EventHolder) this.events.get(i);
			if (holder.parent == parent) {
				if (holder.event.getChangeKind() != RewriteEvent.UNCHANGED) {
					return true;
				}
			}
		}
		return false;
	}
	
	public PropertyLocation getPropertyLocation(Object value, int kind) {
		for (int i= 0; i < this.events.size(); i++) {
			EventHolder holder= (EventHolder) this.events.get(i);
			RewriteEvent event= holder.event;
			if (isNodeInEvent(event, value, kind)) {
				return new PropertyLocation(holder.parent, holder.childProperty);
			}
			if (event.isListRewrite()) {
				RewriteEvent[] children= event.getChildren();
				for (int k= 0; k < children.length; k++) {
					if (isNodeInEvent(children[k], value, kind)) {
						return new PropertyLocation(holder.parent, holder.childProperty);
					}
				}
			}
		}
		if (value instanceof ASTNode) {
			ASTNode node= (ASTNode) value;
			return new PropertyLocation(node.getParent(), node.getLocationInParent()); 
		}
		return null;
	}
	
	
	/**
	 * Kind is either ORIGINAL, NEW, or BOTH
	 * @param value
	 * @param kind
	 * @return Returns the event with the given value of <code>null</code>.
	 */
	public RewriteEvent findEvent(Object value, int kind) {
		for (int i= 0; i < this.events.size(); i++) {
			RewriteEvent event= ((EventHolder) this.events.get(i)).event;
			if (isNodeInEvent(event, value, kind)) {
				return event;
			}
			if (event.isListRewrite()) {
				RewriteEvent[] children= event.getChildren();
				for (int k= 0; k < children.length; k++) {
					if (isNodeInEvent(children[k], value, kind)) {
						return children[k];
					}
				}
			}
		}
		return null;
	}
	
	private boolean isNodeInEvent(RewriteEvent event, Object value, int kind) {
		if (((kind & NEW) != 0) && event.getNewValue() == value) {
			return true;
		}
		if (((kind & ORIGINAL) != 0) && event.getOriginalValue() == value) {
			return true;
		}
		return false;
	}
	
	
	public Object getOriginalValue(ASTNode parent, StructuralPropertyDescriptor property) {
		RewriteEvent event= getEvent(parent, property);
		if (event != null) {
			return event.getOriginalValue();
		}
		return accessOriginalValue(parent, property);
	}
	
	public Object getNewValue(ASTNode parent, StructuralPropertyDescriptor property) {
		RewriteEvent event= getEvent(parent, property);
		if (event != null) {
			return event.getNewValue();
		}
		return accessOriginalValue(parent, property);
	}
	
	public int getChangeKind(ASTNode node) {
		RewriteEvent event= findEvent(node, ORIGINAL);
		if (event != null) {
			return event.getChangeKind();
		}
		return RewriteEvent.UNCHANGED;
	}
	
	/*
	 * Gets an original child from the AST.
	 * Temporarily overridden to port the old rewriter to the new infrastructure.
	 */
	private Object accessOriginalValue(ASTNode parent, StructuralPropertyDescriptor childProperty) {
		if (this.nodePropertyMapper != null) {
			return this.nodePropertyMapper.getOriginalValue(parent, childProperty);
		}
		
		return parent.getStructuralProperty(childProperty);
	}	
	
	public TextEditGroup getEventEditGroup(RewriteEvent event) {
		if (this.editGroups == null) {
			return null;
		}
		return (TextEditGroup) this.editGroups.get(event);
	}
	
	public void setEventEditGroup(RewriteEvent event, TextEditGroup editGroup) {
		if (this.editGroups == null) {
			this.editGroups= new IdentityHashMap(5);
		}	
		this.editGroups.put(event, editGroup);
	}
	
	
	public final TextEditGroup getTrackedNodeData(ASTNode node) {
		if (this.trackedNodes != null) {
			return (TextEditGroup) this.trackedNodes.get(node);
		}
		return null;	
	}
	
	public void setTrackedNodeData(ASTNode node, TextEditGroup editGroup) {
		if (this.trackedNodes == null) {
			this.trackedNodes= new IdentityHashMap();
		}
		this.trackedNodes.put(node, editGroup);
	}
	
	/**
	 * Marks a node as tracked. The edits added to the group editGroup can be used to get the
	 * position of the node after the rewrite operation.
	 * @param node The node to track
	 * @param editGroup Collects the range markers describing the node position.
	 */
	public final void markAsTracked(ASTNode node, TextEditGroup editGroup) {
		if (getTrackedNodeData(node) != null) {
			throw new IllegalArgumentException("Node is already marked as tracked"); //$NON-NLS-1$
		}
		setTrackedNodeData(node, editGroup);
	}	
	
	private final CopySourceInfo createCopySourceInfo(PropertyLocation location, ASTNode node, boolean isMove) {
		CopySourceInfo copySource= new CopySourceInfo(location, node, isMove);
		
		if (this.nodeCopySources == null) {
			this.nodeCopySources= new ArrayList();
		}
		this.nodeCopySources.add(copySource);
		return copySource;
	}
	
	public final CopySourceInfo markAsCopySource(ASTNode parent, StructuralPropertyDescriptor property, ASTNode node, boolean isMove) {
		return createCopySourceInfo(new PropertyLocation(parent, property), node, isMove);
	}
	
	public final boolean isRangeCopyPlaceholder(ASTNode node) {
		return node.getProperty(INTERNAL_PLACEHOLDER_PROPERTY) != null;
	}
	
	public final CopySourceInfo createRangeCopy(ASTNode parent, StructuralPropertyDescriptor childProperty, ASTNode first, ASTNode last, boolean isMove, ASTNode internalPlaceholder, ASTNode replacingNode, TextEditGroup editGroup) {
		CopySourceInfo copyInfo= createCopySourceInfo(null, internalPlaceholder, isMove);
		internalPlaceholder.setProperty(INTERNAL_PLACEHOLDER_PROPERTY, internalPlaceholder);
		
		NodeRangeInfo copyRangeInfo= new NodeRangeInfo(parent, childProperty, first, last, copyInfo, replacingNode, editGroup);
		
		ListRewriteEvent listEvent= getListEvent(parent, childProperty, true);
		
		int indexFirst= listEvent.getIndex(first, ListRewriteEvent.OLD);
		if (indexFirst == -1) {
			throw new IllegalArgumentException("Start node is not a original child of the given list"); //$NON-NLS-1$
		}
		int indexLast= listEvent.getIndex(last, ListRewriteEvent.OLD);
		if (indexLast == -1) {
			throw new IllegalArgumentException("End node is not a original child of the given list"); //$NON-NLS-1$
		}

		if (indexFirst > indexLast) {
			throw new IllegalArgumentException("Start node must be before end node"); //$NON-NLS-1$
		}
		
		if (this.nodeRangeInfos == null) {
			this.nodeRangeInfos= new HashMap();
		}
		PropertyLocation loc= new PropertyLocation(parent, childProperty);
		List innerList= (List) this.nodeRangeInfos.get(loc);
		if (innerList == null) {
			innerList= new ArrayList(2);
			this.nodeRangeInfos.put(loc, innerList);
		} else {
			assertNoOverlap(listEvent, indexFirst, indexLast, innerList);
		}
		innerList.add(copyRangeInfo);
		
		
		return copyInfo;
	}
	
	public CopySourceInfo[] getNodeCopySources(ASTNode node) {
		if (this.nodeCopySources == null) {
			return null;
		}
		return internalGetCopySources(this.nodeCopySources, node);
	}
	
	
	public CopySourceInfo[] internalGetCopySources(List copySources, ASTNode node) {
		ArrayList res= new ArrayList(3);
		for (int i= 0; i < copySources.size(); i++) {
			CopySourceInfo curr= (CopySourceInfo) copySources.get(i);
			if (curr.getNode() == node) {
				res.add(curr);
			}
		}
		if (res.isEmpty()) {
			return null;
		}
		
		CopySourceInfo[] arr= (CopySourceInfo[]) res.toArray(new CopySourceInfo[res.size()]);
		Arrays.sort(arr);
		return arr;
	}
	
	
	private void assertNoOverlap(ListRewriteEvent listEvent, int indexFirst, int indexLast, List innerList) {
		for (Iterator iter= innerList.iterator(); iter.hasNext();) {
			NodeRangeInfo curr= (NodeRangeInfo) iter.next();
			int currStart= listEvent.getIndex(curr.getStartNode(), ListRewriteEvent.BOTH);
			int currEnd= listEvent.getIndex(curr.getEndNode(), ListRewriteEvent.BOTH);
			if (currStart < indexFirst && currEnd < indexLast && currEnd >= indexFirst
 currStart > indexFirst && currStart <= currEnd && currEnd > indexLast) {
				throw new IllegalArgumentException("Range overlapps with an existing copy or move range"); //$NON-NLS-1$ 
			}
		}
	}
	
	public void prepareMovedNodes(TargetSourceRangeComputer sourceRangeComputer) {
		if (this.nodeCopySources != null) {
			prepareSingleNodeCopies();
		}
		
		if (this.nodeRangeInfos != null) {
			prepareNodeRangeCopies(sourceRangeComputer);
		}
	}
	
	public void revertMovedNodes() {
		if (this.nodeRangeInfos != null) {
			removeMoveRangePlaceholders();
		}
	}
	
	private void removeMoveRangePlaceholders() {
		for (Iterator iter= this.nodeRangeInfos.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry= (Map.Entry) iter.next();
			Set placeholders= new HashSet(); // collect all placeholders
			List rangeInfos= (List) entry.getValue(); // list of CopySourceRange
			for (int i= 0; i < rangeInfos.size(); i++) {
				placeholders.add(((NodeRangeInfo) rangeInfos.get(i)).getInternalPlaceholder());
			}
			
			PropertyLocation loc= (PropertyLocation) entry.getKey();
			
			RewriteEvent[] children= getListEvent(loc.getParent(), loc.getProperty(), true).getChildren();
			List revertedChildren= new ArrayList();
			revertListWithRanges(children, placeholders, revertedChildren);
			RewriteEvent[] revertedChildrenArr= (RewriteEvent[]) revertedChildren.toArray(new RewriteEvent[revertedChildren.size()]);
			addEvent(loc.getParent(), loc.getProperty(), new ListRewriteEvent(revertedChildrenArr)); // replace the current edits
		}	
	}
	
	private void revertListWithRanges(RewriteEvent[] childEvents, Set placeholders, List revertedChildren) {
		for (int i= 0; i < childEvents.length; i++) {
			RewriteEvent event= childEvents[i];
			ASTNode node= (ASTNode) event.getOriginalValue();
			if (placeholders.contains(node)) {
				RewriteEvent[] placeholderChildren= getListEvent(node, Block.STATEMENTS_PROPERTY, false).getChildren();
				revertListWithRanges(placeholderChildren, placeholders, revertedChildren);
			} else {
				revertedChildren.add(event);
			}
		}
	}

	private void prepareNodeRangeCopies(TargetSourceRangeComputer sourceRangeComputer) {
		for (Iterator iter= this.nodeRangeInfos.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry= (Map.Entry) iter.next();
			List rangeInfos= (List) entry.getValue(); // list of CopySourceRange
			Collections.sort(rangeInfos); // sort by start index, length, move or copy
			
			PropertyLocation loc= (PropertyLocation) entry.getKey();
			RewriteEvent[] children= getListEvent(loc.getParent(), loc.getProperty(), true).getChildren();
			
			RewriteEvent[] newChildren= processListWithRanges(rangeInfos, children, sourceRangeComputer);
			addEvent(loc.getParent(), loc.getProperty(), new ListRewriteEvent(newChildren)); // replace the current edits
		}		
	}

	private RewriteEvent[] processListWithRanges(List rangeInfos, RewriteEvent[] childEvents, TargetSourceRangeComputer sourceRangeComputer) {
		List newChildEvents= new ArrayList(childEvents.length);
		NodeRangeInfo topInfo= null;
		Stack newChildrenStack= new Stack();
		Stack topInfoStack= new Stack();

		Iterator rangeInfoIterator= rangeInfos.iterator();
		NodeRangeInfo nextInfo= (NodeRangeInfo) rangeInfoIterator.next();
		
		for (int k= 0; k < childEvents.length; k++) {
			RewriteEvent event= childEvents[k];
			ASTNode node= (ASTNode) event.getOriginalValue();
			// check for ranges and add a placeholder for them
			while (nextInfo != null && node == nextInfo.getStartNode()) { // is this child the beginning of a range?
				nextInfo.updatePlaceholderSourceRanges(sourceRangeComputer);
				
				Block internalPlaceholder= nextInfo.getInternalPlaceholder();
				RewriteEvent newEvent;
				if (nextInfo.isMove()) {
					newEvent= new NodeRewriteEvent(internalPlaceholder, nextInfo.replacingNode); // remove or replace
				} else {
					newEvent= new NodeRewriteEvent(internalPlaceholder, internalPlaceholder); // unchanged
				}
				newChildEvents.add(newEvent);
				if (nextInfo.editGroup != null) {
					setEventEditGroup(newEvent, nextInfo.editGroup);
				}
				
				newChildrenStack.push(newChildEvents);
				topInfoStack.push(topInfo);
				
				newChildEvents= new ArrayList(childEvents.length);
				topInfo= nextInfo;
				
				nextInfo= rangeInfoIterator.hasNext() ? (NodeRangeInfo) rangeInfoIterator.next() : null;
			}
			
			newChildEvents.add(event);

			while (topInfo != null && node == topInfo.getEndNode()) {
				RewriteEvent[] placeholderChildEvents= (RewriteEvent[]) newChildEvents.toArray(new RewriteEvent[newChildEvents.size()]);
				Block internalPlaceholder= topInfo.getInternalPlaceholder();
				addEvent(internalPlaceholder, Block.STATEMENTS_PROPERTY, new ListRewriteEvent(placeholderChildEvents));
				
				newChildEvents= (List) newChildrenStack.pop();
				topInfo= (NodeRangeInfo) topInfoStack.pop();
			}
		}
		return (RewriteEvent[]) newChildEvents.toArray(new RewriteEvent[newChildEvents.size()]);
	}

	/**
	 * Make sure all moved nodes are marked as removed or replaced.
	 */
	private void prepareSingleNodeCopies() {
		for (int i= 0; i < this.nodeCopySources.size(); i++) {
			CopySourceInfo curr= (CopySourceInfo) this.nodeCopySources.get(i);
			if (curr.isMove && curr.location != null) {
				doMarkMovedAsRemoved(curr, curr.location.getParent(), curr.location.getProperty());
			}
		}
		
	}
	
	private void doMarkMovedAsRemoved(CopySourceInfo curr, ASTNode parent, StructuralPropertyDescriptor childProperty) {
		if (childProperty.isChildListProperty()) {
			ListRewriteEvent event= getListEvent(parent, childProperty, true);
			int index= event.getIndex(curr.getNode(), ListRewriteEvent.OLD);
			if (index != -1 && event.getChangeKind(index) == RewriteEvent.UNCHANGED) {
				event.setNewValue(null, index);
			}
		} else {
			NodeRewriteEvent event= getNodeEvent(parent, childProperty, true);
			if (event.getChangeKind() == RewriteEvent.UNCHANGED) {
				event.setNewValue(null);
			}
		}
	}

	public boolean isInsertBoundToPrevious(ASTNode node) {	
		if (this.insertBoundToPrevious != null) {
			return this.insertBoundToPrevious.contains(node);
		}
		return false;
	}

	public void setInsertBoundToPrevious(ASTNode node) {
		if (this.insertBoundToPrevious == null) {
			this.insertBoundToPrevious= new HashSet();
		}
		this.insertBoundToPrevious.add(node);
	}
	
	private void validateIsListProperty(StructuralPropertyDescriptor property) {
		if (!property.isChildListProperty()) {
			String message= property.getId() + " is not a list property"; //$NON-NLS-1$
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateHasChildProperty(ASTNode parent, StructuralPropertyDescriptor property) {
		if (!parent.structuralPropertiesForType().contains(property)) {
			String message= Signature.getSimpleName(parent.getClass().getName()) + " has no property " + property.getId(); //$NON-NLS-1$
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateIsNodeProperty(StructuralPropertyDescriptor property) {
		if (property.isChildListProperty()) {
			String message= property.getId() + " is not a node property"; //$NON-NLS-1$
			throw new IllegalArgumentException(message);
		}
	}	
	
	public String toString() {
		StringBuffer buf= new StringBuffer();
		for (int i= 0; i < this.events.size(); i++) {
			buf.append(this.events.get(i).toString()).append('\n');
		}
		return buf.toString();
	}
	
	public static boolean isNewNode(ASTNode node) {
		return (node.getFlags() & ASTNode.ORIGINAL) == 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7411.java