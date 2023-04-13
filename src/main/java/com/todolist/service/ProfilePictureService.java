package com.todolist.service;

import com.todolist.entity.user.ProfilePictureData;
import com.todolist.entity.user.UserModel;
import com.todolist.repository.ProfilePictureRepository;
import jakarta.transaction.Transactional;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static com.todolist.SensitiveData.IMAGE_LOCATION;


@Service
public class ProfilePictureService {

    private ProfilePictureRepository profilePictureRepository;

    @Autowired
    public ProfilePictureService(ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }

    @Transactional
    public String uploadImageToServer(MultipartFile image, UserModel user) throws IOException {
        if(image.getSize() > 10485760){ //10485760
            throw new IOException("File exceeds the size of 10 MB");
        }
        String imagePath = IMAGE_LOCATION + "/" + user.getUsername() + "/profile.jpg";


        Path path = Paths.get(imagePath);
        Files.deleteIfExists(path); //deletes old profile picture if exists
        Files.createDirectories(path); //creates user folder if it does not exist


        ProfilePictureData uploadedData = new ProfilePictureData();
        uploadedData.setName("profile.jpg");
        uploadedData.setUser(user);
        uploadedData.setImgPath(imagePath);




        image.transferTo(new File(imagePath));

        ProfilePictureData savedData = profilePictureRepository.save(uploadedData);

        this.profilePictureRepository.deleteUsersOldPicture(user, savedData.getId());

        return "File " + savedData.getName() + " uploaded successfully";

    }

    public String getUserImage(String username) {
        try {
            String imagePath = IMAGE_LOCATION + "/" + username + "/profile.jpg";
            File imageFile = new File(imagePath);
            if(imageFile.exists()){
                byte[] image = FileUtil.readAsByteArray(imageFile);
                return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(image);
            }
            else {
                byte[] image = FileUtil.readAsByteArray(new File(IMAGE_LOCATION + "/default-user.jpg"));
                return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(image);
            }
        } catch (IOException e) {
            throw new RuntimeException("Image for user: " + username + " not found");
        }
    }
}
