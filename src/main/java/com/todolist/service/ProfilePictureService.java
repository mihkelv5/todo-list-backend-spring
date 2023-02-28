package com.todolist.service;

import com.todolist.entity.user.ProfilePictureData;
import com.todolist.entity.user.UserModel;
import com.todolist.repository.ProfilePictureRepository;
import com.todolist.util.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        String imagePath = IMAGE_LOCATION + "/" + user.getUsername() + "/" + image.getOriginalFilename();

        Files.createDirectories(Paths.get(imagePath));

        ProfilePictureData uploadedData = new ProfilePictureData();
        uploadedData.setName(image.getOriginalFilename());
        uploadedData.setUser(user);
        uploadedData.setImgPath(imagePath);

        image.transferTo(new File(imagePath));

        ProfilePictureData savedData = profilePictureRepository.save(uploadedData);

        this.profilePictureRepository.deleteUsersOldPicture(user, savedData.getId());

        return "File " + savedData.getName() + " uploaded successfully";

    }

    public byte[] downloadImageFromServer(String username) {
        try {
            ProfilePictureData savedData = this.profilePictureRepository.findByUsername(username);
            return  Files.readAllBytes(new File(savedData.getImgPath()).toPath());
        } catch (IOException e){
            return null;
        }
    }
}
