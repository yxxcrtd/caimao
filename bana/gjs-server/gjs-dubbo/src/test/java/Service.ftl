<#if package?? && 0 < package?length>package ${s!};</#if>

import ${package!}.${classNameEntity!?cap_first};
import ${package!}.req.FQuery${className!?cap_first}Req;

/**
* ${classNameEntity!?cap_first} 服务接口
*
* Created by ${author!} on ${time?string("yyyy-MM-dd HH:mm:ss")} ${time?string("EEEE")}
*/
public interface I${className!?cap_first}Service {

   /**
    * 查询${entity!?cap_first}列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    FQuery${className!?cap_first}Req query${className!?cap_first}List(FQuery${className!?cap_first}Req req) throws Exception;

   /**
    * 查询指定${entity!?cap_first}内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    ${classNameEntity!?cap_first} selectById(Long id) throws Exception;

   /**
    * 添加${entity!?cap_first}的接口
    *
    * @param entity
    * @throws Exception
    */
    void insert(${classNameEntity!?cap_first} entity) throws Exception;

   /**
    * 删除${entity!?cap_first}的接口
    *
    * @param id
    * @throws Exception
    */
    void deleteById(Long id) throws Exception;

   /**
    * 更新${entity!?cap_first}的接口
    *
    * @param entity
    * @throws Exception
    */
    void update(${classNameEntity!?cap_first} entity) throws Exception;

}
