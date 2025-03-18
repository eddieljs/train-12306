package com.eddie.train.business.service;


import com.eddie.train.business.req.StationQueryReq;
import com.eddie.train.business.req.StationSaveReq;

import java.util.List;


public interface StationService {

     void save(StationSaveReq req);

     PageResp<StationQueryResp> queryList(StationQueryReq req);

     void delete(Long id);

     List<StationQueryResp> queryMine();

      List<StationQueryResp> queryAll() ;
}