package Menu;

import java.util.Scanner;

import Entidades.Lista;
import Entidades.Produto;
import aed3.ArvoreBMais;
import aed3.HashExtensivel;
import aed3.ParIDGTIN;
import aed3.ParUsuarioLista;
import Arquivos.ArquivoLista;
import Arquivos.ArquivoProduto;
import Arquivos.ArquivoUsuario;

public class MenuProduto {
    public Scanner console;
    public ArquivoProduto arqProduto;
    public ArquivoUsuario arqUsu;
    public ArquivoLista arqList;
    public HashExtensivel<ParIDGTIN> iCode;
    public ArvoreBMais<ParUsuarioLista> arvoreLista;


    public MenuProduto() throws Exception {
        arqUsu = new ArquivoUsuario();
        arqList = new ArquivoLista();
        arqProduto = new ArquivoProduto(); // Inicializando o arquivo de produtos
        console = new Scanner(System.in);

        iCode = new HashExtensivel<>(ParIDGTIN.class.getConstructor(),
                4,
                ".\\Dados\\produto\\produtoCodigo.d.db",
                ".\\Dados\\produto\\produtoCodigo.c.db");

        arvoreLista = new ArvoreBMais<>(ParUsuarioLista.class.getConstructor(),
                4,
                ".\\Dados\\produto\\arvore_lista_produto.db");
    }

    public void menu(int idUsuario) throws Exception {
        console = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n\nEasyGift 1.0");
            System.out.println("---------");
            System.out.println("> Produtos - Autenticado");
            System.out.println("\n0 - Voltar");
            System.out.println("1 - Buscar produtos por GTIN");
            System.out.println("2 - Listar todos os Produtos");
            System.out.println("3 - Cadastrar um novo Produto");
            System.out.print("\nOpção: ");

            opcao = console.nextInt();
            int id;

            switch (opcao) {
                case 0:
                    System.out.println("Voltando...");
                    break;
                case 1:
                    buscarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    cadastrarProduto();
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    public void cadastrarProduto() throws Exception {
        System.out.println("\n\n\n---------");
        System.out.println("> Produtos - Cadastro de Novo Produto");

        System.out.print("GTIN-13: ");
        console.nextLine(); // consumir enter pendente
        String gtin = console.nextLine();

        System.out.print("Nome do Produto: ");
        String nome = console.nextLine();

        System.out.print("Descrição do Produto: ");
        String descricao = console.nextLine();

        Produto produto = new Produto(gtin, nome, descricao);
        int id = arqProduto.create(produto);
        iCode.create(new ParIDGTIN(produto.getGtin13(), id));


        // Se tiver índice, você pode criar aqui
        // iCode.create(new ParIDGTIN(produto.getCodigoCompartilhavel(), id));
        // arvoreProduto.create(new ParUsuarioProduto(idUsuario, id));

        System.out.println("\n✅ Produto cadastrado com sucesso! (ID = " + id + ")");
    }

    public int listarProdutos() throws Exception {
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Listagem de Todos os Produtos");
        System.out.println("---------");

        int index = 1; // contador para exibição numerada
        int escolhido = -1;

        for (int i = 1; i <= arqProduto.tamanho(); i++) { // IDs começam em 1
            Produto p = arqProduto.read(i);
            if (p != null) {
                System.out.println("(" + index + ") " + p.getNome() + " - " + p.getDescricao());
                index++;
            }
        }

        if (index == 1) { // não encontrou nenhum produto
            System.out.println("Nenhum produto cadastrado.");
            return -1;
        }

        System.out.print("Digite o número do produto desejado: ");
        int opcao = console.nextInt();

        if (opcao >= 1 && opcao < index) {
            // Encontrar o ID real do produto selecionado
            int count = 0;
            for (int i = 1; i <= arqProduto.tamanho(); i++) {
                Produto p = arqProduto.read(i);
                if (p != null) {
                    count++;
                    if (count == opcao) {
                        escolhido = i; // ID real do produto
                        break;
                    }
                }
            }
            verProduto(escolhido);
        } else {
            System.out.println("Opção inválida.");
        }

        return escolhido;
    }

    // Ver detalhes de um produto
    public void verProduto(int id) throws Exception {
        Produto produto = arqProduto.read(id);
        if (produto != null) {
            System.out.println("\n--- Detalhes do Produto ---");
            System.out.println("Nome: " + produto.getNome());
            System.out.println("GTIN-13: " + produto.getGtin13());
            System.out.println("Descrição: " + produto.getDescricao());
        } else {
            System.out.println("\nProduto não encontrado.");
        }
    }

    public void buscarProduto() throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Buscar Produto");
        System.out.print("Digite o GTIN-13 do produto: ");
        String gtin = console.nextLine();

        try {
            // Buscar no índice hash pelo GTIN-13
            ParIDGTIN pcid = iCode.read(new ParIDGTIN(gtin, -1).hashCode());
            if (pcid != null) {
                Produto produto = arqProduto.read(pcid.getId());
                if (produto != null) {
                    System.out.println("Produto encontrado!");
                    verProduto(produto.getId());
                } else {
                    System.out.println("Produto não encontrado no arquivo.");
                }
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar o produto.");
            e.printStackTrace();
        }
    }
}
