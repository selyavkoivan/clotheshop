package by.bsuir.clotheshop.model.service.cloudinary;

import by.bsuir.clotheshop.model.service.cloudinary.data.CloudData;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class PhotoUploader {
    public static String uploadImage(MultipartFile image) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CloudData.CLOUD_NAME,
                "api_key", CloudData.API_KEY,
                "api_secret", CloudData.API_SECRET,
                "secure", true));

        Map uploadResult = cloudinary.uploader().upload(convertMultiPartToFile(image), ObjectUtils.emptyMap());
        if (uploadResult.isEmpty())
            return "";
        return uploadResult.get("url").toString();
    }

    private static byte[] convertMultiPartToFile(MultipartFile file) throws IOException {
        return cropImageSquare(file.getBytes());
    }

    private static byte[] cropImageSquare(byte[] image) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return image;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2),
                yc - (squareSize / 2),
                squareSize,
                squareSize
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(croppedImage, "png", baos);
        return baos.toByteArray();
    }
}
