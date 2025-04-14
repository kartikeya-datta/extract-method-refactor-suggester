error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3821.java
text:
```scala
S@@prite sprite = atlas.createSprite(imageName);

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.graphics.particles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ParticleEffect {
	private ArrayList<ParticleEmitter> emitters = new ArrayList();

	public void start () {
		for (ParticleEmitter emitter : emitters)
			emitter.start();
	}

	public void draw (SpriteBatch spriteBatch, float delta) {
		for (int i = 0, n = emitters.size(); i < n; i++)
			emitters.get(i).draw(spriteBatch, delta);
	}

	public void allowCompletion () {
		for (int i = 0, n = emitters.size(); i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			emitter.setContinuous(false);
			emitter.durationTimer = emitter.duration;
		}
	}

	public boolean isComplete () {
		for (int i = 0, n = emitters.size(); i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			if (emitter.isContinuous()) return false;
			if (!emitter.isComplete()) return false;
		}
		return true;
	}

	public void setDuration (int duration) {
		for (int i = 0, n = emitters.size(); i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			emitter.setContinuous(false);
			emitter.duration = duration;
			emitter.durationTimer = 0;
		}
	}

	public void setPosition (float x, float y) {
		for (int i = 0, n = emitters.size(); i < n; i++)
			emitters.get(i).setPosition(x, y);
	}

	public ArrayList<ParticleEmitter> getEmitters () {
		return emitters;
	}

	public void save (File file) {
		Writer output = null;
		try {
			output = new FileWriter(file);
			int index = 0;
			for (int i = 0, n = emitters.size(); i < n; i++) {
				ParticleEmitter emitter = emitters.get(i);
				if (index++ > 0) output.write("\n\n");
				emitter.save(output);
				output.write("- Image Path -\n");
				output.write(emitter.getImagePath() + "\n");
			}
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error saving effect: " + file, ex);
		} finally {
			try {
				if (output != null) output.close();
			} catch (IOException ex) {
			}
		}
	}

	public void load (FileHandle effectFile, FileHandle imagesDir) {
		loadEmitters(effectFile);
		loadEmitterImages(imagesDir);
	}

	public void load (FileHandle effectFile, TextureAtlas atlas) {
		loadEmitters(effectFile);
		loadEmitterImages(atlas);
	}

	public void loadEmitters (FileHandle effectFile) {
		InputStream input = effectFile.read();
		emitters.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input), 512);
			while (true) {
				ParticleEmitter emitter = new ParticleEmitter(reader);
				reader.readLine();
				emitter.setImagePath(reader.readLine());
				emitters.add(emitter);
				if (reader.readLine() == null) break;
				if (reader.readLine() == null) break;
			}
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException ex) {
			}
		}
	}

	public void loadEmitterImages (TextureAtlas atlas) {
		for (int i = 0, n = emitters.size(); i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			String imagePath = emitter.getImagePath();
			if (imagePath == null) continue;
			String imageName = new File(imagePath.replace('\\', '/')).getName();
			int lastDotIndex = imageName.lastIndexOf('.');
			if (lastDotIndex != -1) imageName = imageName.substring(0, lastDotIndex);
			Sprite sprite = atlas.getSprite(imageName);
			if (sprite == null) throw new IllegalArgumentException("SpriteSheet missing image: " + imageName);
			emitter.setSprite(sprite);
		}
	}

	public void loadEmitterImages (FileHandle imagesDir) {
		for (int i = 0, n = emitters.size(); i < n; i++) {
			ParticleEmitter emitter = emitters.get(i);
			String imagePath = emitter.getImagePath();
			if (imagePath == null) continue;
			String imageName = new File(imagePath.replace('\\', '/')).getName();
			emitter.setSprite(new Sprite(loadTexture(imagesDir.child(imageName))));
		}
	}

	protected Texture loadTexture (FileHandle file) {
		return Gdx.graphics.newTexture(file, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge,
			TextureWrap.ClampToEdge);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3821.java