package com.edukg.open.model;

import lombok.Data;

import java.util.List;

/**
 * 教材构建图谱需要，列表中每个字典为一个章节
 *
 * @author tanzheng
 * @date 2022/10/12
 */
@Data
public class Termdef {

    String name;

    String content;

    List<String> termdef;

    List<Termdef> children;

    boolean isLeaf;
}
