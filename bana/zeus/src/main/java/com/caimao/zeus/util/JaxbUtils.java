/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.*;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 任培伟
 * @version 1.0
 */
public class JaxbUtils {

    private static Logger logger = LoggerFactory.getLogger(JaxbUtils.class);

    private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

    public static JAXBContext getJaxbContext(Class<?> classesToBeBound) {
        JAXBContext jaxbContext = jaxbContexts.get(classesToBeBound);

        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(classesToBeBound);
            } catch (JAXBException e) {
                logger.error("初始化JaxbContext实例时出错!", e);
            }
            jaxbContexts.putIfAbsent(classesToBeBound, jaxbContext);
        }
        return jaxbContext;
    }

    public static Marshaller createMarshaller(Class<?> classesToBeBound,
                                              String encoding) {
        JAXBContext jaxbContext = getJaxbContext(classesToBeBound);

        Marshaller marshaller = null;
        try {
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.debug("得到Marshaller实例时出错!", e);
        }

        if (marshaller != null) {
            try {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                if (StringUtils.isNotBlank(encoding)) {
                    marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
                }
            } catch (PropertyException e) {
                logger.error("设置Marshaller实例的属性时出错!", e);
            }
        }
        return marshaller;
    }

    public static Unmarshaller createUnMarshaller(Class<?> clazz) {
        JAXBContext jaxbContext = getJaxbContext(clazz);

        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.debug("得到UnMarshaller实例时出错!", e);
        }
        return unmarshaller;
    }

    public static void toXml(Writer writer, Collection<?> root, String rootName, Class<?> classesToBeBound, String encoding) {
        Marshaller marshaller = createMarshaller(classesToBeBound, encoding);

        CollectionWrapper collectionWrapper = new CollectionWrapper();
        collectionWrapper.root = root;
        JAXBElement<CollectionWrapper> jaxbElement =
                new JAXBElement<CollectionWrapper>(new QName(rootName), CollectionWrapper.class, collectionWrapper);

        try {
            marshaller.marshal(jaxbElement, writer);
        } catch (JAXBException e) {
            logger.error("导出" + root.toString() + "为xml格式文件时出错!", e);
        }
    }

    public static String toXml(Collection<?> root, String rootName, Class<?> classesToBeBound, String encoding) {
        Writer writer = new StringWriter();
        toXml(writer, root, rootName, classesToBeBound, encoding);
        return writer.toString();
    }

    public static String toXml(Collection<?> root, String rootName, Class<?> classesToBeBound) {
        Writer writer = new StringWriter();
        toXml(writer, root, rootName, classesToBeBound, null);
        return writer.toString();
    }

    public static void toXml(Writer writer, Object root, Class<?> classesToBeBound, String encoding) {
        Marshaller marshaller = createMarshaller(classesToBeBound, encoding);
        try {
            marshaller.marshal(root, writer);
        } catch (JAXBException e) {
            logger.error("导出" + root.toString() + "为xml格式文件时出错!", e);
        }
    }

    public static String toXml(Object root, Class<?> classesToBeBound, String encoding) {
        Writer writer = new StringWriter();
        toXml(writer, root, classesToBeBound, encoding);
        return writer.toString();
    }

    public static String toXml(Object root, String encoding) {
        Class<?> classesToBeBound = ReflectUtils.getRealClass(root);
        return toXml(root, classesToBeBound, encoding);
    }

    public static String toXml(Object root) {
        Class<?> classesToBeBound = ReflectUtils.getRealClass(root);
        return toXml(root, classesToBeBound, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(InputStream inputStream, Class<T> declaredType) {
        Unmarshaller unmarshaller = createUnMarshaller(declaredType);

        T t = null;
        try {
            t = (T) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            logger.error("将输入流转化为" + declaredType.getName() + "对象时出错!", e);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(Reader reader, Class<T> declaredType) {
        Unmarshaller unmarshaller = createUnMarshaller(declaredType);

        T t = null;
        try {
            t = (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            logger.error("将输入流转化为" + declaredType.getName() + "对象时出错!", e);
        }
        return t;
    }

    public static <T> T fromXml(String xml, Class<T> declaredType) {
        StringReader reader = new StringReader(xml);
        return fromXml(reader, declaredType);
    }

    public static class CollectionWrapper {
        @XmlAnyElement
        private Collection<?> root;

        public Collection<?> getRoot() {
            return root;
        }

        public void setRoot(Collection<?> root) {
            this.root = root;
        }
    }
}