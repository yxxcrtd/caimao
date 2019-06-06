<#if package?? && 0 < package?length>package ${s!};</#if>

import ${package!}.${classNameEntity!?cap_first};
import ${package!}.QueryBase;

import java.io.Serializable;

/**
* ${classNameEntity!?cap_first} 查询请求对象
*
* Created by ${author!} on ${time?string("yyyy-MM-dd HH:mm:ss")} ${time?string("EEEE")}
*/
public class FQuery${className!?cap_first}Req extends QueryBase<${classNameEntity!?cap_first}> implements Serializable {
    // TODO 这里的字段需要根据业务需求删除，当前提供的是全字段
<#if fields?? && 0 < fields?size>
  <#list fields as f>
    private ${f.fieldType!} ${f.fieldName!};
  </#list>
</#if>

<#if fields?? && 0 < fields?size>
  <#list fields as f>
    public ${f.fieldType!} get${f.fieldName!?cap_first}() {
        return ${f.fieldName!};
    }

    public void set${f.fieldName?cap_first}(${f.fieldType!} ${f.fieldName!}) {
        this.${f.fieldName!} = ${f.fieldName!};
    }

  </#list>
</#if>
}