<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${className}">

    <!-- 表所有字段和属性 -->
    <resultMap id="BaseResultMap" type="${package}.${className}Entity" >
        <id column="id" property="id" jdbcType="BIGINT" />
  <#if fields?? && 0 < fields?size>
    <#list fields as f>
      <#if ("id" != f.fieldName!)>
        <result column="${f.fieldNameOriginal!}" property="${f.fieldName!}" jdbcType="${f.fieldTypeOriginal!?upper_case}" />
      </#if>
    </#list>
  </#if>
    </resultMap>

    <!-- 表所有字段 -->
    <sql id="Base_Column_List" >
<#if fields?? && 0 < fields?size>
    <#list fields as f>
        ${f.fieldNameOriginal!}<#if f_has_next>, </#if>
    </#list>
</#if>
    </sql>

    <!-- 根据ID查找 -->
    <select id="selectById" resultMap="BaseResultMap" parameterType="java.lang.Long" >
        select <include refid="Base_Column_List" /> from ${tableName} where id = ${r"#{id, jdbcType=BIGINT}"}
    </select>

    <!-- 根据ID删除 -->
    <delete id="deleteById" parameterType="java.lang.Long" >
        delete from ${tableName} where id = ${r"#{id, jdbcType=BIGINT}"}
    </delete>

    <!-- 分页查询 -->
    <select id="query${className!?cap_first}WithPage" resultMap="BaseResultMap" parameterType="${s!}.FQuery${className!?cap_first}Req" >
        select <include refid="Base_Column_List" /> from ${tableName}
        <trim prefix="WHERE" prefixOverrides="AND | OR">
            <if test="categoryId !=null and categoryId != ''">
                AND category_id = ${r"#{categoryId}"}
            </if>
        </trim>
        ORDER BY <include refid="Base_Column_List" /> DESC
    </select>

    <!-- 插入 -->
    <insert id="insert" parameterType="${package}.${className}Entity">
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#if fields?? && 0 < fields?size>
            <#list fields as f>
            <if test="${f.fieldName!} != null">
                ${f.fieldNameOriginal!},
            </if>
            </#list>
        </#if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
    <#if fields?? && 0 < fields?size>
        <#list fields as f>
            <if test="${f.fieldName!} != null">
                ${r"#{"}${f.fieldName!}, jdbcType=<#if ("id" == f.fieldName!)>BIGINT<#else>${f.fieldTypeOriginal!?upper_case}</#if>},
            </if>
        </#list>
    </#if>
        </trim>
    </insert>

    <!-- 修改 -->
    <update id="update" parameterType="${package}.${className}Entity">
        update ${tableName}
        <set>
<#if fields?? && 0 < fields?size>
    <#list fields as f>
        <#if ("id" != f.fieldName!)>
            <if test="${f.fieldName!} != null">
                ${f.fieldNameOriginal!} = ${r"#{"}${f.fieldName!}, jdbcType=${f.fieldTypeOriginal!?upper_case}},
            </if>
        </#if>
    </#list>
</#if>
        </set>
        where id = ${r"#{"}id, jdbcType=BIGINT}
    </update>

</mapper>