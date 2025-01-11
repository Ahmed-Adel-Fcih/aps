package com.qeema.aps.application.port.out;

import com.qeema.aps.domain.refund.Refund;

public interface RefundPort {
    void processRefund(Refund refund);
}
