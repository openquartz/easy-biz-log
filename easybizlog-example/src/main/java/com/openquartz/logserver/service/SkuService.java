package com.openquartz.logserver.service;

import com.openquartz.logserver.pojo.ObjectSku;

/**
 * @author svnee
 **/
public interface SkuService {

    Long createObjectSkuNoJoinTransaction(ObjectSku sku);

    Long createObjectBusinessError(ObjectSku sku);

    Long createObjectBusinessError2(ObjectSku sku);

    Long createObjectSkuNoJoinTransactionRollBack(ObjectSku sku);
}
