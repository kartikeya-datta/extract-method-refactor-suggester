error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1453.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1453.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1453.java
text:
```scala
o@@ut.writeInt(new Integer(BooleanCompressor.compress(b)).intValue());

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

import java.awt.Color;
import java.util.Date;

import org.columba.core.base.BooleanCompressor;
import org.columba.core.gui.base.ColorFactory;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.IColumbaHeader;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.parser.AddressParser;
import org.columba.ristretto.parser.ParserException;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class DefaultHeaderBinding extends TupleBinding {

	public Object entryToObject(TupleInput in) {
		ColumbaHeader header =  new ColumbaHeader();

		header.getAttributes().put("columba.uid", new Integer(in.readInt()));
		
		// load boolean headerfields, which are compressed in one int value
		int compressedFlags = 0;
		
		compressedFlags = in.readInt();

		for (int i = 0; i < CachedHeaderfields.INTERNAL_COMPRESSED_HEADERFIELDS.length; i++) {
			header.set(CachedHeaderfields.INTERNAL_COMPRESSED_HEADERFIELDS[i],
					BooleanCompressor.decompress(compressedFlags, i));
		}

		// load other internal headerfields, non-boolean type
		String[] columnNames = CachedHeaderfields.INTERNAL_HEADERFIELDS;
		Class[] columnTypes = CachedHeaderfields.INTERNAL_HEADERFIELDS_TYPE;

		for (int j = 0; j < columnNames.length; j++) {
			Object value = null;

			if (columnTypes[j] == Integer.class) {
				value = new Integer(in.readInt());
			} else if (columnTypes[j] == Date.class) {
				value = new Date(in.readLong());
			} else if (columnTypes[j] == Color.class) {
				value = ColorFactory.getColor(in.readInt());
			} else if (columnTypes[j] == Address.class) {
				try {
					value = AddressParser.parseAddress(in.readString());
				} catch (IndexOutOfBoundsException e) {
				} catch (IllegalArgumentException e) {
				} catch (ParserException e) {
				} finally {
					if (value == null)
						value = "";
				}
			} else
				value = in.readString();

			if (value != null) {
				header.set(columnNames[j], value);
			}
		}

		//load default headerfields, as defined in RFC822
		columnNames = CachedHeaderfields.getDefaultHeaderfields();

		for (int j = 0; j < columnNames.length; j++) {
			String value = in.readString();
			if (value != null) {
				header.set(columnNames[j], value);
			}
		}
		
		return header;
	}

	public void objectToEntry(Object arg0, TupleOutput out) {
		IColumbaHeader header = (IColumbaHeader) arg0;
		
		out.writeInt(((Integer)header.getAttributes().get("columba.uid")).intValue());
		
		// save boolean headerfields, compressing them to one int value
		Boolean[] b = new Boolean[CachedHeaderfields.INTERNAL_COMPRESSED_HEADERFIELDS.length];

		for (int i = 0; i < b.length; i++) {
			b[i] = (Boolean) header
					.get(CachedHeaderfields.INTERNAL_COMPRESSED_HEADERFIELDS[i]);

			// if value doesn't exist, use false as default
			if (b[i] == null) {
				b[i] = Boolean.FALSE;
			}
		}

		out.writeInt(new Integer(BooleanCompressor.compress(b)));

		// save other internal headerfields, of non-boolean type
		String[] columnNames = CachedHeaderfields.INTERNAL_HEADERFIELDS;
		Class[] columnTypes = CachedHeaderfields.INTERNAL_HEADERFIELDS_TYPE;
		Object o;

		for (int j = 0; j < columnNames.length; j++) {
			o = header.get(columnNames[j]);

			//System.out.println("type="+o.getClass());

			if (columnTypes[j] == Integer.class)
				out.writeInt(((Integer) o).intValue());
			else if (columnTypes[j] == Date.class) {
				out.writeLong(((Date) o).getTime());
			} else if (columnTypes[j] == Color.class) {
				out.writeInt(((Color) o).getRGB());
			} else if (columnTypes[j] == Address.class) {
				if (o instanceof Address)
					out.writeString(((Address) o).toString());
				else
					out.writeString((String) o);
			} else
				out.writeString(o.toString());
		}

		// save default headerfields, as defined in RFC822
		columnNames = CachedHeaderfields.getDefaultHeaderfields();

		for (int j = 0; j < columnNames.length; j++) {
			String v = (String) header.get(columnNames[j]);
			if ( v==null) v = "";
			out.writeString(v);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1453.java