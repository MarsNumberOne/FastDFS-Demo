package com.example.demofastdfs;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsProject: demo-fastdfs
 * @BelongsPackage: com.example.demofastdfs
 * @Author: Farben
 * @CreateTime: 2019-07-18 13:25
 * @Description: ${Description}
 */
@RestController
public class FastDFSController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * 上传图片同时生成默认大小缩略图
     * @param file
     */
    @PostMapping("img")
    public void uploadImage(@RequestParam("file") MultipartFile file){
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "Author"));
        metaDataSet.add(new MetaData("CreateDate", "当前时间"));
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
            //拿到元数据
            Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
            // 带分组的路径
            String fullPath = storePath.getFullPath();
            // 获取缩略图路径
            String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片同时生成指定大小缩略图
     * 指定缩略图大小
     * @param file
     */
    @PostMapping("imgCustom")
    public void uploadimgCustom(@RequestParam("file") MultipartFile file){
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "Author"));
        metaDataSet.add(new MetaData("CreateDate", "当前时间"));

        FastImageFile build = new FastImageFile.Builder()
//                .withThumbImage(200, 200)
//                .withFile(in, file.length(), fileExtName)
//                .withMetaData(metaDataSet)
                .build();
        StorePath storePath = fastFileStorageClient.uploadImage(build);
        //拿到元数据
        Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
        // 带分组的路径
        String fullPath = storePath.getFullPath();
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
    }
}
