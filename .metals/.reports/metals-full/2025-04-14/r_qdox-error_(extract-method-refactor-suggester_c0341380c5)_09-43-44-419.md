error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1623.java
text:
```scala
L@@OG.severe("root element <toolbar> expected, but was "+toolBarElement.getName());

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
package org.columba.core.gui.toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JButton;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Create a toolbar from an xml file.
 * 
 * @author fdietz
 * 
 */
public class ToolBarXMLDecoder {

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.menu");

	private IExtensionHandler pluginHandler;

	private IFrameMediator mediator;

	public ToolBarXMLDecoder(IFrameMediator mediator) {
		super();

		this.mediator = mediator;

		try {
			pluginHandler = PluginManager
					.getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_ACTION);
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ExtendableToolBar createToolBar(InputStream is) {
		ExtendableToolBar toolBar = new ExtendableToolBar();

		extendToolBar(toolBar, is);

		return toolBar;
	}

	public void extendToolBar(ExtendableToolBar toolBar, InputStream is) {

		// add cancel button
		AbstractColumbaAction cancelAction = getAction("Cancel", mediator);
		JButton button = ToolBarButtonFactory.createButton(cancelAction);
		toolBar.add(button);

//		toolBar.add(Box.createHorizontalGlue());
//
//		// add busy animated icon
//		ImageSequenceTimer image = new ImageSequenceTimer();
//		toolBar.add(image);

		Document doc = retrieveDocument(is);

		Element toolBarElement = doc.getRootElement();
		if (toolBarElement.getName().equals("toolbar") == false) {
			LOG.severe("root element <toolbar> expected");
			return;
		}

		Iterator it = toolBarElement.getChildren().listIterator();
		while (it.hasNext()) {
			Element menuElement = (Element) it.next();
			if (menuElement.getName().equals("button")) {

				String actionId = menuElement.getAttributeValue("id");
				// deprecated config-file support
				if (actionId == null)
					actionId = menuElement.getAttributeValue("action");

				// deprecated config-file support
				// -> skip creation of "Cancel" button
				if (actionId.equals("Cancel"))
					continue;

				AbstractColumbaAction action = getAction(actionId, mediator);
				if ( action == null ) continue;
				
				toolBar.add(action);

			} else if (menuElement.getName().equals("separator")) {
				toolBar.addSeparator();
			} else
				LOG
						.severe("unkown element tag <" + menuElement.getName()
								+ ">");
		}

	}

	private AbstractColumbaAction getAction(String id,
			IFrameMediator frameMediator) {
		if (id == null)
			throw new IllegalArgumentException("id == null");
		if (frameMediator == null)
			throw new IllegalArgumentException("frameMediator == null");

		IExtension extension = pluginHandler.getExtension(id);

		AbstractColumbaAction a = null;

		try {
			if (extension != null)
				a = (AbstractColumbaAction) extension
						.instanciateExtension(new Object[] { frameMediator });
		} catch (PluginException e) {
			LOG.severe(e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();

		}

		return a;

	}

	/**
	 * @param xmlResource
	 * @return
	 */
	private Document retrieveDocument(InputStream is) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		try {
			doc = builder.build(is);
		} catch (JDOMException e) {
			LOG.severe(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOG.severe(e.getMessage());
			e.printStackTrace();
		}
		return doc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1623.java