package com.kundy.excelutils.service.impl;

import com.kundy.excelutils.constant.ExcelFormat;
import com.kundy.excelutils.entity.ExcelHeaderInfo;
import com.kundy.excelutils.entity.po.TtlProductInfoPo;
import com.kundy.excelutils.mapper.TtlProductInfoMapper;
import com.kundy.excelutils.service.TtlProductInfoService;
import com.kundy.excelutils.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author kundy
 * @create 2019/2/16 11:22 AM
 */
@Service
@Slf4j
public class TtlProductInfoServiceImpl implements TtlProductInfoService {

    // 每个线程导出记录最大行数
    private static final int THREAD_MAX_ROW = 20000;

    @Autowired
    private TtlProductInfoMapper mapper;
    @Autowired
    private TtlProductInfoMapper ttlProductInfoMapper;

    @Override
    public List<TtlProductInfoPo> listProduct(Map<String, Object> map) {
        return this.mapper.listProduct(map);
    }

    @Override
    public void export(HttpServletResponse response, String fileName) {
        // 待导出数据
        List<TtlProductInfoPo> productInfoPos = this.multiThreadListProduct();
        ExcelUtils excelUtils = new ExcelUtils(productInfoPos, getHeaderInfo(), getFormatInfo());
        excelUtils.sendHttpResponse(response, fileName, excelUtils.getWorkbook());
    }

    @Override
    public int insertTtlProductInfoPo() {
        int su=0;
        try {
            for(int i=1; i<=1000; i++){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String date = sdf.format(new Date());
                TtlProductInfoPo ttlProductInfoPo = new TtlProductInfoPo();
                ttlProductInfoPo.setBranchName("品牌名称"+i);
                Long num = (long)i;
                ttlProductInfoPo.setBranchId(num);
                ttlProductInfoPo.setCategoryId(num);
                ttlProductInfoPo.setCategoryName("类型名称"+i);
                ttlProductInfoPo.setCreateTime(date);
                ttlProductInfoPo.setIsDel("0");
                ttlProductInfoPo.setPrice(new BigDecimal("1000"));
                ttlProductInfoPo.setShopName("商品名称"+i);
                ttlProductInfoPo.setSalesNum(100);
                ttlProductInfoPo.setStock(11);
                ttlProductInfoPo.setUpdateTime(date);
                ttlProductInfoPo.setProductName("产品名称"+i);
                ttlProductInfoPo.setShopId(num);
                int number=ttlProductInfoMapper.insertTtlProductInfoPo(ttlProductInfoPo);
                if(number>0){
                    su++;
                }
            }
        }catch (Exception e){
            log.info("error:",e);
        }
        return su;
    }

    // 获取表头信息
    private List<ExcelHeaderInfo> getHeaderInfo() {
        return Arrays.asList(
                new ExcelHeaderInfo(1, 1, 0, 0, "id"),
                new ExcelHeaderInfo(1, 1, 1, 1, "商品名称"),

                new ExcelHeaderInfo(0, 0, 2, 3, "分类"),
                new ExcelHeaderInfo(1, 1, 2, 2, "类型ID"),
                new ExcelHeaderInfo(1, 1, 3, 3, "分类名称"),

                new ExcelHeaderInfo(0, 0, 4, 5, "品牌"),
                new ExcelHeaderInfo(1, 1, 4, 4, "品牌ID"),
                new ExcelHeaderInfo(1, 1, 5, 5, "品牌名称"),

                new ExcelHeaderInfo(0, 0, 6, 7, "商店"),
                new ExcelHeaderInfo(1, 1, 6, 6, "商店ID"),
                new ExcelHeaderInfo(1, 1, 7, 7, "商店名称"),

                new ExcelHeaderInfo(1, 1, 8, 8, "价格"),
                new ExcelHeaderInfo(1, 1, 9, 9, "库存"),
                new ExcelHeaderInfo(1, 1, 10, 10, "销量"),
                new ExcelHeaderInfo(1, 1, 11, 11, "插入时间"),
                new ExcelHeaderInfo(1, 1, 12, 12, "更新时间"),
                new ExcelHeaderInfo(1, 1, 13, 13, "记录是否已经删除")
        );
    }

    // 获取格式化信息
    private Map<String, ExcelFormat> getFormatInfo() {
        Map<String, ExcelFormat> format = new HashMap<>();
        format.put("id", ExcelFormat.FORMAT_INTEGER);
        format.put("categoryId", ExcelFormat.FORMAT_INTEGER);
        format.put("branchId",   ExcelFormat.FORMAT_INTEGER);
        format.put("shopId", ExcelFormat.FORMAT_INTEGER);
        format.put("price", ExcelFormat.FORMAT_DOUBLE);
        format.put("stock", ExcelFormat.FORMAT_INTEGER);
        format.put("salesNum", ExcelFormat.FORMAT_INTEGER);
        format.put("isDel", ExcelFormat.FORMAT_INTEGER);
        return format;
    }

    // 多线程查询报表
    private List<TtlProductInfoPo> multiThreadListProduct() {
        List<FutureTask<List<TtlProductInfoPo>>> tasks = new ArrayList<>();
        List<TtlProductInfoPo> productInfoPos = new ArrayList<>();

        int totalNum = 500000;
        int loopNum = new Double(Math.ceil((double) totalNum / THREAD_MAX_ROW)).intValue();
        log.info("多线程查询，总数：{},开启线程数：{}", totalNum, loopNum);
        long start = System.currentTimeMillis();

        executeTask(tasks, loopNum, totalNum);

        for (FutureTask<List<TtlProductInfoPo>> task : tasks) {
            try {
                productInfoPos.addAll(task.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("查询结束，耗时:{}", System.currentTimeMillis() - start);
        return productInfoPos;
    }

    // 执行查询任务
    private void executeTask(List<FutureTask<List<TtlProductInfoPo>>> tasks, int loopNum, int total) {
        for (int i = 0; i < loopNum; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("offset", i * THREAD_MAX_ROW);
            if (i == loopNum - 1) {
                map.put("limit", total - THREAD_MAX_ROW * i);
            } else {
                map.put("limit", THREAD_MAX_ROW);
            }
            FutureTask<List<TtlProductInfoPo>> task = new FutureTask<>(new listThread(map));
            log.info("开始查询第{}条开始的{}条记录", i * THREAD_MAX_ROW, THREAD_MAX_ROW);
            new Thread(task).start();
            // 将任务添加到tasks列表中
            tasks.add(task);
        }
    }

    private class listThread implements Callable<List<TtlProductInfoPo>> {

        private Map<String, Object> map;

        private listThread(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public List<TtlProductInfoPo> call() {
            return listProduct(map);
        }
    }

}
