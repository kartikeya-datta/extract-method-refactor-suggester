error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1650.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1650.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1650.java
text:
```scala
C@@olorSchemeService.setTabAttributes(this, tabFolder);

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
package org.eclipse.ui.internal.presentations;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ColorSchemeService;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.themes.ITheme;

/**
 * Controls the appearance of views stacked into the workbench.
 * 
 * @since 3.0
 */
public class PartTabFolderPresentation extends BasicStackPresentation {
	
	private IPreferenceStore preferenceStore = WorkbenchPlugin.getDefault().getPreferenceStore();
		
	private final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			if (IPreferenceConstants.VIEW_TAB_POSITION.equals(propertyChangeEvent.getProperty()) && !isDisposed()) {
				int tabLocation = preferenceStore.getInt(IPreferenceConstants.VIEW_TAB_POSITION); 
				setTabPosition(tabLocation);
			} else if (IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS.equals(propertyChangeEvent.getProperty()) && !isDisposed()) {
				boolean traditionalTab = preferenceStore.getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS); 
				setTabStyle(traditionalTab);
			}		
		}
	};
	
	public PartTabFolderPresentation(Composite parent, IStackPresentationSite newSite, int flags) {
		
		super(new CTabFolder(parent, SWT.BORDER), newSite);
		CTabFolder tabFolder = getTabFolder();
		
		preferenceStore.addPropertyChangeListener(propertyChangeListener);
		int tabLocation = preferenceStore.getInt(IPreferenceConstants.VIEW_TAB_POSITION); 
		
		setTabPosition(tabLocation);
		setTabStyle(preferenceStore.getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
		
		// do not support close box on unselected tabs.
		tabFolder.setUnselectedCloseVisible(false);
		
		// do not support icons in unselected tabs.
		tabFolder.setUnselectedImageVisible(false);
		
		//tabFolder.setBorderVisible(true);
		// set basic colors
		ColorSchemeService.setTabAttributes(tabFolder);

		updateGradient();
		
		tabFolder.setMinimizeVisible((flags & SWT.MIN) != 0);
		tabFolder.setMaximizeVisible((flags & SWT.MAX) != 0);
	}
	
	/**
     * Set the tab folder tab style to a tradional style tab
	 * @param traditionalTab <code>true</code> if traditional style tabs should be used
     * <code>false</code> otherwise.
	 */
	protected void setTabStyle(boolean traditionalTab) {
		// set the tab style to non-simple
		getTabFolder().setSimpleTab(traditionalTab);
	}

	/**
	 * Update the tab folder's colours to match the current theme settings
	 * and active state
	 */
	private void updateGradient() {
		Color fgColor;
		ITheme currentTheme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
        FontRegistry fontRegistry = currentTheme.getFontRegistry();	    
		ColorRegistry colorRegistry = currentTheme.getColorRegistry();
		Color [] bgColors = new Color[2];
		int [] percent = new int[1];
		boolean vertical;
		
        if (isActive()){
        	
        	CTabItem item = getTabFolder().getSelection();
            if(item != null && !getPartForTab(item).isBusy()){
            	Font tabFont = fontRegistry.get(IWorkbenchThemeConstants.TAB_TEXT_FONT);
            	item.setFont(tabFont);
            }
            
	        fgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR);
            bgColors[0] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
            bgColors[1] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
            percent[0] = currentTheme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT);
            vertical = currentTheme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL);
		} else {
	        fgColor = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR);
            bgColors[0] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START);
            bgColors[1] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END);
            percent[0] = currentTheme.getInt(IWorkbenchThemeConstants.INACTIVE_TAB_PERCENT);
            vertical = currentTheme.getBoolean(IWorkbenchThemeConstants.INACTIVE_TAB_VERTICAL);
		}	
		drawGradient(fgColor, bgColors, percent, vertical);	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.skins.Presentation#setActive(boolean)
	 */
	public void setActive(boolean isActive) {
		super.setActive(isActive);
		
		updateGradient();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.StackPresentation#dispose()
	 */
	public void dispose() {
		preferenceStore.removePropertyChangeListener(propertyChangeListener);
		super.dispose();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1650.java