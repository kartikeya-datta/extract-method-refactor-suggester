error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14791.java
text:
```scala
t@@hrow new RuntimeException("Error when launching multilang subprocess", e);

package backtype.storm.spout;

import backtype.storm.generated.ShellComponent;
import backtype.storm.task.TopologyContext;
import backtype.storm.utils.ShellProcess;
import backtype.storm.utils.Utils;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;


public class ShellSpout implements ISpout {
    public static Logger LOG = Logger.getLogger(ShellSpout.class);

    private SpoutOutputCollector _collector;
    private String[] _command;
    private ShellProcess _process;

    public ShellSpout(ShellComponent component) {
        this(component.get_execution_command(), component.get_script());
    }
    
    public ShellSpout(String... command) {
        _command = command;
    }
    
    public void open(Map stormConf, TopologyContext context,
                     SpoutOutputCollector collector) {
        _process = new ShellProcess(_command);
        _collector = collector;

        try {
            Number subpid = _process.launch(stormConf, context);
            LOG.info("Launched subprocess with pid " + subpid);
        } catch (IOException e) {
            throw new RuntimeException("Error when launching multilang subprocess\n" + _process.getErrorsString(), e);
        }
    }

    public void close() {
        _process.destroy();
    }

    private JSONObject _next;
    public void nextTuple() {
        if (_next == null) {
            _next = new JSONObject();
            _next.put("command", "next");
        }

        querySubprocess(_next);
    }

    private JSONObject _ack;
    public void ack(Object msgId) {
        if (_ack == null) {
            _ack = new JSONObject();
            _ack.put("command", "ack");
        }

        _ack.put("id", msgId);
        querySubprocess(_ack);
    }

    private JSONObject _fail;
    public void fail(Object msgId) {
        if (_fail == null) {
            _fail = new JSONObject();
            _fail.put("command", "fail");
        }

        _fail.put("id", msgId);
        querySubprocess(_fail);
    }

    private void querySubprocess(Object query) {
        try {
            _process.writeMessage(query);

            while (true) {
                JSONObject action = _process.readMessage();
                String command = (String) action.get("command");
                if (command.equals("sync")) {
                    return;
                } else if (command.equals("log")) {
                    String msg = (String) action.get("msg");
                    LOG.info("Shell msg: " + msg);
                } else if (command.equals("emit")) {
                    String stream = (String) action.get("stream");
                    if (stream == null) stream = Utils.DEFAULT_STREAM_ID;
                    Long task = (Long) action.get("task");
                    List<Object> tuple = (List) action.get("tuple");
                    Object messageId = (Object) action.get("id");
                    if (task == null) {
                        List<Integer> outtasks = _collector.emit(stream, tuple, messageId);
                        _process.writeMessage(outtasks);
                    } else {
                        _collector.emitDirect((int)task.longValue(), stream, tuple, messageId);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14791.java