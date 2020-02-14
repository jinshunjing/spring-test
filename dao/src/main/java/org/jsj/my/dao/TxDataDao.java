package org.jsj.my.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jsj.dal.dao.TTxDataMapper;
import org.jsj.dal.model.TTxData;
import org.jsj.dal.model.TTxDataExample;
import org.jsj.my.model.TxData;
import org.jsj.my.util.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JSJ
 */
@Slf4j
@Component
public class TxDataDao {
    @Resource
    private TTxDataMapper txDataMapper;

    /**
     * 分页查询
     *
     * @param address
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<TxData> pageByAddress(String address, int pageNo, int pageSize) {
        TTxDataExample example = new TTxDataExample();
        example.createCriteria().andAddressEqualTo(address);
        return page(example, "create_time desc", pageNo, pageSize);
    }

    private TxData query(TTxDataExample example) {
        List<TTxData> records = txDataMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        return toBean(records.get(0));
    }

    private List<TxData> list(TTxDataExample example) {
        List<TTxData> records = txDataMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        return records.stream().map(this::toBean).collect(Collectors.toList());
    }

    private Page<TxData> page(TTxDataExample example, String orderByClause,
                              int pageNo, int pageSize) {
        // 记录总数
        int count = (int) txDataMapper.countByExample(example);
        if (count < 1) {
            return Page.empty();
        }

        // 验证分页参数
        boolean isPage = true;
        if (0 >= pageSize) {
            isPage = false;
        }
        if (isPage) {
            int pageMax = ((count - 1) / pageSize) + 1;
            if (0 >= pageNo || pageNo > pageMax) {
                // 越界
                return Page.empty();
            }
        }
        if (isPage) {
            example.setOffset((pageNo - 1) * pageSize);
            example.setLimit(pageSize);
        }

        // 查询记录
        example.setOrderByClause("create_time desc");
        List<TTxData> records = txDataMapper.selectByExample(example);
        if (records.isEmpty()) {
            return Page.empty();
        }
        List<TxData> beanList = records.stream().map(this::toBean).collect(Collectors.toList());

        // 返回结果
        if (!isPage) {
            count = beanList.size();
        }
        return Page.of(beanList, count);
    }

    private int save(TTxData record) {
        int rows = txDataMapper.insertSelective(record);
        log.info("Saved {} record: {}", rows, JSON.toJSONString(record));
        return rows;
    }

    private int update(TTxDataExample example, TTxData record) {
        int rows = txDataMapper.updateByExampleSelective(record, example);
        log.info("Updated {} record: {}", rows, JSON.toJSONString(record));
        return rows;
    }

    public int update(TTxData record) {
        int rows = txDataMapper.updateByPrimaryKeySelective(record);
        log.info("Updated {} record: {}", rows, JSON.toJSONString(record));
        return rows;
    }

    private TTxData toRecord(TxData bean) {
        return null;
    }

    private TxData toBean(TTxData record) {
        return null;
    }
}
