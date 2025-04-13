error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4768.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4768.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4768.java
text:
```scala
S@@ystem.err.println("Unknown attribute: " + v);

/*
 * @(#)FigureAttributes.java
 *
 * Project:     JHotdraw - a GUI framework for technical drawings
 *              http://www.jhotdraw.org
 *              http://jhotdraw.sourceforge.net
 * Copyright:   © by the original author(s) and all contributors
 * License:     Lesser GNU Public License (LGPL)
 *              http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.figures;

import java.util.*;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;

import CH.ifa.draw.util.*;
import CH.ifa.draw.framework.*;

/**
 * A container for a figure's attributes. The attributes are stored
 * as key/value pairs.
 *
 * @see Figure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class   FigureAttributes
		extends Object
		implements Cloneable, Serializable {

	private Hashtable fMap;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -6886355144423666716L;
	private int figureAttributesSerializedDataVersion = 1;

	/**
	 * Constructs the FigureAttributes.
	 */
	public FigureAttributes() {
		fMap = new Hashtable();
	}

	/**
	 * Gets the attribute with the given name.
	 * @returns attribute or null if the key is not defined
	 */
	public Object get(String name) {
		return fMap.get(name);
	}

	/**
	 * Sets the attribute with the given name and
	 * overwrites its previous value.
	 */
	public void set(String name, Object value) {
		if (value != null) {
			fMap.put(name, value);
		}
		else {
			fMap.remove(name);
		}
	}

	/**
	 * Tests if an attribute is defined.
	 */
	public boolean hasDefined(String name) {
		return fMap.containsKey(name);
	}

	/**
	 * Clones the attributes.
	 */
   public Object clone() {
		try {
			FigureAttributes a = (FigureAttributes) super.clone();
			a.fMap = (Hashtable) fMap.clone();
			return a;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Reads the attributes from a StorableInput.
	 * FigureAttributes store the following types directly:
	 * Color, Boolean, String, Int. Other attribute types
	 * have to implement the Storable interface or they
	 * have to be wrapped by an object that implements Storable.
	 * @see Storable
	 * @see #write
	 */
	public void read(StorableInput dr) throws IOException {
		String s = dr.readString();
		if (!s.toLowerCase().equals("attributes")) {
			throw new IOException("Attributes expected");
		}

		fMap = new Hashtable();
		int size = dr.readInt();
		for (int i=0; i<size; i++) {
			String key = dr.readString();
			String valtype = dr.readString();
			Object val = null;
			if (valtype.equals("Color")) {
				val = new Color(dr.readInt(), dr.readInt(), dr.readInt());
			}
			else if (valtype.equals("Boolean")) {
				val = new Boolean(dr.readString());
			}
			else if (valtype.equals("String")) {
				val = dr.readString();
			}
			else if (valtype.equals("Int")) {
				val = new Integer(dr.readInt());
			}
			else if (valtype.equals("Storable")) {
				val = dr.readStorable();
			}
			else if (valtype.equals(Figure.POPUP_MENU)) {
				// read String but don't store it
				continue;
			}
			else if (valtype.equals("UNKNOWN")) {
				continue;
			}
			fMap.put(key,val);
		}
	}

	/**
	 * Writes the attributes to a StorableInput.
	 * FigureAttributes store the following types directly:
	 * Color, Boolean, String, Int. Other attribute types
	 * have to implement the Storable interface or they
	 * have to be wrapped by an object that implements Storable.
	 * @see Storable
	 * @see #write
	 */
	public void write(StorableOutput dw) {
		dw.writeString("attributes");

		dw.writeInt(fMap.size());   // number of attributes
		Enumeration k = fMap.keys();
		while (k.hasMoreElements()) {
			String s = (String) k.nextElement();
			Object v = fMap.get(s);

			dw.writeString(s);
			
			if (v instanceof String) {
				dw.writeString("String");
				dw.writeString((String) v);
			}
			else if (v instanceof Color) {
				writeColor(dw, "Color", (Color)v);
			}
			else if (v instanceof Boolean) {
				dw.writeString("Boolean");
				if (((Boolean)v).booleanValue()) {
					dw.writeString("TRUE");
				}
				else {
					dw.writeString("FALSE");
				}
			}
			else if (v instanceof Integer) {
				dw.writeString("Int");
				dw.writeInt(((Integer)v).intValue());
			}
			else if (v instanceof Storable) {
				dw.writeString("Storable");
				dw.writeStorable((Storable)v);
			}
			else if (v instanceof javax.swing.JPopupMenu) {
				dw.writeString(Figure.POPUP_MENU);
			}
			else {
				System.out.println("Unknown attribute: " + v);
				dw.writeString("UNKNOWN");
			}
		}
	}

	public static void writeColor(StorableOutput dw, String colorName, Color color) {
	   if (color != null) {
			dw.writeString(colorName);
			dw.writeInt(color.getRed());
			dw.writeInt(color.getGreen());
			dw.writeInt(color.getBlue());
		}
	}

	public static Color readColor(StorableInput dr) throws IOException {
		return new Color(dr.readInt(), dr.readInt(), dr.readInt());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4768.java