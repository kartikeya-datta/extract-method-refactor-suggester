error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10848.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10848.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10848.java
text:
```scala
i@@f (!ClasspathUriResolver.isClasspathUri(iconNormalizedURI) && checkAccessable(iconNormalizedURI))

package org.eclipse.emf.editor.provider;

/**
 * <copyright> 
 *
 * Copyright (c) 2008 itemis AG and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   itemis AG - Initial API and implementation
 *
 * </copyright>
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.editor.EEPlugin;
import org.eclipse.emf.editor.extxpt.ExtXptFacade;
import org.eclipse.emf.editor.ui.ImageRegistry;

/**
 * @author Dennis Huebner
 * 
 */
public class ExtendedLabelProvider implements IItemLabelProvider {

	private static final String ICONS_FOLDER = "icons";
	private static final String ICON_EXTENSION_NAME = "icon";
	private static final String LABEL_EXTENSION_NAME = "label";
	private final ExtXptFacade facade;
	private IItemLabelProvider registryItemLabelProvider;

	public ExtendedLabelProvider(ExtXptFacade facade) {
		this.facade = facade;
		this.registryItemLabelProvider = new RegistryItemLabelProvider();
	}

	public Object getImage(Object element) {
		Object retVal = null;
		try {
			if (element instanceof EObject) {
				EObject eObject = (EObject) element;
				String iconName = evaluate(eObject, ICON_EXTENSION_NAME);
				if (iconName != null) {
					// TODO try instance scope
					retVal = locateImage(iconName, eObject.eResource(), eObject);
					// if not found try metamodel scope
					Resource eResource = eObject.eClass().eResource();
					if (retVal == null)
						retVal = locateImage(iconName, eResource, eObject);
				}
			}
		}
		catch (Throwable ex) {
			EEPlugin.logError("ERROR fetching Icon", ex);
		}
		if (retVal == null) {
			// Fallback: Ask registry for image
			retVal = registryItemLabelProvider.getImage(element);
		}
		return retVal;

	}

	/**
	 * @param iconName
	 * @param eResource
	 * @param eObject
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private Object locateImage(String iconName, Resource eResource, EObject eObject) throws MalformedURLException,
			IOException {
		// TODO understand the requirements :)
		Object retVal = null;
		if (eResource != null) {
			URI metaModelURI = eResource.getURI();
			URI iconURI = createIconURI(eObject, iconName, metaModelURI);

			if (metaModelURI.isPlatform()) {
				// platform resource... good
				String platformString = metaModelURI.toPlatformString(true);

				IFile metaModelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
				if (metaModelFile != null) {
					// using IFile to allow resource change listening
					IFile f = metaModelFile.getProject().getFile(iconURI.path());
					if (f != null && f.exists()) {
						// ask registry
						retVal = ImageRegistry.getDefault().getImage(f);
					}
				}
			}
			else if (metaModelURI.isArchive()) {// archived
				// handle archived resources
				// return URI. Image will be stored in
				// ExtendedImageRegistry
				// which is not refreshable
				if (checkAccessable(iconURI))
					retVal = iconURI;
			}
			else {
				// eResource not present physically... bad
				URI classpathURI = URI.createURI(ClasspathUriResolver.CLASSPATH_SCHEME + ":" + iconURI.path());
				URI iconNormalizedURI = new ClasspathUriResolver().resolve(facade.getProject(), classpathURI);
				// return URI. Image will be stored in
				// ExtendedImageRegistry
				// which is not refreshable
				if (!ClasspathUriResolver.isClassapthUri(iconNormalizedURI) && checkAccessable(iconNormalizedURI))
					retVal = iconNormalizedURI;
			}
		}
		return retVal;
	}

	private boolean checkAccessable(URI iconURI) throws IOException {
		URL url = new URL(iconURI.toString());
		if (url != null) {
			try {
				InputStream in = url.openStream();// check readable
				in.close();
				return true;
			}
			catch (FileNotFoundException e) {
				// ignore no feedback to user
			}
		}
		return false;
	}

	/**
	 * @param element
	 * @param iconName
	 * @param metaModelURI
	 * @return
	 */
	private URI createIconURI(EObject eObject, String iconName, URI metaModelURI) {
		String packageName = packageName(eObject);
		return metaModelURI.trimSegments(metaModelURI.segmentCount()).appendSegment(ICONS_FOLDER).appendSegment(
				packageName).appendSegment(iconName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.edit.provider.IItemLabelProvider#getText(java.lang.Object
	 * )
	 */
	public String getText(Object element) {
		String text = evaluate(element, LABEL_EXTENSION_NAME);
		return text;
	}

	/**
	 * @param element
	 * @param retVal
	 * @return
	 */
	private String evaluate(Object element, String name) {
		String retVal = null;
		if (element instanceof EObject && facade != null) {
			EObject eO = (EObject) element;
			retVal = (String) facade.style(name, eO);
		}
		return retVal;
	}

	/**
	 * @param object
	 * @return
	 */
	private String packageName(EObject object) {
		return object.eClass().getEPackage().getName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10848.java