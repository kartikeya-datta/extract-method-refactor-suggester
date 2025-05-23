error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4638.java
text:
```scala
private static final S@@tring ORIENTATION_COMMAND_LINE = "-dir";//$NON-NLS-1$

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

package org.eclipse.ui.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ExternalActionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.window.WindowManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.commands.CommandManagerEvent;
import org.eclipse.ui.commands.ICommandManagerListener;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.contexts.ContextManagerEvent;
import org.eclipse.ui.contexts.IContextManagerListener;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.internal.activities.ws.WorkbenchActivitySupport;
import org.eclipse.ui.internal.commands.ws.CommandCallback;
import org.eclipse.ui.internal.commands.ws.WorkbenchCommandSupport;
import org.eclipse.ui.internal.contexts.ws.WorkbenchContextSupport;
import org.eclipse.ui.internal.intro.IIntroRegistry;
import org.eclipse.ui.internal.intro.IntroDescriptor;
import org.eclipse.ui.internal.misc.Assert;
import org.eclipse.ui.internal.misc.Policy;
import org.eclipse.ui.internal.misc.UIStats;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.internal.testing.WorkbenchTestable;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.FontDefinition;
import org.eclipse.ui.internal.themes.ThemeElementHelper;
import org.eclipse.ui.internal.themes.WorkbenchThemeManager;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.themes.IThemeManager;

/**
 * The workbench class represents the top of the Eclipse user interface. Its
 * primary responsability is the management of workbench windows, dialogs,
 * wizards, and other workbench-related windows.
 * <p>
 * Note that any code that is run during the creation of a workbench instance
 * should not required access to the display.
 * </p>
 * <p>
 * Note that this internal class changed significantly between 2.1 and 3.0.
 * Applications that used to define subclasses of this internal class need to
 * be rewritten to use the new workbench advisor API.
 * </p>
 */
public final class Workbench implements IWorkbench {
    private static final String LEFT_TO_RIGHT = "ltr"; //$NON-NLS-1$
	private static final String RIGHT_TO_LEFT = "rtl";//$NON-NLS-1$
	private static final String ORIENTATION_COMMAND_LINE = "-orientation";//$NON-NLS-1$
	private static final String ORIENTATION_PROPERTY = "eclipse.orientation";//$NON-NLS-1$
    private static final String VERSION_STRING[] = { "0.046", "2.0" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String DEFAULT_WORKBENCH_STATE_FILENAME = "workbench.xml"; //$NON-NLS-1$

    /**
     * Holds onto the only instance of Workbench.
     */
    private static Workbench instance;

    /**
     * The testable object facade.
     * 
     * @since 3.0
     */
    private static WorkbenchTestable testableObject;

    /**
     * The display used for all UI interactions with this workbench.
     * 
     * @since 3.0
     */
    private Display display;

    private WindowManager windowManager;

    private WorkbenchWindow activatedWindow;

    private EditorHistory editorHistory;

    private boolean runEventLoop = true;

    private boolean isStarting = true;

    private boolean isClosing = false;

    /**
     * PlatformUI return code (as opposed to IPlatformRunnable return code).
     */
    private int returnCode;

    private ListenerList windowListeners = new ListenerList();

    /**
     * Advisor providing application-specific configuration and customization
     * of the workbench.
     * 
     * @since 3.0
     */
    private WorkbenchAdvisor advisor;

    /**
     * Object for configuring the workbench. Lazily initialized to an instance
     * unique to the workbench instance.
     * 
     * @since 3.0
     */
    private WorkbenchConfigurer workbenchConfigurer;

    //for dynamic UI
    /**
     * ExtensionEventHandler handles extension life-cycle events. 
     */
    private ExtensionEventHandler extensionEventHandler;

    /**
     * A count of how many large updates are going on. This tracks nesting of
     * requests to disable services during a large update -- similar to the
     * <code>setRedraw</code> functionality on <code>Control</code>. When
     * this value becomes greater than zero, services are disabled. When this
     * value becomes zero, services are enabled. Please see
     * <code>largeUpdateStart()</code> and <code>largeUpdateEnd()</code>.
     */
    private int largeUpdates = 0;

    /**
     * Creates a new workbench.
     * 
     * @param display
     *            the display to be used for all UI interactions with the
     *            workbench
     * @param advisor
     *            the application-specific advisor that configures and
     *            specializes this workbench instance
     * @since 3.0
     */
    private Workbench(Display display, WorkbenchAdvisor advisor) {
        super();

        if (instance != null && instance.isRunning()) {
            throw new IllegalStateException(WorkbenchMessages
                    .getString("Workbench.CreatingWorkbenchTwice")); //$NON-NLS-1$
        }
        Assert.isNotNull(display);
        Assert.isNotNull(advisor);
        this.advisor = advisor;
        this.display = display;
        Workbench.instance = this;

        // for dynamic UI  [This seems to be for everything that isn't handled by some
        // subclass of RegistryManager.  I think that when an extension is moved to the
        // RegistryManager implementation, then it should be removed from the list in
        // ExtensionEventHandler#appear.
        // I've found that the new wizard extension in particular is a poor choice to
        // use as an example, since the result of reading the registry is not cached
        // -- so it is re-read each time.  The only real contribution of this dialog is
        // to show the user a nice dialog describing the addition.]
        extensionEventHandler = new ExtensionEventHandler(this);
        Platform.getExtensionRegistry().addRegistryChangeListener(
                extensionEventHandler);
    }

    /**
     * Returns the one and only instance of the workbench, if there is one.
     * 
     * @return the workbench, or <code>null</code> if the workbench has not
     *         been created, or has been created and already completed
     */
    public static final Workbench getInstance() {
        return instance;
    }

    /**
     * Creates the workbench and associates it with the the given display and
     * workbench advisor, and runs the workbench UI. This entails processing
     * and dispatching events until the workbench is closed or restarted.
     * <p>
     * This method is intended to be called by <code>PlatformUI</code>.
     * Fails if the workbench UI has already been created.
     * </p>
     * <p>
     * The display passed in must be the default display.
     * </p>
     * 
     * @param display
     *            the display to be used for all UI interactions with the
     *            workbench
     * @param advisor
     *            the application-specific advisor that configures and
     *            specializes the workbench
     * @return return code {@link PlatformUI#RETURN_OK RETURN_OK}for normal
     *         exit; {@link PlatformUI#RETURN_RESTART RETURN_RESTART}if the
     *         workbench was terminated with a call to
     *         {@link IWorkbench#restart IWorkbench.restart}; other values
     *         reserved for future use
     */
    public static final int createAndRunWorkbench(Display display,
            WorkbenchAdvisor advisor) {
        // create the workbench instance
        Workbench workbench = new Workbench(display, advisor);
        // run the workbench event loop
        int returnCode = workbench.runUI();
        return returnCode;
    }

    /**
     * Creates the <code>Display</code> to be used by the workbench.
     * 
     * @return the display
     */
    public static Display createDisplay() {
        // setup the application name used by SWT to lookup resources on some
        // platforms
        String applicationName = WorkbenchPlugin.getDefault().getAppName();
        if (applicationName != null) {
            Display.setAppName(applicationName);
        }

        // create the display
        Display newDisplay = null;
        if (Policy.DEBUG_SWT_GRAPHICS) {
            DeviceData data = new DeviceData();
            data.tracking = true;
            newDisplay = new Display(data);
        } else {
            newDisplay = new Display();
        }

        // workaround for 1GEZ9UR and 1GF07HN
        newDisplay.setWarnings(false);

        //Set the priority higher than normal so as to be higher
        //than the JobManager.
        Thread.currentThread().setPriority(
                Math.min(Thread.MAX_PRIORITY, Thread.NORM_PRIORITY + 1));

        return newDisplay;
    }

    /**
     * Returns the testable object facade, for use by the test harness.
     * 
     * @return the testable object facade
     * @since 3.0
     */
    public static WorkbenchTestable getWorkbenchTestable() {
        if (testableObject == null) {
            testableObject = new WorkbenchTestable();
        }
        return testableObject;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public void addWindowListener(IWindowListener l) {
        windowListeners.add(l);
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public void removeWindowListener(IWindowListener l) {
        windowListeners.remove(l);
    }

    /**
     * Fire window opened event.
     * 
     * @param window
     *            The window which just opened; should not be <code>null</code>.
     */
    protected void fireWindowOpened(final IWorkbenchWindow window) {
        Object list[] = windowListeners.getListeners();
        for (int i = 0; i < list.length; i++) {
            final IWindowListener l = (IWindowListener) list[i];
            Platform.run(new SafeRunnable() {
                public void run() {
                    l.windowOpened(window);
                }
            });
        }
    }

    /**
     * Fire window closed event.
     * 
     * @param window
     *            The window which just closed; should not be <code>null</code>.
     */
    protected void fireWindowClosed(final IWorkbenchWindow window) {
        if (activatedWindow == window) {
            // Do not hang onto it so it can be GC'ed
            activatedWindow = null;
        }

        Object list[] = windowListeners.getListeners();
        for (int i = 0; i < list.length; i++) {
            final IWindowListener l = (IWindowListener) list[i];
            Platform.run(new SafeRunnable() {
                public void run() {
                    l.windowClosed(window);
                }
            });
        }
    }

    /**
     * Fire window activated event.
     * 
     * @param window
     *            The window which was just activated; should not be <code>null</code>.
     */
    protected void fireWindowActivated(final IWorkbenchWindow window) {
        Object list[] = windowListeners.getListeners();
        for (int i = 0; i < list.length; i++) {
            final IWindowListener l = (IWindowListener) list[i];
            Platform.run(new SafeRunnable() {
                public void run() {
                    l.windowActivated(window);
                }
            });
        }
    }

    /**
     * Fire window deactivated event.
     * 
     * @param window
     *            The window which was just deactivated; should not be <code>null</code>.
     */
    protected void fireWindowDeactivated(final IWorkbenchWindow window) {
        Object list[] = windowListeners.getListeners();
        for (int i = 0; i < list.length; i++) {
            final IWindowListener l = (IWindowListener) list[i];
            Platform.run(new SafeRunnable() {
                public void run() {
                    l.windowDeactivated(window);
                }
            });
        }
    }

    /**
     * Closes the workbench. Assumes that the busy cursor is active.
     * 
     * @param force
     *            true if the close is mandatory, and false if the close is
     *            allowed to fail
     * @return true if the close succeeded, and false otherwise
     */
    private boolean busyClose(final boolean force) {

        // notify the advisor of preShutdown and allow it to veto if not forced
        isClosing = advisor.preShutdown();
        if (!force && !isClosing) {
            return false;
        }

        // save any open editors if they are dirty
        isClosing = saveAllEditors(!force);
        if (!force && !isClosing) {
            return false;
        }

        IPreferenceStore store = getPreferenceStore();
        boolean closeEditors = store
                .getBoolean(IPreferenceConstants.CLOSE_EDITORS_ON_EXIT);
        if (closeEditors) {
            Platform.run(new SafeRunnable() {
                public void run() {
                    IWorkbenchWindow windows[] = getWorkbenchWindows();
                    for (int i = 0; i < windows.length; i++) {
                        IWorkbenchPage pages[] = windows[i].getPages();
                        for (int j = 0; j < pages.length; j++) {
                            isClosing = isClosing
                                    && pages[j].closeAllEditors(false);
                        }
                    }
                }
            });
            if (!force && !isClosing) {
                return false;
            }
        }

        if (getWorkbenchConfigurer().getSaveAndRestore()) {
            Platform.run(new SafeRunnable() {
                public void run() {
                    XMLMemento mem = recordWorkbenchState();
                    //Save the IMemento to a file.
                    saveMementoToFile(mem);
                }

                public void handleException(Throwable e) {
                    String message;
                    if (e.getMessage() == null) {
                        message = WorkbenchMessages
                                .getString("ErrorClosingNoArg"); //$NON-NLS-1$
                    } else {
                        message = WorkbenchMessages
                                .format(
                                        "ErrorClosingOneArg", new Object[] { e.getMessage() }); //$NON-NLS-1$
                    }

                    if (!MessageDialog.openQuestion(null, WorkbenchMessages
                            .getString("Error"), message)) { //$NON-NLS-1$
                        isClosing = false;
                    }
                }
            });
        }
        if (!force && !isClosing) {
            return false;
        }

        Platform.run(new SafeRunnable(WorkbenchMessages
                .getString("ErrorClosing")) { //$NON-NLS-1$
                    public void run() {
                        if (isClosing || force)
                            isClosing = windowManager.close();
                    }
                });

        if (!force && !isClosing) {
            return false;
        }

        shutdown();

        runEventLoop = false;
        return true;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public boolean saveAllEditors(boolean confirm) {
        final boolean finalConfirm = confirm;
        final boolean[] result = new boolean[1];
        result[0] = true;

        Platform.run(new SafeRunnable(WorkbenchMessages
                .getString("ErrorClosing")) { //$NON-NLS-1$
                    public void run() {
                        //Collect dirtyEditors
                        ArrayList dirtyEditors = new ArrayList();
                        ArrayList dirtyEditorsInput = new ArrayList();
                        IWorkbenchWindow windows[] = getWorkbenchWindows();
                        for (int i = 0; i < windows.length; i++) {
                            IWorkbenchPage pages[] = windows[i].getPages();
                            for (int j = 0; j < pages.length; j++) {
                                WorkbenchPage page = (WorkbenchPage) pages[j];
                                IEditorPart editors[] = page.getDirtyEditors();
                                for (int k = 0; k < editors.length; k++) {
                                    IEditorPart editor = editors[k];
                                    if (editor.isDirty()) {
                                        if (!dirtyEditorsInput.contains(editor
                                                .getEditorInput())) {
                                            dirtyEditors.add(editor);
                                            dirtyEditorsInput.add(editor
                                                    .getEditorInput());
                                        }
                                    }
                                }
                            }
                        }
                        if (dirtyEditors.size() > 0) {
                            IWorkbenchWindow w = getActiveWorkbenchWindow();
                            if (w == null)
                                w = windows[0];
                            result[0] = EditorManager.saveAll(dirtyEditors,
                                    finalConfirm, w);
                        }
                    }
                });
        return result[0];
    }

    /**
     * Opens a new workbench window and page with a specific perspective.
     * 
     * Assumes that busy cursor is active.
     */
    private IWorkbenchWindow busyOpenWorkbenchWindow(String perspID,
            IAdaptable input) throws WorkbenchException {
        // Create a workbench window (becomes active window)
        WorkbenchWindow newWindow = newWorkbenchWindow();
        newWindow.create(); // must be created before adding to window manager
        windowManager.add(newWindow);

        // Create the initial page.
        try {
            newWindow.busyOpenPage(perspID, input);
        } catch (WorkbenchException e) {
            windowManager.remove(newWindow);
            throw e;
        }

        // Open window after opening page, to avoid flicker.
        newWindow.open();

        return newWindow;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public boolean close() {
        return close(PlatformUI.RETURN_OK, false);
    }

    /**
     * Closes the workbench, returning the given return code from the run
     * method. If forced, the workbench is closed no matter what.
     * 
     * @param returnCode
     *            {@link PlatformUI#RETURN_OK RETURN_OK}for normal exit;
     *            {@link PlatformUI#RETURN_RESTART RETURN_RESTART}if the
     *            workbench was terminated with a call to
     *            {@link IWorkbench#restart IWorkbench.restart};
     *            {@link PlatformUI#RETURN_UNSTARTABLE RETURN_UNSTARTABLE}if
     *            the workbench could not be started; other values reserved for
     *            future use
     * @param force
     *            true to force the workbench close, and false for a "soft"
     *            close that can be canceled
     * @return true if the close was successful, and false if the close was
     *         canceled
     */
    /* package */
    boolean close(int returnCode, final boolean force) {
        this.returnCode = returnCode;
        final boolean[] ret = new boolean[1];
        BusyIndicator.showWhile(null, new Runnable() {
            public void run() {
                ret[0] = busyClose(force);
            }
        });
        return ret[0];
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchWindow getActiveWorkbenchWindow() {
        // Return null if called from a non-UI thread.
        // This is not spec'ed behaviour and is misleading, however this is how it
        // worked in 2.1 and we cannot change it now.
        // For more details, see [Bug 57384] [RCP] Main window not active on startup
        if (Display.getCurrent() == null) {
            return null;
        }

        // Look at the current shell and up its parent
        // hierarchy for a workbench window.
        Control shell = display.getActiveShell();
        while (shell != null) {
            Object data = shell.getData();
            if (data instanceof IWorkbenchWindow)
                return (IWorkbenchWindow) data;
            shell = shell.getParent();
        }

        // Look for the window that was last known being
        // the active one
        WorkbenchWindow win = getActivatedWindow();
        if (win != null) {
            return win;
        }

        // Look at all the shells and pick the first one
        // that is a workbench window.
        Shell shells[] = display.getShells();
        for (int i = 0; i < shells.length; i++) {
            Object data = shells[i].getData();
            if (data instanceof IWorkbenchWindow)
                return (IWorkbenchWindow) data;
        }

        // Can't find anything!
        return null;
    }

    /*
     * Returns the editor history.
     */
    protected EditorHistory getEditorHistory() {
        if (editorHistory == null) {
            editorHistory = new EditorHistory();
        }
        return editorHistory;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IEditorRegistry getEditorRegistry() {
        return WorkbenchPlugin.getDefault().getEditorRegistry();
    }

    /*
     * Returns the number for a new window. This will be the first number > 0
     * which is not used to identify another window in the workbench.
     */
    private int getNewWindowNumber() {
        // Get window list.
        Window[] windows = windowManager.getWindows();
        int count = windows.length;

        // Create an array of booleans (size = window count).
        // Cross off every number found in the window list.
        boolean checkArray[] = new boolean[count];
        for (int nX = 0; nX < count; nX++) {
            if (windows[nX] instanceof WorkbenchWindow) {
                WorkbenchWindow ww = (WorkbenchWindow) windows[nX];
                int index = ww.getNumber() - 1;
                if (index >= 0 && index < count)
                    checkArray[index] = true;
            }
        }

        // Return first index which is not used.
        // If no empty index was found then every slot is full.
        // Return next index.
        for (int index = 0; index < count; index++) {
            if (!checkArray[index])
                return index + 1;
        }
        return count + 1;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IPerspectiveRegistry getPerspectiveRegistry() {
        return WorkbenchPlugin.getDefault().getPerspectiveRegistry();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public PreferenceManager getPreferenceManager() {
        return WorkbenchPlugin.getDefault().getPreferenceManager();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IPreferenceStore getPreferenceStore() {
        return WorkbenchPlugin.getDefault().getPreferenceStore();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public ISharedImages getSharedImages() {
        return WorkbenchPlugin.getDefault().getSharedImages();
    }

    /**
     * Returns the window manager for this workbench.
     * 
     * @return the window manager
     */
    /* package */
    WindowManager getWindowManager() {
        return windowManager;
    }

    /*
     * Answer the workbench state file.
     */
    private File getWorkbenchStateFile() {
        IPath path = WorkbenchPlugin.getDefault().getStateLocation();
        path = path.append(DEFAULT_WORKBENCH_STATE_FILENAME);
        return path.toFile();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public int getWorkbenchWindowCount() {
        return windowManager.getWindowCount();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchWindow[] getWorkbenchWindows() {
        Window[] windows = windowManager.getWindows();
        IWorkbenchWindow[] dwindows = new IWorkbenchWindow[windows.length];
        System.arraycopy(windows, 0, dwindows, 0, windows.length);
        return dwindows;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkingSetManager getWorkingSetManager() {
        return WorkbenchPlugin.getDefault().getWorkingSetManager();
    }

    /**
     * Initializes the workbench now that the display is created.
     * 
     * @return true if init succeeded.
     */
    private boolean init(Display display) {
        // setup debug mode if required.
        if (WorkbenchPlugin.getDefault().isDebugging()) {
            WorkbenchPlugin.DEBUG = true;
            ModalContext.setDebugMode(true);
        }

        // create workbench window manager
        windowManager = new WindowManager();

        IIntroRegistry introRegistry = WorkbenchPlugin.getDefault()
                .getIntroRegistry();
        if (introRegistry.getIntroCount() > 0) {
            IProduct product = Platform.getProduct();
            if (product != null) {
                introDescriptor = (IntroDescriptor) introRegistry
                        .getIntroForProduct(product.getId());
            }
        }

        // begin the initialization of the activity, command, and context
        // managers

        workbenchActivitySupport = new WorkbenchActivitySupport();
        activityHelper = ActivityPersistanceHelper.getInstance();

        workbenchContextSupport = new WorkbenchContextSupport(this);
        workbenchCommandSupport = new WorkbenchCommandSupport(this);
        workbenchContextSupport.initialize(); // deferred key binding support

        workbenchCommandSupport.getCommandManager().addCommandManagerListener(
                commandManagerListener);

        workbenchContextSupport.getContextManager().addContextManagerListener(
                contextManagerListener);

        initializeCommandResolver();

        addWindowListener(windowListener);

        // end the initialization of the activity, command, and context
        // managers

        initializeImages();
        initializeFonts();
        initializeColors();
        initializeApplicationColors();

        // now that the workbench is sufficiently initialized, let the advisor
        // have a turn.
        advisor.internalBasicInitialize(getWorkbenchConfigurer());

        // configure use of color icons in toolbars
        boolean useColorIcons = getPreferenceStore().getBoolean(
                IPreferenceConstants.COLOR_ICONS);
        ActionContributionItem.setUseColorIconsInToolbars(useColorIcons);

        // initialize workbench single-click vs double-click behavior
        initializeSingleClickOption();

        // deadlock code
        boolean avoidDeadlock = true;

        String[] commandLineArgs = Platform.getCommandLineArgs();
        for (int i = 0; i < commandLineArgs.length; i++) {
            if (commandLineArgs[i].equalsIgnoreCase("-allowDeadlock")) //$NON-NLS-1$
                avoidDeadlock = false;
        }
        
        Window.setDefaultOrientation(getDefaultOrientation(commandLineArgs));

        if (avoidDeadlock) {
            UILockListener uiLockListener = new UILockListener(display);
            Platform.getJobManager().setLockListener(uiLockListener);
            display
                    .setSynchronizer(new UISynchronizer(display, uiLockListener));
        }

        // attempt to restore a previous workbench state
        try {
            UIStats.start(UIStats.RESTORE_WORKBENCH, "Workbench"); //$NON-NLS-1$

            advisor.preStartup();

            if (!advisor.openWindows()) {
                return false;
            }

        } finally {
            UIStats.end(UIStats.RESTORE_WORKBENCH, "Workbench"); //$NON-NLS-1$
        }

        forceOpenPerspective();

        isStarting = false;

        return true;
    }

    /**
     * Get the default orientation from the command line
     * arguments. If there are no arguments imply the 
     * orientation.
	 * @param commandLineArgs
	 * @return int
	 * @see SWT#NONE
	 * @see SWT#RIGHT_TO_LEFT
	 * @see SWT#LEFT_TO_RIGHT
	 */
	private int getDefaultOrientation(String[] commandLineArgs) {
		
		int orientation = getCommandLineOrientation(commandLineArgs);
		
		orientation = getSystemPropertyOrientation(commandLineArgs);
		
		if(orientation != SWT.NONE)
			return orientation;

		Locale locale = Locale.getDefault();
		String lang = locale.getLanguage();

		if ("iw".equals(lang) || "ar".equals(lang) || "fa".equals(lang) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
 "ur".equals(lang)) //$NON-NLS-1$
			return SWT.RIGHT_TO_LEFT;
			
		return SWT.NONE; //Use the default value if there is nothing specified
	}

	/**
	 * Check to see if the orientation was set in the
	 * system properties. If there is no orientation 
	 * specified return SWT#NONE.
	 * @param commandLineArgs
	 * @return int
	 * @see SWT#NONE
	 * @see SWT#RIGHT_TO_LEFT
	 * @see SWT#LEFT_TO_RIGHT
	 */
	private int getSystemPropertyOrientation(String[] commandLineArgs) {
		String orientation = System.getProperty(ORIENTATION_PROPERTY);
		if(RIGHT_TO_LEFT.equals(orientation))
			return SWT.RIGHT_TO_LEFT;
		if(LEFT_TO_RIGHT.equals(orientation))
			return SWT.LEFT_TO_RIGHT;
		return SWT.NONE;
	}

	/**
	 * Find the orientation in the commandLineArgs. If there
	 * is no orientation specified return SWT#NONE.
	 * @param commandLineArgs
	 * @return int
	 * @see SWT#NONE
	 * @see SWT#RIGHT_TO_LEFT
	 * @see SWT#LEFT_TO_RIGHT
	 */
	private int getCommandLineOrientation(String[] commandLineArgs) {
		//Do not process the last one as it will never have a parameter
		for (int i = 0; i < commandLineArgs.length - 1; i++) {
			if(commandLineArgs[i].equalsIgnoreCase(ORIENTATION_COMMAND_LINE)){
				String orientation = commandLineArgs[i+1];
				if(orientation.equals(RIGHT_TO_LEFT)){
					System.setProperty(ORIENTATION_PROPERTY,RIGHT_TO_LEFT);
					return SWT.RIGHT_TO_LEFT;
				}
				if(orientation.equals(LEFT_TO_RIGHT)){
					System.setProperty(ORIENTATION_PROPERTY,LEFT_TO_RIGHT);
					return SWT.LEFT_TO_RIGHT;
				}
			}
		}
		
		return SWT.NONE;
	}

    /**
     * Establishes the relationship between JFace actions and the command manager.
     */
    private void initializeCommandResolver() {
        ExternalActionManager.getInstance().setCallback(
                new CommandCallback(this));
    }

    /**
     * Initialize colors defined by the new colorDefinitions extension point.
     * Note this will be rolled into initializeColors() at some point.
     * 
     * @since 3.0
     */
    private void initializeApplicationColors() {
        ColorDefinition[] colorDefinitions = WorkbenchPlugin.getDefault()
                .getThemeRegistry().getColors();
        ThemeElementHelper.populateRegistry(getThemeManager().getTheme(
                IThemeManager.DEFAULT_THEME), colorDefinitions,
                getPreferenceStore());
    }

    private void initializeSingleClickOption() {
        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        boolean openOnSingleClick = store
                .getBoolean(IPreferenceConstants.OPEN_ON_SINGLE_CLICK);
        boolean selectOnHover = store
                .getBoolean(IPreferenceConstants.SELECT_ON_HOVER);
        boolean openAfterDelay = store
                .getBoolean(IPreferenceConstants.OPEN_AFTER_DELAY);
        int singleClickMethod = openOnSingleClick ? OpenStrategy.SINGLE_CLICK
                : OpenStrategy.DOUBLE_CLICK;
        if (openOnSingleClick) {
            if (selectOnHover)
                singleClickMethod |= OpenStrategy.SELECT_ON_HOVER;
            if (openAfterDelay)
                singleClickMethod |= OpenStrategy.ARROW_KEYS_OPEN;
        }
        OpenStrategy.setOpenMethod(singleClickMethod);
    }

    /*
     * Initializes the workbench fonts with the stored values. 
     */
    private void initializeFonts() {
        FontDefinition[] fontDefinitions = WorkbenchPlugin.getDefault()
                .getThemeRegistry().getFonts();
        ThemeElementHelper.populateRegistry(getThemeManager().getTheme(
                IThemeManager.DEFAULT_THEME), fontDefinitions,
                getPreferenceStore());
    }

    /*
     * Initialize the workbench images.
     *
     * @param windowImages
     *            An array of the descriptors of the images to be used in the
     *            corner of each window, or <code>null</code> if none. It is
     *            expected that the array will contain the same icon, rendered
     *            at different sizes.
     * @since 3.0
     */
    private void initializeImages() {
        ImageDescriptor[] windowImages = WorkbenchPlugin.getDefault()
                .getWindowImages();
        if (windowImages == null)
            return;

        Image[] images = new Image[windowImages.length];
        for (int i = 0; i < windowImages.length; ++i)
            images[i] = windowImages[i].createImage();
        Window.setDefaultImages(images);
    }

    /*
     * Take the workbenches' images out of the shared registry.
     * 
     * @since 3.0
     */
    private void uninitializeImages() {
        Window.setDefaultImage(null);
    }

    /*
     * Initialize the workbench colors.
     * 
     * @since 3.0
     */
    private void initializeColors() {
        // @issue some colors are generic; some are app-specific
        WorkbenchColors.startup();
    }

    /**
     * Returns <code>true</code> if the workbench is in the process of
     * closing.
     */
    public boolean isClosing() {
        return isClosing;
    }

    /*
     * Returns true if the workbench is in the process of starting
     */
    /* package */
    boolean isStarting() {
        return isStarting;
    }

    /*
     * Creates a new workbench window.
     * 
     * @return the new workbench window
     */
    private WorkbenchWindow newWorkbenchWindow() {
        return new WorkbenchWindow(getNewWindowNumber());
    }

    /*
     * If a perspective was specified on the command line (-perspective) then
     * force that perspective to open in the active window.
     */
    private void forceOpenPerspective() {
        if (getWorkbenchWindowCount() == 0) {
            // there should be an open window by now, bail out.
            return;
        }

        String perspId = null;
        String[] commandLineArgs = Platform.getCommandLineArgs();
        for (int i = 0; i < commandLineArgs.length - 1; i++) {
            if (commandLineArgs[i].equalsIgnoreCase("-perspective")) { //$NON-NLS-1$
                perspId = commandLineArgs[i + 1];
                break;
            }
        }
        if (perspId == null) {
            return;
        }
        IPerspectiveDescriptor desc = getPerspectiveRegistry()
                .findPerspectiveWithId(perspId);
        if (desc == null) {
            return;
        }

        IWorkbenchWindow win = getActiveWorkbenchWindow();
        if (win == null) {
            win = getWorkbenchWindows()[0];
        }
        try {
            showPerspective(perspId, win);
        } catch (WorkbenchException e) {
            String msg = "Workbench exception showing specified command line perspective on startup."; //$NON-NLS-1$
            WorkbenchPlugin.log(msg, new Status(IStatus.ERROR,
                    PlatformUI.PLUGIN_ID, 0, msg, e));
        }
    }

    /*
     * opens the initial workbench window.
     */
    /* package */void openFirstTimeWindow() {

        // create the workbench window
        WorkbenchWindow newWindow = newWorkbenchWindow();
        newWindow.create();
        windowManager.add(newWindow);

        // Create the initial page.
        try {
            newWindow.openPage(
                    getPerspectiveRegistry().getDefaultPerspective(),
                    getDefaultPageInput());
        } catch (WorkbenchException e) {
            ErrorDialog.openError(newWindow.getShell(), WorkbenchMessages
                    .getString("Problems_Opening_Page"), //$NON-NLS-1$
                    e.getMessage(), e.getStatus());
        }
        newWindow.open();
    }

    /*
     * Restores the workbench UI from the workbench state file (workbench.xml).
     * 
     * @return a status object indicating OK if a window was opened, RESTORE_CODE_RESET if no
     *   window was opened but one should be, and RESTORE_CODE_EXIT if the
     *   workbench should close immediately
     */
    /* package */IStatus restoreState() {

        if (!getWorkbenchConfigurer().getSaveAndRestore()) {
            String msg = WorkbenchMessages
                    .getString("Workbench.restoreDisabled"); //$NON-NLS-1$
            return new Status(IStatus.WARNING, WorkbenchPlugin.PI_WORKBENCH,
                    IWorkbenchConfigurer.RESTORE_CODE_RESET, msg, null);
        }
        // Read the workbench state file.
        final File stateFile = getWorkbenchStateFile();
        // If there is no state file cause one to open.
        if (!stateFile.exists()) {
            String msg = WorkbenchMessages
                    .getString("Workbench.noStateToRestore"); //$NON-NLS-1$
            return new Status(IStatus.WARNING, WorkbenchPlugin.PI_WORKBENCH,
                    IWorkbenchConfigurer.RESTORE_CODE_RESET, msg, null); //$NON-NLS-1$
        }

        final IStatus result[] = { new Status(IStatus.OK,
                WorkbenchPlugin.PI_WORKBENCH, IStatus.OK, "", null) }; //$NON-NLS-1$
        Platform.run(new SafeRunnable(WorkbenchMessages
                .getString("ErrorReadingState")) { //$NON-NLS-1$
                    public void run() throws Exception {
                        FileInputStream input = new FileInputStream(stateFile);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(input, "utf-8")); //$NON-NLS-1$
                        IMemento memento = XMLMemento.createReadRoot(reader);

                        // Validate known version format
                        String version = memento
                                .getString(IWorkbenchConstants.TAG_VERSION);
                        boolean valid = false;
                        for (int i = 0; i < VERSION_STRING.length; i++) {
                            if (VERSION_STRING[i].equals(version)) {
                                valid = true;
                                break;
                            }
                        }
                        if (!valid) {
                            reader.close();
                            String msg = WorkbenchMessages
                                    .getString("Invalid_workbench_state_ve"); //$NON-NLS-1$
                            MessageDialog.openError((Shell) null,
                                    WorkbenchMessages
                                            .getString("Restoring_Problems"), //$NON-NLS-1$
                                    msg); //$NON-NLS-1$
                            stateFile.delete();
                            result[0] = new Status(IStatus.ERROR,
                                    WorkbenchPlugin.PI_WORKBENCH,
                                    IWorkbenchConfigurer.RESTORE_CODE_RESET,
                                    msg, null);
                            return;
                        }

                        // Validate compatible version format
                        // We no longer support the release 1.0 format
                        if (VERSION_STRING[0].equals(version)) {
                            reader.close();
                            String msg = WorkbenchMessages
                                    .getString("Workbench.incompatibleSavedStateVersion"); //$NON-NLS-1$
                            boolean ignoreSavedState = new MessageDialog(
                                    null,
                                    WorkbenchMessages
                                            .getString("Workbench.incompatibleUIState"),//$NON-NLS-1$
                                    null, msg, MessageDialog.WARNING,
                                    new String[] { IDialogConstants.OK_LABEL,
                                            IDialogConstants.CANCEL_LABEL }, 0)
                                    .open() == 0;
                            // OK is the default
                            if (ignoreSavedState) {
                                stateFile.delete();
                                result[0] = new Status(
                                        IStatus.WARNING,
                                        WorkbenchPlugin.PI_WORKBENCH,
                                        IWorkbenchConfigurer.RESTORE_CODE_RESET,
                                        msg, null);
                            } else {
                                result[0] = new Status(IStatus.WARNING,
                                        WorkbenchPlugin.PI_WORKBENCH,
                                        IWorkbenchConfigurer.RESTORE_CODE_EXIT,
                                        msg, null);
                            }
                            return;
                        }

                        // Restore the saved state
                        IStatus restoreResult = restoreState(memento);
                        reader.close();
                        if (restoreResult.getSeverity() == IStatus.ERROR) {
                            ErrorDialog
                                    .openError(
                                            null,
                                            WorkbenchMessages
                                                    .getString("Workspace.problemsTitle"), //$NON-NLS-1$
                                            WorkbenchMessages
                                                    .getString("Workbench.problemsRestoringMsg"), //$NON-NLS-1$
                                            restoreResult);
                        }
                    }

                    public void handleException(Throwable e) {
                        super.handleException(e);
                        String msg = e.getMessage() == null ? "" : e.getMessage(); //$NON-NLS-1$
                        result[0] = new Status(IStatus.ERROR,
                                WorkbenchPlugin.PI_WORKBENCH,
                                IWorkbenchConfigurer.RESTORE_CODE_RESET, msg, e);
                        stateFile.delete();
                    }

                });
        // ensure at least one window was opened
        if (result[0].isOK() && windowManager.getWindows().length == 0) {
            String msg = WorkbenchMessages
                    .getString("Workbench.noWindowsRestored"); //$NON-NLS-1$
            result[0] = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH,
                    IWorkbenchConfigurer.RESTORE_CODE_RESET, msg, null);
        }
        return result[0];
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchWindow openWorkbenchWindow(IAdaptable input)
            throws WorkbenchException {
        return openWorkbenchWindow(getPerspectiveRegistry()
                .getDefaultPerspective(), input);
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchWindow openWorkbenchWindow(final String perspID,
            final IAdaptable input) throws WorkbenchException {
        // Run op in busy cursor.
        final Object[] result = new Object[1];
        BusyIndicator.showWhile(null, new Runnable() {
            public void run() {
                try {
                    result[0] = busyOpenWorkbenchWindow(perspID, input);
                } catch (WorkbenchException e) {
                    result[0] = e;
                }
            }
        });
        if (result[0] instanceof IWorkbenchWindow) {
            return (IWorkbenchWindow) result[0];
        } else if (result[0] instanceof WorkbenchException) {
            throw (WorkbenchException) result[0];
        } else {
            throw new WorkbenchException(WorkbenchMessages
                    .getString("Abnormal_Workbench_Conditi")); //$NON-NLS-1$
        }
    }

    /*
     * Record the workbench UI in a document
     */
    private XMLMemento recordWorkbenchState() {
        XMLMemento memento = XMLMemento
                .createWriteRoot(IWorkbenchConstants.TAG_WORKBENCH);
        IStatus status = saveState(memento);
        if (status.getSeverity() != IStatus.OK) {
            // don't use newWindow as parent because it has not yet been opened (bug 76724)
            ErrorDialog.openError(null, WorkbenchMessages
                    .getString("Workbench.problemsSaving"), //$NON-NLS-1$
                    WorkbenchMessages.getString("Workbench.problemsSavingMsg"), //$NON-NLS-1$
                    status);
        }
        return memento;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public boolean restart() {
        // this is the return code from run() to trigger a restart
        return close(PlatformUI.RETURN_RESTART, false);
    }

    /*
     * Restores the state of the previously saved workbench
     */
    private IStatus restoreState(IMemento memento) {

        MultiStatus result = new MultiStatus(
                PlatformUI.PLUGIN_ID,
                IStatus.OK,
                WorkbenchMessages.getString("Workbench.problemsRestoring"), null); //$NON-NLS-1$
        IMemento childMem;
        try {
            UIStats.start(UIStats.RESTORE_WORKBENCH, "MRUList"); //$NON-NLS-1$
            IMemento mruMemento = memento
                    .getChild(IWorkbenchConstants.TAG_MRU_LIST); //$NON-NLS-1$
            if (mruMemento != null) {
                result.add(getEditorHistory().restoreState(mruMemento));
            }
        } finally {
            UIStats.end(UIStats.RESTORE_WORKBENCH, "MRUList"); //$NON-NLS-1$
        }
        // Get the child windows.
        IMemento[] children = memento
                .getChildren(IWorkbenchConstants.TAG_WINDOW);

        // Read the workbench windows.
        for (int x = 0; x < children.length; x++) {
            childMem = children[x];
            WorkbenchWindow newWindow = newWorkbenchWindow();
            newWindow.create();

            // allow the application to specify an initial perspective to open
            // @issue temporary workaround for ignoring initial perspective
            //			String initialPerspectiveId =
            // getAdvisor().getInitialWindowPerspectiveId();
            //			if (initialPerspectiveId != null) {
            //				IPerspectiveDescriptor desc =
            // getPerspectiveRegistry().findPerspectiveWithId(initialPerspectiveId);
            //				result.merge(newWindow.restoreState(childMem, desc));
            //			}
            // add the window so that any work done in newWindow.restoreState that relies on Workbench methods has windows to work with						
            windowManager.add(newWindow);
            // whether the window was opened
            boolean opened = false;
            // now that we've added it to the window manager we need to listen 
            // for any exception that might hose us before we get a chance to
            // open it.  If one occurs, remove the new window from the manager.
            try {
                result.merge(newWindow.restoreState(childMem, null));
                try {
                    getAdvisor().postWindowRestore(
                            newWindow.getWindowConfigurer());
                } catch (WorkbenchException e) {
                    result.add(e.getStatus());
                }
                newWindow.open();
                opened = true;
            } finally {
                if (!opened)
                    newWindow.close();
            }
        }
        return result;
    }

    /**
     * Returns an array with the ids of all plugins that extend the
     * <code>org.eclipse.ui.startup</code> extension point.
     */
    public String[] getEarlyActivatedPlugins() {
        IExtensionPoint point = Platform.getExtensionRegistry()
                .getExtensionPoint(PlatformUI.PLUGIN_ID,
                        IWorkbenchConstants.PL_STARTUP);
        IExtension[] extensions = point.getExtensions();
        String[] result = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            result[i] = extensions[i].getNamespace();
        }
        return result;
    }

    /*
     * Starts all plugins that extend the <code> org.eclipse.ui.startup </code>
     * extension point, and that the user has not disabled via the preference
     * page.
     */
    private void startPlugins() {
        Runnable work = new Runnable() {
            final String disabledPlugins = getPreferenceStore().getString(
                    IPreferenceConstants.PLUGINS_NOT_ACTIVATED_ON_STARTUP);

            public void run() {
                IExtensionRegistry registry = Platform.getExtensionRegistry();

                // bug 55901: don't use getConfigElements directly, for pre-3.0
                //            compat, make sure to allow both missing class
                //            attribute and a missing startup element
                IExtensionPoint point = registry.getExtensionPoint(
                        PlatformUI.PLUGIN_ID, IWorkbenchConstants.PL_STARTUP);

                IExtension[] extensions = point.getExtensions();
                for (int i = 0; i < extensions.length; ++i) {
                    IExtension extension = extensions[i];

                    // if the plugin is not in the set of disabled plugins, then
                    // execute the code to start it
                    if (disabledPlugins.indexOf(extension.getNamespace()) == -1)
                        Platform.run(new EarlyStartupRunnable(extension));
                }
            }
        };

        Thread thread = new Thread(work);
        thread.start();
    }

    /**
     * Internal method for running the workbench UI. This entails processing
     * and dispatching events until the workbench is closed or restarted.
     * 
     * @return return code {@link PlatformUI#RETURN_OK RETURN_OK}for normal
     *         exit; {@link PlatformUI#RETURN_RESTART RETURN_RESTART}if the
     *         workbench was terminated with a call to
     *         {@link IWorkbench#restart IWorkbench.restart};
     *         {@link PlatformUI#RETURN_UNSTARTABLE RETURN_UNSTARTABLE}if the
     *         workbench could not be started; other values reserved for future
     *         use
     * @since 3.0
     */
    private int runUI() {
        UIStats.start(UIStats.START_WORKBENCH, "Workbench"); //$NON-NLS-1$

        Listener closeListener = new Listener() {
            public void handleEvent(Event event) {
                event.doit = close();
            }
        };

        // Initialize an exception handler.
        Window.IExceptionHandler handler = ExceptionHandler.getInstance();

        try {
            // react to display close event by closing workbench nicely
            display.addListener(SWT.Close, closeListener);

            // install backstop to catch exceptions thrown out of event loop
            Window.setExceptionHandler(handler);

            // initialize workbench and restore or open one window
            boolean initOK = init(display);

            // drop the splash screen now that a workbench window is up
            Platform.endSplash();

            // let the advisor run its start up code
            if (initOK) {
                advisor.postStartup(); // may trigger a close/restart
            }

            if (initOK && runEventLoop) {
                // start eager plug-ins
                startPlugins();

                display.asyncExec(new Runnable() {
                    public void run() {
                        UIStats.end(UIStats.START_WORKBENCH, "Workbench"); //$NON-NLS-1$
                    }
                });

                getWorkbenchTestable().init(display, this);

                // the event loop
                runEventLoop(handler, display);
            }

        } catch (final Exception e) {
            if (!display.isDisposed()) {
                handler.handleException(e);
            } else {
                String msg = "Exception in Workbench.runUI after display was disposed"; //$NON-NLS-1$
                WorkbenchPlugin.log(msg, new Status(IStatus.ERROR,
                        WorkbenchPlugin.PI_WORKBENCH, 1, msg, e));
            }
        } finally {
            // mandatory clean up
            if (!display.isDisposed()) {
                display.removeListener(SWT.Close, closeListener);
            }
        }

        // restart or exit based on returnCode
        return returnCode;
    }

    /*
     * Runs an event loop for the workbench.
     */
    private void runEventLoop(Window.IExceptionHandler handler, Display display) {
        runEventLoop = true;
        while (runEventLoop) {
            try {
                if (!display.readAndDispatch()) {
                    getAdvisor().eventLoopIdle(display);
                }
            } catch (Throwable t) {
                handler.handleException(t);
            }
        }
    }

    /*
     * Saves the current state of the workbench so it can be restored later on
     */
    private IStatus saveState(IMemento memento) {
        MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
                WorkbenchMessages.getString("Workbench.problemsSaving"), null); //$NON-NLS-1$

        // Save the version number.
        memento.putString(IWorkbenchConstants.TAG_VERSION, VERSION_STRING[1]);

        // Save the workbench windows.
        IWorkbenchWindow[] windows = getWorkbenchWindows();
        for (int nX = 0; nX < windows.length; nX++) {
            WorkbenchWindow window = (WorkbenchWindow) windows[nX];
            IMemento childMem = memento
                    .createChild(IWorkbenchConstants.TAG_WINDOW);
            result.merge(window.saveState(childMem));
        }
        result.add(getEditorHistory().saveState(
                memento.createChild(IWorkbenchConstants.TAG_MRU_LIST))); //$NON-NLS-1$
        return result;
    }

    /*
     * Save the workbench UI in a persistence file.
     */
    private boolean saveMementoToFile(XMLMemento memento) {
        // Save it to a file.
        File stateFile = getWorkbenchStateFile();
        try {
            FileOutputStream stream = new FileOutputStream(stateFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "utf-8"); //$NON-NLS-1$
            memento.save(writer);
            writer.close();
        } catch (IOException e) {
            stateFile.delete();
            MessageDialog.openError((Shell) null, WorkbenchMessages
                    .getString("SavingProblem"), //$NON-NLS-1$
                    WorkbenchMessages.getString("ProblemSavingState")); //$NON-NLS-1$
            return false;
        }

        // Success !
        return true;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchPage showPerspective(String perspectiveId,
            IWorkbenchWindow window) throws WorkbenchException {
        Assert.isNotNull(perspectiveId);

        // If the specified window has the requested perspective open, then the
        // window
        // is given focus and the perspective is shown. The page's input is
        // ignored.
        WorkbenchWindow win = (WorkbenchWindow) window;
        if (win != null) {
            WorkbenchPage page = win.getActiveWorkbenchPage();
            if (page != null) {
                IPerspectiveDescriptor perspectives[] = page
                        .getOpenedPerspectives();
                for (int i = 0; i < perspectives.length; i++) {
                    IPerspectiveDescriptor persp = perspectives[i];
                    if (perspectiveId.equals(persp.getId())) {
                        win.getShell().open();
                        page.setPerspective(persp);
                        return page;
                    }
                }
            }
        }

        // If another window that has the workspace root as input and the
        // requested
        // perpective open and active, then the window is given focus.
        IAdaptable input = getDefaultPageInput();
        IWorkbenchWindow[] windows = getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            win = (WorkbenchWindow) windows[i];
            if (window != win) {
                WorkbenchPage page = win.getActiveWorkbenchPage();
                if (page != null) {
                    boolean inputSame = false;
                    if (input == null)
                        inputSame = (page.getInput() == null);
                    else
                        inputSame = input.equals(page.getInput());
                    if (inputSame) {
                        Perspective persp = page.getActivePerspective();
                        if (perspectiveId.equals(persp.getDesc().getId())) {
                            Shell shell = win.getShell();
                            shell.open();
                            if (shell.getMinimized())
                                shell.setMinimized(false);
                            return page;
                        }
                    }
                }
            }
        }

        // Otherwise the requested perspective is opened and shown in the
        // specified
        // window or in a new window depending on the current user preference
        // for opening
        // perspectives, and that window is given focus.
        win = (WorkbenchWindow) window;
        if (win != null) {
            IPreferenceStore store = WorkbenchPlugin.getDefault()
                    .getPreferenceStore();
            int mode = store.getInt(IPreferenceConstants.OPEN_PERSP_MODE);
            IWorkbenchPage page = win.getActiveWorkbenchPage();
            IPerspectiveDescriptor persp = null;
            if (page != null)
                persp = page.getPerspective();

            // Only open a new window if user preference is set and the window
            // has an active perspective.
            if (IPreferenceConstants.OPM_NEW_WINDOW == mode && persp != null) {
                IWorkbenchWindow newWindow = openWorkbenchWindow(perspectiveId,
                        input);
                return newWindow.getActivePage();
            } else {
                IPerspectiveDescriptor desc = getPerspectiveRegistry()
                        .findPerspectiveWithId(perspectiveId);
                if (desc == null)
                    throw new WorkbenchException(
                            WorkbenchMessages
                                    .getString("WorkbenchPage.ErrorRecreatingPerspective")); //$NON-NLS-1$
                win.getShell().open();
                if (page == null)
                    page = win.openPage(perspectiveId, input);
                else
                    page.setPerspective(desc);
                return page;
            }
        }

        // Just throw an exception....
        throw new WorkbenchException(
                WorkbenchMessages
                        .format(
                                "Workbench.showPerspectiveError", new Object[] { perspectiveId })); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IWorkbenchPage showPerspective(String perspectiveId,
            IWorkbenchWindow window, IAdaptable input)
            throws WorkbenchException {
        Assert.isNotNull(perspectiveId);

        // If the specified window has the requested perspective open and the
        // same requested
        // input, then the window is given focus and the perspective is shown.
        boolean inputSameAsWindow = false;
        WorkbenchWindow win = (WorkbenchWindow) window;
        if (win != null) {
            WorkbenchPage page = win.getActiveWorkbenchPage();
            if (page != null) {
                boolean inputSame = false;
                if (input == null)
                    inputSame = (page.getInput() == null);
                else
                    inputSame = input.equals(page.getInput());
                if (inputSame) {
                    inputSameAsWindow = true;
                    IPerspectiveDescriptor perspectives[] = page
                            .getOpenedPerspectives();
                    for (int i = 0; i < perspectives.length; i++) {
                        IPerspectiveDescriptor persp = perspectives[i];
                        if (perspectiveId.equals(persp.getId())) {
                            win.getShell().open();
                            page.setPerspective(persp);
                            return page;
                        }
                    }
                }
            }
        }

        // If another window has the requested input and the requested
        // perpective open and active, then that window is given focus.
        IWorkbenchWindow[] windows = getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            win = (WorkbenchWindow) windows[i];
            if (window != win) {
                WorkbenchPage page = win.getActiveWorkbenchPage();
                if (page != null) {
                    boolean inputSame = false;
                    if (input == null)
                        inputSame = (page.getInput() == null);
                    else
                        inputSame = input.equals(page.getInput());
                    if (inputSame) {
                        Perspective persp = page.getActivePerspective();
                        if (perspectiveId.equals(persp.getDesc().getId())) {
                            win.getShell().open();
                            return page;
                        }
                    }
                }
            }
        }

        // If the specified window has the same requested input but not the
        // requested
        // perspective, then the window is given focus and the perspective is
        // opened and shown
        // on condition that the user preference is not to open perspectives in
        // a new window.
        win = (WorkbenchWindow) window;
        if (inputSameAsWindow && win != null) {
            IPreferenceStore store = WorkbenchPlugin.getDefault()
                    .getPreferenceStore();
            int mode = store.getInt(IPreferenceConstants.OPEN_PERSP_MODE);

            if (IPreferenceConstants.OPM_NEW_WINDOW != mode) {
                IWorkbenchPage page = win.getActiveWorkbenchPage();
                IPerspectiveDescriptor desc = getPerspectiveRegistry()
                        .findPerspectiveWithId(perspectiveId);
                if (desc == null)
                    throw new WorkbenchException(
                            WorkbenchMessages
                                    .getString("WorkbenchPage.ErrorRecreatingPerspective")); //$NON-NLS-1$
                win.getShell().open();
                if (page == null)
                    page = win.openPage(perspectiveId, input);
                else
                    page.setPerspective(desc);
                return page;
            }
        }

        // If the specified window has no active perspective, then open the
        // requested perspective and show the specified window.
        if (win != null) {
            IWorkbenchPage page = win.getActiveWorkbenchPage();
            IPerspectiveDescriptor persp = null;
            if (page != null)
                persp = page.getPerspective();
            if (persp == null) {
                IPerspectiveDescriptor desc = getPerspectiveRegistry()
                        .findPerspectiveWithId(perspectiveId);
                if (desc == null)
                    throw new WorkbenchException(
                            WorkbenchMessages
                                    .getString("WorkbenchPage.ErrorRecreatingPerspective")); //$NON-NLS-1$
                win.getShell().open();
                if (page == null)
                    page = win.openPage(perspectiveId, input);
                else
                    page.setPerspective(desc);
                return page;
            }
        }

        // Otherwise the requested perspective is opened and shown in a new
        // window, and the
        // window is given focus.
        IWorkbenchWindow newWindow = openWorkbenchWindow(perspectiveId, input);
        return newWindow.getActivePage();
    }

    /*
     * Shuts down the application.
     */
    private void shutdown() {
        // shutdown application-specific portions first
        advisor.postShutdown();

        // for dynamic UI 
        Platform.getExtensionRegistry().removeRegistryChangeListener(
                extensionEventHandler);

        // shutdown the rest of the workbench
        WorkbenchColors.shutdown();
        activityHelper.shutdown();
        uninitializeImages();
        if (WorkbenchPlugin.getDefault() != null) {
            WorkbenchPlugin.getDefault().reset();
        }
        WorkbenchThemeManager.getInstance().dispose();
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public IDecoratorManager getDecoratorManager() {
        return WorkbenchPlugin.getDefault().getDecoratorManager();
    }

    /*
     * Returns the workbench window which was last known being the active one,
     * or <code> null </code> .
     */
    private WorkbenchWindow getActivatedWindow() {
        if (activatedWindow != null) {
            Shell shell = activatedWindow.getShell();
            if (shell != null && !shell.isDisposed()) {
                return activatedWindow;
            }
        }

        return null;
    }

    /*
     * Sets the workbench window which was last known being the active one, or
     * <code> null </code> .
     */
    /* package */
    void setActivatedWindow(WorkbenchWindow window) {
        activatedWindow = window;
    }

    /**
     * Returns the unique object that applications use to configure the
     * workbench.
     * <p>
     * IMPORTANT This method is declared package-private to prevent regular
     * plug-ins from downcasting IWorkbench to Workbench and getting hold of
     * the workbench configurer that would allow them to tamper with the
     * workbench. The workbench configurer is available only to the
     * application.
     * </p>
     */
    /* package */
    WorkbenchConfigurer getWorkbenchConfigurer() {
        if (workbenchConfigurer == null) {
            workbenchConfigurer = new WorkbenchConfigurer();
        }
        return workbenchConfigurer;
    }

    /**
     * Returns the workbench advisor that created this workbench.
     * <p>
     * IMPORTANT This method is declared package-private to prevent regular
     * plug-ins from downcasting IWorkbench to Workbench and getting hold of
     * the workbench advisor that would allow them to tamper with the
     * workbench. The workbench advisor is internal to the application.
     * </p>
     */
    /* package */
    WorkbenchAdvisor getAdvisor() {
        return advisor;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbench.
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Returns the default perspective id.
     * 
     * @return the default perspective id
     */
    public String getDefaultPerspectiveId() {
        String id = getAdvisor().getInitialWindowPerspectiveId();
        // make sure we the advisor gave us one
        Assert.isNotNull(id);
        return id;
    }

    /**
     * Returns the default workbench window page input.
     * 
     * @return the default window page input or <code>null</code> if none
     */
    public IAdaptable getDefaultPageInput() {
        return getAdvisor().getDefaultPageInput();
    }

    /**
     * Returns the id of the preference page that should be presented most
     * prominently.
     * 
     * @return the id of the preference page, or <code>null</code> if none
     */
    public String getMainPreferencePageId() {
        String id = getAdvisor().getMainPreferencePageId();
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbench
     * @since 3.0
     */
    public IElementFactory getElementFactory(String factoryId) {
        Assert.isNotNull(factoryId);
        return WorkbenchPlugin.getDefault().getElementFactory(factoryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbench#getProgressService()
     */
    public IProgressService getProgressService() {
        return ProgressManager.getInstance();
    }

    private WorkbenchActivitySupport workbenchActivitySupport;

    private WorkbenchCommandSupport workbenchCommandSupport;

    private WorkbenchContextSupport workbenchContextSupport;

    public IWorkbenchActivitySupport getActivitySupport() {
        return workbenchActivitySupport;
    }

    public IWorkbenchCommandSupport getCommandSupport() {
        return workbenchCommandSupport;
    }

    public IWorkbenchContextSupport getContextSupport() {
        return workbenchContextSupport;
    }

    private final ICommandManagerListener commandManagerListener = new ICommandManagerListener() {

        public final void commandManagerChanged(
                final CommandManagerEvent commandManagerEvent) {
            updateActiveWorkbenchWindowMenuManager(false);
        }
    };

    private final IContextManagerListener contextManagerListener = new IContextManagerListener() {

        public final void contextManagerChanged(
                final ContextManagerEvent contextManagerEvent) {
            final Set enabledContextIds = workbenchContextSupport
                    .getContextManager().getEnabledContextIds();
            final Map enabledContextTree = workbenchContextSupport
                    .createFilteredContextTreeFor(enabledContextIds);
            workbenchCommandSupport.setActiveContextIds(enabledContextTree);
        }
    };

    private final IWindowListener windowListener = new IWindowListener() {

        public void windowActivated(IWorkbenchWindow window) {
            updateActiveWorkbenchWindowMenuManager(true);
        }

        public void windowClosed(IWorkbenchWindow window) {
            updateActiveWorkbenchWindowMenuManager(true);
        }

        public void windowDeactivated(IWorkbenchWindow window) {
            updateActiveWorkbenchWindowMenuManager(true);
        }

        public void windowOpened(IWorkbenchWindow window) {
            updateActiveWorkbenchWindowMenuManager(true);
        }
    };

    private void updateActiveWorkbenchWindowMenuManager(boolean textOnly) {
        final IWorkbenchWindow workbenchWindow = getActiveWorkbenchWindow();

        if (workbenchWindow instanceof WorkbenchWindow) {
            final WorkbenchWindow window = (WorkbenchWindow) workbenchWindow;
            if (window.isClosing()) {
                return;
            }

            final MenuManager menuManager = window.getMenuManager();

            if (textOnly)
                menuManager.update(IAction.TEXT);
            else
                menuManager.updateAll(true);
        }
    }

    private ActivityPersistanceHelper activityHelper;

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbench#getIntroManager()
     */
    public IIntroManager getIntroManager() {
        return introManager;
    }

    /** 
     * @return the workbench intro manager
     * @since 3.0
     */
    /*package*/WorkbenchIntroManager getWorkbenchIntroManager() {
        return introManager;
    }

    private WorkbenchIntroManager introManager = new WorkbenchIntroManager(this);

    /** 
     * @return the intro extension for this workbench.
     * 
     * @since 3.0
     */
    public IntroDescriptor getIntroDescriptor() {
        return introDescriptor;
    }

    /**
     * This method exists as a test hook.  This method should 
     * <strong>NEVER</strong> be called by clients.
     * 
     * @since 3.0
     */
    public void setIntroDescriptor(IntroDescriptor descriptor) {
        if (introManager.getIntro() != null) {
            introManager.closeIntro(introManager.getIntro());
        }
        introDescriptor = descriptor;
    }

    /**
     * The descriptor for the intro extension that is valid for this workspace, <code>null</code> if none.
     */
    private IntroDescriptor introDescriptor;

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbench#getThemeManager()
     */
    public IThemeManager getThemeManager() {
        return WorkbenchThemeManager.getInstance();
    }

    /**
     * Returns <code>true</code> if the workbench is running,
     * <code>false</code> if it has been terminated. 
     */
    public boolean isRunning() {
        return runEventLoop;
    }

    /**
     * Return the presentation ID specified by the preference or the default ID if undefined.
     * 
     * @return the presentation ID
     * @see IWorkbenchPreferenceConstants#PRESENTATION_FACTORY_ID
     */
    public String getPresentationId() {
        String factoryId = PrefUtil.getAPIPreferenceStore().getString(
                IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID);

        // Workaround for bug 58975 - New preference mechanism does not properly initialize defaults
        // Ensure that the UI plugin has started too.
        if (factoryId == null || factoryId.equals("")) { //$NON-NLS-1$
            factoryId = "org.eclipse.ui.presentations.default"; //$NON-NLS-1$
        }
        return factoryId;
    }

    /**
     * <p>
     * Indicates the start of a large update within the workbench. This is used
     * to disable CPU-intensive, change-sensitive services that were temporarily
     * disabled in the midst of large changes. This method should always be
     * called in tandem with <code>largeUpdateEnd</code>, and the event loop
     * should not be allowed to spin before that method is called.
     * </p>
     * <p>
     * Important: always use with <code>largeUpdateEnd</code>!
     * </p>
     */
    public final void largeUpdateStart() {
        if (largeUpdates++ == 0) {
            workbenchCommandSupport.setProcessing(false);
            workbenchContextSupport.setProcessing(false);
            final IWorkbenchWindow[] windows = getWorkbenchWindows();
            for (int i = 0; i < windows.length; i++) {
                IWorkbenchWindow window = windows[i];
                if (window instanceof WorkbenchWindow) {
                    ((WorkbenchWindow) window).largeUpdateStart();
                }
            }
        }
    }

    /**
     * <p>
     * Indicates the end of a large update within the workbench. This is used to
     * re-enable services that were temporarily disabled in the midst of large
     * changes. This method should always be called in tandem with
     * <code>largeUpdateStart</code>, and the event loop should not be
     * allowed to spin before this method is called.
     * </p>
     * <p>
     * Important: always protect this call by using <code>finally</code>!
     * </p>
     */
    public final void largeUpdateEnd() {
        if (--largeUpdates == 0) {
            workbenchCommandSupport.setProcessing(true);
            workbenchContextSupport.setProcessing(true);
            
            // Perform window-specific blocking.
            final IWorkbenchWindow[] windows = getWorkbenchWindows();
            for (int i = 0; i < windows.length; i++) {
                IWorkbenchWindow window = windows[i];
                if (window instanceof WorkbenchWindow) {
                    ((WorkbenchWindow) window).largeUpdateEnd();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4638.java