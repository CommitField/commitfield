package cmf.commitField.domain.admin.controller;

import cmf.commitField.global.aws.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/pet")
@RequiredArgsConstructor
public class ApiV1PetImgController {
    private final S3Client s3Client;
    private final S3Service s3Service;

    @GetMapping("/test")
    public List<String> home() {
        List<Bucket> bucketList = s3Client.listBuckets().buckets();
        return bucketList.stream().map(Bucket::name).collect(Collectors.toList());
    }

    @GetMapping("/upload")
    public String upload() {
        return """
                <form action="/api/v1/upload" method="post" enctype="multipart/form-data">
                    <input type="file" name="file" accept="image/*">
                    <input type="submit" value="Upload">
                </form>
                """;
    }

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String img = s3Service.uploadFile(file,"pet");

        return """
                <img src="%s">
                <hr>
                <div>업로드 완료</div>
                """.formatted(img);
    }

    @GetMapping("/deleteFile")
    public String showDeleteFile() {
        return """
                <form action="/api/v1/deleteFile" method="post">
                    <input type="text" name="fileName">
                    <input type="submit" value="delete">
                </form>
                """;
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(String fileName) {
        s3Service.deleteFile(fileName);
        return "파일이 삭제되었습니다.";
    }
}
