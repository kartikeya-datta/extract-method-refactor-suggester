error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1064.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1064.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1064.java
text:
```scala
p@@resentation.setBackgroundColor(c[1]);

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.lang.ref.WeakReference;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.presentations.BasicStackPresentation;
import org.eclipse.ui.internal.presentations.PaneFolder;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * ColorSchemeService is the service that sets the colors on widgets as
 * appropriate.
 */
public class ColorSchemeService {

    private static final String LISTENER_KEY = "org.eclipse.ui.internal.ColorSchemeService"; //$NON-NLS-1$
    
    public static void setViewColors(final Control control) {
	    ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	    if (control.getData(LISTENER_KEY) == null) {
	        final IPropertyChangeListener listener = new IPropertyChangeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent event) {
                    
                    String property = event.getProperty();
                    if (property.equals(IThemeManager.CHANGE_CURRENT_THEME) 
 property.equals(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END)) {
                        setViewColors(control);                        
                    }
                }	            
	        };
	        control.setData(LISTENER_KEY, listener);
	        control.addDisposeListener(new DisposeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
                 */
                public void widgetDisposed(DisposeEvent e) {
                    PlatformUI
                    .getWorkbench()
                    .getThemeManager()
                    .removePropertyChangeListener(listener);
                    control.setData(LISTENER_KEY, null);
                }});
	        
	        PlatformUI
	        .getWorkbench()
	        .getThemeManager()
	        .addPropertyChangeListener(listener);	
	    }
	    control.setBackground(theme.getColorRegistry().get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END));
    }
    
	public static void setTabAttributes(BasicStackPresentation presentation, final PaneFolder control) {
	    if (presentation == null)  // the reference to the presentation was lost by the listener
	    	return;	    

	    ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	    if (control.getControl().getData(LISTENER_KEY) == null) {
	    	final WeakReference ref = new WeakReference(presentation);
	        final IPropertyChangeListener listener = new IPropertyChangeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent event) {
                    
                    String property = event.getProperty();
                    if (property.equals(IThemeManager.CHANGE_CURRENT_THEME) 
 property.equals(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START)
 property.equals(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END)
 property.equals(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR)
 property.equals(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR)
 property.equals(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START)
 property.equals(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END)
 property.equals(IWorkbenchThemeConstants.TAB_TEXT_FONT)) {
                        setTabAttributes((BasicStackPresentation) ref.get(), control);                        
                    }
                }	            
	        };
	        control.getControl().setData(LISTENER_KEY, listener);
	        control.getControl().addDisposeListener(new DisposeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
                 */
                public void widgetDisposed(DisposeEvent e) {
                    PlatformUI
                    .getWorkbench()
                    .getThemeManager()
                    .removePropertyChangeListener(listener);
                    control.getControl().setData(LISTENER_KEY, null);   
                }});
	        
	        PlatformUI
	        .getWorkbench()
	        .getThemeManager()
	        .addPropertyChangeListener(listener);	        
	    }
	    
	    int [] percent = new int[1];
	    boolean vertical;
	    ColorRegistry colorRegistry = theme.getColorRegistry();
        control.getControl().setForeground(colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR));

        Color [] c = new Color[2];
        c[0] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START);
        c[1] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END);

        // Note: This is currently being overridden in PartTabFolderPresentation
        percent[0] = theme.getInt(IWorkbenchThemeConstants.INACTIVE_TAB_PERCENT);
        // Note: This is currently being overridden in PartTabFolderPresentation
        vertical = theme.getBoolean(IWorkbenchThemeConstants.INACTIVE_TAB_VERTICAL);
	        

        presentation.setBackgroundColors(c[0], c[1], c[1]);

        if (presentation.isActive()) {                
			control.setSelectionForeground(colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR));
			c[0] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
	        c[1] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
	
	        percent[0] = theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT);
	        vertical = theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL);
		}
        control.setSelectionBackground(c, percent, vertical);
        CTabItem [] items = control.getItems();
        Font tabFont = theme.getFontRegistry().get(IWorkbenchThemeConstants.TAB_TEXT_FONT);
        control.getControl().setFont(tabFont);
        for (int i = 0; i < items.length; i++) {
			items[i].setFont(tabFont);
		}
	}

    public static void setViewTitleFont(BasicStackPresentation presentation, final Label control) {
        if (presentation == null)  // the reference to the presentation was lost by the listener
	    	return;	    

	    ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	    if (control.getData(LISTENER_KEY) == null) {
	    	final WeakReference ref = new WeakReference(presentation);
	        final IPropertyChangeListener listener = new IPropertyChangeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent event) {
                    
                    String property = event.getProperty();
                    if (property.equals(IThemeManager.CHANGE_CURRENT_THEME) 
 property.equals(IWorkbenchThemeConstants.VIEW_MESSAGE_TEXT_FONT)) {
                        setViewTitleFont((BasicStackPresentation) ref.get(), control);   
                        // have to call setControlSize here because it is not safe to call until
                        // the presentation is fully initialized.
                        ((BasicStackPresentation) ref.get()).setControlSize();
                    }
                }	            
	        };
	        control.setData(LISTENER_KEY, listener);
	        control.addDisposeListener(new DisposeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
                 */
                public void widgetDisposed(DisposeEvent e) {
                    PlatformUI
                    .getWorkbench()
                    .getThemeManager()
                    .removePropertyChangeListener(listener);
                    control.setData(LISTENER_KEY, null);
                }});
	        
	        PlatformUI
	        .getWorkbench()
	        .getThemeManager()
	        .addPropertyChangeListener(listener);	
	    }
	    control.setFont(theme.getFontRegistry().get(IWorkbenchThemeConstants.VIEW_MESSAGE_TEXT_FONT));	    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1064.java