package br.ufpr.dinf.gres.api.resource;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadForm {

    private List<MultipartFile> files;

    public FileUploadForm() {
    }

    public FileUploadForm(List<MultipartFile> files) {
        this.files = files;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
