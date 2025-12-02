package com.zhoupeng.modules.bus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.dto.ProposalCreateDTO;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;

import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:50
 */
public interface BusProposalService {
    /**
     * 分页查询计划书
     */
    IPage<ProposalPageVO> pageProposals(ProposalPageQueryDTO queryDTO);

    /**
     * 新增计划书
     */
    void createProposal(ProposalCreateDTO dto, String currentUserId);

    /**
     * 获取产品下拉列表
     */
    List<ProductSimpleDTO> listProductOptions(String keyword);
}
