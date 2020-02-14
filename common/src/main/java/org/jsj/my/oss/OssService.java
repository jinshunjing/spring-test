package org.jsj.my.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云服务
 *
 * @author SJ
 */
@Slf4j
public class OssService {
    private String accessKeyId;
    private String accessKeySecret;

    private String endpoint;
    private String bucketName;

    private String dapp;

    public void serverConfig(String endpoint, String bucket, String key, String secret, String dapp) {
        this.endpoint = endpoint;
        this.accessKeyId = key;
        this.accessKeySecret = secret;
        this.bucketName = bucket;
        this.dapp = dapp;
    }

    public String uploadDAppPhoto(InputStream invoice, String address) {
        return uploadPicture(invoice, dapp + "/" + address + "/");
    }

    public String getDAppPhotoPath(String hash, String address) {
        return getFullPathUrl(dapp + "/" + address + "/", hash);
    }

    public String uploadPicture(InputStream photo, String prefix) {
        String hash = getRandomUUID();
        String path = getFullPath(prefix, hash);

        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");

        try {
            PutObjectResult result = client.putObject(bucketName, path, photo, meta);
            if (result.getETag() != null) {
                return hash;
            }
        } catch (Exception e) {
            log.error("OSS error", e);
        }
        return null;
    }

    public String getFullPath(String prefix, String hash) {
        return prefix + hash.substring(0, 2) + "/" + hash.substring(2, 4) + "/" + hash.substring(4, 6) + "/" + hash;
    }

    public String getFullPathUrl(String prefix, String hash) {
        return "https://" + bucketName + "." + endpoint + "/" + getFullPath(prefix, hash);
    }

    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        StringBuilder sb = new StringBuilder();
        sb.append(digits(mostSigBits >> 32, 8));
        sb.append(digits(mostSigBits >> 16, 4));
        sb.append(digits(mostSigBits, 4));
        sb.append(digits(leastSigBits >> 48, 4));
        sb.append(digits(leastSigBits, 12));
        assert (sb.length() == 32);
        return sb.toString();
    }
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

}
