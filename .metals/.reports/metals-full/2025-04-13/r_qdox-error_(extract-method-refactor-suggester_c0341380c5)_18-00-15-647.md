error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2665.java
text:
```scala
r@@eturn FORMAT_WRAP.equals(format);

package org.eclipse.ui.internal.dialogs;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.ui.internal.*;
import org.eclipse.core.runtime.*;
import org.xml.sax.*;
import org.apache.xerces.parsers.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;
/**
 * A parser for the the welcome page
 */
public class WelcomeParser extends DefaultHandler {
	private static final String TAG_WELCOME_PAGE = "welcomePage"; //$NON-NLS-1$	
	private static final String TAG_INTRO = "intro"; //$NON-NLS-1$	
	private static final String TAG_ITEM = "item"; //$NON-NLS-1$	
	private static final String TAG_BOLD = "b"; //$NON-NLS-1$	
	private static final String TAG_ACTION = "action"; //$NON-NLS-1$	
	private static final String TAG_PARAGRAPH = "p"; //$NON-NLS-1$	
	private static final String TAG_TOPIC = "topic"; //$NON-NLS-1$	
	private static final String ATT_TITLE = "title"; //$NON-NLS-1$	
	private static final String ATT_FORMAT = "format"; //$NON-NLS-1$	
	private static final String ATT_PLUGIN_ID = "pluginId"; //$NON-NLS-1$	
	private static final String ATT_CLASS = "class"; //$NON-NLS-1$	
	private static final String ATT_ID = "id"; //$NON-NLS-1$
	private static final String ATT_HREF = "href"; //$NON-NLS-1$
	
	private static final String FORMAT_WRAP = "wrap";
	private static final String FORMAT_NOWRAP = "nowrap";
	private static final char DELIMITER = '\n'; // sax parser replaces crlf with lf
	
	private SAXParser parser;

	private String title;
	private WelcomeItem introItem;
	private ArrayList items = new ArrayList();
	private String format;
	
	private class WelcomeContentHandler implements ContentHandler {
		protected ContentHandler parent;
		public void setParent(ContentHandler p) {
			parent = p;
		}
		public void characters(char[] ch, int start, int length) throws SAXException {
		}
		public void endDocument() throws SAXException {
		}
		public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		}
		public void endPrefixMapping(String prefix) throws SAXException {
		}
		public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		}
		public void processingInstruction(String target, String data) throws SAXException {
		}
		public void setDocumentLocator(Locator locator) {
		}
		public void skippedEntity(String name) throws SAXException {
		}
		public void startDocument() throws org.xml.sax.SAXException {
		}
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		}
		public void startPrefixMapping(String prefix, String uri) throws SAXException {
		}
	}

	private class WelcomePageHandler extends WelcomeContentHandler {
		public WelcomePageHandler(String newTitle) {
			title = newTitle;
		}
		public void startElement (String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
			if (localName.equals(TAG_INTRO)) {
				ItemHandler h = new IntroItemHandler();
				h.setParent(WelcomePageHandler.this);
				parser.setContentHandler(h);
			} else if (localName.equals(TAG_ITEM)) {
				ItemHandler h = new ItemHandler();
				h.setParent(WelcomePageHandler.this);
				parser.setContentHandler(h);
			}
		}
	}	

	private class ItemHandler extends WelcomeContentHandler {
		private ArrayList boldRanges = new ArrayList();
		protected ArrayList wrapRanges = new ArrayList();
		private ArrayList actionRanges = new ArrayList();
		private ArrayList pluginIds  = new ArrayList();
		private ArrayList classes  = new ArrayList();
		private ArrayList helpRanges  = new ArrayList();
		private ArrayList helpIds  = new ArrayList();
		private ArrayList helpHrefs  = new ArrayList();
		private StringBuffer text = new StringBuffer();
		protected int offset = 0;
		protected int textStart;
		protected int wrapStart;

		private class BoldHandler extends WelcomeContentHandler {
			public void characters(char[] ch, int start, int length) throws SAXException {
				ItemHandler.this.characters(ch, start, length);
			}
			public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
				if (localName.equals(TAG_BOLD)) {
					boldRanges.add(new int[] {textStart, offset - textStart});
					parser.setContentHandler(parent);
				}
			}
		}
		private class ActionHandler extends WelcomeContentHandler {
			public ActionHandler(String pluginId, String className) {
				pluginIds.add(pluginId);
				classes.add(className);
			}
			public void characters(char[] ch, int start, int length) throws SAXException {
				ItemHandler.this.characters(ch, start, length);
			}
			public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
				if (localName.equals(TAG_ACTION)) {
					actionRanges.add(new int[] {textStart, offset - textStart});
					parser.setContentHandler(parent);
				}
			}
		}	
		private class TopicHandler extends WelcomeContentHandler {
			public TopicHandler(String helpId, String href) {
				helpIds.add(helpId);
				helpHrefs.add(href);
			}
			public void characters(char[] ch, int start, int length) throws SAXException {
				ItemHandler.this.characters(ch, start, length);
			}
			public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
				if (localName.equals(TAG_TOPIC)) {
					helpRanges.add(new int[] {textStart, offset - textStart});
					parser.setContentHandler(parent);
				}
			}
		}	
		
		protected WelcomeItem constructWelcomeItem() {
			if (isFormatWrapped()) {
				// replace all line delimiters with a space
				for (int i=0; i<wrapRanges.size(); i++) {
					int[] range = (int[])wrapRanges.get(i);
					int start = range[0];
					int length = range[1];
					for (int j=start; j<start+length; j++) {
						char ch = text.charAt(j);
						if (ch == DELIMITER) {
							text.replace(j,j+1," ");
						} 
					}
				}
			}
			return new WelcomeItem(
				text.toString(), 
				(int[][])boldRanges.toArray(new int[boldRanges.size()][2]),
				(int[][])actionRanges.toArray(new int[actionRanges.size()][2]),
				(String[])pluginIds.toArray(new String[pluginIds.size()]),
				(String[])classes.toArray(new String[classes.size()]),
				(int[][])helpRanges.toArray(new int[helpRanges.size()][2]),
				(String[])helpIds.toArray(new String[helpIds.size()]),
				(String[])helpHrefs.toArray(new String[helpHrefs.size()]));
		}
		public void characters(char[] ch, int start, int length) throws SAXException {
			for (int i = 0; i < length; i++) {
				text.append(ch[start + i]);
			}
			offset += length;
		}

		public void startElement (String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
			textStart = offset;
			if (localName.equals(TAG_BOLD)) {
				BoldHandler h = new BoldHandler();
				h.setParent(ItemHandler.this);
				parser.setContentHandler(h);
			} else if(localName.equals(TAG_ACTION)) {
				ActionHandler h = new ActionHandler(atts.getValue(ATT_PLUGIN_ID), atts.getValue(ATT_CLASS));
				h.setParent(ItemHandler.this);
				parser.setContentHandler(h);
			} else if(localName.equals(TAG_PARAGRAPH)) {
				wrapStart = textStart;
			} else if(localName.equals(TAG_TOPIC)) {
				TopicHandler h = new TopicHandler(atts.getValue(ATT_ID), atts.getValue(ATT_HREF));
				h.setParent(ItemHandler.this);
				parser.setContentHandler(h);
			}
		}
		public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
			if (localName.equals(TAG_ITEM)) {
				items.add(constructWelcomeItem());
				parser.setContentHandler(parent);
			} else if (localName.equals(TAG_PARAGRAPH)) {
				wrapRanges.add(new int[] {wrapStart, offset - wrapStart});
			}				
		}
	}	
	private class IntroItemHandler extends ItemHandler {
		public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
			if (localName.equals(TAG_INTRO)) {
				introItem = constructWelcomeItem();
				parser.setContentHandler(parent);
			} else if (localName.equals(TAG_PARAGRAPH)) {
				wrapRanges.add(new int[] {wrapStart, offset - wrapStart});
			}					
		}
	}
/**
 * Creates a new welcome parser.
 */
public WelcomeParser() {
	super();
	parser = new SAXParser();
	parser.setContentHandler(this);
	parser.setDTDHandler(this);
	parser.setEntityResolver(this);
	parser.setErrorHandler(this);
}
/**
 * Returns the intro item.
 */
public WelcomeItem getIntroItem() {
	return introItem;
}
/**
 * Returns the items.
 */
public WelcomeItem[] getItems() {
	return (WelcomeItem[])items.toArray(new WelcomeItem[items.size()]);
}
/**
 * Returns the title
 */
public String getTitle() {
	return title;
}
/**
 * Returns whether or not the welcome editor input should be wrapped.
 */
public boolean isFormatWrapped() {
	return format.equals(FORMAT_WRAP);
}
/**
 * Parse the contents of the input stream
 */
public void parse(InputStream is) {
	try {
		parser.parse(new InputSource(is));
	} catch (SAXException e) {
		IStatus status = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, 1, WorkbenchMessages.getString("WelcomeParser.parseException"), e);  //$NON-NLS-1$	
		WorkbenchPlugin.log(WorkbenchMessages.getString("WelcomeParser.parseError"), status);  //$NON-NLS-1$	
	} catch (IOException e) {
		IStatus status = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, 1, WorkbenchMessages.getString("WelcomeParser.parseException"), e); //$NON-NLS-1$	
		WorkbenchPlugin.log(WorkbenchMessages.getString("WelcomeParser.parseError"), status);  //$NON-NLS-1$	
	}
}
/**
 * Handles the start element
 */
public void startElement (String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
	if (localName.equals(TAG_WELCOME_PAGE)) {
		WelcomeContentHandler h = new WelcomePageHandler(atts.getValue(ATT_TITLE));
		format = atts.getValue(ATT_FORMAT);
		h.setParent(this);
		parser.setContentHandler(h);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2665.java