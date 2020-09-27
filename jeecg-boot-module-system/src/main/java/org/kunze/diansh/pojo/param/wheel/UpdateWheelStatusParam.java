package org.kunze.diansh.pojo.param.wheel;

import lombok.Data;

import java.util.List;

/**
 * 修改轮播图状态参数实体
 *
 * @author 姜伟
 * @date 2020/7/24
 */
@Data
public class UpdateWheelStatusParam {
    /**
     * 状态 0：开启 1：关闭
     */
    private String status;

    /**
     * 修改人
     */
    private String updateName;

    /**
     * 轮播图Id
     */
    private List<String> wheelIdList;
}
