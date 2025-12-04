package org.ikitadevs.timerapp.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.entities.Avatar;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.AvatarMapper;
import org.ikitadevs.timerapp.repositories.AvatarRepository;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvatarService {
    private final FileStorageService fileStorageService;
    private final UserService userService;
    private final AvatarMapper avatarMapper;
    @Transactional
    public void deleteAvatarByUserUuid(UUID uuid) {
        User user = userService.getByUuidWithAvatar(uuid);
        try {
            if(user.getAvatar() != null) {
                fileStorageService.deleteFile(user.getAvatar().getPath());
                user.setAvatar(null);
                userService.saveUser(user);
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
    @Transactional
    public AvatarResponseDto updateAvatarByUserUuid(MultipartFile avatar, UUID uuid) {
        User user = userService.getByUuidWithAvatar(uuid);
        String filepath = fileStorageService.storeFile(avatar, "avatar", "image");
        Avatar newAvatar = new Avatar();
        newAvatar.setPath(filepath);
        newAvatar.setUser(user);
        user.setAvatar(newAvatar);
        userService.saveUser(user);
        return avatarMapper.toAvatarResponseDto(newAvatar);
    }

    @Transactional(readOnly = true)
    public AvatarResponseDto getAvatarByUserUuid(UUID uuid) {
        User user = userService.getByUuidWithAvatar(uuid);
        Avatar avatar = user.getAvatar();
        return avatarMapper.toAvatarResponseDto(avatar);
    }
}
