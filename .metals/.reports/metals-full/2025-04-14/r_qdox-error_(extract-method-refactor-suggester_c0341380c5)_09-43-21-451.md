error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9338.java
text:
```scala
e@@lse processor.mouseMoved(mouseX, mouseY);

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
package com.badlogic.gdx.backends.gwt;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;

public class GwtInput implements Input {
	boolean touched = false;
	int mouseX, mouseY;
	int deltaX, deltaY;
	boolean justTouched = false;
	Set<Integer> pressedButtons = new HashSet<Integer>();
	Set<Integer> pressedKeys = new HashSet<Integer>();
	InputProcessor processor;
	char lastKeyCharPressed;
	float keyRepeatTimer;
	long currentEventTimeStamp;
	final CanvasElement canvas;
	boolean hasFocus = true;

	public GwtInput (CanvasElement canvas) {
		this.canvas = canvas;
		hookEvents();
	}

	@Override
	public float getAccelerometerX () {
		return 0;
	}

	@Override
	public float getAccelerometerY () {
		return 0;
	}

	@Override
	public float getAccelerometerZ () {
		return 0;
	}

	@Override
	public int getX () {
		return mouseX;
	}

	@Override
	public int getX (int pointer) {
		if (pointer != 0) return 0;
		return mouseX;
	}

	@Override
	public int getDeltaX () {
		return deltaX;
	}

	@Override
	public int getDeltaX (int pointer) {
		if (pointer != 0) return 0;
		return deltaX;
	}

	@Override
	public int getY () {
		return mouseY;
	}

	@Override
	public int getY (int pointer) {
		if (pointer != 0) return 0;
		return mouseY;
	}

	@Override
	public int getDeltaY () {
		return deltaY;
	}

	@Override
	public int getDeltaY (int pointer) {
		if (pointer != 0) return 0;
		return deltaY;
	}

	@Override
	public boolean isTouched () {
		return touched;
	}

	@Override
	public boolean justTouched () {
		return justTouched;
	}

	@Override
	public boolean isTouched (int pointer) {
		if (pointer != 0) return false;
		return touched;
	}

	@Override
	public boolean isButtonPressed (int button) {
		return button == Buttons.LEFT && touched;
	}

	@Override
	public boolean isKeyPressed (int key) {
		if(key == Keys.ANY_KEY) return pressedKeys.size() > 0;
		return pressedKeys.contains(key);
	}

	@Override
	public void getTextInput (TextInputListener listener, String title, String text) {
		String input = getTextInputJSNI(title, text);
		if (input != null) {
			listener.input(input);
		}
		else {
			listener.canceled();
		}
	}

	@Override
	public void getPlaceholderTextInput (TextInputListener listener, String title, String placeholder) {
		String input = getTextInputJSNI(title, placeholder);
		if (input != null) {
			if (input.equals(placeholder)) {
				listener.input(input);
			}
			else {
				listener.canceled();
			}
		}
		else {
			listener.canceled();
		}
	}

	@Override
	public void setOnscreenKeyboardVisible (boolean visible) {
	}

	@Override
	public void vibrate (int milliseconds) {
	}

	@Override
	public void vibrate (long[] pattern, int repeat) {
	}

	@Override
	public void cancelVibrate () {
	}

	@Override
	public float getAzimuth () {
		return 0;
	}

	@Override
	public float getPitch () {
		return 0;
	}

	@Override
	public float getRoll () {
		return 0;
	}

	@Override
	public void getRotationMatrix (float[] matrix) {
	}

	@Override
	public long getCurrentEventTime () {
		return currentEventTimeStamp;
	}

	@Override
	public void setCatchBackKey (boolean catchBack) {
	}

	@Override
	public void setCatchMenuKey (boolean catchMenu) {
	}

	@Override
	public void setInputProcessor (InputProcessor processor) {
		this.processor = processor;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return processor;
	}

	@Override
	public boolean isPeripheralAvailable (Peripheral peripheral) {
		if (peripheral == Peripheral.Accelerometer) return false;
		if (peripheral == Peripheral.Compass) return false;
		if (peripheral == Peripheral.HardwareKeyboard) return true;
		if (peripheral == Peripheral.MultitouchScreen) return false;
		if (peripheral == Peripheral.OnscreenKeyboard) return false;
		if (peripheral == Peripheral.Vibrator) return false;
		return false;
	}

	@Override
	public int getRotation () {
		return 0;
	}

	@Override
	public Orientation getNativeOrientation () {
		return Orientation.Landscape;
	}

	private native String getTextInputJSNI(String title, String text) /*-{
		return prompt("Please enter your name","Harry Potter");
	}-*/;
	
	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * @return is Cursor catched
	 */
	private native boolean isCursorCatchedJSNI() /*-{
		if(!navigator.pointer) {
	        navigator.pointer = navigator.webkitPointer || navigator.mozPointer;
    	}
	    if(navigator.pointer) {
	        if(typeof(navigator.pointer.isLocked) === "boolean") {
	            // Chrome initially launched with this interface
	            return navigator.pointer.isLocked;
	        } else if(typeof(navigator.pointer.isLocked) === "function") {
	            // Some older builds might provide isLocked as a function
	            return navigator.pointer.isLocked();
	        } else if(typeof(navigator.pointer.islocked) === "function") {
	            // For compatibility with early Firefox build
	            return navigator.pointer.islocked();
	        }
	    }
    	return false;
   	}-*/;

	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * @param element Canvas
	 */
	private native void setCursorCatchedJSNI(CanvasElement element) /*-{
	    // Navigator pointer is not the right interface according to spec.
	    // Here for backwards compatibility only
	    if(!navigator.pointer) {
	        navigator.pointer = navigator.webkitPointer || navigator.mozPointer;
    	}
    	// element.requestPointerLock
	    if(!element.requestPointerLock) {
	        element.requestPointerLock = (function() {
	            return  element.webkitRequestPointerLock ||
	                    element.mozRequestPointerLock    ||
	                    function(){
	                        if(navigator.pointer) {
	                            navigator.pointer.lock(element);
	                        }
	                    };
	        })();
	    }
	    element.requestPointerLock();
	}-*/;
	
	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 */
	private native void exitCursorCatchedJSNI() /*-{
    	if(!$doc.exitPointerLock) {
	        $doc.exitPointerLock = (function() {
	            return  $doc.webkitExitPointerLock ||
	                    $doc.mozExitPointerLock ||
	                    function(){
	                        if(navigator.pointer) {
	                            var elem = this;
	                            navigator.pointer.unlock();
	                        }
	                    };
	        })();
    	}
	}-*/;
	
	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * @param event JavaScript Mouse Event
	 * @return movement in x direction
	 */
	private native float getMovementXJSNI(NativeEvent event) /*-{
		return event.movementX || event.webkitMovementX || 0;
	}-*/;
	
	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * @param event JavaScript Mouse Event
	 * @return movement in y direction
	 */
	private native float getMovementYJSNI(NativeEvent event) /*-{
		return event.movementY || event.webkitMovementY || 0;
	}-*/;
	
	/**
	 * works only for Chrome > Version 18 with enabled Mouse Lock 
	 * enable in about:flags or start Chrome with the --enable-pointer-lock flag
	 */
	@Override
	public void setCursorCatched (boolean catched) {
		if(catched) setCursorCatchedJSNI(canvas);
		else exitCursorCatchedJSNI();
	}

	@Override
	public boolean isCursorCatched () {
		return isCursorCatchedJSNI();
	}

	@Override
	public void setCursorPosition (int x, int y) {
		// FIXME??
	}

	// kindly borrowed from our dear playn friends...
	static native void addEventListener (JavaScriptObject target, String name, GwtInput handler, boolean capture) /*-{
		target
				.addEventListener(
						name,
						function(e) {
							handler.@com.badlogic.gdx.backends.gwt.GwtInput::handleEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
						}, capture);
	}-*/;

	private static native float getMouseWheelVelocity (NativeEvent evt) /*-{
		var delta = 0.0;
		var agentInfo = @com.badlogic.gdx.backends.gwt.GwtApplication::agentInfo()();

		if (agentInfo.isFirefox) {
			if (agentInfo.isMacOS) {
				delta = 1.0 * evt.detail;
			} else {
				delta = 1.0 * evt.detail / 3;
			}
		} else if (agentInfo.isOpera) {
			if (agentInfo.isLinux) {
				delta = -1.0 * evt.wheelDelta / 80;
			} else {
				// on mac
				delta = -1.0 * evt.wheelDelta / 40;
			}
		} else if (agentInfo.isChrome || agentInfo.isSafari) {
			delta = -1.0 * evt.wheelDelta / 120;
			// handle touchpad for chrome
			if (Math.abs(delta) < 1) {
				if (agentInfo.isWindows) {
					delta = -1.0 * evt.wheelDelta;
				} else if (agentInfo.isMacOS) {
					delta = -1.0 * evt.wheelDelta / 3;
				}
			}
		}
		return delta;
	}-*/;

	/** Kindly borrowed from PlayN. **/
	protected static native String getMouseWheelEvent() /*-{
    	if (navigator.userAgent.toLowerCase().indexOf('firefox') != -1) {
      	return "DOMMouseScroll";
   	} else {
      	return "mousewheel";
   	}
  	}-*/;
	
	/** Kindly borrowed from PlayN. **/
	protected static float getRelativeX (NativeEvent e, Element target) {
		return e.getClientX() - target.getAbsoluteLeft() + target.getScrollLeft() + target.getOwnerDocument().getScrollLeft();
	}

	/** Kindly borrowed from PlayN. **/
	protected static float getRelativeY (NativeEvent e, Element target) {
		return e.getClientY() - target.getAbsoluteTop() + target.getScrollTop() + target.getOwnerDocument().getScrollTop();
	}

	private void hookEvents () {
		addEventListener(canvas, "mousedown", this, true);
		addEventListener(Document.get(), "mousedown", this, true);
		addEventListener(canvas, "mouseup", this, true);
		addEventListener(Document.get(), "mouseup", this, true);
		addEventListener(canvas, "mousemove", this, true);
		addEventListener(Document.get(), "mousemove", this, true);
		addEventListener(canvas, getMouseWheelEvent(), this, true);
		addEventListener(Document.get(), "keydown", this, false);
		addEventListener(Document.get(), "keyup", this, false);
		addEventListener(Document.get(), "keypress", this, false);
	}
	
	private int getButton(int button) {
		if(button == NativeEvent.BUTTON_LEFT) return Buttons.LEFT;
		if(button == NativeEvent.BUTTON_RIGHT) return Buttons.RIGHT;
		if(button == NativeEvent.BUTTON_MIDDLE) return Buttons.MIDDLE;
		return Buttons.LEFT;
	}

	private void handleEvent (NativeEvent e) {
		
		if(e.getType().equals("mousedown")) {
			if(!e.getEventTarget().equals(canvas) || touched) {
				float mouseX = (int)getRelativeX(e, canvas);
				float mouseY = (int)getRelativeY(e, canvas);
				if(mouseX < 0 || mouseX > Gdx.graphics.getWidth() || mouseY < 0 || mouseY > Gdx.graphics.getHeight()) {
					hasFocus = false;
				}
				return;
			}
			hasFocus = true;
			this.justTouched = true;
			this.touched = true;
			this.pressedButtons.add(getButton(e.getButton()));
			this.deltaX = 0;
			this.deltaY = 0;
			if(isCursorCatched()) {
				this.mouseX += getMovementXJSNI(e);
				this.mouseY += getMovementYJSNI(e);
			} else {
				this.mouseX = (int)getRelativeX(e, canvas);
				this.mouseY = (int)getRelativeY(e, canvas);
			}
			this.currentEventTimeStamp = TimeUtils.nanoTime();
			if(processor != null) processor.touchDown(mouseX, mouseY, 0, getButton(e.getButton()));
		}
		
		if(e.getType().equals("mousemove")) {
			if(isCursorCatched()) {
				this.deltaX = (int) getMovementXJSNI(e);
				this.deltaY = (int) getMovementYJSNI(e);
				this.mouseX += getMovementXJSNI(e);
				this.mouseY += getMovementYJSNI(e);
			} else {
				this.deltaX = (int)getRelativeX(e, canvas) - mouseX;
				this.deltaY = (int)getRelativeY(e, canvas) - mouseY;
				this.mouseX = (int)getRelativeX(e, canvas);
				this.mouseY = (int)getRelativeY(e, canvas);
			}
			this.currentEventTimeStamp = TimeUtils.nanoTime();
			if(processor != null) {
				if(touched) processor.touchDragged(mouseX, mouseY, 0);
				else processor.touchMoved(mouseX, mouseY);
			}
		}
		
		if(e.getType().equals("mouseup")) {
			if(!touched) return;
			this.pressedButtons.remove(getButton(e.getButton()));
			this.touched = pressedButtons.size() > 0;
			if(isCursorCatched()) {
				this.deltaX = (int) getMovementXJSNI(e);
				this.deltaY = (int) getMovementYJSNI(e);
				this.mouseX += getMovementXJSNI(e);
				this.mouseY += getMovementYJSNI(e);
			} else {
				this.deltaX = (int)getRelativeX(e, canvas) - mouseX;
				this.deltaY = (int)getRelativeY(e, canvas) - mouseY;
				this.mouseX = (int)getRelativeX(e, canvas);
				this.mouseY = (int)getRelativeY(e, canvas);
			}
			this.currentEventTimeStamp = TimeUtils.nanoTime();
			this.touched = false;
			if(processor != null) processor.touchUp(mouseX, mouseY, 0, getButton(e.getButton()));
		}
		
		if(e.getType().equals("keydown") && hasFocus) {
			System.out.println("keydown");
			int code = keyForCode(e.getKeyCode());
			if(code == 67) {
				e.preventDefault();
				if(processor != null) {
					processor.keyDown(code);
					processor.keyTyped('\b');
				}
			} else {
				this.pressedKeys.add(code);
				if(processor != null) processor.keyDown(code);
			}
		}
		
		if(e.getType().equals("keypress") && hasFocus) {
			System.out.println("keypress");
			char c = (char)e.getCharCode();
			if(processor != null) processor.keyTyped(c);
		}
		
		if(e.getType().equals("keyup") && hasFocus) {
			System.out.println("keyup");
			int code = keyForCode(e.getKeyCode());
			this.pressedKeys.remove(code);
			if(processor != null) processor.keyUp(code);
		}
		
//		if(hasFocus) e.preventDefault();
	}
	
	/** borrowed from PlayN, thanks guys **/
  private static int keyForCode(int keyCode) {
	    switch (keyCode) {
	    case KeyCodes.KEY_ALT: return Keys.ALT_LEFT;
	    case KeyCodes.KEY_BACKSPACE: return Keys.BACKSPACE;
	    case KeyCodes.KEY_CTRL: return Keys.CONTROL_LEFT;
	    case KeyCodes.KEY_DELETE: return Keys.DEL;
	    case KeyCodes.KEY_DOWN: return Keys.DOWN;
	    case KeyCodes.KEY_END: return Keys.END;
	    case KeyCodes.KEY_ENTER: return Keys.ENTER;
	    case KeyCodes.KEY_ESCAPE: return Keys.ESCAPE;
	    case KeyCodes.KEY_HOME: return Keys.HOME;
	    case KeyCodes.KEY_LEFT: return Keys.LEFT;
	    case KeyCodes.KEY_PAGEDOWN: return Keys.PAGE_DOWN;
	    case KeyCodes.KEY_PAGEUP: return Keys.PAGE_UP;
	    case KeyCodes.KEY_RIGHT: return Keys.RIGHT;
	    case KeyCodes.KEY_SHIFT: return Keys.SHIFT_LEFT;
	    case KeyCodes.KEY_TAB: return Keys.TAB;
	    case KeyCodes.KEY_UP: return Keys.UP;

	    case KEY_PAUSE: return Keys.UNKNOWN; // FIXME
	    case KEY_CAPS_LOCK: return Keys.UNKNOWN; // FIXME
	    case KEY_SPACE: return Keys.SPACE;
	    case KEY_INSERT: return Keys.INSERT;
	    case KEY_0: return Keys.NUM_0;
	    case KEY_1: return Keys.NUM_1;
	    case KEY_2: return Keys.NUM_2;
	    case KEY_3: return Keys.NUM_3;
	    case KEY_4: return Keys.NUM_4;
	    case KEY_5: return Keys.NUM_5;
	    case KEY_6: return Keys.NUM_6;
	    case KEY_7: return Keys.NUM_7;
	    case KEY_8: return Keys.NUM_8;
	    case KEY_9: return Keys.NUM_9;
	    case KEY_A: return Keys.A;
	    case KEY_B: return Keys.B;
	    case KEY_C: return Keys.C;
	    case KEY_D: return Keys.D;
	    case KEY_E: return Keys.E;
	    case KEY_F: return Keys.F;
	    case KEY_G: return Keys.G;
	    case KEY_H: return Keys.H;
	    case KEY_I: return Keys.I;
	    case KEY_J: return Keys.J;
	    case KEY_K: return Keys.K;
	    case KEY_L: return Keys.L;
	    case KEY_M: return Keys.M;
	    case KEY_N: return Keys.N;
	    case KEY_O: return Keys.O;
	    case KEY_P: return Keys.P;
	    case KEY_Q: return Keys.Q;
	    case KEY_R: return Keys.R;
	    case KEY_S: return Keys.S;
	    case KEY_T: return Keys.T;
	    case KEY_U: return Keys.U;
	    case KEY_V: return Keys.V;
	    case KEY_W: return Keys.W;
	    case KEY_X: return Keys.X;
	    case KEY_Y: return Keys.Y;
	    case KEY_Z: return Keys.Z;
	    case KEY_LEFT_WINDOW_KEY: return Keys.UNKNOWN; // FIXME
	    case KEY_RIGHT_WINDOW_KEY: return Keys.UNKNOWN; // FIXME
	    // case KEY_SELECT_KEY: return Keys.SELECT_KEY;
	    case KEY_NUMPAD0: return Keys.NUM_0;
	    case KEY_NUMPAD1: return Keys.NUM_1;
	    case KEY_NUMPAD2: return Keys.NUM_2;
	    case KEY_NUMPAD3: return Keys.NUM_3;
	    case KEY_NUMPAD4: return Keys.NUM_4;
	    case KEY_NUMPAD5: return Keys.NUM_5;
	    case KEY_NUMPAD6: return Keys.NUM_6;
	    case KEY_NUMPAD7: return Keys.NUM_7;
	    case KEY_NUMPAD8: return Keys.NUM_8;
	    case KEY_NUMPAD9: return Keys.NUM_9;
	    case KEY_MULTIPLY: return Keys.UNKNOWN; // FIXME
	    case KEY_ADD: return Keys.PLUS;
	    case KEY_SUBTRACT: return Keys.MINUS;
	    case KEY_DECIMAL_POINT_KEY: return Keys.PERIOD;
	    case KEY_DIVIDE: return Keys.UNKNOWN; // FIXME
	    case KEY_F1: return Keys.F1;
	    case KEY_F2: return Keys.F2;
	    case KEY_F3: return Keys.F3;
	    case KEY_F4: return Keys.F4;
	    case KEY_F5: return Keys.F5;
	    case KEY_F6: return Keys.F6;
	    case KEY_F7: return Keys.F7;
	    case KEY_F8: return Keys.F8;
	    case KEY_F9: return Keys.F9;
	    case KEY_F10: return Keys.F10;
	    case KEY_F11: return Keys.F11;
	    case KEY_F12: return Keys.F12;
	    case KEY_NUM_LOCK: return Keys.NUM;
	    case KEY_SCROLL_LOCK: return Keys.UNKNOWN; // FIXME
	    case KEY_SEMICOLON: return Keys.SEMICOLON;
	    case KEY_EQUALS: return Keys.EQUALS;
	    case KEY_COMMA: return Keys.COMMA;
	    case KEY_DASH: return Keys.MINUS;
	    case KEY_PERIOD: return Keys.PERIOD;
	    case KEY_FORWARD_SLASH: return Keys.SLASH;
	    case KEY_GRAVE_ACCENT: return Keys.UNKNOWN; // FIXME
	    case KEY_OPEN_BRACKET: return Keys.LEFT_BRACKET;
	    case KEY_BACKSLASH: return Keys.BACKSLASH;
	    case KEY_CLOSE_BRACKET: return Keys.RIGHT_BRACKET;
	    case KEY_SINGLE_QUOTE: return Keys.APOSTROPHE;
	    default: return Keys.UNKNOWN;
	    }
	  }

	  // these are absent from KeyCodes; we know not why...
	  private static final int KEY_PAUSE = 19;
	  private static final int KEY_CAPS_LOCK = 20;
	  private static final int KEY_SPACE = 32;
	  private static final int KEY_INSERT = 45;
	  private static final int KEY_0 = 48;
	  private static final int KEY_1 = 49;
	  private static final int KEY_2 = 50;
	  private static final int KEY_3 = 51;
	  private static final int KEY_4 = 52;
	  private static final int KEY_5 = 53;
	  private static final int KEY_6 = 54;
	  private static final int KEY_7 = 55;
	  private static final int KEY_8 = 56;
	  private static final int KEY_9 = 57;
	  private static final int KEY_A = 65;
	  private static final int KEY_B = 66;
	  private static final int KEY_C = 67;
	  private static final int KEY_D = 68;
	  private static final int KEY_E = 69;
	  private static final int KEY_F = 70;
	  private static final int KEY_G = 71;
	  private static final int KEY_H = 72;
	  private static final int KEY_I = 73;
	  private static final int KEY_J = 74;
	  private static final int KEY_K = 75;
	  private static final int KEY_L = 76;
	  private static final int KEY_M = 77;
	  private static final int KEY_N = 78;
	  private static final int KEY_O = 79;
	  private static final int KEY_P = 80;
	  private static final int KEY_Q = 81;
	  private static final int KEY_R = 82;
	  private static final int KEY_S = 83;
	  private static final int KEY_T = 84;
	  private static final int KEY_U = 85;
	  private static final int KEY_V = 86;
	  private static final int KEY_W = 87;
	  private static final int KEY_X = 88;
	  private static final int KEY_Y = 89;
	  private static final int KEY_Z = 90;
	  private static final int KEY_LEFT_WINDOW_KEY = 91;
	  private static final int KEY_RIGHT_WINDOW_KEY = 92;
	  private static final int KEY_SELECT_KEY = 93;
	  private static final int KEY_NUMPAD0 = 96;
	  private static final int KEY_NUMPAD1 = 97;
	  private static final int KEY_NUMPAD2 = 98;
	  private static final int KEY_NUMPAD3 = 99;
	  private static final int KEY_NUMPAD4 = 100;
	  private static final int KEY_NUMPAD5 = 101;
	  private static final int KEY_NUMPAD6 = 102;
	  private static final int KEY_NUMPAD7 = 103;
	  private static final int KEY_NUMPAD8 = 104;
	  private static final int KEY_NUMPAD9 = 105;
	  private static final int KEY_MULTIPLY = 106;
	  private static final int KEY_ADD = 107;
	  private static final int KEY_SUBTRACT = 109;
	  private static final int KEY_DECIMAL_POINT_KEY = 110;
	  private static final int KEY_DIVIDE = 111;
	  private static final int KEY_F1 = 112;
	  private static final int KEY_F2 = 113;
	  private static final int KEY_F3 = 114;
	  private static final int KEY_F4 = 115;
	  private static final int KEY_F5 = 116;
	  private static final int KEY_F6 = 117;
	  private static final int KEY_F7 = 118;
	  private static final int KEY_F8 = 119;
	  private static final int KEY_F9 = 120;
	  private static final int KEY_F10 = 121;
	  private static final int KEY_F11 = 122;
	  private static final int KEY_F12 = 123;
	  private static final int KEY_NUM_LOCK = 144;
	  private static final int KEY_SCROLL_LOCK = 145;
	  private static final int KEY_SEMICOLON = 186;
	  private static final int KEY_EQUALS = 187;
	  private static final int KEY_COMMA = 188;
	  private static final int KEY_DASH = 189;
	  private static final int KEY_PERIOD = 190;
	  private static final int KEY_FORWARD_SLASH = 191;
	  private static final int KEY_GRAVE_ACCENT = 192;
	  private static final int KEY_OPEN_BRACKET = 219;
	  private static final int KEY_BACKSLASH = 220;
	  private static final int KEY_CLOSE_BRACKET = 221;
	  private static final int KEY_SINGLE_QUOTE = 222;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9338.java