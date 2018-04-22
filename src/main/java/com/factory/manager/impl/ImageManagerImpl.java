package com.factory.manager.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.ImageDao;
import com.factory.dao.impl.NVPair;
import com.factory.domain.Image;
import com.factory.exceptions.InternalServerException;
import com.factory.manager.ImageManager;
import com.factory.utils.Params;

@Component
public class ImageManagerImpl implements ImageManager {

	@Autowired private ImageDao imageDao;
	
	@Override
	public int createImage(String base64, int companyId, String type, Integer mappingId, Integer position,
			Integer ownerId) throws InternalServerException {
		Image image = new Image();
		image.setCompanyId(companyId);
		image.setType(type);
		image.setMappingId(mappingId);
		image.setPosition(position);
		image.setCreatedAt(Instant.now());
		image.setOwnerId(ownerId);
		
		int id = imageDao.persist(image);
		
		if(base64.contains(","))
			base64 = base64.split(",")[1];
		
		byte[] data = Base64.decodeBase64(base64);
		try (OutputStream stream = new FileOutputStream(Params.IMAGE_PATH + Integer.toString(id) + ".jpg")) {
		    stream.write(data);
		} catch (Exception e) {
			throw new InternalServerException(e);
		}
		return id;
	}

	@Override
	public void updateImageNotNull(int imageId, Integer companyId, String type, Integer mappingId,
			Integer position, Integer ownerId) {
		List<NVPair> newValue = new ArrayList<NVPair>();
		
		if (companyId != null)
			newValue.add(new NVPair(ImageDao.Field.COMPANY_ID.name, companyId));
		if (type != null)
			newValue.add(new NVPair(ImageDao.Field.TYPE.name, type));
		if (mappingId != null)
			newValue.add(new NVPair(ImageDao.Field.MAPPING_ID.name, mappingId));
		if (position != null)
			newValue.add(new NVPair(ImageDao.Field.POSITION.name, position));
		if (ownerId != null)
			newValue.add(new NVPair(ImageDao.Field.OWNER_ID.name, ownerId));
		
		imageDao.update(imageId, newValue);
	}

}
