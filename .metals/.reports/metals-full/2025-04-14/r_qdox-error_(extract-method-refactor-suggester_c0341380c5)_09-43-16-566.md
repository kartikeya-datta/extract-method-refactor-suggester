error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3725.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3725.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3725.java
text:
```scala
.@@getString("DefaultTheme.label") : descriptor.getName(); //$NON-NLS-1$

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

import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.DataFormatException;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * @since 3.0
 */
public class Theme implements ITheme {

    /**
     * The translation bundle in which to look up internationalized text.
     */
    private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(Theme.class.getName());

    private CascadingColorRegistry themeColorRegistry;

    private CascadingFontRegistry themeFontRegistry;

    private IThemeDescriptor descriptor;

    private IPropertyChangeListener themeListener;

    private CascadingMap dataMap;

    private ListenerList propertyChangeListeners = new ListenerList();

    private ThemeRegistry themeRegistry;

    private IPropertyChangeListener propertyListener;

    /**
     * @param descriptor
     */
    public Theme(IThemeDescriptor descriptor) {
        themeRegistry = ((ThemeRegistry) WorkbenchPlugin.getDefault()
                .getThemeRegistry());
        this.descriptor = descriptor;
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (descriptor != null) {

            ColorDefinition[] definitions = this.descriptor.getColors();

            ITheme theme = workbench.getThemeManager().getTheme(
                    IThemeManager.DEFAULT_THEME);
            if (definitions.length > 0) {
                themeColorRegistry = new CascadingColorRegistry(theme
                        .getColorRegistry());
                ThemeElementHelper.populateRegistry(this, definitions,
                        workbench.getPreferenceStore());
            }

            FontDefinition[] fontDefinitions = this.descriptor.getFonts();
            if (fontDefinitions.length > 0) {
                themeFontRegistry = new CascadingFontRegistry(theme
                        .getFontRegistry());
                ThemeElementHelper.populateRegistry(this, fontDefinitions,
                        workbench.getPreferenceStore());
            }

            dataMap = new CascadingMap(((ThemeRegistry) WorkbenchPlugin
                    .getDefault().getThemeRegistry()).getData(), descriptor
                    .getData());
        }

        getColorRegistry().addListener(getCascadeListener());
        getFontRegistry().addListener(getCascadeListener());
        workbench.getPreferenceStore().addPropertyChangeListener(
                getPropertyListener());
    }

    /**
     * Listener that is responsible for responding to preference changes.
     * 
     * @return
     */
    private IPropertyChangeListener getPropertyListener() {
        if (propertyListener == null) {
            propertyListener = new IPropertyChangeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent event) {
                    String[] split = ThemeElementHelper.splitPropertyName(
                            Theme.this, event.getProperty());
                    String key = split[1];
                    String theme = split[0];
                    if (key.equals(IPreferenceConstants.CURRENT_THEME_ID))
                        return;
                    try {
                        if (themeColorRegistry != null) { // we're using cascading registries
                            if (getColorRegistry().hasValueFor(key)
                                    && theme != null && theme.equals(getId())) {
                                RGB rgb = StringConverter.asRGB((String) event
                                        .getNewValue());
                                getColorRegistry().put(key, rgb);
                                processDefaultsTo(key, rgb);
                                return;
                            }
                        } else {
                            if (getColorRegistry().hasValueFor(key)
                                    && theme == null) {
                                RGB rgb = StringConverter.asRGB((String) event
                                        .getNewValue());
                                getColorRegistry().put(key, rgb);
                                processDefaultsTo(key, rgb);
                                return;
                            }
                        }

                        if (themeFontRegistry != null) {
                            if (getFontRegistry().hasValueFor(key)
                                    && theme != null && theme.equals(getId())) {
                                FontData[] data = PreferenceConverter
                                        .basicGetFontData((String) event
                                                .getNewValue());
                                getFontRegistry().put(key, data);
                                processDefaultsTo(key, data);
                                return;
                            }
                        } else {
                            if (getFontRegistry().hasValueFor(key)
                                    && theme == null) {
                                FontData[] data = PreferenceConverter
                                        .basicGetFontData((String) event
                                                .getNewValue());
                                getFontRegistry().put(key, data);
                                processDefaultsTo(key, data);
                                return;
                            }
                        }
                    } catch (DataFormatException e) {
                        //no-op
                    }
                }

                /**
                 * Process all fonts that default to the given ID.
                 * 
                 * @param key the font ID
                 * @param fd the new FontData for defaulted fonts
                 */
                private void processDefaultsTo(String key, FontData[] fd) {
                    FontDefinition[] defs = WorkbenchPlugin.getDefault()
                            .getThemeRegistry().getFontsFor(getId());
                    for (int i = 0; i < defs.length; i++) {
                        String defaultsTo = defs[i].getDefaultsTo();
                        if (defaultsTo != null && defaultsTo.equals(key)) {
                            IPreferenceStore store = WorkbenchPlugin
                                    .getDefault().getPreferenceStore();
                            if (store.isDefault(ThemeElementHelper
                                    .createPreferenceKey(Theme.this, defs[i]
                                            .getId()))) {
                                getFontRegistry().put(defs[i].getId(), fd);
                                processDefaultsTo(defs[i].getId(), fd);
                            }
                        }
                    }
                }

                /**
                 * Process all colors that default to the given ID.
                 * 
                 * @param key the color ID
                 * @param rgb the new RGB value for defaulted colors
                 */
                private void processDefaultsTo(String key, RGB rgb) {
                    ColorDefinition[] defs = WorkbenchPlugin.getDefault()
                            .getThemeRegistry().getColorsFor(getId());
                    for (int i = 0; i < defs.length; i++) {
                        String defaultsTo = defs[i].getDefaultsTo();
                        if (defaultsTo != null && defaultsTo.equals(key)) {
                            IPreferenceStore store = WorkbenchPlugin
                                    .getDefault().getPreferenceStore();
                            if (store.isDefault(ThemeElementHelper
                                    .createPreferenceKey(Theme.this, defs[i]
                                            .getId()))) {
                                getColorRegistry().put(defs[i].getId(), rgb);
                                processDefaultsTo(defs[i].getId(), rgb);
                            }
                        }
                    }
                }
            };
        }
        return propertyListener;
    }

    /**
     * Listener that is responsible for rebroadcasting events fired from the base font/color registry
     */
    private IPropertyChangeListener getCascadeListener() {
        if (themeListener == null) {
            themeListener = new IPropertyChangeListener() {

                /* (non-Javadoc)
                 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
                 */
                public void propertyChange(PropertyChangeEvent event) {
                    firePropertyChange(event);
                }
            };
        }
        return themeListener;
    }

    public ColorRegistry getColorRegistry() {
        if (themeColorRegistry != null)
            return themeColorRegistry;
        else
            return WorkbenchThemeManager.getInstance()
                    .getDefaultThemeColorRegistry();
    }

    public FontRegistry getFontRegistry() {
        if (themeFontRegistry != null)
            return themeFontRegistry;
        else
            return WorkbenchThemeManager.getInstance()
                    .getDefaultThemeFontRegistry();
    }

    public void dispose() {
        if (themeColorRegistry != null) {
            themeColorRegistry.removeListener(themeListener);
            themeColorRegistry.dispose();
        }
        if (themeFontRegistry != null) {
            themeFontRegistry.removeListener(themeListener);
            themeFontRegistry.dispose();
        }
        PlatformUI.getWorkbench().getPreferenceStore()
                .removePropertyChangeListener(getPropertyListener());
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.ITheme#getId()
     */
    public String getId() {
        return descriptor == null ? IThemeManager.DEFAULT_THEME : descriptor
                .getId();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbench#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        propertyChangeListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbench#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        propertyChangeListeners.remove(listener);
    }

    private void firePropertyChange(PropertyChangeEvent event) {
        Object[] listeners = propertyChangeListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((IPropertyChangeListener) listeners[i]).propertyChange(event);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.themes.ITheme#getLabel()
     */
    public String getLabel() {
        return descriptor == null ? RESOURCE_BUNDLE
                .getString("DefaultTheme.label") : descriptor.getLabel(); //$NON-NLS-1$ 
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.ITheme#getString(java.lang.String)
     */
    public String getString(String key) {
        if (dataMap != null)
            return (String) dataMap.get(key);
        return (String) themeRegistry.getData().get(key);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.ITheme#keySet()
     */
    public Set keySet() {
        if (dataMap != null)
            return dataMap.keySet();

        return themeRegistry.getData().keySet();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.ITheme#getInt(java.lang.String)
     */
    public int getInt(String key) {
        String string = getString(key);
        if (string == null)
            return 0;
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.themes.ITheme#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String key) {
        String string = getString(key);
        if (string == null)
            return false;

        return Boolean.valueOf(getString(key)).booleanValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3725.java