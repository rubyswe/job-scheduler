package io.github.rubyswe.job_scheduler.handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ImageSorter {
    // test folder
    private static final String TEST_FOLDER = "/Users/ruby/pic_test";

    private static final int MIN_LONG_SIDE = 3840;
    private static final int MIN_SHORT_SIDE = 2160;
    private static final String MY_PICTURES_PATH = "/Users/ruby/my_pictures"; // path for folder

    public int sortImages(String folderPath) throws IOException {

        Path folder = Paths.get(folderPath); // get folder to work on
        Path targetDir = folder.resolve("4k"); // create a sub folder within

        Path path = folder;
        Stream<Path> paths = Files.walk(path, 1); // Traverses through files on 1 level and prints the files
        Files.createDirectories(targetDir);

        // puts all 4k images into a list
        List<Path> fourKImages = paths
                    .filter(Files::isRegularFile) // check to see if file is a regular file
                    // gets file's name, lowercase it and keep it only it ends in .jpg or.jpeg
                    .filter(file -> {String name = file.getFileName().toString().toLowerCase();
                        return name.endsWith(".jpg") || name.endsWith(".jpeg");})
                    .filter(this::isFourK)
                    .toList();
        int move = 0;
        for(Path img : fourKImages){
            if (moveToFolder(img, targetDir)){
                move++;
            }
        }
        return move;
    }

    public boolean isFourK(Path image) {

        int width;
        int height;

        int longSide;
        int shortSide;

        try{
            BufferedImage img = ImageIO.read(image.toFile());
            if (img != null){
                width = img.getWidth();
                height = img.getHeight();
            }
            else {
                System.out.println("Error reading file: " + image.getFileName());
                return false;
            }
            longSide = Math.max(width, height);
            shortSide = Math.min(width, height);

        } catch (IOException e) {
            System.out.println("Skipped image: " + image.getFileName());
            return false;
        }
        return longSide >= MIN_LONG_SIDE && shortSide >= MIN_SHORT_SIDE;
    }

    public boolean moveToFolder(Path image, Path tarDir){

        Path destination = tarDir.resolve(image.getFileName());
        try {
            Files.move(image, destination);
            return true;
        } catch (FileAlreadyExistsException e){
            System.out.println("File already exists: " + image.getFileName());
            return false;
        }
        catch (IOException e) {
            System.out.println("Error moving file: " + image.getFileName());
            return false;
        }
    }
}
