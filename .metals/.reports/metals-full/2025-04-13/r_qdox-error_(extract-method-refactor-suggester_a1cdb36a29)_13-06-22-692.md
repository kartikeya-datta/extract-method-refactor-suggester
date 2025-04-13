error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1100.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1100.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1100.java
text:
```scala
public A@@bstractIcon getStructureIcon(IProgramElement.Kind kind, IProgramElement.Accessibility accessibility) {

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.ui.swing;

import javax.swing.*;

import org.aspectj.ajde.ui.*;
import org.aspectj.asm.*;
import org.aspectj.asm.IProgramElement;

/**
 * Default icons.  Override behavior for custom icons.
 *
 * @author  Mik Kersten
 */
public class IconRegistry extends AbstractIconRegistry {

    //public static IconRegistry INSTANCE = null;
    protected String RESOURCE_PATH = "org/aspectj/ajde/resources/";
    
    private final Icon START_AJDE = makeIcon("actions/startAjde.gif");
    private final Icon STOP_AJDE = makeIcon("actions/stopAjde.gif");
    private final Icon BUILD = makeIcon("actions/build.gif");
    private final Icon DEBUG = makeIcon("actions/debug.gif");
    private final Icon EXECUTE = makeIcon("actions/execute.gif");
    private final Icon AJBROWSER = makeIcon("structure/advice.gif");
	private final Icon AJBROWSER_ENABLED = makeIcon("actions/browserEnabled.gif");
	private final Icon AJBROWSER_DISABLED = makeIcon("actions/browserDisabled.gif");   
	private final Icon STRUCTURE_VIEW = makeIcon("actions/structureView.gif"); 

	private final Icon HIDE_ASSOCIATIONS = makeIcon("actions/hideAssociations.gif"); 
	private final Icon HIDE_NON_AJ = makeIcon("actions/hideNonAJ.gif"); 
	private final Icon GRANULARITY = makeIcon("actions/granularity.gif"); 
	private final Icon AJDE_SMALL = makeIcon("actions/ajdeSmall.gif"); 

	private final Icon ERROR = makeIcon("structure/error.gif");
	private final Icon WARNING = makeIcon("structure/warning.gif");
	private final Icon INFO = makeIcon("structure/info.gif");  

	private final Icon POPUP = makeIcon("actions/popup.gif");
	private final Icon FILTER = makeIcon("actions/filter.gif");
	private final Icon RELATIONS = makeIcon("actions/relations.gif");
	private final Icon ORDER = makeIcon("actions/order.gif");
	
    private final Icon ZOOM_STRUCTURE_TO_FILE_MODE = makeIcon("actions/zoomStructureToFileMode.gif");
    private final Icon ZOOM_STRUCTURE_TO_GLOBAL_MODE = makeIcon("actions/zoomStructureToGlobalMode.gif");
    private final Icon SPLIT_STRUCTURE_VIEW = makeIcon("actions/splitStructureView.gif");
    private final Icon MERGE_STRUCTURE_VIEW = makeIcon("actions/mergeStructureView.gif");
	 
    private final Icon BACK = makeIcon("actions/back.gif");
    private final Icon FORWARD = makeIcon("actions/forward.gif");
    private final Icon SEARCH = makeIcon("actions/search.gif");
    private final Icon OPEN_CONFIG = makeIcon("actions/openConfig.gif");
    private final Icon CLOSE_CONFIG = makeIcon("actions/closeConfig.gif");
    private final Icon SAVE = makeIcon("actions/save.gif");
    private final Icon SAVE_ALL = makeIcon("actions/saveAll.gif");
    private final Icon BROWSER_OPTIONS = makeIcon("actions/browseroptions.gif");
   
    private final Icon ACCESSIBILITY_PUBLIC = makeIcon("structure/accessibility-public.gif");
    private final Icon ACCESSIBILITY_PACKAGE = makeIcon("structure/accessibility-package.gif");
    private final Icon ACCESSIBILITY_PROTECTED = makeIcon("structure/accessibility-protected.gif");
    private final Icon ACCESSIBILITY_PRIVATE = makeIcon("structure/accessibility-private.gif");
    private final Icon ACCESSIBILITY_PRIVILEGED = makeIcon("structure/accessibility-privileged.gif");

	public Icon getAjdeSmallIcon() { return AJDE_SMALL; }
    public Icon getHideAssociationsIcon() { return HIDE_ASSOCIATIONS; }
    public Icon getHideNonAJIcon() { return HIDE_NON_AJ; }
    public Icon getGranularityIcon() { return GRANULARITY; }
    public Icon getErrorIcon() { return ERROR; }
    public Icon getWarningIcon() { return WARNING; }
    public Icon getInfoIcon() { return INFO; }
    public Icon getAJBrowserIcon() { return AJBROWSER; }
    public Icon getAJBrowserEnabledIcon() { return AJBROWSER_ENABLED; }
    public Icon getAJBrowserDisabledIcon() { return AJBROWSER_DISABLED; }
	public Icon getPopupIcon() { return POPUP; }
	public Icon getFilterIcon() { return FILTER; }
	public Icon getOrderIcon() { return ORDER; }
	public Icon getRelationsIcon() { return RELATIONS; }
    public Icon getStartAjdeIcon() { return START_AJDE; }
    public Icon getStopAjdeIcon() { return STOP_AJDE; }
    public Icon getBackIcon() { return BACK; }
    public Icon getForwardIcon() { return FORWARD; }
    public Icon getSearchIcon() { return SEARCH; }
    public Icon getBuildIcon() { return BUILD; }
    public Icon getDebugIcon() { return DEBUG; }
    public Icon getExecuteIcon() { return EXECUTE; }
    public Icon getOpenConfigIcon() { return OPEN_CONFIG; }
    public Icon getCloseConfigIcon() { return CLOSE_CONFIG; }
    public Icon getOpenIcon() { return OPEN_CONFIG; }
    public Icon getSaveIcon() { return SAVE; }
    public Icon getSaveAllIcon() { return SAVE_ALL; }
    public Icon getBrowserOptionsIcon() { return BROWSER_OPTIONS; }
    public Icon getZoomStructureToFileModeIcon() { return ZOOM_STRUCTURE_TO_FILE_MODE; }
    public Icon getZoomStructureToGlobalModeIcon() { return ZOOM_STRUCTURE_TO_GLOBAL_MODE; }
    public Icon getSplitStructureViewIcon() { return SPLIT_STRUCTURE_VIEW; }
    public Icon getMergeStructureViewIcon() { return MERGE_STRUCTURE_VIEW; }
	public Icon getStructureViewIcon() { return STRUCTURE_VIEW; }

    public Icon getAssociationSwingIcon(IRelationship.Kind relation) {
		return convertToSwingIcon(getIcon(relation));	
    }
    
	protected AbstractIcon getStructureIcon(IProgramElement.Kind kind, IProgramElement.Accessibility accessibility) {
		return getIcon(kind);	
	}
    
    public Icon getStructureSwingIcon(IProgramElement.Kind kind, IProgramElement.Accessibility accessibility) { 
		return convertToSwingIcon(getStructureIcon(kind, accessibility));    	
    }
	
	public Icon getStructureSwingIcon(IProgramElement.Kind kind) {
		return convertToSwingIcon(getIcon(kind));
	}

	public Icon getAccessibilitySwingIcon(IProgramElement.Accessibility accessibility) {
		if (accessibility == IProgramElement.Accessibility.PUBLIC) {
			return ACCESSIBILITY_PUBLIC;
		} else if (accessibility == IProgramElement.Accessibility.PACKAGE) {
			return ACCESSIBILITY_PACKAGE;
		} else if (accessibility == IProgramElement.Accessibility.PROTECTED) {
			return ACCESSIBILITY_PROTECTED;
		} else if (accessibility == IProgramElement.Accessibility.PRIVATE) {
			return ACCESSIBILITY_PRIVATE;
		} else if (accessibility == IProgramElement.Accessibility.PRIVILEGED) {
			return ACCESSIBILITY_PRIVILEGED;
		} else {
			return null;
		}
	}

	public Icon convertToSwingIcon(AbstractIcon iconAdapter) {
		if (iconAdapter != null) {
			return (Icon)iconAdapter.getIconResource();
		} else {
			return null;
		}		
	}

	protected AbstractIcon createIcon(String path) {
		return new AbstractIcon(new ImageIcon(ClassLoader.getSystemResource(path)));		
	}

    protected Icon makeIcon(String iconPath) {
        return new ImageIcon(ClassLoader.getSystemResource(RESOURCE_PATH + iconPath));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1100.java