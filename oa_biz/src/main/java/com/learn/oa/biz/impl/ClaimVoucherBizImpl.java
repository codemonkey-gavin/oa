package com.learn.oa.biz.impl;

import com.learn.oa.biz.ClaimVoucherBiz;
import com.learn.oa.dao.ClaimVoucherDao;
import com.learn.oa.dao.ClaimVoucherItemDao;
import com.learn.oa.dao.DealRecordDao;
import com.learn.oa.dao.EmployeeDao;
import com.learn.oa.entity.ClaimVoucher;
import com.learn.oa.entity.ClaimVoucherItem;
import com.learn.oa.entity.DealRecord;
import com.learn.oa.entity.Employee;
import com.learn.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;
    @Autowired
    private EmployeeDao employeeDao;

    public void add(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATE);
        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }

    public void edit(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATE);
        claimVoucherDao.update(claimVoucher);

        List<ClaimVoucherItem> olds = claimVoucherItemDao.select(claimVoucher.getId());
        for (ClaimVoucherItem old : olds) {
            Boolean isHave = false;
            for (ClaimVoucherItem item : items) {
                if (old.getId() == item.getId()) {
                    isHave = true;
                    break;
                }
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId() != null && item.getId() > 0) {
                claimVoucherItemDao.update(item);
            } else {
                claimVoucherItemDao.insert(item);
            }
        }
    }

    public ClaimVoucher getClaimVoucher(Integer id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getClaimVoucherItems(Integer cvid) {
        return claimVoucherItemDao.select(cvid);
    }

    public List<DealRecord> getDealRecords(Integer cvid) {
        return dealRecordDao.selectByClaimVoucherId(cvid);
    }

    public List<ClaimVoucher> getByCreateSn(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getByNextDealSn(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void submit(Integer id) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_SUBMIT);
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Contant.POST_FM).get(0).getSn());
        claimVoucherDao.update(claimVoucher);

        DealRecord dealRecord = new DealRecord();
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setDealWay(Contant.DEAL_SUBMIT);
        dealRecord.setDealResult(Contant.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);
    }

    public void check(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());
        if (dealRecord.getDealWay().equals(Contant.DEAL_PASS)) {
            if (claimVoucher.getTotalAmount() <= Contant.LIMIT_CHECK || employee.getPost().equals(Contant.POST_GM)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_CASHIER).get(0).getSn());

                dealRecord.setDealResult(Contant.CLAIMVOUCHER_APPROVED);
            } else {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_GM).get(0).getSn());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_BACK)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_BACK);
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_REJECT)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_TERMINATED);
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_PAID)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_PAID);
            claimVoucher.setNextDealSn(null);
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
