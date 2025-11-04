package com.life.jigsaw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.life.jigsaw.domain.ScoreRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分记录Mapper接口
 */
@Mapper
public interface ScoreRecordMapper extends BaseMapper<ScoreRecord> {
}
