package com.insiders.poc1.integrations;

import com.google.gson.Gson;
import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Component;

@Component
public class ViaCepApi {

    private final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public AddressRequestDto getCompleteAddress(String cep) throws IOException {
        String urlString =  VIA_CEP_URL + cep + "/json/";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String inputLine;
            StringBuilder responseString = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseString.append(inputLine);
            }
            in.close();
            httpClient.close();
            var address = new Gson().fromJson(responseString.toString(), AddressRequestDto.class);
            if(address.getUf() == null) {
                throw new RuntimeException("Cep não encontrado"); //TODO - Criar exceção para quando o cep não for encontrado.
            }
            return address;
        } else {
            httpClient.close();
            throw new RuntimeException("Não foi possível realizar a busca com o CEP informado.");
            // TODO - Criar exceção para cep fora do padrão (não válido).
        }
    }
}