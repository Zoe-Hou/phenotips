/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.configuration.spi;

import org.phenotips.configuration.RecordElement;
import org.phenotips.configuration.RecordSection;

import org.xwiki.uiextension.UIExtension;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * An implementation for {@link RecordElement} which reads the configuration from an {@code UIExtension} xobject.
 *
 * @version $Id$
 * @since 1.4
 */
public class UIXRecordElement implements RecordElement
{
    /** @see #getExtension() */
    private final UIExtension extension;

    /** @see #getContainingSection() */
    private final RecordSection section;

    /**
     * Simple constructor, taking a UI {@code extension} and the parent record {@code section} as parameters.
     *
     * @param extension the {@link UIExtension UI extension} object defining this record element
     * @param section the parent {@link RecordSection section} containing this element
     * @throws IllegalArgumentException if {@code extension} or {@code section} are null
     */
    public UIXRecordElement(UIExtension extension, RecordSection section)
    {
        if (extension == null || section == null) {
            throw new IllegalArgumentException("DefaultRecordElement constructor parameters must not be null");
        }
        this.extension = extension;
        this.section = section;
    }

    @Override
    public UIExtension getExtension()
    {
        return this.extension;
    }

    @Override
    public String getName()
    {
        String result = this.extension.getParameters().get("title");
        if (StringUtils.isBlank(result)) {
            result = StringUtils.capitalize(StringUtils.replaceChars(
                StringUtils.substringAfterLast(this.extension.getId(), "."), "_-", "  "));
        }
        return result;
    }

    @Override
    public boolean isEnabled()
    {
        return !StringUtils.equals("false", this.extension.getParameters().get("enabled"));
    }

    @Override
    public boolean containsPrivateIdentifiableInformation()
    {
        return StringUtils.equals("true", this.extension.getParameters().get("contains_PII"));
    }

    @Override
    public List<String> getDisplayedFields()
    {
        List<String> result = new LinkedList<String>();
        String usedFields = this.extension.getParameters().get("fields");
        if (usedFields != null) {
            for (String usedField : StringUtils.split(usedFields, ",")) {
                result.add(StringUtils.trim(usedField));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public RecordSection getContainingSection()
    {
        return this.section;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
