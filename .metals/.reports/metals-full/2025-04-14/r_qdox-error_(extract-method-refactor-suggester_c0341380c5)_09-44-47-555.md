error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17716.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17716.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[230,2]

error in qdox parser
file content:
```java
offset: 6606
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17716.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket.util.convert.converters;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import wicket.util.convert.ConversionException;

/**
 * <p>
 * Standard {@link LocaleConverter}implementation that converts an incoming
 * locale-sensitive String into a <code>java.util.Date</code> object, optionally using a
 * default value or throwing a {@link ConversionException}if a conversion error occurs.
 * </p>
 */
public class DateLocaleConverter extends BaseLocaleConverter
{
    private boolean lenient = false;

    private int dateStyle = DateFormat.SHORT;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. The locale is the default locale for this instance of the
     * Java Virtual Machine and an unlocalized pattern is used for the convertion.
     */
    public DateLocaleConverter()
    {
        this(Locale.getDefault());
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. An unlocalized pattern is used for the convertion.
     * @param locale The locale
     */
    public DateLocaleConverter(Locale locale)
    {
        this(locale, null);
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. An unlocalized pattern is used for the convertion.
     * @param locale The locale
     * @param pattern The convertion pattern
     */
    public DateLocaleConverter(Locale locale, String pattern)
    {
        super(locale, pattern, false);
    }

    // --------------------------------------------------------- Methods

    /**
     * Returns whether date formatting is lenient.
     * @return true if the <code>DateFormat</code> used for formatting is lenient
     * @see java.text.DateFormat#isLenient
     */
    public boolean isLenient()
    {
        return lenient;
    }

    /**
     * Specify whether or not date-time parsing should be lenient.
     * @param lenient true if the <code>DateFormat</code> used for formatting should be
     *            lenient
     * @see java.text.DateFormat#setLenient
     */
    public void setLenient(boolean lenient)
    {
        this.lenient = lenient;
    }

    /**
     * get date style
     * @return int date style as a constant from DateFormat
     */
    public int getDateStyle()
    {
        return dateStyle;
    }

    /**
     * set date style
     * @param dateStyle
     */
    public void setDateStyle(int dateStyle)
    {
        this.dateStyle = dateStyle;
    }

    // --------------------------------------------------------- Methods

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     * @return converted object
     * @exception ConversionException if conversion cannot be performed successfully
     */
    protected Object parse(Object value, String pattern) throws ConversionException
    {
        DateFormat formatter = getFormat(pattern, locale);

        try
        {
            return formatter.parse((String) value);
        }
        catch (ParseException e)
        {
            String dpat = null;

            if (pattern != null)
            {
                dpat = pattern;
            }
            else if (formatter instanceof SimpleDateFormat)
            {
                dpat = ((SimpleDateFormat) formatter).toLocalizedPattern();
            }

            throw new ConversionException(e).setPattern(dpat);
        }
    }

    /**
     * format value with pattern or using the default pattern
     * @see wicket.util.convert.Formatter#format(java.lang.Object, java.lang.String)
     */
    public String format(Object value, String pattern) throws IllegalArgumentException
    {
        DateFormat format = getFormat(pattern, locale);
        Date date = null;

        if (value instanceof Date)
        {
            date = (Date) value;
        }
        else
        {
            date = (Date) convert(Date.class, value);
        }

        return format.format(date);
    }

    /**
     * Get date format.
     * @param pattern the pattern to use for formatting
     * @param locale the locale
     * @return the date format object for the given pattern and locale
     */
    private DateFormat getFormat(String pattern, Locale locale)
    {
        DateFormat format = null;

        if (pattern == null)
        {
            format = DateFormat.getDateInstance(dateStyle, locale);
            format.setLenient(lenient);
        }
        else
        {
            SimpleDateFormat _format = new SimpleDateFormat(pattern, locale);

            _format.setLenient(lenient);

            if (locPattern)
            {
                _format.applyLocalizedPattern(pattern);
            }
            else
            {
                _format.applyPattern(pattern);
            }

            format = _format;
        }

        return format;
    }

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     * @param type Data type to which this value should be converted
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     * @return converted object
     * @exception ConversionException if conversion cannot be performed successfully
     */
    public Object convert(Class type, Object value, String pattern)
    {
        if (value == null)
        {
            return null;
        }

        return parse(value, pattern);
    }
}@@
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17716.java