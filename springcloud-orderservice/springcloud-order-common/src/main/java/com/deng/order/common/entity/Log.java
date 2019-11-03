package com.deng.order.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 11:49:05 AM 

* 类说明 

*/
@Data
@TableName("log")
public class Log  implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 方法操作名称
     */
    private String name;

    /**
     * 日志类型 0登陆日志 1操作日志
     */
    private Integer logType;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求用户
     */
    private String username;

    /**
     * ip
     */
    private String ip;

    /**
     * ip信息
     */
    private String ipInfo;

    /**
     * 花费时间
     */
    private Integer costTime;

    /**
     * 删除状态：1删除，0未删除
     */
    @TableField(value = "del_flag")
    @TableLogic
    private Integer delFlag = 0;

    /**
     * 创建人用户名
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
