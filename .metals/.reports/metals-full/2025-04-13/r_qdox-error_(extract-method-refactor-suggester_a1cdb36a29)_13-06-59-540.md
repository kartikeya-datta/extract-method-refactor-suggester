error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2019.java
text:
```scala
public final v@@oid clear() {

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

package com.badlogic.gdx.graphics.g3d;

import java.util.Comparator;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class Attributes implements Iterable<Attribute>, Comparator<Attribute> {
	protected long mask;
	protected final Array<Attribute> attributes = new Array<Attribute>();

	protected boolean sorted = true;
	
	/** Sort the attributes by their ID */
	public final void sort() {
		if (!sorted) {
			attributes.sort(this);
			sorted = true;
		}
	}
	
	/** @return Bitwise mask of the ID's of all the containing attributes */  
	public final long getMask() {
		return mask;
	}
	
	/** Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
	 * @return The attribute (which can safely be cast) if any, otherwise null */
	public final Attribute get(final long type) {
		if (has(type))
			for (int i = 0; i < attributes.size; i++)
				if (attributes.get(i).type == type)
					return attributes.get(i);
		return null;
	}
	
	/** Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
	 * @return The attribute if any, otherwise null */
	public final <T extends Attribute> T get(Class<T> clazz, final long type) {
		return (T)get(type);
	}
	
	/** Get multiple attributes at once.
	 * Example: material.get(out, ColorAttribute.Diffuse | ColorAttribute.Specular | TextureAttribute.Diffuse); */
	public final Array<Attribute> get(final Array<Attribute> out, final long type) {
		for (int i = 0; i < attributes.size; i++)
			if ((attributes.get(i).type & type) != 0)
				out.add(attributes.get(i));
		return out;
	}
	
	/** Removes all attributes */
	public void clear() {
		mask = 0;
		attributes.clear();
	}
	
	/** @return The amount of attributes this material contains. */
	public int size() {
		return attributes.size;
	}
	
	private final void enable(final long mask) {
		this.mask |= mask; 
	}
	private final void disable(final long mask) {
		this.mask &= -1L ^ mask;
	}
	
	/** Add a attribute to this material.
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Attribute attribute) {
		final int idx = indexOf(attribute.type);
		if (idx < 0) {
			enable(attribute.type);
			attributes.add(attribute);
			sorted = false;
		} else {
			attributes.set(idx, attribute);
		}
	}

	/** Add multiple attributes to this material. 
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Attribute attribute1, final Attribute attribute2) {
		set(attribute1);
		set(attribute2);
	}
	
	/** Add multiple attributes to this material. 
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Attribute attribute1, final Attribute attribute2, final Attribute attribute3) {
		set(attribute1);
		set(attribute2);
		set(attribute3);
	}
	
	/** Add multiple attributes to this material. 
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Attribute attribute1, final Attribute attribute2, final Attribute attribute3, final Attribute attribute4) {
		set(attribute1);
		set(attribute2);
		set(attribute3);
		set(attribute4);
	}
	
	/** Add an array of attributes to this material. 
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Attribute... attributes) {
		for (final Attribute attr : attributes)
			set(attr);
	}

	/** Add an array of attributes to this material.
	 * If the material already contains an attribute of the same type it is overwritten. */
	public final void set(final Iterable<Attribute> attributes) {
		for (final Attribute attr : attributes)
			set(attr);
	}
	
	/** Removes the attribute from the material, i.e.: material.remove(BlendingAttribute.ID);
	 * Can also be used to remove multiple attributes also, i.e. remove(AttributeA.ID | AttributeB.ID); */
	public final void remove(final long mask) {
		for (int i = 0; i < attributes.size; i++) {
			final long type = attributes.get(i).type;
			if ((mask & type) == type) {
				attributes.removeIndex(i);
				disable(type);
				sorted = false;
			}
		}
	}
	
	/** @return True if this collection has the specified attribute, i.e. attributes.has(ColorAttribute.Diffuse);
	 *  Or when multiple attribute types are specified, true if this collection has all specified attributes,
	 *  i.e. attributes.has(out, ColorAttribute.Diffuse | ColorAttribute.Specular | TextureAttribute.Diffuse); */
	public final boolean has(final long type) {
		return type > 0 && (this.mask & type) == type;
	}
	
	/** @return the index of the attribute with the specified type or negative if not available. */
	protected int indexOf(final long type) {
		if (has(type))
			for (int i = 0; i < attributes.size; i++)
				if (attributes.get(i).type == type)
					return i;
		return -1;
	}
	
	/** Check if this collection has the same attributes as the other collection. 
	 * If compareValues is true, it also compares the values of each attribute.
	 * @param compareValues True to compare attribute values, false to only compare attribute types
	 * @return True if this collection contains the same attributes (and optionally attribute values) as the other. */
	public final boolean same(final Attributes other, boolean compareValues) {
		if (other == this)
			return true;
		if ((other == null) || (mask != other.mask))
			return false;
		if (!compareValues)
			return true;
		sort();
		other.sort();
		for (int i = 0; i < attributes.size; i++)
			if (!attributes.get(i).equals(other.attributes.get(i)))
				return false;
		return true;
	}
	
	/** See {@link #same(Attributes, boolean)}
	 * @return True if this collection contains the same attributes (but not values) as the other. */
	public final boolean same(final Attributes other) {
		return same(other, false);
	}
	
	/** Used for sorting attributes */
	@Override
	public final int compare (final Attribute arg0, final Attribute arg1) {
		return (int)(arg0.type - arg1.type);
	}
	
	/** Used for iterating through the attributes */
	@Override
	public final Iterator<Attribute> iterator () {
		return attributes.iterator();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2019.java