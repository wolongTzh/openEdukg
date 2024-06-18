package com.edukg.open.model.param;

import com.edukg.open.model.Triples;
import lombok.Data;

import java.util.List;

/**
 * 开始关系抽取参数
 *
 * @author tanzheng
 * @date 2023/12/20
 */

@Data
public class SaveTriplesParam {

    String userId;

    String id;

    Triples triples;
}
