error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5173.java
text:
```scala
public v@@oid clamp( )

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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
package com.badlogic.gdx.graphics;


/**
 * A color class, holding the r, g, b and alpha component
 * as floats in the range [0,1]. All methods perform clamping
 * on the internal values after execution. 
 * 
 * @author mzechner
 *
 */
public class Color 
{
	public static final Color WHITE = new Color( 1, 1, 1, 1 );
	public static final Color BLACK = new Color( 0, 0, 0, 1 );
	public static final Color RED = new Color( 1, 0, 0, 1 );
	public static final Color GREEN = new Color( 0, 1, 0, 1 );
	public static final Color BLUE = new Color( 0, 0, 1, 1 );
	
	/** the red, green, blue and alpha components **/
	public float r, g, b, a;
	
	/**
	 * Constructor, sets the components of the color
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component
	 * @param a the alpha component
	 */
	public Color( float r, float g, float b, float a )
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		clamp();
	}

	/**
	 * Constructs a new color using the given color
	 * @param color the color
	 */
	public Color( Color color )
	{
		set( color );
	}
	
	/**
	 * Sets this color to the given color.
	 * @param color the Color
	 */
	public Color set( Color color )
	{
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
		clamp();
		return this;
	}
	
	/**
	 * Multiplies the this color and the given color
	 * @param color the color
	 * @return this color.
	 */
	public Color mul( Color color )
	{
		this.r *= color.r;
		this.g *= color.g;
		this.b *= color.b;
		this.a *= color.a;
		clamp();
		return this;
	}

	/**
	 * Multiplies all components of this Color with the given value. 
	 * 
	 * @param value the value
	 * @return this color
	 */
	public Color mul(float value) 
	{	
		this.r *= value;
		this.g *= value;
		this.b *= value;
		this.a *= value;
		clamp();
		return this;
	}
	
	/**
	 * Adds the given color to this color.
	 * @param color the color
	 * @return this color
	 */
	public Color add( Color color )
	{
		this.r += color.r;
		this.g += color.g;
		this.b += color.b;
		this.a += color.a;		
		clamp();
		return this;
	}

	/**
	 * Subtracts the given color from this color
	 * @param color the color
	 * @return this color
	 */
	public Color sub(Color color) 
	{	
		this.r -= color.r;
		this.g -= color.g;
		this.b -= color.b;
		this.a -= color.a;
		clamp();
		return this;
	}
	
	private void clamp( )
	{
		if( r < 0 ) r = 0; 
		if( r > 1 ) r = 1;
		
		if( g < 0 ) g = 0; 
		if( g > 1 ) g = 1;
		
		if( b < 0 ) b = 0; 
		if( b > 1 ) b = 1;
		
		if( a < 0 ) a = 0; 
		if( a > 1 ) a = 1;
	}

	public void set(float r, float g, float b, float a) 
	{	
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (Float.compare(color.a, a) != 0) return false;
        if (Float.compare(color.b, b) != 0) return false;
        if (Float.compare(color.g, g) != 0) return false;
        if (Float.compare(color.r, r) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
        result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
        result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
        result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
        return result;
    }
    
    /**
     * Packs the four color components which should be in the range 0-255 into
     * a 32-bit integer and then converts it to a float. Note that no range
     * checking is performed for higher performance. 
     * @param r the red component, 0 - 255
     * @param g the green component, 0 - 255
     * @param b the blue component, 0 - 255
     * @param a the alpha component, 0 - 255
     * @return the packed color as a float
     */
    public static float toFloatBits( int r, int g, int b, int a )
    {
    	int color = ( a << 24 ) | ( b << 16 ) | ( g << 8 ) | r;
    	float floatColor = Float.intBitsToFloat( color );
    	return floatColor;
    }

    /**
     * Packs the four color components which should be in the range 0-255 into
     * a 32-bit. Note that no range checking is performed for higher performance. 
     * @param r the red component, 0 - 255
     * @param g the green component, 0 - 255
     * @param b the blue component, 0 - 255
     * @param a the alpha component, 0 - 255
     * @return the packed color as a 32-bit int
     */
    public static float toIntBits( int r, int g, int b, int a )
    {
    	return ( a << 24 ) | ( b << 16 ) | ( g << 8 ) | r;

    }
    
    /**
     * Packs the 4 components of this color into a 32-bit int and returns it as a 
     * float. 
     * 
     * @return the packed color as a 32-bit float
     */
    public float toFloatBits( )
    {
    	int color = ( (int)( 255 * a ) << 24 ) |
    		   		( (int)( 255 * b ) << 16 ) |
    		   		( (int)( 255 * g ) << 8 ) |
    		   		( (int)( 255 * r ) );
        return Float.intBitsToFloat( color );
    }
    
    /**
     * Packs the 4 components of this color into a 32-bit int.
     * @return the packed color as a 32-bit int.
     */
    public int toIntBits( )
    {
    	int color = ( (int)( 255 * a ) << 24 ) |
   					( (int)( 255 * b ) << 16 ) |
   					( (int)( 255 * g ) << 8 ) |
   					( (int)( 255 * r ) );
    	return color;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5173.java