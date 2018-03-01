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
package org.phenotips.data.internal;

import org.phenotips.Constants;
import org.phenotips.data.Cancer;
import org.phenotips.data.CancerMetadatum;

import org.xwiki.model.EntityType;
import org.xwiki.model.reference.EntityReference;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.json.JSONObject;

import com.xpn.xwiki.objects.BaseObject;

import javafx.beans.property.ListProperty;

/**
 * Implementation of patient data based on the XWiki data model, where cancer data is represented by properties in
 * objects of type {@code PhenoTips.CancerClass}.
 *
 * @version $Id$
 * @since 1.4
 */
public class PhenoTipsCancer extends AbstractPhenoTipsVocabularyProperty implements Cancer
{
    /** The XClass used for storing cancer data. */
    protected static final EntityReference CANCER_CLASS_REFERENCE = new EntityReference("CancerClass",
            EntityType.DOCUMENT, Constants.CODE_SPACE_REFERENCE);

    /** The XClass used for storing cancer qualifiers data. */
    protected static final EntityReference CANCER_QUALIFIER_CLASS_REFERENCE =
            new EntityReference("CancerQualifierClass", EntityType.DOCUMENT, Constants.CODE_SPACE_REFERENCE);

    protected static final String INTERNAL_CANCER_PROPERTY = "cancer";

    protected static final String INTERNAL_AFFECTED_PROPERTY = "affected";

    protected static final String INTERNAL_QUALIFIERS_PROPERTY = "qualifiers";

    protected static final String JSON_CANCER_PROPERTY = "id";

    protected static final String JSON_AFFECTED_PROPERTY = INTERNAL_AFFECTED_PROPERTY;

    protected static final String JSON_QUALIFIERS_PROPERTY = INTERNAL_QUALIFIERS_PROPERTY;

    /**
     * Constructor that copies the data from a {@code cancerObject}.
     *
     * @param cancerObject the cancer {@link BaseObject}
     */
    public PhenoTipsCancer(@Nonnull final BaseObject cancerObject)
    {
        super("ab");
    }

    public PhenoTipsCancer(@Nonnull final JSONObject json)
    {
        super(json);
    }

    @Override
    public boolean isAffected()
    {
        return false;
    }

    @Override
    public Collection<CancerMetadatum> getQualifiers()
    {
        return null;
    }

    @Override
    public String getId()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public JSONObject toJSON()
    {
        return null;
    }
}
