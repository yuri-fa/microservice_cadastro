package br.com.pecem.localsurf;

import net.logstash.logback.argument.StructuredArgument;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@SpringBootTest
class CadastroDeMenbrosApplicationTests {

	@Test
	void contextLoads() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		List<StructuredArgument> structuredArgumentList = new ArrayList<>();
		structuredArgumentList.add(kv("teste","meu pau"));
		logger.info("[START]",structuredArgumentList.toArray(new StructuredArgument[structuredArgumentList.size()]));

	}

}
