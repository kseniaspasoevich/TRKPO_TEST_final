package documents.jpa.entityParser.files;

import documents.dto.files.documents.FilePathDto;
import documents.jpa.entity.files.documents.FilePath;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class FilePathParser {


    public FilePathDto EtoDTO(FilePath filePath) {
        return FilePathDto.builder().path(filePath.getPath())
                .name(filePath.getName())
                .createdTime(filePath.getCreatedTime())
                .id(filePath.getId())
                .size(filePath.getSize()).build();
    }

    public FilePath DTOtoE(FilePathDto filePathDto) {

        FilePath filePath = new FilePath();
        filePath.setPath(filePathDto.getPath());
        filePath.setName(filePathDto.getName() == null ? filePathDto.getPath() : filePathDto.getName());
        filePath.setCreatedTime(new Date());
        filePath.setSize((long)filePathDto.getPath().length());

        return filePath;
    }

    public List<FilePathDto> fromList(List<FilePath> list) {
        List<FilePathDto> filePathDtos = new LinkedList<>();
        list.forEach(v -> {
            filePathDtos.add(EtoDTO(v));
        });
        return filePathDtos;
    }
}
