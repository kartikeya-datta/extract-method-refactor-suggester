error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3151.java
text:
```scala
public v@@oid encode( InputStream in, OutputStream out, int workerStatusController ) throws IOException

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

/** Document the purpose of this class.
 *
 * @version 1.0
 * @author Timo Stich
 */

package org.columba.mail.coder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

public class QuotedPrintableEncoder extends Encoder {

	private byte[] special;
	private int specialSize;
	private int maxLineLength;

	public QuotedPrintableEncoder() {
		super();
		coding = new String("quoted-printable");
		
		special = null;
		specialSize = 0;
		
		maxLineLength = 74;
	}

	public void setSpecial( byte[] special ) {
		this.special = special;
		specialSize = Array.getLength( special );
	}


	private boolean checkSpecial( byte op ) {
			
		for( int i=0; i<specialSize; i++ ) {
			if( special[i] == op ) return true;	
		}
			
		return false;
	}

	public String encode(String input, String charset)
		throws UnsupportedEncodingException {
		StringBuffer output = new StringBuffer();
		byte[] inBytes;
		byte[] code = { 61, 47, 47 }; // =00 

		if (charset == null) {
			inBytes = input.getBytes();
		} else {
			inBytes = input.getBytes(charset);
		}

		int length = Array.getLength(inBytes);
		int lineLength = 0;

		for (int i = 0; i < length; i++) {
			if ((((inBytes[i] >= 33) && (inBytes[i] <= 60))
 ((inBytes[i] >= 62) && (inBytes[i] <= 126))
 (inBytes[i] == 9)
 (inBytes[i] == 32)
 (inBytes[i] == 10)
 (inBytes[i] == 13) )
				&& !checkSpecial(inBytes[i]) ) {
				output.append( new String( inBytes, i, 1, "US-ASCII") );
			}
			else {				
				if( ((0x0f) & (inBytes[i] >> 4)) > 9 ) {
					code[1] = (byte)(((0x0f) & (inBytes[i] >> 4)) + 55); // A..F
				} else {
					code[1] = (byte)(((0x0f) & (inBytes[i] >> 4)) + 48); // 0..9
				}

				if( (inBytes[i] & 0x0F) > 9 ) {
					code[2] = (byte)((inBytes[i] & 0x0F) + 55); // A..F
				} else {
					code[2] = (byte)((inBytes[i] & 0x0F) + 48); // 0..9
				}
								
				output.append( new String(code, "US-ASCII") );					
			}
			
			if( inBytes[i] != 13 ) {
				lineLength++;
				if( (lineLength > maxLineLength) && (maxLineLength > 0 ) ) {
					output.append( "=\n" );
					lineLength = 0;
				}	
			} else {
				lineLength = 0;
			}
		}

		return output.toString();
	}

	public void encode( InputStream in, OutputStream out ) throws IOException
	{
		BufferedInputStream bufferedIn = new BufferedInputStream( in );
		BufferedOutputStream bufferedOut = new BufferedOutputStream( out );

		byte[] inBytes = new byte[1];
		byte[] code = { 61, 47, 47 }; // =00 
		byte[] softBreak = { 61, 13 }; // =\n

		int read = bufferedIn.read( inBytes );
		int lineLength = 0;
		

		while( read > 0 ) {
			if (((inBytes[0] >= 33) && (inBytes[0] <= 60))
 ((inBytes[0] >= 62) && (inBytes[0] <= 126))
 (inBytes[0] == 9)
 (inBytes[0] == 32)
 (inBytes[0] == 10)
 (inBytes[0] == 13) ) {
				bufferedOut.write(inBytes[0]);
			}
			else {				
				if( ((0x0f) & (inBytes[0] >> 4)) > 9 ) {
					code[1] = (byte)(((0x0f) & (inBytes[0] >> 4)) + 55); // A..F
				} else {
					code[1] = (byte)(((0x0f) & (inBytes[0] >> 4)) + 48); // 0..9
				}

				if( (inBytes[0] & 0x0F) > 9 ) {
					code[2] = (byte)((inBytes[0] & 0x0F) + 55); // A..F
				} else {
					code[2] = (byte)((inBytes[0] & 0x0F) + 48); // 0..9
				}
								
				bufferedOut.write( code );					
			}
			
			if( inBytes[0] != 13 ) {
				lineLength++;
				if( (lineLength > maxLineLength) && (maxLineLength > 0 ) ) {
					bufferedOut.write( softBreak );
					lineLength = 0;
				}	
			} else {
				lineLength = 0;
			}
			
			read = bufferedIn.read( inBytes );
		}

		
	}


	/**
	 * Sets the maxLineLength.
	 * @param maxLineLength The maxLineLength to set
	 */
	public void setMaxLineLength(int maxLineLength) {
		this.maxLineLength = maxLineLength;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3151.java