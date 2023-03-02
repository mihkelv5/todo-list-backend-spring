package com.todolist.service;

import com.todolist.entity.user.ProfilePictureData;
import com.todolist.entity.user.UserModel;
import com.todolist.repository.ProfilePictureRepository;
import jakarta.transaction.Transactional;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static com.todolist.SensitiveData.IMAGE_LOCATION;


@Service
public class ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;
    private final UserService userService;

    public ProfilePictureService(ProfilePictureRepository profilePictureRepository, UserService userService) {
        this.profilePictureRepository = profilePictureRepository;
        this.userService = userService;
    }

    @Transactional
    public String uploadImageToServer(MultipartFile image) throws IOException {
        if(image.getSize() > 10000000){
            throw new RuntimeException("file too big");
        }



        UserModel user = this.userService.getCurrentUser();
        String imagePath = IMAGE_LOCATION + "/" + user.getUsername() + "/profile.jpg";



        Files.deleteIfExists(Paths.get(imagePath)); //deletes old profile picture if exists
        Files.createDirectories(Paths.get(imagePath)); //creates user folder if it does not exist

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

            ProfilePictureData savedData = this.profilePictureRepository.findByUsername(username);
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
