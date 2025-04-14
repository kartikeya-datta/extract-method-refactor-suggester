error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9675.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9675.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9675.java
text:
```scala
O@@bject id = msg.get("id");

package backtype.storm.multilang;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;

/**
 * JsonSerializer implements the JSON multilang protocol.
 */
public class JsonSerializer implements ISerializer {
    private DataOutputStream processIn;
    private BufferedReader processOut;

    public void initialize(OutputStream processIn, InputStream processOut) {
        this.processIn = new DataOutputStream(processIn);
        this.processOut = new BufferedReader(new InputStreamReader(processOut));
    }

    public Number connect(Map conf, TopologyContext context)
            throws IOException, NoOutputException {
        JSONObject setupInfo = new JSONObject();
        setupInfo.put("pidDir", context.getPIDDir());
        setupInfo.put("conf", conf);
        setupInfo.put("context", context);
        writeMessage(setupInfo);

        Number pid = (Number) ((JSONObject) readMessage()).get("pid");
        return pid;
    }

    public void writeBoltMsg(BoltMsg boltMsg) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("id", boltMsg.getId());
        obj.put("comp", boltMsg.getComp());
        obj.put("stream", boltMsg.getStream());
        obj.put("task", boltMsg.getTask());
        obj.put("tuple", boltMsg.getTuple());
        writeMessage(obj);
    }

    public void writeSpoutMsg(SpoutMsg msg) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("command", msg.getCommand());
        obj.put("id", msg.getId());
        writeMessage(obj);
    }

    public void writeTaskIds(List<Integer> taskIds) throws IOException {
        writeMessage(taskIds);
    }

    private void writeMessage(Object msg) throws IOException {
        writeString(JSONValue.toJSONString(msg));
    }

    private void writeString(String str) throws IOException {
        byte[] strBytes = str.getBytes("UTF-8");
        processIn.write(strBytes, 0, strBytes.length);
        processIn.writeBytes("\nend\n");
        processIn.flush();
    }

    public ShellMsg readShellMsg() throws IOException, NoOutputException {
        JSONObject msg = (JSONObject) readMessage();
        ShellMsg shellMsg = new ShellMsg();

        String command = (String) msg.get("command");
        shellMsg.setCommand(command);

        String id = (String) msg.get("id");
        shellMsg.setId(id);

        String log = (String) msg.get("msg");
        shellMsg.setMsg(log);

        String stream = (String) msg.get("stream");
        if (stream == null)
            stream = Utils.DEFAULT_STREAM_ID;
        shellMsg.setStream(stream);

        Object taskObj = msg.get("task");
        if (taskObj != null) {
            shellMsg.setTask((Long) taskObj);
        } else {
            shellMsg.setTask(0);
        }

        Object need_task_ids = msg.get("need_task_ids");
        if (need_task_ids == null || ((Boolean) need_task_ids).booleanValue()) {
            shellMsg.setNeedTaskIds(true);
        } else {
            shellMsg.setNeedTaskIds(false);
        }

        shellMsg.setTuple((List) msg.get("tuple"));

        List<Tuple> anchors = new ArrayList<Tuple>();
        Object anchorObj = msg.get("anchors");
        if (anchorObj != null) {
            if (anchorObj instanceof String) {
                anchorObj = Arrays.asList(anchorObj);
            }
            for (Object o : (List) anchorObj) {
                shellMsg.addAnchor((String) o);
            }
        }

        return shellMsg;
    }

    private Object readMessage() throws IOException, NoOutputException {
        String string = readString();
        Object msg = JSONValue.parse(string);
        if (msg != null) {
            return msg;
        } else {
            throw new IOException("unable to parse: " + string);
        }
    }

    private String readString() throws IOException, NoOutputException {
        StringBuilder line = new StringBuilder();

        // synchronized (processOut) {
        while (true) {
            String subline = processOut.readLine();
            if (subline == null) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Pipe to subprocess seems to be broken!");
                if (line.length() == 0) {
                    errorMessage.append(" No output read.\n");
                } else {
                    errorMessage.append(" Currently read output: "
                            + line.toString() + "\n");
                }
                errorMessage.append("Serializer Exception:\n");
                throw new NoOutputException(errorMessage.toString());
            }
            if (subline.equals("end")) {
                break;
            }
            if (line.length() != 0) {
                line.append("\n");
            }
            line.append(subline);
        }
        // }
        return line.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9675.java