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

package org.eclipse.gef.examples.text.edit;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public class DocumentPart extends BlockTextPart implements TextStyleManager {

	public DocumentPart(Container model) {
		super(model);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy("Text Editing", new BlockEditPolicy()); //$NON-NLS-1$
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == TextStyleManager.class)
			return key.cast(this);
		return super.getAdapter(key);
	}

	@Override
	public void getTextLocation(CaretRequest search, SearchResult result) {
		if (search.getType() == CaretRequest.DOCUMENT) {
			search.isInto = true;
			search.isForward = !search.isForward;
			search.isRecursive = true;
			search.setType(CaretRequest.COLUMN);
		}
		super.getTextLocation(search, result);
	}

	@Override
	public Object getStyleState(String styleID, SelectionRange range) {
		return StyleService.STATE_EDITABLE;
	}

	@Override
	public Object getStyleValue(String styleID, SelectionRange range) {
		if (styleID.equals(Style.PROPERTY_BOLD)) {
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				if (!run.getContainer().getStyle().isBold())
					return Boolean.FALSE;
			}
			return Boolean.TRUE;
		} else if (styleID.equals(Style.PROPERTY_FONT_SIZE)) {
			int fontHeight = -1;
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				if (fontHeight == -1)
					fontHeight = run.getContainer().getStyle().getFontHeight();
				else if (fontHeight != run.getContainer().getStyle().getFontHeight())
					return StyleService.UNDEFINED;
			}
			return Integer.valueOf(fontHeight);
		} else if (styleID.equals(Style.PROPERTY_FONT)) {
			String fontName = null;
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				if (fontName == null)
					fontName = run.getContainer().getStyle().getFontFamily();
				else if (!fontName.equals(run.getContainer().getStyle().getFontFamily()))
					return StyleService.UNDEFINED;
			}
			return fontName;
		} else if (styleID.equals(Style.PROPERTY_ITALIC)) {
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				if (!run.getContainer().getStyle().isItalic())
					return Boolean.FALSE;
			}
			return Boolean.TRUE;
		} else if (styleID.equals(Style.PROPERTY_UNDERLINE)) {
			for (Iterator<?> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				if (!run.getContainer().getStyle().isUnderline())
					return Boolean.FALSE;
			}
			return Boolean.TRUE;
		} else if (Style.PROPERTY_ALIGNMENT.equals(styleID)) {
			int alignment = 0;
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				Style style = run.getBlockContainer().getStyle();
				if (alignment == 0)
					alignment = style.getAlignment();
				if (!style.isSet(styleID) || style.getAlignment() != alignment)
					return StyleService.UNDEFINED;
			}
			return Integer.valueOf(alignment);
		} else if (Style.PROPERTY_ORIENTATION.equals(styleID)) {
			int orientation = 0;
			for (Iterator<EditPart> iter = range.getLeafParts().iterator(); iter.hasNext();) {
				TextRun run = (TextRun) ((TextEditPart) iter.next()).getModel();
				Style style = run.getBlockContainer().getStyle();
				if (orientation == 0)
					orientation = style.getOrientation();
				if (!style.isSet(styleID) || style.getOrientation() != orientation)
					return StyleService.UNDEFINED;
			}
			return Integer.valueOf(orientation);
		}
		return StyleService.UNDEFINED;
	}

}