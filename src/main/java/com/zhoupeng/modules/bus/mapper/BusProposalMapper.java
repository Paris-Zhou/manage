package com.zhoupeng.modules.bus.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.model.BusProposal;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:39
 */
@Mapper
public interface BusProposalMapper {
    int insert(BusProposal proposal);

    BusProposal selectById(@Param("id") Integer id);

    int countByCondition(@Param("q") ProposalPageQueryDTO query);


    IPage<ProposalPageVO> pageProposals(Page<?> page,@Param("q")  ProposalPageQueryDTO query);
}
