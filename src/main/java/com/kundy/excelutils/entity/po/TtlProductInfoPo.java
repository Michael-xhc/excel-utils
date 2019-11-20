package com.kundy.excelutils.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.IdGenerator;

import java.math.BigDecimal;

/**
 * @author kundy
 * @create 2019/2/15 11:22 PM
 *
 */
@Data
@Accessors(chain = true)
public class TtlProductInfoPo {

    private Long id;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long branchId;
    private String branchName;
    private Long shopId;
    private String shopName;
    private BigDecimal price;
    private Integer stock;
    private Integer salesNum;
    private String createTime;
    private String updateTime;
    private String isDel;
}
