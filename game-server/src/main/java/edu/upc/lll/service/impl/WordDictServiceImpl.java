package edu.upc.lll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.upc.lll.mapper.WordDictMapper;
import edu.upc.lll.pojo.WordDict;
import edu.upc.lll.service.WordDictService;
import org.springframework.stereotype.Service;

@Service
public class WordDictServiceImpl extends ServiceImpl<WordDictMapper, WordDict> implements WordDictService {
}