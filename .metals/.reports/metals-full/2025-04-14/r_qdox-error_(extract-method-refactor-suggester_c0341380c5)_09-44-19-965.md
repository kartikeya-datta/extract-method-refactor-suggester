error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4439.java
text:
```scala
a@@ssert getClass() == getElementClass() : ""+getClass() + " != " + getElementClass();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.model;

import org.jboss.staxmapper.XMLContentWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * A generic model element.  Model elements are not generally thread-safe.
 *
 * @param <E> the concrete model element type
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractModelElement<E extends AbstractModelElement<E>> implements Serializable, Cloneable, XMLContentWriter, XMLStreamConstants {

    private static final long serialVersionUID = 66064050420378211L;

    // FIXME make non-transient and final when MSC-16 is fixed

    /**
     * Construct a new instance.
     */
    protected AbstractModelElement() {
        //assert getClass() == getElementClass() : ""+getClass() + " != " + getElementClass();
    }

    /**
     * Get an element hash consisting of the last 8 bytes of the given array.
     *
     * @param bytes the bytes
     * @return the element hash
     */
    protected static long calculateElementHashOf(byte[] bytes) {
        assert bytes.length >= 8;
        long h = 0L;
        final int offs = bytes.length - 8;
        for (int i = 0; i < 8; i ++) {
            h = h << 8 | bytes[offs + i] & 0xffL;
        }
        return h;
    }

    /**
     * Calculate the cumulative element hash of an array of objects.  Changing the order of the objects will change
     * the result.
     *
     * @param objects the objects
     * @param initial the base hash (can be 0)
     * @return the modified hash
     */
    protected static long calculateElementHashOf(final Object[] objects, long initial) {
        for (Object o : objects) {
            if (o != null) initial = Long.rotateLeft(initial, 1) ^ o.hashCode() & 0xffffffffL;
        }
        return initial;
    }

    /**
     * Calculate the cumulative element hash of an array of enums.  Changing the order of the objects will change
     * the result.
     *
     * @param enums the enums
     * @param initial the base hash (can be 0)
     * @return the modified hash
     */
    protected static long calculateElementHashOf(final Enum<?>[] enums, long initial) {
        for (Enum<?> e : enums) {
            if (e != null) initial = Long.rotateLeft(initial, 1) ^ e.ordinal() & 0xffffffffL;
        }
        return initial;
    }

    /**
     * Calculate the cumulative element hash of an iterable sequence of elements.  In order to return consistent
     * results, the sequence should be sorted in some predictable order.
     *
     * @param elements the elements
     * @param initial the base hash (can be 0)
     * @return the modified hash
     */
    protected static long calculateElementHashOf(Iterable<? extends AbstractModelElement<?>> elements, long initial) {
        for (AbstractModelElement<?> element : elements) {
            initial = Long.rotateLeft(initial, 1) ^ element.elementHash();
        }
        return initial;
    }

    private static char[] table = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Convert a byte array into a hex string.
     *
     * @param bytes the bytes
     * @return the string
     */
    protected static String bytesToHexString(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            builder.append(table[b >> 4 & 0x0f]).append(table[b & 0x0f]);
        }
        return builder.toString();
    }

    /**
     * Convert a hex string into a byte[].
     *
     * @param s the string
     * @return the bytes
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int x = Character.digit(s.charAt(j), 16) << 4;
            j++;
            x = x | Character.digit(s.charAt(j), 16);
            j++;
            data[i] = (byte) (x & 0xFF);
        }
        return data;
    }


    /**
     * Get an exception reporting an unexpected XML element.
     *
     * @param reader the stream reader
     * @return the exception
     */
    protected static XMLStreamException unexpectedElement(final XMLExtendedStreamReader reader) {
        return new XMLStreamException("Unexpected element '" + reader.getName() + "' encountered", reader.getLocation());
    }

    /**
     * Get an exception reporting an unexpected XML attribute.
     *
     * @param reader the stream reader
     * @param index the element index
     * @return the exception
     */
    protected static XMLStreamException unexpectedAttribute(final XMLExtendedStreamReader reader, final int index) {
        return new XMLStreamException("Unexpected attribute '" + reader.getAttributeName(index) + "' encountered", reader.getLocation());
    }

    /**
     * Get an exception reporting a missing, required XML attribute.
     *
     * @param reader the stream reader
     * @param required a set of enums whose toString method returns the attribute name
     * @return the exception
     */
    protected static XMLStreamException missingRequired(final XMLExtendedStreamReader reader, final Set<?> required) {
        final StringBuilder b = new StringBuilder();
        Iterator<?> iterator = required.iterator();
        while (iterator.hasNext()) {
            final Object o = iterator.next();
            b.append(o.toString());
            if (iterator.hasNext()) {
                b.append(", ");
            }
        }
        return new XMLStreamException("Missing required attribute(s): " + b, reader.getLocation());
    }

    /**
     * Get an exception reporting a missing, required XML child element.
     *
     * @param reader the stream reader
     * @param required a set of enums whose toString method returns the attribute name
     * @return the exception
     */
    protected static XMLStreamException missingRequiredElement(final XMLExtendedStreamReader reader, final Set<?> required) {
        final StringBuilder b = new StringBuilder();
        Iterator<?> iterator = required.iterator();
        while (iterator.hasNext()) {
            final Object o = iterator.next();
            b.append(o.toString());
            if (iterator.hasNext()) {
                b.append(", ");
            }
        }
        return new XMLStreamException("Missing required element(s): " + b, reader.getLocation());
    }

    /**
     * Checks that the current element has no attributes, throwing an {@link XMLStreamException}
     * if one is found.
     *
     * @param reader the reader
     * @throws XMLStreamException if an error occurs
     */
    protected static void requireNoAttributes(final XMLExtendedStreamReader reader) throws XMLStreamException {
        if (reader.getAttributeCount() > 0) {
            throw unexpectedAttribute(reader, 0);
        }
    }

    /**
     * Consumes the remainder of the current element, throwing an {@link XMLStreamException}
     * if it contains any child elements.
     *
     * @param reader the reader
     * @throws XMLStreamException if an error occurs
     */
    protected static void requireNoContent(final XMLExtendedStreamReader reader) throws XMLStreamException {
        if (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            throw unexpectedElement(reader);
        }
    }

    /**
     * Get an exception reporting that an element of a given type and name has already been declared in this scope.
     *
     * @param reader the stream reader
     * @param name the name that was redeclared
     * @return the exception
     */
    protected static XMLStreamException duplicateNamedElement(final XMLExtendedStreamReader reader, final String name) {
        return new XMLStreamException("An element of this type named '" + name + "' has already been declared", reader.getLocation());
    }

    /**
     * Read an element which contains only a single boolean attribute.
     *
     * @param reader the reader
     * @param attributeName the attribute name, usually "value"
     * @return the boolean value
     * @throws XMLStreamException if an error occurs or if the element does not
     *            contain the specified attribute, contains other attributes,
     *            or contains child elements.
     */
    protected static boolean readBooleanAttributeElement(final XMLExtendedStreamReader reader, final String attributeName) throws XMLStreamException {
        requireSingleAttribute(reader, attributeName);
        final boolean value = Boolean.parseBoolean(reader.getAttributeValue(0));
        requireNoContent(reader);
        return value;
    }

    /**
     * Read an element which contains only a single string attribute.
     *
     * @param reader the reader
     * @param attributeName the attribute name, usually "value" or "name"
     * @return the string value
     * @throws XMLStreamException if an error occurs or if the element does not
     *            contain the specified attribute, contains other attributes,
     *            or contains child elements.
     */
    protected static String readStringAttributeElement(final XMLExtendedStreamReader reader, final String attributeName) throws XMLStreamException {
        requireSingleAttribute(reader, attributeName);
        final String value = reader.getAttributeValue(0);
        requireNoContent(reader);
        return value;
    }

    /**
     * Read an element which contains only a single list attribute of a given type.
     *
     * @param reader the reader
     * @param attributeName the attribute name, usually "value"
     * @param type the value type class
     * @param <T> the value type
     * @return the value list
     * @throws XMLStreamException if an error occurs or if the element does not
     *            contain the specified attribute, contains other attributes,
     *            or contains child elements.
     */
    @SuppressWarnings({ "unchecked" })
    protected static <T> List<T> readListAttributeElement(final XMLExtendedStreamReader reader, final String attributeName, final Class<T> type) throws XMLStreamException {
        requireSingleAttribute(reader, attributeName);
        // todo: fix this when this method signature is corrected
        final List<T> value = (List<T>) reader.getListAttributeValue(0, type);
        requireNoContent(reader);
        return value;
    }

    /**
     * Read an element which contains only a single list attribute of a given type, returning it as an array.
     *
     * @param reader the reader
     * @param attributeName the attribute name, usually "value"
     * @param type the value type class
     * @param <T> the value type
     * @return the value list as an array
     * @throws XMLStreamException if an error occurs or if the element does not
     *            contain the specified attribute, contains other attributes,
     *            or contains child elements.
     */
    @SuppressWarnings({ "unchecked" })
    protected static <T> T[] readArrayAttributeElement(final XMLExtendedStreamReader reader, final String attributeName, final Class<T> type) throws XMLStreamException {
        final List<T> list = readListAttributeElement(reader, attributeName, type);
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    /**
     * Require that the current element have only a single attribute with the given name.
     *
     * @param reader the reader
     * @param attributeName the attribute name
     * @throws XMLStreamException if an error occurs
     */
    private static void requireSingleAttribute(final XMLExtendedStreamReader reader, final String attributeName) throws XMLStreamException {
        final int count = reader.getAttributeCount();
        if (count == 0) {
            throw missingRequired(reader, Collections.singleton(attributeName));
        }
        if (reader.getAttributeNamespace(0) != null || ! attributeName.equals(reader.getAttributeLocalName(0))) {
            throw unexpectedAttribute(reader, 0);
        }
        if (count > 1) {
            throw unexpectedAttribute(reader, 1);
        }
    }

    protected static Map<String, NamespaceAttribute> readNamespaces(final XMLExtendedStreamReader reader) {
        int count = reader.getNamespaceCount();
        Map<String, NamespaceAttribute> result = new HashMap<String, NamespaceAttribute>();
        for (int i = 0; i < count; i++) {
            String prefix = reader.getNamespacePrefix(i);
            String uri = reader.getNamespaceURI(i);
            result.put(uri, new NamespaceAttribute(prefix, uri));
        }
        return result;
    }

    protected static String readSchemaLocation(final XMLExtendedStreamReader reader) throws XMLStreamException {
        final int count = reader.getAttributeCount();
        if (count == 0) {
            return null;
        }
        String loc = null;
        for (int i = 0; i < count; i++) {
            if ("http://www.w3.org/2001/XMLSchema-instance".equals(reader.getAttributeNamespace(i))
                    && "schemaLocation".equals(reader.getAttributeLocalName(i))) {
                loc = reader.getAttributeValue(i);
            }
        }
        return loc;
    }


    /**
     * Returns a new {@link TreeMap} by passing the provided map to its constructor.
     * Thread safety note: <code>toCopy</code>'s monitor is held while the TreeMap
     * is being constructed.
     *
     * @param <K> the type of <code>toCopy</code>'s keys
     * @param <V> the type of <code>toCopy</code>'s values
     * @param toCopy the map to copy. Cannot be <code>null</code>
     * @return the copy
     */
    protected static <K, V> NavigableMap<K, V> safeCopyMap(NavigableMap<K, V> toCopy) {
        synchronized (toCopy) {
            return new TreeMap<K, V>(toCopy);
        }
    }

    /**
     * Calculate a hash of this model element's complete contents.  This value is used to verify the state of the model
     * after applying a change; it should be unlikely (but is not guaranteed to be) to return the same value for two complete model
     * representations that differ by either small or large changes.
     *
     * @return the checksum
     */
    public abstract long elementHash();

    /**
     * Get this instance, cast to the concrete type.
     *
     * @return the concrete instance
     */
    public final E cast() {
        return getElementClass().cast(this);
    }

    /**
     * Cast another instance to this concrete type.
     *
     * @return the other instance
     */
    public final E cast(Object other) {
        return getElementClass().cast(other);
    }

    /**
     * Get the concrete class of the element.
     *
     * @return the concrete class
     */
    protected abstract Class<E> getElementClass();

    /**
     * Write the content for this type.  The start element will have already been written.
     *
     * @param streamWriter the stream writer
     * @throws XMLStreamException if an error occurs
     */
    public abstract void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException;

    /**
     * Determine if this object is the same as the given object.  This is an identity comparison.
     *
     * @param obj the other object
     * @return {@code true} if the objects are the same
     */
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }

    /**
     * Get the identity hash code of this object.
     *
     * @return the identity hash code
     */
    public final int hashCode() {
        return super.hashCode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4439.java