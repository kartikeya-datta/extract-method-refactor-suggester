error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3455.java
text:
```scala
v@@oid consolidate () {

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package java.io;

import com.google.gwt.storage.client.Storage;

/** Saves binary data to the local storage; currently using hex encoding. The string is prefixed with "hex:"
 * @author haustein */
public class RandomAccessFile /* implements DataOutput, DataInput, Closeable */{
	
/*
 * public final FileDescriptor getFD() throws IOException { } public final FileChannel getChannel() { }
 */
	final String name;
	final boolean writeable;
	boolean dirty;
	String data;
	int newDataPos;
	StringBuilder newData;
	int pos;
	int len;
	
	DataInputStream dis = new DataInputStream(new RafInputStream());
	DataOutputStream dos = new DataOutputStream(new RafOutputStream());

	public RandomAccessFile (String name, String mode) throws FileNotFoundException {
		this(new File(name), mode);
	}

	public RandomAccessFile (File file, String mode) throws FileNotFoundException {
		name = file.getCanonicalPath();

		mode = mode.toLowerCase();
		if (!mode.equals("r") && !mode.equals("rw")) {
			throw new IllegalArgumentException("mode: '" + mode + "'");
		}
		writeable = mode.equals("rw");
		if (file.exists()) {
			data = atob(File.LocalStorage.getItem(name));
			len = data.length();
		} else if (writeable) {
			data = "";
			dirty = true;
			try {
				flush();
			} catch (IOException e) {
				throw new FileNotFoundException("" + e);
			}
		} else {
			throw new FileNotFoundException(name);
		}
	}

	static native String btoa (String s) /*-{
														return $wnd.btoa(s);
														}-*/;

	static native String atob (String s) /*-{
														return $wnd.atob(s);
														}-*/;

	public int read () throws IOException {
		return dis.read();
	}

	public int read (byte b[], int off, int len) throws IOException {
		return dis.read(b, off, len);
	}

	public int read (byte b[]) throws IOException {
		return dis.read(b);
	}

	public final void readFully (byte b[]) throws IOException {
		dis.readFully(b);
	}

	public final void readFully (byte b[], int off, int len) throws IOException {
		dis.readFully(b, off, len);
	}

	public int skipBytes (int n) throws IOException {
		return dis.skipBytes(n);
	}

	public void write (int b) throws IOException {
		dos.write(b);
	};

	public void write (byte b[]) throws IOException {
		dos.write(b);
	}

	public void write (byte b[], int off, int len) throws IOException {
		dos.write(b, off, len);
	}

	public long getFilePointer () throws IOException {
		return pos;
	}

	public void seek (long pos) throws IOException {
		if (pos < 0) {
			throw new IllegalArgumentException();
		}
		this.pos = (int)pos;
	}

	public long length () throws IOException {
		return len;
	}

	public void setLength (long newLength) throws IOException {
		if (len != newLength) {
			consolidate();
			if (data.length() > newLength) {
				data = data.substring(0, (int)newLength);
				len = (int)newLength;
			} else {
// System.out.println("padding " + (newLength - len) + " zeros in setLength to " + newLength);
				while (len < newLength) {
					write(0);
				}
			}
		}
	}

	public void close () throws IOException {
		if (data != null) {
			flush();
			data = null;
		}
	}

	private void consolidate () {
		if (newData == null) {
			return;
		}

// System.out.println("consolidate(); ndp: " + newDataPos + " nd-len: " + newData.length());

		if (data.length() < newDataPos) {
			StringBuilder filler = new StringBuilder();
			while (data.length() + filler.length() < newDataPos) {
				filler.append('\0');
			}
// System.out.println("padding " + (filler.length()) + " zeros in consolidate ");

			data += filler.toString();
		}

		int p2 = newDataPos + newData.length();
		data = data.substring(0, newDataPos) + newData.toString() + (p2 < data.length() ? data.substring(p2) : "");
		newData = null;
	}

	void flush () throws IOException {
		if (!dirty) {
			return;
		}
		consolidate();
		File.LocalStorage.setItem(name, btoa(data));
		dirty = false;
	}

	public final boolean readBoolean () throws IOException {
		return dis.readBoolean();
	}

	public final byte readByte () throws IOException {
		return dis.readByte();
	}

	public final int readUnsignedByte () throws IOException {
		return dis.readUnsignedByte();
	}

	public final short readShort () throws IOException {
		return dis.readShort();
	}

	public final int readUnsignedShort () throws IOException {
		return dis.readUnsignedShort();
	}

	public final char readChar () throws IOException {
		return dis.readChar();
	}

	public final int readInt () throws IOException {
		return dis.readInt();
	}

	public final long readLong () throws IOException {
		return dis.readLong();
	}

	public final float readFloat () throws IOException {
		return dis.readFloat();
	}

	public final double readDouble () throws IOException {
		return dis.readDouble();
	}

	public final String readLine () throws IOException {
		return dis.readLine();
	}

	public final String readUTF () throws IOException {
		return dis.readUTF();
	}

	public final void writeBoolean (boolean v) throws IOException {
		dos.writeBoolean(v);
	}

	public final void writeByte (int v) throws IOException {
		dos.writeByte(v);
	}

	public final void writeShort (int v) throws IOException {
		dos.writeShort(v);
	}

	public final void writeChar (int v) throws IOException {
		dos.writeChar(v);
	}

	public final void writeInt (int v) throws IOException {
		dos.writeInt(v);
	}

	public final void writeLong (long v) throws IOException {
		dos.writeLong(v);
	}

	public final void writeFloat (float v) throws IOException {
		dos.writeFloat(v);
	}

	public final void writeDouble (double v) throws IOException {
		dos.writeDouble(v);
	}

	public final void writeBytes (String s) throws IOException {
		dos.writeBytes(s);
	}

	public final void writeChars (String s) throws IOException {
		dos.writeChars(s);
	}

	public final void writeUTF (String str) throws IOException {
		dos.writeUTF(str);
	}

// public final FileChannel getChannel() throws IOException { }

	class RafInputStream extends InputStream {
		@Override
		public int read () throws IOException {
			if (pos >= len) {
				return -1;
			} else {
				consolidate();
				return data.charAt(pos++);
// int p2 = (pos << 1);
// int result = Character.digit(data.charAt(p2), 16) * 16 +
// Character.digit(data.charAt(p2 + 1), 16);
// pos++;
// return result;
			}
		}
	}

	class RafOutputStream extends OutputStream {

		public void write (int b) throws IOException {
			if (!writeable) {
				throw new IOException("not writeable");
			}

			if (newData == null) {
				newDataPos = pos;
				newData = new StringBuilder();
// System.out.println("no buf; newDataPos: " + pos);
			} else if (newDataPos + newData.length() != pos) {
				consolidate();
				newDataPos = pos;
				newData = new StringBuilder();
// System.out.println("pos mismatch; newDataPos: " + pos);
			}

			newData.append((char)(b & 255));
// newData.append("" + Character.forDigit((b >> 4) & 15, 16) +
// Character.forDigit(b & 15, 16));
			pos++;
			len = Math.max(pos, len);
			dirty = true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3455.java