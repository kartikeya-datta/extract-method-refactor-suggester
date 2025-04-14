error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3957.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3957.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3957.java
text:
```scala
o@@penEditorDropDownHandler, Priority.MEDIUM);

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.AbstractHandler;
import org.eclipse.ui.commands.ExecutionException;
import org.eclipse.ui.commands.HandlerSubmission;
import org.eclipse.ui.commands.IHandler;
import org.eclipse.ui.commands.Priority;
import org.eclipse.ui.internal.ColorSchemeService;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.themes.ITheme;

/**
 * Controls the appearance of views stacked into the workbench.
 * 
 * @since 3.0
 */
public class EditorPresentation extends BasicStackPresentation {

    private IPreferenceStore preferenceStore = WorkbenchPlugin.getDefault()
            .getPreferenceStore();

    private HandlerSubmission openEditorDropDownHandlerSubmission;

    private CTabFolder2Adapter showListListener = new CTabFolder2Adapter() {

        public void showList(CTabFolderEvent event) {
            CTabFolder tabFolder = getTabFolder();
            event.doit = false;
            Point p = tabFolder.toDisplay(event.x, event.y);
            p.y += +event.height;
            EditorPresentation.this.showList(tabFolder.getShell(), p.x, p.y);
        }
    };

    private final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (IPreferenceConstants.EDITOR_TAB_POSITION
                    .equals(propertyChangeEvent.getProperty())
                    && !isDisposed()) {
                int tabLocation = preferenceStore
                        .getInt(IPreferenceConstants.EDITOR_TAB_POSITION);
                setTabPosition(tabLocation);
            } else if (IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS
                    .equals(propertyChangeEvent.getProperty())
                    && !isDisposed()) {
                boolean traditionalTab = preferenceStore
                        .getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
                setTabStyle(traditionalTab);
            }

            boolean multiChanged = IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS
                    .equals(propertyChangeEvent.getProperty());
            boolean styleChanged = IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS
                    .equals(propertyChangeEvent.getProperty());
            CTabFolder tabFolder = getTabFolder();

            if ((multiChanged || styleChanged) && tabFolder != null) {
                if (multiChanged) {
                    boolean multi = preferenceStore
                            .getBoolean(IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS);
                    tabFolder.setSingleTab(!multi);
                } else {
                    boolean simple = preferenceStore
                            .getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
                    tabFolder.setSimpleTab(simple);
                }

                CTabItem[] tabItems = tabFolder.getItems();

                for (int i = 0; i < tabItems.length; i++) {
                    CTabItem tabItem = tabItems[i];
                    initTab(tabItem, getPartForTab(tabItem));
                }
            }
        }
    };

    public EditorPresentation(Composite parent, IStackPresentationSite newSite,
            int flags) {
        super(new CTabFolder(parent, SWT.BORDER), newSite);
        final CTabFolder tabFolder = getTabFolder();
        tabFolder.addCTabFolder2Listener(showListListener);
        preferenceStore.addPropertyChangeListener(propertyChangeListener);
        int tabLocation = preferenceStore
                .getInt(IPreferenceConstants.EDITOR_TAB_POSITION);
        setTabPosition(tabLocation);
        tabFolder.setSingleTab(!preferenceStore
                .getBoolean(IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS));
        setTabStyle(preferenceStore
                .getBoolean(IPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
        // do not support close box on unselected tabs.
        tabFolder.setUnselectedCloseVisible(true);
        // do not support icons in unselected tabs.
        tabFolder.setUnselectedImageVisible(true);
        //tabFolder.setBorderVisible(true);
        // set basic colors
        ColorSchemeService.setTabAttributes(this, tabFolder);
        updateGradient();
        tabFolder.setMinimizeVisible((flags & SWT.MIN) != 0);
        tabFolder.setMaximizeVisible((flags & SWT.MAX) != 0);
        final Shell shell = tabFolder.getShell();
        IWorkbenchWindow workbenchWindow = null;
        IHandler openEditorDropDownHandler = new AbstractHandler() {

            public void execute(Object parameter) throws ExecutionException {
                Rectangle clientArea = tabFolder.getClientArea();
                Point location = tabFolder.getDisplay().map(tabFolder, null,
                        clientArea.x, clientArea.y);
                showList(shell, location.x, location.y);
            }
        };
        openEditorDropDownHandlerSubmission = new HandlerSubmission(null,
                shell, null, "org.eclipse.ui.window.openEditorDropDown", //$NON-NLS-1$
                openEditorDropDownHandler, Priority.NORMAL);
        PlatformUI.getWorkbench().getCommandSupport().addHandlerSubmissions(
                Collections.singletonList(openEditorDropDownHandlerSubmission));
    }

    public void dispose() {
        if (openEditorDropDownHandlerSubmission != null) {
            PlatformUI
                    .getWorkbench()
                    .getCommandSupport()
                    .removeHandlerSubmissions(
                            Collections
                                    .singletonList(openEditorDropDownHandlerSubmission));
            openEditorDropDownHandlerSubmission = null;
        }
        preferenceStore.removePropertyChangeListener(propertyChangeListener);
        getTabFolder().removeCTabFolder2Listener(showListListener);
        super.dispose();
    }

    protected void initTab(CTabItem tabItem, IPresentablePart part) {
        tabItem.setText(getLabelText(part, true,
                (getTabFolder().getStyle() & SWT.MULTI) == 0));
        tabItem.setImage(getLabelImage(part));
        tabItem.setToolTipText(getLabelToolTipText(part));
    }

    String getLabelText(IPresentablePart presentablePart, boolean dirtyLeft,
            boolean includePath) {
        String title = presentablePart.getTitle().trim();
        String text = title;

        if (includePath) {
            String titleTooltip = presentablePart.getTitleToolTip().trim();

            if (titleTooltip.endsWith(title))
                    titleTooltip = titleTooltip.substring(0,
                            titleTooltip.lastIndexOf(title)).trim();

            if (titleTooltip.endsWith("\\"))
                    //$NON-NLS-1$
                    //$NON-NLS-1$
                    titleTooltip = titleTooltip.substring(0,
                            titleTooltip.lastIndexOf("\\")).trim(); //$NON-NLS-1$

            if (titleTooltip.endsWith("/"))
                    //$NON-NLS-1$
                    //$NON-NLS-1$
                    titleTooltip = titleTooltip.substring(0,
                            titleTooltip.lastIndexOf("/")).trim(); //$NON-NLS-1$

            if (titleTooltip.length() >= 1) text += " - " + titleTooltip; //$NON-NLS-1$
        }

        if (presentablePart.isDirty()) {
            if (dirtyLeft)
                text = "* " + text; //$NON-NLS-1$
            else
                text = text + " *"; //$NON-NLS-1$
        }

        return text;
    }

    Image getLabelImage(IPresentablePart presentablePart) {
        return presentablePart.getTitleImage();
    }

    String getLabelToolTipText(IPresentablePart presentablePart) {
        return presentablePart.getTitleToolTip();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.skins.Presentation#setActive(boolean)
     */
    public void setActive(boolean isActive) {
        super.setActive(isActive);

        updateGradient();
    }

    /**
     * Set the tab folder tab style to a tradional style tab
     * 
     * @param traditionalTab
     *            <code>true</code> if traditional style tabs should be used
     *            <code>false</code> otherwise.
     */
    protected void setTabStyle(boolean traditionalTab) {
        // set the tab style to non-simple
        getTabFolder().setSimpleTab(traditionalTab);
    }

    private void showList(Shell parentShell, int x, int y) {
        final CTabFolder tabFolder = getTabFolder();
        ArrayList items = new ArrayList(Arrays.asList(tabFolder.getItems()));

        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            CTabItem tabItem = (CTabItem) iterator.next();

            if (tabItem.isShowing()) iterator.remove();
        }

        if (items.isEmpty()) return;
        int shellStyle = SWT.RESIZE | SWT.ON_TOP | SWT.NO_TRIM;
        int tableStyle = SWT.V_SCROLL | SWT.H_SCROLL;
        final EditorList editorList = new EditorList(tabFolder.getShell(),
                shellStyle, tableStyle);
        editorList.setInput(this);
        Point size = editorList.computeSizeHint();
        int minX = 50; //labelComposite.getSize().x;
        int minY = 300;
        if (size.x < minX) size.x = minX;
        if (size.y < minY) size.y = minY;
        editorList.setSize(size.x, size.y);
        Rectangle bounds = Display.getCurrent().getBounds();
        if (x + size.x > bounds.width) x = bounds.width - size.x;
        if (y + size.y > bounds.height) y = bounds.height - size.y;
        editorList.setLocation(new Point(x, y));
        editorList.setVisible(true);
        editorList.setFocus();
        editorList.getTableViewer().getTable().getShell().addListener(
                SWT.Deactivate, new Listener() {

                    public void handleEvent(Event event) {
                        editorList.setVisible(false);
                    }
                });
    }

    /**
     * Update the tab folder's colours to match the current theme settings and
     * active state
     */
    private void updateGradient() {
        Color fgColor;
        ITheme currentTheme = PlatformUI.getWorkbench().getThemeManager()
                .getCurrentTheme();
        FontRegistry fontRegistry = currentTheme.getFontRegistry();
        ColorRegistry colorRegistry = currentTheme.getColorRegistry();
        Color[] bgColors = new Color[2];
        int[] percent = new int[1];
        boolean vertical;
        if (isActive()) {
            fgColor = colorRegistry
                    .get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR);
            bgColors[0] = colorRegistry
                    .get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
            bgColors[1] = colorRegistry
                    .get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
            percent[0] = currentTheme
                    .getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT);
            vertical = currentTheme
                    .getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL);

        } else {
            fgColor = colorRegistry
                    .get(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR);
            bgColors[0] = colorRegistry
                    .get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START);
            bgColors[1] = colorRegistry
                    .get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END);
            percent[0] = currentTheme
                    .getInt(IWorkbenchThemeConstants.INACTIVE_TAB_PERCENT);
            vertical = currentTheme
                    .getBoolean(IWorkbenchThemeConstants.INACTIVE_TAB_VERTICAL);
        }

        getTabFolder().setFont(
                fontRegistry.get(IWorkbenchThemeConstants.TAB_TEXT_FONT));

        drawGradient(fgColor, bgColors, percent, vertical);
    }

    void setSelection(CTabItem tabItem) {
        getSite().selectPart(getPartForTab(tabItem));
    }

    void close(IPresentablePart presentablePart) {
        getSite().close(presentablePart);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.presentations.BasicStackPresentation#getCurrentTitle()
     */
    protected String getCurrentTitle() {
        return "";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3957.java