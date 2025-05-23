error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8516.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8516.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8516.java
text:
```scala
I@@WorkbenchPreferenceConstants.CURRENT_THEME_ID, id);

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.themes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * Theme manager for the Workbench.
 *
 * @since 3.0
 */
public class WorkbenchThemeManager implements IThemeManager {

    private static WorkbenchThemeManager instance;

    /**
     * Returns the singelton instance of the WorkbenchThemeManager
     * 
     * @return singleton instance
     */
    public static synchronized WorkbenchThemeManager getInstance() {
        if (instance == null) {
            instance = new WorkbenchThemeManager();
            instance.getCurrentTheme(); //initialize the current theme
        }
        return instance;
    }

    private ITheme currentTheme;

    private IPropertyChangeListener currentThemeListener = new IPropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent event) {
            firePropertyChange(event);
            if (event.getSource() instanceof FontRegistry) {
                JFaceResources.getFontRegistry().put(event.getProperty(),
                        (FontData[]) event.getNewValue());
            } else if (event.getSource() instanceof ColorRegistry) {
                JFaceResources.getColorRegistry().put(event.getProperty(),
                        (RGB) event.getNewValue());
            }
        }
    };

    private ColorRegistry defaultThemeColorRegistry;

    private FontRegistry defaultThemeFontRegistry;

    private ListenerList propertyChangeListeners = new ListenerList();

    private IThemeRegistry themeRegistry;

    private Map themes = new HashMap(7);

    /*
     * Call dispose when we close
     */
    private WorkbenchThemeManager() {
        defaultThemeColorRegistry = new ColorRegistry(PlatformUI.getWorkbench()
                .getDisplay());

        defaultThemeFontRegistry = new FontRegistry(PlatformUI.getWorkbench()
                .getDisplay());

        //copy the font values from preferences.
        FontRegistry jfaceFonts = JFaceResources.getFontRegistry();
        for (Iterator i = jfaceFonts.getKeySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            defaultThemeFontRegistry.put(key, jfaceFonts.getFontData(key));
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.IThemeManager#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        propertyChangeListeners.add(listener);
    }

    /**
     * Disposes all ThemeEntries.
     */
    public void dispose() {
        for (Iterator i = themes.values().iterator(); i.hasNext();) {
            ITheme theme = (ITheme) i.next();
            theme.removePropertyChangeListener(currentThemeListener);
            theme.dispose();
        }
        themes.clear();
    }

    private boolean doSetCurrentTheme(String id) {
        ITheme oldTheme = currentTheme;
        ITheme newTheme = getTheme(id);
        if (oldTheme != newTheme && newTheme != null) {
            currentTheme = newTheme;
            return true;
        }

        return false;
    }

    protected void firePropertyChange(PropertyChangeEvent event) {
        Object[] listeners = propertyChangeListeners.getListeners();

        for (int i = 0; i < listeners.length; i++) {
            ((IPropertyChangeListener) listeners[i]).propertyChange(event);
        }
    }

    protected void firePropertyChange(String changeId, ITheme oldTheme,
            ITheme newTheme) {

        PropertyChangeEvent event = new PropertyChangeEvent(this, changeId,
                oldTheme, newTheme);
        firePropertyChange(event);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.IThemeManager#getCurrentTheme()
     */
    public ITheme getCurrentTheme() {
        if (currentTheme == null) {			
            String themeId = PrefUtil.getAPIPreferenceStore()
                    .getString(IWorkbenchPreferenceConstants.CURRENT_THEME_ID);
            if (themeId.equals("")) //$NON-NLS-1$
                themeId = IThemeManager.DEFAULT_THEME;

            setCurrentTheme(themeId);
            if (currentTheme == null) { // bad preference
                setCurrentTheme(IThemeManager.DEFAULT_THEME);
            }
        }
        return currentTheme;
    }

	/**
	 * Return the default color registry.
	 * 
	 * @return the default color registry
	 */
    public ColorRegistry getDefaultThemeColorRegistry() {
        return defaultThemeColorRegistry;
    }

	/**
	 * Return the default font registry.
	 * 
	 * @return the default font registry
	 */
    public FontRegistry getDefaultThemeFontRegistry() {
        return defaultThemeFontRegistry;
    }

    private ITheme getTheme(IThemeDescriptor td) {
        ITheme theme = (ITheme) themes.get(td);
        if (theme == null) {
            theme = new Theme(td);
            themes.put(td, theme);
        }
        return theme;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.IThemeManager#getTheme(java.lang.String)
     */
    public ITheme getTheme(String id) {
        if (id.equals(IThemeManager.DEFAULT_THEME))
            return getTheme((IThemeDescriptor) null);

        IThemeDescriptor td = getThemeRegistry().findTheme(id);
        if (td == null)
            return null;
        return getTheme(td);
    }

    /**
     * Answer the IThemeRegistry for the Workbench 
     */
    private IThemeRegistry getThemeRegistry() {
        if (themeRegistry == null) {
            themeRegistry = WorkbenchPlugin.getDefault().getThemeRegistry();
        }
        return themeRegistry;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.IThemeManager#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        propertyChangeListeners.remove(listener);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.IThemeManager#setCurrentTheme(java.lang.String)
     */
    public void setCurrentTheme(String id) {
        ITheme oldTheme = currentTheme;
        if (WorkbenchThemeManager.getInstance().doSetCurrentTheme(id)) {
            firePropertyChange(CHANGE_CURRENT_THEME, oldTheme,
                    getCurrentTheme());
            if (oldTheme != null)
                oldTheme.removePropertyChangeListener(currentThemeListener);
            currentTheme.addPropertyChangeListener(currentThemeListener);

            // update the preference if required.
            if (!PrefUtil.getAPIPreferenceStore().getString(
                    IWorkbenchPreferenceConstants.CURRENT_THEME_ID).equals(id)) {
				PrefUtil.getAPIPreferenceStore().setValue(
                        IWorkbenchPreferenceConstants.CURRENT_THEME_ID, id); //$NON-NLS-1$
				PrefUtil.saveAPIPrefs();
            }

            //update the jface registries
            {
                ColorRegistry jfaceColors = JFaceResources.getColorRegistry();
                ColorRegistry themeColors = currentTheme.getColorRegistry();
                for (Iterator i = themeColors.getKeySet().iterator(); i
                        .hasNext();) {
                    String key = (String) i.next();
                    jfaceColors.put(key, themeColors.getRGB(key));
                }
            }
            {
                FontRegistry jfaceFonts = JFaceResources.getFontRegistry();
                FontRegistry themeFonts = currentTheme.getFontRegistry();
                for (Iterator i = themeFonts.getKeySet().iterator(); i
                        .hasNext();) {
                    String key = (String) i.next();
                    jfaceFonts.put(key, themeFonts.getFontData(key));
                }
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8516.java