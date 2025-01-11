package com.qeema.aps.application.port.in;

import java.util.List;

import com.qeema.aps.domain.refund.Refund;

public interface ProcessRefundUseCase {
    void processRefund(Refund refund);

    List<Refund> getRefundList(Refund refund);
}
