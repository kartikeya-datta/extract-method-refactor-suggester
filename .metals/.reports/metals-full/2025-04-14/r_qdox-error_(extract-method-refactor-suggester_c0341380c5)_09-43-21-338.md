error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7692.java
text:
```scala
W@@orkbench.getInstance().getConfigurationElementTracker().registerObject(element, desc, IConfigurationElementTracker.REF_WEAK);

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
package org.eclipse.ui.internal.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.ClosePerspectiveAction;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.experimental.IConfigurationElementAdditionHandler;
import org.eclipse.ui.internal.registry.experimental.IConfigurationElementRemovalHandler;
import org.eclipse.ui.internal.registry.experimental.IConfigurationElementTracker;
import org.eclipse.ui.internal.util.PrefUtil;

/**
 * Perspective registry.
 */
public class PerspectiveRegistry implements IPerspectiveRegistry, IConfigurationElementRemovalHandler, IConfigurationElementAdditionHandler {
    private String defaultPerspID;

    private static final String EXT = "_persp.xml"; //$NON-NLS-1$

    private static final String ID_DEF_PERSP = "PerspectiveRegistry.DEFAULT_PERSP"; //$NON-NLS-1$

    private static final String PERSP = "_persp"; //$NON-NLS-1$

    private static final char SPACE_DELIMITER = ' '; //$NON-NLS-1$

    private List perspectives = new ArrayList(10);
    
    //keep track of the perspectives the user has selected to remove or revert
    private ArrayList perspToRemove = new ArrayList(5);

    /**
     * Construct a new registry.
     */
    public PerspectiveRegistry() {
    	Workbench.getInstance().getConfigurationElementTracker().registerRemovalHandler(this);
    	Workbench.getInstance().getConfigurationElementTracker().registerAdditionHandler(this);

    	IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                /* To ensure the that no custom perspective definitions are deleted
                 * when preferences are imported, merge old and new values */
                if (event.getProperty().endsWith(PERSP)) {
                    /* A Perspective is being changed, merge */
                    mergePerspectives(event);
                } else if (event.getProperty().equals(
                        IPreferenceConstants.PERSPECTIVES)) {
                    /* The list of perpsectives is being changed, merge */
                    updatePreferenceList((IPreferenceStore) event.getSource());
                }
            }

            private void mergePerspectives(PropertyChangeEvent event) {
                IPreferenceStore store = (IPreferenceStore) event.getSource();
                if (event.getNewValue() == null) {
                    /* Perpsective is being removed; if the user has deleted or reverted 
                     * a custom perspective, let the change pass through.
                     * Otherwise, restore the custom perspective entry */

                    // Find the matching descriptor in the registry
                    IPerspectiveDescriptor[] perspectiveList = getPerspectives();
                    for (int i = 0; i < perspectiveList.length; i++) {
                        String id = perspectiveList[i].getId();
                        if (event.getProperty().startsWith(id)) { //found descriptor
                            //see if the perspective has been flagged for reverting or deleting
                            if (!perspToRemove.contains(id)) { //restore
                                store.setValue(id + PERSP, (String) event
                                        .getOldValue());
                            } else { //remove element from the list
                                perspToRemove.remove(id);
                            }
                        }
                    }
                } else if ((event.getOldValue() == null || event.getOldValue()
                        .equals(""))) { //$NON-NLS-1$

                    /* New perspective is being added, update the perspectiveRegistry to 
                     * contain the new custom perspective */

                    String id = event.getProperty().substring(0,
                            event.getProperty().lastIndexOf(PERSP));
                    if (findPerspectiveWithId(id) == null) {
                        //perspective does not already exist in registry, add it
                        PerspectiveDescriptor desc = new PerspectiveDescriptor(
                                null, null, null);
                        StringReader reader = new StringReader((String) event
                                .getNewValue());
                        try {
                            XMLMemento memento = XMLMemento
                                    .createReadRoot(reader);
                            desc.restoreState(memento);
                            addPerspective(desc);
                        } catch (WorkbenchException e) {
                            unableToLoadPerspective(e.getStatus());
                        }
                    }
                }
                /* If necessary, add to the list of perspectives */
                updatePreferenceList(store);
            }

            /* Update the list of perspectives from the registry.  This will be called 
             * for each perspective during an import preferences, but is necessary
             * to ensure the perspectives list stays consistent with the registry */
            private void updatePreferenceList(IPreferenceStore store) {
                IPerspectiveDescriptor[] perspectiveList = getPerspectives();
                StringBuffer perspBuffer = new StringBuffer();
                for (int i = 0; i < perspectiveList.length; i++) {
                    PerspectiveDescriptor desc = (PerspectiveDescriptor) perspectiveList[i];
                    if (hasCustomDefinition(desc)) {
                        perspBuffer.append(desc.getId())
                                .append(SPACE_DELIMITER);
                    }
                }
                String newList = perspBuffer.toString().trim();
                store.setValue(IPreferenceConstants.PERSPECTIVES, newList);
            }
        });

    }

    /**
     * Adds a perspective.  This is typically used by the reader.
     * 
     * @param desc
     */
    public void addPerspective(PerspectiveDescriptor desc) {
        if (desc == null)
            return;
        add(desc);
    }

    /**
	 * @param desc
	 */
	private void add(PerspectiveDescriptor desc) {
		perspectives.add(desc);
		IConfigurationElement element = desc.getConfigElement();
		if (element != null) {
			Workbench.getInstance().getConfigurationElementTracker().registerObject(element, desc);
		}
	}

	/**
	 * Create a new perspective.
	 * 
	 * @param label
	 *            the name of the new descriptor
	 * @param originalDescriptor
	 *            the descriptor on which to base the new descriptor
	 * @return a new perspective descriptor or <code>null</code> if the
	 *         creation failed.
	 */
    public PerspectiveDescriptor createPerspective(String label,
            PerspectiveDescriptor originalDescriptor) {
        // Sanity check to avoid invalid or duplicate labels.
        if (!validateLabel(label))
            return null;
        if (findPerspectiveWithLabel(label) != null)
            return null;

        // Calculate ID.
        String id = label.replace(' ', '_');
        id = id.trim();

        // Create descriptor.
        PerspectiveDescriptor desc = new PerspectiveDescriptor(id, label,
                originalDescriptor);
        add(desc);
        return desc;
    }

    /**
     * Reverts a list of perspectives back to the plugin definition
     * @param perspToRevert
     */
    public void revertPerspectives(ArrayList perspToRevert) {
        //indicate that the user is removing these perspectives	
        for (int i = 0; i < perspToRevert.size(); i++) {
            PerspectiveDescriptor desc = (PerspectiveDescriptor) perspToRevert
                    .get(i);
            perspToRemove.add(desc.getId());
            desc.revertToPredefined();
        }
    }

    /**
     * Deletes a list of perspectives
     * @param perspToDelete
     */
    public void deletePerspectives(ArrayList perspToDelete) {
        for (int i = 0; i < perspToDelete.size(); i++)
            deletePerspective((IPerspectiveDescriptor) perspToDelete.get(i));
    }

    /**
     * Delete a perspective.
     * Has no effect if the perspective is defined in an extension.
     * 
     * @param in
     */
    public void deletePerspective(IPerspectiveDescriptor in) {
        PerspectiveDescriptor desc = (PerspectiveDescriptor) in;
        // Don't delete predefined perspectives
        if (!desc.isPredefined()) {
            perspToRemove.add(desc.getId());
            perspectives.remove(desc);
            desc.deleteCustomDefinition();
            verifyDefaultPerspective();
        }
    }
    
    /**
     * Delete a perspective.  This will remove perspectives defined in extensions.
     * 
     * @param desc the perspective to delete
     * @since 3.1
     */
    private void internalDeletePerspective(PerspectiveDescriptor desc) {
        perspToRemove.add(desc.getId());
        perspectives.remove(desc);
        desc.deleteCustomDefinition();
        verifyDefaultPerspective();
    }

	/**
     * Removes the custom definition of a perspective from the preference store
     * @param desc
     */
    /* package */
    void deleteCustomDefinition(PerspectiveDescriptor desc) {
        //remove the entry from the preference store.
        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();

        /* To delete the perspective definition from the preference store, use the 
         * setToDefault method.  Since no default is defined, this will remove the
         * entry */
        store.setToDefault(desc.getId() + PERSP);

    }

    /**
     * Method hasCustomDefinition.
     * @param desc
     */
    /* package */
    boolean hasCustomDefinition(PerspectiveDescriptor desc) {
        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        return store.contains(desc.getId() + PERSP);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveRegistry#findPerspectiveWithId(java.lang.String)
     */
    public IPerspectiveDescriptor findPerspectiveWithId(String id) {
    	for (Iterator i = perspectives.iterator(); i.hasNext();) {
			PerspectiveDescriptor desc = (PerspectiveDescriptor) i.next();
			if (desc.getId().equals(id))
				return desc;
		}

        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveRegistry#findPerspectiveWithLabel(java.lang.String)
     */
    public IPerspectiveDescriptor findPerspectiveWithLabel(String label) {
    	for (Iterator i = perspectives.iterator(); i.hasNext();) {
			PerspectiveDescriptor desc = (PerspectiveDescriptor) i.next();
			if (desc.getLabel().equals(label)) 
				return desc;
		}
        return null;
    }

    /**
     * @see IPerspectiveRegistry#getDefaultPerspective()
     */
    public String getDefaultPerspective() {
        return defaultPerspID;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveRegistry#getPerspectives()
     */
    public IPerspectiveDescriptor[] getPerspectives() {
    	return (IPerspectiveDescriptor[]) perspectives.toArray(new IPerspectiveDescriptor[perspectives.size()]);
    }

    /**
     * Loads the registry.
     */
    public void load() {
        // Load the registries.  
        loadPredefined();
        loadCustom();

        // Get default perspective.
        // Get it from the R1.0 dialog settings first. Fixes bug 17039 
        IDialogSettings dialogSettings = WorkbenchPlugin.getDefault()
                .getDialogSettings();
        String str = dialogSettings.get(ID_DEF_PERSP);
        if (str != null && str.length() > 0) {
            setDefaultPerspective(str);
            dialogSettings.put(ID_DEF_PERSP, ""); //$NON-NLS-1$
        }
        verifyDefaultPerspective();
    }

    /**
     * Read children from the file system.
     */
    private void loadCustom() {
        Reader reader = null;

        /* Get the entries from the Preference store */
        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();

        /* Get the space-delimited list of custom perspective ids */
        String customPerspectives = store
                .getString(IPreferenceConstants.PERSPECTIVES);
        String[] perspectivesList = StringConverter.asArray(customPerspectives);

        for (int i = 0; i < perspectivesList.length; i++) {
            try {
                String xmlString = store.getString(perspectivesList[i] + PERSP);
                if (xmlString != null && xmlString.length() != 0)
                    reader = new StringReader(xmlString);

                // Restore the layout state.
                XMLMemento memento = XMLMemento.createReadRoot(reader);
                PerspectiveDescriptor newPersp = new PerspectiveDescriptor(
                        null, null, null);
                newPersp.restoreState(memento);
                String id = newPersp.getId();
                IPerspectiveDescriptor oldPersp = findPerspectiveWithId(id);
                if (oldPersp == null)
                    add(newPersp);
                reader.close();
            } catch (IOException e) {
                unableToLoadPerspective(null);
            } catch (WorkbenchException e) {
                unableToLoadPerspective(e.getStatus());
            }
        }

        // bug 69387: don't look for state location if there is no instance
        // area, i.e., -data @none
        if (Platform.getInstanceLocation() == null)
            return;

        /* Get the entries from files, if any */
        IPath path = WorkbenchPlugin.getDefault().getStateLocation();
        File folder = path.toFile();

        if (folder.isDirectory()) {
            File[] fileList = folder.listFiles();
            int nSize = fileList.length;
            for (int nX = 0; nX < nSize; nX++) {
                File file = fileList[nX];
                if (file.getName().endsWith(EXT)) {
                    //get the memento
                    InputStream stream = null;
                    try {
                        stream = new FileInputStream(file);
                        reader = new BufferedReader(new InputStreamReader(
                                stream, "utf-8")); //$NON-NLS-1$

                        // Restore the layout state.
                        XMLMemento memento = XMLMemento.createReadRoot(reader);
                        PerspectiveDescriptor newPersp = new PerspectiveDescriptor(
                                null, null, null);
                        newPersp.restoreState(memento);
                        IPerspectiveDescriptor oldPersp = findPerspectiveWithId(newPersp
                                .getId());
                        if (oldPersp == null)
                            add(newPersp);

                        //save to the preference store
                        saveCustomPersp(newPersp, memento);

                        //delete the file
                        file.delete();

                        reader.close();
                        stream.close();
                    } catch (IOException e) {
                        unableToLoadPerspective(null);
                    } catch (WorkbenchException e) {
                        unableToLoadPerspective(e.getStatus());
                    }
                }
            }
        }
    }

    /**
     * @param status
     */
    private void unableToLoadPerspective(IStatus status) {
        String title = WorkbenchMessages
                .getString("Perspective.problemLoadingTitle"); //$NON-NLS-1$
        String msg = WorkbenchMessages
                .getString("Perspective.errorLoadingState"); //$NON-NLS-1$
        if (status == null) {
            MessageDialog.openError((Shell) null, title, msg);
        } else {
            ErrorDialog.openError((Shell) null, title, msg, status);
        }
    }

    /**
     * Saves a custom perspective definition to the preference store.
     * 
     * @param desc the perspective 
     * @param memento the memento to save to
     * @throws IOException
     */
    public void saveCustomPersp(PerspectiveDescriptor desc,
            XMLMemento memento) throws IOException {

        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();

        // Save it to the preference store.
        Writer writer = new StringWriter();

        memento.save(writer);
        writer.close();
        store.setValue(desc.getId() + PERSP, writer.toString());

    }

    /**
     * Gets the Custom perspective definition from the preference store.
     * 
     * @param id the id of the perspective to find
     * @return IMemento a memento containing the perspective description
     * 
     * @throws WorkbenchException
     * @throws IOException
     */
    public IMemento getCustomPersp(String id) throws WorkbenchException,
            IOException {
        Reader reader = null;

        IPreferenceStore store = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        String xmlString = store.getString(id + PERSP);
        if (xmlString != null && xmlString.length() != 0) { //defined in store
            reader = new StringReader(xmlString);
        }
        XMLMemento memento = XMLMemento.createReadRoot(reader);
        reader.close();
        return memento;
    }

    /**
     * Read children from the plugin registry.
     */
    private void loadPredefined() {
        PerspectiveRegistryReader reader = new PerspectiveRegistryReader(this);
        reader.readPerspectives(Platform.getExtensionRegistry());
    }

    /**
     * @see IPerspectiveRegistry#setDefaultPerspective(String)
     */
    public void setDefaultPerspective(String id) {
        IPerspectiveDescriptor desc = findPerspectiveWithId(id);
        if (desc != null) {
            defaultPerspID = id;
            PrefUtil.getAPIPreferenceStore().setValue(
                    IWorkbenchPreferenceConstants.DEFAULT_PERSPECTIVE_ID, id);
        }
    }

    /**
     * Return <code>true</code> if a label is valid.
     * This checks only the given label in isolation.  It does not
     * check whether the given label is used by any
     * existing perspectives.
     * 
     * @param label the label to test
     * @return whether the label is valid
     */
    public boolean validateLabel(String label) {
        label = label.trim();
        if (label.length() <= 0)
            return false;
        return true;
    }

    /**
     * Verifies the id of the default perspective.  If the
     * default perspective is invalid use the workbench default.
     */
    private void verifyDefaultPerspective() {
        // Step 1: Try current defPerspId value.
        IPerspectiveDescriptor desc = null;
        if (defaultPerspID != null)
            desc = findPerspectiveWithId(defaultPerspID);
        if (desc != null)
            return;

        // Step 2. Read default value.
        String str = PrefUtil.getAPIPreferenceStore().getString(
                IWorkbenchPreferenceConstants.DEFAULT_PERSPECTIVE_ID);
        if (str != null && str.length() > 0)
            desc = findPerspectiveWithId(str);
        if (desc != null) {
        	defaultPerspID = str;
            return;
        }

        // Step 3. Use application-specific default
        defaultPerspID = Workbench.getInstance().getDefaultPerspectiveId();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveRegistry#clonePerspective(java.lang.String, java.lang.String, org.eclipse.ui.IPerspectiveDescriptor)
     */
    public IPerspectiveDescriptor clonePerspective(String id, String label,
            IPerspectiveDescriptor originalDescriptor) {

        // Check for invalid labels
        if (label == null || !(label.trim().length() > 0))
            throw new IllegalArgumentException();

        // Check for duplicates
        IPerspectiveDescriptor desc = findPerspectiveWithId(id);
        if (desc != null)
            throw new IllegalArgumentException();

        // Create descriptor.
        desc = new PerspectiveDescriptor(id, label,
                (PerspectiveDescriptor) originalDescriptor);
        add((PerspectiveDescriptor) desc);
        return desc;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveRegistry#revertPerspective(org.eclipse.ui.IPerspectiveDescriptor)
     */
    public void revertPerspective(IPerspectiveDescriptor perspToRevert) {
        PerspectiveDescriptor desc = (PerspectiveDescriptor) perspToRevert;
        perspToRemove.add(desc.getId());
        desc.revertToPredefined();
    }

    /**
     * 
     */
    public void dispose() {
    	Workbench.getInstance().getConfigurationElementTracker().unregisterRemovalHandler(this);
    	Workbench.getInstance().getConfigurationElementTracker().unregisterAdditionHandler(this);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.experimental.IConfigurationElementRemovalHandler#removeInstance(org.eclipse.core.runtime.IConfigurationElement, java.lang.Object)
	 */
	public void removeInstance(IConfigurationElement source, Object object) {
		if (object instanceof PerspectiveDescriptor) {
	        // close the perspective in all windows
	        IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
	                .getWorkbenchWindows();
	        PerspectiveDescriptor desc = (PerspectiveDescriptor) object;
			for (int w = 0; w < windows.length; ++w) {
	            IWorkbenchWindow window = windows[w];
	            IWorkbenchPage[] pages = window.getPages();
	            for (int p = 0; p < pages.length; ++p) {
	                WorkbenchPage page = (WorkbenchPage) pages[p];
	                ClosePerspectiveAction.closePerspective(page, page
	                        .findPerspective(desc));
	            }
	        }
			
			//((Workbench)PlatformUI.getWorkbench()).getPerspectiveHistory().removeItem(desc);
			
			internalDeletePerspective(desc);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.experimental.IConfigurationElementAdditionHandler#addInstance(org.eclipse.ui.internal.registry.experimental.IConfigurationElementTracker, org.eclipse.core.runtime.IConfigurationElement)
	 */
	public void addInstance(IConfigurationElementTracker tracker, IConfigurationElement element) {
		PerspectiveRegistryReader reader = new PerspectiveRegistryReader(this);
		reader.readElement(element);		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7692.java