package com.consumer.consumer_api;

import com.consumer.consumer_api.client.IbgeClient;
import com.consumer.consumer_api.model.Estado;
import com.consumer.consumer_api.model.Municipio;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class ConsumerApiApplication implements CommandLineRunner {
	@Autowired
	private IbgeClient ibgeClient;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ConsumerApiApplication.class, args);
		initRestTemplate();
		initHttpClient();
	}

	private static void initRestTemplate() {
		System.out.println("--- RestTemplate ---");

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://servicodados.ibge.gov.br/api/v1/localidades/estados";
		String result = restTemplate.getForObject(url, String.class);
		System.out.println(result);
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

	private static void initHttpClient() throws IOException {
		System.out.println("--- HttpClient ---");

        HttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("http://servicodados.ibge.gov.br/api/v1/localidades/estados");

            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("--- Feign ---");

		System.out.println(ibgeClient.getEstados());

		List<Estado> estados = ibgeClient.getEstados();
		List<Municipio> municipiosRN = ibgeClient.getMunicipiosRN();

		estados.forEach(System.out::println);
		municipiosRN.forEach(System.out::println);

		for (Estado estado : estados) {
			System.out.print(estado.getId());
			System.out.print(" - ");
			System.out.println(estado.getNome());
		}

		for (Municipio municipio : municipiosRN) {
			System.out.print(municipio.getId());
			System.out.print(" - ");
			System.out.println(municipio.getNome());
		}
	}
}


