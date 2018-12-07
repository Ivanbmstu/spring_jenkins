package ru.alfabank.infra;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InfraApplicationTests {

	@Test
	public void contextLoads() {
		try {
			var runtimeException = new RuntimeException();
			throw runtimeException;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

}
