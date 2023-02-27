package com.todolist.service;

import com.todolist.entity.user.ProfilePictureData;
import com.todolist.entity.user.UserModel;
import com.todolist.repository.ProfilePictureRepository;
import com.todolist.util.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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



        ProfilePictureData uploadedData = new ProfilePictureData();
        uploadedData.setName(image.getOriginalFilename());
        uploadedData.setUser(user);
        uploadedData.setImageData(ImageUtil.compressImage(image.getBytes()));

        if(uploadedData.getImageData().length > 75000){ //max file size ~70kb
            return "File size too big";
        }

        ProfilePictureData savedData = profilePictureRepository.save(uploadedData);

        this.profilePictureRepository.deleteUsersOldPicture(user, savedData.getId());

        return "File " + savedData.getName() + " uploaded successfully";

    }

    public byte[] downloadImageFromServer(String username) {
        ProfilePictureData savedData = this.profilePictureRepository.findByUsername(username);
        return ImageUtil.decompressImage(savedData.getImageData());

    }
}
