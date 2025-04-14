error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6479.java
text:
```scala
t@@ext.setText("Lorem ipsum dolor sit amet"); //$NON-NLS-1$

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
package org.eclipse.ui.internal.themes;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IThemePreview;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.themes.ITheme;


/**
 * @since 3.0
 */
public class WorkbenchPreview implements IThemePreview {

    private IPreferenceStore store;
    private boolean disposed = false;
    private CTabFolder folder;
    private ITheme theme;
    private ToolBar toolBar;
    private CLabel viewMessage;
    private ViewForm viewForm;
    
    private IPropertyChangeListener fontAndColorListener = new IPropertyChangeListener(){        
        public void propertyChange(PropertyChangeEvent event) {  
            if (!disposed) {
                setColorsAndFonts();
                //viewMessage.setSize(viewMessage.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
                viewForm.layout(true);
            }
        }};
        
    private IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent event) {
            if (!disposed) {
				if (IPreferenceConstants.VIEW_TAB_POSITION.equals(event.getProperty())) {				 
					setTabPosition();
				} else if (IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS.equals(event.getProperty())) {				
					setTabStyle();
				}				
            }
        }};


    /* (non-Javadoc)
     * @see org.eclipse.ui.IPresentationPreview#createControl(org.eclipse.swt.widgets.Composite, org.eclipse.ui.themes.ITheme)
     */
    public void createControl(Composite parent, ITheme currentTheme) {        
        this.theme = currentTheme;
        store = WorkbenchPlugin.getDefault().getPreferenceStore();
        folder = new CTabFolder(parent, SWT.BORDER);
        folder.setUnselectedCloseVisible(false);
        folder.setEnabled(false);
        folder.setMaximizeVisible(true);
        folder.setMinimizeVisible(true);
        
        viewForm = new ViewForm(folder, SWT.NONE);
        viewForm.marginHeight = 0;
        viewForm.marginWidth = 0;
        viewForm.setBorderVisible(false);
        toolBar = new ToolBar(viewForm, SWT.FLAT | SWT.WRAP);
        ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);

        Image hoverImage =
			WorkbenchImages.getImage(IWorkbenchGraphicConstants.IMG_LCL_VIEW_MENU_HOVER);
        toolItem.setImage(hoverImage);
        
        viewForm.setTopRight(toolBar);
        
        viewMessage = new CLabel(viewForm, SWT.NONE);
        viewMessage.setText("Etu?"); //$NON-NLS-1$
        viewForm.setTopLeft(viewMessage);        
        
        CTabItem item = new CTabItem(folder, SWT.CLOSE);  
        item.setText("Lorem"); //$NON-NLS-1$
        Text text = new Text(viewForm, SWT.NONE);
        viewForm.setContent(text);
        text.setText("Lorem ipsum dolor sit amet\n"); //$NON-NLS-1$                
        item = new CTabItem(folder, SWT.CLOSE);
        item.setText("Ipsum"); //$NON-NLS-1$
        item.setControl(viewForm);        
        item.setImage(WorkbenchImages.getImage(ISharedImages.IMG_TOOL_COPY));
            
        folder.setSelection(item);
        
        item = new CTabItem(folder, SWT.CLOSE);
        item.setText("Dolor"); //$NON-NLS-1$
        item = new CTabItem(folder, SWT.CLOSE);
        item.setText("Sit"); //$NON-NLS-1$
        
        currentTheme.addPropertyChangeListener(fontAndColorListener);
        store.addPropertyChangeListener(preferenceListener);
        setColorsAndFonts();
        setTabPosition();
        setTabStyle();
    }

    /**
     * Set the tab style from preferences.
     */
    protected void setTabStyle() {
        boolean traditionalTab = store.getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
        folder.setSimpleTab(traditionalTab);
    }

    /**
     * Set the tab location from preferences.
     */
    protected void setTabPosition() {
        int tabLocation = store.getInt(IPreferenceConstants.VIEW_TAB_POSITION);
        folder.setTabPosition(tabLocation);        
    }

    /**
     * Set the folder colors and fonts
     */
    private void setColorsAndFonts() {
        folder.setSelectionForeground(theme.getColorRegistry().get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR));               
        folder.setForeground(theme.getColorRegistry().get(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR));
        
        Color [] colors = new Color[2];
        colors[0] = theme.getColorRegistry().get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START);
        colors[1] = theme.getColorRegistry().get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END);
        folder.setBackground(colors, new int [] {theme.getInt(IWorkbenchThemeConstants.INACTIVE_TAB_PERCENT)}, theme.getBoolean(IWorkbenchThemeConstants.INACTIVE_TAB_VERTICAL));
        toolBar.setBackground(colors[1]);
        viewMessage.setBackground(colors[1]);
        viewForm.setBackground(colors[1]);
        
        colors[0] = theme.getColorRegistry().get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
        colors[1] = theme.getColorRegistry().get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
        folder.setSelectionBackground(colors, new int [] {theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT)}, theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL));
        
        folder.setFont(theme.getFontRegistry().get(IWorkbenchThemeConstants.TAB_TEXT_FONT));
        viewMessage.setFont(theme.getFontRegistry().get(IWorkbenchThemeConstants.VIEW_MESSAGE_TEXT_FONT));
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IPresentationPreview#dispose()
     */
    public void dispose() {
        disposed = true;
        theme.removePropertyChangeListener(fontAndColorListener);
        store.removePropertyChangeListener(preferenceListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6479.java