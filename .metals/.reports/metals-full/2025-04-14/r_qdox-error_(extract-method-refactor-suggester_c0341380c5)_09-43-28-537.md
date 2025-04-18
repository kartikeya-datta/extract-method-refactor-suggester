error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16323.java
text:
```scala
r@@eturn new Message[0];

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

package org.apache.jmeter.protocol.mail.sampler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;

import org.apache.commons.io.IOUtils;

public class MailFileFolder extends Folder {

    private static final String FILENAME_FORMAT = "%d.msg";
    private static final String FILENAME_REGEX = "\\d+\\.msg";
    private boolean isOpen;
    private final File folderPath;// Parent folder (or single message file)
    private final boolean isFile;
    private static final FilenameFilter FILENAME_FILTER = new FilenameFilter(){
        public boolean accept(File dir, String name) {
            return name.matches(FILENAME_REGEX);
        }

    };

    public MailFileFolder(Store store, String path) {
        super(store);
        String base = store.getURLName().getHost(); // == ServerName from mail sampler
        File parentFolder = new File(base);
        isFile = parentFolder.isFile();
        if (isFile){
            folderPath = new File(base);
        } else {
            folderPath = new File(base,path);
        }
    }

    public MailFileFolder(Store store, URLName path) {
        this(store, path.getFile());
    }

    @Override
    public void appendMessages(Message[] messages) throws MessagingException {
        throw new MessagingException("Not supported");
    }

    @Override
    public void close(boolean expunge) throws MessagingException {
        this.store.close();
        isOpen = false;
    }

    @Override
    public boolean create(int type) throws MessagingException {
        return false;
    }

    @Override
    public boolean delete(boolean recurse) throws MessagingException {
        return false;
    }

    @Override
    public boolean exists() throws MessagingException {
        return true;
    }

    @Override
    public Message[] expunge() throws MessagingException {
        return null;
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        return this;
    }

    @Override
    public String getFullName() {
        return this.toString();
    }

    @Override
    public Message getMessage(int index) throws MessagingException {
        File f;
        if (isFile) {
            f = folderPath;
        } else {
            f = new File(folderPath,String.format(FILENAME_FORMAT, Integer.valueOf(index)));
        }
        try {
            FileInputStream fis = new FileInputStream(f);
            try {
                Message m = new MailFileMessage(this, fis, index);
                return m;
            } finally {
                IOUtils.closeQuietly(fis);
            }
        } catch (FileNotFoundException e) {
            throw new MessagingException("Cannot open folder: "+e.getMessage());
        }
    }

    @Override
    public int getMessageCount() throws MessagingException {
        if (!isOpen) return -1;
        if (isFile) return 1;
        File[] listFiles = folderPath.listFiles(FILENAME_FILTER);
        return listFiles != null ? listFiles.length : 0;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public Folder getParent() throws MessagingException {
        return null;
    }

    @Override
    public Flags getPermanentFlags() {
        return null;
    }

    @Override
    public char getSeparator() throws MessagingException {
        return '/';
    }

    @Override
    public int getType() throws MessagingException {
        return HOLDS_MESSAGES;
    }

    @Override
    public boolean hasNewMessages() throws MessagingException {
        return false;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public Folder[] list(String pattern) throws MessagingException {
        return new Folder[]{this};
    }

    @Override
    public void open(int mode) throws MessagingException {
        if (mode != READ_ONLY) {
            throw new MessagingException("Implementation only supports read-only access");
        }
        this.mode = mode;
        isOpen = true;
    }

    @Override
    public boolean renameTo(Folder newName) throws MessagingException {
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16323.java