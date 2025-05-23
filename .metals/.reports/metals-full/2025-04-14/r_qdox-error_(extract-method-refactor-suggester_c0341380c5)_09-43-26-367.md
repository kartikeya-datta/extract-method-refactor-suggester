error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9249.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9249.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9249.java
text:
```scala
s@@cheduler.queueForDecoration(elements[i],adapted,true,null);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.decorators;

import java.lang.reflect.Method;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.internal.*;

/**
 * The DecoratorManager is the class that handles all of the
 * decorators defined in the image.
 * 
 * @since 2.0
 */
public class DecoratorManager
	implements ILabelDecorator, ILabelProviderListener, IDecoratorManager {

	private DecorationScheduler scheduler;

	private LightweightDecoratorManager lightweightManager;

	//Hold onto the list of listeners to be told if a change has occured
	private ListenerList listeners = new ListenerList();

	//The cachedDecorators are a 1-many mapping of type to full decorator.
	private HashMap cachedFullDecorators = new HashMap();
	//The full definitions read from the registry
	private FullDecoratorDefinition[] fullDefinitions;

	private static final FullDecoratorDefinition[] EMPTY_FULL_DEF =
		new FullDecoratorDefinition[0];

	private final String PREFERENCE_SEPARATOR = ","; //$NON-NLS-1$
	private final String VALUE_SEPARATOR = ":"; //$NON-NLS-1$
	private final String P_TRUE = "true"; //$NON-NLS-1$
	private final String P_FALSE = "false"; //$NON-NLS-1$

	/**
	 * Create a new instance of the receiver and load the
	 * settings from the installed plug-ins.
	 */
	public DecoratorManager() {
		DecoratorRegistryReader reader = new DecoratorRegistryReader();
		Collection values = reader.readRegistry(Platform.getPluginRegistry());

		ArrayList full = new ArrayList();
		ArrayList lightweight = new ArrayList();
		Iterator allDefinitions = values.iterator();
		while (allDefinitions.hasNext()) {
			DecoratorDefinition nextDefinition =
				(DecoratorDefinition) allDefinitions.next();
			if (nextDefinition.isFull())
				full.add(nextDefinition);
			else
				lightweight.add(nextDefinition);
		}

		fullDefinitions = new FullDecoratorDefinition[full.size()];
		full.toArray(fullDefinitions);

		LightweightDecoratorDefinition[] lightweightDefinitions =
			new LightweightDecoratorDefinition[lightweight.size()];
		lightweight.toArray(lightweightDefinitions);

		lightweightManager =
			new LightweightDecoratorManager(lightweightDefinitions);

		scheduler = new DecorationScheduler(this);
	}
	
	/**
	 * For dynamic UI
	 * 
	 * @param definition the definition to add
	 * @since 3.0
	 */
	public void addDecorator(DecoratorDefinition definition) {
		if (definition.isFull()) {
			if (getFullDecoratorDefinition(definition.getId()) == null) {
				FullDecoratorDefinition [] oldDefs = fullDefinitions;
				fullDefinitions = new FullDecoratorDefinition[fullDefinitions.length + 1];
				System.arraycopy(oldDefs, 0, fullDefinitions, 0, oldDefs.length);
				fullDefinitions[oldDefs.length] = (FullDecoratorDefinition) definition;
				clearCaches();
				updateForEnablementChange();
			}
		}
		else {
			if (getLightweightManager().addDecorator((LightweightDecoratorDefinition) definition)) {
				clearCaches();
				updateForEnablementChange();
			}
		}
	}

	/**
	 * See if the supplied decorator cache has a value for the
	 * element. If not calculate it from the enabledDefinitions and
	 * update the cache.
	 * @return Collection of DecoratorDefinition.
	 * @param element. The element being tested.
	 * @param cachedDecorators. The cache for decorator lookup.
	 * @param enabledDefinitions. The definitions currently defined for this decorator.
	 */

	static Collection getDecoratorsFor(
		Object element,
		DecoratorDefinition[] enabledDefinitions) {

		ArrayList decorators = new ArrayList();

		for (int i = 0; i < enabledDefinitions.length; i++) {
			if (enabledDefinitions[i]
				.getEnablement()
				.isEnabledForExpression(
					element,
					ActionExpression.EXP_TYPE_OBJECT_CLASS))
				decorators.add(enabledDefinitions[i]);
		}

		return decorators;

	}

	/**
	 * Restore the stored values from the preference
	 * store and register the receiver as a listener
	 * for all of the enabled decorators.
	 */

	public void restoreListeners() {
		applyDecoratorsPreference();
	}

	/**
	 * Add the listener to the list of listeners.
	 */
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove the listener from the list.
	 */
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Inform all of the listeners that require an update
	 */
	void fireListeners(final LabelProviderChangedEvent event) {
		Object [] array = listeners.getListeners();
		for (int i = 0; i < array.length; i ++) {
			final ILabelProviderListener l = (ILabelProviderListener)array[i];
			Platform.run(new SafeRunnable() {
				public void run() {
					l.labelProviderChanged(event);
				}
				public void handleException(Throwable e) {
					super.handleException(e);
					//If and unexpected exception happens, remove it
					//to make sure the workbench keeps running.
					removeListener(l);
				}
			});
		}		
	}

	/**
	 * Decorate the image provided for the element type.
	 * Then look for an IResource that adapts to it an apply
	 * all of the adaptable decorators.
	 * @return String or null if there are none defined for this type.
	 * @param Image
	 * @param Object
	 */
	public String decorateText(String text, Object element) {

		//Get any adaptions to IResource
		Object adapted = getResourceAdapter(element);
		String result = scheduler.decorateWithText(text, element, adapted);
		FullDecoratorDefinition[] decorators = getDecoratorsFor(element);
		for (int i = 0; i < decorators.length; i++) {
			if (decorators[i].getEnablement().isEnabledFor(element)) {
				String newResult = decorators[i].decorateText(result, element);
				if (newResult != null)
					result = newResult;
			}
		}

		if (adapted != null) {
			decorators = getDecoratorsFor(adapted);
			for (int i = 0; i < decorators.length; i++) {
				if (decorators[i].isAdaptable()
					&& decorators[i].getEnablement().isEnabledFor(adapted)) {
					String newResult =
						decorators[i].decorateText(result, adapted);
					if (newResult != null)
						result = newResult;
				}
			}
		}

		return result;
	}

	/**
	 * Decorate the image provided for the element type.
	 * Then look for an IResource that adapts to it an apply
	 * all of the adaptable decorators.
	 * @return Image or null if there are none defined for this type.
	 * @param Image
	 * @param Object
	 */
	public Image decorateImage(Image image, Object element) {

		Object adapted = getResourceAdapter(element);
		Image result = scheduler.decorateWithOverlays(image, element, adapted);
		FullDecoratorDefinition[] decorators = getDecoratorsFor(element);

		for (int i = 0; i < decorators.length; i++) {
			if (decorators[i].getEnablement().isEnabledFor(element)) {
				Image newResult = decorators[i].decorateImage(result, element);
				if (newResult != null)
					result = newResult;
			}
		}

		//Get any adaptions to IResource

		if (adapted != null) {
			decorators = getDecoratorsFor(adapted);
			for (int i = 0; i < decorators.length; i++) {
				if (decorators[i].isAdaptable()
					&& decorators[i].getEnablement().isEnabledFor(adapted)) {
					Image newResult =
						decorators[i].decorateImage(result, adapted);
					if (newResult != null)
						result = newResult;
				}
			}
		}

		return result;
	}

	/**
	 * Get the resource adapted object for the supplied
	 * element. Return null if there isn't one.
	 */
	private Object getResourceAdapter(Object element) {

		//Get any adaptions to IResource (when resources are available)
		if (element instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) element;
			Class contributorResourceAdapterClass = LegacyResourceSupport.getIContributorResourceAdapterClass();
			if (contributorResourceAdapterClass == null) {
				return null;
			}
			Object resourceAdapter = adaptable.getAdapter(contributorResourceAdapterClass);
			if (resourceAdapter == null)
				// reflective equivalent of
				//    resourceAdapter = DefaultContributorResourceAdapter.getDefault();
				try {
					Class c = LegacyResourceSupport.getDefaultContributorResourceAdapterClass();
					Method m = c.getDeclaredMethod("getDefault", new Class[0]); //$NON-NLS-1$
					resourceAdapter = m.invoke(null, new Object[0]);
				} catch (Exception e) {
					// shouldn't happen - but play it safe
					return null;
				}

			Object adapted;
			// reflective equivalent of
			//    adapted = ((IContributorResourceAdapter) resourceAdapter).getAdaptedResource(adaptable);
			try {
				Method m = contributorResourceAdapterClass.getDeclaredMethod("getAdaptedResource", new Class[] {IAdaptable.class});  //$NON-NLS-1$
				adapted = m.invoke(resourceAdapter, new Object[] {adaptable});
			} catch (Exception e) {
				// shouldn't happen - but play it safe
				return null;
			}
			if (adapted != element) {
				return adapted; //Avoid applying decorator twice
			}
		}
		return null;
	}

	/**
	* Return whether or not the decorator registered for element
	* has a label property called property name.
	*/
	public boolean isLabelProperty(Object element, String property) {
		return isLabelProperty(element, property, true);
	}

	/**
	* Return whether or not the decorator registered for element
	* has a label property called property name.
	* Check for an adapted resource if checkAdapted is true.
	*/
	public boolean isLabelProperty(
		Object element,
		String property,
		boolean checkAdapted) {
		boolean fullCheck =
			isLabelProperty(element, property, getDecoratorsFor(element));

		if (fullCheck)
			return fullCheck;

		boolean lightweightCheck =
			isLabelProperty(
				element,
				property,
				lightweightManager.getDecoratorsFor(element));

		if (lightweightCheck)
			return true;

		if (checkAdapted) {
			//Get any adaptions to IResource
			Object adapted = getResourceAdapter(element);
			if (adapted == null || adapted == element)
				return false;

			fullCheck =
				isLabelProperty(adapted, property, getDecoratorsFor(adapted));
			if (fullCheck)
				return fullCheck;

			return isLabelProperty(
				adapted,
				property,
				lightweightManager.getDecoratorsFor(adapted));
		}
		return false;
	}

	private boolean isLabelProperty(
		Object element,
		String property,
		DecoratorDefinition[] decorators) {
		for (int i = 0; i < decorators.length; i++) {
			if (decorators[i].getEnablement().isEnabledFor(element)
				&& decorators[i].isLabelProperty(element, property))
				return true;
		}

		return false;
	}

	/**
	 * Return the enabled full decorator definitions.
	 * @return FullDecoratorDefinition[]
	 */
	private FullDecoratorDefinition[] enabledFullDefinitions() {
		ArrayList result = new ArrayList();
		for (int i = 0; i < fullDefinitions.length; i++) {
			if (fullDefinitions[i].isEnabled())
				result.add(fullDefinitions[i]);
		}
		FullDecoratorDefinition[] returnArray =
			new FullDecoratorDefinition[result.size()];
		result.toArray(returnArray);
		return returnArray;
	}

	/*
	 * @see IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		//Do nothing as this is not viewer dependant
	}

	/**
	 * Clear the caches in the manager. This is required
	 * to avoid updates that may occur due to changes in 
	 * enablement.
	 */
	public void clearCaches() {
		cachedFullDecorators.clear();
		lightweightManager.reset();
	}
		
	/**
	 * Enablement had changed. Fire the listeners and write
	 * the preference.
	 */
	public void updateForEnablementChange() {
		//Clear any results that may be around as all labels have changed
		scheduler.clearResults();
		fireListeners(new LabelProviderChangedEvent(this));
		writeDecoratorsPreference();
	}

	/**
	 * Get the DecoratorDefinitions defined on the receiver.
	 */
	public DecoratorDefinition[] getAllDecoratorDefinitions() {
		LightweightDecoratorDefinition[] lightweightDefinitions =
			lightweightManager.getDefinitions();
		DecoratorDefinition[] returnValue =
			new DecoratorDefinition[fullDefinitions.length
				+ lightweightDefinitions.length];
		System.arraycopy(
			fullDefinitions,
			0,
			returnValue,
			0,
			fullDefinitions.length);
		System.arraycopy(
			lightweightDefinitions,
			0,
			returnValue,
			fullDefinitions.length,
			lightweightDefinitions.length);
		return returnValue;
	}

	/*
	 * @see ILabelProviderListener#labelProviderChanged(LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		Object[] elements = event.getElements();
		scheduler.clearResults();
		//If the elements are not specified send out a general update
		if(elements == null)
			fireListeners(event);
		else{
		  //Assume that someone is going to care about the 
		  //decoration result and just start it right away
		  for(int i = 0; i < elements.length; i ++){
			  Object adapted = getResourceAdapter(elements[i]);
			  //Force an update in case full decorators are the only ones enabled
			  scheduler.queueForDecoration(elements[i],adapted,true);
		  }
		}			
	}

	/**
	 * Store the currently enabled decorators in
	 * preference store.
	 */
	private void writeDecoratorsPreference() {
		StringBuffer enabledIds = new StringBuffer();
		writeDecoratorsPreference(enabledIds, fullDefinitions);
		writeDecoratorsPreference(
			enabledIds,
			lightweightManager.getDefinitions());

		WorkbenchPlugin.getDefault().getPreferenceStore().setValue(
			IPreferenceConstants.ENABLED_DECORATORS,
			enabledIds.toString());
	}

	private void writeDecoratorsPreference(
		StringBuffer enabledIds,
		DecoratorDefinition[] definitions) {
		for (int i = 0; i < definitions.length; i++) {
			enabledIds.append(definitions[i].getId());
			enabledIds.append(VALUE_SEPARATOR);
			if (definitions[i].isEnabled())
				enabledIds.append(P_TRUE);
			else
				enabledIds.append(P_FALSE);

			enabledIds.append(PREFERENCE_SEPARATOR);
		}
	}

	/**
	 * Get the currently enabled decorators in
	 * preference store and set the state of the
	 * current definitions accordingly.
	 */
	private void applyDecoratorsPreference() {

		String preferenceValue =
			WorkbenchPlugin.getDefault().getPreferenceStore().getString(
				IPreferenceConstants.ENABLED_DECORATORS);

		StringTokenizer tokenizer =
			new StringTokenizer(preferenceValue, PREFERENCE_SEPARATOR);
		Set enabledIds = new HashSet();
		Set disabledIds = new HashSet();
		while (tokenizer.hasMoreTokens()) {
			String nextValuePair = tokenizer.nextToken();

			//Strip out the true or false to get the id
			String id =
				nextValuePair.substring(
					0,
					nextValuePair.indexOf(VALUE_SEPARATOR));
			if (nextValuePair.endsWith(P_TRUE))
				enabledIds.add(id);
			else
				disabledIds.add(id);
		}

		for (int i = 0; i < fullDefinitions.length; i++) {
			String id = fullDefinitions[i].getId();
			if (enabledIds.contains(id))
				fullDefinitions[i].setEnabled(true);
			else {
				if (disabledIds.contains(id))
					fullDefinitions[i].setEnabled(false);
			}
		}

		LightweightDecoratorDefinition[] lightweightDefinitions =
			lightweightManager.getDefinitions();
		for (int i = 0; i < lightweightDefinitions.length; i++) {
			String id = lightweightDefinitions[i].getId();
			if (enabledIds.contains(id))
				lightweightDefinitions[i].setEnabled(true);
			else {
				if (disabledIds.contains(id))
					lightweightDefinitions[i].setEnabled(
						false);
			}
		}

	}

	/**
	 * Shutdown the decorator manager by disabling all
	 * of the decorators so that dispose() will be called
	 * on them.
	 */
	public void shutdown() {
		//Disable all of the enabled decorators 
		//so as to force a dispose of thier decorators
		for (int i = 0; i < fullDefinitions.length; i++) {
			if (fullDefinitions[i].isEnabled())
				fullDefinitions[i].setEnabled(false);
		}
		lightweightManager.shutdown();
		scheduler.shutdown();
	}
	/**
	 * @see IDecoratorManager#getEnabled(String)
	 */
	public boolean getEnabled(String decoratorId) {
		DecoratorDefinition definition = getDecoratorDefinition(decoratorId);
		if (definition == null)
			return false;
		else
			return definition.isEnabled();
	}

	/**
	 * @see IDecoratorManager#getLabelDecorator()
	 */
	public ILabelDecorator getLabelDecorator() {
		return this;
	}

	/**
	 * @see IDecoratorManager#setEnabled(String, boolean)
	 */
	public void setEnabled(String decoratorId, boolean enabled)
		throws CoreException {
		DecoratorDefinition definition = getDecoratorDefinition(decoratorId);
		if (definition != null)
			definition.setEnabled(enabled);
	}

	/*
	 * @see IDecoratorManager#getBaseLabelProvider(String)
 	 */
 	public IBaseLabelProvider getBaseLabelProvider(String decoratorId) {
		IBaseLabelProvider fullProvider = getLabelDecorator(decoratorId);
		if (fullProvider == null)
			return getLightweightLabelDecorator(decoratorId);
		else
			return fullProvider;
	}

	/*
	 * @see IDecoratorManager#getLabelDecorator(String)
	 */
	public ILabelDecorator getLabelDecorator(String decoratorId) {
		FullDecoratorDefinition definition =
			getFullDecoratorDefinition(decoratorId);

		//Do not return for a disabled decorator
		if (definition != null && definition.isEnabled()) {
			return definition.getDecorator();
		}
		return null;
	}

	/*
	 * @see IDecoratorManager#getLightweightLabelDecorator(String)
	 */
	public ILightweightLabelDecorator getLightweightLabelDecorator(String decoratorId) {
		LightweightDecoratorDefinition definition =
			lightweightManager.getDecoratorDefinition(decoratorId);
		//Do not return for a disabled decorator
		if (definition != null && definition.isEnabled()) {
			return definition.getDecorator();
		}
		return null;
	}

	/**
	 * Get the DecoratorDefinition with the supplied id
	 * @return DecoratorDefinition or <code>null</code> if it is not found
	 * @param decoratorId String
	 */
	private DecoratorDefinition getDecoratorDefinition(String decoratorId) {
		DecoratorDefinition returnValue =
			getFullDecoratorDefinition(decoratorId);
		if (returnValue == null)
			return lightweightManager.getDecoratorDefinition(decoratorId);
		else
			return returnValue;
	}

	/**
	 * Get the FullDecoratorDefinition with the supplied id
	 * @return FullDecoratorDefinition or <code>null</code> if it is not found
	 * @param decoratorId String
	 */
	private FullDecoratorDefinition getFullDecoratorDefinition(String decoratorId) {
		for (int i = 0; i < fullDefinitions.length; i++) {
			if (fullDefinitions[i].getId().equals(decoratorId))
				return fullDefinitions[i];
		}
		return null;
	}

	/**
	 * Get the full decorator definitions registered for elements of this type.
	 */
	private FullDecoratorDefinition[] getDecoratorsFor(Object element) {

		if (element == null)
			return EMPTY_FULL_DEF;

		String className = element.getClass().getName();
		FullDecoratorDefinition[] decoratorArray =
			(FullDecoratorDefinition[]) cachedFullDecorators.get(className);
		if (decoratorArray != null) {
			return decoratorArray;
		}

		Collection decorators =
			getDecoratorsFor(element, enabledFullDefinitions());

		if (decorators.size() == 0)
			decoratorArray = EMPTY_FULL_DEF;
		else {
			decoratorArray = new FullDecoratorDefinition[decorators.size()];
			decorators.toArray(decoratorArray);
		}

		cachedFullDecorators.put(className, decoratorArray);
		return decoratorArray;
	}

	/**
	 * Returns the lightweightManager.
	 * @return LightweightDecoratorManager
	 */
	LightweightDecoratorManager getLightweightManager() {
		return lightweightManager;
	}

	/**
	 * @see org.eclipse.ui.IDecoratorManager#update(java.lang.String)
	 */
	public void update(String decoratorId) {

		IBaseLabelProvider provider = getBaseLabelProvider(decoratorId);
		if(provider != null){
			scheduler.clearResults();
			fireListeners(new LabelProviderChangedEvent(provider));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9249.java