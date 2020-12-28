package com.o2o.action.server.DTO;

import java.util.List;

public class ResultDTO {
    private List<documentDTO> documents;

    public List<documentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<documentDTO> documents) {
        this.documents = documents;
    }
}
