error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2451.java
text:
```scala
R@@untime.getRuntime().exec(new String[]{programFileName, path});

package org.eclipse.ui.internal.misc;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.EditorDescriptor;

public class ExternalEditor {
	private IFile file;
	private EditorDescriptor descriptor;
/**
 * Create an external editor.
 */
public ExternalEditor(IFile newFile, EditorDescriptor editorDescriptor) {
	this.file = newFile;
	this.descriptor = editorDescriptor;
}
/**
 * open the editor. If the descriptor has a program then use it - otherwise build its
 * info from the descriptor.
 * @exception	Throws a CoreException if the external editor could not be opened.
 */
public void open() throws CoreException {

	Program program = this.descriptor.getProgram();
	if (program == null)
		openWithUserDefinedProgram();
	else {
		String path = file.getLocation().toOSString();
		if (!program.execute(path)) 
			throw new CoreException(new Status(
				Status.ERROR, 
				WorkbenchPlugin.PI_WORKBENCH, 
				0, 
				WorkbenchMessages.format("ExternalEditor.errorMessage", new Object[] {path}), //$NON-NLS-1$
				null));
	}
}
/**
 * open the editor.
 * @exception	Throws a CoreException if the external editor could not be opened.
 */
public void openWithUserDefinedProgram() throws CoreException {
	// We need to determine if the command refers to a program in the plugin
	// install directory. Otherwise we assume the program is on the path.

	String programFileName = null;
	IConfigurationElement configurationElement = descriptor.getConfigurationElement();

	// Check if we have a config element (if we don't it is an
	// external editor created on the resource associations page).
	if (configurationElement != null) {
		// Get editor's plugin directory.
		URL installURL = configurationElement.getDeclaringExtension().getDeclaringPluginDescriptor().getInstallURL();
		try {
			// See if the program file is in the plugin directory
			URL commandURL = new URL(installURL, descriptor.getFileName());
			URL localName = Platform.asLocalURL(commandURL); // this will bring the file local if the plugin is on a server
			File file = new File(localName.getFile());
			//Check that it exists before we assert it is valid
			if(file.exists())
				programFileName = file.getAbsolutePath();
		} catch (IOException e) {
			// Program file is not in the plugin directory
		}
	}

	if (programFileName == null) 
		// Program file is not in the plugin directory therefore
		// assume it is on the path
		programFileName = descriptor.getFileName();

	// Get the full path of the file to open	
	String path = file.getLocation().toOSString();

	// Open the file
	
	// ShellCommand was removed in response to PR 23888.  If an exception was 
	// thrown, it was not caught in time, and no feedback was given to user
	
	try {
		Process p = Runtime.getRuntime().exec(new String[]{programFileName, path});
	} catch (Exception e) {
		throw new CoreException(new Status(
			Status.ERROR, 
			WorkbenchPlugin.PI_WORKBENCH, 
			0, 
			WorkbenchMessages.format("ExternalEditor.errorMessage", new Object[] {programFileName}), //$NON-NLS-1$
			e));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2451.java