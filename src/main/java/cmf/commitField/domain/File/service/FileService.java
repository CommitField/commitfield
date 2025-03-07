package cmf.commitField.domain.File.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    // resources/static/uploads 폴더 경로
    private final String UPLOAD_DIR = "src/main/resources/static/uploads";  // 상대 경로

    // 파일 저장 메소드
    public String saveFile(MultipartFile file) throws IOException {
        // 파일 이름을 유니크하게 생성
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // 파일 경로 생성
        Path path = Paths.get(UPLOAD_DIR, filename);  // static/uploads 폴더에 저장

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(path.getParent());

        // 파일 저장
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 저장된 파일의 URL 반환 (웹에서 접근할 수 있는 경로)
        return "/uploads/" + filename;  // 클라이언트가 접근할 수 있는 URL 반환
    }
}

