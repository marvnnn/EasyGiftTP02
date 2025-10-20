package Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Arquivos.ArquivoLista;
import Arquivos.ArquivoListaProduto;
import Arquivos.ArquivoProduto;
import Entidades.Lista;
import Entidades.ListaProduto;
import Entidades.Produto;
import aed3.HashExtensivel;
import aed3.ParIDListaProduto;

/**
 * Classe MenuListaProduto
 * Responsável por gerenciar os produtos dentro de uma lista.
 */
public class MenuListaProduto {

    private Scanner console;
    private ArquivoListaProduto arqListaProduto;
    private ArquivoProduto arqProduto; // ✅ precisa desse para buscar o produto
    private HashExtensivel<ParIDListaProduto> indiceListaProduto;
    private MenuProduto menuProduto;

    // Construtor
    public MenuListaProduto() {
        try {
            console = new Scanner(System.in);
            arqListaProduto = new ArquivoListaProduto();
            arqProduto = new ArquivoProduto(); // ✅ inicializa o arquivo de produtos também

            indiceListaProduto = new HashExtensivel<>(
                    ParIDListaProduto.class.getConstructor(),
                    4,
                    ".\\Dados\\listaproduto\\listaproduto.d.db",
                    ".\\Dados\\listaproduto\\listaproduto.c.db");

        } catch (Exception e) {
            System.out.println("Erro ao inicializar MenuListaProduto: " + e.getMessage());
        }
    }

    /**
     * Menu principal de Produtos dentro de uma Lista
     */
    public void menu(int idLista, int idUsuario) throws Exception {
        int opcao;
        do {
            System.out.println("\n\n---------");
            System.out.println("> Gerenciar Produtos da Lista");
            System.out.println("0 - Voltar");
            System.out.println("1 - Adicionar produto à lista");
            System.out.println("2 - Remover produto da lista");
            System.out.println("3 - Listar produtos da lista");
            System.out.print("\nOpção: ");
            opcao = console.nextInt();

            switch (opcao) {
                case 0:
                    System.out.println("Voltando...");
                    break;
                case 1:
                    incluirProduto(idLista, idUsuario);
                    break;
                case 2:
                    excluirProduto(idLista);
                    break;
                case 3:
                    listarProdutos(idLista);
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    /**
     * Inclui um produto em uma lista
     */
    public void incluirProduto(int idLista, int idUsuario) throws Exception {
        menuProduto = new MenuProduto();
        int idProduto = menuProduto.listarProdutos(idUsuario); // Lista produtos e retorna o ID escolhido
        if (idProduto != -1) {
            // Criando a relação ListaProduto com quantidade 1 e observações vazias por
            // padrão
            ListaProduto lp = new ListaProduto(idLista, idProduto, 1, "");
            int id = arqListaProduto.create(lp);
            indiceListaProduto.create(new ParIDListaProduto(idLista, idProduto));
            System.out.println("✅ Produto adicionado à lista (ID da relação = " + id + ")");
        } else {
            System.out.println("Nenhum produto selecionado.");
        }
    }

    /**
     * Exclui um produto de uma lista
     */
    public void excluirProduto(int idLista) {
        try {
            System.out.println("\n\n---------");
            System.out.println("> Excluir Produto da Lista - ID da Lista: " + idLista);
            System.out.println("---------");

            int count = 0;
            int index = 1;

            // Array para mapear índice da exibição para o ID do registro
            List<Integer> listaIds = new ArrayList<>();

            // Percorre os registros de ListaProduto
            for (int i = 1; i <= arqListaProduto.tamanho(); i++) {
                ListaProduto lp = arqListaProduto.read(i);
                if (lp != null && lp.getIdLIsta() == idLista) {
                    Produto produto = arqProduto.read(lp.getIdProduto());
                    if (produto != null) {
                        System.out.println("(" + index + ") " + produto.getNome() + " - " + produto.getDescricao());
                        listaIds.add(lp.getId()); // armazena o ID do registro ListaProduto
                        index++;
                        count++;
                    }
                }
            }

            if (count == 0) {
                System.out.println("Nenhum produto encontrado nesta lista.");
                return;
            }

            // Pergunta ao usuário qual produto excluir
            System.out.print("Digite o número do produto que deseja excluir (0 para cancelar): ");
            int opcao = console.nextInt();

            if (opcao == 0) {
                System.out.println("Operação cancelada.");
                return;
            }

            if (opcao < 1 || opcao > listaIds.size()) {
                System.out.println("Opção inválida!");
                return;
            }

            int idListaProduto = listaIds.get(opcao - 1); // pega o ID real do registro
            boolean sucesso = arqListaProduto.delete(idListaProduto);

            if (sucesso) {
                System.out.println("✅ Produto removido da lista com sucesso!");
            } else {
                System.out.println("❌ Falha ao remover o produto.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lista todos os produtos de uma lista
     */
    /**
     * Lista todos os produtos de uma lista, mostrando quantidade e observações
     */
    public void listarProdutos(int idLista) {
        System.out.println("\n\n---------");
        System.out.println("> Lista de Produtos - ID da Lista: " + idLista);
        System.out.println("---------");

        int count = 0;
        int index = 1;

        try {
            arqListaProduto.arquivo.seek(12); // pula o cabeçalho (4 + 8 bytes)

            while (arqListaProduto.arquivo.getFilePointer() < arqListaProduto.arquivo.length()) {
                byte lapide = arqListaProduto.arquivo.readByte();
                short tam = arqListaProduto.arquivo.readShort();

                if (lapide == ' ') { // registro válido
                    byte[] ba = new byte[tam];
                    arqListaProduto.arquivo.read(ba);

                    ListaProduto lp = new ListaProduto();
                    lp.fromByteArray(ba);

                    if (lp.getIdLIsta() == idLista) {
                        Produto produto = arqProduto.read(lp.getIdProduto());
                        if (produto != null) {
                            System.out.println("(" + index + ") " + produto.getNome() + " (x"
                                    + lp.getQuantidade() + ") - Observações: "
                                    + lp.getObservacoes());
                            index++;
                            count++;
                        }
                    }
                } else {
                    // pula o registro excluído
                    arqListaProduto.arquivo.skipBytes(tam);
                }
            }

            if (count == 0) {
                System.out.println("Nenhum produto encontrado nesta lista.");
            } else {
                System.out.println("\nTotal de produtos nesta lista: " + count);
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listarListasDoProduto(int idProduto, ArquivoLista arqLista) {
        System.out.println("\n\n---------");
        System.out.println("> Listas contendo o produto ID: " + idProduto);
        System.out.println("---------");

        int count = 0;

        try {
            // Percorre o arquivo de ListaProduto
            for (int i = 1; i <= arqListaProduto.tamanho(); i++) {
                ListaProduto lp = arqListaProduto.read(i);
                if (lp != null && lp.getIdProduto() == idProduto) {
                    // Para cada correspondência, buscar o nome da lista
                    Lista lista = arqLista.read(lp.getIdLIsta());
                    if (lista != null) {
                        System.out.println("- " + lista.getNome() + " (ID: " + lista.getId() + ")");
                        count++;
                    }
                }
            }

            if (count == 0) {
                System.out.println("Nenhuma lista contém este produto.");
            } else {
                System.out.println("\nTotal de listas com este produto: " + count);
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar listas do produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
