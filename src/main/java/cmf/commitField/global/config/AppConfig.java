package cmf.commitField.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class AppConfig {
    @Getter
    private static String siteFrontUrl;
    @Getter
    private static String siteBackUrl;
    @Getter
    private static String siteCookieDomain;

    @Value("${custom.dev.frontUrl}")
    public void setSiteFrontUrl(String siteFrontUrl) {
        this.siteFrontUrl = siteFrontUrl;
    }
    @Value("${custom.dev.backUrl}")
    public void setSiteBackUrl(String siteBackUrl) {
        this.siteBackUrl = siteBackUrl;
    }
    @Value("${custom.dev.cookieDomain}")
    public void setSiteCookieDomain(String siteCookieDomain) {
        this.siteCookieDomain = siteCookieDomain;
    }

}
