package com.caimao.gjs.server.utils;

import com.caimao.gjs.server.trade.protocol.sjs.entity.res.FSJSDoOpenAccountRes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class XMLForBean {
    private static DocumentBuilder DOCBUILDER;
    private static final String HEAD = "head";
    private static final String RECORD = "record";
    private static final String TAG_START = "<";
    private static final String TAG_END = ">";
    private static final String TAG_START_FOR_END = "</";
    private static final String BODY = "body";
    private static final String REQUEST = "request";

    static {
        try {
            DOCBUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
        }
    }

    private static <T> T toObject(Element el, Class<T> cls) {
        T o = null;
        try {
            NodeList list = el.getChildNodes();
            o = cls.newInstance();
            if (list != null) {

                for (int i = 0; i < list.getLength(); i++) {
                    Node n = list.item(i);

                    if (!n.getNodeName().equals("#text")) {
                        Element node = (Element) list.item(i);
                        Node vnode = node.getFirstChild();
                        if (vnode != null) {
                            setFieldValue(o, node.getTagName(), vnode.getNodeValue());
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }


    private static Class<?> getClassGenricType(Class<?> cls) {
        Type type = cls.getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        return (Class<?>) p.getActualTypeArguments()[0];
    }

    private static void toList(Element el, Object o) {
        try {
            Field field = getDeclaredField(o, RECORD);

            List list = new ArrayList<>();
            NodeList nodes = el.getElementsByTagName(RECORD);
            if (nodes != null) {
                Class cls = getClassGenricType(o.getClass());
                for (int i = 0; i < nodes.getLength(); i++) {
                    list.add(toObject((Element) nodes.item(i), cls));
                }
            }
            field.setAccessible(true);
            field.set(o, list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static <T> T toObject(String xml, Class<T> cls) {
        T v = null;
        try {
            Document doc = DOCBUILDER.parse(new InputSource(new StringReader(xml)));
            NodeList nodes = doc.getElementsByTagName(HEAD);
            if (nodes != null && nodes.getLength() > 0) {
                Element el = (Element) nodes.item(0);
                v = toObject(el, cls);
                if (v != null) {
                    nodes = doc.getElementsByTagName(BODY);
                    if (nodes != null && nodes.getLength() > 0) {
                        el = (Element) nodes.item(0);
                        toList(el, v);
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    private static Object getObjectValue(Field f, String str) {
        String name = f.getType().getName();
        if (name.equalsIgnoreCase("string")) {
            return str;
        } else if (name.equalsIgnoreCase("int")) {
            return Integer.parseInt(str);
        } else if (name.equalsIgnoreCase("boolean")) {
            return Boolean.parseBoolean(str);
        } else if (name.equalsIgnoreCase("long")) {
            return Long.parseLong(str);
        } else if (name.equalsIgnoreCase("float")) {
            return Float.parseFloat(str);
        } else if (name.equalsIgnoreCase("double")) {
            return Double.parseDouble(str);
        }
        return str;
    }

    public static String toXML(Object object, String headXml) {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        addTagStart(xml, REQUEST);
        addTagStart(xml, HEAD);
        xml.append(headXml);
        addTagEnd(xml, HEAD);
        addTagStart(xml, BODY);
        addTagStart(xml, RECORD);
        addObject(xml, object);
        addTagEnd(xml, RECORD);
        addTagEnd(xml, BODY);
        addTagEnd(xml, REQUEST);
        return xml.toString();
    }

    private static void addObject(StringBuilder xml, Object o) {
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                addTag(xml, o, fields[i]);
            }
        }
    }


    private static void addTagStart(StringBuilder xml, String name) {
        xml.append(TAG_START).append(name).append(TAG_END);
    }

    private static void addTag(StringBuilder xml, Object o, Field field) {
        String name = field.getName();
        try {
            field.setAccessible(true);
            Object v = field.get(o);
            addTagStart(xml, name);
            if (v != null) {
                xml.append(v.toString());
            }
            addTagEnd(xml, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void addTagEnd(StringBuilder xml, String name) {
        xml.append(TAG_START_FOR_END).append(name).append(TAG_END);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */

    private static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters     : 父类中的方法参数
     * @return 父类中方法的执行结果
     */

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) {
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        //抑制Java对方法进行检查,主要是针对私有方法而言
        method.setAccessible(true);

        try {
            if (null != method) {

                //调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */

    private static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value     : 将要设置的值
     */

    private static void setFieldValue(Object object, String fieldName, Object value) {

        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            return;
        }
        //抑制Java对其的检查
        field.setAccessible(true);

        try {
            //将 object 中 field 所代表的值 设置为 value
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */

    private static Object getFieldValue(Object object, String fieldName) {

        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);

        //抑制Java对其的检查
        field.setAccessible(true);

        try {
            //获取 object 中 field 所代表的属性值
            return field.get(object);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<response>\n" +
                "<head>\n" +
                "  <h_teller_id_1></h_teller_id_1>\n" +
                "  <h_rsp_msg>处理成功</h_rsp_msg>\n" +
                "  <h_teller_id_2></h_teller_id_2>\n" +
                "  <h_exch_code>880120</h_exch_code>\n" +
                "  <h_branch_id>B0077001</h_branch_id>\n" +
                "  <h_rsp_code>HJ0000</h_rsp_code>\n" +
                "  <h_bank_no>1111</h_bank_no>\n" +
                "  <h_term_type>02</h_term_type>\n" +
                "  <h_work_date></h_work_date>\n" +
                "  <h_teller_id>C09100</h_teller_id>\n" +
                "  <h_exch_date></h_exch_date>\n" +
                "  <h_bk_seq_no>0000000001</h_bk_seq_no>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <record>\n" +
                "    <acct_no>1080127021</acct_no>\n" +
                "  </record>\n" +
                "</body>\n" +
                "</response>";
        FSJSDoOpenAccountRes res = XMLForBean.toObject(xml, FSJSDoOpenAccountRes.class);
        System.out.println(res.getRecord().get(0).getAcct_no());

    }
}