error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6842.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6842.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6842.java
text:
```scala
r@@eturn null;

/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jan-Hendrik Diederich, Bredex GmbH - bug 201052
 *******************************************************************************/

package org.eclipse.ui.internal.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.KeywordRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The WorkbenchPreferenceExtensionNode is the abstract class for all property
 * and page nodes in the workbench.
 * 
 * @since 3.1
 */
public abstract class WorkbenchPreferenceExtensionNode extends WorkbenchPreferenceExpressionNode {
	
	private static final String TAG_KEYWORD_REFERENCE = "keywordReference"; //$NON-NLS-1$

	private Collection keywordReferences;
	
	private IConfigurationElement configurationElement;

	private ImageDescriptor imageDescriptor;

	private Image image;

	private Collection keywordLabelCache;
	

	/**
	 * Create a new instance of the reciever.
	 * 
	 * @param id
	 * @param configurationElement 
	 */
	public WorkbenchPreferenceExtensionNode(String id, IConfigurationElement configurationElement) {
		super(id);
		this.configurationElement = configurationElement;
	}

	/**
	 * Get the ids of the keywords the receiver is bound to.
	 * 
	 * @return Collection of <code>String</code>.  Never <code>null</code>.
	 */
	public Collection getKeywordReferences() {
		if (keywordReferences == null) {
			IConfigurationElement[] references = getConfigurationElement()
					.getChildren(TAG_KEYWORD_REFERENCE);
			HashSet list = new HashSet(references.length);
			for (int i = 0; i < references.length; i++) {
				IConfigurationElement page = references[i];
				String id = page.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
				if (id != null) {
					list.add(id);
				}
			}

			if (!list.isEmpty()) {
				keywordReferences = list;
			} else {
				keywordReferences = Collections.EMPTY_SET;
			}
			
		}
		return keywordReferences;
	}

	/**
	 * Get the labels of all of the keywords of the receiver.
	 * 
	 * @return Collection of <code>String</code>.  Never <code>null</code>.
	 */
	public Collection getKeywordLabels() {
		if (keywordLabelCache != null) {
			return keywordLabelCache;
		}
		
		Collection refs = getKeywordReferences();
		
		if(refs == Collections.EMPTY_SET) {
			keywordLabelCache = Collections.EMPTY_SET; 
			return keywordLabelCache;
		}
		
		keywordLabelCache = new ArrayList(refs.size());
		Iterator referenceIterator = refs.iterator();
		while(referenceIterator.hasNext()){
			Object label = KeywordRegistry.getInstance().getKeywordLabel(
					(String) referenceIterator.next());
			if(label != null) {
				keywordLabelCache.add(label);
			}
		}
		
		return keywordLabelCache;
	}
	
	/**
	 * Clear the keyword cache, if any.
	 */
	public void clearKeywords() {
		keywordLabelCache = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferenceNode#disposeResources()
	 */
	public void disposeResources() {
        if (image != null) {
            image.dispose();
            image = null;
        }
        super.disposeResources();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferenceNode#getLabelImage()
	 */
	public Image getLabelImage() {		
        if (image == null) {
        	ImageDescriptor desc = getImageDescriptor();
        	if (desc != null) {
				image = imageDescriptor.createImage();
			}
        }
        return image;
    }


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferenceNode#getLabelText()
	 */
	public String getLabelText() {
		return getConfigurationElement().getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
	}

    /**
     * Returns the image descriptor for this node.
     * 
     * @return the image descriptor
     */
    public ImageDescriptor getImageDescriptor() {
    	if (imageDescriptor != null) {
			return imageDescriptor;
		}
    	
    	String imageName = getConfigurationElement().getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
		if (imageName != null) {
			String contributingPluginId = getConfigurationElement().getNamespace();
			imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(contributingPluginId, imageName);
		}
		return imageDescriptor;
    }
    
    /**
     * Return the configuration element.
     * 
     * @return the configuration element
     */
	public IConfigurationElement getConfigurationElement() {
		return configurationElement;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.activities.support.IPluginContribution#getLocalId()
	 */
	public String getLocalId() {
		return getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.activities.support.IPluginContribution#getPluginId()
	 */
	public String getPluginId() {
		return getConfigurationElement().getNamespace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6842.java