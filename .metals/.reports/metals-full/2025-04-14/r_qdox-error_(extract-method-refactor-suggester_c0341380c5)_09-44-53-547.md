error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/978.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/978.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/978.java
text:
```scala
t@@ouchEventPool.freeAll(touchEvents);

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
package com.badlogic.gdx.backends.ios;

import cli.MonoTouch.Foundation.NSObject;
import cli.MonoTouch.Foundation.NSSet;
import cli.MonoTouch.Foundation.NSSetEnumerator;
import cli.MonoTouch.UIKit.UIAcceleration;
import cli.MonoTouch.UIKit.UIAccelerometer;
import cli.MonoTouch.UIKit.UIAccelerometerDelegate;
import cli.MonoTouch.UIKit.UIEvent;
import cli.MonoTouch.UIKit.UITouch;
import cli.MonoTouch.UIKit.UITouchPhase;
import cli.System.Drawing.PointF;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

public class IOSInput implements Input {
	static final int MAX_TOUCHES = 20;

	IOSApplication app;
	IOSApplicationConfiguration config;
	int[] deltaX = new int[MAX_TOUCHES];
	int[] deltaY = new int[MAX_TOUCHES];
	int[] touchX = new int[MAX_TOUCHES];
	int[] touchY = new int[MAX_TOUCHES];
	// we store the pointer to the UITouch struct here, or 0
	int[] touchDown = new int[MAX_TOUCHES];
	int numTouched = 0;
	boolean justTouched = false;
	Pool<TouchEvent> touchEventPool = new Pool<TouchEvent>() {
		@Override
		protected TouchEvent newObject() {
			return new TouchEvent();
		}
	};
	Array<TouchEvent> touchEvents = new Array<TouchEvent>();
	TouchEvent currentEvent = null;
	float[] acceleration = new float[3];
	InputProcessor inputProcessor = null;
	
	public IOSInput(IOSApplication app) {
		this.app = app;
		this.config = app.config;
	}
	
	void setupPeripherals() {
		setupAccelerometer();
	}

	private void setupAccelerometer() {
		if(config.useAccelerometer) {
			UIAccelerometer.get_SharedAccelerometer().set_Delegate(new UIAccelerometerDelegate() {

				@Override
				public void DidAccelerate(UIAccelerometer accelerometer, UIAcceleration values) {
					//super.DidAccelerate(accelerometer, values);
					// FIXME take orientation into account, these values here get flipped by iOS...
					acceleration[0] = (float)values.get_X() * 10;
					acceleration[1] = (float)values.get_Y() * 10;
					acceleration[2] = (float)values.get_Z() * 10;
				}
			});
			UIAccelerometer.get_SharedAccelerometer().set_UpdateInterval(config.accelerometerUpdate);
		}
	}

	@Override
	public float getAccelerometerX() {
		return acceleration[0];
	}

	@Override
	public float getAccelerometerY() {
		return acceleration[1];
	}

	@Override
	public float getAccelerometerZ() {
		return acceleration[2];
	}

	@Override
	public float getAzimuth() {
		// FIXME implement this
		return 0;
	}

	@Override
	public float getPitch() {
		// FIXME implement this
		return 0;
	}

	@Override
	public float getRoll() {
		// FIXME implement this
		return 0;
	}

	@Override
	public void getRotationMatrix(float[] matrix) {
		// FIXME implement this
	}

	@Override
	public int getX() {
		return touchX[0];
	}

	@Override
	public int getX(int pointer) {
		return touchX[pointer];
	}

	@Override
	public int getDeltaX() {
		return deltaX[0];
	}

	@Override
	public int getDeltaX(int pointer) {
		return deltaX[pointer];
	}

	@Override
	public int getY() {
		return touchY[0];
	}

	@Override
	public int getY(int pointer) {
		return touchY[pointer];
	}

	@Override
	public int getDeltaY() {
		return deltaY[0];
	}

	@Override
	public int getDeltaY(int pointer) {
		return deltaY[pointer];
	}

	@Override
	public boolean isTouched() {
		return touchDown[0] != 0;
	}

	@Override
	public boolean justTouched() {
		return justTouched;
	}

	@Override
	public boolean isTouched(int pointer) {
		return touchDown[pointer] != 0;
	}

	@Override
	public boolean isButtonPressed(int button) {
		return button == Buttons.LEFT && numTouched > 0;
	}

	@Override
	public boolean isKeyPressed(int key) {
		return false;
	}

	@Override
	public void getTextInput(TextInputListener listener, String title, String text) {
		// FIXME implement this
	}

	@Override
	public void getPlaceholderTextInput(TextInputListener listener, String title, String placeholder) {
		// FIXME implement this
	}

	@Override
	public void setOnscreenKeyboardVisible(boolean visible) {
	}

	@Override
	public void vibrate(int milliseconds) {
		// FIXME implement this
	}

	@Override
	public void vibrate(long[] pattern, int repeat) {
		// FIXME implement this
	}

	@Override
	public void cancelVibrate() {
		// FIXME implement this
	}

	@Override
	public long getCurrentEventTime() {
		return currentEvent.timestamp;
	}

	@Override
	public void setCatchBackKey(boolean catchBack) {
	}

	@Override
	public void setCatchMenuKey(boolean catchMenu) {
	}

	@Override
	public void setInputProcessor(InputProcessor processor) {
		this.inputProcessor = processor;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return inputProcessor;
	}

	@Override
	public boolean isPeripheralAvailable(Peripheral peripheral) {
		if(peripheral == Peripheral.Accelerometer && config.useAccelerometer) return true;
		if(peripheral == Peripheral.MultitouchScreen) return true;
		// FIXME implement this (not sure if possible)
//		if(peripheral == Peripheral.OnscreenKeyboard) return true;
		// FIXME implement this
//		if(peripheral == Peripheral.Compass) return true;
		
		return false;
	}

	@Override
	public int getRotation() {
		// FIXME implement this
		return 0;
	}

	@Override
	public Orientation getNativeOrientation() {
		// FIXME implement this
		return null;
	}

	@Override
	public void setCursorCatched(boolean catched) {
	}

	@Override
	public boolean isCursorCatched() {
		return false;
	}

	@Override
	public void setCursorPosition(int x, int y) {
	}

	public void touchDown(NSSet touches, UIEvent event) {
		toTouchEvents(touches, event);
	}

	public void touchUp(NSSet touches, UIEvent event) {
		toTouchEvents(touches, event);
	}

	public void touchMoved(NSSet touches, UIEvent event) {
		toTouchEvents(touches, event);
	}
	
	void processEvents() {
		synchronized(touchEvents) {
			justTouched = false;
			for(TouchEvent event: touchEvents) {
				currentEvent = event;
				switch(event.phase) {
				case UITouchPhase.Began:
					if(inputProcessor != null) inputProcessor.touchDown(event.x, event.y, event.pointer, Buttons.LEFT);
					if(numTouched == 1)
						justTouched = true;
					break;
				case UITouchPhase.Cancelled:
				case UITouchPhase.Ended:
					if(inputProcessor != null) inputProcessor.touchUp(event.x, event.y, event.pointer, Buttons.LEFT);
					break;
				case UITouchPhase.Moved:
				case UITouchPhase.Stationary:
					if(inputProcessor != null) inputProcessor.touchDragged(event.x, event.y, event.pointer);
					break;
				}
			}
			touchEventPool.free(touchEvents);
			touchEvents.clear();
		}
	}
	
	NSSetEnumerator touchEnumerator = new NSSetEnumerator(new NSSetEnumerator.Method() {
		public void Invoke(NSObject obj, boolean[] stop) {
			UITouch touch = (UITouch) obj;
			PointF loc = touch.LocationInView(touch.get_View());
			synchronized(touchEvents) {
				TouchEvent event = touchEventPool.obtain();
				event.x = (int)(loc.get_X() * app.displayScaleFactor);
				event.y = (int)(loc.get_Y() * app.displayScaleFactor);
				event.phase = touch.get_Phase().Value;
				event.timestamp = (long)(touch.get_Timestamp() * 1000000000);
				touchEvents.add(event);
				
				if(touch.get_Phase().Value == UITouchPhase.Began) {
					event.pointer = getFreePointer();
					touchDown[event.pointer] = touch.get_Handle().ToInt32();
					touchX[event.pointer] = event.x;
					touchY[event.pointer] = event.y;
					deltaX[event.pointer] = 0;
					deltaY[event.pointer] = 0; 
					numTouched++;
				}
				
				if(touch.get_Phase().Value == UITouchPhase.Moved ||
					touch.get_Phase().Value == UITouchPhase.Stationary) {
					event.pointer = findPointer(touch);
					deltaX[event.pointer] = event.x - touchX[event.pointer];
					deltaY[event.pointer] = event.y - touchY[event.pointer]; 
					touchX[event.pointer] = event.x;
					touchY[event.pointer] = event.y;
				}
				
				if(touch.get_Phase().Value == UITouchPhase.Cancelled ||
					touch.get_Phase().Value == UITouchPhase.Ended) {
					event.pointer = findPointer(touch);
					touchDown[event.pointer] = 0; 
					touchX[event.pointer] = event.x;
					touchY[event.pointer] = event.y;
					deltaX[event.pointer] = 0;
					deltaY[event.pointer] = 0;
					numTouched--;
				}
			}
			stop[0] = false;
		}
	});
	
	private int getFreePointer() {
		for(int i = 0; i < touchDown.length; i++) {
			if(touchDown[i] == 0) return i;
		}
		throw new GdxRuntimeException("Couldn't find free pointer id!");
	}
	
	private int findPointer(UITouch touch) {
		int ptr = touch.get_Handle().ToInt32();
		for(int i = 0; i < touchDown.length; i++) {
			if(touchDown[i] == ptr) return i;
		}
		throw new GdxRuntimeException("Couldn't find pointer id for touch event!");
	}

	private void toTouchEvents(NSSet touches, UIEvent event) {
		touches.Enumerate(touchEnumerator);
	}
	
	static class TouchEvent {
		int phase;
		long timestamp;
		int x, y;
		int pointer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/978.java