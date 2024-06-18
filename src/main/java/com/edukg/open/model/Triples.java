package com.edukg.open.model;

import lombok.Data;

import java.util.List;

/**
 * Triples
 *
 * @author tanzheng
 * @date 2023/12/21
 */

@Data
public class Triples {

    List<Node> nodes;

    List<Link> links;
}
