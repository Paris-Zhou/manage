package com.zhoupeng.modules.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.model.BusProduct;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:34
 */
@Mapper
public interface BusProductMapper extends BaseMapper<BusProduct> {
    BusProduct selectById(@Param("id") Integer id);

    List<ProductSimpleDTO> selectProductOptions(@Param("keyword") String keyword);
}
