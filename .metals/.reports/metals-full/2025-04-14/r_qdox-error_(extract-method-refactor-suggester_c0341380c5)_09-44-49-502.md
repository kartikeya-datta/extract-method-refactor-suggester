error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8037.java
text:
```scala
t@@his.writer = writer;

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.common.http.client;

import org.apache.lucene.util.IOUtils;
import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.unit.TimeValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 */
public class HttpDownloadHelper {

    private boolean useTimestamp = false;
    private boolean skipExisting = false;

    public boolean download(URL source, File dest, @Nullable DownloadProgress progress, TimeValue timeout) throws Exception {
        if (dest.exists() && skipExisting) {
            return true;
        }

        //don't do any progress, unless asked
        if (progress == null) {
            progress = new NullProgress();
        }

        //set the timestamp to the file date.
        long timestamp = 0;

        boolean hasTimestamp = false;
        if (useTimestamp && dest.exists()) {
            timestamp = dest.lastModified();
            hasTimestamp = true;
        }

        GetThread getThread = new GetThread(source, dest, hasTimestamp, timestamp, progress);

        try {
            getThread.setDaemon(true);
            getThread.start();
            getThread.join(timeout.millis());

            if (getThread.isAlive()) {
                throw new ElasticsearchTimeoutException("The GET operation took longer than " + timeout + ", stopping it.");
            }
        }
        catch (InterruptedException ie) {
            return false;
        } finally {
            getThread.closeStreams();
        }

        return getThread.wasSuccessful();
    }


    /**
     * Interface implemented for reporting
     * progress of downloading.
     */
    public interface DownloadProgress {
        /**
         * begin a download
         */
        void beginDownload();

        /**
         * tick handler
         */
        void onTick();

        /**
         * end a download
         */
        void endDownload();
    }

    /**
     * do nothing with progress info
     */
    public static class NullProgress implements DownloadProgress {

        /**
         * begin a download
         */
        public void beginDownload() {

        }

        /**
         * tick handler
         */
        public void onTick() {
        }

        /**
         * end a download
         */
        public void endDownload() {

        }
    }

    /**
     * verbose progress system prints to some output stream
     */
    public static class VerboseProgress implements DownloadProgress {
        private int dots = 0;
        // CheckStyle:VisibilityModifier OFF - bc
        PrintWriter writer;
        // CheckStyle:VisibilityModifier ON

        /**
         * Construct a verbose progress reporter.
         *
         * @param out the output stream.
         */
        public VerboseProgress(PrintStream out) {
            this.writer = new PrintWriter(out);
        }

        /**
         * Construct a verbose progress reporter.
         *
         * @param writer the output stream.
         */
        public VerboseProgress(PrintWriter writer) {
            this.writer = this.writer;
        }

        /**
         * begin a download
         */
        public void beginDownload() {
            writer.print("Downloading ");
            dots = 0;
        }

        /**
         * tick handler
         */
        public void onTick() {
            writer.print(".");
            if (dots++ > 50) {
                writer.flush();
                dots = 0;
            }
        }

        /**
         * end a download
         */
        public void endDownload() {
            writer.println("DONE");
            writer.flush();
        }
    }

    private class GetThread extends Thread {

        private final URL source;
        private final File dest;
        private final boolean hasTimestamp;
        private final long timestamp;
        private final DownloadProgress progress;

        private boolean success = false;
        private IOException ioexception = null;
        private InputStream is = null;
        private OutputStream os = null;
        private URLConnection connection;
        private int redirections = 0;

        GetThread(URL source, File dest, boolean h, long t, DownloadProgress p) {
            this.source = source;
            this.dest = dest;
            hasTimestamp = h;
            timestamp = t;
            progress = p;
        }

        public void run() {
            try {
                success = get();
            } catch (IOException ioex) {
                ioexception = ioex;
            }
        }

        private boolean get() throws IOException {

            connection = openConnection(source);

            if (connection == null) {
                return false;
            }

            boolean downloadSucceeded = downloadFile();

            //if (and only if) the use file time option is set, then
            //the saved file now has its timestamp set to that of the
            //downloaded file
            if (downloadSucceeded && useTimestamp) {
                updateTimeStamp();
            }

            return downloadSucceeded;
        }


        private boolean redirectionAllowed(URL aSource, URL aDest) throws IOException {
            // Argh, github does this...
//            if (!(aSource.getProtocol().equals(aDest.getProtocol()) || ("http"
//                    .equals(aSource.getProtocol()) && "https".equals(aDest
//                    .getProtocol())))) {
//                String message = "Redirection detected from "
//                        + aSource.getProtocol() + " to " + aDest.getProtocol()
//                        + ". Protocol switch unsafe, not allowed.";
//                throw new IOException(message);
//            }

            redirections++;
            if (redirections > 5) {
                String message = "More than " + 5 + " times redirected, giving up";
                throw new IOException(message);
            }


            return true;
        }

        private URLConnection openConnection(URL aSource) throws IOException {

            // set up the URL connection
            URLConnection connection = aSource.openConnection();
            // modify the headers
            // NB: things like user authentication could go in here too.
            if (hasTimestamp) {
                connection.setIfModifiedSince(timestamp);
            }

            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
                ((HttpURLConnection) connection).setUseCaches(true);
                ((HttpURLConnection) connection).setConnectTimeout(5000);
            }
            // connect to the remote site (may take some time)
            connection.connect();

            // First check on a 301 / 302 (moved) response (HTTP only)
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                        responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                    String newLocation = httpConnection.getHeaderField("Location");
                    String message = aSource
                            + (responseCode == HttpURLConnection.HTTP_MOVED_PERM ? " permanently"
                            : "") + " moved to " + newLocation;
                    URL newURL = new URL(newLocation);
                    if (!redirectionAllowed(aSource, newURL)) {
                        return null;
                    }
                    return openConnection(newURL);
                }
                // next test for a 304 result (HTTP only)
                long lastModified = httpConnection.getLastModified();
                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED
 (lastModified != 0 && hasTimestamp && timestamp >= lastModified)) {
                    // not modified so no file download. just return
                    // instead and trace out something so the user
                    // doesn't think that the download happened when it
                    // didn't
                    return null;
                }
                // test for 401 result (HTTP only)
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    String message = "HTTP Authorization failure";
                    throw new IOException(message);
                }
            }

            //REVISIT: at this point even non HTTP connections may
            //support the if-modified-since behaviour -we just check
            //the date of the content and skip the write if it is not
            //newer. Some protocols (FTP) don't include dates, of
            //course.
            return connection;
        }

        private boolean downloadFile() throws FileNotFoundException, IOException {
            IOException lastEx = null;
            for (int i = 0; i < 3; i++) {
                // this three attempt trick is to get round quirks in different
                // Java implementations. Some of them take a few goes to bind
                // property; we ignore the first couple of such failures.
                try {
                    is = connection.getInputStream();
                    break;
                } catch (IOException ex) {
                    lastEx = ex;
                }
            }
            if (is == null) {
                throw new IOException("Can't get " + source + " to " + dest, lastEx);
            }

            os = new FileOutputStream(dest);
            progress.beginDownload();
            boolean finished = false;
            try {
                byte[] buffer = new byte[1024 * 100];
                int length;
                while (!isInterrupted() && (length = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, length);
                    progress.onTick();
                }
                finished = !isInterrupted();
            } finally {
                if (!finished) {
                    // we have started to (over)write dest, but failed.
                    // Try to delete the garbage we'd otherwise leave
                    // behind.
                    IOUtils.closeWhileHandlingException(os, is);
                    dest.delete();
                } else {
                    IOUtils.close(os, is);
                }
            }
            progress.endDownload();
            return true;
        }

        private void updateTimeStamp() {
            long remoteTimestamp = connection.getLastModified();
            if (remoteTimestamp != 0) {
                dest.setLastModified(remoteTimestamp);
            }
        }

        /**
         * Has the download completed successfully?
         * <p/>
         * <p>Re-throws any exception caught during executaion.</p>
         */
        boolean wasSuccessful() throws IOException {
            if (ioexception != null) {
                throw ioexception;
            }
            return success;
        }

        /**
         * Closes streams, interrupts the download, may delete the
         * output file.
         */
        void closeStreams() throws IOException {
            interrupt();
            if (success) {
                IOUtils.close(is, os);
            } else {
                IOUtils.closeWhileHandlingException(is, os);
                if (dest != null && dest.exists()) {
                    dest.delete();
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8037.java