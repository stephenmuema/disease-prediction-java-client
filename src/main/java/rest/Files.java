package rest;

import java.io.File;

public class Files {
    private int id;
    private String name;
    private File f;

    public Files(String name, File f) {
        this.name = name;
        this.f = f;
    }

    public Files(String name) {
        this.name = name;
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}