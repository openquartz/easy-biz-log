package com.openquartz.logserver.pojo;

import com.openquartz.javaobjdiff.annotation.DiffAlias;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author svnee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @DiffAlias(alias = "订单ID")
    private Long orderId;
    @DiffAlias(alias = "订单号")
    private String orderNo;
    private String purchaseName;
    private String productName;
    @DiffAlias(alias = "创建时间")
    private Date createTime;

    @DiffAlias(alias ="创建人")
    private UserDO creator;
    @DiffAlias(alias ="更新人")
    private UserDO updater;
    @DiffAlias(alias ="列表项")
    private List<String> items;

    @DiffAlias(alias = "拓展信息")
    private String[] extInfo;

    @Data
    public static class UserDO {
        @DiffAlias(alias = "用户ID")
        private Long userId;
        @DiffAlias(alias = "用户姓名")
        private String userName;
    }
}
