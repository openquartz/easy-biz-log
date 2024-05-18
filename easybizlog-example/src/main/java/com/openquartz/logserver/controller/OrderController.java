package com.openquartz.logserver.controller;

import com.openquartz.logserver.pojo.Order;
import com.openquartz.logserver.service.IOrderService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @PostMapping("/create")
    public void create(@RequestBody Order order){

        orderService.createOrder(order);
    }

}
