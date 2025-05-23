error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2033.java
text:
```scala
t@@ime = (diff < 0f) ? 0f : duration;

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

package com.badlogic.gdx.graphics.g3d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/** Class to control one or more {@link Animation}s on a {@link ModelInstance}.
 * Use the {@link #setAnimation(String, int, float, AnimationListener)} method to change the current animation.
 * Use the {@link #animate(String, int, float, AnimationListener, float)} method to start an animation, 
 * optionally blending onto the current animation.
 * Use the {@link #queue(String, int, float, AnimationListener, float)} method to queue an animation to be played
 * when the current animation is finished.
 * Use the {@link #action(String, int, float, AnimationListener, float)} method to play a (short) animation on top
 * of the current animation.
 * 
 * You can use multiple AnimationControllers on the same ModelInstance, as long as they don't interfere with each
 * other (don't affect the same {@link Node}s).
 * 
 * @author Xoppa */
public class AnimationController extends BaseAnimationController {
	
	/** Listener that will be informed when an animation is looped or completed.
	 * @author Xoppa */
	public interface AnimationListener {
		/** Gets called when an animation is completed.
		 * @param animation The animation which just completed. */
		void onEnd(final AnimationDesc animation);
		/** Gets called when an animation is looped. The {@link AnimationDesc#loopCount} is updated prior to this call
		 * and can be read or written to alter the number of remaining loops. 
		 * @param animation The animation which just looped. */
		void onLoop(final AnimationDesc animation);
	}
	
	/** Class describing how to play and {@link Animation}. You can read the values within this class to get the
	 * progress of the animation. Do not change the values. Only valid when the animation is currently played.
	 * @author Xoppa */
	public static class AnimationDesc {
		/** Listener which will be informed when the animation is looped or ended. */
		public AnimationListener listener;
		/** The animation to be applied. */
		public Animation animation;
		/** The speed at which to play the animation (can be negative), 1.0 for normal speed. */
		public float speed;
		/** The current animation time. */
		public float time;
		/** The offset within the animation (animation time = offsetTime + time) */
		public float offset;
		/** The duration of the animation */
		public float duration;
		/** The number of remaining loops, negative for continuous, zero if stopped. */
		public int loopCount;
		protected AnimationDesc() { }
		/** @return the remaining time or 0 if still animating. */
		protected float update(float delta) {
			if (loopCount != 0 && animation != null) {
				final float diff = speed * delta; 
				time += diff;
				int loops = (int)Math.abs(time / duration);
				if (time < 0f) {
					loops++;
					while (time < 0f)
						time += duration;
				}
				time = Math.abs(time % duration);
				for (int i = 0; i < loops; i++) {
					if (loopCount > 0)
						loopCount--;
					if (loopCount != 0 && listener != null)
						listener.onLoop(this);
					if (loopCount == 0) {
						final float result = ((loops - 1) - i) * duration + (diff < 0f ? duration - time : time); 
						time = (diff < 0f) ? duration : 0f;
						if (listener != null)
							listener.onEnd(this);
						return result;
					}
				}
				return 0f;
			} else
				return delta;
		}
	}
	protected final Pool<AnimationDesc> animationPool = new Pool<AnimationDesc>() {
		@Override
		protected AnimationDesc newObject() {
			return new AnimationDesc();
		}
	};
	
	/** The animation currently playing. Do not alter this value. */
	public AnimationDesc current;
	/** The animation queued to be played when the {@link #current} animation is completed. Do not alter this value. */
	public AnimationDesc queued;
	/** The transition time which should be applied to the queued animation. Do not alter this value. */
	public float queuedTransitionTime;
	/** The animation which previously played. Do not alter this value. */
	public AnimationDesc previous;
	/** The current transition time. Do not alter this value. */
	public float transitionCurrentTime;
	/** The target transition time. Do not alter this value. */
	public float transitionTargetTime;
	/** Whether an action is being performed. Do not alter this value. */
	public boolean inAction;
	
	private boolean justChangedAnimation = false;

	/** Construct a new AnimationController.
	 * @param target The {@link ModelInstance} on which the animations will be performed. */
	public AnimationController (final ModelInstance target) {
		super(target);
	}
	
	private AnimationDesc obtain(final Animation anim, float offset, float duration, int loopCount, float speed, final AnimationListener listener) {
		if (anim == null)
			return null;
		final AnimationDesc result = animationPool.obtain();
		result.animation = anim;
		result.listener = listener;
		result.loopCount = loopCount;
		result.speed = speed;
		result.offset = offset;
		result.duration = duration < 0 ? (anim.duration - offset) : duration;
		result.time = speed < 0 ? result.duration : 0.f;
		return result;
	}
	
	private AnimationDesc obtain(final String id, float offset, float duration, int loopCount, float speed, final AnimationListener listener) {
		if (id == null)
			return null;
		final Animation anim = target.getAnimation(id);
		if (anim == null)
			throw new GdxRuntimeException("Unknown animation: "+id);
		return obtain(anim, offset, duration, loopCount, speed, listener);
	}
	
	private AnimationDesc obtain(final AnimationDesc anim) {
		return obtain(anim.animation, anim.offset, anim.duration, anim.loopCount, anim.speed, anim.listener);
	}
	
	private boolean updating; //FIXME
	/** Update any animations currently being played.
	 * @param delta The time elapsed since last update, change this to alter the overall speed (can be negative). */
	public void update(float delta) {
		if (previous != null && ((transitionCurrentTime += delta) >= transitionTargetTime)) {
			removeAnimation(previous.animation);
			justChangedAnimation = true;
			animationPool.free(previous);
			previous = null;
		}
		if (justChangedAnimation) {
			target.calculateTransforms();
			justChangedAnimation = false;
		}
		if (current == null || current.loopCount == 0 || current.animation == null)
			return;
		justChangedAnimation = false;
		updating = true;
		final float remain = current.update(delta);
		if (remain != 0f && queued != null) {
			inAction = false;
			animate(queued, queuedTransitionTime);
			queued = null;
			updating = false;
			update(remain);
			return;
		}
		if (previous != null)
			applyAnimations(previous.animation, previous.offset + previous.time, current.animation, current.offset + current.time, transitionCurrentTime / transitionTargetTime);
		else
			applyAnimation(current.animation, current.offset + current.time);
		updating = false;
	}
	
	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id) {
		return setAnimation(id, 1, 1.0f, null);
	}
	
	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id, int loopCount) {
		return setAnimation(id, loopCount, 1.0f, null);
	}

	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id, final AnimationListener listener) {
		return setAnimation(id, 1, 1.0f, listener);
	}
	
	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id, int loopCount, final AnimationListener listener) {
		return setAnimation(id, loopCount, 1.0f, listener);
	}
	
	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id, int loopCount, float speed, final AnimationListener listener) {
		return setAnimation(id, 0f, -1f, loopCount, speed, listener);
	}
	
	/** Set the active animation, replacing any current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param offset The offset in seconds to the start of the animation.
	 * @param duration The duration in seconds of the animation (or negative to play till the end of the animation).
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc setAnimation(final String id, float offset, float duration, int loopCount, float speed, final AnimationListener listener) {
		return setAnimation(obtain(id, offset, duration, loopCount, speed, listener));
	}
	
	/** Set the active animation, replacing any current animation. */
	protected AnimationDesc setAnimation(final Animation anim, float offset, float duration, int loopCount, float speed, final AnimationListener listener) {
		return setAnimation(obtain(anim, offset, duration, loopCount, speed, listener));
	}
	
	/** Set the active animation, replacing any current animation. */
	protected AnimationDesc setAnimation(final AnimationDesc anim) {
		if (updating) // FIXME Remove this? Just intended for debugging
			throw new GdxRuntimeException("Cannot change animation during update");
		if (current == null)
			current = anim;
		else {
			if (anim != null && current.animation == anim.animation)
				anim.time = current.time;
			else
				removeAnimation(current.animation);
			animationPool.free(current);
			current = anim;
		}
		justChangedAnimation = true;
		return anim;
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc animate(final String id, float transitionTime) {
		return animate(id, 1, 1.0f, null, transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc animate(final String id, final AnimationListener listener, float transitionTime) {
		return animate(id, 1, 1.0f, listener, transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc animate(final String id, int loopCount, final AnimationListener listener, float transitionTime) {
		return animate(id, loopCount, 1.0f, listener, transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc animate(final String id, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return animate(id, 0f, -1f, loopCount, speed, listener, transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param offset The offset in seconds to the start of the animation.
	 * @param duration The duration in seconds of the animation (or negative to play till the end of the animation).
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc animate(final String id, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return animate(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time. */
	protected AnimationDesc animate(final Animation anim, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return animate(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
	}
	
	/** Changes the current animation by blending the new on top of the old during the transition time. */ 
	protected AnimationDesc animate(final AnimationDesc anim, float transitionTime) {
		if (current == null)
			current = anim;
		else if (inAction)
			queue(anim, transitionTime);
		else if (anim != null && current.animation == anim.animation) {
			anim.time = current.time;
			animationPool.free(current);
			current = anim;
		} else {
			if (previous != null)
				animationPool.free(previous);
			previous = current;
			current = anim;
			transitionCurrentTime = 0f;
			transitionTargetTime = transitionTime;
		}
		return anim;
	}
	
	/** Queue an animation to be applied when the {@link #current} animation is finished. 
	 * If the current animation is continuously looping it will be synchronized on next loop.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc queue(final String id, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return queue(id, 0f, -1f, loopCount, speed, listener, transitionTime);	
	}
	
	/** Queue an animation to be applied when the {@link #current} animation is finished. 
	 * If the current animation is continuously looping it will be synchronized on next loop.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param offset The offset in seconds to the start of the animation.
	 * @param duration The duration in seconds of the animation (or negative to play till the end of the animation).
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc queue(final String id, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return queue(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);	
	}
	
	/** Queue an animation to be applied when the current is finished. If current is continuous it will be synced on next loop. */
	protected AnimationDesc queue(final Animation anim, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return queue(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
	}
	
	/** Queue an animation to be applied when the current is finished. If current is continuous it will be synced on next loop. */
	protected AnimationDesc queue(final AnimationDesc anim, float transitionTime) {
		if (current == null || current.loopCount == 0)
			animate(anim, transitionTime);
		else {
			if (queued != null)
				animationPool.free(queued);
			queued = anim;
			queuedTransitionTime = transitionTime;
			if (current.loopCount < 0)
				current.loopCount = 1;
		}
		return anim;
	}
	
	/** Apply an action animation on top of the current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc action(final String id, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return action(id, 0, -1f, loopCount, speed, listener, transitionTime);	
	}
	
	/** Apply an action animation on top of the current animation.
	 * @param id The ID of the {@link Animation} within the {@link ModelInstance}.
	 * @param offset The offset in seconds to the start of the animation.
	 * @param duration The duration in seconds of the animation (or negative to play till the end of the animation). 
	 * @param loopCount The number of times to loop the animation, zero to play the animation only once, 
	 * negative to continuously loop the animation. 
	 * @param speed The speed at which the animation should be played. Default is 1.0f. A value of 2.0f will play
	 * the animation at twice the normal speed, a value of 0.5f will play the animation at half the normal speed, etc.
	 * This value can be negative, causing the animation to played in reverse.
	 * This value cannot be zero.
	 * @param listener The {@link AnimationListener} which will be informed when the animation is looped or completed.
	 * @param transitionTime The time to transition the new animation on top of the currently playing animation (if any).
	 * @return The {@link AnimationDesc} which can be read to get the progress of the animation. Will be invalid when the
	 * animation is completed. */
	public AnimationDesc action(final String id, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return action(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);	
	}
	
	/** Apply an action animation on top of the current animation. */
	protected AnimationDesc action(final Animation anim, float offset, float duration, int loopCount, float speed, final AnimationListener listener, float transitionTime) {
		return action(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
	}
	
	/** Apply an action animation on top of the current animation. */
	protected AnimationDesc action(final AnimationDesc anim, float transitionTime) {
		if (anim.loopCount < 0)
			throw new GdxRuntimeException("An action cannot be continuous");
		if (current == null || current.loopCount == 0)
			animate(anim, transitionTime);
		else {
			AnimationDesc toQueue = inAction ? null : obtain(current);
			inAction = false;
			animate(anim, transitionTime);
			inAction = true;
			if (toQueue != null)
				queue(toQueue, transitionTime);
		}
		return anim;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2033.java