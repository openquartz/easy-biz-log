package com.openquartz.easybizlog.core.diff;

import de.danielbechler.diff.node.DiffNode;

/**
 * @author svnee
 */
public interface IDiffItemsToLogContentService {

    String toLogContent(DiffNode diffNode, final Object o1, final Object o2);
}
