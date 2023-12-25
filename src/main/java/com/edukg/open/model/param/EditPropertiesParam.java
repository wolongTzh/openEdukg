package com.edukg.open.model.param;

import com.edukg.open.model.Properties;
import lombok.Data;

/**
 * 编辑教材信息参数
 *
 * @author tanzheng
 * @date 2023/12/20
 */

@Data
public class EditPropertiesParam {

    String userId;
    String taskId;

    Properties properties;
}
