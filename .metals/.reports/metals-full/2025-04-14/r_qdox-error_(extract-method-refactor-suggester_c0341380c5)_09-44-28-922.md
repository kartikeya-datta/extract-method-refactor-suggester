error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12280.java
text:
```scala
S@@tructureModelManager.getDefault().getInlineAnnotations(

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.aspectj.ajde.Ajde;
import org.aspectj.asm.LinkNode;
import org.aspectj.asm.ProgramElementNode;
import org.aspectj.asm.RelationNode;
import org.aspectj.asm.StructureModel;
import org.aspectj.asm.StructureModelManager;
import org.aspectj.asm.StructureNode;

/**
 * Prototype functionality for package view clients.
 */  
public class StructureModelUtil {

	/**
	 * This method returns a map from affected source lines in a class to
	 * a List of aspects affecting that line.
	 * Based on method of same name by mik kirsten. To be replaced when StructureModelUtil
	 * corrects its implementation
	 * 
	 * @param the full path of the source file to get a map for
	 * 
	 * @return a Map from line numbers to a List of ProgramElementNodes.
	 */
	public static Map getLinesToAspectMap(String sourceFilePath) {

		Map annotationsMap =
			StructureModelManager.INSTANCE.getInlineAnnotations(
				sourceFilePath,
				true,
				true);

		Map aspectMap = new HashMap();
		Set keys = annotationsMap.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			Object key = it.next();
			List annotations = (List) annotationsMap.get(key);
			for (Iterator it2 = annotations.iterator(); it2.hasNext();) {
				ProgramElementNode node = (ProgramElementNode) it2.next();

				List relations = node.getRelations();

				for (Iterator it3 = relations.iterator(); it3.hasNext();) {
					RelationNode relationNode = (RelationNode) it3.next();

					if (relationNode.getKind().equals("Advice")) {
						List children = relationNode.getChildren();

						List aspects = new Vector();

						for (Iterator it4 = children.iterator();
							it4.hasNext();
							) {
							Object object = it4.next();

							if (object instanceof LinkNode) {
								ProgramElementNode pNode =
									((LinkNode) object).getProgramElementNode();

								if (pNode.getProgramElementKind()
									== ProgramElementNode.Kind.ADVICE) {

									StructureNode theAspect = pNode.getParent();

									aspects.add(theAspect);

								}
							}
						}
						if (!aspects.isEmpty()) {
							aspectMap.put(key, aspects);
						}
					}

				}
			}
		}
		return aspectMap;
	}

	/**
	 * This method is copied from StructureModelUtil inoder for it to use the working
	 * version of getLineToAspectMap()
	 * 
	 * @return		the set of aspects with advice that affects the specified package
	 */
	public static Set getAspectsAffectingPackage(ProgramElementNode packageNode) {
		List files = StructureModelUtil.getFilesInPackage(packageNode);
		Set aspects = new HashSet();
		for (Iterator it = files.iterator(); it.hasNext();) {
			ProgramElementNode fileNode = (ProgramElementNode) it.next();
			Map adviceMap =
				getLinesToAspectMap(
					fileNode.getSourceLocation().getSourceFile().getAbsolutePath());
			Collection values = adviceMap.values();
			for (Iterator it2 = values.iterator(); it2.hasNext();) {
				aspects.add(it2.next());
			}
		}
		return aspects;
	}

	public static List getPackagesInModel() {
		List packages = new ArrayList();
		StructureModel model =
			Ajde.getDefault().getStructureModelManager().getStructureModel();
		if (model.equals(StructureModel.NO_STRUCTURE)) {
			return null;
		} else {
			return getPackagesHelper(
				(ProgramElementNode) model.getRoot(),
				ProgramElementNode.Kind.PACKAGE,
				null,
				packages);
		}
	}

	private static List getPackagesHelper(
		ProgramElementNode node,
		ProgramElementNode.Kind kind,
		String prename,
		List matches) {

		if (kind == null || node.getProgramElementKind().equals(kind)) {
			if (prename == null) {
				prename = new String(node.toString());
			} else {
				prename = new String(prename + "." + node);
			}
			Object[] o = new Object[2];
			o[0] = node;
			o[1] = prename;

			matches.add(o);
		}

		for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
			StructureNode nextNode = (StructureNode) it.next();
			if (nextNode instanceof ProgramElementNode) {
				getPackagesHelper(
					(ProgramElementNode) nextNode,
					kind,
					prename,
					matches);
			}
		}

		return matches;
	}

	/**
	 * Helper function sorts a list of resources into alphabetical order
	 */
	private List sortElements(List oldElements) {
		Object[] temp = oldElements.toArray();
		SortingComparator comparator = new SortingComparator();

		Arrays.sort(temp, comparator);

		List newResources = Arrays.asList(temp);

		return newResources;
	}

	private static List sortArray(List oldElements) {
		Object[] temp = oldElements.toArray();
		SortArrayComparator comparator = new SortArrayComparator();

		Arrays.sort(temp, comparator);
		
		List newElements = Arrays.asList(temp);

		return newElements;
	}

	private class SortingComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			ProgramElementNode p1 = (ProgramElementNode) o1;
			ProgramElementNode p2 = (ProgramElementNode) o2;

			String name1 = p1.getName();
			String name2 = p2.getName();

			return name1.compareTo(name2);
		}
	}

	private static class SortArrayComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			Object[] array1 = (Object[]) o1;
			Object[] array2 = (Object[]) o2;

			ProgramElementNode p1 = (ProgramElementNode) array1[1];
			ProgramElementNode p2 = (ProgramElementNode) array2[1];

			String name1 = p1.getName();
			String name2 = p2.getName();

			return name1.compareTo(name2);
		}
	}

	/**
	 * @return		all of the AspectJ and Java source files in a package
	 */ 
	public static List getFilesInPackage(ProgramElementNode packageNode) {
		List packageContents;
		if (packageNode == null) {
			return null;
		} else {
			packageContents = packageNode.getChildren();	
		}
		List files = new ArrayList();
		for (Iterator it = packageContents.iterator(); it.hasNext(); ) {
			ProgramElementNode packageItem = (ProgramElementNode)it.next();
			if (packageItem.getProgramElementKind() == ProgramElementNode.Kind.FILE_JAVA 
 packageItem.getProgramElementKind() == ProgramElementNode.Kind.FILE_ASPECTJ) {
				files.add(packageItem);
			}
		} 
		return files;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12280.java