error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[162,2]

error in qdox parser
file content:
```java
offset: 5002
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17711.java
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


import java.text.ParseException;

import java.util.Locale;

import wicket.util.convert.Formatter;

/**
 * base class for localized converters.
 */
public abstract class BaseLocaleConverter implements LocaleConverter, Formatter
{
    // ----------------------------------------------------- Instance Variables

    /** The locale specified to our Constructor, by default - system locale. */
    protected Locale locale = Locale.getDefault();

    /** The default pattern specified to our Constructor, if any. */
    protected String pattern = null;

    /** The flag indicating whether the given pattern string is localized or not. */
    protected boolean locPattern = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link LocaleConverter}that will throw a {@link wicket.util.convert.ConversionException}if a
     * conversion error occurs. An unlocalized pattern is used for the convertion.
     * @param locale The locale
     * @param pattern The convertion pattern
     */
    protected BaseLocaleConverter(Locale locale, String pattern)
    {
        this(locale, pattern, false);
    }

    /**
     * Create a {@link LocaleConverter}that will throw a {@link wicket.util.convert.ConversionException}if
     * an conversion error occurs.
     * @param locale The locale
     * @param pattern The convertion pattern
     * @param locPattern Indicate whether the pattern is localized or not
     */
    protected BaseLocaleConverter(Locale locale, String pattern, boolean locPattern)
    {
        if (locale != null)
        {
            this.locale = locale;
        }

        this.pattern = pattern;
        this.locPattern = locPattern;
    }

    // --------------------------------------------------------- Methods

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     * @return parsed object
     * @throws ParseException
     * @exception wicket.util.convert.ConversionException if conversion cannot be performed successfully
     */
    abstract protected Object parse(Object value, String pattern) throws ParseException;

    /**
     * Convert the specified locale-sensitive input object into an output object. The
     * default pattern is used for the convertion.
     * @param value The input object to be converted
     * @return converted object
     * @exception wicket.util.convert.ConversionException if conversion cannot be performed successfully
     */
    public Object convert(Object value)
    {
        return convert(value, null);
    }

    /**
     * Convert the specified locale-sensitive input object into an output object.
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     * @return converted object
     * @exception wicket.util.convert.ConversionException if conversion cannot be performed successfully
     */
    public Object convert(Object value, String pattern)
    {
        return convert(null, value, pattern);
    }

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type. The default pattern is used for the convertion.
     * @param type Data type to which this value should be converted
     * @param value The input object to be converted
     * @return converted object
     * @exception wicket.util.convert.ConversionException if conversion cannot be performed successfully
     */
    public Object convert(Class type, Object value)
    {
        return convert(type, value, null);
    }

    /**
     * get the locale
     * @return Locale
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * get the pattern
     * @return String
     */
    public String getPattern()
    {
        return pattern;
    }

    /**
     * set the locale
     * @param locale
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    /**
     * set the pattern
     * @param string
     */
    public void setPattern(String string)
    {
        pattern = string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17711.java