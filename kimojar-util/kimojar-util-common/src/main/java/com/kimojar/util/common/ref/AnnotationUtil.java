/**
 * ==============================================================================
 * PROJECT kimojar-util-common
 * PACKAGE com.kimojar.util.common.ref
 * FILE AnnotationUtil.java
 * AUTHOR KiMoJar
 * EMAIL mongoosej@foxmial.com
 * ADDRESS https://www.yuque.com/mongoosej
 * CREATE 2023-02-01
 * ==============================================================================
 * Copyright (C) 2023
 * KiMoJar All rights reserved
 * ==============================================================================
 * This project is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or under
 * EPL Eclipse Public License 1.0.
 * 
 * This means that you have to chose in advance which take before you import
 * the library into your project.
 * 
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE whatever license you
 * decide to adopt.
 * ==============================================================================
 */
package com.kimojar.util.common.ref;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author KiMoJar
 * @date 2023-02-01
 */
public class AnnotationUtil {

	/**
	 * 修改指定类的指定注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param annotationClass 指定注解
	 * @param propertyName 指定注解属性名
	 * @param newValue 新的注解属性值
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> void changeClassAnnotationProperty(Class<?> clazz, Class<T> annotationClass, String propertyName, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		getAnnotationValues(annotation).put(propertyName, newValue);
	}

	/**
	 * 修改指定类的指定注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param annotationClass 指定注解
	 * @param propertyKV 注解属性名与新的值的kv映射
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> void changeClassAnnotationProperties(Class<?> clazz, Class<T> annotationClass, Map<String, Object> propertyKV) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		Map<String, Object> memberValues = getAnnotationValues(annotation);
		for(Entry<String, Object> entry : propertyKV.entrySet())
			memberValues.put(entry.getKey(), entry.getValue());
	}

	/**
	 * 获取指定类的指定注解的属性值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param annotationClass
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Object getClassAnnotationValue(Class<?> clazz, Class<T> annotationClass, String property) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		return getAnnotationValues(annotation).get(property);
	}

	/**
	 * 获取指定类的指定注解的属性值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param annotationClass
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Map<String, Object> getClassAnnotationValues(Class<?> clazz, Class<T> annotationClass) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Annotation annotation = clazz.getAnnotation(annotationClass);
		return getAnnotationValues(annotation);
	}

	/**
	 * 修改指定类的方法的注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param methodName 指定方法名
	 * @param annotationClass 指定注解
	 * @param propertyName 指定注解属性名
	 * @param newValue 新的注解属性值
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> void changeMethodAnnotationProperty(Class<?> clazz, String methodName, Class<T> annotationClass, String propertyName, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		/*
		 * 返回一个数组，其中包含反映该类对象表示的类或接口的所有公共方法的方法对象，包括由类或接口声明的方法对象以及从超类和超接口继承的方法对象。
		 * getMethods获取的methods和getDeclaredMethod获取的methods的root是不同的两个实例，意味着，方法上面的注解实例存在两个。
		 * 所以，如果不确定方法上面的注解使用时是从哪里获取（getMethods or getDeclaredMethod）的，那两个方法上面的注解实例都要反射修改。
		 */
		for(Method method : clazz.getMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				getAnnotationValues(annotation).put(propertyName, newValue);
				break;
			}
		}
		/*
		 * 返回一个方法对象，该对象反映由该类对象表示的类或接口的指定声明方法。
		 * getMethods获取的methods和getDeclaredMethod获取的methods的root是不同的两个实例，意味着，方法上面的注解实例存在两个。
		 * 所以，如果不确定方法上面的注解使用时是从哪里获取（getMethods or getDeclaredMethod）的，那两个方法上面的注解实例都要反射修改。
		 */
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				getAnnotationValues(annotation).put(propertyName, newValue);
				break;
			}
		}
	}

	/**
	 * 修改指定类的方法的注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param methodName 指定方法名
	 * @param annotationClass 指定注解类
	 * @param propertyKV 注解属性名与新的值的kv映射
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> void changeMethodAnnotationProperties(Class<?> clazz, String methodName, Class<T> annotationClass, Map<String, Object> propertyKV) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		/*
		 * 返回一个数组，其中包含反映该类对象表示的类或接口的所有公共方法的方法对象，包括由类或接口声明的方法对象以及从超类和超接口继承的方法对象。
		 * getMethods获取的methods和getDeclaredMethod获取的methods的root是不同的两个实例，意味着，方法上面的注解实例存在两个。
		 * 所以，如果不确定方法上面的注解使用时是从哪里获取（getMethods or getDeclaredMethod）的，那两个方法上面的注解实例都要反射修改。
		 */
		for(Method method : clazz.getMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				Map<String, Object> memberValues = getAnnotationValues(annotation);
				for(Entry<String, Object> entry : propertyKV.entrySet())
					memberValues.put(entry.getKey(), entry.getValue());
				break;
			}
		}
		/*
		 * 返回一个方法对象，该对象反映由该类对象表示的类或接口的指定声明方法。
		 * getMethods获取的methods和getDeclaredMethod获取的methods的root是不同的两个实例，意味着，方法上面的注解实例存在两个。
		 * 所以，如果不确定方法上面的注解使用时是从哪里获取（getMethods or getDeclaredMethod）的，那两个方法上面的注解实例都要反射修改。
		 */
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				Map<String, Object> memberValues = getAnnotationValues(annotation);
				for(Entry<String, Object> entry : propertyKV.entrySet())
					memberValues.put(entry.getKey(), entry.getValue());
				break;
			}
		}
	}

	/**
	 * 获取指定类的指定方法的指令注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param methodName
	 * @param annotationClass
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Object getMethodAnnotationValue(Class<?> clazz, String methodName, Class<T> annotationClass, String property) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				return getAnnotationValues(annotation).get(property);
			}
		}
		return null;
	}

	/**
	 * 获取指定类的指定方法的指令注解的属性的值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param methodName
	 * @param annotationClass
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Object getMethodAnnotationValues(Class<?> clazz, String methodName, Class<T> annotationClass) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				Annotation annotation = method.getAnnotation(annotationClass);
				return getAnnotationValues(annotation);
			}
		}
		return null;
	}

	/**
	 * 获取指定类的指定变量的注解属性
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fieldName
	 * @param annotationClass
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Object getFieldAnnotationValue(Class<?> clazz, String fieldName, Class<T> annotationClass, String property) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Field field : clazz.getDeclaredFields()) {
			if(field.getName().equals(fieldName)) {
				Annotation annotation = field.getAnnotation(annotationClass);
				return getAnnotationValues(annotation).get(property);
			}
		}
		return null;
	}

	/**
	 * 获取指定类的指定变量的注解属性
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fieldName
	 * @param annotationClass
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Annotation> Map<String, Object> getFieldAnnotationValues(Class<?> clazz, String fieldName, Class<T> annotationClass, String property) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Field field : clazz.getDeclaredFields()) {
			if(field.getName().equals(fieldName)) {
				Annotation annotation = field.getAnnotation(annotationClass);
				return getAnnotationValues(annotation);
			}
		}
		return null;
	}

	/**
	 * 修改指定类的方法的参数的注解的属性的值
	 * 似乎不启用，需要再研究
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param methodName 指定方法名
	 * @param parameterIndex 指定参数在方法参数列表中的下标
	 * @param annotationClass 指定注解
	 * @param propertyName 指定注解属性名
	 * @param newValue 新的注解属性值
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Deprecated
	public static <T extends Annotation> void changeParameterAnnotationProperty(Class<?> clazz, String methodName, int parameterIndex, Class<T> annotationClass, String propertyName, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Method method : clazz.getMethods()) {
			if(method.getName().equals(methodName)) {
				if(method.getParameters().length > parameterIndex) {
					Parameter para = method.getParameters()[parameterIndex];
					for(Annotation annotation : para.getAnnotations()) {
						if(annotation.annotationType().getName().equals(annotationClass.getName())) {
							getAnnotationValues(annotation).put(propertyName, newValue);
							break;
						}
					}
					break;
				}
			}
		}
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				if(method.getParameters().length > parameterIndex) {
					Parameter para = method.getParameters()[parameterIndex];
					for(Annotation annotation : para.getAnnotations()) {
						if(annotation.annotationType().getName().equals(annotationClass.getName())) {
							getAnnotationValues(annotation).put(propertyName, newValue);
							break;
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * 修改指定类的方法的参数的注解的属性的值
	 * 似乎不启用，需要再研究
	 * 
	 * @param <T>
	 * @param clazz 指定类
	 * @param methodName 指定方法名
	 * @param parameterIndex 指定参数在方法参数列表中的下标
	 * @param annotationClass 指定注解
	 * @param propertyKV 注解属性名与新的值的kv映射
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Deprecated
	public static <T extends Annotation> void changeParameterAnnotationProperties(Class<?> clazz, String methodName, int parameterIndex, Class<T> annotationClass, Map<String, Object> propertyKV) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		for(Method method : clazz.getMethods()) {
			if(method.getName().equals(methodName)) {
				if(method.getParameters().length > parameterIndex) {
					Parameter para = method.getParameters()[parameterIndex];
					Annotation annotation = para.getAnnotation(annotationClass);
					Map<String, Object> memberValues = getAnnotationValues(annotation);
					for(Entry<String, Object> entry : propertyKV.entrySet())
						memberValues.put(entry.getKey(), entry.getValue());
					break;
				}
			}
		}
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(methodName)) {
				if(method.getParameters().length > parameterIndex) {
					Parameter para = method.getParameters()[parameterIndex];
					Annotation annotation = para.getAnnotation(annotationClass);
					Map<String, Object> memberValues = getAnnotationValues(annotation);
					for(Entry<String, Object> entry : propertyKV.entrySet())
						memberValues.put(entry.getKey(), entry.getValue());
					break;
				}
			}
		}
	}

	/**
	 * 获取参数注解的属性
	 * 
	 * @param annotation
	 * @param property
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getAnnotationValue(Annotation annotation, String property) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return getAnnotationValues(annotation).get(property);
	}

	/**
	 * 获取参数注解的属性
	 * 
	 * @param annotation
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> getAnnotationValues(Annotation annotation) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		InvocationHandler annotationInvocationHandler = Proxy.getInvocationHandler(annotation);
		Field memberValuesField = annotationInvocationHandler.getClass().getDeclaredField("memberValues");
		memberValuesField.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(annotationInvocationHandler);
		return memberValues;
	}
}
