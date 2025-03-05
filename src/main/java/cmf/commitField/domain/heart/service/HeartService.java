package cmf.commitField.domain.heart.service;

public interface HeartService {

    void heart(Long userId, Long roomId);

    void heartDelete(Long userId, Long roomId);
}