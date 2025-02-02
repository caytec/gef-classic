/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public class InlineContainer extends Container {

	private static final long serialVersionUID = 1;

	/**
	 * @param type
	 * @since 3.1
	 */
	public InlineContainer(int type) {
		super(type);
	}

	/**
	 * @see org.eclipse.gef.examples.text.model.Container#newContainer()
	 */
	@Override
	Container newContainer() {
		return new InlineContainer(getType());
	}

}
