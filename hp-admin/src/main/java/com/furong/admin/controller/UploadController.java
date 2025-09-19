package com.furong.admin.controller;

import com.furong.exception.ErrorCode;
import com.furong.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传Controller
 * @author Origami
 * @date 2025/9/9
 */
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传相关接口")
public class UploadController {

    @Value("${file.upload.path:/tmp/uploads/}")
    private String uploadPath;

    @Value("${file.upload.url:http://localhost:8081/uploads/}")
    private String uploadUrl;

    @PostMapping("/avatar")
    @Operation(summary = "上传头像")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.buildFail(ErrorCode.FILE_NOT_EMPTY.getCode(), ErrorCode.FILE_NOT_EMPTY.getMessage());
        }

        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return Result.buildFail(ErrorCode.FILE_NAME_NOT_EMPTY.getCode(), ErrorCode.FILE_NAME_NOT_EMPTY.getMessage());
            }

            // 获取文件扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 生成新的文件名
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);
            
            // 返回文件访问URL
            String fileUrl = uploadUrl + newFilename;
            return Result.buildSuccess(fileUrl);
            
        } catch (IOException e) {
            e.printStackTrace();
            return Result.buildFail(ErrorCode.FILE_UPLOAD_FAILED.getCode(), ErrorCode.FILE_UPLOAD_FAILED.getMessage() + " "+ e.getMessage());
        }
    }
}
