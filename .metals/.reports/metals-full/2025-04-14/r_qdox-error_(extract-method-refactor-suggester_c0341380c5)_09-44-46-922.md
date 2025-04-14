error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4028.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4028.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4028.java
text:
```scala
f@@or (int i = commonWords, s = bits.length; s > i; i++) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.utils;

import java.util.Arrays;

/** A bitset, without size limitation, allows comparison via bitwise operators to other bitfields.
 * 
 * @author mzechner
 * @author jshapcott */
public class Bits {

	long[] bits = {0};

	public Bits () {
	}

	/** Creates a bit set whose initial size is large enough to explicitly represent bits with indices in the range 0 through
	 * nbits-1.
	 * @param nbits the initial size of the bit set */
	public Bits (int nbits) {
		checkCapacity(nbits >>> 6);
	}

	/** @param index the index of the bit
	 * @return whether the bit is set
	 * @throws ArrayIndexOutOfBoundsException if index < 0 */
	public boolean get (int index) {
		final int word = index >>> 6;
		if (word >= bits.length) return false;
		return (bits[word] & (1L << (index & 0x3F))) != 0L;
	}

	/** Returns the bit at the given index and clears it in one go.
	 * @param index the index of the bit
	 * @return whether the bit was set before invocation
	 * @throws ArrayIndexOutOfBoundsException if index < 0 */
	public boolean getAndClear (int index) {
		final int word = index >>> 6;
		if (word >= bits.length) return false;
		long oldBits = bits[word];
		bits[word] &= ~(1L << (index & 0x3F));
		return bits[word] != oldBits;
	}

	/** Returns the bit at the given index and sets it in one go.
	 * @param index the index of the bit
	 * @return whether the bit was set before invocation
	 * @throws ArrayIndexOutOfBoundsException if index < 0 */
	public boolean getAndSet (int index) {
		final int word = index >>> 6;
		checkCapacity(word);
		long oldBits = bits[word];
		bits[word] |= 1L << (index & 0x3F);
		return bits[word] == oldBits;
	}

	/** @param index the index of the bit to set
	 * @throws ArrayIndexOutOfBoundsException if index < 0 */
	public void set (int index) {
		final int word = index >>> 6;
		checkCapacity(word);
		bits[word] |= 1L << (index & 0x3F);
	}

	/** @param index the index of the bit to flip */
	public void flip (int index) {
		final int word = index >>> 6;
		checkCapacity(word);
		bits[word] ^= 1L << (index & 0x3F);
	}

	private void checkCapacity (int len) {
		if (len >= bits.length) {
			long[] newBits = new long[len + 1];
			System.arraycopy(bits, 0, newBits, 0, bits.length);
			bits = newBits;
		}
	}

	/** @param index the index of the bit to clear
	 * @throws ArrayIndexOutOfBoundsException if index < 0 */
	public void clear (int index) {
		final int word = index >>> 6;
		if (word >= bits.length) return;
		bits[word] &= ~(1L << (index & 0x3F));
	}

	/** Clears the entire bitset */
	public void clear () {
		long[] bits = this.bits;
		int length = bits.length;
		for (int i = 0; i < length; i++) {
			bits[i] = 0L;
		}
	}

	/** @return the number of bits currently stored, <b>not</b> the highset set bit! */
	public int numBits () {
		return bits.length << 6;
	}

	/** Returns the "logical size" of this bitset: the index of the highest set bit in the bitset plus one. Returns zero if the
	 * bitset contains no set bits.
	 * 
	 * @return the logical size of this bitset */
	public int length () {
		long[] bits = this.bits;
		for (int word = bits.length - 1; word >= 0; --word) {
			long bitsAtWord = bits[word];
			if (bitsAtWord != 0) {
				for (int bit = 63; bit >= 0; --bit) {
					if ((bitsAtWord & (1L << (bit & 0x3F))) != 0L) {
						return (word << 6) + bit;
					}
				}
			}
		}
		return 0;
	}

	/** @return true if this bitset contains no bits that are set to true */
	public boolean isEmpty () {
		long[] bits = this.bits;
		int length = bits.length;
		for (int i = 0; i < length; i++) {
			if (bits[i] != 0L) {
				return false;
			}
		}
		return true;
	}

	/** Returns the index of the first bit that is set to true that occurs on or after the specified starting index. If no such bit
	 * exists then -1 is returned. */
	public int nextSetBit (int fromIndex) {
		long[] bits = this.bits;
		int word = fromIndex >>> 6;
		int bitsLength = bits.length;
		if (word >= bitsLength) return -1;
		long bitsAtWord = bits[word];
		if (bitsAtWord != 0) {
			for (int i = fromIndex & 0x3f; i < 64; i++) {
				if ((bitsAtWord & (1L << (i & 0x3F))) != 0L) {
					return (word << 6) + i;
				}
			}
		}
		for (word++; word < bitsLength; word++) {
			if (word != 0) {
				bitsAtWord = bits[word];
				if (bitsAtWord != 0) {
					for (int i = 0; i < 64; i++) {
						if ((bitsAtWord & (1L << (i & 0x3F))) != 0L) {
							return (word << 6) + i;
						}
					}
				}
			}
		}
		return -1;
	}

	/** Returns the index of the first bit that is set to false that occurs on or after the specified starting index. If no such bit
	 * exists then -1 is returned. */
	public int nextClearBit (int fromIndex) {
		long[] bits = this.bits;
		int word = fromIndex >>> 6;
		int bitsLength = bits.length;
		if (word >= bitsLength) return -1;
		long bitsAtWord = bits[word];
		for (int i = fromIndex & 0x3f; i < 64; i++) {
			if ((bitsAtWord & (1L << (i & 0x3F))) == 0L) {
				return (word << 6) + i;
			}
		}
		for (word++; word < bitsLength; word++) {
			if (word == 0) {
				return word << 6;
			}
			bitsAtWord = bits[word];
			for (int i = 0; i < 64; i++) {
				if ((bitsAtWord & (1L << (i & 0x3F))) == 0L) {
					return (word << 6) + i;
				}
			}
		}
		return -1;
	}

	/** Performs a logical <b>AND</b> of this target bit set with the argument bit set. This bit set is modified so that each bit in
	 * it has the value true if and only if it both initially had the value true and the corresponding bit in the bit set argument
	 * also had the value true.
	 * @param other a bit set */
	public void and (Bits other) {
		int commonWords = Math.min(bits.length, other.bits.length);
		for (int i = 0; commonWords > i; i++) {
			bits[i] &= other.bits[i];
		}
		
		if (bits.length > commonWords) {
			for (int i = other.bits.length, s = bits.length; s > i; i++) {
				bits[i] = 0L;
			}
		}
	}

	/** Clears all of the bits in this bit set whose corresponding bit is set in the specified bit set.
	 * 
	 * @param other a bit set */
	public void andNot (Bits other) {
		for (int i = 0, j = bits.length, k = other.bits.length; i < j && i < k; i++) {
			bits[i] &= ~other.bits[i];
		}
	}

	/** Performs a logical <b>OR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it has the
	 * value true if and only if it either already had the value true or the corresponding bit in the bit set argument has the
	 * value true.
	 * @param other a bit set */
	public void or (Bits other) {
		int commonWords = Math.min(bits.length, other.bits.length);
		for (int i = 0; commonWords > i; i++) {
			bits[i] |= other.bits[i];
		}
		
		if (commonWords < other.bits.length) {
			checkCapacity(other.bits.length);
			for (int i = commonWords, s = other.bits.length; s > i; i++) {
				bits[i] = other.bits[i];
			}
		}
	}

	/** Performs a logical <b>XOR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it has
	 * the value true if and only if one of the following statements holds:
	 * <ul>
	 * <li>The bit initially has the value true, and the corresponding bit in the argument has the value false.</li>
	 * <li>The bit initially has the value false, and the corresponding bit in the argument has the value true.</li>
	 * </ul>
	 * @param other */
	public void xor (Bits other) {
		int commonWords = Math.min(bits.length, other.bits.length);
		
		for (int i = 0; commonWords > i; i++) {
			bits[i] ^= other.bits[i];
		}
		
		if (bits.length > commonWords) {
			for (int i = other.bits.length, s = bits.length; s > i; i++) {
				bits[i] = 0L;
			}
		} else if (commonWords < other.bits.length) {
			checkCapacity(other.bits.length);
			for (int i = commonWords, s = other.bits.length; s > i; i++) {
				bits[i] = other.bits[i];
			}
		}
	}

	/** Returns true if the specified BitSet has any bits set to true that are also set to true in this BitSet.
	 * 
	 * @param other a bit set
	 * @return boolean indicating whether this bit set intersects the specified bit set */
	public boolean intersects (Bits other) {
		long[] bits = this.bits;
		long[] otherBits = other.bits;
		for (int i = Math.min(bits.length, otherBits.length) - 1; i >= 0; i--) {
			if ((bits[i] & otherBits[i]) != 0) {
				return true;
			}
		}
		return false;
	}

	/** Returns true if this bit set is a super set of the specified set, i.e. it has all bits set to true that are also set to true
	 * in the specified BitSet.
	 * 
	 * @param other a bit set
	 * @return boolean indicating whether this bit set is a super set of the specified set */
	public boolean containsAll (Bits other) {
		long[] bits = this.bits;
		long[] otherBits = other.bits;
		int otherBitsLength = otherBits.length;
		int bitsLength = bits.length;

		for (int i = bitsLength; i < otherBitsLength; i++) {
			if (otherBits[i] != 0) {
				return false;
			}
		}
		for (int i = Math.min(bitsLength, otherBitsLength) - 1; i >= 0; i--) {
			if ((bits[i] & otherBits[i]) != otherBits[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int word = length() >>> 6;
		int hash = 0;
		for (int i = 0; word >= i; i++) {
			hash = 127 * hash + (int)(bits[i] ^ (bits[i] >>> 32));
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Bits other = (Bits) obj;
		long[] otherBits = other.bits;
		
		int commonWords = Math.min(bits.length, otherBits.length);
		for (int i = 0; commonWords > i; i++) {
			if (bits[i] != otherBits[i])
				return false;
		}
		
		if (bits.length == otherBits.length)
			return true;
		
		return length() == other.length();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4028.java