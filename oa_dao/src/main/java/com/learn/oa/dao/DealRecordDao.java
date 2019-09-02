package com.learn.oa.dao;

import com.learn.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dealRecordDao")
public interface DealRecordDao {
    void insert(DealRecord dealRecord);

    List<DealRecord> selectByClaimVoucherId(Integer cvid);
}
