package com.sexto.ia.service;

import com.sexto.ia.model.Filme;
import com.sexto.ia.model.PerfilInicial;
import com.sexto.ia.repository.UsuarioRepository;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecomendadorService{

    @Inject
    UsuarioRepository repository;

    private Recommender recomendador;
    private FilmeService filmeService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RecomendadorService(){

    }

    public RecomendadorService(Recommender recommender, FilmeService filmeService) {
        this.recomendador = recommender;
        this.filmeService = filmeService;
    }

    public List<Filme> recomenda(Long userId,int quantidadeDeRecomendacoes) throws TasteException {
        int quantidadeParaBusca = quantidadeDeRecomendacoes;
        int quantidadeObtida = 0;
        List<Filme> filmes;
        do {
            String mensagemFormat = "Obtendo %d recomendacoes para o usuario %d";
            logger.info(String.format(mensagemFormat,quantidadeParaBusca, userId));
            List<RecommendedItem>  recomendacoes = recomendador.recommend(userId,quantidadeParaBusca);
            List<Long> ids = recomendacoes
                    .stream()
                    .map(RecommendedItem::getItemID)
                    .collect(Collectors.toList());

            filmes = filmeService.list(ids);

// -- ALERTA DE GANBIARRA --2 atualiza a aviação dos filmes conforme são recalculados os modelos
            filmes.forEach(filme -> {
                RecommendedItem recomendacao =
                        recomendacoes.stream().filter(item -> item.getItemID()  == filme.getId()).findFirst().get();
                if(filme.getAvaliacao() == null || filme.getAvaliacao().floatValue() != recomendacao.getValue() ){
                    filme.setAvaliacao((double) recomendacao.getValue());
                    filmeService.updateAvaliacao(filme, recomendacao.getValue());
                }
            });
            quantidadeObtida = filmes.size();
            quantidadeParaBusca += 10;
        }while (quantidadeDeRecomendacoes > quantidadeObtida);
        return filmes;
    }

    public Long montaPerfilInicial(PerfilInicial perfilInicial) throws IOException {
        List<Filme> filmeList = new ArrayList<>();
        // definir um id para um NOVO USUARIO (ultimo +1)
        String[] ultimaLinha = CsvService.getLastRowFromRatingFile();
// ALERTA DE GANBIARRA -- prefira utilizar um sistema de filas para esta etapa
// para garantir a ordem correta de usuários novos inseridos
        Long novoUsuarioId = perfilInicial.getUsuarioId() == null ? Long.parseLong(ultimaLinha[0]) + 1 :
                perfilInicial.getUsuarioId() == null ? repository.getMaxId() + 1 : perfilInicial.getUsuarioId();
        // Salvar os filmes MAIS LEGAIS para o NOVO USUARIO no arquivo
        // Salvar os filmes de afinidade NORMAL para o NOVO USUARIO usuario no arquivo
        // Salvar os filmes MENOS LEGAIS para o NOVO USUARIO no arquivo
        List<String[]> novasLinhas = new ArrayList<>();
        novasLinhas.addAll(novosRegistros(perfilInicial.getFilmesMaisLegais(), novoUsuarioId, 5.0));
        novasLinhas.addAll(novosRegistros(perfilInicial.getFilmesNormais(), novoUsuarioId,3.0));
        novasLinhas.addAll(novosRegistros(perfilInicial.getFilmesMenosLegais(), novoUsuarioId, 1.0));
        CsvService.writeNewRows(CsvService.readRatingFile(),novasLinhas);
        logger.info("Perfil montado para o usuário de id : "+novoUsuarioId);
        return novoUsuarioId;
    }

    public List<String[]> novosRegistros(List<Long> filmeIdList, long usuarioId, double nota){
        return filmeIdList.stream().map(filmeId -> new String[]{
                    String.valueOf(usuarioId),
                    String.valueOf(filmeId),
                    String.format("%.2f", nota),
                    String.valueOf(System.currentTimeMillis())
            }).collect(Collectors.toList());
    }
}
