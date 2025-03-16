package com.eddie.train.${module}.service;

import com.eddie.train.common.resp.PageResp;
import com.eddie.train.${module}.req.${Domain}QueryReq;
import com.eddie.train.${module}.req.${Domain}SaveReq;
import com.eddie.train.${module}.resp.${Domain}QueryResp;
import java.util.List;


public interface ${Domain}Service {

     void save(${Domain}SaveReq req);

     PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req);

     void delete(Long id);

     List<${Domain}QueryResp> queryMine();


}