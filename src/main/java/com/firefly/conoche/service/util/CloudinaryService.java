package com.firefly.conoche.service.util;


import com.cloudinary.Cloudinary;
import com.firefly.conoche.domain.RealTimeEventImage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by melvin on 3/22/2017.
 * Servicio para acceder a cloudinary
 */

@Service
@Transactional
public class CloudinaryService {

    private Cloudinary cloudinary;

    /**
     * Constructor
     * @param cloudinary
     */
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Borra una url de cloudinary
     * @param key
     * @throws IOException
     */
    public void delete(String key) throws IOException{
        Map config = new HashMap();
        config.put("invalidate", true);
        cloudinary.uploader().destroy(key, config);
    }

    /**
     * Borra un grupo de urls de cloudinary.
     * @param keys
     * @throws IOException
     */
    public void delete(Iterable<String> keys) throws IOException {
        for(String key : keys) {
            delete(key);
        }
    }

    /**
     * Borra una grupo de imagenes en tiempo real de cloudinary
     * @param imgs
     * @throws IOException
     */
    public void deleteRealTimeImageUrl(Iterable<RealTimeEventImage> imgs) throws IOException{
        if(imgs == null) {
            return;
        }
        for(RealTimeEventImage img : imgs) {
            delete(img.getImageUrl());
        }
    }
}
