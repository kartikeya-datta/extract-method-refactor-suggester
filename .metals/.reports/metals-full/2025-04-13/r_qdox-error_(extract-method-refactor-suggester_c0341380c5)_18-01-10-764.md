error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14145.java
text:
```scala
O@@bject item = i.next();

/*******************************************************************************
 * Copyright (c) 2005 - 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package org.eclipse.xtend.typesystem.xsd.util;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EmfFormatter {
	@SuppressWarnings("unchecked")
	public static String objToStr(Object obj, String ident) {
		String innerIdent = "  " + ident;
		if (obj instanceof EObject) {
			StringBuffer buf = new StringBuffer();
			EObject eobj = (EObject) obj;
			buf.append(eobj.eClass().getName() + " {\n");
			for (EStructuralFeature f : eobj.eClass()
					.getEAllStructuralFeatures()) {
				if (!eobj.eIsSet(f))
					continue;
				String fType = "unknown";
				String val = "???";
				if (f instanceof EReference) {
					EReference r = (EReference) f;
					if (r.isContainment()) {
						val = objToStr(eobj.eGet(f), innerIdent);
						fType = "cref";
					} else {
						val = refToStr(eobj, r);
						fType = "ref";
					}
				} else if (f instanceof EAttribute) {
					fType = "attr";
					// logger.debug(Msg.create("Path:").path(eobj));
					Object at = eobj.eGet(f);
					if (eobj != at)
						val = objToStr(at, innerIdent);
					else
						val = "<same as container object>";
				}
				String vType = f.getEType().getName();
				String name = f.getName();
				buf.append(innerIdent + fType + " " + vType + " " + name + " "
						+ val + "\n");
			}
			buf.append(ident + "}");
			return buf.toString();
		}
		if (obj instanceof Collection) {
			StringBuffer buf = new StringBuffer();
			buf.append("[\n");
			for (Object o : (Collection) obj)
				buf.append(innerIdent + objToStr(o, innerIdent) + "\n");
			buf.append(ident + "]");
			return buf.toString();
		}
		if (obj != null)
			return "'" + obj + "'";
		return "null";
	}

	@SuppressWarnings("unchecked")
	private static String refToStr(EObject obj, EReference ref) {
		StringBuffer buf = new StringBuffer();
		Object o = obj.eGet(ref);
		if (o instanceof EObject) {
			EObject eo = (EObject) o;

			if (eo instanceof ENamedElement)
				buf.append("'" + ((ENamedElement) eo).getName() + "' ");
			buf.append("ref:" + EcoreUtil.getURI(eo));
			return buf.toString();
		}
		if (o instanceof Collection) {
			buf.append("[");
			for (Iterator i = ((Collection) o).iterator(); i.hasNext();) {
				Object item = (Object) i.next();
				buf.append(EcoreUtil.getURI((EObject) item));
				if (i.hasNext())
					buf.append(", ");
			}
			buf.append("]");
			return buf.toString();
		}
		return "?????";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14145.java