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

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.phenotips.data.Cancer;
import org.phenotips.data.CancerQualifier;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.json.JSONObject;

import com.xpn.xwiki.objects.BaseObject;

/**
 * Implementation of patient data based on the XWiki data model, where cancer data is represented by properties in
 * objects of type {@code PhenoTips.CancerClass}.
 *
 * @version $Id$
 * @since 1.4
 */
public class PhenoTipsCancer extends AbstractPhenoTipsVocabularyProperty implements Cancer
{
    protected static final String CANCER_PROPERTY = "cancer";

    protected static final String AFFECTED_PROPERTY = "affected";

    protected static final String JSON_QUALIFIERS_PROPERTY = "qualifiers";

    protected static final String JSON_CANCER_PROPERTY = "id";

    private Set<CancerQualifier> qualifiers;

    private boolean affected;

    /**
     * Constructor that copies the data from a {@code cancerObject}.
     *
     * @param cancerObject the cancer {@link BaseObject}
     */
    public PhenoTipsCancer(@Nonnull final XWikiDocument doc, @Nonnull final BaseObject cancerObject)
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
        return this.affected;
    }

    public void setAffected(final boolean affected)
    {
        this.affected = affected;
    }

    @Override
    @Nonnull
    public Collection<CancerQualifier> getQualifiers()
    {
        return Collections.unmodifiableSet(this.qualifiers);
    }

    public void setQualifiers(@Nullable final Collection<CancerQualifier> qualifiers)
    {
        this.qualifiers = CollectionUtils.isNotEmpty(qualifiers) ? new HashSet<>(qualifiers) : new HashSet<>();
    }

    private boolean addQualifiers(@Nullable final Collection<CancerQualifier> qualifiers)
    {
        return CollectionUtils.isNotEmpty(qualifiers) && this.qualifiers.addAll(qualifiers);
    }

    @Override
    @Nonnull
    public String getId()
    {
        return this.id;
    }

    @Override
    @Nullable
    public String getName()
    {
        return this.name;
    }

    public void setName(@Nullable final String name)
    {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
    }

    @Override
    @Nonnull
    public JSONObject toJSON()
    {
        return null;
    }

    @Nonnull
    @Override
    public Cancer mergeData(@Nonnull final Cancer cancer)
    {
        if (!Objects.equals(getId(), cancer.getId())) {
            throw new IllegalArgumentException("Cannot merge cancer objects with different identifiers");
        }
        setAffected(cancer.isAffected());
        setName(cancer.getName());
        addQualifiers(cancer.getQualifiers());
        return this;
    }

    @Override
    public void write(@Nonnull final BaseObject baseObject, @Nonnull final XWikiContext context)
    {
        baseObject.set(CANCER_PROPERTY, getId(), context);
        baseObject.set(AFFECTED_PROPERTY, isAffected(), context);
    }
}
