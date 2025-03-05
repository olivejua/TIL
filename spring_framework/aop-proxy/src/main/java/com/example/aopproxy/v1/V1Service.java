package com.example.aopproxy.v1;

import com.example.aopproxy.pojo.Pojo;
import com.example.aopproxy.pojo.SimplePojo;
import org.springframework.stereotype.Service;

@Service
public class V1Service {

    public void methodA() {
        Pojo pojo = new SimplePojo();

        //calling code
        pojo.foo();
    }
}
