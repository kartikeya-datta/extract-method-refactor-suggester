error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1571.java
text:
```scala
s@@uper("org.columba.core.action", "org/columba/core/action/action.xml");

//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.core.action;

import java.util.ListIterator;

import org.columba.core.gui.FrameController;
import org.columba.core.io.DiskIO;
import org.columba.core.plugin.AbstractPluginHandler;
import org.columba.core.xml.XmlElement;
import org.columba.core.xml.XmlIO;

public class ActionPluginHandler extends AbstractPluginHandler{
	
	protected XmlElement parentNode;

	/**
	 * @see org.columba.core.plugin.AbstractPluginHandler#getNames()
	 */
	public String[] getPluginIdList() {
		int count = parentNode.count();

		String[] list = new String[count];

		for (int i = 0; i < count; i++) {
			XmlElement action = parentNode.getElement(i);
			String s = action.getAttribute("name");

			list[i] = s;

		}

		return list;
	}

	/**
		 * @see org.columba.core.plugin.AbstractPluginHandler#getPluginClassName(java.lang.String, java.lang.String)
		 */
	protected String getPluginClassName(String name, String id) {

		int count = parentNode.count();

		for (int i = 0; i < count; i++) {

			XmlElement action = parentNode.getElement(i);
			String s = action.getAttribute("name");

			if (name.equalsIgnoreCase(s))
				return action.getAttribute(id);

		}

		return null;
	}

	public Object getPlugin(String name, Object[] args) throws Exception {
		String className = getPluginClassName(name, "class");
		return getPlugin(name, className, args);
	}

	public Class getPluginClass(String name) {
		String className = getPluginClassName(name, "class");
		
		try {

			Class clazz = Class.forName(className);
			return clazz;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ActionPluginHandler() {
		super("action", "org/columba/core/action/action.xml");

		parentNode = getConfig().getRoot().getElement("actionlist");
	}

	
	public BasicAction getAction( String name, FrameController controller ) throws Exception {
		return (BasicAction) getPlugin(name, new Object[] { controller } );
	}

	public IMenu getIMenu( String name, FrameController controller ) throws Exception {
		return (IMenu) getPlugin(name, new Object[] { controller } );
	}

	public void addActionList(String actionXml) {		
		XmlIO actionXmlIO = new XmlIO();
		actionXmlIO.setURL(DiskIO.getResourceURL(actionXml));
		actionXmlIO.load();

		XmlElement actionlist = actionXmlIO.getRoot().getElement("actionlist");
		
		for( int i=0; i<actionlist.count(); i++) {
			parentNode.addElement(actionlist.getElement(i));
		}
	}

	/* (non-Javadoc)
	 * @see org.columba.core.plugin.AbstractPluginHandler#addPlugin(java.lang.String, java.io.File, org.columba.core.xml.XmlElement)
	 */
	/*
	public void addPlugin(String name, File pluginFolder, XmlElement element) {
		XmlElement extension;

		for( int i=0; i<element.count(); i++) {
			extension = element.getElement(i);
			extension.addAttribute("name",name+"$"+extension.getAttribute("name"));
			
			super.addPlugin(extension.getAttribute("name"), pluginFolder, element);
			parentNode.addElement(extension);
		}
	}
*/
	/* (non-Javadoc)
	 * @see org.columba.core.plugin.AbstractPluginHandler#addExtension(org.columba.core.xml.XmlElement)
	 */
	public void addExtension(String id, XmlElement extension) {
		ListIterator iterator = extension.getElements().listIterator();
		XmlElement action;
		while( iterator.hasNext() ) {
			action = (XmlElement) iterator.next();
			action.addAttribute("name", id + '$' + action.getAttribute("name"));
			parentNode.addElement(action);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1571.java