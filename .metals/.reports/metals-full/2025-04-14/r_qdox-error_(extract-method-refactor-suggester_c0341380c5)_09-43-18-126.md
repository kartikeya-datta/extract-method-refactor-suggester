error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4450.java
text:
```scala
public v@@oid close() throws IOException {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.folder.headercache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.columba.core.logging.Logging;

/**
 * Object stream writer used to save header-cache files.
 * <p>
 * To make this more failsafe, we first try to save the file as ".backup". If
 * everything went ok, we rename this file to the original filename. If not, the
 * ObjectReader class is using the last working version of the file instead.
 * 
 * @author fdietz
 */
public class ObjectWriter {
	private static final int NULL = 0;
	private static final int STRING = 1;
	private static final int DATE = 2;
	private static final int BOOLEAN = 3;
	private static final int INTEGER = 4;
	private static final int COLOR = 5;
	private static final int OBJECT = 6;
	protected File file;
	protected FileOutputStream ostream;
	protected ObjectOutputStream oos;
	protected File newFile;

	public ObjectWriter(File file) throws Exception {
		this.file = file;

		// create ".backup" file from original filename
		newFile = new File(file.getAbsolutePath() + ".new");

		// use the ".backup" file, instead of the original
		ostream = new FileOutputStream(newFile.getPath());
		oos = new ObjectOutputStream(ostream);
	}

	public void writeString(String str) throws IOException {
		oos.writeUTF(str);
	}
	
	public void writeInt(int in) throws IOException {
		oos.writeInt(in);
	}
	
	public void writeLong(long lon) throws IOException {
		oos.writeLong(lon);
	}
	
	public void writeObject(Object o) throws IOException {
		oos.writeObject(o);
	}

	/*
	public void writeObject(Object value) throws Exception {
		Object o = value;

		if (o == null) {
			oos.writeInt(NULL);
		} else if (o instanceof String) {
			oos.writeInt(STRING);
			oos.writeUTF((String) o);
		} else if (o instanceof Integer) {
			oos.writeInt(INTEGER);
			oos.writeInt(((Integer) o).intValue());
		} else if (o instanceof Boolean) {
			oos.writeInt(BOOLEAN);
			oos.writeBoolean(((Boolean) o).booleanValue());
		} else if (o instanceof Date) {
			oos.writeInt(DATE);
			oos.writeLong(((Date) o).getTime());
		} else if (o instanceof Color) {
			oos.writeInt(COLOR);
			oos.writeInt(((Color) o).getRGB());
		} else {
			oos.writeInt(OBJECT);
			oos.writeObject(value);
		}
	}
	*/
	
	public void close() throws Exception {

		try {
			// close all file streams
			oos.close();
			ostream.close();
		} catch (IOException e) {
			if (Logging.DEBUG)
				e.printStackTrace();

			// wasn't able to successfully finish saving the ".backup" file
			// -> simply leave the original message untouched
			// --> original message will be used next time
			
			newFile.delete();
			
			return;
		}

		// successfully saved ".backup" file
		File oldFile = new File(file.getAbsolutePath() + ".old");
		if ( oldFile.exists() ) oldFile.delete();
		
		// rename original to failsafe ".old" file
		file.renameTo(oldFile);
		// rename ".new" to original resulting file
		newFile.renameTo(file);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4450.java