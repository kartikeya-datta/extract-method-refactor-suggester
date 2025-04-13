error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2722.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2722.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2722.java
text:
```scala
final L@@ist<VEXElement> children = new ArrayList<VEXElement>();

/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.docbook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.xml.vex.core.internal.provisional.dom.I.VEXDocument;
import org.eclipse.wst.xml.vex.core.internal.provisional.dom.I.VEXElement;
import org.eclipse.wst.xml.vex.ui.internal.editor.VexEditor;
import org.eclipse.wst.xml.vex.ui.internal.outline.IOutlineProvider;

/**
 * Provides an outline of the sections of a DocBook document.
 */
public class DocBookOutlineProvider implements IOutlineProvider {
    
    public void init(VexEditor editor) {
    }

    public ITreeContentProvider getContentProvider() {
        return this.contentProvider;
    }

    public IBaseLabelProvider getLabelProvider() {
        return this.labelProvider;
    }

    public VEXElement getOutlineElement(final VEXElement child) {
    	
    	VEXElement element = child;
    	while (( element.getParent() != null) 
    			&& !isTitledElement(element) ) {
    		element = element.getParent();    		
    	}
    	
    	return element;
    }

    private final ITreeContentProvider contentProvider = new ITreeContentProvider() {

        public void dispose() {
        }

        public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        }

        public Object[] getChildren(final Object parentElement) {
            return getOutlineChildren((VEXElement) parentElement);
        }

        public Object getParent(final Object element) {
            final VEXElement parent = ((VEXElement) element).getParent();
            if (parent == null) {
                return element;
            } else {
                return getOutlineElement(parent);
            }
        }

        public boolean hasChildren(final Object element) {
            return getOutlineChildren((VEXElement) element).length > 0;
        }

        public Object[] getElements(final Object inputElement) {
            final VEXDocument document = (VEXDocument) inputElement;
            return new Object[] { document.getRootElement() };
        }
        
    };
    
    /**
     * Returns an array of the children of the given element that represent
     * nodes in the outline. These are elements such as "section" that 
     * contain "title" elements that we can use as label content.
     * @param element
     * @return
     */
    private VEXElement[] getOutlineChildren(final VEXElement element) {
        final List children = new ArrayList();
        final EList<VEXElement> childElements = element.getChildElements();
        for (int i = 0; i < childElements.size(); i++) {
            if (titledElements.contains(childElements.get(i).getName())) {
                children.add(childElements.get(i));
            }
        }
        return (VEXElement[]) children.toArray(new VEXElement[children.size()]);
    }

    
    private final ILabelProvider labelProvider = new LabelProvider() {
        public String getText(final Object o) {
            final VEXElement e = (VEXElement) o;
            VEXElement titleChild = findChild(e, "title");
            if (titleChild != null) {
                return titleChild.getText();
            } else {
                VEXElement infoChild = findChild(e, e.getName() + "info");
                if (infoChild != null) {
                    titleChild = findChild(infoChild, "title");
                    if (titleChild != null) {
                        return titleChild.getText();
                    }
                }
            }
            return e.getName();
        }
    };

    /**
     * Returns a value from a map of tag names that contain title elements 
     * The tags all have a similar characteristic. Their content model
     * includes a <title/> element as the first or second child.
     * 
     * For simplicity, we assume that the document is valid w.r.t. the DTD
     * and thus the 'title' element is optional and is one of the first two
     * elements inside the given element. 
     * 
     * @param element
     * @return
     */
    private boolean isTitledElement( final VEXElement e ) {
    	
    	if( titledElements.contains(e.getName() )
 e.getParent() == null ) {
    		final List<VEXElement> children = e.getChildElements();
    		if( (children.size() > 0 && children.get(0).getName().equals("title"))
 (children.size() > 1 && children.get(1).getName().equals("title"))  		
    		  ) {
    		    	return true;
    		    }
    		}	
    	return false;
    }

    /**
     * Finds the first child of the given element with the given name. Returns
     * null if none found. We should move this to XPath when we gain that
     * facility.
     */
    private VEXElement findChild(VEXElement parent, String childName) {
        List<VEXElement> children = parent.getChildElements();
        for (int i = 0; i < children.size(); i++) {
            VEXElement child = children.get(i);
            if (child.getName().equals(childName)) {
                return child;
            }
        }
        return null;
    }
    
    private final static int MAPSIZE = 101; // prime greater than 65/.80
    
    private final static HashSet titledElements = new HashSet(MAPSIZE);
    
    static {
 // Comment on each line indicates tags content model if different than title
 // We are only interested in location of title tag within Content Model
 // so any extra following content is ignored.

// In general, it is impossible to keep this sort of table accurate and 
// up-to-date for more than the most basic of cases. It would be better 
// to extract this information from the DTD for the document.

// For the moment, it is observed that this imperfect method works for 
// vast majority of DocBook documents.

    	titledElements.add("abstract"); // title?
    	titledElements.add("article"); 
    //	titledElements.add("articleinfo"); 
    	titledElements.add("appendix"); 
    //	titledElements.add("authorblurb"); // title?
    //	titledElements.add("bibliodiv");
    //	titledElements.add("calloutlist");
    //	titledElements.add("caution"); // title?
    	titledElements.add("chapter"); // docinfo?, title
    //	titledElements.add("colophon");
    //	titledElements.add("constraintdef"); 
    //	titledElements.add("dedication");
    	
    //	titledElements.add("equation"); // blockinfo?, title
    //	titledElements.add("example"); // blockinfo?, title
    //	titledElements.add("figure"); // blockinfo?, title
    	titledElements.add("glossary"); // glossaryinfo?, title
    //	titledElements.add("glossdiv"); 
    //	titledElements.add("glosslist"); // blockinfo?, title
    //	titledElements.add("important"); // title?
    //	titledElements.add("index"); // indexinfo, title
    //	titledElements.add("indexdiv");
    //	titledElements.add("itemizedlist"); // blockinfo?, title
    	
    //	titledElements.add("legalnotice"); // blockinfo?, title
    //	titledElements.add("lot"); // beginpage?, title
    //	titledElements.add("msg"); // title?
    //	titledElements.add("msgexplan"); // title?
    //	titledElements.add("msgmain"); // title?
    //	titledElements.add("msgrel"); // title?
    //	titledElements.add("msgset");
    //	titledElements.add("msgsub"); // title?
    //	titledElements.add("note"); // title?
    //	titledElements.add("orderedlist"); // blockinfo?, title
    	
    	titledElements.add("part"); // beginpage?, partinfo?, title
    	titledElements.add("partintro"); 
    //	titledElements.add("personblurb"); // title?
    	titledElements.add("preface"); // beginpage?, prefaceinfo?, title
    //	titledElements.add("procedure"); // blockinfo?, title
    //	titledElements.add("productionset");
    //	titledElements.add("qandadiv"); // blockinfo?, title
    //	titledElements.add("qandaset"); // blockinfo?, title
    //	titledElements.add("reference"); // beginpage?, referenceinfo?, title
    //	titledElements.add("refsect1"); // refsect1info?, title
    	
    //	titledElements.add("refsect2"); // refsect2info, title
    //	titledElements.add("refsect3"); // refsect3info, title
    //	titledElements.add("refsection"); // refsectioninfo?, title
    //	titledElements.add("refsynopsisdiv"); // refsynopsisdivinfo?, title
    	titledElements.add("sect1"); // sect1info?, title
    	titledElements.add("sect2"); // sect2info?, title
       	titledElements.add("sect3"); // sect3info?, title
       	titledElements.add("sect4"); // sect4info?, title
       	titledElements.add("sect5"); // sect5info?, title
    	titledElements.add("section"); // sectioninfo?, title
    	
    //	titledElements.add("segmentedlist"); 
    //	titledElements.add("set");
    //	titledElements.add("setindex"); // setindexinfo?, title
    //	titledElements.add("sidebar"); // sidebarinfo?, title
    	titledElements.add("simplesect");
    //	titledElements.add("step"); // table?
    //	titledElements.add("table"); // blockinfo?, title
    //	titledElements.add("task"); // blockinfo?, title
    //	titledElements.add("taskprerequisites");// blockinfo?, title
    //	titledElements.add("taskrelated"); // blockinfo?, title
    	
    //	titledElements.add("tasksummary"); // blockinfo?, title
    //	titledElements.add("tip"); // title?
    	titledElements.add("toc"); // beginpage?, title
    //	titledElements.add("variablelist"); // blockinfo?, title
    //	titledElements.add("warning"); // title?
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2722.java