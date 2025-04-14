error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18075.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18075.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18075.java
text:
```scala
private G@@ranularity granularity = StructureViewProperties.Granularity.DECLARED_ELEMENTS;

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

import java.io.ObjectStreamException;
import java.util.*;

import org.aspectj.asm.*;

/** 
 * Nested properties use the typesafe enum pattern.
 * 
 * @author Mik Kersten
 */
public class StructureViewProperties {
	
	/**
	 * @deprecated
	 */
	public static final String SORT_DECLARATIONAL = StructureViewProperties.Sorting.DECLARATIONAL.toString();
	
	/**
	 * @deprecated
	 */ 
	public void setSorting(String sorting) { }
	
    private List relations = new ArrayList();
    private List filteredMemberAccessibility = new ArrayList();
    private List filteredMemberModifiers = new ArrayList();
    private List filteredMemberKinds = new ArrayList();
    private List grouping = new ArrayList();
    private Sorting sorting = Sorting.DECLARATIONAL;
	private Granularity granularity = Granularity.MEMBER;

    public List getRelations() {
        return relations;
    }
    
    public void setRelations(List relations) {
    	this.relations = relations;	
    }

    public void addRelation(IRelationship.Kind kind) {
        relations.add(kind);
    }

    public void removeRelation(IRelationship.Kind kind) {
        relations.remove(kind);
    }

    public void setFilteredMemberAccessibility(List memberVisibility) {
        this.filteredMemberAccessibility = memberVisibility;
    }

    public List getFilteredMemberAccessibility() {
        return filteredMemberAccessibility;
    }

    public void addFilteredMemberAccessibility(IProgramElement.Accessibility accessibility) {
 	   	this.filteredMemberAccessibility.add(accessibility);
	}
	
	public void removeFilteredMemberAccessibility(IProgramElement.Accessibility accessibility) {
		this.filteredMemberAccessibility.remove(accessibility);	
	}

    public List getFilteredMemberModifiers() {
        return filteredMemberModifiers;
    }

    public void setFilteredMemberModifiers(List memberModifiers) {
        this.filteredMemberModifiers = memberModifiers;
    }
    
    public void addFilteredMemberModifiers(IProgramElement.Modifiers modifiers) {
 	   	this.filteredMemberModifiers.add(modifiers);
	}
	
	public void removeFilteredMemberModifiers(IProgramElement.Modifiers modifiers) {
		this.filteredMemberModifiers.remove(modifiers);	
	}
    
    public StructureViewProperties.Sorting getSorting() {
        return sorting;
    }

    public void setSorting(StructureViewProperties.Sorting sorting) {
        this.sorting = sorting;
    }
    
	public List getFilteredMemberKinds() {
		return filteredMemberKinds;
	}

	public void setFilteredMemberKinds(List memberKinds) {
		this.filteredMemberKinds = memberKinds;
	}
	
    public void addFilteredMemberKind(IProgramElement.Kind kind) {
 	   	this.filteredMemberKinds.add(kind);
	}
	
	public void removeFilteredMemberKind(IProgramElement.Kind kind) {
		this.filteredMemberKinds.remove(kind);	
	}
	
	public List getGrouping() {
		return grouping;
	}

	public void setGrouping(List grouping) {
		this.grouping = grouping;
	}
	
	
	public void addGrouping(Grouping grouping) {
		this.grouping.add(grouping);
	}

	public void removeGrouping(Grouping grouping) {
		this.grouping.remove(grouping);
	}

	public Granularity getGranularity() {
		return granularity;
	}

	public void setGranularity(Granularity granularity) {
		this.granularity = granularity;
	}
	
	public String getName() {
		return "<unnamed view>";
	}
	
	public String toString() {
		return "\nView Properties:"
			+ "\n-> sorting: " + sorting	
			+ "\n-> grouping: " + grouping
			+ "\n-> filtered member kinds: " + filteredMemberKinds
			+ "\n-> filtered member accessibility: " + filteredMemberAccessibility
			+ "\n-> filtered member modifiers: " + filteredMemberModifiers
			+ "\n-> relations: " + relations;
	}
	
	public static class Hierarchy {
		
		public static final Hierarchy DECLARATION = new Hierarchy("package hierarchy");
		public static final Hierarchy CROSSCUTTING = new Hierarchy("crosscutting structure");
		public static final Hierarchy INHERITANCE = new Hierarchy("type hierarchy");
		public static final Hierarchy[] ALL = { DECLARATION, CROSSCUTTING, INHERITANCE };
		  
		private final String name;
		
		private Hierarchy(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}	

		// The 4 declarations below are necessary for serialization
		private static int nextOrdinal = 0;
		private final int ordinal = nextOrdinal++;
		private Object readResolve() throws ObjectStreamException {
			return ALL[ordinal];
		}
	}
	
	public static class Grouping {
		
		public static final Grouping KIND = new Grouping("group by kind");
		public static final Grouping VISIBILITY = new Grouping("group by visibility");
		public static final Grouping[] ALL = { KIND, VISIBILITY };
		
		private final String name;
		
		private Grouping(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}	

		// The 4 declarations below are necessary for serialization
		private static int nextOrdinal = 0;
		private final int ordinal = nextOrdinal++;
		private Object readResolve() throws ObjectStreamException {
			return ALL[ordinal];
		}
	}
 
	public static class Sorting {
	
 		public static final Sorting ALPHABETICAL = new Sorting("sort alphabetically");
 		public static final Sorting DECLARATIONAL = new Sorting("sort declarationally");
 		public static final Sorting[] ALL = { ALPHABETICAL, DECLARATIONAL };
		
		private final String name;
		
		private Sorting(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}	

		// The 4 declarations below are necessary for serialization
		private static int nextOrdinal = 0;
		private final int ordinal = nextOrdinal++;
		private Object readResolve() throws ObjectStreamException {
			return ALL[ordinal];
		}
	}
	
	public static class Granularity {
	
		public static final Granularity PACKAGE = new Granularity("package");
		public static final Granularity FILE = new Granularity("file");
		public static final Granularity TYPE = new Granularity("type");
		public static final Granularity MEMBER = new Granularity("member");
		public static final Granularity DECLARED_ELEMENTS = new Granularity("declared body elements");
		public static final Granularity[] ALL = { PACKAGE, FILE, TYPE, MEMBER, DECLARED_ELEMENTS };
		
		private final String name;
		
		private Granularity(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}	

		// The 4 declarations below are necessary for serialization
		private static int nextOrdinal = 0;
		private final int ordinal = nextOrdinal++;
		private Object readResolve() throws ObjectStreamException {
			return ALL[ordinal];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18075.java