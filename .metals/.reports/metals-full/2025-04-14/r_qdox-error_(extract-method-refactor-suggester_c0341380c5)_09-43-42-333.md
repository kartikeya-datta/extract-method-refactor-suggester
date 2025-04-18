error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6909.java
text:
```scala
protected static F@@ieldCaseConversion globalFieldCaseConversion = FieldCaseConversion.NONE;

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.util.json;

import org.elasticsearch.util.Strings;
import org.elasticsearch.util.concurrent.NotThreadSafe;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.Date;

/**
 * A helper builder for JSON documents.
 *
 * <p>Best constructed using {@link #stringJsonBuilder()} or {@link #binaryJsonBuilder()}. When used to create
 * source for actions/operations, it is recommended to use {@link #binaryJsonBuilder()}.
 *
 * @author kimchy (shay.banon)
 */
@NotThreadSafe
public abstract class JsonBuilder<T extends JsonBuilder> {

    public static enum FieldCaseConversion {
        /**
         * No came conversion will occur.
         */
        NONE,
        /**
         * Camel Case will be converted to Underscore casing.
         */
        UNDERSCORE,
        /**
         * Underscore will be converted to Camel case conversion.
         */
        CAMELCASE
    }

    private final static DateTimeFormatter defaultDatePrinter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

    protected static FieldCaseConversion globalFieldCaseConversion = FieldCaseConversion.CAMELCASE;

    public static void globalFieldCaseConversion(FieldCaseConversion globalFieldCaseConversion) {
        JsonBuilder.globalFieldCaseConversion = globalFieldCaseConversion;
    }

    protected org.codehaus.jackson.JsonGenerator generator;

    protected T builder;

    protected FieldCaseConversion fieldCaseConversion = globalFieldCaseConversion;

    public static StringJsonBuilder stringJsonBuilder() throws IOException {
        return StringJsonBuilder.Cached.cached();
    }

    public static BinaryJsonBuilder jsonBuilder() throws IOException {
        return BinaryJsonBuilder.Cached.cached();
    }

    public static BinaryJsonBuilder binaryJsonBuilder() throws IOException {
        return BinaryJsonBuilder.Cached.cached();
    }

    public T fieldCaseConversion(FieldCaseConversion fieldCaseConversion) {
        this.fieldCaseConversion = fieldCaseConversion;
        return builder;
    }

    public T prettyPrint() {
        generator.useDefaultPrettyPrinter();
        return builder;
    }

    public T startObject(String name) throws IOException {
        field(name);
        startObject();
        return builder;
    }

    public T startObject() throws IOException {
        generator.writeStartObject();
        return builder;
    }

    public T endObject() throws IOException {
        generator.writeEndObject();
        return builder;
    }

    public T array(String name, String... values) throws IOException {
        startArray(name);
        for (String value : values) {
            value(value);
        }
        endArray();
        return builder;
    }

    public T array(String name, Object... values) throws IOException {
        startArray(name);
        for (Object value : values) {
            value(value);
        }
        endArray();
        return builder;
    }

    public T startArray(String name) throws IOException {
        field(name);
        startArray();
        return builder;
    }

    public T startArray() throws IOException {
        generator.writeStartArray();
        return builder;
    }

    public T endArray() throws IOException {
        generator.writeEndArray();
        return builder;
    }

    public T field(String name) throws IOException {
        if (fieldCaseConversion == FieldCaseConversion.UNDERSCORE) {
            name = Strings.toUnderscoreCase(name);
        } else if (fieldCaseConversion == FieldCaseConversion.CAMELCASE) {
            name = Strings.toCamelCase(name);
        }
        generator.writeFieldName(name);
        return builder;
    }

    public T field(String name, char[] value, int offset, int length) throws IOException {
        field(name);
        if (value == null) {
            generator.writeNull();
        } else {
            generator.writeString(value, offset, length);
        }
        return builder;
    }

    public T field(String name, String value) throws IOException {
        field(name);
        if (value == null) {
            generator.writeNull();
        } else {
            generator.writeString(value);
        }
        return builder;
    }

    public T field(String name, Integer value) throws IOException {
        return field(name, value.intValue());
    }

    public T field(String name, int value) throws IOException {
        field(name);
        generator.writeNumber(value);
        return builder;
    }

    public T field(String name, Long value) throws IOException {
        return field(name, value.longValue());
    }

    public T field(String name, long value) throws IOException {
        field(name);
        generator.writeNumber(value);
        return builder;
    }

    public T field(String name, Float value) throws IOException {
        return field(name, value.floatValue());
    }

    public T field(String name, float value) throws IOException {
        field(name);
        generator.writeNumber(value);
        return builder;
    }


    public T field(String name, Double value) throws IOException {
        return field(name, value.doubleValue());
    }

    public T field(String name, double value) throws IOException {
        field(name);
        generator.writeNumber(value);
        return builder;
    }

    public T field(String name, Object value) throws IOException {
        if (value == null) {
            nullField(name);
            return builder;
        }
        Class type = value.getClass();
        if (type == String.class) {
            field(name, (String) value);
        } else if (type == Float.class) {
            field(name, ((Float) value).floatValue());
        } else if (type == Double.class) {
            field(name, ((Double) value).doubleValue());
        } else if (type == Integer.class) {
            field(name, ((Integer) value).intValue());
        } else if (type == Long.class) {
            field(name, ((Long) value).longValue());
        } else if (type == Boolean.class) {
            field(name, ((Boolean) value).booleanValue());
        } else if (type == Date.class) {
            field(name, (Date) value);
        } else if (type == byte[].class) {
            field(name, (byte[]) value);
        } else if (value instanceof ReadableInstant) {
            field(name, (ReadableInstant) value);
        } else {
            field(name, value.toString());
        }
        return builder;
    }

    public T field(String name, boolean value) throws IOException {
        field(name);
        generator.writeBoolean(value);
        return builder;
    }

    public T field(String name, byte[] value) throws IOException {
        field(name);
        generator.writeBinary(value);
        return builder;
    }

    public T field(String name, ReadableInstant date) throws IOException {
        field(name);
        return value(date);
    }

    public T field(String name, ReadableInstant date, DateTimeFormatter formatter) throws IOException {
        field(name);
        return value(date, formatter);
    }

    public T field(String name, Date date) throws IOException {
        field(name);
        return value(date);
    }

    public T field(String name, Date date, DateTimeFormatter formatter) throws IOException {
        field(name);
        return value(date, formatter);
    }

    public T nullField(String name) throws IOException {
        generator.writeNullField(name);
        return builder;
    }

    public T nullValue() throws IOException {
        generator.writeNull();
        return builder;
    }

    public T raw(String json) throws IOException {
        generator.writeRaw(json);
        return builder;
    }

    public abstract T raw(byte[] json) throws IOException;

    public T value(Boolean value) throws IOException {
        return value(value.booleanValue());
    }

    public T value(boolean value) throws IOException {
        generator.writeBoolean(value);
        return builder;
    }

    public T value(ReadableInstant date) throws IOException {
        return value(date, defaultDatePrinter);
    }

    public T value(ReadableInstant date, DateTimeFormatter dateTimeFormatter) throws IOException {
        return value(dateTimeFormatter.print(date));
    }

    public T value(Date date) throws IOException {
        return value(date, defaultDatePrinter);
    }

    public T value(Date date, DateTimeFormatter dateTimeFormatter) throws IOException {
        return value(dateTimeFormatter.print(date.getTime()));
    }

    public T value(Integer value) throws IOException {
        return value(value.intValue());
    }

    public T value(int value) throws IOException {
        generator.writeNumber(value);
        return builder;
    }

    public T value(Long value) throws IOException {
        return value(value.longValue());
    }

    public T value(long value) throws IOException {
        generator.writeNumber(value);
        return builder;
    }

    public T value(Float value) throws IOException {
        return value(value.floatValue());
    }

    public T value(float value) throws IOException {
        generator.writeNumber(value);
        return builder;
    }

    public T value(Double value) throws IOException {
        return value(value.doubleValue());
    }

    public T value(double value) throws IOException {
        generator.writeNumber(value);
        return builder;
    }

    public T value(String value) throws IOException {
        generator.writeString(value);
        return builder;
    }

    public T value(byte[] value) throws IOException {
        generator.writeBinary(value);
        return builder;
    }

    public T value(Object value) throws IOException {
        Class type = value.getClass();
        if (type == String.class) {
            value((String) value);
        } else if (type == Float.class) {
            value(((Float) value).floatValue());
        } else if (type == Double.class) {
            value(((Double) value).doubleValue());
        } else if (type == Integer.class) {
            value(((Integer) value).intValue());
        } else if (type == Long.class) {
            value(((Long) value).longValue());
        } else if (type == Boolean.class) {
            value((Boolean) value);
        } else if (type == byte[].class) {
            value((byte[]) value);
        } else if (type == Date.class) {
            value((Date) value);
        } else if (value instanceof ReadableInstant) {
            value((ReadableInstant) value);
        } else {
            throw new IOException("Type not allowed [" + type + "]");
        }
        return builder;
    }

    public T flush() throws IOException {
        generator.flush();
        return builder;
    }

    public abstract T reset() throws IOException;

    public abstract byte[] unsafeBytes() throws IOException;

    public abstract int unsafeBytesLength() throws IOException;

    public abstract byte[] copiedBytes() throws IOException;

    public abstract String string() throws IOException;

    protected StringBuilder cachedStringBuilder() {
        return null;
    }

    public void close() {
        try {
            generator.close();
        } catch (IOException e) {
            // ignore
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6909.java