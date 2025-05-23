final AjaxEditableLabel<String> ajaxLabel = new AjaxEditableLabel<String>("ajaxLabel",

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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

/**
 * @author Gerolf Seitz
 */
public class AjaxEditableLabelPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public AjaxEditableLabelPage()
	{
		final AjaxEditableLabel ajaxLabel = new AjaxEditableLabel("ajaxLabel",
			new Model<String>("ajaxTest"));
		add(ajaxLabel);
		add(new AjaxLink("toggle")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				ajaxLabel.setEnabled(!ajaxLabel.isEnabled());
				target.addComponent(ajaxLabel);
			}
		});
	}

}