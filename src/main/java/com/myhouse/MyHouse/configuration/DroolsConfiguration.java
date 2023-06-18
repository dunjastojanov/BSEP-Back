package com.myhouse.MyHouse.configuration;

import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.repository.AlarmRuleRepository;
import com.myhouse.MyHouse.repository.DeviceMessageRepository;
import lombok.RequiredArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.myhouse.MyHouse.service")
public class DroolsConfiguration {
        private static final String drlFile = "rules.drl";
        private final AlarmRuleRepository alarmRuleRepository;
        @Bean
        public KieSession kieSession() {
            KieServices kieServices = KieServices.Factory.get();

            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            KieModule kieModule = kieBuilder.getKieModule();

            KieContainer kc = kieServices.newKieContainer(kieModule.getReleaseId());
            KieSession kieSession = kc.newKieSession();
            alarmRuleRepository.findAll().forEach(kieSession::insert);
            return kieSession;
        }
}
