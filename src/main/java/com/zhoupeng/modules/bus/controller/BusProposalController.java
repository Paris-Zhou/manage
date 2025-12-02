package com.zhoupeng.modules.bus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.dto.ProposalCreateDTO;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.service.BusProposalService;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:14:01
 */

@RestController
@RequestMapping("/api/proposals")
public class BusProposalController {
    @Resource
    private BusProposalService busProposalService;

    /**
     * 计划书分页查询
     */
    @PostMapping("/page")
    public IPage<ProposalPageVO> page(@RequestBody ProposalPageQueryDTO queryDTO) {
        return busProposalService.pageProposals(queryDTO);
    }

    /**
     * 新增计划书
     * 前端要先调用 `/api/proposals/products` 拿产品列表，下拉选择一个 productId，一起提交
     */
    @PostMapping
    public String create(@RequestBody ProposalCreateDTO dto) {
        String currentUserId = "1";
        busProposalService.createProposal(dto, currentUserId);
        return "OK";
    }

    /**
     * 获取产品下拉列表（新增计划书时调用）
     */
    @PostMapping("/products")
    public List<ProductSimpleDTO> listProductOptions(@RequestParam(required = false) String keyword) {
        return busProposalService.listProductOptions(keyword);
    }
}
