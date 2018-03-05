package com.qxcmp.core.validation;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    private static Set<String> supportedType = new HashSet<>();

    static {
        supportedType.add("jpg");
        supportedType.add("jpeg");
        supportedType.add("png");
        supportedType.add("gif");
    }

    @Override
    public void initialize(Image constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || StringUtils.isEmpty(value.getOriginalFilename())) {
            return true;
        }

        String fileType = FilenameUtils.getExtension(value.getOriginalFilename());

        return supportedType.contains(fileType.toLowerCase());
    }
}
