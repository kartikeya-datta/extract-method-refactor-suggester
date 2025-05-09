error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2123.java
text:
```scala
r@@eturn checkFile.getName().endsWith("." + getFileExtension());

/*
 * @(#)StandardStorageFormat.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.util;

import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import CH.ifa.draw.framework.Drawing;

/**
 * A StandardStorageFormat is an internal file format to store and restore
 * Drawings. It uses its own descriptive syntax ands write classes and attributes
 * as plain text in a text file. The StandardStorageFormat has the file extension
 * "draw" (e.g. my_picasso.draw).
 *
 * @author Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class StandardStorageFormat implements StorageFormat {

	/**
	 * FileFilter for a javax.swing.JFileChooser which recognizes files with the
	 * extension "draw"
	 */
	private FileFilter myFileFilter;
	
	/**
	 * File extension
	 */
	private String myFileExtension;

	/**
	 * Description of the file type when displaying the FileFilter
	 */
	private String myFileDescription;
	
	/**
	 * Create a StandardStorageFormat for storing and restoring Drawings.
	 */
	public StandardStorageFormat() {
		setFileExtension(createFileExtension());
		setFileDescription(createFileDescription());
		setFileFilter(createFileFilter());
	}

	/**
	 * Factory method to create the file extension recognized by the FileFilter for this
	 * StandardStorageFormat. The StandardStorageFormat has the file extension "draw"
	 * (e.g. my_picasso.draw).
	 *
	 * @return new file extension
	 */
	protected String createFileExtension() {
		return myFileExtension = "draw";
	}
	
	/**
	 * Set the file extension for the storage format
	 *
	 * @param newFileExtension extension
	 */
	public void setFileExtension(String newFileExtension) {
		myFileExtension = newFileExtension;
	}
	
	/**
	 * Return the file extension for the storage format
	 *
	 * @return file extension
	 */
	public String getFileExtension() {
		return myFileExtension;
	}

	/**
	 * Factory method to create a file description for the file type when displaying the
	 * associated FileFilter.
	 *
	 * @return new file description
	 */
	public String createFileDescription() {
		return "Internal Format (" + getFileExtension() + ")";
	}

	/**
	 * Set the file description for the file type of the storage format
	 *
	 * @param newFileDescription description of the file type
	 */	
	public void setFileDescription(String newFileDescription) {
		myFileDescription = newFileDescription;
	}
	
	/**
	 * Return the file description for the file type of the storage format
	 *
	 * @return description of the file type
	 */	
	public String getFileDescription() {
		return myFileDescription;
	}
	
	/**
	 * Factory method to create a FileFilter that accepts file with the appropriate
	 * file exention used by a javax.swing.JFileChooser. Subclasses can override this
	 * method to provide their own file filters.
	 *
	 * @return FileFilter for this StorageFormat
	 */
	protected FileFilter createFileFilter() {
		return new FileFilter() {
			public boolean accept(File checkFile) {
				// still display directories for navigation
				if (checkFile.isDirectory()) {
					return true;
				}
				else {
					return checkFile.getName().endsWith("." + getFileDescription());
				}
			}

			public String getDescription() {
				return getFileDescription();
			}
		};
	}
	
	/**
	 * Set the FileFilter used to identify Drawing files with the correct file
	 * extension for this StorageFormat.
	 *
	 * @param newFileFilter FileFilter for this StorageFormat
	 */
	public void setFileFilter(FileFilter newFileFilter) {
		myFileFilter = newFileFilter;
	}
	
	/**
	 * Return the FileFilter used to identify Drawing files with the correct file
	 * extension for this StorageFormat.
	 *
	 * @return FileFilter for this StorageFormat
	 */
	public FileFilter getFileFilter() {
		return myFileFilter;
	}

	/**
	 * @see CH.ifa.draw.util.StorageFormat#isRestoreFormat()
	 */
	public boolean isRestoreFormat() {
		return true;
	}

	/**
	 * @see CH.ifa.draw.util.StorageFormat#isStoreFormat()
	 */
	public boolean isStoreFormat() {
		return true;
	}

	/**
	 * Store a Drawing under a given name. If the file name does not have the correct
	 * file extension, then the file extension is added.
	 *
	 * @param fileName file name of the Drawing under which it should be stored
	 * @param saveDrawing drawing to be saved
	 * @return file name with correct file extension
	 */
	public String store(String fileName, Drawing saveDrawing) throws IOException {
		FileOutputStream stream = new FileOutputStream(adjustFileName(fileName));
		StorableOutput output = new StorableOutput(stream);
		output.writeStorable(saveDrawing);
		output.close();
		return adjustFileName(fileName);
	}

	/**
	 * Restore a Drawing from a file with a given name.
	 *
	 * @param fileName of the file in which the Drawing has been saved
	 * @return restored Drawing
	 */
	public Drawing restore(String fileName) throws IOException {
		if (!hasCorrectFileExtension(fileName)) {
			return null;
		}
		else {
			FileInputStream stream = new FileInputStream(fileName);
			StorableInput input = new StorableInput(stream);
			return (Drawing)input.readStorable();
		}
	}
	
	/**
	 * Test, whether two StorageFormats are the same. They are the same if they both support the
	 * same file extension.
	 *
	 * @return true, if both StorageFormats have the same file extension, false otherwise
	 */
	public boolean equals(Object compareObject) {
		if (compareObject instanceof StandardStorageFormat) {
			return getFileExtension().equals(((StandardStorageFormat)compareObject).getFileExtension());
		}
		else {
			return false;
		}
	}

	/**
	 * Adjust a file name to have the correct file extension.
	 *
	 * @param testFileName file name to be tested for a correct file extension
	 * @return testFileName + file extension if necessary
	 */	
	protected String adjustFileName(String testFileName) {
		if (!hasCorrectFileExtension(testFileName)) {
			return testFileName + "." + getFileExtension();
		}
		else {
			return testFileName;
		}
	}
	
	/**
	 * Test whether the file name has the correct file extension
	 *
	 * @return true, if the file has the correct extension, false otherwise
	 */
	protected boolean hasCorrectFileExtension(String testFileName) {
		return testFileName.endsWith("." + getFileExtension());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2123.java