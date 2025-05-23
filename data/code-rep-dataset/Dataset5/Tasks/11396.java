public class MarkupInheritanceBase_12 extends WebPage

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;


/**
 */
public class MarkupInheritanceBase_12 extends WebPage<Void>
{
	private static final long serialVersionUID = 1L;

	private int counter = 0;

	/**
	 * Construct.
	 * 
	 */
	public MarkupInheritanceBase_12()
	{
		add(new Label("label1", new PropertyModel(this, "counter")));
		add(new Link("link")
		{
			private static final long serialVersionUID = 1L;

			public void onClick()
			{
				counter++;
			}
		});
	}

	/**
	 * Gets the counter.
	 * 
	 * @return counter
	 */
	public int getCounter()
	{
		return counter;
	}

	/**
	 * Sets the counter.
	 * 
	 * @param counter
	 *            counter
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
}