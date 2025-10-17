package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aed3.*;

public class ListaProduto implements Registro {

    private int id;
    private int idLIsta;
    private int idProduto;

    public ListaProduto(int idLIsta, int idProduto) {
        this.id = -1;
        this.idLIsta = idLIsta;
        this.idProduto = idProduto;
    }

    public ListaProduto() {
        this.id = -1;
        this.idLIsta = -1;
        this.idProduto = -1;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdLIsta() {
        return idLIsta;
    }
    public void setIdLIsta(int idLIsta) {
        this.idLIsta = idLIsta;
    }
    public int getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }


    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.id);
        dos.writeInt(this.idLIsta);
        dos.writeInt(this.idProduto);
       

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.idLIsta = dis.readInt();
        this.idProduto = dis.readInt();
     
    }

    
}
