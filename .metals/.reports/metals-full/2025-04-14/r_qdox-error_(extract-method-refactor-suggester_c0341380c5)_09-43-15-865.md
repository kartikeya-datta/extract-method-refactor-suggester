error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3787.java
text:
```scala
i@@f(!RoleManager.getInstance().isFiltering())

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
package org.eclipse.ui.internal.registry;

import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.roles.RoleManager;
import org.eclipse.ui.*;
import org.eclipse.jface.resource.ImageDescriptor;
import java.util.*;

/* (non-Javadoc)
 * Implementation of IFileEditorMapping.
 */
public class FileEditorMapping extends Object implements IFileEditorMapping, Cloneable {
	private String name = "*"; //$NON-NLS-1$
	private String extension;

	// Collection of EditorDescriptor, where the first one
	// if considered the default one.
	private List editors = new ArrayList(1);
	private List deletedEditors = new ArrayList(1);
	/**
	 *  Create an instance of this class.
	 *
	 *  @param extension java.lang.String
	 *  @param mimeType java.lang.String
	 */
	public FileEditorMapping(String extension) {
		this("*", extension); //$NON-NLS-1$
	}
	/**
	 *  Create an instance of this class.
	 *
	 *  @param name java.lang.String
	 *  @param extension java.lang.String
	 */
	public FileEditorMapping(String name, String extension) {
		super();
		if (name == null || name.length() < 1)
			setName("*");
		//$NON-NLS-1$
		else
			setName(name);
		if (extension == null)
			setExtension("");
		//$NON-NLS-1$
		else
			setExtension(extension);
	}
	/**
	 * Add the given editor to the list of editors registered.
	 */
	public void addEditor(EditorDescriptor editor) {
		editors.add(editor);
		deletedEditors.remove(editor);
	}
	/**
	 * Clone the receiver.
	 */
	public Object clone() {
		try {
			FileEditorMapping clone = (FileEditorMapping) super.clone();
			clone.editors = (List) ((ArrayList) editors).clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	/**
	 * @see java.lang.Object.equals(Object obj)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FileEditorMapping))
			return false;
		FileEditorMapping mapping = (FileEditorMapping) obj;
		if (!this.name.equals(mapping.name))
			return false;
		if (!this.extension.equals(mapping.extension))
			return false;

		if (!compareList(this.editors, mapping.editors))
			return false;
		return compareList(this.deletedEditors, mapping.deletedEditors);
	}
	/**
	 * Compare the editor ids from both lists and return true if they
	 * are equals.
	 */
	private boolean compareList(List l1, List l2) {
		if (l1.size() != l2.size())
			return false;

		Iterator i1 = l1.iterator();
		Iterator i2 = l2.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			Object o1 = i1.next();
			Object o2 = i2.next();
			if (!(o1 == null ? o2 == null : o1.equals(o2)))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public IEditorDescriptor getDefaultEditor() {
		
		List filtered = filteredEditors();
		if (filtered.size() == 0)
			return null;
		else
			return (IEditorDescriptor) filtered.get(0);
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public IEditorDescriptor[] getEditors() {
		
		List filtered = filteredEditors();
		IEditorDescriptor[] array = new IEditorDescriptor[filtered.size()];
		filtered.toArray(array);
		return array;
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public IEditorDescriptor[] getDeletedEditors() {
		IEditorDescriptor[] array = new IEditorDescriptor[deletedEditors.size()];
		deletedEditors.toArray(array);
		return array;
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public String getExtension() {
		return extension;
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public ImageDescriptor getImageDescriptor() {
		IEditorDescriptor editor = getDefaultEditor();
		if (editor == null) {
			return WorkbenchImages.getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
		} else {
			return editor.getImageDescriptor();
		}
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public String getLabel() {
		return name + (extension.length() == 0 ? "" : "." + extension); //$NON-NLS-1$ //$NON-NLS-2$
	}
	/* (non-Javadoc)
	 * Method declared on IFileEditorMapping.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Remove the given editor from the set of editors registered.
	 */
	public void removeEditor(EditorDescriptor editor) {
		editors.remove(editor);
		deletedEditors.add(editor);
	}
	/**
	 * Set the default editor registered for file type
	 * described by this mapping.
	 */
	public void setDefaultEditor(EditorDescriptor editor) {
		editors.remove(editor);
		editors.add(0, editor);
	}
	/**
	 * Set the collection of all editors (EditorDescriptor)
	 * registered for the file type described by this mapping.
	 * Typically an editor is registered either through a plugin or explicitly by
	 * the user modifying the associations in the preference pages.
	 * This modifies the internal list to share the passed list.
	 * (hence the clear indication of list in the method name)
	 */
	public void setEditorsList(List newEditors) {
		editors = newEditors;
	}
	/**
	 * Set the collection of all editors (EditorDescriptor)
	 * formally registered for the file type described by this mapping 
	 * which have been deleted by the user.
	 * This modifies the internal list to share the passed list.
	 * (hence the clear indication of list in the method name)
	 */
	public void setDeletedEditorsList(List newDeletedEditors) {
		deletedEditors = newDeletedEditors;
	}
	/**
	 * Set the file's extension.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	/**
	 * Set the file's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the filtered version of the editors.
	 * @return List
	 */
	private List filteredEditors() {
		if(!RoleManager.getInstance().filterRoles)
			return editors;
			
		ArrayList filtered = new ArrayList();
		RoleManager manager = RoleManager.getInstance();
		Iterator editorsIterator = editors.iterator();

		while (editorsIterator.hasNext()) {
			IEditorDescriptor next = (IEditorDescriptor) editorsIterator.next();
			if (manager.isEnabledId(next.getId()))
				filtered.add(next);
		}

		return filtered;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3787.java