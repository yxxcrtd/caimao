<#if package?? && 0 < package?length>package ${s!};</#if>

import ${package!}.${classNameEntity!?cap_first};
import ${package!}.req.FQuery${className!?cap_first}Req;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* ${className!?cap_first}Entity DAO实现
*
* Created by ${author!} on ${time?string("yyyy-MM-dd HH:mm:ss")} ${time?string("EEEE")}
*/
@Repository
public class ${className!?cap_first}DAO extends SqlSessionDaoSupport {

   /**
    * 查询${className!?cap_first}列表
    *
    * @param req
    * @return
    */
    public List<${className!?cap_first}Entity> query${className!?cap_first}WithPage(FQuery${className!?cap_first}Req req) {
        return this.getSqlSession().selectList("${className!?cap_first}.query${className!?cap_first}WithPage", req);
    }

   /**
    * 查询指定${className!?cap_first}内容
    *
    * @param id
    * @return
    */
    public ${className!?cap_first}Entity selectById(Long id) {
        return this.getSqlSession().selectOne("${className!?cap_first}.selectById", id);
    }

   /**
    * 添加${className!?cap_first}的接口
    *
    * @param entity
    */
    public Integer insert(${className!?cap_first}Entity entity) {
        return this.getSqlSession().insert("${className!?cap_first}.insert", entity);
    }

   /**
    * 删除${className!?cap_first}的接口
    *
    * @param id
    */
    public Integer deleteById(Long id) {
        return this.getSqlSession().delete("${className!?cap_first}.deleteById", id);
    }

   /**
    * 更新${className!?cap_first}的接口
    *
    * @param entity
    */
    public Integer update(${className!?cap_first}Entity entity) {
        return this.getSqlSession().update("${className!?cap_first}.update", entity);
    }

}