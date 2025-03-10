package cmf.commitField.global.aws.s3;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private static final String BUCKET_NAME = "cmf-bucket-dev-seoyeon-1";
    private static final String REGION = "ap-northeast-2";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private final Logger logger = LoggerFactory.getLogger(S3Service.class.getName());

    // 파일 업로드 기능
    public String uploadFile(MultipartFile file, String dirName) throws IOException {
        try {

            // 파일 크기 검증
            validateFileSize(file);

            // UUID로 고유한 파일명 생성
            String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            // PutObjectRequest 객체를 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType(file.getContentType())
    //                .acl(ObjectCannedACL.PUBLIC_READ) // Public Read 권한 추가
                    .build();
            // 파일 S3에 업로드
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            logger.info("파일 업로드 성공");

            // S3 파일 URL 반환
            return "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + fileName;

        } catch (IOException e) {
            logger.error("파일 업로드 실패: {}", e.getMessage());
            throw new IOException("파일 업로드 중 오류 발생", e);
        }
    }

    // 파일 크기 검증 메서드
    private void validateFileSize(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("파일 크기가 5MB를 초과하여 업로드할 수 없습니다.");
        }
    }



    // 파일 삭제 기능
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        logger.info("파일 삭제 성공: {}", fileName);
    }
}
