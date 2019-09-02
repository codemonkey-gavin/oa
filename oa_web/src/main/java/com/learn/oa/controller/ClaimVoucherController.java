package com.learn.oa.controller;

import com.learn.oa.biz.ClaimVoucherBiz;
import com.learn.oa.dto.ClaimVoucherInfo;
import com.learn.oa.entity.ClaimVoucher;
import com.learn.oa.entity.DealRecord;
import com.learn.oa.entity.Employee;
import com.learn.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller("claimVoucherController")
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {

    @Autowired
    private ClaimVoucherBiz claimVoucherBiz;

    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map) {
        map.put("info", new ClaimVoucherInfo());
        map.put("items", Contant.getItems());
        return "/claimvoucher/claim_voucher_add";
    }

    @RequestMapping("/add")
    public String add(HttpSession session, ClaimVoucherInfo info) {
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherBiz.add(info.getClaimVoucher(), info.getItems());
        return "redirect:detail?id=" + info.getClaimVoucher().getId();
    }

    @RequestMapping("/detail")
    public String detail(int id, Map<String, Object> map) {
        map.put("claimVoucher", claimVoucherBiz.getClaimVoucher(id));
        map.put("claimVoucherItems", claimVoucherBiz.getClaimVoucherItems(id));
        map.put("dealRecords", claimVoucherBiz.getDealRecords(id));
        return "/claimvoucher/claim_voucher_detail";
    }

    @RequestMapping("/self")
    public String self(HttpSession session, Map<String, Object> map) {
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list", claimVoucherBiz.getByCreateSn(employee.getSn()));
        return "/claimvoucher/claim_voucher_self";
    }

    @RequestMapping("/deal")
    public String deal(HttpSession session, Map<String, Object> map) {
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list", claimVoucherBiz.getByNextDealSn(employee.getSn()));
        return "/claimvoucher/claim_voucher_deal";
    }

    @RequestMapping("/to_edit")
    public String toEdit(int id, Map<String, Object> map) {
        ClaimVoucherInfo info = new ClaimVoucherInfo();
        info.setClaimVoucher(claimVoucherBiz.getClaimVoucher(id));
        info.setItems(claimVoucherBiz.getClaimVoucherItems(id));
        map.put("info", info);
        map.put("items", Contant.getItems());
        return "/claimvoucher/claim_voucher_edit";
    }

    @RequestMapping("/edit")
    public String edit(HttpSession session, ClaimVoucherInfo info) {
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherBiz.add(info.getClaimVoucher(), info.getItems());
        return "redirect:detail?id=" + info.getClaimVoucher().getId();
    }

    @RequestMapping("/submit")
    public String submit(int id) {
        claimVoucherBiz.submit(id);
        return "redirect:claim_voucher_deal";
    }

    @RequestMapping("/to_check")
    public String toCheck(int id, Map<String, Object> map) {
        map.put("claimVoucher", claimVoucherBiz.getClaimVoucher(id));
        map.put("claimVoucherItems", claimVoucherBiz.getClaimVoucherItems(id));
        DealRecord dealRecord = new DealRecord();
        dealRecord.setClaimVoucherId(id);
        map.put("dealRecord", dealRecord);
        return "/claimvoucher/claim_voucher_check";
    }

    @RequestMapping("/check")
    public String check(HttpSession session, DealRecord dealRecord) {
        Employee employee = (Employee) session.getAttribute("employee");
        dealRecord.setDealSn(employee.getSn());
        claimVoucherBiz.check(dealRecord);
        return "/claimvoucher/claim_voucher_deal";
    }
}
