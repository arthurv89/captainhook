package com.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.framework.client.ClientApplication;
import org.springframework.boot.SpringApplication;

public class ClientMain {
    public static void main(final String[] args) {
        SpringApplication.run(new Class[]{
                ClientApplication.class,
                ClientRunner.class
        }, args);
    }
}
