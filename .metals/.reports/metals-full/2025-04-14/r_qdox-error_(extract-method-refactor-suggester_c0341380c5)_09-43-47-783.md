error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/330.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/330.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/330.java
text:
```scala
f@@oundAny = elementFound ? Boolean.TRUE : Boolean.FALSE;

/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.dialogs;

import com.ibm.icu.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.internal.misc.StringMatcher;

/**
 * A filter used in conjunction with <code>FilteredTree</code>.  In order to 
 * determine if a node should be filtered it uses the content provider of the 
 * tree to do pattern matching on its children.  This causes the entire tree
 * structure to be realized.
 *  
 * @see org.eclipse.ui.dialogs.FilteredTree  
 * @since 3.2
 */
public class PatternFilter extends ViewerFilter {
	/*
	 * Cache of filtered elements in the tree
	 */
    private Map cache = new HashMap();
    
    /*
     * Maps parent elements to TRUE or FALSE
     */
    private Map foundAnyCache = new HashMap();
    
	/**
	 * Whether to include a leading wildcard for all provided patterns.  A
	 * trailing wildcard is always included.
	 */
	private boolean includeLeadingWildcard = false;

	/**
	 * The string pattern matcher used for this pattern filter.  
	 */
    private StringMatcher matcher;
    
    private static Object[] EMPTY = new Object[0];

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#filter(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object[])
     */
    public final Object[] filter(Viewer viewer, Object parent, Object[] elements) {
        if (matcher == null) {
			return elements;
		}

        Object[] filtered = (Object[]) cache.get(parent);
        if (filtered == null) {
        	Boolean foundAny = (Boolean) foundAnyCache.get(parent);
        	if (foundAny != null && !foundAny.booleanValue()) {
        		filtered = EMPTY;
        	} else {
        		filtered = super.filter(viewer, parent, elements);
        	}
            cache.put(parent, filtered);
        }
        return filtered;
    }

    /**
     * Returns true if any of the elements makes it through the filter. 
     * @param viewer
     * @param parent
     * @param elements the elements (must not be an empty array)
     * @return true if any of the elements makes it through the filter.
     */
    private boolean isAnyVisible(Viewer viewer, Object parent, Object[] elements) {
    	if (matcher == null) {
    		return true;
    	}
    	
    	Object[] filtered = (Object[]) cache.get(parent);
    	if (filtered != null) {
    		return filtered.length > 0;
    	}
    	Boolean foundAny = (Boolean) foundAnyCache.get(parent);
    	if (foundAny == null) {
    		boolean elementFound = false;
    		for (int i = 0; i < elements.length && !elementFound; i++) {
				Object element = elements[i];
	    		elementFound = isElementVisible(viewer, element);
			}
    		foundAny = Boolean.valueOf(elementFound);
    		foundAnyCache.put(parent, foundAny);
    	}
    	return foundAny.booleanValue();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public final boolean select(Viewer viewer, Object parentElement,
			Object element) {
        return isElementVisible(viewer, element);
    }
    
    /**
	 * Sets whether a leading wildcard should be attached to each pattern
	 * string.
	 * 
	 * @param includeLeadingWildcard
	 *            Whether a leading wildcard should be added.
	 */
	public final void setIncludeLeadingWildcard(
			final boolean includeLeadingWildcard) {
		this.includeLeadingWildcard = includeLeadingWildcard;
	}

    /**
     * The pattern string for which this filter should select 
     * elements in the viewer.
     * 
     * @param patternString
     */
    public void setPattern(String patternString) {
        cache.clear();
        foundAnyCache.clear();
        if (patternString == null || patternString.equals("")) { //$NON-NLS-1$
			matcher = null;
		} else {
			String pattern = patternString + "*"; //$NON-NLS-1$
			if (includeLeadingWildcard) {
				pattern = "*" + pattern; //$NON-NLS-1$
			}
			matcher = new StringMatcher(pattern, true, false);
		}
    }

    /**
     * Answers whether the given String matches the pattern.
     * 
     * @param string the String to test
     * 
     * @return whether the string matches the pattern
     */
    private boolean match(String string) {
    	if (matcher == null) {
			return true;
		}
        return matcher.match(string);
    }
    
    /**
     * Answers whether the given element is a valid selection in 
     * the filtered tree.  For example, if a tree has items that 
     * are categorized, the category itself may  not be a valid 
     * selection since it is used merely to organize the elements.
     * 
     * @param element
     * @return true if this element is eligible for automatic selection
     */
    public boolean isElementSelectable(Object element){
    	return element != null;
    }
    
    /**
     * Answers whether the given element in the given viewer matches
     * the filter pattern.  This is a default implementation that will 
     * show a leaf element in the tree based on whether the provided  
     * filter text matches the text of the given element's text, or that 
     * of it's children (if the element has any).  
     * 
     * Subclasses may override this method.
     * 
     * @param viewer the tree viewer in which the element resides
     * @param element the element in the tree to check for a match
     * 
     * @return true if the element matches the filter pattern
     */
    public boolean isElementVisible(Viewer viewer, Object element){
    	return isParentMatch(viewer, element) || isLeafMatch(viewer, element);
    }
    
    /**
     * Check if the parent (category) is a match to the filter text.  The default 
     * behavior returns true if the element has at least one child element that is 
     * a match with the filter text.
     * 
     * Subclasses may override this method.
     * 
     * @param viewer the viewer that contains the element
     * @param element the tree element to check
     * @return true if the given element has children that matches the filter text
     */
    protected boolean isParentMatch(Viewer viewer, Object element){
        Object[] children = ((ITreeContentProvider) ((AbstractTreeViewer) viewer)
                .getContentProvider()).getChildren(element);

        if ((children != null) && (children.length > 0)) {
			return isAnyVisible(viewer, element, children);
		}	
        return false;
    }
    
    /**
     * Check if the current (leaf) element is a match with the filter text.  
     * The default behavior checks that the label of the element is a match. 
     * 
     * Subclasses should override this method.
     * 
     * @param viewer the viewer that contains the element
     * @param element the tree element to check
     * @return true if the given element's label matches the filter text
     */
    protected boolean isLeafMatch(Viewer viewer, Object element){
        String labelText = ((ILabelProvider) ((StructuredViewer) viewer)
                .getLabelProvider()).getText(element);
        
        if(labelText == null) {
			return false;
		}
        return wordMatches(labelText);  
    }
    
    /**
     * Take the given filter text and break it down into words using a 
     * BreakIterator.  
     * 
     * @param text
     * @return an array of words
     */
    private String[] getWords(String text){
    	List words = new ArrayList();
		// Break the text up into words, separating based on whitespace and
		// common punctuation.
		// Previously used String.split(..., "\\W"), where "\W" is a regular
		// expression (see the Javadoc for class Pattern).
		// Need to avoid both String.split and regular expressions, in order to
		// compile against JCL Foundation (bug 80053).
		// Also need to do this in an NL-sensitive way. The use of BreakIterator
		// was suggested in bug 90579.
		BreakIterator iter = BreakIterator.getWordInstance();
		iter.setText(text);
		int i = iter.first();
		while (i != java.text.BreakIterator.DONE && i < text.length()) {
			int j = iter.following(i);
			if (j == java.text.BreakIterator.DONE) {
				j = text.length();
			}
			// match the word
			if (Character.isLetterOrDigit(text.charAt(i))) {
				String word = text.substring(i, j);
				words.add(word);
			}
			i = j;
		}
		return (String[]) words.toArray(new String[words.size()]);
    }
    
	/**
	 * Return whether or not if any of the words in text satisfy the
	 * match critera.
	 * 
	 * @param text the text to match
	 * @return boolean <code>true</code> if one of the words in text 
	 * 					satisifes the match criteria.
	 */
	protected boolean wordMatches(String text) {
		if (text == null) {
			return false;
		}
		
		//If the whole text matches we are all set
		if(match(text)) {
			return true;
		}
		
		// Otherwise check if any of the words of the text matches
		String[] words = getWords(text);
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (match(word)) {
				return true;
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/330.java