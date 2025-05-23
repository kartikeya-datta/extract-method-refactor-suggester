error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4319.java
text:
```scala
D@@ate date = converter.convertToObject("05.11.2011", Locale.GERMAN);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.extensions.yui.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.extensions.yui.calendar.DateTimeField.AM_PM;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.util.tester.DiffUtil;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class DatePickerTest extends WicketTestCase
{
	/** log. */
	private static final Logger log = LoggerFactory.getLogger(DatePickerTest.class);

	private TimeZone defaultTz = TimeZone.getDefault();

	/**
	 * @see org.apache.wicket.WicketTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		TimeZone.setDefault(defaultTz);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(defaultTz));

		super.tearDown();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void test1() throws Exception
	{
		log.error("=========== test1() =================");
		myTestExecution(DatesPage1.class, "DatesPage1_ExpectedResult.html");
	}

	/**
	 * @throws Exception
	 */
	public void test2() throws Exception
	{
		log.error("=========== test2() =================");
		Class<? extends Page> pageClass = DatesPage2.class;
		Date date = new GregorianCalendar(2010, 10, 06, 0, 0).getTime();
		tester.getSession().setLocale(Locale.GERMAN);
		tester.startPage(pageClass);
		tester.assertRenderedPage(pageClass);
		FormTester formTester = tester.newFormTester("form");
		formTester.setValue("dateTimeField:date", "06.11.2010");
		formTester.setValue("dateTimeField:hours", "00");
		formTester.setValue("dateTimeField:minutes", "00");
		formTester.setValue("dateField:date", "06.11.2010");
		formTester.submit();
		DatesPage2 page = (DatesPage2)tester.getLastRenderedPage();

		log.error("orig: " + date.getTime() + "; date: " + page.date.getTime() + "; dateTime: " +
			page.dateTime.getTime());
		log.error("orig: " + date + "; date: " + page.date + "; dateTime: " + page.dateTime);
		assertEquals(0, date.compareTo(page.dateTime));
		assertEquals(0, date.compareTo(page.date));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void test3() throws Exception
	{
		log.error("=========== test3() =================");
		TimeZone tzClient = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone tzServer = TimeZone.getTimeZone("Europe/Berlin");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));

		Class<? extends Page> pageClass = DatesPage2.class;
		MutableDateTime dt = new MutableDateTime(DateTimeZone.forTimeZone(tzClient));
		dt.setDateTime(2010, 11, 06, 0, 0, 0, 0);
		Date date = new Date(dt.getMillis());

		WebClientInfo clientInfo = (WebClientInfo)tester.getSession().getClientInfo();
		clientInfo.getProperties().setTimeZone(tzClient);

		tester.getSession().setLocale(Locale.GERMAN);
		tester.startPage(pageClass);
		tester.assertRenderedPage(pageClass);
		FormTester formTester = tester.newFormTester("form");
		formTester.setValue("dateTimeField:date", "06.11.2010");
		formTester.setValue("dateTimeField:hours", "00");
		formTester.setValue("dateTimeField:minutes", "00");
		formTester.setValue("dateField:date", "06.11.2010");
		formTester.submit();

		DatesPage2 page = (DatesPage2)tester.getLastRenderedPage();

		log.error("orig: " + date.getTime() + "; date: " + page.date.getTime() + "; dateTime: " +
			page.dateTime.getTime());
		log.error("orig: " + date + "; date: " + page.date + "; dateTime: " + page.dateTime);
		assertEquals(0, date.compareTo(page.dateTime));
		assertEquals(0, date.compareTo(page.date));
	}

	/**
	 * Tests joda & jvm default time zone handling
	 */
	public void testJodaTimeDefaultTimeZone()
	{
		TimeZone origJvmDef = TimeZone.getDefault();
		DateTimeZone origJodaDef = DateTimeZone.getDefault();

		// lets find a timezone different from current default
		String newId = null;
		for (String id : TimeZone.getAvailableIDs())
		{
			if (!id.equals(origJvmDef.getID()))
			{
				newId = id;
				break;
			}
		}

		assertNotNull(newId);

		TimeZone.setDefault(TimeZone.getTimeZone(newId));

		TimeZone newJvmDef = TimeZone.getDefault();
		DateTimeZone newJodaDef = DateTimeZone.getDefault();

		// if this fails we are under security manager
		// and we have no right to set default timezone
		assertNotSame(origJvmDef, newJvmDef);

		// this should be true because joda caches the
		// default timezone and even for the first
		// lookup it uses a System property if possible
		// for more info see org.joda.time.DateTimeZone.getDefault()
		assertSame(origJodaDef, newJodaDef);
	}
	
	/**
	 * Test date conversion with the server having a different current date than the client time
	 * zone.
	 * 
	 * @throws ParseException
	 */
	public void testDifferentDateTimeZoneConversion() throws ParseException
	{
		log.error("=========== testDifferentDateTimeZoneConversion() =================");
		TimeZone origJvmDef = TimeZone.getDefault();
		DateTimeZone origJodaDef = DateTimeZone.getDefault();
		TimeZone tzClient = TimeZone.getTimeZone("Australia/South");
		TimeZone tzServer = TimeZone.getTimeZone("Europe/Berlin");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));
		// Locale.setDefault(Locale.GERMAN);

		Class<? extends Page> pageClass = DatesPage2.class;
		MutableDateTime dt = new MutableDateTime(DateTimeZone.forTimeZone(tzClient));
		dt.setDateTime(2010, 11, 06, 0, 0, 0, 0);
		Date date = new Date(dt.getMillis());

		WebClientInfo clientInfo = (WebClientInfo)tester.getSession().getClientInfo();
		clientInfo.getProperties().setTimeZone(tzClient);

		tester.getSession().setLocale(Locale.GERMAN);
		tester.startPage(pageClass);
		tester.assertRenderedPage(pageClass);
		FormTester formTester = tester.newFormTester("form");
		formTester.setValue("dateTimeField:date", "06.11.2010");
		formTester.setValue("dateTimeField:hours", "00");
		formTester.setValue("dateTimeField:minutes", "00");
		formTester.setValue("dateField:date", "06.11.2010");
		formTester.submit();

		DatesPage2 page = (DatesPage2)tester.getLastRenderedPage();

		log.error("orig: " + date.getTime() + "; date: " + page.date.getTime() + "; dateTime: " +
			page.dateTime.getTime());
		log.error("orig: " + date + "; date: " + page.date + "; dateTime: " + page.dateTime);
		assertEquals(0, date.compareTo(page.dateTime));
		assertEquals(0, date.compareTo(page.date));

		TimeZone.setDefault(origJvmDef);
		DateTimeZone.setDefault(origJodaDef);
	}

	public void testStyleDateConverterTimeZoneDifference() throws ParseException
	{
		TimeZone origJvmDef = TimeZone.getDefault();
		DateTimeZone origJodaDef = DateTimeZone.getDefault();
		TimeZone tzClient = TimeZone.getTimeZone("Etc/GMT-14");
		TimeZone tzServer = TimeZone.getTimeZone("Etc/GMT+12");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));
		Locale.setDefault(Locale.GERMAN);

		WebClientInfo clientInfo = (WebClientInfo)tester.getSession().getClientInfo();
		clientInfo.getProperties().setTimeZone(tzClient);

		StyleDateConverter converter = new StyleDateConverter(true);

		Calendar cal = Calendar.getInstance(tzClient);
		cal.set(2011, 10, 5, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date dateRef = cal.getTime();
		Date date = converter.convertToObject("11/05/2011", Locale.GERMAN);
		log.error("ref: " + dateRef.getTime() + "; converted: " + date.getTime());
		log.error("ref: " + dateRef + "; date: " + date);
		assertEquals(0, dateRef.compareTo(date));

		TimeZone.setDefault(origJvmDef);
		DateTimeZone.setDefault(origJodaDef);
	}
	/**
	 * 
	 * @throws ParseException
	 */
	public void testDates1() throws ParseException
	{
		log.error("=========== testDates1() =================");
		TimeZone tzClient = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone tzServer = TimeZone.getTimeZone("Europe/Berlin");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));
		Locale.setDefault(Locale.GERMAN);

// Date orig = convertDate("06.11.2010", null, null, null, false, tzClient);
// Date origJoda = convertDateJoda("06.11.2010", null, null, null, false, tzClient);
		Date orig3 = convertDateNew("06.11.2010", null, null, null, false, tzClient);

		MutableDateTime dt = new MutableDateTime(DateTimeZone.forTimeZone(tzClient));
		dt.setDateTime(2010, 11, 06, 0, 0, 0, 0);
		Date date = new Date(dt.getMillis());

		log.error(/* "actual: " + orig.getTime() + "; joda: " + origJoda.getTime() + */"; origNew: " +
			orig3.getTime() + "; expected: " + date.getTime());
		log.error(/* "actual: " + orig + "; joda: " + origJoda + */"; origNew: " + orig3 +
			"; expected: " + date);
		assertEquals(date.getTime(), orig3.getTime());
// assertEquals(date.getTime(), orig.getTime());
// assertEquals(origJoda.getTime(), orig.getTime());
	}

	/**
	 * 
	 * @throws ParseException
	 */
	public void testDates2() throws ParseException
	{
		log.error("=========== testDates2() =================");
		TimeZone tzClient = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone tzServer = TimeZone.getTimeZone("Europe/Berlin");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));
		Locale.setDefault(Locale.GERMAN);

// Date orig = convertDate("06.11.2010", 0, 0, AM_PM.AM, false, tzClient);
// Date origJoda = convertDateJoda("06.11.2010", 0, 0, AM_PM.AM, false, tzClient);
		Date orig3 = convertDateNew("06.11.2010", 0, 0, AM_PM.AM, false, tzClient);

		MutableDateTime dt = new MutableDateTime(DateTimeZone.forTimeZone(tzClient));
		dt.setDateTime(2010, 11, 06, 0, 0, 0, 0);
		Date date = new Date(dt.getMillis());

		log.error(/* "actual: " + orig.getTime() + "; joda: " + origJoda.getTime() + */"; origNew: " +
			orig3.getTime() + "; expected: " + date.getTime());
		log.error(/* "actual: " + orig + "; joda: " + origJoda + */"; origNew: " + orig3 +
			"; expected: " + date);
		assertEquals(date.getTime(), orig3.getTime());
// assertEquals(date.getTime(), orig.getTime());
// assertEquals(origJoda.getTime(), orig.getTime());
	}

	/**
	 * 
	 * @throws ParseException
	 */
	public void testDates3() throws ParseException
	{
		log.error("=========== testDates3() =================");
		TimeZone tzClient = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone tzServer = TimeZone.getTimeZone("Europe/Berlin");

		TimeZone.setDefault(tzServer);
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(tzServer));
		Locale.setDefault(Locale.GERMAN);

// Date orig = convertDate("06.11.2010", 12, 0, null, false, tzClient);
// Date origJoda = convertDateJoda("06.11.2010", 12, 0, null, false, tzClient);
		Date orig3 = convertDateNew("06.11.2010", 12, 0, null, false, tzClient);

		MutableDateTime dt = new MutableDateTime(DateTimeZone.forTimeZone(tzClient));
		dt.setDateTime(2010, 11, 06, 12, 0, 0, 0);
		Date date = new Date(dt.getMillis());

		log.error(/* "actual: " + orig.getTime() + "; joda: " + origJoda.getTime() + */"; origNew: " +
			orig3.getTime() + "; expected: " + date.getTime());
		log.error(/* "actual: " + orig + "; joda: " + origJoda + */"; origNew: " + orig3 +
			"; expected: " + date);
		assertEquals(date.getTime(), orig3.getTime());
// assertEquals(date.getTime(), orig.getTime());
// assertEquals(origJoda.getTime(), orig.getTime());
	}

	/**
	 * Simulate what DateTimeField does
	 * 
	 * @param dateStr
	 * @param hours
	 * @param minutes
	 * @param amOrPm
	 * @param use12HourFormat
	 * @param tzClient
	 * @return Date
	 * @throws ParseException
	 */
	public Date convertDate(final String dateStr, final Integer hours, final Integer minutes,
		final AM_PM amOrPm, final boolean use12HourFormat, final TimeZone tzClient)
		throws ParseException
	{
		log.error(">>> convertDate()");
		Date dateFieldInput = (dateStr != null ? DateFormat.getDateInstance().parse(dateStr) : null);

		// Default to today, if date entry was invisible
		final MutableDateTime date;
		if (dateFieldInput != null)
		{
			log.error("1. dateFieldInput: " + dateFieldInput.getTime() + "  " + dateFieldInput);
			date = new MutableDateTime(dateFieldInput);
		}
		else
		{
			log.error("1. dateFieldInput: null");
			// Current date
			date = new MutableDateTime();
		}
		log.error("2. mutable date: " + date.getMillis() + "  " + date);

		// always set secs to 0
		date.setSecondOfMinute(0);
		log.error("3. secs = 0: " + date.getMillis() + "  " + date);

		// The AM/PM field
		if (use12HourFormat)
		{
			date.set(DateTimeFieldType.halfdayOfDay(), amOrPm == AM_PM.PM ? 1 : 0);
		}
		log.error("4. AM/PM: " + date.getMillis() + "  " + date);

		// The hours
		if (hours == null)
		{
			date.setHourOfDay(0);
		}
		else
		{
			date.set(DateTimeFieldType.hourOfDay(), hours % (use12HourFormat ? 12 : 24));
		}
		log.error("5. hours: " + date.getMillis() + "  " + date);

		// The minutes
		if (minutes == null)
		{
			date.setMinuteOfHour(0);
		}
		else
		{
			date.setMinuteOfHour(minutes);
		}
		log.error("6. minutes: " + date.getMillis() + "  " + date);

		// Use the client timezone to properly calculate the millisecs
		if (tzClient != null)
		{
			date.setZoneRetainFields(DateTimeZone.forTimeZone(tzClient));
			log.error("7. zone: " + date.getMillis() + "  " + date);
		}

		Date rtn = new Date(date.getMillis());
		log.error("8. final date: " + rtn.getTime() + "  " + rtn);
		return rtn;
	}

	/**
	 * Simulate what DateTimeField does
	 * 
	 * @param dateStr
	 * @param hours
	 * @param minutes
	 * @param amOrPm
	 * @param use12HourFormat
	 * @param tzClient
	 * @return Date
	 * @throws ParseException
	 */
	public Date convertDateNew(final String dateStr, final Integer hours, final Integer minutes,
		final AM_PM amOrPm, final boolean use12HourFormat, final TimeZone tzClient)
		throws ParseException
	{
		log.error(">>> convertDateNew()");
		// This is what I get from field.getConvertedInput()
		Date dateFieldInput = (dateStr != null ? DateFormat.getDateInstance().parse(dateStr) : null);

		// Default with "now"
		if (dateFieldInput == null)
		{
			dateFieldInput = new Date();
		}

		// Get year, month and day ignoring any timezone of the Date object
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFieldInput);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int iHours = (hours == null ? 0 : hours % 24);
		int iMins = (minutes == null ? 0 : minutes);

		// Use the input to create a date object with proper timezone
		MutableDateTime date = new MutableDateTime(year, month, day, iHours, iMins, 0, 0,
			DateTimeZone.forTimeZone(tzClient));

		// Use the input to create a date object. Ignore the timezone provided by dateFieldInput and
		// use tzClient instead. No re-calculation will happen. It should be the same as above.
// MutableDateTime date = new MutableDateTime(dateFieldInput,
// DateTimeZone.forTimeZone(tzClient));
		log.error("1. date: " + date.getMillis() + "  " + date);

		// Adjust for halfday if needed
		int halfday = 0;
		if (use12HourFormat)
		{
			halfday = (amOrPm == AM_PM.PM ? 1 : 0);
			date.set(DateTimeFieldType.halfdayOfDay(), halfday);
			date.set(DateTimeFieldType.hourOfDay(), iHours % 12);
		}
		log.error("2. halfday adjustments: " + date.getMillis() + "  " + date);

		Date rtn = new Date(date.getMillis());
		log.error("3. final date: " + rtn.getTime() + "  " + rtn);
		return rtn;
	}

	/**
	 * Simulate what DateTimeField does
	 * 
	 * @param dateStr
	 * @param hours
	 * @param minutes
	 * @param amOrPm
	 * @param use12HourFormat
	 * @param tzClient
	 * @return Date
	 * @throws ParseException
	 */
	public Date convertDateJoda(final String dateStr, final Integer hours, final Integer minutes,
		final AM_PM amOrPm, final boolean use12HourFormat, final TimeZone tzClient)
		throws ParseException
	{
		log.error(">>> convertDateJoda()");

		DateTimeFormatter fmt = DateTimeFormat.shortDate();
		// fmt.withZone(timeZone).parseDateTime("10/1/06 5:00 AM");
		MutableDateTime date = (dateStr != null ? fmt.parseMutableDateTime(dateStr)
			: new MutableDateTime());

		log.error("1. mutable date: " + date.getMillis() + "  " + date);

		// always set secs to 0
		date.setSecondOfMinute(0);
		log.error("2. secs = 0: " + date.getMillis() + "  " + date);

		// The AM/PM field
		if (use12HourFormat)
		{
			date.set(DateTimeFieldType.halfdayOfDay(), amOrPm == AM_PM.PM ? 1 : 0);
		}
		log.error("3. AM/PM: " + date.getMillis() + "  " + date);

		// The hours
		if (hours == null)
		{
			date.setHourOfDay(0);
		}
		else
		{
			date.set(DateTimeFieldType.hourOfDay(), hours % (use12HourFormat ? 12 : 24));
		}
		log.error("4. hours: " + date.getMillis() + "  " + date);

		// The minutes
		if (minutes == null)
		{
			date.setMinuteOfHour(0);
		}
		else
		{
			date.setMinuteOfHour(minutes);
		}
		log.error("5. minutes: " + date.getMillis() + "  " + date);

		// Use the client timezone to properly calculate the millisecs
		if (tzClient != null)
		{
			date.setZoneRetainFields(DateTimeZone.forTimeZone(tzClient));
		}
		log.error("6. timezone: " + date.getMillis() + "  " + date);

		Date rtn = new Date(date.getMillis());
		log.error("7. final date: " + rtn.getTime() + "  " + rtn);
		return rtn;
	}

	/**
	 * Use <code>-Dwicket.replace.expected.results=true</code> to automatically replace the expected
	 * output file.
	 * 
	 * @param <T>
	 * 
	 * @param pageClass
	 * @param filename
	 * @throws Exception
	 */
	protected <T extends Page> void myTestExecution(final Class<T> pageClass, final String filename)
		throws Exception
	{
		System.out.println("=== " + pageClass.getName() + " ===");

		tester.getSession().setLocale(Locale.GERMAN);
		tester.startPage(pageClass);
		tester.assertRenderedPage(pageClass);

		String document = tester.getLastResponseAsString();
		document = document.replaceAll("\\d\\d\\.\\d\\d\\.\\d\\d", "xx.xx.xx");
		document = document.replaceAll("\\d\\d/\\d\\d/\\d\\d\\d\\d", "xx.xx.xxxx");
		document = document.replaceAll("\\d\\d/\\d\\d\\d\\d", "xx.xxxx");

		DiffUtil.validatePage(document, pageClass, filename, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4319.java