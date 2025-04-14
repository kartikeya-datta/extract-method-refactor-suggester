error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18222.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18222.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18222.java
text:
```scala
g@@etResourceAsStream("/META-INF/org.apache.openjpa.revision.properties");

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.conf;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This class contains version information for OpenJPA. It uses
 * Ant's filter tokens to convert the template into a java
 * file with current information.
 *
 * @author Marc Prud'hommeaux, Patrick Linskey
 */
public class OpenJPAVersion {

    public static final String VERSION_NUMBER;
    public static final String VERSION_ID;
    public static final String VENDOR_NAME = "OpenJPA";
    public static final int MAJOR_RELEASE;
    public static final int MINOR_RELEASE;
    public static final int PATCH_RELEASE;
    public static final String RELEASE_STATUS;
    public static final String REVISION_NUMBER;

    static {
        Package pack = OpenJPAVersion.class.getPackage();
        String vers = pack == null ? null : pack.getImplementationVersion();
        if (vers == null || vers.length() == 0)
            vers = "0.0.0";

        VERSION_NUMBER = vers;

        StringTokenizer tok = new StringTokenizer(VERSION_NUMBER, ".-");

        int major, minor, patch;

        try {
            major = tok.hasMoreTokens() ? Integer.parseInt(tok.nextToken()) : 0;
        } catch (Exception e) {
            major = 0;
        }

        try {
            minor = tok.hasMoreTokens() ? Integer.parseInt(tok.nextToken()) : 0;
        } catch (Exception e) {
            minor = 0;
        }

        try {
            patch = tok.hasMoreTokens() ? Integer.parseInt(tok.nextToken()) : 0;
        } catch (Exception e) {
            patch = 0;
        }

        String revision = "";
        try {
            InputStream in = OpenJPAVersion.class.
                getResourceAsStream("/META-INF/revision.properties");
            if (in != null) {
                try {
                    Properties props = new Properties();
                    props.load(in);
                    revision = props.getProperty("revision.number");
                } finally {
                    in.close();
                }
            }
        } catch (Exception e) {
        }

        MAJOR_RELEASE = major;
        MINOR_RELEASE = minor;
        PATCH_RELEASE = patch;
        RELEASE_STATUS = tok.hasMoreTokens() ? tok.nextToken("!") : "";
        REVISION_NUMBER = revision;
        VERSION_ID = VERSION_NUMBER + "-r" + REVISION_NUMBER;
    }

    public static void main(String [] args) {
        System.out.println(new OpenJPAVersion().toString());
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(80 * 30);
        appendOpenJPABanner(buf);
        buf.append("\n");

        getProperty("os.name", buf).append("\n");
        getProperty("os.version", buf).append("\n");
        getProperty("os.arch", buf).append("\n\n");

        getProperty("java.version", buf).append("\n");
        getProperty("java.vendor", buf).append("\n\n");

        buf.append("java.class.path:\n");
        StringTokenizer tok = new StringTokenizer
            (System.getProperty("java.class.path"), File.pathSeparator);
        while (tok.hasMoreTokens()) {
            buf.append("\t").append(tok.nextToken());
            buf.append("\n");
        }
        buf.append("\n");

        getProperty("user.dir", buf);

        return buf.toString();
    }

    public void appendOpenJPABanner(StringBuffer buf) {
        buf.append(VENDOR_NAME).append(" ");
        buf.append(VERSION_NUMBER);
        buf.append("\n");
        buf.append("version id: ").append(VERSION_ID);
        buf.append("\n");
        buf.append("Apache svn revision: ").append(REVISION_NUMBER);
        buf.append("\n");
    }

    private StringBuffer getProperty(String prop, StringBuffer buf) {
        buf.append(prop).append(": ").append(System.getProperty(prop));
        return buf;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18222.java