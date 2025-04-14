error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/286.java
text:
```scala
r@@eturn Boolean.valueOf(result).booleanValue();

package org.apache.lucene.xmlparser;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DOMUtils
{
    public static Element getChildByTagOrFail(Element e, String name)	throws ParserException
	{
		Element kid = getChildByTagName(e, name);
		if (null == kid)
		{
			throw new ParserException(e.getTagName() + " missing \"" + name
					+ "\" child element");
		}
		return kid;
	}
     
     public static Element getFirstChildOrFail(Element e) throws ParserException
	{
		Element kid = getFirstChildElement(e);
		if (null == kid)
		{
			throw new ParserException(e.getTagName()
					+ " does not contain a child element");
		}
		return kid;
	}
     
	public static String getAttributeOrFail(Element e, String name)	throws ParserException
	{
		String v = e.getAttribute(name);
		if (null == v)
		{
			throw new ParserException(e.getTagName() + " missing \"" + name
					+ "\" attribute");
		}
		return v;
	}
    public static String getAttributeWithInheritanceOrFail(Element e,	String name) throws ParserException
	{
		String v = getAttributeWithInheritance(e, name);
		if (null == v)
		{
			throw new ParserException(e.getTagName() + " missing \"" + name
					+ "\" attribute");
		}
		return v;
	}
    public static String getNonBlankTextOrFail(Element e) throws ParserException
	{
		String v = getText(e);
		if (null != v)
			v = v.trim();
		if (null == v || 0 == v.length())
		{
			throw new ParserException(e.getTagName() + " has no text");
		}
		return v;
	}
 
     
     
     
	
	
	/* Convenience method where there is only one child Element of a given name */
	public static Element getChildByTagName(Element e, String name)
	{
	       for (Node kid = e.getFirstChild(); kid != null; kid = kid.getNextSibling())
		{
			if( (kid.getNodeType()==Node.ELEMENT_NODE) && (name.equals(kid.getNodeName())) )
			{
				return (Element)kid;
			}
		}
		return null;
	}

	/**
	 * Returns an attribute value from this node, or first parent node with this attribute defined
	 * @param element 
	 * @param attributeName
	 * @return A non-zero-length value if defined, otherwise null
	 */
	public static String getAttributeWithInheritance(Element element, String attributeName)
	{
		String result=element.getAttribute(attributeName);
		if( (result==null)|| ("".equals(result) ) )
		{
			Node n=element.getParentNode();
			if((n==element)||(n==null))
			{
				return null;
			}
			if(n instanceof Element)
			{
				Element parent=(Element) n;
				return getAttributeWithInheritance(parent,attributeName);
			}
			return null; //we reached the top level of the document without finding attribute
		}
		return result;		
	}



	/* Convenience method where there is only one child Element of a given name */
	public static String getChildTextByTagName(Element e, String tagName)
	{
		Element child=getChildByTagName(e,tagName);
		if(child!=null)
		{
			return getText(child);
		}
		return null;
	}

	/* Convenience method to append a new child with text*/
	public static Element insertChild(Element parent, String tagName, String text)
	{
	  	Element child = parent.getOwnerDocument().createElement(tagName);
		parent.appendChild(child);
		if(text!=null)
		{
		  	child.appendChild(child.getOwnerDocument().createTextNode(text));
		}
		return child;
	}

	public static String getAttribute(Element element, String attributeName, String deflt)
	{
		String result=element.getAttribute(attributeName);
		if( (result==null)|| ("".equals(result) ) )
		{
			return deflt;
		}
		return result;
	}
	public static float getAttribute(Element element, String attributeName, float deflt)
	{
		String result=element.getAttribute(attributeName);
		if( (result==null)|| ("".equals(result) ) )
		{
			return deflt;
		}
		return Float.parseFloat(result);
	}	

	public static int getAttribute(Element element, String attributeName, int deflt)
	{
		String result=element.getAttribute(attributeName);
		if( (result==null)|| ("".equals(result) ) )
		{
			return deflt;
		}
		return Integer.parseInt(result);
	}
	
	public static boolean getAttribute(Element element, String attributeName,
			boolean deflt)
	{
		String result = element.getAttribute(attributeName);
		if ((result == null) || ("".equals(result)))
		{
			return deflt;
		}
		return Boolean.getBoolean(result);
	}	

	/* Returns text of node and all child nodes - without markup */
	//MH changed to Node from Element 25/11/2005
	public static String getText(Node e)
	{
		StringBuffer sb=new StringBuffer();
		getTextBuffer(e, sb);
		return sb.toString();
	}
	
	public static Element getFirstChildElement(Element element)
	{
		for (Node kid = element.getFirstChild(); kid != null; kid = kid
				.getNextSibling())
		{
			if (kid.getNodeType() == Node.ELEMENT_NODE) 
			{
				return (Element) kid;
			}
		}
		return null;
	}	

	private static void getTextBuffer(Node e, StringBuffer sb)
	{
	    for (Node kid = e.getFirstChild(); kid != null; kid = kid.getNextSibling())
		{
			switch(kid.getNodeType())
			{
				case Node.TEXT_NODE:
				{
					sb.append(kid.getNodeValue());
					break;
				}
				case Node.ELEMENT_NODE:
				{
					getTextBuffer(kid, sb);
					break;
				}
				case Node.ENTITY_REFERENCE_NODE:
				{
					getTextBuffer(kid, sb);
					break;
				}
			}
		}
	}

	/**
	* Helper method to parse an XML file into a DOM tree, given a reader.
	* @param is reader of the XML file to be parsed
	* @return an org.w3c.dom.Document object
	*/
	public static Document loadXML(Reader is)
	{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		
		try
		{
			db = dbf.newDocumentBuilder();
		}
		catch (Exception se)
		{
			throw new RuntimeException("Parser configuration error", se);
		}

		// Step 3: parse the input file
		org.w3c.dom.Document doc = null;
		try
		{
			doc = db.parse(new InputSource(is));
			//doc = db.parse(is);
		}
		catch (Exception se)
		{
			throw new RuntimeException("Error parsing file:" + se, se);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/286.java