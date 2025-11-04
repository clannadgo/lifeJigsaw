package com.life.jigsaw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.life.jigsaw.domain.DailyTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 每日任务Mapper接口
 */
@Mapper
public interface DailyTaskMapper extends BaseMapper<DailyTask> {
}
