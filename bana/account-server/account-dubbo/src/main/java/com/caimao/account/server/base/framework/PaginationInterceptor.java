package com.caimao.account.server.base.framework;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mybatis 分页插件
 * Created by WangXu on 2015/5/29.
 */
@Intercepts({@org.apache.ibatis.plugin.Signature(type=org.apache.ibatis.executor.statement.StatementHandler.class, method="prepare", args={Connection.class})})
public class PaginationInterceptor implements Interceptor {
    private static final Logger LOG = LoggerFactory.getLogger(PaginationInterceptor.class);

    private String pageMapper = "";
    private String pageVarName = "page";

    @Autowired
    private PageConverterFactory pageConverterFactory;

    public String getPageMapper() { return this.pageMapper; }

    public void setPageMapper(String pageMapper) {
        this.pageMapper = pageMapper;
    }

    public String getPageVarName() {
        return this.pageVarName;
    }

    public void setPageVarName(String pageVarName) {
        this.pageVarName = pageVarName;
    }


    @Override
    public Object intercept(Invocation invoke) throws Throwable {
        if ((invoke.getTarget() instanceof RoutingStatementHandler)) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler)invoke.getTarget();

            BaseStatementHandler delegate = (BaseStatementHandler)ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement)ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

            if (mappedStatement.getId().matches(this.pageMapper)) {
                BoundSql boundSql = delegate.getBoundSql();
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    throw new RuntimeException("no page parameter");
                }

                IPageConverter pageConverter = null;
                IPageParameter page = null;
                if ((parameterObject instanceof IPageParameter)) {
                    page = (IPageParameter)parameterObject;
                } else if ((parameterObject instanceof Map)) {
                    Map map = (Map)parameterObject;
                    page = (IPageParameter)map.get(this.pageVarName);
                    if (page == null)
                        page = new PageParameter();
                } else if (this.pageConverterFactory != null) {
                    pageConverter = this.pageConverterFactory.createPageConverter(parameterObject);
                    if (pageConverter != null) {
                        page = pageConverter.toPage(parameterObject);
                    }
                }

                if (page == null) {
                    Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, this.pageVarName);
                    if (pageField != null) {
                        page = (IPageParameter)ReflectHelper.getValueByFieldName(parameterObject, this.pageVarName);
                        if (page == null) {
                            page = new PageParameter();
                        }
                        ReflectHelper.setValueByFieldName(parameterObject, this.pageVarName, page);
                    } else {
                        throw new RuntimeException("no page parameter");
                    }
                }

                String sql = boundSql.getSql();
                if (page.isRequireTotal()) {
                    Connection connection = (Connection)invoke.getArgs()[0];

                    String countSql = generateCountSql(sql);
                    LOG.debug("COUNT SQL:[{}]", countSql);
                    PreparedStatement countStmt = connection.prepareStatement(countSql);
                    try {
                        setParameters(countStmt, mappedStatement, boundSql, parameterObject);

                        ResultSet rs = countStmt.executeQuery();
                        int count = 0;
                        if (rs.next()) {
                            count = rs.getInt(1);
                        }
                        page.setTotal(count);
                        if (pageConverter != null) {
                            pageConverter.returnTotal(parameterObject, count);
                        }

                        rs.close();
                    }
                    finally {
                        countStmt.close();
                    }
                }
                String pageSql = sql + " LIMIT " + page.getStart() + "," + page.getLimit();
                LOG.debug("PAGE SQL:[{}]", pageSql);
                ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
            }
        }
        return invoke.proceed();
    }

    private String generateCountSql(String sql) {
        String upperSql = sql.trim().toUpperCase();
        int startIndex = 0;
        int stack = 1;
        for (int i = 0; i < 10; i++) {
            int indexs = upperSql.indexOf("SELECT ", startIndex + 2);
            int indexf = upperSql.indexOf("FROM ", startIndex + 2);
            if ((indexs < 0) || (indexf < indexs)) {
                stack--;
                startIndex = indexf;
            } else {
                stack++;
                startIndex = indexs;
            }

            for (int k = 0; k < 10; k++){
                String orderString;
                int indexAsc = sql.indexOf(" ASC");
                int indexDesc = sql.indexOf(" DESC");
                if(indexAsc < 0 && indexDesc < 0){ continue;}
                else if(indexAsc > 0 && indexDesc > 0){orderString = indexAsc < indexDesc?" ASC":" DESC";}
                else if(indexAsc > 0){ orderString = " ASC";}
                else if(indexDesc > 0){ orderString = " DESC";}
                else{ orderString = "";}
                if("".equals(orderString)) continue;
                Matcher matcher = Pattern.compile("ORDER BY (.*?)" + orderString, Pattern.DOTALL).matcher(sql);
                sql = matcher.replaceAll("");
                sql = sql.replace(",target_id DESC", "");
                sql = sql.replace(",created DESC", "");
            }
            if (stack == 0) {
                return "select count(1)  " + sql.substring(indexf);
            }
        }

        return "select count(1) from (" + sql + ") myCount";
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject)
            throws SQLException
    {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());

        List parameterMappings = boundSql.getParameterMappings();

        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);

            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = (ParameterMapping)parameterMappings.get(i);
                if (parameterMapping.getMode() == ParameterMode.OUT)
                    continue;
                String propertyName = parameterMapping.getProperty();
                PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                Object value;
                if (parameterObject == null) {
                    value = null;
                }
                else
                {
                    if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()))
                    {
                        value = parameterObject;
                    }
                    else
                    {
                        if (boundSql.hasAdditionalParameter(propertyName)) {
                            value = boundSql.getAdditionalParameter(propertyName);
                        } else if ((propertyName.startsWith("__frch_")) && (boundSql.hasAdditionalParameter(prop.getName())))
                        {
                            value = boundSql.getAdditionalParameter(prop.getName());
                            if (value != null) {
                                value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                            }

                        }
                        else
                        {
                            value = metaObject == null ? null : metaObject.getValue(propertyName);
                        }
                    }
                }
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                if (typeHandler == null) {
                    throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                }

                typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
            }
        }
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
