package edu.upc.lll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.upc.lll.pojo.WordDict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WordDictMapper extends BaseMapper<WordDict> {
}