package com.caimao.account.api.entity.req;


import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 所有验证测试的
 * Created by WangXu on 2015/6/4.
 */
public class DemoValidate implements Serializable {

    /**
     * 参考URL ： http://docs.jboss.org/hibernate/validator/4.2/reference/zh-CN/html_single/
     */

    @AssertFalse(message = "这个值必须是 false")
    @AssertTrue(message = "这个值必须是 true")
    @DecimalMax(value = "10", message = "被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.")
    @DecimalMin(value = "1", message = "被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.")
    @Digits(integer = 2, fraction = 3, message = "整数与小数是否要求的位数")
    @Future(message = "检查给定的日期是否比现在晚.")
    @Past(message = "检查标注对象中的值表示的日期比当前早.")
    @Max(value = 10, message = "检查该值是否小于或等于约束条件中指定的最大值.")
    @Min(value = 1, message = "检查该值是否大于或等于约束条件中规定的最小值.")
    @NotNull(message = "这个值不能为null")
    @Null(message = "这个值必须为null")
    @Pattern(regexp = "[1-9]", message = "检查该字符串是否能够在match指定的情况下被regex定义的正则表达式匹配.")
    @Size(min = 1, max = 10, message = "值大小是否在最大最小之间（包括最大最小值） @ String, Collection, Map and arrays")
    @CreditCardNumber(message = "这个是不是一个信用卡的卡号")
    @Email(message = "邮箱格式是否正确")
    @Length(min = 2, max = 10, message = "字符串的长度是否在指定的大小上")
    @NotBlank(message = "检查注释字符串不是null,长度大于0。@NotEmpty不同的是,这个约束只能应用于字符串,尾随空格将被忽略。")
    @NotEmpty(message = "检查是否带注释的元素是不是空。")
    @Range(min = 1, max = 10, message = "检查是否带注释的值介于(包容)指定的最小值和最大值")
    @SafeHtml(message = "检查是否带注释的值包含潜在的恶意片段如<脚本/ >。为了使用这个约束,jsoup库必须在类路径的一部分。与whitelistType属性可以选择预定义的白名单类型。您还可以指定额外的html标记的白名单additionalTags属性。")
    @URL(message = "是否是一个URL值")
    @Valid // 递归的对关联对象进行校验, 如果关联对象是个集合或者数组, 那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验.

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
