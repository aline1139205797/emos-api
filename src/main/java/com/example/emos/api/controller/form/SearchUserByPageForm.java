package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Schema(description = "查询用户分页记录表单")
public class SearchUserByPageForm {
    @NotNull(message = "pageIndex不能为空")
    @Min(value = 1, message = "pageIndex不能小于1")
    @Schema(description = "页数")
    private Integer pageIndex;

    @NotNull(message = "pageSize不能为空")
    @Range(min = 10, max = 50, message = "pageSize必须在10~50之间")
    @Schema(description = "每页条数")
    private Integer pageSize;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,10}$",message = "name内容不正确")
    @Schema(description = "姓名")
    private String name;

    @Pattern(regexp = "^男$|^女$",message = "sex内容不正确")
    @Schema(description = "性别")
    private String sex;

    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{2,10}$",message = "role内容不正确")
    @Schema(description = "角色")
    private String role;

    @Min(value = 1,message = "dept不能小于1")
    private Integer deptId;

    @Min(value = 1,message = "status不能小于1")
    private Integer status;
}
