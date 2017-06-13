/**
 * Copyright (c) 2015-present Jorge Díaz All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package jorgediazest.util.model;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Portlet;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jorgediazest.util.service.Service;
import jorgediazest.util.service.ServiceWrapper;

/**
 * @author Jorge Díaz
 */
public class ModelWrapper implements Model, Cloneable {

	public ModelWrapper(Model model) {
		this.model = model;
	}

	public void addFilter(Criterion filter) {
		if (serviceWrapper == null) {
			serviceWrapper = new ServiceWrapper(model.getService());
		}

		serviceWrapper.addFilter(filter);
	}

	@Override
	public Model clone() {
		ModelWrapper modelWrapper = new ModelWrapper(model);

		if (serviceWrapper != null) {
			modelWrapper.serviceWrapper = serviceWrapper.clone();
		}

		modelWrapper.setNameSuffix(suffix);

		return modelWrapper;
	}

	@Override
	public int compareTo(Model o) {
		return model.compareTo(o);
	}

	@Override
	public long count() {
		return model.count();
	}

	@Override
	public long count(Criterion condition) {
		return model.count(condition);
	}

	@Override
	public Criterion generateCriterionFilter(String stringFilter) {
		return model.generateCriterionFilter(stringFilter);
	}

	@Override
	public Criterion generateInCriteria(String property, List<Long> list) {
		return model.generateInCriteria(property, list);
	}

	@Override
	public Criterion generateSingleCriterion(String filter) {
		return model.generateSingleCriterion(filter);
	}

	@Override
	public Criterion generateSingleCriterion(
		String attrName, String attrValue, String op) {

		return model.generateSingleCriterion(attrName, attrValue, op);
	}

	@Override
	public int getAttributePos(String name) {
		return model.getAttributePos(name);
	}

	@Override
	public Object[][] getAttributes() {
		return model.getAttributes();
	}

	@Override
	public String[] getAttributesName() {
		return model.getAttributesName();
	}

	@Override
	public int[] getAttributesType() {
		return model.getAttributesType();
	}

	@Override
	public int getAttributeType(String name) {
		return model.getAttributeType(name);
	}

	@Override
	public Class<?> getAttributeTypeClass(String name) {
		return model.getAttributeTypeClass(name);
	}

	@Override
	public String getClassName() {
		return model.getClassName();
	}

	@Override
	public long getClassNameId() {
		return model.getClassNameId();
	}

	@Override
	public Criterion getCompanyFilter(long companyId) {
		return model.getCompanyFilter(companyId);
	}

	@Override
	public Criterion getCompanyGroupFilter(long companyId, long groupId) {
		return model.getCompanyGroupFilter(companyId, groupId);
	}

	public String getDisplayName(Locale locale) {
		String displayName = model.getDisplayName(locale);

		if (Validator.isNotNull(displayName) && Validator.isNotNull(suffix)) {
			return displayName + " (" + suffix + ")";
		}

		return displayName;
	}

	@Override
	public Model getFilteredModel(Criterion filters) {
		return model.getFilteredModel(filters);
	}

	@Override
	public Model getFilteredModel(Criterion filters, String nameSufix) {
		return model.getFilteredModel(filters, nameSufix);
	}

	@Override
	public Model getFilteredModel(String filters) {
		return model.getFilteredModel(filters);
	}

	@Override
	public Model getFilteredModel(String filters, String nameSufix) {
		return model.getFilteredModel(filters, nameSufix);
	}

	@Override
	public Indexer getIndexer() {
		return model.getIndexer();
	}

	@Override
	public Indexer getIndexerNullSafe() {
		return model.getIndexerNullSafe();
	}

	@Override
	public ModelFactory getModelFactory() {
		return model.getModelFactory();
	}

	@Override
	public String getName() {
		if (name == null) {
			return model.getName();
		}

		return name;
	}

	@Override
	public Portlet getPortlet() {
		return model.getPortlet();
	}

	@Override
	public String getPortletId() {
		return model.getPortletId();
	}

	@Override
	public Set<Portlet> getPortlets() {
		return model.getPortlets();
	}

	@Override
	public String getPrimaryKeyAttribute() {
		return model.getPrimaryKeyAttribute();
	}

	@Override
	public String[] getPrimaryKeyMultiAttribute() {
		return model.getPrimaryKeyMultiAttribute();
	}

	@Override
	public Property getProperty(String attribute) {
		return model.getProperty(attribute);
	}

	@Override
	public Projection getPropertyProjection(String attribute) {
		return model.getPropertyProjection(attribute);
	}

	@Override
	public ProjectionList getPropertyProjection(String[] attributes) {
		return model.getPropertyProjection(attributes);
	}

	@Override
	public ProjectionList getPropertyProjection(
		String[] attributes, List<String> validAttributes) {

		return model.getPropertyProjection(attributes, validAttributes);
	}

	@Override
	public Service getService() {
		if (serviceWrapper == null) {
			return model.getService();
		}

		return serviceWrapper;
	}

	@Override
	public StagedModelDataHandler<?> getStagedModelDataHandler() {
		return model.getStagedModelDataHandler();
	}

	@Override
	public TableInfo getTableInfo() {
		return model.getTableInfo();
	}

	@Override
	public TableInfo getTableInfo(String attribute) {
		return model.getTableInfo(attribute);
	}

	@Override
	public Map<String, TableInfo> getTableInfoMappings() {
		return model.getTableInfoMappings();
	}

	@Override
	public TrashHandler getTrashHandler() {
		return model.getTrashHandler();
	}

	@Override
	public boolean hasAttribute(String attribute) {
		return model.hasAttribute(attribute);
	}

	@Override
	public boolean hasAttributes(String[] attributes) {
		return model.hasAttributes(attributes);
	}

	@Override
	public boolean hasIndexer() {
		return model.hasIndexer();
	}

	@Override
	public boolean hasIndexerEnabled() {
		return model.hasIndexerEnabled();
	}

	@Override
	public boolean isAuditedModel() {
		return model.isAuditedModel();
	}

	@Override
	public boolean isGroupedModel() {
		return model.isGroupedModel();
	}

	@Override
	public boolean isPartOfPrimaryKeyMultiAttribute(String attribute) {
		return model.isPartOfPrimaryKeyMultiAttribute(attribute);
	}

	@Override
	public boolean isResourcedModel() {
		return model.isResourcedModel();
	}

	@Override
	public boolean isStagedModel() {
		return model.isStagedModel();
	}

	@Override
	public boolean isTrashEnabled() {
		return model.isTrashEnabled();
	}

	@Override
	public boolean isWorkflowEnabled() {
		return model.isWorkflowEnabled();
	}

	@Override
	public boolean modelEqualsClass(Class<?> clazz) {
		return model.modelEqualsClass(clazz);
	}

	public void setFilter(Criterion filter) {
		if (serviceWrapper == null) {
			serviceWrapper = new ServiceWrapper(model.getService());
		}

		serviceWrapper.setFilter(filter);
	}

	public void setNameSuffix(String suffix) {
		if (Validator.isNull(suffix)) {
			return;
		}

		this.suffix = suffix;

		this.name = getClassName() + "_" + this.suffix;
	}

	@Override
	public String toString() {
		return model.toString();
	}

	protected Model model;
	protected String name = null;
	protected ServiceWrapper serviceWrapper = null;
	protected String suffix = null;

}