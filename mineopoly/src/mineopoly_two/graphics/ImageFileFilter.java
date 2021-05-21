package mineopoly_two.graphics;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ImageFileFilter implements FilenameFilter {
    private static final Set<String> supportedImageFileTypes = new HashSet<>(Arrays.asList(".jpg", ".jpeg",
                                                                                            ".png", ".bmp", ".gif"));

    @Override
    public boolean accept(File dir, String name) {
        String fileExtension = name.substring(name.indexOf('.'));
        return supportedImageFileTypes.contains(fileExtension);
    }
}
