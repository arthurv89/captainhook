package com.arthurvlug._service.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.arthurvlug._service.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages= {
        ServiceConfiguration.PACKAGE_NAME + ".client",
        ServiceConfiguration.PACKAGE_NAME + ".common"
})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientComponentScanner {
}
