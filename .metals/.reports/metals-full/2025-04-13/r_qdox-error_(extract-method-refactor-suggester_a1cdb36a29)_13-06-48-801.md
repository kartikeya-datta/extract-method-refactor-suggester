error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[222,2]

error in qdox parser
file content:
```java
offset: 6497
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17717.java
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


import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wicket.util.convert.ConversionException;

/**
 * <p>
 * Modified {@link LocaleConverter}implementation for this framework
 * </p>
 */
public abstract class DecimalLocaleConverter extends BaseLocaleConverter
{
    protected Pattern nonDigitPattern = Pattern.compile(".*[^0-9&&[^\\,]&&[^\\.]&&[^\\-]].*");

    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. The locale is the default locale for this instance of the
     * Java Virtual Machine and an unlocalized pattern is used for the convertion.
     */
    public DecimalLocaleConverter()
    {
        this(Locale.getDefault());
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. No pattern is used for the convertion.
     * @param locale The locale
     */
    public DecimalLocaleConverter(Locale locale)
    {
        this(locale, null);
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs. An unlocalized pattern is used for the convertion.
     * @param locale The locale
     * @param pattern The convertion pattern
     */
    public DecimalLocaleConverter(Locale locale, String pattern)
    {
        this(locale, pattern, false);
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link ConversionException}if a
     * conversion error occurs.
     * @param locale The locale
     * @param pattern The convertion pattern
     * @param locPattern Indicate whether the pattern is localized or not
     */
    public DecimalLocaleConverter(Locale locale, String pattern, boolean locPattern)
    {
        super(locale, pattern, locPattern);
    }

    // --------------------------------------------------------- Methods

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     * @param value The input object to be converted
     * @param pattern The pattern is used for the conversion
     * @return converted object
     * @throws ParseException
     * @exception ConversionException if conversion cannot be performed successfully
     */
    protected Object parse(Object value, String pattern) throws ParseException
    {
        if (value == null)
        {
            return null;
        }

        DecimalFormat formatter = getFormat(pattern);

        return formatter.parse((String) value);
    }

    /**
     * Convert the specified input object into a locale-sensitive output string
     * @param value The input object to be formatted
     * @param pattern The pattern is used for the conversion
     * @return formatted object
     * @exception IllegalArgumentException if formatting cannot be performed successfully
     */
    public String format(Object value, String pattern) throws IllegalArgumentException
    {
        if (value == null)
        {
            return null;
        }

        DecimalFormat formatter = getFormat(pattern);

        return formatter.format(value);
    }

    /**
     * get format and optionally apply pattern if given
     * @param pattern pattern or null
     * @return DecimalFormat formatter instance
     */
    protected DecimalFormat getFormat(String pattern)
    {
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(locale);

        if (pattern != null)
        {
            if (locPattern)
            {
                formatter.applyLocalizedPattern(pattern);
            }
            else
            {
                formatter.applyPattern(pattern);
            }
        }

        return formatter;
    }

    /**
     * translate value to a number optionally using the supplied pattern
     * @param value the value to convert
     * @param pattern the patter to use (optional)
     * @return Number
     * @throws ConversionException
     */
    protected Number getNumber(Object value, String pattern) throws ConversionException
    {
        if (value instanceof Number)
        {
            return (Number) value;
        }

        Number temp = null;

        try
        {
            if (pattern != null)
            {
                temp = (Number) parse(value, pattern);
            }
            else
            {
                String stringval = null;

                if (value instanceof String)
                {
                    stringval = (String) value;
                }
                else if (value instanceof String[])
                {
                    stringval = ((String[]) value)[0];
                }
                else
                {
                    stringval = String.valueOf(value);
                }

                Matcher nonDigitMatcher = nonDigitPattern.matcher(stringval);

                if (nonDigitMatcher.matches())
                {
                    throw new ConversionException(stringval + " is not a valid number");
                }

                temp = (Number) parse(value, this.pattern);
            }
        }
        catch (Exception e)
        {
            String dpat = null;

            if (pattern != null)
            {
                dpat = pattern;
            }
            else
            {
                DecimalFormat formatter = getFormat(pattern);

                dpat = formatter.toLocalizedPattern();
            }

            throw new ConversionException(e).setPattern(dpat);
        }

        return temp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17717.java