error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2409.java
text:
```scala
r@@eturn true;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

/**
 * This class is used to read resource editor registry descriptors from
 * the platform registry.
 */
public class EditorRegistryReader extends RegistryReader {

    private EditorRegistry editorRegistry;

    /**
     * Get the editors that are defined in the registry
     * and add them to the ResourceEditorRegistry
     *
     * Warning:
     * The registry must be passed in because this method is called during the
     * process of setting up the registry and at this time it has not been
     * safely setup with the plugin.
     */
    protected void addEditors(EditorRegistry registry) {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        this.editorRegistry = registry;
        readRegistry(extensionRegistry, PlatformUI.PLUGIN_ID,
                IWorkbenchRegistryConstants.PL_EDITOR);
    }

    /**
     * Implementation of the abstract method that
     * processes one configuration element.
     */
    protected boolean readElement(IConfigurationElement element) {
        if (!element.getName().equals(IWorkbenchRegistryConstants.TAG_EDITOR)) {
			return false;
		}

        String id = element.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
        if (id == null) {
            logMissingAttribute(element, IWorkbenchRegistryConstants.ATT_ID);
            return true;
        }
        
        EditorDescriptor editor = new EditorDescriptor(id, element);
        
        List extensionsVector = new ArrayList();
        List filenamesVector = new ArrayList();
		List contentTypeVector = new ArrayList();
        boolean defaultEditor = false;

        // Get editor name (required field).
        if (element.getAttribute(IWorkbenchRegistryConstants.ATT_NAME) == null) {
            logMissingAttribute(element, IWorkbenchRegistryConstants.ATT_NAME);
            return true;
        }

        // Get editor icon (required field for internal editors)
        if (element.getAttribute(IWorkbenchRegistryConstants.ATT_ICON) == null) {
            if (getClassValue(element, IWorkbenchRegistryConstants.ATT_CLASS) != null) {
                logMissingAttribute(element, IWorkbenchRegistryConstants.ATT_ICON);
                // Don't bail in this case - we can live without an icon as one will be provided for free in EditorDescriptor
            }
        }
        
        // Get target extensions (optional field)
        String extensionsString = element.getAttribute(IWorkbenchRegistryConstants.ATT_EXTENSIONS);
        if (extensionsString != null) {
            StringTokenizer tokenizer = new StringTokenizer(extensionsString,
                    ",");//$NON-NLS-1$
            while (tokenizer.hasMoreTokens()) {
                extensionsVector.add(tokenizer.nextToken().trim());
            }
        }
        String filenamesString = element.getAttribute(IWorkbenchRegistryConstants.ATT_FILENAMES);
        if (filenamesString != null) {
            StringTokenizer tokenizer = new StringTokenizer(filenamesString,
                    ",");//$NON-NLS-1$
            while (tokenizer.hasMoreTokens()) {
                filenamesVector.add(tokenizer.nextToken().trim());
            }
        }
        
		IConfigurationElement [] bindings = element.getChildren(IWorkbenchRegistryConstants.TAG_CONTENT_TYPE_BINDING);
		for (int i = 0; i < bindings.length; i++) {
			String contentTypeId = bindings[i].getAttribute(IWorkbenchRegistryConstants.ATT_CONTENT_TYPE_ID);
			if (contentTypeId == null) {
				continue;
			}
			contentTypeVector.add(contentTypeId);
		}
		
        // Is this the default editor?
        String def = element.getAttribute(IWorkbenchRegistryConstants.ATT_DEFAULT);
        if (def != null) {
			defaultEditor = Boolean.valueOf(def).booleanValue();
		}

        // Add the editor to the manager.	
        editorRegistry.addEditorFromPlugin(editor, extensionsVector,
                filenamesVector, contentTypeVector, defaultEditor);
        return true;
    }


    /**
     * @param editorRegistry
     * @param element
     */
    public void readElement(EditorRegistry editorRegistry,
            IConfigurationElement element) {
        this.editorRegistry = editorRegistry;
        readElement(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2409.java