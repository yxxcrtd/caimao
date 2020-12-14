
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

/**
 * 定制代码生成工具
 *
 * Created by yangxinxin@huobi.com on 2015/10/24
 */
public class Code {

    // 模板存放的路径
    private static final String PATH_TEMPLATE = "D:\\www\\caimao\\bana\\gjs-server\\gjs-dubbo\\src\\test\\java";
//    // gjs-api目标路径
//    private static final String PATH_GJS_API_TARGET = "D:\\Code\\caimao\\bana\\gjs-server\\gjs-api\\src\\main\\java\\";
//    // gjs-dubbo Mapper路径
//    private static final String PATH_MAPPER = "D:\\Code\\caimao\\bana\\gjs-server\\gjs-dubbo\\src\\main\\resources\\META-INF\\mappers";
//    // gjs-dubbo目标路径
//    private static final String PATH_GJS_DUBBO_TARGET = "D:\\Code\\caimao\\bana\\gjs-server\\gjs-dubbo\\src\\main\\java\\";

    // bana-api目标路径
    private static final String PATH_GJS_API_TARGET = "D:\\www\\caimao\\bana\\bana-server\\bana-api\\src\\main\\java";
    // bana-dubbo Mapper路径
    private static final String PATH_MAPPER = "D:\\www\\caimao\\bana\\bana-server\\bana-dubbo\\src\\main\\resources\\META-INF\\mappers";
    // bana-dubbo目标路径
    private static final String PATH_GJS_DUBBO_TARGET = "D:\\www\\caimao\\bana\\bana-server\\bana-dubbo\\src\\main\\java\\";

    // 数据库连接地址
    private static final String CONFIG_DATABASE = "jdbc:mysql://172.32.1.218:3307/pz?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
    // 数据库用户名
    private static final String CONFIG_USERNAME = "caimao";
    // 数据库密码
    private static final String CONFIG_PASSWORD = "caimao1234567654321";
    // 表名
    private static final String CONFIG_TABLE = "guji_user_stock";
    // 包名
    private static final String CONFIG_PACKAGE = "com.caimao.bana.api.entity";
    // 创建者（如果不填，会自动生成当前登录账户）
    private static final String CONFIG_AUTHOR = "yangxinxin@huobi.com";

    // 初始化实例
    private Configuration cfg = null;
    private String entity = "";
    private String className = "";

    Map<String, Object> map = new HashMap<>();

    /**
     * Configuration实例的职责：
     * 1，加载模板的路径
     * 2，生产模板的实例
     *
     * @throws IOException
     */
    private void init() throws IOException {
        List<Map<String, String>> list = (List<Map<String, String>>) getTableFieldAndType();
        map.put("author", "".equals(CONFIG_AUTHOR) ? System.getProperty("user.name") : CONFIG_AUTHOR);
        map.put("time", new Date());
        map.put("fields", list); // for (Iterator iterator = list.iterator(); iterator.hasNext();) { System.out.println(iterator.next()); }
        map.put("package", CONFIG_PACKAGE);
        map.put("tableName", CONFIG_TABLE);
        className = getClassName(CONFIG_TABLE);
        map.put("className", className);

        cfg = new Configuration();
        // 设置模板加载的目录
        cfg.setDirectoryForTemplateLoading(new File(PATH_TEMPLATE));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * 生成对象的实体类
     *
     * @throws TemplateException
     * @throws IOException
     */
    private void generateEntity() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("Entity.ftl");
        map.put("classNameEntity", className + "Entity");
        String packageName = getClassNameByPackageName(PATH_GJS_API_TARGET, CONFIG_PACKAGE);
        FileUtils.forceMkdir(new File(packageName));
        File file = new File(packageName + File.separator + className + "Entity.java");
        Writer out = new FileWriter(file);
        template.process(map, out);
        out.close();
        System.out.println("1，生成对象的实体类：" + packageName + File.separator + className + "Entity.java");
    }

    /**
     * 生成对象的Mapper.xml
     *
     * @throws IOException
     * @throws TemplateException
     */
    private void generateMapper() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("Mapper.ftl");
        FileUtils.forceMkdir(new File(PATH_MAPPER));
        File file = new File(PATH_MAPPER + File.separator + className + "Mapper.xml");
        Writer out = new FileWriter(file);
        String s = CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf(".")) + ".entity.req";
        map.put("s", s); // 包名
        map.put("package1", CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf("."))); // 因为所有包都统一放到了req下，需要把配置的包名最后一个单词去了
        template.process(map, out);
        out.close();
        System.out.println("2，生成对象的Mapper：" + PATH_MAPPER + File.separator + className + "Mapper.xml");
    }

    /**
     * 生成对象的查询
     *
     * @throws TemplateException
     * @throws IOException
     */
    private void generateReq() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("Req.ftl");
        map.put("classNameEntity", className + "Entity");
        String s = CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf(".")) + ".entity.req";
        map.put("s", s); // 包名
        String packageName = getClassNameByPackageName(PATH_GJS_API_TARGET, s);
        FileUtils.forceMkdir(new File(packageName));
        File file = new File(packageName + File.separator + "FQuery" + StringUtils.capitalize(className) + "Req.java");
        Writer out = new FileWriter(file);
        template.process(map, out);
        out.close();
        System.out.println("3，生成对象的查询：" + packageName + File.separator + "FQuery" + StringUtils.capitalize(className) + "Req.java");
    }

    /**
     * 生成对象的Service
     *
     * @throws IOException
     * @throws TemplateException
     */
    private void generateService() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("Service.ftl");
        String s = CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf(".")) + ".service";
        map.put("s", s); // 包名
        String packageName = getClassNameByPackageName(PATH_GJS_API_TARGET, s);
        FileUtils.forceMkdir(new File(packageName));
        File file = new File(packageName + File.separator + "I" + StringUtils.capitalize(className) + "Service.java");
        Writer out = new FileWriter(file);
        template.process(map, out);
        out.close();
        System.out.println("4，生成对象的接口：" + packageName + File.separator + "I" + StringUtils.capitalize(className) + "Service.java");
    }

    /**
     * 生成对象的ServiceImpl
     *
     * @throws IOException
     * @throws TemplateException
     */
    private void generateServiceImpl() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("ServiceImpl.ftl");
        String s = CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf("."));
        s = s.substring(0, s.lastIndexOf(".")) + ".server.service";
        map.put("s", s); // 包名
        map.put("package1", CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf("."))); // 使用package生成import的package，但是目录得往上一级
        map.put("s1", s.substring(0, s.lastIndexOf(".")) + ".dao"); // 包名（把上面的包名中的 service 换成 dao）
        String packageName = getClassNameByPackageName(PATH_GJS_DUBBO_TARGET, s);
        FileUtils.forceMkdir(new File(packageName));
        File file = new File(packageName + File.separator + "I" + StringUtils.capitalize(className) + "ServiceImpl.java");
        Writer out = new FileWriter(file);
        template.process(map, out);
        out.close();
        System.out.println("5，生成对象的接口实现：" + packageName + File.separator + "I" + StringUtils.capitalize(className) + "ServiceImpl.java");
    }

    /**
     * 生成对象的DAO
     *
     * @throws IOException
     * @throws TemplateException
     */
    private void generateDAO() throws IOException, TemplateException {
        // 使用Configuration实例加载指定的模板文件
        Template template = cfg.getTemplate("DAO.ftl");
        String s = CONFIG_PACKAGE.substring(0, CONFIG_PACKAGE.lastIndexOf("."));
        s = s.substring(0, s.lastIndexOf(".")) + ".server.dao";
        map.put("s", s); // 包名
        String packageName = getClassNameByPackageName(PATH_GJS_DUBBO_TARGET, s);
        FileUtils.forceMkdir(new File(packageName));
        File file = new File(packageName + File.separator + StringUtils.capitalize(className) + "DAO.java");
        Writer out = new FileWriter(file);
        template.process(map, out);
        out.close();
        System.out.println("6，生成对象的DAO：" + packageName + File.separator + StringUtils.capitalize(className) + "DAO.java");
    }

    /**
     * 获取数据库中表字段和字段类型
     *
     * @return
     */
    private static Collection<Map<String, String>> getTableFieldAndType() {
        List<Map<String, String>> list = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(CONFIG_DATABASE, CONFIG_USERNAME, CONFIG_PASSWORD);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getColumns(null, null, CONFIG_TABLE, null);
            while (resultSet.next()) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put("fieldNameOriginal", resultSet.getString("COLUMN_NAME"));
                map.put("fieldName", genFieldName(resultSet.getString("COLUMN_NAME")));
                map.put("fieldTypeOriginal", genXMLFieldType(resultSet.getString("TYPE_NAME")));
                map.put("fieldType", genFieldType(resultSet.getString("TYPE_NAME")));
                map.put("fieldRemark", resultSet.getString("REMARKS"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 根据根路径和包名，生成最终的全路径
     *
     * @param rootPath
     * @param packageName
     * @return
     */
    private static String getClassNameByPackageName(String rootPath, String packageName) {
        String returnString = "";
        returnString += rootPath;
        String[] packagePath = packageName.split("\\.");
        for (String s : packagePath) {
            returnString += File.separator + s;
        }
        return returnString;
    }

    /**
     * 根据表字段获取字段名称
     *
     * @param field 字段名称
     * @return
     */
    private static String genFieldName(String field) {
        String result = "";
        String[] fields = field.toLowerCase().split("_");
        result += fields[0];
        if (1 < fields.length) {
            for(int i = 1; i < fields.length; i++) {
                result += fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1, fields[i].length());
            }
        }
        return result;
    }

    /**
     * 根据表字段类型生成对应的java的数据类型
     *
     * @param type 字段类型
     * @return
     */
    private static String genFieldType(String type) {
        String result = "String";
        if ("varchar".equals(type.toLowerCase()) || "text".equals(type.toLowerCase())) {
            result = "String";
        } else if ("int".equals(type.toLowerCase()) || "smallint".equals(type.toLowerCase()) || type.toLowerCase().startsWith("int")) {
            result = "Integer";
        } else if ("bigint".equals(type.toLowerCase()) || type.toLowerCase().startsWith("bigint")) {
            result = "Long";
        } else if ("decimal".equals(type.toLowerCase())) {
            result = "Float";
        } else if ("boolean".equals(type.toLowerCase())) {
            result = "Boolean";
        }
        return result;
    }

    /**
     * 根据表字段类型生成对应的Mapper.xml数据类型
     *
     * @param type 字段类型
     * @return
     */
    private static String genXMLFieldType(String type) {
        String result = "varchar";
        if ("int unsigned".equals(type.toLowerCase()) || "smallint".equals(type.toLowerCase()) || type.toLowerCase().startsWith("int")) {
            result = "integer";
        } else if ("text".equals(type.toLowerCase()) || type.toLowerCase().startsWith("int")) {
            result = "longvarchar";
        } else if ("decimal".equals(type.toLowerCase())) {
            result = "decimal";
        }
        return result;
    }

    /**
     * 根据表名生成类名
     *
     * @param tableName 表名
     * @return
     */
    private static String getClassName(String tableName) {
        String result = "";
        String[] fields = tableName.toLowerCase().split("_");
        if (1 < fields.length) {
            for(int i = 0; i < fields.length; i++) {
                result += fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1, fields[i].length());
            }
        }
        return result;
    }

    /**
     * 生成代码
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void generateCode() throws IOException, TemplateException {
        Code code = new Code();
        code.init();

        // 1，生成对象的实体类
        code.generateEntity();

        // 2，生成对象的Mapper.xml
        code.generateMapper();

        // 3，生成对象的查询
        code.generateReq();

        // 4，生成对象的Service
        code.generateService();

        // 5，生成对象的ServiceImpl
        code.generateServiceImpl();

        // 6，生成对象的DAO
        code.generateDAO();
    }

}
