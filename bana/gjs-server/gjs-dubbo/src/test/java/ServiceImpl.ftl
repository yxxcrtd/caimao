<#if package?? && 0 < package?length>package ${s!};</#if>

import ${package!}.${classNameEntity!?cap_first};
import ${package!}.req.FQuery${className!?cap_first}Req;
import ${package1!}.service.I${className!?cap_first}Service;
import ${s1!}.${className!?cap_first}DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* ${classNameEntity!?cap_first} 服务接口实现
*
* Created by ${author!} on ${time?string("yyyy-MM-dd HH:mm:ss")} ${time?string("EEEE")}
*/
@Service("${className!?uncap_first}Service")
public class I${className!?cap_first}ServiceImpl implements I${className!?cap_first}Service {

    @Autowired
    private ${className!?cap_first}DAO ${className!?uncap_first}DAO;

   /**
    * 查询${className!?cap_first}列表
    *
    * @param req
    * @return
    * @throws Exception
    */
    @Override
    public FQuery${className!?cap_first}Req query${className!?cap_first}List(FQuery${className!?cap_first}Req req) throws Exception {
//        if (req.getDateStart() != null) req.setDateStart(req.getDateStart() + " 00:00:00");
//        if (req.getDateEnd() != null) req.setDateEnd(req.getDateEnd() + " 23:59:59");
//        if (null != req.getCategoryId()) { req.setCategoryId(req.getCategoryId()); }
//        if (null != req.getIsHot()) { req.setIsHot(req.getIsHot()); }
        List<${classNameEntity!?cap_first}> list = this.${className!?uncap_first}DAO.query${className!?cap_first}WithPage(req);
        return req;
    }

   /**
    * 查询指定${className!?cap_first}内容
    *
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    public ${classNameEntity!?cap_first} selectById(Long id) throws Exception {
        return this.${className!?uncap_first}DAO.selectById(id);
    }

   /**
    * 添加${className!?cap_first}的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void insert(${classNameEntity!?cap_first} entity) throws Exception {
        this.${className!?uncap_first}DAO.insert(entity);
    }

   /**
    * 删除${className!?cap_first}的接口
    *
    * @param id
    * @throws Exception
    */
    @Override
    public void deleteById(Long id) throws Exception {
        this.${className!?uncap_first}DAO.deleteById(id);
    }

   /**
    * 更新${className!?cap_first}的接口
    *
    * @param entity
    * @throws Exception
    */
    @Override
    public void update(${classNameEntity!?cap_first} entity) throws Exception {
        this.${className!?uncap_first}DAO.update(entity);
    }

}
