error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4618.java
text:
```scala
i@@f (_diagContext == null && _conf != null) {

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
package org.apache.openjpa.lib.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.openjpa.lib.conf.Configurable;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.GenericConfigurable;
import org.apache.openjpa.lib.util.Files;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.Options;
import org.apache.openjpa.lib.util.concurrent.ConcurrentHashMap;

/**
 * Default {@link LogFactory} implementation. For ease of automatic
 * configuration, this implementation keys on only the last dot-separated
 * token of the log channel name.
 *
 * @author Patrick Linskey
 */
public class LogFactoryImpl 
    implements LogFactory, GenericConfigurable, Configurable {

    private static Localizer _loc = Localizer.forPackage(LogFactoryImpl.class);

    public static final String TRACE_STR = _loc.get("log-trace").getMessage();
    public static final String INFO_STR = _loc.get("log-info").getMessage();
    public static final String WARN_STR = _loc.get("log-warn").getMessage();
    public static final String ERROR_STR = _loc.get("log-error").getMessage();
    public static final String FATAL_STR = _loc.get("log-fatal").getMessage();

    public static final String STDOUT = "stdout";
    public static final String STDERR = "stderr";

    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * The time at which this factory was initialized.
     */
    protected final long initializationMillis;

    /**
     * The {@link Log}s that this factory manages, keyed by log channel name.
     */
    private Map _logs = new ConcurrentHashMap(); // <String,Log>

    /**
     * The default logging level.
     */
    private short _defaultLogLevel = Log.INFO;

    /**
     * Storage for logging level configuration specified at configuration time.
     */
    private Map _configuredLevels = new HashMap(); // <String,Integer>

    /**
     * The stream to write to. Defaults to System.err.
     */
    private PrintStream _out = System.err;

    /**
     * A token to add to all log messages. If <code>null</code>, the 
     * configuration's id will be used.
     */
    private String _diagContext = null;
    private boolean _diagContextComputed = false;
    
    private Configuration _conf;


    public LogFactoryImpl() {
        initializationMillis = System.currentTimeMillis();
    }

    public Log getLog(String channel) {
        // no locking; ok if same log created multiple times
        LogImpl l = (LogImpl) _logs.get(channel);
        if (l == null) {
            l = newLogImpl();
            l.setChannel(channel);
            Short lvl = (Short) _configuredLevels.get(shorten(channel));
            l.setLevel(lvl == null ? _defaultLogLevel : lvl.shortValue());
            _logs.put(channel, l);
        }
        return l;
    }

    /**
     * Create a new log. The log will be cached.
     */
    protected LogImpl newLogImpl() {
        return new LogImpl();
    }

    /**
     * The string name of the default level for unconfigured log channels;
     * used for automatic configuration.
     */
    public void setDefaultLevel(String level) {
        _defaultLogLevel = getLevel(level);
    }

    /**
     * The default level for unconfigured log channels.
     */
    public short getDefaultLevel() {
        return _defaultLogLevel;
    }

    /**
     * The default level for unconfigured log channels.
     */
    public void setDefaultLevel(short level) {
        _defaultLogLevel = level;
    }

    /**
     * A string to prefix all log messages with. Set to
     * <code>null</code> to use the configuration's Id property setting.
     */
    public void setDiagnosticContext(String val) {
        _diagContext = val;
    }

    /**
     * A string to prefix all log messages with. Set to
     * <code>null</code> to use the configuration's Id property setting.
     */
    public String getDiagnosticContext() {
        if (!_diagContextComputed) {
            // this initialization has to happen lazily because there is no
            // guarantee that conf.getId() will be populated by the time that
            // endConfiguration() is called.
            if (_diagContext == null) {
                _diagContext = _conf.getId();
            }
            if ("".equals(_diagContext))
                _diagContext = null;
            _diagContextComputed = true;
        }
        return _diagContext;
    }

    /**
     * The stream to write to. Recognized values are: <code>stdout</code>
     * and <code>stderr</code>. Any other value will be considered a file name.
     */
    public void setFile(String file) {
        if (STDOUT.equals(file))
            _out = System.out;
        else if (STDERR.equals(file))
            _out = System.err;
        else {
            File f = Files.getFile(file, null);
            try {
                _out = new PrintStream(new FileOutputStream
                    (f.getCanonicalPath(), true));
            } catch (IOException ioe) {
                throw new IllegalArgumentException(_loc.get("log-bad-file",
                    file) + " " + ioe.toString());
            }
        }
    }

    /**
     * The stream to write to.
     */
    public PrintStream getStream() {
        return _out;
    }

    /**
     * The stream to write to.
     */
    public void setStream(PrintStream stream) {
        if (stream == null)
            throw new NullPointerException("stream == null");
        _out = stream;
    }

    /**
     * Returns a string representation of the specified log level constant.
     */
    public static String getLevelName(short level) {
        switch (level) {
            case Log.TRACE:
                return TRACE_STR;
            case Log.INFO:
                return INFO_STR;
            case Log.WARN:
                return WARN_STR;
            case Log.ERROR:
                return ERROR_STR;
            case Log.FATAL:
                return FATAL_STR;
            default:
                return _loc.get("log-unknown").getMessage();
        }
    }

    /**
     * Returns a symbolic constant for the specified string level.
     */
    public static short getLevel(String str) {
        str = str.toUpperCase().trim();
        short val = TRACE_STR.equals(str) ? Log.TRACE :
            INFO_STR.equals(str) ? Log.INFO :
                WARN_STR.equals(str) ? Log.WARN :
                    ERROR_STR.equals(str) ? Log.ERROR :
                        FATAL_STR.equals(str) ? Log.FATAL : -1;

        if (val == -1)
            throw new IllegalArgumentException
                (_loc.get("log-bad-constant", str).getMessage());

        return val;
    }

    // ---------- Configurable implementation ----------
    
    public void setConfiguration(Configuration conf) {
        _conf = conf;
    }
    
    public void startConfiguration() {
    }

    public void endConfiguration() {
    }

    // ---------- GenericConfigurable implementation ----------

    public void setInto(Options opts) {
        if (!opts.isEmpty()) {
            Map.Entry e;
            for (Iterator iter = opts.entrySet().iterator(); iter.hasNext();) {
                e = (Map.Entry) iter.next();
                _configuredLevels.put(shorten((String) e.getKey()),
                    new Short(getLevel((String) e.getValue())));
            }
            opts.clear();
        }
    }

    private static String shorten(String channel) {
        return channel.substring(channel.lastIndexOf('.') + 1);
    }

    /**
     * A simple implementation of the {@link Log} interface. Writes
     * output to stderr.
     */
    public class LogImpl extends AbstractLog {

        private short _level = INFO;
        private String _channel;

        protected boolean isEnabled(short level) {
            return level >= _level;
        }

        protected void log(short level, String message, Throwable t) {
            String msg = formatMessage(level, message, t);
            synchronized (_out) {
                _out.print(msg);
            }
        }

        /**
         * Convert <code>message</code> into a string ready to be written to
         * the log. The string should include the terminating newline.
         *
         * @param t may be null
         */
        protected String formatMessage(short level, String message,
            Throwable t) {
            // we write to a StringBuffer and then flush it all at
            // once as a single line, since some environments(e.g., JBoss)
            // override the System output stream to flush any calls
            // to write without regard to line breaks, making the
            // output incomprehensibe.
            StringBuffer buf = new StringBuffer();

            buf.append(getOffset());
            buf.append("  ");
            if (getDiagnosticContext() != null)
                buf.append(getDiagnosticContext()).append("  ");
            buf.append(getLevelName(level));
            if (level == INFO || level == WARN)
                buf.append(" ");
            buf.append("  [");
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
            buf.append(_channel);
            buf.append(" - ");
            buf.append(message);
            buf.append(NEWLINE);

            if (t != null) {
                StringWriter swriter = new StringWriter();
                PrintWriter pwriter = new PrintWriter(swriter);
                t.printStackTrace(pwriter);
                pwriter.flush();
                buf.append(swriter.toString());
            }
            return buf.toString();
        }

        private long getOffset() {
            return System.currentTimeMillis() - initializationMillis;
        }

        public void setChannel(String val) {
            _channel = val;
        }

        public String getChannel() {
            return _channel;
        }

        public void setLevel(short val) {
            _level = val;
        }

        public short getLevel() {
            return _level;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4618.java