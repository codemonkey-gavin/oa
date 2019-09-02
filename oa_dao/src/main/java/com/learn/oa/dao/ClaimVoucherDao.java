package com.learn.oa.dao;

import com.learn.oa.entity.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dlaimVoucherDao")
public interface ClaimVoucherDao {
    void insert(ClaimVoucher claimVoucher);

    void update(ClaimVoucher claimVoucher);

    void delete(Integer id);

    ClaimVoucher select(Integer id);

    List<ClaimVoucher> selectByCreateSn(String sn);

    List<ClaimVoucher> selectByNextDealSn(String sn);
}
