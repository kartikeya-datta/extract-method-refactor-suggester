error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/205.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/205.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 654
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/205.java
text:
```scala
class CronSequenceGenerator {

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.springframework.scheduling.support;

import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.util.StringUtils;

/**
 * Date sequence generator for a <a
 * href="http://www.manpagez.com/man/5/crontab/">Crontab pattern</a> allowing
 * client to specify a pattern that the sequence matches. The pattern is a list
 * of 6 single space separated fields representing (second, minute, hour, day,
 * month, weekday). Month and weekday names can be given as the first three
 * letters of the English names.<br/>
 * <br/>
 *
 * Example patterns
 * <ul>
 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
 * <li>"0 0 8-10/30 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
 * </ul>
 *
 * @author Dave Syer
 * @author Juergen Hoeller
 * @since 3.0
 * @see CronTrigger
 */
public class CronSequenceGenerator {

	private final BitSet seconds = new BitSet(60);

	private final BitSet minutes = new BitSet(60);

	private final BitSet hours = new BitSet(24);

	private final BitSet daysOfWeek = new BitSet(7);

	private final BitSet daysOfMonth = new BitSet(31);

	private final BitSet months = new BitSet(12);

	private final String expression;


	/**
	 * Construct a {@link CronSequenceGenerator} from the pattern provided.
	 * @param expression a space-separated list of time fields
	 * @throws IllegalArgumentException if the pattern cannot be parsed
	 */
	public CronSequenceGenerator(String expression) {
		this.expression = expression;
		parse(expression);
	}


	/**
	 * Get the next {@link Date} in the sequence matching the Cron pattern and
	 * after the value provided. The return value will have a whole number of
	 * seconds, and will be after the input value.
	 * @param date a seed value
	 * @return the next value matching the pattern
	 */
	public Date next(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		// Truncate to the next whole second
		calendar.add(Calendar.SECOND, 1);
		calendar.set(Calendar.MILLISECOND, 0);

		int second = calendar.get(Calendar.SECOND);
		findNext(this.seconds, second, 60, calendar, Calendar.SECOND);

		int minute = calendar.get(Calendar.MINUTE);
		findNext(this.minutes, minute, 60, calendar, Calendar.MINUTE, Calendar.SECOND);

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		findNext(this.hours, hour, 24, calendar, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		findNextDay(calendar, this.daysOfMonth, dayOfMonth, daysOfWeek, dayOfWeek, 366);

		int month = calendar.get(Calendar.MONTH);
		findNext(this.months, month, 12, calendar, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
				Calendar.MINUTE, Calendar.SECOND);

		return calendar.getTime();
	}

	private void findNextDay(
			Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek, int max) {

		int count = 0;
		// the DAY_OF_WEEK values in java.util.Calendar start with 1 (Sunday),
		// but in the cron pattern, they start with 0, so we subtract 1 here
		while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek-1)) && count++ < max) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			reset(calendar, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
		}
		if (count > max) {
			throw new IllegalStateException("Overflow in day for expression=" + this.expression);
		}
	}

	/**
	 * Search the bits provided for the next set bit after the value provided,
	 * and reset the calendar.
	 * @param bits a {@link BitSet} representing the allowed values of the field
	 * @param value the current value of the field
	 * @param max the largest value that the field can have
	 * @param calendar the calendar to increment as we move through the bits
	 * @param field the field to increment in the calendar (@see
	 * {@link Calendar} for the static constants defining valid fields)
	 * @param lowerOrders the Calendar field ids that should be reset (i.e. the
	 * ones of lower significance than the field of interest)
	 * @return the value of the calendar field that is next in the sequence
	 */
	private void findNext(BitSet bits, int value, int max, Calendar calendar, int field, int... lowerOrders) {
		int nextValue = bits.nextSetBit(value);
		//roll over if needed
		if (nextValue == -1) {
			calendar.add(field, max - value);
			nextValue = bits.nextSetBit(0);
		}
		if (nextValue != value) {
			calendar.set(field, nextValue);
			reset(calendar, lowerOrders);
		}
	}

	/**
	 * Reset the calendar setting all the fields provided to zero.
	 */
	private void reset(Calendar calendar, int... fields) {
		for (int field : fields) {
			calendar.set(field, 0);
		}
	}


	// Parsing logic invoked by the constructor.

	/**
	 * Parse the given pattern expression.
	 */
	private void parse(String expression) throws IllegalArgumentException {
		String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
		if (fields.length != 6) {
			throw new IllegalArgumentException(String.format(""
					+ "cron expression must consist of 6 fields (found %d in %s)", fields.length, expression));
		}
		setNumberHits(this.seconds, fields[0], 60);
		setNumberHits(this.minutes, fields[1], 60);
		setNumberHits(this.hours, fields[2], 24);
		setDaysOfMonth(this.daysOfMonth, fields[3], 31);
		setNumberHits(this.months, replaceOrdinals(fields[4], "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC"), 12);
		setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
		if (this.daysOfWeek.get(7)) {
			// Sunday can be represented as 0 or 7
			this.daysOfWeek.set(0);
			this.daysOfWeek.clear(7);
		}
	}

	/**
	 * Replace the values in the commaSeparatedList (case insensitive) with
	 * their index in the list.
	 * @return a new string with the values from the list replaced
	 */
	private String replaceOrdinals(String value, String commaSeparatedList) {
		String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);
		for (int i = 0; i < list.length; i++) {
			String item = list[i].toUpperCase();
			value = StringUtils.replace(value.toUpperCase(), item, "" + i);
		}
		return value;
	}

	private void setDaysOfMonth(BitSet bits, String field, int max) {
		// Days of month start with 1 (in Cron and Calendar) so add one
		setDays(bits, field, max+1);
		// ... and remove it from the front
		bits.clear(0);
	}

	private void setDays(BitSet bits, String field, int max) {
		if (field.contains("?")) {
			field = "*";
		}
		setNumberHits(bits, field, max);
	}

	private void setNumberHits(BitSet bits, String value, int max) {
		String[] fields = StringUtils.delimitedListToStringArray(value, ",");
		for (String field : fields) {
			if (!field.contains("/")) {
				// Not an incrementer so it must be a range (possibly empty)
				int[] range = getRange(field, max);
				bits.set(range[0], range[1] + 1);
			}
			else {
				String[] split = StringUtils.delimitedListToStringArray(field, "/");
				if (split.length > 2) {
					throw new IllegalArgumentException("Incrementer has more than two fields: " + field);
				}
				int[] range = getRange(split[0], max);
				if (!split[0].contains("-")) {
					range[1] = max - 1;
				}
				int delta = Integer.valueOf(split[1]);
				for (int i = range[0]; i <= range[1]; i += delta) {
					bits.set(i);
				}
			}
		}
	}

	private int[] getRange(String field, int max) {
		int[] result = new int[2];
		if (field.contains("*")) {
			result[0] = 0;
			result[1] = max-1;
			return result;
		}
		if (!field.contains("-")) {
			result[0] = result[1] = Integer.valueOf(field);
		}
		else {
			String[] split = StringUtils.delimitedListToStringArray(field, "-");
			if (split.length > 2) {
				throw new IllegalArgumentException("Range has more than two fields: " + field);
			}
			result[0] = Integer.valueOf(split[0]);
			result[1] = Integer.valueOf(split[1]);
		}
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CronSequenceGenerator)) {
			return false;
		}
		CronSequenceGenerator cron = (CronSequenceGenerator) obj;
		return cron.months.equals(months) && cron.daysOfMonth.equals(daysOfMonth) && cron.daysOfWeek.equals(daysOfWeek)
				&& cron.hours.equals(hours) && cron.minutes.equals(minutes) && cron.seconds.equals(seconds);
	}

	@Override
	public int hashCode() {
		return 37 + 17 * months.hashCode() + 29 * daysOfMonth.hashCode() + 37 * daysOfWeek.hashCode() + 41
				* hours.hashCode() + 53 * minutes.hashCode() + 61 * seconds.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + expression;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/205.java