package com.factory.manager;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.factory.exceptions.InternalServerException;

public interface ImageManager {
	
	/**
	 * Create an image as jpg in local disk and record the image path in database
	 * @param base64 the encoded image file
	 * @param type the type of the image for the project. Defaulted to Others if type is not recognized
	 * @param typeMappingId the mapping id of the image type
	 * @param ownerId the owner of the image
	 * @param title the title of the image, if applicable. Defaulted to empty String if type is not recognized
	 * @return the database id for the saved image
	 * @throws FileNotFoundException if the output file path is not available
	 * @throws IOException if write image file process error
	 */
	public int createImage(String base64, String type, int typeMappingId, int ownerId, String title) 
			throws InternalServerException;//FileNotFoundException, IOException;

}
