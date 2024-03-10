package cn.gracias.chatgpt.sdk.domain.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineItem {

    /** 模型 */
    private String name;

    /** 金额 */
    private BigDecimal cost;

}
