package com.insiders.poc1.integrations;

import com.google.gson.Gson;
import com.insiders.poc1.controller.dto.request.AddressRequestDto;
import com.insiders.poc1.exception.InvalidInputException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

@Component
public class ViaCepApi {

    private final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public AddressRequestDto getCompleteAddress(String cep) {

        try {
            String urlString = VIA_CEP_URL + cep + "/json/";
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
                if (address.getUf() == null) {
                    throw new InvalidInputException("The zip code entered is not valid!");
                }
                return address;
            } else {
                httpClient.close();
                throw new InvalidInputException("The zip code entered is not valid!");
            }
        } catch (IOException ex) {
            throw new InvalidInputException("Invalid zip code!");
        }
    }
}