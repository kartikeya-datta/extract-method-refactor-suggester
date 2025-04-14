error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1369.java
text:
```scala
i@@f (line.startsWith("#") || JOrphanUtils.isBlank(line)) {// $NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.http.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.util.JOrphanUtils;

/**
 * This class provides an interface to headers file to pass HTTP headers along
 * with a request.
 *
 * @version $Revision$
 */
public class HeaderManager extends ConfigTestElement implements Serializable {

    private static final long serialVersionUID = 240L;

    public static final String HEADERS = "HeaderManager.headers";// $NON-NLS-1$

    private static final String[] COLUMN_RESOURCE_NAMES = {
          "name",             // $NON-NLS-1$
          "value"             // $NON-NLS-1$
        };

    private static final int COLUMN_COUNT = COLUMN_RESOURCE_NAMES.length;


    /**
     * Apache SOAP driver does not provide an easy way to get and set the cookie
     * or HTTP header. Therefore it is necessary to store the SOAPHTTPConnection
     * object and reuse it.
     */
    private Object SOAPHeader = null;

    public HeaderManager() {
        setProperty(new CollectionProperty(HEADERS, new ArrayList<Object>()));
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        super.clear();
        setProperty(new CollectionProperty(HEADERS, new ArrayList<Object>()));
    }

    /**
     * Get the collection of JMeterProperty entries representing the headers.
     *
     * @return the header collection property
     */
    public CollectionProperty getHeaders() {
        return (CollectionProperty) getProperty(HEADERS);
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public String getColumnName(int column) {
        return COLUMN_RESOURCE_NAMES[column];
    }

    public Class<? extends String> getColumnClass(int column) {
        return COLUMN_RESOURCE_NAMES[column].getClass();
    }

    public Header getHeader(int row) {
        return (Header) getHeaders().get(row).getObjectValue();
    }

    /**
     * Save the header data to a file.
     */
    public void save(String headFile) throws IOException {
        File file = new File(headFile);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("user.dir")// $NON-NLS-1$
                    + File.separator + headFile);
        }
        PrintWriter writer = new PrintWriter(new FileWriter(file)); // TODO Charset ?
        writer.println("# JMeter generated Header file");// $NON-NLS-1$
        final CollectionProperty hdrs = getHeaders();
        for (int i = 0; i < hdrs.size(); i++) {
            final JMeterProperty hdr = hdrs.get(i);
            Header head = (Header) hdr.getObjectValue();
            writer.println(head.toString());
        }
        writer.flush();
        writer.close();
    }

    /**
     * Add header data from a file.
     */
    public void addFile(String headerFile) throws IOException {
        File file = new File(headerFile);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("user.dir")// $NON-NLS-1$
                    + File.separator + headerFile);
        }
        if (!file.canRead()) {
            throw new IOException("The file you specified cannot be read.");
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file)); // TODO Charset ?
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.startsWith("#") || line.trim().length() == 0) {// $NON-NLS-1$
                        continue;
                    }
                    String[] st = JOrphanUtils.split(line, "\t", " ");// $NON-NLS-1$ $NON-NLS-2$
                    int name = 0;
                    int value = 1;
                    Header header = new Header(st[name], st[value]);
                    getHeaders().addItem(header);
                } catch (Exception e) {
                    throw new IOException("Error parsing header line\n\t'" + line + "'\n\t" + e);
                }
            }
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * Add a header.
     */
    public void add(Header h) {
        getHeaders().addItem(h);
    }

    /**
     * Add an empty header.
     */
    public void add() {
        getHeaders().addItem(new Header());
    }

    /**
     * Remove a header.
     */
    public void remove(int index) {
        getHeaders().remove(index);
    }

    /**
     * Return the number of headers.
     */
    public int size() {
        return getHeaders().size();
    }

    /**
     * Return the header at index i.
     */
    public Header get(int i) {
        return (Header) getHeaders().get(i).getObjectValue();
    }

    /**
     * Remove from Headers the header named name
     * @param name header name
     */
    public void removeHeaderNamed(String name) {
        List<Integer> removeIndices = new ArrayList<Integer>();
        for (int i = getHeaders().size() - 1; i >= 0; i--) {
            Header header = (Header) getHeaders().get(i).getObjectValue();
            if (header == null) {
                continue;
            }
            if (header.getName().equalsIgnoreCase(name)) {
                removeIndices.add(Integer.valueOf(i));
            }
        }
        for (Integer indice : removeIndices) {
        	getHeaders().remove(indice.intValue());
		}
    }

    /**
     * Added support for SOAP related header stuff. 1-29-04 Peter Lin
     *
     * @return the SOAP header Object
     */
    public Object getSOAPHeader() {
        return this.SOAPHeader;
    }

    /**
     * Set the SOAPHeader with the SOAPHTTPConnection object. We may or may not
     * want to rename this to setHeaderObject(Object). Concievably, other
     * samplers may need this kind of functionality. 1-29-04 Peter Lin
     *
     * @param header
     */
    public void setSOAPHeader(Object header) {
        this.SOAPHeader = header;
    }

    /**
     * Merge the attributes with a another HeaderManager's attributes.
     * @param element The object to be merged with
     * @param preferLocalValues When both objects have a value for the
     *        same attribute, this flag determines which value is preferresd.
     */
    public HeaderManager merge(TestElement element, boolean preferLocalValues) {
        if (!(element instanceof HeaderManager)) {
            throw new IllegalArgumentException("Cannot merge type:" + this.getClass().getName() + " with type:" + element.getClass().getName());
        }

        // start off with a merged object as a copy of the local object
        HeaderManager merged = (HeaderManager)this.clone();

        HeaderManager other = (HeaderManager)element;
        // iterate thru each of the other headers
        for (int i = 0; i < other.getHeaders().size(); i++) {
            Header otherHeader = other.get(i);
            boolean found = false;
            // find the same property in the local headers
            for (int j = 0; j < merged.getHeaders().size(); j++) {
                Header mergedHeader = merged.get(j);
                if (mergedHeader.getName().equalsIgnoreCase(otherHeader.getName())) {
                    // we have a match
                    found = true;
                    if (!preferLocalValues) {
                        // prefer values from the other object
                        if ( (otherHeader.getValue() == null) || (otherHeader.getValue().length() == 0) ) {
                            // the other object has an empty value, so remove this value from the merged object
                            merged.remove(j);
                        } else {
                            // use the other object's value
                            mergedHeader.setValue(otherHeader.getValue());
                        }
                    }
                    // break out of the inner loop
                    break;
                }
            }
            if (!found) {
                // the other object has a new value to be added to the merged
                merged.add(otherHeader);
            }
        }

        // finally, merge the names so it's clear they've been merged
        merged.setName(merged.getName() + ":" + other.getName());

        return merged;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1369.java