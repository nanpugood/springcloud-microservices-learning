package com.central.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-center")
public interface RemoteFeignClient {

	@GetMapping("order/create")
	void createOrder();
	@GetMapping("order/findByMobile")
	void findByMobile();
	
}
