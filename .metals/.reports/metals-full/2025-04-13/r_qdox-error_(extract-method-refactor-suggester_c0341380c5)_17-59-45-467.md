error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7193.java
text:
```scala
s@@tage.getViewport().update(width, height, true);

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

package com.badlogic.gdx.tests.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tests.utils.GdxTest;

public class NetAPITest extends GdxTest implements HttpResponseListener {

	SpriteBatch batch;
	Skin skin;
	Stage stage;
	TextButton btnDownloadImage;
	TextButton btnDownloadText;
	TextButton btnDownloadError;
	TextButton btnPost;
	TextButton btnCancel;
	Label statusLabel;
	Texture texture;
	String text;
	BitmapFont font;
	HttpRequest httpRequest;

	Object clickedButton;

	public boolean needsGL20 () {
		// just because the non pot, we could change the image instead...
		return true;
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		font.dispose();
		if (texture != null) texture.dispose();
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		font = new BitmapFont();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		{
			statusLabel = new Label("", skin);
			statusLabel.setWrap(true);
			statusLabel.setWidth(Gdx.graphics.getWidth() * 0.96f);
			statusLabel.setAlignment(Align.center);
			statusLabel.setPosition(Gdx.graphics.getWidth() * 0.5f - statusLabel.getWidth() * 0.5f, 30f);
			statusLabel.setColor(Color.CYAN);
			stage.addActor(statusLabel);
		}

		{
			ClickListener clickListener = new ClickListener() {
				@Override
				public void clicked (InputEvent event, float x, float y) {
					super.clicked(event, x, y);

					clickedButton = event.getListenerActor();
					setButtonDisabled(true);
					if (texture != null) texture.dispose();
					texture = null;
					text = null;

					String url;
					String httpMethod = Net.HttpMethods.GET;
					String requestContent = null;
					if (clickedButton == btnDownloadImage)
						url = "http://i.imgur.com/vxomF.jpg";
					else if (clickedButton == btnDownloadText)
						url = "http://www.apache.org/licenses/LICENSE-2.0.txt";
					else if (clickedButton == btnDownloadError)
						url = "http://www.badlogicgames.com/doesnotexist";
					else {
						url = "http://posttestserver.com/post.php?dump";
						httpMethod = Net.HttpMethods.POST;
						requestContent = "name1=value1&name2=value2";
					}

					httpRequest = new HttpRequest(httpMethod);
					httpRequest.setUrl(url);
					httpRequest.setContent(requestContent);
					Gdx.net.sendHttpRequest(httpRequest, NetAPITest.this);

					statusLabel.setText("Downloading data from " + httpRequest.getUrl());
				}
			};

			ClickListener cancelListener = new ClickListener() {
				@Override
				public void clicked (InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Gdx.net.cancelHttpRequest(httpRequest);
					statusLabel.setText("Cancelling request " + httpRequest.getUrl());
				}
			};

			btnCancel = new TextButton("Cancel", skin);
			btnCancel.setPosition(Gdx.graphics.getWidth() * 0.5f - btnCancel.getWidth() * 1.5f, 60f);
			btnCancel.addListener(cancelListener);
			stage.addActor(btnCancel);

			btnDownloadImage = new TextButton("GET Image", skin);
			btnDownloadImage.setPosition(btnCancel.getX() + btnCancel.getWidth() + 10, 60f);
			btnDownloadImage.addListener(clickListener);
			stage.addActor(btnDownloadImage);

			btnDownloadText = new TextButton("GET Text", skin);
			btnDownloadText.setPosition(btnDownloadImage.getX() + btnDownloadImage.getWidth() + 10, 60f);
			btnDownloadText.addListener(clickListener);
			stage.addActor(btnDownloadText);

			btnDownloadError = new TextButton("GET Error", skin);
			btnDownloadError.setPosition(btnDownloadText.getX() + btnDownloadText.getWidth() + 10, 60f);
			btnDownloadError.addListener(clickListener);
			stage.addActor(btnDownloadError);

			btnPost = new TextButton("POST", skin);
			btnPost.setPosition(btnDownloadError.getX() + btnDownloadError.getWidth() + 10, 60f);
			btnPost.addListener(clickListener);
			stage.addActor(btnPost);
		}

	}

	@Override
	public void handleHttpResponse (HttpResponse httpResponse) {

		final int statusCode = httpResponse.getStatus().getStatusCode();
		// We are not in main thread right now so we need to post to main thread for ui updates
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run () {
				statusLabel.setText("HTTP Request status: " + statusCode);
				setButtonDisabled(false);
			}
		});

		if (statusCode != 200) {
			Gdx.app.log("NetAPITest", "An error ocurred since statusCode is not OK");
			setText(httpResponse);
			return;
		}

		if (clickedButton == btnDownloadImage) {
			final byte[] rawImageBytes = httpResponse.getResult();
			Gdx.app.postRunnable(new Runnable() {
				public void run () {
					Pixmap pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
					texture = new Texture(pixmap);
				}
			});
		} else {
			setText(httpResponse);
		}
	}

	void setText (HttpResponse httpResponse) {
		final String newText = httpResponse.getResultAsString();
		Gdx.app.postRunnable(new Runnable() {
			public void run () {
				text = newText;
			}
		});
	}

	void setButtonDisabled (boolean disabled) {
		Touchable t = disabled ? Touchable.disabled : Touchable.enabled;

		btnDownloadImage.setDisabled(disabled);
		btnDownloadImage.setTouchable(t);

		btnDownloadText.setDisabled(disabled);
		btnDownloadText.setTouchable(t);

		btnDownloadError.setDisabled(disabled);
		btnDownloadError.setTouchable(t);

		btnPost.setDisabled(disabled);
		btnPost.setTouchable(t);
	}

	@Override
	public void failed (Throwable t) {
		setButtonDisabled(false);
		statusLabel.setText("Failed to perform the HTTP Request: " + t.getMessage());
		t.printStackTrace();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (texture != null) {
			batch.begin();
			batch.draw(texture, Gdx.graphics.getWidth() * 0.5f - texture.getWidth() * 0.5f, 100f);
			batch.end();
		} else if (text != null) {
			batch.begin();
			font.drawMultiLine(batch, text, 10, Gdx.graphics.getHeight() - 10);
			batch.end();
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void cancelled () {
		Gdx.app.postRunnable(new Runnable() {
			public void run () {
				setButtonDisabled(false);
				statusLabel.setText("HTTP request cancelled");
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7193.java