error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4765.java
text:
```scala
public b@@oolean mouseMoved (int x, int y) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.input;

import java.io.DataOutputStream;
import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;

/** Sends all inputs from touch, key, accelerometer and compass to a {@link RemoteInput} at the given ip/port. Instantiate this and
 * call sendUpdate() periodically.
 * 
 * @author mzechner */
public class RemoteSender implements InputProcessor {
	private DataOutputStream out;
	private boolean connected = false;

	public static final int KEY_DOWN = 0;
	public static final int KEY_UP = 1;
	public static final int KEY_TYPED = 2;

	public static final int TOUCH_DOWN = 3;
	public static final int TOUCH_UP = 4;
	public static final int TOUCH_DRAGGED = 5;

	public static final int ACCEL = 6;
	public static final int COMPASS = 7;
	public static final int SIZE = 8;

	public RemoteSender (String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(3000);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeBoolean(Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen));
			connected = true;
			Gdx.input.setInputProcessor(this);
		} catch (Exception e) {
			Gdx.app.log("RemoteSender", "couldn't connect to " + ip + ":" + port);
		}
	}

	public void sendUpdate () {
		synchronized (this) {
			if (!connected) return;
		}
		try {
			out.writeInt(ACCEL);
			out.writeFloat(Gdx.input.getAccelerometerX());
			out.writeFloat(Gdx.input.getAccelerometerY());
			out.writeFloat(Gdx.input.getAccelerometerZ());
			out.writeInt(COMPASS);
			out.writeFloat(Gdx.input.getAzimuth());
			out.writeFloat(Gdx.input.getPitch());
			out.writeFloat(Gdx.input.getRoll());
			out.writeInt(SIZE);
			out.writeFloat(Gdx.graphics.getWidth());
			out.writeFloat(Gdx.graphics.getHeight());
		} catch (Throwable t) {
			out = null;
			connected = false;
		}
	}

	@Override
	public boolean keyDown (int keycode) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(KEY_DOWN);
			out.writeInt(keycode);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(KEY_UP);
			out.writeInt(keycode);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(KEY_TYPED);
			out.writeChar(character);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(TOUCH_DOWN);
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(pointer);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(TOUCH_UP);
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(pointer);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		synchronized (this) {
			if (!connected) return false;
		}

		try {
			out.writeInt(TOUCH_DRAGGED);
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(pointer);
		} catch (Throwable t) {
			synchronized (this) {
				connected = false;
			}
		}
		return false;
	}

	@Override
	public boolean touchMoved (int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}

	public boolean isConnected () {
		synchronized (this) {
			return connected;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4765.java