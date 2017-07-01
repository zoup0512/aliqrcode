package com.zhoup.android.aliqrcode.module.model.bean;

import java.math.BigDecimal;

/**
 * Created by zhoup on 2017/6/25.
 */

public class AlipayAmountEvent {
    private BigDecimal amount;

    public AlipayAmountEvent(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
