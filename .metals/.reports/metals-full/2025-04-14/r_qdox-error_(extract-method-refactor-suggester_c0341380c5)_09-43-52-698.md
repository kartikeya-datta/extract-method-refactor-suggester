error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5514.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5514.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5514.java
text:
```scala
i@@f (!handle.parent().exists()) fail();

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class FilesTest extends GdxTest {
	String message = "";
	boolean success;
	BitmapFont font;
	SpriteBatch batch;

	@Override
	public void create () {
		font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
		batch = new SpriteBatch();

		if (Gdx.files.isExternalStorageAvailable()) {
			message += "External storage available\n";
			message += "External storage path: " + Gdx.files.getExternalStoragePath() + "\n";

			try {
				InputStream in = Gdx.files.internal("data/cube.obj").read();
				try {
					in.close();
				} catch (IOException e) {
				}
				message += "Open internal success\n";
			} catch (Throwable e) {
				message += "Couldn't open internal data/cube.obj\n" + e.getMessage() + "\n";
			}

			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external("test.txt").write(false)));
				out.write("test");
				message += "Write external success\n";
			} catch (GdxRuntimeException ex) {
				message += "Couldn't open externalstorage/test.txt\n";
			} catch (IOException e) {
				message += "Couldn't write externalstorage/test.txt\n";
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
			}

			try {
				InputStream in = Gdx.files.external("test.txt").read();
				try {
					in.close();
				} catch (IOException e) {
				}
				message += "Open external success\n";
			} catch (Throwable e) {
				message += "Couldn't open internal externalstorage/test.txt\n" + e.getMessage() + "\n";
			}

			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(Gdx.files.external("test.txt").read()));
				if (!in.readLine().equals("test"))
					message += "Read result wrong\n";
				else
					message += "Read external success\n";
			} catch (GdxRuntimeException ex) {
				message += "Couldn't open externalstorage/test.txt\n";
			} catch (IOException e) {
				message += "Couldn't read externalstorage/test.txt\n";
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}

			if (!Gdx.files.external("test.txt").delete()) message += "Couldn't delete externalstorage/test.txt";
		} else {
			message += "External storage not available";
		}
		if (Gdx.files.isLocalStorageAvailable()) {
			message += "Local storage available\n";
			message += "Local storage path: " + Gdx.files.getLocalStoragePath() + "\n";

			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(Gdx.files.local("test.txt").write(false)));
				out.write("test");
				message += "Write local success\n";
			} catch (GdxRuntimeException ex) {
				message += "Couldn't open localstorage/test.txt\n";
			} catch (IOException e) {
				message += "Couldn't write localstorage/test.txt\n";
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
			}

			try {
				InputStream in = Gdx.files.local("test.txt").read();
				try {
					in.close();
				} catch (IOException e) {
				}
				message += "Open local success\n";
			} catch (Throwable e) {
				message += "Couldn't open localstorage/test.txt\n" + e.getMessage() + "\n";
			}

			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(Gdx.files.local("test.txt").read()));
				if (!in.readLine().equals("test"))
					message += "Read result wrong\n";
				else
					message += "Read local success\n";
			} catch (GdxRuntimeException ex) {
				message += "Couldn't open localstorage/test.txt\n";
			} catch (IOException e) {
				message += "Couldn't read localstorage/test.txt\n";
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}

			if (!Gdx.files.local("test.txt").delete()) message += "Couldn't delete localstorage/test.txt";
		}
		try {
			testClasspath();
			testInternal();
			testExternal();
			testAbsolute();
			testLocal();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void testClasspath () throws IOException {
		// no classpath support on ios
		if(Gdx.app.getType() == ApplicationType.iOS) return;
		FileHandle handle = Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png");
		if (!handle.exists()) fail();
		if (handle.isDirectory()) fail();
		try {
			handle.delete();
			fail();
		} catch (Exception expected) {
		}
		try {
			handle.list();
			fail();
		} catch (Exception expected) {
		}
		try {
			handle.read().close();
			fail();
		} catch (Exception ignored) {
		}
		FileHandle dir = Gdx.files.classpath("com/badlogic/gdx/utils");
		if (dir.isDirectory()) fail();
		FileHandle child = dir.child("arial-15.fnt");
		if (!child.name().equals("arial-15.fnt")) fail();
		if (!child.nameWithoutExtension().equals("arial-15")) fail();
		if (!child.extension().equals("fnt")) fail();
		handle.read().close();
		if (handle.readBytes().length != handle.length()) fail();
	}

	private void testInternal () throws IOException {
		FileHandle handle = Gdx.files.internal("data/badlogic.jpg");
		if (!handle.exists()) fail("Couldn't find internal file");
		if (handle.isDirectory()) fail("Internal file shouldn't be a directory");
		try {
			handle.delete();
			fail("Shouldn't be able to delete internal file");
		} catch (Exception expected) {
		}
		if (handle.list().length != 0) fail("File length shouldn't be 0");
		if (Gdx.app.getType() != ApplicationType.Android) {
			if (!handle.parent().exists()) fail("Parent doesn't exist");
		}
		try {
			handle.read().close();
			fail();
		} catch (Exception ignored) {
		}
		FileHandle dir;
		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS)
			dir = Gdx.files.internal("data");
		else
			dir = Gdx.files.internal("../gdx-tests-android/assets/data");
		if (Gdx.app.getType() != ApplicationType.Android) {
			if (!dir.exists()) fail();
		}
		if (!dir.isDirectory()) fail();
		if (dir.list().length == 0) fail();
		FileHandle child = dir.child("badlogic.jpg");
		if (!child.name().equals("badlogic.jpg")) fail();
		if (!child.nameWithoutExtension().equals("badlogic")) fail();
		if (!child.extension().equals("jpg")) fail();
		if (Gdx.app.getType() != ApplicationType.Android) {
			if (!child.parent().exists()) fail();
		}
		FileHandle copy = Gdx.files.external("badlogic.jpg-copy");
		copy.delete();
		if (copy.exists()) fail();
		handle.copyTo(copy);
		if (!copy.exists()) fail();
		if (copy.length() != 68465) fail();
		copy.delete();
		if (copy.exists()) fail();
		handle.read().close();
		if (handle.readBytes().length != handle.length()) fail();
	}

	private void testExternal () throws IOException {
		String path = "meow";
		FileHandle handle = Gdx.files.external(path);
		handle.delete();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		if (handle.delete()) fail();
		if (handle.list().length != 0) fail();
		if (handle.child("meow").exists()) fail();
		if (!handle.parent().exists()) fail();
		try {
			handle.read().close();
			fail();
		} catch (Exception ignored) {
		}
		handle.mkdirs();
		if (!handle.exists()) fail();
		if (!handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		handle.child("meow").mkdirs();
		if (handle.list().length != 1) fail();
		FileHandle child = handle.list()[0];
		if (!child.name().equals("meow")) fail();
		if (!child.parent().exists()) fail();
		if (!handle.deleteDirectory()) fail();
		if (handle.exists()) fail();
		OutputStream output = handle.write(false);
		output.write("moo".getBytes());
		output.close();
		if (!handle.exists()) fail();
		if (handle.length() != 3) fail();
		FileHandle copy = Gdx.files.external(path + "-copy");
		copy.delete();
		if (copy.exists()) fail();
		handle.copyTo(copy);
		if (!copy.exists()) fail();
		if (copy.length() != 3) fail();
		FileHandle move = Gdx.files.external(path + "-move");
		move.delete();
		if (move.exists()) fail();
		copy.moveTo(move);
		if (!move.exists()) fail();
		if (move.length() != 3) fail();
		move.deleteDirectory();
		if (move.exists()) fail();
		InputStream input = handle.read();
		byte[] bytes = new byte[6];
		if (input.read(bytes) != 3) fail();
		input.close();
		if (!new String(bytes, 0, 3).equals("moo")) fail();
		output = handle.write(true);
		output.write("cow".getBytes());
		output.close();
		if (handle.length() != 6) fail();
		input = handle.read();
		if (input.read(bytes) != 6) fail();
		input.close();
		if (!new String(bytes, 0, 6).equals("moocow")) fail();
		if (handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		if (!handle.name().equals("meow")) fail();
		if (!handle.nameWithoutExtension().equals("meow")) fail();
		if (!handle.extension().equals("")) fail();
		handle.deleteDirectory();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		handle.delete();
		handle.deleteDirectory();
	}

	private void testAbsolute () throws IOException {
		String path = new File(Gdx.files.getExternalStoragePath(), "meow").getAbsolutePath();
		FileHandle handle = Gdx.files.absolute(path);
		handle.delete();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		if (handle.delete()) fail();
		if (handle.list().length != 0) fail();
		if (handle.child("meow").exists()) fail();
		if (!handle.parent().exists()) fail();
		try {
			handle.read().close();
			fail();
		} catch (Exception ignored) {
		}
		handle.mkdirs();
		if (!handle.exists()) fail();
		if (!handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		handle.child("meow").mkdirs();
		if (handle.list().length != 1) fail();
		FileHandle child = handle.list()[0];
		if (!child.name().equals("meow")) fail();
		if (!child.parent().exists()) fail();
		if (!handle.deleteDirectory()) fail();
		if (handle.exists()) fail();
		OutputStream output = handle.write(false);
		output.write("moo".getBytes());
		output.close();
		if (!handle.exists()) fail();
		if (handle.length() != 3) fail();
		FileHandle copy = Gdx.files.absolute(path + "-copy");
		copy.delete();
		if (copy.exists()) fail();
		handle.copyTo(copy);
		if (!copy.exists()) fail();
		if (copy.length() != 3) fail();
		FileHandle move = Gdx.files.absolute(path + "-move");
		move.delete();
		if (move.exists()) fail();
		copy.moveTo(move);
		if (!move.exists()) fail();
		if (move.length() != 3) fail();
		move.deleteDirectory();
		if (move.exists()) fail();
		InputStream input = handle.read();
		byte[] bytes = new byte[6];
		if (input.read(bytes) != 3) fail();
		input.close();
		if (!new String(bytes, 0, 3).equals("moo")) fail();
		output = handle.write(true);
		output.write("cow".getBytes());
		output.close();
		if (handle.length() != 6) fail();
		input = handle.read();
		if (input.read(bytes) != 6) fail();
		input.close();
		if (!new String(bytes, 0, 6).equals("moocow")) fail();
		if (handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		if (!handle.name().equals("meow")) fail();
		if (!handle.nameWithoutExtension().equals("meow")) fail();
		if (!handle.extension().equals("")) fail();
		handle.deleteDirectory();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		handle.delete();
		handle.deleteDirectory();
	}

	private void testLocal () throws IOException {
		String path = "meow";
		FileHandle handle = Gdx.files.local(path);
		handle.delete();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		if (handle.delete()) fail();
		if (handle.list().length != 0) fail();
		if (handle.child("meow").exists()) fail();
		if (handle.parent().exists()) fail();
		try {
			handle.read().close();
			fail();
		} catch (Exception ignored) {
		}
		handle.mkdirs();
		if (!handle.exists()) fail();
		if (!handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		handle.child("meow").mkdirs();
		if (handle.list().length != 1) fail();
		FileHandle child = handle.list()[0];
		if (!child.name().equals("meow")) fail();
		if (!child.parent().exists()) fail();
		if (!handle.deleteDirectory()) fail();
		if (handle.exists()) fail();
		OutputStream output = handle.write(false);
		output.write("moo".getBytes());
		output.close();
		if (!handle.exists()) fail();
		if (handle.length() != 3) fail();
		FileHandle copy = Gdx.files.local(path + "-copy");
		copy.delete();
		if (copy.exists()) fail();
		handle.copyTo(copy);
		if (!copy.exists()) fail();
		if (copy.length() != 3) fail();
		FileHandle move = Gdx.files.local(path + "-move");
		move.delete();
		if (move.exists()) fail();
		copy.moveTo(move);
		if (!move.exists()) fail();
		if (move.length() != 3) fail();
		move.deleteDirectory();
		if (move.exists()) fail();
		InputStream input = handle.read();
		byte[] bytes = new byte[6];
		if (input.read(bytes) != 3) fail();
		input.close();
		if (!new String(bytes, 0, 3).equals("moo")) fail();
		output = handle.write(true);
		output.write("cow".getBytes());
		output.close();
		if (handle.length() != 6) fail();
		input = handle.read();
		if (input.read(bytes) != 6) fail();
		input.close();
		if (!new String(bytes, 0, 6).equals("moocow")) fail();
		if (handle.isDirectory()) fail();
		if (handle.list().length != 0) fail();
		if (!handle.name().equals("meow")) fail();
		if (!handle.nameWithoutExtension().equals("meow")) fail();
		if (!handle.extension().equals("")) fail();
		handle.deleteDirectory();
		if (handle.exists()) fail();
		if (handle.isDirectory()) fail();
		handle.delete();
		handle.deleteDirectory();
	}

	private void fail () {
		throw new RuntimeException();
	}
	
	private void fail(String msg) {
		throw new RuntimeException(msg);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.drawMultiLine(batch, message, 20, Gdx.graphics.getHeight() - 20);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	@Override
	public boolean needsGL20 () {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5514.java