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
import java.io.InputStream;
import java.util.Date;
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
        metaDataSet.add(new MetaData("Author", "安吉小小"));
        metaDataSet.add(new MetaData("CreateDate", new Date().toString()));
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
            System.out.println("上传结果---" + storePath);
            //拿到元数据
            Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
            System.out.println("元数据---" + metadata);
            // 带分组的路径
            String fullPath = storePath.getFullPath();
            System.out.println("带分组的路径---" + fullPath);
            String path = storePath.getPath();
            System.out.println("路径---" + path);
            // 获取缩略图路径
            String thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getPath());
            System.out.println("缩略图路径---" + thumbImagePath);
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
    public void uploadimgCustom(@RequestParam("file") MultipartFile file) throws Exception {

        FastImageFile fastImageFile = crtFastImageFileOnly(file);

        StorePath storePath = fastFileStorageClient.uploadImage(fastImageFile);
        System.out.println("上传结果---" + storePath);
        //拿到元数据
        Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
        System.out.println("元数据---" + metadata);

        // 带分组的路径
        String fullPath = storePath.getFullPath();
        System.out.println("带分组的路径---" + fullPath);
        // 获取缩略图路径
        //String path = thumbImageConfig.getThumbImagePath(storePath.getPath());

    }

    /**
     * 只上传文件
     *
     * @return
     * @throws Exception
     */
    private FastImageFile crtFastImageFileOnly(MultipartFile file) throws Exception {
        InputStream in = file.getInputStream();
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "Author"));
        metaDataSet.add(new MetaData("CreateDate", "当前时间"));
        String name = file.getOriginalFilename();
        String fileExtName = FilenameUtils.getExtension(name);
        return new FastImageFile.Builder()
                .withFile(in, file.getSize(), fileExtName)
                .withMetaData(metaDataSet)
                .build();
    }
}
