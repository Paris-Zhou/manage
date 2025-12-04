package com.zhoupeng.modules.bus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhoupeng.common.api.CommonResult;
import com.zhoupeng.modules.bus.dto.BusProductPageQueryDTO;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.dto.ProposalCreateDTO;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.model.BusProduct;
import com.zhoupeng.modules.bus.service.BusProposalService;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;
import com.zhoupeng.security.util.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:14:01
 */
@Slf4j
@RestController
@RequestMapping("/api/proposals")
public class BusProposalController {
    @Resource
    private BusProposalService busProposalService;

    /**
     * 计划书分页查询
     */
    @PostMapping("/page")
    public CommonResult<IPage<ProposalPageVO>> page(@RequestBody ProposalPageQueryDTO queryDTO) {
        return CommonResult.success(busProposalService.pageProposals(queryDTO));

    }

    /**
     * 新增计划书
     * 前端要先调用 `/api/proposals/products` 拿产品列表，下拉选择一个 productId，一起提交
     */
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody ProposalCreateDTO dto) {
        busProposalService.createProposal(dto);
        return CommonResult.success(null);
    }

    /**
     * 获取产品下拉列表（新增计划书时调用）
     */
    @PostMapping("/products")
    public CommonResult<List<ProductSimpleDTO>> listProductOptions(@RequestParam(required = false) String keyword) {
        return CommonResult.success(busProposalService.listProductOptions(keyword));
    }
    @ApiOperation("分页查询产品列表")
    @PostMapping("/page2")
    public CommonResult<IPage<BusProduct>> page(@RequestBody BusProductPageQueryDTO queryDTO) {
        IPage<BusProduct> page = busProposalService.pageProducts(queryDTO);
        return CommonResult.success(page);
    }
}
