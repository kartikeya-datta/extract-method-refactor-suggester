error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4610.java
text:
```scala
r@@eturn MathUtils.clamp(a * a * a * (a * (a * 6 - 15) + 10), 0, 1);

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

package com.badlogic.gdx.math;

/** Takes a linear value in the range of 0-1 and outputs a (usually) non-linear, interpolated value.
 * @author Nathan Sweet */
public abstract class Interpolation {
	abstract public float apply (float a);

	public float apply (float start, float end, float a) {
		return start + (end - start) * apply(a);
	}

	static public final Interpolation linear = new Interpolation() {
		public float apply (float a) {
			return a;
		}
	};

	static public final Interpolation fade = new Interpolation() {
		public float apply (float a) {
			return a * a * a * (a * (a * 6 - 15) + 10);
		}
	};

	static public final Pow pow2 = new Pow(2);
	static public final PowIn pow2In = new PowIn(2);
	static public final PowOut pow2Out = new PowOut(2);

	static public final Pow pow3 = new Pow(3);
	static public final PowIn pow3In = new PowIn(3);
	static public final PowOut pow3Out = new PowOut(3);

	static public final Pow pow4 = new Pow(4);
	static public final PowIn pow4In = new PowIn(4);
	static public final PowOut pow4Out = new PowOut(4);

	static public final Pow pow5 = new Pow(5);
	static public final PowIn pow5In = new PowIn(5);
	static public final PowOut pow5Out = new PowOut(5);

	static public final Interpolation sine = new Interpolation() {
		public float apply (float a) {
			return (1 - MathUtils.cos(a * MathUtils.PI)) / 2;
		}
	};

	static public final Interpolation sineIn = new Interpolation() {
		public float apply (float a) {
			return 1 - MathUtils.cos(a * MathUtils.PI / 2);
		}
	};

	static public final Interpolation sineOut = new Interpolation() {
		public float apply (float a) {
			return MathUtils.sin(a * MathUtils.PI / 2);
		}
	};

	static public final Interpolation exp10 = new Exp(2, 10);
	static public final Interpolation exp10In = new ExpIn(2, 10);
	static public final Interpolation exp10Out = new ExpOut(2, 10);

	static public final Interpolation exp5 = new Exp(2, 5);
	static public final Interpolation exp5In = new ExpIn(2, 5);
	static public final Interpolation exp5Out = new ExpOut(2, 5);

	static public final Interpolation circle = new Interpolation() {
		public float apply (float a) {
			if (a <= 0.5f) {
				a *= 2;
				return (1 - (float)Math.sqrt(1 - a * a)) / 2;
			}
			a--;
			a *= 2;
			return ((float)Math.sqrt(1 - a * a) + 1) / 2;
		}
	};

	static public final Interpolation circleIn = new Interpolation() {
		public float apply (float a) {
			return 1 - (float)Math.sqrt(1 - a * a);
		}
	};

	static public final Interpolation circleOut = new Interpolation() {
		public float apply (float a) {
			a--;
			return (float)Math.sqrt(1 - a * a);
		}
	};

	static public final Elastic elastic = new Elastic(2, 10);
	static public final Elastic elasticIn = new ElasticIn(2, 10);
	static public final Elastic elasticOut = new ElasticOut(2, 10);

	static public final Interpolation swing = new Swing(1.5f);
	static public final Interpolation swingIn = new SwingIn(2f);
	static public final Interpolation swingOut = new SwingOut(2f);

	static public final Interpolation bounce = new Bounce(4);
	static public final Interpolation bounceIn = new BounceIn(4);
	static public final Interpolation bounceOut = new BounceOut(4);

	//

	static public class Pow extends Interpolation {
		final int power;

		public Pow (int power) {
			this.power = power;
		}

		public float apply (float a) {
			if (a <= 0.5f) return (float)Math.pow(a * 2, power) / 2;
			return (float)Math.pow((a - 1) * 2, power) / (power % 2 == 0 ? -2 : 2) + 1;
		}
	}

	static public class PowIn extends Pow {
		public PowIn (int power) {
			super(power);
		}

		public float apply (float a) {
			return (float)Math.pow(a, power);
		}
	}

	static public class PowOut extends Pow {
		public PowOut (int power) {
			super(power);
		}

		public float apply (float a) {
			return (float)Math.pow(a - 1, power) * (power % 2 == 0 ? -1 : 1) + 1;
		}
	}

	//

	static public class Exp extends Interpolation {
		final float value, power, min, scale;

		public Exp (float value, float power) {
			this.value = value;
			this.power = power;
			min = (float)Math.pow(value, -power);
			scale = 1 / (1 - min);
		}

		public float apply (float a) {
			if (a <= 0.5f) return ((float)Math.pow(value, power * (a * 2 - 1)) - min) * scale / 2;
			return (2 - ((float)Math.pow(value, -power * (a * 2 - 1)) - min) * scale) / 2;
		}
	};

	static public class ExpIn extends Exp {
		public ExpIn (float value, float power) {
			super(value, power);
		}

		public float apply (float a) {
			return ((float)Math.pow(value, power * (a - 1)) - min) * scale;
		}
	}

	static public class ExpOut extends Exp {
		public ExpOut (float value, float power) {
			super(value, power);
		}

		public float apply (float a) {
			return 1 - ((float)Math.pow(value, -power * a) - min) * scale;
		}
	}

	//

	static public class Elastic extends Interpolation {
		final float value, power;

		public Elastic (float value, float power) {
			this.value = value;
			this.power = power;
		}

		public float apply (float a) {
			if (a <= 0.5f) {
				a *= 2;
				return (float)Math.pow(value, power * (a - 1)) * MathUtils.sin(a * 20) * 1.0955f / 2;
			}
			a = 1 - a;
			a *= 2;
			return 1 - (float)Math.pow(value, power * (a - 1)) * MathUtils.sin((a) * 20) * 1.0955f / 2;
		}
	}

	static public class ElasticIn extends Elastic {
		public ElasticIn (float value, float power) {
			super(value, power);
		}

		public float apply (float a) {
			return (float)Math.pow(value, power * (a - 1)) * MathUtils.sin(a * 20) * 1.0955f;
		}
	}

	static public class ElasticOut extends Elastic {
		public ElasticOut (float value, float power) {
			super(value, power);
		}

		public float apply (float a) {
			a = 1 - a;
			return (1 - (float)Math.pow(value, power * (a - 1)) * MathUtils.sin(a * 20) * 1.0955f);
		}
	}

	//

	static public class Bounce extends BounceOut {
		public Bounce (float[] widths, float[] heights) {
			super(widths, heights);
		}

		public Bounce (int bounces) {
			super(bounces);
		}

		private float out (float a) {
			float test = a + widths[0] / 2;
			if (test < widths[0]) return test / (widths[0] / 2) - 1;
			return super.apply(a);
		}

		public float apply (float a) {
			if (a <= 0.5f) return (1 - out(1 - a * 2)) / 2;
			return out(a * 2 - 1) / 2 + 0.5f;
		}
	}

	static public class BounceOut extends Interpolation {
		final float[] widths, heights;

		public BounceOut (float[] widths, float[] heights) {
			if (widths.length != heights.length)
				throw new IllegalArgumentException("Must be the same number of widths and heights.");
			this.widths = widths;
			this.heights = heights;
		}

		public BounceOut (int bounces) {
			if (bounces < 2 || bounces > 5) throw new IllegalArgumentException("bounces cannot be < 2 or > 5: " + bounces);
			widths = new float[bounces];
			heights = new float[bounces];
			heights[0] = 1;
			switch (bounces) {
			case 2:
				widths[0] = 0.6f;
				widths[1] = 0.4f;
				heights[1] = 0.33f;
				break;
			case 3:
				widths[0] = 0.4f;
				widths[1] = 0.4f;
				widths[2] = 0.2f;
				heights[1] = 0.33f;
				heights[2] = 0.1f;
				break;
			case 4:
				widths[0] = 0.34f;
				widths[1] = 0.34f;
				widths[2] = 0.2f;
				widths[3] = 0.15f;
				heights[1] = 0.26f;
				heights[2] = 0.11f;
				heights[3] = 0.03f;
				break;
			case 5:
				widths[0] = 0.3f;
				widths[1] = 0.3f;
				widths[2] = 0.2f;
				widths[3] = 0.1f;
				widths[4] = 0.1f;
				heights[1] = 0.45f;
				heights[2] = 0.3f;
				heights[3] = 0.15f;
				heights[4] = 0.06f;
				break;
			}
			widths[0] *= 2;
		}

		public float apply (float a) {
			a += widths[0] / 2;
			float width = 0, height = 0;
			for (int i = 0, n = widths.length; i < n; i++) {
				width = widths[i];
				if (a <= width) {
					height = heights[i];
					break;
				}
				a -= width;
			}
			a /= width;
			float z = 4 / width * height * a;
			return 1 - (z - z * a) * width;
		}
	}

	static public class BounceIn extends BounceOut {
		public BounceIn (float[] widths, float[] heights) {
			super(widths, heights);
		}

		public BounceIn (int bounces) {
			super(bounces);
		}

		public float apply (float a) {
			return 1 - super.apply(1 - a);
		}
	}

	//

	static public class Swing extends Interpolation {
		private final float scale;

		public Swing (float scale) {
			this.scale = scale * 2;
		}

		public float apply (float a) {
			if (a <= 0.5f) {
				a *= 2;
				return a * a * ((scale + 1) * a - scale) / 2;
			}
			a--;
			a *= 2;
			return a * a * ((scale + 1) * a + scale) / 2 + 1;
		}
	}

	static public class SwingOut extends Interpolation {
		private final float scale;

		public SwingOut (float scale) {
			this.scale = scale;
		}

		public float apply (float a) {
			a--;
			return a * a * ((scale + 1) * a + scale) + 1;
		}
	}

	static public class SwingIn extends Interpolation {
		private final float scale;

		public SwingIn (float scale) {
			this.scale = scale;
		}

		public float apply (float a) {
			return a * a * ((scale + 1) * a - scale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4610.java