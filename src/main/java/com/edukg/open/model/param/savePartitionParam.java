package com.edukg.open.model.param;

import com.edukg.open.model.Partition;
import lombok.Data;

import java.util.List;

/**
 * 教材构建图谱所需章节结构
 *
 * @author tanzheng
 * @date 2022/10/12
 */

@Data
public class savePartitionParam {

    String userId;

    String id;

    List<Partition> partition;
}
