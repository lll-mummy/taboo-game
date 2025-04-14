package edu.upc.lll.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("word_dict")
public class WordDict {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String word;
}