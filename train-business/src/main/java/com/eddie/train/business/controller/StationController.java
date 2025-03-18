package com.eddie.train.business.controller;


import com.eddie.train.business.resp.StationQueryResp;
import com.eddie.train.business.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {
    @Resource
    private StationService stationService;



}