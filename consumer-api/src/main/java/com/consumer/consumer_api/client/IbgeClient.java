package com.consumer.consumer_api.client;

import com.consumer.consumer_api.model.Estado;
import com.consumer.consumer_api.model.Municipio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ibge-client", url = "http://servicodados.ibge.gov.br/api/v1")
public interface IbgeClient {

    @GetMapping("localidades/estados")
    List<Estado> getEstados();

    @GetMapping("localidades/estados/24/municipios")
    List<Municipio> getMunicipiosRN();


}
