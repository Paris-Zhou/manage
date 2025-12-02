package com.zhoupeng.modules.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoupeng.common.exception.ApiException;
import com.zhoupeng.modules.bus.dto.BusProductPageQueryDTO;
import com.zhoupeng.modules.bus.dto.ProductSimpleDTO;
import com.zhoupeng.modules.bus.dto.ProposalCreateDTO;
import com.zhoupeng.modules.bus.dto.ProposalPageQueryDTO;
import com.zhoupeng.modules.bus.mapper.BusProductMapper;
import com.zhoupeng.modules.bus.mapper.BusProposalMapper;
import com.zhoupeng.modules.bus.model.BusProduct;
import com.zhoupeng.modules.bus.model.BusProposal;
import com.zhoupeng.modules.bus.service.BusProposalService;
import com.zhoupeng.modules.bus.vo.ProposalPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:51
 */
@Slf4j
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
    private static final DateTimeFormatter BIRTHDAY_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional
    public void createProposal(ProposalCreateDTO dto, String currentUserId) {
        BusProposal entity = new BusProposal();
        // 先补全年龄和生日
        fillAgeAndBirthday(dto);
        entity.setProductId(dto.getProductId());
        entity.setType(dto.getType());
        entity.setMoney(dto.getMoney());
        entity.setRelation(dto.getRelation());

        entity.setInsuredName(dto.getInsuredName());

//        entity.setInsuredAge(dto.getInsuredAge());
//        entity.setInsuredBirthday(dto.getInsuredBirthday());

        entity.setInsuredGender(dto.getInsuredGender());
        entity.setInsuredLocation(dto.getInsuredLocation());
        entity.setInsuredHabit(dto.getInsuredHabit());

        entity.setRemark(dto.getRemark());
        entity.setStatus("正常");
        entity.setDeleted(0);

        entity.setCreateTime(LocalDateTime.now());
        entity.setCreateBy(currentUserId);
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 简单生成一个计划书编号，你后面可以换成自己的规则
        entity.setCode("P" +format);

        busProposalMapper.insert(entity);
        // 异步执行 Playwright，不阻塞当前请求
        new Thread(() -> {
            try {
                PlaywrightLoginDemo.show(entity.getCode());
            } catch (Exception e) {
                // 这里最好加点日志，避免线程悄悄挂了
                log.error(e.getMessage());
            }
        }).start();
    }
    /**
     * 补全年龄和生日：
     * - 有生日没年龄：根据生日计算年龄
     * - 有年龄没生日：根据年龄估算生日（按今天减去age年）
     * - 两个都有：以生日为准重新计算年龄
     */
    private void fillAgeAndBirthday(ProposalCreateDTO dto) {
        String ageStr = dto.getInsuredAge();
        String birthdayStr = dto.getInsuredBirthday();

        boolean hasAge = StringUtils.isNotBlank(ageStr);
        boolean hasBirthday = StringUtils.isNotBlank(birthdayStr);

        if (!hasAge && !hasBirthday) {
            // 两个都没有，看你的业务决定是允许为空还是直接抛错
            // throw new BusinessException("年龄或生日至少填一个");
            return;
        }

        LocalDate today = LocalDate.now();

        if (hasBirthday) {
            // 1. 有生日：根据生日算年龄，覆盖/补全年龄
            LocalDate birth;
            try {
                birth = LocalDate.parse(birthdayStr, BIRTHDAY_FMT);
            } catch (Exception e) {
                throw new ApiException("生日格式不正确，应为yyyy-MM-dd");
            }
            if (birth.isAfter(today)) {
                throw new ApiException("生日不能大于当前日期");
            }
            int age = Period.between(birth, today).getYears();
            if (age < 0) {
                age = 0;
            }
            dto.setInsuredAge(String.valueOf(age));
        } else if (hasAge) {
            // 2. 只有年龄：根据年龄估算生日
            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                throw new ApiException("年龄必须为数字");
            }
            if (age < 0) {
                throw new ApiException("年龄不能为负数");
            }

            // 假定生日为“今天减去 age 年”，月份和日期用今天的
            // 例如今天是 2025-12-02，年龄=30，则生日估算为 1995-12-02
            LocalDate birth = today.minusYears(age);
            dto.setInsuredBirthday(birth.format(BIRTHDAY_FMT));
        }
    }
    @Override
    public List<ProductSimpleDTO> listProductOptions(String keyword) {
        return busProductMapper.selectProductOptions(keyword);
    }

    @Override
    public IPage<BusProduct> pageProducts(BusProductPageQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() == null ? 1 : queryDTO.getPageNum();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();

        Page<BusProduct> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<BusProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BusProduct::getDeleted, "0"); // 只查未删除

        if (StringUtils.isNotBlank(queryDTO.getName())) {
            wrapper.like(BusProduct::getName, queryDTO.getName());
        }
        if (StringUtils.isNotBlank(queryDTO.getCode())) {
            wrapper.like(BusProduct::getCode, queryDTO.getCode());
        }
        if (StringUtils.isNotBlank(queryDTO.getType())) {
            wrapper.like(BusProduct::getType, queryDTO.getType());
        }
        if (StringUtils.isNotBlank(queryDTO.getCompany())) {
            wrapper.like(BusProduct::getCompany, queryDTO.getCompany());
        }

        wrapper.orderByDesc(BusProduct::getCreateTime);

        return busProductMapper.selectPage(page, wrapper);
    }
}
