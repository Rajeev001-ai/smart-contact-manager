package com.scm.scm20.userService;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.scm20.Helpers.AppConstants;

@Service
public class ImageServiceImple {

    private final Cloudinary cloudinary;

    public ImageServiceImple(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile contactImage, String filename) {
        try {
            byte[] data = contactImage.getBytes();

            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", filename
            ));

            return this.getUrlFormPublicId(filename);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUrlFormPublicId(String publicId) {
        return cloudinary
            .url()
            .transformation(
                new Transformation<>()
                    .width(AppConstants.CONTACT_IMAGE_WIDTH)
                    .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                    .crop(AppConstants.CONTACT_IMAGE_CROP)
            )
            .generate(publicId);
    }
}
