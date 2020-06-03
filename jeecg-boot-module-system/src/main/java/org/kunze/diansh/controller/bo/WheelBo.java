package org.kunze.diansh.controller.bo;

import lombok.Data;
import org.kunze.diansh.controller.vo.WheelVo;

import java.io.Serializable;

@Data
public class WheelBo extends WheelVo implements Serializable {

    /**图片名称*/
  //  private String weelName;

    /**轮播图片所展示的端*/
  //  private String wheelPort;

    /**轮播图类型*/
    private String wheelIsflag;
}
