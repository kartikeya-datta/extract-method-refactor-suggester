error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 874
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8181.java
text:
```scala
private static final class XSNamedMapEntry implements Map.Entry {

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

p@@ackage org.apache.xerces.impl.xs.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;

/**
 * Contains the map between qnames and XSObject's.
 *
 * @xerces.internal 
 *
 * @author Sandy Gao, IBM
 * @author Michael Glavassevich, IBM
 *
 * @version $Id$
 */
public class XSNamedMapImpl extends AbstractMap implements XSNamedMap {

    /**
     * An immutable empty map.
     */
    public static final XSNamedMapImpl EMPTY_MAP = new XSNamedMapImpl(new XSObject[0], 0);
    
    // components of these namespaces are stored in this map
    final String[] fNamespaces;
    // number of namespaces
    final int fNSNum;
    // each entry contains components in one namespace
    final SymbolHash[] fMaps;
    // store all components from all namespace.
    // used when this map is accessed as a list.
    XSObject[] fArray = null;
    // store the number of components.
    // used when this map is accessed as a list.
    int fLength = -1;
    // Set of Map.Entry<QName,XSObject> for the java.util.Map methods
    private Set fEntrySet = null;
    
    /**
     * Construct an XSNamedMap implementation for one namespace
     * 
     * @param namespace the namespace to which the components belong
     * @param map       the map from local names to components
     */
    public XSNamedMapImpl(String namespace, SymbolHash map) {
        fNamespaces = new String[] {namespace};
        fMaps = new SymbolHash[] {map};
        fNSNum = 1;
    }

    /**
     * Construct an XSNamedMap implementation for a list of namespaces
     * 
     * @param namespaces the namespaces to which the components belong
     * @param maps       the maps from local names to components
     * @param num        the number of namespaces
     */
    public XSNamedMapImpl(String[] namespaces, SymbolHash[] maps, int num) {
        fNamespaces = namespaces;
        fMaps = maps;
        fNSNum = num;
    }

    /**
     * Construct an XSNamedMap implementation one namespace from an array
     * 
     * @param array     containing all components
     * @param length    number of components
     */
    public XSNamedMapImpl(XSObject[] array, int length) {
        if (length == 0) {
            fNamespaces = null;
            fMaps = null;
            fNSNum = 0;
            fArray = array;
            fLength = 0;
            return;
        }
        // because all components are from the same target namesapce,
        // get the namespace from the first one.
        fNamespaces = new String[]{array[0].getNamespace()};
        fMaps = null;
        fNSNum = 1;
        // copy elements to the Vector
        fArray = array;
        fLength = length;
    }

    /**
     * The number of <code>XSObjects</code> in the <code>XSObjectList</code>. 
     * The range of valid child object indices is 0 to <code>length-1</code> 
     * inclusive. 
     */
    public synchronized int getLength() {
        if (fLength == -1) {
            fLength = 0;
            for (int i = 0; i < fNSNum; i++) {
                fLength += fMaps[i].getLength();
            }
        }
        return fLength;
    }

    /**
     * Retrieves an <code>XSObject</code> specified by local name and 
     * namespace URI.
     * <br>Per XML Namespaces, applications must use the value <code>null</code> as the 
     * <code>namespace</code> parameter for methods if they wish to specify 
     * no namespace.
     * @param namespace The namespace URI of the <code>XSObject</code> to 
     *   retrieve, or <code>null</code> if the <code>XSObject</code> has no 
     *   namespace. 
     * @param localName The local name of the <code>XSObject</code> to 
     *   retrieve.
     * @return A <code>XSObject</code> (of any type) with the specified local 
     *   name and namespace URI, or <code>null</code> if they do not 
     *   identify any object in this map.
     */
    public XSObject itemByName(String namespace, String localName) {
        for (int i = 0; i < fNSNum; i++) {
            if (isEqual(namespace, fNamespaces[i])) {
                // when this map is created from SymbolHash's
                // get the component from SymbolHash
                if (fMaps != null) {
                    return (XSObject)fMaps[i].get(localName);
                }
                // Otherwise (it's created from an array)
                // go through the array to find a matching name
                XSObject ret;
                for (int j = 0; j < fLength; j++) {
                    ret = fArray[j];
                    if (ret.getName().equals(localName)) {
                        return ret;
                    }
                }
                return null;
            }
        }
        return null;
    }

    /**
     * Returns the <code>index</code>th item in the collection or 
     * <code>null</code> if <code>index</code> is greater than or equal to 
     * the number of objects in the list. The index starts at 0. 
     * @param index  index into the collection. 
     * @return  The <code>XSObject</code> at the <code>index</code>th 
     *   position in the <code>XSObjectList</code>, or <code>null</code> if 
     *   the index specified is not valid. 
     */
    public synchronized XSObject item(int index) {
        if (fArray == null) {
            // calculate the total number of elements
            getLength();
            fArray = new XSObject[fLength];
            int pos = 0;
            // get components from all SymbolHashes
            for (int i = 0; i < fNSNum; i++) {
                pos += fMaps[i].getValues(fArray, pos);
            }
        }
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fArray[index];
    }
    
    static boolean isEqual(String one, String two) {
        return (one != null) ? one.equals(two) : (two == null);
    }
    
    /*
     * java.util.Map methods
     */
    
    public boolean containsKey(Object key) {
        return (get(key) != null);
    }
    
    public Object get(Object key) {
        if (key instanceof QName) {
            final QName name = (QName) key;
            String namespaceURI = name.getNamespaceURI();
            if (XMLConstants.NULL_NS_URI.equals(namespaceURI)) {
                namespaceURI = null;
            }
            String localPart = name.getLocalPart();
            return itemByName(namespaceURI, localPart);
        }
        return null;
    }
    
    public int size() {
        return getLength();
    }

    public synchronized Set entrySet() {
        // Defer creation of the entry set until it is actually needed.
        if (fEntrySet == null) {
            final int length = getLength();
            final XSNamedMapEntry[] entries = new XSNamedMapEntry[length];
            for (int i = 0; i < length; ++i) {
                XSObject xso = item(i);
                entries[i] = new XSNamedMapEntry(new QName(xso.getNamespace(), xso.getName()), xso);
            }
            // Create a view of this immutable map.
            fEntrySet = new AbstractSet() {
                public Iterator iterator() {
                    return new Iterator() {
                        private int index = 0;
                        public boolean hasNext() {
                            return (index < length);
                        }
                        public Object next() {
                            if (index < length) {
                                return entries[index++];
                            }
                            throw new NoSuchElementException();
                        }
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
                public int size() {
                    return length;
                }
            };
        }
        return fEntrySet;
    }
    
    /** An entry in the XSNamedMap. **/
    private final class XSNamedMapEntry implements Map.Entry {
        private final QName key;
        private final XSObject value;
        public XSNamedMapEntry(QName key, XSObject value) {
            this.key = key;
            this.value = value;
        }
        public Object getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }
        public boolean equals(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry e = (Map.Entry) o;
                Object otherKey = e.getKey();
                Object otherValue = e.getValue();
                return (key == null ? otherKey == null : key.equals(otherKey)) &&
                    (value == null ? otherValue == null : value.equals(otherValue));
            }
            return false;
        }
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) 
                ^ (value == null ? 0 : value.hashCode());
        }
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(String.valueOf(key));
            buffer.append('=');
            buffer.append(String.valueOf(value));
            return buffer.toString();
        }
    }
    
} // class XSNamedMapImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8181.java