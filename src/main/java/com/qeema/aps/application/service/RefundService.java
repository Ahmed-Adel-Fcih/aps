package com.qeema.aps.application.service;

import java.util.List;

import com.qeema.aps.application.port.in.ProcessRefundUseCase;
import com.qeema.aps.application.port.out.RefundPort;
import com.qeema.aps.domain.refund.Refund;

public class RefundService implements ProcessRefundUseCase {

    private final RefundPort processRefundPort;

    public RefundService(RefundPort processRefundPort) {
        this.processRefundPort = processRefundPort;
    }

    @Override
    public void processRefund(Refund refund) {
        processRefundPort.processRefund(refund);
    }

    @Override
    public List<Refund> getRefundList(Refund refund) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRefundList'");
    }
}