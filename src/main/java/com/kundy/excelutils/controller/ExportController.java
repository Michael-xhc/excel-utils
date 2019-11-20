package com.kundy.excelutils.controller;

import com.kundy.excelutils.entity.po.TtlProductInfoPo;
import com.kundy.excelutils.mapper.TtlProductInfoMapper;
import com.kundy.excelutils.service.TtlProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author kundy
 * @create 2019/2/13 9:06 PM
 */

@Controller
@RequestMapping("/excelUtils")
public class ExportController {

    @Autowired
    private TtlProductInfoService productInfoService;
    @Autowired
    private TtlProductInfoService ttlProductInfoService;

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        this.productInfoService.export(response, "商品信息" + new Random().nextInt(1000));
    }

    @GetMapping("/insert")
    @ResponseBody
    public String insert() {
        int num = this.ttlProductInfoService.insertTtlProductInfoPo();
        return "总共插入"+num+"条";
    }

//    public  class MyRunnable implements Runnable{
//
//        @Override
//        public void run() {
//            for(int i=1; i<=1000; i++){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                String date = sdf.format(new Date());
//                TtlProductInfoPo ttlProductInfoPo = new TtlProductInfoPo();
//                ttlProductInfoPo.setBranchName("品牌名称"+i);
//                Long num = (long)i;
//                ttlProductInfoPo.setBranchId(num);
//                ttlProductInfoPo.setCategoryId(num);
//                ttlProductInfoPo.setCategoryName("类型名称"+i);
//                ttlProductInfoPo.setCreateTime(date);
//                ttlProductInfoPo.setIsDel("0");
//                ttlProductInfoPo.setPrice(new BigDecimal("1000"));
//                ttlProductInfoPo.setShopName("商品名称"+i);
//                ttlProductInfoPo.setSalesNum(100);
//                ttlProductInfoPo.setStock(11);
//                ttlProductInfoPo.setUpdateTime(date);
//                int infoPo = ttlProductInfoMapper.insertTtlProductInfoPo(ttlProductInfoPo);
//            }
//        }
//    }

//    public static void main(String[] args) {
//        MyRunnable myRunnable = new MyRunnable();
//        Thread thread = new Thread(myRunnable, "线程1");
//        Thread thread1 = new Thread(myRunnable, "线程2");
//        thread.start();
//        thread1.start();
//    }

//   public static class MyDemo extends Thread{
//        @Override
//        public void run(){
//            for(int i=0;i<1000;i++){
//                System.out.println(Thread.currentThread().getName()+ "---》"+i);
//            }
//        }
//   }

//    public static void main(String[] args) {
//        MyDemo myDemo = new MyDemo();
//        MyDemo myDemo1 = new MyDemo();
//
//        myDemo.setName("线程1");
//        myDemo1.setName("线程2");
//
//        myDemo.start();
//        myDemo1.start();
//    }

//    public  void main(String[] args) {
//        for(int i=1; i<=1000; i++){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            String date = sdf.format(new Date());
//            TtlProductInfoPo ttlProductInfoPo = new TtlProductInfoPo();
//            ttlProductInfoPo.setBranchName("品牌名称"+i);
//            Long num = (long)i;
//            ttlProductInfoPo.setBranchId(num);
//            ttlProductInfoPo.setCategoryId(num);
//            ttlProductInfoPo.setCategoryName("类型名称"+i);
//            ttlProductInfoPo.setCreateTime(date);
//            ttlProductInfoPo.setIsDel("0");
//            ttlProductInfoPo.setPrice(new BigDecimal("1000"));
//            ttlProductInfoPo.setShopName("商品名称"+i);
//            ttlProductInfoPo.setSalesNum(100);
//            ttlProductInfoPo.setStock(11);
//            ttlProductInfoPo.setUpdateTime(date);
//            int infoPo = ttlProductInfoMapper.insertTtlProductInfoPo(ttlProductInfoPo);
//        }
//    }

}
