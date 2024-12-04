package com.corelate.forms.service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("orchestrator")
interface AppFeignClient {
    //Fetch for BPNM microservice Data
}
