error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17771.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17771.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[334,80]

error in qdox parser
file content:
```java
offset: 8828
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17771.java
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
package wicket.util.size;


import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wicket.util.string.StringValue;
import wicket.util.string.StringValueConversionException;
import wicket.util.value.LongValue;

/**
 * Immutable byte count value.
 * @author Jonathan Locke
 */
public final class Bytes extends LongValue
{
	/** serialVersionUID. */
	private static final long serialVersionUID = -2131507164691475126L;
	
	/** Pattern for string parsing. */
    private static final Pattern PATTERN = Pattern.compile(
            "([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G|T)B?", Pattern.CASE_INSENSITIVE);

    /**
     * Private constructor forces use of static factory methods.
     * @param bytes Number of bytes
     */
    private Bytes(final long bytes)
    {
        super(bytes);
    }

    /**
     * Convert to Bytes object.
     * @param bytes to convert
     * @return input as Bytes
     */
    public static Bytes bytes(final long bytes)
    {
        return new Bytes(bytes);
    }

    /**
     * Convert to Bytes object.
     * @param kilobytes to convert
     * @return input as Bytes
     */
    public static Bytes kilobytes(final long kilobytes)
    {
        return bytes(kilobytes * 1024);
    }

    /**
     * Convert to Bytes object.
     * @param megabytes to convert
     * @return input as Bytes
     */
    public static Bytes megabytes(final long megabytes)
    {
        return kilobytes(megabytes * 1024);
    }

    /**
     * Convert to Bytes object.
     * @param gigabytes to convert
     * @return input as Bytes
     */
    public static Bytes gigabytes(final long gigabytes)
    {
        return megabytes(gigabytes * 1024);
    }

    /**
     * Convert to Bytes object.
     * @param terabytes to convert
     * @return input as Bytes
     */
    public static Bytes terabytes(final long terabytes)
    {
        return gigabytes(terabytes * 1024);
    }

    /**
     * Convert to Bytes object.
     * @param bytes to convert
     * @return input as Bytes
     */
    public static Bytes bytes(final double bytes)
    {
        return bytes(Math.round(bytes));
    }

    /**
     * Convert to Bytes object.
     * @param kilobytes to convert
     * @return input as Bytes
     */
    public static Bytes kilobytes(final double kilobytes)
    {
        return bytes(kilobytes * 1024.0);
    }

    /**
     * Convert to Bytes object.
     * @param megabytes to convert
     * @return input as Bytes
     */
    public static Bytes megabytes(final double megabytes)
    {
        return kilobytes(megabytes * 1024.0);
    }

    /**
     * Convert to Bytes object.
     * @param gigabytes to convert
     * @return input as Bytes
     */
    public static Bytes gigabytes(final double gigabytes)
    {
        return megabytes(gigabytes * 1024.0);
    }

    /**
     * Convert to Bytes object.
     * @param terabytes to convert
     * @return input as Bytes
     */
    public static Bytes terabytes(final double terabytes)
    {
        return gigabytes(terabytes * 1024.0);
    }

    /**
     * Gets the bytes.
     * @return bytes
     */
    public final long bytes()
    {
        return value;
    }

    /**
     * Gets the kilobytes.
     * @return the kilobytes
     */
    public final double kilobytes()
    {
        return value / 1024.0;
    }

    /**
     * Gets the megabytes.
     * @return the megabytes
     */
    public final double megabytes()
    {
        return kilobytes() / 1024.0;
    }

    /**
     * Gets the gigabytes.
     * @return the gigabytes
     */
    public final double gigabytes()
    {
        return megabytes() / 1024.0;
    }

    /**
     * Gets the terabytes.
     * @return the terabytes
     */
    public final double terabytes()
    {
        return gigabytes() / 1024.0;
    }

    /**
     * Converts a string to a number of bytes. Strings consist of a floating point value
     * followed by K, M, G or T for kilobytes, megabytes, gigabytes or terabytes,
     * respectively. The abbreviations KB, MB, GB and TB are also accepted. Matching is
     * case insensitive.
     * @param string The string to convert
     * @param locale The Locale to be used for transformation
     * @return The Bytes value for the string
     * @throws StringValueConversionException
     */
    public static Bytes valueOf(final String string, final Locale locale) throws StringValueConversionException
    {
        final Matcher matcher = PATTERN.matcher(string);

        if (matcher.matches())
        {
            try
            {
                final double value = NumberFormat.getNumberInstance(locale).parse(matcher.group(1))
                        .doubleValue();
                final String units = matcher.group(3);

                if (units.equalsIgnoreCase(""))
                {
                    return bytes(value);
                }
                else if (units.equalsIgnoreCase("K"))
                {
                    return kilobytes(value);
                }
                else if (units.equalsIgnoreCase("M"))
                {
                    return megabytes(value);
                }
                else if (units.equalsIgnoreCase("G"))
                {
                    return gigabytes(value);
                }
                else if (units.equalsIgnoreCase("T"))
                {
                    return terabytes(value);
                }
                else
                {
                    throw new StringValueConversionException("Units not recognized: " + string);
                }
            }
            catch (ParseException e)
            {
                throw new StringValueConversionException("Unable to parse numeric part: " + string,
                        e);
            }
        }
        else
        {
            throw new StringValueConversionException("Unable to parse bytes: " + string);
        }
    }

    /**
     * Converts a string to a number of bytes. Strings consist of a floating point value
     * followed by K, M, G or T for kilobytes, megabytes, gigabytes or terabytes,
     * respectively. The abbreviations KB, MB, GB and TB are also accepted. Matching is
     * case insensitive.
     * @param string The string to convert
     * @return The Bytes value for the string
     * @throws StringValueConversionException
     */
    public static Bytes valueOf(final String string) throws StringValueConversionException
    {
    	return valueOf(string, Locale.getDefault());
    }

    /**
     * Converts this byte count to a string using the default locale.
     * @return The string for this byte count
     */
    public String toString()
    {
    	return toString(Locale.getDefault());
    }

    /**
     * Converts this byte count to a string using the given locale.
     * @param locale locale for conversion
     * @return The string for this byte count
     */
    public String toString(final Locale locale)
    {
        if (value >= 0)
        {
            if (terabytes() >= 1.0)
            {
                return unitString(gigabytes(), "T", locale);
            }

            if (gigabytes() >= 1.0)
            {
                return unitString(gigabytes(), "G", locale);
            }

            if (megabytes() >= 1.0)
            {
                return unitString(megabytes(), "M", locale);
            }

            if (kilobytes() >= 1.0)
            {
                return unitString(kilobytes(), "K", locale);
            }

            return Long.toString(value) + " bytes";
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Convert value to formatted floating point number and units.
     * @param value The value
     * @param units The units
     * @param locale The locale
     * @return The formatted string
     */
    private String unitString(final double value, final String units, final Locale locale)
    {
        return StringValue.valueOf(value, locale) + units;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17771.java