package com.learn.oa.biz;

import com.learn.oa.entity.ClaimVoucher;
import com.learn.oa.entity.ClaimVoucherItem;
import com.learn.oa.entity.DealRecord;

import java.util.List;

public interface ClaimVoucherBiz {
    void add(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);
    void edit(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);
    ClaimVoucher getClaimVoucher(Integer id);
    List<ClaimVoucherItem> getClaimVoucherItems(Integer cvid);
    List<DealRecord> getDealRecords(Integer cvid);
    List<ClaimVoucher> getByCreateSn(String sn);
    List<ClaimVoucher> getByNextDealSn(String sn);
    void submit(Integer id);
    void check(DealRecord dealRecord);
}
