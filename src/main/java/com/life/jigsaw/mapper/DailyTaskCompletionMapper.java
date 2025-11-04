package com.life.jigsaw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.life.jigsaw.domain.DailyTaskCompletion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 每日任务完成记录Mapper接口
 */
@Mapper
public interface DailyTaskCompletionMapper extends BaseMapper<DailyTaskCompletion> {
}
