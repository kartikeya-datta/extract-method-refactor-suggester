error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[399,2]

error in qdox parser
file content:
```java
offset: 9467
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5726.java
text:
```scala
package wicket.extensions.markup.html.datepicker;

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.customcomponents;

import java.io.Serializable;
import java.util.Locale;

import wicket.Session;

/**
 * The properties of the date picker component.
 *
 * @author Eelco Hillenius
 */
public class DatePickerProperties implements Serializable
{
	/**
	 * The format string that will be used to enter the date in the input field. This
	 * format will be honored even if the input field is hidden.
	 */
	private String ifFormat = null;

	/**
	 * Wether the calendar is in ``single-click mode'' or ``double-click mode''. If
	 * true (the default) the calendar will be created in single-click mode.
	 */
	private boolean mode = true;

	/**
	 * Specifies which day is to be displayed as the first day of week. Possible
	 * values are 0 to 6; 0 means Sunday, 1 means Monday, ..., 6 means Saturday. The
	 * end user can easily change this too, by clicking on the day name in the
	 * calendar header.
	 */
	private int firstDay = 0;

	/**
	 * If ``true'' then the calendar will display week numbers.
	 */
	private boolean weekNumbers = true;

	/**
	 * Alignment of the calendar, relative to the reference element. The reference
	 * element is dynamically chosen like this: if a displayArea is specified then it
	 * will be the reference element. Otherwise, the input field is the reference
	 * element.
	 * <p>
	 * Align may contain one or two characters. The first character dictates the
	 * vertical alignment, relative to the element, and the second character dictates
	 * the horizontal alignment. If the second character is missing it will be assumed
	 * "l" (the left margin of the calendar will be at the same horizontal position as
	 * the left margin of the element). The characters given for the align parameters
	 * are case sensitive. This function only makes sense when the calendar is in
	 * popup mode. After computing the position it uses Calendar.showAt to display the
	 * calendar there.
	 * </p>
	 * <p>
	 * <strong>Vertical alignment</strong> The first character in ``align'' can take
	 * one of the following values:
	 * <ul>
	 * <li>T -- completely above the reference element (bottom margin of the calendar
	 * aligned to the top margin of the element). </li>
	 * <li>t -- above the element but may overlap it (bottom margin of the calendar
	 * aligned to the bottom margin of the element). </li>
	 * <li>c -- the calendar displays vertically centered to the reference element.
	 * It might overlap it (that depends on the horizontal alignment). </li>
	 * <li>b -- below the element but may overlap it (top margin of the calendar
	 * aligned to the top margin of the element). </li>
	 * <li>B -- completely below the element (top margin of the calendar aligned to
	 * the bottom margin of the element). </li>
	 * </ul>
	 * </p>
	 * <p>
	 * <strong>Horizontal alignment</strong> The second character in ``align'' can
	 * take one of the following values:
	 * <ul>
	 * <li>L -- completely to the left of the reference element (right margin of the
	 * calendar aligned to the left margin of the element). </li>
	 * <li>l -- to the left of the element but may overlap it (left margin of the
	 * calendar aligned to the left margin of the element). </li>
	 * <li>c -- horizontally centered to the element. Might overlap it, depending on
	 * the vertical alignment. </li>
	 * <li>r -- to the right of the element but may overlap it (right margin of the
	 * calendar aligned to the right margin of the element). </li>
	 * <li>R -- completely to the right of the element (left margin of the calendar
	 * aligned to the right margin of the element). </li>
	 * </ul>
	 * </p>
	 */
	private String align = null;

	/**
	 * If this is set to true then the calendar will also allow time selection.
	 */
	private boolean showsTime = false;

	/**
	 * Set this to ``12'' or ``24'' to configure the way that the calendar will
	 * display time.
	 */
	private String timeFormat = null;

	/**
	 * Set this to ``false'' if you want the calendar to update the field only when
	 * closed (by default it updates the field at each date change, even if the
	 * calendar is not closed).
	 */
	private boolean electric = true;

	/**
	 * If set to ``true'' then days belonging to months overlapping with the currently
	 * displayed month will also be displayed in the calendar (but in a ``faded-out''
	 * color).
	 */
	private boolean showOthers = false;

	/**
	 * Construct.
	 */
	public DatePickerProperties()
	{
	}

	/**
	 * Return the properties as a script.
	 * @return the properties as a script
	 */
	public String toScript()
	{
		StringBuffer b = new StringBuffer();
		// create the script that represents these properties. Only create entries for
		// values that are different from the default value (save a bit bandwith)

		if (!isMode())
		{
			b.append("\n\tmode : false,");
		}

		if (getFirstDay() != 0)
		{
			b.append("\n\tfistDay : ").append(getFirstDay()).append(",");
		}

		if (!isWeekNumbers())
		{
			b.append("\n\tweekNumbers : false,");
		}

		if (getAlign() != null)
		{
			b.append("\n\talign : ").append(getAlign()).append(",");
		}

		if (isShowsTime())
		{
			b.append("\n\tshowsTime : true,");
		}

		if (getTimeFormat() != null)
		{
			b.append("\n\timeFormat : ").append(getTimeFormat()).append(",");
		}

		if (!isElectric())
		{
			b.append("\n\telectric : false,");
		}

		if (isShowOthers())
		{
			b.append("\n\tshowOthers : true,");
		}

		// append date format
		String ifFormat = getIfFormat();
		// if null, try some heuristics
		if (ifFormat == null)
		{
			// get the short date format object for the current locale
			ifFormat = getDatePattern();
		}
		b.append("\n\t\tifFormat : \"").append(ifFormat).append("\"");
		
		return b.toString();
	}

	/**
	 * When property ifFormat is not set, this method is called to get the date pattern.
	 * This method gets the current locale, and returns the pattern based on that.
	 * <p>
	 * This locale/pattern map is by far complete. Override this method or set
	 * ifFormat if this doesnt work for you. Any contributions are welcome.
	 * </p>
	 * @return the pattern
	 */
	protected String getDatePattern()
	{
		// TODO this is a very shallow implementation; see if there is anything smarter
		// to do with the date pattern

		Locale locale = Session.get().getLocale();

		// now, just try a few that I know of

		if (Locale.GERMAN.equals(locale))
		{
			return "%d.%m.%Y";
		}

		if ("nl".equals(locale.getLanguage()))
		{
			return "%d-%m-%Y";
		}

		// return US pattern by default
		return "%Y/%m/%d";		
	}

	/**
	 * Gets the align.
	 * @return align
	 */
	public String getAlign()
	{
		return align;
	}

	/**
	 * Sets the align.
	 * @param align align
	 */
	public void setAlign(String align)
	{
		this.align = align;
	}

	/**
	 * Gets the electric.
	 * @return electric
	 */
	public boolean isElectric()
	{
		return electric;
	}

	/**
	 * Sets the electric.
	 * @param electric electric
	 */
	public void setElectric(boolean electric)
	{
		this.electric = electric;
	}

	/**
	 * Gets the firstDay.
	 * @return firstDay
	 */
	public int getFirstDay()
	{
		return firstDay;
	}

	/**
	 * Sets the firstDay.
	 * @param firstDay firstDay
	 */
	public void setFirstDay(int firstDay)
	{
		this.firstDay = firstDay;
	}

	/**
	 * Gets the ifFormat.
	 * @return ifFormat
	 */
	public String getIfFormat()
	{
		return ifFormat;
	}

	/**
	 * Sets the ifFormat.
	 * @param ifFormat ifFormat
	 */
	public void setIfFormat(String ifFormat)
	{
		this.ifFormat = ifFormat;
	}

	/**
	 * Gets the mode.
	 * @return mode
	 */
	public boolean isMode()
	{
		return mode;
	}

	/**
	 * Sets the mode.
	 * @param mode mode
	 */
	public void setMode(boolean mode)
	{
		this.mode = mode;
	}

	/**
	 * Gets the showOthers.
	 * @return showOthers
	 */
	public boolean isShowOthers()
	{
		return showOthers;
	}

	/**
	 * Sets the showOthers.
	 * @param showOthers showOthers
	 */
	public void setShowOthers(boolean showOthers)
	{
		this.showOthers = showOthers;
	}

	/**
	 * Gets the showsTime.
	 * @return showsTime
	 */
	public boolean isShowsTime()
	{
		return showsTime;
	}

	/**
	 * Sets the showsTime.
	 * @param showsTime showsTime
	 */
	public void setShowsTime(boolean showsTime)
	{
		this.showsTime = showsTime;
	}

	/**
	 * Gets the timeFormat.
	 * @return timeFormat
	 */
	public String getTimeFormat()
	{
		return timeFormat;
	}

	/**
	 * Sets the timeFormat.
	 * @param timeFormat timeFormat
	 */
	public void setTimeFormat(String timeFormat)
	{
		this.timeFormat = timeFormat;
	}

	/**
	 * Gets the weekNumbers.
	 * @return weekNumbers
	 */
	public boolean isWeekNumbers()
	{
		return weekNumbers;
	}

	/**
	 * Sets the weekNumbers.
	 * @param weekNumbers weekNumbers
	 */
	public void setWeekNumbers(boolean weekNumbers)
	{
		this.weekNumbers = weekNumbers;
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5726.java