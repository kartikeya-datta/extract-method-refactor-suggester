error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/861.java
text:
```scala
public static C@@omplexFormat getInstance() {

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

package org.apache.commons.math.complex;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.math.util.CompositeFormat;

/**
 * Formats a Complex number in cartesian format "Re(c) + Im(c)i".  'i' can
 * be replaced with 'j' (or anything else), and the number format for both real
 * and imaginary parts can be configured.
 *
 * @author Apache Software Foundation
 * @version $Revision$ $Date$
 */
public class ComplexFormat extends CompositeFormat {
    
    /** Serializable version identifier */
    private static final long serialVersionUID = -3343698360149467646L;

     /** The default imaginary character. */
    private static final String DEFAULT_IMAGINARY_CHARACTER = "i";
    
    /** The notation used to signify the imaginary part of the complex number. */
    private String imaginaryCharacter;
    
    /** The format used for the imaginary part. */
    private NumberFormat imaginaryFormat;

    /** The format used for the real part. */
    private NumberFormat realFormat;
    
    /**
     * Create an instance with the default imaginary character, 'i', and the
     * default number format for both real and imaginary parts.
     */
    public ComplexFormat() {
        this(DEFAULT_IMAGINARY_CHARACTER, getDefaultNumberFormat());
    }

    /**
     * Create an instance with a custom number format for both real and
     * imaginary parts.
     * @param format the custom format for both real and imaginary parts.
     */
    public ComplexFormat(NumberFormat format) {
        this(DEFAULT_IMAGINARY_CHARACTER, format);
    }
    
    /**
     * Create an instance with a custom number format for the real part and a
     * custom number format for the imaginary part.
     * @param realFormat the custom format for the real part.
     * @param imaginaryFormat the custom format for the imaginary part.
     */
    public ComplexFormat(NumberFormat realFormat, NumberFormat imaginaryFormat) {
        this(DEFAULT_IMAGINARY_CHARACTER, realFormat, imaginaryFormat);
    }
    
    /**
     * Create an instance with a custom imaginary character, and the default
     * number format for both real and imaginary parts.
     * @param imaginaryCharacter The custom imaginary character.
     */
    public ComplexFormat(String imaginaryCharacter) {
        this(imaginaryCharacter, getDefaultNumberFormat());
    }
    
    /**
     * Create an instance with a custom imaginary character, and a custom number
     * format for both real and imaginary parts.
     * @param imaginaryCharacter The custom imaginary character.
     * @param format the custom format for both real and imaginary parts.
     */
    public ComplexFormat(String imaginaryCharacter, NumberFormat format) {
        this(imaginaryCharacter, format, (NumberFormat)format.clone());
    }
    
    /**
     * Create an instance with a custom imaginary character, a custom number
     * format for the real part, and a custom number format for the imaginary
     * part.
     * @param imaginaryCharacter The custom imaginary character.
     * @param realFormat the custom format for the real part.
     * @param imaginaryFormat the custom format for the imaginary part.
     */
    public ComplexFormat(String imaginaryCharacter, NumberFormat realFormat,
            NumberFormat imaginaryFormat) {
        super();
        setImaginaryCharacter(imaginaryCharacter);
        setImaginaryFormat(imaginaryFormat);
        setRealFormat(realFormat);
    }

    /**
     * Get the set of locales for which complex formats are available.
     * <p>This is the same set as the {@link NumberFormat} set.</p> 
     * @return available complex format locales.
     */
    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    /**
     * This static method calls {@link #format(Object)} on a default instance of
     * ComplexFormat.
     *
     * @param c Complex object to format
     * @return A formatted number in the form "Re(c) + Im(c)i"
     */
    public static String formatComplex(Complex c) {
        return getInstance().format(c);
    }
    
    /**
     * Formats a {@link Complex} object to produce a string.
     *
     * @param complex the object to format.
     * @param toAppendTo where the text is to be appended
     * @param pos On input: an alignment field, if desired. On output: the
     *            offsets of the alignment field
     * @return the value passed in as toAppendTo.
     */
    public StringBuffer format(Complex complex, StringBuffer toAppendTo,
            FieldPosition pos) {
        
        pos.setBeginIndex(0);
        pos.setEndIndex(0);

        // format real
        double re = complex.getReal();
        formatDouble(re, getRealFormat(), toAppendTo, pos);
        
        // format sign and imaginary
        double im = complex.getImaginary();
        if (im < 0.0) {
            toAppendTo.append(" - ");
            formatDouble(-im, getImaginaryFormat(), toAppendTo, pos);
            toAppendTo.append(getImaginaryCharacter());
        } else if (im > 0.0 || Double.isNaN(im)) {
            toAppendTo.append(" + ");
            formatDouble(im, getImaginaryFormat(), toAppendTo, pos);
            toAppendTo.append(getImaginaryCharacter());
        }
        
        return toAppendTo;
    }
    
    /**
     * Formats a object to produce a string.  <code>obj</code> must be either a 
     * {@link Complex} object or a {@link Number} object.  Any other type of
     * object will result in an {@link IllegalArgumentException} being thrown.
     *
     * @param obj the object to format.
     * @param toAppendTo where the text is to be appended
     * @param pos On input: an alignment field, if desired. On output: the
     *            offsets of the alignment field
     * @return the value passed in as toAppendTo.
     * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
     * @throws IllegalArgumentException is <code>obj</code> is not a valid type.
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
            FieldPosition pos) {
        
        StringBuffer ret = null;
        
        if (obj instanceof Complex) {
            ret = format( (Complex)obj, toAppendTo, pos);
        } else if (obj instanceof Number) {
            ret = format( new Complex(((Number)obj).doubleValue(), 0.0),
                toAppendTo, pos);
        } else { 
            throw new IllegalArgumentException("Cannot format given Object as a Complex");
        }
        
        return ret;
    }

    /**
     * Access the imaginaryCharacter.
     * @return the imaginaryCharacter.
     */
    public String getImaginaryCharacter() {
        return imaginaryCharacter;
    }
    
    /**
     * Access the imaginaryFormat.
     * @return the imaginaryFormat.
     */
    public NumberFormat getImaginaryFormat() {
        return imaginaryFormat;
    }
    
    /**
     * Returns the default complex format for the current locale.
     * @return the default complex format.
     */
    public static CompositeFormat getInstance() {
        return getInstance(Locale.getDefault());
    }
    
    /**
     * Returns the default complex format for the given locale.
     * @param locale the specific locale used by the format.
     * @return the complex format specific to the given locale.
     */
    public static ComplexFormat getInstance(Locale locale) {
        NumberFormat f = getDefaultNumberFormat(locale);
        return new ComplexFormat(f);
    }
    
    /**
     * Access the realFormat.
     * @return the realFormat.
     */
    public NumberFormat getRealFormat() {
        return realFormat;
    }

    /**
     * Parses a string to produce a {@link Complex} object.
     *
     * @param source the string to parse
     * @return the parsed {@link Complex} object.
     * @exception ParseException if the beginning of the specified string
     *            cannot be parsed.
     */
    public Complex parse(String source) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Complex result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new ParseException("Unparseable complex number: \"" + source +
                                     "\"", parsePosition.getErrorIndex());
        }
        return result;
    }
    
    /**
     * Parses a string to produce a {@link Complex} object.
     *
     * @param source the string to parse
     * @param pos input/ouput parsing parameter.
     * @return the parsed {@link Complex} object.
     */
    public Complex parse(String source, ParsePosition pos) {
        int initialIndex = pos.getIndex();

        // parse whitespace
        parseAndIgnoreWhitespace(source, pos);

        // parse real
        Number re = parseNumber(source, getRealFormat(), pos);
        if (re == null) {
            // invalid real number
            // set index back to initial, error index should already be set
            pos.setIndex(initialIndex);
            return null;
        }

        // parse sign
        int startIndex = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        int sign = 0;
        switch (c) {
        case 0 :
            // no sign
            // return real only complex number
            return new Complex(re.doubleValue(), 0.0);
        case '-' :
            sign = -1;
            break;
        case '+' :
            sign = 1;
            break;
        default :
            // invalid sign
            // set index back to initial, error index should be the last
            // character examined.
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
        }

        // parse whitespace
        parseAndIgnoreWhitespace(source, pos);

        // parse imaginary
        Number im = parseNumber(source, getRealFormat(), pos);
        if (im == null) {
            // invalid imaginary number
            // set index back to initial, error index should already be set
            pos.setIndex(initialIndex);
            return null;
        }

        // parse imaginary character
        if (!parseFixedstring(source, getImaginaryCharacter(), pos)) {
            return null;
        }

        return new Complex(re.doubleValue(), im.doubleValue() * sign);

    }
     
    /**
     * Parses a string to produce a object.
     *
     * @param source the string to parse
     * @param pos input/ouput parsing parameter.
     * @return the parsed object.
     * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
     */
    public Object parseObject(String source, ParsePosition pos) {
        return parse(source, pos);
    }

    /**
     * Modify the imaginaryCharacter.
     * @param imaginaryCharacter The new imaginaryCharacter value.
     * @throws IllegalArgumentException if <code>imaginaryCharacter</code> is
     *         <code>null</code> or an empty string.
     */
    public void setImaginaryCharacter(String imaginaryCharacter) {
        if (imaginaryCharacter == null || imaginaryCharacter.length() == 0) {
            throw new IllegalArgumentException(
                "imaginaryCharacter must be a non-empty string.");
        }
        this.imaginaryCharacter = imaginaryCharacter;
    }
    
    /**
     * Modify the imaginaryFormat.
     * @param imaginaryFormat The new imaginaryFormat value.
     * @throws IllegalArgumentException if <code>imaginaryFormat</code> is
     *         <code>null</code>.
     */
    public void setImaginaryFormat(NumberFormat imaginaryFormat) {
        if (imaginaryFormat == null) {
            throw new IllegalArgumentException(
                "imaginaryFormat can not be null.");
        }
        this.imaginaryFormat = imaginaryFormat;
    }
    
    /**
     * Modify the realFormat.
     * @param realFormat The new realFormat value.
     * @throws IllegalArgumentException if <code>realFormat</code> is
     *         <code>null</code>.
     */
    public void setRealFormat(NumberFormat realFormat) {
        if (realFormat == null) {
            throw new IllegalArgumentException(
                "realFormat can not be null.");
        }
        this.realFormat = realFormat;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/861.java