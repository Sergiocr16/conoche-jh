package com.firefly.conoche.service.util;


import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by melvin on 3/22/2017.
 */

@Service
@Transactional
public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public void delete(String key) throws IOException{
        Map config = new HashMap();
        config.put("invalidate", true);
        cloudinary.uploader().destroy(key, config);
    }
}
