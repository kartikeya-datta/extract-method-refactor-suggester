error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[644,80]

error in qdox parser
file content:
```java
offset: 19383
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17776.java
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
package wicket.util.string;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import wicket.util.time.Duration;
import wicket.util.time.Time;

/**
 * Holds an immutable string value with methods to convert to and from various types.
 * @author Jonathan Locke
 */
public class StringValue
{
    /** The underlying string. */
    private final String text;

    /** Locale to be used for formatting and parsing. */
    private final Locale locale;
    
    /**
     * Private constructor to force use of static factory methods.
     * @param text The text for this string value
     */
    protected StringValue(final String text)
    {
        this.text = text;
        this.locale = Locale.getDefault();
    }
    
    /**
     * Private constructor to force use of static factory methods.
     * @param text The text for this string value
     * @param locale the locale for formatting and parsing
     */
    protected StringValue(final String text, final Locale locale)
    {
        this.text = text;
        this.locale = locale;
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param object An object
     * @return String value for object
     */
    public static StringValue valueOf(final Object object)
    {
        return valueOf(Strings.toString(object));
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param object An object
     * @param locale Locale to be used for formatting
     * @return String value for object
     */
    public static StringValue valueOf(final Object object, final Locale locale)
    {
        return valueOf(Strings.toString(object), locale);
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param string A string
     * @return String value for string
     */
    public static StringValue valueOf(final String string)
    {
        return new StringValue(string);
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param string A string
     * @param locale Locale to be used for formatting
     * @return String value for string
     */
    public static StringValue valueOf(final String string, final Locale locale)
    {
        return new StringValue(string, locale);
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param buffer A string buffer
     * @return String value
     */
    public static StringValue valueOf(final StringBuffer buffer)
    {
        return valueOf(buffer.toString());
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param value Double precision value
     * @return String value formatted with one place after decimal
     */
    public static StringValue valueOf(final double value)
    {
        return valueOf(value, Locale.getDefault());
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param value Double precision value
     * @param locale Locale to be used for formatting
     * @return String value formatted with one place after decimal
     */
    public static StringValue valueOf(final double value, final Locale locale)
    {
        return valueOf(value, 1, locale);
    }

    /**
     * Converts the given input to an instance of StringValue.
     * @param value Double precision value
     * @param places Number of places after decimal
     * @param locale Locale to be used for formatting
     * @return String value formatted with the given number of places after decimal
     */
    public static StringValue valueOf(final double value, final int places, final Locale locale)
    {
        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            return valueOf("N/A");
        }
        else
        {
            final DecimalFormat format = new DecimalFormat(
                    "#." + repeat(places, '#'), new DecimalFormatSymbols(locale));
            return valueOf(format.format(value));
        }
    }

    /**
     * @param times Number of times to repeat character
     * @param c Character to repeat
     * @return Repeated character string
     */
    public static StringValue repeat(final int times, final char c)
    {
        final StringBuffer buffer = new StringBuffer(times);

        for (int i = 0; i < times; i++)
        {
            buffer.append(c);
        }

        return valueOf(buffer);
    }

    /**
     * @param times Number of times to repeat string
     * @param s String to repeat
     * @return Repeated character string
     */
    public static StringValue repeat(final int times, final String s)
    {
        final StringBuffer buffer = new StringBuffer(times);

        for (int i = 0; i < times; i++)
        {
            buffer.append(s);
        }

        return valueOf(buffer);
    }

    /**
     * @return The string value
     */
    public final String toString()
    {
        return text;
    }

    /**
     * Converts this StringValue to a given type.
     * @param type The type to convert to
     * @return The converted value
     * @throws StringValueConversionException
     */
    public final Object to(final Class type) throws StringValueConversionException
    {
        if (type == String.class)
        {
            return toString();
        }

        if ((type == Integer.TYPE) || (type == Integer.class))
        {
            return toInteger();
        }

        if ((type == Long.TYPE) || (type == Long.class))
        {
            return toLongObject();
        }

        if ((type == Boolean.TYPE) || (type == Boolean.class))
        {
            return toBooleanObject();
        }

        if ((type == Double.TYPE) || (type == Double.class))
        {
            return toDoubleObject();
        }

        if ((type == Character.TYPE) || (type == Character.class))
        {
            return toCharacter();
        }

        if (type == Time.class)
        {
            return toTime();
        }

        if (type == Duration.class)
        {
            return toDuration();
        }

        throw new StringValueConversionException("Cannot convert '"
                + toString() + "'to type " + type);
    }

    /**
     * Convert this text to a boolean.
     * @return This string value as a boolean
     * @throws StringValueConversionException
     */
    public final boolean toBoolean() throws StringValueConversionException
    {
        return Strings.toBoolean(text);
    }

    /**
     * Convert this text to a char.
     * @return This string value as a character
     * @throws StringValueConversionException
     */
    public final char toChar() throws StringValueConversionException
    {
        return Strings.toChar(text);
    }

    /**
     * Replaces on this text.
     * @param searchFor What to search for
     * @param replaceWith What to replace with
     * @return This string value with searchFor replaces with replaceWith
     */
    public final String replaceAll(final String searchFor, final String replaceWith)
    {
        return Strings.replaceAll(text, searchFor, replaceWith);
    }

    /**
     * Gets the substring before the first occurence given char.
     * @param c char to scan for
     * @return the substring
     */
    public final String beforeFirst(final char c)
    {
        return Strings.beforeFirst(text, c);
    }

    /**
     * Gets the substring after the first occurence given char.
     * @param c char to scan for
     * @return the substring
     */
    public final String afterFirst(final char c)
    {
        return Strings.afterFirst(text, c);
    }

    /**
     * Gets the substring before the last occurence given char.
     * @param c char to scan for
     * @return the substring
     */
    public final String beforeLast(final char c)
    {
        return Strings.afterLast(text, c);
    }

    /**
     * Gets the substring after the last occurence given char.
     * @param c char to scan for
     * @return the substring
     */
    public final String afterLast(final char c)
    {
        return Strings.afterLast(text, c);
    }

    /**
     * Convert this text to an Integer and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Integer toInteger() throws StringValueConversionException
    {
        try
        {
            return new Integer(text);
        }
        catch (NumberFormatException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to an Integer value", e);
        }
    }

    /**
     * Convert this text to a Long and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Long toLongObject() throws StringValueConversionException
    {
        try
        {
            return new Long(text);
        }
        catch (NumberFormatException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to a Long value", e);
        }
    }

    /**
     * Convert this text to a Double and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Double toDoubleObject() throws StringValueConversionException
    {
        return new Double(toDouble());
    }

    /**
     * Convert this text to an int and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final int toInt() throws StringValueConversionException
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to an int value", e);
        }
    }

    /**
     * Convert this text to a long and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final long toLong() throws StringValueConversionException
    {
        try
        {
            return Long.parseLong(text);
        }
        catch (NumberFormatException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to a long value", e);
        }
    }

    /**
     * Convert this text to a double and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final double toDouble() throws StringValueConversionException
    {
        try
        {
            return NumberFormat.getNumberInstance(this.locale).parse(text).doubleValue();
        }
        catch (ParseException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to a double value", e);
        }
    }

    /**
     * Convert this text to a boolean and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Boolean toBooleanObject() throws StringValueConversionException
    {
        return new Boolean(toBoolean());
    }

    /**
     * Convert this text to a Character and convert unchecked NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Character toCharacter() throws StringValueConversionException
    {
        return new Character(toChar());
    }

    /**
     * Convert this text to a Duration instance and convert unchecked
     * NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Duration toDuration() throws StringValueConversionException
    {
        return Duration.valueOf(text, this.locale);
    }

    /**
     * Convert this text to a time instance and convert unchecked
     * NumberFormatExceptions to checked.
     * @return Converted text
     * @throws StringValueConversionException
     */
    public final Time toTime() throws StringValueConversionException
    {
        try
        {
            return Time.valueOf(text);
        }
        catch (ParseException e)
        {
            throw new StringValueConversionException("Unable to convert '"
                    + text + "' to a Time value", e);
        }
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     */
    public final String toOptionalString()
    {
        return text;
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Duration toOptionalDuration() throws StringValueConversionException
    {
        return (text == null) ? null : toDuration();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Time toOptionalTime() throws StringValueConversionException
    {
        return (text == null) ? null : toTime();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Integer toOptionalInteger() throws StringValueConversionException
    {
        return (text == null) ? null : toInteger();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Long toOptionalLong() throws StringValueConversionException
    {
        return (text == null) ? null : toLongObject();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Double toOptionalDouble() throws StringValueConversionException
    {
        return (text == null) ? null : toDoubleObject();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Boolean toOptionalBoolean() throws StringValueConversionException
    {
        return (text == null) ? null : toBooleanObject();
    }

    /**
     * Convert to object types, returning null if text is null.
     * @return converted
     * @throws StringValueConversionException
     */
    public final Character toOptionalCharacter() throws StringValueConversionException
    {
        return (text == null) ? null : toCharacter();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     */
    public final String toString(final String defaultValue)
    {
        return (text == null) ? defaultValue : text;
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final Duration toDuration(final Duration defaultValue)
            throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toDuration();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final Time toTime(final Time defaultValue) throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toTime();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final int toInt(final int defaultValue) throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toInt();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final long toLong(final long defaultValue) throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toLong();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final double toDouble(final double defaultValue) throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toDouble();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final boolean toBoolean(final boolean defaultValue)
            throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toBoolean();
    }

    /**
     * Convert to primitive types, returning default value if text is null.
     * @param defaultValue the default value to return of text is null
     * @return the converted text as a primitive or the default if text is null
     * @throws StringValueConversionException
     */
    public final char toChar(final char defaultValue) throws StringValueConversionException
    {
        return (text == null) ? defaultValue : toChar();
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17776.java