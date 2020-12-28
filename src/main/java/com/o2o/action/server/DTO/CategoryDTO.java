package com.o2o.action.server.DTO;

public class CategoryDTO {
    private String name;
    private int idx;

    public CategoryDTO(int idx, String name){
        this.idx = idx;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
