error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17211.java
text:
```scala
p@@ublic final class Line implements Serializable

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.forminput;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Simple model object for FormInput example. Has a number of simple properties
 * that can be retrieved and set.
 */
public final class FormInputModel implements Serializable
{
	/**
	 * Represents a line of text. Hack to get around the fact that strings are
	 * immutable.
	 */
	public final class Line
	{
		private String text;

		/**
		 * Construct.
		 * 
		 * @param text
		 */
		public Line(String text)
		{
			this.text = text;
		}

		/**
		 * Gets text.
		 * 
		 * @return text
		 */
		public String getText()
		{
			return text;
		}

		/**
		 * Sets text.
		 * 
		 * @param text
		 *            text
		 */
		public void setText(String text)
		{
			this.text = text;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return text;
		}
	}

	private String stringProperty = "test";
	private Integer integerProperty = new Integer(100);
	private Double doubleProperty = new Double(20.5);
	private Date dateProperty = new Date();
	private Integer integerInRangeProperty = new Integer(50);
	private Boolean booleanProperty;
	private URL urlProperty;
	private String numberRadioChoice;
	private Set siteSelection = new HashSet();
	private List lines = new ArrayList();

	/**
	 * Construct.
	 */
	public FormInputModel()
	{
		try
		{
			urlProperty = new URL("http://wicket.sourceforge.net");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		lines.add(new Line("line one"));
		lines.add(new Line("line two"));
		lines.add(new Line("line three"));
	}

	/**
	 * Gets dateProperty.
	 * 
	 * @return dateProperty
	 */
	public Date getDateProperty()
	{
		return dateProperty;
	}

	/**
	 * Sets dateProperty.
	 * 
	 * @param dateProperty
	 *            dateProperty
	 */
	public void setDateProperty(Date dateProperty)
	{
		this.dateProperty = dateProperty;
	}

	/**
	 * Gets doubleProperty.
	 * 
	 * @return doubleProperty
	 */
	public Double getDoubleProperty()
	{
		return doubleProperty;
	}

	/**
	 * Sets doubleProperty.
	 * 
	 * @param doubleProperty
	 *            doubleProperty
	 */
	public void setDoubleProperty(Double doubleProperty)
	{
		this.doubleProperty = doubleProperty;
	}

	/**
	 * Gets integerProperty.
	 * 
	 * @return integerProperty
	 */
	public Integer getIntegerProperty()
	{
		return integerProperty;
	}

	/**
	 * Sets integerProperty.
	 * 
	 * @param integerProperty
	 *            integerProperty
	 */
	public void setIntegerProperty(Integer integerProperty)
	{
		this.integerProperty = integerProperty;
	}

	/**
	 * Gets stringProperty.
	 * 
	 * @return stringProperty
	 */
	public String getStringProperty()
	{
		return stringProperty;
	}

	/**
	 * Sets stringProperty.
	 * 
	 * @param stringProperty
	 *            stringProperty
	 */
	public void setStringProperty(String stringProperty)
	{
		this.stringProperty = stringProperty;
	}

	/**
	 * Gets integerInRangeProperty.
	 * 
	 * @return integerInRangeProperty
	 */
	public Integer getIntegerInRangeProperty()
	{
		return integerInRangeProperty;
	}

	/**
	 * Sets integerInRangeProperty.
	 * 
	 * @param integerInRangeProperty
	 *            integerInRangeProperty
	 */
	public void setIntegerInRangeProperty(Integer integerInRangeProperty)
	{
		this.integerInRangeProperty = integerInRangeProperty;
	}

	/**
	 * Gets the booleanProperty.
	 * @return booleanProperty
	 */
	public Boolean getBooleanProperty()
	{
		return booleanProperty;
	}
	
	/**
	 * Sets the booleanProperty.
	 * @param booleanProperty booleanProperty
	 */
	public void setBooleanProperty(Boolean booleanProperty)
	{
		this.booleanProperty = booleanProperty;
	}

	/**
	 * Gets the urlProperty.
	 * @return urlProperty
	 */
	public URL getUrlProperty()
	{
		return urlProperty;
	}

	/**
	 * Sets the urlProperty.
	 * @param urlProperty urlProperty
	 */
	public void setUrlProperty(URL urlProperty)
	{
		this.urlProperty = urlProperty;
	}

	/**
	 * Gets the favoriteColor.
	 * @return favoriteColor
	 */
	public String getNumberRadioChoice()
	{
		return numberRadioChoice;
	}

	/**
	 * Sets the favoriteColor.
	 * @param favoriteColor favoriteColor
	 */
	public void setNumberRadioChoice(String favoriteColor)
	{
		this.numberRadioChoice = favoriteColor;
	}

	/**
	 * Gets the selectedSites.
	 * @return selectedSites
	 */
	public Set getSiteSelection()
	{
		return siteSelection;
	}

	/**
	 * Sets the selectedSites.
	 * @param selectedSites selectedSites
	 */
	public void setSiteSelection(Set selectedSites)
	{
		this.siteSelection = selectedSites;
	}

	/**
	 * Gets lines.
	 * @return lines
	 */
	public List getLines()
	{
		return lines;
	}

	/**
	 * Sets lines.
	 * @param lines lines
	 */
	public void setLines(List lines)
	{
		this.lines = lines;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer b = new StringBuffer();
		b.append("[TestInputObject stringProperty = '").append(stringProperty)
		 .append("', integerProperty = ").append(integerProperty)
		 .append(", doubleProperty = ").append(doubleProperty)
		 .append(", dateProperty = ").append(dateProperty)
		 .append(", booleanProperty = ").append(booleanProperty)
		 .append(", integerInRangeProperty = ").append(integerInRangeProperty)
		 .append(", urlProperty = ").append(urlProperty)
		 .append(", numberRadioChoice = ").append(numberRadioChoice);
		b.append(",selected sites {");
		for(Iterator i = siteSelection.iterator(); i.hasNext();)
		{
			b.append(i.next());
			if(i.hasNext()) b.append(",");
		}
		b.append("}");
		b.append(",lines {");
		for(Iterator i = lines.iterator(); i.hasNext();)
		{
			b.append(i.next());
			if(i.hasNext()) b.append(",");
		}
		b.append("}");
		b.append("]");
		return b.toString();
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17211.java