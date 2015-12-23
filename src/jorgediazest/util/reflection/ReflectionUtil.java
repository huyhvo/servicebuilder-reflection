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

package com.jorgediaz.util.reflection;

import com.jorgediaz.util.model.Model;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jorge Díaz
 */
public class ReflectionUtil {

	public static Object castStringToJdbcTypeObject(int type, String value) {
		value = StringUtil.unquote(value);

		Object result = null;

		switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
			case Types.CLOB:
				result = value;
				break;

			case Types.NUMERIC:
			case Types.DECIMAL:
				result = new java.math.BigDecimal(value);
				break;

			case Types.BIT:
			case Types.BOOLEAN:
				result = Boolean.parseBoolean(value);
				break;

			case Types.TINYINT:
				result = Byte.parseByte(value);
				break;

			case Types.SMALLINT:
				result = Short.parseShort(value);
				break;

			case Types.INTEGER:
				result = Integer.parseInt(value);
				break;

			case Types.BIGINT:
				result = Long.parseLong(value);
				break;

			case Types.REAL:
			case Types.FLOAT:
				result = Float.parseFloat(value);
				break;

			case Types.DOUBLE:
				result = Double.parseDouble(value);
				break;

			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				result = value.getBytes();
				break;

			case Types.DATE:
				result = java.sql.Date.valueOf(value);
				break;

			case Types.TIME:
				result = java.sql.Time.valueOf(value);
				break;

			case Types.TIMESTAMP:
				result = java.sql.Timestamp.valueOf(value);
				break;

			default:
				throw new RuntimeException(
					"Unsupported conversion for " +
						ReflectionUtil.getJdbcTypeNames().get(type));
		}

		return result;
	}

	public static Criterion generateSingleCriterion(
		Model model, String attrName, String attrValue, String op) {

		Criterion criterion = null;

		if (model.isPartOfPrimaryKeyMultiAttribute(attrName)) {
			attrName = "primaryKey." + attrName;
		}

		try {
			if (model.hasAttribute(attrValue)) {
				criterion =
					(Criterion)contructorCriterionImpl.newInstance(
						contructorPropertyExpression.newInstance(
					new Object[] { attrName, attrValue, op}));
			}
			else {
				Object value =
					ReflectionUtil.castStringToJdbcTypeObject(
						model.getAttributeType(attrName), attrValue);

				criterion =
					(Criterion)contructorCriterionImpl.newInstance(
						contructorSimpleExpression.newInstance(
					new Object[] { attrName, value, op}));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return criterion;
	}

	public static Class<?> getJdbcTypeClass(int type) {
		Class<?> result = Object.class;

		switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
			case Types.CLOB:
				result = String.class;
				break;

			case Types.NUMERIC:
			case Types.DECIMAL:
				result = java.math.BigDecimal.class;
				break;

			case Types.BIT:
			case Types.BOOLEAN:
				result = Boolean.class;
				break;

			case Types.TINYINT:
				result = Byte.class;
				break;

			case Types.SMALLINT:
				result = Short.class;
				break;

			case Types.INTEGER:
				result = Integer.class;
				break;

			case Types.BIGINT:
				result = Long.class;
				break;

			case Types.REAL:
			case Types.FLOAT:
				result = Float.class;
				break;

			case Types.DOUBLE:
				result = Double.class;
				break;

			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				result = Byte[].class;
				break;

			case Types.DATE:
				result = java.sql.Date.class;
				break;

			case Types.TIME:
				result = java.sql.Time.class;
				break;

			case Types.TIMESTAMP:
				result = java.sql.Timestamp.class;
				break;
		}

		return result;
	}

	public static Map<Integer, String> getJdbcTypeNames() {

		if (jdbcTypeNames == null) {
			Map<Integer, String> aux = new HashMap<Integer, String>();

			for (Field field : Types.class.getFields()) {
				try {
					aux.put((Integer)field.get(null), field.getName());
				}
				catch (IllegalArgumentException e) {
				}
				catch (IllegalAccessException e) {
				}
			}

			jdbcTypeNames = aux;
		}

		return jdbcTypeNames;
	}

	public static Object getLiferayModelImplField(
		Class<?> classLiferayModelImpl, String liferayModelImplField) {

		Object data = null;
		try {
			Field field = classLiferayModelImpl.getDeclaredField(
				liferayModelImplField);
			data = field.get(null);
		}
		catch (Exception e) {
			throw new RuntimeException(
				"Error accessing to " +
				classLiferayModelImpl.getName() + "#" +
				liferayModelImplField, e);
		}

		return data;
	}

	public static void makeFieldModifiable(Field nameField)
		throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField =
			nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}

	public static void setFieldValue(Object owner, Field field, Object value)
		throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		makeFieldModifiable(field);
		field.set(owner, value);
	}

	static Log _log = LogFactoryUtil.getLog(ReflectionUtil.class);

	private static Constructor<?> contructorCriterionImpl;
	private static Constructor<?> contructorPropertyExpression;
	private static Constructor<?> contructorSimpleExpression;
	private static Map<Integer, String> jdbcTypeNames = null;

	static {
		try {
			Class<?> criterion =
				PortalClassLoaderUtil.getClassLoader().loadClass(
				"org.hibernate.criterion.Criterion");

			Class<?> simpleExpression =
				PortalClassLoaderUtil.getClassLoader().loadClass(
				"org.hibernate.criterion.SimpleExpression");
			contructorSimpleExpression =
				simpleExpression.getDeclaredConstructor(
					String.class, Object.class, String.class);
			contructorSimpleExpression.setAccessible(true);

			Class<?> propertyExpression =
				PortalClassLoaderUtil.getClassLoader().loadClass(
				"org.hibernate.criterion.PropertyExpression");
			contructorPropertyExpression =
				propertyExpression.getDeclaredConstructor(
					String.class, String.class, String.class);
			contructorPropertyExpression.setAccessible(true);

			Class<?> criterionImpl =
				PortalClassLoaderUtil.getClassLoader().loadClass(
				"com.liferay.portal.dao.orm.hibernate.CriterionImpl");
			contructorCriterionImpl = criterionImpl.getDeclaredConstructor(
				criterion);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}