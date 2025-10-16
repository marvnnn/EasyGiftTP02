package Menu;

import java.util.Scanner;

import Entidades.Lista;
import aed3.ParCID;

public class MenuProduto {
    public Scanner console;

    public void menu(int idUsuario) throws Exception{
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
                case 1: buscarProduto();
                    break;
                case 2: listarProdutos();
                    break;
                case 3: cadastrarProduto();
                    break;
                default:
                    break;
            }
        } while(opcao != 0);
    }
}
