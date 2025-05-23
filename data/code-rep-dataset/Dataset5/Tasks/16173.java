this(id, null);

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
package org.apache.wicket.extensions.ajax.markup.html;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

/**
 * A variant of the {@link AjaxButton} that displays a busy indicator while the ajax request is in
 * progress.
 * 
 * @param <T>
 * @author evan
 * 
 */
public abstract class IndicatingAjaxButton<T> extends AjaxButton<T> implements IAjaxIndicatorAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final WicketAjaxIndicatorAppender indicatorAppender = new WicketAjaxIndicatorAppender();

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public IndicatingAjaxButton(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param form
	 */
	public IndicatingAjaxButton(String id, Form<?> form)
	{
		super(id, form);
		add(indicatorAppender);
	}

	/**
	 * @see IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
	 * @return the markup id of the ajax indicator
	 * 
	 */
	public String getAjaxIndicatorMarkupId()
	{
		return indicatorAppender.getMarkupId();
	}

}
 No newline at end of file