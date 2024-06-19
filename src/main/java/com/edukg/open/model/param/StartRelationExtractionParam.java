package com.edukg.open.model.param;

import com.edukg.open.model.Properties;
import lombok.Data;

import java.util.List;

/**
 * 开始关系抽取参数
 *
 * @author tanzheng
 * @date 2023/12/20
 */

@Data
public class StartRelationExtractionParam {

    String userId;
    String taskId;

    List<List<String>> relation;
}
