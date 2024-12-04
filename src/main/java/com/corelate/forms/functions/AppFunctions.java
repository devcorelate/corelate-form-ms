package com.corelate.forms.functions;

import com.corelate.forms.service.IAppService;
import com.corelate.forms.service.IFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AppFunctions {

    private static final Logger log = LoggerFactory.getLogger(AppFunctions.class);

    @Bean
    public Consumer<String> updateCommunication(IFormService iFormService) {
        return formId -> {
            log.info("Updating Communication status for the template : " + formId);
            iFormService.updateCommunicationStatus(formId);
        };
    }

}
