package org.jsj.my.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息接收器
 *
 * @author JSJ
 */
@Data
public class MessageAcceptor {

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "tab页类型 1:我的收藏 2:24小时涨跌幅排序")
    private Integer tabType;

    @ApiModelProperty(value = "排序类型,ASC:正序排序 DESC:倒序排序 目前只用于24小时涨跌幅")
    private String orderBy;

    @ApiModelProperty(value = "货币大写简称")
    private String name;

    @ApiModelProperty(value = "分页个数")
    private Integer limit;

    @ApiModelProperty(value = "分页起点位置")
    private Integer offset;

    @ApiModelProperty(value = "匿名用户")
    private boolean anonymous;

    @ApiModelProperty(value = "收藏的币种列表")
    private String[] coinNames;

}
