package org.jsj.my.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author JSJ
 */
@Data
@Builder
public class MessageSender {

    @ApiModelProperty(value = "分页个数")
    private Integer limit;

    @ApiModelProperty(value = "分页起点位置")
    private Integer offset;

    @ApiModelProperty(value = "搜索货币时的输入内容")
    private String name;

    @ApiModelProperty(value = "分页数据")
    private Object data;

}
