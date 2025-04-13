error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15045.java
text:
```scala
m@@yType.fileName = "abc.txt";

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
package org.eclipse.swt.snippets;

/*
 * Drag and Drop example snippet: define data transfer types that subclass each
 * other
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet171 {

/*
 * The data being transferred is an <bold>array of type MyType</bold> where
 * MyType is define as:
 */
static class MyType {
	String fileName;
	long fileLength;
	long lastModified;
}

static class MyTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "MytypeTransfer";
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static MyTransfer _instance = new MyTransfer();

	public static Transfer getInstance() {
		return _instance;
	}

	byte[] javaToByteArray(Object object) {
		MyType data = (MyType) object;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream writeOut = new DataOutputStream(out);
			byte[] buffer = data.fileName.getBytes();
			writeOut.writeInt(buffer.length);
			writeOut.write(buffer);
			writeOut.writeLong(data.fileLength);
			writeOut.writeLong(data.lastModified);
			buffer = out.toByteArray();
			writeOut.close();
			return buffer;
		} catch (IOException e) {
		}
		return null;
	}

	Object byteArrayToJava(byte[] bytes) {
		MyType data = new MyType();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			DataInputStream readIn = new DataInputStream(in);
			int size = readIn.readInt();
			byte[] buffer = new byte[size];
			readIn.read(buffer);
			data.fileName = new String(buffer);
			data.fileLength = readIn.readLong();
			data.lastModified = readIn.readLong();
			readIn.close();
		} catch (IOException ex) {
			return null;
		}
		return data;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (!checkMyType(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		byte[] buffer = javaToByteArray(object);
		super.javaToNative(buffer, transferData);
	}

	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null)
				return null;
			return byteArrayToJava(buffer);
		}
		return null;
	}

	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}

	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}

	boolean checkMyType(Object object) {
		return object != null && object instanceof MyType;
	}

	protected boolean validate(Object object) {
		return checkMyType(object);
	}
}

/*
 * The data being transferred is an <bold>array of type MyType2</bold>
 * where MyType2 is define as:
 */
static class MyType2 extends MyType {
	String version;
}

static class MyTransfer2 extends MyTransfer {

	private static final String MYTYPE2NAME = "Mytype2Transfer";
	private static final int MYTYPE2ID = registerType(MYTYPE2NAME);
	private static MyTransfer _instance = new MyTransfer2();

	public static Transfer getInstance() {
		return _instance;
	}

	protected String[] getTypeNames() {
		return new String[] { MYTYPE2NAME };
	}

	protected int[] getTypeIds() {
		return new int[] { MYTYPE2ID };
	}

	byte[] javaToByteArray(Object object) {
		MyType2 data = (MyType2) object;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream writeOut = new DataOutputStream(out);
			byte[] buffer = data.fileName.getBytes();
			writeOut.writeInt(buffer.length);
			writeOut.write(buffer);
			writeOut.writeLong(data.fileLength);
			writeOut.writeLong(data.lastModified);
			buffer = data.version.getBytes();
			writeOut.writeInt(buffer.length);
			writeOut.write(buffer);
			buffer = out.toByteArray();
			writeOut.close();
			return buffer;
		} catch (IOException e) {
		}
		return null;
	}

	Object byteArrayToJava(byte[] bytes) {
		MyType2 data = new MyType2();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			DataInputStream readIn = new DataInputStream(in);
			int size = readIn.readInt();
			byte[] buffer = new byte[size];
			readIn.read(buffer);
			data.fileName = new String(buffer);
			data.fileLength = readIn.readLong();
			data.lastModified = readIn.readLong();
			size = readIn.readInt();
			buffer = new byte[size];
			readIn.read(buffer);
			data.version = new String(buffer);
			readIn.close();
		} catch (IOException ex) {
			return null;
		}
		return data;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (!checkMyType2(object)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		super.javaToNative(object, transferData);
	}

	boolean checkMyType2(Object object) {
		if (!checkMyType(object))
			return false;
		return object != null && object instanceof MyType2;
	}

	protected boolean validate(Object object) {
		return checkMyType2(object);
	}
}

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Label label1 = new Label(shell, SWT.BORDER | SWT.WRAP);
	label1.setText("Drag Source for MyData and MyData2");
	final Label label2 = new Label(shell, SWT.BORDER | SWT.WRAP);
	label2.setText("Drop Target for MyData");
	final Label label3 = new Label(shell, SWT.BORDER | SWT.WRAP);
	label3.setText("Drop Target for MyData2");

	DragSource source = new DragSource(label1, DND.DROP_COPY);
	source.setTransfer(new Transfer[] { MyTransfer.getInstance(),
			MyTransfer2.getInstance() });
	source.addDragListener(new DragSourceAdapter() {
		public void dragSetData(DragSourceEvent event) {
			MyType2 myType = new MyType2();
			myType.fileName = "C:\\abc.txt";
			myType.fileLength = 1000;
			myType.lastModified = 12312313;
			myType.version = "version 2";
			event.data = myType;
		}
	});
	DropTarget targetMyType = new DropTarget(label2, DND.DROP_COPY | DND.DROP_DEFAULT);
	targetMyType.setTransfer(new Transfer[] { MyTransfer.getInstance() });
	targetMyType.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT)
				event.detail = DND.DROP_COPY;
		}

		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT)
				event.detail = DND.DROP_COPY;
		}

		public void drop(DropTargetEvent event) {
			if (event.data != null) {
				MyType myType = (MyType) event.data;
				if (myType != null) {
					String string = "MyType: " + myType.fileName;
					label2.setText(string);
				}
			}
		}

	});
	DropTarget targetMyType2 = new DropTarget(label3, DND.DROP_COPY	| DND.DROP_DEFAULT);
	targetMyType2.setTransfer(new Transfer[] { MyTransfer2.getInstance() });
	targetMyType2.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT)
				event.detail = DND.DROP_COPY;
		}

		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT)
				event.detail = DND.DROP_COPY;
		}

		public void drop(DropTargetEvent event) {
			if (event.data != null) {
				MyType2 myType = (MyType2) event.data;
				if (myType != null) {
					String string = "MyType2: " + myType.fileName + ":"
							+ myType.version;
					label3.setText(string);
				}
			}
		}

	});
	shell.setSize(300, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15045.java