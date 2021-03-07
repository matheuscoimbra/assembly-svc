package com.assembly.vote.assembly;

import com.assembly.vote.assembly.domain.core.AssemblyFacade;
import com.assembly.vote.assembly.domain.core.model.Agenda;
import com.assembly.vote.assembly.domain.core.model.dto.VoteRequestDTO;
import com.assembly.vote.assembly.domain.core.model.enums.VOTE;
import com.assembly.vote.assembly.domain.infrastructure.repository.AgendaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class AssemblyApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private AssemblyFacade assemblyFacade;

    @Autowired
    private AgendaRepository agendaRepository;

    private String url;

    public List<Agenda> data() {

        return Arrays.asList(Agenda.builder().title("pauta1").build(),
                Agenda.builder().title("pauta2").build(),
                Agenda.builder().title("pauta3").build(),
                Agenda.builder().id("123").title("pauta4").build());
    }


    @PostConstruct
    public void init(){
        this.url="/v1/agenda";

        agendaRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(agendaRepository::save)
                .doOnNext((item -> {
                    System.out.println("Pauta inserida : " + item);
                }))
                .blockLast();
    }



    @Test
    public void listarTest() {

        client.get()
                .uri(url+"/all?page=0&size=5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Agenda.class)
                .consumeWith(response -> {
                    List<Agenda> agenda = response.getResponseBody();
                    agenda.forEach(p -> {
                        System.out.println(p.getTitle());
                    });

                    Assertions.assertThat(agenda.size()>0).isTrue();
                });
    }

    @Test
    public void getOneItem(){

            client.get().uri(url+"?id=","123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title", "pauta4");

    }

    @Test
    public void createAgenda(){

        var agenda = Agenda.builder().title("pautaX").description("pauta desc").build();

        client.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(agenda), Agenda.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("pauta desc")
                .jsonPath("$.title").isEqualTo("pautaX");



    }

    @Test
    public void startAgenda(){

        client.patch().uri(url+"/start?agendaId=123&durationAgenda=00:10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.open",true);

    }

    @Test
    public void testeVote(){
        VoteRequestDTO voteRequestDTO = new VoteRequestDTO();
        voteRequestDTO.setVote(VOTE.NAO);
        voteRequestDTO.setCpf("28120065018");
        voteRequestDTO.setAgendaId("123");
        client.post().uri("/v1/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(voteRequestDTO), VoteRequestDTO.class)
                .exchange()
                .expectStatus().is4xxClientError();

    }
}
