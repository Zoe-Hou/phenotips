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
package org.phenotips.data.internal.controller;

import org.phenotips.Constants;
import org.phenotips.data.Cancer;
import org.phenotips.data.IndexedPatientData;
import org.phenotips.data.Patient;
import org.phenotips.data.PatientData;
import org.phenotips.data.PatientDataController;
import org.phenotips.data.PatientWritePolicy;
import org.phenotips.data.internal.PhenoTipsCancer;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.EntityReference;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

/**
 * Handles the patients cancers.
 *
 * @version $Id$
 * @since 1.4
 */
@Component(roles = { PatientDataController.class })
@Named("cancers")
@Singleton
public class CancersController extends AbstractComplexController<Cancer>
{
    /** The XClass used for storing cancer data. */
    static final EntityReference CANCER_CLASS_REFERENCE = new EntityReference("CancerClass",
            EntityType.DOCUMENT, Constants.CODE_SPACE_REFERENCE);

    private static final String CANCERS_FIELD_NAME = "cancers";

    private static final String CONTROLLER_NAME = CANCERS_FIELD_NAME;

    private static final String JSON_KEY_PRIMARY = "primary";

    @Inject
    private Logger logger;

    /** Provides access to the current execution context. */
    @Inject
    private Provider<XWikiContext> xcontextProvider;

    @Override
    protected List<String> getBooleanFields()
    {
        return Collections.singletonList(JSON_KEY_PRIMARY);
    }

    @Override
    protected List<String> getCodeFields()
    {
        return Collections.emptyList();
    }

    @Override
    protected List<String> getProperties()
    {
        return Collections.emptyList();
    }

    @Override
    protected String getJsonPropertyName()
    {
        return CONTROLLER_NAME;
    }

    @Override
    public String getName()
    {
        return CONTROLLER_NAME;
    }

    @Override
    public PatientData<Cancer> load(@Nonnull final Patient patient)
    {
        try{
            final XWikiDocument doc = patient.getXDocument();
            final List<BaseObject> cancerXWikiObjects = doc.getXObjects(CANCER_CLASS_REFERENCE);
            if (CollectionUtils.isEmpty(cancerXWikiObjects)) {
                return null;
            }
            final List<Cancer> cancers = cancerXWikiObjects.stream()
                    .filter(cancerObj -> cancerObj != null && !cancerObj.getFieldList().isEmpty())
                    .map(PhenoTipsCancer::new)
                    .collect(Collectors.toList());
            return !cancers.isEmpty() ? new IndexedPatientData<>(getName(), cancers) : null;
        } catch (final Exception e) {
            this.logger.error(ERROR_MESSAGE_LOAD_FAILED, e.getMessage());
        }
        return null;
    }

    @Override
    public void writeJSON(
            @Nonnull final Patient patient,
            @Nonnull final JSONObject json,
            @Nullable final Collection<String> selectedFieldNames)
    {
        if (selectedFieldNames == null || selectedFieldNames.contains(CANCERS_FIELD_NAME)) {
            final JSONArray cancersJson = new JSONArray();
            final PatientData<Cancer> data = patient.getData(getName());
            if (data != null && data.size() != 0 && data.isIndexed()) {
                data.forEach(cancer -> addCancerJson(cancer, cancersJson));
            }
            json.put(getJsonPropertyName(), cancersJson);
        }
    }

    /**
     * Adds the {@link JSONObject} generated from {@code cancer} to the {@code cancersJson}.
     *
     * @param cancer the {@link Cancer} object containing cancer data
     * @param cancersJson the {@link JSONArray} containing all cancer data for a {@link Patient patient}
     */
    private void addCancerJson(@Nonnull final Cancer cancer, @Nonnull final JSONArray cancersJson)
    {
        if (StringUtils.isNotBlank(cancer.getId())) {
            cancersJson.put(cancer.toJSON());
        }
    }

    @Override
    public PatientData<Cancer> readJSON(@Nullable final JSONObject json)
    {
        if (json == null || !json.has(getJsonPropertyName())) {
            return null;
        }
        try {
            final JSONArray cancersJson = json.getJSONArray(getJsonPropertyName());
            final List<Cancer> cancers = IntStream.of(0, cancersJson.length())
                    .mapToObj(cancersJson::optJSONObject)
                    .filter(Objects::nonNull)
                    .map(PhenoTipsCancer::new)
                    .collect(Collectors.toList());
            return new IndexedPatientData<>(getName(), cancers);
        } catch (final Exception e) {
            this.logger.error("Could not load cancers from JSON: [{}]", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void save(@Nonnull final Patient patient)
    {
        save(patient, PatientWritePolicy.UPDATE);
    }

    @Override
    public void save(@Nonnull final Patient patient, @Nonnull final PatientWritePolicy policy)
    {
        try {
            final XWikiDocument docX = patient.getXDocument();
            final PatientData<Cancer> cancers = patient.getData(getName());
            if (cancers == null) {
                if (PatientWritePolicy.REPLACE.equals(policy)) {
                    docX.removeXObjects(CANCER_CLASS_REFERENCE);
                }
            } else {
                if (!cancers.isIndexed()) {
                    this.logger.error(ERROR_MESSAGE_DATA_IN_MEMORY_IN_WRONG_FORMAT);
                    return;
                }
                saveCancers(docX, patient, cancers, policy, this.xcontextProvider.get());
            }
        } catch (final Exception ex) {
            this.logger.error("Failed to save cancers data: {}", ex.getMessage(), ex);
        }
    }
}
