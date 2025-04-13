error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4971.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4971.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4971.java
text:
```scala
t@@his(new LwjglFileHandle(new File(".prefs/" + name), FileType.External));

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

package com.badlogic.gdx.backends.lwjgl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LwjglPreferences implements Preferences {
	private final String name;
	private final Properties properties = new Properties();
	private final FileHandle file;

	public LwjglPreferences (String name) {
		this(new LwjglFileHandle(new File(name), FileType.External));
	}

	public LwjglPreferences (FileHandle file) {
		this.name = file.name();
		this.file = file;
		if (!file.exists()) return;
		InputStream in = null;
		try {
			in = new BufferedInputStream(file.read());
			properties.loadFromXML(in);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (in != null) try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void putBoolean (String key, boolean val) {
		properties.put(key, Boolean.toString(val));
	}

	@Override
	public void putInteger (String key, int val) {
		properties.put(key, Integer.toString(val));
	}

	@Override
	public void putLong (String key, long val) {
		properties.put(key, Long.toString(val));
	}

	@Override
	public void putFloat (String key, float val) {
		properties.put(key, Float.toString(val));
	}

	@Override
	public void putString (String key, String val) {
		properties.put(key, val);
	}

	@Override
	public void put (Map<String, ?> vals) {
		for (Entry<String, ?> val : vals.entrySet()) {
			if (val.getValue() instanceof Boolean) putBoolean(val.getKey(), (Boolean)val.getValue());
			if (val.getValue() instanceof Integer) putInteger(val.getKey(), (Integer)val.getValue());
			if (val.getValue() instanceof Long) putLong(val.getKey(), (Long)val.getValue());
			if (val.getValue() instanceof String) putString(val.getKey(), (String)val.getValue());
			if (val.getValue() instanceof Float) putFloat(val.getKey(), (Float)val.getValue());
		}
	}

	@Override
	public boolean getBoolean (String key) {
		return getBoolean(key, false);
	}

	@Override
	public int getInteger (String key) {
		return getInteger(key, 0);
	}

	@Override
	public long getLong (String key) {
		return getLong(key, 0);
	}

	@Override
	public float getFloat (String key) {
		return getFloat(key, 0);
	}

	@Override
	public String getString (String key) {
		return getString(key, "");
	}

	@Override
	public boolean getBoolean (String key, boolean defValue) {
		return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defValue)));
	}

	@Override
	public int getInteger (String key, int defValue) {
		return Integer.parseInt(properties.getProperty(key, Integer.toString(defValue)));
	}

	@Override
	public long getLong (String key, long defValue) {
		return Long.parseLong(properties.getProperty(key, Long.toString(defValue)));
	}

	@Override
	public float getFloat (String key, float defValue) {
		return Float.parseFloat(properties.getProperty(key, Float.toString(defValue)));
	}

	@Override
	public String getString (String key, String defValue) {
		return properties.getProperty(key, defValue);
	}

	@Override
	public Map<String, ?> get () {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Entry<Object, Object> val : properties.entrySet()) {
			if (val.getValue() instanceof Boolean)
				map.put((String)val.getKey(), (Boolean)Boolean.parseBoolean((String)val.getValue()));
			if (val.getValue() instanceof Integer) map.put((String)val.getKey(), (Integer)Integer.parseInt((String)val.getValue()));
			if (val.getValue() instanceof Long) map.put((String)val.getKey(), (Long)Long.parseLong((String)val.getValue()));
			if (val.getValue() instanceof String) map.put((String)val.getKey(), (String)val.getValue());
			if (val.getValue() instanceof Float) map.put((String)val.getKey(), (Float)Float.parseFloat((String)val.getValue()));
		}

		return map;
	}

	@Override
	public boolean contains (String key) {
		return properties.containsKey(key);
	}

	@Override
	public void clear () {
		properties.clear();
	}

	@Override
	public void flush () {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(file.write(false));
			properties.storeToXML(out, null);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error writing preferences: " + file, ex);
		} finally {
			if (out != null) try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void remove (String key) {
		properties.remove(key);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4971.java