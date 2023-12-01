package com.edukg.open.model;

import lombok.Data;

import java.util.List;

/**
 * 教材构建图谱所需章节结构
 *
 * @author tanzheng
 * @date 2022/10/12
 */

@Data
public class Partition {

    String title;

    List<Partition> children;

    Boolean isLeaf;
}
