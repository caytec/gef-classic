/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * An action that matches the height of all selected EditPart's Figures to the
 * width of the Primary Selection EditPart's Figure.
 */
public class MatchHeightAction extends MatchSizeAction {

	/**
	 * Constructs a <code>MatchHeightAction</code> and associates it with the given
	 * part.
	 * 
	 * @param part The workbench part associated with this MatchHeightAction
	 */
	public MatchHeightAction(IWorkbenchPart part) {
		super(part);
		setText(GEFMessages.MatchHeightAction_Label);
		setImageDescriptor(InternalImages.DESC_MATCH_HEIGHT);
		setDisabledImageDescriptor(InternalImages.DESC_MATCH_HEIGHT_DIS);
		setToolTipText(GEFMessages.MatchHeightAction_Tooltip);
		setId(GEFActionConstants.MATCH_HEIGHT);
	}

	/**
	 * Returns 0 to make this action affect only the height delta.
	 * 
	 * @param precisePartBounds    the precise bounds of the EditPart's Figure to be
	 *                             matched
	 * @param precisePrimaryBounds the precise bounds of the Primary Selection
	 *                             EditPart's Figure
	 * @return 0.
	 */
	@Override
	protected double getPreciseWidthDelta(PrecisionRectangle precisePartBounds,
			PrecisionRectangle precisePrimaryBounds) {
		return 0;
	}

}
