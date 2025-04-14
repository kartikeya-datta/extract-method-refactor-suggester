error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8263.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8263.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 598
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8263.java
text:
```scala
public final class TIFFFileFormat extends FileFormat {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;

/**
 * Baseline TIFF decoder revision 6.0
 * Extension T4-encoding CCITT T.4 1D
 */
final class TIFFFileFormat extends FileFormat {

boolean isFileFormat(LEDataInputStream stream) {
	try {
		byte[] header = new byte[4];
		stream.read(header);
		stream.unread(header);
		if (header[0] != header[1]) return false;
		if (!(header[0] == 0x49 && header[2] == 42 && header[3] == 0) &&
			!(header[0] == 0x4d && header[2] == 0 && header[3] == 42)) {
			return false;
		} 	
		return true;
	} catch (Exception e) {
		return false;
	}
}

ImageData[] loadFromByteStream() {	
	byte[] header = new byte[8];
	boolean isLittleEndian;
	ImageData[] images = new ImageData[0];
	TIFFRandomFileAccess file = new TIFFRandomFileAccess(inputStream);
	try {
		file.read(header);
		if (header[0] != header[1]) SWT.error(SWT.ERROR_INVALID_IMAGE);
		if (!(header[0] == 0x49 && header[2] == 42 && header[3] == 0) &&
			!(header[0] == 0x4d && header[2] == 0 && header[3] == 42)) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		} 
		isLittleEndian = header[0] == 0x49;	
		int offset = isLittleEndian ? 
			(header[4] & 0xFF) | ((header[5] & 0xFF) << 8) | ((header[6] & 0xFF) << 16) | ((header[7] & 0xFF) << 24) :
			(header[7] & 0xFF) | ((header[6] & 0xFF) << 8) | ((header[5] & 0xFF) << 16) | ((header[4] & 0xFF) << 24);
		file.seek(offset);
		TIFFDirectory directory = new TIFFDirectory(file, isLittleEndian, loader);
		ImageData image = directory.read();
		/* A baseline reader is only expected to read the first directory */
		images = new ImageData[] {image};
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return images;
}

void unloadIntoByteStream(ImageLoader loader) {
	/* We do not currently support writing multi-page tiff,
	 * so we use the first image data in the loader's array. */
	ImageData image = loader.data[0];
	TIFFDirectory directory = new TIFFDirectory(image);
	try {
		directory.writeToStream(outputStream);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8263.java