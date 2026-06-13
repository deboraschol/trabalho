package com.agencia.viagens;

import com.agencia.viagens.model.Destino;
import com.agencia.viagens.repository.DestinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Insere destinos iniciais no banco ao subir a aplicação.
 * Só executa se o banco estiver vazio.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DestinoRepository destinoRepository;

    @Override
    public void run(String... args) {
        if (destinoRepository.count() == 0) {
            destinoRepository.saveAll(List.of(

                Destino.builder()
                    .nome("Paris")
                    .localizacao("França")
                    .descricao("A romântica Cidade Luz, repleta de arte, gastronomia e cultura.")
                    .idioma("Francês")
                    .moeda("Euro (EUR)")
                    .notaMedia(0.0)
                    .totalAvaliacoes(0)
                    .build(),

                Destino.builder()
                    .nome("Rio de Janeiro")
                    .localizacao("Brasil")
                    .descricao("Cidade Maravilhosa com praias incríveis, o Cristo Redentor e o famoso Carnaval.")
                    .idioma("Português")
                    .moeda("Real (BRL)")
                    .notaMedia(0.0)
                    .totalAvaliacoes(0)
                    .build(),

                Destino.builder()
                    .nome("Tóquio")
                    .localizacao("Japão")
                    .descricao("Metrópole futurista que mistura tradição milenar e modernidade tecnológica.")
                    .idioma("Japonês")
                    .moeda("Iene (JPY)")
                    .notaMedia(0.0)
                    .totalAvaliacoes(0)
                    .build()

            ));

            System.out.println("✅ 3 destinos inseridos com sucesso!");
        }
    }
}
