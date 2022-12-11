package com.datn.coworkingspace.utils;

public class FileUtils {
    public static String generateSpaceUUID() {
        return "space-" + java.util.UUID.randomUUID();
    }

    public static String generateProfileUUID() {
        return "profile-" + java.util.UUID.randomUUID();
    }

    public static String generateCategoryUUID () {
        return "category-" + java.util.UUID.randomUUID();
    }

    public static boolean checkImageFile(String name) {
        String[] splitFileName = name.split("\\.");
        String extension = splitFileName[splitFileName.length-1];
        if (extension.contains("png") ||
                extension.contains("jpg") ||
                extension.contains("jpeg") ) {
            return true;
        }
        return false;
    }
}
