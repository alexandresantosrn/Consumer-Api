package com.consumer.consumer_api;

import com.consumer.consumer_api.client.IbgeClient;
import com.consumer.consumer_api.model.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class ConsumerApiApplication implements CommandLineRunner {
	@Autowired
	private IbgeClient ibgeClient;

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApiApplication.class, args);
		initRestTemplate();
	}

	private static void initRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://servicodados.ibge.gov.br/api/v1/localidades/estados";
		String response = restTemplate.getForObject(url, String.class);
		System.out.println(response);
		List estados = restTemplate.getForObject(url, List.class);

		for(Object estado : estados) {
			LinkedHashMap estadoMap = (LinkedHashMap) estado;
			Estado estadoObj = new Estado();
			estadoObj.setId((Integer) estadoMap.get("id"));
			estadoObj.setSigla((String) estadoMap.get("sigla"));
			estadoObj.setNome((String) estadoMap.get("nome"));
			System.out.println(estadoObj);

			System.out.print(estadoObj.getId());
			System.out.print(" - ");
			System.out.println(estadoObj.getNome());
		}
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(ibgeClient.getEstados());
		List<Estado> estados = ibgeClient.getEstados();

		for (Estado estado : estados) {
			System.out.print(estado.getId());
			System.out.print(" - ");
			System.out.println(estado.getNome());
		}
	}
}


