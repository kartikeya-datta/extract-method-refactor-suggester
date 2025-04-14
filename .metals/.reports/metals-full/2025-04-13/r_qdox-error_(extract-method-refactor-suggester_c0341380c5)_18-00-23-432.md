error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9434.java
text:
```scala
t@@his(2, absoluteBoundingRectangle2D);

/*
 * @(#)QuadTree.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */


package CH.ifa.draw.standard;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.io.Serializable;

/**
 * @author: WMG (INIT Copyright (C) 2000 All rights reserved)
 * @version <$CURRENT_VERSION$>
 */
class QuadTree implements Serializable {

	//_________________________________________________________VARIABLES

	private Rectangle2D  _absoluteBoundingRectangle2D = new Rectangle2D.Double();
	private int          _nMaxTreeDepth;
	private Hashtable    _theHashtable = new Hashtable();
	private Hashtable    _outsideHashtable = new Hashtable();
	private QuadTree     _nwQuadTree;
	private QuadTree     _neQuadTree;
	private QuadTree     _swQuadTree;
	private QuadTree     _seQuadTree;

	//______________________________________________________CONSTRUCTORS

	public QuadTree(Rectangle2D absoluteBoundingRectangle2D) {
		this(6, absoluteBoundingRectangle2D);
	}

	public QuadTree(int nMaxTreeDepth, Rectangle2D
		absoluteBoundingRectangle2D) {
		_init(nMaxTreeDepth, absoluteBoundingRectangle2D);
	}

	private QuadTree() {
	}

	//____________________________________________________PUBLIC METHODS

	public void add(Object anObject, Rectangle2D absoluteBoundingRectangle2D) {
		if (_nMaxTreeDepth == 1) {
			if (absoluteBoundingRectangle2D.intersects(_absoluteBoundingRectangle2D)) {
				_theHashtable.put(anObject, absoluteBoundingRectangle2D);
			}
			else {
				_outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
			}
			return;
		}

		boolean bNW = absoluteBoundingRectangle2D.intersects(
			_nwQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bNE = absoluteBoundingRectangle2D.intersects(
			_neQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bSW = absoluteBoundingRectangle2D.intersects(
			_swQuadTree.getAbsoluteBoundingRectangle2D());

		boolean bSE = absoluteBoundingRectangle2D.intersects(
			_seQuadTree.getAbsoluteBoundingRectangle2D());

		int nCount = 0;

		if (bNW == true) {
			nCount++;
		}
		if (bNE == true) {
			nCount++;
		}
		if (bSW == true) {
			nCount++;
		}
		if (bSE == true) {
			nCount++;
		}

		if (nCount > 1) {
			_theHashtable.put(anObject, absoluteBoundingRectangle2D);
			return;
		}
		if (nCount == 0) {
			_outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
			return;
		}

		if (bNW == true) {
			_nwQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bNE == true) {
			_neQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bSW == true) {
			_swQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
		if (bSE == true) {
			_seQuadTree.add(anObject, absoluteBoundingRectangle2D);
		}
	}

	public Object remove(Object anObject) {
		Object returnObject = _theHashtable.remove(anObject);
		if (returnObject != null) {
			return returnObject;
		}

		if (_nMaxTreeDepth > 1) {
			returnObject = _nwQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _neQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _swQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}

			returnObject = _seQuadTree.remove(anObject);
			if (returnObject != null) {
				return returnObject;
			}
		}

		returnObject = _outsideHashtable.remove(anObject);
		if (returnObject != null) {
			return returnObject;
		}

		return null;
	}


	public void clear() {
		_theHashtable.clear();
		_outsideHashtable.clear();
		if (_nMaxTreeDepth > 1) {
			_nwQuadTree.clear();
			_neQuadTree.clear();
			_swQuadTree.clear();
			_seQuadTree.clear();
		}
	}

	public int getMaxTreeDepth() {
		return _nMaxTreeDepth;
	}

	public Vector getAll() {
		Vector v = new Vector();
		v.addAll(_theHashtable.keySet());
		v.addAll(_outsideHashtable.keySet());

		if (_nMaxTreeDepth > 1) {
			v.addAll(_nwQuadTree.getAll());
			v.addAll(_neQuadTree.getAll());
			v.addAll(_swQuadTree.getAll());
			v.addAll(_seQuadTree.getAll());
		}

		return v;
	}

	public Vector getAllWithin(Rectangle2D r) {
		Vector v = new Vector();
		for (Iterator ii = _outsideHashtable.keySet().iterator(); ii.hasNext(); ) {
			Object anObject = ii.next();
			Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
			_outsideHashtable.get(anObject);

			if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
				v.addElement(anObject);
			}
		}

		if (_absoluteBoundingRectangle2D.intersects(r)) {
			for(Iterator i = _theHashtable.keySet().iterator(); i.hasNext(); ) {
				Object anObject = i.next();
				Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
				_theHashtable.get(anObject);

				if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
					v.addElement(anObject);
				}
			}

			if (_nMaxTreeDepth > 1) {
				v.addAll(_nwQuadTree.getAllWithin(r));
				v.addAll(_neQuadTree.getAllWithin(r));
				v.addAll(_swQuadTree.getAllWithin(r));
				v.addAll(_seQuadTree.getAllWithin(r));
			}
		}

		return v;
	}

	public Rectangle2D getAbsoluteBoundingRectangle2D() {
		return _absoluteBoundingRectangle2D;
	}

	//___________________________________________________PRIVATE METHODS

	private void _init(int nMaxTreeDepth, Rectangle2D absoluteBoundingRectangle2D) {
		_absoluteBoundingRectangle2D.setRect(absoluteBoundingRectangle2D);
		_nMaxTreeDepth = nMaxTreeDepth;

		if (_nMaxTreeDepth > 1) {
			_nwQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeNorthwest(absoluteBoundingRectangle2D));
			_neQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeNortheast(absoluteBoundingRectangle2D));
			_swQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeSouthwest(absoluteBoundingRectangle2D));
			_seQuadTree = new QuadTree(_nMaxTreeDepth-1,
			_makeSoutheast(absoluteBoundingRectangle2D));
		}
	}

	private Rectangle2D _makeNorthwest(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeNortheast(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
			r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeSouthwest(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX(), r.getY() + r.getHeight() / 2.0,
			r.getWidth() / 2.0, r.getHeight() / 2.0);
	}

	private Rectangle2D _makeSoutheast(Rectangle2D r) {
		return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
			r.getY() + r.getHeight() / 2.0, r.getWidth() / 2.0,
			r.getHeight() / 2.0);
	}

//_______________________________________________________________END

} //end of class QuadTree
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9434.java