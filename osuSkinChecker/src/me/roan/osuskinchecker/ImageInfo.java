package me.roan.osuskinchecker;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * ImageInfo objects are used to describe an
 * image file and check whether or not a
 * matching image file exists.
 * @author Roan
 * @see Info
 * @see ImageModel
 */
public final class ImageInfo implements Info{
	/**
	 * Whether or not the SD/HD distinction
	 * does not apply to the image described
	 * by this information object
	 */
	private boolean singleImage = false;
	/**
	 * Whether or not the image described by this
	 * information object is a legacy file and not 
	 * used anymore by recent skins.
	 */
	private boolean legacy = false;
	/**
	 * Whether or not multiple versions of the
	 * image described by this information
	 * object can exist, where extra files are named by adding
	 * <code>-n</code> to the name. Where <code>n</code> is an
	 * integer <code>>= 0</code>.
	 */
	protected boolean variableWithDash = false;
	/**
	 * Whether or not multiple versions of the
	 * image described by this information
	 * object can exist, where extra files are named by adding
	 * <code>n</code> to the name. Where <code>n</code> is an
	 * integer <code>>= 0</code>.
	 */
	protected boolean variableWithoutDash = false;
	/**
	 * The skin.ini setting the custom path is tied to
	 */
	protected String customProperty = null;
	/**
	 * The default resource location for the custom path
	 */
	protected String customDefault = null;
	/**
	 * The mania key count this custom path setting is for
	 */
	protected int customKeyCount = -1;
	/**
	 * Boolean to store whether or not a SD image
	 * exists that matches the criteria specified
	 * by this information object. This variable is
	 * <code>null</code> if no checks have been executed yet.
	 */
	private Boolean hasSD = null;
	/**
	 * Boolean to store whether or not a HD image
	 * exists that matches the criteria specified
	 * by this information object. This variable is
	 * <code>null</code> if no checks have been executed yet.
	 */
	private Boolean hasHD = null;
	/**
	 * Boolean to store whether or not a missing
	 * HD image for a file could be ignored because
	 * its SD variant is empty
	 */
	protected boolean ignored = false;
	/**
	 * Boolean to store whether or not the match
	 * found for this image is animated
	 */
	protected boolean animated = false;
	/**
	 * A list of allowed extensions for the
	 * file described by this information object
	 */
	private String[] extensions;
	/**
	 * The base name of the file described
	 * by this information object
	 */
	protected String name;
	/**
	 * Indicates the spinner style, null is this is not
	 * a spinner image, true if this is an image for the
	 * old spinner style, false if this is an image for
	 * the new spinner style.
	 */
	private Boolean spinner = null;
	/**
	 * Boolean to store whether or not the
	 * entire image could be ignored
	 */
	private boolean ignore = false;
	/**
	 * If this image is an animation the number
	 * of frames in the animation
	 */
	protected int frames;
	/**
	 * Full name custom path included for this image
	 */
	private String fullName = null;
	/**
	 * Whether or not this image is empty
	 */
	private boolean empty = true;

	/**
	 * Creates an information object
	 * for a file as specified by its
	 * database entry.
	 * @param line The database entry
	 *        specifying the properties
	 *        for this information object
	 */
	public ImageInfo(String line){
		String[] data = line.split(" +");
		int offset = 0;
		if(!data[0].equals("-")){
			char[] args = data[0].toUpperCase(Locale.ROOT).toCharArray();
			for(char option : args){
				switch(option){
				case 'N':
					variableWithDash = true;
					break;
				case 'M':
					variableWithoutDash = true;
					break;
				case 'S':
					singleImage = true;
					break;
				case 'C':
					customProperty = data[1];
					customDefault = data[2];
					offset += 2;
					break;
				case 'P':
					customKeyCount = Integer.parseInt(data[1]);
					customProperty = data[2];
					customDefault = data[3];
					offset += 3;
					break;
				case 'L':
					legacy = true;
					break;
				case 'O':
					spinner = Boolean.parseBoolean(data[1]);
					offset++;
					break;
				}
			}
		}
		this.extensions = data[1 + offset].split(",");
		this.name = 2 + offset < data.length ? data[2 + offset] : "";
	}

	@Override
	public String toString(){
		setFullName();
		return fullName;
	}

	@Override
	public void reset(){
		hasSD = null;
		hasHD = null;
		ignored = false;
		animated = false;
		ignore = false;
		fullName = null;
		empty = true;
		hasSDVersion();
		hasHDVersion();
	}

	@Override
	public boolean show(){
		if(SkinChecker.showAll){
			return !legacy || SkinChecker.checkLegacy;
		}else{
			if((legacy && !SkinChecker.checkLegacy) || ignore){
				return false;
			}else{
				hasHDVersion();
				hasSDVersion();
				return (SkinChecker.checkSD ? (SkinChecker.ignoreSD ? (hasHD ? false : !hasSD) : !hasSD) : false) || (SkinChecker.checkHD ? ((ignored && SkinChecker.ignoreEmpty) ? false : !hasHD) : false);
			}
		}
	}

	/**
	 * Checks whether or not a SD
	 * version exists of the image
	 * described by this information
	 * object.
	 * @return Whether or not a SD
	 *         version exists.
	 */
	protected String hasSDVersion(){
		if(ignore){
			return "Ignored";
		}else{
			if(hasSD == null){
				for(String ext : extensions){
					setFullName();
					File file;
					if((file = checkForFile(SkinChecker.skinFolder, fullName, false, ext, variableWithDash, variableWithoutDash)) != null){
						SkinChecker.allFiles.remove(file);
						hasSD = true;
						break;
					}
				}
				if(hasSD == null){
					hasSD = false;
				}
			}
			hasHDVersion();
			if(ignore || (hasHD && SkinChecker.ignoreSD && !hasSD)){
				return "Ignored";
			}
			return hasSD ? "Yes" : "No";
		}
	}

	/**
	 * Checks whether or not a HD
	 * version exists of the image
	 * described by this information
	 * object. An image is considered 
	 * a HD image if it's name ends 
	 * in <code>@2x</code>.
	 * @return Whether or not a HD
	 *         version exists.
	 */
	protected String hasHDVersion(){
		if(ignore){
			return "Ignored";
		}else{
			if(singleImage){
				hasHD = true;
				return "N/A";
			}
			if(hasHD == null){
				for(String ext : extensions){
					setFullName();
					File file;
					if((file = checkForFile(SkinChecker.skinFolder, fullName, true, ext, variableWithDash, variableWithoutDash)) != null){
						SkinChecker.allFiles.remove(file);
						hasHD = true;
						break;
					}
				}
				if(hasHD == null){
					hasHD = false;
				}
			}
			return hasHD ? "Yes" : "No";
		}
	}
	
	/**
	 * Sets the full name for this file, this combines the name
	 * of the image file with custom paths in the skin.ini
	 */
	private final void setFullName(){
		if(fullName == null){
			String customPath = SkinChecker.resolveCustomPath(customProperty, customDefault, customKeyCount);
			if(customPath != null){
				fullName = customPath.replace("/", File.separator) + name;
			}else{
				fullName = name;
			}
		}
	}

	/**
	 * Check to see if a specific image exists 
	 * given a specific set of conditions
	 * @param folder The folder the image is located in
	 * @param name The base name of the image
	 * @param hd Whether or not to check for an HD version of
	 *        the image. The name of the HD variant of an image
	 *        end with <code>@2x</code>.
	 * @param ext The extension of the image
	 * @param variableDash Whether or not multiple versions of the
	 *        image can exist, where extra files are named by adding
	 *        <code>-n</code> to the name. Where <code>n</code> is an
	 *        integer <code>>= 0</code>.
	 * @param variableNoDash Whether or not multiple versions of the
	 *        image can exist, where extra files are named by adding
	 *        <code>n</code> to the name. Where <code>n</code> is an
	 *        integer <code>>= 0</code>.
	 * @return A file that matches all the criteria or <code>null</code>
	 *         if none were found.
	 */
	private File checkForFile(File folder, String name, boolean hd, final String ext, boolean variableDash, boolean variableNoDash){
		String extension;
		File match = null;
		if(hd){
			if(hasSD == null || hasSD == true){
				File sdver = checkForFile(folder, name, false, ext, variableDash, variableNoDash);
				if(sdver != null && empty){
					ignored = true;
				}
			}
			extension = "@2x." + ext;
		}else{
			extension = "." + ext;
		}

		if(spinner != null){
			if(!new File(folder, "spinner-background.png").exists()){
				if(spinner){
					ignore = true;
				}
			}else{
				if(!spinner){
					ignore = true;
				}
			}
		}
		if(ignore){
			return null;
		}

		if(variableDash && (match = new File(folder, name + "-0" + extension)).exists()){
			if(!hd){
				empty = empty ? isEmptyImage(match) : false;
			}
			animated = true;
			SkinChecker.allFiles.remove(match);
			int c = 1;
			File fhd;
			File fsd;
			while(true){
				fsd = new File(folder, name + "-" + c + "." + ext);
				fhd = new File(folder, name + "-" + c + "@2x." + ext);
				
				if(!fsd.exists() && !fhd.exists()){
					break;
				}
				
				if(!hd){
					empty = empty ? isEmptyImage(fsd) : false;
				}
				SkinChecker.allFiles.remove(fsd);
				SkinChecker.allFiles.remove(fhd);
				c++;
			}
			frames = c;
		}
		if(variableNoDash && (match = new File(folder, name + "0" + extension)).exists()){
			if(!hd){
				empty = empty ? isEmptyImage(match) : false;
			}
			animated = true;
			SkinChecker.allFiles.remove(match);
			int c = 1;
			File fhd;
			File fsd;
			while(true){
				fsd = new File(folder, name + c + "." + ext);
				fhd = new File(folder, name + c + "@2x." + ext);
				
				if(!fsd.exists() && !fhd.exists()){
					break;
				}
				
				if(!hd){
					empty = empty ? isEmptyImage(fsd) : false;
				}
				SkinChecker.allFiles.remove(fsd);
				SkinChecker.allFiles.remove(fhd);
				c++;
			}
			frames = c;
		}
		File orig = new File(folder, name + extension);
		if(orig.exists()){
			empty = empty ? isEmptyImage(orig) : false;
			SkinChecker.allFiles.remove(orig);
		}
		if(animated && match != null){
			return match;
		}else if(orig.exists()){
			return orig;
		}

		return null;
	}

	/**
	 * Checks if an image is empty.
	 * An Image is considered empty if 
	 * it's dimensions are 1 by 1 or if
	 * it does not exist.
	 * @param img The image file to check
	 * @return Whether or not the image is empty
	 */
	private static boolean isEmptyImage(File img){
		if(img.exists()){
			try{
				Iterator<ImageReader> readers = ImageIO.getImageReadersBySuffix(img.getName().substring(img.getName().lastIndexOf('.') + 1));
				while(readers.hasNext()){
					ImageReader reader = readers.next();
					reader.setInput(ImageIO.createImageInputStream(img));
					return reader.getWidth(0) == 1 && reader.getHeight(0) == 1;
				}
				return false;
			}catch(IOException e){
				return false;
			}
		}else{
			return true;
		}
	}
}