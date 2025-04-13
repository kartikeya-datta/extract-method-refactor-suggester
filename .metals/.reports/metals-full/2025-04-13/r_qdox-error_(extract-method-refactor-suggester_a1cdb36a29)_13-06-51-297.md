error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8440.java
text:
```scala
public A@@ndroidPreferences(SharedPreferences preferences) {

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
package com.badlogic.gdx.backends.android;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.badlogic.gdx.Preferences;

public class AndroidPreferences implements Preferences {
	SharedPreferences sharedPrefs;
	
	AndroidPreferences(SharedPreferences preferences) {
		this.sharedPrefs = preferences;
	}

	@Override public void putBoolean (String key, boolean val) {
		Editor edit = this.sharedPrefs.edit();
		edit.putBoolean(key, val);
		edit.commit();
	}

	@Override public void putInteger (String key, int val) {
		Editor edit = this.sharedPrefs.edit();
		edit.putInt(key, val);
		edit.commit();
	}

	@Override public void putLong (String key, long val) {
		Editor edit = this.sharedPrefs.edit();
		edit.putLong(key, val);
		edit.commit();
	}

	@Override public void putFloat (String key, float val) {
		Editor edit = this.sharedPrefs.edit();
		edit.putFloat(key, val);
		edit.commit();
	}
	
	@Override public void putString(String key, String val) {
		Editor edit = this.sharedPrefs.edit();
		edit.putString(key, val);
		edit.commit();
	}

	@Override public void put (Map<String, ?> vals) {
		Editor edit = this.sharedPrefs.edit();
		for(Entry<String, ?> val: vals.entrySet()) {
			if(val.getValue() instanceof Boolean)
				putBoolean(val.getKey(), (Boolean)val.getValue());
			if(val.getValue() instanceof Integer)
				putInteger(val.getKey(), (Integer)val.getValue());
			if(val.getValue() instanceof Long)
				putLong(val.getKey(), (Long)val.getValue());
			if(val.getValue() instanceof String)
				putString(val.getKey(), (String)val.getValue());
			if(val.getValue() instanceof Float) 
				putFloat(val.getKey(), (Float)val.getValue());
		}
		edit.commit();
	}

	@Override public boolean getBoolean (String key) {		
		return sharedPrefs.getBoolean(key, false);
	}

	@Override public int getInteger (String key) {
		return sharedPrefs.getInt(key, 0);
	}

	@Override public long getLong (String key) {
		return sharedPrefs.getLong(key, 0);
	}

	@Override public float getFloat (String key) {
		return sharedPrefs.getFloat(key, 0);
	}

	@Override public String getString (String key) {
		return sharedPrefs.getString(key, "");
	}

	@Override public boolean getBoolean (String key, boolean defValue) {
		return sharedPrefs.getBoolean(key, defValue);
	}

	@Override public int getInteger (String key, int defValue) {
		return sharedPrefs.getInt(key, defValue);
	}

	@Override public long getLong (String key, long defValue) {
		return sharedPrefs.getLong(key, defValue);
	}

	@Override public float getFloat (String key, float defValue) {
		return sharedPrefs.getFloat(key, defValue);
	}

	@Override public String getString (String key, String defValue) {
		return sharedPrefs.getString(key, defValue);
	}

	@Override public Map<String, ?> get () {		
		return sharedPrefs.getAll();		
	}

	@Override public boolean contains (String key) {
		return sharedPrefs.contains(key);
	}

	@Override public void clear () {
		Editor edit = sharedPrefs.edit();
		edit.clear();
		edit.commit();
	}
	
	@Override public void flush () {		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8440.java