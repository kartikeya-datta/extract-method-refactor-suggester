error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5298.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5298.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5298.java
text:
```scala
i@@f (keySequence.isChildOf(mode, false))

package org.eclipse.ui.internal;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.actions.Util;
import org.eclipse.ui.internal.actions.keybindings.KeyMachine;
import org.eclipse.ui.internal.actions.keybindings.KeyManager;
import org.eclipse.ui.internal.actions.keybindings.KeySequence;
import org.eclipse.ui.internal.actions.keybindings.KeyStroke;
import org.eclipse.ui.internal.actions.keybindings.Match;
import org.eclipse.ui.internal.registry.IActionSet;

/**
 * @version 	2.0
 * @author
 */
public class WWinKeyBindingService {
	/* A number increased whenever the action mapping changes so
	 * its children can keep their mapping in sync with the ones in
	 * the parent.
	 */
	private long updateNumber = 0;
	/* Maps all global actions definition ids to the action */
	private HashMap globalActionDefIdToAction = new HashMap();
	/* Maps all action sets definition ids to the action */
	private HashMap actionSetDefIdToAction = new HashMap();
	/* A listener to property changes so the mappings can
	 * be updated whenever the active configuration changes.
	 */
	private IPropertyChangeListener propertyListener;
	/* The current KeyBindindService */
	private KeyBindingService activeService;
	/* The window this service is managing the accelerators for.*/
	private WorkbenchWindow window;

	private AcceleratorMenu accMenu;

	private VerifyListener verifyListener = new VerifyListener() {
		public void verifyText(VerifyEvent event) {
			event.doit = false;
			clear();
		}
	};

	private void setStatusLineMessage(KeySequence keySequence) {
		StringBuffer stringBuffer = new StringBuffer();
		
		if (keySequence != null) {
			Iterator iterator = keySequence.getKeyStrokes().iterator();
			int i = 0;
			
			while (iterator.hasNext()) {					
				if (i != 0)
					stringBuffer.append(' ');
	
				KeyStroke keyStroke = (KeyStroke) iterator.next();
				int accelerator = keyStroke.getAccelerator();
				stringBuffer.append(
					org.eclipse.jface.action.Action.convertAccelerator(
					accelerator));					
				i++;
			}		
		}
	
		window.getActionBars().getStatusLineManager().setMessage(stringBuffer.toString());
	}

	public void clear() {		
		KeyManager keyManager = KeyManager.getInstance();
		KeyMachine keyMachine = keyManager.getKeyMachine();
		
		/*if (*/keyMachine.setMode(KeySequence.create());//) {
			setStatusLineMessage(null);	
			updateAccelerators();
		//}
	}
	
	public void pressed(KeyStroke keyStroke, Event event) { 
		//System.out.println("pressed(" + keyStroke.getAccelerator() + ")");
		KeyManager keyManager = KeyManager.getInstance();
		KeyMachine keyMachine = keyManager.getKeyMachine();
		Map keySequenceMapForMode = keyMachine.getKeySequenceMapForMode();
		KeySequence mode = keyMachine.getMode();
		List keyStrokes = new ArrayList(mode.getKeyStrokes());
		keyStrokes.add(keyStroke);
		KeySequence keySequence = KeySequence.create(keyStrokes);
		SortedSet matchSet = (SortedSet) keySequenceMapForMode.get(keySequence);
		
		if (matchSet != null && matchSet.size() == 1) {
			clear();
			Match match = (Match) matchSet.iterator().next();				
			invoke(match.getBinding().getAction(), event);					
		} else {
			keyMachine.setMode(keySequence);
			setStatusLineMessage(keySequence);
			keySequenceMapForMode = keyMachine.getKeySequenceMapForMode();
			
			if (keySequenceMapForMode.isEmpty())
				clear();	
			else
				updateAccelerators();
		}
	}

	public void invoke(String action, Event event) {		
		//System.out.println("invoke(" + action + ")");
		if (activeService != null) {
			IAction a = activeService.getAction(action);
			
			if (a != null && a.isEnabled())
				a.runWithEvent(event);
		}
	}

	/**
	 * Create an instance of WWinKeyBindingService and initializes it.
	 */			
	public WWinKeyBindingService(final WorkbenchWindow window) {
		this.window = window;
		IWorkbenchPage[] pages = window.getPages();
		final IPartListener partListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part) {
				update(part,false);
			}
			public void partBroughtToTop(IWorkbenchPart part) {}
			public void partClosed(IWorkbenchPart part) {}
			public void partDeactivated(IWorkbenchPart part) {
				clear();
			}
			public void partOpened(IWorkbenchPart part) {}
		};
		final ShellListener shellListener = new ShellAdapter() {
			public void shellDeactivated(ShellEvent e) {
				clear();
			}
		};		
		for(int i=0; i<pages.length;i++) {
			pages[i].addPartListener(partListener);
		}
		window.addPageListener(new IPageListener() {
			public void pageActivated(IWorkbenchPage page){}
			public void pageClosed(IWorkbenchPage page){}
			public void pageOpened(IWorkbenchPage page){
				page.addPartListener(partListener);
				partListener.partActivated(page.getActivePart());
				window.getShell().removeShellListener(shellListener);				
				window.getShell().addShellListener(shellListener);				
			}
		});
		propertyListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(IWorkbenchConstants.ACCELERATOR_CONFIGURATION_ID)) {
					IWorkbenchPage page = window.getActivePage();
					if(page != null) {
						IWorkbenchPart part = page.getActivePart();
						if(part != null) {
							update(part,true);
							return;
						}
					}
					MenuManager menuManager = window.getMenuManager();
					menuManager.updateAll(true);
				}
			}
		};
		IPreferenceStore store = WorkbenchPlugin.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(propertyListener);
	}
	/** 
	 * Remove the propety change listener when the windows is disposed.
	 */
	public void dispose() {
		IPreferenceStore store = WorkbenchPlugin.getDefault().getPreferenceStore();
		store.removePropertyChangeListener(propertyListener);
	}
	/**
	 * Register a global action in this service
	 */	
	public void registerGlobalAction(IAction action) {
		updateNumber++;
		globalActionDefIdToAction.put(action.getActionDefinitionId(),action);
	}
	/**
	 * Register all action from the specifed action set.
	 */	
	public void registerActionSets(IActionSet sets[]) {
		updateNumber++;
		actionSetDefIdToAction.clear();
		
		for (int i=0; i<sets.length; i++) {
			if (sets[i] instanceof PluginActionSet) {
				PluginActionSet set = (PluginActionSet)sets[i];
				IAction actions[] = set.getPluginActions();
				
				for (int j = 0; j < actions.length; j++) {
					Action action = (Action)actions[j];
					String defId = action.getActionDefinitionId();
					
					if (defId != null) {
						actionSetDefIdToAction.put(action.getActionDefinitionId(),action);
					}
				}
			}
		}
	}

	/**
	 * Return the update number used to keep children and parent in sync.
	 */
	public long getUpdateNumber() {
		return updateNumber;
	}
	/**
	 * Returns a Map with all action registered in this service.
	 */
	public HashMap getMapping() {
		HashMap result = (HashMap)globalActionDefIdToAction.clone();
		result.putAll(actionSetDefIdToAction);
		return result;
	}
	/**
	 * Returns the workbench window.
	 */
	public IWorkbenchWindow getWindow() {
		return window;	
	}
	/**
	 * Remove or restore the accelerators in the menus.
	 */
   	public void update(IWorkbenchPart part, boolean force) {
   		if (part == null)
   			return;
   		
		String[] oldScopeIds = new String[0];
   		
   		if (activeService != null)
   			oldScopeIds = activeService.getScopeIds();
   			
    	activeService = (KeyBindingService) part.getSite().getKeyBindingService();
		clear();

   		String[] newScopeIds = new String[0];
  		
   		if (activeService != null)
   			newScopeIds = activeService.getScopeIds();

    	if (force || Util.compare(oldScopeIds, newScopeIds) != 0) {
			KeyManager keyManager = KeyManager.getInstance();
			KeyMachine keyMachine = keyManager.getKeyMachine();
	    	
	    	// TBD: remove this later
	    	if (newScopeIds == null || newScopeIds.length == 0)
	    		newScopeIds = new String[] { "org.eclipse.ui.globalScope" };
	    	
	    	try {
	    		keyMachine.setScopes(newScopeIds);
	    	} catch (IllegalArgumentException eIllegalArgument) {
	    		System.err.println(eIllegalArgument);
	    	}
	    			    	   	
	    	WorkbenchWindow w = (WorkbenchWindow) getWindow();
   	 		MenuManager menuManager = w.getMenuManager();
 			menuManager.update(IAction.TEXT);
    	}
    }
    /**
     * Returns the definition id for <code>accelerator</code>
     */
    public String getDefinitionId(int accelerator) {
    	if (activeService == null) 
    		return null;

		KeyManager keyManager = KeyManager.getInstance();
		KeyMachine keyMachine = keyManager.getKeyMachine();        
		KeySequence mode = keyMachine.getMode();
		List keyStrokes = new ArrayList(mode.getKeyStrokes());
		keyStrokes.add(KeyStroke.create(accelerator));
    	KeySequence keySequence = KeySequence.create(keyStrokes);    		
		Map keySequenceMapForMode = keyMachine.getKeySequenceMapForMode();
		SortedSet matchSet = (SortedSet) keySequenceMapForMode.get(keySequence);

		if (matchSet != null && matchSet.size() == 1) {
			Match match = (Match) matchSet.iterator().next();				
			return match.getBinding().getAction();
		}
		
		return null;
    }

	/**
	 * Update the KeyBindingMenu with the current set of accelerators.
	 */
	public void updateAccelerators() {
		KeyManager keyManager = KeyManager.getInstance();
		KeyMachine keyMachine = keyManager.getKeyMachine();      		
		KeySequence mode = keyMachine.getMode();
		List keyStrokes = mode.getKeyStrokes();
		int size = keyStrokes.size();
		
		Map keySequenceMapForMode = keyMachine.getKeySequenceMapForMode();
		SortedSet keyStrokeSetForMode = new TreeSet();
		Iterator iterator = keySequenceMapForMode.keySet().iterator();

		while (iterator.hasNext()) {
			KeySequence keySequence = (KeySequence) iterator.next();
			
			if (keySequence.isChildOf(mode))
				keyStrokeSetForMode.add(keySequence.getKeyStrokes().get(size));	
		}

	   	iterator = keyStrokeSetForMode.iterator();
	   	int[] accelerators = new int[keyStrokeSetForMode.size()];
		int i = 0;
			   	
	   	while (iterator.hasNext()) {
	   		KeyStroke keyStroke = (KeyStroke) iterator.next();
	   		accelerators[i++] = keyStroke.getAccelerator();	   		
	   	}

		if (accMenu == null || accMenu.isDisposed()) {		
			Menu parent = window.getShell().getMenuBar();
			if (parent == null || parent.getItemCount() < 1)
				return;
			MenuItem parentItem = parent.getItem(parent.getItemCount() - 1);
			parent = parentItem.getMenu();
			accMenu = new AcceleratorMenu(parent);
		}
		
		if (accMenu == null)
			return;
		
		accMenu.setAccelerators(accelerators);		
		accMenu.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent selectionEvent) {
				Event event = new Event();
				event.item = selectionEvent.item;
				event.detail = selectionEvent.detail;
				event.x = selectionEvent.x;
				event.y = selectionEvent.y;
				event.width = selectionEvent.width;
				event.height = selectionEvent.height;
				event.stateMask = selectionEvent.stateMask;
				event.doit = selectionEvent.doit;
				event.data = selectionEvent.data;
				event.display = selectionEvent.display;
				event.time = selectionEvent.time;
				event.widget = selectionEvent.widget;
				pressed(KeyStroke.create(selectionEvent.detail), event);
			}
		});

		if (mode.getKeyStrokes().size() == 0)
			accMenu.removeVerifyListener(verifyListener);
		else
			accMenu.addVerifyListener(verifyListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5298.java