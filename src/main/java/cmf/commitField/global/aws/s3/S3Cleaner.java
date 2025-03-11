package cmf.commitField.global.aws.s3;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class S3Cleaner {

    private final S3Client s3Client;
    private final String bucketName = "cmf-bucket-dev-seoyeon-1"; // S3 버킷 이름

    public S3Cleaner(S3Client s3Client) {
        this.s3Client = s3Client;
    }

//    @PreDestroy
//    public void cleanup() {
//        // S3에서 테스트 파일 삭제
//        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
//                .bucket(bucketName)
//                .build();
//
//        // 파일 목록을 가져옵니다.
//        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjects);
//
//        // 각 파일을 삭제합니다.
//        for (S3Object s3Object : listObjectsResponse.contents()) {
//            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(s3Object.key()));
//        }
//
//        System.out.println("S3에 있는 테스트 파일들을 삭제했습니다.");
//    }
}