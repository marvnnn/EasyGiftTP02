package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import aed3.*;

public class Produto implements Registro {
    private int id;
    private String gtin13;
    private String nome;
    private String descricao;

    public Produto() {
        this.id = -1;
        this.gtin13 = "0000000000000";
        this.nome = "";
        this.descricao = "";
    }

    public Produto(String gtin13, String nome, String descricao) {
        this.id = -1;
        this.gtin13 = gtin13;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.id);
        dos.writeUTF(this.gtin13);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.gtin13 = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
    }
}