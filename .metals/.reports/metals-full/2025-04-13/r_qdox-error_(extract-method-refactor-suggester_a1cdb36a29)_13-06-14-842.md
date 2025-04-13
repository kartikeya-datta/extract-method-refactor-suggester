error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3993.java
text:
```scala
b@@oolean usingMotif = "motif".equals(SWT.getPlatform()); //$NON-NLS-1$

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

package org.eclipse.ui.internal.presentations;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.presentations.IPresentablePart;

public class BasicStackList extends AbstractTableInformationControl {

    private class BasicStackListContentProvider implements IStructuredContentProvider {

        private DefaultPartPresentation basicStackPresentation;

        public BasicStackListContentProvider() {
            //no-op
        }

        public void dispose() {
            //no-op 
        }

        public Object[] getElements(Object inputElement) {  
        	if (basicStackPresentation == null) { return new CTabItem[0]; }                  
            final PaneFolder tabFolder = basicStackPresentation.getTabFolder();

            /* TODO
            ArrayList items = new ArrayList(Arrays.asList(tabFolder.getItems()));

            for (Iterator iterator = items.iterator(); iterator.hasNext();) {
                CTabItem tabItem = (CTabItem) iterator.next();

                if (tabItem.isShowing()) iterator.remove();
            }
            
            return items.toArray();
            */
            
            return tabFolder.getItems();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        	basicStackPresentation = (DefaultPartPresentation) newInput;
        }
    }
    
    private class BasicStackListLabelProvider extends LabelProvider implements IFontProvider {

    	private Font boldFont = null;

		public BasicStackListLabelProvider() { 
		    //no-op
    	}

    	public String getText(Object element) {
    	    CTabItem tabItem = (CTabItem) element;
            DefaultPartPresentation basicStackPresentation = (DefaultPartPresentation) getTableViewer()
            .getInput();
            IPresentablePart presentablePart = basicStackPresentation.getPartForTab(tabItem);
            // TODO: the 'true' parameter should be removed
    	    return basicStackPresentation.getLabelText(presentablePart, true); 
    	}

    	public Image getImage(Object element) {
    	    CTabItem tabItem = (CTabItem) element;
            DefaultPartPresentation basicStackPresentation = (DefaultPartPresentation) getTableViewer()
            .getInput();
            IPresentablePart presentablePart = basicStackPresentation.getPartForTab(tabItem);    	    
    	    return basicStackPresentation.getLabelImage(presentablePart);
    	}
    	
		public Font getFont(Object element) {
			CTabItem tabItem = (CTabItem) element;
			// check to determine if the user selected the single tab mode
			CTabFolder tabFolder = tabItem.getParent();
			boolean singleTab = tabFolder.getSingle();
			
			// if in single tab mode, do not use the bold font for non-visible tabs
			// if in multiple tab mode, use the bold for non-visible tabs only
			if (tabItem.isShowing() || singleTab)
				return null;
			
			if (boldFont == null) {
				Font originalFont = tabItem.getFont();
				FontData fontData[] = originalFont.getFontData();
				// Adding the bold attribute
				for (int i = 0; i < fontData.length; i++) 
					fontData[i].setStyle(fontData[i].getStyle()|SWT.BOLD);
				boldFont = new Font(tabItem.getDisplay(), fontData);
			}
			return boldFont;
		}
		
		public void dispose() {
			super.dispose();
			if (boldFont != null)
				boldFont.dispose();
		}
    }    
    
    private class BasicStackListViewerSorter extends ViewerSorter {
    	
    	public BasicStackListViewerSorter(){
    	    //no-op
    	}
    	
    	public BasicStackListViewerSorter(Collator collator) {
    		super(collator);
    	}
    	
    	/* (non-Javadoc)
    	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
    	 */
    	public int compare(Viewer viewer, Object e1, Object e2) {
    		int cat1 = category(e1);
    		int cat2 = category(e2);

    		if (cat1 != cat2)
    			return cat1 - cat2;

    		// cat1 == cat2

    		String name1;
    		String name2;

    		if (viewer == null || !(viewer instanceof ContentViewer)) {
    			name1 = e1.toString();
    			name2 = e2.toString();
    		} else {
    			IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
    			if (prov instanceof ILabelProvider) {
    				ILabelProvider lprov = (ILabelProvider) prov;
    				name1 = lprov.getText(e1);
    				name2 = lprov.getText(e2);
    				// ILabelProvider's implementation in BasicStackList calls 
    				// DefaultEditorPresentation.getLabelText which returns the name of dirty 
    				// files begining with dirty prefix, sorting should not take dirty prefix in consideration
    				String prefix = DefaultEditorPresentation.DIRTY_PREFIX;
    				if (name1.startsWith(prefix))
    					name1 = name1.substring(prefix.length());
    				if (name2.startsWith(prefix))
    					name2 = name2.substring(prefix.length());
    			} else {
    				name1 = e1.toString();
    				name2 = e2.toString();
    			}
    		}
    		if (name1 == null)
    			name1 = "";//$NON-NLS-1$
    		if (name2 == null)
    			name2 = "";//$NON-NLS-1$
    		return collator.compare(name1, name2);
    	}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
		 */
		public int category(Object element) {
			
			CTabItem tabItem = (CTabItem) element;
			if (tabItem.isShowing())
				return 1; // visible
			return 0; // not visible
		}
    }
    
    public BasicStackList(Shell parent, int shellStyle, int treeStyle) {
        super(parent, shellStyle, treeStyle);
    }

    protected TableViewer createTableViewer(Composite parent, int style) {
        Table table = new Table(parent, SWT.SINGLE | (style & ~SWT.MULTI));
        table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        TableViewer tableViewer = new TableViewer(table) {
        	/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.TableViewer#internalRefresh(java.lang.Object)
			 */
			protected void internalRefresh(Object element) {
				boolean usingMotif = "motif".equals(SWT.getPlatform());
				try {
					// This avoids a "graphic is disposed" error on Motif by not letting
					// it redraw while we remove entries.  Some items in this table are
					// being removed and may have icons which may have already been
					// disposed elsewhere.
					if (usingMotif) getTable().setRedraw(false);
					super.internalRefresh(element);
				} finally {
					if (usingMotif) getTable().setRedraw(true);
				}
			}
        };
        tableViewer.addFilter(new NamePatternFilter());
        tableViewer.setContentProvider(new BasicStackListContentProvider());
        tableViewer.setSorter(new BasicStackListViewerSorter());
        tableViewer.setLabelProvider(new BasicStackListLabelProvider());
        return tableViewer;
    }

    public void setInput(Object information) {
    	DefaultPartPresentation basicStackPresentation = (DefaultPartPresentation) information;
        inputChanged(basicStackPresentation, basicStackPresentation.getTabFolder()
                .getSelection());
    }

    protected void gotoSelectedElement() {
        Object selectedElement = getSelectedElement();

        DefaultPartPresentation basicStackPresentation = null;
        if (selectedElement != null) {
        	basicStackPresentation = (DefaultPartPresentation) getTableViewer()
                    .getInput();            
        }
        //close the shell
        dispose();

        //open the part, if one was selected
        if (basicStackPresentation != null)
            basicStackPresentation.setSelection((CTabItem) selectedElement);
    }
    
    protected boolean deleteSelectedElements() {
    	
        IStructuredSelection structuredSelection = getSelectedElements();
        
        if (structuredSelection != null) {
        	DefaultPartPresentation basicStackPresentation = (DefaultPartPresentation) getTableViewer()
            .getInput();
            
        	ArrayList list = new ArrayList(structuredSelection.size());
        	
            for (Iterator iterator = structuredSelection.iterator(); iterator.hasNext();) {
    			IPresentablePart presentablePart = basicStackPresentation.getPartForTab((CTabItem) iterator.next());
    			list.add(presentablePart);                
            }
            
            basicStackPresentation.close((IPresentablePart[]) list.toArray(new IPresentablePart[list.size()]));
        }
        
		if (((DefaultPartPresentation)getTableViewer().getInput()).isDisposed()) {
			fComposite.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3993.java