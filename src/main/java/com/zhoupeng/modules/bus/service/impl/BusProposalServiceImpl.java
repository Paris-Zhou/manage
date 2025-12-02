package com.zhoupeng.modules.bus.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.dto.ProposalCreateDTO;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.mapper.BusProductMapper;
import com.zhoupeng.modules.bus.mapper.BusProposalMapper;
import com.zhoupeng.modules.bus.model.BusProposal;
import com.zhoupeng.modules.bus.service.BusProposalService;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:51
 */
@Service
public class BusProposalServiceImpl implements BusProposalService {
    @Resource
    private BusProposalMapper busProposalMapper;

    @Resource
    private BusProductMapper busProductMapper;

    @Override
    public IPage<ProposalPageVO> pageProposals(ProposalPageQueryDTO queryDTO) {
        Page<ProposalPageVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return busProposalMapper.pageProposals(page, queryDTO);
    }

    @Override
    @Transactional
    public void createProposal(ProposalCreateDTO dto, String currentUserId) {
        BusProposal entity = new BusProposal();
        entity.setProductId(dto.getProductId());
        entity.setType(dto.getType());
        entity.setMoney(dto.getMoney());
        entity.setRelation(dto.getRelation());

        entity.setInsuredName(dto.getInsuredName());
        entity.setInsuredAge(dto.getInsuredAge());
        entity.setInsuredBirthday(dto.getInsuredBirthday());
        entity.setInsuredGender(dto.getInsuredGender());
        entity.setInsuredLocation(dto.getInsuredLocation());
        entity.setInsuredHabit(dto.getInsuredHabit());

        entity.setRemark(dto.getRemark());
        entity.setStatus("正常");
        entity.setDeleted(0);

        entity.setCreateTime(LocalDateTime.now());
        entity.setCreateBy(currentUserId);

        // 简单生成一个计划书编号，你后面可以换成自己的规则
        entity.setCode("P" + LocalDateTime.now().toLocalDate() + "-" + UUID.randomUUID().toString().substring(0, 8));

        busProposalMapper.insert(entity);
    }

    @Override
    public List<ProductSimpleDTO> listProductOptions(String keyword) {
        return busProductMapper.selectProductOptions(keyword);
    }
}
