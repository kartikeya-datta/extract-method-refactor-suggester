error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8306.java
text:
```scala
J@@MeterProperty jmp = prop.clone();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
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

package org.apache.jmeter.protocol.http.util;

import java.io.Serializable;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

/**
 * Class representing a file parameter for http upload.
 * Consists of a http parameter name/file path pair with (optional) mimetype.
 *
 * Also provides temporary storage for the headers which are sent with files.
 *
 */
public class HTTPFileArg extends AbstractTestElement implements Serializable {

    private static final long serialVersionUID = 240L;

    /** Name used to store the file's path. */
    private static final String FILEPATH = "File.path";

    /** Name used to store the file's paramname. */
    private static final String PARAMNAME = "File.paramname";

    /** Name used to store the file's mimetype. */
    private static final String MIMETYPE = "File.mimetype";

    /** temporary storage area for the body header. */
    private String header;

    /**
     * Constructor for an empty HTTPFileArg object
     */
    public HTTPFileArg() {
    }

    /**
     * Constructor for the HTTPFileArg object with given path.
     */
    public HTTPFileArg(String path) {
        this(path, "", "");
    }

    /**
     * Constructor for the HTTPFileArg object with full information.
     */
    public HTTPFileArg(String path, String paramname, String mimetype) {
        if (path == null || paramname == null || mimetype == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setPath(path);
        setParamName(paramname);
        setMimeType(mimetype);
    }

    /**
     * Constructor for the HTTPFileArg object with full information,
     * using existing properties
     */
    public HTTPFileArg(JMeterProperty path, JMeterProperty paramname, JMeterProperty mimetype) {
        if (path == null || paramname == null || mimetype == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setProperty(FILEPATH, path);
        setProperty(MIMETYPE, mimetype);
        setProperty(PARAMNAME, paramname);
    }

    private void setProperty(String name, JMeterProperty prop) {
        JMeterProperty jmp = (JMeterProperty) prop.clone();
        jmp.setName(name);
        setProperty(jmp);
    }

    /**
     * Copy Constructor.
     */
    public HTTPFileArg(HTTPFileArg file) {
        this(file.getPath(), file.getParamName(), file.getMimeType());
    }

    /**
     * Set the http parameter name of the File.
     *
     * @param newParamName
     * the new http parameter name
     */
    public void setParamName(String newParamName) {
        setProperty(new StringProperty(PARAMNAME, newParamName));
    }

    /**
     * Get the http parameter name of the File.
     *
     * @return the http parameter name
     */
    public String getParamName() {
        return getPropertyAsString(PARAMNAME);
    }

    /**
     * Set the mimetype of the File.
     *
     * @param newMimeType
     * the new mimetype
     */
    public void setMimeType(String newMimeType) {
        setProperty(new StringProperty(MIMETYPE, newMimeType));
    }

    /**
     * Get the mimetype of the File.
     *
     * @return the http parameter mimetype
     */
    public String getMimeType() {
        return getPropertyAsString(MIMETYPE);
    }

    /**
     * Set the path of the File.
     *
     * @param newPath
     *  the new path
     */
    public void setPath(String newPath) {
        setProperty(new StringProperty(FILEPATH, newPath));
    }

    /**
     * Get the path of the File.
     *
     * @return the file's path
     */
    public String getPath() {
        return getPropertyAsString(FILEPATH);
    }

   /**
    * Sets the body header for the HTTPFileArg object. Header
    * contains path, parameter name and mime type information.
    * This is only intended for use by methods which need to store information
    * temporarily whilst creating the HTTP body.
    * 
    * @param newHeader
    *  the new Header value
    */
   public void setHeader(String newHeader) {
       header = newHeader;
   }

   /**
    * Gets the saved body header for the HTTPFileArg object.
    */
   public String getHeader() {
       return header;
   }

    /**
     * returns path, param name, mime type information of
     * HTTPFileArg object.
     *
     * @return the string demonstration of HTTPFileArg object in this
     * format:
     *    "path:'<PATH>'|param:'<PARAM NAME>'|mimetype:'<MIME TYPE>'"
     */
    @Override
    public String toString() {
        return "path:'" + getPath()
            + "'|param:'" + getParamName()
            + "'|mimetype:'" + getMimeType() + "'";
    }

    /**
     * Check if the entry is not empty.
     * @return true if Path, name or mimetype fields are not the empty string
     */
    public boolean isNotEmpty() {
        return getPath().length() > 0
 getParamName().length() > 0
 getMimeType().length() > 0; // TODO should we allow mimetype only?
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8306.java