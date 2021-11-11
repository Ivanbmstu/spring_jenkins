package ru.alfabank.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InfraApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("HELLO WORLD");
		System.out.println(System.getProperty("java.version"));
		System.out.println("props \n" + System.getProperties());
	}

}
