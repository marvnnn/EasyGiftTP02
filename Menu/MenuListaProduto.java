package Menu;

import java.util.Scanner;

import Arquivos.ArquivoListaProduto;
import Arquivos.ArquivoProduto;
import Entidades.ListaProduto;
import Entidades.Produto;
import aed3.HashExtensivel;
import aed3.ParIDGTIN;
import aed3.ParIDListaProduto;

/**
 * Classe MenuListaProduto
 * Responsável por gerenciar os produtos dentro de uma lista.
 */
public class MenuListaProduto {

    private Scanner console;
    private ArquivoListaProduto arqListaProduto;
    private HashExtensivel<ParIDListaProduto> indiceListaProduto; // Índice para busca por GTIN
    private MenuProduto menuProduto;

    // Construtor
    public MenuListaProduto() {
        try {
            console = new Scanner(System.in);
            arqListaProduto = new ArquivoListaProduto();
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
    public void menu(int idLista) throws Exception {
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
                    incluirProduto(idLista);
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
    public void incluirProduto(int idLista) throws Exception {
        menuProduto = new MenuProduto();
        menuProduto.listarProdutos();
        System.out.print("Digite o ID do produto a ser adicionado: ");
        int idProduto = console.nextInt();
        ListaProduto lp = new ListaProduto(idLista, idProduto);
        int id = arqListaProduto.create(lp);
        indiceListaProduto.create(new ParIDListaProduto(idLista, idProduto));
        System.out.println("Produto adicionado à lista com ID: " + idLista);

    }

    /**
     * Exclui um produto da lista
     */
    public void excluirProduto(int idLista) throws Exception {
        // Lógica para selecionar e remover um produto da lista
    }

    /**
     * Lista todos os produtos de uma lista
     */
    public void listarProdutos(int idLista) throws Exception {
        // Percorre o arquivo de ListaProduto e mostra os produtos da lista
    }

}
