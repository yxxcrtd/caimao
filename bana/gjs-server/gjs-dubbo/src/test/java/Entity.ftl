<#if package?? && 0 < package?length>package ${package!};</#if>

import java.io.Serializable;

/**
* ${classNameEntity!?cap_first} 实例对象
*
* Created by ${author!} on ${time?string("yyyy-MM-dd HH:mm:ss")} ${time?string("EEEE")}
*/
public class ${classNameEntity!} implements Serializable {

<#if fields?? && 0 < fields?size>
  <#list fields as f>
    /** ${f.fieldRemark} */
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