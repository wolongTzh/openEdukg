package com.edukg.open.model.param;

import com.edukg.open.model.Termdef;
import lombok.Data;

import java.util.List;

/**
 * 教材构建图谱所需保存图谱参数
 *
 * @author tanzheng
 * @date 2022/10/12
 */

@Data
public class SaveGraphParam {

    String userId;

    String taskId;

    List<Termdef> termdef;
}
