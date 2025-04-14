error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8391.java
text:
```scala
private static final i@@nt SHADOW_WIDTH = 1;

/******************************************************************************
 *  Copyright (c) 2012 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package com.github.mobile.ui.issue;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static android.graphics.Typeface.DEFAULT_BOLD;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Locale.US;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.style.DynamicDrawableSpan;
import android.util.TypedValue;
import android.widget.TextView;

import com.github.mobile.R.color;
import com.github.mobile.ui.StyledText;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.egit.github.core.Label;

/**
 * Span that draws a {@link Label}
 */
public class LabelDrawableSpan extends DynamicDrawableSpan {

    private static final int PADDING_LEFT = 10;

    private static final int PADDING_RIGHT = 10;

    private static final int PADDING_TOP = 8;

    private static final int PADDING_BOTTOM = 8;

    private static final int SHADOW_WIDTH = 2;

    private static final int CORNERS = 2;

    private static final int BORDER = 1;

    private static float getPixels(final Resources resources, final int dp) {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    private static class LabelDrawable extends PaintDrawable {

        private final String name;

        private final int bg;

        private final float height;

        private final float width;

        private final int borderColor;

        private final float paddingLeft;

        private final float paddingRight;

        private final float paddingTop;

        private final float paddingBottom;

        private final float shadowWidth;

        private final float border;

        private final float corners;

        private final float textHeight;

        /**
         * Create drawable for labels
         *
         * @param resources
         * @param textSize
         * @param label
         */
        public LabelDrawable(final Resources resources, final float textSize, Label label) {
            borderColor = resources.getColor(color.label_border);
            paddingTop = getPixels(resources, PADDING_TOP);
            paddingLeft = getPixels(resources, PADDING_LEFT);
            paddingRight = getPixels(resources, PADDING_RIGHT);
            paddingBottom = getPixels(resources, PADDING_BOTTOM);
            shadowWidth = getPixels(resources, SHADOW_WIDTH);
            corners = getPixels(resources, CORNERS);
            border = getPixels(resources, BORDER);

            name = label.getName().toUpperCase(US);
            bg = Color.parseColor('#' + label.getColor());

            Paint p = getPaint();
            p.setAntiAlias(true);
            p.setColor(resources.getColor(android.R.color.transparent));
            p.setTypeface(DEFAULT_BOLD);
            p.setTextSize(textSize);

            final Rect bounds = new Rect();
            final Rect textBounds = new Rect();
            p.getTextBounds(name, 0, name.length(), textBounds);
            bounds.right = Math.round(textBounds.width() + paddingLeft + paddingRight + 0.5F);
            width = bounds.width();
            textHeight = textBounds.height();
            bounds.bottom = Math.round(textHeight + paddingTop + paddingBottom + 0.5F);
            height = bounds.height();
            bounds.right += border;
            bounds.bottom += border;

            p.setTypeface(DEFAULT_BOLD);
            setBounds(bounds);
        }

        @Override
        public void draw(final Canvas canvas) {
            super.draw(canvas);

            final Paint paint = getPaint();
            final int original = paint.getColor();

            final RectF rect = new RectF();
            rect.right = width;
            rect.bottom = height;

            paint.setStyle(FILL);
            paint.setColor(bg);
            canvas.drawRoundRect(rect, corners + 1, corners + 1, paint);

            paint.setStyle(STROKE);
            paint.setColor(borderColor);
            rect.top += border / 2;
            rect.left += border / 2;
            paint.setStrokeWidth(border);
            canvas.drawRoundRect(rect, corners, corners, paint);

            paint.setStyle(FILL);
            paint.setColor(WHITE);
            paint.setShadowLayer(shadowWidth, 0, 0, BLACK);
            canvas.drawText(name, paddingLeft, rect.bottom - ((height - textHeight) / 2), paint);
            paint.clearShadowLayer();

            paint.setColor(original);
        }
    }

    /**
     * Set text on view to be given labels
     *
     * @param view
     * @param labels
     */
    public static void setText(final TextView view, final Collection<Label> labels) {
        final Label[] sortedLabels = labels.toArray(new Label[labels.size()]);
        Arrays.sort(sortedLabels, new Comparator<Label>() {

            @Override
            public int compare(final Label lhs, final Label rhs) {
                return CASE_INSENSITIVE_ORDER.compare(lhs.getName(), rhs.getName());
            }
        });

        final StyledText text = new StyledText();
        for (int i = 0; i < sortedLabels.length; i++) {
            text.append('\uFFFC', new LabelDrawableSpan(view.getResources(), view.getTextSize(), sortedLabels[i]));
            if (i + 1 < sortedLabels.length)
                text.append(' ');
        }
        view.setText(text);
    }

    /**
     * Set text on view to be given label
     *
     * @param view
     * @param label
     */
    public static void setText(final TextView view, Label label) {
        StyledText text = new StyledText();
        text.append('\uFFFC', new LabelDrawableSpan(view.getResources(), view.getTextSize(), label));
        view.setText(text);
    }

    private final Resources resources;

    private final float textSize;

    private final Label label;

    /**
     * Create background span for label
     *
     * @param resources
     * @param textSize
     *
     * @param label
     */
    public LabelDrawableSpan(final Resources resources, final float textSize, final Label label) {
        this.resources = resources;
        this.textSize = textSize;
        this.label = label;
    }

    @Override
    public Drawable getDrawable() {
        return new LabelDrawable(resources, textSize, label);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8391.java